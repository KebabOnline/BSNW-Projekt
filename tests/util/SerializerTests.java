package util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    public void fileTest1() throws IOException {
        String sampleData = "TestContent";
        String fileNameSource = "sourceFile.txt";
        String targetDirectoryPath = "targetDirectory";

        // 1. Quelldatei erstellen und mit Beispielinhalt füllen
        File sourceFile = new File(fileNameSource);
        try (FileOutputStream fos = new FileOutputStream(sourceFile); DataOutputStream dos = new DataOutputStream(fos)) {
            dos.writeUTF(sampleData);
        }

        // 2. Zielverzeichnis vorbereiten
        File targetDirectory = new File(targetDirectoryPath);
        if (!targetDirectory.exists()) {
            assertTrue(targetDirectory.mkdirs());
        }

        // 3. Serialisieren und deserialisieren
        MySerialization ms = new MySerialization();

        // Byte-Stream für die Serialisierung
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ms.serializeFile(sourceFile, baos);

        // Byte-Stream für die Deserialisierung
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        File deserializedFile = ms.deserializeFile(bais, targetDirectory);

        // 4. Überprüfen, ob die deserialisierte Datei korrekt ist
        assertTrue(deserializedFile.exists());
        assertEquals(sourceFile.getName(), deserializedFile.getName());
        assertEquals(sourceFile.length(), deserializedFile.length());

        // 5. Inhalt der Quelldatei und der deserialisierten Datei vergleichen
        try (DataInputStream originalDis = new DataInputStream(new FileInputStream(sourceFile));
             DataInputStream deserializedDis = new DataInputStream(new FileInputStream(deserializedFile))) {

            String originalContent = originalDis.readUTF();
            String deserializedContent = deserializedDis.readUTF();

            assertEquals(originalContent, deserializedContent);
        }

        // 6. Aufräumen
        assertTrue(sourceFile.delete());
        assertTrue(deserializedFile.delete());
        assertTrue(targetDirectory.delete());

    }
}
