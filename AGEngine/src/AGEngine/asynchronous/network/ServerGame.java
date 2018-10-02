package AGEngine.asynchronous.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import processing.core.PApplet;

public class ServerGame extends PApplet implements Runnable{
	
	private static CopyOnWriteArrayList<ServerGameInputThread> _client_input;
	private static CopyOnWriteArrayList<ServerGameOutputThread> _client_output;
	
	private static ConcurrentHashMap<Integer, RectangleObject> _rectangles;
	private ServerSocket _server = null;
	private Thread _thread = null;
	private int _client_count;
	
	// Flag to control the Write thread behaviour
	private boolean threadFlag = false;
	// This flag when set to true, signifies a write to be made to the server
	private boolean writeDone = false;
	
	public synchronized boolean isWriteDone() {
		return writeDone;
	}

	public synchronized void setWriteDone(boolean writeDone) {
		this.writeDone = writeDone;
	}
	
	public synchronized boolean isThreadFlag() {
		return threadFlag;
	}

	public synchronized void setThreadFlag(boolean threadFlag) {
		this.threadFlag = threadFlag;
	}
	
	public synchronized int get_client_count() {
		return _client_count;
	}

	public synchronized void set_client_count(int _client_count) {
		this._client_count = _client_count;
	}
	
	public synchronized ConcurrentHashMap<Integer, RectangleObject> get_rectangles() {
		return _rectangles;
	}

	public synchronized void set_rectangles(ConcurrentHashMap<Integer, RectangleObject> rectangles) {
		_rectangles = rectangles;
	}
	
	public ServerGame(int port) {
		try
	    {  
			_client_count = 0;
			System.out.println("Binding to port " + port + ", please wait  ...");
	        _server = new ServerSocket(port);
	        setThreadFlag(true);
	        
	        System.out.println("Server started: " + _server);
	        start(); 
	    }
	    catch(IOException ioe)
	    {  
	    	System.out.println("Can not bind to port " + port + ": " + ioe.getMessage()); 
	    }
	}
	
	public void start()
	{  
		if (_thread == null)
	    {  
			_thread = new Thread(this); 
	        _thread.start();
	    }
	}
	
	public void stop()
	{  
		if (isThreadFlag())
	    {  
			setThreadFlag(false);
	    }
		// Close all the client threads
		for(int i = 0; i < _client_input.size(); i++) {
			_client_input.get(i).close();
		}
		for(int i = 0; i < _client_output.size(); i++) {
			_client_output.get(i).close();
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (isThreadFlag())
		{  
			try
			{  
				System.out.println("Waiting for a client ...");
				Socket s = _server.accept();
				addThread(s);
				// Create object unique to new client
				createClientRectangle(s.getPort());
				// Send the new object to all clients
				for (int i = 0; i < get_client_count(); i++)
		        {
					if(_client_output.get(i).getID() != s.getPort())
						_client_output.get(i).send(get_rectangles().get(s.getPort()));
					else
						_client_output.get(i).pushAll(get_rectangles());
		        }
				Thread.sleep(2000);
			}
			catch(IOException ioe)
			{  
				System.out.println("Server accept error: " + ioe); 
				stop(); 
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				stop();
			}
		}
	}

	
	private int findClient(int ID)
	{  
		for (int i = 0; i < get_client_count(); i++) {
			if (_client_input.get(i).getID() == ID)
				return i;
		}
		return -1;
	}
	
	public synchronized void handle(RectangleObject obj)
	{
		// Code to handle client signaling quit
		// Update all the other clients
		for (int i = 0; i < get_client_count(); i++)
		{
			// Only update clients whose id does not match the object id so as not to send duplicates
			if(_client_output.get(i).getID() != obj.getId())
				_client_output.get(i).send(obj);  
		}
	}
	
	// Remove a client from the list if an error occurs and close its threads
	public synchronized void remove(int ID)
	{  
		int pos = findClient(ID);
	    if (pos >= 0)
	    {  
	    	ServerGameInputThread toTerminateip = _client_input.get(pos);
	    	ServerGameOutputThread toTerminateop = _client_output.get(pos);
	        System.out.println("Removing client thread " + ID + " at " + pos);
	        toTerminateip.close();
			toTerminateop.close(); 
			_client_input.remove(pos);
			_client_output.remove(pos);
	        _client_count--;
	        // Update other clients about the change
	        _rectangles.get(ID).setAlive(false);
	        handle(get_rectangles().get(ID));
	        // Remove from server
	        _rectangles.remove(ID);
	     }
	 }
	
	private synchronized void addThread(Socket socket)
	{ 
		System.out.println("Client accepted: " + socket);
		_client_input.add(new ServerGameInputThread(this, socket));
		_client_output.add(new ServerGameOutputThread(this, socket));
		_client_count++;
	}
	
	public synchronized void createClientRectangle(int id) {
		RectangleObject rectangle = new RectangleObject(id, random(0, 500),
				random(0, 500), random(30, 50), random(30, 50), true);
		rectangle.setColor(random(0, 255), random(0, 255), random(0, 255));
		_rectangles.put(id, rectangle);
	}

	public static void main(String[] args) {
		ServerGame server = new ServerGame(5500);
		
		_client_input = new CopyOnWriteArrayList<ServerGameInputThread>();
        _client_output = new CopyOnWriteArrayList<ServerGameOutputThread>();
        _rectangles = new ConcurrentHashMap<Integer, RectangleObject>();
        
		if(server.isThreadFlag()) {
			PApplet.main("AGEngine.asynchronous.network.ServerGame");
		}
		else {
			System.out.println("Unable to start the server, exiting");
		}
	}
	
	public void settings(){
		size(500, 500);
    }

    public void setup(){
    	stroke(0);
    	fill(255, 0, 0);
    	frameRate(60);
    }

    public void draw(){
    	background(255);
    	
    	for(RectangleObject rectangle : get_rectangles().values()) {
    		fill(rectangle.getR(), rectangle.getG(), rectangle.getB());
    		rect(rectangle.getX(), rectangle.getY(), 
    				rectangle.getWidth(), rectangle.getHeight());
    	}
    	
    }

}
