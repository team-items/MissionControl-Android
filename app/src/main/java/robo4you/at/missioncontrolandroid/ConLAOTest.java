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
                "\"SomeFloatNumber2\" : {\n" +
                "\"DataType\" : \"Float\",\n" +
                "\"MinBound\" : -1023.9999999,\n" +
                "\"MaxBound\" : 1023.9999999,\n" +
                "}\n" +
                "},\n" +
                "\"Controller\" : {\n" +
                "}\n" +
                "}\n" +
                "}";
        ConnLAO connLAO = new ConnLAO(message);
        setContentView(connLAO.generateLayout(getApplicationContext()));


    }

}
