package model;

import com.google.gson.annotations.SerializedName;

public class Sorteo
{
    @SerializedName("c_sorte")
    private int cSorte;
    @SerializedName("c_juego")
    private int cJuego;
    @SerializedName("fileName")
    private String fileName;
    @SerializedName("f_sorte")
    private double fSorte;
    @SerializedName("t_servj")
    private String servj;

    public Sorteo() {
    }

    public Sorteo(int cSorte, int cJuego, String fileName, double fSorte, String servj) {
        this.cSorte = cSorte;
        this.cJuego = cJuego;
        this.fileName = fileName;
        this.fSorte = fSorte;
        this.servj = servj;
    }

    public int getcSorte() {
        return cSorte;
    }

    public void setcSorte(int cSorte) {
        this.cSorte = cSorte;
    }

    public int getcJuego() {
        return cJuego;
    }

    public void setcJuego(int cJuego) {
        this.cJuego = cJuego;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public double getfSorte() {
        return fSorte;
    }

    public void setfSorte(double fSorte) {
        this.fSorte = fSorte;
    }

    public String getServj() {
        return servj;
    }

    public void setServj(String servj) {
        this.servj = servj;
    }

    @Override
    public String toString() {
        return "Sorteo{" + "cSorte=" + cSorte + ", cJuego=" + cJuego + ", fileName=" + fileName + ", fSorte=" + fSorte + ", servj=" + servj + '}';
    }
    
    
    

}
