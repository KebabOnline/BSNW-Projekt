package tcp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
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

    @Test
    public void runReverseEchoServer() throws IOException {
        ConnectionFactory connectionFactory = new ConnectionFactory(new ReverseEchoServer());
        System.out.println("test: going to accept new connections");
        connectionFactory.acceptNewConnections();
    }

    @Test
    public void runReverseEchoClient() throws IOException {
        //Verbinden
        Socket socket = new Socket("localhost", 7777);
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

        long longValue = 123456L;
        double doubleValue = 123.456;
        String stringValue = "aaaAAAAaaAaAaAa";

        //send values
        dataOutputStream.writeLong(longValue);
        System.out.println("sent "+ longValue);
        dataOutputStream.writeDouble(doubleValue);
        System.out.println("sent "+ doubleValue);
        dataOutputStream.writeUTF(stringValue);
        System.out.println("sent "+ stringValue);

        //receive values
        String receivedString = dataInputStream.readUTF();
        System.out.println("read: "+ receivedString);
        double receivedDouble = dataInputStream.readDouble();
        System.out.println("read: "+ receivedDouble);
        long receivedLong = dataInputStream.readLong();
        System.out.println("read: "+ receivedLong);

        Assertions.assertEquals(longValue, receivedLong);
        Assertions.assertEquals(doubleValue, receivedDouble);
        Assertions.assertEquals(stringValue, receivedString);
    }

}
