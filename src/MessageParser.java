import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ConcurrentHashMap;
import java.util.*;
public class MessageParser {

// to be continue
    public static byte [] HttpRequestParser(Socket clientSocket) {
        ConcurrentHashMap<String, Object> map = new ConcurrentHashMap();
        InputStream inStream=null ;
        byte [] reqeustbuffer=new byte [8192  //max size of an http request
                +1000 ];
        int byteRed=0;
        try
        {
            inStream = clientSocket.getInputStream();
            BufferedInputStream requestbuffer=new BufferedInputStream(inStream);
            byteRed =requestbuffer.read(reqeustbuffer,0,9192);
        }


        catch (IOException e) {
            System.err.println(e.getMessage());
        }
        catch (NullPointerException e) {
            System.err.println(e.getMessage());
        }
//        finally {
//            try{
//                inStream.close();
//            }
//            catch(IOException e)
//            {
//
//            }
//        }
        return Arrays.copyOfRange(reqeustbuffer, 0, byteRed+1);

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

//            value = new byte[]{(byte) inStream.read()};
            map.put("VN", inStream.read());

//            value = new byte[]{(byte) inStream.read()};
            map.put("CD", inStream.read());
//            Integer cdint=new Integer((int)value[0]);
//            map.put("CD-INT", cdint);

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
//        finally {
//    try{
//        if(inStream!=null)
//        inStream.close();
//    }
//    catch(IOException e)
//    {
//
//    }
//        }
        return map;
    }

    public static void  Socks4ReplyWriter(int reply, DataOutputStream outToClient) {
//        DataOutputStream outToClient=null;
        try {


//            outToClient = new DataOutputStream(clientSocket.getOutputStream());
            outToClient.write(0);
            outToClient.write(reply);
            outToClient.write(1);
            outToClient.write(1);
            outToClient.write(1);
            outToClient.write(1);
            outToClient.write(1);
            outToClient.write(1);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (NullPointerException e) {
            System.err.println(e.getMessage());
        }
//        finally {
//            try {
//                if (outToClient != null)
//                    outToClient.close();
//            } catch (IOException e) {
//
//            }

//        }
    }
    //for tests perpose
    public static void Socks4ParserPrinter (ConcurrentHashMap<String, Object> map)
    {

        System.out.println("VN"+(int)map.get("VN"));
        System.out.println("CD"+map.get("CD"));
        System.out.println("DSTPORT"+map.get("DSTPORT"));
        System.out.println("DSTIP"+map.get("DSTIP"));

    }
}



