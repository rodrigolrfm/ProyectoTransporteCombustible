
import javax.lang.model.SourceVersion;

public class ThreeBestInsert
{
	public int whenLastCalculated;
	public double[] bestCost = new double[3];
	public Node[] bestLocation = Arrays.initializeWithDefaultNodeInstances(3);

	public final void compareAndAdd(double costInsert, Node placeInsert)
	{
		if (costInsert >= bestCost[2])
		{
			return;
		}
		else if (costInsert >= bestCost[1])
		{
			bestCost[2] = costInsert;
			bestLocation[2] = placeInsert;
		}
		else if (costInsert >= bestCost[0])
		{
			bestCost[2] = bestCost[1];
			bestLocation[2] = bestLocation[1];
			bestCost[1] = costInsert;
			bestLocation[1] = placeInsert;
		}
		else
		{
			bestCost[2] = bestCost[1];
			bestLocation[2] = bestLocation[1];
			bestCost[1] = bestCost[0];
			bestLocation[1] = bestLocation[0];
			bestCost[0] = costInsert;
			bestLocation[0] = placeInsert;
		}
	}

	// Resets the structure (no insertion calculated)
    public final void reset()
    {
        bestCost[0] = 1.e30;
        bestLocation[0] = null;
        bestCost[1] = 1.e30;
        bestLocation[1] = null;
        bestCost[2] = 1.e30;
        bestLocation[2] = null;
    }

    public ThreeBestInsert()
    {
        reset();
    }
}