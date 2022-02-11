package com.dperez.maymweb.modelo.acciones.comprobaciones;

import java.util.Date;

import javax.persistence.Entity;

import com.dperez.maymweb.modelo.acciones.Accion;
import com.dperez.maymweb.modelo.usuario.Responsable;

@Entity
public class Implementacion extends Comprobacion {

	public Implementacion() {
	}

	public Implementacion(Accion accion) {
		super(accion);
	}

	public Implementacion(Accion accion, Responsable responsable, Date fechaEstimada) {
		super(accion, responsable, fechaEstimada);
	}

}
