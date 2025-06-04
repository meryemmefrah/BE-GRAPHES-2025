package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;
import java.util.Collections;

import org.insa.graphs.algorithm.AbstractSolution;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.Point;

public class AStarAlgorithm extends ShortestPathAlgorithm {

    public AStarAlgorithm(ShortestPathData donnees) {
        super(donnees);
    }

    // Fonction pour calculer l'heuristique (distance à vol d'oiseau ou temps estimé)
    private double coutEstime(Node depuis, Node vers, ShortestPathData donnees) {
        double distance = Point.distance(depuis.getPoint(), vers.getPoint());
        if (donnees.getMode() == ShortestPathData.Mode.TIME) {
            double vitesseMax = donnees.getGraph().getGraphInformation().getMaximumSpeed();
            if (vitesseMax <= 0)
                vitesseMax = 130.0; // On prend 130km/h si la vitesse max n'est pas définie
            return distance / (vitesseMax * 1000.0 / 3600.0); // conversion en secondes
        }
        return distance;
    }

    @Override
    protected ShortestPathSolution doRun() {
        // On récupère les données du problème
        final ShortestPathData donnees = getInputData();
        int nbSommets = donnees.getGraph().size();
        Node destination = donnees.getDestination();

        // On crée la liste des labels pour chaque sommet
        ArrayList<LabelStar> listeLabels = new ArrayList<>(nbSommets);
        boolean[] sommetsVisites = new boolean[nbSommets];

        // Initialisation des labels avec coût infini et heuristique
        for (int i = 0; i < nbSommets; i++) {
            Node sommet = donnees.getGraph().get(i);
            double heuristique = coutEstime(sommet, destination, donnees);
            listeLabels.add(new LabelStar(sommet, false, Double.POSITIVE_INFINITY, null, heuristique));
        }

        BinaryHeap<Label> tas = new BinaryHeap<>();
        LabelStar labelOrigine = listeLabels.get(donnees.getOrigin().getId());
        labelOrigine.setNewPath(null, 0.0); // Le coût pour atteindre l'origine est 0
        tas.insert(labelOrigine);

        notifyOriginProcessed(donnees.getOrigin()); // On signale que l'origine est traitée

        // Boucle principale de l'algo A*
        while (!tas.isEmpty()) {
            LabelStar labelMin = (LabelStar) tas.deleteMin(); // On prend le label avec le plus petit coût total
            int idCourant = labelMin.getNode().getId();
            if (sommetsVisites[idCourant]) continue; // Si déjà visité, on passe
            sommetsVisites[idCourant] = true;
            notifyNodeMarked(labelMin.getNode());

            // Si on a atteint la destination, on arrête tout
            if (labelMin.getNode().equals(destination)) break;

            // On regarde tous les voisins du sommet courant
            for (Arc arc : labelMin.getNode().getSuccessors()) {
                if (!donnees.isAllowed(arc)) continue; // On saute les arcs interdits
                Node sommetSuccesseur = arc.getDestination();
                int idSuccesseur = sommetSuccesseur.getId();
                if (sommetsVisites[idSuccesseur]) continue; // Déjà traité

                LabelStar labelSuccesseur = listeLabels.get(idSuccesseur);
                double coutTemporaire = labelMin.getCost() + donnees.getCost(arc);
                // Si on trouve un chemin plus court
                if (coutTemporaire < labelSuccesseur.getCost()) {
                    labelSuccesseur.setNewPath(arc, coutTemporaire); // On met à jour le père et le coût
                    tas.insert(labelSuccesseur); // On ajoute dans le tas
                    notifyNodeReached(sommetSuccesseur); // On signale qu'on a atteint ce sommet
                }
            }
        }

        // On reconstruit le chemin à partir de la destination
        ArrayList<Arc> arcsChemin = new ArrayList<>();
        Node sommetActuel = destination;
        LabelStar labelDestination = listeLabels.get(sommetActuel.getId());

        // Si le coût est infini, il n'y a pas de solution
        if (labelDestination.getCost() == Double.POSITIVE_INFINITY)
            return new ShortestPathSolution(donnees, AbstractSolution.Status.INFEASIBLE);

        // On remonte les arcs depuis la destination jusqu'à l'origine
        while (sommetActuel != donnees.getOrigin()) {
            Arc arc = labelDestination.getPere();
            if (arc == null) break;
            arcsChemin.add(arc);
            sommetActuel = arc.getOrigin();
            labelDestination = listeLabels.get(sommetActuel.getId());
        }
        Collections.reverse(arcsChemin); // On remet le chemin dans le bon ordre

        notifyDestinationReached(destination);
        return new ShortestPathSolution(donnees, AbstractSolution.Status.FEASIBLE, new Path(donnees.getGraph(), arcsChemin));
    }
}