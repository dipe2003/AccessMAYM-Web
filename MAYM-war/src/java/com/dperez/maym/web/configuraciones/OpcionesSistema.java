/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dperez.maym.web.configuraciones;

import java.awt.Color;

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
    private String colorBody;
    private String colorBoton;

    public OpcionesSistema() {
        colorSuperiorPanelTitulo = "#2a2a2a";
        colorInferiorPanelTitulo = "black";
        colorPanelTitulo = "#337ab7";
        colorFuentePanelEncabezado = "#cce8f6";
        colorFuentePanelTitulo = "#cce8f6";
        colorBody = "#ffffff";
        colorBoton = "#337ab7";
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

    public String getColorBody() {
        return colorBody;
    }

    public void setColorBody(String colorBody) {
        this.colorBody = colorBody;
    }

    public String getColorBoton() {
        return colorBoton;
    }

    public void setColorBoton(String colorBoton) {
        this.colorBoton = colorBoton;
    }
    
    public String getColorBrighter(String color){
        Color colorBrighter = Color.decode(color).brighter();
        return String.format("#%02x%02x%02x", colorBrighter.getRed(), colorBrighter.getGreen(), colorBrighter.getBlue()); 
    }
    
    public String getColorDarker(String color){
        Color colorDarker = Color.decode(color).darker();
        return String.format("#%02x%02x%02x", colorDarker.getRed(), colorDarker.getGreen(), colorDarker.getBlue()); 
    }

}
