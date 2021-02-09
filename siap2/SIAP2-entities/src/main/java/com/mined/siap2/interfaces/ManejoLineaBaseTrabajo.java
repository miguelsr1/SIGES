/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package com.mined.siap2.interfaces;

import java.util.Date;

/**
 * Esta clase corresponde al manejo de Líneas base y Líneas de Trabajo de los POA.
 * @author Sofis Solutions
 */
//Notas al desarrollador:
//al agregar una nueva clase que extienda de esta también añadir al converter su conversion.
//funciona como una lista doblemente encadenada donde el anterior es la linea base y siguiente es la linea de trabajo
//cuidado de no generar foreign key
//fecha fijacion se setea solo en caso de que se fije la linea base y es la fecha que fue fijada
public interface ManejoLineaBaseTrabajo<Class> {

    
    public Class getLineaBase();

    public Class getLineaTrabajo();

    public Date getFechaFijacion();

    public void setLineaBase(Class o);

    public void setLineaTrabajo(Class o);

    public void setFechaFijacion(Date d);

}
