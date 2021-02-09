/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.dto.EstGenerica;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroEstadisticas;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.daos.EstadisticasEstudiantesDAO;
import sv.gob.mined.siges.persistencia.entidades.SgEstIndicador;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgConfiguracion;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class EstadisticasEstudiantesBean {

    @PersistenceContext(name = Constantes.MAIN_DATASOURCE)
    private EntityManager em;

    @Inject
    private ConfiguracionBean configBean;

    
     /**
     * Método genérico para obtener estadísticas encargado de invocar métodos específicos según el indicador
     *
     * @param filtro FiltroEstadisticas
     * @return Lista de <EstGenerica>
     * @throws GeneralException
     */
    public List<EstGenerica> obtenerEstadisticas(FiltroEstadisticas filtro) {
        try {

            if (filtro.getIndicadorPk() == null){
                throw new IllegalStateException();
            }
            
            SgEstIndicador indicador = em.find(SgEstIndicador.class, filtro.getIndicadorPk());
            
            switch (indicador.getIndCodigo()) {
                    case Constantes.INDICADOR_MATRICULA_NIVEL_EDUCATIVO:
                        return this.matriculaPorNivelEducativo(filtro);
                    case Constantes.INDICADOR_POBLACION_NO_ESCOLARIZADA_POR_EDAD:
                        return this.poblacionNoEscolarizadaPorEdad(filtro);
                    case Constantes.INDICADOR_PORCENTAJE_POBLACION_NO_ESCOLARIZADA_POR_EDAD:
                        return this.porcentajePoblacionNoEscolarizadaPorEdad(filtro);
                    case Constantes.INDICADOR_PORCENTAJE_ESTUDIANTES_DESERTORES:
                        return this.porcentajeEstudiantesDesertores(filtro);
                    case Constantes.INDICADOR_TASA_BRUTA_INGRESO_PRIMER_GRADO_EDUCACION_BASICA:
                        return this.tasaBrutaIngresoPrimerGradoEducacionBasica(filtro);
                    case Constantes.INDICADOR_PORCENTAJE_DE_REPETIDORES:
                        return this.porcentajeDeRepetidores(filtro);
                    case Constantes.INDICADOR_TASA_REPETICION:
                        return this.tasaDeRepeticion(filtro);
                    case Constantes.INDICADOR_TASA_TRANSICION_NIVEL:
                        return this.tasaTransicionPorNivel(filtro);
                    case Constantes.INDICADOR_TASA_TRANSICION_CICLO:
                        return this.tasaTransicionPorCiclo(filtro);
                    case Constantes.INDICADOR_TASA_DESERCION:
                        return this.tasaDesercion(filtro);
                    case Constantes.INDICADOR_PORCENTAJE_DE_TRABAJADORES:
                        return this.porcentajeDeTrabajadores(filtro);
                    case Constantes.INDICADOR_PORCENTAJE_ESTUDIANTES_DISCAPACIDAD:
                        return this.porcentajeConDiscapacidad(filtro);
                    case Constantes.INDICADOR_DISTRIBUCION_PORCENTUAL_ESTUDIANTES_CON_DISCAPACIDAD:
                        return this.distribucionPorcentualEstudiantesConDiscapacidad(filtro);
                    case Constantes.INDICADOR_DISTRIBUCION_PORCENTUAL_ESTUDIANTES_SEGUN_CONVIVENCIA_FAMILIAR:
                        return this.distribucionPorcentualEstudiantesSegunConvivenciaFamiliar(filtro);
                    case Constantes.INDICADOR_TASA_NETA_INGRESO_PRIMER_GRADO_EDUCACION_BASICA:
                        return this.tasaNetaIngresoPrimerGradoEducacionBasica(filtro);
                    case Constantes.INDICADOR_TASA_ESPECIFICA_ESCOLARIZACION_POR_EDAD:
                        return this.tasaEspecificaDeEscolarizacionPorEdad(filtro);
                    case Constantes.INDICADOR_PORCENTAJE_ESTUDIANTES_PRIMER_GRADO_CON_EXPERIENCIA_EDU_PARVPARVULARIA:
                        return this.porcentajeDeEstudiantesDePrimerGradoConExperienciaEnEdeucacionParvularia(filtro);
                    case Constantes.INDICADOR_DISTRIBUCION_PORCENTUAL_MATRICULA_POR_NIVEL_EDUCATIVO:
                        return this.distribucionPorcentualDeLaMatriculaPorNivelEducativo(filtro);
                    case Constantes.INDICADOR_TASA_BRUTA_MATRICULA_NIVEL_EDUCATIVO:
                        return this.tasaBrutaDeMatriculaPorNivelEducativo(filtro);
                    case Constantes.INDICADOR_TASA_NETA_MATRICULA_NIVEL_EDUCATIVO:
                        return this.tasaNetaDeMatriculaPorNivelEducativo(filtro);
                    case Constantes.INDICADOR_TASA_ESPECIFICA_MATRICULA_GRADO:
                        return this.tasaEspecificaMatriculaPorGrado(filtro);
                    case Constantes.INDICADOR_DISTRIBUCION_PORCENTUAL_ESTUDIANTES_ACTIVIDAD_LABORAL:
                        return this.distribucionPorcentualDeEstudiantesSegunActividadLaboral(filtro);
                    case Constantes.INDICADOR_PORCENTAJE_ESTUDIANTES_APROBADOS:
                        return this.porcentajeEstudiantesAprobados(filtro);
                    case Constantes.INDICADOR_TASA_BRUTA_APROBACION:
                        return this.tasaBrutaAprobacion(filtro);
                    case Constantes.INDICADOR_TASA_PROMOCION:
                        return this.tasaPromocion(filtro);
                    case Constantes.INDICADOR_CENTROS_EDUCATIVOS_SEGUN_NIVEL:
                        return this.centrosEducativosSegunNivelEducativo(filtro);
                    case Constantes.INDICADOR_PORCENTAJE_ESTUDIANTES_ACCESO_INTERNET:
                        return this.porcentajeDeEstudiantesConAccesoAInternet(filtro);
                    case Constantes.INDICADOR_PORCENTAJE_ESTUDIANTES_ACCESO_COMPUTADORA:
                        return this.porcentajeDeEstudiantesConAccesoAComputadora(filtro);
                    case Constantes.INDICADOR_ESTUDIANTES_POR_SECCION:
                        return this.estudiantesPorSeccion(filtro);
                    case Constantes.INDICADOR_TASA_BRUTA_INGRESO_GRADO:
                        return this.tasaBrutaIngresoPorGrado(filtro);
                    case Constantes.INDICADOR_PORCENTAJE_CON_SOBRE_EDAD_GRADO:
                        return this.porcentajeConSobreedad(filtro);
                    case Constantes.INDICADOR_DISTRIBUCION_PORCENTUAL_DOCENTES_SEGUN_ANIOS_SERVICIO:
                        return this.distribucionPorcentualDocentesSegunAniosServicio(filtro);
                    case Constantes.INDICADOR_PROMEDIO_ESTUDIANTES_POR_DOCENTE:
                        return this.promedioEstudiantesPorDocente(filtro);
                    case Constantes.INDICADOR_PROMEDIO_DOCENTES_POR_GRADO_ACADEMICO_ALCANZADO:
                        return this.porcentajeDeDocentesPorGradoAcademicoAlcanzado(filtro);
                    case Constantes.INDICADOR_PORCENTAJE_DOCENTES_CON_ACCESO_INTERNET:
                        return this.porcentajeDeDocentesConAccesoAInternet(filtro);
                    case Constantes.INDICADOR_PORCENTAJE_CENTROS_EDUCATIVOS_ACCESO_SERVICIOS_BASICOS:
                        return this.porcentajeDeCentrosEducativosConAccesoServiciosBasicos(filtro);
                    case Constantes.INDICADOR_PORCENTAJE_DE_DOCENTES_CERTIFICADOS_POR_NIVEL_EDUCATIVO:
                        return this.porcentajeDeDocentesCertificadosPorNivelEducativo(filtro);
                    case Constantes.INDICADOR_DISTRIBUCION_DE_DOCENTES_SEGUN_NIVEL_EDUCATIVO:
                        return this.distribucionDeDocentesSegunNivelEducativo(filtro);
                    case Constantes.INDICADOR_PORCENTAJE_ESTUDIANTES_SEGUN_LOGRO_PAES:
                        return this.porcentajeDeEstudiantesSegunNivelDeLogroEnPAES(filtro);
                    case Constantes.INDICADOR_DISTRIBUCION_PORCENTUAL_ESTUDIANTES_SEGUN_CAUSA_RETIRO_CENTRO_EDUCATIVO:
                        return this.distribucionPorcentualEstudiantesSegunCausaRetiroCentroEducativo(filtro);
                    default:
                        throw new IllegalStateException();
                }
            
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio P-01
     *
     * @param filtro FiltroEstadisticas
     * @return Lista de <EstGenerica>
     * @throws GeneralException
     */
    public List<EstGenerica> poblacionNoEscolarizadaPorEdad(FiltroEstadisticas filtro) throws GeneralException {
        try {
            EstadisticasEstudiantesDAO codDAO = new EstadisticasEstudiantesDAO(em);
            if (codDAO.existeExtraccionEstudiantes(filtro.getAnio(), filtro.getNombrePk())) {

                SgConfiguracion c = configBean.obtenerPorCodigo(Constantes.CONFIG_EDAD_MAXIMA_POBLACION_ESTADISTICAS);
                if (c != null) {
                    filtro.setEdadMaximaPoblacionEstadisticas(Integer.parseInt(c.getConValor()));
                }

                return codDAO.poblacionNoEscolarizadaPorEdad(filtro);
            }
            return null;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio P-02
     *
     * @param filtro FiltroEstadisticas
     * @return Lista de <EstGenerica>
     * @throws GeneralException
     */
    public List<EstGenerica> porcentajePoblacionNoEscolarizadaPorEdad(FiltroEstadisticas filtro) throws GeneralException {
        try {
            EstadisticasEstudiantesDAO codDAO = new EstadisticasEstudiantesDAO(em);
            if (codDAO.existeExtraccionEstudiantes(filtro.getAnio(), filtro.getNombrePk())) {

                SgConfiguracion c = configBean.obtenerPorCodigo(Constantes.CONFIG_EDAD_MAXIMA_POBLACION_ESTADISTICAS);
                if (c != null) {
                    filtro.setEdadMaximaPoblacionEstadisticas(Integer.parseInt(c.getConValor()));
                }

                return codDAO.porcentajePoblacionNoEscolarizadaPorEdad(filtro);
            }
            return null;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio P-03
     *
     * @param filtro FiltroEstadisticas
     * @return Lista de <EstGenerica>
     * @throws GeneralException
     */
    public List<EstGenerica> porcentajeEstudiantesDesertores(FiltroEstadisticas filtro) throws GeneralException {
        try {
            EstadisticasEstudiantesDAO codDAO = new EstadisticasEstudiantesDAO(em);
            if (codDAO.existeExtraccionEstudiantes(filtro.getAnio(), filtro.getNombrePk())) {
                return codDAO.porcentajeEstudiantesDesertores(filtro);
            }
            return null;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio M-01
     *
     * @param filtro FiltroEstadisticas
     * @return Lista de <EstGenerica>
     * @throws GeneralException
     */
    public List<EstGenerica> matriculaPorNivelEducativo(FiltroEstadisticas filtro) throws GeneralException {
        try {
            EstadisticasEstudiantesDAO codDAO = new EstadisticasEstudiantesDAO(em);
            if (codDAO.existeExtraccionEstudiantes(filtro.getAnio(), filtro.getNombrePk())) {
                return codDAO.matriculaPorNivelEducativo(filtro);
            }
            return null;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio I-01
     *
     * @param filtro FiltroEstadisticas
     * @return Lista de <EstGenerica>
     * @throws GeneralException
     */
    public List<EstGenerica> tasaBrutaIngresoPrimerGradoEducacionBasica(FiltroEstadisticas filtro) throws GeneralException {
        try {
            EstadisticasEstudiantesDAO codDAO = new EstadisticasEstudiantesDAO(em);
            if (codDAO.existeExtraccionEstudiantes(filtro.getAnio(), filtro.getNombrePk())) {
                return codDAO.tasaBrutaIngresoPrimerGradoEducacionBasica(filtro);
            }
            return null;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio T-01
     *
     * @param filtro FiltroEstadisticas
     * @return Lista de <EstGenerica>
     * @throws GeneralException
     */
    public List<EstGenerica> porcentajeDeRepetidores(FiltroEstadisticas filtro) throws GeneralException {
        try {
            EstadisticasEstudiantesDAO codDAO = new EstadisticasEstudiantesDAO(em);
            if (codDAO.existeExtraccionEstudiantes(filtro.getAnio(), filtro.getNombrePk())) {
                return codDAO.porcentajeDeRepetidores(filtro);
            }
            return null;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio T-02
     *
     * @param filtro FiltroEstadisticas
     * @return Lista de <EstGenerica>
     * @throws GeneralException
     */
    public List<EstGenerica> tasaDeRepeticion(FiltroEstadisticas filtro) throws GeneralException {
        try {
            if (filtro.getAnioComparacion() >= filtro.getAnio()) {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_ANIO_COMPARACION_DEBE_SER_MENOR);
                throw be;
            }
            EstadisticasEstudiantesDAO codDAO = new EstadisticasEstudiantesDAO(em);
            if (codDAO.existeExtraccionEstudiantes(filtro.getAnio(), filtro.getNombrePk()) && codDAO.existeExtraccionEstudiantes(filtro.getAnioComparacion(), filtro.getNombrePkComparacion())) {
                return codDAO.tasaDeRepeticion(filtro);
            }
            return null;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio T-04
     *
     * @param filtro FiltroEstadisticas
     * @return Lista de <EstGenerica>
     * @throws GeneralException
     */
    public List<EstGenerica> tasaTransicionPorCiclo(FiltroEstadisticas filtro) throws GeneralException {
        try {

            if (filtro.getAnioComparacion() >= filtro.getAnio()) {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_ANIO_COMPARACION_DEBE_SER_MENOR);
                throw be;
            }

            EstadisticasEstudiantesDAO codDAO = new EstadisticasEstudiantesDAO(em);
            if (codDAO.existeExtraccionEstudiantes(filtro.getAnio(), filtro.getNombrePk()) && codDAO.existeExtraccionEstudiantes(filtro.getAnioComparacion(), filtro.getNombrePkComparacion())) {
                return codDAO.tasaTransicionPorCiclo(filtro);
            }
            return null;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio T-05
     *
     * @param filtro FiltroEstadisticas
     * @return Lista de <EstGenerica>
     * @throws GeneralException
     */
    public List<EstGenerica> tasaTransicionPorNivel(FiltroEstadisticas filtro) throws GeneralException {
        try {

            if (filtro.getAnioComparacion() >= filtro.getAnio()) {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_ANIO_COMPARACION_DEBE_SER_MENOR);
                throw be;
            }

            EstadisticasEstudiantesDAO codDAO = new EstadisticasEstudiantesDAO(em);
            if (codDAO.existeExtraccionEstudiantes(filtro.getAnio(), filtro.getNombrePk()) && codDAO.existeExtraccionEstudiantes(filtro.getAnioComparacion(), filtro.getNombrePkComparacion())) {
                return codDAO.tasaTransicionPorNivel(filtro);
            }
            return null;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio T-07
     *
     * @param filtro FiltroEstadisticas
     * @return Lista de <EstGenerica>
     * @throws GeneralException
     */
    public List<EstGenerica> tasaDesercion(FiltroEstadisticas filtro) throws GeneralException {
        try {
            EstadisticasEstudiantesDAO codDAO = new EstadisticasEstudiantesDAO(em);
            if (codDAO.existeExtraccionEstudiantes(filtro.getAnio(), filtro.getNombrePk()) && codDAO.existeExtraccionEstudiantes(filtro.getAnioComparacion(), filtro.getNombrePkComparacion())) {
                return codDAO.tasaDesercion(filtro);
            }
            return null;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio CC-01
     *
     * @param filtro FiltroEstadisticas
     * @return Lista de <EstGenerica>
     * @throws GeneralException
     */
    public List<EstGenerica> porcentajeDeTrabajadores(FiltroEstadisticas filtro) throws GeneralException {
        try {
            EstadisticasEstudiantesDAO codDAO = new EstadisticasEstudiantesDAO(em);
            if (codDAO.existeExtraccionEstudiantes(filtro.getAnio(), filtro.getNombrePk())) {
                return codDAO.porcentajeDeTrabajadores(filtro);
            }
            return null;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio CC-02
     *
     * @param filtro FiltroEstadisticas
     * @return Lista de <EstGenerica>
     * @throws GeneralException
     */
    public List<EstGenerica> distribucionPorcentualDeEstudiantesSegunActividadLaboral(FiltroEstadisticas filtro) throws GeneralException {
        try {
            EstadisticasEstudiantesDAO codDAO = new EstadisticasEstudiantesDAO(em);
            if (codDAO.existeExtraccionEstudiantes(filtro.getAnio(), filtro.getNombrePk())) {
                return codDAO.distribucionPorcentualDeEstudiantesSegunActividadLaboral(filtro);
            }
            return null;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio CC-03
     *
     * @param filtro FiltroEstadisticas
     * @return Lista de <EstGenerica>
     * @throws GeneralException
     */
    public List<EstGenerica> porcentajeConDiscapacidad(FiltroEstadisticas filtro) throws GeneralException {
        try {
            EstadisticasEstudiantesDAO codDAO = new EstadisticasEstudiantesDAO(em);
            if (codDAO.existeExtraccionEstudiantes(filtro.getAnio(), filtro.getNombrePk())) {
                return codDAO.porcentajeConDiscapacidad(filtro);
            }
            return null;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio CC-04
     *
     * @param filtro FiltroEstadisticas
     * @return Lista de <EstGenerica>
     * @throws GeneralException
     */
    public List<EstGenerica> distribucionPorcentualEstudiantesConDiscapacidad(FiltroEstadisticas filtro) throws GeneralException {
        try {
            EstadisticasEstudiantesDAO codDAO = new EstadisticasEstudiantesDAO(em);
            if (codDAO.existeExtraccionEstudiantes(filtro.getAnio(), filtro.getNombrePk())) {
                return codDAO.distribucionPorcentualEstudiantesConDiscapacidad(filtro);
            }
            return null;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio CC-05
     *
     * @param filtro FiltroEstadisticas
     * @return Lista de <EstGenerica>
     * @throws GeneralException
     */
    public List<EstGenerica> distribucionPorcentualEstudiantesSegunConvivenciaFamiliar(FiltroEstadisticas filtro) throws GeneralException {
        try {
            EstadisticasEstudiantesDAO codDAO = new EstadisticasEstudiantesDAO(em);
            if (codDAO.existeExtraccionEstudiantes(filtro.getAnio(), filtro.getNombrePk())) {
                return codDAO.distribucionPorcentualEstudiantesSegunConvivenciaFamiliar(filtro);
            }
            return null;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio CC-06
     *
     * @param filtro FiltroEstadisticas
     * @return Lista de <EstGenerica>
     * @throws GeneralException
     */
    public List<EstGenerica> porcentajeConSobreedad(FiltroEstadisticas filtro) throws GeneralException {
        try {
            EstadisticasEstudiantesDAO codDAO = new EstadisticasEstudiantesDAO(em);
            if (codDAO.existeExtraccionEstudiantes(filtro.getAnio(), filtro.getNombrePk())) {
                return codDAO.porcentajeConSobreedad(filtro);
            }
            return null;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    /**
     * Devuelve los elementos que satisfacen el criterio CC-07
     *
     * @param filtro FiltroEstadisticas
     * @return Lista de <EstGenerica>
     * @throws GeneralException
     */
    public List<EstGenerica> distribucionPorcentualEstudiantesSegunCausaRetiroCentroEducativo(FiltroEstadisticas filtro) throws GeneralException {
        try {
            EstadisticasEstudiantesDAO codDAO = new EstadisticasEstudiantesDAO(em);
            if (codDAO.existeExtraccionEstudiantes(filtro.getAnio(), filtro.getNombrePk())) {
                return codDAO.distribucionPorcentualEstudiantesSegunCausaRetiroCentroEducativo(filtro);
            }
            return null;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    

    /**
     * Devuelve los elementos que satisfacen el criterio I-02
     *
     * @param filtro FiltroEstadisticas
     * @return Lista de <EstGenerica>
     * @throws GeneralException
     */
    public List<EstGenerica> tasaNetaIngresoPrimerGradoEducacionBasica(FiltroEstadisticas filtro) throws GeneralException {
        try {
            EstadisticasEstudiantesDAO codDAO = new EstadisticasEstudiantesDAO(em);
            if (codDAO.existeExtraccionEstudiantes(filtro.getAnio(), filtro.getNombrePk())) {
                return codDAO.tasaNetaIngresoPrimerGradoEducacionBasica(filtro);
            }
            return null;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio I-03
     *
     * @param filtro FiltroEstadisticas
     * @return Lista de <EstGenerica>
     * @throws GeneralException
     */
    public List<EstGenerica> tasaEspecificaDeEscolarizacionPorEdad(FiltroEstadisticas filtro) throws GeneralException {
        try {
            EstadisticasEstudiantesDAO codDAO = new EstadisticasEstudiantesDAO(em);
            if (codDAO.existeExtraccionEstudiantes(filtro.getAnio(), filtro.getNombrePk())) {

                SgConfiguracion c = configBean.obtenerPorCodigo(Constantes.CONFIG_EDAD_MAXIMA_POBLACION_ESTADISTICAS);
                if (c != null) {
                    filtro.setEdadMaximaPoblacionEstadisticas(Integer.parseInt(c.getConValor()));
                }

                return codDAO.tasaEspecificaDeEscolarizacionPorEdad(filtro);
            }
            return null;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio I-04
     *
     * @param filtro FiltroEstadisticas
     * @return Lista de <EstGenerica>
     * @throws GeneralException
     */
    public List<EstGenerica> porcentajeDeEstudiantesDePrimerGradoConExperienciaEnEdeucacionParvularia(FiltroEstadisticas filtro) throws GeneralException {
        try {
            EstadisticasEstudiantesDAO codDAO = new EstadisticasEstudiantesDAO(em);
            if (codDAO.existeExtraccionEstudiantes(filtro.getAnio(), filtro.getNombrePk())) {
                return codDAO.porcentajeDeEstudiantesDePrimerGradoConExperienciaEnEducacionParvularia(filtro);
            }
            return null;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio M-02
     *
     * @param filtro FiltroEstadisticas
     * @return Lista de <EstGenerica>
     * @throws GeneralException
     */
    public List<EstGenerica> distribucionPorcentualDeLaMatriculaPorNivelEducativo(FiltroEstadisticas filtro) throws GeneralException {
        try {
            EstadisticasEstudiantesDAO codDAO = new EstadisticasEstudiantesDAO(em);
            if (codDAO.existeExtraccionEstudiantes(filtro.getAnio(), filtro.getNombrePk())) {
                return codDAO.distribucionPorcentualDeLaMatriculaPorNivelEducativo(filtro);
            }
            return null;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio M-03
     *
     * @param filtro FiltroEstadisticas
     * @return Lista de <EstGenerica>
     * @throws GeneralException
     */
    public List<EstGenerica> tasaBrutaDeMatriculaPorNivelEducativo(FiltroEstadisticas filtro) throws GeneralException {
        try {
            EstadisticasEstudiantesDAO codDAO = new EstadisticasEstudiantesDAO(em);
            if (codDAO.existeExtraccionEstudiantes(filtro.getAnio(), filtro.getNombrePk())) {
                return codDAO.tasaBrutaDeMatriculaPorNivelEducativo(filtro);
            }
            return null;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio M-04
     *
     * @param filtro FiltroEstadisticas
     * @return Lista de <EstGenerica>
     * @throws GeneralException
     */
    public List<EstGenerica> tasaNetaDeMatriculaPorNivelEducativo(FiltroEstadisticas filtro) throws GeneralException {
        try {
            EstadisticasEstudiantesDAO codDAO = new EstadisticasEstudiantesDAO(em);
            if (codDAO.existeExtraccionEstudiantes(filtro.getAnio(), filtro.getNombrePk())) {
                return codDAO.tasaNetaDeMatriculaPorNivelEducativo(filtro);
            }
            return null;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio. No se utiliza
     *
     * @param filtro FiltroEstadisticas
     * @return Lista de <EstGenerica>
     * @throws GeneralException
     */
    public List<EstGenerica> tasaBrutaDeIngresoAlSextoGrado(FiltroEstadisticas filtro) throws GeneralException {
        try {
            EstadisticasEstudiantesDAO codDAO = new EstadisticasEstudiantesDAO(em);
            if (codDAO.existeExtraccionEstudiantes(filtro.getAnio(), filtro.getNombrePk())) {
                return codDAO.tasaBrutaDeIngresoAlSextoGrado(filtro);
            }
            return null;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio. No se utiliza
     *
     * @param filtro FiltroEstadisticas
     * @return Lista de <EstGenerica>
     * @throws GeneralException
     */
    public List<EstGenerica> tasaDeIngresoEfectivoAlSeptimoGrado(FiltroEstadisticas filtro) throws GeneralException {
        try {
            EstadisticasEstudiantesDAO codDAO = new EstadisticasEstudiantesDAO(em);
            if (codDAO.existeExtraccionEstudiantes(filtro.getAnio(), filtro.getNombrePk())) {
                return codDAO.tasaDeIngresoEfectivoAlSeptimoGrado(filtro);
            }
            return null;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio F-01
     *
     * @param filtro FiltroEstadisticas
     * @return Lista de <EstGenerica>
     * @throws GeneralException
     */
    public List<EstGenerica> porcentajeEstudiantesAprobados(FiltroEstadisticas filtro) throws GeneralException {
        try {
            EstadisticasEstudiantesDAO codDAO = new EstadisticasEstudiantesDAO(em);
            if (codDAO.existeExtraccionEstudiantes(filtro.getAnio(), filtro.getNombrePk())) {
                return codDAO.porcentajeEstudiantesAprobados(filtro);
            }
            return null;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio F-02
     *
     * @param filtro FiltroEstadisticas
     * @return Lista de <EstGenerica>
     * @throws GeneralException
     */
    public List<EstGenerica> tasaBrutaAprobacion(FiltroEstadisticas filtro) throws GeneralException {
        try {
            EstadisticasEstudiantesDAO codDAO = new EstadisticasEstudiantesDAO(em);
            if (codDAO.existeExtraccionEstudiantes(filtro.getAnio(), filtro.getNombrePk())) {
                return codDAO.tasaBrutaAprobacion(filtro);
            }
            return null;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio F-03
     *
     * @param filtro FiltroEstadisticas
     * @return Lista de <EstGenerica>
     * @throws GeneralException
     */
    public List<EstGenerica> tasaPromocion(FiltroEstadisticas filtro) throws GeneralException {
        try {

            if (filtro.getAnioComparacion() >= filtro.getAnio()) {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_ANIO_COMPARACION_DEBE_SER_MENOR);
                throw be;
            }

            EstadisticasEstudiantesDAO codDAO = new EstadisticasEstudiantesDAO(em);
            if (codDAO.existeExtraccionEstudiantes(filtro.getAnio(), filtro.getNombrePk()) && codDAO.existeExtraccionEstudiantes(filtro.getAnioComparacion(), filtro.getNombrePkComparacion())) {
                return codDAO.tasaPromocion(filtro);
            }
            return null;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio RE-01
     *
     * @param filtro FiltroEstadisticas
     * @return Lista de <EstGenerica>
     * @throws GeneralException
     */
    public List<EstGenerica> centrosEducativosSegunNivelEducativo(FiltroEstadisticas filtro) throws GeneralException {
        try {
            EstadisticasEstudiantesDAO codDAO = new EstadisticasEstudiantesDAO(em);
            if (codDAO.existeExtraccionEstudiantes(filtro.getAnio(), filtro.getNombrePk())) {
                return codDAO.centrosEducativosSegunNivelEducativo(filtro);
            }
            return null;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio RE-03
     *
     * @param filtro FiltroEstadisticas
     * @return Lista de <EstGenerica>
     * @throws GeneralException
     */
    public List<EstGenerica> porcentajeDeEstudiantesConAccesoAInternet(FiltroEstadisticas filtro) throws GeneralException {
        try {
            EstadisticasEstudiantesDAO codDAO = new EstadisticasEstudiantesDAO(em);
            if (codDAO.existeExtraccionEstudiantes(filtro.getAnio(), filtro.getNombrePk())) {
                return codDAO.porcentajeDeEstudiantesConAccesoAInternet(filtro);
            }
            return null;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio RE-05
     *
     * @param filtro FiltroEstadisticas
     * @return Lista de <EstGenerica>
     * @throws GeneralException
     */
    public List<EstGenerica> porcentajeDeEstudiantesConAccesoAComputadora(FiltroEstadisticas filtro) throws GeneralException {
        try {
            EstadisticasEstudiantesDAO codDAO = new EstadisticasEstudiantesDAO(em);
            if (codDAO.existeExtraccionEstudiantes(filtro.getAnio(), filtro.getNombrePk())) {
                return codDAO.porcentajeDeEstudiantesConAccesoAComputadora(filtro);
            }
            return null;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio RE-07
     *
     * @param filtro FiltroEstadisticas
     * @return Lista de <EstGenerica>
     * @throws GeneralException
     */
    public List<EstGenerica> estudiantesPorSeccion(FiltroEstadisticas filtro) throws GeneralException {
        try {
            EstadisticasEstudiantesDAO codDAO = new EstadisticasEstudiantesDAO(em);
            if (codDAO.existeExtraccionEstudiantes(filtro.getAnio(), filtro.getNombrePk())) {
                return codDAO.estudiantesPorSeccion(filtro);
            }
            return null;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio T-09
     *
     * @param filtro FiltroEstadisticas
     * @return Lista de <EstGenerica>
     * @throws GeneralException
     */
    public List<EstGenerica> tasaBrutaIngresoPorGrado(FiltroEstadisticas filtro) throws GeneralException {
        try {
            EstadisticasEstudiantesDAO codDAO = new EstadisticasEstudiantesDAO(em);
            if (codDAO.existeExtraccionEstudiantes(filtro.getAnio(), filtro.getNombrePk())) {
                return codDAO.tasaBrutaIngresoPorGrado(filtro);
            }
            return null;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio M-05
     *
     * @param filtro FiltroEstadisticas
     * @return Lista de <EstGenerica>
     * @throws GeneralException
     */
    public List<EstGenerica> tasaEspecificaMatriculaPorGrado(FiltroEstadisticas filtro) throws GeneralException {
        try {
            EstadisticasEstudiantesDAO codDAO = new EstadisticasEstudiantesDAO(em);
            if (codDAO.existeExtraccionEstudiantes(filtro.getAnio(), filtro.getNombrePk())) {
                return codDAO.tasaEspecificaMatriculaPorGrado(filtro);
            }
            return null;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio D-03
     *
     * @param filtro FiltroEstadisticas
     * @return Lista de <EstGenerica>
     * @throws GeneralException
     */
    public List<EstGenerica> distribucionPorcentualDocentesSegunAniosServicio(FiltroEstadisticas filtro) throws GeneralException {
        try {
            EstadisticasEstudiantesDAO codDAO = new EstadisticasEstudiantesDAO(em);
            if (codDAO.existeExtraccionEstudiantes(filtro.getAnio(), filtro.getNombrePk())) {
                return codDAO.distribucionPorcentualDocentesSegunAniosServicio(filtro);
            }
            return null;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio D-04
     *
     * @param filtro FiltroEstadisticas
     * @return Lista de <EstGenerica>
     * @throws GeneralException
     */
    public List<EstGenerica> promedioEstudiantesPorDocente(FiltroEstadisticas filtro) throws GeneralException {
        try {
            EstadisticasEstudiantesDAO codDAO = new EstadisticasEstudiantesDAO(em);
            if (codDAO.existeExtraccionEstudiantes(filtro.getAnio(), filtro.getNombrePk()) && codDAO.existeExtraccionPersonales(filtro.getAnio(), filtro.getNombrePk())) {
                return codDAO.promedioEstudiantesPorDocente(filtro);
            }
            return null;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio D-05
     *
     * @param filtro FiltroEstadisticas
     * @return Lista de <EstGenerica>
     * @throws GeneralException
     */
    public List<EstGenerica> porcentajeDeDocentesPorGradoAcademicoAlcanzado(FiltroEstadisticas filtro) throws GeneralException {
        try {
            EstadisticasEstudiantesDAO codDAO = new EstadisticasEstudiantesDAO(em);
            if (codDAO.existeExtraccionPersonales(filtro.getAnio(), filtro.getNombrePk())) {
                return codDAO.porcentajeDeDocentesPorGradoAcademicoAlcanzado(filtro);
            }
            return null;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio RE-04
     *
     * @param filtro FiltroEstadisticas
     * @return Lista de <EstGenerica>
     * @throws GeneralException
     */
    public List<EstGenerica> porcentajeDeDocentesConAccesoAInternet(FiltroEstadisticas filtro) throws GeneralException {
        try {
            EstadisticasEstudiantesDAO codDAO = new EstadisticasEstudiantesDAO(em);
            if (codDAO.existeExtraccionPersonales(filtro.getAnio(), filtro.getNombrePk())) {
                return codDAO.porcentajeDeDocentesConAccesoAInternet(filtro);
            }
            return null;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio 38 RE-02
     *
     * @param filtro FiltroEstadisticas
     * @return Lista de <EstGenerica>
     * @throws GeneralException
     */
    public List<EstGenerica> porcentajeDeCentrosEducativosConAccesoServiciosBasicos(FiltroEstadisticas filtro) throws GeneralException {
        try {
            EstadisticasEstudiantesDAO codDAO = new EstadisticasEstudiantesDAO(em);
            if (codDAO.existeExtraccionPersonales(filtro.getAnio(), filtro.getNombrePk())) {
                return codDAO.porcentajeDeCentrosEducativosConAccesoServiciosBasicos(filtro);
            }
            return null;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio 33 D-02
     *
     * @param filtro FiltroEstadisticas
     * @return Lista de <EstGenerica>
     * @throws GeneralException
     */
    public List<EstGenerica> porcentajeDeDocentesCertificadosPorNivelEducativo(FiltroEstadisticas filtro) throws GeneralException {
        try {
            EstadisticasEstudiantesDAO codDAO = new EstadisticasEstudiantesDAO(em);
            if (codDAO.existeExtraccionPersonales(filtro.getAnio(), filtro.getNombrePk())) {
                return codDAO.porcentajeDeDocentesCertificadosPorNivelEducativo(filtro);
            }
            return null;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio 32 D-01
     *
     * @param filtro FiltroEstadisticas
     * @return Lista de <EstGenerica>
     * @throws GeneralException
     */
    public List<EstGenerica> distribucionDeDocentesSegunNivelEducativo(FiltroEstadisticas filtro) throws GeneralException {
        try {
            EstadisticasEstudiantesDAO codDAO = new EstadisticasEstudiantesDAO(em);
            if (codDAO.existeExtraccionPersonales(filtro.getAnio(), filtro.getNombrePk())) {
                return codDAO.distribucionDeDocentesSegunNivelEducativo(filtro);
            }
            return null;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    
    /**
     * Devuelve los elementos que satisfacen el criterio 32 D-01
     *
     * @param filtro FiltroEstadisticas
     * @return Lista de <EstGenerica>
     * @throws GeneralException
     */
    public List<EstGenerica> porcentajeDeEstudiantesSegunNivelDeLogroEnPAES(FiltroEstadisticas filtro) throws GeneralException {
        try {
            EstadisticasEstudiantesDAO codDAO = new EstadisticasEstudiantesDAO(em);
            return codDAO.porcentajeDeEstudiantesSegunNivelDeLogroEnPAES(filtro);
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
}
