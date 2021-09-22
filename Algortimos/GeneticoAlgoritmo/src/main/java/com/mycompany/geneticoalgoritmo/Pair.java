package com.mycompany.geneticoalgoritmo;

import java.util.Comparator;

public final class Pair<T1 extends Comparable<T1>, T2 > implements java.lang.Comparable<Pair<T1,T2>>
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
	public int compareTo(Pair<T1, T2> p2) {
		// TODO Auto-generated method stub
		int comparator = this.first.compareTo(p2.first);
		return comparator;
	}
}