/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.utilidades;

import java.util.List;
import sv.gob.mined.siges.enumerados.EnumSeccionesCargoBienes;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgAfSeccionCategoria;

/**
 *
 * @author Sofis Solutions
 */
public class OperacionesGenerales {
    
    public static Boolean existeEnArray(List<SgAfSeccionCategoria> lista, EnumSeccionesCargoBienes value) {
        for(SgAfSeccionCategoria s : lista) {
                if(s.getScaSeccion().equals(value)) {
                    return true;
                }
        }
        return false;
    }
    
}
