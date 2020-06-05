package server.Socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class remoteServer {
    private String STATUS = "firstLink";
    private final int PORT = 8335; // 端口号
    private final byte VERSION = 0X05;// 只支持socks5
    private final byte METHDOS = 0X00;// 一律不验证
    private final byte[] CMD = new byte[1024 * 512];

    public remoteServer(String[] args) {
        try (ServerSocket server = new ServerSocket(PORT)) {
            Socket connection = server.accept();
            OutputStream out = connection.getOutputStream();
            InputStream in = connection.getInputStream();
            while (true) {
                switch (STATUS) {
                    case "firstLink":
                        this.firstLink(in, out, connection);
                        this.STATUS = "LinkRemoteServer";
                        break;
                    case "LinkRemoteServer":
                        this.LinkRemoteServer(in, out, connection);
                        break;

                    case "close":

                        break;
                    default:
                        break;
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void firstLink(InputStream in, OutputStream out, Socket connection) {
        try {
            byte[] buff = new byte[255];
            in.read(buff, 0, 2);
            int version = buff[0];
            int methods = buff[1];
            System.out.println(version + methods);
            if (version != this.VERSION) {
                throw new RuntimeException("version must 0x05");
            }

            if (methods != this.METHDOS) {
                throw new RuntimeException("methods must 0x00");
            }

            buff[0] = VERSION;
            buff[1] = METHDOS;
            out.write(buff, 0, 2);
            this.STATUS = "LinkRemoteServer";
        } catch (Exception e) {

        }
    }

    public void LinkRemoteServer(InputStream in, OutputStream out, Socket connection) {
        byte[] buff = new byte[255];
        byte[] cmd = new byte[1024 * 512];
        try {
            in.read(buff, 0, 4);
            int version = buff[0];
            int addr = buff[3];
            System.out.println(version);
        } catch (Exception e) {
            // TODO: handle exception
        }
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