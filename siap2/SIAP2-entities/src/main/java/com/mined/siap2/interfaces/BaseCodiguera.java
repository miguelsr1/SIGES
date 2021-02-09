/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package com.mined.siap2.interfaces;


/**
 * Interfaz para las codigueras.
 * Estas tienen como base: Id, Código y Nombre.
 * @author Sofis Solutions
 */
public interface BaseCodiguera {

    public Integer getId();

    public void setId(Integer id);
  

    public String getCodigo();

    public void setCodigo(String codigo);
    

    public String getNombre();

    public void setNombre(String nombre);

}
