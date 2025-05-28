/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.dperez.maym.web.inicio;

import com.dperez.maymweb.modelo.acciones.Accion;
import com.dperez.maymweb.facades.FacadeLectura;
import com.dperez.maymweb.modelo.acciones.Estado;
import com.dperez.maymweb.modelo.area.Area;
import com.dperez.maymweb.modelo.codificacion.Codificacion;
import com.dperez.maymweb.modelo.deteccion.TipoDeteccion;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author dperez
 */
@Named
@ViewScoped
public class GraficosIndex implements Serializable {

    private FacadeLectura fLectura;

    // Valores generales
    private Date fechaInicial;
    private Date fechaFinal;
    private String strFechaInicial;
    private String strFechaFinal;
    private List<Accion> listaTotalAcciones;

    // Valores para grafico Estados
    private int TotalAcciones;
    private int CorrectivasCerradas;
    private int CorrectivasPendientes;
    private int CorrectivasDesestimadas;
    private int CorrectivasProcesoImp;
    private int CorrectivasProcesoVerif;

    private int PreventivasCerradas;
    private int PreventivasPendientes;
    private int PreventivasDesestimadas;
    private int PreventivasProcesoImp;
    private int PreventivasProcesoVerif;

    private int MejorasCerradas;
    private int MejorasPendientes;
    private int MejorasDesestimadas;
    private int MejorasProcesoImp;
    private int MejorasProcesoVerif;

    // Valores para grafico de tipos
    private int AccionesMejora;
    private int AccionesCorrectivas;
    private int AccionesPreventivas;

    // Valores para graficos de proceso
    private String nombresProcesos;
    private int tamanio;

    private String totalesACPorProceso;
    private String nombresACProcesos;

    private String totalesAPPorProceso;
    private String nombresAPProcesos;

    private String totalesOMPorProceso;
    private String nombresOMProcesos;

    // Valores para grafico de detecciones
    private int totalDeteccionesInternas;
    private int totalDeteccionesExternas;

    // Valores para grafico por codificaciones
    private String nombresCodificaciones;
    private String totalCodificacionesCorrectivas;
    private String totalCodificacionesPreventivas;
    private String totalCodificacionesMejoras;

    //<editor-fold desc="Fechas">
    // Getters
    public Date getFechaInicial() {
        return fechaInicial;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public String getStrFechaInicial() {
        SimpleDateFormat fDate = new SimpleDateFormat("dd/MM/yyyy");
        if (fechaInicial == null) {
            return this.strFechaInicial;
        } else {
            return fDate.format(fechaInicial);
        }
    }

    public String getStrFechaFinal() {
        SimpleDateFormat fDate = new SimpleDateFormat("dd/MM/yyyy");
        if (fechaFinal == null) {
            return this.strFechaFinal;
        } else {
            return fDate.format(fechaFinal);
        }
    }

    // Setters
    public void setFechaInicial(Date fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public void setStrFechaInicial(String strFechaInicial) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            this.fechaInicial = sdf.parse(strFechaInicial);
        } catch (ParseException ex) {
        }
        this.strFechaInicial = strFechaInicial;
    }

    public void setStrFechaFinal(String strFechaFinal) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            this.fechaFinal = sdf.parse(strFechaFinal);
        } catch (ParseException ex) {
        }
        this.strFechaFinal = strFechaFinal;
    }

    //</editor-fold>
    //<editor-fold desc="Estados">
    // Getters
    public int getTotalAcciones() {
        return TotalAcciones;
    }

    public int getCorrectivasCerradas() {
        return CorrectivasCerradas;
    }

    public int getCorrectivasPendientes() {
        return CorrectivasPendientes;
    }

    public int getCorrectivasDesestimadas() {
        return CorrectivasDesestimadas;
    }

    public int getCorrectivasProcesoImp() {
        return CorrectivasProcesoImp;
    }

    public int getCorrectivasProcesoVerif() {
        return CorrectivasProcesoVerif;
    }

    public int getPreventivasCerradas() {
        return PreventivasCerradas;
    }

    public int getPreventivasPendientes() {
        return PreventivasPendientes;
    }

    public int getPreventivasDesestimadas() {
        return PreventivasDesestimadas;
    }

    public int getPreventivasProcesoImp() {
        return PreventivasProcesoImp;
    }

    public int getPreventivasProcesoVerif() {
        return PreventivasProcesoVerif;
    }

    public int getMejorasCerradas() {
        return MejorasCerradas;
    }

    public int getMejorasPendientes() {
        return MejorasPendientes;
    }

    public int getMejorasDesestimadas() {
        return MejorasDesestimadas;
    }

    public int getMejorasProcesoImp() {
        return MejorasProcesoImp;
    }

    public int getMejorasProcesoVerif() {
        return MejorasProcesoVerif;
    }

    // Setters
    public void setTotalAcciones(int TotalAcciones) {
        this.TotalAcciones = TotalAcciones;
    }

    public void setCorrectivasCerradas(int CorrectivasCerradas) {
        this.CorrectivasCerradas = CorrectivasCerradas;
    }

    public void setCorrectivasPendientes(int CorrectivasPendientes) {
        this.CorrectivasPendientes = CorrectivasPendientes;
    }

    public void setCorrectivasDesestimadas(int CorrectivasDesestimadas) {
        this.CorrectivasDesestimadas = CorrectivasDesestimadas;
    }

    public void setCorrectivasProcesoImp(int CorrectivasProcesoImp) {
        this.CorrectivasProcesoImp = CorrectivasProcesoImp;
    }

    public void setCorrectivasProcesoVerif(int CorrectivasProcesoVerif) {
        this.CorrectivasProcesoVerif = CorrectivasProcesoVerif;
    }

    public void setPreventivasCerradas(int PreventivasCerradas) {
        this.PreventivasCerradas = PreventivasCerradas;
    }

    public void setPreventivasPendientes(int PreventivasPendientes) {
        this.PreventivasPendientes = PreventivasPendientes;
    }

    public void setPreventivasDesestimadas(int PreventivasDesestimadas) {
        this.PreventivasDesestimadas = PreventivasDesestimadas;
    }

    public void setPreventivasProcesoImp(int PreventivasProcesoImp) {
        this.PreventivasProcesoImp = PreventivasProcesoImp;
    }

    public void setPreventivasProcesoVerif(int PreventivasProcesoVerif) {
        this.PreventivasProcesoVerif = PreventivasProcesoVerif;
    }

    public void setMejorasCerradas(int MejorasCerradas) {
        this.MejorasCerradas = MejorasCerradas;
    }

    public void setMejorasPendientes(int MejorasPendientes) {
        this.MejorasPendientes = MejorasPendientes;
    }

    public void setMejorasDesestimadas(int MejorasDesestimadas) {
        this.MejorasDesestimadas = MejorasDesestimadas;
    }

    public void setMejorasProcesoImp(int MejorasProcesoImp) {
        this.MejorasProcesoImp = MejorasProcesoImp;
    }

    public void setMejorasProcesoVerif(int MejorasProcesoVerif) {
        this.MejorasProcesoVerif = MejorasProcesoVerif;
    }

    //</editor-fold>
    //<editor-fold desc="Tipos">
    // Getters
    public int getAccionesMejora() {
        return AccionesMejora;
    }

    public int getAccionesCorrectivas() {
        return AccionesCorrectivas;
    }

    public int getAccionesPreventivas() {
        return AccionesPreventivas;
    }

    public int getTamanio() {
        return tamanio;
    }

    // Setters
    public void setAccionesMejora(int AccionesMejora) {
        this.AccionesMejora = AccionesMejora;
    }

    public void setAccionesCorrectivas(int AccionesCorrectivas) {
        this.AccionesCorrectivas = AccionesCorrectivas;
    }

    public void setAccionesPreventivas(int AccionesPreventivas) {
        this.AccionesPreventivas = AccionesPreventivas;
    }

    public void setTamanio(int tamanio) {
        this.tamanio = tamanio;
    }

    //</editor-fold>
    //<editor-fold desc="Por Procesos">
    // Getters
    public String getNombresProcesos() {
        return nombresProcesos;
    }

    // Setters
    public void setNombresProcesos(String nombresProcesos) {
        this.nombresProcesos = nombresProcesos;
    }

    public String getTotalesACPorProceso() {
        return totalesACPorProceso;
    }

    public void setTotalesACPorProceso(String totalesACPorProceso) {
        this.totalesACPorProceso = totalesACPorProceso;
    }

    public String getNombresACProcesos() {
        return nombresACProcesos;
    }

    public void setNombresACProcesos(String nombresACProcesos) {
        this.nombresACProcesos = nombresACProcesos;
    }

    public String getTotalesAPPorProceso() {
        return totalesAPPorProceso;
    }

    public void setTotalesAPPorProceso(String totalesAPPorProceso) {
        this.totalesAPPorProceso = totalesAPPorProceso;
    }

    public String getNombresAPProcesos() {
        return nombresAPProcesos;
    }

    public void setNombresAPProcesos(String nombresAPProcesos) {
        this.nombresAPProcesos = nombresAPProcesos;
    }

    public String getTotalesOMPorProceso() {
        return totalesOMPorProceso;
    }

    public void setTotalesOMPorProceso(String totalesOMPorProceso) {
        this.totalesOMPorProceso = totalesOMPorProceso;
    }

    public String getNombresOMProcesos() {
        return nombresOMProcesos;
    }

    public void setNombresOMProcesos(String nombresOMProcesos) {
        this.nombresOMProcesos = nombresOMProcesos;
    }

    //</editor-fold>
    // Getters
    //<editor-fold desc="Detecciones">
    public int getTotalDeteccionesInternas() {
        return totalDeteccionesInternas;
    }

    public int getTotalDeteccionesExternas() {
        return totalDeteccionesExternas;
    }

    // Setters
    public void setTotalDeteccionesInternas(int totalDeteccionesInternas) {
        this.totalDeteccionesInternas = totalDeteccionesInternas;
    }

    public void setTotalDeteccionesExternas(int totalDeteccionesExternas) {
        this.totalDeteccionesExternas = totalDeteccionesExternas;
    }

    //</editor-fold>
    //<editor-fold desc="Codificaciones">
    // Getters
    public String getNombresCodificaciones() {
        return nombresCodificaciones;
    }

    public String getTotalCodificacionesCorrectivas() {
        return totalCodificacionesCorrectivas;
    }

    public String getTotalCodificacionesPreventivas() {
        return totalCodificacionesPreventivas;
    }

    public String getTotalCodificacionesMejoras() {
        return totalCodificacionesMejoras;
    }

    // Setters
    public void setTotalCodificacionesCorrectivas(String totalCodificacionesCorrectivas) {
        this.totalCodificacionesCorrectivas = totalCodificacionesCorrectivas;
    }

    public void setNombresCodificaciones(String nombresCodificaciones) {
        this.nombresCodificaciones = nombresCodificaciones;
    }

    public void setTotalCodificacionesPreventivas(String totalCodificacionesPreventivas) {
        this.totalCodificacionesPreventivas = totalCodificacionesPreventivas;
    }

    public void setTotalCodificacionesMejoras(String totalCodificacionesMejoras) {
        this.totalCodificacionesMejoras = totalCodificacionesMejoras;
    }

    //</editor-fold>
    // Metodos
    @PostConstruct
    public void init() {
        fLectura = new FacadeLectura();
        // llenar los valores de estado.
        listaTotalAcciones = fLectura.listarAcciones();
        List<Accion> lstAcciones = new ArrayList();

        // calcular fechas
        if (!listaTotalAcciones.isEmpty()) {
            fechaInicial = calcularFechaInicial(listaTotalAcciones);
            fechaFinal = calcularFechaFinal(listaTotalAcciones);
        }
        lstAcciones = filtrarFechasAcciones(listaTotalAcciones, fechaInicial, fechaFinal);

        // total por tipo de deteccion
        calcularTotalDeteccion(lstAcciones);

        // totales por tipo
        calcularTotalPorTipo(lstAcciones);

        TotalAcciones = lstAcciones.size();

        // totales por estado
        CorrectivasCerradas = calcularTotalPorEstado(listaTotalAcciones, "Correctiva", Estado.CERRADA);
        CorrectivasPendientes = calcularTotalPorEstado(listaTotalAcciones, "Correctiva", Estado.PENDIENTE);
        CorrectivasDesestimadas = calcularTotalPorEstado(listaTotalAcciones, "Correctiva", Estado.DESESTIMADA);
        CorrectivasProcesoImp = calcularTotalPorEstado(listaTotalAcciones, "Correctiva", Estado.PROCESO_IMP);
        CorrectivasProcesoVerif = calcularTotalPorEstado(listaTotalAcciones, "Correctiva", Estado.PROCESO_VER);

        PreventivasCerradas = calcularTotalPorEstado(listaTotalAcciones, "Preventiva", Estado.CERRADA);
        PreventivasPendientes = calcularTotalPorEstado(listaTotalAcciones, "Preventiva", Estado.PENDIENTE);
        PreventivasDesestimadas = calcularTotalPorEstado(listaTotalAcciones, "Preventiva", Estado.DESESTIMADA);
        PreventivasProcesoImp = calcularTotalPorEstado(listaTotalAcciones, "Preventiva", Estado.PROCESO_IMP);
        PreventivasProcesoVerif = calcularTotalPorEstado(listaTotalAcciones, "Preventiva", Estado.PROCESO_VER);

        MejorasCerradas = calcularTotalPorEstado(listaTotalAcciones, "Mejora", Estado.CERRADA);
        MejorasPendientes = calcularTotalPorEstado(listaTotalAcciones, "Mejora", Estado.PENDIENTE);
        MejorasDesestimadas = calcularTotalPorEstado(listaTotalAcciones, "Mejora", Estado.DESESTIMADA);
        MejorasProcesoImp = calcularTotalPorEstado(listaTotalAcciones, "Mejora", Estado.PROCESO_IMP);
        MejorasProcesoVerif = calcularTotalPorEstado(listaTotalAcciones, "Mejora", Estado.PROCESO_VER);

        // totales por codificacion
        List<Codificacion> codificaciones = extraerCodificacionAcciones(lstAcciones);
        totalCodificacionesCorrectivas = generarTotalesPorCodificaciones(codificaciones, "Correctiva");
        totalCodificacionesPreventivas = generarTotalesPorCodificaciones(codificaciones, "Preventiva");
        totalCodificacionesMejoras = generarTotalesPorCodificaciones(codificaciones, "Mejora");
        nombresCodificaciones = generarNombresPorCodificaciones(codificaciones);

        // totales por area
        List<Area> listaAreas = extraerAreasAcciones(lstAcciones);
        tamanio = listaAreas.size();

        nombresProcesos = generarNombresPorProceso(listaAreas);
        totalesACPorProceso = generarACTotalesPorProceso(listaAreas);
        totalesAPPorProceso = generarAPTotalesPorProceso(listaAreas);
        totalesOMPorProceso = generarOMTotalesPorProceso(listaAreas);
    }

    private List<Accion> filtrarFechasAcciones(List<Accion> acciones, Date fechaInicial, Date fechaFinal) {
        return acciones.stream()
                .filter((Accion accion) -> accion.getFechaDeteccion().after(fechaInicial) && accion.getFechaDeteccion().before(fechaFinal))
                .toList();
    }

    /**
     * Calcula el total por estado por tipo de accion. correspondientes.
     *
     * @param acciones
     */
    private int calcularTotalPorEstado(List<Accion> acciones, String tipoAccion, Estado estadoAccion) {
        return (int) acciones.stream()
                .filter((a -> a.getClass().getSimpleName().equals(tipoAccion) && a.getEstadoDeAccion() == estadoAccion))
                .count();
    }

    /**
     * Determina el tipo de accion y aumenta el valor correspondiente.
     *
     * @param accion
     */
    private void calcularTotalPorTipo(List<Accion> acciones) {
        AccionesCorrectivas = (int) acciones.stream()
                .filter((Accion accion) -> accion.getClass().getSimpleName().equalsIgnoreCase("correctiva"))
                .count();
        AccionesPreventivas = (int) acciones.stream()
                .filter((Accion accion) -> accion.getClass().getSimpleName().equalsIgnoreCase("preventiva"))
                .count();
        AccionesMejora = (int) acciones.stream()
                .filter((Accion accion) -> accion.getClass().getSimpleName().equalsIgnoreCase("mejora")).count();
    }

    /**
     * Determina el tipo de deteccion de cada accion y calcula el total.
     *
     * @param acciones
     */
    private void calcularTotalDeteccion(List<Accion> acciones) {
        totalDeteccionesInternas = (int) acciones.stream()
                .filter((Accion accion) -> accion.getDeteccionAccion().getTipoDeDeteccion() == TipoDeteccion.INTERNA)
                .count();
        totalDeteccionesExternas = (int) acciones.stream()
                .filter((Accion accion) -> accion.getDeteccionAccion().getTipoDeDeteccion() == TipoDeteccion.EXTERNA)
                .count();
    }

    private Date calcularFechaInicial(List<Accion> acciones) {
        return acciones.stream()
                .min(Comparator.naturalOrder()).get().getFechaDeteccion();
    }

    private Date calcularFechaFinal(List<Accion> acciones) {
        return acciones.stream()
                .max(Comparator.naturalOrder()).get().getFechaDeteccion();
    }

    /**
     * Genera una cadena con arreglo de cada nombre de area.
     *
     * @param areas
     * @return
     */
    private String generarNombresPorProceso(List<Area> areas) {
        StringBuilder strBuilderNombres = new StringBuilder();
        for (int i = 0; i < tamanio; i++) {
            strBuilderNombres
                    .append("'")
                    .append(areas.get(i).getNombre())
                    .append("'");
            if (i + 1 < tamanio) {
                strBuilderNombres.append(",");
            }
        }
        return strBuilderNombres.insert(0, "[").append("]").toString();
    }

    /**
     * Genera una cadena con arreglo del total de acciones por codificacion.
     *
     * @param codificaciones
     * @return
     */
    private String generarTotalesPorCodificaciones(List<Codificacion> codificaciones, String tipoAccion) {
        StringBuilder strBuilderTotales = new StringBuilder();
        int total = codificaciones.size();
        for (int i = 0; i < total; i++) {
            strBuilderTotales.append(codificaciones.get(i).getAccionesCodificacion().stream()
                    .filter(accion -> accion.getFechaDeteccion().before(fechaFinal) && accion.getFechaDeteccion().after(fechaInicial))
                    .filter(accion -> accion.getClass().getSimpleName().equals(tipoAccion))
                    .count());
            if (i + 1 < total) {
                strBuilderTotales.append(",");
            }
        }
        return strBuilderTotales.insert(0, "[").append("]").toString();
    }

    /**
     * Genera una cadena con arreglo de cada nombre de codificacion.
     *
     * @param codificaciones
     * @return
     */
    private String generarNombresPorCodificaciones(List<Codificacion> codificaciones) {
        StringBuilder strBuilderNombres = new StringBuilder();
        int total = codificaciones.size();
        for (int i = 0; i < total; i++) {
            strBuilderNombres
                    .append("'")
                    .append(codificaciones.get(i).getNombre())
                    .append("'");
            if (i + 1 < total) {
                strBuilderNombres.append(",");
            }
        }
        return strBuilderNombres.insert(0, "[").append("]").toString();
    }

    /**
     * Extrae una lista de las codificaciones de las acciones.
     *
     * @param acciones
     * @return
     */
    private List<Codificacion> extraerCodificacionAcciones(List<Accion> acciones) {
        List<Codificacion> codificaciones = new ArrayList<>();
        acciones.stream()
                .forEach((Accion accion) -> {
                    if (!codificaciones.contains(accion.getCodificacionAccion())) {
                        codificaciones.add(accion.getCodificacionAccion());
                    }
                });
        return codificaciones;
    }

    /**
     * Genera una cadena con arreglo del total de acciones en cada area.
     *
     * @param areas
     * @return
     */
    private String generarACTotalesPorProceso(List<Area> areas) {
        StringBuilder strBuilderTotales = new StringBuilder();
        for (int i = 0; i < tamanio; i++) {
            strBuilderTotales.append(areas.get(i).getAccionesArea().stream()
                    .filter(accion -> accion.getFechaDeteccion().before(fechaFinal) && accion.getFechaDeteccion().after(fechaInicial))
                    .filter(accion -> accion.getClass().getSimpleName().equals("Correctiva"))
                    .count());
            if (i + 1 < tamanio) {
                strBuilderTotales.append(",");
            }
        }
        return strBuilderTotales.insert(0, "[").append("]").toString();
    }

    /**
     * Genera una cadena con arreglo del total de acciones en cada area.
     *
     * @param areas
     * @return
     */
    private String generarAPTotalesPorProceso(List<Area> areas) {
        StringBuilder strBuilderTotales = new StringBuilder();
        for (int i = 0; i < tamanio; i++) {
            strBuilderTotales.append(areas.get(i).getAccionesArea().stream()
                    .filter(accion -> accion.getFechaDeteccion().before(fechaFinal) && accion.getFechaDeteccion().after(fechaInicial))
                    .filter(accion -> accion.getClass().getSimpleName().equals("Preventiva"))
                    .count());
            if (i + 1 < tamanio) {
                strBuilderTotales.append(",");
            }
        }
        return strBuilderTotales.insert(0, "[").append("]").toString();
    }

    /**
     * Genera una cadena con arreglo del total de acciones en cada area.
     *
     * @param areas
     * @return
     */
    private String generarOMTotalesPorProceso(List<Area> areas) {
        StringBuilder strBuilderTotales = new StringBuilder();
        for (int i = 0; i < tamanio; i++) {
            strBuilderTotales.append(areas.get(i).getAccionesArea().stream()
                    .filter(accion -> accion.getFechaDeteccion().before(fechaFinal) && accion.getFechaDeteccion().after(fechaInicial))
                    .filter(accion -> accion.getClass().getSimpleName().equals("Mejora"))
                    .count());
            if (i + 1 < tamanio) {
                strBuilderTotales.append(",");
            }
        }
        return strBuilderTotales.insert(0, "[").append("]").toString();
    }

    /**
     * Extrae una lista de las areas de las acciones.
     *
     * @param acciones
     * @return
     */
    private List<Area> extraerAreasAcciones(List<Accion> acciones) {
        List<Area> areas = new ArrayList<>();
        acciones.stream()
                .forEach((Accion accion) -> {
                    if (!areas.contains(accion.getAreaAccion())) {
                        areas.add(accion.getAreaAccion());
                    }
                });
        return areas;
    }

    /**
     * Se toman las fechas y se calculan todos los totales.
     */
    public void filtrarPorFecha() {

        List<Accion> lstAcciones = filtrarFechasAcciones(listaTotalAcciones, fechaInicial, fechaFinal);

        calcularTotalDeteccion(lstAcciones);

        calcularTotalPorTipo(lstAcciones);

        TotalAcciones = lstAcciones.size();

        // totales por estado
        // totales por estado
        CorrectivasCerradas = calcularTotalPorEstado(lstAcciones, "Correctiva", Estado.CERRADA);
        CorrectivasPendientes = calcularTotalPorEstado(lstAcciones, "Correctiva", Estado.PENDIENTE);
        CorrectivasDesestimadas = calcularTotalPorEstado(lstAcciones, "Correctiva", Estado.DESESTIMADA);
        CorrectivasProcesoImp = calcularTotalPorEstado(lstAcciones, "Correctiva", Estado.PROCESO_IMP);
        CorrectivasProcesoVerif = calcularTotalPorEstado(lstAcciones, "Correctiva", Estado.PROCESO_VER);

        PreventivasCerradas = calcularTotalPorEstado(lstAcciones, "Preventiva", Estado.CERRADA);
        PreventivasPendientes = calcularTotalPorEstado(lstAcciones, "Preventiva", Estado.PENDIENTE);
        PreventivasDesestimadas = calcularTotalPorEstado(lstAcciones, "Preventiva", Estado.DESESTIMADA);
        PreventivasProcesoImp = calcularTotalPorEstado(lstAcciones, "Preventiva", Estado.PROCESO_IMP);
        PreventivasProcesoVerif = calcularTotalPorEstado(lstAcciones, "Preventiva", Estado.PROCESO_VER);

        MejorasCerradas = calcularTotalPorEstado(lstAcciones, "Mejora", Estado.CERRADA);
        MejorasPendientes = calcularTotalPorEstado(lstAcciones, "Mejora", Estado.PENDIENTE);
        MejorasDesestimadas = calcularTotalPorEstado(lstAcciones, "Mejora", Estado.DESESTIMADA);
        MejorasProcesoImp = calcularTotalPorEstado(lstAcciones, "Mejora", Estado.PROCESO_IMP);
        MejorasProcesoVerif = calcularTotalPorEstado(lstAcciones, "Mejora", Estado.PROCESO_VER);

        // totales por codificacion
        List<Codificacion> codificaciones = extraerCodificacionAcciones(lstAcciones);
        totalCodificacionesCorrectivas = generarTotalesPorCodificaciones(codificaciones, "Correctiva");
        totalCodificacionesPreventivas = generarTotalesPorCodificaciones(codificaciones, "Preventiva");
        totalCodificacionesMejoras = generarTotalesPorCodificaciones(codificaciones, "Mejora");
        nombresCodificaciones = generarNombresPorCodificaciones(codificaciones);

        // totales por area
        List<Area> areas = extraerAreasAcciones(lstAcciones);
        tamanio = areas.size();

        nombresProcesos = generarNombresPorProceso(areas);

        totalesACPorProceso = generarACTotalesPorProceso(areas);
        totalesAPPorProceso = generarAPTotalesPorProceso(areas);
        totalesOMPorProceso = generarOMTotalesPorProceso(areas);

    }

    public void resetFechas() {
        fechaInicial = calcularFechaInicial(listaTotalAcciones);
        fechaFinal = calcularFechaFinal(listaTotalAcciones);
        filtrarPorFecha();
    }

}
