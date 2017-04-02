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
public class TestSend2 extends Thread {

    private String passwd, message, friendname;
    private Map<String, String> map;


    public static void main(String[] args) throws IOException {

        TestSend2 testSend = new TestSend2();
        testSend.start();
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("请输入用户名");
        String in = scanner.next();
        while (true) {
            System.out.println("请输入操作/reg/login/message");
            String items = scanner.next();
            map = new HashMap<>();
            DataOutputStream outputStream = null;
            Socket socket = null;
            try {
                socket = new Socket("127.0.0.1", 25565);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("连接已经建立");
            switch (items) {
                case "reg":
                    map.put("items", "reg");
                    map.put("username", in);
                    System.out.println("请输入密码");
                    passwd = scanner.next();
                    map.put("passwd", passwd);
                    break;
                case "login":
                    map = new HashMap<>();
                    map.put("items", "login");
                    map.put("username", in);
                    System.out.println("请输入密码");
                    passwd = scanner.next();
                    map.put("passwd", passwd);
                    break;
                case "message":
                    map = new HashMap<>();
                    map.put("items", "message");
                    map.put("username", in);
                    System.out.println("输入发送内容");
                    message = scanner.next();
                    map.put("message", message);
                    System.out.println("请输入发送对象 ");
                    friendname = scanner.next();
                    map.put("friendname", friendname);
                    break;


            }


            //向服务器端发送数据
            //将json转化为String类型
            JSONObject json = new JSONObject(map);
            String jsonString = "";
            jsonString = json.toString();

            try {
                outputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outputStream.writeUTF(jsonString);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

            DataInputStream inputStream = null;
            String strInputstream = "";
            try {
                inputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                strInputstream = inputStream.readUTF();
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("输入信息为：" + strInputstream);
            String strInputstream2 = "";
            try {
                inputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                strInputstream2 = inputStream.readUTF();
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("输入信息为：" + strInputstream2);


//            try {
//                outputStream.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            try {
//                inputStream.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }
}