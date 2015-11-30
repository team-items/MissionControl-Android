package robo4you.at.missioncontrolandroid.Messages;

import robo4you.at.missioncontrolandroid.JSONLibary.JSONException;

/**
 * Created by rapha on 20.11.2015.
 */
public class Message {
    public static Object getMessageType(String json){
        if(json.contains("ConnACK")){
            return ConnACK.class;
        }else if(json.contains("ConnREJ")){
            return ConnREJ.class;
        }else if(json.contains("ConnREQ")){
            return ConnREQ.class;
        }else{
            throw new JSONException("JSON String not recognized!");
        }
    }
}
