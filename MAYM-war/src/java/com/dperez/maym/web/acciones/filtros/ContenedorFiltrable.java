/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dperez.maym.web.acciones.filtros;

import java.io.Serializable;

/**
 * Clase para aplicación de filtros de busqueda por texto.
 * El texto filtrable puede ser cualquiera que se decida extraer de la clase contenido.
 * @author dipe2
 * @param <T>
 */
public class ContenedorFiltrable<T> implements Serializable {
    private String textoFiltrable;
    private T Contenido;

    public ContenedorFiltrable(String textoFiltrable, T Contenido) {
        this.textoFiltrable = textoFiltrable.toLowerCase();
        this.Contenido = Contenido;
    }

    public String getTextoFiltrable() {
        return textoFiltrable;
    }

    public void setTextoFiltrable(String textoFiltrable) {
        this.textoFiltrable = textoFiltrable;
    }

    public T getContenido() {
        return Contenido;
    }

    public void setContenido(T Contenido) {
        this.Contenido = Contenido;
    }   
    
}
