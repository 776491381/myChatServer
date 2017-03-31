package cn.fyypumpkin.chatserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * Created by fyy on 3/30/17.
 */
public class ServerRun
{
    final  static  int port = 25565;
    public static Map<String,Socket> clients;
    private static ServerSocket serverSocket;
    private static int count = 0;

    private static ServerRun instance = null;
    public static synchronized ServerRun getInstance(){

        if(instance==null){
            instance = new ServerRun();
        }
        return instance;
    }

//    public Map<String, Socket> getClients() {
//        return clients;
//    }
//
//    public void setClients(Map<String, Socket> clients) {
//        this.clients = clients;
//    }

    public ServerRun() {
        try {
            clients = new HashMap<>();
            serverSocket = new ServerSocket(port);
            while(true){
                Socket socket = serverSocket.accept();
//                clients.put(String.valueOf(socket.getInetAddress()),socket);
//                Iterator<Map.Entry<String,Socket>> entryIterator = clients.entrySet().iterator();
//                while(entryIterator.hasNext()){
//                    Map.Entry<String,Socket> entry = entryIterator.next();
//                    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
//                }
                count++;
                SocketThread socketThread = new SocketThread(socket);
                socketThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {

        ServerRun.getInstance();

    }


}
