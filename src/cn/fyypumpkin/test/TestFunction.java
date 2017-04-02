package cn.fyypumpkin.test;

import cn.fyypumpkin.chatserver.SocketUtils;
import org.json.JSONObject;

/**
 * Created by fyy on 3/31/17.
 */
public class TestFunction {
    public static void main(String[] args) {
        SocketUtils.reg("test1","2222");
        SocketUtils.login("test1","2222");
        SocketUtils.reg("test2","2222");
        SocketUtils.login("test2","2222");
        SocketUtils.addFriend("test1","test2");
        JSONObject json = SocketUtils.returnfriendSocket("test2");
        System.out.println("json--------------->"+json);
//        SocketUtils.addFriend("111","222");
//        SocketUtils.setLogusers("222");
//        SocketUtils.displayLogusers();
    }

}
