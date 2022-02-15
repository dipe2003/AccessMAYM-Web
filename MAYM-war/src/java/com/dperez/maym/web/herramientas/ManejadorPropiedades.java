/*
* Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
* Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
*/
package com.dperez.maym.web.herramientas;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Properties;
import javax.ejb.Stateless;
import javax.inject.Named;

/**
 *
 * @author dipe2
 */

@Named
@Stateless
public class ManejadorPropiedades {
    
    public static Properties getPropiedades(String directorio){
        Properties prop = new Properties();
        try (InputStream input =new FileInputStream(directorio+"config.properties")) {
            
            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
            }
            
            //load a properties file from class path, inside static method
            prop.load(input);
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return prop;
    }
    
    public static void setPropiedades(String directorio, Map<String, String> propiedades){
        try (OutputStream output = new FileOutputStream(directorio+"config.properties")) {

            Properties prop = new Properties();

            // set the properties value
            propiedades.entrySet().stream()
                    .forEach(entry->{
                        prop.setProperty(entry.getKey(), entry.getValue());
                    });

            // save properties to project root folder
            prop.store(output, null);

        } catch (IOException io) {
            io.printStackTrace();
        }

    }   
    
}
