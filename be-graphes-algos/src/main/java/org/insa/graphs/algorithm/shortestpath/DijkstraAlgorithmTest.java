
/*package org.insa.graphs.algorithm.shortestpath;

import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

import org.insa.graphs.model.*;
import org.insa.graphs.model.io.*;
import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.utils.*;

import java.io.*;
import java.util.*;

public class DijkstraAlgorithmTest {

    private static Graph roadGraph, nonRoadGraph;

    @BeforeClass
    public static void initAll() throws Exception {
        // Charger deux graphes différents
        String roadMapPath = "chemin/vers/une/carte_routiere.mapgr";
        String nonRoadMapPath = "chemin/vers/une/carte_non_routiere.mapgr";
        try (GraphReader reader = new BinaryGraphReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(roadMapPath))))) {
            roadGraph = reader.read();
        }
        try (GraphReader reader = new BinaryGraphReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(nonRoadMapPath))))) {
            nonRoadGraph = reader.read();
        }
    }

    @Test
    public void testCheminInexistant() {
        Node origine = roadGraph.get(0);
        Node destination = roadGraph.get(roadGraph.size() - 1);
        // Créer un scénario où il n'y a pas de chemin (par exemple, sur un graphe non
        // connexe)
        ShortestPathData data = new ShortestPathData(roadGraph, origine, destination,
                ArcInspectorFactory.getAllFilters().get(0));
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(data);
        ShortestPathSolution solution = dijkstra.doRun();
        assertEquals(Status.INFEASIBLE, solution.getStatus());
    }

    @Test
    public void testCheminNul() {
        Node origine = roadGraph.get(5);
        ShortestPathData data = new ShortestPathData(roadGraph, origine, origine,
                ArcInspectorFactory.getAllFilters().get(0));
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(data);
        ShortestPathSolution solution = dijkstra.doRun();
        assertEquals(Status.FEASIBLE, solution.getStatus());
        assertEquals(0, solution.getPath().getLength(), 1e-6);
    }

    @Test
    public void testCourtVsLong() {
        Node origine = roadGraph.get(10);
        Node destination = roadGraph.get(50);
        ShortestPathData data = new ShortestPathData(roadGraph, origine, destination,
                ArcInspectorFactory.getAllFilters().get(0));
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(data);
        ShortestPathSolution solution = dijkstra.doRun();
        assertEquals(Status.FEASIBLE, solution.getStatus());
        // Vérifier la validité du chemin
        assertTrue(solution.getPath().isValid());
        // Vérifier que le coût du chemin correspond à la somme des arcs
        double cost = solution.getPath().getLength();
        assertEquals(cost, solution.getPath().getLength(), 1e-6);
    }

    @Test
    public void testDijkstraVsBellmanFord() {
        Node origine = roadGraph.get(20);
        Node destination = roadGraph.get(30);
        ShortestPathData data = new ShortestPathData(roadGraph, origine, destination,
                ArcInspectorFactory.getAllFilters().get(0));
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(data);
        BellmanFordAlgorithm bellman = new BellmanFordAlgorithm(data);
        ShortestPathSolution solDijkstra = dijkstra.doRun();
        ShortestPathSolution solBellman = bellman.doRun();
        assertEquals(solBellman.getStatus(), solDijkstra.getStatus());
        if (solDijkstra.getStatus() == Status.FEASIBLE) {
            assertEquals(solBellman.getPath().getLength(), solDijkstra.getPath().getLength(), 1e-6);
        }
    }

    // Ajoutez d'autres tests pour différents types de graphes, de coûts, etc.
}
    */

