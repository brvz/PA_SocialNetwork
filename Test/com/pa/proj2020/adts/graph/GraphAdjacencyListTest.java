package com.pa.proj2020.adts.graph;

import com.sun.jndi.ldap.ext.StartTlsResponseImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;


/**
 * This class has the purpose to test all the methods of GraphAdjacencyList.java.
 */
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

    /***
     * Setup graph before tests
     */
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

    /***
     * Test insert int an edge
     */
    @Test
    public void test_insert_edges() {

        assertEquals(9, graph.numEdges());

        graph.insertEdge(v6, v5, "FE");
        assertEquals(10, graph.numEdges());

        Vertex<String> v10 = graph.insertVertex(null);
        assertThrows(InvalidVertexException.class, () -> graph.insertEdge(v10.element(), v2.element(), "GZ"), "Null V.");

        Vertex<String> v11 = graphTest.insertVertex("11");
        graph.insertEdge(v1.element(), v2.element(), "AB3");
        assertEquals(10, graph.numEdges());

        assertThrows(InvalidEdgeException.class, () -> graph.insertEdge(v1.element(), v2.element(), e1.element()), "There's already an edge with this element.");


        assertThrows(InvalidVertexException.class, () -> graph.insertVertex("A"), "There's already a vertex with this element.");

    }

    /***
     * Test edges list
     */
    @Test
    public void test_edges() {
        Collection<Edge<String, String>> edgeList = graph.edges();

        assertEquals(edgeList, graph.edges());
    }

    /***
     * Test edges list
     */
    @Test
    public void test_vertices() {
        Vertex[] vertices = new Vertex[2];
        vertices[0] = v1;
        vertices[1] = v2;

        assertEquals(vertices[0], e1.vertices()[1]);
        assertEquals(vertices[1], e1.vertices()[0]);
    }

    /***
     * Tests for each vertex how many incident edges
     */
    @Test
    public void test_incident_edges() {

        Collection<Edge<String, String>> edgeList = graph.incidentEdges(v1);

        assertEquals(edgeList, graph.incidentEdges(v1));
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


    /***
     * Tests if vertexs are oposite to each other
     */
    @Test
    public void test_opposite() {
        assertEquals(v2, graph.opposite(v1, e1));
        assertEquals(v1, graph.opposite(v2, e1));
    }

    /***
     * Tests if vertexs is removed
     */
    @Test
    public void test_remove_vertex() {
        assertEquals(v1.element(), graph.removeVertex(v1));
    }

    /***
     * Tests if edge is removed
     */
    @Test
    public void test_remove_edge() {
        assertEquals(e1.element(), graph.removeEdge(e1));
        assertThrows(InvalidEdgeException.class, () -> graph.removeEdge(null), "Null edge.");
        Vertex<String> v11 = graphTest.insertVertex("11");
        Vertex<String> v12 = graphTest.insertVertex("12");
        Edge<String, String> e12 = graphTest.insertEdge(v11, v12, "e12");
        assertThrows(InvalidEdgeException.class, () -> graph.removeEdge(e12), "Edge does not belong to this graph.");
    }

    /***
     * Tests if vertex element is replaced
     */
    @Test
    public void test_replace_vertex() {
        assertEquals(v1.element(), graph.replace(v1, "Z"));

        assertThrows(InvalidVertexException.class, () -> graph.replace(v1, "B"), "There's already a vertex with this element.");

    }

    /***
     * Tests if edge element is replaced
     */
    @Test
    public void test_replace_edge() {
        assertEquals(e1.element(), graph.replace(e1, "Z"));

        assertThrows(InvalidEdgeException.class, () -> graph.replace(e1, e3.element()), "There's already an edge with this element.");
    }

    /***
     * Tests if digraph toString returns values
     */
    @Test
    public void test_to_string() {
        assertEquals("Graph with 6 vertices and 9 edges:\n"
        +"--- Vertices: \n"
        +"\tA\n"
        +"\tB\n"
        +"\tC\n"
        +"\tD\n"
        +"\tE\n"
        +"\tF\n\n"
        +"--- Edges: \n"
        +"\tEdge{{AB}, vertexOutbound=Vertex{B}, vertexInbound=Vertex{A}}\n"
        +"\tEdge{{AC}, vertexOutbound=Vertex{C}, vertexInbound=Vertex{A}}\n"
        +"\tEdge{{AD}, vertexOutbound=Vertex{D}, vertexInbound=Vertex{A}}\n"
        +"\tEdge{{AB2}, vertexOutbound=Vertex{A}, vertexInbound=Vertex{B}}\n"
        +"\tEdge{{BC}, vertexOutbound=Vertex{C}, vertexInbound=Vertex{B}}\n"
        +"\tEdge{{BE}, vertexOutbound=Vertex{E}, vertexInbound=Vertex{B}}\n"
        +"\tEdge{{CD}, vertexOutbound=Vertex{D}, vertexInbound=Vertex{C}}\n"
        +"\tEdge{{DF}, vertexOutbound=Vertex{F}, vertexInbound=Vertex{D}}\n"
        +"\tEdge{{DF2}, vertexOutbound=Vertex{F}, vertexInbound=Vertex{D}}\n\n"

        +"--- MyVertex: \n"
        +"\tVertex{A}\n"
        +"\tVertex{B}\n"
        +"\tVertex{C}\n"
        +"\tVertex{D}\n"
        +"\tVertex{E}\n"
        +"\tVertex{F}\n\n"

        +"--- MyEdge: \n"
        +"\tEdge{{AB}, vertexOutbound=Vertex{B}, vertexInbound=Vertex{A}}\n"
        +"\tEdge{{AC}, vertexOutbound=Vertex{C}, vertexInbound=Vertex{A}}\n"
        +"\tEdge{{AD}, vertexOutbound=Vertex{D}, vertexInbound=Vertex{A}}\n"
        +"\tEdge{{AB2}, vertexOutbound=Vertex{A}, vertexInbound=Vertex{B}}\n"
        +"\tEdge{{BC}, vertexOutbound=Vertex{C}, vertexInbound=Vertex{B}}\n"
        +"\tEdge{{BE}, vertexOutbound=Vertex{E}, vertexInbound=Vertex{B}}\n"
        +"\tEdge{{CD}, vertexOutbound=Vertex{D}, vertexInbound=Vertex{C}}\n"
        +"\tEdge{{DF}, vertexOutbound=Vertex{F}, vertexInbound=Vertex{D}}\n"
        +"\tEdge{{DF2}, vertexOutbound=Vertex{F}, vertexInbound=Vertex{D}}\n"
                , graph.toString());
    }


    /***
     * Tests if vertex is in graph
     */
    @Test
    public void test_contains() {
        Vertex<String> temp = graphTest.insertVertex("asdasd");
        assertEquals(true, graph.vertices().contains(v1));
        assertEquals(false, graph.vertices().contains(temp));
    }

    /**
     * Test outbound edges
     */
    @Test
    public void test_outboundEdges(){
        Collection<Edge<String, String>> outboundEdges = graph.outboundEdges(v1);
        assertEquals(outboundEdges, graph.outboundEdges(v1));
    }
}