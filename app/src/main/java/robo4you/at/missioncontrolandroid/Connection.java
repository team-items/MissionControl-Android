package robo4you.at.missioncontrolandroid;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Florian on 01.02.2016.
 */
public class Connection extends Thread {
    Socket socket;
    PrintWriter out;
    BufferedReader in;

    boolean gotconlao = false;
    String ip;
    int port;
    int segmentsize;

    JSONObject obj;

    public Connection(String ip, String port) {
        this.ip = ip;
        this.port = Integer.parseInt(port);
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
                obj = new JSONObject(msg);
                gotconlao = true;
                //step 4

                out.println("{\"ConnSTT\":{}}");

            } catch (Exception e) {
                System.err.println(e);
            }

        } catch (Exception e) {
            System.err.println(e);
        }
    }
}