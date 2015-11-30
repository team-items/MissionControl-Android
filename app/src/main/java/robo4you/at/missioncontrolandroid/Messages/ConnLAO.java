package robo4you.at.missioncontrolandroid.Messages;

import android.content.Context;
import android.widget.LinearLayout;
import java.util.Iterator;

import robo4you.at.missioncontrolandroid.Button;
import robo4you.at.missioncontrolandroid.Controller;
import robo4you.at.missioncontrolandroid.JSONLibary.JSONException;
import robo4you.at.missioncontrolandroid.JSONLibary.JSONObject;
import robo4you.at.missioncontrolandroid.MainActivity;
import robo4you.at.missioncontrolandroid.Motor;
import robo4you.at.missioncontrolandroid.Sensor;

/**
 * Created by rapha on 20.11.2015.
 */
public class ConnLAO {
    private JSONObject information = new JSONObject();
    private JSONObject controller = new JSONObject();

    public ConnLAO(String connLAO) {
        if (!connLAO.contains("ConnLAO")) throw new JSONException("Wrong json String");
        JSONObject message = new JSONObject(connLAO).getJSONObject("ConnLAO");
        this.information = message.getJSONObject("Information");
        this.controller = message.getJSONObject("Controller");
    }

    public LinearLayout generateLayout(Context context) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        Iterator<String> iterator = information.keys();
        Sensor sensor = null;
        //sensors
        while (iterator.hasNext()) {
            String name = iterator.next();
            JSONObject obj = information.getJSONObject(name);
            if (obj.has("DataType")) {
                sensor = new Sensor((int) obj.getDouble("min"),
                        (int) obj.getDouble("max"), name, context);
                if (!obj.has("Graph")) {
                    sensor.hideGraph();
                }else{
                    int numbers_to_display = obj.getInt("Graph");
                    sensor.setValues_to_display(numbers_to_display);
                }
            } else {
                Iterator<String> subSensors = information.getJSONObject(name).keys();
                while (subSensors.hasNext()) {
                    String sensorName = iterator.next();
                    JSONObject jsonObject = information.getJSONObject(name);
                    if (obj.has("DataType")) {
                        sensor = new Sensor((int) jsonObject.getDouble("min"),
                                (int) jsonObject.getDouble("max"), sensorName, context);
                        if (!obj.has("Graph")) {
                            sensor.hideGraph();
                        }else{
                            int numbers_to_display = obj.getInt("Graph");
                            sensor.setValues_to_display(numbers_to_display);
                        }
                    }
                    if (sensor != null) {
                        layout.addView(sensor.getLayout());
                        MainActivity.addSensor(sensor);
                        sensor = null;
                    }
                }
            }
            if (sensor != null) {
                layout.addView(sensor.getLayout());
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
                    control = new Motor((int) controlObj.getDouble("min"),
                            (int) controlObj.getDouble("max"),
                            controlName,
                            context);
                }
            } else {
                Iterator<String> subIterator = controlObj.keys();
                while (subIterator.hasNext()) {
                    String sensorName = iterator.next();
                    JSONObject jsonObject = controller.getJSONObject(sensorName);
                    if (jsonObject.has("ControlType")) {
                        if (jsonObject.get("ControlType").toString().equals("Button")) {
                            control = new Button(controlName, context);
                        } else if (jsonObject.get("ControlType").toString().equals("Slider")) {
                            control = new Motor((int) jsonObject.getDouble("min"),
                                    (int) jsonObject.getDouble("max"),
                                    sensorName,
                                    context);
                        }
                    }
                    if (control != null) {
                        layout.addView(control.getLayout());
                        MainActivity.addController(control);
                        control = null;
                    }
                }
            }
            if (control != null) {
                layout.addView(control.getLayout());
                MainActivity.addController(control);
            }
        }
        return layout;
    }
}

