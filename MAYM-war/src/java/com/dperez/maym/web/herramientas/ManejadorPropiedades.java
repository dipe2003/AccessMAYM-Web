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

    public static Properties getPropiedades(String directorio) {
        Properties prop = new Properties();
        try (InputStream input = new FileInputStream(directorio + "config.properties")) {

            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
            }

            prop.load(input);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return prop;
    }

    public static void setPropiedades(String directorio, Map<String, String> propiedades) {

        InputStream input = null;
        Properties prop = new Properties();

        try {
            input = new FileInputStream(directorio + "config.properties");
            if (input != null) {               
                prop.load(input);
            }
        } catch (IOException ex) {
            System.out.println("Sorry, unable to find config.properties");
        }

        try {
            OutputStream output = new FileOutputStream(directorio + "config.properties");

            propiedades.entrySet().stream()
                    .forEach(entry -> {
                        if (prop.containsKey(entry.getKey()) && entry.getValue() != null) {
                            prop.replace(entry.getKey(), entry.getValue());
                        } else {
                            if (entry.getValue() != null) {
                                prop.putIfAbsent(entry.getKey(), entry.getValue());
                            }
                        }
                    });

            prop.store(output, null);
        } catch (IOException ex2) {
            ex2.printStackTrace();
        }
    }
}
