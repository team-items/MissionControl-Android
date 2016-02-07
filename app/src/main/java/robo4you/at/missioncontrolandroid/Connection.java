package robo4you.at.missioncontrolandroid;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Iterator;
import java.util.logging.Handler;

/**
 * Created by Florian on 01.02.2016.
 */
public class Connection extends Thread {
    Socket socket;
    PrintWriter out;
    BufferedReader in;
    android.os.Handler handler;
    MainActivity activity;
    boolean gotconlao = false;
    String ip;
    int port;
    int segmentsize;
    private boolean run = true;

    JSONObject obj;

    public Connection(String ip, String port, android.os.Handler handler, MainActivity activity) {
        this.ip = ip;
        this.port = Integer.parseInt(port);
        this.handler = handler;
        this.activity = activity;
    }
    public void kill(){
        run = false;
    }


    public void run() {
        String msg = "";
        try {
            socket = new Socket(ip, port);
            Log.e("connection",""+socket.isConnected());
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //Begin handshake - step 1

            out.println("{\"ConnREQ\" : {\"HardwareType\" : \"Smartphone\",\"PreferredCrypto\" : \"None\",\"SupportedDT\" : [\"Bool\", \"String\", \"Integer\", \"Slider\", \"Button\"]}}");

            //step 2    achtung noch auf conrej checken

            boolean valid = false;
            while (!valid) {
                msg += ((char) in.read());
                try {
                    new JSONObject(msg);
                    valid = true;
                } catch (Exception e) {
                    valid = false;
                }
            }

            obj = new JSONObject(msg);

            try {
                segmentsize = obj.getJSONObject("ConnACK").getInt("SegmentSize");
                Log.e("segmentsize",""+segmentsize);
                //step 3

                msg = "";
                valid = false;
                while (!valid) {
                    msg += ((char) in.read());
                    try {
                        new JSONObject(msg);
                        valid = true;
                    } catch (Exception e) {
                        valid = false;
                    }
                }
                Log.e("connLAO",msg);
                obj = new JSONObject(msg);
                gotconlao = true;
                //step 4

                out.println("{\"ConnSTT\":{}}");

            } catch (Exception e) {
                System.err.println(e);
            }

            //getting datas
            InputStream inputStream = socket.getInputStream();
            Long start;
            while(run){
                if (inputStream.available()>0) {
                    byte[] updateMsg = new byte[segmentsize];
                    start = System.currentTimeMillis();
                    int bytesRead = inputStream.read(updateMsg);
                    Log.e("bytes",""+bytesRead);
                    if (bytesRead>8){
                        msg = new String(updateMsg, "UTF-8").trim();
                        if (msg.length()>0 && isFullMessage(msg)) {
                            Log.e("message",msg);
                            JSONObject data = new JSONObject(msg).getJSONObject("Data");
                            Iterator<String> iterator = data.keys();
                            while (iterator.hasNext()) {
                                String sensorName = iterator.next();
                                Log.e("sensor", sensorName);
                                Object value = data.get(sensorName);
                                Sensor s = activity.sensorTreeMap.get(sensorName);
                                if (value instanceof Integer) {
                                    handler.post(new UpdateRunnable(s, (int) value));
                                } else if (value instanceof String && (value.equals("false") || value.equals("true"))) {
                                    if (value.equals("true")) {
                                        handler.post(new UpdateRunnable(s, 1));
                                    } else if (value.equals("false")) {
                                        handler.post(new UpdateRunnable(s, 0));
                                    }
                                } else if (value instanceof Double) {
                                    handler.post(new UpdateRunnable(s, (double) value));
                                }
                            }
                            Log.e("time", "" + (System.currentTimeMillis() - start));
                        }
                    }
                }else{
                    Thread.sleep(10);
                }
            }
        } catch (Exception e) {
            Log.e("error",e.toString());
            e.printStackTrace(System.out);
        }finally {
            if (in!=null) try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (out!=null)out.close();
            if (socket!=null) try {
                socket.close();
            } catch (IOException e) {
                Log.e("error", e.toString());
            }
        }
    }
    public static boolean isFullMessage(String message){

        int open = 0;
        int close = 0;

        for (char c:message.toCharArray()){
            if (c=='{')open++;
            else if (c=='}')close++;
        }
        if (open==close&&open!=0){
            try {
                new JSONObject(message);
                return true;
            } catch (JSONException e) {
                Log.e("error","failed to parse");
                return false;
            }
        }
        return false;
    }
    public void sendMessage(String message){
        out.println(message);
    }
}
class UpdateRunnable<T extends Number> implements Runnable{

    private Sensor s;
    private T value;

    public UpdateRunnable(Sensor s, T value){
        this.s = s;
        this.value = value;
    }
    @Override
    public void run() {
        s.addPoint(value);
    }
}
