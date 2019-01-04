import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.*;

class EchoRunnable implements Runnable {
    private SocketChannel clientSocket ;

    public EchoRunnable(SocketChannel clientSocket) {
        this.clientSocket = clientSocket;
//        try {
//            this.clientSocket.setSoTimeout(500);
//        } catch (SocketException e) {
//        }

    }
        @Override
        public void run () {
            System.out.println("connected");
            try
//                    (
//                    BufferedReader inFromClient =
//                            new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//                    DataOutputStream outToClient = new DataOutputStream(clientSocket.getOutputStream());
//                    InputStream inStream=clientSocket.getInputStream()
//            )
            {

//                if((int)map.get("CD")!=1  ||(int)map.get("VN")!=4  )
//                {
//                    System.out.println("wrong Sock4 request");
//                }

//                else
//                {
//                    MessageParser.Socks4ReplyWriter(90,outToClient);
//                    System.out.println(new String(MessageParser.HttpRequestParser(clientSocket)));
//
//                }

                //Establish Client channel

                //Parse Socks 4 request
                ConcurrentHashMap<String, Object> map =MessageParser.Socks4Parser(clientSocket);
                MessageParser.Socks4ParserPrinter(map);
                // validate request

                // 0 - message is valid 1-"wrong Sock4 request - Command isn't supported" 2-"wrong Sock4 request - Wrong version "
               int validrequest= MessageParser.Socks4MessageValidator(map);

                //handle falsy request

                if(validrequest!=0 )
                {

                }
                //Establish dst channel
//                SocketChannel socketChannel = SocketChannel.open();
//                socketChannel.connect(new InetSocketAddress("http://jenkov.com", 80));
                //establish selector
                //close when one terminates

}
//         catch(IOException e){
//                System.err.println(e.getMessage());
//            }
            finally{
                try {
                    if(this.clientSocket!=null)
                    this.clientSocket.close();
                } catch (IOException e) {
                }
            }
        }
    }


class Sockspy {
    public static void main(String argv[]) throws Exception {
        ServerSocketChannel welcomeSocket = ServerSocketChannel.open();
        welcomeSocket.bind(new InetSocketAddress(8080));
//                new ServerSocketChannel(8080);  // bind + listen
        ExecutorService executor = Executors.newFixedThreadPool(20);

        while (true)
        {
            SocketChannel clientSocket = welcomeSocket.accept();

//            SocketChannel clientChannel=SocketChannel.open();
//             clientChannel =clientSocket.getChannel();
            boolean empty =clientSocket==null;
            System.out.println("clientChannel is empty: "+empty);
            Runnable worker = new EchoRunnable(clientSocket);
            executor.execute(worker);
        }
        // executor.shutdown();
    }
}
