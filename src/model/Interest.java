package model;

public class Interest {
    private int identify;
    private String hashtag;

    public Interest(int identify, String hashtag) {
        setIdentify(identify);
        setHashtag(hashtag);
    }

    public int getIdentify() {
        return identify;
    }

    public void setIdentify(int identify) {
        this.identify = identify;
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }


}
