package smartgraph.view;

import com.pa.proj2020.adts.graph.Edge;
import com.pa.proj2020.adts.graph.GraphAdjacencyList;
import com.pa.proj2020.adts.graph.Vertex;
import model.Factory.Relationship;
import model.Factory.Simple;
import model.User;

public class MainTeste {
    public static void main(String[] args) {
       /*SocialNetwork sn = new SocialNetwork("Net");

        User u1 = new User(1);
        User u2 = new User(2);
        User u3 = new User(3);
        User u4 = new User(4);
        User u5 = new User(5);

        Relationship r1 = new Simple("A");
        Relationship r2 = new Simple("B");
        Relationship r3 = new Simple("C");
        Relationship r4 = new Simple("D");

        sn.addUser(u1);
        sn.addUser(u2);
        sn.addUser(u3);
        sn.addUser(u4);
        sn.addUser(u5);

        sn.addRelationship(u1,u2,r1);
        sn.addRelationship(u3,u2,r2);
        sn.addRelationship(u4,u5,r3);
        sn.addRelationship(u2,u5,r4);

        System.out.println(sn.digraph);*/


     GraphAdjacencyList graph = new GraphAdjacencyList();

     Vertex<String> v1;
     Vertex<String> v2;
     Vertex<String> v3;
     Vertex<String> v4;

     Edge<String, String> e1;
     Edge<String, String> e2;


     v1 = graph.insertVertex("a");
     v2 = graph.insertVertex("b");
     v3 = graph.insertVertex("c");
     v4 = graph.insertVertex("d");

     e1 = graph.insertEdge(v1,v2, "ab");
     e2 = graph.insertEdge(v1,v2, "cb");

     System.out.println("Edges: " + graph.edges());
     System.out.println("Graph" + graph.toString());


    }
}