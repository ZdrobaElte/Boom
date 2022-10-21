import java.awt.*;
import java.util.ArrayList;

public class Player {
    protected String name;
    protected Integer actionPoint = 10;
    protected Integer metal = 10;
    protected Integer fuel = 50;

    protected ArrayList<GameElement> ownedGameElements = new ArrayList<GameElement>();

    public Player(String name) {
        this.name = name;
    }

    public void setActionPoint(Integer actionPoint) {
        this.actionPoint = actionPoint;
    }

    public void setMetal(Integer metal) {
        this.metal = metal;
    }

    public void spendMetal(Integer pmetal) {
        this.metal -= pmetal;
    }

    public void useFuel(Integer fuel) {
        this.fuel -= fuel;
    }

    public void useActionPoint(Integer actionPoint){
        this.actionPoint -= actionPoint;
    }

    public Integer getActionPoint() {
        return actionPoint;
    }

    public Integer getMetal() {
        return metal;
    }

    public Integer getFuel() {
        return fuel;
    }

    public void addGameElement(GameElement gameElement){
        ownedGameElements.add(gameElement);
    }

    public void newRound(Integer round){
        for(GameElement gameElement : ownedGameElements){
            if (gameElement instanceof Bolygo){
                ((Bolygo) gameElement).energyProduction();
                metal += ((Bolygo) gameElement).metalProduction();
            }
        }
        this.fuel = 20 + round * 10;
        this.actionPoint = 10 + round * 2;
    }

}
