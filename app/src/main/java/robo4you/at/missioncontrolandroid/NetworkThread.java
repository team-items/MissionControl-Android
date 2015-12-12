package robo4you.at.missioncontrolandroid;

import android.os.Handler;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by rapha on 11.12.2015.
 */
public class NetworkThread implements Runnable {

    private Socket socket;
    private String ip;
    private int port;
    private Handler handler;
    private DataOutputStream write;
    private DataInputStream read;
    private boolean dataToSend = false;
    private String sendData = "";
    private String currentMessage;
    private boolean run = true;


    public NetworkThread(String ip, int port){
        this.ip = ip;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            this.socket = new Socket(ip, port);
            Log.e("missoncontrol","socket status: "+socket.isConnected());
            this.read = new DataInputStream(socket.getInputStream());
            this.write = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            Log.e("missioncontrol",e.getMessage());
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(read));
        while (!Thread.currentThread().isInterrupted() && run) {
            if (!dataToSend) {
                try {
                    char c;
                    StringBuilder message = new StringBuilder("");
                    while (reader.ready() && (c=(char)reader.read())!=-1){
                        message.append(c);
                    }
                    this.currentMessage = message.toString();
                    ///do something  with the message
                    Log.e("missioncontrol",message.toString());
                } catch (IOException e) {
                    Log.e("missioncontrol", e.getMessage());
                }
            }else{
                try {
                    synchronized(sendData){
                        write.writeUTF(sendData);
                    }
                    write.flush();
                    Log.e("missioncontrol", "data send");
                    sendData = "";
                    dataToSend = false;
                } catch (IOException e) {
                    Log.e("missioncontrol",e.getMessage());
                }
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
    public void sendData(String data){
        this.sendData = data;
        dataToSend = true;
    }
    public void kill(){
        this.run = false;
    }

}
