package server.Socket;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) {
        try (ServerSocket s = new ServerSocket(8189)) {
            try (Socket incoming = s.accept()) {
                InputStream inStream = incoming.getInputStream();
                OutputStream outStream = incoming.getOutputStream();
                try (Scanner in = new Scanner(inStream, "UTF-8")) {
                    PrintWriter out = new PrintWriter(new OutputStreamWriter(outStream, "UTF-8"), true/* autoFlush */);
                    out.println("Hello! Enter Bye to exit.");
                    boolean done = false;
                    while (!done && in.hasNextLine()) {
                        String line = in.nextLine();
                        out.println("Echo:" + line);

                        if (line.trim().equals("Bye")) {
                            done = true;
                        }
                        if (line.trim().equals("www.baidu.com")) {
                            Socket remote = new Socket("www.baidu.com", 80);
                            Scanner remoteIn = new Scanner(remote.getInputStream(), "UTF-8");
                            while (remoteIn.hasNextLine()) {
                                String Reline = in.nextLine();
                                out.println(Reline);
                            }

                            remote.close();
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}