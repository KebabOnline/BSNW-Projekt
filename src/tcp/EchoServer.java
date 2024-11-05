package tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class EchoServer implements ConnectionHandler{
    @Override
    public void handleConnection(InputStream is, OutputStream os) throws IOException {
        int receivedByte = is.read(); // Read Byte
        os.write(receivedByte); // Write same Byte

        is.close(); // TCP signalisiert dem anderen Prozess, dass nichts mehr kommt.
        os.close(); // dem OS wird gesagt, dass man nichts mehr empfangen will.
    }
}
