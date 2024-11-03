package tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionFactory {
    private int port;

    public ConnectionFactory(int port){
        this.port = port;
    }

    public void acceptNewConnections() {
        try (ServerSocket srvSocket = new ServerSocket(7777);
             Socket newConnection = srvSocket.accept()) {

            handleConnection(newConnection);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void handleConnection(Socket newConnection) {
        try (OutputStream outputStream = newConnection.getOutputStream();
             InputStream inputStream = newConnection.getInputStream()) {

            // Sende Byte über den OutputStream
            outputStream.write(1);
            outputStream.flush();

            // Lese Byte über InputStream und zeig an
            int receivedByte = inputStream.read();
            System.out.println(receivedByte);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
