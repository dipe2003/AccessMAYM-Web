/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maymweb.deteccion;

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
public class ManejadorDeteccion {
    @PersistenceContext(unitName = "MAYM-Web-Datos")
    private EntityManager em;
    
    public ManejadorDeteccion(){}
    
    public int CrearDeteccion(Deteccion deteccion){
        try{
            em.persist(deteccion);
            return deteccion.getId();
        }catch(Exception ex){
            System.out.println("Error al crear deteccion: " + ex.getMessage());
        }
        return -1;
    }
    
    public int ActualizarDeteccion(Deteccion deteccion){
        try{
            em.merge(deteccion);
            return deteccion.getId();
        }catch(Exception ex){
            System.out.println("Error al actualizar deteccion: " + ex.getMessage());
        }
        return -1;
    }
    
    public int BorrarDeteccion(Deteccion deteccion){
        try{
            em.remove(deteccion);
            return deteccion.getId();
        }catch(Exception ex){
            System.out.println("Error al borrar deteccion: " + ex.getMessage());
        }
        return -1;
    }
    
    public Deteccion GetDeteccion(int IdDeteccion){
        Deteccion deteccion = em.find(Deteccion.class, IdDeteccion);
        return deteccion;
    }
    
    public List<Deteccion> ListarDetecciones(){
        List<Deteccion> detecciones = new ArrayList<>();
        TypedQuery<Deteccion> query = em.createQuery("SELECT d FROM Deteccion d", Deteccion.class);
        detecciones = query.getResultList();
        return detecciones;
    }
}
