package ui.util;

import com.sun.javafx.geom.Vec3f;
import org.xmlpull.v1.XmlPullParser;

import java.awt.*;

public class XmlPullAttributes implements AttributeSet {

    private XmlPullParser parser;

    XmlPullAttributes (XmlPullParser parser) {
        this.parser = parser;
    }

    @Override
    public String getAttributeValue (String namespace, String name) {
        return parser.getAttributeValue(namespace, name);
    }
}
