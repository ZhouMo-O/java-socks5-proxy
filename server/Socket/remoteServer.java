package server.Socket;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import inheritance.server.Server;

public class remoteServer {
    private final int PORT = 8335; // 端口号
    private final byte VERSION = 0X05;// 只支持socks5
    private final byte METHDOS = 0X00;// 一律不验证

    public remoteServer(String[] args) {
        try (ServerSocket server = new ServerSocket(PORT)) {

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public void firstLink() {

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

}