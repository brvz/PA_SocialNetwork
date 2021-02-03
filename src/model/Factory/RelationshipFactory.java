package model.Factory;

public interface RelationshipFactory {
    Relationship create(String name, int... args);
    void update(Relationship r, int... args);
}
