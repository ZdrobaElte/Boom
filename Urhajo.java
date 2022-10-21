package urhajok;

import java.awt.image.BufferedImage;

enum urhajoEnum
{
    ANYA, VADASZ, CIRKALO, CSATA, KOLONIA, SZALLITO
}

public abstract class Urhajo
{
    int vedekezes;
    int sebesseg;
    int eletero;
    int femkoltseg;
    int fogyasztas;
    Boolean alive = true;

    public int getTamadas() {return tamadas;}

    public int getVedekezes() {
        return vedekezes;
    }

    public int getSebesseg() {
        return sebesseg;
    }

    public int getEletero() {
        return eletero;
    }

    public int getFemkoltseg()
    {
        return femkoltseg;
    }

    public int getFogyasztas()
    {
        return fogyasztas;
    }

    public Boolean isAlive(){
        return alive;
    }
}
