package robo4you.at.missioncontrolandroid;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.io.IOException;
import java.net.Socket;

import robo4you.at.missioncontrolandroid.SlidingTabLayout.SlidingTabLayout;

public class MainActivity extends ActionBarActivity{

    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Sensors","Controls"};
    int Numboftabs =2;
    static float display_density;
    static Typeface font;
    Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_layout);
        display_density = getApplicationContext().getResources().getDisplayMetrics().density;
        font = Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/Roboto-Thin.ttf");
        // Creating The Toolbar and setting it as the Toolbar for the activity

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter =  new ViewPagerAdapter(getSupportFragmentManager(),Titles,Numboftabs);
        adapter.main = this;
        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);

        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);

        final String ip = getIntent().getStringExtra("ip");
        final String port = getIntent().getStringExtra("port");
        Thread connectionThread = new Thread() {
            @Override
            public void run() {
                try {
                    socket = new Socket(ip, Integer.parseInt(port));
                } catch (IOException e) {
                    Log.e("missioncontrol","IOException: "+e.getMessage());
                }
            }
        };
        connectionThread.start();

    }

    public static float getDisplay_density(){
        return display_density;
    }
    public static Typeface getTypeface(){
        return font;
    }
    public static float dpFromPx(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }


}
