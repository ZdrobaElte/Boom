package epuletek;
public abstract class Epulet {
    protected int szint;
    protected int costToUpgrade = 5;

    public int getCostToUpgrade()
    {
        return costToUpgrade;
    }
    public int getSzint()
    {
        return szint;
    }
    public void setSzint(int szint)
    {
        this.szint = szint;
    }
    public void upgrade(){
        szint++;
        costToUpgrade *= 2;
    }
}
