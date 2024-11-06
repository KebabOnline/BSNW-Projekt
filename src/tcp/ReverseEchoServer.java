package tcp;

import java.io.*;

public class ReverseEchoServer implements ConnectionHandler{
    @Override
    public void handleConnection(InputStream is, OutputStream os) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(is);
        DataOutputStream dataOutputStream = new DataOutputStream(os);

        // Read values in order
        long receivedLong = dataInputStream.readLong();
        double receivedDouble = dataInputStream.readDouble();
        String receivedString = dataInputStream.readUTF();

        // Print values
        System.out.println("Recieved Long: " + receivedLong);
        System.out.println("Recieved Double: " + receivedDouble);
        System.out.println("Recieved String: " + receivedString);

        // Send values back in reverse order
        dataOutputStream.writeUTF(receivedString);
        dataOutputStream.writeDouble(receivedDouble);
        dataOutputStream.writeLong(receivedLong);

        // Close streams
        dataInputStream.close();
        dataOutputStream.close();
    }
}
