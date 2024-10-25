import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        try (Socket socket = new Socket("localhost", 7777)){
            OutputStream os = socket.getOutputStream();
            os.write(1); //byte mit dem wert 1 senden

            InputStream is = socket.getInputStream();
            int receivedByte = is.read(); //byte empfangen

            System.out.println("Empfangenes Byte: " + receivedByte);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}