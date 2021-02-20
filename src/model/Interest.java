package model;

/***
 * Interest class is a class that defines the interest of an user.
 */
public class Interest {

    private int identify;
    private String hashtag;

    public Interest(int identify, String hashtag) {
        setIdentify(identify);
        setHashtag(hashtag);
    }

    /**
     * Return the identify of the interest.
     * @return identify - int
     */
    public int getIdentify() {
        return identify;
    }

    /***
     * Set the identify of the interest.
     * @param identify - int
     */
    public void setIdentify(int identify) {
        this.identify = identify;
    }

    /***
     * Return hashtag/name of the interest.
     * @return hashtag - String
     */
    public String getHashtag() {
        return hashtag;
    }

    /***
     * Set the hashtag/name of the interest.
     * @param hashtag - String
     */
    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }


}
