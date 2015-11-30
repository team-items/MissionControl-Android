package robo4you.at.missioncontrolandroid.Messages;

import robo4you.at.missioncontrolandroid.JSONLibary.JSONArray;
import robo4you.at.missioncontrolandroid.JSONLibary.JSONException;
import robo4you.at.missioncontrolandroid.JSONLibary.JSONObject;

/**
 * Created by rapha on 16.11.2015.
 */
public class ConnREQ {
    private String hardwareType = "";
    private JSONArray supportedCrypto = new JSONArray();
    private String preferredCrypto = "";
    private JSONArray supportedDT = new JSONArray();
    private String password = "";

    public ConnREQ(String connREQ) throws JSONException {
        if (!connREQ.contains("ConnREQ")) throw new JSONException("Wrong json String");
        try {
            JSONObject obj = new JSONObject(connREQ);
            this.hardwareType = obj.getJSONObject("ConnREQ").getString("HardwareType");
            this.supportedCrypto = obj.getJSONObject("ConnREQ").getJSONArray("SupportedCrypto");
            this.preferredCrypto = obj.getJSONObject("ConnREQ").getString("PreferredCrypto");
            this.supportedDT = obj.getJSONObject("ConnREQ").getJSONArray("SupportedDT");
            this.password = obj.getJSONObject("ConnREQ").getString("Password");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ConnREQ(String hardwareType, JSONArray supportedCrypto, String preferredCrypto,
                   JSONArray supportedDT, String password) {
        this.hardwareType = hardwareType;
        this.supportedCrypto = supportedCrypto;
        this.preferredCrypto = preferredCrypto;
        this.supportedDT = supportedDT;
        this.password = password;
    }

    public String getJSON() {
        StringBuilder json = new StringBuilder();
        json.append("{\n\"ConnREQ\" : {\n");
        json.append("\"HardwareType\" : \"" + hardwareType + "\",\n");
        json.append("\"SupportedCrypto\" : \"" + getStringfromList(supportedCrypto) + ",\n");
        json.append("\"PreferredCrypto\" : \"" + preferredCrypto + "\",\n");
        json.append("\"SupportedDT\" : \"" + getStringfromList(supportedDT) + ",\n");
        json.append("\"Password\" : \"" + password + "\"\n");
        json.append("}\n}");
        return json.toString();
    }

    public static String getStringfromList(JSONArray list) {
        String res = "[";
        for (int index = 0; index < list.length(); index++) {
            res += "\"" + list.get(index) + "\"";
            if (index != list.length() - 1) res += ", ";
        }
        res += "]";
        return res;
    }
    public static ConnREQ getDefaultConnREQ(){
        ConnREQ connREQ = new ConnREQ("Smartphone", new JSONArray(new String[]{}),"None",
                new JSONArray(new String[]{"Bool", "String", "Integer", "Slider", "Button"}), "");
        return connREQ;
    }
}