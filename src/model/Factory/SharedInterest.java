package model.Factory;

import model.Factory.Relationship;

public class SharedInterest implements Relationship {

    protected String name;
    private int numberInterests;

    public SharedInterest(String name, int number) {
        setRelationName(name);
        setNumberInterests(number);
    }

    public int getNumberInterests() {
        return numberInterests;
    }

    public void setNumberInterests(int numberInterests) {
        this.numberInterests = numberInterests;
    }

    @Override
    public String getRelationName() {
        return this.name;
    }

    @Override
    public void setRelationName(String relationName) {
        this.name = relationName;
    }
}
