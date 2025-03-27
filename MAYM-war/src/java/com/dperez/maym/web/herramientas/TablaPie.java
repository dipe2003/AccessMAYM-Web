/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dperez.maym.web.herramientas;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

/**
 *
 * @author dipe2
 */
public class TablaPie extends PdfPageEventHelper{
    
    private PdfTemplate totalPagesTemplate;
    private BaseFont baseFont;


    
    public TablaPie(){
        
    }
    
       @Override
    public void onOpenDocument(PdfWriter writer, Document document) {
        // Crear el marcador para el total de páginas
        totalPagesTemplate = writer.getDirectContent().createTemplate(50, 50);
        try {
            baseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        try {            
            PdfContentByte cb = writer.getDirectContent();
        Font font = new Font(Font.HELVETICA, 8, Font.NORMAL);

        // Texto con el número de página actual y marcador del total
        String text = "Página " + writer.getPageNumber() + " de ";
        Phrase phrase = new Phrase(text, font);

        // Posicionar el texto en el centro del pie de página
        ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, phrase,
                (document.right() - document.left()) / 2 + document.leftMargin(),
                document.bottom() - 20, 0);

        // Añadir el marcador para el total de páginas
        float textWidth = baseFont.getWidthPoint(text, 10);
        cb.addTemplate(totalPagesTemplate,
                (document.right() - document.left()) / 2 + document.leftMargin() + textWidth/2,
                document.bottom() - 20);   
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
    
     @Override
    public void onCloseDocument(PdfWriter writer, Document document) {
        totalPagesTemplate.beginText();        
        totalPagesTemplate.setFontAndSize(baseFont, 8);

        totalPagesTemplate.showText(String.valueOf(writer.getPageNumber() - 1));
        totalPagesTemplate.endText();

    }

    
    
   
}


