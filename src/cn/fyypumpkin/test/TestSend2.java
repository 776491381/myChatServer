package cn.fyypumpkin.test;

import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by fyy on 3/30/17.
 */
public class TestSend2 {

    Socket socket = null;

    public static void main(String[] args) throws IOException {
        TestSend2 testSend2 = new TestSend2();
        testSend2.init();
        testSend2.hand();

    }

    public void init() {
        try {
//            socket = new Socket("127.0.0.1", 25565);
                socket = new Socket("123.206.101.70", 25565);

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("连接已经建立");
    }


    private void hand() {

        try {
            InputThread inputThread = new InputThread(new DataInputStream(new BufferedInputStream(socket.getInputStream())));
            OutputThread outputThread = new OutputThread(new DataOutputStream(new BufferedOutputStream(socket.getOutputStream())));
            inputThread.start();
            outputThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}