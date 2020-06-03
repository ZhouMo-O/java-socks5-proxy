package server.Socket;

import java.io.*;
import java.net.*;
import java.util.*;

public class ThreadedServer {
    public static void main(String[] args) {
        try (ServerSocket s = new ServerSocket(8189)) {
            int i = 1;
            while (true) {
                Socket incoming = s.accept();
                System.out.println("Spawning " + i);
                Runnable r = new ThreadedEchoHandler(incoming);
                Thread t = new Thread(r);
                t.start();
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class ThreadedEchoHandler implements Runnable {
    private Socket incoming;

    public ThreadedEchoHandler(Socket incomingSocket) {
        incoming = incomingSocket;
    }

    public void run() {
        try (InputStream inStream = incoming.getInputStream(); OutputStream outStream = incoming.getOutputStream()) {
            Scanner in = new Scanner(inStream, "UTF-8");
            PrintWriter out = new PrintWriter(new OutputStreamWriter(outStream, "UTF-8"), true);
            out.println("hello enter Bye to exit.");
            boolean done = false;
            while (!done && in.hasNextLine()) {
                String line = in.nextLine();
                out.println("Echo:" + line);
                if (line.trim().equals("Bye")) {
                    done = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}