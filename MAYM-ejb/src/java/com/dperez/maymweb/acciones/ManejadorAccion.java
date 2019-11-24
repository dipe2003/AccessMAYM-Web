/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maymweb.acciones;

import com.dperez.maymweb.accion.comprobaciones.Comprobacion;
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
public class ManejadorAccion {
    @PersistenceContext(unitName = "MAYM-ejbPU")
    private EntityManager em;
    
    public ManejadorAccion(){}
    
    public int CrearAccion(Accion accion){
        try{
            em.persist(accion);
            return accion.getId();
        }catch(Exception ex){
            System.out.println("Error al crear accion: " + ex.getMessage());
        }
        return -1;
    }
    
    public int ActualizarAccion(Accion accion){
        try{
            em.merge(accion);
            return accion.getId();
        }catch(Exception ex){
            System.out.println("Error al actualizar accion: " + ex.getMessage());
        }
        return -1;
    }
    
    public int BorrarAccion(Accion accion){
        try{
            em.remove(accion);
            return accion.getId();
        }catch(Exception ex){
            System.out.println("Error al borrar accion: " + ex.getMessage());
        }
        return -1;
    }
    
    public Accion GetAccion(int IdAccion){
        Accion accion = em.find(Accion.class, IdAccion);
        return accion;
    }
    
    public List<Accion> ListarAcciones(){
        List<Accion> acciones;
        TypedQuery<Accion> query = em.createQuery("SELECT a FROM Accion a", Accion.class);
        acciones = query.getResultList();
        return acciones;
    }
    
    //  Comprobaciones
    public int crearComprobacion(Comprobacion comprobacion){
         try{
            em.persist(comprobacion);
            return comprobacion.getId();
        }catch(Exception ex){
            System.out.println("Error al crear comprobacion: " + ex.getMessage());
        }
        return -1;
    }
}
