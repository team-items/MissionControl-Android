package robo4you.at.missioncontrolandroid;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;

import robo4you.at.missioncontrolandroid.Messages.ConnLAO;
import robo4you.at.missioncontrolandroid.SlidingTabLayout.SlidingTabLayout;

public class ConLAOTest extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String message = "{\n" +
                "\"ConnLAO\" : {\n" +
                "\"Information\" : {\n" +
                "\"Float\" : {\n" +
                "\"SomeFloatNumber\" : {\n" +
                "\"MinBound\" : -1023.9999999,\n" +
                "\"MaxBound\" : 1023.9999999,\n" +
                "\"Graph\" : 20\n" +
                "}\n" +
                "},\n" +
                "\"Integer\" : {\n" +
                "\"SomeIntegerNumber\" : {\n" +
                "\"MinBound\" : 0,\n" +
                "\"MaxBound\" : 1023\n" +
                "}\n" +
                "}\n" +
                "},\n" +
                "\"Controller\" : {\n" +
                "\"SomeGroup\" : {\n" +
                "\"SomeSlider\" : {\n" +
                "\"ControlType\" : \"Slider\",\n" +
                "\"MinBound\" : 0,\n" +
                "\"MaxBound\" : 100,\n" +
                "\"AutoUpdate\" : \"true\"\n" +
                "},\n" +
                "\"SomeButton\" : {\n" +
                "\"ControlType\" : \"Button\",\n" +
                "\"Descriptor\" : \"Set\"\n" +
                "}\n" +
                "},\n" +
                "\"SomeOtherButton\" : {\n" +
                "\"ControlType\" : \"Button\",\n" +
                "\"Descriptor\" : \"Switch\"\n" +
                "}\n" +
                "}\n" +
                "}\n" +
                "}";
        setContentView(R.layout.display_layout);
        CharSequence Titles[]={"Sensors","Controls"};
        ViewPagerAdapter adapter =  new ViewPagerAdapter(getSupportFragmentManager(),Titles,2);
        adapter.main = this;
        // Assigning ViewPager View and setting the adapter
        ViewPager pager = (ViewPager) findViewById(R.id.pager);

        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        SlidingTabLayout tabs = (SlidingTabLayout) findViewById(R.id.tabs);
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
        ConnLAO connLAO = new ConnLAO(message, adapter);
        connLAO.generateLayout(this);

    }

}
