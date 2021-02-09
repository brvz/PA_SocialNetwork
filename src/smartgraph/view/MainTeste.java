package smartgraph.view;

import com.pa.proj2020.adts.graph.Edge;
import com.pa.proj2020.adts.graph.GraphAdjacencyList;
import com.pa.proj2020.adts.graph.Vertex;

import model.Relationship;
import model.SocialNetwork;
import model.User;

public class MainTeste {
    public static void main(String[] args) {
     SocialNetwork sn = new SocialNetwork("SN");

     User u1 = new User(1);
     User u2 = new User(2);
     User u3 = new User(3);
     User u4 = new User(4);
     User u5 = new User(5);
     User u6 = new User(6);


     sn.addUser(u1);
     sn.addUser(u2);
     sn.addUser(u3);
     sn.addUser(u4);
     sn.addUser(u5);
     sn.addUser(u6);

     System.out.println(u1);
     u1.CSVaddUsersInterest(sn.getInterestList());
     u2.CSVaddUsersInterest(sn.getInterestList());
     u3.CSVaddUsersInterest(sn.getInterestList());
     u4.CSVaddUsersInterest(sn.getInterestList());
     u5.CSVaddUsersInterest(sn.getInterestList());
     u6.CSVaddUsersInterest(sn.getInterestList());

     sn.addRelationship(u1,u2, new Relationship(u1,u2));
     sn.addRelationship(u2,u1, new Relationship(u2,u1));
     sn.addRelationship(u1,u3, new Relationship(u1,u3));
     sn.addRelationship(u1,u4, new Relationship(u1,u4));
     sn.addRelationship(u2,u3, new Relationship(u2,u3));
     sn.addRelationship(u3,u4, new Relationship(u3,u4));
     sn.addRelationship(u2,u5, new Relationship(u2,u5));
     sn.addRelationship(u6,u4, new Relationship(u6,u4));


     System.out.println(sn.getSn());


     /*GraphAdjacencyList graph = new GraphAdjacencyList();

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

*/
    }
}