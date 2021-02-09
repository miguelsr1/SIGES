/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.web.enumerados;

/**
 *
 * @author jgiron
 */
public enum EnumAcumulador {
    AREA_INVERSION(1),
    SUB_AREA_INVERSION(2),
    RUBRO(3),
    COMPONENTE(4),
    UNIDAD_PRESUPUESTARIA(5),
    LINEA_PRESUPUESTARIA(6);

    Integer val = 0;

    EnumAcumulador(Integer val) {
        this.val = val;
    }
}
