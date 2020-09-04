package co.uk.genesisengineers.core.shape;

import co.uk.genesisengineers.core.content.Context;

public interface ShapeFactory {
    Shape loadRes(Context context, int resId);
}
