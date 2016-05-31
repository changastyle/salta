package model;
public class Instalaciones {
    
     private long id;
     private String nombre;
     private String descripcion;

    public Instalaciones() {
    }

	
    public Instalaciones(long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
    public Instalaciones(long id, String nombre, String descripcion) {
       this.id = id;
       this.nombre = nombre;
       this.descripcion = descripcion;
    }
   
    public long getId() {
        return this.id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    public String getNombre() {
        return this.nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getDescripcion() {
        return this.descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString()
    {
        return "Instalaciones{" + "id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + '}';
    }

    
}
