package gob.mined.siap2.business;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siges.entities.data.impl.SgAfBienDepreciable;
import gob.mined.siges.entities.data.impl.SgAfCategoriaBienes;
import gob.mined.siges.entities.data.impl.SgAfEstadosBienes;
import gob.mined.siges.entities.data.impl.SgAfEstadosCalidad;
import gob.mined.siges.entities.data.impl.SgAfFormaAdquisicion;
import gob.mined.siges.entities.data.impl.SgAfFuenteFinanciamiento;
import gob.mined.siges.entities.data.impl.SgAfTipoBienes;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.persistence.dao.imp.BienesDepreciablesDAO;
import gob.mined.siap2.persistence.dao.imp.LoteBienesDAO;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.apache.commons.lang3.StringUtils;

@LocalBean
@Stateless(name = "BienesDepreciablesBean")
public class BienesDepreciablesBean {
    private static final Logger logger = Logger.getLogger(BienesDepreciablesBean.class.getName());
    
    @Inject
    @JPADAO
    private GeneralDAO generalDAO;
    
    @Inject
    @JPADAO
    private BienesDepreciablesDAO bienesDAO;
    
    @Inject
    @JPADAO
    private LoteBienesDAO lotesDAO;
     /**
     * Este método crea o actualiza un registro de PresupuestoEscolar
     * @param pe
     */
    public void crearActualizar(SgAfBienDepreciable bien) {
        try {
            generalDAO.update(bien);
        } catch (BusinessException be) {
            throw be;
        } catch (DAOGeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    
    public void guardarBienes(List<SgAfBienDepreciable> bienes) {
        try {
            if(bienes != null && !bienes.isEmpty() && bienes.size() > 0) {
                SgAfEstadosBienes estado = obtenerEstadoBienPorCodigo(ConstantesEstandares.CODIGO_ESTADO_EXISTENTE);
                SgAfEstadosCalidad calidad = obtenerCalidadBienPorCodigo(ConstantesEstandares.CODIGO_ESTADO_CALIDAD_BUENO);
                SgAfFormaAdquisicion formaAdq = obtenerFormaAdquisicionPorCodigo(ConstantesEstandares.CODIGO_FORMA_ADQUISICION_GOES);
                SgAfFuenteFinanciamiento fuente = obtenerFuenteFinanciamientoPorCodigo(ConstantesEstandares.CODIGO_FUENTE_RECURSO_GOES);
                for(SgAfBienDepreciable bien : bienes) {
                    bien.setBdeEstadosBienes(estado);
                    bien.setBdeEstadoCalidad(calidad);
                    bien.setBdeFuenteFinanciamiento(fuente);
                    bien.setBdeFormaAdquisicion(formaAdq);
                    
                    guardarBienDepreciable(bien);
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (DAOGeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    public SgAfBienDepreciable guardarBienDepreciable(SgAfBienDepreciable bien) throws DAOGeneralException, BusinessException{
        try {
            if(bien != null) {
                if(bien.getBdeTipoBien() == null) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_TIPO_BIEN_VACIO);
                    throw b;
                }
                if(bien.getBdeTipoBien().getTbiCategoriaBienes() == null) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_CATEGORIA_BIEN_VACIO);
                    throw b;
                }
                bien.setBdeCategoriaFk(bien.getBdeTipoBien().getTbiCategoriaBienes());
                
                bien.setBdeDocumentoAdquisicion(bien.getBdeDocumentoAdquisicion() != null ? bien.getBdeDocumentoAdquisicion() : "");
                
                bien.setBdeEsValorEstimado(Boolean.FALSE);
                bien.setBdeVersion(0);
                bien.setBdeCompletado(Boolean.FALSE);
                bien.setBdeDepreciado(Boolean.FALSE);
                bien.setBdeDepreciadoCompleto(Boolean.FALSE);
                bien.setBdeTipoBienCategoriaVinculada(Boolean.TRUE);
                bien.setBdeVidaUtil(bien.getBdeCategoriaFk() != null && bien.getBdeCategoriaFk().getCabVidaUtil() != null ? bien.getBdeCategoriaFk().getCabVidaUtil() : 5);
                
                Integer correlativo= bienesDAO.obtenerUltimoCorrelativo(bien.getBdeTipoBien().getTbiPk());
                correlativo = (correlativo != null ? correlativo : 0) + 1;
                String codigoCorrelativo = StringUtils.leftPad(correlativo.toString(), 3, "0");
                bien.setBdeNumCorrelativo(correlativo);
                bien.setBdeNumCorrelativoSiap(correlativo);
                bien.setBdeCodigoCorrelativo(codigoCorrelativo);
                bien.setBdeCodigoInventario(bien.getBdeTipoBien().getTbiCodigo() + "-" + codigoCorrelativo);
                
                
                if(bien.getBdeCantidadLote() != null && bien.getBdeCantidadLote() > 1) {
                    bien.setBdeEsLote(Boolean.FALSE);
                    bien.setBdeEsLoteSiap(Boolean.TRUE);
                }
                bien.setBdeBienEsFuenteSiap(Boolean.TRUE);
                bien = (SgAfBienDepreciable) generalDAO.update(bien);
                
                bienesDAO.guardarAuditoriaBienDepreciable(bien);
               
            }
            return bien;
        } catch (BusinessException be) {
            throw be;
        } catch (DAOGeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    public SgAfEstadosCalidad obtenerCalidadBienPorCodigo(String codigo) {
        try {
            List<SgAfEstadosCalidad> resultado = generalDAO.findByOneProperty(SgAfEstadosCalidad.class, "ecaCodigo", codigo);
            if (resultado.size() == 1) {
                return resultado.get(0);
            } else if (resultado.isEmpty()) {
                return null;
            } else {
                BusinessException be = new BusinessException();
                be.addError(ConstantesErrores.ERROR_DEMASIADOS_RESULTADOS);
                throw be;
            }
        } catch (DAOGeneralException ex) {
            logger.log(Level.SEVERE, ex.getMessage() , ex);
            TechnicalException te = new TechnicalException();
            te.addError(ex.getMessage());
            throw te;
        }
    }
    
    public SgAfCategoriaBienes obtenerCategoriaPorCodigo(String codigo) {
        try {
            List<SgAfCategoriaBienes> resultado = generalDAO.findByOneProperty(SgAfCategoriaBienes.class, "cabCodigo", codigo);
            if (resultado.size() == 1) {
                return resultado.get(0);
            } else if (resultado.isEmpty()) {
                return null;
            } else {
                BusinessException be = new BusinessException();
                be.addError(ConstantesErrores.ERROR_DEMASIADOS_RESULTADOS);
                throw be;
            }
        } catch (DAOGeneralException ex) {
            logger.log(Level.SEVERE, ex.getMessage() , ex);
            TechnicalException te = new TechnicalException();
            te.addError(ex.getMessage());
            throw te;
        }
    }
    
    public SgAfTipoBienes obtenerTipoBienPorCodigo(String codigo) {
        try {
            List<SgAfTipoBienes> resultado = generalDAO.findByOneProperty(SgAfTipoBienes.class, "tbiCodigo", codigo);
            if (resultado.size() == 1) {
                return resultado.get(0);
            } else if (resultado.isEmpty()) {
                return null;
            } else {
                BusinessException be = new BusinessException();
                be.addError(ConstantesErrores.ERROR_DEMASIADOS_RESULTADOS);
                throw be;
            }
        } catch (DAOGeneralException ex) {
            logger.log(Level.SEVERE, ex.getMessage() , ex);
            TechnicalException te = new TechnicalException();
            te.addError(ex.getMessage());
            throw te;
        }
    }
    
    public SgAfFormaAdquisicion obtenerFormaAdquisicionPorCodigo(String codigo) {
        try {
            List<SgAfFormaAdquisicion> resultado = generalDAO.findByOneProperty(SgAfFormaAdquisicion.class, "fadCodigo", codigo);
            if (resultado.size() == 1) {
                return resultado.get(0);
            } else if (resultado.isEmpty()) {
                return null;
            } else {
                BusinessException be = new BusinessException();
                be.addError(ConstantesErrores.ERROR_DEMASIADOS_RESULTADOS);
                throw be;
            }
        } catch (DAOGeneralException ex) {
            logger.log(Level.SEVERE, ex.getMessage() , ex);
            TechnicalException te = new TechnicalException();
            te.addError(ex.getMessage());
            throw te;
        }
    }
    
    public SgAfFuenteFinanciamiento obtenerFuenteFinanciamientoPorCodigo(String codigo) {
        try {
            List<SgAfFuenteFinanciamiento> resultado = generalDAO.findByOneProperty(SgAfFuenteFinanciamiento.class, "ffiCodigo", codigo);
            if (resultado.size() == 1) {
                return resultado.get(0);
            } else if (resultado.isEmpty()) {
                return null;
            } else {
                BusinessException be = new BusinessException();
                be.addError(ConstantesErrores.ERROR_DEMASIADOS_RESULTADOS);
                throw be;
            }
        } catch (DAOGeneralException ex) {
            logger.log(Level.SEVERE, ex.getMessage() , ex);
            TechnicalException te = new TechnicalException();
            te.addError(ex.getMessage());
            throw te;
        }
    }
    
    public SgAfEstadosBienes obtenerEstadoBienPorCodigo(String codigo) {
        try {
            List<SgAfEstadosBienes> resultado = generalDAO.findByOneProperty(SgAfEstadosBienes.class, "ebiCodigo", codigo);
            if (resultado.size() == 1) {
                return resultado.get(0);
            } else if (resultado.isEmpty()) {
                return null;
            } else {
                BusinessException be = new BusinessException();
                be.addError(ConstantesErrores.ERROR_DEMASIADOS_RESULTADOS);
                throw be;
            }
        } catch (DAOGeneralException ex) {
            logger.log(Level.SEVERE, ex.getMessage() , ex);
            TechnicalException te = new TechnicalException();
            te.addError(ex.getMessage());
            throw te;
        }
    }
    
    public List<SgAfTipoBienes> obtenerTiposBienPorQuery(String query) {
        return bienesDAO.obtenerTiposBienPorQuery(query);
    }
}
