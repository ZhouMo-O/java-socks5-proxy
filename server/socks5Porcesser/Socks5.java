package server.socks5Porcesser;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Socks5 {
    private static final int PORT = 8335;
    private static final byte VERSION = 0X05;
    private static final byte METHOD = 0x0;// 不加密
    private static final byte RSV = 0x00;
    private static String SERVER_IP_ADDRESS;
    private static Logger log = Logger.getGlobal();

    public static class cmdhandle {
        private Socket clientSocket;
        private String targetAddr;
        private int targetPort = 80;
        private byte[] buff = new byte[512];

        public cmdhandle(Socket clientSocket) {
            this.clientSocket = clientSocket;

        }

        private void run() throws IOException {
            firstLinkProcess();// first link process
            handleClientCmd();// cmd process
        }

        // 处理第一次连接交换验证方法
        private void firstLinkProcess() throws IOException {
            log.info("first link process");
            OutputStream clientOutput = clientSocket.getOutputStream();
            InputStream clientInput = clientSocket.getInputStream();

            int bufferLength = clientInput.read(buff);
            System.out.println("> " + bytesToHexString(buff, 0, bufferLength));
            int version = buff[0];// 获取socks版本
            if (version != VERSION) {
                System.out.println("not support socks");
                throw new RuntimeException("only support socks5");
            }
            buff[0] = VERSION;
            buff[1] = METHOD; // 目前只支持 不做任何校验，所以直接返回0
            clientOutput.write(buff, 0, 2);
            clientOutput.flush();// 刷新数据
            log.info("remoteSend > version: " + buff[0] + " auth: " + buff[1]);
        }

        // 第一步握手完成之后连接
        private void handleClientCmd() throws IOException {
            log.info("handle ClientCmd");
            InputStream clientInput = clientSocket.getInputStream();
            byte[] buff = new byte[512];
            int len = clientInput.read(buff);
            int version = buff[0];
            int cmd = buff[1];
            int rsv = buff[2];
            int addrType = buff[3];
            System.out.println("> " + bytesToHexString(buff, 1, len));
            System.out.println(addrType);

            switch (addrType) {
                case 1:
                    log.info("Cient Addr type is ip");
                    targetAddr = findHost(buff, 4, 7);// 获取Ip
                    targetPort = findPort(buff, 8, 9);// 获取port
                    break;
                case 3:
                    log.info("Cient Addr type is domain");
                    int domainLen = buff[4];
                    targetAddr = new String(Arrays.copyOfRange(buff, 4, domainLen + 5));

                    targetPort = findPort(buff, domainLen + 5, domainLen + 6);
                    break;
                default:
                    log.info("not support");
                    break;
            }
            log.info("targetAddr: " + targetAddr + " targetPort: " + targetPort);
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

    }

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(PORT);) {
            log.info("server start listen port" + PORT);
            while (true) {
                Socket socket = server.accept();
                new cmdhandle(socket).run();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}