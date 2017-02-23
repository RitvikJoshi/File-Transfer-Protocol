import java.util.*;
import java.io.*;
import java.net.Socket;

public class Command_server{

    String Client_Ipaddr;
    int Client_port;
    String user;
    String current_path;
    static Hashtable<String,String> file_dir = new Hashtable<String,String>();


    static{
        file_dir.put("ritvik", "C:\\Users\\sup33\\Desktop\\filesystem\\ritvik");
        file_dir.put("supriya", "C:\\Users\\sup33\\Desktop\\filesystem\\supriya");
    }



    public Command_server(String Ipaddr, int port, String user){
        this.Client_Ipaddr=Ipaddr;
        this.Client_port=port;
        this.user=user;
        System.out.println("User"+user);
        if(file_dir.containsKey(user)){
            this.current_path= file_dir.get(user);
            System.out.println("Current path"+current_path);
        }

    }

    public String execute(String cmd){
        System.out.println("In execite"+cmd);
        String [] cmd_array = cmd.split(" ");
        String filename;
        switch(cmd_array[0]){

            case "mkdir" :
                if(cmd_array.length>1){
                    System.out.println("Creating directory");
                    filename = cmd_array[1];
                    System.out.println("fileName="+filename);
                    MKDIR mkd = new MKDIR(current_path,filename);
                    mkd.create_directory();
                    return "Successfully created ";
                }
                else{
                    return "Failed: Command was wrong";
                }

            case "cd":
                if(cmd_array.length>1){
                    filename = cmd_array[1];
                }

        }




        return "Failed:Command not found:"+cmd_array[0];


    }



}

class MKDIR{

    String path;
    String filename;

    MKDIR(String path, String Filename){
        this.path =path;
        this.filename =Filename;
    }

    public boolean create_directory(){
        try{
            System.out.println(path+"  fileName="+filename);
            File dir = new File(path, filename);
            System.out.println("  dir"+dir);
            boolean ans =dir.mkdir();
            String statement ="Directory created successfully";
            System.out.println(statement+ans);
            return ans;
        }catch(Exception e){
            System.out.println(e);
            return false;
        }
    }


}

//class CD{
//
//	String path;
//	String filename;
//	
//	CD(String path, String filename){
//		this.path= path;
//		this.filename=filename;
//	}
//	
//	public boolean change_directory(){
//		
//		if(filename.equals("..")){
//			
//		}else{
//			current_path = this.path+
//		}
//		
//		return true;
//	}
//}