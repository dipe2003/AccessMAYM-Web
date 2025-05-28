/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dperez.maymweb.modelo.empresa;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author dipe2
 */
@Entity
@Table(name = "OpcionesSistema")
public class OpcionesSistema implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @OneToOne(cascade = CascadeType.ALL)
    private Empresa empresaOpciones;

    @OneToOne(mappedBy = "opcionesOpcionesSistema", orphanRemoval = true, cascade = CascadeType.ALL)
    private OpcionesApariencia opcionesApariencia;

    @OneToOne(mappedBy = "opcionesOpcionesCorreo", orphanRemoval = true, cascade = CascadeType.ALL)
    private OpcionesCorreo opcionesCorreo;

    public OpcionesSistema() {
        opcionesApariencia = new OpcionesApariencia();
        opcionesApariencia.setOpcionesOpcionesSistema(this);
        opcionesCorreo = new OpcionesCorreo();
        opcionesCorreo.setOpcionesOpcionesCorreo(this);
    }

    //<editor-fold desc="GETTERS">
    public int getId() {
        return id;
    }

    public Empresa getEmpresaOpciones() {
        return empresaOpciones;
    }

    public OpcionesApariencia getOpcionesApariencia() {
        return opcionesApariencia;
    }

    public OpcionesCorreo getOpcionesCorreo() {
        return opcionesCorreo;
    }

    //</editor-fold>
    //<editor-fold desc="SETTERS">
    public void setId(int id) {
        this.id = id;
    }

    public void setEmpresaOpciones(Empresa empresaOpciones) {
        this.empresaOpciones = empresaOpciones;
    }

    public void setOpcionesApariencia(OpcionesApariencia opcionesApariencia) {
        this.opcionesApariencia = opcionesApariencia;
    }

    public void setOpcionesCorreo(OpcionesCorreo opcionesCorreo) {
        this.opcionesCorreo = opcionesCorreo;
    }

    //</editor-fold>
}
