package AGEngine.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import AGEngine.core.Manager;
import AGEngine.objects.*;

/* Inspiration and explicit code from following sources:
 * https://www.geeksforgeeks.org/introducing-threads-socket-programming-java/
 * http://pirate.shu.edu/~wachsmut/Teaching/CSAS2214/Virtual/Lectures/chat-client-server.html
 * https://stackoverflow.com/questions/27736175/how-to-send-receive-objects-using-sockets-in-java
 * https://www.developer.com/design/article.php/10925_3604491_3/Objects-and-ClientServer-Connections.htm
 * https://www.geeksforgeeks.org/difference-hashmap-concurrenthashmap/
 * https://www.codejava.net/java-core/the-java-language/java-8-lambda-runnable-example
 *  */

public class AsyncClient implements Runnable{

	// Socket to connect with server
	protected Socket cSocket;
	// Port Number to connect to
	protected int portNumber;
	// Server Address to connect to
	protected String address;
	// manger to call handling operations
	protected Manager manager;

	public AsyncClient(String address, int serverPortNumber, Manager manager) 
	{
		this.portNumber = serverPortNumber;
		this.address = address;
		this.manager = manager;
	}

	protected void readFromServer() {
		ObjectInputStream ois = null;
		try 
		{
			ois = new ObjectInputStream(cSocket.getInputStream());
		} catch (IOException ioe) 
		{
			System.out.println("Error getting input stream: " + ioe);
			ioe.printStackTrace();
		}
		
		// List of game objects to accept from server
		List<GameObject> list = new ArrayList<>();
		
		while (true) {
			while (true) {
				try {
					// implementation to accept from server
					GameObject gameObject;
					gameObject = (GameObject)ois.readObject();
					if(gameObject != null) {
						list.add(gameObject);
					}
					else {
						break;
					}
				}
				catch (Exception e) 
				{
					e.printStackTrace();
					System.out.println("Connection lost with server, closing read thread");
					return;
				}
			}
			// implementation to handle client getting game objects from server
			if(list != null && list.size() > 0) {
				manager.getDataFromServer(list);
			}
			list = null;
		}
	}

	protected void writeToServer() 
	{
		ObjectOutputStream ous = null;
		try 
		{
			ous = new ObjectOutputStream(cSocket.getOutputStream());
		} catch (IOException ioe) 
		{
			System.out.println("Error sending output stream: " + ioe);
			ioe.printStackTrace();
		}
		while (true) 
		{
			try {
				// Implementation to send data to server
				GameObject data = manager.sendDataToServer();
				if(data != null) {
					ous.writeObject(data);
					ous.reset();
				}
			} catch (IOException ioe) 
			{
				System.out.println("Error sending output stream: " + ioe);
				ioe.printStackTrace();
			}
		}
	}

	protected void connectToServer() {
		try 
		{
			cSocket = new Socket(address, portNumber);
			System.out.println("Connected: " + cSocket);
		}
		catch (IOException ioe) {
			System.out.println("Unexpected exception: " + ioe.getMessage());
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Establishing connection. Please wait ...");
		connectToServer();
		Runnable read = () -> readFromServer();
		Runnable write = () -> writeToServer();
		new Thread(read).start();
		new Thread(write).start();
		
	}

}
