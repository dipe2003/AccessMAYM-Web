/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maymweb.facades;

import com.dperez.maymweb.controlador.configuracion.ControladorConfiguracion;
import com.dperez.maymweb.modelo.usuario.Credencial;
import com.dperez.maymweb.modelo.usuario.EnumPermiso;

/**
 *
 * @author Diego
 */
public class FacadeMain {

    private final ControladorConfiguracion cConfig;
    
    //  Constructores
    public FacadeMain(){
        cConfig = new ControladorConfiguracion();
    }
    
    // Metodos genericos independientes del nivel de permiso del usuario.
    /**
     * Setea los datos del usuario y actualiza la base de datos. No se realiza comprobacion de password.
     * Para cambiar password utilizar metodo cambiarPasswordUsuario().
     * @param IdUsuario
     * @param NombreUsuario
     * @param ApellidoUsuario
     * @param CorreoUsuario
     * @param PermisoUsuario
     * @param RecibeAlertas
     * @param IdArea
     * @return -1 si no se actualizo.
     */
    public int CambiarDatosUsuario(int IdUsuario, String NombreUsuario, String ApellidoUsuario, String CorreoUsuario,
            EnumPermiso PermisoUsuario, boolean RecibeAlertas, int IdArea){
        return cConfig.cambiarDatosUsuario(IdUsuario, NombreUsuario, ApellidoUsuario, CorreoUsuario, PermisoUsuario, RecibeAlertas, IdArea);
    }
    
    
    /**
     * Comprueba la validez del password ingresado con el correspondiente del usuario en la base de datos.
     * @param IdUsuario
     * @param Password
     * @return True si es valido | Fales si no es valido.
     */
    public boolean ComprobarValidezPassword(int IdUsuario, String Password){
        return cConfig.comprobarValidezPassword(IdUsuario, Password);
    }
    
    /**
     * Cambia la credencial (password y password key) del usuario especificado y actualiza la base de datos.
     * Si el password ingresado no coincide con el guardado no se actualiza.
     * @param IdUsuario
     * @param OldPassword: password del usuario guardado en la base de datos.
     * @param NewPassword: nuevo password para el usuario.
     * @return Retorna la credencial del usuario actualizada. Retorna Null si no se pudo actualizar.
     */
    public Credencial CambiarCredencialUsuario(int IdUsuario, String OldPassword, String NewPassword){
        return cConfig.cambiarCredencialUsuario(IdUsuario, OldPassword, NewPassword);
    }
    
    /**
     * Cambia la credencial (password y password key) del usuario especificado y actualiza la base de datos.
     * El metodo debe ser utilizado por un Administrador del Sistema para resetear el password de un usuario.
     * @param IdUsuario
     * @param NewPassword: nuevo password para el usuario.
     * @return Retorna la credencial del usuario actualizada. Retorna Null si no se pudo actualizar.
     */
    public Credencial ResetCredencialUsuario(int IdUsuario, String NewPassword){
       return cConfig.resetCredencialUsuario(IdUsuario, NewPassword);
    }
    
    /**
     * Comprueba si existe el usuario con el id especificado.
     * @param IdUsuario
     * @return
     */
    public boolean ExisteUsuario(int IdUsuario){
        return cConfig.existeUsuario(IdUsuario);
    }
    
}
