package sfs;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class SimpleFileServerCLI {
    private static SimpleFileServerClient client;
    private static Socket socket;
    private static final String PROMPT = "> ";
    private static final String DEFAULT_ROOT_DIR = "client_files";

    public static void main(String[] args) {
        printUsage();
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print(PROMPT);
                String input = scanner.nextLine().trim();

                if (input.isEmpty()) continue;

                String[] commands = input.split("\\s+");
                processCommand(commands);
            }
        }
    }

    private static void processCommand(String[] commands) {
        try {
            switch (commands[0].toLowerCase()) {
                case "connect":
                    handleConnect(commands);
                    break;
                case "close":
                    handleClose();
                    break;
                case "put":
                    handlePut(commands);
                    break;
                case "get":
                    handleGet(commands);
                    break;
                case "exit":
                    System.exit(0);
                    break;
                default:
                    System.out.println("Unknown command. Available commands: connect, close, put, get, exit");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void handleConnect(String[] commands) throws IOException {
        if (socket != null && !socket.isClosed()) {
            System.out.println("Already connected. Please close current connection first.");
            return;
        }

        if (commands.length < 2) {
            System.out.println("Usage: connect <host> [port]");
            return;
        }

        String host = commands[1];
        int port = commands.length > 2 ? Integer.parseInt(commands[2]) : 4444;

        try {
            socket = new Socket(host, port);
            client = new SimpleFileServerClient(
                    DEFAULT_ROOT_DIR,
                    socket.getInputStream(),
                    socket.getOutputStream()
            );
            System.out.println("Connected to " + host + ":" + port);
        } catch (IOException e) {
            System.out.println("Failed to connect: " + e.getMessage());
            socket = null;
            client = null;
        }
    }

    private static void handleClose() throws IOException {
        if (socket == null || socket.isClosed()) {
            System.out.println("Not connected.");
            return;
        }

        socket.close();
        socket = null;
        client = null;
        System.out.println("Connection closed.");
    }

    private static void handlePut(String[] commands) throws IOException {
        if (socket == null || socket.isClosed() || client == null) {
            System.out.println("Not connected. Use 'connect' first.");
            return;
        }

        if (commands.length < 2) {
            System.out.println("Usage: put <filename>");
            return;
        }

        try {
            client.putFile(commands[1]);
            System.out.println("File uploaded successfully.");
        } catch (IOException e) {
            System.out.println("Failed to upload file: " + e.getMessage());
        }
    }

    private static void handleGet(String[] commands) throws IOException {
        if (socket == null || socket.isClosed() || client == null) {
            System.out.println("Not connected. Use 'connect' first.");
            return;
        }

        if (commands.length < 2) {
            System.out.println("Usage: get <filename>");
            return;
        }

        try {
            client.getFile(commands[1]);
            System.out.println("File downloaded successfully.");
        } catch (IOException e) {
            System.out.println("Failed to download file: " + e.getMessage());
        }
    }

    private static void printUsage() {
        System.out.println("valid commands:");
        System.out.println("connect <ip> [port]");
        System.out.println("put <file name>");
        System.out.println("get <file name>");
        System.out.println("close");
        System.out.println("exit");
    }
}