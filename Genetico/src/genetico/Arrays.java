package genetico;
import java.util.*;
import java.io.*;

public final class Arrays
{
	public static Node[] initializeWithDefaultNodeInstances(int length)
	{
		Node[] array = new Node[length];
		for (int i = 0; i < length; i++)
		{
			array[i] = new Node();
		}
		return array;
	}

	public static <T extends java.io.Closeable> void deleteArray(T[] array)
	{
		for (T element : array)
		{
			if (element != null)
				element.close();
		}
	}
}