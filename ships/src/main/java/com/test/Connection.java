package com.test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Connection implements Runnable {

    private String serverIp;
    private int serverPort;
    private Socket socket;

    public Connection() {
    }

    private void connectToServer() {
        try {
            Socket socket = new Socket(serverIp, serverPort);
            socket.setSoTimeout(15000);
            startConnection(socket);
        } catch (IOException e) {
            createServer();
        }
    }

    private void createServer() {
        System.out.println("opening server..");
        try {
            ServerSocket serverSocket = new ServerSocket(serverPort);
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
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                pw.println(scanner.nextLine()); pw.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            while (true) {
                System.out.println(">" + br.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
