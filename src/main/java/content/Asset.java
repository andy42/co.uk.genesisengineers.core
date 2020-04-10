package content;

import org.json.JSONObject;

public class Asset {
    public String name;
    public int id;
    public String filePath;
    public String fileType;
    public int type;

    public Asset(JSONObject jsonObject){
        name = jsonObject.getString("name");
        id = jsonObject.getInt("id");
        filePath = jsonObject.getString("filePath");
        type = jsonObject.getInt("type");
        fileType = jsonObject.getString("fileType");
    }

    public int getId(){
        return id;
    }
}
