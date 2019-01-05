import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ConcurrentHashMap;
import java.util.*;
 class MessageParser {

// to be continue
    public static byte [] HttpRequestParser(SocketChannel clientChannel) {
        ConcurrentHashMap<String, Object> map = new ConcurrentHashMap();

        ByteBuffer requestbuffer=null;
        int byteRed=0;
        try
        {
//            inStream = clientSocket.getInputStream();
//            BufferedInputStream requestbuffer=new BufferedInputStream(inStream);
             requestbuffer=ByteBuffer.allocate(
                    8192  //max size of an http request
                            +1000
            );
//             requestbuffer.clear();
            byteRed =clientChannel.read(requestbuffer);
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
        if(byteRed==-1)
            return null;
        return Arrays.copyOfRange(requestbuffer.array(), 0, byteRed+1);

    }








    public static ConcurrentHashMap<String, Object> Socks4Parser(SocketChannel clientSocket) {
//        ConcurrentHashMap x = new ConcurrentHashMap();
//        return x;
//        try {
        ByteBuffer byteBuffer=null;
        ConcurrentHashMap<String, Object> map = new ConcurrentHashMap();
        int byteRed=0;
        byte[] valuesVMtoDSTIP=null;
//        InputStream inStream=null ;
try
{
//             inStream = clientSocket.getInputStream();

//           =new byte[8];
//            clientSocket.read()
//            value = new byte[]{(byte) inStream.read()};

        byteBuffer=ByteBuffer.allocate(100);
        byteBuffer.clear();
        byteRed=clientSocket.read(byteBuffer);
         byteBuffer.clear();
        valuesVMtoDSTIP=byteBuffer.array();
     //TODO add error
     if(byteRed!=8)
     {
         byteRed=5;
     }
            map.put("VN", (byte)valuesVMtoDSTIP[0]);

//            value = new byte[]{(byte) inStream.read()};
            map.put("CD", (byte)valuesVMtoDSTIP[1]);
//            Integer cdint=new Integer((int)value[0]);
//            map.put("CD-INT", cdint);


    int port=(valuesVMtoDSTIP[2] & 0xFF) << 8 | (valuesVMtoDSTIP[3] & 0xFF);
    map.put("DSTPORT", port);

    String IPvalue = (0xff &valuesVMtoDSTIP[4])+"."+(0xff &valuesVMtoDSTIP[5])
            +"."+(0xff &valuesVMtoDSTIP[6])+"."+(0xff &valuesVMtoDSTIP[7]);
            map.put("DSTIP", IPvalue);

            //DSTIP-84.-39.22.46
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

    public static void  Socks4ReplyWriter(int reply, SocketChannel outToClient) {
//        DataOutputStream outToClient=null;
        try {
        byte[] replyArray={0,(byte)reply,1,1,1,1,1,1};

            outToClient.write(ByteBuffer.wrap(replyArray));
//            outToClient = new DataOutputStream(clientSocket.getOutputStream());
//            outToClient.write(0);
//            outToClient.write(reply);
//            outToClient.write(1);
//            outToClient.write(1);
//            outToClient.write(1);
//            outToClient.write(1);
//            outToClient.write(1);
//            outToClient.write(1);
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

    public static int Socks4MessageValidator( ConcurrentHashMap<String, Object> map)
    {
        int CD=(byte)map.get("CD");
        if(CD!=1 )
        {
            return 1;
//            System.out.println("wrong Sock4 request - Command isn't supported");
        }
        int VN=(int)(byte)map.get("VN");
        if (VN!=4 )
        {
            return 2;
//            System.out.println("wrong Sock4 request - Wrong version ");
        }
        return 0;
    }
    //for tests perpose
    public  static void Socks4ParserPrinter (ConcurrentHashMap<String, Object> map)
    {

        System.out.println("VN"+map.get("VN"));
        System.out.println("CD"+map.get("CD"));
        System.out.println("DSTPORT"+map.get("DSTPORT"));
        System.out.println("DSTIP"+map.get("DSTIP"));

    }

    public static InetAddress  DomainIPresolve  ( String domain ) throws UnknownHostException
     {
        return InetAddress.getByName(domain);
     }


}





