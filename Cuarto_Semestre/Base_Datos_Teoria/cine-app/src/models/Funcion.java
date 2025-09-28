package models;
import java.time.LocalDateTime;
public class Funcion {
    private int id;
    private int salaId;
    private int peliculaId;
    private LocalDateTime fechaHora;
    public Funcion(){ }
    public Funcion(int id,int salaId,int peliculaId,LocalDateTime fechaHora){
        this.id=id;this.salaId=salaId;this.peliculaId=peliculaId;this.fechaHora=fechaHora;
    }
    // getters/setters
    public int getId(){return id;}
    public void setId(int id){this.id=id;}
    public int getSalaId(){return salaId;}
    public void setSalaId(int salaId){this.salaId=salaId;}
    public int getPeliculaId(){return peliculaId;}
    public void setPeliculaId(int peliculaId){this.peliculaId=peliculaId;}
    public LocalDateTime getFechaHora(){return fechaHora;}
    public void setFechaHora(LocalDateTime fechaHora){this.fechaHora=fechaHora;}
}
