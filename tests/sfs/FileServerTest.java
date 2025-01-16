package sfs;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServerTest {
    @Test
    public void testServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket(4444);
        Socket clientSocket = serverSocket.accept();
        SimpleFileServer server = new SimpleFileServer("server_files", clientSocket.getInputStream(), clientSocket.getOutputStream());
        server.start();
    }
}
