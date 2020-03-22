package ui.view;


import content.Context;
import ui.util.AttributeParser;
import ui.util.AttributeSet;

import java.util.HashMap;

public class LinearLayout extends ViewGroup {

    protected int orientation;
    protected int gravity;

    public final static int HORIZONTAL = 0;
    public final static int VERTICAL = 1;

    public final static String HORIZONTAL_KEY = "horizontal";
    public final static String VERTICAL_KEY = "vertical";

    public final static String GRAVITY_KEY = "gravity";

    private static final HashMap<String, Integer> GRAVITY_MAP = new HashMap<String, Integer>() {{
        put("center", Gravity.CENTER);
        put("left", Gravity.LEFT);
        put("right", Gravity.RIGHT);
        put("top", Gravity.TOP);
        put("bottom", Gravity.BOTTOM);
    }};


    private int totalLength = 0;

    public LinearLayout (Context context, AttributeSet attrs) {
        super(context, attrs);

        String orientationValue = attrs.getAttributeValue(null, "orientation");
        if (orientationValue != null && orientationValue.equalsIgnoreCase(HORIZONTAL_KEY)) {
            this.orientation = HORIZONTAL;
        } else {
            this.orientation = VERTICAL;
        }

        String gravityValue = attrs.getAttributeValue(null, GRAVITY_KEY);
        if (gravityValue != null && GRAVITY_MAP.containsKey(gravityValue)) {
            this.gravity = GRAVITY_MAP.get(gravityValue);
        } else {
            this.gravity = Gravity.CENTER;
        }


    }

    public LinearLayout () {

    }

    @Override
    public void onMeasure (int widthMeasureSpec, int heightMeasureSpec) {
        if (this.orientation == VERTICAL) {
            measureVertical(widthMeasureSpec, heightMeasureSpec);
        } else {
            measureHorizontal(widthMeasureSpec, heightMeasureSpec);
        }
    }

    private void measureVertical (int widthMeasureSpec, int heightMeasureSpec) {
        int width = 0;
        int height = 0;
        totalLength = 0;

        //Width
        if (layoutParams.width > 0) {
            width = layoutParams.width;
        } else if (layoutParams.width == ViewGroup.LayoutParams.FILL_PARENT) {
            width = widthMeasureSpec;
        }

        //Height
        if (layoutParams.height > 0) {
            height = layoutParams.height;
        } else if (layoutParams.height == ViewGroup.LayoutParams.FILL_PARENT) {
            height = heightMeasureSpec;
        }

        for (View child : children) {

            child.onMeasure(width, height);

            int childWidth = child.getMeasuredWidth() + child.getLeftMargin() + child.getRightMargin();
            int childHeight = child.getMeasuredHeight() + child.getTopMargin() + child.getBottomMargin();

            totalLength += childHeight;
            if (layoutParams.width == ViewGroup.LayoutParams.WRAP_CONTENT && childWidth > width) {
                width = childWidth;
            }
        }

        if (layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            height = totalLength;
        }

        setMeasuredDimension(width, height);
    }

    private void measureHorizontal (int widthMeasureSpec, int heightMeasureSpec) {

        int width = 0;
        int height = 0;
        totalLength = 0;

        //Width
        if (layoutParams.width > 0) {
            width = layoutParams.width;
        } else if (layoutParams.width == ViewGroup.LayoutParams.FILL_PARENT) {
            width = widthMeasureSpec;
        }

        //Height
        if (layoutParams.height > 0) {
            height = layoutParams.height;
        } else if (layoutParams.height == ViewGroup.LayoutParams.FILL_PARENT) {
            height = heightMeasureSpec;
        }

        for (View child : children) {
            child.onMeasure(width, height);
            int childWidth = child.getMeasuredWidth() + child.getLeftMargin() + child.getRightMargin();
            int childHeight = child.getMeasuredHeight() + child.getTopMargin() + child.getBottomMargin();

            totalLength += childWidth;
            if (layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT && childHeight > height) {
                height = childHeight;
            }

        }
        if (layoutParams.width == ViewGroup.LayoutParams.WRAP_CONTENT) {
            width = totalLength;
        }
        setMeasuredDimension(width, height);
    }

    public void onLayout (int width, int height, int x, int y) {
        this.dimensions.x = width;
        this.dimensions.y = height;
        this.position.x = x;
        this.position.y = y;

        int orientationPosition = 0;

        for (View child : children) {
            if (this.orientation == VERTICAL) {

                LinearLayout.LayoutParams childLayoutParams = (LinearLayout.LayoutParams) child.getLayoutParams();
                int childX = 0;
                int childY = 0;

                switch (childLayoutParams.layoutGravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
                    case Gravity.CENTER_HORIZONTAL:
                        //childLeft = paddingLeft + ((childSpace - childWidth) / 2) + lp.leftMargin - lp.rightMargin;
                        childX = (int) ((width - child.getMeasuredWidth()) * 0.5f);
                        break;
                    case Gravity.LEFT:
                        childX = child.getLeftMargin();
                        break;
                    case Gravity.RIGHT:
                        childX = width - (child.getMeasuredWidth() + child.getRightMargin());
                        break;
                }

                if ((this.gravity & Gravity.CENTER) == Gravity.CENTER) {
                    childY = (int) ((height - totalLength) * 0.5f);
                }

                child.onLayout(child.getMeasuredWidth(), child.getMeasuredHeight(), childX, childY + orientationPosition + child.getTopMargin());
                orientationPosition += child.getMeasuredHeight() + child.getTopMargin() + child.getBottomMargin();
            }

            if (this.orientation == HORIZONTAL) {

                LinearLayout.LayoutParams childLayoutParams = (LinearLayout.LayoutParams) child.getLayoutParams();
                int childX = 0;
                int childY = 0;

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

                if ((this.gravity & Gravity.CENTER) == Gravity.CENTER) {
                    childX += (int) ((width - totalLength) * 0.5f);
                }

                child.onLayout(child.getMeasuredWidth(), child.getMeasuredHeight(), childX + orientationPosition + child.getLeftMargin(), childY);
                orientationPosition += child.getMeasuredWidth() + child.getLeftMargin() + child.getRightMargin();
            }

        }
    }

    @Override
    public LinearLayout.LayoutParams generateLayoutParams (AttributeSet attrs) {
        return new LinearLayout.LayoutParams(getContext(), attrs);
    }

    public static class LayoutParams extends ViewGroup.LayoutParams {
        public int layoutGravity = Gravity.CENTER;

        public LayoutParams (Context context, AttributeSet attrs) {
            width = AttributeParser.getLayoutDimension(attrs, "layout_width");
            height = AttributeParser.getLayoutDimension(attrs, "layout_height");

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

        public LayoutParams (int width, int height, int layoutGravity) {
            super(width, height);
            this.layoutGravity = layoutGravity;
        }
    }
}
