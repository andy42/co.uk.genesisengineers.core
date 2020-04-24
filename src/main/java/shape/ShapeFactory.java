package shape;

import content.Context;

public interface ShapeFactory {
    Shape loadRes(Context context, int resId);
}
