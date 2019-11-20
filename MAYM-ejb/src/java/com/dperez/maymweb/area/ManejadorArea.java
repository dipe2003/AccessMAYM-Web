/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maymweb.area;

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
public class ManejadorArea{
    @PersistenceContext(unitName = "MAYM-Web-Datos")
    private EntityManager em;
    
    public ManejadorArea(){}
    
    public int CrearArea(Area area){
        try{
            em.persist(area);
            return area.getId();
        }catch(Exception ex){
            System.out.println("Error al crear area: " + ex.getMessage());
        }
        return -1;
    }
    
    public int ActualizarArea(Area area){
        try{
            em.merge(area);
            return area.getId();
        }catch(Exception ex){
            System.out.println("Error al actualizar area: " + ex.getMessage());
        }
        return -1;
    }
    
    public int BorrarArea(Area area){
        try{
            em.remove(area);
            return area.getId();
        }catch(Exception ex){
            System.out.println("Error al borrar area: " + ex.getMessage());
        }
        return -1;
    }
    
    public Area GetArea(int IdArea){
        Area area = em.find(Area.class, IdArea);
        return area;
    }
    
    public List<Area> ListarAreas(){
        List<Area> areas = new ArrayList<>();
        TypedQuery<Area> query = em.createQuery("SELECT a FROM Area a", Area.class);
        areas = query.getResultList();
        return areas;
    }
}
