package robo4you.at.missioncontrolandroid;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.*;
import android.widget.Button;

import java.lang.reflect.Field;
import java.util.regex.Pattern;

/**
 * Created by Raphael on 08.11.2015.
 */
public class NumPicker extends Activity {

    NumberPicker numPicker;
    EditText valueET;
    double max, min;
    static TextView viewValue;
    double value;
    static Motor motor;

    public NumPicker(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.numberpicker_layout);

        this.numPicker = (NumberPicker) findViewById(R.id.numberPicker);
        this.valueET = (EditText) findViewById(R.id.valueET);
        android.widget.Button exitBtn = (Button)findViewById(R.id.saveBtn);
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                onDestroy();
            }
        });
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = (int) (dm.widthPixels * .9);
        int height = (int) (dm.heightPixels * .9);

        getWindow().setLayout(width, height);

        Bundle extras = getIntent().getExtras();
        max = extras.getDouble("max");
        min = extras.getDouble("min");
        value = extras.getDouble("value");
        Log.e("missioncontrol","value in NumPicker: "+value);
        valueET.setText("" + value);

        if(min<0){
            numPicker.setMinValue(0);
            numPicker.setMaxValue((int) (max - min));
            numPicker.setValue((int)(value-min));
            numPicker.setFormatter(new NumberPicker.Formatter() {
                @Override
                public String format(int index) {
                    motor.setValue(index+min);
                    return Integer.toString((int)(index+min));
                }
            });
        }else{
            numPicker.setMinValue((int)min);
            numPicker.setMaxValue((int) max);
            numPicker.setValue((int)value);
            numPicker.setFormatter(new NumberPicker.Formatter() {
                @Override
                public String format(int index) {
                    value = index;
                    motor.setValue(index);
                    valueET.setText(""+index);
                    viewValue.setText("" + index);
                    return Integer.toString(index);
                }
            });
        }
        numPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                value = numPicker.getValue() + (int)min;
                valueET.setText("" + (Math.round(value*10)/10));
                viewValue.setText("" + (Math.round(value*10)/10));
            }
        });
       /* valueET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (Pattern.matches("-?[0-9]+\\.?[0-9]+]?", s) && Double.parseDouble(s.toString()) >= min &&
                        Double.parseDouble(s.toString()) <= max) {
                    value = Double.parseDouble(s.toString().trim());
                    viewValue.setText(s.toString().trim());
                    numPicker.setValue((int) value);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        numPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if(min<0&&max>=0){
                    value = (Math.abs(min)+Math.abs(max))*newVal;
                    valueET.setText(""+(value));
                    viewValue.setText("" + value);
                }else{
                    value = (max-max)*newVal;
                    valueET.setText(""+(value));
                    viewValue.setText("" + value);
                }
            }
        });
        */
    }
}
