package robo4you.at.missioncontrolandroid;

import android.os.Handler;
import android.util.Log;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Scanner;

import robo4you.at.missioncontrolandroid.JSONLibary.JSONObject;

/**
 * Created by rapha on 11.12.2015.
 */
public class NetworkThread implements Runnable {

    private Socket socket;
    private String ip;
    private int port;
    private Handler handler;
    private PrintWriter write;
    private InputStream read;
    private boolean dataToSend = false;
    private String sendData = "";
    private String currentMessage;
    private StringBuffer readBuffer = new StringBuffer();
    private boolean run = true;
    private int segmentSize = 999999;


    public NetworkThread(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            Log.e("missoncontrol", " before socket status:");
            this.socket = new Socket(ip, port);
            Log.e("missoncontrol", "socket status: " + socket.isConnected());
            this.read = socket.getInputStream();
            this.write = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            Log.e("missioncontrol", e.getMessage());
        }
        byte[] bytes = new byte[segmentSize];
        StringBuffer message = new StringBuffer();
        Scanner scan = new Scanner(read);
        while (!Thread.currentThread().isInterrupted() && run) {

            if (!dataToSend) {
                    int bracets = 0;
                    message = new StringBuffer().append(readBuffer);
                    if (scan.hasNextLine()) {
                        boolean first = true;
                        while (bracets!=0 || first){
                            String line = "";
                            if (scan.hasNextLine()){
                                line = scan.nextLine();
                                for (char c:line.toCharArray()){
                                    if(c=='{')bracets++;
                                    else if (c=='}')bracets--;
                                    if (bracets==0){
                                        readBuffer.append(c);
                                        Log.e("message", message.toString());
                                        message = new StringBuffer();
                                        first = true;
                                    }
                                    message.append(c);

                                }
                            }
                        }
                    }
                    //Log.e("new", message.toString());


                /**
                 try {
                 message.append(new String(bytes, "UTF-8"));
                 Log.e("network", "new: "+isfullMessage(message.toString()));
                 } catch (UnsupportedEncodingException e) {
                 e.printStackTrace();
                 }
                 */
            } else if (dataToSend) {
                write.print(sendData);
                write.flush();
                sendData = "";
                //write.flush();
                dataToSend = false;
            }
        }
        try {
            read.close();
            write.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendData(String data) {
        this.sendData = data;
        dataToSend = true;
    }

    public static boolean isfullMessage(String currentMessage) {
        int openBracets = 0;
        int closeBracets = 0;
        for (int i = 0; i < currentMessage.length(); i++) {
            if (currentMessage.charAt(i) == '{') {
                openBracets++;
            } else if (currentMessage.charAt(i) == '}') {
                closeBracets++;
            }
        }
        return openBracets == closeBracets;
    }

    public void kill() {
        this.run = false;
    }

}
