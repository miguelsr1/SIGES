/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.utilidades;

import java.util.List;
import sv.gob.mined.siges.web.dto.catalogo.SgAfCategoriaBienes;
import sv.gob.mined.siges.web.dto.catalogo.SgAfSeccionCategoria;
import sv.gob.mined.siges.web.enumerados.EnumSeccionesCargoBienes;

/**
 *
 * @author Sofis Solutions
 */
public class OperacionesGenerales {
    
    public static Boolean existeEnArray(String[] array, String value) {
        for(String s : array) {
            if(s.equals(value)) {
                return true;
            }
        }
        return false;
    }
    
    public static Boolean existeEnArray(List<SgAfSeccionCategoria> lista, EnumSeccionesCargoBienes value) {
        for(SgAfSeccionCategoria s : lista) {
            if(s.getScaSeccion().equals(value)) {
                return true;
            }
        }
        return false;
    }
    
    public static SgAfCategoriaBienes obtenerCategoriaEnArray(List<SgAfCategoriaBienes> lista, Long value) {
        for(SgAfCategoriaBienes s : lista) {
            if(s.getCabPk().equals(value)) {
                return s;
            }
        }
        return null;
    }
}
