package robo4you.at.missioncontrolandroid;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by Raphael on 10.10.2015.
 */
public class Motor extends Controller {

    String label;
    double value, max;
    final double min;
    TextView textView_label, textView_value;
    SeekBar seekBar;
    Button go;
    LinearLayout layout;
    final Context context;

    public Motor(final double min, final double max, String label, final Context context) {
        this.min = min;
        this.max = max;
        this.context = context;
        layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.motor_servo_layout, null);
        textView_label = (TextView) layout.findViewById(R.id.label);
        textView_label.setTextColor(Color.BLACK);
        textView_label.setText(label);
        textView_value = (TextView) layout.findViewById(R.id.motor_value);
        textView_value.setTextColor(Color.BLACK);
        textView_label.setTypeface(MainActivity.getTypeface());
        textView_value.setTypeface(MainActivity.getTypeface());
        textView_value.setText("" + min);
        go = (Button) layout.findViewById(R.id.go);
        seekBar = (SeekBar) layout.findViewById(R.id.seekBar);
        seekBar.getProgressDrawable().setColorFilter(context.getResources().getColor(R.color.itemsRed), PorterDuff.Mode.MULTIPLY);
        seekBar.getThumb().setColorFilter(context.getResources().getColor(R.color.itemsRed), PorterDuff.Mode.SRC_IN);
        final double unit;
        if (min >= 0) {
            unit = (max - min) / 100;
        } else {
            unit = (Math.abs(min) + Math.abs(max)) / 100;
            }
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                double motor_value = (min + unit * progress + 0.5);
                String valueText = "" + Math.round(motor_value);
                textView_value.setText(valueText);
                value = motor_value;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        this.label = label;
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send update message
            }
        });
    }
    public LinearLayout getLayout(){
        return layout;
    }

    @Override
    public double getValue() {
        return value;
    }



}
