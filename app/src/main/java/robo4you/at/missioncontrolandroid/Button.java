package robo4you.at.missioncontrolandroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

/**
 * Created by Raphael on 04.11.2015.
 */
public class Button extends Controller {

    private int value;
    private Context context;
    private LinearLayout layout;
    private String label;
    private Switch switchWidget;
    private TextView label_tv;

    public Button(String label, int value, final Context context) {
        this.value = value;
        this.context = context;
        this.label = label;
        generateLayout();
    }

    private void generateLayout() {
        layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.button_layout, null);
        this.label_tv = (TextView) layout.findViewById(R.id.motor_label);
        this.label_tv.setTypeface(MainActivity.getTypeface());
        label_tv.setText(label);
        this.switchWidget = (Switch) layout.findViewById(R.id.switchWidget);
    }

    public Button(String label, final Context context) {
        this.value = 0;
        this.context = context;
        this.label = label;
        generateLayout();
    }

    @Override
    public LinearLayout getLayout() {
        return layout;
    }

    @Override
    public double getValue() {
        return value;
    }

    public boolean isClicked() {
        return value == 1;
    }
}
