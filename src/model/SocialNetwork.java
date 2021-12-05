package model;

import com.opencsv.CSVReader;
import com.pa.proj2020.adts.graph.*;
import observer.Subject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Integer.*;

/**
 * Class responsible for the main methods to solve the problem. This class is also the Model in the MVC pattern,
 * allowing the graph to be constructed and the user interface to have the right functionalities.
 */

public class SocialNetwork extends Subject {

    private GraphAdjacencyList<User, Relationship> network;
    private String name;
    private List<Interest> interestList;
    private Stack<List<User>> lastUsers;
    private Stack<List<User>> lastUsersHistory;
    private final static Logger log = new Logger();
    public boolean redo = false;
    public boolean undo = false;

    private LoggerProperties loggerProperties;


    public SocialNetwork(String name) {
        network = new GraphAdjacencyList<>();
        this.name = name;
        lastUsers = new Stack<>();
        lastUsersHistory = new Stack<>();
        CSVReadInterest("input_Files/interests.csv");
        this.loggerProperties = new LoggerProperties();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public GraphAdjacencyList<User, Relationship> getSocialNetwork() {
        return network;
    }

    public Vertex<User> checkUser(User user) throws DefaultException {
        if (user == null) {
            throw new DefaultException("User cannot be null.");
        }

        Vertex<User> find = null;
        for (Vertex<User> v : network.vertices()) {
            if (v.element().equals(user)) {
                find = v;
            }
        }

        if (find == null) {
            throw new DefaultException("Utilizador com o numero (" + user.getId() + ") não existe");
        }

        return find;
    }

    public Edge<Relationship, User> checkRelationship(Relationship relationship) throws DefaultException {
        if (relationship == null)
            throw new DefaultException("A relationship cannot be null");

        Edge<Relationship, User> find = null;
        for (Edge<Relationship, User> e : network.edges()) {
            if (e.element().equals(relationship))
                find = e;
        }
        if (find == null)
            throw new DefaultException("Relationship (" + relationship + ") does not exist");

        return find;
    }

    public List<Relationship> getRelationshipBetween(User user1, User user2) throws DefaultException {

        checkUser(user1);
        checkUser(user2);

        List<Relationship> list = new ArrayList<>();

        network.edges().stream().filter((edge) -> (edge.vertices()[0].element().equals(user1) && edge.vertices()[1].element().equals(user2)
                || edge.vertices()[1].element().equals(user1) && edge.vertices()[0].element().equals(user2))).forEachOrdered((edge) -> {
            list.add(edge.element());
        });

        return list;

    }

    public void addUser(User u) throws DefaultException, InvalidVertexException {
        if (u == null) throw new DefaultException("User does not exists");

        try {
            network.insertVertex(u);
        } catch (InvalidVertexException e) {
            throw new InvalidVertexException("User already exists: " + u.getId());
        }

        notifyObservers(this);
    }

    public void removeUser(User u) throws DefaultException, InvalidVertexException {
        if (u == null) {
            throw new DefaultException("This user cannot be null");
        }
        try {
            Vertex<User> userVertex = null;
            for (Vertex<User> user : network.vertices()) {
                if (user.element().getId() == u.getId())
                    userVertex = user;
            }
            if (userVertex != null)
                network.removeVertex(userVertex);
        } catch (InvalidVertexException e) {
            throw new InvalidVertexException("Utilizador com o numero (" + u.getId() + ") já existe");
        }
    }

    public void addRelationship(User u1, User u2, Relationship r) throws DefaultException {
        if (r == null) throw new DefaultException("This relationship cannot be null");

        Vertex<User> p1 = checkUser(u1);
        Vertex<User> p2 = checkUser(u2);

        network.insertEdge(p1, p2, r);
        notifyObservers(this);
    }

    public List<Relationship> incidentRelationships(User user) throws DefaultException {
        checkUser(user);
        List<Relationship> incidentEdges = new ArrayList<>();
        for (Vertex<User> u : network.vertices()) {
            if ((u.element().equals(user))) {
                for (Edge<Relationship, User> e : network.incidentEdges(u)) {
                    incidentEdges.add(e.element());
                }
            }
        }

        return incidentEdges;
    }

    public List<User> getUsers() {
        List<User> list = new ArrayList<>();
        for (Vertex<User> vertices : network.vertices()) {
            list.add(vertices.element());
        }

        return list;
    }

    public List<Relationship> relationships() {
        List<Relationship> list = network.edges().stream().map(Edge::element).collect(Collectors.toList());
        return list;
    }

    public User getIdOfUser(int userId) {
        for (Vertex<User> v : getSocialNetwork().vertices()) {
            if (v.element().getId() == userId) {
                return v.element();
            }
        }

        return null;
    }

    public Vertex<User> getUserVertex(User user) {
        for (Vertex<User> v : getSocialNetwork().vertices()) {
            if (v.element() == user) {
                return v;
            }
        }

        return null;
    }

    public Edge<Relationship, User> getRelationshipEdge(Relationship relationship) {
        for (Edge<Relationship, User> e : getSocialNetwork().edges()) {
            if (e.element() == relationship) {
                return e;
            }
        }

        return null;
    }

    public void removeUserById(int number) {
        User toRemove = getIdOfUser(number);
        Collection<Vertex<User>> toRemoveUser = new ArrayList<>();
        for (Vertex<User> v : network.vertices()) {
            if (v.element().getId() == toRemove.getId()) {
                Collection<Edge<Relationship, User>> edges = network.outboundEdges(v);
                for (Edge e : edges) {
                    User u = (User) network.opposite(v, e).element();
                    if (u.getType() == User.UserType.INCLUIDO) {
                        int count = 0;
                        for (Relationship r : relationships()) {
                            if (r.getUser1().getId() == u.getId())
                                count++;
                            if (r.getUser2().getId() == u.getId()) {
                                count++;
                            }
                        }
                        if (count == 1) {
                            toRemoveUser.add(checkUser(u));
                        }
                    }
                    network.removeEdge(e);
                }
                toRemoveUser.add(v);
            }
        }

        for (Vertex<User> userVertex : toRemoveUser) {
            network.removeVertex(userVertex);
        }

        notifyObservers(this);
    }

    public int countRelations(int id) {
        int count = 0;
        User toCount = getIdOfUser(id);
        Vertex v = network.checkV(toCount);
        List<Edge> edges = (List<Edge>) network.outboundEdges(v);
        edges.addAll((List<Edge>) network.incidentEdges(v));
        for (Edge e : edges) {
            Relationship r = (Relationship) e.element();
            if (r.getType() == Relationship.RelationshipType.SIMPLES || r.getType() == Relationship.RelationshipType.PARTILHA)
                count++;
        }
        return count;
    }


    @Override
    public String toString() {
        return "SocialNetwork (name = " + name + ")";
    }

    public User checkType(int id) {
        if (checkId(id) && getIdOfUser(id).getType() == User.UserType.INCLUIDO) {
            getIdOfUser(id).setType(User.UserType.ADICIONADO);
            try (BufferedReader br = new BufferedReader(new FileReader("input_Files/relationships.csv"))) {
                String readLine;
                int count = 0;
                while ((readLine = br.readLine()) != null && id > count) {
                    count++;
                    if (count == id) {
                        readLine = readLine.replace("\uFEFF", "");
                        String[] values = readLine.split(";");
                        getIdOfUser(id).setJoinDate(values[1]);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return getIdOfUser(id);

        } else if (checkId(id) && this.getIdOfUser(id).getType() == User.UserType.ADICIONADO)
            return getIdOfUser(id);
        else
            return null;
    }

    public boolean checkId(int id) {
        return getIdOfUser(id) != null;
    }

    public void readCSVRelationshipsByUser(int userId) {
        List<User> list = new ArrayList<>();
        User check = getIdOfUser(userId);
        if (check != null && check.getType() == User.UserType.ADICIONADO)
            throw new DefaultException("User already added");

        try (BufferedReader br = new BufferedReader(new FileReader("input_Files/relationships.csv"))) {
            String readLine;
            int count = 0;
            while ((readLine = br.readLine()) != null && userId > count) {
                count++;
                if (count == userId) {
                    readLine = readLine.replace("\uFEFF", "");
                    String[] values = readLine.split(";");
                    if (checkType(userId) == null) {
                        User user = new User(userId, User.UserType.ADICIONADO, values[1]);
                        this.addUser(user);
                        list.add(user);
                        addIncludedUsers(values, user);
                        logAddUser(user);
                    } else {
                        User user = checkType(userId);
                        list.add(user);
                        addIncludedUsers(values, user);

                        for (Edge<Relationship, User> e : network.incidentEdges(getUserVertex(user))) {
                            Vertex<User> userTemp = network.opposite(getUserVertex(user), getRelationshipEdge(e.element()));
                            e.element().setDate(values[1]);
                            for (Relationship relationship : getRelationshipBetween(userTemp.element(), user)) {
                                if (!relationship.getDate().equals(user.getJoinDate())) {
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
    }

    public void clearLastUsers() {
        lastUsers.clear();
    }

    public void readCSVBatch(List<Integer> userId) {
        List<User> list = new ArrayList<>();
        for (int batchUser : userId) {
            User check = getIdOfUser(batchUser);
            if (check != null && check.getType() == User.UserType.ADICIONADO) {
                throw new DefaultException("User already added");
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
                            User user = new User(batchUser, User.UserType.ADICIONADO, values[1]);
                            this.addUser(user);
                            list.add(user);
                            addIncludedUsers(values, user);
                            logAddUser(user);
                        } else {
                            User user = checkType(batchUser);
                            list.add(user);
                            addIncludedUsers(values, user);
                            for (Edge<Relationship, User> e : network.incidentEdges(getUserVertex(user))) {
                                Vertex<User> userTemp = network.opposite(getUserVertex(user), getRelationshipEdge(e.element()));
                                e.element().setDate(values[1]);
                                for (Relationship relationship : getRelationshipBetween(userTemp.element(), user)) {
                                    if (!relationship.getDate().equals(user.getJoinDate())) {
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
    }

    public Stack<List<User>> getLastUsers() { return lastUsers; }

    public Stack<List<User>> getLastUsersHistory() { return lastUsersHistory; }

    public void clearLastUsersHistory() { lastUsersHistory.clear(); }

    public void addIncludedUsers(String[] values, User user) {
        List<Relationship> redoRelationships = new ArrayList<>();
        for (int i = 2; i < values.length; i++) {
            String valTemp = values[i];
            String dateTemp = values[1];
            int idIncluded = parseInt(valTemp);
            Relationship relationship = null;
            if (!checkId(idIncluded)) {
                User userIncluded = new User(idIncluded, User.UserType.INCLUIDO, dateTemp);
                addUser(userIncluded);
                relationship = new Relationship(user, userIncluded, dateTemp);
                relationship.setRelationshipType();
                addRelationship(user, userIncluded, relationship);
                logAddRelationship(relationship);
            } else {
                relationship = new Relationship(user, getIdOfUser(idIncluded), dateTemp);
                Boolean check = false;
                for (Edge<Relationship, User> e : network.edges()) {
                    if (network.opposite(checkUser(user), e).element().getId() == idIncluded) {
                        check = true;
                        break;
                    }
                }
                if (!check) {
                    relationship.setRelationshipType();
                    addRelationship(user, getIdOfUser(idIncluded), relationship);
                }else if (isRedo()) {
                    if (!redoRelationships.contains(relationship)) {
                        relationship.setRelationshipType();
                        addRelationship(user, getIdOfUser(idIncluded), relationship);
                    }
                }
            }

            redoRelationships.add(relationship);
        }
    }

    public void CSVReadInterest(String csv) {
        CSVReader reader = null;
        interestList = new LinkedList<>();
        int id;
        String name;
        try {
            reader = new CSVReader(new FileReader(csv));
            String[] nextLine;
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
        return interestList.stream().map(Interest::getName).collect(Collectors.toList());
    }

    public void logAddUser(User user) {
        if (loggerProperties.getUserAdded()) {
            if (user.getType().equals(User.UserType.ADICIONADO)) {
                log.writeToFile("| < " + user.getId() + " > | "
                        + user.getJoinDate() + " | "
                        + "< " + countRelations(user.getId()) + " > | "
                        + "< " + user.getNumberOfInterests() + " > \n");
            }
        }
    }

    public void logAddRelationship(Relationship rel) {
        if (loggerProperties.getRelationAdded()) {
            if (loggerProperties.getUserIncluded() == true) {
                if (rel.getUser2().getType().equals(User.UserType.INCLUIDO)) {
                    log.writeToFile("| < " + rel.getUser1().getId() + " > | < "
                            + rel.getUser2().getId() + " > | "
                            + "< " + rel.showInterestInCommonLog() + " >  \n");
                }
            } else {
                log.writeToFile("| < " + rel.getUser1().getId() + " > | <  > | "
                        + "< " + rel.showInterestInCommonLog() + " >  \n");
            }
        }
    }

    public void logUndo() {
        if (loggerProperties.getUndo()) {
            log.writeToFile("undo");
        }
    }

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

    public boolean isRedo() {
        return redo;
    }

    public void setRedo(boolean redo) {
        this.redo = redo;
    }

    public boolean isUndo(){
        return undo;
    }

    public void setUndo(boolean undo){
        this.undo = undo;
    }

}
