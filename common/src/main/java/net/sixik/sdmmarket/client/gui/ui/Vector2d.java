package net.sixik.sdmmarket.client.gui.ui;

public class Vector2d {
    public double x;
    public double y;

    public Vector2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static Vector2d create(double x, double y){
        return new Vector2d(x, y);
    }

    public Vector2d add(Vector2d other) {
        return new Vector2d(x + other.x, y + other.y);
    }

    public Vector2d subtract(Vector2d other) {
        return new Vector2d(x - other.x, y - other.y);
    }

    public Vector2d multiply(double scalar) {
        return new Vector2d(x * scalar, y * scalar);
    }

    public Vector2d divide(double scalar) {
        return new Vector2d(x / scalar, y / scalar);
    }

    public Vector2d multiply(Vector2d scalar) {
        return new Vector2d(x * scalar.x, y * scalar.y);
    }

    public Vector2d divide(Vector2d scalar) {
        return new Vector2d(x / scalar.x, y / scalar.y);
    }

    public double lengthSquared() {
        return x * x + y * y;
    }

    public double length() {
        return Math.sqrt(lengthSquared());
    }

    public Vector2d normalize() {
        double len = length();
        return new Vector2d(x / len, y / len);
    }

    public double dot(Vector2d other) {
        return x * other.x + y * other.y;
    }

    public Vector2d cross(Vector2d other) {
        return new Vector2d(y * other.x - x * other.y, x * other.y + y * other.x);
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
}
