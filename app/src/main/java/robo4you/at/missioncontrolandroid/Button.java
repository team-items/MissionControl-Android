package robo4you.at.missioncontrolandroid;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private TextView label_tv;
    private android.widget.Button button;

    public Button(String label, int value, final Context context) {
        this.value = value;
        this.context = context;
        this.label = label;
    }

    public LinearLayout generateLayout(View parent) {
        layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.button_layout, null);
        this.label_tv = (TextView) layout.findViewById(R.id.motor_label);
        this.label_tv.setTypeface(MainActivity.getTypeface());
        label_tv.setText(label);

        this.button = (android.widget.Button)layout.findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            private boolean status = true;
            @Override
            public void onClick(View v) {
                if(status){
                    button.setBackgroundColor(-1);
                    button.setTextColor(context.getResources().getColor(R.color.itemsRed));
                    button.setText("0");
                    value = 0;
                }else{
                    button.setBackgroundColor(context.getResources().getColor(R.color.itemsRed));
                    button.setTextColor(-1);
                    button.setText("1");
                    value = 1;
                }
                status = !status;
            }
        });

        return layout;
    }

    public Button(String label, final Context context) {
        this.value = 0;
        this.context = context;
        this.label = label;
    }

    public LinearLayout getLayout() {
        return layout;
    }

    public double getValue() {
        return value;
    }

    public boolean isClicked() {
        return value == 1;
    }

}
