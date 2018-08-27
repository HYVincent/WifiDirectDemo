package com.shenmou.wifidirectdemo.utils;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
/**
 * @author Vincent Vincent
 * @version v1.0
 * @name WifiDirectDemo
 * @page com.shenmou.wifidirectdemo.utils
 * @class describe
 * @date 2018/8/27 9:36
 */
public class Client extends Socket {

    private static final String SERVER_IP = "192.168.21.178";
    private static final int SERVER_PORT = 7777;

    private Socket client;
    private PrintWriter out;
    private BufferedReader in;

    /**
     * 与服务器连接，并输入发送消息
     */
    public Client() throws Exception {
        super(SERVER_IP, SERVER_PORT);
        client = this;
        out = new PrintWriter(this.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(this.getInputStream()));
        new readLineThread();

        while (true) {
            in = new BufferedReader(new InputStreamReader(System.in));
            String input = in.readLine();
            out.println(input);
        }
    }

    /**
     * 用于监听服务器端向客户端发送消息线程类
     */
    class readLineThread extends Thread {

        private BufferedReader buff;

        public readLineThread() {
            try {
                buff = new BufferedReader(new InputStreamReader(
                        client.getInputStream()));
                start();
            } catch (Exception e) {
            }
        }

        @Override
        public void run() {
            try {
                while (true) {
                    String result = buff.readLine();
                    if ("byeClient".equals(result)) {// 客户端申请退出，服务端返回确认退出
                        break;
                    } else {// 输出服务端发送消息
                        System.out.println(result);
                    }
                }
                in.close();
                out.close();
                client.close();
            } catch (Exception e) {
            }
        }
    }

   /* public static void main(String[] args) {
        try {
            new Client();// 启动客户端
        } catch (Exception e) {
        }
    }*/
}
