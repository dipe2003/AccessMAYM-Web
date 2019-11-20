/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dperez.maym.web.herramientas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author dperez
 * 12/12/2018
 */

public class Presentacion<T> {

        /**
     * Calcula la cantidad de paginas necsarias para mostrar el total de items de acuerdo al maximo definido para cada una.
     * @param totalDeItems
     * @param maxItemPagina
     * @return
     */
    public static int calcularCantidadPaginas(int totalDeItems, int maxItemPagina){
        Double resto = (double) totalDeItems / (double) maxItemPagina;
        int cantidad = resto.intValue();
        resto = resto - resto.intValue();
        if(resto > 0){
            cantidad ++;
        }
        return cantidad;
    }
    
     /**
     * Carga la lista de acciones para visualizar.
     * @param pagina
     * @param maxItems
     * @param listaItems 
     * @return  
     */
    public List<T> cargarPagina(int pagina, int maxItems, List<T> listaItems){      
        List<T> nuevaLista = new ArrayList<>();
        int min = 0;
        int max = maxItems;
        if(pagina!= 1) {
            min = (pagina-1) * maxItems;
            max = min + maxItems;
        }
        if(max > listaItems.size()) max = listaItems.size();
        nuevaLista.clear();
        for(int i = min; i < max; i++){
            nuevaLista.add(listaItems.get(i));
        }
        return nuevaLista;
    }
}
