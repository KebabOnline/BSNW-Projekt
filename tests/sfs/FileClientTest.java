package sfs;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;

public class FileClientTest {

    private static final String ROOT_DIR = "test_root_dir";
    private static final String GET_FILE_NAME = "test_download.txt";
    private static final String NOT_EXISTING_FILE_NAME = "non_existing.txt";
    private static final String PUT_FILE_NAME = "test_upload.txt";
    private static final byte[] TEST_BYTES = "Hello, FileServer!".getBytes();

    @Test
    public void testGet() throws IOException {
        String rootDirName = this.getRootDir();

        Socket socket = new Socket("localhost", 4444);
        SimpleFileServerClient sfsClient =
                new SimpleFileServerClient(rootDirName, socket.getInputStream(), socket.getOutputStream());

        sfsClient.getFile(GET_FILE_NAME);
        File file = new File(rootDirName + "/" + GET_FILE_NAME);
        Assertions.assertTrue((file.exists()));
    }

    @Test
    public void testGetFileDoesNotExist() throws IOException {
        String rootDirName = this.getRootDir();

        Socket socket = new Socket("localhost", 4444);
        SimpleFileServerClient sfsClient =
                new SimpleFileServerClient(rootDirName, socket.getInputStream(), socket.getOutputStream());

        sfsClient.getFile(NOT_EXISTING_FILE_NAME);
        File file = new File(rootDirName + "/" + NOT_EXISTING_FILE_NAME);
        Assertions.assertTrue((!file.exists()));
    }

    @Test
    public void testPut() throws IOException {
        String rootDirName = this.getRootDir();
        String fileName = rootDirName + "/" + PUT_FILE_NAME;
        // produce a file
        File file = new File(fileName);
        OutputStream os = new FileOutputStream(file);
        os.write(TEST_BYTES);
        os.close();

        Socket socket = new Socket("localhost", 4444);
        SimpleFileServerClient sfsClient =
                new SimpleFileServerClient(rootDirName, socket.getInputStream(), socket.getOutputStream());

        sfsClient.putFile(PUT_FILE_NAME);
    }

    @Test
    public void testBeleg() throws IOException {
        String rootDirName = this.getRootDir();

        Socket socket = new Socket("141.45.154.136", 4444);
        SimpleFileServerClient sfsClient =
                new SimpleFileServerClient(rootDirName, socket.getInputStream(), socket.getOutputStream());

        sfsClient.getFile("values.txt");
        //read long and int from file
        try (DataInputStream dataInputStream = new DataInputStream(new FileInputStream(rootDirName + "/values.txt"))) {
            long longValue = dataInputStream.readLong();
            int intValue = dataInputStream.readInt();
            System.out.println("Read long: " + longValue);
            System.out.println("Read int: " + intValue);
            long result = longValue + intValue;
            System.out.println("Result: " + result);
            try (DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(rootDirName + "/ergebnis.txt"))) {
                dataOutputStream.writeLong(result);
                dataOutputStream.writeUTF ("Kerem Gürbüz");
                dataOutputStream.writeUTF ("587049");
            }
            sfsClient.putFile("ergebnis.txt");
        }
    }

    public String getRootDir() {
        int i = 0;
        String rootDirName = ROOT_DIR + "_" + i++;
        File rootDir = new File(rootDirName);
        boolean again = true;
        while(again) {
            if(rootDir.exists()) {
                again = true;
                if(!rootDir.delete()) rootDir.deleteOnExit();
                rootDirName = ROOT_DIR + "_" + i++;
                rootDir = new File(rootDirName);
            } else {
                again = false;
                if(!rootDir.mkdirs()) Assertions.fail("cannot create root dir");
            }
        }
        System.out.println(">>>>>>>> client works in " + rootDirName + " <<<<<<<<<<<<<<<<<<<<<<<<");
        return rootDirName;
    }
}
