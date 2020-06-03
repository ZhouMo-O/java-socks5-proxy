package client.socket;

import java.io.IOException;
import java.net.InetAddress;

public class inetAddress {
    public static void main(String[] args) throws IOException {
        if (args.length > 0) {
            String host = args[0];
            InetAddress[] addresses = InetAddress.getAllByName(host);
            for (InetAddress a : addresses) {
                System.out.println(a);
            }
        } else {
            InetAddress localHosAddress = InetAddress.getLocalHost();
            System.out.println(localHosAddress);
        }
    }
}