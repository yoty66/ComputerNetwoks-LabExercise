import java.io.*;
import java.net.*;
import java.util.*;
import java.nio.*;
public class Sockspy {
    public static void main(String[] args) {

        try {
            InetAddress local_adress = InetAddress.getLocalHost();
//            InetAddress test=InetAddress.getByName("127.0.0.1");
//            System.out.println(local_adress.toString());

            ServerSocket server = new ServerSocket(8080);

            System.out.println(server.toString());
//            support one connection :
            System.out.println("open accept()");
            Socket connection = server.accept();
            System.out.println("accepted");
            System.out.println(server.toString());
        } catch (UnknownHostException e) {
            System.out.println("UnknownHostException");
        } catch (IOException e) {
            System.out.println("IOException");
        }
    }

}




