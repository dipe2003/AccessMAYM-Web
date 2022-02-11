/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maymweb.modelo.producto;

import com.dperez.maymweb.modelo.acciones.Accion;
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
    private int id;
    private String nombre = new String();
    private String datos = new String();
    
    @ManyToOne
    private Accion accionProducto;
    
    // Constructores
    public Producto(){}
    public Producto(String nombreProducto, String datosProducto, Accion accion){
        this.nombre = nombreProducto;
        this.datos = datosProducto;
        this.accionProducto = accion;
    }

    public Producto(String NombreProductoAfectado, String DatosProductoAfectado) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    // Getters
    public int getId() {return id;}
    public String getNombre() {return nombre;}
    public String getDatos() {return datos;}
    
    public Accion getAccionProducto() {return accionProducto;}
    
    // Setters
    public void setId(int id) {this.id = id;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public void setDatos(String datos) {this.datos = datos;}
    
    public void setAccionProducto(Accion accionProducto) {
        this.accionProducto = accionProducto;
    }

    @Override
    public int compareTo(Producto otroProducto) {
        return this.nombre.compareToIgnoreCase(otroProducto.nombre);
    }
}
