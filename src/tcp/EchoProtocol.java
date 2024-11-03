package tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class EchoProtocol implements ConnectionHandler{
    @Override
    public void handleConnection(InputStream is, OutputStream os) throws IOException {
        int receivedByte = is.read(); // Read Byte
        os.write(receivedByte); // Write same Byte

        is.close();
        os.close();
    }
}
