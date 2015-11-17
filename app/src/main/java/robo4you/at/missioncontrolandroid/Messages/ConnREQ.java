package robo4you.at.missioncontrolandroid.Messages;


import org.json.JSONException;
import org.json.JSONObject;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by rapha on 16.11.2015.
 */
public class ConnREQ {
    private String hardwareType = "";
    private LinkedList<String> supportedCrypto = new LinkedList<>();
    private String preferredCrypto = "";
    private LinkedList<String> supportedDT = new LinkedList<>();
    private String password = "";

    public ConnREQ(String jsonMessage) throws JSONException {
        if (!jsonMessage.contains("ConnREQ"))throw new JSONException("Wrong json String");
        try {
            JSONObject obj = new JSONObject(jsonMessage);
            this.hardwareType = obj.getJSONObject("ConnREQ").getString("HardwareType");
            this.supportedCrypto = new LinkedList(Arrays.asList(obj.getJSONObject("ConnREQ").
                    getJSONArray("SupportedCrypto")));
            this.preferredCrypto = obj.getJSONObject("ConnREQ").getString("PreferredCrypto");
            this.supportedDT = new LinkedList(Arrays.asList(obj.getJSONObject("ConnREQ").
                    getJSONArray("SupportedDT")));
            this.password = obj.getJSONObject("ConnREQ").getString("Password");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ConnREQ(String hardwareType, LinkedList<String> supportedCrypto, String preferredCrypto,
                   LinkedList<String> supportedDT, String password) {
        this.hardwareType = hardwareType;
        this.supportedCrypto = supportedCrypto;
        this.preferredCrypto = preferredCrypto;
        this.supportedDT = supportedDT;
        this.password = password;
    }
    public String getJSON(){
        StringBuilder json = new StringBuilder();
        json.append("{\n\"ConnREQ\" : {\n");
        json.append("\"HardwareType\" : \""+hardwareType+"\",\n");
        json.append("\"SupportedCrypto\" : \""+getStringfromList(supportedCrypto)+",\n");
        json.append("\"PreferredCrypto\" : \""+preferredCrypto+"\",\n");
        json.append("\"SupportedDT\" : \""+getStringfromList(supportedDT)+",\n");
        json.append("\"Password\" : \""+password+"\"\n");
        json.append("}\n}");
        return json.toString();
    }
    public static String getStringfromList(LinkedList<String> list){
        String res = "[";
        for (int index = 0;index<list.size();index++){
            res+="\""+list.get(index)+"\"";
            if (index!=list.size()-1)res+=", ";
        }
        res+="]";
        return res;
    }
}
