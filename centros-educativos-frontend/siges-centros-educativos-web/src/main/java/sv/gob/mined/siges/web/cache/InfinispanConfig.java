/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.cache;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.Factory;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.infinispan.jcache.embedded.JCachingProvider;
import sv.gob.mined.siges.web.constantes.Constantes;

/**
 *
 * @author Sofis Solutions
 */
@ApplicationScoped
public class InfinispanConfig {

    private static final Logger LOGGER = Logger.getLogger(InfinispanConfig.class.getName());

    @Inject
    @ConfigProperty(name = "cache.catalogo.defaultExpiryMinutes", defaultValue = "5")
    private String durationMinutes;

    @Produces
    @ApplicationScoped
    private CacheManager manager;

    public void init(@Observes @Initialized(ApplicationScoped.class) Object init) throws Exception {

        JCachingProvider cachingProvider = (JCachingProvider) Caching.getCachingProvider();
        manager = cachingProvider.getCacheManager();

        LOGGER.log(Level.INFO, "### Cache Provider started: " + cachingProvider.getDefaultURI());
        LOGGER.log(Level.INFO, "### Expiricy policy: " + durationMinutes + " minutes.");

        Duration duration = new Duration(TimeUnit.MINUTES, Long.parseLong(durationMinutes));

        Factory expiryPolicyFactory = CreatedExpiryPolicy.factoryOf(duration);
        MutableConfiguration mutableConfiguration = new MutableConfiguration();
        mutableConfiguration.setTypes(String.class, String.class);
        mutableConfiguration.setStoreByValue(true);
        mutableConfiguration.setExpiryPolicyFactory(expiryPolicyFactory);

        //Centros
        manager.createCache(Constantes.AYUDA_CACHE, mutableConfiguration);
        manager.createCache(Constantes.SEDES_LAZY_CACHE, mutableConfiguration);
        manager.createCache(Constantes.ORG_LAZY_CACHE, mutableConfiguration);
        
        //Catalogo
        manager.createCache(Constantes.SECTOR_ECONOMICO_CACHE, mutableConfiguration);
        manager.createCache(Constantes.MODALIDAD_ATENCION_CACHE, mutableConfiguration);
        manager.createCache(Constantes.ESCALA_CALIFICACION_CACHE, mutableConfiguration);
        manager.createCache(Constantes.SUBMODALIDAD_ATENCION_CACHE, mutableConfiguration);
        manager.createCache(Constantes.PROGRAMA_EDUCATIVO_CACHE, mutableConfiguration);
        manager.createCache(Constantes.CLASIFICACION_CACHE, mutableConfiguration);
        manager.createCache(Constantes.MOTIVO_INASISTENCIA_CACHE, mutableConfiguration);
        manager.createCache(Constantes.SEXO_CACHE, mutableConfiguration);
        manager.createCache(Constantes.ETNIA_CACHE, mutableConfiguration);
        manager.createCache(Constantes.MEDIO_TRANSPORTE_CACHE, mutableConfiguration);
        manager.createCache(Constantes.ESTADO_CIVIL_CACHE, mutableConfiguration);
        manager.createCache(Constantes.TIPO_TELEFONO_CACHE, mutableConfiguration);
        manager.createCache(Constantes.TIPO_IDENTIFICACION_CACHE, mutableConfiguration);
        manager.createCache(Constantes.PAIS_CACHE, mutableConfiguration);
        manager.createCache(Constantes.DEPARTAMENTO_CACHE, mutableConfiguration);
        manager.createCache(Constantes.MUNICIPIO_CACHE, mutableConfiguration);
        manager.createCache(Constantes.CANTON_CACHE, mutableConfiguration);
        manager.createCache(Constantes.CASERIO_CACHE, mutableConfiguration);
        manager.createCache(Constantes.ZONA_CACHE, mutableConfiguration);
        manager.createCache(Constantes.TIPO_MANIFESTACION_CACHE, mutableConfiguration);
        manager.createCache(Constantes.JORNADA_LABORAL_CACHE, mutableConfiguration);
        manager.createCache(Constantes.AREA_INDICADOR_CACHE, mutableConfiguration);
        manager.createCache(Constantes.AREA_ASIGNATURA_MODULO_CACHE, mutableConfiguration);
        manager.createCache(Constantes.CALIFICACION_CACHE, mutableConfiguration);
        manager.createCache(Constantes.NIVEL_EMPLEADO_CACHE, mutableConfiguration);
        manager.createCache(Constantes.CATEGORIA_EMPLEADO_CACHE, mutableConfiguration);
        manager.createCache(Constantes.CARGO_CACHE, mutableConfiguration);
        manager.createCache(Constantes.TIPO_CONTRATO_CACHE, mutableConfiguration);
        manager.createCache(Constantes.LEY_SALARIO_CACHE, mutableConfiguration);
        manager.createCache(Constantes.FUENTE_FINANCIAMIENTO_CACHE, mutableConfiguration);
        manager.createCache(Constantes.INSTITUCION_PAGA_SALARIO_CACHE, mutableConfiguration);
        manager.createCache(Constantes.TIPO_INSTITUCION_PAGA_CACHE, mutableConfiguration);
        manager.createCache(Constantes.TIPO_NOMBRAMIENTO_CACHE, mutableConfiguration);
        manager.createCache(Constantes.MODO_PAGO_CACHE, mutableConfiguration);
        manager.createCache(Constantes.SISTEMA_GESTION_CONTENIDO_CACHE, mutableConfiguration);
        manager.createCache(Constantes.IDIOMA_CACHE, mutableConfiguration);
        manager.createCache(Constantes.NIVEL_IDIOMA_CACHE, mutableConfiguration);
        manager.createCache(Constantes.ESCOLARIDAD_CACHE, mutableConfiguration);
        manager.createCache(Constantes.TIPO_ESTUDIO_CACHE, mutableConfiguration);
        manager.createCache(Constantes.CARRERA_CACHE, mutableConfiguration);
        manager.createCache(Constantes.ESPECIALIDAD_CACHE, mutableConfiguration);
        manager.createCache(Constantes.MODALIDAD_ESTUDIO_CACHE, mutableConfiguration);
        manager.createCache(Constantes.TIPO_CAPACITACION_CACHE, mutableConfiguration);
        manager.createCache(Constantes.ALCANCE_CAPACITACION_CACHE, mutableConfiguration);
        manager.createCache(Constantes.PROFESION_CACHE, mutableConfiguration);
        manager.createCache(Constantes.TIPO_TRABAJO_CACHE, mutableConfiguration);
        manager.createCache(Constantes.DEPENDENCIA_ECONOMICA_CACHE, mutableConfiguration);
        manager.createCache(Constantes.TIPO_SANGRE_CACHE, mutableConfiguration);
        manager.createCache(Constantes.DISCAPACIDAD_CACHE, mutableConfiguration);
        manager.createCache(Constantes.TRASTORNO_APRENDIZAJE_CACHE, mutableConfiguration);
        manager.createCache(Constantes.OCUPACION_CACHE, mutableConfiguration);
        manager.createCache(Constantes.MOTIVO_RETIRO_CACHE, mutableConfiguration);
        manager.createCache(Constantes.TIPO_PARENTESCO_CACHE, mutableConfiguration);
        manager.createCache(Constantes.NIVEL_FORMACION_DOCENTE_CACHE, mutableConfiguration);
        manager.createCache(Constantes.CONFIGURACION_CACHE, mutableConfiguration);
        manager.createCache(Constantes.CONFIGURACION_CACHE_CODIGO, mutableConfiguration);
        manager.createCache(Constantes.CONFIGURACION_FIRMA_ELECTRONICA_CACHE_CODIGO, mutableConfiguration);
        manager.createCache(Constantes.CATEGORIA_FORMACION_DOCENTE_CACHE, mutableConfiguration);
        manager.createCache(Constantes.MODULO_FORMACION_DOCENTE_CACHE, mutableConfiguration);
        manager.createCache(Constantes.NACIONALIDAD_CACHE, mutableConfiguration);
        manager.createCache(Constantes.TIPO_CALLE_CACHE, mutableConfiguration);
        manager.createCache(Constantes.SERVICIO_INFRAESTRUCTURA_CACHE, mutableConfiguration);
        manager.createCache(Constantes.TIPO_ORGANISMO_ADMINISTRATIVO_CACHE, mutableConfiguration);
        manager.createCache(Constantes.AFP_CACHE, mutableConfiguration);
        manager.createCache(Constantes.TIPO_SERVICIO_SANITARIO_CACHE, mutableConfiguration);
        manager.createCache(Constantes.TIPO_REPRESENTANTE_CACHE, mutableConfiguration);
        manager.createCache(Constantes.CARGO_TITULAR_CACHE, mutableConfiguration);
        manager.createCache(Constantes.DEFINICION_TITULO_CACHE, mutableConfiguration);
        manager.createCache(Constantes.TIPO_RIESGO_CACHE, mutableConfiguration);
        manager.createCache(Constantes.GRADO_AFECTACION_CACHE, mutableConfiguration);
        manager.createCache(Constantes.GLOSARIOS_CACHE, mutableConfiguration);
        manager.createCache(Constantes.MOTIVO_INASISTENCIA_PERSONAL_CACHE, mutableConfiguration);
        manager.createCache(Constantes.GRADO_RIESGO_CACHE, mutableConfiguration);
        manager.createCache(Constantes.TIPO_PROVEEDOR_CACHE, mutableConfiguration);
        manager.createCache(Constantes.PROYECTO_INSTITUCIONAL_CACHE, mutableConfiguration);
        manager.createCache(Constantes.VELOCIDAD_INTERNET_CACHE, mutableConfiguration);
        manager.createCache(Constantes.TIPO_APOYO_CACHE, mutableConfiguration);
        manager.createCache(Constantes.TIPO_ACCION_CACHE, mutableConfiguration);
        manager.createCache(Constantes.TIPOS_MODULO_CACHE, mutableConfiguration);
        manager.createCache(Constantes.CATEGORIAS_CURSO_CACHE, mutableConfiguration);
        manager.createCache(Constantes.MOTIVO_TRASLADO_CACHE, mutableConfiguration);
        manager.createCache(Constantes.ORGANISMO_COORDINACION_ESCOLAR_CACHE, mutableConfiguration);
        manager.createCache(Constantes.RECURSOS_CACHE, mutableConfiguration);
        manager.createCache(Constantes.CARGO_OAE_CACHE, mutableConfiguration);
        manager.createCache(Constantes.FORMULAS_CACHE, mutableConfiguration);
        manager.createCache(Constantes.CARGA_MAXIMO_NIVEL_EDUCATIVO_ALCANZADO, mutableConfiguration);
        manager.createCache(Constantes.MOTIVO_FALLECIMIENTO_CACHE, mutableConfiguration);        
        manager.createCache(Constantes.ESTADO_PROCESO_LEGALIZACION_CACHE, mutableConfiguration);
        manager.createCache(Constantes.MINISTERIO_OTORGANTE_CACHE, mutableConfiguration);
        manager.createCache(Constantes.NATURALEZA_CONTRATO_CACHE, mutableConfiguration);
        manager.createCache(Constantes.TIPO_ACUERDO_CACHE, mutableConfiguration);
        manager.createCache(Constantes.CATEGORIA_VIOLENCIA_CACHE, mutableConfiguration);
        manager.createCache(Constantes.IMPLEMENTADORA_CACHE, mutableConfiguration);
        manager.createCache(Constantes.MOTIVO_ANULACION_TITULO, mutableConfiguration);
        manager.createCache(Constantes.TIPO_ALFABETIZADOR, mutableConfiguration);
        manager.createCache(Constantes.MOTIVO_REIMPRESION, mutableConfiguration);
        manager.createCache(Constantes.PREGUNTAS_CIERRE_ANIO_CACHE, mutableConfiguration);
        manager.createCache(Constantes.TITULO_ANTERIOR_CACHE, mutableConfiguration);
        manager.createCache(Constantes.PLANTILLAS_CACHE, mutableConfiguration);
        manager.createCache(Constantes.PERFIL_CACHE, mutableConfiguration);
        manager.createCache(Constantes.REFERENCIAS_CACHE, mutableConfiguration);
        manager.createCache(Constantes.TERAPIAS_CACHE, mutableConfiguration);
        manager.createCache(Constantes.MOTIVOS_SELECCION_CACHE, mutableConfiguration);
        manager.createCache(Constantes.CALIDAD_INGRESO_APLICANTE_CACHE, mutableConfiguration);
        manager.createCache(Constantes.IDENTIFICACION_OAE_CACHE, mutableConfiguration);
        manager.createCache(Constantes.ENFERMEDAD_NO_TRANSMITIBLE_CACHE, mutableConfiguration);
        manager.createCache(Constantes.TIEMPO_COMIDA_DIA_CACHE, mutableConfiguration);
        
        manager.createCache(Constantes.COMPANIAS_TELECOMUNICACION_CACHE, mutableConfiguration);
        manager.createCache(Constantes.ELEMENTOS_HOGAR_CACHE, mutableConfiguration);
        manager.createCache(Constantes.FUENTES_ABASTECIMIENTO_AGUA, mutableConfiguration);
        manager.createCache(Constantes.MATERIALES_PISO_CACHE, mutableConfiguration);
        manager.createCache(Constantes.MOTIVO_CIERRE_SEDE_CACHE, mutableConfiguration);
        //Centros cache
        manager.createCache(Constantes.ORG_CURRICULAR_CACHE, mutableConfiguration);
        manager.createCache(Constantes.NIVEL_CACHE, mutableConfiguration);
        manager.createCache(Constantes.CICLO_CACHE, mutableConfiguration);
        manager.createCache(Constantes.MODALIDAD_CACHE, mutableConfiguration);
        manager.createCache(Constantes.OPCION_CACHE, mutableConfiguration);
        manager.createCache(Constantes.REL_MOD_ED_MOD_ATEN_CACHE, mutableConfiguration);
        manager.createCache(Constantes.REL_OPCION_PROG_CACHE, mutableConfiguration);
        manager.createCache(Constantes.GRADO_CACHE, mutableConfiguration);
        manager.createCache(Constantes.ANIO_LECTIVO_CACHE, mutableConfiguration);
        manager.createCache(Constantes.REL_GRADO_PLAN_ESTUDIO_CACHE, mutableConfiguration);
        manager.createCache(Constantes.COMPONENTE_PLAN_GRADO_CACHE, mutableConfiguration);
        manager.createCache(Constantes.RANGO_FECHA_CACHE, mutableConfiguration);
        manager.createCache(Constantes.CALENDARIO_CACHE, mutableConfiguration);
        manager.createCache(Constantes.COMPONENTE_PLAN_ESTUDIO_NUMERICO_CURSADO_ESTUDIANTE_CACHE, mutableConfiguration);
        manager.createCache(Constantes.COMPONENTE_PLAN_ESTUDIO_CONCEPTUAL_CURSADO_ESTUDIANTE_CACHE, mutableConfiguration);
        manager.createCache(Constantes.COMPONENTE_PLAN_ESTUDIO_NUMERICO_ACTUALES_ORD_ESTUDIANTE_CACHE, mutableConfiguration);
        manager.createCache(Constantes.PROBABILIDAD_DESERCION_ESTUDIANTE_CACHE, mutableConfiguration);
        
        //graficos
        manager.createCache(Constantes.GRAFICO_ALERTAS_ESTUDIANTE, mutableConfiguration);
        manager.createCache(Constantes.GRAFICO_ASISTENCIAS_ESTUDIANTE_POR_ANIO, mutableConfiguration);
        manager.createCache(Constantes.GRAFICO_ASISTENCIAS_ESTUDIANTE_POR_MES_EN_ANIO, mutableConfiguration);
        manager.createCache(Constantes.GRAFICO_CALIFICACIONES_APR_NUMERICAS_ESTUDIANTE_POR_GRADO_CONTRA_PROMEDIO_NACIONAL, mutableConfiguration);
        manager.createCache(Constantes.GRAFICO_CALIFICACIONES_APR_NUMERICAS_ESTUDIANTE_POR_GRADO_Y_COMPONENTE, mutableConfiguration);
        manager.createCache(Constantes.GRAFICO_CALIFICACIONES_ORD_ACTUALES_NUMERICAS_ESTUDIANTE_POR_GRADO_Y_COMPONENTE, mutableConfiguration);
        manager.createCache(Constantes.GRAFICO_CALIFICACIONES_APR_CONCEPTUALES_ESTUDIANTE_POR_GRADO_Y_COMPONENTE, mutableConfiguration);
        manager.createCache(Constantes.GRAFICO_MOT_INASISTENCIAS_ESTUDIANTE_POR_ANIO, mutableConfiguration);
        manager.createCache(Constantes.GRAFICO_MOT_INASISTENCIAS_ESTUDIANTE_POR_MES_EN_ANIO, mutableConfiguration);
        manager.createCache(Constantes.GRAFICO_EVOLUCION_ENCUESTAS_MATRICULAS, mutableConfiguration);
        manager.createCache(Constantes.GRAFICO_EVOLUCION_ENCUESTAS_SEXO, mutableConfiguration);
        
        
        
                   
    }

    public CacheManager getManager() {
        return manager;
    }

}
