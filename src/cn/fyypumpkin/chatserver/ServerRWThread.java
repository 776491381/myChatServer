package cn.fyypumpkin.chatserver;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fyy on 4/5/17.
 */
public class ServerRWThread extends Thread {

    private Socket socket = null;
    private
    DataInputStream inputStream;
    private DataOutputStream outputStream;
    private String strInputstream;
    private int flag =0;

    public ServerRWThread(Socket socket, DataInputStream inputStream, DataOutputStream outputStream, String strInputstream) {
        this.socket = socket;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.strInputstream = strInputstream;
    }


    @Override
    public void run() {

        while (true) {

            try {
                strInputstream = inputStream.readUTF();
                System.out.println("接受到的数据长度为：" + strInputstream + "  端口号： " + socket.getPort());
                JSONObject json = new JSONObject(strInputstream);
                String items = (String) json.get("items");
//                if(flag == 0) {
//                    flag=1;
//                }
                SocketUtils.traverseMap(ServerRun.clients);
                switch (items) {
                    case "reg":
                        String isreged = SocketUtils.reg((String) json.get("username"), (String) json.get("passwd"));
                        Map<String, String> map = new HashMap<>();
                        if (isreged.equals("logsuccess")) {
                            map.put("items", "isreg");
                            map.put("isreg", "success");
                            map.put("tips", "注册成功");
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
//                        outputStream.close();
//                        socket.shutdownOutput();
                        break;
                    case "addfriend":
                        Map<String, String> frinedmap = new HashMap<>();
                        boolean isadded = SocketUtils.addFriend((String) json.get("friendname"), (String) json.get("username"));
                        if (isadded) {
                            frinedmap.put("items", "isaddded");
                            frinedmap.put("isadded", "success");
                            frinedmap.put("tips", "添加成功");
                            System.out.println("添加成功");
                        } else {
                            frinedmap.put("items", "isaddded");
                            frinedmap.put("isadded", "failed");
                            frinedmap.put("tips", "添加失败");
                            System.out.println("添加失败");
                        }
                        String add = SocketUtils.jsonToString(frinedmap);
                        outputStream.writeUTF(add);
                        outputStream.flush();
                        break;
                    case "message":
                        SocketUtils.sendMessage(json);
                        SocketUtils.saveMessage((String) json.get("username"), (String) json.get("message"), (String) json.get("friendname"));
                        break;
                    case "login":
                        String islog = SocketUtils.login((String) json.get("username"), (String) json.get("passwd"));
                        Map<String, String> logmap = new HashMap<>();
                        if (islog.equals("logsuccess")) {
                            logmap.put("items", "islog");
                            logmap.put("islog", "success");
                            logmap.put("tips", "登陆成功");
                            SocketUtils.setMap((String) json.get("username"), socket);
                            String friendSocket = SocketUtils.returnfriendSocket((String) json.get("username")).toString();
                            outputStream.writeUTF(friendSocket);
                        } else {
                            logmap.put("items", "islog");
                            logmap.put("islog", "failed");
                            logmap.put("tips", "登录失败");
                        }
                        String log = SocketUtils.jsonToString(logmap);
//                    SocketUtils.returnfriendSocket((String) json.get("username"));

                        outputStream.writeUTF(log);
                        outputStream.flush();
//                        outputStream.close();
//                        socket.shutdownOutput();
                        break;
                    case "logout":
                        SocketUtils.delLogusers((String) json.get("username"));
                        outputStream.close();
                        inputStream.close();
                        socket.close();
                        break;
                    default:
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }
}