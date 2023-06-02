package upo.graph.impl;

import java.util.ArrayList;
import java.util.Arrays;

import upo.graph.base.WeightedGraph;

public class AdjMatrixUndir extends AdjMatrixUndirWeight{

	private ArrayList<String> vertices = new ArrayList<>(); //contiene i vertici
	private double[][] adjMatrix; //matrice di adiacenza

	public AdjMatrixUndir() {
		adjMatrix = new double [size()][size()];
		Arrays.fill(adjMatrix, 0);
	}

	@Override
	public double getEdgeWeight(String sourceVertex, String targetVertex)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Il grafo non è pesato, impossibile completare operazione.");
	}

	@Override
	public void setEdgeWeight(String sourceVertex, String targetVertex, double weight)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Il grafo non è pesato, impossibile completare operazione.");
	}

	@Override
	public WeightedGraph getBellmanFordShortestPaths(String startingVertex)
			throws UnsupportedOperationException, IllegalArgumentException {
		throw new UnsupportedOperationException("Il grafo non è pesato, impossibile completare operazione.");
	}

	@Override
	public WeightedGraph getDijkstraShortestPaths(String startingVertex)
			throws UnsupportedOperationException, IllegalArgumentException {
		throw new UnsupportedOperationException("Il grafo non è pesato, impossibile completare operazione.");
	}

	@Override
	public WeightedGraph getPrimMST(String startingVertex)
			throws UnsupportedOperationException, IllegalArgumentException {
		throw new UnsupportedOperationException("Il grafo non è pesato, impossibile completare operazione.");
	}

	@Override
	public WeightedGraph getKruskalMST() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Il grafo non è pesato, impossibile completare operazione.");
	}

	@Override
	public WeightedGraph getFloydWarshallShortestPaths() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Il grafo non è pesato, impossibile completare operazione.");
	}
}
