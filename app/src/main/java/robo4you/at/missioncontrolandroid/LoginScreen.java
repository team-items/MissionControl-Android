package robo4you.at.missioncontrolandroid;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class LoginScreen extends ActionBarActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        Button connect = (Button)findViewById(R.id.connectbtn);
        connect.setOnClickListener(this);
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
}
