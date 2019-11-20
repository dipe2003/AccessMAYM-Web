/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maymweb.producto;

import com.dperez.maymweb.accion.acciones.Correctiva;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author dperez
 */
@Entity
@Table(name="Productos")
public class Producto implements Serializable, Comparable<Producto>{
    @Id@GeneratedValue(strategy = GenerationType.AUTO)
    private int Id;
    private String Nombre = new String();
    private String Datos = new String();
    
    @ManyToOne
    private Correctiva AccionCorrectivaConProductoAfectado;
    
    // Constructores
    public Producto(){}
    public Producto(String NombreProducto, String DatosProducto){
        this.Nombre = NombreProducto;
        this.Datos = DatosProducto;
    }
    
    // Getters
    public int getId() {return Id;}
    public String getNombre() {return Nombre;}
    public String getDatos() {return Datos;}
    
    public Correctiva getAccionCorrectivaConProductoAfectado() {return AccionCorrectivaConProductoAfectado;}
    
    // Setters
    public void setId(int Id) {this.Id = Id;}
    public void setNombre(String Nombre) {this.Nombre = Nombre;}
    public void setDatos(String Datos) {this.Datos = Datos;}
    
    public void setAccionCorrectivaConProductoAfectado(Correctiva AccionCorrectivaConProductoAfectado) {
        this.AccionCorrectivaConProductoAfectado = AccionCorrectivaConProductoAfectado;
    }

    @Override
    public int compareTo(Producto OtroProducto) {
        return this.Nombre.compareToIgnoreCase(OtroProducto.Nombre);
    }
}
