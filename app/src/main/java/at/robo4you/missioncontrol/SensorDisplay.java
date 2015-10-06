package at.robo4you.missioncontrol;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


/**
 * Created by Raphael on 04.10.2015.
 */
public class SensorDisplay extends Fragment implements View.OnClickListener{

    GraphView graph;
    TextView sensor_name;
    String sensor_text = "Sensor";
    int numberOfValue = 0;
    float sensorValue;
    LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{});
    //change
    final int NUMBER_OF_VALUES_TO_DISPLAY = 100;
    //change
    boolean visible = true;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //// TEST
        View v =inflater.inflate(R.layout.sensor_values_layout,container,false);
        graph = (GraphView)v.findViewById(R.id.graph);
        series = new LineGraphSeries<>(new DataPoint[] {
        });
        graph.addSeries(series);
        //TEST
        sensor_name = (TextView)v.findViewById(R.id.sensor_name);
        sensor_name.setText(this.sensor_text);
        v.setOnClickListener(this);
        return v;
    }


    @Override
    public void onClick(View v) {
        if (visible){
            graph.setVisibility(View.GONE);
            visible = false;
        }else {
            graph.setVisibility(View.VISIBLE);
            visible = true;
        }
    }
    public void appendPointToGraph(float value){
        series.appendData(new DataPoint(numberOfValue++,value),true,NUMBER_OF_VALUES_TO_DISPLAY);
        this.sensorValue = value;
    }
}
