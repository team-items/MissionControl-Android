package robo4you.at.missioncontrolandroid;

import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;

public class LoginScreen extends ActionBarActivity implements View.OnClickListener,QRCodeReaderView.OnQRCodeReadListener{


    private QRCodeReaderView mydecoderview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        Button connect = (Button)findViewById(R.id.connectbtn);
        connect.setOnClickListener(this);
        mydecoderview = (QRCodeReaderView)findViewById(R.id.qrdecoderview);
        mydecoderview.setOnQRCodeReadListener(this);
    }

    @Override
    public void onClick(View v) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
            }
        }, 0);
    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        Log.e("myapp",text);
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
        mydecoderview.getCameraManager().startPreview();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mydecoderview.getCameraManager().stopPreview();
    }
}
