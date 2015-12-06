package robo4you.at.missioncontrolandroid;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import robo4you.at.missioncontrolandroid.Messages.ConnLAO;

public class ConLAOTest extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String message = "{\n" +
                "\"ConnLAO\" : {\n" +
                "\"Information\" : {\n" +
                "\"SomeFloatNumber\" : {\n" +
                "\"DataType\" : \"Float\",\n" +
                "\"MinBound\" : -1023.9999999,\n" +
                "\"MaxBound\" : 1023.9999999,\n" +
                "\"Graph\" : 20\n" +
                "},\n" +
                "\"Integer\" : {\n" +
                "\"SomeIntegerNumber\" : {\n" +
                "\"DataType\" : \"Integer\",\n" +
                "\"MinBound\" : 0,\n" +
                "\"MaxBound\" : 1023\n" +
                "}\n" +
                "},\n" +
                "},\n" +
                "\"Controller\" : {\n" +
                "\"SomeGroup\" : {\n" +
                "\"SomeSlider\" : {\n" +
                "\"ControlType\" : \"Slider\",\n" +
                "\"MinBound\" : 0,\n" +
                "\"MaxBound\" : 100\n" +
                "},\n" +
                "\"SomeButton\" : {\n" +
                "\"ControlType\" : \"Button\"\n" +
                "}\n" +
                "},\n" +
                "\"SomeOtherButton\" : {\n" +
                "\"ControlType\" : \"Button\"\n" +
                "}\n" +
                "}\n" +
                "}\n" +
                "}";
        ConnLAO connLAO = new ConnLAO(message);
        setContentView(connLAO.generateLayout(getApplicationContext()));


    }

}
