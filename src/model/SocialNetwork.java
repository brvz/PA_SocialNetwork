package model;

import com.pa.proj2020.adts.graph.Edge;
import com.pa.proj2020.adts.graph.GraphAdjacencyList;
import com.pa.proj2020.adts.graph.InvalidVertexException;
import com.pa.proj2020.adts.graph.Vertex;
import model.Factory.Relationship;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SocialNetwork {

    private GraphAdjacencyList<User, Relationship> sn;
    private String name;


    public SocialNetwork(String name) {
        sn = new GraphAdjacencyList<>();
        setName(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public GraphAdjacencyList<User, Relationship> getSn() {
        return sn;
    }

    /**
     * Returns the Vertex if page exist
     *
     * @param user User
     * @return the existing vertex with user
     * @throws SocialNetworkException if <code>user</code> or
     *                                <code>find</code> doesn't exist in digraph.
     */
    public Vertex<User> checkUser(User user) throws SocialNetworkException {
        if (user == null) {
            throw new SocialNetworkException("Utilizador não pode ser nulo.");
        }

        Vertex<User> find = null;
        for (Vertex<User> v : sn.vertices()) {
            if (v.element().equals(user)) {
                find = v;
            }
        }

        if (find == null) {
            throw new SocialNetworkException("Utilizador com o numero (" + user.getNumber() + ") não existe");
        }

        return find;
    }

    /**
     * Returns the Edge if link exist
     *
     * @param relationship Relationship
     * @return the existing edge with relationship
     * @throws SocialNetworkException if <code>relationship</code> or
     *                                <code>find</code> doesn't exist in digraph.
     */
    public Edge<Relationship, User> checkRelationship(Relationship relationship) throws SocialNetworkException {
        if (relationship == null) {
            throw new SocialNetworkException("Relação não pode ser nula");
        }
        Edge<Relationship, User> find = null;
        for (Edge<Relationship, User> h : sn.edges()) {
            if (h.element().equals(relationship)) {
                find = h;
            }
        }
        if (find == null) {
            throw new SocialNetworkException("Relação (" + relationship.getRelationName() + ") não existe");
        }
        return find;
    }

    /**
     * Add a new user to model
     *
     * @param u User
     * @throws SocialNetworkException if <code>user</code> equals null.
     * @throws InvalidVertexException if <code>user</code> already exists.
     */
    public void addUser (User u) throws SocialNetworkException, InvalidVertexException{
        if(u == null) throw new SocialNetworkException("User does not exists");

        try{
            sn.insertVertex(u);
        }catch (InvalidVertexException e){
            throw new InvalidVertexException("User already exists: " + u.getNumber());
        }
    }

    /**
     * Add a new user to model
     *
     * @param u User
     * @throws SocialNetworkException if <code>user</code> equals null.
     * @throws SocialNetworkException if <code>user</code> already exists.
     */
    public void removeUser(User u) throws SocialNetworkException, InvalidVertexException {
        if (u == null) {
            throw new SocialNetworkException("Utilizador não pode ser nulo");
        }
        try {
            Vertex<User> userVertex = null;
            for (Vertex<User> user :sn.vertices()){
                if (user.element().getNumber() == u.getNumber()){
                    userVertex = user;
                }
            }
            if (userVertex != null)
                sn.removeVertex(userVertex);
        } catch (InvalidVertexException e) {
            throw new InvalidVertexException("Utilizador com o numero (" + u.getNumber() + ") já existe");
        }
    }

    /**
     * Add a new relationship to model
     *
     * @param u1        User
     * @param u2        User
     * @param r Relationship
     * @throws SocialNetworkException if <code>relationship</code> equals null.
     * @throws SocialNetworkException if <code>relationship</code> already exists.
     */
    public void addRelationship(User u1, User u2, Relationship r) throws SocialNetworkException {
        if (r == null) {
            throw new SocialNetworkException("Relação não pode ser nula");
        }

        Vertex<User> p1 = checkUser(u1);
        Vertex<User> p2 = checkUser(u2);

        sn.insertEdge(p1, p2, r);
        System.out.println(r.getRelationName());
    }

    /**
     * Returns a inbound's <i>incident</i> edges as a List.
     * <p>
     * Incident edges are all edges that have vertex <code>inbound</code> as the
     * <i>inbound vertex</i>, i.e., the relationship "entering" User
     * <code>inbound</code>. If there are no incident relationships, e.g., an isolated
     * user, returns an empty collection.
     *
     * @param u User for which to obtain the incident relationship
     * @return collection of relationship
     */
    public List<Relationship> incidentEdges(User u) throws SocialNetworkException {
        checkUser(u);

        List<Relationship> incidentEdges = new ArrayList<>();
        sn.vertices().stream().filter((p) -> (p.element().equals(u))).forEachOrdered((p) -> {
            sn.incidentEdges(p).forEach((h) -> {
                incidentEdges.add(h.element());
            });
        });
        return incidentEdges;
    }

    /**
     * Returns the list of users
     *
     * @return list of users
     */
    public List<User> users() {
        List<User> list = new ArrayList<>();
        for (Vertex<User> vertices : sn.vertices()) {
            list.add(vertices.element());
        }
        return list;
    }

    /**
     * Returns the list of relationships
     *
     * @return list of relationships
     */
    public List<Relationship> relationships() {
        List<Relationship> list = sn.edges().stream().map(Edge::element).collect(Collectors.toList());
        return list;
    }

    public boolean isEmpty() {
        return (this.sn == null);
    }

    @Override
    public String toString() {
        return "SocialNetwork{ name=" + name + " }";
    }
}
