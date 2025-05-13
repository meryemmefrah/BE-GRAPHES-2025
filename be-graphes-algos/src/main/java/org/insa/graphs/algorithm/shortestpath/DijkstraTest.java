//A CORRIGER 




package org.insa.graphs.algorithm.shortestpath;

import java.io.*;
import java.util.*;

import org.insa.graphs.model.*;
import org.insa.graphs.model.io.*;
import org.insa.graphs.algorithm.*;

public class DijkstraTest  {

    public static void main(String[] args) throws Exception {
        // Liste de scénarios de test
        List<TestScenario> scenarios = Arrays.asList(
            new TestScenario(
                "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/insa.mapgr",
                8, 22,
                AbstractInputData.Mode.LENGTH,
                "INSA - distance"
            ),
            new TestScenario(
                "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/toulouse.mapgr",
                24345, 67854,
                AbstractInputData.Mode.TIME,
                "Toulouse - temps"
            )
        );

        for (TestScenario scenario : scenarios) {
            runScenario(scenario);
            System.out.println("--------------------------------------------------");
        }
    }

    public static void runScenario(TestScenario sc) throws Exception {
        System.out.println("🧪 Scénario : " + sc.description);

        // Chargement du graphe
        Graph graph;
        try (GraphReader reader = new BinaryGraphReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(sc.mapPath))))) {
            graph = reader.read();
        }

        Node origin = graph.get(sc.originId);
        Node dest = graph.get(sc.destinationId);
        ShortestPathData data = new ShortestPathData(graph, origin, dest, sc.mode);

        // Exécution Dijkstra
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(data);
        ShortestPathSolution solDijkstra = dijkstra.run();

        if (solDijkstra.getStatus() == AbstractSolution.Status.INFEASIBLE) {
            System.out.println("Aucun chemin trouvé.");
            return;
        }

        Path path = solDijkstra.getPath();
        if (!path.isValid()) {
            System.out.println("Chemin invalide.");
            return;
        }

        System.out.println("✅ Chemin valide.");

        double cost = (sc.mode == AbstractInputData.Mode.LENGTH)
                        ? path.getLength()
                        : path.getMinimumTravelTime();

        System.out.printf("➡️  Coût (selon le mode) : %.2f\n", cost);

        // Vérification avec Bellman-Ford sur les petits graphes
        if (graph.size() < 1000) {
            BellmanFordAlgorithm bellman = new BellmanFordAlgorithm(data);
            ShortestPathSolution solBF = bellman.run();

            if (solBF.getStatus() == AbstractSolution.Status.FEASIBLE) {
                double costBF = (sc.mode == AbstractInputData.Mode.LENGTH)
                                    ? solBF.getPath().getLength()
                                    : solBF.getPath().getMinimumTravelTime();

                if (Math.abs(cost - costBF) < 1e-4) {
                    System.out.println("✅ Résultat identique à Bellman-Ford.");
                } else {
                    System.out.printf("⚠️  Différence avec Bellman-Ford : %.4f vs %.4f\n", cost, costBF);
                }
            } else {
                System.out.println("⚠️  Bellman-Ford n’a pas trouvé de solution.");
            }
        }
    }

    // Classe pour décrire un scénario de test
    static class TestScenario {
        String mapPath;
        int originId;
        int destinationId;
        ArcInspector mode;
        String description;

        public TestScenario(String mapPath, int originId, int destinationId, ArcInspector mode, String description) {
            this.mapPath = mapPath;
            this.originId = originId;
            this.destinationId = destinationId;
            this.mode = mode;
            this.description = description;
        }
    }
}
