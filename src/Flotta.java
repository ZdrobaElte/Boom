import urhajok.Urhajo;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Flotta extends GameElement {
    private ArrayList<Urhajo> urhajok = new ArrayList<>();

    public Flotta() throws IOException {
        super();
        this.kep = ImageIO.read(new File());
    }

    public ArrayList<Urhajo> getUrhajok() {
        return urhajok;
    }

    public void addUrhajo(Urhajo urhajo) {
        urhajok.add(urhajo);
    }

    public void mergeFlotta(Flotta flotta) {
        urhajok.addAll(flotta.getUrhajok());
    }

    public Integer getActionPointUsage() {
        if (urhajok.size() <= 5)
            return 1;
        if (urhajok.size() <= 10)
            return 2;
        return 3;
    }

}
