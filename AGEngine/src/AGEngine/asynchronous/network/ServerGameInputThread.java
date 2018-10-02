package AGEngine.asynchronous.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ServerGameInputThread implements Runnable{
	private ServerGame _server = null;
	private Socket _socket = null;
	private int ID = -1;
	
	private ObjectInputStream _streamIn = null;
	private boolean threadController = false;

	public synchronized boolean isThreadController() {
		return threadController;
	}

	public synchronized int getID() {
		return ID;
	}

	public synchronized void setThreadController(boolean threadController) {
		this.threadController = threadController;
	}

	public ServerGameInputThread(ServerGame server, Socket socket)
	{  
		super();
		_server   = server;
	    _socket   = socket;
	    ID = _socket.getPort();
	    open();  
	    (new Thread()).start();
	}
	
	public void open()
	{ 
		try
	    {  
			_streamIn  = new ObjectInputStream(_socket.getInputStream());
			setThreadController(true);
	    }
	    catch(IOException ioe)
	    {  
	    	System.out.println("Error getting input stream: " + ioe);
	        _server.remove(getID());
	        //_server.stop();
	    }
	}
	
	public void close()
	{  
		try
	    {  
			setThreadController(false);
			if (_streamIn != null) _streamIn.close();
	    }
	    catch(IOException ioe)
	    {  
	    	System.out.println("Error closing input stream: " + ioe);
	    }
	}
	
	public void run()
	{  
		while (isThreadController())
	    {  
			try
	        {  
				RectangleObject rObject = (RectangleObject) _streamIn.readObject();
				_server.handle(rObject);
				Thread.sleep(200);
	        }
	        catch(IOException ioe)
	        {  
	        	System.out.println("Listening error: " + ioe.getMessage());
	        	_server.remove(getID());
	        } catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				_server.remove(getID());
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				_server.remove(getID());
			}
	    }
	}	
}
