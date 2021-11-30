package com.gangling.scm.base.utils;

/**
 * 包含两个属性的泛型小容器
 *
 * @author liulian
 */
public class TwoPropTuple<A, B> {

    public final A firstProp;

    public final B secondProp;

    public TwoPropTuple(A a, B b) {
        firstProp = a;
        secondProp = b;
    }

    @Override
    public String toString() {
        return "( firstProp = " + firstProp.toString() + ", secondProp = " + secondProp.toString() + " )";
    }
}
