package view.template;

import model.Relationship;
import model.SocialNetwork;
import model.User;

public class TextMostRelationships extends TextTemplate {

    SocialNetwork sn;

    public TextMostRelationships(SocialNetwork sn) {
        this.sn = sn;
    }

    @Override
    public String showTitle() {
        return "Utilizador com mais relações:";
    }

    @Override
    public String showText() {

        int count = 0;
        int idUser = 0;
        for(User u1 : sn.getUsers()){
            if(count < sn.incidentRelationships(u1).size() ){
                idUser = 0;
                idUser = u1.getNumber();
                count = 0;
                count = sn.incidentRelationships(u1).size();
            }
        }
        return "Utilizador " + idUser + " com " + count + " relacionamentos.";
    }
}
