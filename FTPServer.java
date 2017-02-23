import com.sun.org.apache.regexp.internal.RE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;

/**
 * Created by sup33 on 12/4/2016.
 */
public class FTPServer {
    ;//= new ServerSocket(port);
    String ipAddress;
    String name;
    ServerSocket aServerSocket = null;

    //aServerSocket = new ServerSocket(port);
    public FTPServer(){

        try {
            aServerSocket = new ServerSocket(3000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String str[]) {
        //System.out.println("In run"+name);

        new FTPServer().start();



    }

    private  void start() {
        System.out.println("Starting with Server");
        while (true) {
            try {
                System.out.println("waiting for connection:");
                Socket controlChannel = aServerSocket.accept();
                System.out.println("Accepted the request");
                //System.out.println(controlChannel.toString());
                new Thread(new authentication(controlChannel)).start();



            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }
}


class authentication implements Runnable{
    Socket sock;
    authentication(Socket sock){
        this.sock=sock;
    }

    @Override
    public void run() {
        String UserName;
        String Password;

        try {
            PrintWriter out = new PrintWriter(sock.getOutputStream(), true);
            out.println("FTP 220:Connection Successful");
            System.out.println("Sent 220");
            //System.out.println(sock.toString());
            BufferedReader input = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            System.out.println("reding the input");
            UserName = input.readLine();
            //System.out.println(sock.toString());
            System.out.println(UserName);
            Password = input.readLine();
            //System.out.println(sock.toString());
            System.out.println(Password);
            String output = checkAuthentication(UserName,Password);
            out.println(output);
            String address = input.readLine();
            System.out.println(address);
            String add[] = address.split(" ");
            System.out.println(add[0]+' '+add[1]);
            out.println("FTP 200"   );

            String cmd = input.readLine();
            Command_server aCommand = new Command_server(add[0], Integer.parseInt(add[1]),UserName);
            while(!cmd.equals("Quit")) {
                System.out.println("cmd=" + cmd);
                String  status=aCommand.execute(cmd);
                out.println(status);
                cmd = input.readLine();

            }

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
        Registry.put("supriya","godge");

        if (Registry.containsKey(userName)){
            if(Registry.get(userName).equals(password))
                return "FTP 230:Authentication Successful";

        }
        return "Authentication failed";
    }
}

