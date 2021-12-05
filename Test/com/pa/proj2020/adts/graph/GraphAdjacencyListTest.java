package com.pa.proj2020.adts.graph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;


class GraphAdjacencyListTest {
    GraphAdjacencyList<String, String> graph;
    GraphAdjacencyList<String, String> graphTest;

    Vertex<String> v1;
    Vertex<String> v2;
    Vertex<String> v3;
    Vertex<String> v4;
    Vertex<String> v5;
    Vertex<String> v6;

    Edge<String, String> e1;
    Edge<String, String> e2;
    Edge<String, String> e3;
    Edge<String, String> e4;
    Edge<String, String> e5;
    Edge<String, String> e6;
    Edge<String, String> e7;
    Edge<String, String> e8;
    Edge<String, String> e9;

    @BeforeEach
    void setUp() {

        graph = new GraphAdjacencyList<>();
        graphTest = new GraphAdjacencyList<>();

        v1 = graph.insertVertex("A");
        v2 = graph.insertVertex("B");
        v3 = graph.insertVertex("C");
        v4 = graph.insertVertex("D");
        v5 = graph.insertVertex("E");
        v6 = graph.insertVertex("F");

        e1 = graph.insertEdge(v1, v2, "AB");
        e2 = graph.insertEdge(v2, v1, "AB2");
        e3 = graph.insertEdge(v1, v3, "AC");
        e4 = graph.insertEdge(v1, v4, "AD");
        e5 = graph.insertEdge(v2, v3, "BC");
        e6 = graph.insertEdge(v3, v4, "CD");
        e7 = graph.insertEdge(v2, v5, "BE");
        e8 = graph.insertEdge(v4, v6, "DF");
        e9 = graph.insertEdge(v4, v6, "DF2");

    }

    @Test
    void insert_edges_test() {
        assertEquals(7, graph.numEdges());

        graph.insertEdge(v1, v3, "AC");
        assertEquals(8, graph.numEdges());

        Vertex<String> v7 = graphTest.insertVertex("V");
        graph.insertEdge(v7, v5, "VE");
        assertEquals(8, graph.numEdges());

        Vertex<String> v8 = graph.insertVertex(null);
        assertThrows(InvalidVertexException.class, () -> graph.insertEdge(v8.element(), v2.element(), "GB"), "Null V.");

        assertThrows(InvalidEdgeException.class, () -> graph.insertEdge(v1.element(), v2.element(), e1.element()), "There's already an edge with this element.");

        assertThrows(InvalidVertexException.class, () -> graph.insertVertex("A"), "There's already a vertex with this element.");
    }

    @Test
    public void test_replace_vertex() {
        assertEquals(v1.element(), graph.replace(v1, "X"));

        assertThrows(InvalidVertexException.class, () -> graph.replace(v1, "B"), "There's already a vertex with this element.");

    }


    @Test
    public void test_replace_edge() {
        assertEquals(e1.element(), graph.replace(e1, "AX"));

        assertThrows(InvalidEdgeException.class, () -> graph.replace(e1, e3.element()), "There's already an edge with this element.");
    }

    @Test
    public void test_edges() {
        Collection<Edge<String, String>> edgeList = graph.edges();

        assertEquals(edgeList, graph.edges());
    }

    @Test
    public void test_vertices() {
        Vertex[] vertices = new Vertex[2];
        vertices[0] = v1;
        vertices[1] = v2;

        assertEquals(vertices[0], e1.vertices()[1]);
        assertEquals(vertices[1], e1.vertices()[0]);
    }

    @Test
    public void test_remove_vertex() {
        assertEquals(v1.element(), graph.removeVertex(v1));
    }

    @Test
    public void test_remove_edge() {
        assertEquals(e1.element(), graph.removeEdge(e1));
        Vertex<String> v9 = graphTest.insertVertex("11");
        Vertex<String> v10 = graphTest.insertVertex("12");
        Edge<String, String> e8 = graphTest.insertEdge(v9, v10, "E8");
        assertThrows(InvalidEdgeException.class, () -> graph.removeEdge(e8), "Edge does not belong to this graph.");
        assertThrows(InvalidEdgeException.class, () -> graph.removeEdge(null), "Null edge.");
    }

    @Test
    public void test_contains() {
        Vertex<String> temp = graphTest.insertVertex("teststs");
        assertEquals(true, graph.vertices().contains(v1));
        assertEquals(false, graph.vertices().contains(temp));
    }
    @Test
    public void test_opposite() {
        assertEquals(v2, graph.opposite(v1, e1));
        assertEquals(v1, graph.opposite(v2, e1));
    }

    @Test
    public void test_outboundEdges(){
        Collection<Edge<String, String>> outboundEdges = graph.outboundEdges(v1);
        assertEquals(outboundEdges, graph.outboundEdges(v1));
    }

    /***
     * Tests if vertexs are adjacent
     */
    @Test
    public void test_are_adjacent() {
        assertEquals(true, graph.areAdjacent(v1, v2));
        assertEquals(false, graph.areAdjacent(v2, v2));

        Vertex<String> v10 = null;
        assertThrows(InvalidVertexException.class, () -> graph.areAdjacent(v10, v2), "Null vertex.");

        Vertex<String> v11 = graphTest.insertVertex("11");
    }

    @Test
    public void test_incident_edges() {

        Collection<Edge<String, String>> edgeList = graph.incidentEdges(v1);

        assertEquals(edgeList, graph.incidentEdges(v1));
    }
}