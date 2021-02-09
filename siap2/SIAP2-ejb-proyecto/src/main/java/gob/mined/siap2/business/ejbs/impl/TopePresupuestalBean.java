package gob.mined.siap2.business.ejbs.impl;

import com.mined.siap2.to.TopePresupuestalArchivoTo;
import gob.mined.siap2.business.datatype.DataValoresMontos;
import gob.mined.siap2.filtros.FiltroTopePresupuestal;
import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.data.impl.Cuentas;
import gob.mined.siap2.entities.data.impl.FuenteRecursos;
import gob.mined.siap2.entities.data.impl.RelacionGesPresEsAnioFiscal;
import gob.mined.siap2.entities.data.impl.RelacionPresAnioFinanciamiento;
import gob.mined.siges.entities.data.impl.SgPresupuestoEscolarMovimiento;
import gob.mined.siges.entities.data.impl.SgSede;
import gob.mined.siap2.entities.data.impl.TopePresupuestal;
import gob.mined.siap2.entities.data.impl.TopePresupuestalGroup;
import gob.mined.siap2.entities.data.impl.TotalesMatriculas;
import gob.mined.siap2.entities.enums.EnumEstadoOrganismoAdministrativo;
import gob.mined.siap2.entities.enums.EstadoMovimientos;
import gob.mined.siap2.entities.enums.EstadoPresupuestoEscolar;
import gob.mined.siap2.entities.enums.TipoMonto;
import gob.mined.siap2.entities.enums.EstadoTopePresupuestal;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.filtros.FiltroRelPresAnioFiscal;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.persistence.dao.exceptions.DAOObjetoNoEditableException;
import gob.mined.siap2.persistence.dao.imp.PresupuestoEscolarDAO;
import gob.mined.siap2.persistence.dao.imp.TopePresupuestalDAO;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siges.entities.data.impl.SgMovimientosEdicion;
import gob.mined.siges.entities.data.impl.SgOrganismoAdministracionEscolar;
import gob.mined.siges.entities.data.impl.SgPresupuestoEscolar;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.jboss.ejb3.annotation.TransactionTimeout;

@LocalBean
@Stateless(name = "TopePresupuestalBean")
public class TopePresupuestalBean {

    private static final Logger logger = Logger.getLogger(TopePresupuestalBean.class.getName());

    @Inject
    @JPADAO
    private GeneralDAO generalDAO;
    @Inject
    @JPADAO
    private TopePresupuestalDAO presupuestalDAO;
    @Inject
    private SedeBean sedeBean;
    @Inject
    private CuentasBean cuentasBean;
    @Inject
    @JPADAO
    private PresupuestoEscolarDAO presupuestoEscolarDAO;

    /**
     * Este método crea o actualiza un TopePresupuestal
     *
     * @param tp
     * @return 
     */
    public TopePresupuestal crearActualizar(TopePresupuestal tp) {
        try {
            SgPresupuestoEscolarMovimiento movi = crearActualizarMovimiento(tp.getPresupuestoEscolarMovimiento());
            tp.setPresupuestoEscolarMovimiento(movi);
            if (tp.getId() == null) {
                return (TopePresupuestal) generalDAO.create(tp);
            } else {
                return (TopePresupuestal) generalDAO.update(tp);
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método crea o actualiza un TopePresupuestal
     *
     * @param tp
     * @return 
     */
    public TopePresupuestal crearActualizarTopeMovimiento(TopePresupuestal tp) {
        try {
            
            if(tp.getMonto() == null) {
                tp.setMonto(BigDecimal.ZERO);
            }
            if(tp.getMontoAprobado() == null) {
                tp.setMontoAprobado(BigDecimal.ZERO);
            }
          
            
           if((tp.getId() == null || tp.getId() == 0) && tp.getEstado().equals(EstadoTopePresupuestal.EN_PROCESO)){
                tp.setPresupuestoEscolarMovimiento(obtenerNuevoMovimiento(tp));              
            }else if(tp.getId() != null && tp.getEstado().equals(EstadoTopePresupuestal.APROBACION)){
                if(tp.getPresupuestoEscolarMovimiento() == null || tp.getPresupuestoEscolarMovimiento().getId() == null){
                    tp.setPresupuestoEscolarMovimiento(obtenerNuevoMovimiento(tp));
                }
            }else if(tp.getId() != null && tp.getEstado().equals(EstadoTopePresupuestal.EN_PROCESO)){
                if(tp.getPresupuestoEscolarMovimiento() == null || tp.getId() == 0){
                    tp.setPresupuestoEscolarMovimiento(obtenerNuevoMovimiento(tp));
                }
            }
           //tp.setPresupuestoEscolarMovimiento(obtenerNuevoMovimiento(tp)); 
           if(tp.getPresupuestoEscolarMovimiento() != null){
                tp.getPresupuestoEscolarMovimiento().setMonto(tp.getMonto());
                tp.getPresupuestoEscolarMovimiento().setMontoAprobado((tp.getMontoAprobado() != null ? tp.getMontoAprobado() : BigDecimal.ZERO));
                tp.getPresupuestoEscolarMovimiento().getPresupuestoEscolar().setSede(tp.getSede());
               
               //POR VALIDAR QUE SEA ASÍ
               if(tp.getPresupuestoEscolarMovimiento() != null && tp.getPresupuestoEscolarMovimiento().getPresupuestoEscolar() != null 
                       && tp.getPresupuestoEscolarMovimiento().getPresupuestoEscolar().getPk() == null) {
                    //if(tp.getEstado().equals(EstadoTopePresupuestal.EN_PROCESO)) {
                        //tp.getPresupuestoEscolarMovimiento().getPresupuestoEscolar().setEstado(EstadoPresupuestoEscolar.EN_PROCESO);
                   if(tp.getEstado().equals(EstadoTopePresupuestal.APROBACION) && (tp.getPresupuestoEscolarMovimiento().getPresupuestoEscolar().getEstado().equals(EstadoPresupuestoEscolar.EN_PROCESO)
                           || tp.getPresupuestoEscolarMovimiento().getPresupuestoEscolar().getEstado().equals(EstadoPresupuestoEscolar.FORMULADO))) {
                        tp.getPresupuestoEscolarMovimiento().getPresupuestoEscolar().setEstado(EstadoPresupuestoEscolar.EN_AJUSTE);
                   }
                } else {
                   if(tp.getEstado().equals(EstadoTopePresupuestal.APROBACION) && (tp.getPresupuestoEscolarMovimiento().getPresupuestoEscolar().getEstado().equals(EstadoPresupuestoEscolar.EN_PROCESO)
                           || tp.getPresupuestoEscolarMovimiento().getPresupuestoEscolar().getEstado().equals(EstadoPresupuestoEscolar.FORMULADO))) {
                        tp.getPresupuestoEscolarMovimiento().getPresupuestoEscolar().setEstado(EstadoPresupuestoEscolar.EN_AJUSTE);
                   } //else if(tp.getPresupuestoEscolarMovimiento().getPresupuestoEscolar().getEstado().equals(EstadoPresupuestoEscolar.FORMULADO)) {
                     //  tp.getPresupuestoEscolarMovimiento().getPresupuestoEscolar().setEstado(EstadoPresupuestoEscolar.EN_PROCESO);
                   //}
                }
            }
            
            return crearActualizar(tp);
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    /**
     * Este método crea o actualiza un TopePresupuestal
     *
     * @param tp
     * @return 
     */
    public TopePresupuestal crearActualizarTopeMovimiento(TopePresupuestal tp, TipoMonto tipo) {
        try {
            
            if(tp.getMonto() == null) {
                tp.setMonto(BigDecimal.ZERO);
            }
            if(tp.getMontoAprobado() == null) {
                tp.setMontoAprobado(BigDecimal.ZERO);
            }
          
            //creamos o actualizamos el techo
            if(tp.getId() == null) {
                tp = (TopePresupuestal) generalDAO.create(tp);
            } else {
                tp = (TopePresupuestal) generalDAO.update(tp);
            }
            
            SgPresupuestoEscolarMovimiento movi = null;
            SgPresupuestoEscolar presupuesto = obtenerPresupuesto(tp,tipo); //obenemos el presupuesto
            
            movi = presupuestoEscolarDAO.getMovimientoPresupuestoByPresupuestoId(tp.getId());//obtenemos el movimiento del presupuesto
            
            if(movi == null) {
                movi  = new SgPresupuestoEscolarMovimiento();
            }
            
            if(presupuesto.getPk() == null){
                movi.setNumMovimiento(1);
            }else{
                Integer movimientoActual = getMaxMovimiento(presupuesto.getPk());
                movi.setNumMovimiento(movimientoActual == null ? 1 : (movimientoActual+1));
            }
            
            movi.setOrigen(EstadoMovimientos.Values.TRANSFERENCIA);
            movi.setTipo(EstadoMovimientos.Values.INGRESO);
            movi.setMonto(tp.getMonto());
            movi.setMontoAprobado(tp.getMontoAprobado());
            movi.setAnulacion(Boolean.FALSE);
            movi.seteEditado(Boolean.FALSE);
            Boolean editado = Boolean.FALSE;    
            if(presupuesto.getEstado().equals(EstadoPresupuestoEscolar.APROBADO)) {
                editado = Boolean.TRUE; 
                presupuesto.setEstado(EstadoPresupuestoEscolar.EDITADO);
                movi.setAnulacion(Boolean.TRUE);
                movi.seteEditado(Boolean.TRUE);
            }
            
            //Creamos o actualizamos el presupuesto
            presupuesto = crearActualizarPresupuesto(presupuesto);

            movi.setPresupuestoEscolar(presupuesto);
            movi.setTopePresupuestal(tp);
           
            //Creamos o actualizamos el movimiento
            if(movi.getId() == null) {
                movi = crearActualizarMovimientoPresupuesto(movi);
            } else {
                movi = crearActualizarMovimientoPresupuesto(movi);
            }
            //si debe crearse el registro de movimiento en edicion
            if(editado) {
                SgMovimientosEdicion edicion =  obtenerMovimientoEnEdicion(movi);
                crearActualizarMovimientoEdiciponPresupuesto(edicion);
            }
            
            tp.setPresupuestoEscolarMovimiento(movi);
            return (TopePresupuestal) generalDAO.update(tp);
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    /**
     * Este método crea un nuevo objeto de tipo PresupuestoEscolarMovimiento
     * @return 
     */
    public SgPresupuestoEscolarMovimiento obtenerNuevoMovimiento(TopePresupuestal tope){
        try {
            SgPresupuestoEscolarMovimiento movi  = new SgPresupuestoEscolarMovimiento();
            movi.setOrigen(EstadoMovimientos.Values.TRANSFERENCIA);
            movi.setTipo(EstadoMovimientos.Values.INGRESO);
            movi.setMonto(tope.getMonto());
            movi.setMontoAprobado(tope.getMontoAprobado());
            movi.setTopePresupuestal(tope);
            SgPresupuestoEscolar pe = presupuestoEscolarDAO.getPresupuestoEscolarByAnioYSede(tope.getAnioFiscal().getAnio(), tope.getSede().getId());
            if(pe == null){
                pe = crearPresupuestoEscolar(tope);
                movi.setNumMovimiento(1);
            }else{
                
                Integer movimientoActual = getMaxMovimiento(pe.getPk());
                movi.setNumMovimiento(movimientoActual == null ? 1 : (movimientoActual+1));
                if(!pe.getEstado().equals(EstadoPresupuestoEscolar.APROBADO)) {
                    if(tope.getEstado().equals(EstadoTopePresupuestal.EN_PROCESO)) {
                        pe.setEstado(EstadoPresupuestoEscolar.EN_PROCESO);
                    } else {
                        pe.setEstado(EstadoPresupuestoEscolar.EN_AJUSTE);
                    } 
                }
                 
                pe.setCodigo(tope.getAnioFiscal().getAnio()+"-"+tope.getSede().getCodigo());
            }
            movi.setPresupuestoEscolar(pe);
           // movi = (SgPresupuestoEscolarMovimiento) generalDAO.merge(movi);
            return movi;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    /**
     * Este método crea un nuevo objeto de tipo PresupuestoEscolarMovimiento
     * @return 
     */
    public SgPresupuestoEscolarMovimiento crearActualizarMovimientoPresupuesto(TopePresupuestal tope, SgPresupuestoEscolar presupuesto){
        try {
            SgPresupuestoEscolarMovimiento movi  = null;
            if(tope.getId() != null) {
                movi = presupuestoEscolarDAO.getMovimientoPresupuestoByPresupuestoId(tope.getId());
            } else {
                movi = new SgPresupuestoEscolarMovimiento();
            }
            movi.setOrigen(EstadoMovimientos.Values.TRANSFERENCIA);
            movi.setTipo(EstadoMovimientos.Values.INGRESO);
            movi.setMonto(tope.getMonto());
            movi.setMontoAprobado(tope.getMontoAprobado());
            movi.setTopePresupuestal(tope);
            SgPresupuestoEscolar pe = presupuestoEscolarDAO.getPresupuestoEscolarByAnioYSede(tope.getAnioFiscal().getAnio(), tope.getSede().getId());
            if(pe == null){
                pe = crearPresupuestoEscolar(tope);
                movi.setNumMovimiento(1);
            }else{
                
                Integer movimientoActual = getMaxMovimiento(pe.getPk());
                movi.setNumMovimiento(movimientoActual == null ? 1 : (movimientoActual+1));
                if(!pe.getEstado().equals(EstadoPresupuestoEscolar.APROBADO)) {
                    if(tope.getEstado().equals(EstadoTopePresupuestal.EN_PROCESO)) {
                        pe.setEstado(EstadoPresupuestoEscolar.EN_PROCESO);
                    } else {
                        pe.setEstado(EstadoPresupuestoEscolar.EN_AJUSTE);
                    } 
                }
                 
                pe.setCodigo(tope.getAnioFiscal().getAnio()+"-"+tope.getSede().getCodigo());
            }
            movi.setPresupuestoEscolar(pe);
           // movi = (SgPresupuestoEscolarMovimiento) generalDAO.merge(movi);
            return movi;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    public SgPresupuestoEscolar obtenerPresupuesto(TopePresupuestal tope, TipoMonto tipo){
        try {
            SgPresupuestoEscolar pe = presupuestoEscolarDAO.getPresupuestoEscolarByAnioYSede(tope.getAnioFiscal().getAnio(), tope.getSede().getId());
            if(pe == null){
                pe = crearPresupuestoEscolar(tope);
                if(tipo.equals(TipoMonto.FORMULADO)) {
                    pe.setEstado(EstadoPresupuestoEscolar.EN_PROCESO);//Si es nuevo, se crea con estado EN_PROCESO
                } else if(tipo.equals(TipoMonto.APROBADO)) {
                    pe.setEstado(EstadoPresupuestoEscolar.EN_AJUSTE);//Si es nuevo, se crea con estado EN_AJUSTE
                }
            }else{
                if(tipo.equals(TipoMonto.FORMULADO)) {
                    if(pe.getEstado().equals(EstadoPresupuestoEscolar.FORMULADO)) {
                        pe.setEstado(EstadoPresupuestoEscolar.EN_PROCESO); //Si está en formulado, vuelvo a EN_PROCESO
                    }
                } else if(tipo.equals(TipoMonto.APROBADO)) {
                    if(pe.getEstado().equals(EstadoPresupuestoEscolar.EN_PROCESO) || pe.getEstado().equals(EstadoPresupuestoEscolar.FORMULADO) || pe.getEstado().equals(EstadoPresupuestoEscolar.EN_AJUSTE)) {
                        pe.setEstado(EstadoPresupuestoEscolar.EN_AJUSTE); //Si está EN_PROCESO o FORMULADO, o EN_AJUSTE, se cambia a EN_AJUSTE
                    }else if(pe.getEstado().equals(EstadoPresupuestoEscolar.AJUSTADO)) {
                        pe.setEstado(EstadoPresupuestoEscolar.EN_AJUSTE); //Si está en AJUSTADO, vuelvo a EN_AJUSTE
                    } //else if(pe.getEstado().equals(EstadoPresupuestoEscolar.APROBADO)) {
                       // pe.setEstado(EstadoPresupuestoEscolar.EDITADO); //Si está en APROBADO, vuelvo a EDITADO
                    //} 
                }
                //pe.setCodigo(tope.getAnioFiscal().getAnio()+"-"+tope.getSede().getCodigo());
            }
            return pe;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    /**
     * Metodo utilizado para la creacion de un nuevo OBJETO de PresupuestoEscolar
     * @return 
     */
    public SgPresupuestoEscolar crearPresupuestoEscolar(TopePresupuestal tope){
        try {
            SgPresupuestoEscolar pe = new SgPresupuestoEscolar();
            pe.setAnioFiscal(tope.getAnioFiscal());
            pe.setNombre(tope.getAnioFiscal().getAnio()+"-"+tope.getSede().getCodigo());
            pe.setCodigo(tope.getAnioFiscal().getAnio()+"-"+tope.getSede().getCodigo());
            pe.setSede(tope.getSede());
           // if(tope.getEstado().equals(EstadoTopePresupuestal.EN_PROCESO)) {
                pe.setEstado(EstadoPresupuestoEscolar.EN_PROCESO);//Por default EN_PROCESO
           // } else {
               // pe.setEstado(EstadoPresupuestoEscolar.EN_AJUSTE);
           // }
            pe.setHabilitado(true);
            return pe;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    /**
     * Metodo utilizado para la creacion de un nuevo OBJETO de PresupuestoEscolar
     * @return 
     */
    public SgMovimientosEdicion obtenerMovimientoEnEdicion(SgPresupuestoEscolarMovimiento mov){
        try {
            SgMovimientosEdicion movEd = presupuestoEscolarDAO.getMovimientoEdicionByMovimientoId(mov.getId());
            if(movEd == null) {
                movEd = new SgMovimientosEdicion();
            }            
            movEd.setPresupuestoFk(mov.getPresupuestoEscolar());
            movEd.setTechoPresupuestal(mov.getTopePresupuestal());
            movEd.setOriginalEditado(mov);
            movEd.setMonto(mov.getMonto());
            movEd.setMontoAprobado(mov.getMontoAprobado());
            movEd.setNumMoviento(mov.getNumMovimiento() != null ? mov.getNumMovimiento() : 0);
           
            movEd.setMonto(mov.getMonto());
            movEd.setMontoAprobado(mov.getMontoAprobado());
            movEd.setTipo(EstadoMovimientos.Values.INGRESO_MODIFICADO);
            movEd.setOrigen(EstadoMovimientos.Values.TRANSFERENCIA);
            return movEd;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    /**
     * Este método crea o actualiza un registro de PresupuestoEscolarMovimiento
     *
     * @param movi
     * @return 
     */
    public SgPresupuestoEscolarMovimiento crearActualizarMovimiento(SgPresupuestoEscolarMovimiento movi) {
        try {
            if (movi != null) {
                SgPresupuestoEscolar pre = crearActualizarPresupuesto(movi.getPresupuestoEscolar());
                movi.setPresupuestoEscolar(pre);
                if (movi.getId() != null) {
                    logger.log(Level.INFO, "MOVIMIENTO ACTUALIZADO");
                    SgPresupuestoEscolarMovimiento pem = (SgPresupuestoEscolarMovimiento) generalDAO.update(movi);
                    presupuestoEscolarDAO.guardarAuditoriaPrespuestoEscolarMovimiento(pem);
                    return movi;
                } else {
                    logger.log(Level.INFO, "MOVIMIENTO CREADO");
                    SgPresupuestoEscolarMovimiento pem = (SgPresupuestoEscolarMovimiento) generalDAO.create(movi);
                    presupuestoEscolarDAO.guardarAuditoriaPrespuestoEscolarMovimiento(pem);
                    return movi;
                }
            }
            return null;
        } catch (DAOGeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    /**
     * Este método crea o actualiza un registro de PresupuestoEscolarMovimiento
     *
     * @param movi
     * @return 
     */
    public SgPresupuestoEscolarMovimiento crearActualizarMovimientoPresupuesto(SgPresupuestoEscolarMovimiento movi) {
        try {
            if (movi != null) {
                if (movi.getId() != null) {
                    logger.log(Level.INFO, "MOVIMIENTO ACTUALIZADO");
                    SgPresupuestoEscolarMovimiento pem = (SgPresupuestoEscolarMovimiento) generalDAO.update(movi);
                    presupuestoEscolarDAO.guardarAuditoriaPrespuestoEscolarMovimiento(pem);
                    return movi;
                } else {
                    logger.log(Level.INFO, "MOVIMIENTO CREADO");
                    SgPresupuestoEscolarMovimiento pem = (SgPresupuestoEscolarMovimiento) generalDAO.create(movi);
                    presupuestoEscolarDAO.guardarAuditoriaPrespuestoEscolarMovimiento(pem);
                    return movi;
                }
            }
            return null;
        } catch (DAOGeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    /**
     * Este método crea o actualiza un registro de PresupuestoEscolarMovimiento
     *
     * @param movi
     * @return 
     */
    public SgMovimientosEdicion crearActualizarMovimientoEdiciponPresupuesto(SgMovimientosEdicion movi) {
        try {
            if (movi != null) {
                if (movi.getId() != null) {
                    logger.log(Level.INFO, "MOVIMIENTO EDICIÓN ACTUALIZADO");
                    SgMovimientosEdicion pem = (SgMovimientosEdicion) generalDAO.update(movi);
                    presupuestoEscolarDAO.guardarAuditoriaPrespuestoEscolarMovimientoEdicion(pem);
                    return movi;
                } else {
                    logger.log(Level.INFO, "MOVIMIENTO EDICIÓN CREADO");
                    SgMovimientosEdicion pem = (SgMovimientosEdicion) generalDAO.create(movi);
                    presupuestoEscolarDAO.guardarAuditoriaPrespuestoEscolarMovimientoEdicion(pem);
                    return movi;
                }
            }
            return null;
        } catch (DAOGeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    /**
     * Este método crea o actualiza un registro de PresupuestoEscolar
     *
     * @param pre
     * @return 
     */
    public SgPresupuestoEscolar crearActualizarPresupuesto(SgPresupuestoEscolar pre) {
        try {
            if (pre != null) {
                if (pre.getPk() != null) {
                    logger.log(Level.INFO, "PRESUPUESTO ACTUALIZADO");
                    SgPresupuestoEscolar pe = (SgPresupuestoEscolar) generalDAO.update(pre);
                    presupuestoEscolarDAO.guardarAuditoriaPrespuestoEscolar(pe);
                    return pe;
                } else {
                    logger.log(Level.INFO, "PRESUPUESTO CREADO");
                    SgPresupuestoEscolar pe = (SgPresupuestoEscolar) generalDAO.create(pre);
                    presupuestoEscolarDAO.guardarAuditoriaPrespuestoEscolar(pe);
                    return pe;
                }
            }
            return null;
        } catch (DAOGeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    /**
     * Este método actualiza los estados de una lista de registros de TopePresupuestal
     *
     * @param listaTopes
     * @param estadoNuevo
     */
    public void actualizarEstadoTopes(List<TopePresupuestal> listaTopes, EstadoTopePresupuestal estadoNuevo) {
        try {
            if(listaTopes != null && !listaTopes.isEmpty() && estadoNuevo != null){
                for(TopePresupuestal top: listaTopes){
                    top.setEstado(estadoNuevo);
                    crearActualizar(top);
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    
    
    
    

    /**
     * Método que se encarga de eliminar un TopePresupuestal
     *
     * @param id Identificador de registro a eliminar
     */
    public void eliminar(Integer id) {
        try {
            TopePresupuestal ai = (TopePresupuestal) generalDAO.find(TopePresupuestal.class, id);
            if (ai != null) {
                generalDAO.delete(ai);
            }
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método busca un listado de registros que contengan el mismo anio
     *
     * @param anio
     * @return
     */
    public List<TopePresupuestal> getTopePresupuestalByAnio(Integer anio) {
        try {
            List<TopePresupuestal> gestiones = presupuestalDAO.getTopePresupuestalByAnio(anio);
            return gestiones;
        } catch (DAOGeneralException ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    /**
     * Este método obtiene todos los registros de TopePresupuestal cuyos componentes se relacionen a un mismo subComponente
     *
     * @param id
     * @return
     */
    public List<TopePresupuestal> getTopePresupuestalByIdCategoriaComponente(Integer id) {
        try {
            return presupuestalDAO.getTopePresupuestalByIdCategoriaComponente(id);
        } catch (DAOGeneralException ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    public List<TopePresupuestal> getTopesPresupuestalesByFiltro(FiltroTopePresupuestal filtro) {
        try {
            List<CriteriaTO> criterios = new ArrayList<CriteriaTO>();

            if (filtro.getAnioFiscalId() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "anioFiscal.id", filtro.getAnioFiscalId());
                criterios.add(criterio);
            }

            if (filtro.getComponentePresupuestoEscolarId() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "gesPresEs.id", filtro.getComponentePresupuestoEscolarId());
                criterios.add(criterio);
            }

            if (filtro.getCuentaId() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "subCuenta.id", filtro.getCuentaId());
                criterios.add(criterio);
            }

            if (filtro.getSedeId() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sede.id", filtro.getSedeId());
                criterios.add(criterio);
            }

            if (filtro.getDepartamentoId() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sede.direccion.departamento.pk", filtro.getDepartamentoId());
                criterios.add(criterio);
            }
            
            if(filtro.getEstado() != null){
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estado", filtro.getEstado());
                criterios.add(criterio);
            }
            
            if (filtro.getSedesId() != null && !filtro.getSedesId().isEmpty()){
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "sede.id", filtro.getSedesId());
                criterios.add(criterio);
            }
            
            if (filtro.getFuenteFinId() != null){
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "fuenteFinanciamiento.id", filtro.getFuenteFinId());
                criterios.add(criterio);
            }
            
            if (filtro.getFuenteRecursoId() != null ){
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "fuenteRecursos.id", filtro.getFuenteRecursoId());
                criterios.add(criterio);
            }
            
            if (filtro.getPresupuestoEscolarMovimientoTopeId() != null){
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "presupuestoEscolarMovimiento.topePresupuestal.id", filtro.getPresupuestoEscolarMovimientoTopeId());
                criterios.add(criterio);
            }
            if (filtro.getCompPresAnioFiscalId() != null){
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "relGesPres.id", filtro.getCompPresAnioFiscalId());
                criterios.add(criterio);
            }
            CriteriaTO condicion = null;
            if (criterios.size() == 1) {
                condicion = criterios.get(0);
            } else {
                condicion = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[0]));
            }

            return generalDAO.findEntityByCriteria(TopePresupuestal.class, condicion, filtro.getOrderBy(), filtro.getAscending(), filtro.getFirst(), filtro.getMaxResults(), filtro.getIncluirCampos());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }

    }

    /**
     * Este método busca un listado de registros que contengan el mismo ID de
     * AnioFiscal
     *
     * @param idAnio
     * @return
     */
    public List<TopePresupuestal> getTopePresupuestalByAnioId(Integer idAnio) {
        try {
            List<TopePresupuestal> gestiones = presupuestalDAO.getTopePresupuestalByAnioId(idAnio);
            gestiones.isEmpty();
            return gestiones;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método busca un listado de registros que contengan un mismo
     * componente de gestion escolar.
     *
     * @param idComponente
     * @return
     */
    public List<TopePresupuestal> getTopePresupuestalByComponente(Integer idComponente) {
        try {
            List<TopePresupuestal> gestiones = presupuestalDAO.getTopePresupuestalByGesPresEsId(idComponente);
            gestiones.isEmpty();
            return gestiones;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método busca un listado de registros que contengan un mismo numero
     * de cuenta
     *
     * @param idSubCuenta
     * @return
     */
    public List<TopePresupuestal> getTopePresupuestalBySubCuenta(Integer idSubCuenta) {
        try {
            List<TopePresupuestal> gestiones = presupuestalDAO.getTopePresupuestalBySubcuentaId(idSubCuenta);
            gestiones.isEmpty();
            return gestiones;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método busca un listado de registros que contengan un mismo numero
     * de cuenta
     *
     * @return
     */
    public List<TopePresupuestal> getTopePresupuestal() {
        try {
            return presupuestalDAO.getTopePresupuestal();
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método busca un registro de TopePresupuestal filtrado por su ID
     *
     * @param id
     * @return
     */
    public TopePresupuestal getTopePresupuestalById(Integer id) {
        try {
            return presupuestalDAO.getTopePresupuestalById(id);
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método agrupa los resultados de TopePresupuestal
     *
     * @param idAnio
     * @param idCategoria
     * @param idComponente
     * @return
     */
    public List<TopePresupuestalGroup> getTopePresupuestalAgrupado(Integer idAnio, Integer idCategoria, Integer idComponente) {
        try {
            return presupuestalDAO.getTopePresupuestalAgrupado(idAnio, idCategoria, idComponente);
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    /**
     * Este método agrupa los resultados de TopePresupuestal
     *
     * @param idAnio
     * @param idCategoria
     * @param idComponente
     * @return
     */
    public List<TopePresupuestalGroup> getTopePresupuestalFuentesAgrupado(Integer idPresupuesto) throws DAOGeneralException{
        try {
            return presupuestalDAO.getTopePresupuestalFuentesAgrupado(idPresupuesto);
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    /**
     * Este método agrupa los resultados de TopePresupuestal
     *
     * @param idAnio
     * @param idCategoria
     * @param idComponente
     * @return
     */
    public TotalesMatriculas getCantidadMatriculasPorPresupuesto(Integer idPresupuesto) throws DAOGeneralException{
        try {
            return presupuestalDAO.getCantidadMatriculasPorPresupuesto(idPresupuesto);
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    /**
     * Este método agrupa los resultados de TopePresupuestal
     *
     * @param sede
     * @return
     */
    public SgSede getSedeByCodigo(String sede) {
        try {
            return presupuestalDAO.getSedeByCodigo(sede);
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método busca un registro de movimiento, filtrado por ID
     *
     * @param id
     * @return
     */
    public SgPresupuestoEscolarMovimiento getSedeByCodigo(Integer id) {
        try {
            return presupuestalDAO.getMovimientoById(id);
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método buscan el numero de movimiento Maximo actual de la tabla
     * SgPresupuestoEscolarMovimiento
     *
     * @param idPresupuesto
     * @return
     */
    public Integer getMaxMovimiento(Integer idPresupuesto) {
        try {
            return presupuestalDAO.getMaxMovimiento(idPresupuesto);
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método busca todos los registros de TechoPresupuestal filtrados para
     * la generacion de Transferencias a centros educativos
     *
     * @param idComp
     * @param idSubComp
     * @param idUniPres
     * @param idLinPres
     * @return
     */
    public List<TopePresupuestal> generarTransferenciasCentroEducativo(Integer idComp, Integer idSubComp, Integer idUniPres, Integer idLinPres) {
        try {
            return presupuestalDAO.getTopePresupuestalByTransferencias(idComp, idSubComp, idUniPres, idLinPres);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }

    }

    /**
     * Este método obtiene todos los registros de TopePresupuestal cuyos componentes se relacionen a un mismo subComponente
     *
     * @param idComp
     * @param idSubComp
     * @param idUniPres
     * @param idLinPres
     * @return
     */
    public List<TopePresupuestal> getTopePresupuestalByIdComponentePresupuestoEscolar(Integer idComp) {
        try {
            return presupuestalDAO.getTopePresupuestalByIdComponentePresupuestoEscolar(idComp);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }

    }
    @TransactionTimeout(value = 5, unit = TimeUnit.MINUTES)
    public void crearDesdeArchivo(TopePresupuestalArchivoTo archivo) {
        try {
            Map<Cuentas, DataValoresMontos> topesSegunCuentas = new HashMap();
            Map<RelacionPresAnioFinanciamiento, DataValoresMontos> topesSegunFuentesRecursos = new HashMap();
            List <RelacionPresAnioFinanciamiento> listaSegunFuentesRecursos = new ArrayList();
            BusinessException be = new BusinessException();
            if (archivo.getAnioFiscal() == null) {
                be.addError(ConstantesErrores.ERR_DEBE_INGRESAR_AÑO_FISCAL);
            }
            
            if (archivo.getGesPresEs() == null || archivo.getRelGesPres() == null) {
                be.addError(ConstantesErrores.ERR_COMPONENTE_PRESUPUESTO_ESCOLAR_VACIO);
            }            
            if (archivo.getFile() == null) {
                be.addError(ConstantesErrores.ERR_DEBE_SELECIONAR_ARCHIVO);
            }
            if(archivo.getTipoMonto() == null){
                be.addError(ConstantesErrores.ERR_TIPO_MONTO_NO_INGRESADO);
            }
            
            if (!be.getErrores().isEmpty()) {
                throw be;
            }
            RelacionGesPresEsAnioFiscal relPresupuestoAnio = generalDAO.getEntityManager().find(RelacionGesPresEsAnioFiscal.class, archivo.getRelGesPres().getId());

            if(relPresupuestoAnio != null) {
                relPresupuestoAnio.setMontoTotalAprobado(new BigDecimal(BigInteger.ZERO));
                relPresupuestoAnio.setCantidadMatriculasAprobadas(0);
                if (archivo.getTipoMonto().equals(TipoMonto.APROBADO)) {
                    actualizarTopesPorRelGesPres(archivo.getRelGesPres().getId());
                } else {
                    eliminarTopesPorRelGesPres(archivo.getRelGesPres().getId());
                    relPresupuestoAnio.setMontoTotalFormulado(new BigDecimal(BigInteger.ZERO));
                    relPresupuestoAnio.setCantidadMatriculas(0);
                }
            }
            
            List<RelacionPresAnioFinanciamiento> listaRelFuentes = relPresupuestoAnio.getRelFinanciamiento();
            if(listaRelFuentes == null || listaRelFuentes.isEmpty()) {
                be.addError(ConstantesErrores.ERR_CODIGO_SUBCUENTA_INEXISTENTE, new String[]{relPresupuestoAnio.getDescripcion().trim()});
                throw be;
            }
            
            
            if(relPresupuestoAnio != null) {
                for(RelacionPresAnioFinanciamiento rel : listaRelFuentes) {
                    if(archivo.getTipoMonto().equals(TipoMonto.FORMULADO)) {
                        rel.setMontoTotalAprobado(BigDecimal.ZERO);
                        rel.setMontoTotalFormulado(BigDecimal.ZERO);
                    } else if (archivo.getTipoMonto().equals(TipoMonto.APROBADO)) {
                        rel.setMontoTotalAprobado(BigDecimal.ZERO);
                    }
                    listaSegunFuentesRecursos.add(rel);
                    topesSegunFuentesRecursos.put(rel, new DataValoresMontos(BigDecimal.ZERO, BigDecimal.ZERO));
                }
            }
            
            
            
            org.apache.poi.ss.usermodel.Workbook myWorkBook = WorkbookFactory.create(archivo.getFile().getInputStream());
            org.apache.poi.ss.usermodel.Sheet mySheet = myWorkBook.getSheetAt(0);

            // Get iterator to all the rows in current sheet
            Iterator<Row> rowIterator = mySheet.iterator();
            List<Cuentas> cuentas = new ArrayList<>();
            List<FuenteRecursos> fuentesRecurso = new ArrayList<>();
            
            List<TopePresupuestal> nuevosTopes = new ArrayList<>();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                if (row.getRowNum() == 0) {
                    //Obtener cuentas
                    Iterator<Cell> cellIterator = row.cellIterator();

                    if (cellIterator.hasNext()) {
                        cellIterator.next(); // Skip primera columna
                    }

                    whileCuentas:
                    while (cellIterator.hasNext()) {
                        String cuentaCodigo = getValor(cellIterator.next());
                        Cuentas cuenta = null;

                        if (!StringUtils.isBlank(cuentaCodigo)) {
                            cuenta = cuentasBean.getCuentaByCodigoYAnioFiscal(cuentaCodigo,relPresupuestoAnio.getAnioFiscal().getId());
                        } else {
                            break whileCuentas;
                        }

                        if (cuenta == null || cuenta.getCuentaPadre() == null) {
                            //Solamente subcuentas
                            be.addError(ConstantesErrores.ERR_CODIGO_SUBCUENTA_INEXISTENTE, new String[]{cuentaCodigo});
                            throw be;
                        }
                        if(!relPresupuestoAnio.getSubCuenta().getCodigo().equals(cuenta.getCodigo())) {
                            be.addError(ConstantesErrores.ERR_CODIGO_SUBCUENTA_INEXISTENTE_EN_PRESUPUESTO, new String[]{cuentaCodigo,relPresupuestoAnio.getDescripcion().trim()});
                            throw be;
                        }
                        cuentas.add(cuenta);
                    }

                } if (row.getRowNum() == 1) {
                    //Obtener fuentes de financiamiento
                    Iterator<Cell> cellIterator = row.cellIterator();

                    if (cellIterator.hasNext()) {
                        cellIterator.next(); // Skip primera columna
                    }

                    whileFuentesFinanciamiento:
                    while (cellIterator.hasNext()) {
                        String fRecurnsoCodigo = getValor(cellIterator.next());
                        FuenteRecursos fRecurso = null;

                        if (!StringUtils.isBlank(fRecurnsoCodigo)) {
                            List<FuenteRecursos> fuentes = generalDAO.findByOneProperty(FuenteRecursos.class, "codigo", fRecurnsoCodigo);
                            if(fuentes != null && !fuentes.isEmpty() && fuentes.size() > 0) {
                                fRecurso = fuentes.get(0);
                            }
                        } else {
                            break whileFuentesFinanciamiento;
                        }

                        if (fRecurso == null ) {
                            //Solamente fuentes de recurso
                            be.addError(ConstantesErrores.ERR_CODIGO_FUENTE_RECURSO_INEXISTENTE, new String[]{fRecurnsoCodigo});
                            throw be;
                        } else {
                            Boolean existe = Boolean.FALSE;
                            for(RelacionPresAnioFinanciamiento relFuente : listaRelFuentes) {
                                if(fRecurso.getId().equals(relFuente.getFuenteRecursos().getId())
                                        && fRecurso.getFuenteFinanciamiento().getId().equals(relFuente.getFuenteRecursos().getFuenteFinanciamiento().getId())) {
                                    fuentesRecurso.add(fRecurso);
                                    existe = Boolean.TRUE;
                                    break;
                                }
                            }  
                            if(!existe) {
                                be.addError(ConstantesErrores.ERR_CODIGO_FUENTE_RECURSO_INEXISTENTE_EN_COMPONENTE, new String[]{fRecurnsoCodigo,relPresupuestoAnio.getDescripcion().trim()});
                                throw be;
                            }
                        }
                        
                    }

                } else if (row.getRowNum() > 1) {
                    
                    try {
                        //Cuentas                    
                        Iterator<Cell> cellIterator = row.cellIterator();
                        if(cellIterator != null) {
                            //Sede
                            String sedCodigo = getValor(cellIterator.next());
                            SgSede sede = null;
                            if (!StringUtils.isBlank(sedCodigo)) {
                                sede = sedeBean.getSedeByCodigo(sedCodigo);
                            } else {
                                continue;
                            }
                            if (sede == null) {
                                be.addError(ConstantesErrores.ERR_CODIGO_SEDE_INEXISTENTE, new String[]{sedCodigo});
                                throw be;
                            }

                            whileCuentas:
                            while (cellIterator.hasNext()) {
                                if(cellIterator != null) {

                                        Cell cell = cellIterator.next();
                                        BigDecimal monto = getValorBigDecimal(cell);

                                        Integer cuentaIndex = cell.getColumnIndex() - 1;

                                        if (cuentaIndex >= cuentas.size()) {
                                            continue;
                                        }
                                        Boolean agregar = false;
                                        if(sede.getOrganismosAdministracionEscolar() != null && !sede.getOrganismosAdministracionEscolar().isEmpty()) {
                                            for(SgOrganismoAdministracionEscolar org : sede.getOrganismosAdministracionEscolar()) {
                                            
                                                if(org.getOaeMiembrosVigentes() != null && org.getOaeMiembrosVigentes() && org.getOeaEstado() != null && org.getOeaEstado().equals(EnumEstadoOrganismoAdministrativo.APROBADO)) {
                                                    agregar = true; 
                                                    break;
                                                }
                                            }
                                        }
                                        if(agregar) {
                                            TopePresupuestal tope = new TopePresupuestal();
                                            tope.setAnioFiscal(archivo.getAnioFiscal());
                                            tope.setGesPresEs(archivo.getGesPresEs());
                                            tope.setSede(sede);
                                            tope.setSubCuenta(cuentas.get(cuentaIndex));
                                            tope.setFuenteRecursos(fuentesRecurso.get(cuentaIndex));
                                            tope.setFuenteFinanciamiento(fuentesRecurso.get(cuentaIndex).getFuenteFinanciamiento());

                                            if(archivo.getTipoMonto().equals(TipoMonto.APROBADO)){
                                                tope.setMontoAprobado(monto);
                                                tope.setEstado(EstadoTopePresupuestal.APROBACION);
                                            }else if(archivo.getTipoMonto().equals(TipoMonto.FORMULADO)){
                                                tope.setMonto(monto);
                                                tope.setEstado(EstadoTopePresupuestal.EN_PROCESO);
                                            }

                                            nuevosTopes.add(tope);
                                            DataValoresMontos val = null;
                                            if(topesSegunCuentas.containsKey(cuentas.get(cuentaIndex))) {
                                                val = topesSegunCuentas.get(cuentas.get(cuentaIndex));

                                                BigDecimal mt1 = tope.getMonto() != null ? tope.getMonto() : new BigDecimal(BigInteger.ZERO);
                                                BigDecimal mt2 = tope.getMontoAprobado() != null ? tope.getMontoAprobado() : new BigDecimal(BigInteger.ZERO);


                                                BigDecimal mont = val.getMonto() != null ? val.getMonto() : new BigDecimal(BigInteger.ZERO);
                                                BigDecimal montAprobado = val.getMontoAprobado() != null ? val.getMontoAprobado() : new BigDecimal(BigInteger.ZERO);

                                                val.setMonto(mt1.add(mont));
                                                val.setMontoAprobado(mt2.add(montAprobado));
                                                topesSegunCuentas.put(cuentas.get(cuentaIndex), val);
                                            } else {
                                                val = new DataValoresMontos(tope.getMonto(), tope.getMontoAprobado());
                                                topesSegunCuentas.put(cuentas.get(cuentaIndex), new DataValoresMontos(tope.getMonto(), tope.getMontoAprobado()));
                                            }

                                            RelacionPresAnioFinanciamiento f = obtenerRelacionPresAnioFinanciamiento(listaSegunFuentesRecursos, relPresupuestoAnio.getId(), 
                                                    fuentesRecurso.get(cuentaIndex).getFuenteFinanciamiento().getId(), 
                                                    fuentesRecurso.get(cuentaIndex).getId());

                                            if(f != null) {
                                                if(topesSegunFuentesRecursos.containsKey(f)) {
                                                    DataValoresMontos d = topesSegunFuentesRecursos.get(f);

                                                    BigDecimal mt1 = tope.getMonto() != null ? tope.getMonto() : new BigDecimal(BigInteger.ZERO);
                                                    BigDecimal mt2 = tope.getMontoAprobado() != null ? tope.getMontoAprobado() : new BigDecimal(BigInteger.ZERO);


                                                    BigDecimal mont = d.getMonto() != null ? d.getMonto() : new BigDecimal(BigInteger.ZERO);
                                                    BigDecimal montAprobado = d.getMontoAprobado() != null ? d.getMontoAprobado() : new BigDecimal(BigInteger.ZERO);


                                                    d.setMonto(mt1.add(mont));
                                                    d.setMontoAprobado(mt2.add(montAprobado));

                                                    topesSegunFuentesRecursos.put(f, d);

                                                }
                                            }
                                        }
                                        
                                        
                                    }
                                }
                            }
                        }
                    catch (Exception e) {
                        logger.log(Level.SEVERE, "Error al leer la celda", e);
                    }
                }

            }

           
            for (TopePresupuestal t : nuevosTopes) {
                //Buscar tope existente y actualizar
                FiltroTopePresupuestal filtro = new FiltroTopePresupuestal();

                filtro.setAnioFiscalId(t.getAnioFiscal().getId());
                filtro.setComponentePresupuestoEscolarId(t.getGesPresEs().getId());
                filtro.setCuentaId(t.getSubCuenta().getId());
                filtro.setSedeId(t.getSede().getId());
                filtro.setCompPresAnioFiscalId(relPresupuestoAnio.getId());
                filtro.setFuenteFinId(t.getFuenteFinanciamiento().getId());
                filtro.setFuenteRecursoId(t.getFuenteRecursos().getId());
                
                List<TopePresupuestal> existentes = this.getTopesPresupuestalesByFiltro(filtro);
                if (existentes.isEmpty()) {
                    t.setRelGesPres(relPresupuestoAnio);
                   // crearActualizar(t);
                    t = crearActualizarTopeMovimiento(t);
                } else {
                    TopePresupuestal existente = existentes.get(0);
                    if(archivo.getTipoMonto().equals(TipoMonto.APROBADO)){
                        existente.setMontoAprobado(t.getMontoAprobado());
                        existente.setEstado(EstadoTopePresupuestal.APROBACION);
                    }else if(archivo.getTipoMonto().equals(TipoMonto.FORMULADO)){
                        existente.setMonto(t.getMonto());
                        existente.setEstado(EstadoTopePresupuestal.EN_PROCESO);
                    } else {
                        existente.setMonto(t.getMonto());
                    }
                    existente.setFuenteRecursos(t.getFuenteRecursos());
                    existente.setFuenteFinanciamiento(t.getFuenteFinanciamiento());
                    existente.setRelGesPres(relPresupuestoAnio);
                    //existente = crearActualizar(existente);
                    existente = crearActualizarTopeMovimiento(existente);
                }
            }
            
            if(relPresupuestoAnio != null) {
               // List<RelacionPresAnioFinanciamiento> listaRelFuentesActualizada2 = new ArrayList();
                for (Map.Entry<Cuentas, DataValoresMontos> entry : topesSegunCuentas.entrySet()) {
                    Cuentas cuenta = entry.getKey();
                    
                    if(relPresupuestoAnio.getSubCuenta() != null) {
                        if(relPresupuestoAnio.getSubCuenta().getCodigo().equals(cuenta.getCodigo())) {
                            DataValoresMontos datos = entry.getValue();

                            relPresupuestoAnio.setAnioFiscal(archivo.getAnioFiscal());
                            relPresupuestoAnio.setComponentePresupuestoEscolar(archivo.getGesPresEs());

                            if(archivo.getTipoMonto().equals(TipoMonto.APROBADO)){
                                relPresupuestoAnio.setMontoTotalAprobado(datos.getMontoAprobado());

                            }else if(archivo.getTipoMonto().equals(TipoMonto.FORMULADO)){
                                relPresupuestoAnio.setMontoTotalFormulado(datos.getMonto());

                            }
                            
                            if(listaSegunFuentesRecursos != null) {
                                for(RelacionPresAnioFinanciamiento rfin : listaSegunFuentesRecursos) {
                                    DataValoresMontos  montos = topesSegunFuentesRecursos.get(rfin);
                                    if(montos != null) { 
                                        if(archivo.getTipoMonto().equals(TipoMonto.APROBADO)){
                                            rfin.setMontoTotalAprobado(montos.getMontoAprobado());
                                        }else if(archivo.getTipoMonto().equals(TipoMonto.FORMULADO)){
                                            rfin.setMontoTotalFormulado(montos.getMonto());
                                        }
                                        
                                    } 
                                    
                                }
                            }
                            //listaRelFuentesActualizada2 = listaRelFuentesActualizada;
                            generalDAO.merge(relPresupuestoAnio);
                        }
                    }
                }
               //relPresupuestoAnio.setRelFinanciamiento(listaSegunFuentesRecursos);
               generalDAO.merge(relPresupuestoAnio);
            }
        } catch (BusinessException be) {
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }

    }

    public List<RelacionGesPresEsAnioFiscal> getComponentesPresupuestoEscolarByFiltro(FiltroRelPresAnioFiscal filtro) {
        try {
            List<CriteriaTO> criterios = new ArrayList<CriteriaTO>();

            if (filtro.getId() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "id", filtro.getId());
                criterios.add(criterio);
            }

            if (filtro.getComponentePresupuestoEscolarId() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "componentePresupuestoEscolar.id", filtro.getComponentePresupuestoEscolarId());
                criterios.add(criterio);
            }

            if (filtro.getCategoriaPresupuestoEscolarId() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "componentePresupuestoEscolar.categoriaPresupuestoEscolar.id", filtro.getCategoriaPresupuestoEscolarId());
                criterios.add(criterio);
            }
            
            if (filtro.getAnioFiscalId() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "anioFiscal.id", filtro.getAnioFiscalId());
                criterios.add(criterio);
            }
            if (filtro.getSubCuentaId() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "subCuenta.id", filtro.getSubCuentaId());
                criterios.add(criterio);
            }
            
            if (filtro.getTipo() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "tipo", filtro.getTipo());
                criterios.add(criterio);
            }

            if (!TextUtils.isEmpty(filtro.getNombreComplemento())) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "nombreComplemento", filtro.getNombreComplemento());
                criterios.add(criterio);
            }
            if (filtro.getIdFuenteFinanciamiento() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "relFinanciamiento.fuenteFinanciamiento.id", filtro.getIdFuenteFinanciamiento());
                criterios.add(criterio);
            }
            if (filtro.getIdFuenteRecursos() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "relFinanciamiento.fuenteRecursos.id", filtro.getIdFuenteRecursos());
                criterios.add(criterio);
            }
            

            CriteriaTO condicion = null;
            if (criterios.size() == 1) {
                condicion = criterios.get(0);
            } else {
                condicion = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[0]));
            }

            return generalDAO.findEntityByCriteria(RelacionGesPresEsAnioFiscal.class, condicion, filtro.getOrderBy(), filtro.getAscending(), filtro.getFirst(), filtro.getMaxResults(), filtro.getIncluirCampos());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }

    }
    
    public String getValor(Cell cell) {
        String respuesta = "";
        if (cell == null) {
            return respuesta;
        }
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_BOOLEAN:
                respuesta = cell.getBooleanCellValue() + "";
                break;
            case Cell.CELL_TYPE_NUMERIC:
                double d = cell.getNumericCellValue();
                respuesta = d + "";
                //Si termina en .0, le saco el .0
                if (respuesta.length() >= 2 && respuesta.substring(respuesta.length() - 2, respuesta.length()).equals(".0")) {
                    respuesta = respuesta.substring(0, respuesta.length() - 2);
                }
                break;
            case Cell.CELL_TYPE_STRING:
                respuesta = cell.getStringCellValue();
                break;
            case Cell.CELL_TYPE_BLANK:
                break;
            default:
        }
        return respuesta;
    }

    public BigDecimal getValorBigDecimal(Cell cell) {
        BigDecimal respuesta = null;
        if (cell != null && cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            String txt = cell.getNumericCellValue() + "";
            respuesta = new BigDecimal(txt);
        }
        return respuesta;
    }
    
    public List<TopePresupuestal> getCountTopePresupuestalByMatch(Integer idComp, Integer idSub, Integer idSede){
        try {
            return presupuestalDAO.getCountTopePresupuestalByMatch(idComp, idSub, idSede);
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    public void eliminarTopesPorRelGesPres(Integer id) {
        try {
            List<Integer> listaIds = generalDAO.getEntityManager().createQuery("select mov.topePresupuestal.id from  SgPresupuestoEscolarMovimiento mov where mov.topePresupuestal.relGesPres.id= :id and mov.presupuestoEscolar.estado = :estado").
                        setParameter("id", id).setParameter("estado", EstadoPresupuestoEscolar.EN_PROCESO).getResultList();
            if(listaIds != null && !listaIds.isEmpty() && listaIds.size() > 0) {
                Integer techosActualizadosPresupuestoEliminacion = generalDAO.getEntityManager().createQuery("update TopePresupuestal tope set tope.presupuestoEscolarMovimiento=null where tope.id in :listaIds").
                            setParameter("listaIds", listaIds).executeUpdate();
                logger.info("Techos actualizados para eliminar: " + techosActualizadosPresupuestoEliminacion);
                
                Integer eliminadosMovimientos = generalDAO.getEntityManager().createQuery("delete from  SgPresupuestoEscolarMovimiento mov where mov.topePresupuestal.id in :listaIds").
                        setParameter("listaIds", listaIds).executeUpdate();
            
                logger.info("Movimientos eliminados: " + eliminadosMovimientos);
                
                Integer eliminadosDetalleMatriculas = generalDAO.getEntityManager().createQuery("delete from TopeDetalleMatriculas det where det.topePresupuestal.id in :listaIds").
                            setParameter("listaIds", listaIds).executeUpdate();
                logger.info("Detalle de matriculas eliminadas: " + eliminadosDetalleMatriculas);
                
                Integer eliminadosPresupuesto = generalDAO.getEntityManager().createQuery("delete from TopePresupuestal tope where tope.id in :listaIds").
                            setParameter("listaIds", listaIds).executeUpdate();
                logger.info("Techos eliminados: " + eliminadosPresupuesto);
                
                generalDAO.getEntityManager().flush();
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error al aliminar topes", ex);
            BusinessException ge = new BusinessException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ConstantesErrores.ERR_GNRAL_DELETE_TOPES);
            throw ge;
        }
    }
    public void actualizarTopesPorRelGesPres(Integer id) {
        try {
            List<Integer> listaIds = generalDAO.getEntityManager().createQuery("select mov.topePresupuestal.id from  SgPresupuestoEscolarMovimiento mov where mov.topePresupuestal.relGesPres.id= :id and mov.presupuestoEscolar.estado != :estado").
                        setParameter("id", id).setParameter("estado", EstadoPresupuestoEscolar.APROBADO).getResultList();
            
            if(listaIds != null && !listaIds.isEmpty() && listaIds.size() > 0) {
                Integer eliminadosMovimientos = generalDAO.getEntityManager().createQuery("update SgPresupuestoEscolarMovimiento mov set mov.montoAprobado=0 where mov.topePresupuestal.id in :listaIds").
                        setParameter("listaIds", listaIds).executeUpdate();
            
                logger.info("Movimientos actualizados: " + eliminadosMovimientos);
                Integer eliminadosPresupuesto = generalDAO.getEntityManager().createQuery("update TopePresupuestal tope set tope.montoAprobado=0,tope.cantidadMatriculaAprobada=0  where tope.id in :listaIds").
                            setParameter("listaIds", listaIds).executeUpdate();
                logger.info("Techos actualizados: " + eliminadosPresupuesto);
                generalDAO.getEntityManager().flush();
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error al actualizar topes", ex);
            BusinessException ge = new BusinessException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE_TOPES);
            throw ge;
        }
    }
    
    public RelacionGesPresEsAnioFiscal actualizarRelGesPres(Integer id) {
        try {
            if(id != null) {
                 RelacionGesPresEsAnioFiscal relPresupuestoAnio = generalDAO.getEntityManager().find(RelacionGesPresEsAnioFiscal.class, id);
            
                if(relPresupuestoAnio != null) {

                    List<RelacionPresAnioFinanciamiento> rels = relPresupuestoAnio.getRelFinanciamiento();
                    //List<RelacionPresAnioFinanciamiento> relsActualizada = new ArrayList();
                    
                     List<TopePresupuestalGroup> topesPorFuentes = getTopePresupuestalFuentesAgrupado(relPresupuestoAnio.getId());
                    //for(listaSegunFuentesRecursos)
                    BigDecimal totalMontoFormulado = BigDecimal.ZERO;
                    BigDecimal totalMontoAprobado = BigDecimal.ZERO;
                    for(RelacionPresAnioFinanciamiento rfin : rels) {
                        rfin.setMontoTotalFormulado(BigDecimal.ZERO);
                        rfin.setMontoTotalAprobado(BigDecimal.ZERO);
                            
                        for(TopePresupuestalGroup g: topesPorFuentes) {
                            if(g.getIdFuente().equals(rfin.getFuenteFinanciamiento().getId()) && g.getIdFuenteRecursos().equals(rfin.getFuenteRecursos().getId())) {
                                rfin.setMontoTotalFormulado(g.getMontoFormulacion());
                                rfin.setMontoTotalAprobado(g.getMontoAprobado());

                                totalMontoFormulado = totalMontoFormulado.add(g.getMontoFormulacion());
                                totalMontoAprobado = totalMontoAprobado.add(g.getMontoAprobado());
                            }
                        }
                    }
                    relPresupuestoAnio.setRelFinanciamiento(rels);
                    relPresupuestoAnio.setMontoTotalFormulado(totalMontoFormulado);
                    relPresupuestoAnio.setMontoTotalAprobado(totalMontoAprobado);
                    
                    TotalesMatriculas totales = presupuestalDAO.getCantidadMatriculasPorPresupuesto(relPresupuestoAnio.getId());
                    if(totales != null) {
                        relPresupuestoAnio.setCantidadMatriculas(totales.getMatriculasFormuladas());
                        relPresupuestoAnio.setCantidadMatriculasAprobadas(totales.getMatriculasAprobadas());
                    } else {
                        relPresupuestoAnio.setCantidadMatriculas(0);
                        relPresupuestoAnio.setCantidadMatriculasAprobadas(0); 
                    }       
                    relPresupuestoAnio = (RelacionGesPresEsAnioFiscal) generalDAO.update(relPresupuestoAnio);
                }
                return relPresupuestoAnio;
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error al actualizar topes", ex);
            BusinessException ge = new BusinessException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE_TOPES);
            throw ge;
        }
        return null;
    }
    /**
     * Método utilizado para obtener los registros de Presupuesto con sus fuentes de financiamiento asociadas
     * 
     * @param idSubcomponente
     * @param idAnioFiscal
     * @param idLineaPresupuestaria
     * @return 
     */
    public List<RelacionPresAnioFinanciamiento> getRelacionesAnioFiscalConFinanciamiento(Integer idPresupuesto, Integer idFuenteFinanciamiento, Integer idFuenteRecurso){
        return generalDAO.getEntityManager().createQuery("Select rf from RelacionPresAnioFinanciamiento rf "
                + " WHERE rf.relAnioPresupuesto.id = :idPresupuesto "
                + " and rf.fuenteFinanciamiento.id = :idFuenteFinanciamiento "
                + " and rf.fuenteRecursos.id = :idFuenteRecurso")
                .setParameter("idPresupuesto",idPresupuesto)
                .setParameter("idFuenteFinanciamiento",idFuenteFinanciamiento)
                .setParameter("idFuenteRecurso",idFuenteRecurso)
                .getResultList();
    }
    
    
    public RelacionPresAnioFinanciamiento obtenerRelacionPresAnioFinanciamiento(List<RelacionPresAnioFinanciamiento> relaciones, Integer idPresupuesto, Integer idFuenteFinanciamiento, Integer idFuenteRecurso) {
        RelacionPresAnioFinanciamiento relFin = null;
        for(RelacionPresAnioFinanciamiento rel : relaciones) {
            if(rel.getRelAnioPresupuesto().getId().equals(idPresupuesto) && rel.getFuenteFinanciamiento().getId().equals(idFuenteFinanciamiento) && rel.getFuenteRecursos().getId().equals(idFuenteRecurso)) {
                return rel;
            }
        }
        return relFin;
    }
}
