package robo4you.at.missioncontrolandroid;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
public class Motor extends Controller{

    private String label;
    private double value, max;
    private final double min;
    TextView textView_label, textView_value;
    SeekBar seekBar;
    Button go;
    LinearLayout layout;
    final Context context;
    boolean isslider;

    public Motor(final double minValue, final double maxValue, final String label, final Context context, boolean isslider) {
        this.min = minValue;
        this.max = maxValue;
        this.context = context;
        this.value = minValue;
        this.label = label;
        this.isslider = isslider;
    }

    public void hideButton(){
        go.setVisibility(View.INVISIBLE);
    }

    @Override
    public LinearLayout generateLayout(View parent) {
        layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.motor_servo_layout, null);
        textView_label = (TextView) layout.findViewById(R.id.label);
        textView_label.setTextColor(Color.BLACK);
        textView_label.setText(label);
        textView_value = (TextView) layout.findViewById(R.id.motor_value);
        textView_value.setTextColor(Color.BLACK);
        textView_label.setTypeface(MainActivity.getTypeface());
        textView_value.setTypeface(MainActivity.getTypeface());
        textView_value.setText("" + min);
        NumPicker.motor = this;
        textView_value.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NumPicker.viewValue = textView_value;
                Intent intent = new Intent(context.getApplicationContext(),NumPicker.class);
                intent.putExtra("min",min);
                intent.putExtra("max",max);
                intent.putExtra("value",value);
                context.startActivity(intent);
            }
        });

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
                double motor_value = (min + unit * progress);
                String valueText = "" + (Math.round(motor_value*10f)/10f);
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
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send update message
            }
        });

        if(isslider){
            hideButton();
        }

        return layout;
    }

    public void setValue(double value){
        this.value = value;
        Log.e("missioncontrol","change value to: "+value);
    }
    public String getUniqueIdentifier(){
        return this.label;
    }

}