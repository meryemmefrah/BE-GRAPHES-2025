package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;

// Classe pour les labels utilisés dans A* (hérite de Label)
public class LabelStar extends Label {

    // L'heuristique estimée pour atteindre la destination (genre la distance à vol d'oiseau)
    final double coutEstime;

    // Constructeur du label A*
    public LabelStar(Node sommet, boolean marque, double coutRealise, Arc pere, double coutEstime) {
        super(sommet, marque, coutRealise, pere);
        this.coutEstime = coutEstime;
    }

    // Le coût total pour A* (coût déjà parcouru + estimation jusqu'à la fin)
    @Override
    public double getTotalCost() {
        return this.coutRealise + this.coutEstime;
    }

    // Pour récupérer l'heuristique (ça peut servir)
    public double getCoutEstime() {
        return this.coutEstime;
    }

    // Pour comparer deux labels dans le tas (A* regarde le coût total)
    @Override
    public int compareTo(Label autre) {
        double monTotal = this.getCost() + this.coutEstime;
        double autreTotal;
        if (autre instanceof LabelStar) {
            autreTotal = autre.getCost() + ((LabelStar) autre).coutEstime;
        } else {
            autreTotal = autre.getCost();
        }
        return Double.compare(monTotal, autreTotal);
    }
}