package org.insa.graphs.algorithm.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Path;

import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm;
import org.insa.graphs.algorithm.shortestpath.BellmanFordAlgorithm;
import org.insa.graphs.algorithm.shortestpath.AStarAlgorithm;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.AbstractSolution;
import org.insa.graphs.algorithm.ArcInspector;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;

import org.insa.graphs.model.io.BinaryGraphReader;
import java.io.DataInputStream;
import java.io.FileInputStream;

import org.junit.BeforeClass;
import org.junit.Test;

public class ShortestPathTest {
	// On prendra comme référence Bellman Ford car on sait qu'il est valide

	// Some paths...
	private static Path PathD, PathB, PathA, PathD1, PathB1, PathA1;
	private static AbstractSolution.Status emptyD, emptyA;
	private static AbstractSolution.Status nonexistentD, nonexistentA;

	@BeforeClass
	public static void initAll() throws IOException {
		// Test sur la carte carré
		FileInputStream carte1 = new FileInputStream("C:/Users/TAM/Documents/V2-be-graphes/carre.mapgr");
		// Test sur la carte grèce
		FileInputStream carte2 = new FileInputStream("C:/Users/TAM/Documents/V2-be-graphes/greece.mapgr");

		DataInputStream dataInput1 = new DataInputStream(carte1);
		BinaryGraphReader binary1 = new BinaryGraphReader(dataInput1);
		Graph graph1 = binary1.read();
		binary1.close();

		DataInputStream dataInput2 = new DataInputStream(carte2);
		BinaryGraphReader binary2 = new BinaryGraphReader(dataInput2);
		Graph graph2 = binary2.read();
		binary2.close();

		// Créations des données
		List<ArcInspector> Listeinspector = ArcInspectorFactory.getAllFilters();
		ShortestPathData data_1 = new ShortestPathData(graph1, graph1.getNodes().get(5), graph1.getNodes().get(19),
				Listeinspector.get(0));
		ShortestPathData data_2 = new ShortestPathData(graph1, graph1.getNodes().get(5), graph1.getNodes().get(5),
				Listeinspector.get(0));
		ShortestPathData data_3 = new ShortestPathData(graph2, graph2.get(404073), graph2.get(767341),
				Listeinspector.get(0));
		ShortestPathData data_4 = new ShortestPathData(graph2, graph2.getNodes().get(691), graph2.getNodes().get(3465),
				Listeinspector.get(0));

		// Création des donnés pour les algorithmes, utilisées pour les différents cas
		DijkstraAlgorithm D1 = new DijkstraAlgorithm(data_1);
		PathD = D1.run().getPath();
		BellmanFordAlgorithm B1 = new BellmanFordAlgorithm(data_1);
		PathB = B1.run().getPath();
		AStarAlgorithm A1 = new AStarAlgorithm(data_1);
		PathA = A1.run().getPath();

		
		DijkstraAlgorithm D2 = new DijkstraAlgorithm(data_2);
		emptyD = D2.run().getStatus();
		AStarAlgorithm A2 = new AStarAlgorithm(data_2);
		emptyA = A2.run().getStatus();

	
		DijkstraAlgorithm D3 = new DijkstraAlgorithm(data_3);
		nonexistentD = D3.run().getStatus();
		AStarAlgorithm A3 = new AStarAlgorithm(data_3);
		nonexistentA = A3.run().getStatus();

		
		DijkstraAlgorithm D4 = new DijkstraAlgorithm(data_4);
		PathD1 = D4.run().getPath();
		BellmanFordAlgorithm B4 = new BellmanFordAlgorithm(data_4);
		PathB1 = B4.run().getPath();
		AStarAlgorithm A4 = new AStarAlgorithm(data_4);
		PathA1 = A4.run().getPath();

	}

	// Test du plus court chemin, et compare les longueurs avec la longueur du chemin obtenu avec Bellman Ford 
	@Test
	public void Test1() {
		assertEquals((long) (PathA.getLength()), (long) (PathB.getLength()));
		assertEquals((long) (PathB.getLength()), (long) (PathD.getLength()));
		assertEquals((long) (PathA1.getLength()), (long) (PathB1.getLength()));
		assertEquals((long) (PathB1.getLength()), (long) (PathD1.getLength()));

	}

	// Test du plus court chemin, et compare les temps de voyage avec le temps de voyage avec Bellman Ford comme référence 
	@Test
	public void Test2() {
		assertEquals((long) (PathA.getMinimumTravelTime()), (long) (PathB.getMinimumTravelTime()));
		assertEquals((long) (PathB.getMinimumTravelTime()), (long) (PathD.getMinimumTravelTime()));
		assertEquals((long) (PathA1.getMinimumTravelTime()), (long) (PathB1.getMinimumTravelTime()));
		assertEquals((long) (PathB1.getMinimumTravelTime()), (long) (PathD1.getMinimumTravelTime()));

	}

	// Test d'un chemin de longueur nulle, on vérifie s'il est faisable
	@Test
	public void Test3() {
		assertTrue(emptyA.equals(AbstractSolution.Status.INFEASIBLE));
		assertTrue(emptyD.equals(AbstractSolution.Status.INFEASIBLE));
	}

	// Test d'un chemin inexistant, on vérifie s'il est faisable
	@Test
	public void Test4() {
		assertTrue(nonexistentD.equals(AbstractSolution.Status.INFEASIBLE));
		assertTrue(nonexistentA.equals(AbstractSolution.Status.INFEASIBLE));
	}

}