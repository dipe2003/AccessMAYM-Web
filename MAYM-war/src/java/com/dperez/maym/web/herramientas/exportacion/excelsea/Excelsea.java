/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dperez.maym.web.herramientas.exportacion.excelsea;

import com.dperez.maymweb.modelo.acciones.Accion;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author dipe2
 */
public class Excelsea {

    Workbook workbook = new HSSFWorkbook();

    private final CellStyle estiloEncabezadoTabla = workbook.createCellStyle();
    private final CellStyle estiloContenidoTabla = workbook.createCellStyle();
    private final CellStyle estiloContenidoTablaFecha = workbook.createCellStyle();
    private final HSSFFont fuenteEncabezado = ((HSSFWorkbook) workbook).createFont();
    private final HSSFFont fuenteContenido = ((HSSFWorkbook) workbook).createFont();

    private final String[] encabezados = new String[]{
        "Id",
        "Fecha Deteccion",
        "Area",
        "Generada Por",
        "Descripcion",
        "Analisis de Causa",
        "Tipo de Actividad",
        "Actividad",
        "Fecha Estimada Actividad",
        "Responsable Actividad",
        "Fecha Actividad",
        "Fecha Estimada Implementacion",
        "Responsable Implementacion",
        "Fecha Implementacion",
        "Fecha Estimada Eficacia",
        "Responsable Eficacia",
        "Fecha Eficacia",
        "Codificacion",
        "Estado"
    };

    private final String[] encabezadosSinActividades = new String[]{
        "Id",
        "Fecha Deteccion",
        "Area",
        "Generada Por",
        "Descripcion",
        "Analisis de Causa",
        "Fecha Estimada Implementacion",
        "Responsable Implementacion",
        "Fecha Implementacion",
        "Fecha Estimada Eficacia",
        "Responsable Eficacia",
        "Fecha Eficacia",
        "Codificacion",
        "Estado"
    };

    public Excelsea() {

        fuenteEncabezado.setFontName("Arial");
        fuenteEncabezado.setFontHeightInPoints((short) 11);
        fuenteEncabezado.setBold(true);

        fuenteContenido.setFontName("Arial");
        fuenteContenido.setFontHeightInPoints((short) 10);
        fuenteContenido.setBold(false);

        estiloContenidoTabla.setWrapText(true);
        estiloContenidoTabla.setAlignment(HorizontalAlignment.LEFT);
        estiloContenidoTabla.setVerticalAlignment(VerticalAlignment.CENTER);
        estiloContenidoTabla.setFont(fuenteContenido);
        estiloContenidoTabla.setBorderBottom(BorderStyle.THIN);
        estiloContenidoTabla.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        estiloContenidoTabla.setShrinkToFit(true);

        estiloEncabezadoTabla.cloneStyleFrom(estiloContenidoTabla);
        estiloEncabezadoTabla.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        estiloEncabezadoTabla.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        estiloEncabezadoTabla.setFont(fuenteEncabezado);
        estiloEncabezadoTabla.setBorderBottom(BorderStyle.MEDIUM);

        estiloContenidoTablaFecha.cloneStyleFrom(estiloContenidoTabla);
        estiloContenidoTablaFecha.setDataFormat((short) 14);
    }

    public void ExportarLibroExcel(List<Accion> acciones, String tituloArchivo, boolean conActividades) {

        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();

        Sheet sheet = workbook.createSheet("Acciones");

        if (conActividades) {
            CrearEncabezadosTabla(sheet, encabezados);
            CrearContenidoTabla(sheet, acciones, true);
        } else {
            CrearEncabezadosTabla(sheet, encabezadosSinActividades);
            //CrearContenidoTablaSinActividades(sheet, acciones);
            CrearContenidoTabla(sheet, acciones, false);
        }

        try {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment; filename=" + tituloArchivo + ".xls");
            OutputStream outputStream = response.getOutputStream();

            ByteArrayOutputStream outStream = new ByteArrayOutputStream();

            workbook.write(outStream);
            workbook.close();

            outputStream.write(outStream.toByteArray());
            outputStream.flush();
            outputStream.close();

            context.responseComplete();
        } catch (IOException ex) {
            System.out.println("IO ERROR" + ex.getMessage());
        }
    }

    private void CrearEncabezadosTabla(Sheet hoja, String[] encabezadosDeTabla) {
        Row titulos = hoja.createRow(0);
        Cell celdaTitulo = null;

        for (int i = 0; i < encabezadosDeTabla.length; i++) {
            celdaTitulo = titulos.createCell(i);
            hoja.setColumnWidth(i, 6000);
            celdaTitulo.setCellValue(encabezadosDeTabla[i]);
            celdaTitulo.setCellStyle(estiloEncabezadoTabla);
        }
    }

    private void CrearContenidoTabla(Sheet hoja, List<Accion> acciones, boolean conActividades) {
        int[] numFila = new int[]{1};
        for (var a : acciones) {
            AgregarRegistro(a, hoja, numFila, conActividades);
        }
    }

    private void AgregarRegistro(Accion a, Sheet hoja, int[] numFila, boolean conActividades) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyy");
        Cell celda = null;
        Row fila = null;
        int[] columnaActual;
        int i = 0;
        // argg
        do {
            columnaActual = new int[]{0};
            fila = hoja.createRow(numFila[0]++);
            celda = getNextCelda(fila, columnaActual);
            celda.setCellValue(a.getId());

            celda = getNextCelda(fila, columnaActual);
            try {
                celda.setCellValue(df.parse(df.format(a.getFechaDeteccion())));
            } catch (ParseException ex) {
                celda.setCellValue("");
            }
            celda.setCellStyle(estiloContenidoTablaFecha);

            celda = getNextCelda(fila, columnaActual);
            celda.setCellValue(a.getAreaAccion().getNombre());

            celda = getNextCelda(fila, columnaActual);
            celda.setCellValue(a.getDeteccionAccion().getNombre());

            celda = getNextCelda(fila, columnaActual);
            celda.setCellValue(a.getDescripcion());

            celda = getNextCelda(fila, columnaActual);
            celda.setCellValue(a.getAnalisisCausa());

            if (conActividades) {
                agregarActividad(celda, fila, columnaActual, i, a, df);
                i++;
            } else {
                i = a.getActividadesDeAccion().size();
            }

            if (a.getComprobacionImplementacion() != null) {
                celda = getNextCelda(fila, columnaActual);
                try {
                    celda.setCellValue(df.parse(df.format(a.getComprobacionImplementacion().getFechaEstimada())));
                } catch (ParseException ex) {
                    celda.setCellValue("");
                }
                celda.setCellStyle(estiloContenidoTablaFecha);

                celda = getNextCelda(fila, columnaActual);
                celda.setCellValue(a.getComprobacionImplementacion().getResponsableComprobacion().getResponsabilidadResponsable().getNombre());

                if (a.getComprobacionImplementacion().getFechaComprobacion() != null) {
                    celda = getNextCelda(fila, columnaActual);
                    try {
                        celda.setCellValue(df.parse(df.format(a.getComprobacionImplementacion().getFechaComprobacion())));
                    } catch (ParseException ex) {
                        celda.setCellValue("");
                    }
                    celda.setCellStyle(estiloContenidoTablaFecha);
                } else {
                    celda = getNextCelda(fila, columnaActual);
                    celda.setCellValue("");
                }
            } else {
                celda = getNextCelda(fila, columnaActual);
                celda.setCellValue("");
                celda = getNextCelda(fila, columnaActual);
                celda.setCellValue("Sin Definir");
                celda = getNextCelda(fila, columnaActual);
                celda.setCellValue("");
            }

            if (a.getComprobacionEficacia() != null) {
                celda = getNextCelda(fila, columnaActual);
                try {
                    celda.setCellValue(df.parse(df.format(a.getComprobacionEficacia().getFechaEstimada())));
                } catch (ParseException ex) {
                    celda.setCellValue("");
                }
                celda.setCellStyle(estiloContenidoTablaFecha);

                celda = getNextCelda(fila, columnaActual);
                celda.setCellValue(a.getComprobacionEficacia().getResponsableComprobacion().getResponsabilidadResponsable().getNombre());

                if (a.getComprobacionEficacia().getFechaComprobacion() != null) {
                    celda = getNextCelda(fila, columnaActual);
                    try {
                        celda.setCellValue(df.parse(df.format(a.getComprobacionEficacia().getFechaComprobacion())));
                    } catch (ParseException ex) {
                        celda.setCellValue("");
                    }
                    celda.setCellStyle(estiloContenidoTablaFecha);
                } else {
                    celda = getNextCelda(fila, columnaActual);
                    celda.setCellValue("");
                }
            } else {
                celda = getNextCelda(fila, columnaActual);
                celda.setCellValue("");
                celda = getNextCelda(fila, columnaActual);
                celda.setCellValue("Sin Definir");
                celda = getNextCelda(fila, columnaActual);
                celda.setCellValue("");
            }

            celda = getNextCelda(fila, columnaActual);
            celda.setCellValue(a.getCodificacionAccion().getNombre());

            celda = getNextCelda(fila, columnaActual);
            celda.setCellValue(a.getEstadoDeAccion().getDescripcion());

        } while (i < a.getActividadesDeAccion().size());
    }

    private Cell getNextCelda(Row fila, int[] columnaActual) {
        Cell celda = fila.createCell(columnaActual[0]++);
        celda.setCellStyle(estiloContenidoTabla);
        return celda;
    }

    private void agregarActividad(Cell celda, Row fila, int[] columnaActual, int indiceActividad, Accion a, SimpleDateFormat df) {
        if (a.getActividadesDeAccion().isEmpty()) {
            celda = getNextCelda(fila, columnaActual);
            celda.setCellValue("Sin Definir");

            celda = getNextCelda(fila, columnaActual);
            celda.setCellValue("Sin Definir");

            celda = getNextCelda(fila, columnaActual);
            celda.setCellValue("");

            celda = getNextCelda(fila, columnaActual);
            celda.setCellValue("Sin Definir");

            celda = getNextCelda(fila, columnaActual);
            celda.setCellValue("");
        } else {
            celda = getNextCelda(fila, columnaActual);
            celda.setCellValue(a.getActividadesDeAccion().get(indiceActividad).getTipoDeActividad().name());

            celda = getNextCelda(fila, columnaActual);
            celda.setCellValue(a.getActividadesDeAccion().get(indiceActividad).getDescripcion());

            celda = getNextCelda(fila, columnaActual);
            try {
                celda.setCellValue(df.parse(df.format(a.getActividadesDeAccion().get(indiceActividad).getFechaEstimadaImplementacion())));
            } catch (ParseException ex) {
                celda.setCellValue("");
            }
            celda.setCellStyle(estiloContenidoTablaFecha);

            celda = getNextCelda(fila, columnaActual);
            celda.setCellValue(a.getActividadesDeAccion().get(indiceActividad).getResponsableImplementacion().getResponsabilidadResponsable().getNombre());

            if (a.getActividadesDeAccion().get(indiceActividad).getFechaImplementacion() != null) {
                celda = getNextCelda(fila, columnaActual);
                try {
                    celda.setCellValue(df.parse(df.format(a.getActividadesDeAccion().get(indiceActividad).getFechaImplementacion())));
                } catch (ParseException ex) {
                    celda.setCellValue("");
                }
                celda.setCellStyle(estiloContenidoTablaFecha);
            } else {
                celda = getNextCelda(fila, columnaActual);
                celda.setCellValue("");
            }
        }
    }
}
