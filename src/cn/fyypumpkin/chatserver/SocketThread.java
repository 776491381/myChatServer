package cn.fyypumpkin.chatserver;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.json.*;


public class SocketThread extends Thread {
    private static Socket socket = null;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private String strInputstream;

    public SocketThread(Socket socket) {
        SocketThread.socket = socket;
        System.out.println("Server has start");
        System.out.println(socket.getInetAddress());
        inputStream = SocketUtils.getInStream(socket);
        outputStream = SocketUtils.getOutStream(socket);
    }

    @Override
    public void run() {
            try {

                strInputstream = inputStream.readUTF();
                System.out.println("接受到的数据长度为：" + strInputstream);
                JSONObject json = new JSONObject(strInputstream);
                String items = (String) json.get("items");
//                socket.shutdownInput();

                switch (items) {
                    case "reg":
                        String isreged = SocketUtils.reg((String) json.get("username"), (String) json.get("passwd"));
                        Map<String, String> map = new HashMap<>();
                        if (isreged.equals("logsuccess")) {
                            map.put("items", "isreg");
                            map.put("isreg", "success");
                            System.out.println("注册成功");
                        } else {
                            map.put("items", "isreg");
                            map.put("isreg", "failed");
                            map.put("tips", "用户已存在");
                            System.out.println("用户已存在");
                        }

                        String reg = SocketUtils.jsonToString(map);
                        outputStream.writeUTF(reg);
                        outputStream.flush();
//                    outputStream.close();
//                        socket.shutdownOutput();
                        break;
                    case "addfriend":
                        ;
                        break;
                    case "message":
                        ;
                        break;
                    case "login":
                        String islog = SocketUtils.login((String) json.get("username"), (String) json.get("passwd"));
                        Map<String, String> logmap = new HashMap<>();
                        if (islog.equals("logsuccess")) {
                            logmap.put("items", "islog");
                            logmap.put("islog", "success");
                            logmap.put("tips", "登陆成功");
                        } else {
                            logmap.put("items", "islog");
                            logmap.put("islog", "failed");
                            logmap.put("tips", "登录失败");
                        }
                        String log = SocketUtils.jsonToString(logmap);
                        outputStream.writeUTF(log);
                        outputStream.flush();
//                    outputStream.close();
//                        socket.shutdownOutput();


                        break;
                    default:
                        ;
                        break;
                }


            } catch (IOException e) {
                System.out.println("IO Error " + e.getMessage());
                e.printStackTrace();

            } catch (JSONException e) {
                System.out.println("json Error " + e.getMessage());
                e.printStackTrace();

            } finally {

            }


        }


}
