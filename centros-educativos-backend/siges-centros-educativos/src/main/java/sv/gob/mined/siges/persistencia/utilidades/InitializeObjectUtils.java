/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.utilidades;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import org.apache.commons.beanutils.PropertyUtilsBean;
import sv.gob.mined.siges.persistencia.annotations.AtributoInicializarHistorial;
import sv.gob.mined.siges.persistencia.entidades.SgDireccion;
import sv.gob.mined.siges.persistencia.entidades.SgEstudiante;
import sv.gob.mined.siges.persistencia.entidades.SgIdentificacion;
import sv.gob.mined.siges.persistencia.entidades.SgOrganismoAdministracionEscolar;
import sv.gob.mined.siges.persistencia.entidades.SgPersona;
import sv.gob.mined.siges.persistencia.entidades.SgPersonaOrganismoAdministracionLite;
import sv.gob.mined.siges.persistencia.entidades.SgTelefono;
import sv.gob.mined.siges.utils.ReflectionUtils;

/**
 *
 * @author Sofis Solutions
 */
public class InitializeObjectUtils {

    private static final Logger LOGGER = Logger.getLogger(InitializeObjectUtils.class.getName());
    private static PropertyUtilsBean beanUtil = new PropertyUtilsBean();

    /*
    * Inicializa colecciones anotadas con @AtributoInicializarColeccion recursivamente.
    * Inicializa colecciones de relación *ToOne anotada con @AtributoInicializarColeccion.
     */
//    public static void initializeLazyCollectionsRecursive(Object o) {
//        try {
//            if (o != null) {
//                for (Field field : ReflectionUtils.getAllFields(o.getClass())) {
//                    field.setAccessible(true);
//                    if (field.isAnnotationPresent(AtributoInicializarColeccion.class)) {
//                        Object value = field.get(o);
//                        if (value != null) {
//                            if (Collection.class.isAssignableFrom(field.getType())) {
//                                for (Object co : Collection.class.cast(value)) {
//                                    InitializeObjectUtils.initializeLazyCollectionsRecursive(co);
//                                }
//                            } else if (field.isAnnotationPresent(ManyToOne.class) || field.isAnnotationPresent(OneToOne.class)) {
//                                InitializeObjectUtils.initializeLazyCollectionsRecursive(value);
//                            }
//                        }
//                    }
//                }
//            }
//        } catch (Exception ex) {
//            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
//        }
//    }

    /*
    * Inicializa colecciones anotadas con @AtributoInicializarColeccion.
     */
//    public static void initializeLazyCollections(Object o) {
//        try {
//            if (o != null) {
//                for (Field field : ReflectionUtils.getAllFields(o.getClass())) {
//                    field.setAccessible(true);
//                    if (field.isAnnotationPresent(AtributoInicializarColeccion.class)) {
//                        Object value = field.get(o);
//                        if (value != null) {
//                            if (Collection.class.isAssignableFrom(field.getType())) {
//                                Collection.class.cast(value).size();
//                            }
//                        }
//                    }
//                }
//            }
//        } catch (Exception ex) {
//            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
//        }
//    }

    /**
     * Inicializa todas las relaciones *ToOne recursivamente. Inicializa
     * colecciones anotadas con @AtributoInicializarColeccion recursivamente.
     * Los objetos no inicializados quedan en null.
     */
    public static void initializeHistoricRevisionRecursive(Object o, Set<String> inicializados) {
        try {
            if (o != null) {
                String campoId = ReflectionUtils.obtenerNombreCampoAnotado(o.getClass(), Id.class);
                Object id = beanUtil.getProperty(o, campoId);
                String fieldIdentifier;
                for (Field field : ReflectionUtils.getAllFields(o.getClass())) {
                    field.setAccessible(true);
                    fieldIdentifier = o.getClass() + "-" + id + "-" + field.getName();
                    if (field.isAnnotationPresent(ManyToOne.class) || field.isAnnotationPresent(OneToOne.class)) {
                        // Inicializamos objeto *ToOne
                        Object value = beanUtil.getProperty(o, field.getName());
                        if (!inicializados.contains(fieldIdentifier) && value != null) {
                            inicializados.add(fieldIdentifier);
                            InitializeObjectUtils.initializeHistoricRevisionRecursive(value, inicializados);
                        }
                    } else if (field.isAnnotationPresent(AtributoInicializarHistorial.class)) {
                        Object value = beanUtil.getProperty(o, field.getName());
                        if (!inicializados.contains(fieldIdentifier) && value != null && Collection.class.isAssignableFrom(field.getType())) {
                            inicializados.add(fieldIdentifier);
                            for (Object co : Collection.class.cast(value)) {
                                InitializeObjectUtils.initializeHistoricRevisionRecursive(co, inicializados);
                            }
                        }
                    } else if (field.isAnnotationPresent(OneToMany.class) || field.isAnnotationPresent(ManyToMany.class)) {
                        // Collection no inicializada. Reemplazamos proxy por null
                        Object value = beanUtil.getProperty(o, field.getName());
                        if (!inicializados.contains(fieldIdentifier) && value != null && Collection.class.isAssignableFrom(field.getType())) {
                            inicializados.add(fieldIdentifier);
                            for (Object co : Collection.class.cast(value)) {
                                InitializeObjectUtils.initializeHistoricRevisionRecursive(co, inicializados);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public static void inicializarEstudianteHist(SgEstudiante est) {

//        if (est.getEstPersona() instanceof HibernateProxy){
//        }
        SgPersona per = est.getEstPersona();
        per.getPerPk();

        if (per.getPerIdentificaciones() != null) {
            for (SgIdentificacion ide : per.getPerIdentificaciones()) {
                ide.getIdeTipoDocumento().getTinPk();
            }
        }

        if (per.getPerNacionalidad() != null) {
            per.getPerNacionalidad().getNacPk();
        }

        if (per.getPerEstadoCivil() != null) {
            per.getPerEstadoCivil().getEciPk();
        }

        if (per.getPerEtnia() != null) {
            per.getPerEtnia().getEtnPk();
        }

        if (per.getPerSexo() != null) {
            per.getPerSexo().getSexPk();
        }

        if (per.getPerDepartamentoNacimento() != null) {
            per.getPerDepartamentoNacimento().getDepPk();
        }

        if (per.getPerMunicipioNacimiento() != null) {
            per.getPerMunicipioNacimiento().getMunPk();
        }

        if (per.getPerDiscapacidades() != null) {
            per.getPerDiscapacidades().size();
        }

        if (per.getPerTelefonos() != null) {
            for (SgTelefono t : per.getPerTelefonos()) {
                t.getTelTipoTelefono().getTtoPk();
            }
        }

        SgDireccion dir = per.getPerDireccion();
        if (dir != null) {
            if (dir.getDirDepartamento() != null) {
                dir.getDirDepartamento().getDepPk();
            }
            if (dir.getDirMunicipio() != null) {
                dir.getDirMunicipio().getMunPk();
            }
            if (dir.getDirCanton() != null) {
                dir.getDirCanton().getCanPk();
            }
            if (dir.getDirCaserio() != null) {
                dir.getDirCaserio().getCasPk();
            }
            if (dir.getDirTipoCalle() != null) {
                dir.getDirTipoCalle().getTllPk();
            }
        }

        if (est.getEstMedioTransporte() != null) {
            est.getEstMedioTransporte().getMtrPk();
        }

        if (per.getPerOcupacion() != null) {
            per.getPerOcupacion().getOcuPk();
        }

        if (per.getPerTipoSangre() != null) {
            per.getPerTipoSangre().getTsaPk();
        }

        if (per.getPerFoto() != null) {
            per.getPerFoto().getAchPk();
        }

        //Eliminar proxys    
        per.setPerAllegados(null);
        est.setEstManifestacionesViolencia(null);
        est.setEstAsistencia(null);
        est.setEstMatriculas(null);

    }

    public static void inicializarPersonas(SgOrganismoAdministracionEscolar org) {
        if (org.getOaePersonasOrganismoAdministriativoLite() != null) {
            for (SgPersonaOrganismoAdministracionLite per : org.getOaePersonasOrganismoAdministriativoLite()) {
                per.getPoaPk();
                if (per.getPoaPersona() != null) {
                    per.getPoaPersona().getPerPk();
                    if (per.getPoaSexo() != null) {
                        per.getPoaSexo().getSexPk();
                    }
                    if (per.getPoaCargo() != null) {
                        per.getPoaCargo().getCoaPk();
                    }
                } else {
                    if (per.getPoaSexo() != null) {
                        per.getPoaSexo().getSexPk();
                    }
                    if (per.getPoaCargo() != null) {
                        per.getPoaCargo().getCoaPk();
                    }
                }
            }
        }
        if (org.getOaeSede() != null) {
            org.getOaeSede().setSedJornadasLaborales(null);
            org.getOaeSede().setSedRelServicioInfra(null);
            org.getOaeSede().getSedPk();
        }
    }

    public static void inicializarPersona(SgPersona ret) {
        if (ret != null) {
            ret.getPerPk();
            if (ret.getPerTelefonos() != null) {
                ret.getPerTelefonos().size();
            }
            if (ret.getPerIdentificaciones() != null) {
                ret.getPerIdentificaciones().size();
            }
            if (ret.getPerDiscapacidades() != null) {
                ret.getPerDiscapacidades().size();
            }
            if (ret.getPerTrastornosAprendizaje() != null) {
                ret.getPerTrastornosAprendizaje().size();
            }
            if (ret.getPerTerapias() != null) {
                ret.getPerTerapias().size();
            }
            if (ret.getPerReferenciasApoyo() != null) {
                ret.getPerReferenciasApoyo().size();
            }
        }

    }

}
