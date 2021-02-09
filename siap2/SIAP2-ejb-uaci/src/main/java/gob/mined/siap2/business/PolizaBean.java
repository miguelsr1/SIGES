/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business;

import gob.mined.siap2.business.ejbs.SecuenciaBean;
import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.FacturaPolizaConcentracion;
import gob.mined.siap2.entities.data.impl.POInsumos;
import gob.mined.siap2.entities.data.impl.POMontoFuenteInsumoFlujoCajaMensual;
import gob.mined.siap2.entities.data.impl.PolizaDeConcentracion;
import gob.mined.siap2.entities.data.impl.Secuencia;
import gob.mined.siap2.entities.enums.EstadoPoliza;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.persistence.dao.imp.PolizaDAO;
import gob.mined.siap2.utils.generalutils.NumberUtils;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * Esta clase implementa los métodos correspondientes a la gestión de pólizas.
 *
 * @author Sofis Solutions
 */
@Stateless(name = "PolizaBean")
@LocalBean
public class PolizaBean {

    @Inject
    @JPADAO
    private GeneralDAO generalDAO;
    @Inject
    @JPADAO
    private PolizaDAO polizaDAO;
    @Inject
    private SecuenciaBean secuenciaBean;

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    /**
     * Este método obtiene una póliza por su id
     *
     * @param id
     * @return
     * @throws GeneralException
     */
    public PolizaDeConcentracion getPoliza(Integer id) throws GeneralException {
        try {
            PolizaDeConcentracion poliza = (PolizaDeConcentracion) generalDAO.find(PolizaDeConcentracion.class, id);
            poliza.getFuente().getInsumo().getMontosFuentes().isEmpty();
            poliza.getImpuestos().isEmpty();
            poliza.getFacturas().isEmpty();
            return poliza;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método guarda una póliza
     *
     * @param poliza
     * @return
     * @throws GeneralException
     */
    public PolizaDeConcentracion guardarPoliza(PolizaDeConcentracion poliza, Integer idPOMontoFuenteFCM) throws GeneralException {
        try {
            //TODO Mutuo excluir este código
            if (idPOMontoFuenteFCM == null) {
                BusinessException be = new BusinessException();
                be.addError(ConstantesErrores.ERR_DEBE_SELECCIONAR_UN_PAGO_PARA_LA_POLIZA);
                throw be;
            }

            if (poliza != null && poliza.getFuente() != null) {
                POInsumos insumo = (POInsumos) generalDAO.find(POInsumos.class, poliza.getFuente().getInsumo().getId());
                if (insumo != null) {

                    POMontoFuenteInsumoFlujoCajaMensual montoFuenteFCM = (POMontoFuenteInsumoFlujoCajaMensual) generalDAO.find(POMontoFuenteInsumoFlujoCajaMensual.class, idPOMontoFuenteFCM);

                    if (poliza.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
                        BusinessException be = new BusinessException();
                        be.addError(ConstantesErrores.ERR_MONTO_POLIZA_INCORRECTO);
                        throw be;
                    }

                    //Se determina si el monto de la póliza (en cualquier estado) supera el monto del pago seleccionado (monto certificado de la fuente en determinado mes)
                    if (montoFuenteFCM.getMontoCertificado() == null || poliza.getMonto().compareTo(montoFuenteFCM.getMontoCertificado()) > 0) {
                        BusinessException be = new BusinessException();
                        be.addError(ConstantesErrores.ERR_MONTO_POLIZA_SUPERA_MONTO_DE_PAGO_SELECCIONADO);
                        throw be;
                    }
                    //Deben existir facturas agregadas
                    if (poliza.getFacturas() == null || poliza.getFacturas().isEmpty()) {
                        BusinessException be = new BusinessException();
                        be.addError(ConstantesErrores.ERR_DEBE_AGREGAR_FACTURAS_POR_MONTO_POLIZA);
                        throw be;
                    }

                    // Monto de la póliza debe ser igual a la suma de montos de las facturas
                    BigDecimal montoTotalFacturas = BigDecimal.ZERO;
                    for (FacturaPolizaConcentracion factura : poliza.getFacturas()) {
                        montoTotalFacturas = montoTotalFacturas.add(factura.getImporte());
                    }
                    if (poliza.getMonto().compareTo(montoTotalFacturas) != 0) {
                        BusinessException be = new BusinessException();
                        String[] args = {NumberUtils.nomberToString(montoTotalFacturas)};
                        be.addError(ConstantesErrores.ERR_MONTO_FACTURAS_0_DEBE_COINCIDIR_CON_MONTO_POLIZA, args);
                        throw be;
                    }

                    Collection<PolizaDeConcentracion> listaPolizasAplicadasParaPagoSeleccionado = polizaDAO.obtenerPolizasAplicadasPorPagoFuenteFCM(montoFuenteFCM.getId());
                    BigDecimal sumaPolizasAplicadas = BigDecimal.ZERO;
                    /*
                     * Obtengo el monto total de todas las polizas aprobadas
                     */
                    //Si la póliza está siendo editada, sumo todo lo de la base excepto la póliza actual
                    if (poliza.getId() != null) {
                        for (PolizaDeConcentracion pol : listaPolizasAplicadasParaPagoSeleccionado) {
                            if (!poliza.getId().equals(pol.getId())) {
                                sumaPolizasAplicadas = sumaPolizasAplicadas.add(pol.getMonto());
                            }
                        }
                    } else {
                        //Si la ṕoliza no se ha guardado sumo todo lo de la base
                        for (PolizaDeConcentracion pol : listaPolizasAplicadasParaPagoSeleccionado) {
                            sumaPolizasAplicadas = sumaPolizasAplicadas.add(pol.getMonto());
                        }
                    }

                    if (poliza.getEstado().equals(EstadoPoliza.APLICADA)) {
                        sumaPolizasAplicadas = sumaPolizasAplicadas.add(poliza.getMonto());
                    }

                    /*
                     * Se determina si al agregar esta poliza (aplicada), la suma de los montos de todas las pólizas 
                     * que asocian el mismo pago, supera el monto certificado de dicho pago
                     */
                    if (sumaPolizasAplicadas.compareTo(montoFuenteFCM.getMontoCertificado()) > 0) {
                        BusinessException be = new BusinessException();
                        be.addError(ConstantesErrores.ERR_SUMA_MONTOS_POLIZAS_APLICADAS_SUPERA_MONTO_DE_PAGO_ASOCIADO);
                        throw be;
                    }

                    //SI PASA TODAS LAS VALIDACIONES
                    if (poliza.getNumeroPoliza() == null) {
                        Secuencia secuenciaNumCert = secuenciaBean.obtenerSecPorCodigo(ConstantesEstandares.Secuencias.SEC_NUM_POLIZA_CONCENTRACION);
                        Integer numeroPoliza = secuenciaNumCert.getNumero() + 1;
                        poliza.setNumeroPoliza(numeroPoliza);
                        secuenciaNumCert.setNumero(numeroPoliza);
                        generalDAO.update(secuenciaNumCert);
                    }
                    poliza.setPagoFuenteFCM(montoFuenteFCM);

                    
                    //Actualizo el monto usado en pólizas por parte del insumo que asocia
                    BigDecimal montoAplicadoPolizaEnInsumo = BigDecimal.ZERO;
                    if (insumo.getMontoEnPolizasAplicadas() == null) {
                        insumo.setMontoEnPolizasAplicadas(BigDecimal.ZERO);
                    }
                    //Si la poliza se está registrando y guardando aplicada la primera vez
                    if (poliza.getId() == null) {
                        if (poliza.getEstado().equals(EstadoPoliza.APLICADA)) {
                            montoAplicadoPolizaEnInsumo = insumo.getMontoEnPolizasAplicadas().add(poliza.getMonto());
                            insumo.setMontoEnPolizasAplicadas(montoAplicadoPolizaEnInsumo); 
                        }
                    } else {
                        PolizaDeConcentracion polizaBD = (PolizaDeConcentracion)generalDAO.find(PolizaDeConcentracion.class, poliza.getId());
                        
                        //Si no estaba aplicada y la aplico, sumo el valor al insumo
                        if(!polizaBD.getEstado().equals(EstadoPoliza.APLICADA) && poliza.getEstado().equals(EstadoPoliza.APLICADA)){
                            montoAplicadoPolizaEnInsumo = insumo.getMontoEnPolizasAplicadas().add(poliza.getMonto());
                            insumo.setMontoEnPolizasAplicadas(montoAplicadoPolizaEnInsumo);
                        }
                        //Si estaba aplicada y vuelve a estar aplicada, sumo la diferencia entre el monto anterior y el actual
                        if(polizaBD.getEstado().equals(EstadoPoliza.APLICADA) && poliza.getEstado().equals(EstadoPoliza.APLICADA)){
                            BigDecimal montoDiferencia = poliza.getMonto().subtract(polizaBD.getMonto());
                            montoAplicadoPolizaEnInsumo = insumo.getMontoEnPolizasAplicadas().add(montoDiferencia);
                            insumo.setMontoEnPolizasAplicadas(montoAplicadoPolizaEnInsumo);
                        }
                        //Si estaba aplicada y la revierto, resto lo que estaba en la base
                        if(polizaBD.getEstado().equals(EstadoPoliza.APLICADA) && poliza.getEstado().equals(EstadoPoliza.REVERTIDA)){
                            montoAplicadoPolizaEnInsumo = insumo.getMontoEnPolizasAplicadas().subtract(polizaBD.getMonto());  
                            insumo.setMontoEnPolizasAplicadas(montoAplicadoPolizaEnInsumo);
                        }
                    }                    
                    
                    poliza = (PolizaDeConcentracion) generalDAO.update(poliza);
                    montoFuenteFCM.getPolizasConcentracion().add(poliza);

                }
            }

            return poliza;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    public FacturaPolizaConcentracion guardarFactura(FacturaPolizaConcentracion factura) {
        try {
            return (FacturaPolizaConcentracion) generalDAO.update(factura);
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

}
