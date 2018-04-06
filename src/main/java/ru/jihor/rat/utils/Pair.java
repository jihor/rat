package ru.jihor.rat.utils;

/**
 * @author jihor (jihor@ya.ru)
 * Created on 2018-04-06
 */
public class Pair<A, B> {

    private Pair(A _1, B _2) {
        this._1 = _1;
        this._2 = _2;
    }

    private final A _1;
    public A _1(){ return _1;}

    private final B _2;
    public B _2(){ return _2;}

    public static <A, B> Pair<A, B> of(A a, B b){
        return new Pair<>(a, b);
    }
}
