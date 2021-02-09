package gob.mined.siap2.business.ejbs.impl;

import com.mined.siap2.utilidades.PersistenceHelper;
import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.entities.data.impl.Cuentas;
import gob.mined.siap2.entities.data.impl.TransferenciasComponente;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.filtros.FiltroCodiguera;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.persistence.dao.imp.CuentasDAO;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

@LocalBean
@Stateless(name = "CuentasBean")
public class CuentasBean {
    
    private static final Logger logger = Logger.getLogger(CuentasBean.class.getName());
    
    @Inject
    @JPADAO
    private GeneralDAO generalDAO;
    
    @Inject
    @JPADAO
    private CuentasDAO cuentasDAO;

    /**
     * Este método crea o actualiza una cuenta
     *
     * @param cuenta 
     */
    public void crearActualizar(Cuentas cuenta) {
        try {
            generalDAO.update(cuenta);
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOGeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            if (PersistenceHelper.isConstraintViolation(ex)) {
                ge.setCodigo(ConstantesErrores.ERROR_CODIGO_REPETIDO);
                ge.addError(ConstantesErrores.ERROR_CODIGO_REPETIDO_ANIO);
            } else {
                logger.log(Level.SEVERE, null, ex); 
                ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
                ge.addError(ex.getMessage());
            }
            throw ge;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            if (PersistenceHelper.isConstraintViolation(ex)) {
                ge.setCodigo(ConstantesErrores.ERROR_CODIGO_REPETIDO);
                ge.addError(ConstantesErrores.ERROR_CODIGO_REPETIDO_ANIO);
            } else {
                logger.log(Level.SEVERE, null, ex); 
                ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
                ge.addError(ex.getMessage());
            }
            throw ge;
        }

    }
    
    /**
     * Método que se encarga de eliminar una cuenta
     *
     * @param id Identificador de registro a eliminar
     */
    public void eliminar(Integer id) {
        try {
            Cuentas ai = (Cuentas) generalDAO.find(Cuentas.class, id);
            if (ai != null) {
                generalDAO.delete(ai);
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
 
    
    
    /**
     * Este método devuelve una cuenta filtrada por Id
     *
     * @param gesId
     * @return 
     */
    public Cuentas getCuentaById(Integer gesId) {
        return generalDAO.getEntityManager().find(Cuentas.class, gesId);
    }

    /**
     * Este método devuelve una Cuenta filtrada por su Codigo
     * @param codigo
     * @return 
     */
    public Cuentas getCuentaByCodigo(String codigo) {
        try {
            return cuentasDAO.getCuentaByCodigo(codigo);
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    /**
     * Este método devuelve una Cuenta filtrada por su Codigo y año
     * @param codigo
     * @return 
     */
    public Cuentas getCuentaByCodigoYAnioFiscal(String codigo, Integer anio) {
        try {
            return cuentasDAO.getCuentaByCodigoYAnioFiscal(codigo, anio);
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    
    /**
     * Este método devuelve todos los registros de cuentas
     *
     * @return 
     */
    public List<Cuentas> getCuentas() {
        try {
            return cuentasDAO.getCuentas();
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método devuelve todos los registros de Cuentas que sean padres.
     *
     * @return 
     */
    public List<Cuentas> getCuentasPadres() {
        try {
            return cuentasDAO.getCuentasPadres();
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    /**
     * Este método devuelve todos los registros de Cuentas que tengan el mismo
     * padre
     *
     * @param idPadre
     * @return 
     */
    public List<Cuentas> getCuentasByIdPadre(Integer idPadre) {
        try {
            return cuentasDAO.getCuentasByIdPadre(idPadre);
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    /**
     * Este método devuelve todos los registros de Cuentas filtradas por el AnioFiscal
     *
     * @param idAnio
     * @return 
     */
    public List<Cuentas> getCuentasByAnioFiscal(Integer idAnio) {
        try {
            return cuentasDAO.getCuentasByAnioFiscal(idAnio);
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    /**
     * Este método devuelve todos los registros de Cuentas que se encuentren
     * habilitadas
     *
     * @param idPadre
     * @return 
     */
    public List<Cuentas> getSubcuentasHabilitadasByIdPadre(Integer idPadre) {
        try {
            return cuentasDAO.getCuentasHijasHabilitadasByIdPadre(idPadre);
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    /**
     * Este método devuelve todos los registros de Cuentas que se encuentren habilitadas
     * @return 
     */
    public List<Cuentas> getCuentasPadreHabilitadas() {
        try {
            List<Cuentas> gestiones = cuentasDAO.getCuentasPadresHabilitados();
            return gestiones;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    /**
     * Este método devuelve todos los registros de Cuentas que se encuentren
     * habilitadas
     *
     * @return 
     */
    public List<Cuentas> getCuentasHijasHabilitadas() {
        try {
            return cuentasDAO.getCuentasHijasHabilitadas();
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    /**
     * Este método verifíca si una cuenta tiene relacion con otros registros de componente
     *
     * @param idCuenta
     * @return 
     */
    public List<Cuentas> getCuentaRelacionComponente(Integer idCuenta) {
        try {
            return cuentasDAO.getCuentaRelacionComponente(idCuenta);
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    public void crearCopiasUnidadesPresupuestariasPorAnio(AnioFiscal origen, AnioFiscal destino) throws GeneralException {
        try {
            BusinessException ge = new BusinessException();
            if(origen == null) {
               ge.addError(ConstantesErrores.ERR_ANIO_ORIGEN_VACIO); 
            }
            if(destino == null) {
               ge.addError(ConstantesErrores.ERR_ANIO_DESTINO_VACIO); 
            }
            
            if(origen != null && destino != null && (origen.getId().equals(destino.getId()))) {
                ge.addError(ConstantesErrores.ERR_ANIO_ORIGEN_IGUAL_ANIO_DESTINO); 
            }
            if (ge.getErrores().size() > 0) {
                throw ge;
            }
            List<Cuentas> cuentas = cuentasDAO.getCuentasByAnioFiscal(origen.getId());
            if(cuentas != null && !cuentas.isEmpty() && cuentas.size() > 0) {
                for(Cuentas cuenta : cuentas) {
                    //logger.info("Código Cuenta: " +cuenta.getCodigo() + ": Año destino: " + destino.getId());
                    Cuentas c = cuentasDAO.getCuentaByAnioFiscalAndCodigo(destino.getId(),cuenta.getCodigo());
                    
                    if(c == null) {
                        //logger.info("Unidad no encontrada. Código Cuenta: " +cuenta.getCodigo() + ": Año destino: " + destino.getId());
                        c = cuenta.clone();
                        c.setAnioFiscal(destino);
                    } else {
                        //logger.info("Unidad encontrada. Código Cuenta: " +cuenta.getCodigo() + ": Año destino: " + destino.getId() + ", Total lineas: " + (c.getCuentas() != null ? c.getCuentas().size() : null));
                    }
                    List<Cuentas> lineasAgregar = c.getCuentas();
                    List<Cuentas> lineasDuplicar = cuenta.getCuentas();
                    if(lineasDuplicar != null && !lineasDuplicar.isEmpty() && lineasDuplicar.size() > 0) {
                        for(Cuentas cu : lineasDuplicar) {
                            //logger.info("Linea código: " + cu.getCodigo());
                            Boolean agregar = Boolean.TRUE;
                            for(Cuentas cue : lineasAgregar) {
                                if(cue.getCodigo().trim().equals(cu.getCodigo().trim())) {
                                    agregar = Boolean.FALSE;
                                    break;
                                }
                            }
                            if(agregar) {
                                cu = cu.clone();
                                cu.setCuentaPadre(c);
                                lineasAgregar.add(cu);
                            }
                        }
                    }
                    //logger.info("Lineas finales a agregar");
                    for(Cuentas cue: lineasAgregar) {
                      //  logger.info("Linea código agregar: " + cue.getCodigo());
                    }
                    c.setCuentas(lineasAgregar);
                    if(c.getId() == null) {
                       generalDAO.persist(c); 
                    } else {
                        generalDAO.update(c);
                    }
                }
            }
        } catch (BusinessException be) {
            //Si es de tipo negocio envía la misma excepción
            throw be;
        } catch (Exception ex) {
            //Las demás excepciones se consideran técnicas
            logger.log(Level.SEVERE, ex.getMessage() , ex);
            TechnicalException ge = new TechnicalException();
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    /**
     * Este método verifíca si una cuenta tiene relacion con otros registros de transferencia componente
     *
     * @param idCuenta
     * @return 
     */
    public List<TransferenciasComponente> getCuentaTransferenciaComponente(Integer idCuenta) {
        try {
            return cuentasDAO.getCuentaTransferenciaComponente(idCuenta);
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    public List<Cuentas> getCuentasPorFiltro(FiltroCodiguera filtro) {
        try {
            List<CriteriaTO> criterios = new ArrayList<CriteriaTO>();

            if(filtro.getId()!= null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "id", filtro.getId());
                criterios.add(criterio);
            }
            if(filtro.getPadreId()!= null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cuentaPadre.id", filtro.getPadreId());
                criterios.add(criterio);
            }
            if(filtro.getAnioId()!= null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "anioFiscal.id", filtro.getAnioId());
                criterios.add(criterio);
            }
            if (filtro.getHabilitado() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "habilitado", filtro.getHabilitado());
                criterios.add(criterio);
            }
            if (filtro.getCodigo() != null && !filtro.getCodigo().isEmpty()) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "codigo", filtro.getCodigo().trim().toLowerCase());
                criterios.add(criterio);
            }
            if (filtro.getNombre() != null && !filtro.getNombre().isEmpty()) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "nombreBusqueda", filtro.getNombre().trim().toLowerCase());
                criterios.add(criterio);
            }
            CriteriaTO condicion = null;
            if (criterios.size() == 1) {
                condicion = criterios.get(0);
            } else {
                condicion = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[0]));
            }

            return generalDAO.findEntityByCriteria(Cuentas.class, condicion, filtro.getOrderBy(), filtro.getAscending(), filtro.getFirst(), filtro.getMaxResults(), filtro.getIncluirCampos());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }

    }
}
