package robo4you.at.missioncontrolandroid;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

/**
 * Created by Raphael on 09.10.2015.
 */
public class Sensor<T extends Number> implements View.OnClickListener {

    T min, max;
    String label;
    TextView value;
    TextView labelView;
    private LinearLayout sensor_layout;
    GraphView graph;
    private boolean visible = true;
    LineGraphSeries<DataPoint> series;
    private int x = 0;
    int values_to_display = 50;
    final Context context;
    boolean displayGraph = true;
    boolean scrollToEnd = true;

    public Sensor(T min, T max, String label, final Context context) {
        this.min = min;
        this.max = max;
        this.label = label;
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        if (displayGraph) {
            if (visible) {
                graph.setVisibility(View.GONE);
                visible = false;
            } else {
                graph.setVisibility(View.VISIBLE);
                visible = true;
            }
        }
    }

    public void hideGraph() {
        graph.setVisibility(View.GONE);
        visible = false;
        displayGraph = false;
    }

    public void addPoint(T value) {
        if (value instanceof Double){
            series.appendData(new DataPoint(x++, (Double)value), scrollToEnd, values_to_display);
        }else if (value instanceof Integer){
            series.appendData(new DataPoint(x++, (Integer)value), scrollToEnd, values_to_display);
        }
        this.value.setText("" + value);
    }

    public void setValues_to_display(int values_to_display) {
        this.values_to_display = values_to_display;
    }

    public LinearLayout generateLayout(View parent){
        LinearLayout layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.sensors_layout, null);
        layout.setClickable(true);
        int border = (int)MainActivity.pxFromDp(context,5f);
        graph = (GraphView) layout.findViewById(R.id.graph);
        ((LinearLayout.LayoutParams) graph.getLayoutParams()).setMargins(border, border, border, border);

        Viewport viewport = graph.getViewport();
        viewport.setYAxisBoundsManual(true);
        if (min instanceof Integer){
            viewport.setMinY((Integer)min);
            viewport.setMaxY((Integer)max);
        }else if (min instanceof Double){
            viewport.setMinY((Double)min);
            viewport.setMaxY((Double)max);
        }

        viewport.setXAxisBoundsManual(false);
        viewport.setScrollable(true);

        GridLabelRenderer labelRenderer = graph.getGridLabelRenderer();
        labelRenderer.setGridColor(Color.BLACK);
        labelRenderer.setVerticalLabelsColor(Color.BLACK);
        labelRenderer.setHorizontalLabelsColor(Color.TRANSPARENT);
        labelRenderer.reloadStyles();

        value = (TextView) layout.findViewById(R.id.sensor_value);
        labelView = (TextView) layout.findViewById(R.id.sensor_name);
        Typeface font = MainActivity.getTypeface();
        value.setTypeface(font);
        labelView.setTypeface(font);
        labelView.setText(label);
        series = new LineGraphSeries<DataPoint>();
        series.setColor(context.getResources().getColor(R.color.itemsRed));
        series.setBackgroundColor(context.getResources().getColor(R.color.backgroundColorGraph));
        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(context, "" + dataPoint.getY(), Toast.LENGTH_SHORT).show();
                Log.e("missioncontrol", "X:" + dataPoint.getY());
            }
        });
        graph.addSeries(series);
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(border, border, border, border);
        layout.setLayoutParams(layoutParams);
        layout.setOnClickListener(this);
        layout.setOnClickListener(this);
        //graph.setOnClickListener(this);
        value.setOnClickListener(this);
        labelView.setOnClickListener(this);

        this.sensor_layout = layout;
        return sensor_layout;
    }
    public String getUniqueIdentifier(){
        return this.label;
    }

    public View getLayout() {
        return sensor_layout;
    }
}