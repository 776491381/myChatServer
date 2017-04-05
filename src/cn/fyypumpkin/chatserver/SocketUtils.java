package cn.fyypumpkin.chatserver;

import cn.fyypumpkin.entity.ChatHistoryEntity;
import cn.fyypumpkin.entity.FriendsEntity;
import cn.fyypumpkin.entity.LogUserEntity;
import cn.fyypumpkin.entity.UsersEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;

public class SocketUtils {

//
//    private static String login(String username, String passwd, Session session) {
//        String hql = "from UsersEntity where username =" + "\'" + username + "\'" + " and passwd = " + "\'" + passwd + "\'";
//        Object result = session.createQuery(hql).uniqueResult();
//        HibernateUtils.closesession(session);
//        if (result != null) {
//            return "logsuccess";
//        }
//        return "logfailed";
//    }


    public static String login(String username, String passwd) {
        Session session = HibernateUtils.getSession();
        String hql = "from UsersEntity where username =" + "\'" + username + "\'" + " and passwd = " + "\'" + passwd + "\'";
        Object result = session.createQuery(hql).uniqueResult();
        List<LogUserEntity> logUserEntities = session.createQuery("from LogUserEntity ").list();
        HibernateUtils.closesession(session);
        if (result != null) {
            for (LogUserEntity logUserEntity : logUserEntities) {
                if (logUserEntity.getUsername().equals(username)) {
                    return "logsuccess";
                }
            }
            setLogusers(username);
            return "logsuccess";
        }
        return "logfailed";
    }

    public static JSONObject returnfriendSocket(String username) {

        Map map = new HashMap();
        map.put("items", "friendlist");
        Session session = HibernateUtils.getSession();
        UsersEntity usersEntity = (UsersEntity) session.get(UsersEntity.class, username);
        Set<FriendsEntity> friends = usersEntity.getFriends();
        System.out.println("---------------------->username" + username);
        System.out.println("---------------------->friends" + friends + "size" + friends.size());
        List<LogUserEntity> logpeo = session.createQuery("from LogUserEntity").list();
        System.out.println("---------------------->logpeo" + logpeo + "size" + logpeo.size());
        System.out.println("---------------------->client" + ServerRun.clients + "size" + ServerRun.clients.size());
        for (FriendsEntity friendsEntity : friends) {
            for (LogUserEntity logUserEntity : logpeo) {
                if (friendsEntity.getFriendname().equals(logUserEntity.getUsername())) {
                    System.out.println("进入if");
                    if (ServerRun.clients.get(friendsEntity.getFriendname()) != null) {
                        map.put("friendname", friendsEntity.getFriendname());
//                    for(Map.Entry<String, Socket> entry : ServerRun.clients.entrySet()){
//                        if(entry.getKey().equals(friendsEntity.getFriendname())){
//                            map.put("socket" , entry.getValue());
//                            break;
//                        }
//
//                    }
                        map.put(friendsEntity.getFriendname() + "ip", ServerRun.clients.get(friendsEntity.getFriendname()).getInetAddress());
                        map.put(friendsEntity.getFriendname() + "port", ServerRun.clients.get(friendsEntity.getFriendname()).getPort());
                    }
                }
            }

        }


        JSONObject json = new JSONObject(map);


        return json;
    }

    public static String reg(String username, String passwd) {


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
            return login(username, passwd);

        } else {
            System.out.println("false");
            return "failed";
        }
    }


    public static void saveMessage(String username, String message, String friendname) {
        Session session = HibernateUtils.getSession();
        ChatHistoryEntity chatHistoryEntity = new ChatHistoryEntity(message, getDate("yyyy-MM-dd"), friendname);
        UsersEntity usersEntity = (UsersEntity) session.get(UsersEntity.class, username);
        usersEntity.getChatHistory().add(chatHistoryEntity);
        session.save(chatHistoryEntity);
        session.save(usersEntity);
        Transaction tx = session.beginTransaction();
        tx.commit();
        HibernateUtils.closesession(session);

    }


    public static void sendMessage(JSONObject json) {

        String friendname = null;
        try {
            friendname = (String) json.get("friendname");
            System.out.println(friendname);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Socket socketFriend = new Socket();
        socketFriend = SocketUtils.traverseMap(ServerRun.clients, friendname);
        if (socketFriend == null) {
            System.out.println("未添加好友");
        } else {
            System.out.println("=================");
            DataOutputStream outputStream2 = SocketUtils.getOutStream(socketFriend);
            String message = json.toString();
            try {
                assert outputStream2 != null;
                System.out.println("------------->>>>>>>>" + socketFriend);
                outputStream2.writeUTF(message);
                outputStream2.flush();

                traverseMap(ServerRun.clients);
//                outputStream2.close();
//                socketFriend.close();
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

    static String jsonToString(Map map) {

        JSONObject json = new JSONObject(map);
        return json.toString();


    }


    public static Socket traverseMap(Map<String, Socket> map, String friendname) {

        if (map != null) {
            for (Map.Entry<String, Socket> entry : map.entrySet()) {
                if (entry.getKey().equals(friendname)) {
                    return entry.getValue();
                }
            }
        }
        return null;
    }

    public static void traverseMap(Map<String, Socket> map) {
        if (map != null) {
            for (Map.Entry<String, Socket> entry : map.entrySet()) {
                System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            }
        }
    }

    public static int setMap(Map<String, Socket> map, String username, Socket socket) {
//        map.put(username, socket);
        ServerRun.clients.put(username, socket);
        return 1;
    }

    public static List<LogUserEntity> displayLogusers() {

        String hql = "from LogUserEntity";
        List<LogUserEntity> list = new ArrayList();
        Session session = HibernateUtils.getSession();
        list = session.createQuery(hql).list();
        for (LogUserEntity logUserEntity : list) {
            System.out.println(logUserEntity.getUsername());
        }

        return list;
    }

    public static void setLogusers(String username) {

        Session session = HibernateUtils.getSession();
        LogUserEntity logUserEntity = new LogUserEntity();
        logUserEntity.setUsername(username);
        session.save(logUserEntity);
        Transaction tx = session.beginTransaction();
        tx.commit();
        HibernateUtils.closesession(session);


    }

    public static void delLogusers(String username) {
        String hql = "delete from LogUserEntity where username = " + "\'" + username + "\'";
        Session session = HibernateUtils.getSession();
        session.createQuery(hql).executeUpdate();
        Transaction tx = session.beginTransaction();
        tx.commit();
        HibernateUtils.closesession(session);

    }

    public static boolean addFriend(String friendname, String username) {
        Session session = HibernateUtils.getSession();
        String hql = "from UsersEntity where username = " + "\'" + friendname + "\'";
        Object result = session.createQuery(hql).uniqueResult();
        if (result != null) {
            UsersEntity usersEntity = (UsersEntity) session.get(UsersEntity.class, username);
            FriendsEntity friendsEntity = new FriendsEntity();
            friendsEntity.setFriendname(friendname);
            usersEntity.getFriends().add(friendsEntity);
            session.save(friendsEntity);
            session.save(usersEntity);

            UsersEntity usersEntity2 = (UsersEntity) session.get(UsersEntity.class, friendname);
            FriendsEntity friendsEntity2 = new FriendsEntity();
            friendsEntity2.setFriendname(username);
            usersEntity2.getFriends().add(friendsEntity2);
            session.save(friendsEntity2);
            session.save(usersEntity2);

            Transaction tx = session.beginTransaction();
            tx.commit();
            HibernateUtils.closesession(session);
            return true;
        } else {
            return false;
        }
    }


}
