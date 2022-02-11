package com.dperez.maymweb.persistencia;

import com.dperez.maymweb.modelo.acciones.Accion;
import com.dperez.maymweb.modelo.area.Area;
import com.dperez.maymweb.modelo.codificacion.Codificacion;
import com.dperez.maymweb.modelo.deteccion.Deteccion;
import com.dperez.maymweb.modelo.fortaleza.Fortaleza;
import com.dperez.maymweb.modelo.responsabilidad.Responsabilidad;
import com.dperez.maymweb.modelo.usuario.Responsable;
import com.dperez.maymweb.modelo.usuario.Usuario;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


public class FabricaRepositorio {
    
    public RepositorioPersistencia<Usuario> getRepositorioUsuarios() {
        return new RepositorioPersistencia<>(Usuario.class, getEntityManager());
    }
    
    public RepositorioPersistencia<Codificacion> getRepositorioCodificaciones() {
        return new RepositorioPersistencia<>(Codificacion.class, getEntityManager());
    }
    
    public RepositorioPersistencia<Area> getRepositorioAreas() {
        return new RepositorioPersistencia<>(Area.class, getEntityManager());
    }
    
    public RepositorioPersistencia<Deteccion> getRepositorioDetecciones() {
        return new RepositorioPersistencia<>(Deteccion.class, getEntityManager());
    }
    
    public RepositorioPersistencia<Responsable> getRepositorioResponsables() {
        return new RepositorioPersistencia<>(Responsable.class, getEntityManager());
    }
    
    public RepositorioPersistencia<Responsabilidad> getRepositorioResponsabilidades(){
        return new RepositorioPersistencia<>(Responsabilidad.class, getEntityManager());
    }
    
    public RepositorioPersistencia<Accion> getRespositorioAcciones(){
        return new RepositorioPersistencia<>(Accion.class, getEntityManager());
    }
    
    public RepositorioPersistencia<Fortaleza> getRepositorioFortalezas(){
        return new RepositorioPersistencia<>(Fortaleza.class, getEntityManager());
    }
        
    private EntityManager getEntityManager() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("MAYM-ejbPU");
        return emf.createEntityManager();
    }
}
