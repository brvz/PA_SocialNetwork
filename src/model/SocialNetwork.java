package model;

import Logger.Logger;
import com.opencsv.CSVReader;
import com.pa.proj2020.adts.graph.Edge;
import com.pa.proj2020.adts.graph.GraphAdjacencyList;
import com.pa.proj2020.adts.graph.InvalidVertexException;
import com.pa.proj2020.adts.graph.Vertex;
import observer.Subject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Integer.*;

public class SocialNetwork extends Subject {

    private GraphAdjacencyList<User, Relationship> sn;
    private String name;
    private List<Interest> interestList;
    private final static Logger log = new Logger();


    public SocialNetwork(String name) {
        sn = new GraphAdjacencyList<>();
        setName(name);
        CSVReadInterest("input_Files/interests.csv");

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
            throw new SocialNetworkException("Relação (" + relationship + ") não existe");
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
        notifyObservers(this);
        logAddUser(u);
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
        notifyObservers(this);
        logAddRelationship(u1, r);
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
    public List<Relationship> incidentRelationships(User u) throws SocialNetworkException {
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

    /**
     * Returns a page's <i>outbound</i> relationship as a List.
     * <p>
     * Incident relationship are all edges that have user <code>outbound</code> as the
     * <i>outbound page</i>, i.e., the relationships "leaving" users
     * <code>outbound</code>. If there are no outbound relationships, e.g., an isolated
     * user, returns an empty collection.
     *
     * @param inbound user for which to obtain the outbound relationships
     * @return collection of edges
     */
    public List<Edge<Relationship, User>> outboundEdges(User inbound) throws SocialNetworkException {
        checkUser(inbound);

        List<Edge<Relationship, User>> outboundEdges = new ArrayList<>();
        sn.vertices().stream().filter((p) -> (p.element().equals(inbound))).forEachOrdered((p) -> {
            sn.outboundEdges(p).forEach((h) -> {
                outboundEdges.add(h);
            });
        });
        return outboundEdges;
    }

    public int getNumberOfUsers() {
        return sn.numVertices();
    }


    public List<User> getUsers() {

        List<User> list = new ArrayList<>();

        for (Vertex<User> u : sn.vertices()) {
            list.add(getIdOfUser(u.element().getNumber()));
        }

        return list;
    }


    public User getIdOfUser(int i) {
        for (Vertex<User> v : getSn().vertices()) {
            if (v.element().getNumber() == i) {
                return v.element();
            }
        }
        return null;
    }


    public User getUser(User user) {
        for (Vertex<User> v : getSn().vertices()) {
            if (v.element() == user) {
                return v.element();
            }
        }
        return null;
    }

    public void removeUserById(int number){
        User toRemove = getIdOfUser(number);
        Collection<Vertex<User>> toRemoveUser = new ArrayList<>();

        for(Vertex<User> v: sn.vertices()){
            if(v.element().getNumber() == toRemove.getNumber()){
                Collection<Edge<Relationship, User>> edges = sn.outboundEdges(v);
                for(Edge e : edges){
                    User u = (User)sn.opposite(v,e).element();
                    if(u.getType()==User.UserType.INCLUDED){
                        toRemoveUser.add(sn.opposite(v, e));
                        //sn.removeVertex(sn.opposite(v, e));
                    }
                    sn.removeEdge(e);
                }
                toRemoveUser.add(v);
                //sn.removeVertex(v);
            }
        }
        for (Vertex<User> userVertex : toRemoveUser) {
            sn.removeVertex(userVertex);
        }
        notifyObservers(this);
    }

    public int countRelations(int id){
        int count = 0;
        User toCount = getIdOfUser(id);
        Vertex v = sn.checkV(toCount);
        List<Edge> edges = (List<Edge>) sn.outboundEdges(v);
        edges.addAll((List<Edge>) sn.incidentEdges(v));
        for(Edge e : edges){
            Relationship r = (Relationship) e.element();
            if(r.getType() == Relationship.NameOfRelationship.SIMPLE || r.getType()== Relationship.NameOfRelationship.SHARED_INTEREST){
                count++;
            }
        }
        return count;
    }

    public boolean isEmpty() {
        return (this.sn == null);
    }

    @Override
    public String toString() {
        return "SocialNetwork{ name=" + name + " }";
    }


    public User checkType(int id) {
        if (checkId(id) && this.getIdOfUser(id).getType() == User.UserType.INCLUDED) {
            this.getIdOfUser(id).setType(User.UserType.ADDED);
            return this.getIdOfUser(id);
        } else if (checkId(id) && this.getIdOfUser(id).getType() == User.UserType.ADDED) {
            return this.getIdOfUser(id);
        }


        User user = new User(id, User.UserType.ADDED);
        this.addUser(user);
        return user;
    }


    public boolean checkId(int id) {
        return this.getIdOfUser(id) != null;
    }


    public void readCSVRelationshipsByUser(int userId) {
        User check = getIdOfUser(userId);
        if(check!=null && check.getType()==User.UserType.ADDED){
            throw new SocialNetworkException("User already added");
        }
        try (BufferedReader br = new BufferedReader(new FileReader("input_Files/relationships.csv"))) {
            String line;
            int count = 0;
            while ((line = br.readLine()) != null && userId > count) {
                count++;
                if (count == userId) {
                    line = line.replace("\uFEFF", "");
                    String[] values = line.split(";");

                    User user = checkType(userId);

                    addIncludedUsers(values, user);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Adds users included by added user and log info
     * @param values included users id's
     * @param user added User
     */
    public void addIncludedUsers(String[] values, User user) {
        for (int i = 2; i < values.length; i++) {
            String valTemp = values[i];
            String dateTemp = values[1];
            int idIncluded = parseInt(valTemp);


            if (!checkId(idIncluded)) {
                User userIncluded = new User(idIncluded, User.UserType.INCLUDED);
                this.addUser(userIncluded);
                Relationship relationship = new Relationship(user,userIncluded, dateTemp);
                relationship.setRelationshipType();
                this.addRelationship(user, userIncluded, relationship);
            } else {
                Relationship relationship = new Relationship(user, getIdOfUser(idIncluded), dateTemp);
                relationship.setRelationshipType();

                this.addRelationship(user, getIdOfUser(idIncluded), relationship);
            }
        }
    }


    public void CSVReadInterest(String csv) {
        CSVReader reader = null;
        interestList = new LinkedList<>();
        int id;
        String name;
        try {
            //parsing a CSV file into CSVReader class constructor
            reader = new CSVReader(new FileReader( csv));
            String[] nextLine;
            //reads one line at a time
            while ((nextLine = reader.readNext()) != null) {
                for (String token : nextLine) {
                    if (!token.isEmpty()) {
                        token = token.replace("﻿", "");
                        String[] parts = token.split(";");
                        String part1 = parts[0];
                        String part2 = parts[1];
                        id = Integer.parseInt(part1);
                        name = part2;
                        interestList.add(new Interest(id, name));
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Interest> getInterestList() {
        return interestList;
    }

    public void addInterestList(String name) {
        for (Interest in:
                getInterestList()) {
            if (!in.getHashtag().equals(name)){
                interestList.add(interestList.size(), new Interest(interestList.size()+1, name));
                break;
            }
        }
    }


public void logAddUser(User user){
    log.writeToFile( "| < " +  user.getNumber() + " > | "
             + user.getDate() + " | "
            + "< " + countRelations(user.getNumber()) + " > | "
            + "< "+ user.getNumberOfInterests() + " > \n");
}

public void logAddRelationship(User user, Relationship rel){
        log.writeToFile("| < " +  user.getNumber() + " > | < "
                + rel.getUser2().getNumber() + " > | "
                + "< " + rel.showInterestInCommon1() + " >  \n");
}

    public void logUndo(){
        log.writeToFile("undo");
    }

    public void logRedo(){
        log.writeToFile("redo");
    }



    /*public int minimumCostPath(int origId,
                               int dstId, List<User> users) throws SocialNetworkException {

        Vertex<User> userOrigin = findUserVertex(origId);
        Vertex<User> userDestin= findUserVertex(dstId);

        if(userOrigin==null) throw new SocialNetworkException("userOrigin does not exist");
        if(userDestin==null) throw new SocialNetworkException("userDestin does not exist");
        if(users == null ) throw new SocialNetworkException("users list reference is null");

        //Create auxiliary structures and run the dijkstra algorithm
        Map<Vertex<User>, Integer> costs = new HashMap<>();
        Map<Vertex<User>, Vertex<User>> predecessors = new HashMap<>();

        dijkstra(userOrigin, costs, predecessors);

        //extract the path, making sure it starts out empty
        users.clear();

        //flag to indicate if path was complete
        boolean complete = true;
        Vertex<User> actual = userDestin;
        while( actual != userOrigin) {
            users.add(0, actual.element());
            actual = predecessors.get(actual);
            if( actual == null) {
                complete = false;
                break;
            }
        }
        users.add(0, userOrigin.element());

        if(!complete) {
            users.clear();
            return -1;
        }

        return costs.get(userDestin);
    }


    private void dijkstra(Vertex<User> orig,
                          Map<Vertex<User>, Integer> costs,
                          Map<Vertex<User>, Vertex<User>> predecessors) {

        costs.clear();
        predecessors.clear();
        List<Vertex<User>> unvisited = new ArrayList<>();

        for (Vertex<User> u : sn.vertices()) {
            unvisited.add(u);

            costs.put(u, Integer.MAX_VALUE);
            predecessors.put(u, null);

        }
        costs.put(orig, 0);
        while(!unvisited.isEmpty()) {
            Vertex<User> lowerCostVertex = findLowerCostVertex(unvisited, costs);
            unvisited.remove(lowerCostVertex);
            List<Edge<Relationship, User>> outboundEdges = new ArrayList<>();
            for (Edge<Relationship, User> outboundEdge : sn.outboundEdges(lowerCostVertex)) {
                if(outboundEdge.element().existsInterestsInCommon()){
                    continue;
                }
                outboundEdges.add(outboundEdge);

            }
            for(Edge<Relationship, User> edge : outboundEdges){
                Vertex<User> opposite = sn.opposite(lowerCostVertex, edge);
                if( unvisited.contains(opposite) ) {
                    int cost = edge.element().getInterestsInCommon().size();
                    int pathCost = costs.get(lowerCostVertex) + cost;
                    if( pathCost < costs.get(opposite) ) {
                        costs.put(opposite, pathCost);
                        predecessors.put(opposite, lowerCostVertex);

                    }
                }
            }
        }

    }

    public Vertex<User> findUserVertex(int number) throws SocialNetworkException{
        if(number <= 0 || number > 50) throw new SocialNetworkException("User doesn't exist");
        Vertex<User> temp = null;
        for (Vertex<User> u: sn.vertices()) {
            if(u.element().getNumber() == number){
                return u;
            }
        }
        return temp;
    }

    private Vertex<User> findLowerCostVertex(List<Vertex<User>> unvisited,
                                             Map<Vertex<User>, Integer> costs) {

        int minValue = Integer.MAX_VALUE;
        Vertex<User> lowerCostVertex = null;

        for (Vertex<User> u : unvisited) {
            int costValue = costs.get(u);
            if( costValue <= minValue ) {
                lowerCostVertex = u;
                minValue = costValue;
            }
        }

        return lowerCostVertex;

    }*/

}
