package util;

import javax.xml.crypto.Data;
import java.io.*;

public class MySerialization {

    // Serialisieren des int Arrays in den OutputStream
    void serialize(int[] intArray, OutputStream os) throws IOException {
        DataOutputStream dos = new DataOutputStream(os);
        dos.writeInt(intArray.length); // Zuerst die Länge des Arrays übertragen
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

    // Datei serialisieren (übertragen)
    public void serializeFile(File file, OutputStream os) throws IOException {
        DataOutputStream dos = new DataOutputStream(os);

        // Dateinamen und Länge übertragen
        dos.writeUTF(file.getName());
        dos.writeLong(file.length());

        try (FileInputStream fis = new FileInputStream(file)){ // Dann die Datei in Chunks (Puffer) übertragen
            byte[] buffer = new byte[4096]; // 4KB als Puffer
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) { //Wenn das Ende der Datei erreicht ist, gibt read() -1 zurück
                dos.write(buffer, 0, bytesRead);
            }
        }
        dos.flush(); // Sicherstellen, dass alle Daten gesendet werden
    }

    // Datei deserialisieren (empfangen)
    public File deserializeFile(InputStream is, File targetDirectory) throws IOException {
        DataInputStream dis = new DataInputStream(is);

        // Dateinamen und Länge lesen
        String fileName = dis.readUTF();
        long fileLength = dis.readLong();

        File targetFile = new File(targetDirectory, fileName); // Zieldatei erstellen
        targetFile.getParentFile().mkdirs(); // Sicherstellen, dass Zielverzeichnis existiert

        try (FileOutputStream fos = new FileOutputStream(targetFile)) { // Datei schreiben
            byte[] buffer = new byte[4096]; //4KB als Puffer
            long remainingBytes = fileLength;

            while (remainingBytes > 0) { //Bis alle Bytes gelesen sind
                int bytesToRead = (int) Math.min(buffer.length, remainingBytes); // Berechnen, wie viele Bytes in diesem Durchgang gelesen werden sollen
                int bytesRead = dis.read(buffer, 0, bytesToRead);  // Bytes aus dem Eingabestream in den Puffer lesen

                if (bytesRead == -1) {
                    throw new IOException("Unerwartetes Dateiende");
                }

                fos.write(buffer, 0, bytesRead); // Gelesene Bytes in Zieldatei schreiben
                remainingBytes -= bytesRead;
            }
        }

        return targetFile;
    }
}
