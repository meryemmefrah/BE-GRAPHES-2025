
package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;
import java.util.Collections;

import org.insa.graphs.algorithm.AbstractSolution;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Point;
import org.insa.graphs.model.Path;

public class AStarAlgorithm extends ShortestPathAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }

    // Calcule le coût estimé à vol d'oiseau entre deux noeuds
    private double getEstimatedCost(Node from, Node to, ShortestPathData data) {
        double distance = Point.distance(from.getPoint(), to.getPoint());
        if (data.getMode() == ShortestPathData.Mode.TIME) {
            double maxSpeed = data.getGraph().getGraphInformation().getMaximumSpeed();
            if (maxSpeed <= 0)
                maxSpeed = 130.0; // km/h par défaut
            return distance / (maxSpeed * 1000.0 / 3600.0); // conversion m/s
        }
        return distance;
    }

    @Override
    protected ShortestPathSolution doRun() {
        final ShortestPathData data = getInputData();
        int tailleGraphe = data.getGraph().size();

        ArrayList<LabelStar> labelSommets = new ArrayList<>(tailleGraphe);
        Node destination = data.getDestination();

        // Initialisation des labels
        for (int i = 0; i < tailleGraphe; i++) {
            Node node = data.getGraph().get(i);
            double coutEstime = getEstimatedCost(node, destination, data);
            labelSommets.add(new LabelStar(node, false, -1.0, null, coutEstime));
        }

        BinaryHeap<Label> heap = new BinaryHeap<Label>();

        LabelStar originLabel = labelSommets.get(data.getOrigin().getId());
        originLabel.setNewPath(null, 0);
        heap.insert(originLabel);

        notifyOriginProcessed(data.getOrigin());

        while (!heap.isEmpty()) {
            LabelStar currentLabel = (LabelStar) heap.deleteMin();
            Node currentNode = currentLabel.getNode();

            if (currentLabel.getMarque())
                continue;
            currentLabel.setMarque();
            notifyNodeMarked(currentNode);

            if (currentNode.equals(destination))
                break;

            for (Arc arc : currentNode.getSuccessors()) {
                if (!data.isAllowed(arc))
                    continue;
                Node succ = arc.getDestination();
                LabelStar succLabel = labelSommets.get(succ.getId());

                if (!succLabel.getMarque()) {
                    double newCost = currentLabel.getCost() + data.getCost(arc);
                    if (succLabel.getCost() == -1.0 || newCost < succLabel.getCost()) {
                        try {
                            heap.remove(succLabel);
                        } catch (Exception ignored) {
                        }
                        succLabel.setNewPath(arc, newCost);
                        heap.insert(succLabel);
                        notifyNodeReached(succ);
                    }
                }
            }
        }

        // Construction du chemin
        ArrayList<Arc> arcs = new ArrayList<>();
        Node node = destination;
        LabelStar label = labelSommets.get(node.getId());

        if (label.getCost() == -1.0) {
            return new ShortestPathSolution(data, AbstractSolution.Status.INFEASIBLE);
        }

        while (node != data.getOrigin()) {
            Arc arc = label.getFather();
            if (arc == null)
                break;
            arcs.add(arc);
            node = arc.getOrigin();
            label = labelSommets.get(node.getId());
        }
        Collections.reverse(arcs);

        notifyDestinationReached(destination);
        return new ShortestPathSolution(data, AbstractSolution.Status.FEASIBLE, new Path(data.getGraph(), arcs));
    }
}



