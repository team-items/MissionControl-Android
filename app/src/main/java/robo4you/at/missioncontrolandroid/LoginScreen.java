package robo4you.at.missioncontrolandroid;

import android.content.Intent;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import java.io.IOException;
import java.net.Socket;

public class LoginScreen extends ActionBarActivity implements View.OnClickListener,QRCodeReaderView.OnQRCodeReadListener{
    Connection conn;

    private QRCodeReaderView mydecoderview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createShortCut();
        setContentView(R.layout.activity_login_screen);
        EditText ipField = (EditText)findViewById(R.id.connectionIP);
        EditText portField = (EditText)findViewById(R.id.connectionPort);
        Log.e("display", "" + getResources().getDisplayMetrics().densityDpi);
            MainActivity.font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Roboto-Thin.ttf");

        ipField.setTypeface(MainActivity.font);
        portField.setTypeface(MainActivity.font);
        Button connect = (Button)findViewById(R.id.connectbtn);
        connect.setOnClickListener(this);
        mydecoderview = (QRCodeReaderView)findViewById(R.id.qrdecoderview);
        mydecoderview.setOnQRCodeReadListener(this);
    }
    public void createShortCut(){
        Intent shortcutintent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        shortcutintent.putExtra("duplicate", false);
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "MissionControl");
        Parcelable icon = Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.icon);
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(getApplicationContext(), LoginScreen.class));
        sendBroadcast(shortcutintent);
    }

    @Override
    public void onClick(View v) {
        String validIpAddressRegex = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}↵\n" +
                "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        String validHostnameRegex = "^(?=.{1,255}$)[0-9A-Za-z](?:(?:[0-9A-Za-z]|-){0,61}[0-9A-Za-z])?(?:\\.[0-9A-Za-z](?:(?:[0-9A-Za-z]|-){0,61}[0-9A-Za-z])?)*\\.?$";
        String validPortRegex = "^([0-9]{1,4}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$";
        EditText ipField = (EditText)findViewById(R.id.connectionIP);
        EditText portField = (EditText)findViewById(R.id.connectionPort);
        final String ip = ipField.getText().toString().trim();
        final String port = portField.getText().toString().trim();
        if ((ip.matches(validHostnameRegex) || ip.matches(validIpAddressRegex) && port.matches(validPortRegex))
                && checkSocket(ip,Integer.parseInt(port))){
            Toast.makeText(getApplicationContext(),"performing handshake with: "+ip+":"+port,Toast.LENGTH_LONG).show();

            final Intent i = new Intent(this, MainActivity.class);
            i.putExtra("ip", ip);
            i.putExtra("port", port);
            Handler h = new Handler();
            h.post(new Runnable() {
                @Override
                public void run() {
                    startActivity(i);
                }
            });
        }else{
            Toast.makeText(getApplicationContext(),"wrong ip/port: "+ip+":"+port,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {

        String validIpAddressRegex = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}↵\n" +
                "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        String validHostnameRegex = "^(?=.{1,255}$)[0-9A-Za-z](?:(?:[0-9A-Za-z]|-){0,61}[0-9A-Za-z])?(?:\\.[0-9A-Za-z](?:(?:[0-9A-Za-z]|-){0,61}[0-9A-Za-z])?)*\\.?$";
        String validPortRegex = "^([0-9]{1,4}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$";
        Log.e("myapp", text);
        String ipport = text;
        String[] parts = ipport.split(":");
        String ip = parts[0];
        String port = parts[1];
        if ((ip.matches(validHostnameRegex) || ip.matches(validIpAddressRegex) && port.matches(validPortRegex)
                && checkSocket(ip,Integer.parseInt(port)))){
        Toast.makeText(getApplicationContext(),"performing handshake with: "+ip+":"+port,Toast.LENGTH_LONG).show();

        final Intent i = new Intent(this, MainActivity.class);
        i.putExtra("ip", ip);
        i.putExtra("port", port);
        Handler h = new Handler();
        h.post(new Runnable() {
            @Override
                public void run() {
                    startActivity(i);
                }
            });
        }else{
            Toast.makeText(getApplicationContext(),"wrong ip/port: "+ip+":"+port,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void cameraNotFound() {
        Log.e("Camera","Not found!");
        Toast.makeText(LoginScreen.this, "Camera not found!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void QRCodeNotFoundOnCamImage() {

    }
    @Override
    protected void onResume() {
        super.onResume();
        mydecoderview.getCameraManager().startPreview();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mydecoderview.getCameraManager().stopPreview();
    }
    public static boolean xor(boolean x, boolean y) {
        return ( ( x || y ) && ! ( x && y ) );
    }
    public static boolean checkSocket(String ip, int port){
        CheckSocket checkSocket = new CheckSocket(ip,port);
        new Thread(checkSocket).start();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return checkSocket.connected;
    }
}
class CheckSocket implements Runnable{

    int port;
    String server;
    public boolean connected = false;

    public CheckSocket(String server,int port ){
        this.port = port;
        this.server = server;
    }
    @Override
    public void run() {
        try {
            Socket s = new Socket(server,port);
            if (s.isConnected()){
                connected = true;
            }
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
