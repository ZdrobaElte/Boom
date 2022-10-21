import epuletek.Epulet;
import epuletek.Fembanya;
import epuletek.Naperomu;
import urhajok.Urhajo;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Bolygo extends GameElement
{
    protected ArrayList<Urhajo> urhajok = new ArrayList<>();
    protected ArrayList<Epulet> epuletek = new ArrayList<>();
    protected Integer capacity;
    protected Integer temperature;
    protected Integer energy = 0;

    public Bolygo() throws IOException {
        super();
        this.kep = ImageIO.read(new File());
        Random rand = new Random();
        this.capacity = rand.nextInt(20) / 3 + 3;
        this.temperature = rand.nextInt(20) / 3 + 3;
    }

    public BufferedImage getKep()
    {
        return kep;
    }

    public void setColonized(Player player) throws IOException {
        player.addGameElement(this);
        this.kep = ImageIO.read(new File());
    }

    public void addFlotta(Flotta purhajok){
        urhajok.addAll(purhajok.getUrhajok());
    }

    public ArrayList<Urhajo> getUrhajok() {
        return urhajok;
    }

    public void createBuilding(Epulet epulet) {
        epuletek.add(epulet);
        capacity--;
    }

    public void energyProduction(){
        for (Epulet epulet : epuletek){
            if (epulet instanceof Naperomu)
                energy += ((Naperomu) epulet).termeles(temperature);
        }
    }

    public Integer metalProduction(){
        for (Epulet epulet : epuletek){
            if (epulet instanceof Fembanya){
                return ((Fembanya) epulet).termeles(energy);
            }
        }
        return 0;
    }


}
