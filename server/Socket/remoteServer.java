package server.Socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class remoteServer {
    private final int PORT = 8335; // 端口号
    private static final byte[] VER = { 0x5, 0x0 };// di
    private byte[] buffer = new byte[512];
    private static final byte[] CONNECT_OK = { 0x5, 0x0, 0x0, 0x1, 0, 0, 0, 0, 0, 0 };
    private Socket connection;

    public remoteServer(String[] args) {
        try (ServerSocket server = new ServerSocket(PORT)) {
            Socket connection = server.accept();
            this.connection = connection;
            firstLink();
            linkRemote();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void firstLink() throws IOException {
        try {
            OutputStream out = connection.getOutputStream();
            InputStream in = connection.getInputStream();
            int len = in.read(buffer);
            System.out.println("< " + bytesToHexString(buffer, 0, len));
            out.write(VER);
            out.flush();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private void linkRemote() throws IOException {
        System.out.println("第二次验证");
        OutputStream out = connection.getOutputStream();
        InputStream in = connection.getInputStream();

        System.out.println("> " + bytesToHexString(VER, 0, VER.length));
        int len = in.read(buffer);
        int ver = buffer[2];
        System.out.println(ver);
        System.out.println("< " + bytesToHexString(buffer, 0, len));
        // 查找主机和端口
        String host = findHost(buffer, 4, 7);
        int port = findPort(buffer, 8, 9);
        System.out.println("host=" + host + ",port=" + port);
        out.write(CONNECT_OK);
        Socket targetSocket = new Socket(host, port);
        OutputStream tarOut = targetSocket.getOutputStream();
        InputStream tarIn = targetSocket.getInputStream();
        while (true) {
            while (in.available() != 0) {
                len = in.read(buffer);
                tarOut.write(buffer, 0, len);
                System.out.println(">" + bytesToHexString(buffer, 0, len));
            }

            while (tarIn.available() != 0) {
                len = tarIn.read(buffer);
                System.out.println("<" + bytesToHexString(buffer, 0, len));
                out.write(buffer, 0, len);
            }

            if (connection.isClosed()) {
                System.out.println("closed");
                break;
            }
        }
    }

    public static String findHost(byte[] bArray, int begin, int end) {
        StringBuffer sb = new StringBuffer();
        for (int i = begin; i <= end; i++) {
            sb.append(Integer.toString(0xFF & bArray[i]));
            sb.append(".");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public static int findPort(byte[] bArray, int begin, int end) {
        int port = 0;
        for (int i = begin; i <= end; i++) {
            port <<= 16;
            port += bArray[i];
        }
        return port;
    }

    public static final String bytesToHexString(byte[] bArray, int begin, int end) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = begin; i < end; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
            sb.append(" ");
        }
        return sb.toString();
    }

    // public static void main(String[] args) {
    // byte VERSION = 0x05;
    // byte METHODS = 0x00;
    // byte[] buff = new byte[255];
    // try (ServerSocket server = new ServerSocket(8335);) {
    // Socket connection = server.accept();
    // OutputStream out = connection.getOutputStream();
    // InputStream in = connection.getInputStream();
    // in.read(buff, 0, 2);
    // int version = buff[0];
    // int methodNum = buff[1];

    // System.out.println(version);
    // System.out.println(methodNum);
    // if (version != VERSION) {
    // System.out.println("is not 0x05");
    // } else {
    // System.out.println("is 0x05");
    // buff[0] = VERSION;
    // buff[1] = METHODS;
    // out.write(buff, 0, 2);
    // }
    // out.flush();

    // // 接收客户端命令
    // in.read(buff, 0, 4);

    // } catch (Exception e) {

    // }
    // }

    public static void main(String[] args) {
        remoteServer server = new remoteServer(args);
    }

}