package epuletek;
import epuletek.Epulet;
import urhajok.*;
import java.io.IOException;

enum urhajoEnum
{
    ANYA, VADASZ, CSATA, SZALLITO, CIRKALO, KOLONIA
}

public class Hajogyar extends Epulet
{
    public Urhajo gyart(urhajoEnum pUrhajo) throws IllegalArgumentException, IOException
    {
        switch(pUrhajo)
        {
            case ANYA:
                return new Anya();
            case VADASZ:
                return new Vadasz();
            case KOLONIA:
                return new Kolonia();
            default:
                throw new IllegalArgumentException("Exc occ!");
        }
    }
}
