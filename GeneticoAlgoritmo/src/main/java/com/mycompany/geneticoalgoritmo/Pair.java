package com.mycompany.geneticoalgoritmo;

import java.util.Comparator;

public final class Pair<T1 extends Comparable<T1>, T2 > implements Comparator
{
    public T1 first;
    public T2 second;

    public Pair()
    {
        first = null;
        second = null;
    }

    public Pair(T1 firstValue, T2 secondValue)
    {
        first = firstValue;
        second = secondValue;
    }

    public Pair(Pair<T1, T2> pairToCopy)
    {
        first = pairToCopy.first;
        second = pairToCopy.second;
    }


    @Override
    public int compare(Object o1, Object o2) {
        Pair<T1, T2> p1 = Pair.class.cast(o1);
        Pair<T1, T2> p2 = Pair.class.cast(o2);
        int comparator = p1.first.compareTo(p2.first);
        return comparator;
    }
}
