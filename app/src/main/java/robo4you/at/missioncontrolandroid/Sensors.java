package robo4you.at.missioncontrolandroid;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.ArrayList;

public class Sensors extends Fragment{

    ArrayList<Sensor> sensors = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ScrollView scrollView = new ScrollView(getActivity().getApplicationContext());
        scrollView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        int bottom = (int) ((50 * MainActivity.getDisplay_density()) + 0.5);
        int top = (int) ((55 * MainActivity.getDisplay_density()) + 0.5);
        scrollView.setPadding(0, top, 0, bottom);
        LinearLayout layout = new LinearLayout(getActivity().getApplicationContext());
        layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        layout.setOrientation(LinearLayout.VERTICAL);
        scrollView.setBackgroundColor(getResources().getColor(R.color.backgroundColorGraph));
        for (Sensor sensor:this.sensors){
            layout.addView(sensor.generateLayout(layout));
        }
        scrollView.addView(layout);
        return scrollView;
    }
}
