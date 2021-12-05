package model;

import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * User class represents the parameters that an User has. {Type, number and creation date}.
 */
public class User {
    private UserType type;
    private int id;
    private List<Interest> interests;
    private String joinDate;

    public enum UserType {
        ADICIONADO,
        INCLUIDO;

        public String getType() {
            switch (this) {
                case ADICIONADO:
                    return "Adicionado";
                case INCLUIDO:
                    return "Incluido";
            }
            return "Desconhecido";
        }
    };

    public User(int id,  UserType type, String joinDate) {
        this.id = id;
        this.type = type;
        this.joinDate = joinDate;
        interests = new LinkedList<>();
        CSVaddUsersInterest(readCSV());
    }

    public User(int id, String joinDate) {
        this.id = id;
        interests = new LinkedList<>();
        this.joinDate = joinDate;
        CSVaddUsersInterest(readCSV());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public String getJoinDate(){
        return joinDate;
    }

    public void setJoinDate(String joinDate){
      this.joinDate = joinDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getId() == user.getId();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        String res = "" + id ;
        return res;
    }

    public String showUserToString(){
        String res = "Id: " + id +
                "\nDate: " + joinDate +
                "\nInterests:";
        for(Interest in : interests){
            res += "\n\t" + in.getName() + ";" ;
        }
        return res;
    }

    public List<Interest> getInterestList() {
        return interests;
    }

    public void addInterest(Interest interest) throws DefaultException {
        if (interest == null) throw new DefaultException("This interest does not exist.");
        for (Interest i : interests) {
            if (!i.equals(interest)){
                interests.add(interest);
                break;
            }
        }
        if (interests.isEmpty())
            interests.add(interest);
    }

    public int getNumberOfInterests(){
        return getInterestList().size();
    }

    public void CSVaddUsersInterest(List<Interest> listInterest) {
        try (BufferedReader br = new BufferedReader(new FileReader("input_Files/interests.csv"))) {
            String readLine;
            int count = 0;
            while ((readLine = br.readLine()) != null) {
                count++;
                readLine = readLine.replace("\uFEFF", "");
                String[] values = readLine.split(";");
                int interestId = Integer.parseInt(values[0]);
                for (int i = 2; i < values.length; i++) {
                    int x = Integer.parseInt(values[i]);
                    if(x == this.id){
                        for(Interest in : listInterest){
                            if(interestId == in.getId()){
                                addInterest(in);
                                break;
                            }
                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Interest> readCSV() {
        CSVReader reader = null;
        List<Interest> interestList = new LinkedList<>();
        int id;
        String name;
        try {
            reader = new CSVReader(new FileReader("input_Files/interests.csv"));
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
        return interestList;
    }


}