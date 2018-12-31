///*
//    from the course book:  Computer Networking: A Top Down Approach, by Kurose and Ross
// */
//import java.io.*;
//import java.net.*;
//
//class TCPServer
//{
//    public static void main(String argv[]) throws Exception
//    {
//        String clientSentence;
//        String capitalizedSentence;
//
//        ServerSocket welcomeSocket = new ServerSocket(6789);  // bind + listen
//
//        while(true)
//        {
//            try(Socket connectionSocket = welcomeSocket.accept()) {
//                BufferedReader inFromClient =
//                        new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
//                DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
//
//                clientSentence = inFromClient.readLine();  //read
//                System.out.println("Received: " + clientSentence);
//                capitalizedSentence = clientSentence.toUpperCase() + '\n';
//                outToClient.writeBytes(capitalizedSentence);  // write
//
//                // close (why?)
//            } catch (SocketException e) {
//
//            }
//        }
//    }
//}