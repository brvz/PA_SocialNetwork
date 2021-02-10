package model;

import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class User {

    public enum UserType {
        ADDED,
        INCLUDED;

        public String getType() {
            switch (this) {
                case ADDED:
                    return "Adicionado";
                case INCLUDED:
                    return "Incluido";
            }
            return "Desconhecido";
        }
    };

    // variables
    private User.UserType type;
    private int number;

    private List<Interest> interestList;
    private String creationDate;

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    LocalDateTime now = LocalDateTime.now();

    // Constructor
    public User(int number,  UserType type) {
        setNumber(number);
        setType(type);
        interestList = new LinkedList<>();
        CSVaddUsersInterest(CSVReadInterest());
        creationDate = dtf.format(now);
    }

    public User(int number) {
        setNumber(number);
        interestList = new LinkedList<>();
        CSVaddUsersInterest(CSVReadInterest());
        creationDate = dtf.format(now);
    }

    // Getters and Setters
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }


    // Equals and HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getNumber() == user.getNumber();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getType(), getNumber());
    }

    // ToString
    @Override
    public String toString() {
        String res = ", Id: " + number +
                ", Date: " + creationDate +
                ", Interests: ";
        for(Interest in : interestList){
            res += in.getHashtag() + ";" ;
        }
        return res;
    }

    public List<Interest> getInterestList() {
        return interestList;
    }

    public void addInterest(Interest interest) throws InterestException {
        if (interest==null) throw new InterestException("Este interesse não existe");
        for (Interest i:this.interestList) {
            if (!i.equals(interest)){
                this.interestList.add(interest);
                break;
            }
        }
        if (this.interestList.isEmpty()) this.interestList.add(interest);
    }

    public int getNumberOfInterests(){
        return interestList.size();
    }

    /**
     * Reads file and returns interest id's for this user
     * @return
     */
    public void CSVaddUsersInterest(List<Interest> listInterest) {

        try (BufferedReader br = new BufferedReader(new FileReader("input_Files/interests.csv"))) {
            String line;
            int count = 0;
            while ((line = br.readLine()) != null) {
                count++;
                if (count == this.getNumber()) {
                    line = line.replace("\uFEFF", "");
                    String[] values = line.split(";");

                    for (int i = 2; i < values.length; i++) {
                        int x = Integer.parseInt(values[i]);
                        for(Interest in : listInterest){
                            if( x == in.getIdentify()){
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


    public List<Interest> CSVReadInterest() {
        CSVReader reader = null;
        List<Interest> listTemp = new LinkedList<>();
        int id;
        String name;
        try {
            //parsing a CSV file into CSVReader class constructor
            reader = new CSVReader(new FileReader("input_Files/interests.csv"));
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
                        listTemp.add(new Interest(id, name));
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
        return listTemp;
    }


}