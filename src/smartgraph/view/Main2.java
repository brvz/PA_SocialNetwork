package smartgraph.view;

import javafx.application.Application;
import model.Relationship;
import model.SocialNetwork;
import model.User;

public class Main2 {

    public static void main(String[] args) {

        SocialNetwork sn = new SocialNetwork("lol");

        User u1 = new User(1);
        User u2 = new User(2);
        User u3 = new User(3);
        User u4 = new User(4);
        User u5 = new User(5);
        User u6 = new User(6);

        sn.addUser(u1);
        sn.addUser(u2);
        sn.addUser(u3);
        sn.addUser(u4);
        sn.addUser(u5);
        sn.addUser(u6);

        sn.addRelationship(u1, u2, new Relationship(u1, u2, ""));
        sn.addRelationship(u2, u1, new Relationship(u2, u1, ""));
        sn.addRelationship(u1, u3, new Relationship(u1, u3, ""));
        sn.addRelationship(u1, u4, new Relationship(u1, u4,""));
        sn.addRelationship(u2, u3, new Relationship(u2, u3,""));
        sn.addRelationship(u3, u4, new Relationship(u3, u4,""));
        sn.addRelationship(u2, u5, new Relationship(u2, u5,""));
        sn.addRelationship(u6, u4, new Relationship(u6, u4,""));

        System.out.println(sn.getSn());
    }
}
