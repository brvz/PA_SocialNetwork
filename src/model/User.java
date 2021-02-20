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

/**
 * User class represents the parameters that an User has. {Type, number and creation date}.
 */
public class User {

    /***
     * Define 2 types of an User: Added and Included.
     *  ADDED - User that is added by the client in User Interface and has some users included.
     *  INCLUDED - User that is returned from an added user by correspondecy.
     */
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


    // Constructor
    public User(int number,  UserType type, String date) {
        setNumber(number);
        setType(type);
        interestList = new LinkedList<>();
        CSVaddUsersInterest(CSVReadInterest());
        setDate(date);
    }

    public User(int number, String date) {
        setNumber(number);
        interestList = new LinkedList<>();
        CSVaddUsersInterest(CSVReadInterest());
        setDate(date);
    }

    // Getters and Setters

    /**
     * Return the number of an User.
     * @return number - int
     */
    public int getNumber() {
        return number;
    }

    /**
     * Set the number of an User.
     * @param  number - int
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * Get the type of an User.
     * @return type - UserType
     */
    public UserType getType() {
        return type;
    }

    /**
     * Set the type of an User.
     * @param  type - UserType
     */
    public void setType(UserType type) {
        this.type = type;
    }

    /**
     * Return the creation date of an User.
     * @return creationDate - User.
     */
    public String getDate(){
        return creationDate;
    }

    /**
     * Set the creation date of an User.
     * @param  date - User.
     */
    public void setDate(String date){
      this.creationDate = date;
    }


    // Equals and HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getNumber() == user.getNumber();
    }

   /* @Override
    public int hashCode() {
        return Objects.hash(""+getNumber());
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }*/

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    // ToString
    @Override
    public String toString() {
        String res = "" + number ;
        return res;
    }

    /**
     * Return a string composed by Id, creation date, and the interests from User.
     * The interests is viewed as well accordingly.
     * @return res - String
     */
    public String showUserToString(){
        String res = "Id: " + number +
                "\nDate: " + creationDate +
                "\nInterests:";
        for(Interest in : interestList){
            res += "\n\t" + in.getHashtag() + ";" ;
        }
        return res;
    }

    /**
     * Return the list of interests.
     * @return interestList - List
     */
    public List<Interest> getInterestList() {
        return interestList;
    }

    /**
     * Add interests in a list of interests.
     * @param interest - Interest
     * @throws InterestException if the interests that we want to add doesn't exists.
     */
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

    /**
     * Return the size of interests.
     * @return size of interestList - int
     */
    public int getNumberOfInterests(){
        return getInterestList().size();
    }

    /**
     * Adds interests from a file and returns interest id's for this user.
     * @param listInterest - List
     */
    public void CSVaddUsersInterest(List<Interest> listInterest) {

        try (BufferedReader br = new BufferedReader(new FileReader("input_Files/interests.csv"))) {
            String line;
            int count = 0;
            while ((line = br.readLine()) != null) {
                count++;
                //if (count == this.getNumber()) {
                    line = line.replace("\uFEFF", "");
                    String[] values = line.split(";");
                    int interestId = Integer.parseInt(values[0]);
                    for (int i = 2; i < values.length; i++) {
                        int x = Integer.parseInt(values[i]);
                        if(x == this.number){
                            for(Interest in : listInterest){
                                if( interestId == in.getIdentify()){
                                    addInterest(in);
                                    break;
                                }
                            }
                        }

                    }

                //}
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads interests from a file and returns interest id's for this user.
     * @return listTemp - List
     */
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