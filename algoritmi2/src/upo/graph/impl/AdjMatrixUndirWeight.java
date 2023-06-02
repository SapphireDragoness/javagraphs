package upo.graph.impl;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import upo.graph.base.VisitForest;
import upo.graph.base.VisitForest.Color;
import upo.graph.base.VisitForest.VisitType;
import upo.graph.base.WeightedGraph;

public class AdjMatrixUndirWeight implements WeightedGraph {

	private ArrayList<String> vertices = new ArrayList<>();; // contiene i vertici
	private double[][] adjMatrix; // matrice di adiacenza
	private int time = 0;

	public AdjMatrixUndirWeight() {
		adjMatrix = new double[size()][size()];
		Arrays.fill(adjMatrix, 0);
	}

	@Override
	public int getVertexIndex(String label) {
		return vertices.indexOf(label);
	}

	@Override
	public String getVertexLabel(Integer index) {
		return vertices.get(index);
	}

	@Override
	public int addVertex(String label) {
		vertices.add(label);
		double[][] cloneMatrix = adjMatrix.clone();
		adjMatrix = new double[size()][size()];
		for (int i = 0; i < size() - 1; i++) {
			for (int j = 0; j < size() - 1; j++)
				adjMatrix[i][j] = cloneMatrix[i][j];
		}
		return size() - 1;
	}

	@Override
	public boolean containsVertex(String label) {
		return vertices.contains(label);
	}

	@Override
	public void removeVertex(String label) throws NoSuchElementException {
		if (!containsVertex(label))
			throw new NoSuchElementException("Vertice inesistente.");
		int index = getVertexIndex(label);
		vertices.remove(label);
		double[][] tempMatrix = new double[adjMatrix.length - 1][adjMatrix.length - 1];
		for (int i = 0; i < adjMatrix.length; i++) {
			for (int j = 0; j < adjMatrix.length; j++) {
				if (i != index && j != index) {
					if ((i > index) && (j > index))
						tempMatrix[i - 1][j - 1] = adjMatrix[i][j];
					else if (i > index)
						tempMatrix[i - 1][j] = adjMatrix[i][j];
					else if (j > index)
						tempMatrix[i][j - 1] = adjMatrix[i][j];
					else
						tempMatrix[i][j] = adjMatrix[i][j];
				}
			}
		}
		adjMatrix = tempMatrix;
	}

	@Override
	public void addEdge(String sourceVertex, String targetVertex) throws IllegalArgumentException {
		if (!containsVertex(targetVertex) || !containsVertex(sourceVertex))
			throw new IllegalArgumentException("Vertice inesistente.");
		if (!containsEdge(sourceVertex, targetVertex)) {
			adjMatrix[getVertexIndex(sourceVertex)][getVertexIndex(targetVertex)] = defaultEdgeWeight;
			adjMatrix[getVertexIndex(targetVertex)][getVertexIndex(sourceVertex)] = defaultEdgeWeight;
		}
	}

	@Override
	public boolean containsEdge(String sourceVertex, String targetVertex) throws IllegalArgumentException {
		if (!containsVertex(targetVertex) || !containsVertex(sourceVertex))
			throw new IllegalArgumentException("Vertice inesistente.");
		return adjMatrix[getVertexIndex(sourceVertex)][getVertexIndex(targetVertex)] > 0 && adjMatrix[getVertexIndex(targetVertex)][getVertexIndex(sourceVertex)] > 0;
	}

	@Override
	public void removeEdge(String sourceVertex, String targetVertex)
			throws IllegalArgumentException, NoSuchElementException {
		if (!containsVertex(targetVertex) || !containsVertex(sourceVertex))
			throw new IllegalArgumentException("Vertice inesistente.");
		if (!containsEdge(sourceVertex, targetVertex))
			throw new NoSuchElementException();
		setEdgeWeight(sourceVertex, targetVertex, 0);
	}

	@Override
	public Set<String> getAdjacent(String vertex) throws NoSuchElementException {
		if (!containsVertex(vertex))
			throw new NoSuchElementException();
		Set<String> ris = new HashSet<>();
		for (String v : vertices) {
			if (containsEdge(vertex, v))
				ris.add(v);
		}
		return ris;
	}

	@Override
	public boolean isAdjacent(String targetVertex, String sourceVertex) throws IllegalArgumentException {
		if (!containsVertex(targetVertex) || !containsVertex(sourceVertex))
			throw new IllegalArgumentException("Vertice inesistente.");
		return containsEdge(targetVertex, sourceVertex) && containsEdge(targetVertex, sourceVertex);
	}

	@Override
	public int size() {
		return vertices.size();
	}

	@Override
	public boolean isDirected() {
		return false;
	}

	@Override
	public boolean isCyclic() {
		VisitForest visit = new VisitForest(this, VisitType.DFS_TOT);
		for (String v : vertices) {
			if ((visit.getColor(v) == Color.WHITE) && visitaRicCiclo(visit, v))
				return true;
		}
		return false;
	}

	private boolean visitaRicCiclo(VisitForest visit, String label) {
		visit.setColor(label, Color.GRAY);
		for (String v : getAdjacent(label)) {
			if (visit.getColor(v) == Color.WHITE) {
				visit.setParent(v, label);
				if (visitaRicCiclo(visit, v))
					return true;
			} else if (!v.equals(visit.getPartent(label)));
				return true;
		}
		visit.setColor(label, Color.BLACK);
		return false;
	}

	@Override
	public boolean isDAG() {
		return false;
	}

	private VisitForest visitaGenerica(String startingVertex, VisitForest visit, ArrayDeque<String> frangia) {
		visit.setColor(startingVertex, Color.GRAY);
		frangia.add(startingVertex);
		while (!frangia.isEmpty()) {
			for (String v : getAdjacent(frangia.peek())) {
				if (visit.getColor(v) == Color.WHITE) {
					visit.setColor(v, Color.GRAY);
					visit.setParent(v, frangia.peek());
					frangia.add(v);
				}
			}
			visit.setColor(frangia.peek(), Color.BLACK);
			frangia.poll();
		}
		return visit;
	}

	@Override
	public VisitForest getBFSTree(String startingVertex)
			throws UnsupportedOperationException, IllegalArgumentException {
		VisitForest visit = new VisitForest(this, VisitType.BFS);
		ArrayDeque<String> queue = new ArrayDeque<>();
		return visitaGenerica(startingVertex, visit, queue);
	}

	@Override
	public VisitForest getDFSTree(String startingVertex)
			throws UnsupportedOperationException, IllegalArgumentException {
		if (!containsVertex(startingVertex))
			throw new IllegalArgumentException("Vertice inesistente.");
		VisitForest visit = new VisitForest(this, VisitType.DFS);
		ArrayDeque<String> stack = new Stack<>();
		return visitaGenerica(startingVertex, visit, stack);
	}

	@Override
	public VisitForest getDFSTOTForest(String startingVertex)
			throws UnsupportedOperationException, IllegalArgumentException {
		if (!containsVertex(startingVertex))
			throw new IllegalArgumentException("Vertice inesistente.");
		VisitForest visit = new VisitForest(this, VisitType.DFS_TOT);
		for (String v : vertices) {
			if (visit.getColor(v) == Color.WHITE)
				visitaDFSRicorsiva(visit, v);
		}
		return visit;
	}

	@Override
	public VisitForest getDFSTOTForest(String[] vertexOrdering)
			throws UnsupportedOperationException, IllegalArgumentException {
		for (String v : vertexOrdering) {
			if (!containsVertex(v))
				throw new IllegalArgumentException("Vertice inesistente.");
		}
		VisitForest visit = new VisitForest(this, VisitType.DFS_TOT);
		for (String v : vertexOrdering) {
			if (visit.getColor(v) == Color.WHITE)
				visitaDFSRicorsiva(visit, v);
		}
		return visit;
	}

	private void visitaDFSRicorsiva(VisitForest visit, String vertex) {
		visit.setColor(vertex, Color.GRAY);
		visit.setStartTime(vertex, time);
		time++;
		for (String v : getAdjacent(vertex)) {
			if (visit.getColor(v) == Color.WHITE) {
				visit.setParent(v, vertex);
				visitaDFSRicorsiva(visit, v);
			}
		}
		visit.setColor(vertex, Color.BLACK);
		visit.setEndTime(vertex, time);
		time++;
	}

	@Override
	public String[] topologicalSort() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Il grafo non è orientato, impossibile completare operazione.");
	}

	@Override
	public Set<Set<String>> stronglyConnectedComponents() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Il grafo non è orientato, impossibile completare operazione.");
	}

	@Override
	public Set<Set<String>> connectedComponents() throws UnsupportedOperationException {
		Set<Set<String>> ris = new HashSet<>();
		VisitForest visit = new VisitForest(this, VisitType.DFS);
		for (String v : vertices) {
			if (visit.getColor(v) == Color.WHITE) {
				ris.add(DFSComponentiConnesse(visit, v));
			}
		}
		return ris;
	}
	
	private Set<String> DFSComponentiConnesse(VisitForest visit, String vertex) {
		Set<String> ris = new HashSet<String>();
		DFS(visit, vertex, ris);
		return ris;
	}

	private void DFS(VisitForest visit, String vertex, Set<String> ris) {
		visit.setColor(vertex, Color.GRAY);
		visit.setStartTime(vertex, time);
		time++;
		ris.add(vertex);
		for (String v : getAdjacent(vertex)) {
			if (visit.getColor(v) == Color.WHITE) {
				visit.setParent(v, vertex);
				DFS(visit, v, ris);
			}
		}
		visit.setColor(vertex, Color.BLACK);
		visit.setEndTime(vertex, time);
		time++;
	}

	@Override
	public double getEdgeWeight(String sourceVertex, String targetVertex)
			throws IllegalArgumentException, NoSuchElementException {
		if (!containsVertex(targetVertex) || !containsVertex(sourceVertex))
			throw new IllegalArgumentException("Vertice inesistente.");
		if (!containsEdge(sourceVertex, targetVertex))
			throw new NoSuchElementException("Arco inesistente.");
		return adjMatrix[getVertexIndex(sourceVertex)][getVertexIndex(targetVertex)];
	}

	@Override
	public void setEdgeWeight(String sourceVertex, String targetVertex, double weight)
			throws IllegalArgumentException, NoSuchElementException {
		if (!containsVertex(targetVertex) || !containsVertex(sourceVertex))
			throw new IllegalArgumentException("Vertice inesistente.");
		if (!containsEdge(sourceVertex, targetVertex))
			throw new NoSuchElementException("Arco inesistente.");
		adjMatrix[getVertexIndex(sourceVertex)][getVertexIndex(targetVertex)] = weight;
	}

	@Override
	public WeightedGraph getBellmanFordShortestPaths(String startingVertex)
			throws UnsupportedOperationException, IllegalArgumentException {
		throw new UnsupportedOperationException("Non implementato.");
	}

	@Override
	public WeightedGraph getDijkstraShortestPaths(String startingVertex)
			throws UnsupportedOperationException, IllegalArgumentException {
		throw new UnsupportedOperationException("Non implementato.");
	}

	@Override
	public WeightedGraph getPrimMST(String startingVertex)
			throws UnsupportedOperationException, IllegalArgumentException {
		throw new UnsupportedOperationException("Non implementato.");
	}

	@Override
	public WeightedGraph getKruskalMST() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Non implementato.");
	}

	@Override
	public WeightedGraph getFloydWarshallShortestPaths() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Non implementato.");
	}

}
