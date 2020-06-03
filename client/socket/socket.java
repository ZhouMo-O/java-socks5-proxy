package client.socket;

import java.io.*;
import java.net.*;
import java.util.*;

public class socket {
    public static void main(String[] args) throws IOException {
        try (Socket s = new Socket("127.0.01", 8189); Scanner in = new Scanner(s.getInputStream(), "UTF-8")) {
            s.setSoTimeout(10000);
            while (in.hasNextLine()) {
                String line = in.nextLine();
                System.out.println(line);
            }
        }
    }
}