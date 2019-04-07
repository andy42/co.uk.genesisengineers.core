package util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;

public class ResourceLoader {
    public static void writeString (String path, String str) {
        //ensure that the file exists
        if (!exists(path)) {
            new File(path).mkdirs();
        }

        //write the file
        try {
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(path), "UTF-8");
            out.write(str);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean exists (String path) {
        return getStream(path) != null;
    }

    public static String getText (String path) {
        try {
            InputStream in = getStream(path);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            StringBuilder out = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null) {
                out.append(line + '\n');
            }

            reader.close();
            in.close();

            return out.toString();

        } catch (IOException e) {
            Logger.error("Unable to open:" + path);
            e.printStackTrace();
        }
        return null;
    }

    public static ByteBuffer getBytes (String path) {
        try {
            byte[] buffer = new byte[4096];
            ByteArrayOutputStream outstream = new ByteArrayOutputStream();
            InputStream in = getStream(path);

            int r = 0;
            while ((r = in.read(buffer)) != -1) {
                outstream.write(buffer, 0, r);
            }

            in.close();

            ByteBuffer buf = BufferUtils.createByteBuffer(outstream.size());
            byte[] out = outstream.toByteArray();
            for (int i = 0; i < out.length; i++)
                buf.put(out[i]);

            buf.flip();

            return buf;
        } catch (IOException e) {
            Logger.error("Error with:" + path);
            e.printStackTrace();
        }
        return null;
    }

    public static InputStream getStream (String path) {
        InputStream in = ResourceLoader.class.getResourceAsStream(path);
        if (in == null)
            return ResourceLoader.class.getResourceAsStream("/" + path);
        return in;
    }
}
