import com.sun.org.apache.regexp.internal.RE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.Hashtable;

/**
 * Created by sup33 on 12/4/2016.
 */
public class TFTPServer {
    ;//= new ServerSocket(port);
    String ipAddress;
    String name;

    //aServerSocket = new ServerSocket(port);


    public static void main(String str[]) {
        //System.out.println("In run"+name);
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(1001);
        } catch (IOException e) {
            e.printStackTrace();
        }
        DatagramPacket packet;
        System.out.println("Starting with Server");
        while (true) {
            try {
                System.out.println("waiting for connection:");
                byte buf[] = new byte[1024];
                String connectionSuccess ="FTP 220:Connection Succsful";
                packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                buf = connectionSuccess.getBytes();
                DatagramPacket packetSend = new DatagramPacket(buf, buf.length, address, port);
                System.out.println("Sending the packets");
                socket.send(packetSend);
                String data = new String(packet.getData());
                System.out.println("data"+data);
                new Thread(new authentication1(socket)).start();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }
}

//
//    public static void main(String str[]){
//        System.out.println("Starting the server");
//        try {
//            FTPServer Server1 = new FTPServer("1");
//            Server1.start();
//            FTPServer Server2 = new FTPServer("2");
//            Server2.start();
//            //FTPServer Server3 = new FTPServer("3");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//}

class authentication1 implements Runnable{
    DatagramSocket sock;
    authentication1(DatagramSocket sock){
        this.sock=sock;
    }

    @Override
    public void run() {
        String UserName;
        String Password;
        DatagramPacket packet;
        byte buf[] = new byte[1024];
        try {
        packet = new DatagramPacket(buf, buf.length);
            sock.receive(packet);
            UserName = new String(packet.getData());
            System.out.println("Username :"+UserName);
        InetAddress address = packet.getAddress();
        int port = packet.getPort();
           String  connectionSuccess = "FTP UserName recevied";
        buf = connectionSuccess.getBytes();
        DatagramPacket packetSend = new DatagramPacket(buf, buf.length, address, port);
        System.out.println("Sending the packets");
        sock.send(packetSend);
            sock.receive(packet);
            Password = new String(packet.getData());
            System.out.println("Password:"+Password);
            String pass = "FTP 220 Password recevied";
            buf = pass.getBytes();
            packetSend = new DatagramPacket(buf, buf.length, address, port);
            sock.send(packetSend);
            String reply=checkAuthentication(UserName,Password);
            buf = reply.getBytes();
            packetSend = new DatagramPacket(buf, buf.length, address, port);
            sock.send(packetSend);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String checkAuthentication(String userName, String password) {
        Hashtable<String,String> Registry = new Hashtable<String,String>();
        Registry.put("kansas.cs.rit.edu","CSRIT");
        Registry.put("glados.cs.rit.edu","CSRIT");
        Registry.put("queek.cs.rit.edu","CSRIT");
        Registry.put("ritvik","joshi");

        if (Registry.containsKey(userName)){
            if(Registry.get(userName).equals(password))
                return "FTP 230:Authentication Successfull";

        }
        return "Authentication failed";
    }
}

