import java.io.*;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
//                outToClient.write(0);
//                outToClient.write(90);
//
//                outToClient.write(1);
//                outToClient.write(1);
//                outToClient.write(1);
//                outToClient.write(1);
//                outToClient.write(1);
//                outToClient.write(1);
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
