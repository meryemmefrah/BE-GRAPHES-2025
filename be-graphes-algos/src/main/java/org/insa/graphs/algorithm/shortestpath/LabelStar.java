
package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;

public class LabelStar extends Label {

    private double coutEstime;

    public LabelStar(Node node, boolean marque, double coutrealise, Arc father, double coutEstime) {
        super(node, marque, coutrealise, father);
        this.coutEstime = coutEstime;
    }

    @Override
    public double getTotalCost() {
        return this.coutrealise + this.coutEstime;
    }

    public double getCoutEstime() {
        return this.coutEstime;
    }

    
    @Override
    public int compareTo(Label other) {
        double thisTotal = this.getCost() + this.coutEstime;
        double otherTotal;
        if (other instanceof LabelStar) {
            otherTotal = other.getCost() + ((LabelStar) other).coutEstime;
        } else {
            otherTotal = other.getCost();
        }
    return Double.compare(thisTotal, otherTotal);
    }
    

}


