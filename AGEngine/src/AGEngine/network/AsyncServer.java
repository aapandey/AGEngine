package AGEngine.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/* Inspiration and explicit code from following sources:
 * https://www.geeksforgeeks.org/introducing-threads-socket-programming-java/
 * http://pirate.shu.edu/~wachsmut/Teaching/CSAS2214/Virtual/Lectures/chat-client-server.html
 * https://stackoverflow.com/questions/27736175/how-to-send-receive-objects-using-sockets-in-java
 * https://www.developer.com/design/article.php/10925_3604491_3/Objects-and-ClientServer-Connections.htm
 * https://www.geeksforgeeks.org/difference-hashmap-concurrenthashmap/
 * https://www.codejava.net/java-core/the-java-language/java-8-lambda-runnable-example
 * https://github.com/debalin/multiplayer-game-engine/tree/3811cc19a40ac275e8370546fb6111f634fd91c8
 * https://alvinalexander.com/java/java-8-lambda-thread-runnable-syntax-examples
 *  */

public class AsyncServer implements Runnable 
{
	// Server socket to accept connections
	protected ServerSocket sSocket;
	// Port number to accept connections
	protected int portNumber;
	// Socket connections to establish network with clients and maintain them
	protected ArrayList<Socket> connections;
	// This will work as unique client identifier 
	protected int totalClientCount;

	public AsyncServer(int serverPort) 
	{
		this.portNumber = serverPort;
		this.connections = new ArrayList<Socket>();
		this.totalClientCount = 0;

		try {
			System.out.println("Binding to port " + serverPort + ", please wait  ...");
			sSocket = new ServerSocket(this.portNumber);
			System.out.println("Server started: " + sSocket);
		}
		catch (IOException ioe) {
			System.out.println("Can not bind to port " + serverPort + ": " + ioe.getMessage()); 
			ioe.printStackTrace();
		}
	}

	protected void readFromClient(Socket connection, int connectionID) 
	{
		ObjectInputStream ois = null;
		try 
		{
			ois = new ObjectInputStream(connection.getInputStream());
		} catch (IOException ioe) 
		{
			System.out.println("Error getting input stream: " + ioe);
			ioe.printStackTrace();
		}
		
		// List of game objects to accept from the client

		while (true) 
		{
			while (true) 
			{
				try 
				{
					// implementation to accept objects from clients
				}
				catch (Exception e) 
				{
					System.out.println("Connection lost with client " + connection + ", closing the connection");
					return;
				}
			}
			// implementation to handle server getting the list from client
		}
	}

	protected void writeToClient(Socket connection, int connectionID) 
	{
		ObjectOutputStream ous = null;
		try 
		{
			ous = new ObjectOutputStream(connection.getOutputStream());
		} catch (IOException ioe) 
		{
			System.out.println("Error sending output stream: " + ioe);
			ioe.printStackTrace();
		}

		// List of game objects to pass to the client

		while (true) 
		{
			//synchronized over a game object list 
			{
				// Implementation to pass objects to client
			}
		}
	}

	protected Socket acceptClientConnection() 
	{
		Socket socket = null;
		try 
		{
			socket = sSocket.accept();
			System.out.println("Client accepted: " + socket);
			totalClientCount++;
		} 
		catch (IOException ioe) 
		{
			System.out.println("Server accept error: " + ioe);
			ioe.printStackTrace();
		}
		return socket;
	}

	@Override
	public void run() 
	{
		// TODO Auto-generated method stub
		while (true) {
			System.out.println("Waiting for a client ...");
			Socket connection = acceptClientConnection();
			if (connection != null) {
				connections.add(connection);
				Runnable read = () -> readFromClient(connection, totalClientCount - 1);
				Runnable write = () -> writeToClient(connection, totalClientCount - 1);
				new Thread(read).start();
				new Thread(write).start();
			}
		}
	}

}
