package model.Factory;

public class Simple implements Relationship {
    protected String name;

    public Simple(String name){
        setRelationName(name);
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
