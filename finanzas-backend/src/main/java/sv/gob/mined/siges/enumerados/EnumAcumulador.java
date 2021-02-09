/*
 * SIGES
* Sofis Solutions
 */
package sv.gob.mined.siges.enumerados;

/**
 * Tipo de acumulación para las consultas acumulativas
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
