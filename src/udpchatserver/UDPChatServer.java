package udpchatserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Johnathan Tripp (╯°□°）╯︵ ┻━┻
 */
public class UDPChatServer {

    DatagramSocket socket = null;
    InetAddress addr;
    InetAddress IPAddress;
    int port = 9876;
    boolean running = true;
    Thread sendThread;
    
    public UDPChatServer(){}
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        UDPChatServer s = new UDPChatServer();
        s.run();
    }
    
    public void run(){
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("--------- Server  Started ---------");
        System.out.println("---Type quit to stop the server.---");
        
        try{     
            socket = new DatagramSocket(port);
            addr = InetAddress.getLocalHost();
            String hostName = addr.getHostName();
            String hostAddr = addr.getHostAddress();
            System.out.println("\nHostname: "+hostName+"\nHost Address: "+hostAddr+"\nPort: "+port); //print info about //ip address, port, and host name
            byte[] incoming = new byte[1024];
                
                Thread recvThread = new Thread(new RecvMsg(socket, incoming, input));
                recvThread.start();
            while(running){
//                byte[] incoming = new byte[1024];
//                Thread sendThread = new Thread(new SendMsg(input, socket, IPAddress));
//                Thread recvThread = new Thread(new RecvMsg(socket, incoming));
//                sendThread.start();
//                recvThread.start();
                //Uncomment the following lines and comment out the Thread lines to run a different implementation. Do this for the Client as well.
//                DatagramPacket incomingPacket = new DatagramPacket(incoming,incoming.length);
//                socket.receive(incomingPacket);
//                String message = new String(incomingPacket.getData());
//                System.out.println("Client: " + message);
//                if(message.toLowerCase().trim().equals("quit")){
//                    running = false;
//                    continue;
//                }
//                IPAddress = incomingPacket.getAddress();
//                int port = incomingPacket.getPort();
//                String response = input.readLine();
//                byte[] data = response.getBytes();
//                DatagramPacket replyPacket = new DatagramPacket(data , data.length , IPAddress, port);
//                socket.send(replyPacket);
//                System.out.println("Message Sent.");
//                if(response.toLowerCase().trim().equals("quit")){
//                    running = false;
//                }
            }
            socket.close();
            System.out.println("Connection Ended.");
        
        } catch(SocketException e){
            e.printStackTrace();
        } catch(IOException i){
            i.printStackTrace();
        }
    }   
    
    public class SendMsg implements Runnable {

        BufferedReader input;
        DatagramSocket socket;
        InetAddress IPAddress;
        
        public SendMsg(BufferedReader input, DatagramSocket socket, InetAddress IPAddress){
            this.input = input;
            this.socket = socket;
            this.IPAddress = IPAddress;
        }
        
        @Override
        public void run() {
            try{
                String msg = input.readLine();
                byte[] data = msg.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(data , data.length , IPAddress , port);
                socket.send(sendPacket);
                System.out.println("Message Sent."); 
                if(msg.toLowerCase().trim().equals("quit")){
                    running = false;
                }
                Thread.sleep(1000);
            } catch(IOException e){
                e.printStackTrace();
            } catch (InterruptedException ex) {
                Logger.getLogger(UDPChatServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public class RecvMsg implements Runnable {

        DatagramSocket socket;
        byte[] incoming;
        BufferedReader input;
        
        public RecvMsg(DatagramSocket socket, byte[] incoming, BufferedReader input){
            this.socket = socket;
            this.incoming = incoming;
            this.input = input;
        }
        
        @Override
        public void run() {
            try{
                DatagramPacket incomingPacket = new DatagramPacket(incoming, incoming.length);
                socket.receive(incomingPacket);
                IPAddress = incomingPacket.getAddress();
                port = incomingPacket.getPort();
                String response = new String(incomingPacket.getData());
                System.out.println("Client: " + response);
                if(response.toLowerCase().trim().equals("quit")){
                    running = false;
                }
                sendThread = new Thread(new SendMsg(input, socket, IPAddress));
                sendThread.start();
                Thread.sleep(2000);
            } catch(IOException e){
                e.printStackTrace();
            } catch (InterruptedException ex) {
                Logger.getLogger(UDPChatServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
}
