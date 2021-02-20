package model;

import javax.management.relation.Relation;
import java.util.ArrayList;
import java.util.List;

/**
 * Relationship class represents the connection of two users and that connection can be simple or shared interests
 * accordingly to the type of that relation and their interest in common.
 * The parameters of relationships are: User1 and User2, date, type of relationship mentioned over and the cost.
 */
public class Relationship {

    private User user1, user2;
    private String date;
    private Relationship.NameOfRelationship type;
    private double cost;

    /***
     * Define 2 types of a relationship: Shared Interest and Simple.
     *  SHARED_INTEREST - Interests shared between 2 users in a relationship.
     *  SIMPLE - No interests in common exists.
     */
    public enum NameOfRelationship {
        SHARED_INTEREST,
        SIMPLE;

        /***
         *  Return an option (String) from the switch case that composed by the enum NameOfRelationship.
         * @return NameOfRelationship - String
         */
        public String getType() {
            switch (this) {
                case SHARED_INTEREST:
                    return "sharedInterest";
                case SIMPLE:
                    return "simple";
            }
            return "Desconhecido";
        }
    }


    private List<Interest> interestsInCommon;

    public Relationship(NameOfRelationship nameRel, User u1, User u2, String date) {
        setUser1(u1);
        setUser2(u2);
        setType(nameRel);
        setDate(date);
        this.interestsInCommon = new ArrayList<>();
        this.setInterestsInCommon();
        this.cost = 1.0;
    }

    public Relationship(User u1, User u2, String date) {
        this.type = null;
        setUser1(u1);
        setUser2(u2);
        setDate(date);
        this.interestsInCommon = new ArrayList<>();
        this.setInterestsInCommon();
        this.cost = 1.0;
    }

    /***
     * Return the cost of a relationship.
     * @return cost - Double
     */
    public double getCost() {
        return cost;
    }

    /***
     * Set the type of an relationship.
     * @param type - NameOfRelationship
     */
    public void setType(Relationship.NameOfRelationship type) {
        this.type = type;
    }

    /***
     * Return the a user
     * @return user1 - User
     */
    public User getUser1() {
        return user1;
    }

    /***
     * Return other user.
     * @return user2 - User
     */
    public User getUser2() {
        return user2;
    }

    /***
     * Set a user.
     * @param user1 - User
     */
    public void setUser1(User user1) {
        this.user1 = user1;
    }

    /**
     * Set other user.
     * @param user2 - User
     */
    public void setUser2(User user2) {
        this.user2 = user2;
    }

    /**
     * Return the type of a relationship.
     * @return type - NameOfRelationship
     */
    public NameOfRelationship getType() {
        return this.type;
    }

    /**
     * Return the date.
     * @return date - String
     */
    public String getDate() {
        return date;
    }

    /**
     * Set the date.
     * @param date - String
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Set the Interest in common between 2 users.
     */
    public void setInterestsInCommon() {
        List<Interest> interestA = user1.getInterestList();
        List<Interest> interestB = user2.getInterestList();

        for (int i = 0; i < interestA.size(); i++) {
            for (int j = 0; j < interestB.size(); j++) {
                if (interestA.get(i).getIdentify() == interestB.get(j).getIdentify()) {
                    this.interestsInCommon.add(interestA.get(i));
                }
            }
        }
    }

    /**
     * Return a list of interests in common.
     * @return - interestsInCommon - List
     */
    public List<Interest> getInterestsInCommon() {
        return interestsInCommon;
    }

    /**
     * Verify if exists interests in common between users.
     * @return true if exists - boolean,
     * @return false if doesn't exist - boolean.
     */
    public boolean existsInterestsInCommon() {
        return getInterestsInCommon() != null && getInterestsInCommon().size() > 0;
    }

    /**
     * Set the type of a relationship.
     */
    public void setRelationshipType() {
        if (existsInterestsInCommon()) {
            this.type = NameOfRelationship.SHARED_INTEREST;
        } else {
            this.type = NameOfRelationship.SIMPLE;
        }

    }


    @Override
    public String toString() {
        return "" + getInterestsInCommon().size();
    }

    /**
     * Return a String a hashtag/name of the interest in common.
     * @return res - String
     */
    public String showInterestInCommon() {
        String res = "";
        for (Interest in : interestsInCommon) {
            res += in.getHashtag() + "\n";
        }
        return res;
    }

    /**
     * Return a String a hashtag/name of the interest in common.
     * @return res - String
     */
    public String showInterestInCommonLog() {
        String res = "";
        for (Interest in : interestsInCommon) {
            res += in.getHashtag() + " ";
        }
        return res;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Relationship) {
            Relationship other =  (Relationship)obj;
            return other.user1.equals(user1) && other.user2.equals(user2);
        }
        return super.equals(obj);

    }
}
