public interface Movable {
    //Einheitliche Klasse zum Bewegen der Objekte

    public void doLogic(long delta);
    //Logik Ops, zB Kollisionserkennung

    public void move(long delta);
    //eigentliche Bewegung
}
