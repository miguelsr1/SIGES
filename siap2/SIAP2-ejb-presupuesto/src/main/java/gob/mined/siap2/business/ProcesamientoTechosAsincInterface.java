/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.mined.siap2.business;

import gob.mined.siap2.entities.data.SsUsuario;
import gob.mined.siap2.entities.enums.AccionTechos;
import java.util.List;


/**
 *
 * @author bruno
 */
public interface ProcesamientoTechosAsincInterface {
    void modificarTechos(Integer relId, AccionTechos accion, List<SsUsuario> usuarios);
}