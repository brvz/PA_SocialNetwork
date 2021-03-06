/* 
 * The MIT License
 *
 * Copyright 2019 brunomnsilva@gmail.com.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package smartgraph.view;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.Relationship;
import model.SocialNetwork;
import model.User;
import smartgraph.view.graphview.SmartGraphPanel;
import com.pa.proj2020.adts.graph.Vertex;
import com.pa.proj2020.adts.graph.Graph;
import com.pa.proj2020.adts.graph.GraphEdgeList;
import smartgraph.view.containers.SmartGraphDemoContainer;
import smartgraph.view.graphview.SmartCircularSortedPlacementStrategy;
import smartgraph.view.graphview.SmartPlacementStrategy;
import smartgraph.view.graphview.SmartStylableNode;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author brunomnsilva
 */
public class MainExample extends Application {

    private volatile boolean running;

    @Override
    public void start(Stage ignored) {

        SocialNetwork network = new SocialNetwork("Social Network");
        System.out.println(network);


        
        SmartPlacementStrategy strategy = new SmartCircularSortedPlacementStrategy();

        SmartGraphPanel<User, Relationship> graphView = new SmartGraphPanel<>(network.getSocialNetwork(), strategy);

        Scene scene = new Scene(new SmartGraphDemoContainer(graphView), 1024, 768);

        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("JavaFX SmartGraph Visualization");
        stage.setMinHeight(500);
        stage.setMinWidth(800);
        stage.setScene(scene);
        stage.show();

        graphView.init();

        graphView.setVertexDoubleClickAction(graphVertex -> {
            System.out.println("Vertex contains element: " + graphVertex.getUnderlyingVertex().element());

            if( !graphVertex.removeStyleClass("myVertex") ) {
                graphVertex.addStyleClass("myVertex");
            }            
        });

        graphView.setEdgeDoubleClickAction(graphEdge -> {
            System.out.println("Edge contains element: " + graphEdge.getUnderlyingEdge().element());
            graphEdge.setStyle("-fx-stroke: black; -fx-stroke-width: 2;");
        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private Graph<String, String> build_sample_digraph() {

        Graph<String, String> g = new GraphEdgeList<>();

        g.insertVertex("A");
        g.insertVertex("B");
        g.insertVertex("C");
        g.insertVertex("D");
        g.insertVertex("E");
        g.insertVertex("F");

        g.insertEdge("A", "B", "AB");
        g.insertEdge("B", "A", "AB2");
        g.insertEdge("A", "C", "AC");
        g.insertEdge("A", "D", "AD");
        g.insertEdge("B", "C", "BC");
        g.insertEdge("C", "D", "CD");
        g.insertEdge("B", "E", "BE");
        g.insertEdge("F", "D", "DF");
        g.insertEdge("F", "D", "DF2");

        //yep, its a loop!
        g.insertEdge("A", "A", "Loop");

        return g;
    }

    private Graph<String, String> build_flower_graph() {

        Graph<String, String> g = new GraphEdgeList<>();

        g.insertVertex("A");
        g.insertVertex("B");
        g.insertVertex("C");
        g.insertVertex("D");
        g.insertVertex("E");
        g.insertVertex("F");
        g.insertVertex("G");

        g.insertEdge("A", "B", "1");
        g.insertEdge("A", "C", "2");
        g.insertEdge("A", "D", "3");
        g.insertEdge("A", "E", "4");
        g.insertEdge("A", "F", "5");
        g.insertEdge("A", "G", "6");

        g.insertVertex("H");
        g.insertVertex("I");
        g.insertVertex("J");
        g.insertVertex("K");
        g.insertVertex("L");
        g.insertVertex("M");
        g.insertVertex("N");

        g.insertEdge("H", "I", "7");
        g.insertEdge("H", "J", "8");
        g.insertEdge("H", "K", "9");
        g.insertEdge("H", "L", "10");
        g.insertEdge("H", "M", "11");
        g.insertEdge("H", "N", "12");

        g.insertEdge("A", "H", "0");

        //g.insertVertex("ISOLATED");
        
        return g;
    }

    private static final Random random = new Random(/* seed to reproduce*/);

    private void continuously_test_adding_elements(Graph<String, String> g, SmartGraphPanel<String, String> graphView) {
        //update graph
        running = true;
        final long ITERATION_WAIT = 3000; //milliseconds

        Runnable r;
        r = () -> {
            int count = 0;
            
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(MainExample.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            while (running) {
                try {
                    Thread.sleep(ITERATION_WAIT);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MainExample.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                //generate new vertex with 2/3 probability, else connect two
                //existing
                String id = String.format("%02d", ++count);
                if (random.nextInt(3) < 2) {
                    //add a new vertex connected to a random existing vertex
                    Vertex<String> existing = get_random_vertex(g);
                    Vertex<String> vertexId = g.insertVertex(("V" + id));
                    g.insertEdge(existing, vertexId, ("E" + id));
                    
                    //this variant must be called to ensure the view has reflected the
                    //underlying graph before styling a node immediately after.
                    graphView.updateAndWait();
                    
                    //color new vertices
                    SmartStylableNode stylableVertex = graphView.getStylableVertex(vertexId);
                    if(stylableVertex != null) {
                        stylableVertex.setStyle("-fx-fill: orange;");
                    }
                } else {
                    Vertex<String> existing1 = get_random_vertex(g);
                    Vertex<String> existing2 = get_random_vertex(g);
                    g.insertEdge(existing1, existing2, ("E" + id));
                    
                    graphView.update();
                }

                
            }
        };

        new Thread(r).start();
    }

    private static Vertex<String> get_random_vertex(Graph<String, String> g) {

        int size = g.numVertices();
        int rand = random.nextInt(size);
        Vertex<String> existing = null;
        int i = 0;
        for (Vertex<String> v : g.vertices()) {
            existing = v;
            if (i++ == rand) {
                break;
            }
        }
        return existing;
    }


    private SocialNetwork build_sample_socialNetwork() {

        SocialNetwork sn = new SocialNetwork("SN");

        /*User u1 = new User(1);
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

        sn.addRelationship(u1,u2, new Relationship(u1,u2));
        sn.addRelationship(u2,u1, new Relationship(u2,u1));
        sn.addRelationship(u1,u3, new Relationship(u1,u3));
        sn.addRelationship(u1,u4, new Relationship(u1,u4));
        sn.addRelationship(u2,u3, new Relationship(u2,u3));
        sn.addRelationship(u3,u4, new Relationship(u3,u4));
        sn.addRelationship(u2,u5, new Relationship(u2,u5));
        sn.addRelationship(u6,u4, new Relationship(u6,u4));*/


        return sn;
    }
}
