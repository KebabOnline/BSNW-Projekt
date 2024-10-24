package streams;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class FileStreamTests {
    @Test
    public void writeAndRead() throws Exception {
        String filename = "testFile.txt";
        OutputStream os = new FileOutputStream(filename);;

        //write
        String someText = "Hallo";
        os.write(someText.getBytes()); //stores someText in testFile.txt in byte form

        //read
        InputStream is = new FileInputStream(filename);
        byte[] readBuffer = new byte[100];
        is.read(readBuffer); //read bytes from textFile.txt and stores them in readBuffer

        String readString = new String (readBuffer); //converts byte array into string
        StringBuilder sb = new StringBuilder();
        sb.append("wrote: ");
        sb.append(someText);
        sb.append(" | read: ");
        sb.append(readString);
        System.out.println(sb.toString());

        readString = readString.substring(0, someText.length());
        System.out.println(readString);

        Assertions.assertTrue(readString.equals(someText));
    }
}
