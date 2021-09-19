import java.util.*;

public class Split
{


 // Problem parameters
 private Params params;
 private int maxVehicles;

 /* Auxiliary data structures to run the Linear Split algorithm */
 private ArrayList< ClientSplit > cliSplit = new ArrayList< ClientSplit >();
 private ArrayList< ArrayList< Double >> potential = new ArrayList< ArrayList< Double >>(); // Potential vector
 private ArrayList< ArrayList< Integer >> pred = new ArrayList< ArrayList< Integer >>(); // Indice of the predecessor in an optimal path
 private ArrayList<Double> sumDistance = new ArrayList<Double>(); // sumDistance[i] for i > 1 contains the sum of distances : sum_{k=1}^{i-1} d_{k,k+1}
 private ArrayList<Double> sumLoad = new ArrayList<Double>(); // sumLoad[i] for i >= 1 contains the sum of loads : sum_{k=1}^{i} q_k
 private ArrayList<Double> sumService = new ArrayList<Double>(); // sumService[i] for i >= 1 contains the sum of service time : sum_{k=1}^{i} s_k

 // To be called with i < j only
 // Computes the cost of propagating the label i until j
 private double propagate(int i, int j, int k)
 {
	 return potential.get(k).get(i) + sumDistance.get(j) - sumDistance.get(i + 1) + cliSplit.get(i + 1).d0_x + cliSplit.get(j).dx_0 + params.penaltyCapacity * Math.<Double>max(sumLoad.get(j) - sumLoad.get(i) - params.vehicleCapacity, 0.0);
 }

 // Tests if i dominates j as a predecessor for all nodes x >= j+1
 // We assume that i < j
 private boolean dominates(int i, int j, int k)
 {
	 return potential.get(k).get(j) + cliSplit.get(j + 1).d0_x > potential.get(k).get(i) + cliSplit.get(i + 1).d0_x + sumDistance.get(j + 1) - sumDistance.get(i + 1) + params.penaltyCapacity * (sumLoad.get(j) - sumLoad.get(i));
 }

 // Tests if j dominates i as a predecessor for all nodes x >= j+1
 // We assume that i < j
 private boolean dominatesRight(int i, int j, int k)
 {
	 return potential.get(k).get(j) + cliSplit.get(j + 1).d0_x < potential.get(k).get(i) + cliSplit.get(i + 1).d0_x + sumDistance.get(j + 1) - sumDistance.get(i + 1) + DefineConstants.MY_EPSILON;
 }

private int splitSimple(Individual indiv)
  {
      // Reinitialize the potential structures
      potential.get(0).set(0,0.0);
      for (int i = 1; i <= params.nbClients; i++)
      {
          potential.get(0).set(i, 1.e30);
      }

      // MAIN ALGORITHM -- Simple Split using Bellman's algorithm in topological order
      // This code has been maintained as it is very simple and can be easily adapted to a variety of constraints, whereas the O(n) Split has a more restricted application scope
      if (params.isDurationConstraint)
      {
          for (int i = 0; i < params.nbClients; i++)
          {
              double load = 0.0;
              double distance = 0.0;
              double serviceDuration = 0.0;
              for (int j = i + 1; j <= params.nbClients && load <= 1.5 * params.vehicleCapacity ; j++)
              {
                  load += cliSplit.get(j).demand;
                  serviceDuration += cliSplit.get(j).serviceTime;
                  if (j == i + 1)
                  {
                      distance += cliSplit.get(j).d0_x;
                  }
                  else
                  {
                      distance += cliSplit.get(j - 1).dnext;
                  }
                  double cost = distance + cliSplit.get(j).dx_0 + params.penaltyCapacity * Math.<Double>max(load - params.vehicleCapacity, 0.0) + params.penaltyDuration * Math.<Double>max(distance + cliSplit.get(j).dx_0 + serviceDuration - params.durationLimit, 0.0);
                  if (potential.get(0).get(i) + cost < potential.get(0).get(j))
                  {
                      potential.get(0).set(j, potential.get(0).get(i) + cost);
                      pred.get(0).set(j, i);
                  }
              }
          }
      }
      else
      {
          Trivial_Deque queue = new Trivial_Deque(params.nbClients + 1, 0);
          for (int i = 1; i <= params.nbClients; i++)
          {
              // The front is the best predecessor for i
              potential.get(0).set(i, propagate(queue.get_front(), i, 0));
              pred.get(0).set(i, queue.get_front());

              if (i < params.nbClients)
              {
                  // If i is not dominated by the last of the pile
                  if (!dominates(queue.get_back(), i, 0))
                  {
                      // then i will be inserted, need to remove whoever is dominated by i.
                      while (queue.size() > 0 && dominatesRight(queue.get_back(), i, 0))
                      {
                          queue.pop_back();
                      }
                      queue.push_back(i);
                  }
                  // Check iteratively if front is dominated by the next front
                  while (queue.size() > 1 && propagate(queue.get_front(), i + 1, 0) > propagate(queue.get_next_front(), i + 1, 0) - DefineConstants.MY_EPSILON)
                  {
                      queue.pop_front();
                  }
              }
          }
      }

      if (potential.get(0).get(params.nbClients) > 1.e29)
      {
          throw new RuntimeException("ERROR : no Split solution has been propagated until the last node");
      }

      // Filling the chromR structure
      for (int k = params.nbVehicles - 1; k >= maxVehicles; k--)
      {
          indiv.chromR.get(k).clear();
      }

      int end = params.nbClients;
      for (int k = maxVehicles - 1; k >= 0; k--)
      {
          indiv.chromR.get(k).clear();
          int begin = pred.get(0).get(end);
          for (int ii = begin; ii < end; ii++)
          {
              indiv.chromR.get(k).add(indiv.chromT.get(ii));
          }
          end = begin;
      }

      // Return OK in case the Split algorithm reached the beginning of the routes
     
    if (end==0){
        return 1;
    }else{
        return 0;
    }
  }
  private int splitLF(Individual indiv)
  {
	  // Initialize the potential structures
	  potential.get(0).set(0,0.0);
	  for (int k = 0; k <= maxVehicles; k++)
	  {
		  for (int i = 1; i <= params.nbClients; i++)
		  {
			  potential.get(k).set(i, 1.e30);
		  }
	  }

	  // MAIN ALGORITHM -- Simple Split using Bellman's algorithm in topological order
	  // This code has been maintained as it is very simple and can be easily adapted to a variety of constraints, whereas the O(n) Split has a more restricted application scope
	  if (params.isDurationConstraint)
	  {
		  for (int k = 0; k < maxVehicles; k++)
		  {
			  for (int i = k; i < params.nbClients && potential.get(k).get(i) < 1.e29 ; i++)
			  {
				  double load = 0.0;
				  double serviceDuration = 0.0;
				  double distance = 0.0;
				  for (int j = i + 1; j <= params.nbClients && load <= 1.5 * params.vehicleCapacity ; j++) // Setting a maximum limit on load infeasibility to accelerate the algorithm
				  {
					  load += cliSplit.get(j).demand;
					  serviceDuration += cliSplit.get(j).serviceTime;
					  if (j == i + 1)
					  {
						  distance += cliSplit.get(j).d0_x;
					  }
					  else
					  {
						  distance += cliSplit.get(j - 1).dnext;
					  }
					  double cost = distance + cliSplit.get(j).dx_0 + params.penaltyCapacity * Math.<Double>max(load - params.vehicleCapacity, 0.0) + params.penaltyDuration * Math.<Double>max(distance + cliSplit.get(j).dx_0 + serviceDuration - params.durationLimit, 0.0);
					  if (potential.get(k).get(i) + cost < potential.get(k + 1).get(j))
					  {
						  potential.get(k + 1).set(j, potential.get(k).get(i) + cost);
						  pred.get(k + 1).set(j, i);
					  }
				  }
			  }
		  }
	  }
	  else // MAIN ALGORITHM -- Without duration constraints in O(n), from "Vidal, T. (2016). Split algorithm in O(n) for the capacitated vehicle routing problem. C&OR"
	  {
		  Trivial_Deque queue = new Trivial_Deque(params.nbClients + 1, 0);
		  for (int k = 0; k < maxVehicles; k++)
		  {
			  // in the Split problem there is always one feasible solution with k routes that reaches the index k in the tour.
			  queue.reset(k);

			  // The range of potentials < 1.29 is always an interval.
			  // The size of the queue will stay >= 1 until we reach the end of this interval.
			  for (int i = k + 1; i <= params.nbClients && queue.size() > 0; i++)
			  {
				  // The front is the best predecessor for i
				  potential.get(k + 1).set(i, propagate(queue.get_front(), i, k));
				  pred.get(k + 1).set(i, queue.get_front());

				  if (i < params.nbClients)
				  {
					  // If i is not dominated by the last of the pile
					  if (!dominates(queue.get_back(), i, k))
					  {
						  // then i will be inserted, need to remove whoever he dominates
						  while (queue.size() > 0 && dominatesRight(queue.get_back(), i, k))
						  {
							  queue.pop_back();
						  }
						  queue.push_back(i);
					  }

					  // Check iteratively if front is dominated by the next front
					  while (queue.size() > 1 && propagate(queue.get_front(), i + 1, k) > propagate(queue.get_next_front(), i + 1, k) - DefineConstants.MY_EPSILON)
					  {
						  queue.pop_front();
					  }
				  }
			  }
		  }
	  }

	  if (potential.get(maxVehicles).get(params.nbClients) > 1.e29)
	  {
		  throw new RuntimeException("ERROR : no Split solution has been propagated until the last node");
	  }

	  // It could be cheaper to use a smaller number of vehicles
	  double minCost = potential.get(maxVehicles).get(params.nbClients);
	  int nbRoutes = maxVehicles;
	  for (int k = 1; k < maxVehicles; k++)
	  {
		  if (potential.get(k).get(params.nbClients) < minCost)
		  {
				  minCost = potential.get(k).get(params.nbClients);
				  nbRoutes = k;
		  }
	  }

	  // Filling the chromR structure
	  for (int k = params.nbVehicles - 1; k >= nbRoutes ; k--)
	  {
		  indiv.chromR.get(k).clear();
	  }

	  int end = params.nbClients;
	  for (int k = nbRoutes - 1; k >= 0; k--)
	  {
		  indiv.chromR.get(k).clear();
		  int begin = pred.get(k + 1).get(end);
		  for (int ii = begin; ii < end; ii++)
		  {
			  indiv.chromR.get(k).add(indiv.chromT.get(ii));
		  }
		  end = begin;
	  }

	  // Return OK in case the Split algorithm reached the beginning of the routes
        if (end==0){
            return 1;
        }else{
            return 0;
        }
  }


  // General Split function (tests the unlimited fleet, and only if it does not produce a feasible solution, runs the Split algorithm for limited fleet)
  public final void generalSplit(Individual indiv, int nbMaxVehicles)
  {
	  // Do not apply Split with fewer vehicles than the trivial (LP) bin packing bound
	  maxVehicles = (int) Math.max(nbMaxVehicles, Math.ceil(params.totalDemand / params.vehicleCapacity));

	  // Initialization of the data structures for the linear split algorithms
	  // Direct application of the code located at https://github.com/vidalt/Split-Library
	  for (int i = 1; i <= params.nbClients; i++)
	  {
		  cliSplit.get(i).demand = params.cli.get(indiv.chromT.get(i - 1)).demand;
		  cliSplit.get(i).serviceTime = params.cli.get(indiv.chromT.get(i - 1)).serviceDuration;
		  cliSplit.get(i).d0_x = params.timeCost.get(0).get(indiv.chromT.get(i - 1));
		  cliSplit.get(i).dx_0 = params.timeCost.get(indiv.chromT.get(i - 1)).get(0);
		  if (i < params.nbClients)
		  {
			  cliSplit.get(i).dnext = params.timeCost.get(indiv.chromT.get(i - 1)).get(indiv.chromT.get(i));
		  }
		  else
		  {
			  cliSplit.get(i).dnext = -1.e30;
		  }
		  sumLoad.set(i, sumLoad.get(i - 1) + cliSplit.get(i).demand);
		  sumService.set(i, sumService.get(i - 1) + cliSplit.get(i).serviceTime);
		  sumDistance.set(i, sumDistance.get(i - 1) + cliSplit.get(i - 1).dnext);
	  }

	  // We first try the simple split, and then the Split with limited fleet if this is not successful
	  if (splitSimple(indiv) == 0)
	  {
		  splitLF(indiv);
	  }

	  // Build up the rest of the Individual structure
	  indiv.evaluateCompleteCost();
  }

  // Constructor
  public Split(Params params)
  {
	  this.params = params;
	  // Structures of the linear Split
	  cliSplit = new ArrayList<ClientSplit>(params.nbClients + 1);
	  sumDistance = new ArrayList<Double>(params.nbClients + 1);
	  sumLoad = new ArrayList<Double>(params.nbClients + 1);
	  sumService = new ArrayList<Double>(params.nbClients + 1);
	  potential = tangible.VectorHelper.nestedArrayList(params.nbVehicles + 1, params.nbClients + 1, 1.e30);
	  pred = tangible.VectorHelper.nestedArrayList(params.nbVehicles + 1, params.nbClients + 1, 0);
  }    

    private int potential(int i, int i0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
      
}

