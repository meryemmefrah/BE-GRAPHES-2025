
package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;

public class Label implements Comparable<Label> {

    protected Node ActualNode;
    protected boolean marque;
    protected double coutrealise;
    protected Arc father;

    public Label(Node ActualNode, boolean marque, double coutrealise, Arc father) {
        this.ActualNode = ActualNode;
        this.marque = marque;
        this.coutrealise = coutrealise;
        this.father=father;
    }

    public Node getNode() {
        return this.ActualNode;
    }

    public boolean getMarque() {
        return this.marque;
    }

    public double getCoutRealise() {
        return this.coutrealise;
    }

    public double getCost() {
        return this.coutrealise;
    }

    public Arc getFather() {
        return this.father;
    }

    public void setMarque() {
        this.marque = true;
    }

    /* Setter Pour Cout et Pere */
    public void setNewPath(Arc arcFather, double coutRealise){
        this.father = arcFather;
        this.coutrealise = coutRealise;
    }
    public double getTotalCost() {
        return this.coutrealise; 
    }

    @Override
    public int compareTo(Label other) {

        if (this.getTotalCost() < other.getTotalCost())
        {
            return -1;
        }
        else if (this.getTotalCost() > other.getTotalCost())
        {
            return 1;
        }
        return 0;
    }
}


