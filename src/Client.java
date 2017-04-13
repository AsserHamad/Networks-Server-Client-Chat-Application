import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class Client{

	static JPanel names;
	static JFrame frame;
	static int selectedPort;
	static int selectedMember;
	static JPanel b_ports;
	static int current_port;
	static JPanel b_names;
	static Hashtable<String,String> table = new Hashtable<String,String>();
	static ArrayList<String> arrayL = new ArrayList<String>();

	public Client() throws IOException{
		System.out.println("CLIENT\n");
		repeat();
	}

	public static ImageIcon resizeImageIcon(ImageIcon x,int width, int height){
		Image img = x.getImage();
		Image newimg = img.getScaledInstance(width, height,  java.awt.Image.SCALE_SMOOTH);
		return new ImageIcon(newimg);
	}

	public void addFont(String name){
		try{
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(name)));
		} catch (IOException|FontFormatException e) {

		}
	}

	public static void repeat() throws IOException{

		int port = (int)(Math.random()*10);
		if(port<3)port=6000;
		else if(port<6)port=6001;
		else if(port<9)port=6002;
		else port=6003;

		Socket socket_client = new Socket("AssMachine", port);
		DataOutputStream server_out = new DataOutputStream(socket_client.getOutputStream());
		BufferedReader server_in = new BufferedReader(new InputStreamReader(socket_client.getInputStream()));


		//USING FONT
		Font y = new Font("Gasalt Regular",Font.BOLD,30);
		Font y1 = new Font("Gasalt Regular",Font.BOLD,1);
		Font y2 = new Font("Gasalt Regular",Font.BOLD,15);
		Font y3 = new Font("Gasalt Regular",Font.BOLD,21);
		Font y4 = new Font("Gasalt Regular",Font.BOLD,40);
		Font basica = new Font("Gasalt Regular",Font.BOLD,35);
		
		JLabel Port = new JLabel(port+"");
		Port.setFont(y4);
		Port.setBounds(40,75,90,30);
		Port.setForeground(Color.WHITE);
		Port.setVisible(false);

		JTextArea text_field = new JTextArea();
		text_field.setEditable(false);
		text_field.setBounds(350, 50, 360, 400);
		text_field.setVisible(false);
		text_field.setOpaque(false);
		text_field.setBackground(new Color(Color.TRANSLUCENT));
		text_field.setFont(y2);

		JLabel recipient_box = new JLabel();
		recipient_box.setVisible(false);
		recipient_box.setIcon(resizeImageIcon(new ImageIcon("box.png"),300,120));
		recipient_box.setBounds(550, -5, 300, 120);
		
		JLabel new_chat = new JLabel();
		new_chat.setVisible(false);
		new_chat.setIcon(resizeImageIcon(new ImageIcon("notification.png"),40,40));
		new_chat.setBounds(50,400,40,40);
		
		JLabel new_chat_name = new JLabel();
		new_chat_name.setText(" !");
		new_chat_name.setVisible(false);
		new_chat_name.setBounds(20,430,400,40);
		new_chat_name.setFont(y3);

		JLabel recipient_name = new JLabel();
		recipient_name.setVisible(false);
		recipient_name.setBounds(630, 30, 200, 30);
		recipient_name.setFont(y);
		recipient_name.setForeground(Color.BLUE);


		JLabel background = new JLabel();
		background.setIcon(new ImageIcon("BG_.jpg"));
		background.setBounds(0,0,800,500);

		JLabel name = new JLabel("");
		JLabel current_chat = new JLabel("");
		JLabel l_port = new JLabel(port+"");


		JTextField chat_box = new JTextField(){
			@Override
			public void setBorder(Border border ) {
			}
		};
		chat_box.setBounds(245,365,500,70);
		chat_box.setOpaque(false);
		chat_box.setForeground(Color.WHITE);
		//chat_box.setBackground(Color.darkGray);
		chat_box.setFont(y);
		//chat_box.setFont(y1);
		chat_box.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				if(!current_chat.getText().equals("") && arrayL.contains(current_chat.getText())){
					PrintWriter pw = new PrintWriter(server_out,true);
					pw.println("chat_"+chat_box.getText()+"_"+current_chat.getText()+"_"+l_port.getText());
					String st = table.get(current_chat.getText())+"\nYou : "+chat_box.getText();
					System.out.println(st);
					table.replace(current_chat.getText(), st);
					text_field.setText(table.get(current_chat.getText()));
					chat_box.setText("");
					
				}
			}
		});

		chat_box.setVisible(false);

		b_names = new JPanel();
		b_names.setLayout(null);
		b_names.setBounds(0, 0, 800, 500);
		b_names.setOpaque(false);
		b_names.setVisible(false);

		b_ports = new JPanel();
		b_ports.setLayout(null);
		b_ports.setBounds(0, 0, 800, 500);
		b_ports.setOpaque(false);
		b_ports.setVisible(false);
		for(int i=0;i<4;i++){
			int x = 6000+i;
			JButton b_port = new JButton(x+"");
			b_port.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					if(!(table.size()>1))System.out.println("the table had "+table.keySet().toString()+" with"+ table.values().toString());
					background.setIcon(new ImageIcon("BG_"+b_port.getText()+".jpg"));
					l_port.setText(b_port.getText());
					PrintWriter pw = new PrintWriter(server_out,true);
					pw.println("GetMemberList_"+l_port.getText()+"\n");
				}
			});
			if(x==6000)b_port.setBounds(110,20,80,70);
			else if(x==6001)b_port.setBounds(190,35,60,50);
			else if(x==6002)b_port.setBounds(250,20,60,50);
			else b_port.setBounds(220,70,100,90);
			//if(x==port)b_port.doClick();


			b_port.setOpaque(false);
			b_port.setBorderPainted(false); 
			b_port.setContentAreaFilled(false); 
			b_port.setFocusPainted(false);
			b_port.setForeground(Color.WHITE);
			b_port.setFont(y1);


			b_ports.add(b_port);
		}
		name.setBounds(220,345,100,30);
		name.setForeground(Color.BLACK);
		name.setFont(y);



		frame = new JFrame();
		frame.setLayout(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 500);
		frame.setResizable(false);

		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 800, 500);
		panel.setOpaque(false);
		panel.setLayout(null);
		JTextField enter_name = new JTextField();
		enter_name.setBounds(250,350,300,50);
		enter_name.setVisible(true);



		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				try {
					socket_client.close();
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}});


		enter_name.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				try {
					//System.out.println("Pressed enter! you are now called "+enter_name.getText());
					server_out.writeBytes("Join_"+enter_name.getText()+"\n");
					background.setIcon(new ImageIcon("BG.jpg"));
					enter_name.setVisible(false);
					panel.remove(enter_name);
					Port.setVisible(true);
					

					name.setText(enter_name.getText());
					chat_box.setVisible(true);
					text_field.setVisible(true);
					b_ports.add(chat_box);
					b_ports.setVisible(true);
					

					background.setIcon(new ImageIcon("BG_"+l_port.getText()+".jpg"));
					l_port.setText(l_port.getText()+"");
					//System.out.println("talking to "+l_port.getText()+" now :D");
					PrintWriter ppw = new PrintWriter(server_out,true);
					ppw.println("GetMemberList_"+l_port.getText()+"\n");

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		enter_name.setFont(y);
		new Thread(new Runnable(){
			public void run() {
				try {
					while(socket_client.isConnected()){
						String s = server_in.readLine();
						String [] m = s.split("_");
						if(m[0].equals("check")){
							PrintWriter pw = new PrintWriter(server_out,true);
							pw.println("GetMemberList_"+l_port.getText()+"\n");
						}
						else if(m[0].equals("members")){
							String [] message = m[1].split(",");
							b_names.removeAll();
							arrayL.clear();
							for(int i=0;i<message.length;i++){
								if(i==message.length-1)message[i]=message[i].substring(0, message[i].length()-1);
								if(!message[i].equals("[") && !(message[i].substring(1).equals(name.getText()) && Integer.parseInt(Port.getText())==Integer.parseInt(l_port.getText()))){
									JButton y = new JButton(message[i].substring(1));
									arrayL.add(y.getText());
									if(!table.contains(y.getText()))table.put(y.getText(), "");
									System.out.println("This member is :");
									y.setOpaque(false);
									y.setFont(basica);
									y.setContentAreaFilled(false);
									y.addActionListener(new ActionListener(){
										public void actionPerformed(ActionEvent arg0) {
											current_chat.setText(y.getText());
											recipient_box.setVisible(true);
											recipient_name.setVisible(true);
											recipient_name.setText(y.getText());
											text_field.setText(table.get(y.getText()));
										}
									});
									y.setBounds(20,i*40 + 160,170,30);
									b_names.add(y);
								}
								b_names.setVisible(false);
								b_names.setVisible(true);
							}
						}else{
							//System.out.println("not members... you sent "+m[0]+m[1]+"\n");
							String x = m[1].split(":")[0].substring(0, m[1].split(":")[0].length()-1);
							if(!table.containsKey(x))table.put(x, "");
							String st = table.get(x)+m[1]+"\n";
							System.out.println("My child, your chat with "+x+" had "+table.get(x)
							+" previously, but now it should have st: "+st+" which came from "+m[1] );
							//table.replace(x, st);
							table.remove(x);
							table.put(x, st);
							if(current_chat.getText().equals(x))
							text_field.setText(table.get(x));
							else{
								new_chat_name.setText(x+" !");
								new_chat.setVisible(true);
								new_chat_name.setVisible(true);
								TimerTask tt = new TimerTask(){
									@Override
									public void run() {
										new_chat.setVisible(false);
										new_chat_name.setVisible(false);
									}
								};
								Timer timer = new Timer();
								timer.schedule(tt, 5000);
							}
							//text_field.setText(text_field.getText()+"\n"+m[1]);
							text_field.setVisible(false);
							text_field.setVisible(true);
						}
					}
				} catch (IOException e) {
					return;
				}
			}
		}).start();

		panel.add(Port);
		panel.add(new_chat_name);
		panel.add(new_chat);
		panel.add(recipient_name);
		panel.add(recipient_box);
		panel.add(enter_name);
		panel.add(name);
		panel.add(b_ports);
		panel.add(b_names);
		panel.add(text_field);
		panel.add(background);

		frame.add(panel);
		frame.setVisible(true);


	}
	public static void main(String argv[]) throws Exception {
		new Client();
	}
}