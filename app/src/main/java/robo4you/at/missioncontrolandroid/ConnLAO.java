package robo4you.at.missioncontrolandroid;

import android.app.Fragment;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;


/**
 * Created by rapha on 20.11.2015.
 */
public class ConnLAO {
    private JSONObject information = new JSONObject();
    private JSONObject controller = new JSONObject();
    private ViewPagerAdapter viewPageAdapter;

    public ConnLAO(JSONObject connLAO, ViewPagerAdapter viewPagerAdapter) {
        try {
            Log.e("connLAO",connLAO.toString());
            this.information = connLAO.getJSONObject("ConnLAO").getJSONObject("Information");
            this.controller = connLAO.getJSONObject("ConnLAO").getJSONObject("Controller");
            Log.e("missioncontrol","test");
            this.viewPageAdapter = viewPagerAdapter;
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public Object[] generateLayout(Context context) throws JSONException {
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
            if (obj.has("DataType")) {
                if (obj.has("MinBound")){
                    sensor = new Sensor((int) obj.getDouble("MinBound"),
                            (int) obj.getDouble("MaxBound"), name, context);
                }else{
                    sensor = new Sensor(0,1, name, context);
                }
            } else {
                Iterator<String> subSensors = information.getJSONObject(name).keys();
                while (subSensors.hasNext()) {

                    String sensorName = subSensors.next();
                    Log.e("missioncontrol",sensorName);
                    JSONObject jsonObject = information.getJSONObject(name).getJSONObject(sensorName);
                    if (jsonObject.has("MinBound")) {

                        sensor = new Sensor((int) jsonObject.getDouble("MinBound"),
                                (int) jsonObject.getDouble("MaxBound"), sensorName, context);

                    }else{
                        sensor = new Sensor(0,1, sensorName, context);
                    }
                    if (sensor != null) {
                        layout.addView(sensor.generateLayout(sensors.getView()));
                        LinearLayout divider = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.divider, null);
                        if (!jsonObject.has("Graph")) {
                            sensor.hideGraph();
                        }else{
                            int numbers_to_display = jsonObject.getInt("Graph");
                            sensor.setValues_to_display(numbers_to_display);
                        }

                        layout.addView(divider);
                        sensors.sensors.add(sensor);
                        Log.e("sensor", "added");
                        //MainActivity.addSensor(sensor);
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
                Log.e("sensor", "added");
                //MainActivity.addSensor(sensor);
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
                    control = new Motor((int) controlObj.getDouble("MinBound"), (int) controlObj.getDouble("MaxBound"), controlName, context, true);
                }

            } else {
                Iterator<String> subIterator = controlObj.keys();
                while (subIterator.hasNext()) {
                    String sensorName = subIterator.next();
                    JSONObject jsonObject = controller.getJSONObject(controlName).getJSONObject(sensorName);
                    if (jsonObject.has("ControlType")) {
                        if (jsonObject.get("ControlType").toString().equals("Button")) {
                            control = null;
                        } else if (jsonObject.get("ControlType").toString().equals("Slider")) {
                            control = new Motor((int) jsonObject.getDouble("MinBound"),
                                    (int) jsonObject.getDouble("MaxBound"),
                                    sensorName,
                                    context, false);
                        }
                    }
                    if (control != null) {
                        layout.addView(control.generateLayout(controllers.getView()));
                        LinearLayout divider = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.divider, null);
                        layout.addView(divider);
                        controllers.controllers.add(control);
                        if (control instanceof Button){
                            //MainActivity.addButton((Button) control);
                        }else{
                            //MainActivity.addMotor((Motor) control);
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
                    //MainActivity.addButton((Button) control);
                }else{
                    //MainActivity.addMotor((Motor) control);
                }
                controllers.controllers.add(control);
            }
        }
        Log.e("viewpage",""+ (viewPageAdapter == null));
        viewPageAdapter.setMotorTab(controllers);
        viewPageAdapter.setSensorsTab(sensors);
        return new Object[]{layout, sensors.sensors};
    }
}

