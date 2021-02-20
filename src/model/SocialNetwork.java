package model;

import Logger.Logger;
import com.opencsv.CSVReader;
import com.pa.proj2020.adts.graph.*;
import observer.Subject;
import Logger.LoggerProperties;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Integer.*;

/**
 * SocialNetwork class is a class that will have crucial methods that will make the possibility to construct the graph
 * implemented in a specific Social Network and translate that to an User Interface more realistic.
 */

public class SocialNetwork extends Subject {

    private GraphAdjacencyList<User, Relationship> sn;
    private String name;
    private List<Interest> interestList;
    //private Stack<User> lastUserAdded;
    private Stack<List<User>> lastUsers;
    private Stack<List<User>> lastUsersHistory;
    private final static Logger log = new Logger();
    public boolean redo = false;
    public boolean undo = false;

    /*
    CONFIGURATION PROPERTIES
     */
    private LoggerProperties loggerProperties;


    public SocialNetwork(String name) {
        sn = new GraphAdjacencyList<>();
        setName(name);
        //lastUserAdded = new Stack<>();
        lastUsers = new Stack<>();
        lastUsersHistory = new Stack<>();
        CSVReadInterest("input_Files/interests.csv");
        this.loggerProperties = new LoggerProperties();
    }

    /**
     * Set the name of the Social Network.
     * @param name - String
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the name of the Social Network.
     * @return  name - String
     */
    public String getName() {
        return name;
    }

    /**
     * Return the AdjacencyList grapth methods.
     * @return sn - GraphAdjacecyList
     */
    public GraphAdjacencyList<User, Relationship> getSn() {
        return sn;
    }

    /**
     * Returns the Vertex if page exist.
     *
     * @param user - User
     * @return the existing vertex with user
     * @throws SocialNetworkException if user or find doesn't exist in graph.
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
     * Returns the Edge if relationship exist.
     *
     * @param relationship - Relationship
     * @return the existing edge with relationship
     * @throws SocialNetworkException if relationship or find doesn't exist in graph.
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
     * Gets the relationship(edges) between 2 users.
     *
     * @param user1 - User
     * @param user2 - User
     * @return list of edges between user1 and user2.
     * @throws SocialNetworkException when the user doesn't exist.
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
     * Add a new user to model.
     *
     * @param u - User
     * @throws SocialNetworkException if user equals null.
     * @throws InvalidVertexException if user already exists.
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
     * Removes an user from model.
     *
     * @param u - User
     * @throws SocialNetworkException if user equals null.
     * @throws SocialNetworkException if user already exists.
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
     * @param u1 - User
     * @param u2 - User
     * @param r - Relationship
     * @throws SocialNetworkException if relationship equals null.
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
     * Returns incidents relationships as a List.
     *
     * @param u - User
     * @return incidentEdges - List
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
     * Returns the list of users.
     *
     * @return list - List
     */
    public List<User> getUsers() {
        List<User> list = new ArrayList<>();
        for (Vertex<User> vertices : sn.vertices()) {
            list.add(vertices.element());
        }
        return list;
    }

    /**
     * Returns the list of relationships
     *
     * @return list - List
     */
    public List<Relationship> relationships() {
        List<Relationship> list = sn.edges().stream().map(Edge::element).collect(Collectors.toList());
        return list;
    }


    /**
     * Return user with self id.
     * @param userId - int
     * @return v.element() - User
     * @return null if doesn't exists.
     */
    public User getIdOfUser(int userId) {
        for (Vertex<User> v : getSn().vertices()) {
            if (v.element().getNumber() == userId) {
                return v.element();
            }
        }
        return null;
    }

    /**
     * Return a vertex from an User.
     * @param user - User
     * @return v - Vertex
     * @return null if doesn't exists.
     */
    public Vertex<User> getUserVertex(User user) {
        for (Vertex<User> v : getSn().vertices()) {
            if (v.element() == user) {
                return v;
            }
        }
        return null;
    }

    /**
     * Returns an edge of the graph.
     * @param rel - Relationship
     * @return e if exist,
     * @return null if doesn't exist.
     */
    public Edge<Relationship, User> getRelationshipEdge(Relationship rel) {
        for (Edge<Relationship, User> e : getSn().edges()) {
            if (e.element() == rel) {
                return e;
            }
        }
        return null;
    }

    /**
     * Removes an user.
     * @param number - int
     */
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

    /**
     * Count the relationship number of an user.
     * @param id - int
     * @return count - int
     */
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


    @Override
    public String toString() {
        return "SocialNetwork{ name=" + name + " }";
    }

    /**
     * Check the type if an User
     * @param id - int
     * @return the element of an user - User.
     */
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

    /**
     * Verify the id of an user.
     * @param id - int
     * @return true if exist
     * @return false if doesn't exist
     */
    public boolean checkId(int id) {
        return this.getIdOfUser(id) != null;
    }

    /**
     * Reads an csv file to get the relationship data.
     * @param userId - int
     */
    public void readCSVRelationshipsByUser(int userId) {
        List<User> list = new ArrayList<>();
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
                        //if(!isRedo()){
                        list.add(user);
                        //}

                        addIncludedUsers(values, user);
                        logAddUser(user);
                    } else {
                        User user = checkType(userId);
                        /*if(!isRedo()){
                            lastUserAdded.push(user);
                        }*/
                        list.add(user);
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
        getLastUsers().push(list);
        //redo = false;
    }

    public void clearLastUsers() {
        lastUsers.clear();
    }

    /**
     * Reads csv file with relationship data that will generate the batch method.
     * @param userId - List
     */
    public void readCSVBatch(List<Integer> userId) {
        List<User> list = new ArrayList<>();
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
                            //getLastUsers().peek().add(user);
                            list.add(user);
                            addIncludedUsers(values, user);
                            logAddUser(user);
                        } else {
                            User user = checkType(batchUser);
                            //lastUserAdded = null;
                            //getLastUsers().peek().add(user);
                            list.add(user);
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

        getLastUsers().push(list);
        //lastUserAdded = null;
    }

    /**
     * Return lastUsers stack
     * @return lastUsers - Stack
     */
    public Stack<List<User>> getLastUsers() {
        return lastUsers;
    }
    public Stack<List<User>> getLastUsersHistory(){ return lastUsersHistory; }
    public void clearLastUsersHistory(){ lastUsersHistory.clear(); }

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
        //setRedo(false);
    }

    /**
     * Reads the csv files that contain interest data.
     * @param csv - String
     */
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

    /**
     * Returns a list of interests.
     * @return interestList - List
     */
    public List<Interest> getInterestList() {
        return interestList;
    }

    /**
     * Return the name of all interests.
     * @return interestList - List
     */
    public List<String> getNameOfAllInterests() {
        return interestList.stream().map(Interest::getHashtag).collect(Collectors.toList());

    }

    /**
     * Write strings to a file that will contain the data of added users: number of user, date, number of relationships and the
     * number of interests.
     * @param user - User
     */
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

    /**
     * Write strings to a file that will contain the data of added relationships: included users, number of user1,
     * number of user2 and the interests in common of the users.
     * @param rel - Relationship
     */
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

    /**
     * Write a string for undo log.
     */
    public void logUndo() {
        if (loggerProperties.getUndo()) {
            log.writeToFile("undo");
        }
    }

    /**
     * Write a string for redo log.
     */
    public void logRedo() {
        if (loggerProperties.getRedo()) {
            log.writeToFile("redo");
        }
    }


    /**
     * Returns a list with the six users that have the most relationships
     *
     * @return top6 - List
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
     * Returns a list with the six users that have the most relationships
     *
     * @return top10 - List
     */
    public List<User> getTop10With() {
        List<User> all = new ArrayList<>(getUsers());
        List<User> top10 = new ArrayList<>();
        for (int i = 0; i < getUsers().size(); i++) {
            User u = getUserMaxRelsWithSharedInterests(all);
            if (u != null) {
                top10.add(u);
            }
            all.remove(u);
        }
        return top10;
    }


    /**
     * Returns a list with the six users without relationships
     *
     * @return top10 - List.
     */
    public List<User> getTop10Without() {
        List<User> all = new ArrayList<>(getUsers());
        List<User> top10 = new ArrayList<>();
        for (int i = 0; i < getUsers().size(); i++) {
            User u = getUserMaxRelsWithoutSharedInterests(all);
            if (u != null) {
                top10.add(u);
            }
            all.remove(u);
        }
        return top10;
    }


    /**
     * Returns the user with the most shared interests.
     *
     * @param users - List
     * @return finalUser - User
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
     * Returns the user without shared interests.
     *
     * @param users - List
     * @return finalUser - User
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

    /**
     * Return an user with most interests.
     * @param users - List
     * @return finalUser - User
     */
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

    /**
     * Check if two users are adjacent, or, if they have an relationship.
     * @param u - User
     * @param v - User
     * @return true if they have a relationship
     * @return false if they doesn't have a relationship
     */
    public boolean areAdjacentUserType(User u, User v) {
        checkUser(v);
        checkUser(u);
        for (User vertex : getUsers()) {
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

    /**
     * return redo is true
     * @return redo - boolean
     */
    public boolean isRedo() {
        return redo;
    }

    /**
     * Set redo
     * @param redo - boolean
     */
    public void setRedo(boolean redo) {
        this.redo = redo;
    }

    /**
     * return undo is true
     * @return undo - boolean
     */
    public boolean isUndo(){
        return undo;
    }

    /**
     * Set undo
     * @param undo - boolean
     */
    public void setUndo(boolean undo){
        this.undo = undo;
    }


    /**
     * Dijsktra determines the shortest path and distance from the source to all destinations in the graph.
     * @param graph - Graph
     * @param source - User
     * @param query - User
     * @return path - List
     */
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

            Collection<Relationship> edges = new ArrayList<>();

            for(Relationship r : relationships()){
                if(r.getUser1().getNumber() == current.getNumber() || r.getUser2().getNumber() == current.getNumber()){
                    edges.add(r);
                }
            }

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
        while(current != source && current != null) {
            path.add(current);
            current = predecessors.get(current);
        }
        path.add(source);
        Collections.reverse(path);
        System.out.println(path);
        return path;
    }


    /**
     * Find the vertex with lower cost.
     * @param distances - Map
     * @param unvisited - List
     * @return lowerCostVertex - User
     */
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
