package com.shenmou.wifidirectdemo.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * @author Vincent Vincent
 * @version v1.0
 * @name WifiDirectDemo
 * @page com.shenmou.wifidirectdemo.utils
 * @class describe
 * @date 2018/8/24 16:49
 */
public class ServiceSocketHandler implements Runnable{
    private static final String TAG = "服务器线程";
    private Socket socket;
    public ServiceSocketHandler(Socket socket){
        this.socket=socket;
    }

    public void run(){
        try{
            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String info = null;
            while((info=br.readLine())!=null){
                Log.d(TAG, "我是客户端，服务器说："+info);
            }
            System.out.println("新连接:"+socket.getInetAddress()+":"+socket.getPort());
            Thread.sleep(1000);
        }catch(Exception e){e.printStackTrace();}finally{
            try{
                System.out.println("关闭连接:"+socket.getInetAddress()+":"+socket.getPort());
                if(socket!=null)socket.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

}
