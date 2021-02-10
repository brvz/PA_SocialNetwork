package command;

import model.SocialNetwork;

public abstract class CommandSocialNetwork implements Command{
    protected SocialNetwork sn;

    public CommandSocialNetwork(SocialNetwork sn){
        this.sn = sn;
    }
}
