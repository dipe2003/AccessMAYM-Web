/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maymweb.accion.actividad;

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
public class ManejadorActividad{
    @PersistenceContext(unitName = "MAYM-Web-Datos")
    private EntityManager em;
    
    public ManejadorActividad(){}
    
    public int CrearActividad(Actividad actividad){
        try{
            em.persist(actividad);
            return actividad.getIdActividad();
        }catch(Exception ex){
            System.out.println("Error al crear actividad: " + ex.getMessage());
        }
        return -1;
    }
    
    public int ActualizarActividad(Actividad actividad){
        try{
            em.merge(actividad);
            return actividad.getIdActividad();
        }catch(Exception ex){
            System.out.println("Error al actualizar actividad: " + ex.getMessage());
        }
        return -1;
    }
    
    public int BorrarActividad(Actividad actividad){
        try{
            em.remove(actividad);
            return actividad.getIdActividad();
        }catch(Exception ex){
            System.out.println("Error al borrar actividad: " + ex.getMessage());
        }
        return -1;
    }
    
    public Actividad GetActividad(int IdActividad){
        Actividad actividad = em.find(Actividad.class, IdActividad);
        return actividad;
    }
    
    public List<Actividad> ListarActividads(){
        List<Actividad> actividads = new ArrayList<>();
        TypedQuery<Actividad> query = em.createQuery("SELECT m FROM Actividad m", Actividad.class);
        actividads = query.getResultList();
        return actividads;
    }
    
}
