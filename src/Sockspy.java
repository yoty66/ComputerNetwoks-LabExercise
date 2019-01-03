import java.io.*;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.*;

class EchoRunnable implements Runnable {
    private Socket clientSocket = null;

    public EchoRunnable(Socket clientSocket) {
        this.clientSocket = clientSocket;
//        try {
//            this.clientSocket.setSoTimeout(500);
//        } catch (SocketException e) {
//        }

    }
        @Override
        public void run () {
            System.out.println("connected");
            byte clientbyte;
            String capitalizedSentence;
            try (
                    BufferedReader inFromClient =
                            new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    DataOutputStream outToClient = new DataOutputStream(clientSocket.getOutputStream());
                    InputStream inStream=clientSocket.getInputStream()
            )
            {
                ConcurrentHashMap<String, Object> map =MessageParser.Socks4Parser(clientSocket);
                MessageParser.Socks4ParserPrinter(map);
                if((int)map.get("CD")!=1  ||(int)map.get("VN")!=4  )
                {
                    System.out.println("wrong Sock4 request");
                }

                else
                {

                    int [] test={0,1,2,3};

//                    System.out.println(Arrays.copyOfRange(test, 0, 4).length+"|||||||"+Arrays.copyOfRange(test, 0, 4)[3]);

                    MessageParser.Socks4ReplyWriter(90,outToClient);
                    System.out.println(new String(MessageParser.HttpRequestParser(clientSocket)));
//                    System.out.println("got here");
                }


//                while ((clientbyte = (byte)inStream.read()) != 0)
//                {
//                    System.out.println(clientbyte);
//                }
//                System.out.println("end stream 1");
//                while ((clientbyte = (byte)inStream.read()) != 0)
//                {
//                    System.out.println(clientbyte);
//                }
//                System.out.println("end stream 2");
//
//
//
//                while ((capitalizedSentence = inFromClient.readLine()) != null)
//
//                {
//                    System.out.println(capitalizedSentence);
//                }

}
         catch(IOException e){
                System.err.println(e.getMessage());
            }
            finally{
                try {
                    this.clientSocket.close();
                } catch (IOException e) {
                }
            }
        }
    }


class Sockspy {
    public static void main(String argv[]) throws Exception {
        ServerSocket welcomeSocket = new ServerSocket(8080);  // bind + listen
        ExecutorService executor = Executors.newFixedThreadPool(20);

        while (true) {
            Socket clientSocket = welcomeSocket.accept();
            Runnable worker = new EchoRunnable(clientSocket);

            executor.execute(worker);
        }
        // executor.shutdown();
    }
}
