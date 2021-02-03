package model.Factory;

public class concreteFactory implements RelationshipFactory{

    /**
     * Creates a relationship
     * @param name, args - name of the relation ship and different interests
     * @return Relationship
     */
    @Override
    public Relationship create(String name, int... args) {
        switch(name.toLowerCase()) {
            case "direct":
                return new Simple(name);
            case "shared_interest":
                return new SharedInterest(name,args[0]);
            default:
                throw new UnsupportedOperationException("Name not supported: " + name);
        }
    }

    @Override
    public void update(Relationship r, int... args) {
        switch(r.getRelationName().toLowerCase()) {
            case "direct_shared":
            case "direct":
            case "shared_interest":
            default:
                throw new UnsupportedOperationException("Name not supported: " + r);
        }
    }
}
