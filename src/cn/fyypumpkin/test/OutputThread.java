package cn.fyypumpkin.test;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by fyy on 4/5/17.
 */
public class OutputThread extends Thread {

    private DataOutputStream outputStream;
    private String passwd, message, friendname;
    private Scanner scanner = null;

    public OutputThread(DataOutputStream outputStream) {
        scanner = new Scanner(System.in);
        this.outputStream = outputStream;


    }

    @Override
    public void run() {
        System.out.println("请输入用户名");
        String in = scanner.next();

        while (true) {

            Map<String, String> map = new HashMap<>();
            System.out.println("请输入操作/reg/login/message");
            String items = scanner.next();
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
                default:
                    break;
            }

            System.out.println(map);
            JSONObject json = new JSONObject(map);
            String jsonString = json.toString();
            try {
                outputStream.writeUTF(jsonString);
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }
}
