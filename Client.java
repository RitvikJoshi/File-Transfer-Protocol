
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.*;

public class Client {
    static String ipaddr="localhost";//"10.182.115.253";
    static int port=1001;
    public static void main(String args[]){
        Client cl = new Client();
        Scanner in= new Scanner(System.in);
        System.out.println("Running program");
        DatagramPacket packet;
        try {
            byte buf[] = new byte[1024];
            InetAddress aInetAddress = InetAddress.getByName(ipaddr);
            DatagramPacket receviepacket = new DatagramPacket(buf, buf.length);
            DatagramSocket socket = new DatagramSocket();
            packet = new DatagramPacket(buf, buf.length, aInetAddress, port);
            socket.send(packet);
            socket.receive(receviepacket);
            String data = new String(receviepacket.getData());
            System.out.println(data);
            System.out.println("Enter username::");
            String name = in.nextLine();
            buf = name.getBytes();
            packet = new DatagramPacket(buf, buf.length, aInetAddress, port);
            socket.send(packet);
            socket.receive(receviepacket);
            data = new String(receviepacket.getData());
            System.out.println(data);
            System.out.println("Enter password::");
            String pass = in.nextLine();
            buf = pass.getBytes();
            packet = new DatagramPacket(buf, buf.length, aInetAddress, port);
            socket.send(packet);
            socket.receive(receviepacket);
            data = new String(receviepacket.getData());
            System.out.println(data);





        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}