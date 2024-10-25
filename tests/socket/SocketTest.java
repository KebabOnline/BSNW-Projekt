package socket;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

            // Test: Sicherstellen, dass das empfangene Byte korrekt ist
            assertEquals(2, receivedByte, "Byte sollte dem gesendeten Byte entsprechen.");

        } catch (IOException e) {
            e.printStackTrace();
            assertEquals("Keine Ausnahme erwartet", e.getMessage());
        }
    }
}
