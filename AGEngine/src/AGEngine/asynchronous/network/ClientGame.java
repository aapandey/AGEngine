package AGEngine.asynchronous.network;

import processing.core.PApplet;
import processing.core.PVector;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/* Inspiration and explicit code from following sources:
 * https://www.geeksforgeeks.org/introducing-threads-socket-programming-java/
 * http://pirate.shu.edu/~wachsmut/Teaching/CSAS2214/Virtual/Lectures/chat-client-server.html
 * https://stackoverflow.com/questions/27736175/how-to-send-receive-objects-using-sockets-in-java
 * https://www.developer.com/design/article.php/10925_3604491_3/Objects-and-ClientServer-Connections.htm
 * https://www.geeksforgeeks.org/difference-hashmap-concurrenthashmap/
 *  */

public class ClientGame extends PApplet implements Runnable{

	private Socket socket = null;
	private Thread outputThread = null;
	//private DataInputStream  console   = null;
	private ObjectOutputStream streamOut = null;
	private ClientGameThread client = null;
	
	// Flag to control the Write thread behaviour
	private boolean threadFlag = false;
	// This flag when set to true, signifies a write to be made to the server
	private boolean writeDone = false;
	
	private static int uniqueID;
	
	// Holder for player rectangles 
	private static ConcurrentHashMap<Integer, RectangleObject> rectangles;
	
	public synchronized int getUniqueID() {
		return uniqueID;
	}

	public synchronized void setUniqueID(int _uniqueID) {
		uniqueID = _uniqueID;
	}

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

	public ClientGame(String serverName, int serverPort)
	{  
		System.out.println("Establishing connection. Please wait ...");
		try
		{  
			socket = new Socket(serverName, serverPort);
			setUniqueID(socket.getLocalPort());
			System.out.println("Connected: " + socket);
			setThreadFlag(true);
			setWriteDone(false);
			start();
		}
		catch(UnknownHostException uhe)
		{  
			System.out.println("Host unknown: " + uhe.getMessage()); 
		}
		catch(IOException ioe)
		{  
				System.out.println("Unexpected exception: " + ioe.getMessage()); 
		}
	}
	
	public void run()
	{  
		while (isThreadFlag())
		{  
			try
			{  
				//streamOut.writeUTF(console.readLine());
				//streamOut.flush();
				if(isWriteDone()) {
					// do something
					streamOut.reset();
					streamOut.writeObject(rectangles.get(getUniqueID()));
					setWriteDone(false);
				}
				Thread.sleep(2000);
			}
			catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void handle(RectangleObject rObject)
	{  
		/*if (msg.equals(".bye"))
		{  
			System.out.println("Good bye. Press RETURN to exit ...");
			stop();
		}
		else
			System.out.println(msg);*/
		if(rObject.isAlive()) {
			addObject(rObject.getId(), rObject);
		}
		else {
			removeObject(rObject.getId());
		}
	}
	
	public void start()
	{  
		//console   = new DataInputStream(System.in);
		try {
			streamOut = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (outputThread == null)
		{  
			client = new ClientGameThread(this, socket);
			outputThread = new Thread(this);                   
			outputThread.start();
		}
	}
	
	public void stop()
	{  
		if (isThreadFlag())
		{  
			//outputThread.join();  
			//outputThread = null;
			setThreadFlag(false);
		}
		try
		{  
			//if (console   != null)  console.close();
			if (streamOut != null)  streamOut.close();
			if (socket    != null)  socket.close();
		}
		catch(IOException ioe)
		{  
			System.out.println("Error closing ..."); 
		}
		client.close();  
		//client.stop();
	}

	public static void main(String[] args) {
		ClientGame client = new ClientGame("127.0.0.1", 5500);
		rectangles = new ConcurrentHashMap<Integer, RectangleObject>();
		if(client.isThreadFlag()) {
			PApplet.main("AGEngine.asynchronous.network.ClientGame");
		}
		else {
			System.out.println("Unable to connect to the server, exiting");
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
    	
    	for(RectangleObject rectangle : rectangles.values()) {
    		fill(rectangle.getR(), rectangle.getG(), rectangle.getB());
    		rect(rectangle.getX(), rectangle.getY(), 
    				rectangle.getWidth(), rectangle.getHeight());
    	}
    	
    }
    
    public synchronized void removeObject(int id) {
    	if(rectangles != null && rectangles.get(id) != null) 
    	{
        	rectangles.remove(id);	
    	}
    }
    
    public synchronized void addObject(int id, RectangleObject obj) 
    {
    	if(rectangles.get(id) != null) {
    		rectangles.put(id, obj); 
    	}
    }
    
    public void keyPressed() {
    	float x = rectangles.get(getUniqueID()).getX();
		float y = rectangles.get(getUniqueID()).getY();
    	if (key == CODED) {
    	    if (keyCode == UP) {
    	    	y -= 1;
    	    	rectangles.get(getUniqueID()).setY(y);
    	    	setWriteDone(true);
    	    }
    	    if (keyCode == DOWN) {
    	    	y += 1;
    	    	rectangles.get(getUniqueID()).setY(y);
    	    	setWriteDone(true);
    	    }
    	    if (keyCode == LEFT) {
    	    	x -= 1;
    	    	rectangles.get(getUniqueID()).setX(x);
    	    	setWriteDone(true);
    	    } 
    	    if (keyCode == RIGHT) {
    	    	x += 1;
    	    	rectangles.get(getUniqueID()).setX(x);
    	    	setWriteDone(true);
    	    } 
    	}
    	if(key == ' ') {
    		y -= 1;
	    	rectangles.get(getUniqueID()).setY(y);
	    	setWriteDone(true);
    	}
    }
    
}
