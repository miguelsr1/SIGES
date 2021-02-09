/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.cache.annotation.CacheResult;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroCalendario;
import sv.gob.mined.siges.filtros.FiltroCodiguera;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgCalendario;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgConfiguracion;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgTipoCalendarioEscolar;
/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class ConfiguracionBean {

    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private CalendarioBean calendarioBean;

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgConfiguracion>
     * @throws GeneralException
     */
    @CacheResult(cacheName = Constantes.CONFIGURACIONES_CACHE)
    public List<SgConfiguracion> obtenerPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgConfiguracion> codDAO = new CodigueraDAO<>(em, SgConfiguracion.class);
            return codDAO.obtenerPorFiltro(filtro, null);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    /**
     * Devuelve elemento dado el codigo
     *
     * @param codigo String
     * @return SgConfiguracion
     * @throws GeneralException
     */
     @CacheResult(cacheName = Constantes.CONFIGURACIONES_CODIGO_CACHE)
    public SgConfiguracion obtenerPorCodigo(String codigo) throws GeneralException {
        try {
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setCodigoExacto(codigo);
            List<SgConfiguracion> conf = this.obtenerPorFiltro(fc);
            if (!conf.isEmpty()) {
                return conf.get(0);
            }
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
        return null;
    }

    public LocalDate obtenerFechaCierreMatriculasPorDefecto(Integer anio, SgTipoCalendarioEscolar tip, Boolean anual) throws GeneralException {
        Boolean esCohorteInternacional = Boolean.FALSE;

        FiltroCodiguera fcn = new FiltroCodiguera();
        if (anual) {
            if (Constantes.CALENDARIO_INTERNACIONAL.equals(tip.getTceCodigo())) {
                fcn.setCodigoExacto(Constantes.CONFIG_DIA_MES_CIERRE_MATRICULA_INTERNACIONAL);
                anio++;
            } else {
                fcn.setCodigoExacto(Constantes.CONFIG_DIA_MES_CIERRE_MATRICULA_NACIONAL);
            }
        } else {
            if (Constantes.CALENDARIO_INTERNACIONAL.equals(tip.getTceCodigo())) {
                fcn.setCodigoExacto(Constantes.CONFIG_DIA_MES_CIERRE_MATRICULA_INTERNACIONAL_PRIMER_COHORTE);
                esCohorteInternacional = Boolean.TRUE;

            } else {
                fcn.setCodigoExacto(Constantes.CONFIG_DIA_MES_CIERRE_MATRICULA_NACIONAL_PRIMER_COHORTE);
            }
        }
        List<SgConfiguracion> cnfs;
        try {
            cnfs = this.obtenerPorFiltro(fcn);
        } catch (Exception ex) {
            Logger.getLogger(ConfiguracionBean.class.getName()).log(Level.SEVERE, null, ex);
            BusinessException be = new BusinessException();
            be.addError("NO_SE_ENCONTRO_CONFIG_DIA_MES_CIERRE_MATRICULA");
            throw be;
        }
        if (cnfs.isEmpty()) {
            BusinessException be = new BusinessException();
            be.addError("NO_SE_ENCONTRO_CONFIG_DIA_MES_CIERRE_MATRICULA");
            throw be;
        }
        SgConfiguracion cnf = cnfs.get(0);
        String[] diaMes = cnf.getConValor().split("/");
        if (esCohorteInternacional) {
            LocalDate fecha = LocalDate.of(anio, Integer.parseInt(diaMes[1]), Integer.parseInt(diaMes[0]));

            FiltroCalendario fc = new FiltroCalendario();
            fc.setTipoCalendarioPk(tip.getTcePk());
            fc.setAnioLectivo(anio);
            fc.setIncluirCampos(new String[]{"calFechaInicio", "calFechaFin"});
            List<SgCalendario> listCal =calendarioBean.obtenerPorFiltro(fc);
            if(!(fecha.compareTo(listCal.get(0).getCalFechaInicio())>0 && fecha.compareTo(listCal.get(0).getCalFechaFin())<0)){
                anio++;
            }
        }

        return LocalDate.of(anio, Integer.parseInt(diaMes[1]), Integer.parseInt(diaMes[0]));
    }

}
