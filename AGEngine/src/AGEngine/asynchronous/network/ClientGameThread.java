package AGEngine.asynchronous.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/* Inspiration and explicit code from following sources:
 * https://www.geeksforgeeks.org/introducing-threads-socket-programming-java/
 * http://pirate.shu.edu/~wachsmut/Teaching/CSAS2214/Virtual/Lectures/chat-client-server.html
 *  */
public class ClientGameThread implements Runnable{

	private Socket _socket = null;
	private ClientGame _client = null;
	private ObjectInputStream _streamIn = null;
	private boolean threadController = false;

	public synchronized boolean isThreadController() {
		return threadController;
	}

	public synchronized void setThreadController(boolean threadController) {
		this.threadController = threadController;
	}

	public ClientGameThread(ClientGame client, Socket socket)
	{  
		_client   = client;
	    _socket   = socket;
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
	        _client.stop();
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
				_client.handle(rObject);
				Thread.sleep(2000);
	        }
	        catch(IOException ioe)
	        {  
	        	System.out.println("Listening error: " + ioe.getMessage());
	        	_client.stop();
	        } 
			catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				_client.stop();
			} 
			catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				_client.stop();
			}
	    }
	}

}
