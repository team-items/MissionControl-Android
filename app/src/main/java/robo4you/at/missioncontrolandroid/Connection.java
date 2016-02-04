package robo4you.at.missioncontrolandroid;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
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
    boolean gotdata = false;
    boolean gotconlao = false;
    String ip;
    int port;
    int segmentsize;

    JSONObject obj;

    public Connection(String ip, String port, android.os.Handler handler, MainActivity activity) {
        this.ip = ip;
        this.port = Integer.parseInt(port);
        this.handler = handler;
        this.activity = activity;
    }


    public void run() {
        String msg = "";
        try {
            socket = new Socket(ip, port);
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

            while(true){
                valid = false;
                msg = "";
                while(!valid){
                    msg += ((char)in.read());
                    try{
                        new JSONObject(msg);
                        valid = true;
                    }catch(Exception e){
                        valid = false;
                    }
                }
                JSONObject data = new JSONObject(msg).getJSONObject("Data");
                Iterator<String> iterator = data.keys();
                while (iterator.hasNext()){
                    String sensorName = iterator.next();
                    final Object value = data.get(sensorName);
                    Sensor s = null;
                    for (Sensor sensor:activity.sensors){
                        if (sensor.getUniqueIdentifier().equals(sensorName)){
                            s=sensor;
                            break;
                        }
                    }

                    if (value instanceof Integer){
                        handler.post(new UpdateRunnable(s,(int)value));
                    }else if(value instanceof String && (value.equals("false")||value.equals("true"))){
                        if (value.equals("true")){
                            handler.post(new UpdateRunnable(s,1));
                        }else if (value.equals("false")){
                            handler.post(new UpdateRunnable(s,0));
                        }
                    }
                }
            }


        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
class UpdateRunnable implements Runnable{

    private Sensor s;
    private int value;

    public UpdateRunnable(Sensor s, int value){
        this.s = s;
        this.value = value;
    }
    @Override
    public void run() {
        s.addPoint(value);
    }
}
