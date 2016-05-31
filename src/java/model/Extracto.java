package model;

public class Extracto
{
    private String posic;
    private String premio;

    public Extracto()
    {
    }
    public Extracto(String posic, String premio)
    {
        this.posic = posic;
        this.premio = premio;
    }
    
    //<editor-fold desc="GYS:">
    public String getPosic()
    {
        return posic;
    }

    public void setPosic(String posic)
    {
        this.posic = posic;
    }

    public String getPremio()
    {
        return premio;
    }

    public void setPremio(String premio)
    {
        this.premio = premio;
    }
    //</editor-fold>
    
    @Override
    public String toString()
    {
        return "Extracto{" + "posic=" + posic + ", premio=" + premio + '}';
    }
}
