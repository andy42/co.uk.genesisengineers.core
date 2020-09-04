package co.uk.genesisengineers.core.ui.view;

import co.uk.genesisengineers.core.content.Context;
import co.uk.genesisengineers.core.ui.util.AttributeParser;
import co.uk.genesisengineers.core.ui.util.AttributeSet;

public class EditText extends TextView {
    private String hintText;

    public EditText (Context context, AttributeSet attrs) {
        super(context, attrs);
        this.isFocusable= AttributeParser.getBoolean(attrs, "focusable", true);
        this.isEditable = AttributeParser.getBoolean(attrs, "editable", true);
    }
}
