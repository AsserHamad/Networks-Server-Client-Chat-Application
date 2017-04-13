import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Hashtable;

class Server6001 {
	static ServerSocket welcomeSocket;
	static Socket master; 
	static Hashtable<String,Socket> table = new Hashtable<String,Socket>();
	static ArrayList<String> arrayL = new ArrayList<String>();
	
	public Server6001() throws UnknownHostException, IOException{
		int port = 6001;
		System.out.println("Server "+port+"\n");
		welcomeSocket  = new ServerSocket(port);
		master = new Socket("AssMachine",5999);
		PrintWriter client_out = new PrintWriter(master.getOutputStream(),true);
		client_out.println("Join_"+port);
		repeat();
		
	}
	public static void repeat() throws IOException{
		BufferedReader master_input = new BufferedReader(new InputStreamReader(master.getInputStream()));
		new Thread(new Runnable(){
			public void run() {
				while(true){
					try {
						while(true){
							//System.out.println("Waiting for input from master");
							String x = master_input.readLine();
							//System.out.println("Master requests that I "+x);
							String[] master_sentence = x.split("_");
							if(master_sentence[0].equals("fetchMembers")){
								PrintWriter pw = new PrintWriter(master.getOutputStream(),true);
								String mems = "";
								mems=table.keySet().toString();
								pw.println("members_"+mems+"_"+master_sentence[1]+"_"+master_sentence[2]);
							}
							else if(master_sentence[0].equals("members")){
								PrintWriter pw = new PrintWriter(table.get(master_sentence[2]).getOutputStream(),true);
								pw.println("members_"+master_sentence[1]);
							}
							else if(master_sentence[0].equals("check")){
								for(int i=0;i<table.size();i++){
									System.out.println("table size is "+table.size()+" and array size is "+arrayL.size()+" with members "+arrayL.toString());;
									PrintWriter pw = new PrintWriter(table.get(arrayL.get(i)).getOutputStream(),true);
									pw.println("check");
								}
							}
							else if(master_sentence[0].equals("chat")){
								String to = master_sentence[3];
								try{
								PrintWriter client_out = new PrintWriter(table.get(to).getOutputStream(),true);
								client_out.println("chat_"+master_sentence[2] + " : " + master_sentence[1]);
								}catch(IOException ie){
									
								}
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		while(true) {
			Socket socket_connect = welcomeSocket.accept();
			//System.out.println("New member added to server");

			//Main Thread
			new Thread(new Runnable(){
				String name;
				public void run() {
					
					try{
						Socket socket = socket_connect;
						name = "";
						while(socket.isConnected()){
							BufferedReader client_in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
							String clientSentence = client_in.readLine();
							//System.out.println("client_in is "+clientSentence);
							String[] recieved = clientSentence.split("_");

							// Requesting to join the server
							if(recieved.length==2 && recieved[0].equals("Join")){
								if(!table.containsKey(recieved[1])){
									name = recieved[1];
									table.put(recieved[1], socket);
									arrayL.add(recieved[1]);
									PrintWriter pw = new PrintWriter(master.getOutputStream(),true);
									pw.println("check");
								}
								else{
									PrintWriter client_out = new PrintWriter(socket.getOutputStream(),true);
									client_out.println("taken");
								}
							}

							// Requesting list of members
							else if(recieved.length==2 && recieved[0].equals("GetMemberList")){
								PrintWriter pw = new PrintWriter(master.getOutputStream(),true);
								pw.println("getMembers_"+recieved[1]+"_"+name+"_6001");
							}

							// Requesting to disconnect
							else if(recieved.length==2 && (recieved[0].equals("QUIT") || recieved[0].equals("BYE"))){
								table.remove(recieved[1]);
								int num = 0;
								for(int i=0;i<arrayL.size();i++)if(arrayL.get(i).equals(recieved[1]))num = i;
								arrayL.remove(num);
								break;
							}

							// Chat message
							else if(recieved[0].equals("chat")){
								PrintWriter pw = new PrintWriter(master.getOutputStream(),true);
								pw.println("chat_"+recieved[1]+"_"+name+"_"+recieved[2]+"_6001_"+recieved[3]);
							}
							else {
								PrintWriter client_out = new PrintWriter(socket.getOutputStream(),true);
								client_out.println("Unknown Command my friend");
							}
						}
					}catch(Exception e){
						try {
						table.remove(name);
						int num = 0;
						for(int i=0;i<arrayL.size();i++)if(arrayL.get(i).equals(name))num = i;
						arrayL.remove(num);
						//System.out.println(name + "just left");
						PrintWriter pw = new PrintWriter(master.getOutputStream(),true);
							pw.println("check");
						} catch (IOException e1) {
						}
						return;
					}
				}
			}).start();


		}
	}
	public static boolean checkPort(int port){
		try{
			Socket x = new Socket("AssMachine",port);
			x.close();
			return false;
		}catch(Exception e){
			return true;
		}
	}
	public static void main(String args[]) throws Exception {
		new Server6001();
	}
}