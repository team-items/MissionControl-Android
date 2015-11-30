package robo4you.at.missioncontrolandroid.Messages;

import robo4you.at.missioncontrolandroid.JSONLibary.JSONException;
import robo4you.at.missioncontrolandroid.JSONLibary.JSONObject;

/**
 * Created by rapha on 20.11.2015.
 */
public class ConnREJ {
    private String error = "";

    public ConnREJ(String connREJ){
        if (!connREJ.contains("ConnREJ")) throw new JSONException("Wrong json String");
        JSONObject obj = new JSONObject(connREJ);
        this.error = obj.getJSONObject("ConnREQ").getString("Error");
    }
    public String getError(){
        return this.error;
    }


}
