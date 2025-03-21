package net.sixik.sdmmarket.client.gui.ui;

public class Vector2 {
    public int x;
    public int y;

    public Vector2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector2 add(Vector2 other) {
        return new Vector2(x + other.x, y + other.y);
    }

    public Vector2 subtract(Vector2 other) {
        return new Vector2(x - other.x, y - other.y);
    }

    public Vector2 multiply(int scalar) {
        return new Vector2(x * scalar, y * scalar);
    }

    public Vector2 divide(int scalar) {
        return new Vector2(x / scalar, y / scalar);
    }

    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    public Vector2 normalize() {
        double len = length();
        return new Vector2((int) (x / len), (int) (y / len));
    }

    public double dot(Vector2 other) {
        return x * other.x + y * other.y;
    }

    public Vector2 cross(Vector2 other) {
        return new Vector2(y * other.x - x * other.y, x * other.y + y * other.x);
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Vector2)) return false;
        Vector2 other = (Vector2) obj;
        return x == other.x && y == other.y;
    }

    public static Vector2 of(int x, int y) {
        return new Vector2(x, y);
    }

    public Vector2 toVector2(){
        return new Vector2((int) x, (int) y);
    }

    public Vector2f toVector2f(){
        return new Vector2f((float) x, (float) y);
    }

    public Vector2d toVector2d(){
        return new Vector2d((double) x, (double) y);
    }

    @Override
    public String toString() {
        return "Vector2{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
