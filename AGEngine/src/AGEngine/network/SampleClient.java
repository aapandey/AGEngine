package AGEngine.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/* Inspiration and explicit code from following sources:
 * Sample code base shared in CSC 591 - GEF
 * https://www.geeksforgeeks.org/socket-programming-in-java/
 *  */

public class SampleClient {

	// Input and output stream for integer data
	private static DataInputStream input_stream;
    private static DataOutputStream output_stream;

    public static void main(String[] args) throws IOException, InterruptedException
    {
        Socket s = new Socket("127.0.0.1", 5200);
        output_stream = new DataOutputStream(s.getOutputStream());
        input_stream = new DataInputStream(s.getInputStream());

        // Id to distinguish between clients passed as command line argument
        int id = Integer.parseInt(args[0]);

        int iter = 0;
        while(true)
        {
        	try {
                output_stream.writeInt(id);
                output_stream.writeInt(iter);
                ++iter;

                System.out.println("Client received: " + input_stream.readInt() + " " + input_stream.readInt());
                Thread.sleep(2000);
        	}
        	catch(Exception e) {
        		closeServerConnection(s, output_stream, input_stream);
        		System.out.println("Exiting due to connection loss");
        		e.printStackTrace();
        		break;
        	}
            
        }
    }

    /** Close server connection, normally called during exceptions */
	private static void closeServerConnection(Socket s, DataOutputStream output_stream2,
			DataInputStream input_stream2) {
		try {
			input_stream2.close();
			output_stream2.close();
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
