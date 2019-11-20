/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maymweb.producto;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Named;
import javax.persistence.TypedQuery;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Diego
 */
@Named
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ManejadorProducto {
    @PersistenceContext(unitName = "MAYM-Web-Datos")
    private EntityManager em;
    
    public ManejadorProducto(){}
    
    public int CrearProducto(Producto producto){
        try{
            em.persist(producto);
            return producto.getId();
        }catch(Exception ex){
            System.out.println("Error al crear producto: " + ex.getMessage());
        }
        return -1;
    }
    
    public int ActualizarProducto(Producto producto){
        try{
            em.merge(producto);
            return producto.getId();
        }catch(Exception ex){
            System.out.println("Error al actualizar producto: " + ex.getMessage());
        }
        return -1;
    }
    
    public int BorrarProducto(Producto producto){
        try{
            em.remove(producto);
            return producto.getId();
        }catch(Exception ex){
            System.out.println("Error al borrar producto: " + ex.getMessage());
        }
        return -1;
    }
    
    public Producto GetProducto(int IdProducto){
        Producto producto = em.find(Producto.class, IdProducto);
        return producto;
    }
    
    public List<Producto> ListarProductos(){
        List<Producto> productos = new ArrayList<>();
        TypedQuery<Producto> query = em.createQuery("SELECT p FROM Producto p", Producto.class);
        productos = query.getResultList();
        return productos;
    }
}
