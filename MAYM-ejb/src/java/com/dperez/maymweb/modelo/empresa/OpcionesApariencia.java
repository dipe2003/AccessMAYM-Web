/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dperez.maymweb.modelo.empresa;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author dipe2
 */
public class OpcionesApariencia {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    
    private String colorSuperiorPanelTitulo;
    private String colorInferiorPanelTitulo;
    private String colorFuentePanelEncabezado;
    private String colorPanelTitulo;
    private String colorFuentePanelTitulo;
    private String colorBody;
    private String colorBoton;

    public OpcionesApariencia() {
        colorSuperiorPanelTitulo = "#2a2a2a";
        colorInferiorPanelTitulo = "black";
        colorPanelTitulo = "#337ab7";
        colorFuentePanelEncabezado = "#cce8f6";
        colorFuentePanelTitulo = "#cce8f6";
        colorBody = "#ffffff";
        colorBoton = "#337ab7";
    }

    public OpcionesApariencia(String colorSuperiorPanelTitulo, String colorInferiorPanelTitulo, String colorFuentePanelEncabezado, String colorPanelTitulo, String colorFuentePanelTitulo, String colorBody, String colorBoton) {
        this.colorSuperiorPanelTitulo = colorSuperiorPanelTitulo;
        this.colorInferiorPanelTitulo = colorInferiorPanelTitulo;
        this.colorFuentePanelEncabezado = colorFuentePanelEncabezado;
        this.colorPanelTitulo = colorPanelTitulo;
        this.colorFuentePanelTitulo = colorFuentePanelTitulo;
        this.colorBody = colorBody;
        this.colorBoton = colorBoton;
    }

    //<editor-fold desc="GETTERS">
    public String getColorSuperiorPanelTitulo() {
        return colorSuperiorPanelTitulo;
    }

    public String getColorInferiorPanelTitulo() {
        return colorInferiorPanelTitulo;
    }

    public String getColorFuentePanelEncabezado() {
        return colorFuentePanelEncabezado;
    }

    public String getColorPanelTitulo() {
        return colorPanelTitulo;
    }

    public String getColorFuentePanelTitulo() {
        return colorFuentePanelTitulo;
    }

    public String getColorBody() {
        return colorBody;
    }

    public String getColorBoton() {
        return colorBoton;
    }
    //</editor-fold>

    //<editor-fold desc="SETTERS">
    public void setColorSuperiorPanelTitulo(String colorSuperiorPanelTitulo) {
        this.colorSuperiorPanelTitulo = colorSuperiorPanelTitulo;
    }

    public void setColorInferiorPanelTitulo(String colorInferiorPanelTitulo) {
        this.colorInferiorPanelTitulo = colorInferiorPanelTitulo;
    }

    public void setColorFuentePanelEncabezado(String colorFuentePanelEncabezado) {
        this.colorFuentePanelEncabezado = colorFuentePanelEncabezado;
    }

    public void setColorPanelTitulo(String colorPanelTitulo) {
        this.colorPanelTitulo = colorPanelTitulo;
    }

    public void setColorFuentePanelTitulo(String colorFuentePanelTitulo) {
        this.colorFuentePanelTitulo = colorFuentePanelTitulo;
    }

    public void setColorBody(String colorBody) {
        this.colorBody = colorBody;
    }

    public void setColorBoton(String colorBoton) {
        this.colorBoton = colorBoton;
    }

    //</editor-fold>
}
