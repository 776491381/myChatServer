package cn.fyypumpkin.chatserver;

import cn.fyypumpkin.entity.UsersEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONObject;

import javax.xml.crypto.Data;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * Created by fyy on 3/28/17.
 */
public class SocketUtils {

//    private static Socket socket;


    public  Socket getSocket(int port){

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            return serverSocket.accept();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public  void startThread(int port){
        SocketThread socketThread = new SocketThread(getSocket(port));
        socketThread.start();
    }



    public static void login(String username , String passwd){






    }

    public static boolean reg(String username , String passwd){


        Session session = HibernateUtils.getSession();
        if(session.get(UsersEntity.class , username) == null){
            UsersEntity user = new UsersEntity();
            java.sql.Date now = getDate();
            user.setRegtime(now);
            user.setUsername(username);
            user.setPasswd(passwd);
            session.save(user);
            Transaction tx = session.beginTransaction();
            tx.commit();
            HibernateUtils.closesession(session);
            login(username,passwd);
            return true;

        }
            return false;
    }


    public static java.sql.Date getDate(){

        java.util.Date nDate = new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String sDate = sdf.format(nDate);
        java.sql.Date now = java.sql.Date.valueOf(sDate);
        return now;

    }


    public static DataOutputStream getOutStream(Socket socket){


        try {
            return new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static DataInputStream getInStream(Socket socket){

        try {
            return new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String jsonToString(Map map){

        JSONObject json = new JSONObject(map);
        return json.toString();


    }





}
