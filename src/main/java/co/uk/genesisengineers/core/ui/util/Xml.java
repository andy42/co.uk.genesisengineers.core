package co.uk.genesisengineers.core.ui.util;

import org.xmlpull.v1.XmlPullParser;

public class Xml {

    public static AttributeSet asAttributeSet (XmlPullParser parser) {
        return (parser instanceof AttributeSet) ? (AttributeSet) parser : new XmlPullAttributes(parser);
    }
}
