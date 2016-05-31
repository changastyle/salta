package model;

import java.util.List;

public class Game
{
    private String nombre;
    private List<JugadaDiaria> detalles;

    public Game()
    {
        this.detalles = new java.util.ArrayList<JugadaDiaria>();
    }
    public Game(String nombre, List<JugadaDiaria> detalles)
    {
        this.nombre = nombre;
        this.detalles = detalles;
    }
    
    //<editor-fold desc="GYS:">

    public String getNombre()
    {
        return nombre;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }

    public List<JugadaDiaria> getDetalles()
    {
        return detalles;
    }

    public void setDetalles(List<JugadaDiaria> detalles)
    {
        this.detalles = detalles;
    }
    
    //</editor-fold>

    public void addJugadaDiaria(JugadaDiaria jugadaDiaria)
    {
        if(detalles != null)
        {
            detalles.add(jugadaDiaria);
        }
    }
    public void removeJugadaDiaria(JugadaDiaria jugadaDiaria)
    {
        if(detalles != null)
        {
            detalles.remove(jugadaDiaria);
        }
    }
    @Override
    public String toString()
    {
        return "Game{" + "nombre=" + nombre + ", detalles=" + detalles + '}';
    }
}
