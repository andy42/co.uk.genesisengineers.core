package util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;

public class FileLoader {


    public static String loadFileAsString (String filePath) {
        ClassLoader classLoader = FileLoader.class.getClassLoader();
        File file = new File(classLoader.getResource(filePath).getFile());
        try {
            return FileUtils.readFileToString(file);
        } catch (Exception e) {
            Logger.error("loadFileAsString Exception " + e.getMessage());
            return null;
        }
    }
}
