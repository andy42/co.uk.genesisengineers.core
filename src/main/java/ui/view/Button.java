package ui.view;

import com.sun.javafx.geom.Vec3f;
import content.Context;
import ui.util.AttributeParser;
import ui.util.AttributeSet;
import util.Vector2Df;
import visualisation.Visualisation;

public class Button extends View {
    private String text;
    protected Vec3f textColor;
    private float textSize = 0.5f;
    private float textModifier = 0.03f;


    public Button (Context context, AttributeSet attrs) {
        super(context, attrs);

        this.text = AttributeParser.getString(attrs, "text", "");

        this.textSize = AttributeParser.getDimension(attrs, "text_size", 10) * textModifier;
        this.isClickable = AttributeParser.getBoolean(attrs, "clickable", true);

        this.backgroundPressedColor = AttributeParser.getColor(attrs, "background_pressed_color", "#FF0000");


        this.topPadding = AttributeParser.getDimension(attrs, "padding_top", 5);
        this.bottomPadding = AttributeParser.getDimension(attrs, "padding_bottom", 5);
        this.leftPadding = AttributeParser.getDimension(attrs, "padding_left", 5);
        this.rightPadding = AttributeParser.getDimension(attrs, "padding_right", 5);

        this.textColor = AttributeParser.getColor(attrs, "text_color", "#000000");
    }

    @Override
    public void render () {
        super.render();
        Visualisation.getInstance().getFont().drawText(this.text, this.textColor, textSize, position.add(new Vector2Df(getLeftPadding(), getTopPadding())));
    }

    @Override
    public void onMeasure (int widthMeasureSpec, int heightMeasureSpec) {
        int width = 0;

        if (layoutParams.width == ViewGroup.LayoutParams.WRAP_CONTENT) {
            width = Visualisation.getInstance().getFont().getTextWidth(this.text, this.textSize) + this.getLeftPadding() + this.getRightPadding();
        } else {
            width = getSize(layoutParams.width, widthMeasureSpec);
        }

        int height = 0;
        if (layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            height = Visualisation.getInstance().getFont().getTextHeight(this.text, this.textSize) + this.getTopPadding() + this.getBottomPadding();
        } else {
            height = getSize(layoutParams.height, heightMeasureSpec);
        }

        setMeasuredDimension(width, height);
    }

    public void setText (String text) {
        this.text = text;
    }
}
