/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dperez.maym.web.herramientas.exportacion.excelsea;

import com.dperez.maymweb.modelo.acciones.Accion;
import com.dperez.maymweb.modelo.acciones.Estado;
import static com.dperez.maymweb.modelo.acciones.Estado.CERRADA;
import static com.dperez.maymweb.modelo.acciones.Estado.DESESTIMADA;
import static com.dperez.maymweb.modelo.acciones.Estado.PENDIENTE;
import static com.dperez.maymweb.modelo.acciones.Estado.PROCESO_IMP;
import static com.dperez.maymweb.modelo.acciones.Estado.PROCESO_VER;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;
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
            CrearContenidoTabla(sheet, acciones);
        } else {
            CrearEncabezadosTabla(sheet, encabezadosSinActividades);
            CrearContenidoTablaSinActividades(sheet, acciones);
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

    private void CrearContenidoTabla(Sheet hoja, List<Accion> acciones) {
        int[] numFila = new int[]{1};
        for (var a : acciones) {
            AgregarRegistro(a, hoja, numFila);
        }
    }

    private void CrearContenidoTablaSinActividades(Sheet hoja, List<Accion> acciones) {
        CrearFilas(hoja, acciones.size());
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyy");

        LlenarDatoEntero(hoja, 0, acciones.stream()
                .collect(Collectors.toMap((Accion a) -> a.getId(), (Accion a) -> a.getId()))
                .values().stream()
                .collect(Collectors.toList()));

        LlenarDatoFecha(hoja, 1, acciones.stream()
                .collect(Collectors.toMap((Accion a) -> a.getId(), (Accion a) -> df.format(a.getFechaDeteccion())))
                .values().stream()
                .collect(Collectors.toList()));

        LlenarDatoTexto(hoja, 2, acciones.stream()
                .collect(Collectors.toMap((Accion a) -> a.getId(), (Accion a) -> a.getAreaAccion().getNombre()))
                .values().stream()
                .collect(Collectors.toList()));

        LlenarDatoTexto(hoja, 3, acciones.stream()
                .collect(Collectors.toMap((Accion a) -> a.getId(), (Accion a) -> a.getDeteccionAccion().getNombre()))
                .values().stream()
                .collect(Collectors.toList()));

        LlenarDatoTexto(hoja, 4, acciones.stream()
                .collect(Collectors.toMap((Accion a) -> a.getId(), (Accion a) -> a.getDescripcion()))
                .values().stream()
                .collect(Collectors.toList()));

        LlenarDatoTexto(hoja, 5, acciones.stream()
                .collect(Collectors.toMap((Accion a) -> a.getId(), (Accion a) -> a.getAnalisisCausa()))
                .values().stream()
                .collect(Collectors.toList()));

        LlenarDatoFecha(hoja, 6, acciones.stream()
                .collect(Collectors.toMap((Accion a) -> a.getId(), (Accion a) -> a.getComprobacionImplementacion() != null ? df.format(a.getComprobacionImplementacion().getFechaEstimada()) : ""))
                .values().stream()
                .collect(Collectors.toList()));

        LlenarDatoTexto(hoja, 7, acciones.stream()
                .collect(Collectors.toMap((Accion a) -> a.getId(), (Accion a) -> a.getComprobacionImplementacion() != null ? a.getComprobacionImplementacion().getResponsableComprobacion().getResponsabilidadResponsable().getNombre() : ""))
                .values().stream()
                .collect(Collectors.toList()));

        LlenarDatoFecha(hoja, 8, acciones.stream()
                .collect(Collectors.toMap((Accion a) -> a.getId(), (Accion a) -> a.getComprobacionImplementacion() != null ? a.getComprobacionImplementacion().getFechaComprobacion() != null ? df.format(a.getComprobacionImplementacion().getFechaComprobacion()) : "" : ""))
                .values().stream()
                .collect(Collectors.toList()));

        LlenarDatoFecha(hoja, 9, acciones.stream()
                .collect(Collectors.toMap((Accion a) -> a.getId(), (Accion a) -> a.getComprobacionEficacia() != null ? df.format(a.getComprobacionEficacia().getFechaEstimada()) : ""))
                .values().stream()
                .collect(Collectors.toList()));

        LlenarDatoTexto(hoja, 10, acciones.stream()
                .collect(Collectors.toMap((Accion a) -> a.getId(), (Accion a) -> a.getComprobacionEficacia() != null ? a.getComprobacionEficacia().getResponsableComprobacion().getResponsabilidadResponsable().getNombre() : ""))
                .values().stream()
                .collect(Collectors.toList()));

        LlenarDatoFecha(hoja, 11, acciones.stream()
                .collect(Collectors.toMap((Accion a) -> a.getId(), (Accion a) -> a.getComprobacionEficacia() != null ? a.getComprobacionEficacia().getFechaComprobacion() != null ? df.format(a.getComprobacionEficacia().getFechaComprobacion()) : "" : ""))
                .values().stream()
                .collect(Collectors.toList()));

        LlenarDatoTexto(hoja, 12, acciones.stream()
                .collect(Collectors.toMap((Accion a) -> a.getId(), (Accion a) -> a.getCodificacionAccion().getNombre()))
                .values().stream()
                .collect(Collectors.toList()));

        LlenarDatoTexto(hoja, 13, acciones.stream()
                .collect(Collectors.toMap((Accion a) -> a.getId(), (Accion a) -> a.getEstadoDeAccion().getDescripcion()))
                .values().stream()
                .collect(Collectors.toList()));
    }

    private void CrearFilas(Sheet hoja, int totalRegistros) {
        Row fila = null;
        for (int i = 1; i <= totalRegistros; i++) {
            fila = hoja.createRow(i);
        }
    }

    private void LlenarDatoFecha(Sheet hoja, int columna, List<String> valores) {
        Row fila = null;
        Cell dato = null;
        int numFila = 1;
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyy");
        for (int i = 0; i < valores.size(); i++, numFila++) {
            fila = hoja.getRow(numFila);
            dato = fila.createCell(columna);
            try {
                dato.setCellValue(df.parse(valores.get(i)));
            } catch (ParseException ex) {
                dato.setCellValue("");
                ex.getMessage();
            } finally {
                dato.setCellStyle(estiloContenidoTablaFecha);
            }
        }
    }

    private void LlenarDatoTexto(Sheet hoja, int columna, List<String> valroes) {
        Row fila = null;
        Cell dato = null;
        int numFila = 1;
        for (int i = 0; i < valroes.size(); i++, numFila++) {
            fila = hoja.getRow(numFila);
            dato = fila.createCell(columna);
            dato.setCellValue(valroes.get(i));
            dato.setCellStyle(estiloContenidoTabla);
        }
    }

    private void LlenarDatoEntero(Sheet hoja, int columna, List<Integer> valores) {
        Row fila = null;
        Cell dato = null;
        int numFila = 1;
        for (int i = 0; i < valores.size(); i++, numFila++) {
            fila = hoja.getRow(numFila);
            dato = fila.createCell(columna);
            dato.setCellValue(valores.get(i));
            dato.setCellStyle(estiloContenidoTabla);
        }
    }

    private void AgregarRegistro(Accion a, Sheet hoja, int[] numFila) {
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
            /*
            /   COMIENZO DE ITERACION POR LA LISTA DE ACTIVIDADES
             */
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
                celda.setCellValue(a.getActividadesDeAccion().get(i).getTipoDeActividad().name());

                celda = getNextCelda(fila, columnaActual);
                celda.setCellValue(a.getActividadesDeAccion().get(i).getDescripcion());

                celda = getNextCelda(fila, columnaActual);
                try {
                    celda.setCellValue(df.parse(df.format(a.getActividadesDeAccion().get(i).getFechaEstimadaImplementacion())));
                } catch (ParseException ex) {
                    celda.setCellValue("");
                }
                celda.setCellStyle(estiloContenidoTablaFecha);

                celda = getNextCelda(fila, columnaActual);
                celda.setCellValue(a.getActividadesDeAccion().get(i).getResponsableImplementacion().getResponsabilidadResponsable().getNombre());

                if (a.getActividadesDeAccion().get(i).getFechaImplementacion() != null) {
                    celda = getNextCelda(fila, columnaActual);
                    try {
                        celda.setCellValue(df.parse(df.format(a.getActividadesDeAccion().get(i).getFechaImplementacion())));
                    } catch (ParseException ex) {
                        celda.setCellValue("");
                    }
                    celda.setCellStyle(estiloContenidoTablaFecha);
                } else {
                    celda = getNextCelda(fila, columnaActual);
                    celda.setCellValue("");
                }
                i++;
            }
            /*
            / FIN DE ITERACION POR LA LISTA DE ACTIVIDADES
             */
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
}
