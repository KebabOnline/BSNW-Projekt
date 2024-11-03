package socket;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SocketTest {

    @Test
    void testSocketConnection() {
        try (Socket socket = new Socket("localhost", 7777)) {

            //Byte zum Server senden
            OutputStream outStream = socket.getOutputStream();
            outStream.write(1);

            //Byte vom Server lesen
            InputStream inStream = socket.getInputStream();
            int receivedByte = inStream.read();

            assertEquals(2, receivedByte);

        } catch (IOException e) {
            e.printStackTrace();
            assertEquals("Keine Ausnahme erwartet", e.getMessage());
        }
    }
    //Basically was das TestPeer gemacht hat
    @Test
    void testServerSocketIncrementResponse() {
        try (ServerSocket srvSocket = new ServerSocket(7777);
             Socket clientSocket = srvSocket.accept();) {

            InputStream inputStream = clientSocket.getInputStream();
            OutputStream outputStream = clientSocket.getOutputStream();

            //Byte lesen, inkrementieren und zurücksenden
            int receivedByte = inputStream.read();
            int incrementedByte = receivedByte + 1;
            outputStream.write(incrementedByte);

            //Verbindung Schließen
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
