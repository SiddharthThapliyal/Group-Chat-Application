import java.awt.image.BufferedImage;
import java.net.*; 
import java.io.*; 
import java.util.*; 
import javax.imageio.ImageIO;
public class GroupChat 
{ 
	private static final String TERMINATE = "Exit", ADDFILE = "Image Send"; 
	static String name; 
	static volatile boolean finished = false; 
	public static void main(String[] args) 
	{ 
		if (args.length != 2) 
			System.out.println("Two arguments required: <multicast-host> <port-number>"); 
		else
		{ 
            
            Scanner sc = new Scanner(System.in);
            
			try
			{ 
				InetAddress group = InetAddress.getByName(args[0]); 
				int port = Integer.parseInt(args[1]); 
				 
				System.out.print("Enter your name: "); 
				name = sc.nextLine(); 
				MulticastSocket socket = new MulticastSocket(port); 
			
				// Since we are deploying 
				socket.setTimeToLive(0); 
				//this on localhost only (For a subnet set it as 1) 
				
				socket.joinGroup(group); 
				Thread t = new Thread(new
				ReadThread(socket,group,port)); 
			
				// Spawn a thread for reading messages 
				t.start(); 
				
				// sent to the current group 
				System.out.println("Start typing messages...\n"); 
				while(true) 
				{ 
					String message,msg=""; 
					message = sc.nextLine(); 
					if(message.equalsIgnoreCase(GroupChat.TERMINATE)) 
					{ 
						finished = true; 
						socket.leaveGroup(group); 
						socket.close(); 
						break; 
					} 
                                        
                                        if(message.equalsIgnoreCase("add file")) {
                                             message = msg = "Image Send";
					}
                                        message = name + ": " + message; 
					byte[] buffer = message.getBytes(); 
					DatagramPacket datagram = new
					DatagramPacket(buffer,buffer.length,group,port); 
					socket.send(datagram);
                                        
                                        
                                        if(msg.equalsIgnoreCase("Image Send"))
                                        {
                                                System.out.println("enter file name :");
                                                String filename = sc.nextLine();
                                                BufferedImage bImage = ImageIO.read(new File("C:\\Users\\Sid\\Desktop\\sid1 project\\" + filename));
                                                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                                ImageIO.write(bImage, "jpg", bos );
                                                byte [] data = bos.toByteArray();
                                                datagram = new DatagramPacket(data,data.length,group,port); 
                                                socket.send(datagram);
                                                System.out.println("Image send.");
      						msg = "";
                                        }    
				} 
			} 
			catch(SocketException se) 
			{ 
				System.out.println("Error creating socket"); 
				se.printStackTrace(); 
			} 
			catch(IOException ie) 
			{ 
				System.out.println("Error reading/writing from/to socket"); 
				ie.printStackTrace(); 
			} 
		} 
	} 
} 
class ReadThread implements Runnable 
{ 
	private MulticastSocket socket; 
	private InetAddress group; 
	private int port; 
	private static final int MAX_LEN = 1000; 
	ReadThread(MulticastSocket socket,InetAddress group,int port) 
	{ 
		this.socket = socket; 
		this.group = group; 
		this.port = port; 
	} 
	
	@Override
	public void run() 
	{ 
		while(!GroupChat.finished) 
		{ 
				byte[] buffer = new byte[ReadThread.MAX_LEN]; 
				DatagramPacket datagram = new
				DatagramPacket(buffer,buffer.length,group,port); 
				String message; 
			try
			{ 
				socket.receive(datagram); 
				message = new
				String(buffer,0,datagram.getLength(),"UTF-8");
                                if(!message.startsWith(GroupChat.name)) 
					System.out.println(message);
                                if(message.endsWith("Image Send"))
                                {
                                    socket.receive(datagram);
                                    byte[] buf = datagram.getData();
                                    ByteArrayInputStream bis = new ByteArrayInputStream(buf);
                                    BufferedImage bImage2 = ImageIO.read(bis);
                                    ImageIO.write(bImage2, "jpg", new File("C:\\Users\\Sid\\Desktop\\sid project\\output1.jpg") );
                                    
                                }
				 
			} 
			catch(IOException e) 
			{ 
				System.out.println("Socket closed!"); 
			} 
		} 
	} 
}
