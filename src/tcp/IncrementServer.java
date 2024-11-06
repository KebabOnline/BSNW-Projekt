package tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IncrementServer implements ConnectionHandler{
    @Override
    public void handleConnection(InputStream is, OutputStream os) throws IOException {
        for(int i = 0; i <= 9; i++) {
            os.write(is.read() + 1); // Increment Byte that is read
        }

        is.close();
        os.close();
    }
}
