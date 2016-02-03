package robo4you.at.missioncontrolandroid;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.ArrayList;

/**
 * Created by Raphael on 10.10.2015.
 */
public class Controllers extends Fragment {

    public ArrayList<Controller> controllers = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ScrollView scrollView = new ScrollView(getActivity().getApplicationContext());
        scrollView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        int px = (int) ((50 * MainActivity.getDisplay_density()) + 0.5);
        scrollView.setPadding(0, 100, 0, px);
        LinearLayout layout = new LinearLayout(getActivity().getApplicationContext());
        layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        layout.setOrientation(LinearLayout.VERTICAL);
        Log.e("missioncontrol",""+controllers.size());
        for (Controller controller : this.controllers) {
            if (controller instanceof Motor){
                Log.e("missioncontrol", ((Motor) controller).getUniqueIdentifier());
                layout.addView(controller.generateLayout(layout));
            }else if (controller instanceof Button){
                Log.e("missioncontrol",((Button) controller).getUniqueIdentifier());
                layout.addView(controller.generateLayout(layout));
            }
            LinearLayout divider = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.divider, null);
            layout.addView(divider);
        }
        scrollView.addView(layout);
        return scrollView;
    }
}
