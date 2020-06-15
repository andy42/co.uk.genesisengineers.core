package co.uk.genesisengineers.core.ui.util;

import org.xmlpull.v1.XmlPullParser;

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
