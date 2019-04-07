package util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URISyntaxException;

/**
 * This is for setting environment variables, it may need to be expanded to include other return types, currently only
 * handles strings.
 */
public class EnvironmentVariables {

    private JSONObject env = new JSONObject();

    private static EnvironmentVariables s_instance = null;

    private EnvironmentVariables () {
    }

    public static EnvironmentVariables getInstance () {
        if (s_instance == null) {
            s_instance = new EnvironmentVariables();
        }
        return s_instance;
    }

    // set environment variable
    public void put (String key, String value) {
        this.env.put(key, value);
    }

    // get strings
    private String getString (String key) {
        return this.getString(key, "");
    }

    public String getString (String key, String defaultValue) {
        try {
            return this.env.getString(key);
        } catch (JSONException e) {
            return defaultValue;
        }
    }

    // get boolean
    public boolean getBoolean (String key) {
        return this.getBoolean(key, false);
    }

    public boolean getBoolean (String key, boolean defaultValue) {
        try {
            return this.env.getBoolean(key);
        } catch (JSONException e) {
            return defaultValue;
        }
    }

    // get file content as string, trimmed (for now) because json
    // TODO move this to its own file handler class.
    private String getFileContent (String filename) throws IOException, URISyntaxException, NullPointerException {

        File file = new File(getClass().getClassLoader().getResource(filename).toURI());

        BufferedReader reader = new BufferedReader(new FileReader(file.toString()));

        String line = null;

        StringBuilder stringBuilder = new StringBuilder();

        try {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line.trim());
            }
            return stringBuilder.toString();
        } finally {
            reader.close();
        }
    }

    private JSONObject getJSONFromFile (String filename) throws IOException, URISyntaxException {
        if (filename == null) {
            Logger.exception(new Exception("Filename set to null."), "EnvironmentVariables file was set to null");

            return null;
        }

        String fileContent = this.getFileContent(filename);

        return new JSONObject(fileContent);
    }

    public boolean load (String filename) {
        try {
            this.env = this.getJSONFromFile(filename);

        } catch (IOException e) {
            Logger.exception(e, "File failed to load, couldn't read or write to stream.");
            return false;
        } catch (URISyntaxException e) {
            Logger.exception(e, "The resource you're trying to specify is incorrectly formatted.");
            return false;
        } catch (NullPointerException e) {
            Logger.exception(e, "Environment variable file missing.");
            return false;
        }

        return true;
    }
}

