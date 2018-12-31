import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ConcurrentHashMap;
import java.util.*;
public class MessageParser {
    public static ConcurrentHashMap<String, Object> HttpRequestParser() {
        ConcurrentHashMap x = new ConcurrentHashMap();
        return x;
    }

    public static ConcurrentHashMap<String, Object> Socks4Parser(Socket clientSocket) {
//        ConcurrentHashMap x = new ConcurrentHashMap();
//        return x;
//        try {

        ConcurrentHashMap<String, Object> map = new ConcurrentHashMap();
try
{
            InputStream inStream = clientSocket.getInputStream();

            byte[] value;

            value = new byte[]{(byte) inStream.read()};
            map.put("VN", value);

            value = new byte[]{(byte) inStream.read()};
            map.put("CD", value);

            value = new byte[]{(byte) inStream.read(), (byte) inStream.read()};
            map.put("DSTPORT", value);

            value = new byte[]{(byte) inStream.read(), (byte) inStream.read(), (byte) inStream.read(), (byte) inStream.read()};
            map.put("DSTIP", value);
}
        catch (IOException e) {
            System.err.println(e.getMessage());
        }
        catch (NullPointerException e) {
            System.err.println(e.getMessage());
        }

        return map;
    }

    //for test perpose
    public static void Socks4MessageParser (ConcurrentHashMap<String, Object> map)
    {
                
    }
}



