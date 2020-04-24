package ui.view;

import com.sun.javafx.geom.Vec3f;
import content.Context;
import input.KeyEvent;
import ui.util.AttributeParser;
import ui.util.AttributeSet;
import util.Vector2Df;
import visualisation.Visualisation;

public class TextView extends View {

    private String text;
    protected Vec3f textColor;
    protected float textSize = 0.5f;
    protected float textModifier = 0.03f;

    private String hintText = "";
    protected Vec3f hintTextColor;
    protected float hintTextSize = 0.5f;
    protected boolean isEditable = false;

    public TextView (Context context, AttributeSet attrs) {
        super(context, attrs);

        this.text = AttributeParser.getString(attrs, "text", "");
        this.textSize = AttributeParser.getDimension(attrs, "text_size", 10) * textModifier;
        this.textColor = AttributeParser.getColor(context, attrs, "text_color", "#000000");

        this.hintText = AttributeParser.getString(attrs, "hint_text", "");
        this.hintTextColor = AttributeParser.getColor(context, attrs, "hint_text_color", "#000000");
        this.hintTextSize = AttributeParser.getDimension(attrs, "hint_text_size", 10) * textModifier;

        this.isEditable = AttributeParser.getBoolean(attrs, "editable", false);
    }

    @Override
    public void render () {
        super.render();

        if(this.text == null){
            return;
        }

        if(this.text.length() == 0 && this.isFocused == false){
            Visualisation.getInstance().getFont().drawText(this.hintText, this.hintTextColor, textSize, position.add(new Vector2Df(getLeftPadding(), getTopPadding())));
        } else {
            Visualisation.getInstance().getFont().drawText(this.text, this.textColor, textSize, position.add(new Vector2Df(getLeftPadding(), getTopPadding())));
        }

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

    @Override
    public Boolean dispatchKeyEvent (KeyEvent keyEvent){

        if(keyEvent.action == KeyEvent.ACTION_UP){
            if(keyEvent.keyValue >= 32 && keyEvent.keyValue <= 126){
                this.text += (char)keyEvent.keyValue;
            }
            if(keyEvent.keyValue == 259){
                if(this.text.length() > 0){
                    this.text = this.text.substring(0, this.text.length() - 1);
                }
            }
        }

        if(keyEvent.keyValue == 259){
            if(keyEvent.action == KeyEvent.ACTION_HOLD || keyEvent.action == KeyEvent.ACTION_DOWN){
                if(this.text.length() > 0){
                    this.text = this.text.substring(0, this.text.length() - 1);
                }
            }
        }
        return true;
    }

    public void setText(String text){
        this.text = text;
    }
}
