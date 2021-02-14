package view.template;

import model.Relationship;
import model.SocialNetwork;
import model.User;

public class TextAddedUsers extends TextTemplate {
    SocialNetwork sn;

    public TextAddedUsers(SocialNetwork sn) {
        this.sn = sn;
    }

    @Override
    public String showTitle() {
        return "Utilizadores Adicionados:";
    }

    @Override
    public String showText() {
        String txt = "";
        for(User u : sn.getUsers()){
            if(u.getType().equals(User.UserType.ADDED)){
                txt += "Utilizador: " + u.getNumber() + "\n -> { ";
                for(Relationship r : sn.incidentRelationships(u)){
                    if(r.getUser2().getNumber() == u.getNumber()){
                        txt += r.getUser1().getNumber() + ";";
                    }
                    if(r.getUser1().getNumber() == u.getNumber()){
                        txt += r.getUser2().getNumber() + ";";
                    }
                }
                txt += " } \n ------------------------------------------------------------------ \n";
            }
        }
        return txt;
    }
}
