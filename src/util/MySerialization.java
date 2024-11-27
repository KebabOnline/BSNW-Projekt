package util;

import javax.xml.crypto.Data;
import java.io.*;

public class MySerialization {

    // Serialisieren des int Arrays in den OutputStream
    void serialize(int[] intArray, OutputStream os) throws IOException {
        DataOutputStream dos = new DataOutputStream(os);
        dos.writeInt(intArray.length);
        for (int i : intArray){
            dos.writeInt(i);
        }
    }


    // Deserialisieren des int Arrays aus dem InputStream
    int[] deserialize(InputStream is) throws IOException {
        DataInputStream dis = new DataInputStream(is);
        int length = dis.readInt();
        int[] result = new int[length]; // Zuerst die Länge des Arrays lesen
        for (int i = 0; i < length; i++) {
            result[i] = dis.readInt(); // Dann jedes Element des Arrays lesen
        }
        return result;
    }
}
