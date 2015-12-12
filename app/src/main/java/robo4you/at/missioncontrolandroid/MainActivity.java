package robo4you.at.missioncontrolandroid;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import java.util.TreeMap;
import robo4you.at.missioncontrolandroid.SlidingTabLayout.SlidingTabLayout;

public class MainActivity extends ActionBarActivity{

    ViewPager pager;
    static ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Sensors","Controls"};
    int Numboftabs =2;
    static float display_density;
    static Typeface font;
    static TreeMap<String,Sensor> sensorTreeMap = new TreeMap<>();
    static TreeMap<String,Motor> motorTreeMap = new TreeMap<>();
    static TreeMap<String,Button> buttonTreeMap = new TreeMap<>();
    Thread networkingThread;
    NetworkThread networkThread;
    private Toolbar toolbar;


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
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        final String ip = getIntent().getStringExtra("ip");
        final String port = getIntent().getStringExtra("port");
        networkThread = new NetworkThread(ip, Integer.parseInt(port));
        networkingThread = new Thread(networkThread);
        networkingThread.start();
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
    public static void addSensor(Sensor sensor){
        sensorTreeMap.put(sensor.getUniqueIdentifier(),sensor);
        adapter.addSensor(sensor);
    }
    public static void addMotor(Motor motor){
        motorTreeMap.put(motor.getUniqueIdentifier(), motor);
        adapter.addController(motor);
    }
    public static void addButton(Button button){
        buttonTreeMap.put(button.getUniqueIdentifier(), button);
        adapter.addController(button);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.disconnect){
            networkThread.kill();
            Intent intent = new Intent(this, LoginScreen.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
