package AGEngine.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

/* Inspiration and explicit code from following sources:
 * Sample code base shared in CSC 591 - GEF
 * http://pirate.shu.edu/~wachsmut/Teaching/CSAS2214/Virtual/Lectures/chat-client-server.html
 *  */

public class SampleServer implements Runnable {

	// Member list to store input and output streams from clients
	private static CopyOnWriteArrayList<DataInputStream> input_streams;
	private static CopyOnWriteArrayList<DataOutputStream> output_streams;
	private static ServerSocket ss;
	
	@Override
	public void run() {
		while(true) {
			Socket s = null;
			try
			{  
				s = ss.accept();
				System.out.println("A new client is connected : " + s);
				output_streams.add(new DataOutputStream(s.getOutputStream()));
				input_streams.add(new DataInputStream(s.getInputStream()));
			}
			catch(Exception e)
			{  
				e.printStackTrace();
			}
		}
	}
	
	/** Remove client connection due to client disconnecting or some interruption */
	private static void removeClient(int index) {
		if(input_streams != null && input_streams.get(index) != null) {
			input_streams.remove(index);
		}
		if(output_streams != null && output_streams.get(index) != null) {
			output_streams.remove(index);
		}
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		ss = new ServerSocket(5200);
		input_streams = new CopyOnWriteArrayList<DataInputStream>();
		output_streams = new CopyOnWriteArrayList<DataOutputStream>();
		
		SampleServer server = new SampleServer();
		(new Thread(server)).start();
		
		int iter = 0;
		while(true) {
			synchronized (server) {
				int i = 0;
				for(DataInputStream din : input_streams) {
					try {
						i++;
						System.out.println("Server received: " + din.readInt() + " " + din.readInt());
					}
					catch (Exception e) {
						removeClient(--i);
						e.printStackTrace();
					}
				}
			}
			System.out.println("Server completed reading all streams now writing");
			synchronized (server) {
				int i = 0;
				try {
					for(DataOutputStream dout: output_streams) {
						i++;
						dout.writeInt(0);
						dout.writeInt(iter);
					}
				}
				catch (Exception e) {
					removeClient(--i);
					e.printStackTrace();
				}
				Thread.sleep(2000);
			}
			++iter;
		}

	}

}
