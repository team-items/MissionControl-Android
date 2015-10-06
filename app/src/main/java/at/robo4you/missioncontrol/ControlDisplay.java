package at.robo4you.missioncontrol;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by Raphael on 05.10.2015.
 */
public class ControlDisplay extends Fragment {

    TextView label;
    TextView value;
    SeekBar seekBar;
    private int gear_value;
    String motor_name = "Motor";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.control_layout,container,false);
        this.label = (TextView)view.findViewById(R.id.sensor_name);
        this.value = (TextView)view.findViewById(R.id.sensor_value_display);
        label.setText(this.motor_name);
        this.seekBar = (SeekBar)view.findViewById(R.id.sensor_value);

        final int step = 1;
        final int max = 2046;
        final int min = 0;
        final int adjustment_value = -1023;

        seekBar.setMax(max);
        value.setText("" + adjustment_value);
        this.gear_value = adjustment_value;

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int sensor_value = min + (progress * step) + adjustment_value;
                value.setText("" + sensor_value);
                gear_value = sensor_value;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        return view;
    }
}
