/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import java.time.temporal.ChronoUnit;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgActividadCalendario;


/**
 *
 * @author Sofis Solutions
 */
public class ActividadCalendarioValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param aca SgActividadCalendario
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgActividadCalendario aca) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (aca==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            
            if (StringUtils.isBlank(aca.getAcaNombre())) {
                ge.addError("acaNombre",Errores.ERROR_NOMBRE_VACIO);
            } else if (aca.getAcaNombre().length() > 255){
                ge.addError("acaNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
            
            if (aca.getAcaAmbito() == null){
                ge.addError("acaAmbito", Errores.ERROR_AMBITO_VACIO);
            } else {
                switch (aca.getAcaAmbito()){
                    case MINED:
                        break;
                    case DEPARTAMENTAL:
                        if (aca.getAcaDepartamento() == null){
                            ge.addError("acaDepartamento", Errores.ERROR_DEPARTAMENTO_VACIO);
                        }
                        break;
                    case SEDE:
                        if (aca.getAcaSede() == null){
                             ge.addError("acaSede", Errores.ERROR_SEDE_VACIO);
                        }
                        break;
                    default:
                        ge.addError("acaAmbito", Errores.ERROR_AMBITO_INCORRECTO);
                        break;
                }
            }
            
            if (aca.getAcaFechaDesde()==null) {
                 ge.addError("acaDesde", Errores.ERROR_FECHA_INICIO_VACIA);
            }
            if (aca.getAcaFechaHasta()==null) {
                 ge.addError("acaHasta", Errores.ERROR_FECHA_FIN_VACIA);
            }
            if (aca.getAcaTipo()==null) {
                ge.addError("acaTipo",Errores.ERROR_TIPO_ACTIVIDAD_VACIO);
            }
            if (aca.getAcaFechaHasta() != null && aca.getAcaFechaDesde() != null) {
                if (aca.getAcaFechaDesde().isAfter(aca.getAcaFechaHasta())) {
                    ge.addError("acaFechaDesde", Errores.ERROR_FECHA_DESDE_MAYOR_HASTA);
                }
                if (ChronoUnit.DAYS.between(aca.getAcaFechaDesde(), aca.getAcaFechaHasta()) > 365) {
                    ge.addError("calHasta", Errores.ERROR_RANGO_SUPERA_MAXIMO);
                }
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

    /**
     * Compara dos elementos del tipo.
     * Indica si los elementos contienen la misma información.
     *
     * @param c1 SgActividadCalendario
     * @param c2 SgActividadCalendario
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgActividadCalendario c1, SgActividadCalendario c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getAcaNombre(), c2.getAcaNombre());             
              
            }
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }
}
