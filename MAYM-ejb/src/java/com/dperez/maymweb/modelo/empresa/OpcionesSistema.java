/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dperez.maymweb.modelo.empresa;

import java.awt.Color;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author dipe2
 */
public class OpcionesSistema {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private Empresa empresaOpciones;

    private OpcionesApariencia opcionesApariencia;

    private OpcionesCorreo opcionesCorreo;

    public OpcionesSistema() {
        opcionesApariencia = new OpcionesApariencia();
        opcionesCorreo = new OpcionesCorreo();
    }

    //<editor-fold desc="GETTERS">
    public int getId() {
        return id;
    }

    public Empresa getEmpresaOpciones() {
        return empresaOpciones;
    }

    public OpcionesApariencia getOpcionesApariencia() {
        return opcionesApariencia;
    }

    public OpcionesCorreo getOpcionesCorreo() {
        return opcionesCorreo;
    }

    //</editor-fold>
    //<editor-fold desc="SETTERS">
    public void setId(int id) {
        this.id = id;
    }

    public void setEmpresaOpciones(Empresa empresaOpciones) {
        this.empresaOpciones = empresaOpciones;
    }

    public void setOpcionesApariencia(OpcionesApariencia opcionesApariencia) {
        this.opcionesApariencia = opcionesApariencia;
    }

    public void setOpcionesCorreo(OpcionesCorreo opcionesCorreo) {
        this.opcionesCorreo = opcionesCorreo;
    }

    //</editor-fold>
    
    //<editor-fold desc="METODOS">
    public String getColorBrighter(String color) {
        Color colorBrighter = Color.decode(color).brighter();
        return String.format("#%02x%02x%02x", colorBrighter.getRed(), colorBrighter.getGreen(), colorBrighter.getBlue());
    }

    public String getColorDarker(String color) {
        Color colorDarker = Color.decode(color).darker();
        return String.format("#%02x%02x%02x", colorDarker.getRed(), colorDarker.getGreen(), colorDarker.getBlue());
    }
//</editor-fold>
}
