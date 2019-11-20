/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.dperez.maymweb.herramientas;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Properties;
import javax.ejb.Stateless;
import javax.inject.Named;

/**
 *
 * @author Diego
 */

@Named
@Stateless
public class IOPropiedades implements Serializable{
    
    private final String homeDir = System.getProperty("user.home");
    private final String separator = System.getProperty("file.separator");
    private String directorio;

    /**
     * Guarda el valor de la propiedad especificado.
     * @param NombrePropiedad
     * @param ValorPropiedad
     * @return Retorna <b>True</b> si se guardo, de lo contrario <b>False</b?>
     */
    public boolean setPropiedad(String NombrePropiedad, String ValorPropiedad){
        directorio = "MAYMWEB"+separator+"Configuracion"+separator;
        
        Properties prop = new Properties();
        OutputStream output = null;
        
        try {            
            output = new FileOutputStream(directorio+"config.properties");
            
            // setea el valor de la propiedad
            prop.setProperty(NombrePropiedad, ValorPropiedad);
            
            // guarda el resultado.
            prop.store(output, null);
            
        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    /**
     * Obtiene la propiedad especificada.
     * @param NombrePropiedad
     * @return Retorna la propiedad si se encontro, de lo contrario devuelve un String vacio.
     */
    public String getPropiedad(String NombrePropiedad){
        directorio = "MAYMWEB"+separator+"Configuracion"+separator;
        
        Properties prop = new Properties();
        InputStream input = null;
        
        try {
            
            input = new FileInputStream(directorio+"config.properties");
            
            // carga el valor de la propiedad.
            prop.load(input);

            return prop.getProperty(NombrePropiedad);
            
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return new String();
    }

}