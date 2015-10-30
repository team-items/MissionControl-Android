package robo4you.at.missioncontrolandroid;

import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;

import java.util.regex.Pattern;

public class LoginScreen extends ActionBarActivity implements View.OnClickListener,QRCodeReaderView.OnQRCodeReadListener{


    private QRCodeReaderView mydecoderview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        Button connect = (Button)findViewById(R.id.connectbtn);
        connect.setOnClickListener(this);
        //mydecoderview = (QRCodeReaderView)findViewById(R.id.qrdecoderview);
        //mydecoderview.setOnQRCodeReadListener(this);
    }

    @Override
    public void onClick(View v) {
        String validIpAddressRegex = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}" +
                "([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$";
        String validHostnameRegex = "^(([a-zA-Z0-9]|[a-zA-Z0-9][a-zA-Z0-9\\-]*[a-zA-Z0-9])\\\\.)*" +
                "([A-Za-z0-9]|[A-Za-z0-9][A-Za-z0-9\\-]*[A-Za-z0-9])$";
        String validPortRegex = "^0*(?:6553[0-5]|655[0-2][0-9]|65[0-4][0-9]{2}|6[0-4][0-9]{3}|[1-5]" +
                "[0-9]{4}|[1-9][0-9]{1,3}|[0-9])$";
        EditText ipField = (EditText)findViewById(R.id.connectionIP);
        EditText portField = (EditText)findViewById(R.id.connectionPort);
        final String ip = ipField.getText().toString().trim();
        final String port = portField.getText().toString().trim();
        boolean isIP = Pattern.matches(validIpAddressRegex,ip);
        boolean isHostname = Pattern.matches(validHostnameRegex,ip);
        if (xor(isHostname,isIP) && Pattern.matches(validPortRegex,port)){
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    Intent i = new Intent(getApplicationContext(),MainActivity.class);
                    i.putExtra("ip",ip);
                    i.putExtra("port",port);
                    startActivity(i);
                }
            }, 0);
        }else{
            Toast.makeText(getApplicationContext(),"Wrong connection: "+ip+":"+port,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        Log.e("myapp", text);
        Toast.makeText(LoginScreen.this, text, Toast.LENGTH_SHORT).show();
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
        //mydecoderview.getCameraManager().startPreview();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //mydecoderview.getCameraManager().stopPreview();
    }
    public static boolean xor(boolean x, boolean y) {
        return ( ( x || y ) && ! ( x && y ) );
    }
}
