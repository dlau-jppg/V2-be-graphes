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
	// On se sert de BellmanFord comme référence étant donné qu'il était déjà codé

	// Some paths...
	private static Path shortPathD, shortPathB, shortPathA, shortPathD4, shortPathB4, shortPathA4;
	private static AbstractSolution.Status emptyPathD, emptyPathA;
	private static AbstractSolution.Status nonexistentPathD, nonexistentPathA;

	@BeforeClass
	public static void initAll() throws IOException {
		// Test sur la carte carré
		// Récupération des données de la carte carré
		FileInputStream carte1 = new FileInputStream("C:/Users/TAM/Documents/V2-be-graphes/carre.mapgr");
		// Contient des chemins inexistants
		FileInputStream carte2 = new FileInputStream("C:/Users/TAM/Documents/V2-be-graphes/greece.mapgr");

		DataInputStream dataInput1 = new DataInputStream(carte1);
		BinaryGraphReader binary1 = new BinaryGraphReader(dataInput1);
		Graph graph1 = binary1.read();
		binary1.close();

		DataInputStream dataInput2 = new DataInputStream(carte2);
		BinaryGraphReader binary2 = new BinaryGraphReader(dataInput2);
		Graph graph2 = binary2.read();
		binary2.close();

		// Création des datas
		List<ArcInspector> Listeinspector = ArcInspectorFactory.getAllFilters();
		ShortestPathData data_1 = new ShortestPathData(graph1, graph1.getNodes().get(5), graph1.getNodes().get(19),
				Listeinspector.get(0));
		ShortestPathData data_2 = new ShortestPathData(graph1, graph1.getNodes().get(5), graph1.getNodes().get(5),
				Listeinspector.get(0));
		ShortestPathData data_3 = new ShortestPathData(graph2, graph2.get(404073), graph2.get(767341),
				Listeinspector.get(0));
		ShortestPathData data_4 = new ShortestPathData(graph2, graph2.getNodes().get(691), graph2.getNodes().get(3465),
				Listeinspector.get(0));

		// Réalisation des algorithmes sur data
		DijkstraAlgorithm D1 = new DijkstraAlgorithm(data_1);
		shortPathD = D1.run().getPath();
		BellmanFordAlgorithm B1 = new BellmanFordAlgorithm(data_1);
		shortPathB = B1.run().getPath();
		AStarAlgorithm A1 = new AStarAlgorithm(data_1);
		shortPathA = A1.run().getPath();

		// Réalisation des algorithmes sur data2, chemin null
		DijkstraAlgorithm D2 = new DijkstraAlgorithm(data_2);
		emptyPathD = D2.run().getStatus();
		AStarAlgorithm A2 = new AStarAlgorithm(data_2);
		emptyPathA = A2.run().getStatus();

		// Réalisation des algorithles sur data3, chemin inexistant
		DijkstraAlgorithm D3 = new DijkstraAlgorithm(data_3);
		nonexistentPathD = D3.run().getStatus();
		AStarAlgorithm A3 = new AStarAlgorithm(data_3);
		nonexistentPathA = A3.run().getStatus();

		// Réalisation des algorithmes sur data4, chemin existant
		DijkstraAlgorithm D4 = new DijkstraAlgorithm(data_4);
		shortPathD4 = D4.run().getPath();
		BellmanFordAlgorithm B4 = new BellmanFordAlgorithm(data_4);
		shortPathB4 = B4.run().getPath();
		AStarAlgorithm A4 = new AStarAlgorithm(data_4);
		shortPathA4 = A4.run().getPath();

	}

	// Chemin court existant -> vérification de la longueure avec Bellman ford en référence
	// référence, test sur les deux cartes
	@Test
	public void Test1() {
		assertEquals((long) (shortPathA.getLength()), (long) (shortPathB.getLength()));
		assertEquals((long) (shortPathB.getLength()), (long) (shortPathD.getLength()));
		assertEquals((long) (shortPathA4.getLength()), (long) (shortPathB4.getLength()));
		assertEquals((long) (shortPathB4.getLength()), (long) (shortPathD4.getLength()));

	}

	// Chemin court existant -> vérification du temps avec Bellman ford en référence
	@Test
	public void Test2() {
		assertEquals((long) (shortPathA.getMinimumTravelTime()), (long) (shortPathB.getMinimumTravelTime()));
		assertEquals((long) (shortPathB.getMinimumTravelTime()), (long) (shortPathD.getMinimumTravelTime()));
		assertEquals((long) (shortPathA4.getMinimumTravelTime()), (long) (shortPathB4.getMinimumTravelTime()));
		assertEquals((long) (shortPathB4.getMinimumTravelTime()), (long) (shortPathD4.getMinimumTravelTime()));

	}

	// Chemin de longueur nulle, on vérifie le statue
	@Test
	public void Test3() {
		assertTrue(emptyPathA.equals(AbstractSolution.Status.INFEASIBLE));
		assertTrue(emptyPathD.equals(AbstractSolution.Status.INFEASIBLE));
	}

	// Chemin inexistant, vérification du status
	@Test
	public void Test4() {
		assertTrue(nonexistentPathD.equals(AbstractSolution.Status.INFEASIBLE));
		assertTrue(nonexistentPathA.equals(AbstractSolution.Status.INFEASIBLE));
	}

}