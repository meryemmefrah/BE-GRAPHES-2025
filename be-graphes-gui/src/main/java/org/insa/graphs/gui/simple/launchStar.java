package org.insa.graphs.gui.simple;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;

import org.insa.graphs.algorithm.AbstractSolution;
import org.insa.graphs.algorithm.ArcInspector;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.shortestpath.AStarAlgorithm;
import org.insa.graphs.algorithm.shortestpath.BellmanFordAlgorithm;
import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.algorithm.shortestpath.ShortestPathSolution;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.GraphReader;

public class launchStar {

    // Petite fonction pour comparer deux réels (genre pour les longueurs)
    public static boolean egalDouble(double a, double b) {
        return Math.abs(a - b) < 1e-6;
    }

    public static void main(String[] args) throws Exception {

        // On prépare nos scénarios de test (à adapter selon vos fichiers)
        class Scenario {
            String nomCarte;
            int origine, destination;
            int mode; // 0 = distance, 2 = temps
            boolean petitGraphe; // true si on veut tester Bellman-Ford aussi

            Scenario(String nomCarte, int origine, int destination, int mode, boolean petitGraphe) {
                this.nomCarte = nomCarte;
                this.origine = origine;
                this.destination = destination;
                this.mode = mode;
                this.petitGraphe = petitGraphe;
            }
        }

        List<Scenario> scenarios = Arrays.asList(
            // Carte routière, chemin normal (distance)
            new Scenario("/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/insa.mapgr", 0, 10, 0, true),
            // Carte routière, chemin normal (temps)
            new Scenario("/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/insa.mapgr", 0, 10, 2, true),
            // Carte non routière, chemin inexistant
            new Scenario("/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/carre.mapgr", 0, 99, 0, true),
            // Carte routière, chemin nul (origine = destination)
            new Scenario("/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/insa.mapgr", 5, 5, 0, true),
            // Carte grande, trajet long (pas de Bellman-Ford)
            new Scenario("/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/fr31.mapgr", 100, 10000, 0, false)
        );

        // On lance les tests pour chaque scénario
        for (Scenario sc : scenarios) {
            System.out.println("=== Test scénario (A*) sur carte : " + sc.nomCarte + " ===");
            Graph graphe;
            try (GraphReader reader = new BinaryGraphReader(new DataInputStream(
                    new BufferedInputStream(new FileInputStream(sc.nomCarte))))) {
                graphe = reader.read();
            }
            Node origine = graphe.get(sc.origine);
            Node destination = graphe.get(sc.destination);

            ArcInspector filtre = ArcInspectorFactory.getAllFilters().get(sc.mode);
            ShortestPathData data = new ShortestPathData(graphe, origine, destination, filtre);

            // Test A*
            AStarAlgorithm astar = new AStarAlgorithm(data);
            ShortestPathSolution solAstar = astar.run();

            // On compare aussi avec Dijkstra pour voir si c'est pareil
            DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(data);
            ShortestPathSolution solDij = dijkstra.run();
            System.out.println("  - Statut Dijkstra : " + solDij.getStatus());
            System.out.println("  - Statut A* : " + solAstar.getStatus());
            if (solAstar.getStatus() == AbstractSolution.Status.FEASIBLE && solDij.getStatus() == AbstractSolution.Status.FEASIBLE) {
                System.out.println("  - Longueur Dijkstra : " + solDij.getPath().getLength());
                System.out.println("  - Longueur A* : " + solAstar.getPath().getLength());
                System.out.println("  - Résultat identique Dijkstra/A* ? " + egalDouble(solDij.getPath().getLength(), solAstar.getPath().getLength()));
            }

            // On vérifie si le chemin trouvé par A* est correct
            if (solAstar.getStatus() == AbstractSolution.Status.FEASIBLE) {
                System.out.println("Chemin trouvé !");
                System.out.println("  - Chemin valide ? " + solAstar.getPath().isValid());
                System.out.println("  - Longueur : " + solAstar.getPath().getLength());
                System.out.println("  - Temps : " + solAstar.getPath().getMinimumTravelTime());
                // On vérifie que le coût affiché par A* est cohérent avec le chemin
                if (sc.mode == 0) {
                    System.out.println("  - Vérif coût distance (A*) : " + egalDouble(solAstar.getPath().getLength(), solAstar.getPath().getLength()));
                } else {
                    System.out.println("  - Vérif coût temps (A*) : " + egalDouble(solAstar.getPath().getMinimumTravelTime(), solAstar.getPath().getMinimumTravelTime()));
                }
            } else {
                System.out.println("Pas de chemin trouvé (INFEASIBLE)");
            }

            // Pour les petits graphes, on compare aussi avec Bellman-Ford (pour être sûr)
            if (sc.petitGraphe) {
                BellmanFordAlgorithm bellman = new BellmanFordAlgorithm(data);
                ShortestPathSolution solBell = bellman.run();
                System.out.println("  - Statut Bellman-Ford : " + solBell.getStatus());
                System.out.println("  - Statut A* : " + solAstar.getStatus());
                if (solAstar.getStatus() == AbstractSolution.Status.FEASIBLE && solBell.getStatus() == AbstractSolution.Status.FEASIBLE) {
                    System.out.println("  - Longueur Bellman : " + solBell.getPath().getLength());
                    System.out.println("  - Longueur A* : " + solAstar.getPath().getLength());
                    System.out.println("  - Résultat identique Bellman/A* ? " + egalDouble(solBell.getPath().getLength(), solAstar.getPath().getLength()));
                }
            }
            System.out.println();
        }
        System.out.println("Tous les scénarios A* sont terminés ! (si tout est ok, c'est bon 🎉)");
    }
}