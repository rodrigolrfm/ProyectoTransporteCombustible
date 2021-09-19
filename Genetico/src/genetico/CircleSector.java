// Simple data structure to represent circle sectors
// Angles are measured in [0,65535] instead of [0,359], in such a way that modulo operations are much faster (since 2^16 = 65536)
// Credit to Fabian Giesen at "https://web.archive.org/web/20200912191950/https://fgiesen.wordpress.com/2015/09/24/intervals-in-modular-arithmetic/" for useful implementation tips regarding interval overlaps in modular arithmetics 
public class CircleSector
{
	public int start;
	public int end;

	// Positive modulo 65536
	public static int positive_mod(int i)
	{
		// 1) Using the formula positive_mod(n,x) = (n % x + x) % x
		// 2) Moreover, remark that "n % 65536" should be automatically compiled in an optimized form as "n & 0xffff" for faster calculations
		return (i % 65536 + 65536) % 65536;
	}

	// Initialize a circle sector from a single point
	public final void initialize(int point)
	{
		start = point;
		end = point;
	}

	// Tests if a point is enclosed in the circle sector
	public final boolean isEnclosed(int point)
	{
		return (positive_mod(point - start) <= positive_mod(end - start));
	}

	// Tests overlap of two circle sectors
	public static boolean overlap(final CircleSector sector1, final CircleSector sector2)
	{
		return ((positive_mod(sector2.start - sector1.start) <= positive_mod(sector1.end - sector1.start)) || (positive_mod(sector1.start - sector2.start) <= positive_mod(sector2.end - sector2.start)));
	}

	// Extends the circle sector to include an additional point 
	// Done in a "greedy" way, such that the resulting circle sector is the smallest
	public final void extend(int point)
	{
		if (!isEnclosed(point))
		{
			if (positive_mod(point - end) <= positive_mod(start - point))
			{
				end = point;
			}
			else
			{
				start = point;
			}
		}
	}
}