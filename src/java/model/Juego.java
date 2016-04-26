package model;

import java.util.List;

public class Juego
{
    private String nombre;
    private List<String> numeros;

    public Juego(String nombre, List<String> numeros)
    {
        this.nombre = nombre;
        this.numeros = numeros;
    }

    public Juego()
    {
        this.numeros = new java.util.ArrayList<String>();
    }

    public String getNombre()
    {
        return nombre;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }

    public List<String> getNumeros()
    {
        return numeros;
    }

    public void setNumeros(List<String> numeros)
    {
        this.numeros = numeros;
    }

    @Override
    public String toString()
    {
        return "Juego{" + "nombre=" + nombre + ", numeros=" + numeros + '}';
    }
    
    public void addNumero(String numero)
    {
        numeros.add(numero);
    }
    public void removeNumero(String numero)
    {
        int contador = 0;
        for(String numeroAUX : numeros)
        {
            if(numeroAUX.equalsIgnoreCase(numero))
            {
                numeros.remove(contador);
            }
            contador++;
        }
    }
    
    
    
    
}
