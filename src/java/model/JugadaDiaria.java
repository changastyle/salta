package model;

import com.google.gson.annotations.SerializedName;
import java.util.Date;
import java.util.List;

public class JugadaDiaria
{
    @SerializedName("c_juego")
    private int cJuego;
    @SerializedName("t_sorte")
    private String tSorte;
    @SerializedName("f_sorte")
    private String fSorte;
    @SerializedName("n_sorte")
    private int nSorte;
    @SerializedName("d_sorte")
    private String dSorte;
    @SerializedName("d_loter")
    private String dLoteria;
    @SerializedName("extractos")
    private List<Extracto> extractos;
    @SerializedName("horaSorteo")
    private String horaSorteo;
    
    public JugadaDiaria()
    {
        extractos = new java.util.ArrayList<Extracto>();
    }
    public JugadaDiaria(int cJuego, String tSorte, String fSorte, int nSorte, String dSorte, String dLoteria, List<Extracto> extractos, String horaSorteo)
    {
        this.cJuego = cJuego;
        this.tSorte = tSorte;
        this.fSorte = fSorte;
        this.nSorte = nSorte;
        this.dSorte = dSorte;
        this.dLoteria = dLoteria;
        this.extractos = extractos;
        this.horaSorteo = horaSorteo;
    }
    
    //<editor-fold desc="GYS:">
    public int getcJuego()
    {
        return cJuego;
    }

    public void setcJuego(int cJuego)
    {
        this.cJuego = cJuego;
    }

    public String gettSorte()
    {
        return tSorte;
    }

    public void settSorte(String tSorte)
    {
        this.tSorte = tSorte;
    }

    public String getfSorte()
    {
        return fSorte;
    }

    public void setfSorte(String fSorte)
    {
        this.fSorte = fSorte;
    }

    public int getnSorte()
    {
        return nSorte;
    }

    public void setnSorte(int nSorte)
    {
        this.nSorte = nSorte;
    }

    public String getdSorte()
    {
        return dSorte;
    }

    public void setdSorte(String dSorte)
    {
        this.dSorte = dSorte;
    }

    public String getdLoteria()
    {
        return dLoteria;
    }

    public void setdLoteria(String dLoteria)
    {
        this.dLoteria = dLoteria;
    }

    public List<Extracto> getExtractos()
    {
        return extractos;
    }

    public void setExtractos(List<Extracto> extractos)
    {
        this.extractos = extractos;
    }

    public String getHoraSorteo()
    {
        return horaSorteo;
    }

    public void setHoraSorteo(String horaSorteo)
    {
        this.horaSorteo = horaSorteo;
    }
    //</editor-fold
    public String imprimirExtractos()
    {
        String salida = "\n";
        
        for(Extracto extracto : extractos)
        {
            salida += "     " + extracto.toString() + "\n";
        }
        
        return salida;
    }

    @Override
    public String toString()
    {
        String salida = "";
        
        salida += "dSorte       " + dSorte + "\n";
        salida += "dLoteria:    " + dLoteria + "\n";
        salida += "cJuego:      " + cJuego + "\n";
        salida += "tSorte:      " + tSorte + "\n";
        salida += "fSorte:      " + fSorte + "\n";
        salida += "nSorte:      " + nSorte + "\n";
        salida += "horaSorteo:  " + horaSorteo + "\n";
        salida += "extractos:" + imprimirExtractos() + "\n";
        return salida;//, dSorte=" + dSorte + ", dLoteria=" + dLoteria + ", extractos=" + extractos + ", horaSorteo=" + horaSorteo + '}';
    }
}