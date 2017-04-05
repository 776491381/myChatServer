package cn.fyypumpkin.test;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * Created by fyy on 4/5/17.
 */
public class InputThread extends  Thread {

    DataInputStream inputStream = null;

    public InputThread(DataInputStream inputStream){
        this.inputStream = inputStream;
    }

    @Override
    public void run() {
        try{

            while (true){
                String strInputstream = "";
                strInputstream = inputStream.readUTF();
                System.out.println("输入信息为：" + strInputstream);
            }


        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
