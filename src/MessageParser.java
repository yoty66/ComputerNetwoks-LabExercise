import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ConcurrentHashMap;
import java.util.*;
 class MessageParser {



     static ConcurrentHashMap<String, Object> Socks4Parser(SocketChannel clientSocket) {

        ByteBuffer byteBuffer;
        ConcurrentHashMap<String, Object> map = new ConcurrentHashMap();
        int byteRed=0;
        byte[] valuesVMtoDSTIP;

try
{
        byteBuffer=ByteBuffer.allocate(100);
        byteBuffer.clear();
        byteRed=clientSocket.read(byteBuffer);
         byteBuffer.clear();
        valuesVMtoDSTIP=byteBuffer.array();
        map.put("VN", valuesVMtoDSTIP[0]);

            map.put("CD", (byte)valuesVMtoDSTIP[1]);

    int port=(valuesVMtoDSTIP[2] & 0xFF) << 8 | (valuesVMtoDSTIP[3] & 0xFF);
    map.put("DSTPORT", port);

    String IPvalue = (0xff &valuesVMtoDSTIP[4])+"."+(0xff &valuesVMtoDSTIP[5])
            +"."+(0xff &valuesVMtoDSTIP[6])+"."+(0xff &valuesVMtoDSTIP[7]);
            map.put("DSTIP", IPvalue);

}
        catch (IOException | NullPointerException e)
        {
            System.err.println("Connection error: while parsing SOCKS request:"+e.getMessage());
        }
         return map;
    }

     static void  Socks4ReplyWriter(int reply, SocketChannel outToClient) {
//        DataOutputStream outToClient=null;
        try {
        byte[] replyArray={0,(byte)reply,1,1,1,1,1,1};

            outToClient.write(ByteBuffer.wrap(replyArray));

        } catch (IOException | NullPointerException e) {
            System.err.println("Connection error: while replying SOCKS request:"+e.getMessage());
        }
    }

     static int Socks4MessageValidator( ConcurrentHashMap<String, Object> map)
    {
        int CD=(byte)map.get("CD");
        if(CD!=1 )
        {
            return 1;
        }
        int VN=(int)(byte)map.get("VN");
        if (VN!=4 )
        {
            return 2;

        }
        return 0;
    }

    //for tests perpose
//    public  static void Socks4ParserPrinter (ConcurrentHashMap<String, Object> map)
//    {
//
//        System.out.println("VN"+map.get("VN"));
//        System.out.println("CD"+map.get("CD"));
//        System.out.println("DSTPORT"+map.get("DSTPORT"));
//        System.out.println("DSTIP"+map.get("DSTIP"));
//
//    }

    public static InetAddress DomainIPResolve(String domain ) throws UnknownHostException
     {
        return InetAddress.getByName(domain);
     }


}





