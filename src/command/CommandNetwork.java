package command;

import model.SocialNetwork;

public abstract class CommandNetwork implements Command{
    protected SocialNetwork network;

    public CommandNetwork(SocialNetwork network){
        this.network = network;
    }
}
