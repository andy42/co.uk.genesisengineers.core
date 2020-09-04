package co.uk.genesisengineers.core.ui.view;

import co.uk.genesisengineers.core.content.Context;
import co.uk.genesisengineers.core.drawable.Drawable;
import co.uk.genesisengineers.core.drawable.DrawableArray;
import co.uk.genesisengineers.core.drawable.DrawableManager;
import co.uk.genesisengineers.core.ui.util.AttributeParser;
import co.uk.genesisengineers.core.ui.util.AttributeSet;
import co.uk.genesisengineers.core.util.Vector2Df;

public class ImageView extends View {

    private Drawable drawable;
    private int drawableArrayIndex = 0;

    public ImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Integer drawableId = AttributeParser.getInteger(attrs, "drawable", null);
        if(drawableId != null){
            this.drawable = DrawableManager.getInstance().getDrawable(drawableId);
        }
        drawableArrayIndex = AttributeParser.getInteger(attrs, "drawableIndex", 0);
    }

    @Override
    public void render() {
        super.render();
        if(visibility != VISIBLE) return;

        if (drawable != null) {
            if (drawable instanceof DrawableArray) {
                ((DrawableArray) drawable).draw(getPosition().add(getDimensions().multiply(new Vector2Df(0.5f, 0.5f))), getDimensions(), 0, drawableArrayIndex);
            }
            else {
                drawable.draw(getPosition().add(getDimensions().multiply(new Vector2Df(0.5f, 0.5f))), getDimensions(), 0);
            }
        }
    }

    public void setDrawable(Drawable drawable){
        this.drawable = drawable;
    }
    public void setDrawableArray(Drawable drawable, int drawableArrayIndex){
        this.drawable = drawable;
        this.drawableArrayIndex = drawableArrayIndex;
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = 0;
        int height = 0;

        if(getLayoutParams().width == ViewGroup.LayoutParams.MATCH_PARENT){
            width = widthMeasureSpec;

            switch (getLayoutParams().height){
                case ViewGroup.LayoutParams.MATCH_PARENT:
                    height = heightMeasureSpec;
                    break;
                case ViewGroup.LayoutParams.WRAP_CONTENT:

                    if(drawable != null && drawable.getDimensions().x != 0){
                        height = (int)(drawable.getDimensions().y/drawable.getDimensions().x)*width;
                    }
                    else {
                        height = 0;
                    }
                    break;
                default:
                    height =  getLayoutParams().height;
            }
            setMeasuredDimension(width, height);
            return;
        }

        if(getLayoutParams().height == ViewGroup.LayoutParams.MATCH_PARENT){
            height = heightMeasureSpec;

            switch (getLayoutParams().width){
                case ViewGroup.LayoutParams.MATCH_PARENT:
                    width = widthMeasureSpec;
                    break;
                case ViewGroup.LayoutParams.WRAP_CONTENT:

                    if(drawable != null && drawable.getDimensions().y != 0){
                        width = (int)(drawable.getDimensions().x/drawable.getDimensions().y)*height;
                    }
                    else {
                        width = 0;
                    }
                    break;
                default:
                    width =  getLayoutParams().height;
            }
            setMeasuredDimension(width, height);
            return;
        }

        if(getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT && getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT){
            setMeasuredDimension((int)drawable.getDimensions().x, (int)drawable.getDimensions().y);
            return;
        }

        if(getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT){
            setMeasuredDimension((int)drawable.getDimensions().x,getLayoutParams().height);
            return;
        }
        if(getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT){
            setMeasuredDimension(getLayoutParams().width, (int)drawable.getDimensions().y);
            return;
        }

        setMeasuredDimension(getLayoutParams().width, getLayoutParams().height);
    }
}
