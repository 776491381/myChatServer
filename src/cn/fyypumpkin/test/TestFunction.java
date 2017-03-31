package cn.fyypumpkin.test;

import cn.fyypumpkin.chatserver.SocketUtils;

/**
 * Created by fyy on 3/31/17.
 */
public class TestFunction {
    public static void main(String[] args) {
        SocketUtils.setLogusers("222");
        SocketUtils.displayLogusers();
    }

}
