package util;

public class CollisionBox {
    public Vector2Df min = new Vector2Df();
    public Vector2Df max = new Vector2Df();

    public void init (Vector2Df position, Vector2Df halfDimensions) {
        this.min = Vector2Df.sub(position, halfDimensions);
        this.max = Vector2Df.add(position, halfDimensions);
    }

    public void initTopLeft (Vector2Df position, Vector2Df dimensions) {
        this.min = position;
        this.max = Vector2Df.add(position, dimensions);
    }

    public void initMinMax (Vector2Df min, Vector2Df max) {
        this.min = min;
        this.max = max;
    }

    public boolean pointCollisionTest (Vector2Df position) {
        if (this.max.x < position.x || this.min.x > position.x) {
            return false;
        }
        if (this.max.y < position.y || this.min.y > position.y) {
            return false;
        }
        return true;
    }

    public boolean boxCollisionTest (CollisionBox secondBox) {
        if (this.max.x < secondBox.min.x || this.min.x > secondBox.max.x)
            return false;
        if (this.max.y < secondBox.min.y || this.min.y > secondBox.max.y)
            return false;
        return true;

    }
}
