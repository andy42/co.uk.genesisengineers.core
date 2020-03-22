package ui.view;

import content.Context;
import ui.util.AttributeParser;
import ui.util.AttributeSet;

public class EditText extends TextView {
    private String hintText;

    public EditText (Context context, AttributeSet attrs) {
        super(context, attrs);
        this.isFocusable= AttributeParser.getBoolean(attrs, "focusable", true);
        this.isEditable = AttributeParser.getBoolean(attrs, "editable", true);
        this.backgroundFocusedColor = AttributeParser.getColor(attrs, "background_focused_color", "#ffffff");
    }
}
