package model;

import java.util.ArrayList;
import java.util.List;

public class Relationship {

    private User user1, user2;
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

    public Relationship(NameOfRelationship nameRel, User u1, User u2) {
        this.user1 = u1;
        this.user2 = u2;
        setType(nameRel);
        this.interestsInCommon = new ArrayList<>();
        this.setInterestsInCommon();
    }

    public Relationship(User u1, User u2){

        this.type = null;
        this.user1 = u1;
        this.user2 = u2;
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

    public NameOfRelationship getType(){
        return this.type;
    }

    public void setInterestsInCommon(){
        List<Interest> interestA =  user1.getInterestList();
        List<Interest> interestB =  user2.getInterestList();

        for (int i = 0; i < interestA.size(); i++) {
            for (int j = 0; j < interestB.size(); j++) {
                if (interestA.get(i).equals(interestB.get(j))) {
                    this.interestsInCommon.add(interestA.get(i));
                }
            }
        }
    }

    private List<Interest> getInterestsInCommon(){
        return interestsInCommon;
    }

    private boolean existsInterestsInCommon(){
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
        return "Relationship{" +
                "Relationship type =" + this.type + ", Between users: " + getUser1().getNumber()
                +" and " + getUser2().getNumber()+
                ", " + "Common interests: " + getInterestsInCommon().size() +
                '}';
    }

}
