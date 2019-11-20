/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maymweb.usuario;

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
public class ManejadorUsuario {
    @PersistenceContext(unitName = "MAYM-Web-Datos")
    private EntityManager em;
    
    public ManejadorUsuario(){}
    
    public int CrearUsuario(Usuario usuario){
        try{
            em.persist(usuario);
            return usuario.getId();
        }catch(Exception ex){
            System.out.println("Error al crear usuario: " + ex.getMessage());
        }
        return -1;
    }
    
    public int ActualizarUsuario(Usuario usuario){
        try{
            em.merge(usuario);
            return usuario.getId();
        }catch(Exception ex){
            System.out.println("Error al actualizar usuario: " + ex.getMessage());
        }
        return -1;
    }
    
    public int BorrarUsuario(Usuario usuario){
        try{
            em.remove(usuario);
            return usuario.getId();
        }catch(Exception ex){
            System.out.println("Error al borrar usuario: " + ex.getMessage());
        }
        return -1;
    }
    
    public Usuario GetUsuario(int IdUsuario){
        Usuario usuario = em.find(Usuario.class, IdUsuario);
        return usuario;
    }
    
    public List<Usuario> ListarUsuarios(){
        List<Usuario> usuarios = new ArrayList<>();
        TypedQuery<Usuario> query = em.createQuery("SELECT u FROM Usuario u", Usuario.class);
        if(query.getResultList()!= null){
            usuarios = query.getResultList();
        }
        return usuarios;
    }
}
