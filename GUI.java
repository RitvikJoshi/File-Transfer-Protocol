import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;

import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.stage.Screen;
import javafx.stage.Stage;
import sun.awt.AWTAccessor;

import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 *  Name:Supriya Godge and Ritvik Joshi
 *  This is the FTP client program which gives the option of graphical interface
 *  and command line
 *
 */
public class GUI extends Application {
    /*
    This is the main class with graphical strcucture
     */
    static String ipaddr;//"10.182.115.120";//"10.182.115.253";
    static int port21=3000;
    static int clientPort=5000;
    static int port20 = 2000;
    static PrintWriter d_out;
    static PrintWriter d_out_20;
    static Socket cmd;
    static ServerSocket cmd_20;
    static BufferedReader d_in;
    static BufferedReader d_in_20;
    static String dir="";
    String name;

    public String nextLine() {
        // The start of new line
         return "\n" + dir + ">> ";


    }


    public void init(){
        /*
            To intialize the graphical interface and to establish the initial connection
         */
        try {
            System.out.println("Enter the ip address of the server");
            Scanner scan = new Scanner(System.in);
            ipaddr = scan.nextLine();
            super.init();
            cmd = new Socket(ipaddr,port21);
            cmd_20 = new ServerSocket(clientPort);
            System.out.println("Connection Established");
             d_in =  new BufferedReader(new InputStreamReader(cmd.getInputStream()));
             d_out = new PrintWriter(cmd.getOutputStream(),true);
             d_in_20 =  new BufferedReader(new InputStreamReader(cmd.getInputStream()));
             d_out_20 = new PrintWriter(cmd.getOutputStream(),true);
            System.out.println("packet sent");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start(Stage primaryStage) throws Exception {
        authenticationScreen(primaryStage);
    }

   public String checkAuthentication(String name, String pass){
       // To perform the initial authentication
        String data ="";
       try {
           String inp= d_in.readLine();
           System.out.println("before sending name"+inp);
           d_out.println(name);
           dir+=name;
           //inp= d_in.readLine();
           //System.out.println("before sending pass"+inp);
           d_out.println(pass);
           data= d_in.readLine();
           //System.out.println(inp);
           //System.out.println(inp);
       } catch (IOException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
       }
       return data;
   }

    private void authenticationScreen(Stage primaryStage) {
        /*
            To handle the authenticaltion of the user
         */
        Stage theprimary = primaryStage;
        primaryStage.setTitle("FTP");
        GridPane grid = new GridPane();

        grid.setPadding(new Insets(50, 50, 50, 50));
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        Label aLabal = new Label("FTP");
        Label aLebal1 = new Label("User Name");
        Label aLebal2 = new Label("Password");
        grid.add(aLabal,0,0);
        grid.add(aLebal1,0,1);
        grid.add(aLebal2,0,2);
        TextField aUserName = new TextField();
        PasswordField aPassword = new PasswordField ();
        grid.add(aUserName,1,1);
        grid.add(aPassword,1,2);
        Button btn = new Button("Sign in");

        btn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {

                name = aUserName.getText();
                String pass = aPassword.getText();
                System.out.println("Name"+name);
                System.out.println("pass"+pass);
                String ans =checkAuthentication(name,pass);
                System.out.println("ans"+ans);
                if (ans.contains("230")){
                    System.out.println("page change");
                    finalScreen(theprimary,name);
                }
               System.out.println(ans);
            }
        });
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 3);

        Scene scene = new Scene(grid);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    private void finalScreen(Stage primaryStage,String user){
        /*
                This method handles all the commands requested by user and sendds it to the
        FTP server
         */
        System.out.println("inside");
        primaryStage.setTitle("FTP Connected:"+user);
        Screen aScreen = Screen.getPrimary();
        Rectangle2D b = aScreen.getVisualBounds();
        primaryStage.setWidth(b.getWidth());
        primaryStage.setHeight(b.getHeight());
        primaryStage.setX(b.getMinX());
        primaryStage.setY(b.getMinY());
        TextField file = new TextField();
        BorderPane aFlow =new BorderPane();
        aFlow.setBackground(new Background(new BackgroundFill(Color.rgb(255, 255, 255), CornerRadii.EMPTY, Insets.EMPTY)));

        aFlow.setPadding(new Insets(20,20,20,20));
        aFlow.setPrefHeight(900);
        aFlow.setPrefWidth(1200);
        TextArea output = new TextArea();
        output.appendText(name+">>");
        //output.setEditable(false);
        output.setPrefWidth(150);
        output.setPrefHeight(500);

        aFlow.setCenter(output);
        VBox aVBox = new VBox();
        aVBox.setPadding(new Insets(20,20,20,20));
        aVBox.setSpacing(5);
        Button CD = new Button("Change Directory");
        //CD.setBackground(new Backgr;ound(new BackgroundFill(Color.rgb(200,200,200), CornerRadii.EMPTY, Insets.EMPTY)));
        CD.setPrefSize(200, 50);
        Button UP = new Button("Make directory");
        UP.setPrefSize(200, 50);
        Button Open = new Button("Read file");
        Open.setPrefSize(200, 50);
        Button download = new Button("Download file");
        Label fileN = new Label("Enter file Name");
        fileN.setPrefSize(200,5);
        download.setPrefSize(200, 50);
        Button ls = new Button("LS");
        ls.setPrefSize(200, 50);

        download.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    if (!file.getText().equals(""))
                    {
                        d_out.println("get "+file.getText());
                        d_out.println(InetAddress.getLocalHost().getHostAddress()+" "+clientPort);
                        Socket getData = cmd_20.accept();
                        BufferedReader input1 = new BufferedReader(new InputStreamReader(getData.getInputStream()));
                        String data = input1.readLine();
                        PrintWriter writer = new PrintWriter("C:\\Users\\sup33\\Downloads\\"+file.getText(), "UTF-8");
                        writer.println(data);
                        writer.close();
                        file.setText("");
                        System.out.println(data);
                        String statis=d_in.readLine();
                        getData.close();
                    }
                    else {
                        file.setText("<<Enter file name here >>");
                    }

                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        Open.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    String fileName = file.getText();
                    System.out.println("File name="+fileName);
                    if (!fileName.equals("")) {
                        d_out.println("read " +fileName);
                        d_out.println(InetAddress.getLocalHost().getHostAddress() + " " + clientPort);
                        Socket getData = cmd_20.accept();
                        BufferedReader input1 = new BufferedReader(new InputStreamReader(getData.getInputStream()));
                        String data;
                        while ((data = input1.readLine()) != null) {
                            output.appendText("\n" + data);
                            System.out.println(data);

                        }
                        file.appendText("");
                        String statis=d_in.readLine();
                        getData.close();
                    }
                    else {
                        file.appendText("<<Enter the file name here>>");
                    }
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        ls.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    d_out.println("ls");
                    d_out.println(InetAddress.getLocalHost().getHostAddress()+" "+clientPort);
                    Socket getData = cmd_20.accept();
                    BufferedReader input1 = new BufferedReader(new InputStreamReader(getData.getInputStream()));
                    String lsList = input1.readLine();
                    output.appendText(nextLine()+lsList);
                    output.appendText(nextLine());
                    String statis=d_in.readLine();
                    getData.close();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        CD.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {

                    dir+="/"+file.getText();
                    d_out.println("CD "+file.getText());
                    String data= d_in.readLine();
                    //output.appendText(nextLine+data);
                    //output.appendText(nextLine);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        Label alabel = new Label("Enter the file Name ");

        file.setPrefSize(200,50);
        aVBox.setSpacing(50);
        aVBox.getChildren().add(CD);
        UP.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {

                try {

                    String fileName = file.getText();
                    if(!fileName.equals("")) {
                        //String data= d_in.readLine();
                        d_out.println("mkdir " + fileName);
                        String data = d_in.readLine();
                        //output.appendText(nextLine + data);
                        output.appendText(nextLine());
                        file.setText("");
                    }
                    else {
                        file.appendText("<<Enter the file name here>>");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        VBox bVbox = new VBox();
        bVbox.getChildren().add(fileN);
        bVbox.getChildren().add(file);
        aVBox.getChildren().add(UP);
        aVBox.getChildren().add(Open);
        aVBox.getChildren().add(ls);
        aVBox.getChildren().add(download);
        aVBox.getChildren().add(bVbox);
        aFlow.setLeft(aVBox);

        Button submit = new Button("Submit");
        submit.setMaxWidth(200);
        submit.setPadding(new Insets(0,40,0,20));
        HBox aHox = new HBox();
        aHox.getChildren().add(submit);
        submit.setPrefSize(300, 30);
        aHox.setAlignment(Pos.CENTER);
        aFlow.setBottom(aHox);

        aFlow.setCenter(output);
        Scene scene1 = new Scene(aFlow);
        primaryStage.setScene(scene1);
        output.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.ENTER))
                {

                    String commandsLines[] = output.getText().split("\n");
                    String cmd = commandsLines[commandsLines.length-1] ;
                    cmd =cmd.replace(dir+">>","");
                    cmd = cmd.trim();
                    System.out.println("current cmd is:"+cmd);
                    String cmdList[] = cmd.split(" ");
                    //d_out.println(cmd);
                    if(cmdList[0].equals("put")) {
                        try{
                            d_out.println(cmd);
                            Thread.sleep(7000);
                            Socket sock= new Socket(ipaddr,2000);

                            File getFile = new File(cmdList[1]);
                            BufferedReader f_in=  new BufferedReader(new FileReader(getFile));
                            PrintWriter f_out = new PrintWriter(sock.getOutputStream(),true);
                            //DataOutputStream out = new DataOutputStream(sock.getOutputStream());
                            int eof=0;
                            byte buffer[] =new byte[1024];
                            String file_inp;
                            String inp="";
                            while((file_inp=f_in.readLine())!=null){
                                inp+= file_inp+"\n";
                            }
                            //inp = inp.replace("\n","\t");
                            inp+=file_inp;
                            System.out.println(inp);
                            f_out.println(inp);
                            System.out.println("reply"+d_in.readLine());
                            sock.close();
                        }
                        catch (FileNotFoundException e){
                            output.appendText(nextLine()+"ERROR: Mentioned file is not present");
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                    if (cmdList[0].equals("ls")) {
                        try {
                            d_out.println("ls");
                            d_out.println(InetAddress.getLocalHost().getHostAddress() + " " + clientPort);
                            Socket getData = cmd_20.accept();
                            BufferedReader input1 = new BufferedReader(new InputStreamReader(getData.getInputStream()));
                            String lsList = input1.readLine();
                            output.appendText(nextLine() + lsList);
                            String statis = d_in.readLine();
                            getData.close();
                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                    if (cmdList[0].equals("get")) {
                        try {
                            d_out.println(cmd);
                            d_out.println(InetAddress.getLocalHost().getHostAddress() + " " + clientPort);
                            Socket getData = cmd_20.accept();
                            BufferedReader input1 = new BufferedReader(new InputStreamReader(getData.getInputStream()));
                            String data = input1.readLine();
                            PrintWriter writer = new PrintWriter("C:\\Users\\sup33\\Downloads\\" + cmdList[1], "UTF-8");
                            writer.println(data);
                            writer.close();
                            System.out.println(data);
                            String statis = d_in.readLine();
                            getData.close();
                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                    if (cmdList[0].equals("cdup")) {
                        try {

                            d_out.println(cmd);
                            dir = name;
                            String data = d_in.readLine();
                            //output.appendText(nextLine+data);
                            //output.appendText(nextLine);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                    if (cmdList[0].equals("read")) {
                        try {
                            d_out.println(cmd);
                            d_out.println(InetAddress.getLocalHost().getHostAddress() + " " + clientPort);
                            Socket getData = cmd_20.accept();
                            BufferedReader input1 = new BufferedReader(new InputStreamReader(getData.getInputStream()));
                            String data;
                            while ((data = input1.readLine()) != null) {
                                output.appendText("\n" + data);
                                System.out.println(data);

                            }
                            String statis = d_in.readLine();
                            getData.close();
                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                    if (cmdList[0].equals("cd") && cmdList.length > 1) {
                        try {

                            d_out.println(cmd);
                            dir += "/" + cmdList[1];
                            String data = d_in.readLine();
                            //output.appendText(nextLine+data);
                            //output.appendText(nextLine);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (cmdList[0].equals("help")) {
                        String help = "Below commands are avilable:\n" +
                                "1.cd         : To change the directory [cd <<name of the directory>>]\n" +
                                "2.cdup     : To go to the root directory\n" +
                                "3.ls          : To list the all files in current directory\n" +
                                "4.get        : To download the file [get <<name of the file>>]\n" +
                                "5.read      : To get the data in the file [read <<name of the file>>]\n" +
                                "6.delete   : To delete the specified file\n" +
                                "7.rmdir    : To delete the directory";
                        output.appendText(nextLine() + help);
                    }

                    if (cmdList[0].equals("mkdir")) {
                        try {

                            //String data= d_in.readLine();
                            d_out.println(cmd);
                            String data = d_in.readLine();
                            output.appendText(nextLine() + data);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    if (cmdList[0].equals("delete")) {
                        try {

                            //String data= d_in.readLine();
                            d_out.println(cmd);
                            String data = d_in.readLine();
                            output.appendText(nextLine() + data);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (cmdList[0].equals("rmdir")) {
                        try {

                            //String data= d_in.readLine();
                            d_out.println(cmd);
                            String data = d_in.readLine();
                            output.appendText(nextLine() + data);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    output.appendText(nextLine());
                    event.consume();
                }




                }
            });

        primaryStage.show();


    }



    public static void main(String str[]){
        Application.launch();
    }
}



