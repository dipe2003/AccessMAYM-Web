/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.dperez.maymweb.acciones;

import java.util.List;

import com.dperez.maymweb.producto.Producto;
import static com.dperez.maymweb.acciones.TipoAccion.CORRECTIVA;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *
 * @author dperez
 */
@Entity
public class Correctiva extends Accion implements Serializable {
    private TipoDesvio Tipo;

    @OneToMany(mappedBy = "AccionCorrectivaConProductoAfectado", orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Producto> ProductosAfectados;

    // Constructores
    public Correctiva() {
        this.ProductosAfectados = new ArrayList<>();
    }

    public Correctiva(Date FechaDeteccion, String Descripcion, TipoDesvio TipoDesvio) {
        super(FechaDeteccion, Descripcion, CORRECTIVA);
        this.ProductosAfectados = new ArrayList<>();
        this.Tipo = TipoDesvio;
    }

    //  Getters
    public TipoDesvio getTipo() {
        return this.Tipo;
    }

    public List<Producto> getProductosAfectados() {
        return this.ProductosAfectados;
    }

    public void setTipo(TipoDesvio Tipo) {
        this.Tipo = Tipo;
    }

    public void setProductosAfectados(List<Producto> ProductosAfectados) {
        this.ProductosAfectados = ProductosAfectados;
        for (Producto prod : this.ProductosAfectados) {
            prod.setAccionCorrectivaConProductoAfectado(this);
        }
    }

    // Listas
//Productos Afectados
    public Producto addProductoAfectado(String nombre, String datos) {
        Producto ProductoAfectado = new Producto(nombre, datos);
        this.ProductosAfectados.add(ProductoAfectado);
        ProductoAfectado.setAccionCorrectivaConProductoAfectado(this);
        return ProductoAfectado;
    }

    public void removeProductoAfectado(Producto ProductoAfectado) {
        this.ProductosAfectados.remove(ProductoAfectado);
        if (ProductoAfectado.getAccionCorrectivaConProductoAfectado() != null && !ProductoAfectado.getAccionCorrectivaConProductoAfectado().equals(this)) {
            ProductoAfectado.setAccionCorrectivaConProductoAfectado(null);
        }
    }

    public void removeProductoAfectado(int IdProductoAfectado) {
        Iterator<Producto> it = this.ProductosAfectados.iterator();
        while (it.hasNext()) {
            Producto p = it.next();
            if (p.getId() == IdProductoAfectado) {
                it.remove();
                if (p.getAccionCorrectivaConProductoAfectado() != null && p.getAccionCorrectivaConProductoAfectado().equals(this)) {
                    p.setAccionCorrectivaConProductoAfectado(null);
                }
            }
        }
    }

}
