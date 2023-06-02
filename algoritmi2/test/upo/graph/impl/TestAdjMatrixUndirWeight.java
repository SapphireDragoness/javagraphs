package upo.graph.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import upo.graph.base.VisitForest;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.HashSet;
import java.lang.UnsupportedOperationException;

class TestAdjMatrixUndirWeight {

	AdjMatrixUndirWeight graph = new AdjMatrixUndirWeight();
	AdjMatrixUndirWeight graph2 = new AdjMatrixUndirWeight();

	@BeforeEach
	void before() {
		graph.addVertex("A");
		graph.addVertex("B");
		graph.addVertex("C");
		graph.addVertex("D");
		graph.addEdge("A", "B");
		graph.addEdge("B", "C");
		graph.addEdge("C", "D");
		graph.addEdge("D", "A");

		graph2.addVertex("A");
		graph2.addVertex("B");
		graph2.addVertex("C");
		graph2.addVertex("D");
	}

	// Test dei metodi per i vertici
	@Test
	void testAddVertex() {
		assertEquals(graph.addVertex("G"), 4);
		assertEquals(graph.addVertex("H"), 5);
		assertEquals(graph.addVertex("I"), 6);
	}

	@Test
	void testGetVertexIndex() {
		assertEquals(graph.getVertexIndex("A"), 0);
		assertEquals(graph.getVertexIndex("B"), 1);
		assertEquals(graph.getVertexIndex("C"), 2);
	}

	@Test
	void testGetVertexLabel() {
		assertEquals(graph.getVertexLabel(0), "A");
		assertEquals(graph.getVertexLabel(1), "B");
		assertEquals(graph.getVertexLabel(2), "C");
	}

	@Test
	void testContainsVertex() {
		assertTrue(graph.containsVertex("A"));
		assertFalse(graph.containsVertex("Z"));
	}

	@Test
	void testRemoveVertex() {
		assertThrows(NoSuchElementException.class, () -> {
			graph.removeVertex("Z");
		});
		graph.addVertex("E");
		assertTrue(graph.size() == 5);
		graph.removeVertex("E");
		assertTrue(graph.size() == 4);
	}

	// Test dei metodi per gli archi
	@Test
	void testAddEdge() {
		graph.addEdge("B", "D");
		assertTrue(graph.containsEdge("B", "D"));
		assertThrows(IllegalArgumentException.class, () -> {
			graph.addEdge("E", "F");
		});
		assertThrows(IllegalArgumentException.class, () -> {
			graph.addEdge("A", "F");
		});
	}

	@Test
	void testContainsEdge() {
		assertTrue(graph.containsEdge("A", "B"));
		assertThrows(IllegalArgumentException.class, () -> {
			graph.containsEdge("E", "F");
		});
		assertThrows(IllegalArgumentException.class, () -> {
			graph.containsEdge("A", "F");
		});
	}

	@Test
	void testRemoveEdge() {
		graph.addEdge("A", "B");
		graph.removeEdge("A", "B");
		assertFalse(graph.containsEdge("A", "B"));
		assertThrows(NoSuchElementException.class, () -> {
			graph.removeEdge("A", "B");
		});
		assertThrows(IllegalArgumentException.class, () -> {
			graph.removeEdge("E", "F");
		});
	}

	@Test
	void testGetAdjacent() {
		Set<String> adjacent = new HashSet<>();
		adjacent.add("B");
		adjacent.add("D");
		assertEquals(graph.getAdjacent("A"), adjacent);
		assertThrows(NoSuchElementException.class, () -> {
			graph.getAdjacent("E");
		});
	}

	@Test
	void testGetEdgeWeight() {
		graph.addEdge("A", "B");
		assertTrue(graph.getEdgeWeight("A", "B") == 1.0);
	}

	@Test
	void testSetEdgeWeight() {
		graph.addEdge("A", "B");
		graph.setEdgeWeight("A", "B", 3.0);
		assertTrue(graph.getEdgeWeight("A", "B") == 3.0);
	}

	// Test algoritmi
	@Test
	void testIsCyclic() {
		assertTrue(graph.isCyclic());
		assertFalse(graph2.isCyclic());
	}

	@Test
	void testGetBFSTree() {
		VisitForest visit = graph.getBFSTree("A");
		assertEquals(visit.getPartent("B"), "A");
		assertEquals(visit.getPartent("C"), "B");
		assertEquals(visit.getPartent("D"), "A");
	}
	
	@Test
	void testGetDFSTOTForest() {
		VisitForest visit1 = graph.getDFSTOTForest("A");
		int[] times1 = { 7, 6, 5, 4 };
		for (int i = 0; i < graph.size(); i++)
			assertEquals(visit1.getEndTime(graph.getVertexLabel(i)), times1[i]);
		
		VisitForest visit2 = graph2.getDFSTOTForest("A");
		int[] times2 = { 1, 3, 5, 7 };
		for (int i = 0; i < graph2.size(); i++)
			assertEquals(visit2.getEndTime(graph.getVertexLabel(i)), times2[i]);
	}

	@Test
	void testConnectedComponents() {
		Set<Set<String>> ris1 = graph.connectedComponents();
		assertEquals(ris1.size(), 1);
		Set<Set<String>> ris2 = graph2.connectedComponents();
		assertEquals(ris2.size(), 4);
		ris1.forEach(a -> assertEquals(4, a.size()));
		ris2.forEach(a -> assertEquals(1, a.size()));
	}

	@Test
	void testGetDijkstraShortestPaths() {
		assertThrows(UnsupportedOperationException.class, () -> {
			graph.getDijkstraShortestPaths("A");
		});
	}

	@Test
	void testGetPrimMST() {
		assertThrows(UnsupportedOperationException.class, () -> {
			graph.getPrimMST("A");
		});
	}

	@Test
	void testKruskalMST() {
		assertThrows(UnsupportedOperationException.class, () -> {
			graph.getKruskalMST();
		});
	}

	@Test
	void testGetBellmanFordShortestPaths() {
		assertThrows(UnsupportedOperationException.class, () -> {
			graph.getBellmanFordShortestPaths("A");
		});
	}

	@Test
	void testGetFloydWarshallShortestPaths() {
		assertThrows(UnsupportedOperationException.class, () -> {
			graph.getFloydWarshallShortestPaths();
		});
	}

}
