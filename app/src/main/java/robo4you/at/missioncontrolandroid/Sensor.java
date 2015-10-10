package robo4you.at.missioncontrolandroid;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.w3c.dom.Text;

/**
 * Created by Raphael on 09.10.2015.
 */
public class Sensor implements View.OnClickListener{

    boolean isDigital;
    int min, max;
    String label;
    TextView value;
    TextView labelView;
    LinearLayout sensor_layout;
    GraphView graph;
    private boolean visible = true;
    LineGraphSeries<DataPoint> series;
    private int x = 0;
    final int VALUES_TO_DISPLAY = 50;

    public Sensor(boolean isDigital, int min, int max, String label, Context context) {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.sensors_layout,null);
        layout.setClickable(true);
        graph = (GraphView)layout.findViewById(R.id.graph);

        GridLabelRenderer labelRenderer = graph.getGridLabelRenderer();
        labelRenderer.setGridColor(Color.BLACK);
        labelRenderer.setVerticalLabelsColor(Color.BLACK);
        labelRenderer.setHorizontalLabelsColor(Color.BLACK);
        labelRenderer.reloadStyles();

        value = (TextView)layout.findViewById(R.id.sensor_value);
        labelView = (TextView)layout.findViewById(R.id.sensor_name);
        Typeface font = MainActivity.getTypeface();
        value.setTypeface(font);
        labelView.setTypeface(font);

        this.isDigital = isDigital;
        this.label = label;
        labelView.setText(label);
        if(isDigital){
            this.min = 0;
            this.max = 1;
        }else{
            this.min = min;
            this.max = max;
            series = new LineGraphSeries<DataPoint>();
            series.setColor(context.getResources().getColor(R.color.itemsRed));
            graph.addSeries(series);
        }

        layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        layout.setOnClickListener(this);
        if (isDigital){
            graph.setVisibility(View.GONE);
        }else{
            layout.setOnClickListener(this);
            graph.setOnClickListener(this);
            value.setOnClickListener(this);
            labelView.setOnClickListener(this);
        }

        this.sensor_layout = layout;
    }
    public LinearLayout getLayout(){
        return sensor_layout;
    }

    @Override
    public void onClick(View v) {
        if (visible){
            graph.setVisibility(View.GONE);
            visible = false;
        }else{
            graph.setVisibility(View.VISIBLE);
            visible = true;
        }
    }
    public void addPoint(int value){
        series.appendData(new DataPoint(x++, value),true,VALUES_TO_DISPLAY);
    }
}
