package model;

import Logger.Logger;
import com.opencsv.CSVReader;
import com.pa.proj2020.adts.graph.*;
import observer.Subject;
import Logger.LoggerProperties;
import smartgraph.view.graphview.SmartGraphVertex;

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
    private User lastUserAdded;
    private List<User> lastUsers;
    private final static Logger log = new Logger();
    public boolean redo = false;

    /*
    CONFIGURATION PROPERTIES
     */
    private LoggerProperties loggerProperties;


    public SocialNetwork(String name) {
        sn = new GraphAdjacencyList<>();
        setName(name);
        lastUserAdded = null;
        lastUsers = new ArrayList<>();
        CSVReadInterest("input_Files/interests.csv");
        this.loggerProperties = new LoggerProperties();
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
     * Gets the relationship(edges) between 2 users
     *
     * @param user1 User
     * @param user2 User
     * @return list of edges between <code>user1</code> and <code>user2</code>
     * @throws SocialNetworkException
     */
    public List<Relationship> getRelationshipBetween(User user1, User user2) throws SocialNetworkException {

        checkUser(user1);
        checkUser(user2);

        List<Relationship> list = new ArrayList<>();

        sn.edges().stream().filter((edge) -> (edge.vertices()[0].element().equals(user1) && edge.vertices()[1].element().equals(user2)
                || edge.vertices()[1].element().equals(user1) && edge.vertices()[0].element().equals(user2))).forEachOrdered((edge) -> {
            list.add(edge.element());
        });

        return list;

    }

    /**
     * Add a new user to model
     *
     * @param u User
     * @throws SocialNetworkException if <code>user</code> equals null.
     * @throws InvalidVertexException if <code>user</code> already exists.
     */
    public void addUser(User u) throws SocialNetworkException, InvalidVertexException {
        if (u == null) throw new SocialNetworkException("User does not exists");

        try {
            sn.insertVertex(u);
        } catch (InvalidVertexException e) {
            throw new InvalidVertexException("User already exists: " + u.getNumber());
        }
        notifyObservers(this);
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
            for (Vertex<User> user : sn.vertices()) {
                if (user.element().getNumber() == u.getNumber()) {
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
     * @param u1 User
     * @param u2 User
     * @param r  Relationship
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
        for (Vertex<User> p : sn.vertices()) {
            if ((p.element().equals(u))) {
                for (Edge<Relationship, User> h : sn.incidentEdges(p)) {
                    incidentEdges.add(h.element());
                }
            }
        }
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

    public Vertex<User> getUserVertex(User user) {
        for (Vertex<User> v : getSn().vertices()) {
            if (v.element() == user) {
                return v;
            }
        }
        return null;
    }

    public Edge<Relationship, User> getRelationshipEdge(Relationship rel) {
        for (Edge<Relationship, User> e : getSn().edges()) {
            if (e.element() == rel) {
                return e;
            }
        }
        return null;
    }

    public void removeUserById(int number) {
        User toRemove = getIdOfUser(number);
        Collection<Vertex<User>> toRemoveUser = new ArrayList<>();

        for (Vertex<User> v : sn.vertices()) {
            if (v.element().getNumber() == toRemove.getNumber()) {
                Collection<Edge<Relationship, User>> edges = sn.outboundEdges(v);
                for (Edge e : edges) {
                    User u = (User) sn.opposite(v, e).element();
                    if (u.getType() == User.UserType.INCLUDED) {
                        int count = 0;
                        for (Relationship r : relationships()) {
                            if (r.getUser1().getNumber() == u.getNumber()) {
                                count++;
                            }
                            if (r.getUser2().getNumber() == u.getNumber()) {
                                count++;
                            }
                        }
                        if (count == 1) {
                            toRemoveUser.add(checkUser(u));
                        }
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

    public int countRelations(int id) {
        int count = 0;
        User toCount = getIdOfUser(id);
        Vertex v = sn.checkV(toCount);
        List<Edge> edges = (List<Edge>) sn.outboundEdges(v);
        edges.addAll((List<Edge>) sn.incidentEdges(v));
        for (Edge e : edges) {
            Relationship r = (Relationship) e.element();
            if (r.getType() == Relationship.NameOfRelationship.SIMPLE || r.getType() == Relationship.NameOfRelationship.SHARED_INTEREST) {
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
            try (BufferedReader br = new BufferedReader(new FileReader("input_Files/relationships.csv"))) {
                String line;
                int count = 0;
                while ((line = br.readLine()) != null && id > count) {
                    count++;
                    if (count == id) {
                        line = line.replace("\uFEFF", "");
                        String[] values = line.split(";");
                        this.getIdOfUser(id).setDate(values[1]);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return this.getIdOfUser(id);
        } else if (checkId(id) && this.getIdOfUser(id).getType() == User.UserType.ADDED) {
            return this.getIdOfUser(id);
        } else {
            return null;
        }

    /*
        User user = new User(id, User.UserType.ADDED,);
        this.addUser(user);
        return user;*/
    }


    public boolean checkId(int id) {
        return this.getIdOfUser(id) != null;
    }


    public void readCSVRelationshipsByUser(int userId) {
        User check = getIdOfUser(userId);
        if (check != null && check.getType() == User.UserType.ADDED) {
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


                    if (checkType(userId) == null) {
                        User user = new User(userId, User.UserType.ADDED, values[1]);
                        this.addUser(user);
                        lastUserAdded = user;
                        addIncludedUsers(values, user);
                        logAddUser(user);
                    } else {
                        User user = checkType(userId);
                        lastUserAdded = user;
                        addIncludedUsers(values, user);

                        for (Edge<Relationship, User> e : sn.incidentEdges(getUserVertex(user))) {
                            Vertex<User> userTemp = sn.opposite(getUserVertex(user), getRelationshipEdge(e.element()));
                            e.element().setDate(values[1]);
                            for (Relationship relationship : getRelationshipBetween(userTemp.element(), user)) {
                                if (!relationship.getDate().equals(user.getDate())) {
                                    relationship.setDate(values[1]);
                                }
                            }
                        }
                        logAddUser(user);
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public User getLastUserAdded() {
        return lastUserAdded;
    }


    public void clearLastUser() {
        lastUserAdded = null;
    }

    public void clearLastUsers() {
        lastUsers.clear();
    }


    public void readCSVBatch(List<Integer> userId) {

        for (int batchUser : userId) {
            User check = getIdOfUser(batchUser);
            if (check != null && check.getType() == User.UserType.ADDED) {
                throw new SocialNetworkException("User already added");
            }
            try (BufferedReader br = new BufferedReader(new FileReader("input_Files/relationships.csv"))) {
                String line;
                int count = 0;
                while ((line = br.readLine()) != null && batchUser > count) {
                    count++;
                    if (count == batchUser) {
                        line = line.replace("\uFEFF", "");
                        String[] values = line.split(";");

                        if (checkType(batchUser) == null) {
                            User user = new User(batchUser, User.UserType.ADDED, values[1]);
                            this.addUser(user);
                            //lastUserAdded = null;
                            getLastUsers().add(user);
                            addIncludedUsers(values, user);
                            logAddUser(user);
                        } else {
                            User user = checkType(batchUser);
                            //lastUserAdded = null;
                            getLastUsers().add(user);
                            addIncludedUsers(values, user);
                            for (Edge<Relationship, User> e : sn.incidentEdges(getUserVertex(user))) {
                                Vertex<User> userTemp = sn.opposite(getUserVertex(user), getRelationshipEdge(e.element()));
                                e.element().setDate(values[1]);
                                for (Relationship relationship : getRelationshipBetween(userTemp.element(), user)) {
                                    if (!relationship.getDate().equals(user.getDate())) {
                                        relationship.setDate(values[1]);
                                    }
                                }
                            }
                            logAddUser(user);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        lastUserAdded = null;
    }

    public List<User> getLastUsers() {
        return lastUsers;
    }

    /**
     * Adds users included by added user and log info
     *
     * @param values included users id's
     * @param user   added User
     */
    public void addIncludedUsers(String[] values, User user) {
        List<Relationship> redoRelationships = new ArrayList<>();
        for (int i = 2; i < values.length; i++) {
            String valTemp = values[i];
            String dateTemp = values[1];
            int idIncluded = parseInt(valTemp);
            Relationship relationship = null;

            if (!checkId(idIncluded)) {
                User userIncluded = new User(idIncluded, User.UserType.INCLUDED, dateTemp);
                this.addUser(userIncluded);
                relationship = new Relationship(user, userIncluded, dateTemp);
                relationship.setRelationshipType();
                this.addRelationship(user, userIncluded, relationship);
                logAddRelationship(relationship);
            } else {
                relationship = new Relationship(user, getIdOfUser(idIncluded), dateTemp);
                Boolean check = false;
                for (Edge<Relationship, User> r : sn.edges()) {
                    if (sn.opposite(checkUser(user), r).element().getNumber() == idIncluded) {
                        check = true;
                        break;
                    }
                }
                if (!check) {
                    relationship.setRelationshipType();
                    this.addRelationship(user, getIdOfUser(idIncluded), relationship);
                    //setRedo(false);
                }else if (isRedo()) {
                    //Relationship relationship = new Relationship(user, getIdOfUser(idIncluded), dateTemp);
                    if (!redoRelationships.contains(relationship)) {
                        relationship.setRelationshipType();
                        this.addRelationship(user, getIdOfUser(idIncluded), relationship);
                        //setRedo(false);
                    }
                }
            }
            redoRelationships.add(relationship);
        }
        setRedo(false);
    }


    public void CSVReadInterest(String csv) {
        CSVReader reader = null;
        interestList = new LinkedList<>();
        int id;
        String name;
        try {
            //parsing a CSV file into CSVReader class constructor
            reader = new CSVReader(new FileReader(csv));
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

    public List<String> getNameOfAllInterests() {
        return interestList.stream().map(Interest::getHashtag).collect(Collectors.toList());

    }

    public void addInterestList(String name) {
        for (Interest in : getInterestList()) {
            if (!in.getHashtag().equals(name)) {
                interestList.add(interestList.size(), new Interest(interestList.size() + 1, name));
                break;
            }
        }
    }


    public void logAddUser(User user) {
        if (loggerProperties.getUserAdded()) {
            if (user.getType().equals(User.UserType.ADDED)) {
                log.writeToFile("| < " + user.getNumber() + " > | "
                        + user.getDate() + " | "
                        + "< " + countRelations(user.getNumber()) + " > | "
                        + "< " + user.getNumberOfInterests() + " > \n");
            }
        }
    }

    public void logAddRelationship(Relationship rel) {
        if (loggerProperties.getRelationAdded()) {
            if (loggerProperties.getUserIncluded() == true) {
                if (rel.getUser2().getType().equals(User.UserType.INCLUDED)) {
                    log.writeToFile("| < " + rel.getUser1().getNumber() + " > | < "
                            + rel.getUser2().getNumber() + " > | "
                            + "< " + rel.showInterestInCommonLog() + " >  \n");
                }
            } else {
                log.writeToFile("| < " + rel.getUser1().getNumber() + " > | <  > | "
                        + "< " + rel.showInterestInCommonLog() + " >  \n");
            }
        }
    }

    public void logUndo() {
        if (loggerProperties.getUndo()) {
            log.writeToFile("undo");
        }
    }

    public void logRedo() {
        if (loggerProperties.getRedo()) {
            log.writeToFile("redo");
        }
    }


    /**
     * Returns a list with the five users that have the most relationships
     *
     * @return top6 - five users with the most direct relationships
     */
    public List<User> getTop6() {
        List<User> all = new ArrayList<>(getUsers());
        List<User> top6 = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            User u = getUsersMostInterests(all);
            top6.add(u);
            all.remove(u);
        }
        return top6;
    }

    /**
     * Returns a list with the five users that have the most relationships
     *
     * @return top10 - five users with the most direct relationships
     */
    public List<User> getTop10With() {
        List<User> all = new ArrayList<>(getUsers());
        List<User> top10 = new ArrayList<>();
        for (int i = 0; i < users().size(); i++) {
            User u = getUserMaxRelsWithSharedInterests(all);
            if (u != null) {
                top10.add(u);
            }
            all.remove(u);
        }
        return top10;
    }


    /**
     * Returns a list with the five users that have the most relationships
     *
     * @return top10 - five users with the most direct relationships
     */
    public List<User> getTop10Without() {
        List<User> all = new ArrayList<>(getUsers());
        List<User> top10 = new ArrayList<>();
        for (int i = 0; i < users().size(); i++) {
            User u = getUserMaxRelsWithoutSharedInterests(all);
            if (u != null) {
                top10.add(u);
            }
            all.remove(u);
        }
        return top10;
    }


    /**
     * Returns the user with the most direct relationships
     *
     * @param users - list of users
     * @return finalUser - user with the most direct relationships
     */
    public User getUserMaxRelsWithSharedInterests(List<User> users) {
        int max = 0;
        User finalUser = null;
        for (User u : users) {
            int cur = 0;
            for (Relationship r : incidentRelationships(u)) {
                if (r.getType().equals(Relationship.NameOfRelationship.SHARED_INTEREST)) {
                    cur++;

                }
            }
            if (cur > max) {
                finalUser = u;
                max = cur;
            }
        }
        return finalUser;
    }


    /**
     * Returns the user with the most direct relationships
     *
     * @param users - list of users
     * @return finalUser - user with the most direct relationships
     */
    public User getUserMaxRelsWithoutSharedInterests(List<User> users) {
        int max = 0;
        User finalUser = null;
        for (User u : users) {
            int cur = 0;
            for (Relationship r : incidentRelationships(u)) {
                if (r.getType().equals(Relationship.NameOfRelationship.SIMPLE)) {
                    cur++;

                }
            }
            if (cur > max) {
                finalUser = u;
                max = cur;
            }
        }
        return finalUser;
    }

    public User getUsersMostInterests(List<User> users) {
        int max = 0;
        User finalUser = null;
        for (User u : users) {
            int cur = u.getNumberOfInterests();
            if (cur > max) {
                finalUser = u;
                max = cur;
            }
        }

        return finalUser;
    }


    public boolean areAdjacentUserType(User u, User v) throws InvalidVertexException {
        checkUser(v);
        checkUser(u);
        for (User vertex : users()) {
            for (Relationship incidentRelationship : incidentRelationships(vertex)) {
                Relationship r = checkRelationship(incidentRelationship).element();
                if (r.getUser1().equals(v) && r.getUser2().equals(u)) {
                    return true;
                }
                if (r.getUser1().equals(u) && r.getUser2().equals(v)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isRedo() {
        return redo;
    }

    public void setRedo(boolean redo) {
        this.redo = redo;
    }





    public List<User> Dijkstra(Graph<User, Relationship> graph, User source,
                                User query) {



        List<User> unvisited = new ArrayList<>();
        //distances : Vertex<String> -> int/double (cost)
        //previous:   Vertex<String> -> Vertex<String>
        Map<User, Double> distances = new HashMap<>();
        Map<User, User> predecessors = new HashMap<>();

        /* inicialização */
        for(Vertex<User> v : graph.vertices()) {
            distances.put(v.element(), Double.MAX_VALUE);
            predecessors.put(v.element(), null);
            unvisited.add(v.element());
        }
        distances.put(source, 0.0);

        while(!unvisited.isEmpty()) {
            User current = findLowerCostVertex(distances, unvisited); //u

            unvisited.remove(current);

            Collection<Relationship> edges = incidentRelationships(current);
            for(Relationship e : edges) {
               User v = graph.opposite(checkUser(current), checkRelationship(e)).element();
                if(/* is unvisited? */ unvisited.contains(v)) {
                    double alt = distances.get(current) + e.getCost();
                    if( alt < distances.get(v)) {
                        distances.put(v, alt);
                        predecessors.put(v, current);
                    }
                }
            }
        }

        System.out.println(distances);
        System.out.println(predecessors);

        System.out.print("Custo de " + source + " -> " + query + "? ");
        System.out.println(distances.get(query));

        System.out.println("Caminho de " + source + " -> " + query + "? ");

        List<User> path = new ArrayList<>();

        User current = query;
        while(current != source) {
            path.add(current);
            current = predecessors.get(current);
        }
        path.add(source);
        Collections.reverse(path);
        System.out.println(path);
        return path;
    }


    private  User findLowerCostVertex(Map<User, Double> distances,
                                                      List<User> unvisited) {
        User lowerCostVertex = null;
        double minCost = Double.MAX_VALUE;

        for(User v : unvisited) {
            double vCost = distances.get(v);
            if(vCost <= minCost) {
                minCost = vCost;
                lowerCostVertex = v;
            }
        }

        return lowerCostVertex;
    }
}
