package org.insa.graphs.algorithm.utils;

import java.util.ArrayList;

/**
 * Implements a binary heap containing elements of type E. Note that all comparisons are
 * based on the compareTo method, hence E must implement Comparable
 *
 * @author Mark Allen Weiss
 * @author DLB
 */
public class BinaryHeap<E extends Comparable<E>> implements PriorityQueue<E> {

    // Number of elements in heap.
    private int currentSize;

    // The heap array.
    protected final ArrayList<E> array;

    /**
     * Construct a new empty binary heap.
     */
    public BinaryHeap() {
        this.currentSize = 0;
        this.array = new ArrayList<E>();
    }

    /**
     * Construct a copy of the given heap.
     *
     * @param heap Binary heap to copy.
     */
    public BinaryHeap(BinaryHeap<E> heap) {
        this.currentSize = heap.currentSize;
        this.array = new ArrayList<E>(heap.array);
    }

    /**
     * Set an element at the given index.
     *
     * @param index Index at which the element should be set.
     * @param value Element to set.
     */
    private void arraySet(int index, E value) {
        if (index == this.array.size()) {
            this.array.add(value);
        }
        else {
            this.array.set(index, value);
        }
    }

    /**
     * @return Index of the parent of the given index.
     */
    protected int indexParent(int index) {
        return (index - 1) / 2;
    }

    /**
     * @return Index of the left child of the given index.
     */
    protected int indexLeft(int index) {
        return index * 2 + 1;
    }

    /**
     * Internal method to percolate up in the heap.
     *
     * @param index Index at which the percolate begins.
     */
    private void percolateUp(int index) {
        E x = this.array.get(index);

        for (; index > 0 && x.compareTo(this.array.get(indexParent(index))) < 0; index =
                indexParent(index)) {
            E moving_val = this.array.get(indexParent(index));
            this.arraySet(index, moving_val);
        }

        this.arraySet(index, x);
    }

    /**
     * Internal method to percolate down in the heap.
     *
     * @param index Index at which the percolate begins.
     */
    private void percolateDown(int index) {
        int ileft = indexLeft(index);
        int iright = ileft + 1;

        if (ileft < this.currentSize) {
            E current = this.array.get(index);
            E left = this.array.get(ileft);
            boolean hasRight = iright < this.currentSize;
            E right = (hasRight) ? this.array.get(iright) : null;

            if (!hasRight || left.compareTo(right) < 0) {
                // Left is smaller
                if (left.compareTo(current) < 0) {
                    this.arraySet(index, left);
                    this.arraySet(ileft, current);
                    this.percolateDown(ileft);
                }
            }
            else {
                // Right is smaller
                if (right.compareTo(current) < 0) {
                    this.arraySet(index, right);
                    this.arraySet(iright, current);
                    this.percolateDown(iright);
                }
            }
        }
    }

    @Override
    public boolean isEmpty() {
        return this.currentSize == 0;
    }

    @Override
    public int size() {
        return this.currentSize;
    }

    @Override
    public void insert(E x) {
        int index = this.currentSize++;
        this.arraySet(index, x);
        this.percolateUp(index);
    }

    @Override
    
    public void remove(E x) throws ElementNotFoundException {
        boolean found = false; //indiquer si l'élément à supprimer a été trouvé.
        for (int i = 0; i < this.currentSize; i++) { //parcourir tous les éléments du tas.
            if (this.array.get(i).equals(x)) { //vérifier si l'élément courant est égal à l'élément à supprimer.
                found = true; //marquer que l'élément a été trouvé.
                E lastElement = this.array.get(this.currentSize - 1); //récupèrer le dernier élément du tas.
                this.currentSize--; //réduire la taille du tas.
                if (i != this.currentSize) { //si l'élément à supprimer n'est pas le dernier élément 
                    this.arraySet(i, lastElement); //remplacer l'élément à supprimer par le dernier élément.
                    if (lastElement.compareTo(x) > 0) { //si le dernier élément est plus grand que l'élément supprimé 
                        percolateDown(i); //réorganiser le tas en descendant l'élément.
                    } else {
                        percolateUp(i); //sinon réorganiser le tas en montant l'élément.
                    }
                }
                break; 
            }
        }
        if (!found) { //si l'élément n'a pas été trouvé dans le tas :
            throw new ElementNotFoundException(x); // Lève une exception.
        }

    }






    @Override
    public E findMin() throws EmptyPriorityQueueException {
        if (isEmpty())
            throw new EmptyPriorityQueueException();
        return this.array.get(0);
    }

    @Override
    public E deleteMin() throws EmptyPriorityQueueException {
        E minItem = findMin();
        E lastItem = this.array.get(--this.currentSize);
        this.arraySet(0, lastItem);
        this.percolateDown(0);
        return minItem;
    }

    /**
     * Creates a multi-lines string representing a sorted view of this binary heap.
     *
     * @return a string containing a sorted view this binary heap.
     */
    public String toStringSorted() {
        return BinaryHeapFormatter.toStringSorted(this, -1);
    }

    /**
     * Creates a multi-lines string representing a sorted view of this binary heap.
     *
     * @param maxElement Maximum number of elements to display. or {@code -1} to display
     *        all the elements.
     * @return a string containing a sorted view this binary heap.
     */
    public String toStringSorted(int maxElement) {
        return BinaryHeapFormatter.toStringSorted(this, maxElement);
    }

    /**
     * Creates a multi-lines string representing a tree view of this binary heap.
     *
     * @return a string containing a tree view of this binary heap.
     */
    public String toStringTree() {
        return BinaryHeapFormatter.toStringTree(this, Integer.MAX_VALUE);
    }

    /**
     * Creates a multi-lines string representing a tree view of this binary heap.
     *
     * @param maxDepth Maximum depth of the tree to display.
     * @return a string containing a tree view of this binary heap.
     */
    public String toStringTree(int maxDepth) {
        return BinaryHeapFormatter.toStringTree(this, maxDepth);
    }

    @Override
    public String toString() {
        return BinaryHeapFormatter.toStringTree(this, 8);
    }

    /*
     * int i; E lastE; boolean notFound = true; for(i = 0; i< this.currentSize; i++) {
     * if(array.get(i).equals(x)) { lastE = array.get(--currentSize); this.arraySet(i,
     * lastE); notFound = false; if(lastE.compareTo(x)>0) { percolateDown(i); } else {
     * percolateUp(i); } break; } } if(notFound) { throw new
     * ElementNotFoundException(x); }
     */

}
