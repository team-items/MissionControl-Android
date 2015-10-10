package robo4you.at.missioncontrolandroid;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by Raphael on 10.10.2015.
 */
public class Motor {

    String label;
    int value,max;
    final int min;
    TextView textView_label, textView_value;
    SeekBar seekBar;
    Button go;
    LinearLayout layout;

    public Motor(final int min, int max, String label, final Context context) {
        this.min = min;
        this.max = max;
        this.label = label;
        LinearLayout layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.motor_servo_layout,null);

        textView_label =  (TextView)layout.findViewById(R.id.label);
        textView_label.setTextColor(Color.BLACK);
        textView_label.setText(label);
        textView_value =  (TextView)layout.findViewById(R.id.motor_value);
        textView_value.setTextColor(Color.BLACK);
        textView_label.setTypeface(MainActivity.getTypeface());
        go = (Button) layout.findViewById(R.id.go);
        seekBar = (SeekBar)layout.findViewById(R.id.seekBar);
        Typeface font = MainActivity.getTypeface();
        textView_label.setTypeface(font);
        textView_value.setTypeface(font);
        final float unit;
        if (min>=0){
            unit = ((float)max-(float)min)/100;
        }else{
            unit = ((float)Math.abs(min)+(float)Math.abs(max))/100;
        }
        Log.e("Unit",""+unit);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int sensor_value = (int)(min+unit*progress+0.5);
                Log.e("Progress:", "" + progress);
                textView_value.setText(""+(int)(min+unit*progress+0.5));
                value = (int)(min+unit*progress+0.5);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send update message
            }
        });
        textView_value.setText("");
        this.layout = layout;
    }
    public LinearLayout getLayout(){
        return layout;
    }
}
