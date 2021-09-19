import java.util.*;

// Simple Deque which is used for all Linear Split algorithms
public class Trivial_Deque {
	public ArrayList<Integer> myDeque = new ArrayList<Integer>(); // Simply a vector structure to keep the elements of
																	// the queue
	public int indexFront; // Index of the front element
	public int indexBack; // Index of the back element

	public final void pop_front() {
		indexFront++;
	} // Removes the front element of the queue D

	public final void pop_back() {
		indexBack--;
	} // Removes the back element of the queue D

	public final void push_back(int i) {
		indexBack++;
		myDeque.set(indexBack, i);
	} // Appends a new element to the back of the queue D

	public final int get_front() {
		return myDeque.get(indexFront);
	}

	public final int get_next_front() {
		return myDeque.get(indexFront + 1);
	}

	public final int get_back() {
		return myDeque.get(indexBack);
	}

	public final void reset(int firstNode) {
		myDeque.set(0, firstNode);
		indexBack = 0;
		indexFront = 0;
	}

	public final int size() {
		return indexBack - indexFront + 1;
	}

	public Trivial_Deque(int nbElements, int firstNode) {
		myDeque = new ArrayList<Integer>(nbElements);
		myDeque.set(0, firstNode);
		indexBack = 0;
		indexFront = 0;
	}
}