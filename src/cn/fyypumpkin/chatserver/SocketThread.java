package cn.fyypumpkin.chatserver;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.oracle.tools.packager.Log;
import org.json.*;

/**
 * Created by fyy on 3/28/17.
 */
public class SocketThread extends Thread {
    private Socket socket = null;

    public SocketThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            System.out.println("Server has start");
            DataInputStream inputStream = null;
            DataOutputStream outputStream = null;
            String strInputstream = "";
            inputStream = SocketUtils.getInStream(socket);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] by = new byte[2048];
            int n;
            while ((n = inputStream.read(by)) != -1) {
                baos.write(by, 0, n);
            }
            strInputstream = new String(baos.toByteArray());
            System.out.println("接受到的数据长度为：" + strInputstream);
            socket.shutdownInput();
//                inputStream.close();  //这样写socket也会关闭
            baos.close();

            JSONObject json = new JSONObject(strInputstream);
            String items = (String) json.get("items");
            System.out.println(items);

            switch (items) {
                case "reg":
                    boolean isreged = SocketUtils.reg((String) json.get("username"), (String) json.get("passwd"));
                    outputStream = SocketUtils.getOutStream(socket);
                    Map<String, String> map = new HashMap<String, String>();
                    if (isreged == true) {
                        map.put("isreged", "success");
                        System.out.println("注册完成");
                    }else{
                        map.put("isreged","failed");
                        System.out.println("注册失败");
                    }

                    String reg = SocketUtils.jsonToString(map);
                    outputStream.writeUTF(reg);
                    outputStream.flush();
                    outputStream.close();
                    break;
                case "addfriend":
                    ;
                    break;
                case "message":
                    ;
                    break;
                case "login":
                    ;
                    break;
                default:
                    ;
                    break;
            }


        } catch (IOException e) {
            System.out.println("IO Error " + e.getMessage());
//        e.printStackTrace();

        } catch (JSONException e) {
            System.out.println("json Error " + e.getMessage());
//        e.printStackTrace();

        } finally {

        }


    }


}
