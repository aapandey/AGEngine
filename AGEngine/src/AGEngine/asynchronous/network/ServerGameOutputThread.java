package AGEngine.asynchronous.network;

import java.awt.geom.RectangularShape;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class ServerGameOutputThread implements Runnable{
	private ServerGame _server = null;
	private Socket _socket = null;
	private int ID = -1;
	
	private ObjectOutputStream _streamOut = null;
	private RectangleObject rObject;
	private boolean threadController = false;
	private boolean writeDone = false;

	public synchronized boolean isWriteDone() {
		return writeDone;
	}

	public synchronized void setWriteDone(boolean writeDone) {
		this.writeDone = writeDone;
	}

	public synchronized boolean isThreadController() {
		return threadController;
	}

	public synchronized int getID() {
		return ID;
	}

	public synchronized void setThreadController(boolean threadController) {
		this.threadController = threadController;
	}

	public ServerGameOutputThread(ServerGame server, Socket socket)
	{  
		super();
		_server   = server;
	    _socket   = socket;
	    ID = _socket.getPort();
	    setWriteDone(false);
	    open();  
	    (new Thread()).start();
	}
	
	public void open()
	{ 
		try
	    {  
			_streamOut  = new ObjectOutputStream(_socket.getOutputStream());
			setThreadController(true);
	    }
	    catch(IOException ioe)
	    {  
	    	System.out.println("Error getting input stream: " + ioe);
	        _server.remove(getID());
	        // Might not need this yet
	        //_server.stop();
	    }
	}
	
	public void close()
	{  
		try
	    {  
			setThreadController(false);
			if (_streamOut != null) _streamOut.close();
	    }
	    catch(IOException ioe)
	    {  
	    	System.out.println("Error closing input stream: " + ioe);
	    }
	}
	
	public synchronized void send(RectangleObject rObj) {
		rObject = null;
		rObject = new RectangleObject(rObj);
		setWriteDone(true);
	}
	
	public synchronized void pushAll(ConcurrentHashMap<Integer, RectangleObject> rectangles)
	{
		for(RectangleObject r : rectangles.values()) {
			send(r);
		}
	}
	
	public void run()
	{  
		while (isThreadController())
	    {  
			try
	        {  
				if(isWriteDone()) {
					//Do something
					try {
						_streamOut.reset();
						_streamOut.writeObject(rObject);
						_streamOut.flush();
						
					} catch (IOException e) {
						System.out.println(ID + " ERROR sending: " + e.getMessage());
				        _server.remove(getID());
				        setThreadController(false);
					}
					setWriteDone(false);
				}
				Thread.sleep(200);
	        }
	        catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				_server.remove(getID());
			}
	    }
	}	
}
