package robo4you.at.missioncontrolandroid.Messages;

import android.app.Fragment;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import java.util.Iterator;

import robo4you.at.missioncontrolandroid.Button;
import robo4you.at.missioncontrolandroid.Controller;
import robo4you.at.missioncontrolandroid.Controllers;
import robo4you.at.missioncontrolandroid.JSONLibary.JSONException;
import robo4you.at.missioncontrolandroid.JSONLibary.JSONObject;
import robo4you.at.missioncontrolandroid.MainActivity;
import robo4you.at.missioncontrolandroid.Motor;
import robo4you.at.missioncontrolandroid.R;
import robo4you.at.missioncontrolandroid.Sensor;
import robo4you.at.missioncontrolandroid.Sensors;
import robo4you.at.missioncontrolandroid.ViewPagerAdapter;

/**
 * Created by rapha on 20.11.2015.
 */
public class ConnLAO {
    private JSONObject information = new JSONObject();
    private JSONObject controller = new JSONObject();
    private ViewPagerAdapter viewPageAdapter;

    public ConnLAO(String connLAO, ViewPagerAdapter viewPagerAdapter) {
        if (!connLAO.contains("ConnLAO")) throw new JSONException("Wrong json String");
        JSONObject message = new JSONObject(connLAO).getJSONObject("ConnLAO");
        this.information = message.getJSONObject("Information");
        this.controller = message.getJSONObject("Controller");
        this.viewPageAdapter = viewPagerAdapter;
    }

    public LinearLayout generateLayout(Context context) {
        Controllers controllers = new Controllers();
        Sensors sensors = new Sensors();
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        Iterator<String> iterator = information.keys();

        Sensor sensor = null;
        //sensors
        while (iterator.hasNext()) {
            String name = iterator.next();

            JSONObject obj = information.getJSONObject(name);
            if (obj.has("MinBound")) {
                sensor = new Sensor((int) obj.getDouble("MinBound"),
                        (int) obj.getDouble("MaxBound"), name, context);

            } else {
                Iterator<String> subSensors = information.getJSONObject(name).keys();
                while (subSensors.hasNext()) {

                    String sensorName = subSensors.next();
                    Log.e("missioncontrol",sensorName);
                    JSONObject jsonObject = information.getJSONObject(name).getJSONObject(sensorName);
                    if (jsonObject.has("MinBound")) {

                        sensor = new Sensor((int) jsonObject.getDouble("MinBound"),
                                (int) jsonObject.getDouble("MaxBound"), sensorName, context);

                    }
                    if (sensor != null) {
                        layout.addView(sensor.generateLayout(sensors.getView()));
                        LinearLayout divider = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.divider, null);
                        if (!obj.has("Graph")) {
                            sensor.hideGraph();
                        }else{
                            int numbers_to_display = obj.getInt("Graph");
                            sensor.setValues_to_display(numbers_to_display);
                        }

                        layout.addView(divider);
                        sensors.sensors.add(sensor);
                        Log.e("sensor", "added");
                        MainActivity.addSensor(sensor);
                        sensor = null;
                    }
                }
            }
            if (sensor != null) {

                layout.addView(sensor.generateLayout(sensors.getView()));
                LinearLayout divider = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.divider, null);
                if (!obj.has("Graph")) {
                    sensor.hideGraph();
                }else{
                    int numbers_to_display = obj.getInt("Graph");
                    sensor.setValues_to_display(numbers_to_display);
                }
                layout.addView(divider);
                sensors.sensors.add(sensor);
                Log.e("sensor","added");
                MainActivity.addSensor(sensor);
            }
        }
        //controls
        iterator = controller.keys();
        Controller control = null;
        while (iterator.hasNext()) {
            String controlName = iterator.next();

            JSONObject controlObj = controller.getJSONObject(controlName);
            if (controlObj.has("ControlType")) {
                if (controlObj.get("ControlType").toString().equals("Button")) {
                    control = new Button(controlName, context);
                } else if (controlObj.get("ControlType").toString().equals("Slider")) {
                    control = new Motor((int) controlObj.getDouble("MinBound"),
                            (int) controlObj.getDouble("MaxBound"),
                            controlName,
                            context);
                }
            } else {
                Iterator<String> subIterator = controlObj.keys();
                while (subIterator.hasNext()) {
                    String sensorName = subIterator.next();
                    JSONObject jsonObject = controller.getJSONObject(controlName).getJSONObject(sensorName);
                    if (jsonObject.has("ControlType")) {
                        if (jsonObject.get("ControlType").toString().equals("Button")) {
                            control = new Button(sensorName, context);
                        } else if (jsonObject.get("ControlType").toString().equals("Slider")) {
                            control = new Motor((int) jsonObject.getDouble("MinBound"),
                                    (int) jsonObject.getDouble("MaxBound"),
                                    sensorName,
                                    context);
                        }
                    }
                    if (control != null) {
                        layout.addView(control.generateLayout(controllers.getView()));
                        LinearLayout divider = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.divider, null);
                        layout.addView(divider);
                        controllers.controllers.add(control);
                        if (control instanceof Button){
                            MainActivity.addButton((Button) control);
                        }else{
                            MainActivity.addMotor((Motor) control);
                        }
                        control = null;
                    }
                }
            }
            if (control != null) {
                layout.addView(control.generateLayout(controllers.getView()));
                LinearLayout divider = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.divider, null);
                layout.addView(divider);
                if (control instanceof Button){
                    MainActivity.addButton((Button) control);
                }else{
                    MainActivity.addMotor((Motor) control);
                }
                controllers.controllers.add(control);
            }
        }
        viewPageAdapter.setMotorTab(controllers);
        viewPageAdapter.setSensorsTab(sensors);
        return layout;
    }
}
