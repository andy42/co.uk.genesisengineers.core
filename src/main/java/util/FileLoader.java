package util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class FileLoader {


    public static String loadFileAsString (String filePath) {
        ClassLoader classLoader = FileLoader.class.getClassLoader();
        //Logger.info("fileloader "+classLoader.getResource(filePath).toString());
        File file = new File(classLoader.getResource(filePath).getFile());
        try {
            return FileUtils.readFileToString(file);
        } catch (Exception e) {
            Logger.error("loadFileAsString Exception " + e.getMessage());
            return null;
        }

    }

    public static List<File> listFiles(String filePath){
        ClassLoader classLoader = FileLoader.class.getClassLoader();
        File file = new File(classLoader.getResource(filePath).getFile());
        return Arrays.asList(file.listFiles());
    }
}
