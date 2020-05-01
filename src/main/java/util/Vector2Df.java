package util;

public class Vector2Df implements Cloneable{
    public float x;
    public float y;

    public Vector2Df () {
        this.x = 0;
        this.y = 0;
    }

    public Vector2Df (Vector2Df position) {
        this.x = position.x;
        this.y = position.y;
    }

    public Vector2Df (float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2Df (int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector2Df (double x, double y) {
        this.x = (float) x;
        this.y = (float) y;
    }

    public void normalise () {
        float length = (float) Math.sqrt(this.x * this.x + this.y * this.y);
        if (length == 0)
            return;
        float l = 1.0f / length;
        this.x *= l;
        this.y *= l;
    }

    public float length () {
        return (float) Math.sqrt(this.x * this.x + this.y * this.y);
    }

    public Vector2Df floor () {
        return new Vector2Df((float) Math.floor(this.x), (float) Math.floor(this.y));
    }

    public static Vector2Df add (Vector2Df left, Vector2Df right) {
        return new Vector2Df(left.x + right.x, left.y + right.y);
    }

    public static Vector2Df sub (Vector2Df left, Vector2Df right) {
        return new Vector2Df(left.x - right.x, left.y - right.y);
    }

    public static Vector2Df multiply (Vector2Df left, Vector2Df right) {
        return new Vector2Df(left.x * right.x, left.y * right.y);
    }

    public static Vector2Df multiply (Vector2Df left, float right) {
        return new Vector2Df(left.x * right, left.y * right);
    }

    public static Vector2Df divide (Vector2Df left, float right) {
        return new Vector2Df(left.x / right, left.y / right);
    }

    public static Vector2Df divide (Vector2Df left, Vector2Df right) {
        return new Vector2Df(left.x / right.x, left.y / right.y);
    }

    public void rotate (float angle) {
        float cs = (float) Math.cos(angle);
        float sn = (float) Math.sin(angle);

        float px = this.x * cs - this.y * sn;
        float py = this.x * sn + this.y * cs;

        this.x = px;
        this.y = py;
    }

    public Vector2Df multiply (Vector2Df vector2) {
        return Vector2Df.multiply(this, vector2);
    }

    public Vector2Df divide (Vector2Df vector2) {
        return Vector2Df.divide(this, vector2);
    }

    public Vector2Df add (Vector2Df vector2) {
        return Vector2Df.add(this, vector2);
    }

    public Vector2Df sub (Vector2Df vector2) {
        return Vector2Df.sub(this, vector2);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public Vector2Df copy(){
        return new Vector2Df(this);
    }

    public String toString(){
        return "x:"+this.x+", y:"+this.y;
    }
}
