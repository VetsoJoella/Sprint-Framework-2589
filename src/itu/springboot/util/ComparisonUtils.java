package itu.springboot.util;

public class ComparisonUtils {

    public static <T extends Comparable<T>> boolean inferiorTo(T a, T b) {
        return a.compareTo(b) < 0;
    }

    public static <T extends Comparable<T>> boolean superiorTo(T a, T b) {
        return a.compareTo(b) < 0;
    }
}