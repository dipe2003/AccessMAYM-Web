/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maym.web.configuraciones;

import com.dperez.maym.web.acciones.filtros.ContenedorFiltrable;
import com.dperez.maym.web.acciones.filtros.DatosFiltros;
import com.dperez.maym.web.herramientas.Presentacion;
import com.dperez.maym.web.inicio.SesionUsuario;
import com.dperez.maymweb.modelo.area.Area;
import com.dperez.maymweb.facades.FacadeAdministrador;
import com.dperez.maymweb.facades.FacadeLectura;
import com.dperez.maymweb.facades.FacadeMain;
import com.dperez.maymweb.modelo.responsabilidad.Responsabilidad;
import com.dperez.maymweb.modelo.usuario.Usuario;
import com.dperez.maymweb.modelo.usuario.EnumPermiso;
import com.dperez.maymweb.modelo.usuario.Responsable;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import static javax.faces.application.FacesMessage.SEVERITY_ERROR;
import static javax.faces.application.FacesMessage.SEVERITY_FATAL;
import static javax.faces.application.FacesMessage.SEVERITY_INFO;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@ViewScoped
public class Usuarios implements Serializable {
    
    private FacadeAdministrador fAdmin;
    private FacadeLectura fLectura;
    private FacadeMain fMain;
    
    @Inject
    private SesionUsuario sesion;
      
    private int IdUsuarioSeleccionado;
    
    private boolean ContieneRegistros;
    
    private int NumeroNuevoUsuario;
    private String Nombre;
    private String Apellido;
    private String Password;
    private String PasswordConfirmacion;
    private String PasswordNuevo;
    private String CorreoElectronico;
    private boolean RecibeAlertas;
    
    private EnumPermiso[] PermisosUsuario;
    private EnumPermiso PermisoSeleccionado;
    
    private List<ContenedorFiltrable<Usuario>> listaUsuariosFiltrable;
    
    private boolean CambiarPassword;
    
    private List<Area> ListaAreas;
    private int AreaSeleccionada;
    
    private String textoBusqueda;
    
    // Pagination
    private static final int MAX_ITEMS = 10;
    private int CantidadPaginas;
    private int PaginaActual;
    private List<ContenedorFiltrable<Usuario>> listaCompletaUsuariosFiltrable;
    
    //<editor-fold desc="Getters">
    public boolean isContieneRegistros() {return ContieneRegistros;}
    public String getCorreoElectronico() {return CorreoElectronico;}
    public int getNumeroNuevoUsuario() {return NumeroNuevoUsuario;}
    public String getNombre() {return this.Nombre;}
    public String getApellido(){return this.Apellido;}
    public String getPassword(){return this.Password;}
    public String getPasswordConfirmacion(){return this.PasswordConfirmacion;}
    public String getPasswordNuevo(){return this.PasswordNuevo;}
    public boolean isRecibeAlertas(){return this.RecibeAlertas;}
    
    public EnumPermiso getPermisoSeleccionado(){return this.PermisoSeleccionado;}
    public EnumPermiso[] getPermisosUsuario(){return this.PermisosUsuario;}
    
    public List<ContenedorFiltrable<Usuario>> getListaUsuariosFiltrable() {return listaUsuariosFiltrable;}    
    
    public boolean isCambiarPassword() {return CambiarPassword;}
    
    public List<Area> getListaAreas() {return ListaAreas;}
    public int getAreaSeleccionada() {return AreaSeleccionada;}
    
    public String getTextoBusqueda(){return this.textoBusqueda;}
    //</editor-fold>
    
    // Paginacion
    public static int getMAX_ITEMS() {return MAX_ITEMS;}
    public int getCantidadPaginas() {return CantidadPaginas;}
    public int getPaginaActual() {return PaginaActual;}
    
    //<editor-fold desc="Setters">
    public void setContieneRegistros(boolean ContieneRegistros) {this.ContieneRegistros = ContieneRegistros;}
    public void setNumeroNuevoUsuario(int NumeroNuevoUsuario) {this.NumeroNuevoUsuario = NumeroNuevoUsuario;}
    public void setNombre(String Nombre) {this.Nombre = Nombre;}
    public void setApellido(String Apellido){this.Apellido = Apellido;}
    public void setPassword(String Password){this.Password = Password;}
    public void setPasswordConfirmacion(String PasswordConfirmacion){this.PasswordConfirmacion = PasswordConfirmacion;}
    public void setPasswordNuevo(String PasswordNuevo){this.PasswordNuevo = PasswordNuevo;}
    public void setCorreoElectronico(String CorreoElectronico){this.CorreoElectronico = CorreoElectronico;}
    public void setRecibeAlertas(boolean RecibeAlertas){this.RecibeAlertas = RecibeAlertas;}
    
    public void setPermisoSeleccionado(EnumPermiso PermisoSeleccionado){this.PermisoSeleccionado = PermisoSeleccionado;}
    public void setPermisosUsuario(EnumPermiso[] PermisosUsuario){this.PermisosUsuario = PermisosUsuario;}

    public void setListaUsuariosFiltrable(List<ContenedorFiltrable<Usuario>> listaUsuariosFiltrable) {
        this.listaUsuariosFiltrable = listaUsuariosFiltrable;
    }    
    
    public void setCambiarPassword(boolean CambiarPassword) {this.CambiarPassword = CambiarPassword;}
    
    public void setListaAreas(List<Area> ListaAreas) {this.ListaAreas = ListaAreas;}
    public void setAreaSeleccionada(int AreaSeleccionada) {this.AreaSeleccionada = AreaSeleccionada;}
    
    public void setTextoBusqueda(String textoBusqueda){this.textoBusqueda = textoBusqueda;}
    //</editor-fold>
    
    //<editor-fold desc="Metodos">
    
    /**
     * Carga de propiedades al inicio
     */
    @PostConstruct
    public void init(){
        fLectura = new FacadeLectura();
        fAdmin = new FacadeAdministrador();
        fMain = new FacadeMain();
                
        this.PermisoSeleccionado  = EnumPermiso.DATOS;
        
        // Paginacion
        PaginaActual = 1;
        
        listaUsuariosFiltrable = new ArrayList<>();
        listaCompletaUsuariosFiltrable = new ArrayList<>();
        fLectura.listarUsuarios(false).stream()
                 .forEach((Usuario u)->{
                     listaCompletaUsuariosFiltrable.add(new ContenedorFiltrable<>(u.getNombreCompleto(), u));
                 });
        listaCompletaUsuariosFiltrable.stream().sorted();
        
        PermisosUsuario = EnumPermiso.values();
        
        cambiarPagina(false, PaginaActual);
        
        // llenar la lista con todas las areas registradas.
        //Areas
        ListaAreas =  new ArrayList<>();
        // llenar la lista de areas con todas las areas registradas.
        List<Area> tmpAreas = fLectura.listarAreas();
        Collections.sort(tmpAreas);
        ListaAreas = tmpAreas.stream()
                .sorted()
                .collect(Collectors.toList());
        
        this.listaResponsabilidades = new ArrayList<>();
        this.listaResponsabilidadesSeleccionadas = new ArrayList<>();
        this.responsabilidadesAEliminar = new ArrayList<>();
        this.responsabilidadesNuevas = new ArrayList<>();
    }
    
    
    /**
     * Crea nuevo usuario con el permiso seleccionado.
     * Muestra un mensaje de errror si no se creo, se agrega el usuario a la empres logueada y redirige a la misma pagina para ver los resultados.
     * @throws java.io.IOException
     */
    public void nuevoUsuario() throws IOException{
        FacesContext context = FacesContext.getCurrentInstance();
        if(Password.equals(PasswordConfirmacion)){
            if((fAdmin.nuevoUsuario(NumeroNuevoUsuario, Nombre,Apellido,CorreoElectronico, Password,
                    PermisoSeleccionado, AreaSeleccionada))!=null){
                context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath() + "/Views/Configuraciones/Usuarios.xhtml?pagina=1");
            }else{
                context.addMessage("form_usuarios:btn_nuevo_usuario", new FacesMessage(SEVERITY_FATAL, "No se pudo crear nuevo usuario", "No se pudo crear nuevo usuario" ));
                context.renderResponse();
            }
        }else{
            context.addMessage("form_usuarios:btn_nuevo_usuario", new FacesMessage(SEVERITY_FATAL, "Los passwords no coniciden", "Los passwords no coniciden" ));
            context.renderResponse();
        }
    }
    
    /**
     * Actualiza el usuario con lo datos ingresados.
     * Muestra un mensaje de errror si no se actualizo y redirige a la misma pagina para ver los resultados.
     * @throws java.io.IOException
     */
    public void editarUsuario() throws IOException{
        FacesContext context = FacesContext.getCurrentInstance();
        if((fMain.CambiarDatosUsuario(IdUsuarioSeleccionado, Nombre, Apellido, CorreoElectronico, PermisoSeleccionado, RecibeAlertas, AreaSeleccionada))!=-1){
            if(sesion.getUsuarioLogueado().getId() == IdUsuarioSeleccionado){
                sesion.setUsuarioLogueado(fLectura.GetUsuario(IdUsuarioSeleccionado));
            }
            context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath() + "/Views/Configuraciones/Usuarios.xhtml?pagina="+PaginaActual);
        }else{
            context.addMessage("form_usuarios:btn_editar_usuario", new FacesMessage(SEVERITY_FATAL, "No se pudo editar usuario", "No se pudo editar usuario" ));
            context.renderResponse();
        }
    }
    
    /**
     * Cambia las credenciales del usuario.
     * Muestra los mensajes correspondientes por cada error/informacion.
     * Redirige a la pagina si se cambio.
     * @throws java.io.IOException
     */
    public void cambiarPassword() throws IOException{
        FacesContext context = FacesContext.getCurrentInstance();
        if(PasswordNuevo.equals(PasswordConfirmacion)){
            if(!PasswordNuevo.isEmpty()){
                if(fMain.ResetCredencialUsuario(IdUsuarioSeleccionado, PasswordNuevo)!=null){
                    context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath() + "/Views/Configuraciones/Usuarios.xhtml?pagina="+PaginaActual);
                }else{
                    context.addMessage("form_usuarios:btn_password_usuario", new FacesMessage(SEVERITY_FATAL, "No se pudo cambiar password", "No se pudo cambiar password" ));
                    context.renderResponse();
                }
            }else{
                context.addMessage("form_usuarios:btn_password_usuario", new FacesMessage(SEVERITY_FATAL, "El nuevo password esta vacio", "El nuevo password esta vacio" ));
                context.renderResponse();
            }
        }else{
            context.addMessage("form_usuarios:btn_password_usuario", new FacesMessage(SEVERITY_FATAL, "Los passwords no coniciden", "Los passwords no coniciden" ));
            context.renderResponse();
        }
    }
    
    /**
     * Eliminina el usuario indicado.
     * Muestra un mensaje de error si no se pudo eliminar, de lo contrario redirige a la misma pagina para visualizar los cambios.
     * @param IdUsuarioSeleccionado
     * @throws java.io.IOException
     */
    public void eliminarUsuario(int IdUsuarioSeleccionado) throws IOException{
        FacesContext context = FacesContext.getCurrentInstance();
        if(fAdmin.eliminarUsuario(IdUsuarioSeleccionado)!=-1){
            context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath() + "/Views/Configuraciones/Usuarios.xhtml?pagina=1");
        }else{
            context.addMessage("form_usuarios:btn_eliminar_usuario_"+IdUsuarioSeleccionado, new FacesMessage(SEVERITY_ERROR, "No se pudo eliminar el usuario", "No se pudo eliminar el usuario" ));
            context.renderResponse();
        }
    }
    
    /**
     * Da de baja un usuario.
     * @param IdUsuario
     * @throws java.io.IOException
     */
    public void darDeBajaUsuario(int IdUsuario) throws IOException{
        FacesContext context = FacesContext.getCurrentInstance();
        if(fAdmin.darDeBajaUsuario(IdUsuario)== -1){
            context.addMessage("form_usuarios:btn_baja_"+IdUsuario, new FacesMessage(SEVERITY_ERROR, "No se pudo dar de baja el usuario", "No se pudo dar de baja el usuario" ));
            context.renderResponse();
        }else{
            context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath() + "/Views/Configuraciones/Usuarios.xhtml?pagina="+PaginaActual);
        }
    }
    /**
     * Da de alta un usuario.
     * @param IdUsuario
     * @throws java.io.IOException
     */
    public void darDeAltaUsuario(int IdUsuario) throws IOException{
        FacesContext context = FacesContext.getCurrentInstance();
        if(fAdmin.darDeAltaUsuario(IdUsuario)== -1){
            context.addMessage("form_usuarios:btn_baja_"+IdUsuario, new FacesMessage(SEVERITY_ERROR, "No se pudo dar de alta el usuario", "No se pudo dar de alta el usuario" ));
            context.renderResponse();
        }else{
            context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath() + "/Views/Configuraciones/Usuarios.xhtml?pagina="+PaginaActual);
        }
    }
    
    public void comprobarNumeroNuevoOperario(){
        FacesContext context = FacesContext.getCurrentInstance();
        if(ComprobarContieneNumeroUsuario()){
            context.addMessage("form_usuarios:numero_nuevo_usuario", new FacesMessage(SEVERITY_ERROR, "El numero ingresado ya esta utilzado", "El numero ingresado ya esta utilzado" ));
            context.renderResponse();
        }
    }
    
    private boolean ComprobarContieneNumeroUsuario(){
        return listaUsuariosFiltrable.stream()
                .anyMatch((ContenedorFiltrable<Usuario> c)->c.getContenido().getId() == NumeroNuevoUsuario);
    }
    
    /**
     * Carga los atributos NombreCodificacion, DescripcionCodificacion e IdCodificacionSeleccionada segun el id especificado en la vista.
     * @param IdUsuario
     */
    public void cargarDatos(int IdUsuario){
        if(IdUsuario < 0 ){
            this.Nombre  = new String();
            this.Apellido  = new String();
            this.CorreoElectronico  = new String();
            this.RecibeAlertas = false;
            this.PermisoSeleccionado  = EnumPermiso.DATOS;
            this.IdUsuarioSeleccionado = -1;
            this.NumeroNuevoUsuario = 0;
            this.CambiarPassword = false;
            this.PasswordNuevo = new String();
            this.PasswordConfirmacion= new String();
            this.AreaSeleccionada = -1;
        }else{

            Usuario usrSeleccionado = listaUsuariosFiltrable.stream()
                    .filter((ContenedorFiltrable<Usuario> c)->c.getContenido().getId() == IdUsuario)
                    .findFirst()
                    .orElse(null).getContenido();
            
            this.Nombre  = usrSeleccionado.getNombre();
            this.Apellido  = usrSeleccionado.getApellido();
            this.CorreoElectronico  = usrSeleccionado.getCorreo();
            this.RecibeAlertas = usrSeleccionado.isRecibeAlertas();
            this.PermisoSeleccionado  = usrSeleccionado.getPermisoUsuario();
            this.IdUsuarioSeleccionado = IdUsuario;
            this.NumeroNuevoUsuario = usrSeleccionado.getId();
            this.CambiarPassword = false;
            this.PasswordNuevo = new String();
            this.PasswordConfirmacion= new String();
            this.AreaSeleccionada = usrSeleccionado.getAreaUsuario().getId();
            
            // verifica que no tenga registros relacionados
            // la lista de comprobaciones y de actividades deben estar vacias => False
            // TODO agregar metodo que comprueba si el usuario es responsable de alguna comprobacion o actividad
            ContieneRegistros = usrSeleccionado.tieneComprobacionAsignada();
        }
    }
    
    public void cambiarPagina(boolean conFiltros, int numero){
        if(conFiltros){
            PaginaActual = numero;
            filtrarTexto();
        }else{
            listaCompletaUsuariosFiltrable = listaCompletaUsuariosFiltrable.stream()
                     .sorted((o1, o2) -> {
                         return o1.getContenido().compareTo(o2.getContenido());
                     })
                    .toList();
            
            cargarPagina(listaCompletaUsuariosFiltrable);
            textoBusqueda ="";
        }
    }
    
    private void cargarPagina(List<ContenedorFiltrable<Usuario>> usuarios){
        CantidadPaginas = Presentacion.calcularCantidadPaginas(usuarios.size(), MAX_ITEMS);
        // Corregir el numero de pagina en caso que se apliquen filtros en una página diferente de 1 y luego de filtrar
        // en esa página no hayan datos para mostrar.
        if(PaginaActual>CantidadPaginas)PaginaActual = 1;
        listaUsuariosFiltrable = Presentacion.cargarPagina(PaginaActual, MAX_ITEMS, usuarios);
        listaUsuariosFiltrable = listaUsuariosFiltrable.stream()
                     .sorted((o1, o2) -> {
                         return o1.getContenido().compareTo(o2.getContenido());
                     })
                .toList();
    }
    
    public void filtrarTexto(){
        cargarPagina(DatosFiltros.FiltrarPorTexto(listaCompletaUsuariosFiltrable, textoBusqueda));
    }
    
    public void resetFiltro(){
        textoBusqueda = "";
        cargarPagina(listaCompletaUsuariosFiltrable);
    }
    //</editor-fold>
    
//<editor-fold desc="Panel Responsabilidades">
    
    private Usuario usuarioSeleccionado;
    private List<Responsabilidad> listaResponsabilidades;
    private List<Responsabilidad> listaResponsabilidadesSeleccionadas;
    private List<Responsabilidad> responsabilidadesNuevas;
    private List<Responsabilidad> responsabilidadesAEliminar;
    private int idResponsabilidadSeleccionada;
    
    //<editor-fold desc="Getters">
    public List<Responsabilidad> getListaResponsabilidades(){return this.listaResponsabilidades;    }
    
    public List<Responsabilidad> getListaResponsabilidadesSeleccionadas() {
        return listaResponsabilidadesSeleccionadas;
    }
    public int getIdResponsabilidadSeleccionada() {
        return idResponsabilidadSeleccionada;
    }
    
    public Usuario getUsuarioSeleccionado() {
        return usuarioSeleccionado;
    }
    //</editor-fold>
    
    //<editor-fold desc="Setters">
    public void setListaResponsabilidades(List<Responsabilidad> ListaResponsabilidades){this.listaResponsabilidades = ListaResponsabilidades;}
    
    public void setListaResponsabilidadesSeleccionadas(List<Responsabilidad> listaResponsabilidadesSeleccionadas) {
        this.listaResponsabilidadesSeleccionadas = listaResponsabilidadesSeleccionadas;
    }
    
    public void setIdResponsabilidadSeleccionada(int idResponsabilidadSeleccionada) {
        this.idResponsabilidadSeleccionada = idResponsabilidadSeleccionada;
    }
    
    public void setUsuarioSeleccionado(Usuario usuarioSeleccionado) {
        this.usuarioSeleccionado = usuarioSeleccionado;
    }
    //</editor-fold>
    //</editor-fold>
    
    //<editor-fold desc="Metodos">
    
    
    /**
     * Actualiza la lista de responsabilidades segun las que ya contenga el usuario.
     * @param idUsuarioSeleccionado
     */
    public void actualizarResponsabilidadesSeleccionadas(int idUsuarioSeleccionado){
        this.usuarioSeleccionado = fLectura.GetUsuario(idUsuarioSeleccionado);
        
        this.listaResponsabilidades.clear();
        this.responsabilidadesAEliminar.clear();
        this.responsabilidadesNuevas.clear();
        
        listaResponsabilidades = fLectura.listarResponsabilidades();
        this.listaResponsabilidadesSeleccionadas.clear();
        usuarioSeleccionado.getResponsablesUsuario().stream()
                .forEach(resp->{
                    listaResponsabilidadesSeleccionadas.add(resp.getResponsabilidadResponsable());
                    listaResponsabilidades.remove(resp.getResponsabilidadResponsable());
                });
        
        listaResponsabilidadesSeleccionadas.sort(Comparator.naturalOrder());
        listaResponsabilidades.sort(Comparator.naturalOrder());
    }
    
    
    /**
     * Agrega la responsabilidad seleccionada a la lista de seleccionadas y la quita
     * de la lista de disponibles.
     */
    public void agregarResponsabilidad(){
        Responsabilidad responsabilidad = listaResponsabilidades.stream()
                .filter(r->r.getId()== idResponsabilidadSeleccionada)
                .findFirst()
                .get();
        // si el usuario tiene la responsabilidad ya asignada y ésta está en la lista de responsabilidades es porque
        // ya había sido seleccionada para eliminar.
        if(usuarioSeleccionado.tieneResponsabilidadAsignada(idResponsabilidadSeleccionada)){
            this.responsabilidadesAEliminar.remove(responsabilidad);
        }else{
            this.responsabilidadesNuevas.add(responsabilidad);
        }
        listaResponsabilidadesSeleccionadas.add(responsabilidad);
        listaResponsabilidades.remove(responsabilidad);
        idResponsabilidadSeleccionada = 0;
    }
    
    /**
     * Quita la responsabilidad seleccionada a la lista de seleccionadas y la quita
     * de las disponibles.
     */
    public void quitarResponsabilidad(){
        Responsabilidad responsabilidad = listaResponsabilidadesSeleccionadas.stream()
                .filter(r->r.getId()== idResponsabilidadSeleccionada)
                .findFirst()
                .get();
        // si el usuario NO tiene la responsabilidad ya asignada y ésta está se está quitando también
        // se debe quitar de las responsabilidades nuevas.
        if(!usuarioSeleccionado.tieneResponsabilidadAsignada(idResponsabilidadSeleccionada)){
            this.responsabilidadesNuevas.remove(responsabilidad);
        }else{
            responsabilidadesAEliminar.add(responsabilidad);
        }
        // si el usuario ya tenía la responsabilidad y está siendo utilizada no se elimina, si no que debe darse de baja.
        // esta comprobación se realiza en el metodo de guardar.
        
        listaResponsabilidades.add(responsabilidad);
        listaResponsabilidadesSeleccionadas.remove(responsabilidad);
        idResponsabilidadSeleccionada = 0;
    }
    
    //TODO: falta caso de eliminar responsabilidad y dar de baja
    public void guardarCambios(){
        try{
            // crear los nuevos responsables utilizando las nuevas responsabilidades seleccionadas.
            responsabilidadesNuevas.stream()
                    .forEach((Responsabilidad responsabilidad)->{
                        fAdmin.nuevoResponsable(responsabilidad.getId(), usuarioSeleccionado.getId());
                    });
            // eliminar las responsabilidades que no estén en uso y dar de bajas las que si lo estén.
            responsabilidadesAEliminar.stream()
                    .forEach((Responsabilidad responsabilidad)->{
                        Responsable responsable = responsabilidad.getResponsables().stream()
                                .filter(resp->resp.getUsuarioResponsable().getId() == usuarioSeleccionado.getId())
                                .findFirst()
                                .orElseThrow();
                        if(responsable.tieneActividadesAsignadas()|| responsable.tieneActividadesAsignadas()){
                            // dar de baja
                            fAdmin.darBajaResponsable(responsabilidad.getId(),
                                    responsable.getId());
                        }else{
                            // eliminar
                            fAdmin.eliminarResponsable(responsable.getId());
                        }
                    });
            FacesContext.getCurrentInstance().addMessage("form_accion_modal_responsabilidades:btn_guardar_responsabilidades",
                    new FacesMessage(SEVERITY_INFO,  "OK", "Las responsabilidaes se actualizacion correctamente." ));
            FacesContext.getCurrentInstance().renderResponse();
        }catch(Exception ex){
            FacesContext.getCurrentInstance().addMessage("form_accion_modal_responsabilidades:btn_guardar_responsabilidades",
                    new FacesMessage(SEVERITY_ERROR,  "Error", "Ocurrio un error." ));
            FacesContext.getCurrentInstance().renderResponse();
        }
    }
    //</editor-fold>
        
}