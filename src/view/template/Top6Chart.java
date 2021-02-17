package view.template;

import com.pa.proj2020.adts.graph.Edge;
import com.pa.proj2020.adts.graph.Vertex;
import javafx.scene.chart.XYChart;
import model.Interest;
import model.Relationship;
import model.SocialNetwork;
import model.User;

import java.util.ArrayList;
import java.util.List;

public class Top6Chart extends ChartsTemplate {
    SocialNetwork sn;
    private List<User> top6;

    public Top6Chart(SocialNetwork sn) {
        this.sn = sn;
        this.top6 = sn.getTop6();
    }

    @Override
    public String getTitle() {
        return "Top 6";
    }

    @Override
    public void addData(XYChart.Series series1) {
        List<User> list = top6;
        for(int i = 0; i < 6; i++){
            series1.getData().add(new XYChart.Data(String.valueOf(list.get(i).getNumber()),interest(list.get(i))));
        }
    }

    public int interest(User u){
        int count = 0;
        if(u.getType().equals(User.UserType.ADDED)){
            for (Relationship r : sn.incidentRelationships(u)) {
                if(u.getNumber() == r.getUser1().getNumber()) {
                    count++;
                }
                else if(u.getNumber() == r.getUser2().getNumber()) {
                    count++;
                }
            }
            for(User users : sn.users()){
                for(Relationship rel : sn.incidentRelationships(users)) {
                    if(u.getNumber() != users.getNumber()){
                        if(u.getNumber() == rel.getUser1().getNumber()) {
                            count++;
                        }
                        else if(u.getNumber() == rel.getUser2().getNumber()) {
                            count++;
                        }
                    }
                }
            }
        }

        if(u.getType().equals(User.UserType.INCLUDED)){
            for(User user : sn.users()){
                for (Relationship r : sn.incidentRelationships(user)) {
                    Vertex<User> userVertex = sn.checkUser(user);
                    Edge<Relationship, User> relationshipUserEdge = sn.checkRelationship(r);
                    if(u.getNumber() == sn.getSn().opposite(userVertex,relationshipUserEdge).element().getNumber()){
                        count++;
                        break;
                    }
                }
            }
        }
        return count;
    }
}
