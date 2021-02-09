/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.mined.siap2.business;

import gob.mined.siap2.business.ejbs.IdiomaBean;
import gob.mined.siap2.business.ejbs.TextoBean;
import gob.mined.siap2.business.ejbs.impl.RangoMatriculaBean;
import gob.mined.siap2.business.ejbs.impl.SedeBean;
import gob.mined.siap2.business.ejbs.impl.SgBeneficiariosBean;
import gob.mined.siap2.business.ejbs.impl.TopePresupuestalBean;
import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.data.SsUsuario;
import gob.mined.siap2.entities.data.impl.Beneficiarios;
import gob.mined.siap2.entities.data.impl.Notificacion;
import gob.mined.siap2.entities.data.impl.RangoMatricula;
import gob.mined.siap2.entities.data.impl.RelacionGesPresEsAnioFiscal;
import gob.mined.siap2.entities.data.impl.TopeDetalleMatriculas;
import gob.mined.siap2.entities.data.impl.TopePresupuestal;
import gob.mined.siap2.entities.data.impl.TopePresupuestalGroup;
import gob.mined.siap2.entities.data.impl.TopePresupuestalMatriculasDetalleGroup;
import gob.mined.siap2.entities.enums.AccionTechos;
import gob.mined.siap2.entities.enums.EstadoPresupuestoEscolar;
import gob.mined.siap2.entities.enums.EstadoTopePresupuestal;
import gob.mined.siap2.entities.enums.TipoMonto;
import gob.mined.siap2.entities.enums.TipoNotificacion;
import gob.mined.siap2.entities.enums.TipoParametro;
import gob.mined.siap2.entities.enums.TipoSede;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.filtros.FiltroSede;
import gob.mined.siap2.filtros.FiltroTopePresupuestal;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.persistence.dao.exceptions.DAOObjetoNoEditableException;
import gob.mined.siap2.persistence.dao.imp.PresupuestoEscolarDAO;
import gob.mined.siges.entities.data.impl.SgClasificacion;
import gob.mined.siges.entities.data.impl.SgPresupuestoEscolar;
import gob.mined.siges.entities.data.impl.SgSede;
import gob.mined.siges.entities.data.impl.SgTipoOrganismoAdministrativo;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Asynchronous;
import javax.ejb.LocalBean;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import org.apache.commons.lang.BooleanUtils;
import org.jboss.ejb3.annotation.TransactionTimeout;

@Stateless(name = "procesamientoTechosAsincBean")
@Remote(ProcesamientoTechosAsincInterface.class)
@Asynchronous
@LocalBean
public class ProcesamientoTechosAsincBean implements ProcesamientoTechosAsincInterface{
    private static final Logger logger = Logger.getLogger(ProcesamientoTechosAsincBean.class.getName());

    @Inject
    @JPADAO
    private GeneralDAO generalDAO;

    @Inject
    private TopePresupuestalBean topeBean;

    @Inject
    private SedeBean sedeBean;

    @Inject
    private SgBeneficiariosBean beneficiariosBean;
    
    @Inject
    private RangoMatriculaBean rangoMatriculaBean;
    
    @Inject
    private IdiomaBean idiBean;
    
    @Inject
    private TextoBean textoBean;
    
    @Inject
    @JPADAO
    private PresupuestoEscolarDAO presupuestoEscolarDAO;
    
    @Asynchronous
    @TransactionTimeout(unit=TimeUnit.MINUTES, value=90)
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @Override
    public void modificarTechos(Integer relId, AccionTechos accion, List<SsUsuario> usuarios) {
        String contenidoNotificacion = "Errores en la ejecución de proceso de generación de techos por sistema."; 
        String presupuesto = "";
        //SsUsuario usuario = null;
        Integer idiomaId = 1;
        Boolean existoso = false;
        RelacionGesPresEsAnioFiscal rel = null;

        TipoMonto tipoMonto = null;
        String contenidoNotificacionAdvertencias = "Aunque las sedes siguientes no crearon o actualizaron sus techos presupuestales por las razones detalladas a continuación.";
        List<String> errores = new ArrayList();
        String advertencia = "";
        try {
            if(accion != null) {
                if(accion.equals(AccionTechos.GENERAR)) {
                    tipoMonto = TipoMonto.FORMULADO;
                } else if(accion.equals(AccionTechos.ACTUALIZAR)) {
                    tipoMonto = TipoMonto.APROBADO;
                }
            }
            if(tipoMonto == null) {
                return ;
            }
            Thread.sleep(5000);//5 segundos de espera
            idiomaId = idiBean.obtenerIdiomaEspaniol().getIdiId();
            
            rel = generalDAO.getEntityManager().find(RelacionGesPresEsAnioFiscal.class, relId);
            
            BusinessException be = new BusinessException();
            if (rel.getFechaMatricula() == null) {
                be.addError(ConstantesErrores.ERR_FECHA_MATRICULA_VACIA);
            }
            if (rel.getComponentePresupuestoEscolar() == null) {
                be.addError(ConstantesErrores.ERR_COMPONENTE_VACIO);
            }
            if (rel.getSubCuenta() == null) {
                be.addError(ConstantesErrores.ERR_LINEA_PRESUPUESTARIA_VACIO);
            }
            if (rel.getAnioFiscal() == null) {
                be.addError(ConstantesErrores.ERR_ANIO_VACIO);
            }
   
            List<Beneficiarios> benef = beneficiariosBean.getBeneficiariosPorPresEsAnioFiscal(rel.getId());
            if (benef == null || benef.isEmpty()){
                be.addError(ConstantesErrores.ERR_BENEFICIARIOS_VACIO);
            }
            if (!be.getErrores().isEmpty()){
                throw be;
            }
            if(usuarios == null) {
                usuarios = new ArrayList();
            }
            if(rel != null) {
                Boolean agregarusuarioComponente = Boolean.TRUE;
                if(rel.getComponentePresupuestoEscolar() != null && rel.getComponentePresupuestoEscolar().getUsuario() != null) {
                    SsUsuario usuarioSunComp= rel.getComponentePresupuestoEscolar() != null ? rel.getComponentePresupuestoEscolar().getUsuario() : null;
                    if(usuarioSunComp != null) {
                        for(SsUsuario usu : usuarios) {
                            if(usu.getUsuId().equals(usuarioSunComp.getUsuId())) {
                                agregarusuarioComponente = Boolean.FALSE;
                                break;
                            }
                        }
                        if(agregarusuarioComponente) {
                            usuarios.add(rel.getComponentePresupuestoEscolar().getUsuario());
                        }
                    }
                }
                presupuesto = rel.getDescripcion().trim();
            }
            
            //presupuesto = rel.getDescripcion().trim();
            
            rel.setMontoTotalAprobado(new BigDecimal(BigInteger.ZERO));
            rel.setCantidadMatriculasAprobadas(0);
            if (tipoMonto.equals(TipoMonto.APROBADO)) {
                topeBean.actualizarTopesPorRelGesPres(relId);
            } else {
                topeBean.eliminarTopesPorRelGesPres(relId);
                rel.setMontoTotalFormulado(new BigDecimal(BigInteger.ZERO));
                rel.setCantidadMatriculas(0);
            }
            
            FiltroSede fil = new FiltroSede();

            List<Long> clasificacionesId = new ArrayList<>();
            for (SgClasificacion c : rel.getClasificaciones()) {
                clasificacionesId.add(c.getClaPk());
            }

            List<TipoSede> tiposSede = new ArrayList<>();
            if (BooleanUtils.isTrue(rel.getComponentePresupuestoEscolar().getCeduOficiales())) {
                tiposSede.add(TipoSede.CED_OFI);
            }
            if (BooleanUtils.isTrue(rel.getComponentePresupuestoEscolar().getCeduPrivadosSub())
                    || BooleanUtils.isTrue(rel.getComponentePresupuestoEscolar().getCeduPrivadosNoSub())) {
                tiposSede.add(TipoSede.CED_PRI);
            }
            if (BooleanUtils.isTrue(rel.getComponentePresupuestoEscolar().getCirculosAlfabetizacion())) {
                tiposSede.add(TipoSede.CIR_ALF);
            }
            if (BooleanUtils.isTrue(rel.getComponentePresupuestoEscolar().getCirculosFamilia())) {
                tiposSede.add(TipoSede.CIR_INF);
            }
            if (BooleanUtils.isTrue(rel.getComponentePresupuestoEscolar().getSedesEducame())) {
                tiposSede.add(TipoSede.UBI_EDUC);
            }

            
                
            //TODO: ver como filtrar por subvencionada
            fil.setIncluirCampos(new String[]{"id", "version", "tipo","codigo"});
            fil.setClasificacionesId(clasificacionesId);
            fil.setOaeYMiembrosVigente(Boolean.TRUE);
            fil.setTipos(tiposSede);
            List<String> tiposOrganismos = new ArrayList();
            if(rel.getComponentePresupuestoEscolar() != null && rel.getComponentePresupuestoEscolar().getTiposOrganismoCurricular() != null) {
                for(SgTipoOrganismoAdministrativo tipo:rel.getComponentePresupuestoEscolar().getTiposOrganismoCurricular()) {
                    tiposOrganismos.add(tipo.getToaCodigo());
                }
            }
            fil.setTiposOrganismos(tiposOrganismos);
            
            List<SgSede> sedes = sedeBean.getSedesByFiltro(fil);

            if (sedes.isEmpty()) {
                return ;
            }

            List<Integer> sedesId = new ArrayList<>();
            for (SgSede sed : sedes) {
                sedesId.add(sed.getId());
            }

            //Consultas topes existentes
            FiltroTopePresupuestal filtro = new FiltroTopePresupuestal();
            filtro.setAnioFiscalId(rel.getAnioFiscal().getId());
            filtro.setComponentePresupuestoEscolarId(rel.getComponentePresupuestoEscolar().getId());
            filtro.setCuentaId(rel.getSubCuenta().getId());
            filtro.setSedesId(sedesId);
            List<TopePresupuestal> existentes = topeBean.getTopesPresupuestalesByFiltro(filtro);
            HashMap<Integer, TopePresupuestal> existentesPorSede = new HashMap<>();
            for (TopePresupuestal t : existentes) {
                existentesPorSede.put(t.getSede().getId(), t);
            }

            //Verificar montos a deducir
            HashMap<Integer, TopePresupuestal> existentesDeducirPorSede = new HashMap<>();
            if (rel.getDeducirComponentePresupuestoEscolar() != null) {
                filtro = new FiltroTopePresupuestal();
                filtro.setIncluirCampos(new String[]{"id", "monto", "montoAprobado", "sede.id","sede.codigo"});
                filtro.setAnioFiscalId(rel.getAnioFiscal().getId());
                filtro.setComponentePresupuestoEscolarId(rel.getDeducirComponentePresupuestoEscolar().getId());
                filtro.setCuentaId(rel.getSubCuenta().getId());
                filtro.setSedesId(sedesId);
                List<TopePresupuestal> existentesDeducir = topeBean.getTopesPresupuestalesByFiltro(filtro);
                for (TopePresupuestal t : existentesDeducir) {
                    existentesDeducirPorSede.put(t.getSede().getId(), t);
                }
            }
            
            //Obtener cantidad matriculas en todas las sedes del sistema
            HashMap<Integer, Long> cantidadMatriculasPorSede = new HashMap<>(); //Sedpk, cantidad
            List<Object[]> cantidades = sedeBean.getCantidadMatriculasPorSede(
                    rel.getFechaMatricula(),
                    sedesId,
                    benef,
                    rel.getComponentePresupuestoEscolar().getSedesAdscritas());
            for (Object[] ob : cantidades) {
                cantidadMatriculasPorSede.put(((Long) ob[0]).intValue(), (Long) ob[1]);
            }

            BigDecimal montoTotalAnio = BigDecimal.ZERO;

            Integer matriculasTotales = 0;
            for (SgSede sede : sedes) {

                BigDecimal monto =  tipoMonto.equals(TipoMonto.APROBADO) ? rel.getMontoPorMatriculaAprobacion() : rel.getMontoPorMatricula();
                BigDecimal montoMinimo = tipoMonto.equals(TipoMonto.APROBADO) ? rel.getMontoMinimoAprobacion() : rel.getMontoMinimo();

                Long cantMatriculas = cantidadMatriculasPorSede.get(sede.getId());
                if (cantMatriculas == null) {
                    cantMatriculas = 0L;
                }
                
                if(cantMatriculas.compareTo(0L) > 0) {
                    //ASIGNACION DE MONTO POR "RANGO DE MATRICULA"
                    if (rel.getComponentePresupuestoEscolar().getParametro() != null && rel.getComponentePresupuestoEscolar().getParametro().equals(TipoParametro.RANGO_MATRICULA)) {
                        List<RangoMatricula> listaRangos = rangoMatriculaBean.getRangoMatriculaByRelacion(rel.getId());
                        if (listaRangos == null || listaRangos.isEmpty()) {
                            be.addError(ConstantesErrores.ERR_RANGOS_NO_ENCONTRADOS);
                            throw be;
                        }
                        BigDecimal rangoMayor;
                        BigDecimal rangoMenor = BigDecimal.ZERO;
                        for (RangoMatricula rango : listaRangos) {
                            rangoMayor = rango.getRango();
                            if (BigDecimal.valueOf(cantMatriculas).compareTo(rangoMenor) >= 0 && BigDecimal.valueOf(cantMatriculas).compareTo(rangoMayor) <= 0) {
                                monto = monto.multiply(new BigDecimal(rango.getValor()));
                                break;
                            }
                            rangoMenor = rango.getRango();
                        }
                    } else {
                        monto = monto.multiply(BigDecimal.valueOf(cantMatriculas));
                    }
                    
                    TopePresupuestal topeDeducir = existentesDeducirPorSede.get(sede.getId());
                    if (topeDeducir != null) {
                        BigDecimal montoDeducir = tipoMonto.equals(TipoMonto.APROBADO) ? topeDeducir.getMontoAprobado() : topeDeducir.getMonto();
                        if (montoDeducir != null) {
                            monto = monto.subtract(montoDeducir);
                        }
                    }

                    if (montoMinimo != null && monto.compareTo(montoMinimo) < 0) {
                        monto = montoMinimo;
                    }

                    

                    
                    TopePresupuestal tope = null;
                    
                    Boolean guardar = Boolean.FALSE;
                
                    SgPresupuestoEscolar pe = presupuestoEscolarDAO.getPresupuestoEscolarByAnioYSede(rel.getAnioFiscal().getAnio(), sede.getId());
                    if(pe == null) {
                         guardar = Boolean.TRUE; //para guardar los datos cuando no existe presupuesto(FORMULADO Y APROBADO)
                    }
                    if (!existentesPorSede.containsKey(sede.getId())) {
                        tope = new TopePresupuestal();
                        tope.setAnioFiscal(rel.getAnioFiscal());
                        tope.setGesPresEs(rel.getComponentePresupuestoEscolar());
                        tope.setSede(sede);
                        tope.setSubCuenta(rel.getSubCuenta());
                        tope.setRelGesPres(rel);
                        if(pe != null) {
                            if(tipoMonto.equals(TipoMonto.FORMULADO)){
                                if(pe.getEstado() != null && 
                                    (pe.getEstado().equals(EstadoPresupuestoEscolar.EN_PROCESO) 
                                    || pe.getEstado().equals(EstadoPresupuestoEscolar.FORMULADO))) {//SE GUARDA SOLO EN CASO QUE EL ESTADO ESTE EN PROCESO O FORMULADO
                                    guardar = Boolean.TRUE;
                                    tope.setEstado(EstadoTopePresupuestal.EN_PROCESO);
                                    tope.setCantidadMatricula(cantMatriculas.intValue());
                                    tope.setMonto(monto);
                                }
                            } else if(tipoMonto.equals(TipoMonto.APROBADO)){
                                if(pe.getEstado() != null && (pe.getEstado().equals(EstadoPresupuestoEscolar.EN_PROCESO)
                                    || pe.getEstado().equals(EstadoPresupuestoEscolar.FORMULADO)
                                    || pe.getEstado().equals(EstadoPresupuestoEscolar.EN_AJUSTE)
                                    || pe.getEstado().equals(EstadoPresupuestoEscolar.APROBADO)
                                  //  || pe.getEstado().equals(EstadoPresupuestoEscolar.EDITADO)
                                    || pe.getEstado().equals(EstadoPresupuestoEscolar.AJUSTADO))) {//SE GUARDAR EN CASO QUE EL ESTADO EN_PROCESO,FORMULADO, EN_AJUSTE, APROBADO,EDITADO Y AJUSTADO
                                    guardar = Boolean.TRUE;
                                    tope.setEstado(EstadoTopePresupuestal.APROBACION);
                                    tope.setMontoAprobado(monto);
                                    tope.setCantidadMatriculaAprobada(cantMatriculas.intValue());
                                    //tope.setCantidadMatricula(0);
                                }
                             }
                        } else {
                            if(tipoMonto.equals(TipoMonto.FORMULADO)){
                                tope.setEstado(EstadoTopePresupuestal.EN_PROCESO);
                                tope.setCantidadMatricula(cantMatriculas.intValue());
                                tope.setMonto(monto);
                            } else if(tipoMonto.equals(TipoMonto.APROBADO)){
                                 tope.setEstado(EstadoTopePresupuestal.APROBACION);
                                 tope.setMontoAprobado(monto);
                                 tope.setCantidadMatriculaAprobada(cantMatriculas.intValue());
                            }
                        }
                    } else {
                        tope = existentesPorSede.get(sede.getId());
                        if(pe != null) {
                            if(tipoMonto.equals(TipoMonto.FORMULADO)){
                                if(pe != null && pe.getEstado() != null && 
                                    (pe.getEstado().equals(EstadoPresupuestoEscolar.EN_PROCESO) 
                                    || pe.getEstado().equals(EstadoPresupuestoEscolar.FORMULADO))) {//SE GUARDA SOLO EN CASO QUE EL ESTADO ESTE EN PROCESO O FORMULADO
                                    guardar = Boolean.TRUE;
                                    tope.setMonto(monto);
                                    tope.setEstado(EstadoTopePresupuestal.EN_PROCESO);
                                    tope.setCantidadMatricula(cantMatriculas.intValue());
                                }
                            } else if(tipoMonto.equals(TipoMonto.APROBADO)){
                                if(pe != null && pe.getEstado() != null 
                                        && (pe.getEstado().equals(EstadoPresupuestoEscolar.EN_PROCESO)
                                    || pe.getEstado().equals(EstadoPresupuestoEscolar.FORMULADO)
                                    || pe.getEstado().equals(EstadoPresupuestoEscolar.EN_AJUSTE)
                                   // || pe.getEstado().equals(EstadoPresupuestoEscolar.APROBADO)
                                   // || pe.getEstado().equals(EstadoPresupuestoEscolar.EDITADO)
                                    || pe.getEstado().equals(EstadoPresupuestoEscolar.AJUSTADO))) {//SE GUARDAR EN CASO QUE EL ESTADO EN_PROCESO,FORMULADO, EN_AJUSTE,EDITADO Y AJUSTADO
                                    tope.setMontoAprobado(monto);
                                    tope.setEstado(EstadoTopePresupuestal.APROBACION);
                                    tope.setCantidadMatriculaAprobada(cantMatriculas.intValue());
                                    guardar = Boolean.TRUE;
                                } else  if(pe != null && pe.getEstado() != null && pe.getEstado().equals(EstadoPresupuestoEscolar.APROBADO)) {//SE GUARDAR EN CASO EL ESTADO ESTE APROBADO Y EL MONTO HAYA CAMBIADO
                                    if(tope.getMontoAprobado() != null && (tope.getMontoAprobado().compareTo(monto) != 0)) {
                                        tope.setMontoAprobado(monto);
                                        tope.setEstado(EstadoTopePresupuestal.APROBACION);
                                        tope.setCantidadMatriculaAprobada(cantMatriculas.intValue());
                                        guardar = Boolean.TRUE;
                                    }
                                }
                            }
                        } else {
                            if(tipoMonto.equals(TipoMonto.FORMULADO)){
                                tope.setMonto(monto);
                                tope.setEstado(EstadoTopePresupuestal.EN_PROCESO);
                                tope.setCantidadMatricula(cantMatriculas.intValue());
                            } else if(tipoMonto.equals(TipoMonto.APROBADO)){
                                tope.setMontoAprobado(monto);
                                tope.setEstado(EstadoTopePresupuestal.APROBACION);
                                tope.setCantidadMatriculaAprobada(cantMatriculas.intValue());
                            }
                        }
                    }
                    if(guardar) {
                        montoTotalAnio = montoTotalAnio.add(monto);
                        matriculasTotales += cantMatriculas.intValue();
                        
                        List<TopeDetalleMatriculas> detalleMatriculas = new ArrayList();
                        TopeDetalleMatriculas dm = null;
                        List<TopePresupuestalMatriculasDetalleGroup> lista = sedeBean.getDetalleCantidadMatriculasPorSede(sede.getId(),rel.getFechaMatricula(),benef);
                        for(TopePresupuestalMatriculasDetalleGroup d : lista) {
                            dm = new TopeDetalleMatriculas();
                            dm.setNivel(d.getNivel());
                            dm.setModalidadEducativa(d.getModalidadEducativa());
                            dm.setModalidadAtencion(d.getModalidadAtencion());
                            dm.setCantidadMatriculas(d.getCantidadMatriculas());
                            detalleMatriculas.add(dm);
                        }
                        tope.setDetalleMatriculas(detalleMatriculas);
                        
                        for(TopeDetalleMatriculas d : tope.getDetalleMatriculas()) {
                            d.setTopePresupuestal(tope);
                        }

                        if(tope.getCantidadMatricula() == null) {
                            tope.setCantidadMatricula(0);
                        }
                        if(tope.getCantidadMatriculaAprobada() == null) {
                            tope.setCantidadMatriculaAprobada(0);
                        }
                        
                        tope.setRelGesPres(rel);
                        tope = topeBean.crearActualizarTopeMovimiento(tope,tipoMonto);
                    } else  {
                        if(pe != null && pe.getEstado() != null) {
                            advertencia = "Sede " + tope.getSede().getCodigo() + " para el año " + tope.getAnioFiscal().getAnio() + " tiene presupuesto en estado " + pe.getEstado().getText()+".";
                            errores.add(advertencia);
                        }

                    }
                    
                } else {
                    logger.info("SEDE SIN MATRICULAS: Id: " + sede.getId());
                }
            }
            
            /**
            if (accion.equals(AccionTechos.ACTUALIZAR)) {
                rel.setMontoTotalAprobado(montoTotalAnio);
                rel.setCantidadMatriculasAprobadas(matriculasTotales);
            } else {
                rel.setMontoTotalFormulado(montoTotalAnio);
                rel.setCantidadMatriculas(matriculasTotales);
            }**/
            //OBTENER AQUI LA SUMA DE LOS MONTOS POR SUBCUENTAS PARA ACTUALIZAR, ESTO DADO QUE HAY TECHOS QUE EXISTEN PERO NO SE TOMAN EN CUENTA PARA ACTUALIZAR EN ESTE PROCESO.
            List<TopePresupuestalGroup> topesPorFuentes = topeBean.getTopePresupuestalFuentesAgrupado(rel.getId());
            //for(listaSegunFuentesRecursos)
            BigDecimal totalMontoFormulado = BigDecimal.ZERO;
            BigDecimal totalMontoAprobado = BigDecimal.ZERO;
            
            for(TopePresupuestalGroup g: topesPorFuentes) {
                totalMontoFormulado = totalMontoFormulado.add(g.getMontoFormulacion());
                totalMontoAprobado = totalMontoAprobado.add(g.getMontoAprobado());
            }
            rel.setMontoTotalFormulado(totalMontoFormulado);
            rel.setMontoTotalAprobado(totalMontoAprobado);
            
            existoso = true;
        } catch (BusinessException be) {
            existoso = false;
            for(String error : be.getErrores()) {
                String mensaje = textoBean.obtenerMensajeByProperty(error, idiomaId);
                //String mensaje = error;
                contenidoNotificacion += "-" + mensaje;
            }
            logger.log(Level.SEVERE, null, be);
        } catch (DAOObjetoNoEditableException e) {
            existoso = false;
            String mensaje = textoBean.obtenerMensajeByProperty(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO, idiomaId);
            contenidoNotificacion += "-" + mensaje;
            
            logger.log(Level.SEVERE, null, e);
        } catch (Exception ex) {
            existoso = false;
            logger.log(Level.SEVERE, null, ex);
            String mensaje = textoBean.obtenerMensajeByProperty(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE, idiomaId);
            contenidoNotificacion += "-" + mensaje;

        } finally {
            try {
                rel.setProcesoEnCurso(Boolean.FALSE);
                generalDAO.update(rel);
                
                if(existoso) {
                    if(tipoMonto.equals(TipoMonto.FORMULADO)) {
                        contenidoNotificacion = "La generación de techos por sistema para FORMULACIÓN del presupuesto " + presupuesto + " se ejecutó correctamente.";
                    } else if(tipoMonto.equals(TipoMonto.APROBADO)) {
                        contenidoNotificacion = "La generación de techos por sistema para APROBACIÓN del presupuesto " + presupuesto + " se ejecutó correctamente.";
                    }
                   if(errores != null && !errores.isEmpty() && errores.size() > 0) {
                       contenidoNotificacion += contenidoNotificacionAdvertencias;
                       for(String err : errores) {
                           contenidoNotificacion += "-" + err;
                       }
                   }
                }
                
                if(usuarios != null && !usuarios.isEmpty() && usuarios.size() > 0) {
                    for(SsUsuario usu : usuarios) {
                        Notificacion n = new Notificacion();
                        n.setTipo(TipoNotificacion.EJECUCION_PROCESO_TECHOS_SISTEMA);
                        n.setUsuario(usu);
                        n.setFecha(new Date());
                        n.setContenido(contenidoNotificacion);
                        n.setObjetoId(rel.getComponentePresupuestoEscolar().getId());
                        generalDAO.update(n);
                    }
                }
                
            } catch (DAOGeneralException ex) {
                logger.log(Level.SEVERE, "Error al procesar archivo de tranferencias", ex);
            }
        }
      //  return new AsyncResult<Void>(null);
    }
}
