import urhajok.Urhajo;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public abstract class GameElement {
    protected BufferedImage kep;

    public abstract ArrayList<Urhajo> getUrhajok();

    public BufferedImage getKep() {
        return kep;
    }

}
