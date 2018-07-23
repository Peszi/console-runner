package com.main;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class FileTransfer implements Runnable {

    public FileTransfer() {
        this.createServer();
    }

    private void createServer() {
        System.out.println("opening server..");
        try {
            ServerSocket serverSocket = new ServerSocket(1234);
            new Thread(this).start();
            Socket socket = serverSocket.accept();

            BufferedOutputStream os = new BufferedOutputStream(socket.getOutputStream());
            try {
                File file = new File("messenger.jar");
                FileInputStream fis = new FileInputStream(file);

                int count;
                byte[] bytes = new byte[8192];
                while ((count = fis.read(bytes)) > 0)
                    os.write(bytes, 0, count);
                os.flush();
                os.close();
                fis.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        connectToServer();
    }

    private void connectToServer() {
        System.out.println("connecting to server");
        try {
            File file = new File("mess.jar");
            file.createNewFile();
            FileOutputStream fw = new FileOutputStream(file);

            Socket socket = new Socket("localhost", 1234);
            BufferedInputStream is = new BufferedInputStream(socket.getInputStream());
            int count;
            byte[] bytes = new byte[8192];
            while ((count = is.read(bytes)) > 0) {
                fw.write(bytes, 0, count);
            }
            is.close();
            fw.close();
        } catch (IOException e) {
            createServer();
        }
    }

    public static void main(String... args) {
        new FileTransfer();
    }
}
