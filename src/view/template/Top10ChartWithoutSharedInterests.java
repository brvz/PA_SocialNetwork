package view.template;

import com.pa.proj2020.adts.graph.Edge;
import com.pa.proj2020.adts.graph.Vertex;
import javafx.scene.chart.XYChart;
import model.Relationship;
import model.SocialNetwork;
import model.User;

import java.util.List;

public class Top10ChartWithoutSharedInterests extends ChartsTemplate {


    SocialNetwork sn;
    private List<User> top10;

    public Top10ChartWithoutSharedInterests(SocialNetwork sn) {
        this.sn = sn;
        this.top10 =  sn.getTop10Without();
    }

    @Override
    public String getTitle() {
        return "Top 10 without Shared Relationships";
    }

    @Override
    public void addData(XYChart.Series series1) {
        List<User> list = top10;
        int aux = 0;
        for(int i = 0; i < list.size(); i++){
            if(aux < 10){
                series1.getData().add(new XYChart.Data(String.valueOf(list.get(i).getNumber()), relationships(list.get(i))));
                aux++;
            }


        }
    }


    public int relationships(User u){
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
