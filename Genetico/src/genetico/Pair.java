package genetico;

import java.util.Comparator;

public final class Pair<T1, T2> implements Comparator
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
        Pair p1 = Pair.class.cast(o1);
        Pair p2 = Pair.class.cast(o2);
        if (p1.first>p2.first){
               return 1 ;
        }else if (p1.first<p2.first){
                return -1;
        }else{
                return 0;
        }
    }     
        
}