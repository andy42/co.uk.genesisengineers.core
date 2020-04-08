package ui.view;

//https://android.googlesource.com/platform/frameworks/base/+/master/core/java/android/view/LayoutInflater.java
//http://www.xmlpull.org/v1/download/unpacked/doc/quick_intro.html
//https://android.googlesource.com/platform/frameworks/base/+/master/core/java/android/util/XmlPullAttributes.java

import content.Context;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import ui.util.AttributeSet;
import ui.util.Xml;
import util.FileLoader;
import util.Logger;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class LayoutInflater {

    private Context mContext = new Context();

    private static final HashMap<String, Constructor<? extends View>> sConstructorMap = new HashMap<String, Constructor<? extends View>>();

    static final Class<?>[] mConstructorSignature = new Class[]{Context.class, AttributeSet.class};

    final Object[] mConstructorArgs = new Object[2];

    public View inflate (Context context, int layoutId, ViewGroup root) {
        return inflate(context.getResources().getAssetFile(layoutId), root);
    }

    public View inflate (File file, ViewGroup root) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            String fileString = FileLoader.loadFileAsString(file);
            if (fileString == null) {
                Logger.error("LayoutInflater.inflate fileName:" + file.getName() + " not found");
                return null;
            }

            if (fileString.isEmpty()) {
                Logger.error("LayoutInflater.inflate fileName:" + file.getName() + " is empty");
                return null;
            }

            xpp.setInput(new StringReader(fileString));
            return inflate(xpp, root);

        } catch (XmlPullParserException e) {
            Logger.exception(e, "LayoutInflater.inflate XmlPullParserException");
            return null;
        }
    }

    public View inflate (XmlPullParser parser, ViewGroup root) {

        final Context inflaterContext = mContext;

        final AttributeSet attrs = Xml.asAttributeSet(parser);

        Context lastContext = (Context) mConstructorArgs[0];
        mConstructorArgs[0] = inflaterContext;
        View result = root;

        try {
            int type;
            while ((type = parser.next()) != XmlPullParser.START_TAG && type != XmlPullParser.END_DOCUMENT) {
                // Empty
            }

            if (type != XmlPullParser.START_TAG) {
                Logger.error(parser.getPositionDescription() + ": No start tag found!");
                return null;
            }
            final String name = parser.getName();

            final View temp = createViewFromTag(root, name, inflaterContext, attrs);

            ViewGroup.LayoutParams params = null;

            if (root != null) {
                params = root.generateLayoutParams(attrs);
            } else {
                params = new ViewGroup.LayoutParams(mContext, attrs);
            }
            temp.setLayoutParams(params);


            rInflateChildren(parser, temp, attrs, true);

            if (temp != null && root != null) {
                root.addView(temp);
            }
            return temp;


        } catch (XmlPullParserException e) {
            Logger.exception(e, e.getMessage());
        } catch (Exception e) {
            Logger.exception(e, e.getMessage());
        }

        return null;
    }

    final void rInflateChildren (XmlPullParser parser, View parent, AttributeSet attrs, boolean finishInflate) throws XmlPullParserException, IOException {
        rInflate(parser, parent, parent.getContext(), attrs, finishInflate);
    }

    void rInflate (XmlPullParser parser, View parent, Context context, AttributeSet attrs, boolean finishInflate) throws XmlPullParserException, IOException {
        final int depth = parser.getDepth();
        int type;
        boolean pendingRequestFocus = false;
        while (((type = parser.next()) != XmlPullParser.END_TAG || parser.getDepth() > depth) && type != XmlPullParser.END_DOCUMENT) {
            if (type != XmlPullParser.START_TAG) {
                continue;
            }

            final String name = parser.getName();
            final View view = createViewFromTag(parent, name, context, attrs);
            final ViewGroup viewGroup = (ViewGroup) parent;

            ViewGroup.LayoutParams params = viewGroup.generateLayoutParams(attrs);
            view.setLayoutParams(params);
            rInflateChildren(parser, view, attrs, true);
            viewGroup.addView(view);
        }
    }

    private View createViewFromTag (View parent, String name, Context context, AttributeSet attrs) {

        if (name.equalsIgnoreCase("View")) {
            return new View(context, attrs);
        }
        else if (name.equalsIgnoreCase("TextView")) {
            return new TextView(context, attrs);
        }
        else if (name.equalsIgnoreCase("Button")) {
            return new Button(context, attrs);
        }
        else if (name.equalsIgnoreCase("LinearLayout")) {
            return new LinearLayout(context, attrs);
        }
        else if (name.equalsIgnoreCase("ScrollView")) {
            return new ScrollView(context, attrs);
        }
        else if (name.equalsIgnoreCase("RecyclerView")){
            return new RecyclerView(context, attrs);
        }
        else if(name.equalsIgnoreCase("EditText")){
            return new EditText(context, attrs);
        }

        return createView(name, "", attrs);
    }

    private boolean verifyClassLoader (Constructor<? extends View> constructor) {
        final ClassLoader constructorLoader = constructor.getDeclaringClass().getClassLoader();
        ClassLoader cl = mContext.getClassLoader();
        do {
            if (constructorLoader == cl) {
                return true;
            }
            cl = cl.getParent();
        } while (cl != null);
        return false;
    }

    private View createView (String name, String prefix, AttributeSet attrs) {
        Constructor<? extends View> constructor = sConstructorMap.get(name);
        if (constructor != null && !verifyClassLoader(constructor)) {
            constructor = null;
            sConstructorMap.remove(name);
        }

        Class<? extends View> clazz = null;

        try {
            if (constructor == null) {
                clazz = mContext.getClassLoader().loadClass(prefix != null ? (prefix + name) : name).asSubclass(View.class);
                constructor = clazz.getConstructor(mConstructorSignature);
                constructor.setAccessible(true);
                sConstructorMap.put(name, constructor);
            }

            Object lastContext = mConstructorArgs[0];
            if (mConstructorArgs[0] == null) {
                // Fill in the context if not already within inflation.
                mConstructorArgs[0] = mContext;
            }
            Object[] args = mConstructorArgs;
            args[1] = attrs;

            final View view = constructor.newInstance(args);
            return view;

        } catch (ClassNotFoundException e) {
            // If loadClass fails, we should propagate the exception.
            Logger.exception(e, e.getMessage());
        } catch (NoSuchMethodException e) {
            Logger.exception(e, e.getMessage());
        } catch (IllegalAccessException e) {
            Logger.exception(e, e.getMessage());
        } catch (InstantiationException e) {
            Logger.exception(e, e.getMessage());
        } catch (InvocationTargetException e) {
            Logger.exception(e, e.getMessage());
        }
        return null;
    }
}
