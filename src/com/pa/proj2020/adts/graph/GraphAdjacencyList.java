package com.pa.proj2020.adts.graph;

import java.util.*;

public class GraphAdjacencyList<V, E> implements Graph<V,E> {

    private final Map<V, Vertex<V>> graph;

    public GraphAdjacencyList() {
        graph = new HashMap<>();
    }

    @Override
    public int numVertices() {
       return graph.keySet().size();
    }

    @Override
    public int numEdges() {
        Collection<Edge<E, V>> list = new LinkedList<>();
        for (Vertex<V> vertex : graph.values()) {
            for (Edge<E, V> edge : incidentEdges(vertex))
                list.add(edge);
        }
        return list.size();
    }

    @Override
    public Collection<Vertex<V>> vertices() {
        return graph.values();
    }

    @Override
    public Collection<Edge<E, V>> edges() {
        ArrayList<Edge<E, V>> edges = new ArrayList<>();
        for (Vertex<V> vertex : graph.values()) {
            edges.addAll(incidentEdges(vertex));
        }
        return edges;
    }

    @Override
    public Collection<Edge<E, V>> incidentEdges(Vertex<V> v) throws InvalidVertexException {
        MyVertex myVertex = checkVertex(v);

        List<Edge<E, V>> incidentEdges = new ArrayList<>();
        //incidentEdges.addAll(myVertex.inadj);
        incidentEdges.addAll(myVertex.edges);
        return incidentEdges;
    }

    /**
     * Returns the opposite vertex to v.
     *
     * @param v vertex
     * @param e edge
     * @return the opposite vertex
     * @throws InvalidVertexException if <code>v</code> is not found in any
     *                                vertices of the digraph according to the equality of {@link Object#equals(java.lang.Object)
     *                                }
     *                                method.
     * @throws InvalidEdgeException   if there already exists an edge
     *                                containing <code>e</code> according to the equality of
     *                                {@link Object#equals(java.lang.Object) }
     *                                method.
     */
    @Override
    public Vertex<V> opposite(Vertex<V> v, Edge<E, V> e) throws InvalidVertexException, InvalidEdgeException {
        MyEdge edge = checkEdge(e);
        if (edge.vertexInbound.equals(v)) {
            return edge.vertexOutbound;
        } else {
            return edge.vertexInbound;
        }
    }

    /**
     * Evaluates whether two vertices are adjacent, i.e., there exists some
     * directed-edge connecting <code>v</code> and <code>u</code>.
     * <p>
     * The existing edge must be directed as
     * <code>v --&gt; u</code>.
     * <p>
     * If, for example, there exists only an edge
     * <code>v &lt;-- u</code>, they are not considered adjacent.
     *
     * @param v outbound vertex
     * @param u  inbound vertex
     * @return true if they are adjacent, false otherwise.
     * @throws InvalidVertexException if <code>v</code> or
     *                                <code>u</code> are invalid vertices for the graph
     */
    @Override
    public boolean areAdjacent(Vertex<V> u, Vertex<V> v) throws InvalidVertexException {
        checkVertex(v);
        checkVertex(u);
        for (Vertex<V> vertex : graph.values()) {
            for (Edge<E, V> incidentEdge : incidentEdges(vertex)) {
                MyEdge edge = checkEdge(incidentEdge);
                if (edge.vertexOutbound.equals(v) && edge.vertexInbound.equals(u)) {
                    return true;
                }
            }
        }

        return false;
    }


    /**
     * Inserts a new vertex with a given element and return its (the vertex's)
     * reference.
     *
     * @param vElement vertex's stored element
     * @return the reference for the newly created vertex
     * @throws InvalidVertexException if <code>vElement</code> is not found
     *                                in any vertices of the digraph according to the equality of {@link Object#equals(java.lang.Object)
     *                                }
     *                                method.
     */
    @Override
    public Vertex<V> insertVertex(V vElement) throws InvalidVertexException {
        if (existsVertexWith(vElement)) {
            throw new InvalidVertexException("There's already a vertex with this element.");
        }
        MyVertex newVertex = new MyVertex(vElement);
        graph.put(vElement, newVertex);
        return newVertex;
    }

    @Override
    public Edge<E, V> insertEdge(Vertex<V> u, Vertex<V> v, E edgeElement) throws InvalidVertexException, InvalidEdgeException {
        if (existsEdgeWith(edgeElement)) {
            throw new InvalidEdgeException("There's already an edge with this element.");
        }

        MyVertex uVertex = checkVertex(v);
        MyVertex vVertex = checkVertex(u);

        MyEdge newEdge = new MyEdge(edgeElement, v, u);

        uVertex.edges.add(newEdge);
        vVertex.edges.add(newEdge);

        return newEdge;
    }

    @Override
    public Edge<E, V> insertEdge(V vElement1, V vElement2, E edgeElement) throws InvalidVertexException, InvalidEdgeException {
        MyVertex vVertex = checkV(vElement1);
        MyVertex uVertex = checkV(vElement2);

        if (existsEdgeWith(edgeElement)) {
            throw new InvalidEdgeException("Already exists edge with element");
        }

        MyEdge newEdge = new MyEdge(edgeElement, vVertex, uVertex);

        uVertex.edges.add(newEdge);
        vVertex.edges.add(newEdge);
        /*for (List<Edge<E, V>> list : graph.values()){
            list.add(newEdge);
        }*/

        return newEdge;
    }

    /**
     * Removes a vertex in the digraph with a given element and return its (the
     * vertex's) reference.
     *
     * @param v vertex
     * @return the reference for the newly created vertex
     * @throws InvalidVertexException if <code>v</code> is not found in any
     *                                vertices of the digraph according to the equality of {@link Object#equals(java.lang.Object)
     *                                }
     *                                method.
     */
    @Override
    public V removeVertex(Vertex<V> v) throws InvalidVertexException {
        checkVertex(v);

        V element = v.element();

        //remove incident edges
        Iterable<Edge<E, V>> incidentEdges = incidentEdges(v);
        for (Edge<E, V> edge : incidentEdges) {
            graph.remove(edge.element());
        }

        graph.remove(v.element());

        return element;
    }

    /**
     * Removes a edge in the digraph with a given element and return its (the
     * edge's) reference.
     *
     * @param e edge
     * @return the reference for the newly created edge
     * @throws InvalidEdgeException if there doesn't exists an edge
     *                              containing <code>edgeElement</code> according to the equality of
     *                              {@link Object#equals(java.lang.Object) }
     *                              method.
     */
    @Override
    public E removeEdge(Edge<E, V> e) throws InvalidEdgeException {
        checkEdge(e);

        E element = e.element();
        graph.remove(e.element());

        return element;
    }

    /**
     * Replace a vertex in the digraph with a given element and return its (the
     * vertex's) reference.
     *
     * @param v          vertex
     * @param newElement vertex's stored element
     * @return the reference for the newly created vertex
     * @throws InvalidVertexException if there already exists an vertex
     *                                containing <code>newElement</code> according to the equality of
     *                                {@link Object#equals(java.lang.Object) }
     *                                method.
     */
    @Override
    public V replace(Vertex<V> v, V newElement) throws InvalidVertexException {
        if (existsVertexWith(newElement)) {
            throw new InvalidVertexException("There's already a vertex with this element.");
        }

        MyVertex vertex = checkVertex(v);

        V oldElement = vertex.element;
        vertex.element = newElement;

        return oldElement;
    }

    /**
     * Replace a edge in the digraph with a given element and return its (the
     * edge's) reference.
     *
     * @param e          edge
     * @param newElement edge's stored element
     * @return the reference for the newly created edge
     * @throws InvalidEdgeException if there already exists an edge
     *                              containing <code>newElement</code> according to the equality of
     *                              {@link Object#equals(java.lang.Object) }
     *                              method.
     */
    @Override
    public E replace(Edge<E, V> e, E newElement) throws InvalidEdgeException {
        if (existsEdgeWith(newElement)) {
            throw new InvalidEdgeException("There's already an edge with this element.");
        }

        MyEdge edge = checkEdge(e);

        E oldElement = edge.element;
        edge.element = newElement;

        return oldElement;
    }



    /**
     * Returns a true if a given vertex's stored element exists in graph.
     *
     * @param vElement vertex's stored element
     * @return true if vertex's stored element exists in graph
     */
    private boolean existsVertexWith(V vElement) {
        return graph.containsKey(vElement);
    }

    /**
     * Returns a true if a given edge's stored element exists in graph.
     *
     * @param edgeElement edge's stored element
     * @return true if edge's stored element exists in graph
     */
    private boolean existsEdgeWith(E edgeElement) {
        int i = 0;
        for (Vertex<V> vertex : graph.values()) {
            for (Edge<E, V> edge : incidentEdges(vertex)) {
                if (edge.element().equals(edgeElement)) {
                    i++;
                }
                if (i >= 2) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * Returns String with the information of the digraph in a given element and
     * return its (the vertex's) reference.
     *
     * @return StringBuilder sb in toString() format
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(
                String.format("Graph with %d vertices and %d edges:\n", numVertices(), numEdges())
        );

        sb.append("--- Vertices: \n");
        graph.keySet().forEach((v) -> {
            sb.append("\t").append(v.toString()).append("\n");
        });
        sb.append("\n--- Edges: \n");
        graph.values().forEach((e) -> {
            for(Edge<E,V> edges : incidentEdges(e)){
                MyEdge newEdge = checkEdge(edges);
                sb.append("\t").append(newEdge.toString()).append("\n");
            }

        });
        sb.append("\n--- MyVertex: \n");
        graph.keySet().forEach((v) -> {
            sb.append("\t").append(checkV(v).toString()).append("\n");
        });

        sb.append("\n--- MyEdge: \n");
        for (Vertex<V> vertex : graph.values()) {
            for (Edge<E, V> incidentEdge : incidentEdges(vertex)) {
                MyEdge newEdge = checkEdge(incidentEdge);
                sb.append("\t").append(newEdge.toString()).append("\n");
            }

        }

        return sb.toString();

    }



    private class MyVertex implements Vertex<V> {

        V element;
        List<Edge<E, V>> edges;


        public MyVertex(V element) {
            this.element = element;
            edges = new LinkedList<>();
        }

        /**
         * Gets V element stored in MyVertex
         *
         * @return V element stored in MyVertex
         */
        @Override
        public V element() {
            return this.element;
        }

        @Override
        public String toString() {
            return "Vertex{" + element + '}';
        }
    }


    private class MyEdge implements Edge<E, V> {

        E element;
        Vertex<V> vertexOutbound;
        Vertex<V> vertexInbound;

        public MyEdge(E element, Vertex<V> vertexOutbound, Vertex<V> vertexInbound) {
            this.element = element;
            this.vertexOutbound = vertexOutbound;
            this.vertexInbound = vertexInbound;
        }

        /**
         * Gets E element stored in MyEdge
         *
         * @return E element stored in MyEdge
         */
        @Override
        public E element() {
            return this.element;
        }

        /**
         * Gets all vertices connected by MyEdge
         *
         * @return Vertex<V>[] Array containing vertexes from MyEdge
         */
        @Override
        public Vertex<V>[] vertices(){
            Vertex[] vertices = new Vertex[2];
            vertices[0] = vertexOutbound;
            vertices[1] = vertexInbound;

            return vertices;
        }

        @Override
        public String toString(){
            return "Edge{{" + element + "}, vertexOutbound=" + vertexOutbound.toString()
                    + ", vertexInbound=" + vertexInbound.toString() + '}';
        }
    }


    /**
     * Checks whether a given vertex is valid and belongs to this digraph.
     *
     * @param v vertex
     * @return the reference for the newly created MyVertex
     * @throws InvalidVertexException if <code>v</code> is not found in any vertices of the digraph according to the equality of {@link Object#equals(java.lang.Object)} method.
     */
    private MyVertex checkVertex(Vertex<V> v) throws InvalidVertexException {
        if (v == null) {
            throw new InvalidVertexException("Null vertex.");
        }
        MyVertex vertex;
        vertex = (MyVertex) v;

        if (!graph.containsKey(vertex.element)) {
            throw new InvalidVertexException("Vertex does not belong to this graph.");
        }

        return vertex;
    }

    /**
     * Checks whether a given edge is valid and belongs to this digraph.
     *
     * @param e edge
     * @return the reference for the newly created MyEdge
     * @throws InvalidEdgeException if there doesn't exists an edge
     *                              containing <code>e</code> according to the equality of
     *                              {@link Object#equals(java.lang.Object) }
     *                              method.
     */
    private MyEdge checkEdge(Edge<E, V> e) throws InvalidEdgeException{
        if (e == null) {
            throw new InvalidEdgeException("Null edge.");
        }

        MyEdge edge;
        edge = (MyEdge) e;

        if (!graph.containsValue(edge.vertexInbound) && !graph.containsValue(edge.vertexOutbound)) {
            throw new InvalidEdgeException("Edge does not belong to this graph.");
        }

        return edge;
    }


    /**
     * Checks whether a given vertex is valid and belongs to this graph.
     *
     * @param v vertex
     * @return the reference for the newly created MyVertex
     * @throws InvalidEdgeException if there doesn't exists a vertex
     *                              containing <code>v</code> according to the equality of
     *                              {@link Object#equals(java.lang.Object) }
     *                              method.
     */
    private MyVertex checkV(V v) throws InvalidVertexException {
        if (v == null) {
            throw new InvalidVertexException("Null V.");
        }

        MyVertex vertex = new MyVertex(v);
        /*try {
            vertex.element = v;
        } catch (ClassCastException e) {
            throw new InvalidVertexException("Not a V.");
        }*/
        if (!graph.containsKey(v)) {
            throw new InvalidVertexException("Vertex does not belong to this graph.");
        }

        return vertex;
    }

}
