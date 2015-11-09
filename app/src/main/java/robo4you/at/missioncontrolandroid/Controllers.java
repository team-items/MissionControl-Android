package robo4you.at.missioncontrolandroid;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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

    ArrayList<Controller> controllers = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ScrollView scrollView = new ScrollView(getActivity().getApplicationContext());
        scrollView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        int px = (int) ((50 * MainActivity.getDisplay_density()) + 0.5);
        scrollView.setPadding(0, 0, 0, px);
        LinearLayout layout = new LinearLayout(getActivity().getApplicationContext());
        layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        layout.setOrientation(LinearLayout.VERTICAL);
        for (Controller controller : this.controllers) {
            layout.addView(controller.getLayout());
        }
        scrollView.addView(layout);
        return scrollView;
    }
}
