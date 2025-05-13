
//changer un peu le code


package org.insa.graphs.algorithm.shortestpath;
import java.util.ArrayList;
import java.util.Collections;

import org.insa.graphs.algorithm.AbstractSolution;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public ArrayList<Label> Init(int tailleGraphe)
    {
        ArrayList<Label> labelSommets = new ArrayList<>();

        for(int i = 0; i< tailleGraphe; i++ )
        {
            labelSommets.add(new Label(data.getGraph().get(i), false, -1.0, null));
        }
        return labelSommets;

    }




    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected ShortestPathSolution doRun() {
        final ShortestPathData data = getInputData();

        int tailleGraphe = data.getGraph().size();
        int index =0;


        ArrayList<Label> labelSommets = new ArrayList<Label>();

        BinaryHeap<Label> leTas = new BinaryHeap<Label>();

        //Initialisation 

        labelSommets = Init(tailleGraphe);
        Label currentLabel;


        currentLabel = labelSommets.get(data.getOrigin().getId());
        currentLabel.setNewPath(null, 0);
        currentLabel.setMarque();
        leTas.insert(currentLabel);

        Node currentNode = data.getOrigin() ;
        double currentCost;
        double newCost;
        Label newLabel;
        notifyOriginProcessed(currentNode);

        //Boucle principale ----------------------------------------------------


        while (!leTas.isEmpty() && !currentNode.equals(data.getDestination()))
        {

            currentLabel = leTas.deleteMin();
            currentNode = currentLabel.getNode();
            index = currentNode.getId();
            currentLabel.setMarque();


            notifyNodeMarked(currentNode);

            
            for(Arc arc : currentNode.getSuccessors())
            {
                if(data.isAllowed(arc))
                {
                    newLabel = labelSommets.get(arc.getDestination().getId());
                    if(!newLabel.getMarque())
                    {
                        currentCost = newLabel.getCost();
                        newCost = data.getCost(arc) + currentLabel.getCost();
                        if(currentCost == -1.0)
                        {
                            newLabel.setNewPath(arc, newCost);
                            leTas.insert(newLabel);
                            notifyNodeReached(newLabel.getNode());
                        }
                        else if(newCost < currentCost)
                        {
                            leTas.remove(newLabel);
                            newLabel.setNewPath(arc, newCost);
                            leTas.insert(newLabel);
                        }
                    }
                }
                
            }
        }


        //Retour chemin obtenu 

        Node finalNode = data.getDestination();
        index = 0;
        ArrayList<Arc> arcListe = new ArrayList<Arc>() ;

        if(labelSommets.get(data.getDestination().getId()).getCost() == -1)
        {
            return new ShortestPathSolution(data, AbstractSolution.Status.INFEASIBLE);
        }

        while(!finalNode.equals(data.getOrigin())  && index < tailleGraphe)
        {
            arcListe.add(labelSommets.get(finalNode.getId()).father);
            finalNode = arcListe.get(arcListe.size()-1).getOrigin();
            index++;
        }
        if(index != tailleGraphe)
        {
            Collections.reverse(arcListe);
            notifyDestinationReached(data.getDestination());
            return new ShortestPathSolution(data, AbstractSolution.Status.FEASIBLE, new Path(data.getGraph(),arcListe ));
        }

        return new ShortestPathSolution(data, AbstractSolution.Status.INFEASIBLE);

    }


}