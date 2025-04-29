package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;

public class Label {

    protected Node node;
    protected boolean marque;
    protected double coutrealise;
    protected Arc father;

    public Label(Node node, boolean marque, double coutrealise, Arc father){
        this.node=node;
        this.marque=true;
        this.coutrealise=-1;
        this.father=null;


    }

    public Node getNode(){
        return this.node;
    }

    public boolean getMarque(){
        return this.marque;
    }

    public double coutrealise(){
        return this.coutrealise;
    }

    public Arc getFather(){
        return this.father;
    }

    public void setMarque(){
        this.marque=true;
    }



    
}
