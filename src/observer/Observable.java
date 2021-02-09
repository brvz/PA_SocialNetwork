package observer;

public interface Observable {

    /**
     *  Attach  observers to the subject.
     * @param observers  to be attached
     */
    public void addObservers(Observer... observers);
    /**
     *  Attach  observers to the subject.
     * @param observer to be removed
     */
    public void removeObservers(Observer observer);

    /**
     * notify all observer
     * @param object, argument of update method
     */
    public void notifyObservers(Object object);

}
