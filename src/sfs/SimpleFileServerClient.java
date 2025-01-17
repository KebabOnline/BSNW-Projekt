package sfs;

import java.io.*;
import java.nio.file.Files;

public class SimpleFileServerClient {
    private final String rootDir;
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;

    // PDU Types
    private static final byte PROTOCOL_VERSION = 1;
    private static final byte GET_REQUEST = 0x00;
    private static final byte PUT_REQUEST = 0x01;
    private static final byte ERROR = 0x02;
    private static final byte OK = 0x03;

    public SimpleFileServerClient(String rootDir, InputStream is, OutputStream os) {
        this.rootDir = rootDir;
        this.inputStream = new DataInputStream(is);
        this.outputStream = new DataOutputStream(os);
        new File(rootDir).mkdirs(); // If directory doesn't exist
    }

    public void getFile(String fileName) throws IOException {
        // Send GET request
        outputStream.writeByte(PROTOCOL_VERSION);
        outputStream.writeByte(GET_REQUEST);
        outputStream.writeUTF(fileName);
        outputStream.flush();

        // Read response
        byte version = inputStream.readByte();
        byte responseType = inputStream.readByte();
        String responseFileName = inputStream.readUTF();

        if (responseType == PUT_REQUEST) {
            // Server sends file content via PUT
            long fileLength = inputStream.readLong();
            byte[] fileContent = new byte[(int)fileLength];
            inputStream.readFully(fileContent);

            // Write to local file
            File file = new File(rootDir + "/" + fileName);
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(fileContent);
            }
        } else if (responseType == ERROR) {
            int errorCode = inputStream.readInt();
            String errorMessage = inputStream.readUTF();
            throw new IOException("Server error: " + errorMessage + " (code: " + errorCode + ")");
        }
    }

    public void putFile(String fileName) throws IOException {
        File file = new File(rootDir + "/" + fileName);
        if (!file.exists()) {
            throw new IOException("File not found: " + fileName);
        }

        // Read file content
        byte[] fileContent = Files.readAllBytes(file.toPath());

        // Send PUT request
        outputStream.writeByte(PROTOCOL_VERSION);
        outputStream.writeByte(PUT_REQUEST);
        outputStream.writeUTF(fileName);
        outputStream.writeLong(fileContent.length);
        outputStream.write(fileContent);
        outputStream.flush();

        // Read response
        byte version = inputStream.readByte();
        byte responseType = inputStream.readByte();
        String responseFileName = inputStream.readUTF();

        if (responseType == ERROR) {
            int errorCode = inputStream.readInt();
            String errorMessage = inputStream.readUTF();
            throw new IOException("Server error: " + errorMessage + " (code: " + errorCode + ")");
        }
        // else: OK response - muss nichts mehr machen
        if (responseType == OK) {
            System.out.println("Put was successful.");
        }
    }
}