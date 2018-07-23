package com.main;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Messenger implements Runnable {

    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 666;

    private String serverIp;
    private int serverPort;
    private Socket socket;

    private Messenger() {
        connectToServer();
    }

    private void setupServerAddress() {
        System.out.println("please write client IP:PORT");
        final String[] clientAddress = new Scanner(System.in).nextLine().split(":");
        if (clientAddress.length == 2) {
            serverIp = clientAddress[0];
            serverPort = Integer.valueOf(clientAddress[1]);
        }
        if (serverIp == null) serverIp = SERVER_IP;
        if (serverPort == 0) serverPort = SERVER_PORT;
    }

    private void connectToServer() {
        setupServerAddress();
        System.out.println("connecting to server " + serverIp + ":" + serverPort);
        try {
            Socket socket = new Socket(serverIp, serverPort);
            startConnection(socket);
        } catch (IOException e) {
            createServer();
        }
    }

    private void createServer() {
        System.out.println("opening server..");
        try {
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
            startConnection(serverSocket.accept());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startConnection(Socket socket) {
        System.out.println("connected with " + socket.getInetAddress().getHostAddress());
        this.socket = socket;
        new Thread(this).start();

        try {
            BufferedOutputStream os = new BufferedOutputStream(socket.getOutputStream());
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                final String line = scanner.nextLine();
                if (line.startsWith("file")) {
                    final String[] args = line.split(" ");
                    if (args.length == 2) {
                        System.out.println("sending file...");
                        sendFile(socket.getOutputStream(), args[1]);
                        continue;
                    }
                }
                pw.println(line); pw.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendFile(OutputStream os, String path) {
        try {
            File file = new File(path);
            final String header = ("file " + path + " " + file.length() + "\n");
            System.out.println(header);
            os.write(header.getBytes());
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            fis.read(buffer);
            fis.close();
            os.write(buffer); os.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            while (true) {
                final String line = br.readLine();
                if (line.startsWith("file")) {
                    final String[] args = line.split(" ");
                    System.out.println("got file '" + args[1] + "' [" + args[2] + "]");
                    readAndSaveFile(br, args[1], Integer.valueOf(args[2]));
                    continue;
                }
                System.out.println(">" + line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readAndSaveFile(BufferedReader br, String path, int length) {
        try {
            File file = new File("tmp/" + path);
            file.createNewFile();
            FileWriter fw = new FileWriter(file);
            System.out.println("reading data... " + length);
            for (int i = 0; i < length; i++) {
                System.out.println(i);
                if (br.ready()) {
                    break;
                }
                br.read();
//                fw.write();
            }
            fw.close();
            System.out.println("file saved!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String... args) {
        new Messenger();
    }
}
