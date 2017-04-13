import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;

public class MainServer {
	static ServerSocket master;
	static Hashtable<String,Socket> table = new Hashtable<String,Socket>();
	static ArrayList<String> arrayL = new ArrayList<String>();
	
	public MainServer() throws IOException{
		System.out.println("MASTER SERVER\n");
		master = new ServerSocket(5999);
		repeat();
	}

	public static void repeat() throws IOException{
		while(true) {
			Socket socket_connect = master.accept();
			Socket socket = socket_connect;
			BufferedReader client_in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			new Thread(new Runnable(){
				public void run() {
					try{
						//System.out.println("I have acquired a new server");
						while(socket.isConnected()){
							String el3awazan = client_in.readLine();
							String [] meen = el3awazan.split("_");
							//System.out.println("Current sentence is "+meen[0]);

							if(meen[0].equals("Join")){
								table.put(meen[1]+"", socket_connect);
								arrayL.add(meen[1]+"");
								//System.out.println("New server added as "+meen[1]);
							}

							// Returning list of members from specific server
							if(meen[0].equals("getMembers")){

								PrintWriter c = new PrintWriter(table.get(meen[1]).getOutputStream(),true);
								//System.out.println("fetchMembers_"+meen[2]+" from "+meen[1]);
								c.println("fetchMembers_" + meen[2] +"_"+meen[3]);
								
							}

							if(meen[0].equals("members")){
								PrintWriter ca = new PrintWriter(table.get(meen[3]).getOutputStream(),true);
								//System.out.println("members_"+meen[1]+"_"+meen[2]);
								ca.println("members_"+meen[1]+"_"+meen[2]);
							}
								// Used to chat
							else if(meen[0].equals("chat")){
								//System.out.println(meen[0]+" "+meen[1]+" "+meen[2]+" "+meen[3]+" "+meen[4]);
								PrintWriter x = new PrintWriter(table.get(meen[5]).getOutputStream(),true);
								x.println("chat_"+meen[1]+"_"+meen[2]+"_"+meen[3]);
							}
							else if(meen[0].equals("check")){
								for(int i=0;i<table.size();i++){
									PrintWriter x = new PrintWriter(table.get(arrayL.get(i)).getOutputStream(),true);
									x.println("check");
								}
							}
						}
					}catch(Exception e){
						//System.out.println("Someone probably left");
						e.printStackTrace();
					}
				}
			}).start();
		}
	}

	public static void main(String[] args) throws IOException {
		new MainServer();
	}
}
