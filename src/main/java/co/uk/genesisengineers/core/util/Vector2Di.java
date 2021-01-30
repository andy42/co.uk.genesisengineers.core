package co.uk.genesisengineers.core.util;

public class Vector2Di {
    public int x;
    public int y;

    public Vector2Di () {
        this.x = 0;
        this.y = 0;
    }

    public Vector2Di (Vector2Di position) {
        this.x = position.x;
        this.y = position.y;
    }

    public Vector2Di (int x, int y) {
        this.x = x;
        this.y = y;
    }

    public float length () {
        return (float) Math.sqrt(this.x * this.x + this.y * this.y);
    }

    public static Vector2Di add (Vector2Di left, Vector2Di right) {
        return new Vector2Di(left.x + right.x, left.y + right.y);
    }

    public static Vector2Di sub (Vector2Di left, Vector2Di right) {
        return new Vector2Di(left.x - right.x, left.y - right.y);
    }

    public static Vector2Di multiply (Vector2Di left, Vector2Di right) {
        return new Vector2Di(left.x * right.x, left.y * right.y);
    }

    public static Vector2Df multiply (Vector2Di left, Vector2Df right) {
        return new Vector2Df(left.x * right.x, left.y * right.y);
    }

    public static Vector2Di multiply (Vector2Di left, int right) {
        return new Vector2Di(left.x * right, left.y * right);
    }

    public static Vector2Di divide (Vector2Di left, int right) {
        return new Vector2Di(left.x / right, left.y / right);
    }

    public static Vector2Di divide (Vector2Di left, Vector2Di right) {
        return new Vector2Di(left.x / right.x, left.y / right.y);
    }

    public Vector2Di multiply (Vector2Di vector2) {
        return Vector2Di.multiply(this, vector2);
    }

    public Vector2Di divide (Vector2Di vector2) {
        return Vector2Di.divide(this, vector2);
    }

    public Vector2Di add (Vector2Di vector2) {
        return Vector2Di.add(this, vector2);
    }

    public Vector2Di sub (Vector2Di vector2) {
        return Vector2Di.sub(this, vector2);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public Vector2Di copy(){
        return new Vector2Di(this);
    }

    public String toString(){
        return "x:"+this.x+", y:"+this.y;
    }
}
