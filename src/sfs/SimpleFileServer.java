package sfs;

import java.io.*;
import java.net.SocketException;
import java.nio.file.Files;

public class SimpleFileServer {
    private final String serverDirectory;
    private final DataInputStream dis;
    private final DataOutputStream dos;

    // PDU Types
    private static final byte PROTOCOL_VERSION = 1;
    private static final byte GET_REQUEST = 0x00;
    private static final byte PUT_REQUEST = 0x01;
    private static final byte ERROR = 0x02;
    private static final byte OK = 0x03;

    public SimpleFileServer(String serverDirectory, InputStream is, OutputStream os) {
        this.serverDirectory = serverDirectory;
        this.dis = new DataInputStream(is);
        this.dos = new DataOutputStream(os);
        new File(serverDirectory).mkdirs(); // If directory doesn't exist
    }

    public void start() throws IOException {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                handleRequest();
            }
        } catch (EOFException | SocketException e) {
            // Connection was closed by client
            System.out.println("Client closed connection");
        }
    }

    private void handleRequest() throws IOException {
        byte version = dis.readByte();
        byte command = dis.readByte();
        String filename = dis.readUTF();

        if (version != PROTOCOL_VERSION) {
            sendError(filename, 1, "Unsupported protocol version");
            return;
        }

        switch (command) {
            case GET_REQUEST:
                handleGetRequest(filename);
                break;
            case PUT_REQUEST:
                handlePutRequest(filename);
                break;
            default:
                sendError(filename, 2, "Unknown command");
        }
    }

    private void handleGetRequest(String filename) throws IOException {
        File file = new File(serverDirectory, filename);

        if (!file.exists()) {
            sendError(filename, 3, "File not found"); // have to create test_download.txt beforehand
            return;
        }

        byte[] content = Files.readAllBytes(file.toPath());

        // Send PUT response with file content
        dos.writeByte(PROTOCOL_VERSION);
        dos.writeByte(PUT_REQUEST);
        dos.writeUTF(filename);
        dos.writeLong(content.length);
        dos.write(content);
        dos.flush();
    }

    private void handlePutRequest(String filename) throws IOException {
        long length = dis.readLong();
        if (length > Integer.MAX_VALUE) {
            sendError(filename, 4, "File too large");
            return;
        }

        byte[] content = new byte[(int)length];
        dis.readFully(content);

        File file = new File(serverDirectory, filename);

        // Delete existing file if file already exists
        if (file.exists()) {
            file.delete();
        }

        // Write file
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(content);
        }

        // Send OK response
        dos.writeByte(PROTOCOL_VERSION);
        dos.writeByte(OK);
        dos.writeUTF(filename);
        dos.flush();
    }

    private void sendError(String filename, int errorCode, String errorMessage) throws IOException {
        dos.writeByte(PROTOCOL_VERSION);
        dos.writeByte(ERROR);
        dos.writeUTF(filename);
        dos.writeInt(errorCode);
        dos.writeUTF(errorMessage);
        dos.flush();
    }
}
