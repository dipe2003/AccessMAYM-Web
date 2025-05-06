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
import com.dperez.maymweb.modelo.acciones.actividad.Actividad;
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
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author dipe2
 */
@Named
public class PdfteameRegistro implements Serializable {

    private CargarArchivo cArchivo;
    private Empresa empresa;

    // 1 pulgada = 72
    private Document documento = new Document(PageSize.A4, 17, 19, 72, 18);

    private final Font fuenteTituloSeccion = FontFactory.getFont("arialbi", 10, Font.BOLD, new Color(0, 0, 0));
    private final Font fuenteTitulosCelda = FontFactory.getFont("arialbi", 9, Font.BOLD, new Color(0, 0, 0));
    private final Font fuenteContenidoCelda = FontFactory.getFont("arialbi", 8, Font.NORMAL, new Color(0, 0, 0));
    private final Color colorFilaImpar = new Color(245,245,245);

    public PdfteameRegistro(Empresa empresa) {
        this.empresa = empresa;
        cArchivo = new CargarArchivo();
    }

    public void ExportarRegistro(String nombreArchivo, String tituloListado, Accion accion, boolean excluirComprobaciones) {

        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();

        try {
            response.setContentType("application/pdf");
            response.setHeader("Content-disposition", "inline; filename=" + nombreArchivo);
            OutputStream outputStream = response.getOutputStream();
            PdfWriter writer = PdfWriter.getInstance(documento, outputStream);

            TablaEncabezado event = new TablaEncabezado(CrearEncabezado(tituloListado), 16, 830);
            writer.setPageEvent(event);

            documento.open();

            documento.add(CrearTituloSeccion("General"));
            PdfPTable tablaGeneral = CrearTablaRegistro(5, new int[]{139, 139, 2, 140, 140});

            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");

            // FILA 1 - Titulos (1 por cada columna): Fecha|Area|Generada Por|Descripcion|Analisis de Causa|Actividades(Descripion|Responsable|Fecha)|Fecha Verif Eficacia|Codificacion|TipoAccion|Estado 
            tablaGeneral.addCell(new PdfPCell(CrearCeldaTitulo("Fecha", 0)));
            tablaGeneral.addCell(new PdfPCell(CrearCeldaTitulo("Area/Sector", 0)));
            tablaGeneral.addCell(CrearCeldaVacia(0, false));

            tablaGeneral.addCell(CrearCeldaTitulo("Generada Por", 2));

            tablaGeneral.addCell(CrearCeldaContenido(df.format(accion.getFechaDeteccion()), false, 0, 0));
            tablaGeneral.addCell(CrearCeldaContenido(accion.getAreaAccion().getNombre(), false, 0, 0));
            tablaGeneral.addCell(CrearCeldaVacia(0, false));
            tablaGeneral.addCell(CrearCeldaContenido(accion.getDeteccionAccion().getNombre(), false, 2, 0));

            tablaGeneral.addCell(CrearCeldaVacia(5, false));
            tablaGeneral.addCell(CrearCeldaTitulo("Descripcion", 2));
            tablaGeneral.addCell(CrearCeldaVacia(0, false));
            tablaGeneral.addCell(CrearCeldaTitulo("Analisis de Causa", 2));
            tablaGeneral.addCell(CrearCeldaContenido(accion.getDescripcion(), false, 2, 150));
            tablaGeneral.addCell(CrearCeldaVacia(0, false));
            tablaGeneral.addCell(CrearCeldaContenido(accion.getAnalisisCausa().equals("") ? "Sin Definir" : accion.getAnalisisCausa(), false, 3, 150));
            tablaGeneral.addCell(CrearCeldaVacia(5, false));
            tablaGeneral.addCell(CrearCeldaTitulo("Referencias", 2));
            tablaGeneral.addCell(CrearCeldaContenido(accion.getReferencias().equals("") ? "--" : accion.getReferencias(), false, 3, 18));

            // separacion antes de codificacion y producto involucrado
            tablaGeneral.addCell(CrearCeldaVacia(5, false));
            tablaGeneral.addCell(new PdfPCell(CrearCeldaTitulo("Codificacion:", 2)));
            tablaGeneral.addCell(CrearCeldaContenido(accion.getCodificacionAccion().getNombre(), false, 3, 18));
            tablaGeneral.addCell(CrearCeldaVacia(5, false));

            tablaGeneral.setComplete(true);
            documento.add(tablaGeneral);

            if (accion.getProductosInvolucrados().size() > 0) {
                documento.add(CrearTituloSeccion("Producto Involucrado"));
                tablaGeneral.addCell(CrearCeldaVacia(5, false));
                PdfPTable tablaProducto = CrearTablaRegistro(2, new int[]{50, 50});
                tablaProducto.addCell(CrearCeldaTitulo("Denominacion", 0));
                tablaProducto.addCell(CrearCeldaTitulo("Datos", 0));
                for (var prod : accion.getProductosInvolucrados()) {
                    tablaProducto.addCell(CrearCeldaContenido(prod.getNombre(), false, 0, 18));
                    tablaProducto.addCell(CrearCeldaContenido(prod.getDatos(), false, 0, 18));
                }
                tablaProducto.addCell(CrearCeldaVacia(5, false));
                documento.add(tablaProducto);
            }

            // FILAS RESTANTES - Datos de Acciones
            documento.add(CrearTituloSeccion("Actividades"));

            //Actividades se divide en 3 columnas para Descripcion|Responsable|Fecha         
            PdfPTable tablaActividades = CrearTablaRegistro(3, new int[]{17, 7, 4});
            tablaActividades.addCell(CrearCeldaTitulo("Actividades", 0));
            tablaActividades.addCell(CrearCeldaTitulo("Responsable", 0));
            tablaActividades.addCell(CrearCeldaTitulo("Fecha", 0));

            // Celda ACTIVIDADES
            int contador = 1;
            boolean filaPar = false;
            if (accion.getActividadesDeAccion().size() > 0) {
                for (Actividad act : accion.getActividadesDeAccion()) {

                    tablaActividades.addCell(CrearCeldaContenido(act.getTipoDeActividad().getDescripcion() + ": " + act.getDescripcion(), filaPar, 0, 0));
                    tablaActividades.addCell(CrearCeldaContenido(act.getResponsableImplementacion().getResponsabilidadResponsable().getNombre(), filaPar, 0, 0));
                    tablaActividades.addCell(CrearCeldaContenido(act.getFechaImplementacion() != null ? df.format(act.getFechaImplementacion()) : "Sin Definir", filaPar, 0, 0));
                    contador++;
                    filaPar = contador % 2 == 0;
                }
            } else {
                tablaActividades.addCell(CrearCeldaContenido("Sin Definir", filaPar, 3, 50));
            }
            tablaActividades.setComplete(true);
            documento.add(tablaActividades);

            if (excluirComprobaciones != true) {
                documento.add(CrearTituloSeccion("Comprobacion de Implementación"));

                PdfPTable tablaImplementacion = CrearTablaRegistro(3, new int[]{20, 20, 60});

                tablaImplementacion.addCell(CrearCeldaTitulo("Fecha Estimada", 0));
                tablaImplementacion.addCell(CrearCeldaTitulo("Fecha Comprobacion", 0));
                tablaImplementacion.addCell(CrearCeldaTitulo("Responsable Comprobacion", 0));

                String implementacion = "Sin Definir";

                if (accion.getComprobacionImplementacion() != null) {
                    if (accion.getComprobacionImplementacion().getFechaEstimada() != null) {
                        implementacion = df.format(accion.getComprobacionImplementacion().getFechaEstimada());
                    }
                    tablaImplementacion.addCell(CrearCeldaContenido(implementacion, false, 0, 18));
                    implementacion = "Sin Comprobar";

                    if (accion.getComprobacionImplementacion().getFechaComprobacion() != null) {
                        implementacion = df.format(accion.getComprobacionImplementacion().getFechaComprobacion());
                    }
                    tablaImplementacion.addCell(CrearCeldaContenido(implementacion, false, 0, 18));
                    implementacion = "Sin Definir";

                    if (accion.getComprobacionImplementacion().getResponsableComprobacion() != null) {
                        implementacion = accion.getComprobacionImplementacion().getResponsableComprobacion().getUsuarioResponsable().getNombreCompleto() + " - "
                                + accion.getComprobacionImplementacion().getResponsableComprobacion().getResponsabilidadResponsable().getNombre();
                    }
                    tablaImplementacion.addCell(CrearCeldaContenido(implementacion, false, 0, 18));
                    implementacion = "Sin Definir";

                    if (accion.getComprobacionImplementacion().getResultadoComprobacion() != null) {
                        implementacion = accion.getComprobacionImplementacion().getResultadoComprobacion().getDescripcion();
                    }
                    tablaImplementacion.addCell(CrearCeldaVacia(3, false));
                    tablaImplementacion.addCell(CrearCeldaTitulo("Resultados de Comprobacion", 0));
                    tablaImplementacion.addCell(CrearCeldaContenido(accion.getComprobacionImplementacion().getResultadoComprobacion().getDescripcion().equals("") ? "Sin Observaciones"
                            : accion.getComprobacionImplementacion().getResultadoComprobacion().getDescripcion() + ": "
                            + accion.getComprobacionImplementacion().getObservaciones(), false, 2, 0));
                } else {
                    tablaImplementacion.addCell(CrearCeldaContenido(implementacion, false, 3, 0));
                }

                tablaImplementacion.setComplete(true);
                documento.add(tablaImplementacion);

                documento.add(CrearTituloSeccion("Verificación de Eficacia"));

                PdfPTable tablaEficacia = CrearTablaRegistro(3, new int[]{20, 20, 60});

                tablaEficacia.addCell(CrearCeldaTitulo("Fecha Estimada", 0));
                tablaEficacia.addCell(CrearCeldaTitulo("Fecha Verificacion", 0));
                tablaEficacia.addCell(CrearCeldaTitulo("Responsable Verificacion", 0));

                String eficacia = "Sin Definir";

                if (accion.getComprobacionEficacia() != null) {
                    if (accion.getComprobacionEficacia().getFechaEstimada() != null) {
                        eficacia = df.format(accion.getComprobacionEficacia().getFechaEstimada());
                    }
                    tablaEficacia.addCell(CrearCeldaContenido(eficacia, false, 0, 18));
                    eficacia = "Sin Verificar";

                    if (accion.getComprobacionEficacia().getFechaComprobacion() != null) {
                        eficacia = df.format(accion.getComprobacionEficacia().getFechaComprobacion());
                    }
                    tablaEficacia.addCell(CrearCeldaContenido(eficacia, false, 0, 18));
                    eficacia = "Sin Definir";

                    if (accion.getComprobacionEficacia().getResponsableComprobacion() != null) {
                        eficacia = accion.getComprobacionEficacia().getResponsableComprobacion().getUsuarioResponsable().getNombreCompleto() + " - "
                                + accion.getComprobacionEficacia().getResponsableComprobacion().getResponsabilidadResponsable().getNombre();
                    }
                    tablaEficacia.addCell(CrearCeldaContenido(eficacia, false, 0, 18));
                    eficacia = "Sin Definir";
                    if (accion.getComprobacionEficacia().getResultadoComprobacion() != null) {
                        eficacia = accion.getComprobacionEficacia().getResultadoComprobacion().getDescripcion();
                    }
                    tablaEficacia.addCell(CrearCeldaVacia(3, false));
                    tablaEficacia.addCell(CrearCeldaTitulo("Resultados de Comprobacion", 0));
                    tablaEficacia.addCell(CrearCeldaContenido(accion.getComprobacionEficacia().getResultadoComprobacion().getDescripcion().equals("") ? "Sin Observaciones"
                            : accion.getComprobacionEficacia().getResultadoComprobacion().getDescripcion() + ": "
                            + accion.getComprobacionEficacia().getObservaciones(), false, 2, 0));
                } else {
                    tablaEficacia.addCell(CrearCeldaContenido(eficacia, false, 3, 0));
                }

                tablaEficacia.setComplete(true);
                documento.add(tablaEficacia);

                documento.add(CrearTituloSeccion(""));
                PdfPTable tablaEstado = CrearTablaRegistro(2, new int[]{20, 80});
                Color colorFondo = ObtenerColor(accion.getEstadoDeAccion());
                tablaEstado.addCell(CrearCeldaTitulo("Estado", 0));
                tablaEstado.addCell(CrearCeldaContenido(accion.getEstadoDeAccion().getDescripcion(), false, colorFondo));

                if (accion.getEstadoDeAccion() == Estado.DESESTIMADA) {
                    tablaEstado.addCell(CrearCeldaTitulo("Motivo Desestimada", 0));
                    tablaEstado.addCell(CrearCeldaContenido(accion.getObservacionesDesestimada(), false, 0, 20));
                }

                tablaEstado.setComplete(true);
                documento.add(tablaEstado);
            }
            documento.close();
            outputStream.flush();
            context.responseComplete();
        } catch (IOException ex) {
            System.out.println("IO Error: " + ex.getMessage());
        } catch (DocumentException de) {
            System.out.println("Document Error: " + de.getMessage());
        }

    }

    private Color ObtenerColor(Estado estado) {
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
        Phrase texto = new Phrase();

        color = Color.decode(empresa.getOpcionesSistema().getColorFuentePanelTitulo());
        fuenteTitulosCelda.setColor(color);

        texto.setFont(fuenteTitulosCelda);
        texto.add(textoTitulo);
        celda.setPhrase(texto);
        return celda;
    }

    private PdfPCell CrearCeldaContenido(String textoContenido, boolean colorFondoPar, int colSpan, int minAltura) {
        PdfPCell celda = new PdfPCell();
        celda.setColspan(colSpan);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        if (colorFondoPar) {
            celda.setBackgroundColor(colorFilaImpar);
        }
        celda.setMinimumHeight(25);
        if (minAltura != 0) {
            celda.setMinimumHeight(minAltura);
        }
        Phrase texto = new Phrase();
        texto.setFont(fuenteContenidoCelda);
        texto.add(textoContenido);
        celda.setPhrase(texto);
        return celda;
    }

    private PdfPCell CrearCeldaContenido(String textoContenido, boolean colorFondoImpar, Color colorCelda) {
        PdfPCell celda = new PdfPCell();
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        celda.setMinimumHeight(25);
        if (colorFondoImpar) {
            celda.setBackgroundColor(colorFilaImpar);
        } else {
            celda.setBackgroundColor(colorCelda);
        }
        Phrase texto = new Phrase();
        texto.setFont(fuenteContenidoCelda);
        texto.add(textoContenido);
        celda.setPhrase(texto);
        return celda;
    }

    private PdfPCell CrearCeldaVacia(int colspan, boolean colorFondoImpar) {
        PdfPCell celda = new PdfPCell();
        if (colorFondoImpar) {
            celda.setBackgroundColor(colorFilaImpar);
        }
        celda.setColspan(colspan);
        celda.setBorderWidthBottom(0);
        celda.setBorderWidthTop(0);
        celda.setBorderWidthRight(0);
        celda.setBorderWidthLeft(0);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celda.setPhrase(new Phrase(""));
        return celda;

    }

    private PdfPTable CrearTablaRegistro(int totalColumnas, int anchosPorCiento[]) {
        PdfPTable tabla = new PdfPTable(totalColumnas);
        tabla.setWidthPercentage(100f);
        tabla.setTotalWidth(100f);
        tabla.setWidths(anchosPorCiento);
        tabla.getDefaultCell().setBorderWidthBottom(1);
        tabla.getDefaultCell().setBorderWidthTop(1);
        tabla.getDefaultCell().setBorderWidthRight(1);
        tabla.getDefaultCell().setBorderWidthLeft(1);
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
        tablaEncabezado.setTotalWidth(560.0f);
        tablaEncabezado.setWidths(new int[]{81, 220, 131, 131});
        tablaEncabezado.getDefaultCell().setBorder(0);
        tablaEncabezado.getDefaultCell().setBorderWidth(0);

        tablaEncabezado.addCell(CrearLogo());
        tablaEncabezado.addCell(CrearDatosEmpresa());
        tablaEncabezado.addCell(new Phrase(""));
        tablaEncabezado.addCell(new Phrase("M.A.Y.M-WebApp")).setVerticalAlignment(Element.ALIGN_RIGHT);
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
            logo.scaleToFit(80, 40);
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
        if (empresa.getNumHabilitacion() != "") {
            texto = new Chunk(empresa.getNombre() + " | Hab. " + empresa.getNumHabilitacion());
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

    private PdfPTable CrearTituloSeccion(String titulo) {
        PdfPTable tablaTitulo = new PdfPTable(1);
        tablaTitulo.setWidthPercentage(100);
        Phrase frase = new Phrase(titulo, fuenteTituloSeccion);
        PdfPCell celda = new PdfPCell();
        celda.setBorder(0);
        celda.enableBorderSide(2);
        celda.addElement(frase);
        tablaTitulo.addCell(celda);
        tablaTitulo.addCell(CrearCeldaVacia(0, false));
        return tablaTitulo;
    }
}
