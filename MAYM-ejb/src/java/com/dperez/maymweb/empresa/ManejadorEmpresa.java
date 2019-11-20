/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maymweb.empresa;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author Diego
 */

@Named
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ManejadorEmpresa  {
    @PersistenceContext(unitName = "MAYM-Web-Datos")
    private EntityManager em;
    
    public int CrearEmpresa(Empresa empresa){
        try{
            em.persist(empresa);
            return empresa.getId();
        }catch(Exception ex){
            System.out.println("Error al crear empresa: " + ex.getMessage());
        }
        return -1;
    }
    
    public int ActualizarEmpresa(Empresa empresa){
        try{
            em.merge(empresa);
            return empresa.getId();
        }catch(Exception ex){
            System.out.println("Error al actualizar empresa: " + ex.getMessage());
        }
        return -1;
    }
    
    public int BorrarEmpresa(Empresa empresa){
        try{
            em.remove(empresa);
            return empresa.getId();
        }catch(Exception ex){
            System.out.println("Error al borrar empresa: " + ex.getMessage());
        }
        return -1;
    }
    
    public Empresa GetEmpresa(int IdEmpresa){
        Empresa empresa = em.find(Empresa.class, IdEmpresa);
        return empresa;
    }
    
    public List<Empresa> ListarEmpresasRegistradas(){
        List<Empresa> empresas = new ArrayList<>();
        TypedQuery<Empresa> query = em.createQuery("FROM Empresa e", Empresa.class);
        try{
            empresas = query.getResultList();
        }catch(Exception ex){
            System.out.println("Error: " + ex.getMessage());
        }
        return empresas;
    }
}
