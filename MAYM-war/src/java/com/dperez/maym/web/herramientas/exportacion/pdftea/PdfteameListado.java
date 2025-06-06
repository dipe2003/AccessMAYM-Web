/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dperez.maym.web.herramientas.exportacion.pdftea;

import com.dperez.maym.web.empresa.Empresa;
import com.dperez.maym.web.herramientas.CargarArchivo;
import com.dperez.maymweb.modelo.acciones.Accion;
import com.dperez.maymweb.modelo.acciones.Estado;
import static com.dperez.maymweb.modelo.acciones.Estado.CERRADA;
import static com.dperez.maymweb.modelo.acciones.Estado.PENDIENTE;
import static com.dperez.maymweb.modelo.acciones.Estado.PROCESO_IMP;
import static com.dperez.maymweb.modelo.acciones.Estado.PROCESO_VER;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author dipe2
 */
@Named
public class PdfteameListado implements Serializable {

    private CargarArchivo cArchivo;
    // 1 pulgada = 72
    private Document documento = new Document();
    private Empresa empresa;
    private final Color colorFilaImpar = new Color(245, 245, 245);

    public PdfteameListado(Empresa empresa) {
        this.empresa = empresa;
        documento.setPageSize(PageSize.A4.rotate());
        documento.setMargins(18, 18, 90, 36);
        cArchivo = new CargarArchivo();
    }

    public PdfteameListado() {
    }

    public void ExportarListado(String nombreArchivo, String tituloListado, List<Accion> acciones) {

        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();

        try {
            response.setContentType("application/pdf");
            response.setHeader("Content-disposition", "inline; filename=" + nombreArchivo);
            OutputStream outputStream = response.getOutputStream();
            PdfWriter writer = PdfWriter.getInstance(documento, outputStream);

            TablaEncabezado event = new TablaEncabezado(CrearEncabezado(tituloListado), 16, 570);
            writer.setPageEvent(event);
            TablaPie eventoPie = new TablaPie();
            writer.setPageEvent(eventoPie);

            documento.open();

            // tabla listado de acciones, el total de columnas es fijo basado en los atributos que tiene la accion por defecto 11 (no inluye el tipo de Accion)            
            PdfPTable tabla = CrearTablaListado(11, new int[]{4, 9, 6, 17, 17, 17, 7, 4, 5, 7, 7});

             CrearFilaTitulo(tabla, false);

            // FILAS RESTANTES - Datos de Acciones
            int contador = 0;
            boolean impar = true;
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");

            for (Accion a : acciones) {
                tabla.addCell(CrearCeldaContenido(df.format(a.getFechaDeteccion()), impar, 0));
                tabla.addCell(CrearCeldaContenido(a.getAreaAccion().getNombre(), impar, 0));
                tabla.addCell(CrearCeldaContenido(a.getDeteccionAccion().getNombre(), impar, 0));
                tabla.addCell(CrearCeldaContenido(a.getDescripcion(), impar, 0));
                tabla.addCell(CrearCeldaContenido(a.getAnalisisCausa().equals("") ? "Sin Definir" : a.getAnalisisCausa(), impar, 0));
                tabla.getDefaultCell().setBorderColor(Color.decode(empresa.getOpcionesSistema().getColorPanelTitulo()).darker());
                // Celda ACTIVIDADES
                if (!a.getActividadesDeAccion().isEmpty()) {
                    PdfPTable tablaActividades = CrearTablaActividades(3, new int[]{17, 7, 4});
                    final boolean repImpar = impar;
                    a.getActividadesDeAccion().forEach((act) -> {
                        tablaActividades.addCell(CrearCeldaContenido(act.getTipoDeActividad().getDescripcion() + ": " + act.getDescripcion(), repImpar, 0)).setBorder(0);
                        tablaActividades.addCell(CrearCeldaContenido(act.getResponsableImplementacion().getResponsabilidadResponsable().getNombre(), repImpar, 0)).setBorder(0);
                        tablaActividades.addCell(CrearCeldaContenido(act.getFechaImplementacion() != null ? df.format(act.getFechaImplementacion()) : "Sin Definir", repImpar, 0)).setBorder(0);
                    });
                    PdfPCell celda = new PdfPCell();
                    celda.setColspan(3);
                    if (impar) {
                        celda.setBackgroundColor(colorFilaImpar);
                    }
                    celda.setBorderColor(Color.decode(empresa.getOpcionesSistema().getColorPanelTitulo()).darker());
                    //celda.setBorder(0);
                    celda.addElement(tablaActividades);
                    tabla.addCell(celda);
                    tabla.setComplete(true);
                } else {
                    tabla.addCell(CrearCeldaContenido("Sin Definir", impar, 3));
                    tabla.setComplete(true);
                }

                String fechaEficacia = "Sin Definir";
                if (a.getComprobacionEficacia() != null) {
                    if (a.getComprobacionEficacia().getFechaComprobacion() != null) {
                        fechaEficacia = df.format(a.getComprobacionEficacia().getFechaComprobacion());
                    } else {
                        if (a.getComprobacionEficacia().getFechaEstimada() != null) {
                            fechaEficacia = df.format(a.getComprobacionEficacia().getFechaEstimada());
                        }
                    }
                }
                tabla.addCell(CrearCeldaContenido(fechaEficacia, impar, 0));
                tabla.addCell(CrearCeldaContenido(a.getCodificacionAccion().getNombre(), impar, 0));
                Color colorFondo = ObtnerColor(a.getEstadoDeAccion());
                // se llama con false para asegurarse que no se cambie el color obtenido para el estado
                tabla.addCell(CrearCeldaContenido(a.getEstadoDeAccion().getDescripcion(), false, colorFondo));
                contador++;
                impar = contador % 2 == 0;
            }

            tabla.completeRow();
            documento.add(tabla);

            documento.close();
            outputStream.flush();
            context.responseComplete();
        } catch (IOException ex) {
            System.out.println("IO Error: " + ex.getMessage());
        } catch (DocumentException de) {
            System.out.println("Document Error: " + de.getMessage());
        }

    }

    public void ExportarPlanAccion(String nombreArchivo, String tituloPlan, List<Accion> acciones,
            String textoIntroduccion, Empresa empresa) {

        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();

        try {
            response.setContentType("application/pdf");
            response.setHeader("Content-disposition", "inline; filename=" + nombreArchivo);
            OutputStream outputStream = response.getOutputStream();
            PdfWriter writer = PdfWriter.getInstance(documento, outputStream);

            Accion accion = acciones.stream().findFirst().get();
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");

            TablaEncabezado event = new TablaEncabezado(CrearEncabezado(tituloPlan), 16, 570);
            writer.setPageEvent(event);
            TablaPie eventoPie = new TablaPie();
            writer.setPageEvent(eventoPie);

            documento.open();
            // TODO: Crear nuevo metodo por separado ya que debe incluir menos columnas (deteccion y fecha deben ser parte del titulo) y no lleva estado (OPCIONAL: incluir REFERNCIAS)           

            Phrase frase = new Phrase();

            frase.setFont(FontFactory.getFont("arialbi", 10, Font.NORMAL, Color.BLACK));
            frase.add(textoIntroduccion);
            documento.add(frase);

            // tabla listado de acciones, el total de columnas es fijo basado en los atributos que tiene la accion por defecto 11 (no inluye el tipo de Accion)            
            PdfPTable tabla = CrearTablaListado(10, new int[]{8, 7, 18, 18, 19, 7, 5, 5, 7, 6});

            CrearFilaTitulo(tabla, true);
            
            // FILAS RESTANTES - Datos de Acciones
            int contador = 0;
            boolean impar = true;

            for (Accion a : acciones) {
                tabla.addCell(CrearCeldaContenido(a.getAreaAccion().getNombre(), impar, 0));
                tabla.addCell(CrearCeldaContenido(a.getReferencias(), impar, 0));
                tabla.addCell(CrearCeldaContenido(a.getDescripcion(), impar, 0));
                tabla.addCell(CrearCeldaContenido(a.getAnalisisCausa().equals("") ? "Sin Definir" : a.getAnalisisCausa(), impar, 0));
                // Celda ACTIVIDADES
                if (a.getActividadesDeAccion().size() > 0) {
                    PdfPTable tablaActividades = CrearTablaActividades(3, new int[]{19, 8, 4});
                    final boolean repImpar = impar;
                    a.getActividadesDeAccion().forEach((act) -> {
                        tablaActividades.addCell(CrearCeldaContenido(act.getTipoDeActividad().getDescripcion() + ": " + act.getDescripcion(), repImpar, 0)).setBorder(0);
                        tablaActividades.addCell(CrearCeldaContenido(act.getResponsableImplementacion().getResponsabilidadResponsable().getNombre(), repImpar, 0)).setBorder(0);
                        tablaActividades.addCell(CrearCeldaContenido(act.getFechaImplementacion() != null ? df.format(act.getFechaImplementacion()) : "Sin Definir", repImpar, 0)).setBorder(0);
                    });
                    PdfPCell celda = new PdfPCell();
                    celda.setColspan(3);
                    if (impar) {
                        celda.setBackgroundColor(colorFilaImpar);
                    }
                    celda.setBorderColor(Color.decode(empresa.getOpcionesSistema().getColorPanelTitulo()).darker());
                    celda.addElement(tablaActividades);
                    tabla.addCell(celda);
                    tabla.setComplete(true);
                } else {
                    tabla.addCell(CrearCeldaContenido("Sin Definir", impar, 3));
                    tabla.setComplete(true);
                }

                String fechaEficacia = "Sin Definir";
                if (a.getComprobacionEficacia() != null) {
                    if (a.getComprobacionEficacia().getFechaComprobacion() != null) {
                        fechaEficacia = df.format(a.getComprobacionEficacia().getFechaComprobacion());
                    } else {
                        if (a.getComprobacionEficacia().getFechaEstimada() != null) {
                            fechaEficacia = df.format(a.getComprobacionEficacia().getFechaEstimada());
                        }
                    }
                }
                tabla.addCell(CrearCeldaContenido(fechaEficacia, impar, 0));
                tabla.addCell(CrearCeldaContenido(a.getCodificacionAccion().getNombre(), impar, 0));
                tabla.addCell(CrearCeldaContenido(a.getClass().getSimpleName(), impar, 0));
                contador++;
                impar = contador % 2 == 0;
            }

            tabla.completeRow();
            documento.add(tabla);

            documento.add(Chunk.NEWLINE);
            frase = new Phrase();
            frase.setFont(FontFactory.getFont("arialbi", 10, Font.NORMAL, Color.BLACK));
            frase.add("Atte.: __________________________________________");

            documento.add(frase);

            documento.close();
            outputStream.flush();
            context.responseComplete();
        } catch (IOException ex) {
            System.out.println("IO Error: " + ex.getMessage());
        } catch (DocumentException de) {
            System.out.println("Document Error: " + de.getMessage());
        }

    }

    private void CrearFilaTitulo(PdfPTable tabla, boolean esPlanAccion) {
        // FILA 1 - Titulos (1 por cada columna): Fecha|Area|Generada Por|Descripcion|Analisis de Causa|Actividades(Descripion|Responsable|Fecha)|Fecha Verif Eficacia|Codificacion|TipoAccion|Estado 
        if (!esPlanAccion) {
            tabla.addCell(CrearCeldaTitulo("Fecha", 0));
            tabla.addCell(CrearCeldaTitulo("Area", 0));
            tabla.addCell(CrearCeldaTitulo("Generada Por", 0));
            tabla.addCell(CrearCeldaTitulo("Descripcion", 0));
            tabla.addCell(CrearCeldaTitulo("Analisis de Causa", 0));

            //Actividades se divide en 3 columnas para Descripcion|Responsable|Fecha            
            tabla.addCell(CrearCeldaTitulo("Actividades", 0));
            tabla.addCell(CrearCeldaTitulo("Responsable", 0));
            tabla.addCell(CrearCeldaTitulo("Fecha", 0));

            tabla.addCell(CrearCeldaTitulo("Fecha Eficacia", 0));
            tabla.addCell(CrearCeldaTitulo("Codificacion", 0));
            tabla.addCell(CrearCeldaTitulo("Estado", 0));
        } else {
            // FILA 1 - Titulos (1 por cada columna): Fecha|Area|Generada Por|Descripcion|Analisis de Causa|Actividades(Descripion|Responsable|Fecha)|Fecha Verif Eficacia|Codificacion|TipoAccion|Estado 
            tabla.addCell(CrearCeldaTitulo("Area", 0));
            tabla.addCell(CrearCeldaTitulo("Referencias", 0));
            tabla.addCell(CrearCeldaTitulo("Descripcion", 0));
            tabla.addCell(CrearCeldaTitulo("Analisis de Causa", 0));

            //Actividades se divide en 3 columnas para Descripcion|Responsable|Fecha            
            tabla.addCell(CrearCeldaTitulo("Actividades", 0));
            tabla.addCell(CrearCeldaTitulo("Responsable", 0));
            tabla.addCell(CrearCeldaTitulo("Fecha", 0));

            tabla.addCell(CrearCeldaTitulo("Fecha Eficacia", 0));
            tabla.addCell(CrearCeldaTitulo("Codificacion", 0));
            tabla.addCell(CrearCeldaTitulo("Tipo", 0));
        }
        tabla.getDefaultCell().setBorderColor(Color.decode(empresa.getOpcionesSistema().getColorPanelTitulo()).darker());
    }

    private Color ObtnerColor(Estado estado) {
        Color colorFondo;
        switch (estado) {
            case CERRADA ->
                colorFondo = new Color(170, 240, 190);
            case PROCESO_IMP ->
                colorFondo = new Color(240, 180, 60);
            case PROCESO_VER ->
                colorFondo = new Color(240, 240, 60);
            case PENDIENTE ->
                colorFondo = new Color(235, 135, 130);
            case DESESTIMADA ->
                colorFondo = new Color(120, 130, 150);
            default ->
                colorFondo = Color.WHITE;
        }
        return colorFondo;
    }

    private PdfPCell CrearCeldaTitulo(String textoTitulo, int colspan) {
        PdfPCell celda = new PdfPCell();
        celda.setColspan(colspan);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);

        Color color = Color.decode(empresa.getOpcionesSistema().getColorPanelTitulo());

        celda.setBackgroundColor(color);
        celda.setFixedHeight(22);
        Phrase texto = new Phrase();

        color = Color.decode(empresa.getOpcionesSistema().getColorFuentePanelTitulo());

        texto.setFont(FontFactory.getFont("arialbi", 8, Font.BOLD, color));
        texto.add(textoTitulo);
        celda.setPhrase(texto);
        celda.setBorderColor(Color.decode(empresa.getOpcionesSistema().getColorPanelTitulo()).darker());
        return celda;
    }

    private PdfPCell CrearCeldaContenido(String textoContenido, boolean colorFondoImpar, int colSpan) {
        PdfPCell celda = new PdfPCell();
        celda.setColspan(colSpan);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        if (colorFondoImpar) {
            celda.setBackgroundColor(colorFilaImpar);
        }

        Phrase texto = new Phrase();
        texto.setFont(FontFactory.getFont("arialbi", 6, Font.NORMAL, Color.BLACK));
        texto.add(textoContenido);
        celda.setPhrase(texto);
        celda.setBorderColor(Color.decode(empresa.getOpcionesSistema().getColorPanelTitulo()).darker());
        return celda;
    }

    private PdfPCell CrearCeldaContenido(String textoContenido, boolean colorFondoImpar, Color colorCelda) {
        PdfPCell celda = new PdfPCell();
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);

        if (colorFondoImpar) {
            celda.setBackgroundColor(colorFilaImpar);
        } else {
            celda.setBackgroundColor(colorCelda);
        }
        Phrase texto = new Phrase();
        texto.setFont(FontFactory.getFont("arialbi", 6, Font.NORMAL, Color.BLACK));
        texto.add(textoContenido);
        celda.setPhrase(texto);
        celda.setBorderColor(Color.decode(empresa.getOpcionesSistema().getColorPanelTitulo()).darker());
        return celda;
    }

    private PdfPTable CrearTablaListado(int totalColumnas, int anchosPorCiento[]) {
        PdfPTable tabla = new PdfPTable(totalColumnas);
        tabla.setWidthPercentage(100f);
        tabla.setHeaderRows(1);
        tabla.setTotalWidth(100f);
        tabla.setWidths(anchosPorCiento);
        tabla.getDefaultCell().setBorderWidthBottom(1);
        tabla.getDefaultCell().setBorderWidthTop(1);
        tabla.getDefaultCell().setBorderWidthRight(1);
        tabla.getDefaultCell().setBorderWidthLeft(1);
        tabla.getDefaultCell().setBorderColor(Color.decode(empresa.getOpcionesSistema().getColorPanelTitulo()).darker());
        return tabla;
    }

    private PdfPTable CrearTablaActividades(int totalColumnas, int anchosPorCiento[]) {
        PdfPTable tabla = new PdfPTable(totalColumnas);
        tabla.setWidthPercentage(100f);
        tabla.setTotalWidth(100f);
        tabla.setWidths(anchosPorCiento);
        tabla.getDefaultCell().setBorderWidthBottom(0);
        tabla.getDefaultCell().setBorderWidthTop(0);
        tabla.getDefaultCell().setBorderWidthRight(0);
        tabla.getDefaultCell().setBorderWidthLeft(0);
        tabla.getDefaultCell().setBorder(0);
        tabla.getDefaultCell().setPadding(0);
        tabla.getDefaultCell().setBorderColor(Color.decode(empresa.getOpcionesSistema().getColorPanelTitulo()).darker());
        return tabla;
    }

    ////// <summary>
    /// Crea la tabla que aloja el encabezado y la completa con los datos.
    /// </summary>
    /// <param name="tituloImpresion"></param>
    /// <returns></returns>
    private PdfPTable CrearEncabezado(String tituloImpresion) throws IOException {
        PdfPTable tablaEncabezado = new PdfPTable(4);
        tablaEncabezado.setWidthPercentage(100f);
        tablaEncabezado.setTotalWidth(810.68f);
        tablaEncabezado.setWidths(new int[]{100, 250, 226, 203});
        tablaEncabezado.getDefaultCell().setBorder(0);
        tablaEncabezado.getDefaultCell().setBorderWidth(0);

        tablaEncabezado.addCell(CrearLogo());
        tablaEncabezado.addCell(CrearDatosEmpresa());
        tablaEncabezado.addCell(new Phrase("M.A.Y.M-WebApp"));
        tablaEncabezado.addCell(new Phrase(""));
        tablaEncabezado.addCell(CrearTituloImpresion(tituloImpresion));

        tablaEncabezado.setSpacingAfter(5);

        return tablaEncabezado;
    }

    /// <summary>
    /// Genera la celda e inserta el logo de la empresa.
    /// </summary>
    /// <returns></returns>
    private PdfPCell CrearLogo() throws IOException {
        Phrase frase = new Phrase();
        Image logo = null;
        try {
            String realPath = cArchivo.getHome() + cArchivo.getSeparator() + "MAYMWEB" + cArchivo.getSeparator();
            logo = Image.getInstance(realPath + empresa.getUbicacionLogoInformes());
        } catch (FileNotFoundException ex) {
            logo = Image.getInstance(FacesContext.getCurrentInstance().getExternalContext().getResource("/Resources/Images/logo_work.jpg"));
        } catch (IOException ex) {
            ex.getMessage();
            frase.add("[No se pudo cargar logo]");
        }
        if (logo != null) {
            logo.scaleToFit(100, 40);
            logo.setAlignment(Element.ALIGN_LEFT);
            Chunk chLogo = new Chunk(logo, 0, 0, true);
            frase.add(chLogo);
        }
        PdfPCell celda = new PdfPCell(frase);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        celda.setBorder(0);
        celda.setBorderWidth(0);
        return celda;
    }

    /// <summary>
    /// Genera la celda con los datos de la empresa
    /// </summary>
    /// <returns></returns>
    private PdfPCell CrearDatosEmpresa() {

        Phrase frase = new Phrase();
        frase.setFont(FontFactory.getFont("arialbi", 8, Font.NORMAL, Color.BLACK));
        Chunk texto;
        if (empresa.getNombreExtra() != null && empresa.getNombreExtra() != "") {
            texto = new Chunk(empresa.getNombre() + " | " + empresa.getNombreExtra());
        } else {
            texto = new Chunk(empresa.getNombre());
        }
        texto.setFont(FontFactory.getFont("arialbi", 9, Font.BOLD, Color.BLACK));
        frase.add(texto);
        frase.add(Chunk.NEWLINE);
        frase.add(new Chunk(empresa.getDireccion()));
        frase.add(Chunk.NEWLINE);
        if (empresa.getMovil() != "") {
            frase.add(new Chunk("Tel: " + empresa.getTelefono() + " | Movil: " + empresa.getMovil()));
        } else {
            frase.add(new Chunk("Tel: " + empresa.getTelefono()));
        }
        if (empresa.getCorreo() != "") {
            frase.add(Chunk.NEWLINE);
            frase.add(new Chunk(empresa.getCorreo()));
        }
        PdfPCell celda = new PdfPCell(frase);
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        celda.setBorder(0);
        celda.setBorderWidth(0);
        return celda;

    }

    /// <summary>
    /// Genera celda con la barra de tiulo
    /// </summary>
    /// <param name="titulo"></param>
    /// <returns></returns>
    private PdfPCell CrearTituloImpresion(String titulo) {
        Phrase f = new Phrase();

        Color color = Color.decode(empresa.getOpcionesSistema().getColorFuentePanelEncabezado());

        f.setFont(FontFactory.getFont("arialbi", 10, Font.BOLD, color));
        f.add(titulo.toUpperCase());

        PdfPCell celda = new PdfPCell();
        celda.setColspan(4);
        celda.setPaddingTop(-5f);

        color = Color.decode(empresa.getOpcionesSistema().getColorInferiorPanelTitulo());

        celda.setBackgroundColor(color);
        celda.setFixedHeight(18);
        celda.setBorder(0);
        celda.setBorderWidth(0);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.addElement(f);
        return celda;
    }

}
