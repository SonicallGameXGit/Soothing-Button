package me.sgx.sb.util;

public class Maths {
    public static boolean isPointInsideAabb(float pointX, float pointY, float aabbX, float aabbY, float aabbWidth, float aabbHeight) {
        return pointX >= aabbX && pointX <= aabbX + aabbWidth && pointY >= aabbY && pointY <= aabbY + aabbHeight;
    }

    public static double scaleRange(double value, Range currentRange, Range nextRange) {
        return ((value - currentRange.min()) / (currentRange.max() - currentRange.min())) * (nextRange.max() - nextRange.min()) + nextRange.min();
    }

    public record Range(double min, double max) { }
}