/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dperez.maym.web.configuraciones;

/**
 *
 * @author dipe2
 */
public class OpcionesSistema {

    private String colorSuperiorPanelTitulo;
    private String colorInferiorPanelTitulo;
    private String colorFuentePanelEncabezado;
    private String colorPanelTitulo;
    private String colorFuentePanelTitulo;

    public OpcionesSistema(String colorSuperiorPanelTitulo, String colorInferiorPanelTitulo, String colorPanelTitulo, String colorFuentePanelEncabezado, String colorFuentePanelTitulo) {
        this.colorSuperiorPanelTitulo = colorSuperiorPanelTitulo;
        this.colorInferiorPanelTitulo = colorInferiorPanelTitulo;
        this.colorFuentePanelEncabezado = colorFuentePanelEncabezado;
        this.colorPanelTitulo = colorPanelTitulo;
        this.colorFuentePanelTitulo = colorFuentePanelTitulo;
    }

    public OpcionesSistema() {
    }

    public String getColorSuperiorPanelTitulo() {
        return colorSuperiorPanelTitulo;
    }

    public void setColorSuperiorPanelTitulo(String colorSuperiorPanelTitulo) {
        this.colorSuperiorPanelTitulo = colorSuperiorPanelTitulo;
    }

    public String getColorInferiorPanelTitulo() {
        return colorInferiorPanelTitulo;
    }

    public void setColorInferiorPanelTitulo(String colorInferiorPanelTitulo) {
        this.colorInferiorPanelTitulo = colorInferiorPanelTitulo;
    }

    public String getColorPanelTitulo() {
        return colorPanelTitulo;
    }

    public void setColorPanelTitulo(String colorPanelTitulo) {
        this.colorPanelTitulo = colorPanelTitulo;
    }

    public String getColorFuentePanelEncabezado() {
        return colorFuentePanelEncabezado;
    }

    public String getColorFuentePanelTitulo() {
        return colorFuentePanelTitulo;
    }

    public void setColorFuentePanelEncabezado(String colorFuentePanelEncabezado) {
        this.colorFuentePanelEncabezado = colorFuentePanelEncabezado;
    }

    public void setColorFuentePanelTitulo(String colorFuentePanelTitulo) {
        this.colorFuentePanelTitulo = colorFuentePanelTitulo;
    }

}
