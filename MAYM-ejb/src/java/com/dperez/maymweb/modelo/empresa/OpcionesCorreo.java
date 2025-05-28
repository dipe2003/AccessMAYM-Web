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
@Table(name = "OpcionesCorreo")
public class OpcionesCorreo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String mailFrom = "";
    private String mailUser = "";
    private String mailPass = "";
    private String mailHostSMTP = "";
    private int mailPort = 0;
    private boolean mailTLS = false;

    private boolean alertasActivadas = false;
    
    @OneToOne(cascade = CascadeType.ALL)
    private OpcionesSistema opcionesOpcionesCorreo;

    public OpcionesCorreo() {
    }

    public OpcionesCorreo(String mailFrom, String mailUser, String mailPass,String mailHostSMTP, int mailPort, boolean mailTLS) {
        this.mailFrom = mailFrom;
        this.mailUser = mailUser;
        this.mailPass = mailPass;
        this.mailHostSMTP = mailHostSMTP;
        this.mailPort = mailPort;
        this.mailTLS = mailTLS;
    }

    //<editor-fold desc="GETTERS">
    public int getId() {
        return id;
    }

    public String getMailFrom() {
        return mailFrom;
    }

    public String getMailUser() {
        return mailUser;
    }

    public String getMailPass() {
        return mailPass;
    }

    public int getMailPort() {
        return mailPort;
    }

    public String getMailHostSMTP() {
        return mailHostSMTP;
    }

    public boolean isMailTLS() {
        return mailTLS;
    }

    public boolean isAlertasActivadas() {
        return alertasActivadas;
    }

    public OpcionesSistema getOpcionesOpcionesCorreo() {
        return opcionesOpcionesCorreo;
    }
    
    
    //</editor-fold>

    //<editor-fold desc="SETTERS">
    public void setId(int id) {
        this.id = id;
    }

    public void setMailFrom(String mailFrom) {
        this.mailFrom = mailFrom;
    }

    public void setMailUser(String mailUser) {
        this.mailUser = mailUser;
    }

    public void setMailPass(String mailPass) {
        this.mailPass = mailPass;
    }

    public void setMailPort(int mailPort) {
        this.mailPort = mailPort;
    }

    public void setMailHostSMTP(String mailHostSMTP) {
        this.mailHostSMTP = mailHostSMTP;
    }

    public void setMailTLS(boolean mailTLS) {
        this.mailTLS = mailTLS;
    }

    public void setAlertasActivadas(boolean alertasActivadas) {
        this.alertasActivadas = alertasActivadas;
    }
    
    //</editor-fold>

    public void setOpcionesOpcionesCorreo(OpcionesSistema opcionesOpcionesCorreo) {
        this.opcionesOpcionesCorreo = opcionesOpcionesCorreo;
    }
}
