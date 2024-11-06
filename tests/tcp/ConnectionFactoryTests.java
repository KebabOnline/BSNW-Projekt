package tcp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ConnectionFactoryTests
{
    @Test
    public void runEchoServer() throws IOException {
        ConnectionFactory connectionFactory = new ConnectionFactory(new EchoServer());
        System.out.println("test: going to accept new connections");
        connectionFactory.acceptNewConnections();
        // stopped hier. Läuft weiter, wenn man z.B. TCPTests.openSocketWriteAndRead() gleichzeitig ausführt
    }

    @Test
    public void testEchoServer() throws IOException {
        //Verbinden
        Socket socket = new Socket("localhost", 7777);
        byte byte2Sent = 42;

        //Byte zum Server senden
        socket.getOutputStream().write(byte2Sent);
        System.out.println("sent "+ byte2Sent);

        //Byte vom Server lesen
        int readValue = socket.getInputStream().read();
        System.out.println("read: "+ readValue);
        //Zu Byte Konvertieren
        byte readByte = (byte) readValue;

        Assertions.assertEquals(byte2Sent, readByte);

    }

    @Test
    public void runIncrementServer() throws IOException {
        ConnectionFactory connectionFactory = new ConnectionFactory(new IncrementServer());
        System.out.println("test: going to accept new connections");
        connectionFactory.acceptNewConnections();
    }

    @Test
    public void runIncrementClient() throws IOException {
        //Verbinden
        Socket socket = new Socket("localhost", 7777);
        //Ersten Byte senden
        byte byte2Sent = 42;
        socket.getOutputStream().write(byte2Sent);
        int readValue = 0;

        for(int i = 0; i <= 9; i++) {
            //Byte vom Server lesen
            readValue = socket.getInputStream().read();
            System.out.println("read: "+ readValue);

            //Byte inkrementieren und zum Server senden
            int incrementedValue = readValue + 1;
            socket.getOutputStream().write(incrementedValue);
            System.out.println("sent "+ incrementedValue);
        }

        Assertions.assertEquals(byte2Sent+19, readValue); //Expected 61 because 62 is only sent not read

    }
}
