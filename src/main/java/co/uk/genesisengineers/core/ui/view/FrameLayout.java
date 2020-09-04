package co.uk.genesisengineers.core.ui.view;

import co.uk.genesisengineers.core.content.Context;
import co.uk.genesisengineers.core.ui.util.AttributeParser;
import co.uk.genesisengineers.core.ui.util.AttributeSet;

public class FrameLayout extends ViewGroup {

    private int maxChildWidth = 0;
    private int maxChildHeight = 0;

    public FrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onMeasure (int widthMeasureSpec, int heightMeasureSpec) {

        if(visibility == View.GONE){
            setMeasuredDimension(0, 0);
            return;
        }

        int width = 0;
        int height = 0;

        maxChildWidth = 0;
        maxChildHeight = 0;

        if (layoutParams.width > 0) {
            width = layoutParams.width;
        } else if (layoutParams.width == ViewGroup.LayoutParams.MATCH_PARENT) {
            width = widthMeasureSpec;
        }

        if (layoutParams.height > 0) {
            height = layoutParams.height;
        } else if (layoutParams.height == ViewGroup.LayoutParams.MATCH_PARENT) {
            height = heightMeasureSpec;
        }

        for (View child : children) {
            child.onMeasure(width - child.getLeftMargin() - child.getRightMargin(),
                    height - child.getTopMargin() - child.getBottomMargin());
            int childWidth = child.getMeasuredWidth() + child.getLeftMargin() + child.getRightMargin();
            int childHeight = child.getMeasuredHeight() + child.getTopMargin() + child.getBottomMargin();

            if(childWidth > maxChildWidth) maxChildWidth= childWidth;
            if(childHeight > maxChildHeight) maxChildHeight= childHeight;
        }

        if (layoutParams.width == ViewGroup.LayoutParams.WRAP_CONTENT) {
            width = maxChildWidth;
        }

        if (layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            height = maxChildHeight;
        }

        for (View child : children) {
            if(((LayoutParams)child.getLayoutParams()).layoutFill){
                child.setMeasuredDimension(
                        width - child.getLeftMargin() - child.getRightMargin(),
                        height - child.getTopMargin() - child.getBottomMargin());
            }
        }

        setMeasuredDimension(width, height);
    }

    @Override
    public void onLayout(int width, int height, int x, int y) {
        this.dimensions.x = width;
        this.dimensions.y = height;
        this.position.x = x;
        this.position.y = y;

        for (View child : children) {

            FrameLayout.LayoutParams childLayoutParams = (FrameLayout.LayoutParams) child.getLayoutParams();
            int childX = 0;
            int childY = 0;

            switch (childLayoutParams.layoutGravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
                case Gravity.CENTER_HORIZONTAL:
                    childX = (int) ((width - child.getMeasuredWidth()) * 0.5f);
                    break;
                case Gravity.LEFT:
                    childX = child.getLeftMargin();
                    break;
                case Gravity.RIGHT:
                    childX = width - (child.getMeasuredWidth() + child.getRightMargin());
                    break;
            }

            switch (childLayoutParams.layoutGravity & Gravity.VERTICAL_GRAVITY_MASK) {
                case Gravity.CENTER_VERTICAL:
                    childY = (int) ((height - child.getMeasuredHeight()) * 0.5f);
                    break;
                case Gravity.TOP:
                    childY = child.getTopMargin();
                    break;
                case Gravity.BOTTOM:
                    childY = height - (child.getMeasuredHeight() + child.getTopMargin());
                    break;
            }

            child.onLayout(child.getMeasuredWidth(), child.getMeasuredHeight(),childX, childY);
        }
    }

    @Override
    public FrameLayout.LayoutParams generateLayoutParams (AttributeSet attrs) {
        return new FrameLayout.LayoutParams(getContext(), attrs);
    }

    public static class LayoutParams extends ViewGroup.LayoutParams {
        public int layoutGravity = Gravity.CENTER;
        public boolean layoutFill = false;

        public LayoutParams (Context context, AttributeSet attrs) {
            width = AttributeParser.getLayoutDimension(attrs, "layout_width");
            height = AttributeParser.getLayoutDimension(attrs, "layout_height");
            layoutFill = AttributeParser.getBoolean(attrs, "layout_fill", false);

            String layoutGravityValue = AttributeParser.getString(attrs, "layout_gravity");
            switch (layoutGravityValue) {
                case "left":
                    this.layoutGravity = Gravity.LEFT;
                    break;
                case "right":
                    this.layoutGravity = Gravity.RIGHT;
                    break;
                case "top":
                    this.layoutGravity = Gravity.TOP;
                    break;
                case "bottom":
                    this.layoutGravity = Gravity.BOTTOM;
                    break;
                case "center_horizontal":
                    this.layoutGravity = Gravity.CENTER_HORIZONTAL;
                    break;
                case "center_vertical":
                    this.layoutGravity = Gravity.CENTER_VERTICAL;
                    break;
                default:
                    this.layoutGravity = Gravity.CENTER;
            }
        }
    }
}
