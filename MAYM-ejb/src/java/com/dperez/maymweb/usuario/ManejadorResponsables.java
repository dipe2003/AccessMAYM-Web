/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maymweb.responsable;

import com.dperez.maymweb.usuario.Responsable;
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
public class ManejadorResponsables {
    @PersistenceContext(unitName = "MAYM-ejbPU")
    private EntityManager em;
    
    public ManejadorResponsables(){}
    
    public int CrearResponsable(Responsable responsable){
        try{
            em.persist(responsable);
            return responsable.getId();
        }catch(Exception ex){
            System.out.println("Error al crear responsable: " + ex.getMessage());
        }
        return -1;
    }
    
    public int ActualizarResponsable(Responsable responsable){
        try{
            em.merge(responsable);
            return responsable.getId();
        }catch(Exception ex){
            System.out.println("Error al actualizar responsable: " + ex.getMessage());
        }
        return -1;
    }
    
    public int BorrarResponsable(Responsable responsable){
        try{
            em.remove(responsable);
            return responsable.getId();
        }catch(Exception ex){
            System.out.println("Error al borrar responsable: " + ex.getMessage());
        }
        return -1;
    }
    
    public Responsable GetResponsable(int IdResponsable){
        Responsable responsable = em.find(Responsable.class, IdResponsable);
        return responsable;
    }
    
    public List<Responsable> ListarResponsables(){
        List<Responsable> responsables = new ArrayList<>();
        TypedQuery<Responsable> query = em.createQuery("SELECT u FROM Responsable u", Responsable.class);
        if(query.getResultList()!= null){
            responsables = query.getResultList();
        }
        return responsables;
    }
}
