package model;

import javax.management.relation.Relation;
import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible for the representation of the Relationship object and every operation regarding it.
 */
public class Relationship {
    private User user1;
    private User user2;
    private String date;
    private List<Interest> interests;
    private RelationshipType type;
    private double cost;

    public enum RelationshipType {
        SIMPLES, PARTILHA;

        public String getType() {
            String str;
            switch (this) {
                case SIMPLES:
                    str = "simples";
                    break;
                case PARTILHA:
                    str = "partilha";
                    break;
                default:
                    str = "n/a";
            }

            return str;
        }
    }

    public Relationship(RelationshipType type, User user1, User user2, String date) {
        this.user1 = user1;
        this.user2 = user2;
        this.type = type;
        this.cost = 1.0;
        this.date = date;
        this.interests = new ArrayList<>();
        this.commonInterests();
    }

    public Relationship(User user1, User user2, String date) {
        this.user1 = user1;
        this.user2 = user2;
        this.type = null;
        this.cost = 1.0;
        this.date = date;
        this.interests = new ArrayList<>();
        this.commonInterests();
    }

    public double getCost() {
        return cost;
    }

    public void setType(RelationshipType type) {
        this.type = type;
    }

    public User getUser1() {
        return user1;
    }

    public User getUser2() {
        return user2;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }

    public RelationshipType getType() {
        return this.type;
    }

    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }

    public void commonInterests() {
        List<Interest> interestA = user1.getInterestList();
        List<Interest> interestB = user2.getInterestList();

        for (int i = 0; i < interestA.size(); i++) {
            for (int j = 0; j < interestB.size(); j++) {
                if (interestA.get(i).getId() == interestB.get(j).getId()) {
                    this.interests.add(interestA.get(i));
                }
            }
        }
    }

    public List<Interest> getInterests() {
        return interests;
    }

    public boolean hasInterests() { return getInterests() != null && getInterests().size() > 0; }

    public void setRelationshipType() {
        if (hasInterests()) {
            this.type = RelationshipType.PARTILHA;
        } else {
            this.type = RelationshipType.SIMPLES;
        }

    }

    @Override
    public String toString() {
        return "" + getInterests().size();
    }

    public String showInterestInCommon() {
        String res = "";
        for (Interest in : interests) {
            res += in.getName() + "\n";
        }
        return res;
    }

    public String showInterestInCommonLog() {
        String res = "";
        for (Interest in : interests) {
            res += in.getName() + " ";
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
