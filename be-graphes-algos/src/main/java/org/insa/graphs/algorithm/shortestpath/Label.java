
package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;

public class Label implements Comparable<Label> {

    protected Node sommetCourant;
    protected boolean marque;
    protected double coutRealise;
    protected Arc pere;

    public Label(Node ActualNode, boolean marque, double coutRealise, Arc pere) {
        this.sommetCourant = ActualNode;
        this.marque = marque;
        this.coutRealise = coutRealise;
        this.pere=pere;
    }

    public Node getNode() {
        return this.sommetCourant;
    }

    public boolean getMarque() {
        return this.marque;
    }

    public double getCoutRealise() {
        return this.coutRealise;
    }

    public double getCost() {
        return this.coutRealise;
    }

    public Arc getPere() {
        return this.pere;
    }

    public void setMarque() {
        this.marque = true;
    }

    /* Setter Pour Cout et Pere */
    public void setNewPath(Arc arcFather, double coutRealise){
        this.pere = arcFather;
        this.coutRealise = coutRealise;
    }
    public double getTotalCost() {
        return this.coutRealise; 
    }

    // Pour comparer deux labels dans le tas (sert à savoir qui a le plus petit coût)
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