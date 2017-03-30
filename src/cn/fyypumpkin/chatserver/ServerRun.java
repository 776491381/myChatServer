package cn.fyypumpkin.chatserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fyy on 3/30/17.
 */
public class ServerRun
{
    final  static  int port = 25565;
    private List<Socket> clients;
    private ServerSocket serverSocket;

    public ServerRun() {
        try {
            clients=new ArrayList<Socket>();
            serverSocket = new ServerSocket(port);
            while(true){
                Socket socket = serverSocket.accept();
                clients.add(socket);
                SocketThread socketThread = new SocketThread(socket);
                socketThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {

        new ServerRun();


    }


}
