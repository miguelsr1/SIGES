/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.daos;

import com.sofis.persistence.dao.exceptions.DAOGeneralException;
import com.sofis.persistence.dao.imp.hibernate.HibernateJpaDAOImp;
import com.sofis.sofisform.to.CriteriaTO;
import com.sofis.sofisform.to.MatchCriteriaTO;
import com.sofis.utils.CriteriaTOUtils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.jwt.JsonWebToken;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumSeccionesCargoBienes;
import sv.gob.mined.siges.filtros.FiltroBienesDepreciables;
import sv.gob.mined.siges.persistencia.entidades.SgAfBienDepreciable;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;
import sv.gob.mined.siges.utils.SofisStringUtils;

public class BienesDepreciablesDAO extends HibernateJpaDAOImp<SgAfBienDepreciable, Long> implements Serializable {
    
    private SeguridadDAO segDAO;
    private JsonWebToken jwt;
    
    public BienesDepreciablesDAO(EntityManager em) throws Exception {
        super(em);
        this.segDAO = new SeguridadDAO(em);
        this.jwt = Lookup.obtenerJWT();
    }

    private List<CriteriaTO> generarCriteria(FiltroBienesDepreciables filtro, BigDecimal valorActivoFijoLimite) {

        List<CriteriaTO> criterios = new ArrayList();
        if(filtro.getTipoUnidad() != null) {
            switch (filtro.getTipoUnidad()) {
                case UNIDAD_ADMINISTRATIVA:
                    if(filtro.getUnidadActivoFijoId()!= null) {
                        MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                                MatchCriteriaTO.types.EQUALS, "bdeUnidadesAdministrativas.uadUnidadActivoFijoFk.uafPk", filtro.getUnidadActivoFijoId());
                        criterios.add(criterio);
                    }
                    if(filtro.getUnidadAdministrativaId()!= null) {
                        MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                                MatchCriteriaTO.types.EQUALS, "bdeUnidadesAdministrativas.uadPk", filtro.getUnidadAdministrativaId());
                        criterios.add(criterio);
                    } else {
                       MatchCriteriaTO criterioNotNullAD = CriteriaTOUtils.createMatchCriteriaTO(
                        MatchCriteriaTO.types.NOT_NULL, "bdeUnidadesAdministrativas.uadPk", 1);
                        criterios.add(criterioNotNullAD); 
                    }
                    
                    if(filtro.getDui() != null && StringUtils.isNotBlank(filtro.getDui())) {
                        MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "bdeEmpleadoFk.empPersonaFk.perDui", filtro.getDui());
                        criterios.add(criterio);
                    }
                    if(filtro.getNombresEmpleado() != null && StringUtils.isNotBlank(filtro.getNombresEmpleado())) {
                        MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "bdeEmpleadoFk.empPersonaFk.perNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getNombresEmpleado()));
                        criterios.add(criterio);
                    }
                    if (filtro.getEmpleadoId() != null) {
                        MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                                MatchCriteriaTO.types.EQUALS, "bdeEmpleadoFk.empPk", filtro.getEmpleadoId());
                        criterios.add(criterio);
                    }
                    break;
                case CENTRO_ESCOLAR:
                    if(filtro.getDepartamentoId() != null) {
                        MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                                MatchCriteriaTO.types.EQUALS, "bdeSede.sedDireccion.dirDepartamento.depPk", filtro.getDepartamentoId());
                        criterios.add(criterio);
                    }
                    if(filtro.getMunicipioId()!= null) {
                        MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                                MatchCriteriaTO.types.EQUALS, "bdeSede.sedDireccion.dirMunicipio.munPk", filtro.getMunicipioId());
                        criterios.add(criterio);
                    }
                    if(filtro.getUnidadAdministrativaId()!= null) {
                        MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                                MatchCriteriaTO.types.EQUALS, "bdeSede.sedPk", filtro.getUnidadAdministrativaId());
                        criterios.add(criterio);
                    } else {
                        MatchCriteriaTO criterioNotNullCE = CriteriaTOUtils.createMatchCriteriaTO(
                            MatchCriteriaTO.types.NOT_NULL, "bdeSede.sedPk", 1);
                        criterios.add(criterioNotNullCE);

                    }
                    if (!StringUtils.isBlank(filtro.getAsignadoA())) {
                       MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                               MatchCriteriaTO.types.CONTAINS_ILIKE, "bdeAsignadoA", SofisStringUtils.normalizarParaBusqueda(filtro.getAsignadoA().trim()));
                       criterios.add(criterio);
                   }
                    break;
                default:
                    break;
            }
        } else {
            if(filtro.getUnidadActivoFijoId()!= null) {
                List<CriteriaTO> unidadesPorAF = new ArrayList();
                MatchCriteriaTO criterioUnidades = CriteriaTOUtils.createMatchCriteriaTO(
                        MatchCriteriaTO.types.EQUALS, "bdeUnidadesAdministrativas.uadUnidadActivoFijoFk.uafPk", filtro.getUnidadActivoFijoId());
                unidadesPorAF.add(criterioUnidades);
                
                if(filtro.getDepartamentoId() != null) {
                    MatchCriteriaTO criterioSedes = CriteriaTOUtils.createMatchCriteriaTO(
                            MatchCriteriaTO.types.EQUALS, "bdeSede.sedDireccion.dirDepartamento.depPk", filtro.getDepartamentoId());
                    unidadesPorAF.add(criterioSedes);
                }
                
                CriteriaTO[] arrCriterioOR = unidadesPorAF.toArray(new CriteriaTO[unidadesPorAF.size()]);
                CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
                criterios.add(criterioOR);
            }
            
            if (!StringUtils.isBlank(filtro.getAsignadoA())) {
                List<CriteriaTO> asignadoACriteria = new ArrayList();

               MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                       MatchCriteriaTO.types.CONTAINS_ILIKE, "bdeAsignadoA", SofisStringUtils.normalizarParaBusqueda(filtro.getAsignadoA().trim()));
               asignadoACriteria.add(criterio);


               MatchCriteriaTO criterioNombres = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "bdeEmpleadoFk.empPersonaFk.perNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getAsignadoA()));
               asignadoACriteria.add(criterioNombres);

               //Si se requiere búsqueda por asignadoA y de nombres de empleado, entonces se hace un OR
               CriteriaTO[] arrCriterioOR = asignadoACriteria.toArray(new CriteriaTO[asignadoACriteria.size()]);
               CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
               criterios.add(criterioOR);
           }

           if (filtro.getEmpleadoId() != null) {
               MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                       MatchCriteriaTO.types.EQUALS, "bdeEmpleadoFk.empPk", filtro.getEmpleadoId());
               criterios.add(criterio);
           }
        }
        
        if(filtro.getCompletado() != null) {
            if(!filtro.getCompletado()) {
                List<CriteriaTO> completados = new ArrayList();
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "bdeCompletado", filtro.getCompletado());
                completados.add(criterio);

                MatchCriteriaTO criterioOtraCategoria = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NULL, "bdeCompletado", 1);
                completados.add(criterioOtraCategoria);
                
                CriteriaTO[] arrCriterioOR = completados.toArray(new CriteriaTO[completados.size()]);
                CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
                criterios.add(criterioOR);
            } else {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "bdeCompletado", filtro.getCompletado());
                criterios.add(criterio);
            }
        }
        
        if(filtro.getEsBienSiap() != null) {
            if(!filtro.getEsBienSiap()) {
                List<CriteriaTO> completados = new ArrayList();
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "bdeBienEsFuenteSiap", filtro.getEsBienSiap());
                completados.add(criterio);

                MatchCriteriaTO criterioOtraCategoria = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NULL, "bdeBienEsFuenteSiap", 1);
                completados.add(criterioOtraCategoria);
                
                CriteriaTO[] arrCriterioOR = completados.toArray(new CriteriaTO[completados.size()]);
                CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
                criterios.add(criterioOR);
            } else {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "bdeBienEsFuenteSiap", filtro.getEsBienSiap());
                criterios.add(criterio);
            }
        }
        
        
        
        if(filtro.getCategoriaId() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "bdeCategoriaFk.cabPk", filtro.getCategoriaId());
            criterios.add(criterio);
        }
        
        if(filtro.getTipoBienId() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "bdeTipoBien.tbiPk", filtro.getTipoBienId());
            criterios.add(criterio);
        }
       if(filtro.getSoloVehiculos() != null && filtro.getSoloVehiculos()) { 
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "bdeCategoriaFk.sgAfSeccionesCategoriaList.scaSeccion", EnumSeccionesCargoBienes.SECCION_VEHICULOS);
            criterios.add(criterio);
        }
        
        
        if(filtro.getIdInmueble() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
            MatchCriteriaTO.types.EQUALS, "bdeInmuebleId", filtro.getIdInmueble());
            criterios.add(criterio);
        }
        
        if(filtro.getCalidadId() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "bdeEstadoCalidad.ecaPk", filtro.getCalidadId());
            criterios.add(criterio);
        }
        
        if(filtro.getEstadoId() != null) {
            List<CriteriaTO> estadosCriteria = new ArrayList();
            
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "bdeEstadosBienes.ebiPk", filtro.getEstadoId());
            estadosCriteria.add(criterio);
            
            
            if(filtro.getValidarEstadoSolicitudVacio() != null && filtro.getValidarEstadoSolicitudVacio()) {
                if(filtro.getTrasladado() != null && filtro.getTrasladado() && StringUtils.isNotBlank(filtro.getEstadoCodigo())) {
                    List<CriteriaTO> estadosCriteriaOr = new ArrayList();
                    List<CriteriaTO> estadosCriteriAnd = new ArrayList();
                    estadosCriteriAnd.add(criterio);
                    
                    MatchCriteriaTO criterioSolicitudVacio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NULL, "bdeEstadosSolicitud", 1);
                    estadosCriteriaOr.add(criterioSolicitudVacio);

                    MatchCriteriaTO criterioCodigoEstadoSolicitud = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "bdeEstadosSolicitud.ebiCodigo", filtro.getEstadoCodigo().trim());
                    estadosCriteriaOr.add(criterioCodigoEstadoSolicitud);
                    
                    CriteriaTO[] arrCriterioOR = estadosCriteriaOr.toArray(new CriteriaTO[estadosCriteriaOr.size()]);
                    CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
                    estadosCriteriAnd.add(criterioOR);
                    
                    CriteriaTO[] arrCriterioAND = estadosCriteriAnd.toArray(new CriteriaTO[estadosCriteriAnd.size()]);
                    CriteriaTO criterioAND = arrCriterioAND.length > 1 ? CriteriaTOUtils.createANDTO(arrCriterioAND) : arrCriterioAND[0];
                    criterios.add(criterioAND);
                } else {
                    MatchCriteriaTO criterioSolicitudVacio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NULL, "bdeEstadosSolicitud", 1);
                    estadosCriteria.add(criterioSolicitudVacio);
                    //Si se requiere busqueda por estado solicitud vacio, entonces se hace un AND
                    CriteriaTO[] arrCriterioAND = estadosCriteria.toArray(new CriteriaTO[estadosCriteria.size()]);
                    CriteriaTO criterioAND = arrCriterioAND.length > 1 ? CriteriaTOUtils.createANDTO(arrCriterioAND) : arrCriterioAND[0];
                    criterios.add(criterioAND);
                }  
            } else if(filtro.getValidarEstadoSolicitud() != null && filtro.getValidarEstadoSolicitud()) {
                MatchCriteriaTO criterioSolicitud = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "bdeEstadosSolicitud.ebiPk", filtro.getEstadoId());
                estadosCriteria.add(criterioSolicitud);
                //Si se requiere búsqueda por estado de solicitud, entonces se hace un OR
                CriteriaTO[] arrCriterioOR = estadosCriteria.toArray(new CriteriaTO[estadosCriteria.size()]);
                CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
                criterios.add(criterioOR);
            } else {
                criterios.add(criterio);  
            }
        } else {
            if(filtro.getValidarEstadoSolicitudVacio() != null && filtro.getValidarEstadoSolicitudVacio()) {
                List<CriteriaTO> estadosCriteriaOr = new ArrayList();
                
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NULL, "bdeEstadosSolicitud", 1);
                estadosCriteriaOr.add(criterio);
                
                if(filtro.getEstadoCodigo() != null) {
                    MatchCriteriaTO criterioCodigoEstadoSolicitud = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NOT_EQUALS, "bdeEstadosSolicitud.ebiCodigo", filtro.getEstadoCodigo().trim());
                    estadosCriteriaOr.add(criterioCodigoEstadoSolicitud);
                }
                CriteriaTO[] arrCriterioOR = estadosCriteriaOr.toArray(new CriteriaTO[estadosCriteriaOr.size()]);
                CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
                criterios.add(criterioOR);
            }
        }
        
        if(filtro.getFormaAdquisicionId()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "bdeFormaAdquisicion.fadPk", filtro.getFormaAdquisicionId());
            criterios.add(criterio);
        }
        
        
        if(filtro.getFuenteId()!= null) {
           MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "bdeFuenteFinanciamiento.ffiPk", filtro.getFuenteId());
            criterios.add(criterio);
        }
        
        if(filtro.getProyectoId()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "bdeProyectos.proPk", filtro.getProyectoId());
            criterios.add(criterio);
        }
        
        if (!StringUtils.isBlank(filtro.getCodigoInventario())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.CONTAINS_ILIKE, "bdeCodigoInventario", SofisStringUtils.normalizarParaBusqueda(filtro.getCodigoInventario().trim()));
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getMarca())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.CONTAINS_ILIKE, "bdeMarca",  SofisStringUtils.normalizarParaBusqueda(filtro.getMarca().trim()));
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getModelo())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.CONTAINS_ILIKE, "bdeModelo", SofisStringUtils.normalizarParaBusqueda(filtro.getModelo().trim()));
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getNoSerie())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.CONTAINS_ILIKE, "bdeNoSerie", SofisStringUtils.normalizarParaBusqueda(filtro.getNoSerie().trim()));
            criterios.add(criterio);
        }
        
        if (filtro.getValorAdquisicionDesde() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.GREATEREQUAL, "bdeValorAdquisicion", filtro.getValorAdquisicionDesde());
            criterios.add(criterio);
        }
        
        if (filtro.getValorAdquisicionHasta() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.LESSEQUAL, "bdeValorAdquisicion", filtro.getValorAdquisicionHasta());
            criterios.add(criterio);
        }
        
        if(StringUtils.isNotBlank(filtro.getMotor())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.CONTAINS_ILIKE, "bdeNoMotor", filtro.getMotor().trim());
            criterios.add(criterio);
        }
        
        if(StringUtils.isNotBlank(filtro.getPlaca())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.CONTAINS_ILIKE, "bdeNoPlaca", filtro.getPlaca().trim());
            criterios.add(criterio);
        }
        
        if(StringUtils.isNotBlank(filtro.getChasis())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.CONTAINS_ILIKE, "bdeNoChasis", filtro.getChasis().trim());
            criterios.add(criterio);
        }
        
        if (filtro.getActivos() != null) {
            if (filtro.getActivos()) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.GREATEREQUAL, "bdeValorAdquisicion", valorActivoFijoLimite);
                criterios.add(criterio);
            } else {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.LESSEQUAL, "bdeValorAdquisicion", valorActivoFijoLimite);
                criterios.add(criterio);
            }  
        }
        
        if(filtro.getDepreciadoCompleto() != null) {
             MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "bdeDepreciadoCompleto", filtro.getDepreciadoCompleto());
                criterios.add(criterio);
        }
        
        if (filtro.getFechaAdquisicionDesde()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATEREQUAL, "bdeFechaAdquisicion", filtro.getFechaAdquisicionDesde());
            criterios.add(criterio);
        }
        if (filtro.getFechaAdquisicionHasta() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.LESSEQUAL, "bdeFechaAdquisicion", filtro.getFechaAdquisicionHasta());
            criterios.add(criterio);
        }
        
        if(filtro.getAnio() != null) {
            if(filtro.getMes() == null) {
                if(filtro.getMayorIgualAnio() != null && filtro.getMayorIgualAnio()) {//Mayor o igual al año 
                    MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATEREQUAL, "bdeFechaAdquisicion", LocalDate.of(filtro.getAnio().intValue(), Constantes.MES_ENERO.intValue(), Constantes.MES_PRIMER_DIA.intValue()));
                    criterios.add(criterio);
                } else if(filtro.getMenorIgualAnio()!= null && filtro.getMenorIgualAnio()) {//Menor o igual al año
                    MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.LESSEQUAL, "bdeFechaAdquisicion", LocalDate.of(filtro.getAnio().intValue(), Constantes.MES_DICIEMBRE.intValue(), Constantes.MES_DICIEMBRE_ULTIMO_DIA.intValue()));
                    criterios.add(criterio);
                } else { // Igual año
                    MatchCriteriaTO criterioMayorIgual = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATEREQUAL, "bdeFechaAdquisicion", LocalDate.of(filtro.getAnio().intValue(), Constantes.MES_ENERO.intValue(), Constantes.MES_PRIMER_DIA.intValue()));
                    criterios.add(criterioMayorIgual);

                    MatchCriteriaTO criterioMenorIgual = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.LESSEQUAL, "bdeFechaAdquisicion", LocalDate.of(filtro.getAnio().intValue(), Constantes.MES_DICIEMBRE.intValue(), Constantes.MES_DICIEMBRE_ULTIMO_DIA.intValue()));
                    criterios.add(criterioMenorIgual);
                }
            } else {
                YearMonth anioMes = YearMonth.of(filtro.getAnio().intValue(), filtro.getMes().intValue());
                
                if(filtro.getMayorIgualAnio() != null && filtro.getMayorIgualAnio() && filtro.getMayorIgualMes() != null && filtro.getMayorIgualMes()) {//Mayor igual año y mes
                    MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATEREQUAL, "bdeFechaAdquisicion", LocalDate.of(filtro.getAnio().intValue(), filtro.getMes().intValue(), Constantes.MES_PRIMER_DIA.intValue()));
                    criterios.add(criterio);                                                                                                  
                } else if(filtro.getMenorIgualAnio()!= null && filtro.getMenorIgualAnio()) { //Menor o igual a año y mes
                    MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.LESSEQUAL, "bdeFechaAdquisicion", LocalDate.of(filtro.getAnio().intValue(), filtro.getMes().intValue(), anioMes.lengthOfMonth()));
                    criterios.add(criterio);
                } else {//Igual a año y mes
                    MatchCriteriaTO criterioMayorIgual = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATEREQUAL, "bdeFechaAdquisicion",  LocalDate.of(filtro.getAnio().intValue(), filtro.getMes().intValue(), Constantes.MES_PRIMER_DIA.intValue()));
                    criterios.add(criterioMayorIgual);

                    MatchCriteriaTO criterioMenorIgual = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.LESSEQUAL, "bdeFechaAdquisicion", LocalDate.of(filtro.getAnio().intValue(), filtro.getMes().intValue(), anioMes.lengthOfMonth()));
                    criterios.add(criterioMenorIgual);
                }
            }
            
            
        }
        
        if (filtro.getFechaCreacionDesde()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATEREQUAL, "bdeFechaCreacion", LocalDateTime.of(filtro.getFechaCreacionDesde(), LocalTime.MIN));
            criterios.add(criterio);
        }
        
        if (filtro.getFechaCreacionHasta() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.LESSEQUAL, "bdeFechaCreacion", LocalDateTime.of(filtro.getFechaCreacionHasta(), LocalTime.MAX));
            criterios.add(criterio);
        }

         if (filtro.getFechaModificacionDesde()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATEREQUAL, "bdeUltModFecha", LocalDateTime.of(filtro.getFechaModificacionDesde(), LocalTime.MIN));
            criterios.add(criterio);
        }
        
        if (filtro.getFechaModificacionHasta() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.LESSEQUAL, "bdeUltModFecha", LocalDateTime.of(filtro.getFechaModificacionHasta(), LocalTime.MAX));
            criterios.add(criterio);
        }
        
        if(StringUtils.isNotBlank(filtro.getUsuarioModificacion())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS_ILIKE, "bdeUltModUsuario", filtro.getUsuarioModificacion());
            criterios.add(criterio);
        }
        
        if(filtro.getEstadoProcesoLote() != null && !filtro.getEstadoProcesoLote().isEmpty()) {
            for(Integer estado : filtro.getEstadoProcesoLote()) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "bdeEstadoProcesoLote", estado);
                criterios.add(criterio);
            } 
        }
        

        return criterios;
    }

    public List<SgAfBienDepreciable> obtenerPorFiltro(FiltroBienesDepreciables filtro, BigDecimal valorActivoFijoLimite, String securityOperation) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = this.generarCriteria(filtro, valorActivoFijoLimite);

            String[] orderBy = filtro.getOrderBy();
            boolean[] asc = filtro.getAscending();

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.findEntityByCriteria(SgAfBienDepreciable.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, securityOperation != null ? segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()) : null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroBienesDepreciables filtro, BigDecimal valorActivoFijoLimite, String securityOperation) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro, valorActivoFijoLimite);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgAfBienDepreciable.class, criterio, false, securityOperation != null ? segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()) : null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
