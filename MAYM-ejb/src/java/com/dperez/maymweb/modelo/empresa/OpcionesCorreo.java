/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dperez.maymweb.modelo.empresa;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author dipe2
 */
public class OpcionesCorreo {

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

    public OpcionesCorreo() {
    }

    public OpcionesCorreo(int id, String mailFrom, String mailUser, String mailPass,String mailHostSMTP, int mailPort, boolean mailTLS) {
        this.id = id;
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
}
