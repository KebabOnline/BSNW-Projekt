package tcp;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ConnectionFactoryTests
{
    @Test
    public void testConnectionFactory() throws IOException {
        ConnectionFactory connectionFactory = new ConnectionFactory(7777);
        System.out.println("test: going to accept new connections");
        connectionFactory.acceptNewConnections();
        // stopped hier. Läuft weiter, wenn man z.B. TCPTests.openSocketWriteAndRead() gleichzeitig ausführt
    }

    @Test
    public void testConnectionFactoryClient() throws IOException {
        Socket socket = new Socket("localhost", 7777);

        //Byte vom Server lesen
        InputStream inStream = socket.getInputStream();
        int receivedByte = inStream.read();
        System.out.println("recieved "+ receivedByte);

        //Byte zum Server senden
        OutputStream outStream = socket.getOutputStream();
        outStream.write(receivedByte);
        System.out.println("sent "+ receivedByte);

    }
}
