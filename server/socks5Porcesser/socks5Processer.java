package server.socks5Porcesser;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import client.socket.socket;

public class socks5Processer {
    private int PORT = 8336;
    private static final byte VERSION = 0x5; // 只 socks5
    private static final byte METHOD = 0x0;// 不加密
    private byte[] buffer = new byte[512];
    private Socket clientSocket;
    private Socket targetSocket;
    private OutputStream targetOutput;
    private InputStream targetInput;
    private String targetAddress;
    int targetPort;

    public socks5Processer(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        System.out.println("link");
    }

    public void run() throws IOException {
        firstLinkProcess();// 处理和socks5的第一次握手
        handleClientCmd();// 处理完和socks5的第一次握手之后，就要开始处理client发来的目标地址
    }

    // 处理和socks5的第一次握手
    private void firstLinkProcess() throws IOException {

        try {
            System.out.println("first link up");
            OutputStream clientOutput = clientSocket.getOutputStream();
            InputStream clientInput = clientSocket.getInputStream();
            int bufferLength = clientInput.read(buffer);
            System.out.println("> " + bytesToHexString(buffer, 0, bufferLength));
            int version = buffer[0];// 获取socks版本
            if (version != VERSION) {
                System.out.println("not support socks");
                throw new RuntimeException("only support socks5");
            }
            buffer[0] = VERSION;
            buffer[1] = METHOD; // 目前只支持 不做任何校验，所以直接返回0
            clientOutput.write(buffer, 0, 2);
            clientOutput.flush();// 刷新数据
            System.out.println("< " + bytesToHexString(buffer, 0, 2));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleClientCmd() {
        try {
            System.out.println("handle Client command");
            OutputStream clientOutput = clientSocket.getOutputStream();
            InputStream clientInput = clientSocket.getInputStream();

            int len = clientInput.read(buffer);
            int addrType = buffer[3];
            System.out.println("> " + bytesToHexString(buffer, 0, len));

            if (addrType == 1) { // 如果是1就是客户端直接发来了Ip
                System.out.println("ip");
                targetAddress = findHost(buffer, 4, 7);// 获取Ip
                targetPort = findPort(buffer, 8, 9);
                System.out.println(targetAddress);
            }
            if (addrType == 3) {// 如果是3代表发来的是一个域名
                int domainLength = buffer[4];
                System.out.println(domainLength);
                targetAddress = new String(Arrays.copyOfRange(buffer, 4, domainLength + 5));
                targetPort = 80;
                System.out.println("domain");
                System.out.println(targetAddress);
            }

            System.out.println("targetHost=" + targetAddress + ",targetPort=" + targetPort);
            handleConnectCommand(targetAddress, targetPort);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleConnectCommand(String targetAddress, int targetPort) {
        targetSocket = null;
        try {
            OutputStream clientOutput = clientSocket.getOutputStream();
            InputStream clientInput = clientSocket.getInputStream();
            targetSocket = new Socket(targetAddress, targetPort);
            clientOutput.write(buildCmd());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        new SocketForwarding(clientSocket, targetSocket).run();
    }

    private byte[] buildCmd() {
        byte[] cmd = { 0x5, 0x0, 0x0, 0x1, 0, 0, 0, 0, 0, 0 }; // 这里需要返回版本号，状态码，rsv，ip，port。版本状态码和RSV固定，Ip和port其实不要发送到服务端，按照格式填写00就行
        return cmd;
    }

    public static int findPort(byte[] bArray, int begin, int end) {
        int port = 0;
        for (int i = begin; i <= end; i++) {
            port <<= 16;
            port += bArray[i];
        }
        return port;
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

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8335)) {
            while (true) {
                Socket socket = serverSocket.accept();
                new socks5Processer(socket).run();

            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }
}

/**
 * Innersocks5Processer
 */
class SocketForwarding {
    private Socket clientSocket;// 客户端socket
    private Socket targetSocket;// 服务端socket
    private String targetAddr;
    private int targetPort;

    SocketForwarding(Socket clientSocket, Socket targetSocket) {
        this.clientSocket = clientSocket;
        this.targetSocket = targetSocket;
        this.targetAddr = targetSocket.getInetAddress().getHostAddress();
        this.targetPort = targetSocket.getPort();
    }

    public void run() {
        OutputStream clientOutput = null;
        InputStream clientInput = null;
        InputStream targetInput = null;
        OutputStream targetOutPut = null;
        long start = System.currentTimeMillis();

        try {
            clientOutput = clientSocket.getOutputStream();
            clientInput = clientSocket.getInputStream();
            targetInput = targetSocket.getInputStream();
            targetOutPut = targetSocket.getOutputStream();
            byte[] buffer = new byte[1024 * 512];

            boolean needSleep = true;
            while (true) {
                while (clientInput.available() != 0) {
                    int len = clientInput.read(buffer);
                    targetOutPut.write(buffer, 0, len);
                    System.out.printf("client to remote bytes=%d", len);
                }

                while (targetInput.available() != 0) {
                    int len = targetInput.read(buffer);
                    clientOutput.write(buffer, 0, len);
                    System.out.printf("remote to remote bytes=%d", len);
                }
                if (clientSocket.isClosed()) {
                    System.out.printf("client closed");
                    break;
                }
                if (System.currentTimeMillis() - start > 30_000) {
                    System.out.println("time out");
                    break;
                }
                if (needSleep) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            close(clientInput);
            close(clientOutput);
            close(targetInput);
            close(targetOutPut);
            close(clientSocket);
            close(targetSocket);
        }
        System.out.println("done.");
    }

    private static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}