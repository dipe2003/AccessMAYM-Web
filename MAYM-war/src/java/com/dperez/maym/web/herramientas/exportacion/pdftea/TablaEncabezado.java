/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dperez.maym.web.herramientas.exportacion.pdftea;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

/**
 *
 * @author dipe2
 */
public class TablaEncabezado extends PdfPageEventHelper{
    
    PdfPTable tablaEncabezado;
    private int xPos;
    private int yPos;
    
    public TablaEncabezado(PdfPTable encabezado, int xPos, int yPos){
        tablaEncabezado = encabezado;
        this.xPos = xPos;
        this.yPos = yPos;
    }
    
    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        try {            
            tablaEncabezado.writeSelectedRows(0, -1, xPos, yPos, writer.getDirectContent());
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
    
    
   
}


