/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.mined.siap2.business.ejbs.impl;

import com.mined.siap2.to.TopePresupuestalArchivoTo;


/**
 *
 * @author bruno
 */
public interface ProcesamientoTechosDesdeArchivoAsincInterface {
    void crearDesdeArchivo(TopePresupuestalArchivoTo archivo);
}