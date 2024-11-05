package tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionFactory {
    public static final int DEFAULT_PORT = 7777;
    private int port;
    private final ConnectionHandler connectionHandler;

    public ConnectionFactory(int port, ConnectionHandler connectionHandler){
        this.port = port;
        this.connectionHandler = connectionHandler;
    }

    // Falls es ohne Port aufgerufen wird.
    public ConnectionFactory(ConnectionHandler connectionHandler){
        this.connectionHandler = connectionHandler;
        this.port = DEFAULT_PORT;
    }

    //

    public void acceptNewConnections() {
        try (ServerSocket srvSocket = new ServerSocket(this.port);
             Socket newConnection = srvSocket.accept()) {

            //Protocol Engine übernimmt hier
            this.connectionHandler.handleConnection(newConnection.getInputStream(), newConnection.getOutputStream());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /*
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

     */


}
