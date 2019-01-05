import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.*;

class ClientThread implements Runnable {
    private SocketChannel clientChannel ;
    private SocketChannel destinationChannel;
    private Selector selector ;
  private SelectionKey keyDestination;
  private SelectionKey keyClient;

    ClientThread(SocketChannel clientChannel) {
        this.clientChannel = clientChannel;


    }
        @Override
        public void run () {
            String clientsIP=null ; //to be used if connection succseed
            String clientsPORT=null;
            try {

                //Parse Socks 4 request

                ConcurrentHashMap<String, Object> map = MessageParser.Socks4Parser(clientChannel);



                // validate request


                // 0 - message is valid 1-"wrong Sock4 request - Command isn't supported" 2-"wrong Sock4 request - Wrong version "
                int validRequest = MessageParser.Socks4MessageValidator(map);

                //handle falsy request

                if (validRequest !=0)
                {
                    System.err.println("Connection error: while Parsing  Socks4 request:"+
                            (validRequest==1?"Command isn't supported":"Unsupported SOCKS protocol version"));
                    return;
                }

                //Establish dst channel and report

                // set some constants
                String DSTIP = (String) map.get("DSTIP");
                int DSTPORT = (int) map.get("DSTPORT");
                clientsIP = "127.0.0.1";
                clientsPORT = (this.clientChannel.getRemoteAddress().toString()).replaceFirst("(.*):(.*)$","$2");

                this.destinationChannel = SocketChannel.open();
                InetSocketAddress destinationAddress = new InetSocketAddress(DSTIP, DSTPORT);

                if (!(destinationChannel.connect(destinationAddress)))
                {
                    System.err.println("Connection error: while connecting to dest: Connection failed  ");
                    return;
                }
                //else
                MessageParser.Socks4ReplyWriter(90, clientChannel);
                System.out.println("Successful connection from " + clientsIP+":"+clientsPORT+ " to "+DSTIP + ":" + DSTPORT);

                //establish selector
                this.establishSelector(clientChannel, destinationChannel);
                //main loop
                while (true) {
                    selector.select();
                    Set<SelectionKey> selectedKeys = selector.selectedKeys();
                    if (selectedKeys.size() != 2) {

                        continue;
                    }
                    //else
                    selectedKeys.clear();
//                    byte[] message;
                    if (this.keyClient.isReadable() && this.keyDestination.isWritable()) {
                        boolean messageHandlerResult = MessageHandler(this.clientChannel, this.destinationChannel, true);
                        if (!messageHandlerResult) {
                            return;//proper message was printed to std err during MessageHandler run
                        }
                    }

                        if (this.keyDestination.isReadable() && this.keyClient.isWritable()) {
                        boolean messageHandlerResult = MessageHandler(this.destinationChannel, this.clientChannel, false);
                        if (!messageHandlerResult)
                        {
                            return;//proper message was printed to std err during MessageHandler run
                        }

                    }
                }

//

                }





            catch(UnresolvedAddressException e){

                        System.err.println("Connection error: while connecting to destination: Unresolved Address ");
                    }
         catch(IOException | NullPointerException e){
                        System.err.println(e.getMessage());
                    }
            finally{
                System.out.println("Closing connection from:  "+clientsIP+":"+clientsPORT);
                        try {
                            if (this.clientChannel != null)
                                this.clientChannel.close();
                            if(this.destinationChannel!=null)
                                this.destinationChannel.close();
                        }
                        catch (IOException e) { }
                    }

            }


        private static boolean MessageHandler(SocketChannel sender ,SocketChannel reciver , boolean isClient) throws IOException
        {
            ByteBuffer x=ByteBuffer.allocate(
                        8192  //max size of an http request
                        +1000);
                x.clear();
                int byteRed=sender.read(x);
                if(byteRed==-1)
                {
                    System.err.println("Connection error: while reading from  "+(isClient?"client":"dest")+": Channel is closed ");
                    return false;
                }

                x.flip();
            String message = new String(Arrays.copyOfRange(x.array(), 0, byteRed));
                if(isClient) {
//                    TODO: ADD the 'spy' element.

                }
//                System.out.println("message:"+message);
                int bytewritten=reciver.write(x);
                x.clear();
                if(bytewritten!=message.length())
                {
                    System.err.println("Connection error: while writing to  "+(isClient?"dest":"client")+": Channel is closed ");
                    return false;
                }

            return true;
        }

        //constant-values  for SelectionKey :  OP_READ-1  	OP_WRITE-4  	OP_CONNECT	8 OP_ACCEPt-16
        private void establishSelector (SocketChannel clientChannel,SocketChannel destinationChannel) throws IOException
        {
            Selector selector = Selector.open();

            clientChannel.configureBlocking(false);
            destinationChannel.configureBlocking(false);

            SelectionKey keyClient= clientChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            keyClient.attach("client");
            SelectionKey keyDestination = destinationChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            keyDestination.attach("dst");
            this.selector= selector;
            this.keyDestination=keyDestination;
            this.keyClient=keyClient;
        }


    }



class Sockspy {
    public static void main(String argv[])
    {
        try {
            ServerSocketChannel welcomeSocket = ServerSocketChannel.open();
            welcomeSocket.bind(new InetSocketAddress(8080));
            ExecutorService executor = Executors.newFixedThreadPool(20);
            boolean connect=true;
            while (connect) {
                SocketChannel clientSocket = welcomeSocket.accept();
                Runnable worker = new ClientThread(clientSocket);
                executor.execute(worker);
            }
        }
        catch (IOException e)
        {
            System.err.println("Connection error: while writing to connecting to client: "+e.getMessage());
        }
    }
}
