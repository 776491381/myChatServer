package cn.fyypumpkin.chatserver;

import cn.fyypumpkin.entity.ChatHistoryEntity;
import cn.fyypumpkin.entity.UsersEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONObject;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Map;

public class SocketUtils {

//    private static Socket socket;


//    private static Socket getSocket(int port) {
//
//        try {
//            ServerSocket serverSocket = new ServerSocket(port);
//            return serverSocket.accept();
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//
//    public static void startThread(int port) {
//        SocketThread socketThread = new SocketThread(getSocket(port));
//        socketThread.start();
//    }


    public static String login(String username, String passwd, Session session) {
        String hql = "from UsersEntity where username ="+"\'"+username+"\'"+" and passwd = "+"\'"+passwd+"\'";
        Object result=session.createQuery(hql).uniqueResult();
        if(result!=null) {
            return "logsuccess";
        }
        return "logfailed";
    }


    public static String login(String username, String passwd) {
        Session session = HibernateUtils.getSession();
        String hql = "from UsersEntity where username ="+"\'"+username+"\'"+" and passwd = "+"\'"+passwd+"\'";
        Object result=session.createQuery(hql).uniqueResult();
        if(result!=null) {
            return "logsuccess";
        }
        return "logfailed";
    }

    public static String reg(String username, String passwd) {


        Session session = HibernateUtils.getSession();
        java.sql.Date now = getDate();
        if ( session.get(UsersEntity.class, username) == null) {
            UsersEntity user = new UsersEntity();
            user.setRegtime(now);
            user.setUsername(username);
            user.setPasswd(passwd);
            session.save(user);
            Transaction tx = session.beginTransaction();
            tx.commit();
            return login(username, passwd, session);

        } else {
            System.out.println("false");
            return "failed";
        }
    }


    public static void saveMessage(String username , String message ,String friendname){
        Session session = HibernateUtils.getSession();
        ChatHistoryEntity chatHistoryEntity = new ChatHistoryEntity(message,getDate(),friendname);
        chatHistoryEntity.setCid("112qwsdfgbhvnjutiyokgmjivughfder");
        UsersEntity usersEntity = (UsersEntity) session.get(UsersEntity.class,username);
        usersEntity.getChatHistory().add(chatHistoryEntity);
        session.save(chatHistoryEntity);
        session.save(usersEntity);
        Transaction tx = session.beginTransaction();
        tx.commit();
    }


    public static void sendMessage(String message , Socket socket){





    }




    public static java.sql.Date getDate() {

        java.util.Date nDate = new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String sDate = sdf.format(nDate);
        java.sql.Date now = java.sql.Date.valueOf(sDate);
        return now;

    }


    public static DataOutputStream getOutStream(Socket socket) {


        try {
            return new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static DataInputStream getInStream(Socket socket) {

        try {

            return new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String jsonToString(Map map) {

        JSONObject json = new JSONObject(map);
        return json.toString();


    }


    public static Socket traverseMap(Map<String, Socket> map, String friendname){

        if(map!=null)
        for (Map.Entry<String, Socket> entry : map.entrySet()) {
            if(entry.getKey().equals(friendname)){
                return entry.getValue();
            }
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }
        return null;
    }


    public static int setMap(Map<String, Socket> map , String username , Socket socket){
        map.put(username,socket);
        ServerRun.clients = map;
        return 1;
    }

}
