package util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SerializerTests {

    @Test
    public void arrayTest() throws IOException {
        MySerialization ms = new MySerialization();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        int[] sample = new int[]{1,2,3,4};
        ms.serialize(sample, os);
        byte[] serializedData = os.toByteArray();
        InputStream is = new ByteArrayInputStream(serializedData);
        int[] result = ms.deserialize(is);
        Assertions.assertArrayEquals(sample, result);
    }
}
