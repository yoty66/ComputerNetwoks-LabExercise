import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.*;

class EchoRunnable implements Runnable {
    private SocketChannel clientChannel ;
    private SocketChannel destinationChannel;
    private Selector selector ;
  private SelectionKey keyDestination;
  private SelectionKey keyClient;
    private int Tnumber;

    public EchoRunnable(SocketChannel clientChannel,int Tnumber) {
        this.clientChannel = clientChannel;
        this.Tnumber=Tnumber;

//        try {
//            this.clientChannel.setSoTimeout(500);
//        } catch (SocketException e) {
//        }

    }
        @Override
        public void run () {
            System.out.println("connected");
            try
            {


//                {
//                    MessageParser.Socks4ReplyWriter(90,outToClient);
//                    System.out.println(new String(MessageParser.HttpRequestParser(clientSocket)));
//
//                }

                //Establish Client channel

                //Parse Socks 4 request
                ConcurrentHashMap<String, Object> map =MessageParser.Socks4Parser(clientChannel);
                MessageParser.Socks4ParserPrinter(map);
                // validate request

                // 0 - message is valid 1-"wrong Sock4 request - Command isn't supported" 2-"wrong Sock4 request - Wrong version "
               int validrequest= MessageParser.Socks4MessageValidator(map);

                //handle falsy request

                if(validrequest!=0 )
                {

                }
                //Establish dst channel
                this.destinationChannel  = SocketChannel.open();
                InetSocketAddress destinationAdress=new InetSocketAddress(
                        (String)map.get("DSTIP"),
                        (int)map.get("DSTPORT"));

                if((destinationChannel.connect(destinationAdress))==true)
                {
                    MessageParser.Socks4ReplyWriter(90,clientChannel);
                }


                System.out.println("destenationSocket adress:"+destinationAdress.getAddress());
                //establish selector
               this.establishSelector(clientChannel,destinationChannel);

               //main loop
                while (true)
                {
                    selector.select();
                    Set<SelectionKey> selectedKeys = selector.selectedKeys();
                 if(selectedKeys.size()!=2)
                    {
                     continue;
                    }
                    //else
                    selectedKeys.clear();
                   if(this.keyClient.isReadable()&&this.keyDestination.isWritable())
                   {
                       byte [] message=MessageParser.HttpRequestParser((SocketChannel)keyClient.channel());
                            String messageString=new String(message);
                            System.out.println("message from "+keyClient.attachment()+":\n"+messageString);
                            //The "spy" effect

                                //TODO add the usr & pass fetcher


                           int numberOfBytesWritten= ((SocketChannel)this.keyDestination.channel()).write(ByteBuffer.wrap(message));
                           if(numberOfBytesWritten!=message.length)
                           {
                               System.out.println("reading less");
                               int x=4;
                               //TODO information leak exception
                           }

                   }

                    if(this.keyDestination.isReadable()&&this.keyClient.isWritable())
                    {
                        byte [] message=MessageParser.HttpRequestParser((SocketChannel)keyDestination.channel());
                        String messageString=new String(message);
                        System.out.println("message from "+keyDestination.attachment()+":\n"+messageString);
                        //The "spy" effect

                        //TODO add the usr & pass fetcher


                        int numberOfBytesWritten= ((SocketChannel)this.keyClient.channel()).write(ByteBuffer.wrap(message));
                        if(numberOfBytesWritten!=message.length)
                        {
                            System.out.println("reading less");
                            int x=4;
                            //TODO information leak exception
                        }

                    }








//                    selector.select();
//                    Set<SelectionKey> selectedKeys = selector.selectedKeys();
//                    Iterator<SelectionKey> iter = selectedKeys.iterator();
//                    while (iter.hasNext())
//                    {
//                        SelectionKey key = iter.next();
//                        boolean isClientKey=key.attachment()=="client";
//                        SelectionKey oppositeKey=isClientKey?this.keyDestination:this.keyClient;
//                        if (key.isReadable()&& oppositeKey.isWritable())
//                        {
//                            byte [] message=MessageParser.HttpRequestParser((SocketChannel)key.channel());
//                            String messageString=new String(message);
//                            System.out.println("message from "+key.attachment()+":\n"+messageString);
//                            //The "spy" effect
//                            if(isClientKey)
//                            {
//                                //TODO add the usr & pass fetcher
//                            }
//
//                           int numberOfBytesWritten= ((SocketChannel)oppositeKey.channel()).write(ByteBuffer.wrap(message));
//                           if(numberOfBytesWritten!=message.length)
//                           {
//                               System.out.println("reading less");
//                               int x=4;
//                               //TODO information leak exception
//                           }
//                            iter.remove();
//                        }
//
//                    }
                }


                //close when one terminates

}



//TODO add UnresolvedAddressException exeception

            catch( UnresolvedAddressException e){

                System.err.println("Connection error: while connecting to destination: Unresolved Address ");
            }
         catch(IOException e){
                System.err.println(e.getMessage());
            }
            finally{
                try {
                    if(this.clientChannel!=null)
                    this.clientChannel.close();
                } catch (IOException e) {
                }
            }
        }


        //constant-values  for SelectionKey :  OP_READ-1  	OP_WRITE-4  	OP_CONNECT	8 OP_ACCEPt-16
        private void establishSelector (SocketChannel clientChannel,SocketChannel destinationChannel) throws IOException
        {
            Selector selector = Selector.open();

            clientChannel.configureBlocking(false);
            destinationChannel.configureBlocking(false);

            SelectionKey keyClient= clientChannel.register(selector, 5);
            keyClient.attach("client");
            SelectionKey keyDestination = destinationChannel.register(selector, 5);
            keyDestination.attach("dst");
//            System.out.println(keyDestination.selector().);
            System.out.println("keyClient:"+( keyClient.interestOps()==5));
            System.out.println("keyDestination:"+(keyDestination.interestOps()==5));
            this.selector= selector;
            this.keyDestination=keyDestination;
            this.keyClient=keyClient;
        }


//    // test: stop excepting after 20 concurrent connections are established
//    @Override
//    public void run () {
//        System.out.println("Thread number:"+this.Tnumber);
//        while(true)
//        {
//
//        }
//    }
    }



class Sockspy {
    public static void main(String argv[]) throws Exception {
        System.out.println("helo");
//        System.out.println("googls IP:"+MessageParser.DomainIPresolve("www.google.com"));

        ServerSocketChannel welcomeSocket = ServerSocketChannel.open();
        welcomeSocket.bind(new InetSocketAddress(8080));
//                new ServerSocketChannel(8080);  // bind + listen
        ExecutorService executor = Executors.newFixedThreadPool(20);
        int Tnember=1;
        while (true)
        {
            SocketChannel clientSocket = welcomeSocket.accept();


            boolean empty =clientSocket==null;
//            System.out.println("clientChannel is empty: "+empty);
            Runnable worker = new EchoRunnable(clientSocket,Tnember);
            Tnember++;
            executor.execute(worker);
        }
        // executor.shutdown();
    }
}
