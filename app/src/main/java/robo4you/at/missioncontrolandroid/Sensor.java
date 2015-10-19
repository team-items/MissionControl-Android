package robo4you.at.missioncontrolandroid;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Random;

/**
 * Created by Raphael on 09.10.2015.
 */
public class Sensor implements View.OnClickListener {

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
    Handler handler = new Handler();

    public Sensor(boolean isDigital, int min, int max, String label, Context context) {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.sensors_layout, null);
        layout.setClickable(true);
        int border = (int)MainActivity.pxFromDp(context,5f);
        graph = (GraphView) layout.findViewById(R.id.graph);
        ((LinearLayout.LayoutParams)graph.getLayoutParams()).setMargins(border,border,border,border);
        Viewport viewport = graph.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(min);
        viewport.setMaxY(max);
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

        this.isDigital = isDigital;
        this.label = label;
        labelView.setText(label);
        if (isDigital) {
            this.min = 0;
            this.max = 1;
        } else {
            this.min = min;
            this.max = max;
            series = new LineGraphSeries<DataPoint>();
            series.setColor(context.getResources().getColor(R.color.itemsRed));
            series.setBackgroundColor(context.getResources().getColor(R.color.backgroundColorGraph));
            graph.addSeries(series);
            graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);
        }
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(border, border, border, border);
        layout.setLayoutParams(layoutParams);
        layout.setOnClickListener(this);
        if (isDigital) {
            graph.setVisibility(View.GONE);
        } else {
            layout.setOnClickListener(this);
            graph.setOnClickListener(this);
            value.setOnClickListener(this);
            labelView.setOnClickListener(this);
        }
        this.sensor_layout = layout;
        Thread thread = new Thread(new UpdateGraph());
        thread.start();
    }

    public LinearLayout getLayout() {
        return sensor_layout;
    }

    @Override
    public void onClick(View v) {
        if (visible) {
            graph.setVisibility(View.GONE);
            visible = false;
        } else {
            graph.setVisibility(View.VISIBLE);
            visible = true;
        }
    }

    public void addPoint(int value) {
        series.appendData(new DataPoint(x++, value), true, VALUES_TO_DISPLAY);
        this.value.setText(""+value);
    }
    public class UpdateGraph implements Runnable{

        @Override
        public void run() {

            for (int i = 0;i<200;i++){
                handler.post(new Runnable() {
                    Random r = new Random();
                    @Override
                    public void run() {
                        addPoint(r.nextInt(max));
                    }
                });
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
