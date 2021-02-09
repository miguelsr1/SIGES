/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import sv.gob.mined.siges.dto.ResultadoAcumuladorDTO;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroAcumulador;

/**
 * Clase genérica para la consulta de datos acumulativas
 *
 * @author Sofis Solutions
 */
@Stateless
public class ConsultasAcumuladoresBean {

    @PersistenceContext
    private EntityManager em;

    /**
     * Devuelve el resultado acumulador que satisfacen el filtro
     * @param filtro
     * @return
     * @throws GeneralException
     */
    public List<ResultadoAcumuladorDTO> obtenerAcumulados(FiltroAcumulador filtro) throws GeneralException {
        try {

            switch (filtro.getAcum1()) {
                case AREA_INVERSION:
                    return obtenerAcumuladosPorAreaInversion(filtro);
                case RUBRO:
                    return obtenerAcumuladosPorRubro(filtro);
                default:
                    return new ArrayList();
            }
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    private List<ResultadoAcumuladorDTO> obtenerAcumuladosPorAreaInversion(FiltroAcumulador filtro) throws GeneralException {

        String consulta = "";
        if (filtro.getAcum2() != null) {
            consulta = "select padre.ai_nombre as nombrePadre,hija.ai_nombre"
                    + ",sum("
                    + (filtro.getFormulado() ? "mov_monto" : "mov_monto_aprobado")
                    + ") from finanzas.sg_presupuesto_escolar_movimiento \n"
                    + "left join siap2.ss_areas_inversion hija on mov_subarea_inversion_pk=ai_id \n"
                    + "left join siap2.ss_areas_inversion padre on hija.ai_area_padre=padre.ai_id\n"
                    + "group by padre.ai_nombre,hija.ai_nombre "
                    + "order by padre.ai_nombre,hija.ai_nombre";
        } else {
            consulta = "select padre.ai_nombre as nombrePadre"
                    + ",sum("
                    + (filtro.getFormulado() ? "mov_monto" : "mov_monto_aprobado")
                    + ") from finanzas.sg_presupuesto_escolar_movimiento \n"
                    + "left join siap2.ss_areas_inversion hija on mov_subarea_inversion_pk=ai_id \n"
                    + "left join siap2.ss_areas_inversion padre on hija.ai_area_padre=padre.ai_id\n"
                    + "group by padre.ai_nombre "
                    + "order by padre.ai_nombre ";
        }
        Query query = em.createNativeQuery(consulta);
        List resultado = query.getResultList();
        List<ResultadoAcumuladorDTO> respuesta = new ArrayList<>();
        resultado.forEach(
                z -> {
                    Object[] fila = (Object[]) z;
                    respuesta.add(transformarFilaEnDTO(fila, filtro));
                });
        return respuesta;

    }

    private List<ResultadoAcumuladorDTO> obtenerAcumuladosPorRubro(FiltroAcumulador filtro) throws GeneralException {

        String consulta = "select ru_nombre,sum("
                + (filtro.getFormulado() ? "mov_monto" : "mov_monto_aprobado")
                + ") as formulado   from finanzas.sg_presupuesto_escolar_movimiento \n"
                + "left join siap2.ss_rubro  on ru_id=mov_rubro_pk\n"
                + "group by ru_nombre \n"
                + "order by ru_nombre";
        Query query = em.createNativeQuery(consulta);
        List resultado = query.getResultList();
        List<ResultadoAcumuladorDTO> respuesta = new ArrayList<>();
        resultado.forEach(
                z -> {
                    Object[] fila = (Object[]) z;
                    respuesta.add(transformarFilaEnDTO(fila, filtro));
                });
        return respuesta;

    }

    /**
     * Transforma una fila en el objeto ResultadoAcumuladorDTO
     * @param fila
     * @param filtro
     * @return
     */
    public ResultadoAcumuladorDTO transformarFilaEnDTO(Object[] fila, FiltroAcumulador filtro) {

        ResultadoAcumuladorDTO ele = new ResultadoAcumuladorDTO();
        int i = 0;
        ele.setAcum1(fila[i] != null ? fila[i].toString() : "-");
        if (filtro.getAcum2() != null) {
            i = i + 1;
            ele.setAcum2(fila[i] != null ? fila[i].toString() : "-");
        }
        i = i + 1;
        ele.setValor((BigDecimal) fila[i]);

        return ele;
    }

}
