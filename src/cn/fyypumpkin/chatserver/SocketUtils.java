package cn.fyypumpkin.chatserver;

import cn.fyypumpkin.entity.ChatHistoryEntity;
import cn.fyypumpkin.entity.LogUserEntity;
import cn.fyypumpkin.entity.UsersEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.type.ListType;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SocketUtils {


    private static String login(String username, String passwd, Session session) {
        String hql = "from UsersEntity where username =" + "\'" + username + "\'" + " and passwd = " + "\'" + passwd + "\'";
        Object result = session.createQuery(hql).uniqueResult();
        if (result != null) {
            return "logsuccess";
        }
        return "logfailed";
    }


    static String login(String username, String passwd) {
        Session session = HibernateUtils.getSession();
        String hql = "from UsersEntity where username =" + "\'" + username + "\'" + " and passwd = " + "\'" + passwd + "\'";
        Object result = session.createQuery(hql).uniqueResult();
        if (result != null) {
            return "logsuccess";
        }
        return "logfailed";
    }

    static String reg(String username, String passwd) {


        Session session = HibernateUtils.getSession();
        java.sql.Date now = getDate("yyyy-MM-dd");
        if (session.get(UsersEntity.class, username) == null) {
            UsersEntity user = new UsersEntity();
            user.setRegtime(now);
            user.setUsername(username);
            user.setPasswd(passwd);
            session.save(user);
            Transaction tx = session.beginTransaction();
            tx.commit();
            HibernateUtils.closesession(session);

            return login(username, passwd, session);

        } else {
            System.out.println("false");
            return "failed";
        }
    }


    public static void saveMessage(String username, String message, String friendname) {
        Session session = HibernateUtils.getSession();
        ChatHistoryEntity chatHistoryEntity = new ChatHistoryEntity(message, getDate("yyyy-MM-dd HH:mm:ss"), friendname);
        UsersEntity usersEntity = (UsersEntity) session.get(UsersEntity.class, username);
        usersEntity.getChatHistory().add(chatHistoryEntity);
        session.save(chatHistoryEntity);
        session.save(usersEntity);
        Transaction tx = session.beginTransaction();
        tx.commit();
        HibernateUtils.closesession(session);

    }


    static void sendMessage(JSONObject json) {

        String friendname = null;
        try {
            friendname = (String) json.get("friendname");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Socket socketFriend = SocketUtils.traverseMap(ServerRun.clients, friendname);
        if (socketFriend == null) {
            System.out.println("未添加好友");
        } else {
            DataOutputStream outputStream2 = SocketUtils.getOutStream(socketFriend);
            String message = json.toString();
            try {
                assert outputStream2 != null;
                outputStream2.writeUTF(message);
                outputStream2.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private static java.sql.Date getDate(String pattern) {

        java.util.Date nDate = new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String sDate = sdf.format(nDate);
        java.sql.Date now = java.sql.Date.valueOf(sDate);
        return now;

    }

    static DataOutputStream getOutStream(Socket socket) {


        try {
            return new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    static DataInputStream getInStream(Socket socket) {

        try {

            return new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    static String jsonToString(Map map) {

        JSONObject json = new JSONObject(map);
        return json.toString();


    }


    static Socket traverseMap(Map<String, Socket> map, String friendname) {

        if (map != null) {
            for (Map.Entry<String, Socket> entry : map.entrySet()) {
                if (entry.getKey().equals(friendname)) {
                    return entry.getValue();
                }
            }
        }
        return null;
    }

    static void traverseMap(Map<String, Socket> map) {
        if (map != null) {
            for (Map.Entry<String, Socket> entry : map.entrySet()) {
                System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            }
        }
    }

    static int setMap(Map<String, Socket> map, String username, Socket socket) {
        map.put(username, socket);
        ServerRun.clients = map;
        return 1;
    }

    public static List<LogUserEntity> displayLogusers(){

        String hql = "from LogUserEntity";
        List<LogUserEntity> list = new ArrayList();
        Session session = HibernateUtils.getSession();
        list = session.createQuery(hql).list();
        for(LogUserEntity logUserEntity : list){
            System.out.println(logUserEntity.getUsername());
        }

        return  list;
    }

     public  static void setLogusers(String username){

        Session session = HibernateUtils.getSession();
        LogUserEntity logUserEntity = new LogUserEntity();
        logUserEntity.setUsername(username);
        session.save(logUserEntity);
        Transaction tx = session.beginTransaction();
        tx.commit();
        HibernateUtils.closesession(session);


    }

    public static void delLogusers(String username){
        String hql = "delete from LogUserEntity where username = "+"\'"+username+"\'";
        Session session = HibernateUtils.getSession();
        session.createQuery(hql).executeUpdate();
        Transaction tx = session.beginTransaction();
        tx.commit();
        HibernateUtils.closesession(session);

    }



}
