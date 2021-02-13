package model;

import java.util.ArrayList;
import java.util.List;

public class Relationship {

    private User user1, user2;
    private String date;
    private Relationship.NameOfRelationship type;

    public enum NameOfRelationship {
        SHARED_INTEREST,
        SIMPLE;

        public String getType() {
            switch (this) {
                case SHARED_INTEREST:
                    return "sharedInterest";
                case SIMPLE:
                    return "simple";
            }
            return "Desconhecido";
        }
    };

    private List<Interest> interestsInCommon;

    public Relationship(NameOfRelationship nameRel, User u1, User u2, String date) {
        setUser1(u1);
        setUser2(u2);
        setType(nameRel);
        setDate(date);
        this.interestsInCommon = new ArrayList<>();
        this.setInterestsInCommon();
    }

    public Relationship(User u1, User u2, String date){
        this.type = null;
        setUser1(u1);
        setUser2(u2);
        setDate(date);
        this.interestsInCommon = new ArrayList<>();
        this.setInterestsInCommon();
    }

    public void setType(Relationship.NameOfRelationship type) {
        this.type = type;
    }

    public User getUser1(){
        return user1;
    }

    public User getUser2(){
        return user2;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }

    public NameOfRelationship getType(){
        return this.type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setInterestsInCommon(){
        List<Interest> interestA =  user1.getInterestList();
        List<Interest> interestB =  user2.getInterestList();

        for (int i = 0; i < interestA.size(); i++) {
            for (int j = 0; j < interestB.size(); j++) {
                if (interestA.get(i).getIdentify() == interestB.get(j).getIdentify()) {
                    this.interestsInCommon.add(interestA.get(i));
                }
            }
        }
    }

    public List<Interest> getInterestsInCommon(){
        return interestsInCommon;
    }

    public boolean existsInterestsInCommon(){
        return getInterestsInCommon() != null && getInterestsInCommon().size() > 0;
    }

    public void setRelationshipType(){
        if(existsInterestsInCommon()){
            this.type= NameOfRelationship.SHARED_INTEREST;
        }
        else{
            this.type= NameOfRelationship.SIMPLE;
        }

    }

    @Override
    public String toString() {
        return "" + getInterestsInCommon().size() ;
    }

    public String showInterestInCommon(){
        String res = "";
        for(Interest in : interestsInCommon){
            res += in.getHashtag() + "\n";
        }
        return res;
    }

    public String showInterestInCommonLog(){
        String res = "";
        for(Interest in : interestsInCommon){
            res += in.getHashtag() + " ";
        }
        return res;
    }


}
