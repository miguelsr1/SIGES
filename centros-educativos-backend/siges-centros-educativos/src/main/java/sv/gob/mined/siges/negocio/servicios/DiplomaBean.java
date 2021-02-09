/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.BooleanUtils;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroDatoContratacion;
import sv.gob.mined.siges.filtros.FiltroDiploma;
import sv.gob.mined.siges.filtros.FiltroSelloFirma;
import sv.gob.mined.siges.filtros.FiltroSelloFirmaTitular;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.validaciones.DiplomaValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.DiplomaDAO;
import sv.gob.mined.siges.persistencia.entidades.SgDatoContratacion;
import sv.gob.mined.siges.persistencia.entidades.SgDiploma;
import sv.gob.mined.siges.persistencia.entidades.SgSelloFirma;
import sv.gob.mined.siges.persistencia.entidades.SgSelloFirmaTitular;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class DiplomaBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;
    
    @Inject
    private SeguridadBean segBean;
    
    @Inject
    private SelloFirmaBean selloFirmaBean;

    @Inject
    private DatoContratacionBean datoContratacionBean;
    
    @Inject
    private SelloFirmaTitularBean selloFirmaTitularBean;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgDiploma
     * @throws GeneralException
     */
    public SgDiploma obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgDiploma> codDAO = new CodigueraDAO<>(em, SgDiploma.class);
                SgDiploma dip=codDAO.obtenerPorId(id,ConstantesOperaciones.BUSCAR_DIPLOMAS);
                if(dip.getDiplomaEstudiantes()!=null){
                    dip.getDiplomaEstudiantes().size();
                }
                return dip;
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return null;
    }

    /**
     * Devuelve si el objeto existe
     *
     * @param id Long
     * @return Boolean
     * @throws GeneralException
     */
    public Boolean objetoExistePorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgDiploma> codDAO = new CodigueraDAO<>(em, SgDiploma.class);
                return codDAO.objetoExistePorId(id,null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }


    /**
     * Guarda el objeto indicado
     *
     * @param dil SgDiploma
     * @return SgDiploma
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgDiploma guardar(SgDiploma dil) throws GeneralException {
        try {
            if (dil != null) {
                if (DiplomaValidacionNegocio.validar(dil)) {
                    
                    if (dil.getDilPk()==null) {
                        FiltroSelloFirma fsf = new FiltroSelloFirma();
                        fsf.setSfiHabilitado(Boolean.TRUE);
                        fsf.setArchivoVacio(Boolean.FALSE);
                        if (dil.getDilSedeFk().getSedSedeAdscritaDe() != null) {
                            List<Long> sedeList = new ArrayList();
                            sedeList.add(dil.getDilSedeFk().getSedSedeAdscritaDe().getSedPk());
                            sedeList.add(dil.getDilSedeFk().getSedPk());
                            fsf.setSfiSedes(sedeList);
                        } else {
                            fsf.setSedPk(dil.getDilSedeFk().getSedPk());
                        }
                        fsf.setSfiTipoRepresentanteCodigo(Constantes.TIPO_REPRESENTANTE_DIRECTOR);
                        fsf.setIncluirCampos(new String[]{"sfiPk", "sfiPersona.perPk", "sfiSede.sedPk", "sfiSede.sedTipo", "sfiSede.sedVersion",
                            "sfiPersona.perVersion","sfiVersion"});
                        List<SgSelloFirma> listSelloFirma = selloFirmaBean.obtenerPorFiltro(fsf);
                        if (listSelloFirma.isEmpty()) {
                            BusinessException ge = new BusinessException();
                            ge.addError("simEstudianteImpresion", Errores.ERROR_SELLO_FIRMA_DIRECTOR_VACIO);
                            throw ge;
                        } else {
                            SgSelloFirma selloFirma = listSelloFirma.get(0);
                            if (dil.getDilSedeFk().getSedSedeAdscritaDe() != null) {
                                List<SgSelloFirma> listSedePadre = listSelloFirma.stream().filter(c -> c.getSfiSede().getSedPk().equals(dil.getDilSedeFk().getSedSedeAdscritaDe().getSedPk())).collect(Collectors.toList());
                                if (!listSedePadre.isEmpty()) {
                                    selloFirma = listSedePadre.get(0);
                                }
                            }

                            FiltroDatoContratacion filtro = new FiltroDatoContratacion();
                            filtro.setDcoCentroEducativo(selloFirma.getSfiSede().getSedPk());
                            filtro.setDcoPersonaPk(selloFirma.getSfiPersona().getPerPk());
                            filtro.setDcoFecha(LocalDate.now());
                            filtro.setIncluirCampos(new String[]{"dcoPk"});
                            filtro.setMaxResults(1L);
                            List<SgDatoContratacion> listdc = datoContratacionBean.obtenerPorFiltro(filtro);

                            if (listdc.isEmpty()) {
                                BusinessException ge = new BusinessException();
                                ge.addError("simEstudianteImpresion", Errores.ERROR_DIRECTOR_NO_TIENE_CONTRATO_VIGENTE);
                                throw ge;
                            }
                            else{
                                dil.setDilSelloDirector(selloFirma);
                            }
                        }
                        
                        FiltroSelloFirmaTitular fsft = new FiltroSelloFirmaTitular();
                        fsft.setSftHabilitado(Boolean.TRUE);
                        fsft.setSftCodigoCargo(Constantes.CARGO_MINISTRO_SELLO_FIRMA);
                        List<SgSelloFirmaTitular> listSelloFirmaTitular = selloFirmaTitularBean.obtenerPorFiltro(fsft);
                        if (listSelloFirmaTitular.size() > 0) {
                            dil.setDilSelloMinistro(listSelloFirmaTitular.get(0));
                        } else {
                            BusinessException ge = new BusinessException();
                            ge.addError(Errores.ERROR_SELLO_FIRMA_MINISTRO_VACIO);
                            throw ge;
                        }
                    }
                    
                    CodigueraDAO<SgDiploma> codDAO = new CodigueraDAO<>(em, SgDiploma.class);

                        return codDAO.guardar(dil,null);
                  
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return dil;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroDiploma filtro) throws GeneralException {
        try {
            DiplomaDAO codDAO = new DiplomaDAO(em,segBean);
            return codDAO.obtenerTotalPorFiltro(filtro,ConstantesOperaciones.BUSCAR_DIPLOMAS);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgDiploma>
     * @throws GeneralException
     */
    public List<SgDiploma> obtenerPorFiltro(FiltroDiploma filtro) throws GeneralException {
        try {
            DiplomaDAO codDAO = new DiplomaDAO(em,segBean);
            List<SgDiploma> dips=codDAO.obtenerPorFiltro(filtro,ConstantesOperaciones.BUSCAR_DIPLOMAS);
            
            if(BooleanUtils.isTrue(filtro.getIncluirDiplomadoEstudiante())){
                for(SgDiploma dip:dips){
                    if(dip.getDiplomaEstudiantes()!=null){
                        dip.getDiplomaEstudiantes().size();
                    }
                }
            }
            return dips;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Elimina el objeto con la id indicada
     *
     * @param id Long
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public void eliminar(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgDiploma> codDAO = new CodigueraDAO<>(em, SgDiploma.class);
                codDAO.eliminarPorId(id,null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
