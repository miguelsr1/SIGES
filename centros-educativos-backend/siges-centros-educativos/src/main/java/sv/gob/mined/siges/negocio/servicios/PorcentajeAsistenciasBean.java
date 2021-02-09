package sv.gob.mined.siges.negocio.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.dto.SgPorcentajeAsistenciasRequest;
import sv.gob.mined.siges.dto.SgPorcentajeAsistenciasResponse;
import sv.gob.mined.siges.filtros.FiltroAsistencia;
import sv.gob.mined.siges.persistencia.entidades.SgAsistencia;
import sv.gob.mined.siges.persistencia.entidades.SgEstudiante;

/**
 *
 * @author usuario
 */
@Stateless
@Traced
public class PorcentajeAsistenciasBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private AsistenciaBean asistenciaBean;

    public SgPorcentajeAsistenciasResponse obtenerPorcentaje(List<SgAsistencia> listAsistencia) {
        Integer inasistencias = 0;
        Integer asistencia = 0;
        Integer inasistenciasJustificadas = 0;
        Integer inasistenciasSinJustificar = 0;
        for (SgAsistencia elem : listAsistencia) {
            if (elem.getAsiInasistencia()) {
                inasistencias++;
                if (elem.getAsiMotivoInasistencia().getMinMotivoJustificado()) {
                    inasistenciasJustificadas++;
                } else {
                    inasistenciasSinJustificar++;
                }
            } else {
                asistencia++;
            }
        }

        SgPorcentajeAsistenciasResponse entity = new SgPorcentajeAsistenciasResponse();
        entity.setPinCantidadAsistencias(asistencia);
        entity.setPinCantidadInasistencias(inasistencias);
        entity.setPinCantidadInasistenciasJustificadas(inasistenciasJustificadas);
        entity.setPinCantidadInasistenciasSinJustificar(inasistenciasSinJustificar);
        if (asistencia + inasistencias > 0) {
            entity.setPinPorcentajeAsistencias((asistencia * 100) / (asistencia + inasistencias));
        } else {
            entity.setPinPorcentajeAsistencias(100);
        }

        return entity;
    }

    public List<SgPorcentajeAsistenciasResponse> obtenerPorcentajeListaAsistencias(SgPorcentajeAsistenciasRequest asis) {
        List<SgPorcentajeAsistenciasResponse> listPorcentaje = new ArrayList();
        if (asis.getPinEstudiantes() != null) {
            FiltroAsistencia fa = new FiltroAsistencia();
            fa.setIncluirCampos(new String[]{
                "asiEstudiante.estPk",
                "asiEstudiante.estVersion",
                "asiInasistencia",
                "asiMotivoInasistencia.minPk",
                "asiMotivoInasistencia.minMotivoJustificado",
                "asiMotivoInasistencia.minVersion",
                "asiVersion"});
            fa.setAsiSeccion(asis.getPinSeccion()!=null?asis.getPinSeccion():null);
            if(asis.getPinEstudiantes()!=null){
                List<Long> estudiantesPk=asis.getPinEstudiantes().stream().map(c->c.getEstPk()).collect(Collectors.toList());
                fa.setCaeEstudiantesPk(estudiantesPk);
            }
            fa.setAsiAnioLectivo(asis.getPinAnioLectivo()!=null?asis.getPinAnioLectivo():null);
            List<SgAsistencia> listAsis = asistenciaBean.obtenerPorFiltro(fa);
            for (SgEstudiante est : asis.getPinEstudiantes()) {
                List<SgAsistencia> listAsistenciaEstudiante = listAsis.stream().filter(c -> c.getAsiEstudiante().getEstPk().equals(est.getEstPk())).collect(Collectors.toList());
                SgPorcentajeAsistenciasResponse asisResp = obtenerPorcentaje(listAsistenciaEstudiante);
                asisResp.setPinEstudiante(est);
                listPorcentaje.add(asisResp);
            }

        }
        return listPorcentaje;
    }
}
