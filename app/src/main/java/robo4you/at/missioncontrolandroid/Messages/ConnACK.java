package robo4you.at.missioncontrolandroid.Messages;

import robo4you.at.missioncontrolandroid.JSONLibary.JSONException;
import robo4you.at.missioncontrolandroid.JSONLibary.JSONObject;

public class ConnACK {

    private int SegmentSize;
    private String ChosenCrypto;

    public ConnACK(String jsonMessage) {
        if (!jsonMessage.contains("ConnACK")) {
            throw new JSONException("Wrong json String");
        }
        JSONObject obj = new JSONObject(jsonMessage);
        this.SegmentSize = obj.getJSONObject("ConnACK").getInt("SegmentSize");
        this.ChosenCrypto = obj.getJSONObject("ConnACK").getString("ChosenCrypto");
    }

    public int getSegmentSize() {
        return SegmentSize;
    }

    public void setSegmentSize(int segmentSize) {
        SegmentSize = segmentSize;
    }

    public String getChosenCrypto() {
        return ChosenCrypto;
    }

    public void setChosenCrypto(String chosenCrypto) {
        ChosenCrypto = chosenCrypto;
    }
}