import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ConcurrentHashMap;
import java.util.*;
public class MessageParser {

// to be continue
    public static ConcurrentHashMap<String, Object> HttpRequestParser(Socket clientSocket) {
        ConcurrentHashMap<String, Object> map = new ConcurrentHashMap();
        InputStream inStream=null ;
        try
        {
            inStream = clientSocket.getInputStream();
            BufferedInputStream request=new BufferedInputStream(inStream);

        }


        catch (IOException e) {
            System.err.println(e.getMessage());
        }
        catch (NullPointerException e) {
            System.err.println(e.getMessage());
        }
        finally {
            try{
                inStream.close();
            }
            catch(IOException e)
            {

            }
        }
        return map;

    }









    public static ConcurrentHashMap<String, Object> Socks4Parser(Socket clientSocket) {
//        ConcurrentHashMap x = new ConcurrentHashMap();
//        return x;
//        try {

        ConcurrentHashMap<String, Object> map = new ConcurrentHashMap();
        InputStream inStream=null ;
try
{
             inStream = clientSocket.getInputStream();

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
        finally {
    try{
        inStream.close();
    }
    catch(IOException e)
    {

    }
        }
        return map;
    }

    //for test perpose
    public static void Socks4ParserPrinter (ConcurrentHashMap<String, Object> map)
    {

        System.out.println("VN"+map.get("VN"));
        System.out.println("CD"+map.get("CD"));
        System.out.println("DSTPORT"+map.get("DSTPORT"));
        System.out.println("DSTIP"+map.get("DSTIP"));

    }
}



