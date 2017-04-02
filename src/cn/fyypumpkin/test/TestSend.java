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
public class TestSend extends Thread {

    public static void main(String[] args) throws IOException {

        TestSend testSend = new TestSend();
        testSend.start();
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        String in = scanner.next();
        while (in!= "exit") {
            String friendname = scanner.next();
            String message = scanner.next();
            DataOutputStream outputStream = null;
//        while(true){
            Socket socket = null;
            try {
                socket = new Socket("123.206.101.70", 25565);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("连接已经建立");
            //向服务器端发送数据
            Map<String, String> map = new HashMap<>();
//        map.put("items", "reg");
            map.put("items", "message");
            map.put("username", in);
            map.put("passwd", "222");
            map.put("message",message);
            map.put("friendname",friendname);
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

            System.out.println("等待接收消息");
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
                strInputstream2 = inputStream.readUTF();
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("输入信息为：" + strInputstream2);
//
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


//        }
            in = scanner.next();
        }
    }
}