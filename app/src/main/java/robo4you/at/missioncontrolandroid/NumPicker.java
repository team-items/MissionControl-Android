package robo4you.at.missioncontrolandroid;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.widget.EditText;
import android.widget.NumberPicker;

import java.util.regex.Pattern;

/**
 * Created by Raphael on 08.11.2015.
 */
public class NumPicker extends Activity {

    NumberPicker numPicker;
    EditText valueET;
    double max, min, value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.numberpicker_layout);

        this.numPicker = (NumberPicker) findViewById(R.id.numberPicker);
        this.valueET = (EditText) findViewById(R.id.valueET);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = (int) (dm.widthPixels * .9);
        int height = (int) (dm.heightPixels * .9);

        getWindow().setLayout(width, height);

        Bundle extras = getIntent().getExtras();
        max = extras.getDouble("max");
        min = extras.getDouble("min");
        value = extras.getDouble("value");
        numPicker.setValue((int) value);
        valueET.setText("" + (Math.round(value * 1000) / 1000));
        numPicker.setMaxValue((int) max);
        numPicker.setMinValue((int) min);

        numPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                valueET.setText("" + newVal);
                value = newVal;
            }
        });
        valueET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (Pattern.matches("-?[0-9]+\\.?[0-9]+]?", s) && Double.parseDouble(s.toString()) >= min &&
                        Double.parseDouble(s.toString()) <= max) {
                    value = Double.parseDouble(s.toString().trim());
                    numPicker.setValue((int) value);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }
}
