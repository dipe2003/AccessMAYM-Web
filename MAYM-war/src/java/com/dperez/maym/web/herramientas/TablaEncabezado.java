/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dperez.maym.web.herramientas;

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
    
    public TablaEncabezado(PdfPTable encabezado){
        tablaEncabezado = encabezado;
    }
    
    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        try {            
            tablaEncabezado.writeSelectedRows(0, -1, 16, 570, writer.getDirectContent());
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
    
    
   
}


