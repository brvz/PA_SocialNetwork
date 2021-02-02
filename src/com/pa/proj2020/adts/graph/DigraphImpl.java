package com.pa.proj2020.adts.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @param <V> Type of element stored at a vertex
 * @param <E> Type of element stored at an edge
 * @see Digraph
 * @see Edge
 * @see Vertex
 */
public class DigraphImpl<V, E> implements Digraph<V, E> {

    private final Map<V, Vertex<V>> graph;

    public DigraphImpl() {
        graph = new HashMap<>();
    }

    /**
     * Returns a vertex's <i>incident</i> edges as a collection.
     * <p>
     * Incident edges are all edges that have vertex <code>inbound</code> as the
     * <i>inbound vertex</i>, i.e., the edges "entering" vertex
     * <code>inbound</code>. If there are no incident edges, e.g., an isolated
     * vertex, returns an empty collection.
     *
     * @param inbound vertex for which to obtain the incident edges
     * @return collection of edges
     */
    @Override
    public Collection<Edge<E, V>> incidentEdges(Vertex<V> inbound) throws InvalidVertexException {
        MyVertex myVertex = checkVertex(inbound);

        List<Edge<E, V>> incidentEdges = new ArrayList<>();
        //incidentEdges.addAll(myVertex.inadj);
        incidentEdges.addAll(myVertex.outadj);
        return incidentEdges;
    }

    /**
     * Returns a vertex's <i>outbound</i> edges as a collection.
     * <p>
     * Incident edges are all edges that have vertex <code>outbound</code> as
     * the
     * <i>outbound vertex</i>, i.e., the edges "leaving" vertex
     * <code>outbound</code>. If there are no outbound edges, e.g., an isolated
     * vertex, returns an empty collection.
     *
     * @param outbound vertex for which to obtain the outbound edges
     * @return collection of edges
     */
    @Override
    public Collection<Edge<E, V>> outboundEdges(Vertex<V> outbound) throws InvalidVertexException {
        MyVertex myVertex = checkVertex(outbound);

        List<Edge<E, V>> outboundEdges = new ArrayList<>();
        outboundEdges.addAll(myVertex.outadj);
        return outboundEdges;
    }

    /**
     * Evaluates whether two vertices are adjacent, i.e., there exists some
     * directed-edge connecting <code>outbound</code> and <code>inbound</code>.
     * <p>
     * The existing edge must be directed as
     * <code>outbound --&gt; inbound</code>.
     * <p>
     * If, for example, there exists only an edge
     * <code>outbound &lt;-- inbound</code>, they are not considered adjacent.
     *
     * @param outbound outbound vertex
     * @param inbound  inbound vertex
     * @return true if they are adjacent, false otherwise.
     * @throws InvalidVertexException if <code>outbound</code> or
     *                                <code>inbound</code> are invalid vertices for the digraph
     */
    @Override
    public boolean areAdjacent(Vertex<V> outbound, Vertex<V> inbound) throws InvalidVertexException {
        checkVertex(outbound);
        checkVertex(inbound);
        for (Vertex<V> vertex : graph.values()) {
            for (Edge<E, V> incidentEdge : incidentEdges(vertex)) {
                MyEdge edge = checkEdge(incidentEdge);
                if (edge.vertexOutbound.equals(outbound) && edge.vertexInbound.equals(inbound)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Inserts a new edge with a given element between two existing vertices and
     * return its (the edge's) reference.
     *
     * @param outbound    outbound vertex
     * @param inbound     inbound vertex
     * @param edgeElement element to store in the new edge
     * @return the reference for the newly created edge
     * @throws InvalidVertexException if <code>outbound</code> or
     *                                <code>inbound</code> are not found in the digraph
     *                                according to the equality of {@link Object#equals(java.lang.Object) }
     *                                method.
     * @throws InvalidEdgeException   if there already exists an edge
     *                                containing <code>edgeElement</code> according to the equality of
     *                                {@link Object#equals(java.lang.Object) }
     *                                method.
     */
    @Override
    public Edge<E, V> insertEdge(Vertex<V> outbound, Vertex<V> inbound, E edgeElement) throws InvalidVertexException, InvalidEdgeException {
        if (existsEdgeWith(edgeElement)) {
            throw new InvalidEdgeException("There's already an edge with this element.");
        }

        MyVertex outVertex = checkVertex(outbound);
        MyVertex inVertex = checkVertex(inbound);

        MyEdge newEdge = new MyEdge(edgeElement, outVertex, inVertex);

        inVertex.outadj.add(newEdge);
        outVertex.inadj.add(newEdge);

        return newEdge;
    }

    /**
     * Inserts a new edge with a given element between two existing vertices and
     * return its (the edge's) reference.
     *
     * @param outboundElement outbound vertex's stored element
     * @param inboundElement  inbound vertex's stored element
     * @param edgeElement     element to store in the new edge
     * @return the reference for the newly created edge
     * @throws InvalidVertexException if <code>outboundElement</code> or
     *                                <code>inboundElement</code> are not found in any vertices of the digraph
     *                                according to the equality of {@link Object#equals(java.lang.Object) }
     *                                method.
     * @throws InvalidEdgeException   if there already exists an edge
     *                                containing <code>edgeElement</code> according to the equality of
     *                                {@link Object#equals(java.lang.Object) }
     *                                method.
     */
    @Override
    public Edge<E, V> insertEdge(V outboundElement, V inboundElement, E edgeElement) throws InvalidVertexException, InvalidEdgeException {
        MyVertex outVertex = checkV(outboundElement);
        MyVertex inVertex = checkV(inboundElement);

        if (existsEdgeWith(edgeElement)) {
            throw new InvalidEdgeException("Already exists edge with element");
        }

        MyEdge newEdge = new MyEdge(edgeElement, outVertex, inVertex);

        inVertex.outadj.add(newEdge);
        outVertex.inadj.add(newEdge);
        /*for (List<Edge<E, V>> list : graph.values()){
            list.add(newEdge);
        }*/

        return newEdge;
    }

    /**
     * Returns number of vertices in digraph.
     *
     * @return an int with the number of vertices in digraph
     */
    @Override
    public int numVertices() {
        return graph.keySet().size();
    }

    /**
     * Returns number of edges in digraph.
     *
     * @return an int with the number of edges in digraph
     */
    @Override
    public int numEdges() {
        Collection<Edge<E, V>> list = new LinkedList<>();
        for (Vertex<V> vertex : graph.values()) {
            for (Edge<E, V> edge : incidentEdges(vertex))
                list.add(edge);
        }
        return list.size();
    }

    public void clear(){
        graph.clear();
    }

    /**
     * Returns all vertices in digraph.
     *
     * @return a list of all vertices in digraph
     */
    @Override
    public Collection<Vertex<V>> vertices() {
        return graph.values();
    }

    /**
     * Returns all edges in digraph.
     *
     * @return a list of all edges in digraph
     */
    @Override
    public Collection<Edge<E, V>> edges() {
        ArrayList<Edge<E, V>> edges = new ArrayList<>();
        for (Vertex<V> vertex : graph.values()) {
            edges.addAll(incidentEdges(vertex));
        }
        return edges;
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
        Collection<Edge<E, V>> inOutEdges = incidentEdges(v);
        inOutEdges.addAll(outboundEdges(v));

        inOutEdges.forEach((edge) -> {
            graph.remove(edge.element());
        });

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
            sb.append("\t").append(e.toString()).append("\n");
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

    /**
     * Returns a true if a given vertex's stored element exists in digraph.
     *
     * @param vElement vertex's stored element
     * @return true if vertex's stored element exists in digraph
     */
    private boolean existsVertexWith(V vElement) {
        return graph.containsKey(vElement);
    }

    /**
     * Returns a true if a given edge's stored element exists in digraph.
     *
     * @param edgeElement edge's stored element
     * @return true if edge's stored element exists in digraph
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



    private class MyVertex implements Vertex<V> {

        V element;
        List<Edge<E, V>> inadj;
        List<Edge<E, V>> outadj;

        public MyVertex(V element) {
            this.element = element;
            inadj = new LinkedList<>();
            outadj = new LinkedList<>();
        }

        /*public List<Edge<E, V>> getInadj() {
            return inadj;
        }

        public List<Edge<E, V>> getOutadj() {
            return outadj;
        }

        public void addINAdjacent(Edge<E,V> e){
            inadj.add(e);
        }

        public void addOUTAdjacent(Edge<E,V> e){
            outadj.add(e);
        }*/

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

        public boolean contains(Vertex<V> v) {
            return (vertexOutbound == v || vertexInbound == v);
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
     * Checks if MyEdge contains Vertex<V> v
     *
     * @param v
     * @return true if MyEdge contains V, false if not
     */
    public boolean contains(Vertex<V> v) {
        for (Vertex<V> vertex : graph.values()) {
            for (Edge<E, V> incidentEdge : incidentEdges(vertex)) {
                MyEdge newEdge = checkEdge(incidentEdge);
                if (newEdge.contains(v)) {
                    return true;
                }
            }
        }
        return false;
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
     * Checks whether a given vertex is valid and belongs to this digraph.
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
}
