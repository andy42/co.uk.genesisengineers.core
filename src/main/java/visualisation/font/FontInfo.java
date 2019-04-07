package visualisation.font;

import util.Logger;
import util.ResourceLoader;
import visualisation.Visualisation;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FontInfo {

    private static final int PAD_TOP = 0;
    private static final int PAD_LEFT = 1;
    private static final int PAD_BOTTOM = 2;
    private static final int PAD_RIGHT = 3;

    private int paddingTop = 0;
    private int paddingLeft = 0;
    private int paddingBottom = 0;
    private int paddingRight = 0;

    private int paddingWidth;
    private int paddingHeight;

    private int lineHeight = 0;


    private static final int DESIRED_PADDING = 3;

    private static final String NUMBER_SEPARATOR = ",";
    private static final String SPLITTER = " ";

    private Map<Integer, CharacterInfo> metaData = new HashMap<Integer, CharacterInfo>();

    private Map<String, String> values = new HashMap<String, String>();


    private int getValueOfVariable (String variable) {
        return Integer.parseInt(values.get(variable));
    }

    private int[] getValuesOfVariable (String variable) {
        String[] numbers = values.get(variable).split(NUMBER_SEPARATOR);
        int[] actualValues = new int[numbers.length];
        for (int i = 0; i < actualValues.length; i++) {
            actualValues[i] = Integer.parseInt(numbers[i]);
        }
        return actualValues;
    }

    private void loadLineSizes (BufferedReader reader) {
        processNextLine(reader);
        this.lineHeight = getValueOfVariable("lineHeight") - paddingHeight;
    }

    private boolean processNextLine (BufferedReader reader) {
        values.clear();
        String line = null;
        try {
            line = reader.readLine();
        } catch (IOException e1) {
        }
        if (line == null) {
            return false;
        }
        for (String part : line.split(SPLITTER)) {
            String[] valuePairs = part.split("=");
            if (valuePairs.length == 2) {
                values.put(valuePairs[0], valuePairs[1]);
            }
        }
        return true;
    }

    private void loadPaddingData (BufferedReader reader) {
        processNextLine(reader);
        int[] padding = getValuesOfVariable("padding");

        this.paddingTop = padding[PAD_TOP];
        this.paddingLeft = padding[PAD_LEFT];
        this.paddingBottom = padding[PAD_BOTTOM];
        this.paddingRight = padding[PAD_RIGHT];


        this.paddingWidth = this.paddingLeft + this.paddingRight;
        this.paddingHeight = this.paddingTop + this.paddingBottom;
    }


    public boolean init (String fileName) {

        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(ResourceLoader.getStream(fileName), "UTF-8"));

            loadPaddingData(reader);
            loadLineSizes(reader);
            int imageWidth = getValueOfVariable("scaleW");

            processNextLine(reader); //skip line
            processNextLine(reader); //skip line

            while (processNextLine(reader)) {

                CharacterInfo c = new CharacterInfo();
                if (c.init(values)) {
                    metaData.put(c.getId(), c);
                }
            }

            reader.close();
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public void print () {
        for (Integer c : metaData.keySet()) {

            CharacterInfo characterInfo = metaData.get(c);
            Logger.info("c " + characterInfo.getId());
        }
    }

    public Set<Integer> getCharacterSet () {
        return metaData.keySet();
    }

    public CharacterInfo getCharacterInfo (Integer c) {
        return metaData.get(c);
    }

    public int getPaddingTop () {
        return paddingTop;
    }

    public int getPaddingLeft () {
        return paddingLeft;
    }

    public int getPaddingBottom () {
        return paddingBottom;
    }

    public int getPaddingRight () {
        return paddingRight;
    }

    public int getPaddingWidth () {
        return paddingWidth;
    }

    public int getPaddingHeight () {
        return paddingHeight;
    }

    public int getLineHeight () {
        return lineHeight;
    }
}
