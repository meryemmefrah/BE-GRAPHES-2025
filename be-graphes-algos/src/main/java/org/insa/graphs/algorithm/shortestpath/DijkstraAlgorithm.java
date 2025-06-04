package org.insa.graphs.algorithm.shortestpath;
import java.util.ArrayList;
import java.util.Collections;

import org.insa.graphs.algorithm.AbstractSolution;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    // Méthode pour initialiser les labels (pas utilisée ici mais laissée au cas où)
    public ArrayList<Label> initialiserLabels(int tailleGraphe){
        ArrayList<Label> listeLabels = new ArrayList<>();
        for(int i = 0; i< tailleGraphe; i++ )
        {
            listeLabels.add(new Label(data.getGraph().get(i), false, -1.0, null));
        }
        return listeLabels;
    }

    public DijkstraAlgorithm(ShortestPathData donnees) {
        super(donnees);
    }

    @Override
    protected ShortestPathSolution doRun() {
        // On récupère les données du problème
        final ShortestPathData donnees = getInputData();
        final int nbSommets = donnees.getGraph().size();

        // On crée la liste des labels pour chaque sommet
        ArrayList<Label> listeLabels = new ArrayList<>(nbSommets);
        BinaryHeap<Label> tas = new BinaryHeap<>();

        // Initialisation des labels : tous à l'infini sauf l'origine
        for (int i = 0; i < nbSommets; i++) {
            listeLabels.add(new Label(donnees.getGraph().get(i), false, Double.POSITIVE_INFINITY, null));
        }
        Label labelOrigine = listeLabels.get(donnees.getOrigin().getId());
        labelOrigine.setNewPath(null, 0.0); // Le coût pour atteindre l'origine est 0
        tas.insert(labelOrigine);

        notifyOriginProcessed(donnees.getOrigin()); // On signale que l'origine est traitée

        // Boucle principale de Dijkstra
        while (!tas.isEmpty()) {
            Label labelMin = tas.deleteMin(); // On prend le label avec le plus petit coût
            Node sommetCourant = labelMin.getNode();
            labelMin.setMarque(); // On marque le sommet comme traité
            notifyNodeMarked(sommetCourant);

            // Si on a atteint la destination, on peut s'arrêter
            if (sommetCourant.equals(donnees.getDestination())) {
                break;
            }

            // On parcourt tous les arcs sortants du sommet courant
            for (Arc arc : sommetCourant.getSuccessors()) {
                if (!donnees.isAllowed(arc)) continue; // On vérifie si l'arc est autorisé

                Node sommetSuccesseur = arc.getDestination();
                Label labelSuccesseur = listeLabels.get(sommetSuccesseur.getId());

                // Si le sommet n'est pas encore marqué
                if (!labelSuccesseur.getMarque()) {
                    double coutTemporaire = labelMin.getCost() + donnees.getCost(arc); // Nouveau coût
                    // Si on trouve un chemin plus court
                    if (coutTemporaire < labelSuccesseur.getCost()) {
                        // Si le label était déjà dans le tas, on le retire (pour éviter les doublons)
                        if (labelSuccesseur.getCost() != Double.POSITIVE_INFINITY) {
                            try { tas.remove(labelSuccesseur); } catch (Exception e) {}
                        }
                        labelSuccesseur.setNewPath(arc, coutTemporaire); // On met à jour le père et le coût
                        tas.insert(labelSuccesseur); // On insère le label mis à jour dans le tas
                        notifyNodeReached(sommetSuccesseur); // On signale qu'on a atteint ce sommet
                    }
                }
            }
        }

        // On reconstruit le chemin à partir de la destination
        ArrayList<Arc> arcsChemin = new ArrayList<>();
        Node sommetActuel = donnees.getDestination();
        Label labelDestination = listeLabels.get(sommetActuel.getId());

        // Si le coût est infini, il n'y a pas de solution
        if (labelDestination.getCost() == Double.POSITIVE_INFINITY) {
            return new ShortestPathSolution(donnees, AbstractSolution.Status.INFEASIBLE);
        }

        // On remonte les arcs depuis la destination jusqu'à l'origine
        while (sommetActuel != donnees.getOrigin()) {
            Arc arc = listeLabels.get(sommetActuel.getId()).getPere();
            if (arc == null) break;
            arcsChemin.add(arc);
            sommetActuel = arc.getOrigin();
        }
        Collections.reverse(arcsChemin); // On remet le chemin dans le bon ordre

        notifyDestinationReached(donnees.getDestination());
        return new ShortestPathSolution(donnees, AbstractSolution.Status.FEASIBLE, new Path(donnees.getGraph(), arcsChemin));
    }
}