import java.net.*;
import java.io.*;

public class ServerClient  {
    private Socket clientSocket;            // Socket to connect to the server
    private BufferedReader in;              // Reader for incoming messages
    private PrintWriter out;                // Writer for outgoing messages
    private Thread receiverThread;          // Thread to listen for incoming messages
    private ObjectOutputStream oos;         // Output stream to send objects
    private MessageHandler messageHandler;  // Interface to handle received messages

    /**
     *
     */
    public interface MessageHandler{
        void  handleMessage(String msg);
    }

    public void setMessageHandler(MessageHandler messageHandler){
        this.messageHandler = messageHandler;
    }

    public void startConection(String host, int port) throws IOException {
        clientSocket =new Socket("localhost",port);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(clientSocket.getOutputStream(),true);
        oos = new ObjectOutputStream(clientSocket.getOutputStream());
        receiverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                listenForMessages();
            }
        });
        receiverThread.start();
    }

    public void sendMessage(String msg){
        out.println(msg);
        out.flush();

    }

    public String receiveMessage() throws IOException{
        return this.in.readLine();
    }
    public void sendObject(Player player){
        try {
            oos.writeObject(player);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void listenForMessages(){
        String response;
        try{
            while((response = in.readLine())!=null){
                if(messageHandler != null){
                    messageHandler.handleMessage(response);
                }
            }
        }
        catch (IOException e){

        }
    }

}



