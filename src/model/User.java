package model;

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
    private String name;
    //private List<Interest> interestList;

    // Constructor
    public User(int number, String name, UserType type) {
        setNumber(number);
        setName(name);
        setType(type);
        //interestList = new LinkedList<>();

    }

    public User(int number) {
        setNumber(number);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return "User{" +
                "type=" + type +
                ", number=" + number +
                ", name='" + name + '\'' +
                '}';
    }

    /*public List<Interest> getInterestList() {
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

    public int getNumberofInterests(){
        return interestList.size();
    }
*/


}