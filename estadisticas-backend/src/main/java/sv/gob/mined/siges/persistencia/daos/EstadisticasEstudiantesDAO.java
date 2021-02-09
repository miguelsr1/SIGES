package sv.gob.mined.siges.persistencia.daos;

import com.sofis.persistence.dao.imp.hibernate.HibernateJpaDAOImp;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.dto.EstGenerica;
import sv.gob.mined.siges.enumerados.EnumDesagregacion;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.filtros.FiltroEstadisticas;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgExtraccion;

/**
 *
 * @author usuario
 */
public class EstadisticasEstudiantesDAO extends HibernateJpaDAOImp<SgExtraccion, Integer> implements Serializable {

    private EntityManager em;

    public EstadisticasEstudiantesDAO(EntityManager em) throws Exception {
        super(em);
        this.em = em;
    }

    //1 - P-01
    public List<EstGenerica> poblacionNoEscolarizadaPorEdad(FiltroEstadisticas filtro) throws GeneralException {
        Session session = em.unwrap(Session.class);

        if (filtro.getEdadMaximaPoblacionEstadisticas() == null) {
            BusinessException be = new BusinessException();
            be.addError(Errores.ERROR_CONFIG_EDAD_MAXIMA_POBLACION_ESTADISTICAS_INCORRECTA);
            throw be;
        }

        String colDesagregacion = "'-'";
        String colPoblacionDesagregacion = "'-'";
        if (filtro.getDesagregacion() != null) {
            if (!filtro.getDesagregacion().equals(EnumDesagregacion.SEXO)) {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_DESAGREGACION_NO_SOPORTADA);
                throw be;
            } else {
                colDesagregacion = "e.ext_est_sexo_nom";
                colPoblacionDesagregacion = "pro_sexo_nom";
            }
        }

        SQLQuery q = session.createSQLQuery("SELECT edad as dato, desagregacion, SUM(b-a) as cantidad FROM ("
                + " select e.ext_est_edad_al_30_oct_mat as edad, " + colDesagregacion + " as desagregacion, count(*) a, 0 b from estadisticas.sg_ext_estudiantes e"
                + " INNER JOIN estadisticas.sg_extracciones ex ON (e.ext_cabezal_fk = ex.ext_pk)"
                + " where ex.ext_anio = :anio and ex.ext_nombre_fk = :nombrePk"
                + " group by e.ext_est_edad_al_30_oct_mat, desagregacion"
                + " UNION ALL"
                + " select pro_edad as edad, " + colPoblacionDesagregacion + " as desagregacion, 0 a, pro_poblacion b from estadisticas.sg_proyecciones_poblacion where pro_anio = :anio"
                + " ) z where edad <= :edadMaxima and edad >= 0 group by z.edad, desagregacion order by z.edad, desagregacion");

        q.addScalar("dato", new IntegerType());
        q.addScalar("desagregacion", new StringType());
        q.addScalar("cantidad", new LongType());
        q.setParameter("anio", filtro.getAnio());
        q.setParameter("nombrePk", filtro.getNombrePk());
        q.setParameter("edadMaxima", filtro.getEdadMaximaPoblacionEstadisticas());
        q.setResultTransformer(Transformers.aliasToBean(EstGenerica.class));
        return q.list();
    }

    //2 - P-02
    public List<EstGenerica> porcentajePoblacionNoEscolarizadaPorEdad(FiltroEstadisticas filtro) throws GeneralException {
        Session session = em.unwrap(Session.class);

        if (filtro.getEdadMaximaPoblacionEstadisticas() == null) {
            BusinessException be = new BusinessException();
            be.addError(Errores.ERROR_CONFIG_EDAD_MAXIMA_POBLACION_ESTADISTICAS_INCORRECTA);
            throw be;
        }

        String colDesagregacion = "'-'";
        String colPoblacionDesagregacion = "'-'";
        if (filtro.getDesagregacion() != null) {
            if (!filtro.getDesagregacion().equals(EnumDesagregacion.SEXO)) {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_DESAGREGACION_NO_SOPORTADA);
                throw be;
            } else {
                colDesagregacion = "e.ext_est_sexo_nom";
                colPoblacionDesagregacion = "pro_sexo_nom";
            }
        }

        SQLQuery q = session.createSQLQuery("SELECT dato, desagregacion,(((sum2 - sum1) / sum2) * 100 ) AS cantidad "
                + " FROM "
                + " (SELECT edad      AS dato, "
                + "       desagregacion, "
                + "       Sum(a) AS sum1, "
                + "       Sum(b) AS sum2 "
                + "       FROM   (SELECT e.ext_est_edad_al_30_oct_mat AS edad, "
                + "               " + colDesagregacion + "  AS desagregacion,"
                + "               Count(*)  a, "
                + "               0 b "
                + "        FROM   estadisticas.sg_ext_estudiantes e "
                + "               INNER JOIN estadisticas.sg_extracciones ex "
                + "                       ON ( e.ext_cabezal_fk = ex.ext_pk ) "
                + "        WHERE  (e.ext_est_sexo_nom = 'Masculino' or e.ext_est_sexo_nom = 'Femenino') "
                + " 	          AND ex.ext_anio = :anio "
                + "               AND ex.ext_nombre_fk = :nombrePk "
                + "        GROUP  BY e.ext_est_edad_al_30_oct_mat, "
                + "                  e.ext_est_sexo_nom "
                + "        UNION ALL "
                + "        SELECT pro_edad      AS edad, "
                + "               " + colPoblacionDesagregacion + " as desagregacion, "
                + "               0             a, "
                + "               pro_poblacion b "
                + "        FROM   estadisticas.sg_proyecciones_poblacion "
                + "        WHERE  pro_anio = :anio) z "
                + "WHERE  edad <= :edadMaxima and edad >= 0"
                + "GROUP  BY z.edad, "
                + "          desagregacion "
                + "ORDER  BY z.edad, "
                + "          desagregacion) h");

        q.addScalar("dato", new IntegerType());
        q.addScalar("desagregacion", new StringType());
        q.addScalar("cantidad", new DoubleType());
        q.setParameter("anio", filtro.getAnio());
        q.setParameter("nombrePk", filtro.getNombrePk());
        q.setParameter("edadMaxima", filtro.getEdadMaximaPoblacionEstadisticas());
        q.setResultTransformer(Transformers.aliasToBean(EstGenerica.class));
        return q.list();
    }

    //3 - P-03
    public List<EstGenerica> porcentajeEstudiantesDesertores(FiltroEstadisticas filtro) throws GeneralException {

        Session session = em.unwrap(Session.class);

        String colDesagregacion = "'-'";
        if (filtro.getDesagregacion() != null) {
            if (filtro.getDesagregacion().equals(EnumDesagregacion.DEPARTAMENTO)) {
                colDesagregacion = "e.ext_est_sede_dep_nom";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.ZONA)) {
                colDesagregacion = "e.ext_est_sede_zon_nom";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_OFI_PRI)) {
                colDesagregacion = "e.ext_est_sede_tipo";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_PUB_PRI)) {
                colDesagregacion = "e.ext_sed_sector";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SEXO)) {
                colDesagregacion = "e.ext_est_sexo_nom";
            } else {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_DESAGREGACION_NO_SOPORTADA);
                throw be;
            }
        }

        SQLQuery q = session.createSQLQuery("Select nombre_grado as dato, desagregacion, (case ex_inicial when 0 then 0 else (((ex_inicial -  ex_final)/ex_inicial) * 100) end) as cantidad from ( "
                + "Select dato, nombre_grado, desagregacion, sum(a) as ex_final, sum(b) as ex_inicial from ( "
                + "SELECT e.ext_est_grado_nom as nombre_grado,"
                + " CONCAT(e.ext_est_nivel_nom, ' - ', "
                + " e.ext_est_ciclo_nom, ' - ', "
                + " e.ext_est_modalidad_educativa_nom, ' - ', "
                + " e.ext_est_modalidad_atencion_nom, ' - ', "
                + " e.ext_est_grado_nom) as dato, " + colDesagregacion + " as desagregacion,"
                + "     e.ext_est_nivel_orden AS orden_nivel, "
                + "     e.ext_est_ciclo_orden AS orden_ciclo, "
                + "     e.ext_est_mod_orden   AS orden_modalidad, "
                + "     e.ext_est_grado_orden AS orden_grado,"
                + " count(*) a, 0 b from estadisticas.sg_ext_estudiantes e "
                + "INNER JOIN estadisticas.sg_extracciones ex ON (e.ext_cabezal_fk = ex.ext_pk) "
                + "where "
                + "ex.ext_anio = :anio and ex.ext_nombre_fk = :nombrePk "
                + "group by e.ext_est_nivel_nom, e.ext_est_ciclo_nom, e.ext_est_modalidad_educativa_nom, e.ext_est_modalidad_atencion_nom, e.ext_est_grado_nom, desagregacion, orden_nivel, orden_ciclo, orden_modalidad, orden_grado  "
                + "UNION ALL "
                + "SELECT e.ext_est_grado_nom as nombre_grado,"
                + " CONCAT(e.ext_est_nivel_nom, ' - ', "
                + " e.ext_est_ciclo_nom, ' - ', "
                + " e.ext_est_modalidad_educativa_nom, ' - ', "
                + " e.ext_est_modalidad_atencion_nom, ' - ', "
                + " e.ext_est_grado_nom) as dato, " + colDesagregacion + "  as desagregacion,"
                + "     e.ext_est_nivel_orden AS orden_nivel, "
                + "     e.ext_est_ciclo_orden AS orden_ciclo, "
                + "     e.ext_est_mod_orden   AS orden_modalidad, "
                + "     e.ext_est_grado_orden AS orden_grado,"
                + " 0 a, count(*) b from estadisticas.sg_ext_estudiantes e "
                + "INNER JOIN estadisticas.sg_extracciones ex ON (e.ext_cabezal_fk = ex.ext_pk) "
                + "where ex.ext_anio = :anio_c and ex.ext_nombre_fk = :nombrePk_c "
                + "group by e.ext_est_nivel_nom, e.ext_est_ciclo_nom, e.ext_est_modalidad_educativa_nom, e.ext_est_modalidad_atencion_nom, e.ext_est_grado_nom, desagregacion, orden_nivel, orden_ciclo, orden_modalidad, orden_grado "
                + ") z group by dato, nombre_grado, desagregacion, orden_nivel, orden_ciclo, orden_modalidad, orden_grado order by orden_nivel, orden_ciclo, orden_modalidad, orden_grado) h");

        q.addScalar("dato", new StringType());
        q.addScalar("desagregacion", new StringType());
        q.addScalar("cantidad", new DoubleType());
        q.setParameter("anio", filtro.getAnio());
        q.setParameter("nombrePk", filtro.getNombrePk());
        q.setParameter("anio_c", filtro.getAnioComparacion());
        q.setParameter("nombrePk_c", filtro.getNombrePkComparacion());
        q.setResultTransformer(Transformers.aliasToBean(EstGenerica.class));
        return q.list();
    }

    //4 - I-01
    public List<EstGenerica> tasaBrutaIngresoPrimerGradoEducacionBasica(FiltroEstadisticas filtro) throws GeneralException {
        Session session = em.unwrap(Session.class);

        String colDesagregacion = "'-'";
        String colPoblacionDesagregacion = "'-'";
        if (filtro.getDesagregacion() != null) {
            if (!filtro.getDesagregacion().equals(EnumDesagregacion.SEXO)) {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_DESAGREGACION_NO_SOPORTADA);
                throw be;
            } else {
                colDesagregacion = "e.ext_est_sexo_nom";
                colPoblacionDesagregacion = "pro_sexo_nom";
            }
        }

        SQLQuery q = session.createSQLQuery("Select dato, desagregacion, (case sum2 when 0 then 0 else (sum1 / sum2 * 100) end) as cantidad FROM ("
                + " SELECT edad as dato, desagregacion, sum(a) as sum1, sum(b) as sum2 FROM ("
                + " SELECT 7 as edad, " + colDesagregacion + " as desagregacion, count(*) a, 0 b from estadisticas.sg_ext_estudiantes e"
                + " INNER JOIN estadisticas.sg_extracciones ex ON (e.ext_cabezal_fk = ex.ext_pk)"
                + " where e.ext_est_nivel = :nivelPk and e.ext_est_grado = :gradoPk"
                + " and e.ext_est_repetidor is not true"
                + " and (e.ext_est_sexo_nom = 'Masculino' or e.ext_est_sexo_nom = 'Femenino')"
                + " and ex.ext_anio = :anio and ex.ext_nombre_fk = :nombrePk"
                + " group by desagregacion"
                + " UNION ALL"
                + " select pro_edad as edad, " + colPoblacionDesagregacion + " as desagregacion, 0 a, pro_poblacion b from estadisticas.sg_proyecciones_poblacion where pro_anio = :anio and pro_edad = 7"
                + " ) z group by z.edad, desagregacion order by z.edad, desagregacion) h");

        q.addScalar("dato", new IntegerType());
        q.addScalar("desagregacion", new StringType());
        q.addScalar("cantidad", new DoubleType());
        q.setParameter("anio", filtro.getAnio());
        q.setParameter("nombrePk", filtro.getNombrePk());
        q.setParameter("gradoPk", Constantes.NIVEL_EDUCACION_BASICA_PRIMER_GRADO_REGULAR_PK);
        q.setParameter("nivelPk", Constantes.NIVEL_EDUCACION_BASICA_PK);
        q.setResultTransformer(Transformers.aliasToBean(EstGenerica.class));
        return q.list();
    }

    //5 - I-02
    public List<EstGenerica> tasaNetaIngresoPrimerGradoEducacionBasica(FiltroEstadisticas filtro) throws GeneralException {
        Session session = em.unwrap(Session.class);

        String colDesagregacion = "'-'";
        String colPoblacionDesagregacion = "'-'";
        if (filtro.getDesagregacion() != null) {
            if (!filtro.getDesagregacion().equals(EnumDesagregacion.SEXO)) {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_DESAGREGACION_NO_SOPORTADA);
                throw be;
            } else {
                colDesagregacion = "e.ext_est_sexo_nom";
                colPoblacionDesagregacion = "pro_sexo_nom";
            }
        }

        SQLQuery q = session.createSQLQuery("SELECT dato,desagregacion, (case total when 0 then 0 else  ((cant / total) * 100) end) AS cantidad "
                + " FROM "
                + " (SELECT "
                + " 'estudiantes' as dato,"
                + " desagregacion,"
                + " Sum(a) AS cant, "
                + " Sum(b) AS total "
                + " FROM   (SELECT "
                + " " + colDesagregacion + " as desagregacion, "
                + " Count(*)  a, "
                + " 0 b "
                + " FROM estadisticas.sg_ext_estudiantes e "
                + " INNER JOIN estadisticas.sg_extracciones ex "
                + " ON ( e.ext_cabezal_fk = ex.ext_pk ) "
                + " WHERE  (e.ext_est_sexo_nom = 'Masculino' or e.ext_est_sexo_nom = 'Femenino') "
                + " and  e.ext_est_repetidor is not true "
                + " AND ex.ext_anio = :anio "
                + " AND ex.ext_nombre_fk = :nombrePk "
                + " AND e.ext_est_nivel = :nivelPk and e.ext_est_grado = :gradoPk "
                + " and e.ext_est_edad_al_30_oct_mat = 7 "
                + " GROUP  BY e.ext_est_edad_al_30_oct_mat, desagregacion "
                + " UNION ALL "
                + " SELECT " + colPoblacionDesagregacion + " as desagregacion,"
                + " 0 a,"
                + " sum(pro_poblacion) b  "
                + " FROM estadisticas.sg_proyecciones_poblacion "
                + " WHERE  pro_anio = :anio "
                + " and pro_edad = 7 "
                + " group by pro_edad, desagregacion )z group by desagregacion ) e");

        q.addScalar("dato", new StringType());
        q.addScalar("desagregacion", new StringType());
        q.addScalar("cantidad", new DoubleType());
        q.setParameter("anio", filtro.getAnio());
        q.setParameter("gradoPk", Constantes.NIVEL_EDUCACION_BASICA_PRIMER_GRADO_REGULAR_PK);
        q.setParameter("nivelPk", Constantes.NIVEL_EDUCACION_BASICA_PK);
        q.setParameter("nombrePk", filtro.getNombrePk());
        q.setResultTransformer(Transformers.aliasToBean(EstGenerica.class));
        return q.list();
    }

    //6 - I-03
    public List<EstGenerica> tasaEspecificaDeEscolarizacionPorEdad(FiltroEstadisticas filtro) throws GeneralException {
        Session session = em.unwrap(Session.class);

        if (filtro.getEdadMaximaPoblacionEstadisticas() == null) {
            BusinessException be = new BusinessException();
            be.addError(Errores.ERROR_CONFIG_EDAD_MAXIMA_POBLACION_ESTADISTICAS_INCORRECTA);
            throw be;
        }

        String colDesagregacion = "'-'";
        String colPoblacionDesagregacion = "'-'";
        if (filtro.getDesagregacion() != null) {
            if (!filtro.getDesagregacion().equals(EnumDesagregacion.SEXO)) {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_DESAGREGACION_NO_SOPORTADA);
                throw be;
            } else {
                colDesagregacion = "e.ext_est_sexo_nom";
                colPoblacionDesagregacion = "pro_sexo_nom";
            }
        }

        SQLQuery q = session.createSQLQuery("SELECT x.edad dato, desagregacion, (case x.cantidad_total when 0 then 0 else ((x.cantidad_matriculados / x.cantidad_total) * 100 ) end)  AS cantidad "
                + "  FROM "
                + " (SELECT e.edad, desagregacion, SUM(e.cantidad_matriculados) cantidad_matriculados, SUM(e.cantidad_total) cantidad_total "
                + " FROM  "
                + " (SELECT z.edad, desagregacion,z.cantidad_matriculados cantidad_matriculados,z.cantidad_total cantidad_total "
                + " FROM (SELECT "
                + " e.ext_est_edad_al_30_oct_mat edad,"
                + " " + colDesagregacion + " as desagregacion,"
                + " Count(*)  cantidad_matriculados,"
                + " 0 cantidad_total "
                + " FROM estadisticas.sg_ext_estudiantes e "
                + " INNER JOIN estadisticas.sg_extracciones ex "
                + " ON ( e.ext_cabezal_fk = ex.ext_pk ) "
                + " WHERE  (e.ext_est_sexo_nom = 'Masculino' or e.ext_est_sexo_nom = 'Femenino') "
                + " AND ex.ext_anio = :anio "
                + " AND ex.ext_nombre_fk = :nombrePk  "
                + " GROUP  BY e.ext_est_edad_al_30_oct_mat, desagregacion "
                + " UNION ALL "
                + " SELECT "
                + " pro_edad edad, "
                + " " + colPoblacionDesagregacion + " as desagregacion, "
                + " 0 cantidad_matriculados, "
                + " pro_poblacion cantidad_total "
                + " FROM estadisticas.sg_proyecciones_poblacion "
                + " WHERE  pro_anio = :anio "
                + " group by pro_edad, desagregacion, pro_poblacion "
                + " ) z "
                + " WHERE  edad <= :edadMaxima and edad >= 0"
                + "  group by z.edad, desagregacion,z.cantidad_matriculados,z.cantidad_total ) e group by e.edad, desagregacion) "
                + " x group by x.edad, desagregacion,x.cantidad_matriculados,x.cantidad_total ");

        q.addScalar("dato", new StringType());
        q.addScalar("desagregacion", new StringType());
        q.addScalar("cantidad", new DoubleType());
        q.setParameter("anio", filtro.getAnio());
        q.setParameter("nombrePk", filtro.getNombrePk());
        q.setParameter("edadMaxima", filtro.getEdadMaximaPoblacionEstadisticas());
        q.setResultTransformer(Transformers.aliasToBean(EstGenerica.class));
        return q.list();
    }

    //7 - I-04
    public List<EstGenerica> porcentajeDeEstudiantesDePrimerGradoConExperienciaEnEducacionParvularia(FiltroEstadisticas filtro) throws GeneralException {
        Session session = em.unwrap(Session.class);

        String colDesagregacion = "'-'";
        if (filtro.getDesagregacion() != null) {
            if (filtro.getDesagregacion().equals(EnumDesagregacion.DEPARTAMENTO)) {
                colDesagregacion = "e.ext_est_sede_dep_nom";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.ZONA)) {
                colDesagregacion = "e.ext_est_sede_zon_nom";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_OFI_PRI)) {
                colDesagregacion = "e.ext_est_sede_tipo";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_PUB_PRI)) {
                colDesagregacion = "e.ext_sed_sector";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SEXO)) {
                colDesagregacion = "e.ext_est_sexo_nom";
            } else {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_DESAGREGACION_NO_SOPORTADA);
                throw be;
            }
        }

        SQLQuery q = session.createSQLQuery("SELECT '' dato,x.desagregacion desagregacion, (case x.cantidad_total when 0 then 0 else ((x.cantidad_matriculados / x.cantidad_total) * 100 ) end)  AS cantidad"
                + " FROM "
                + "(SELECT e.desagregacion, SUM(e.cantidad_matriculados) cantidad_matriculados, SUM(e.cantidad_total) cantidad_total "
                + "FROM  "
                + "(SELECT z.desagregacion,z.cantidad_matriculados cantidad_matriculados,z.cantidad_total cantidad_total "
                + "FROM (SELECT "
                + "e.ext_est_edad_al_30_oct_mat edad, "
                + " " + colDesagregacion + " AS desagregacion, "
                + "Count(*)  cantidad_matriculados,"
                + "0 cantidad_total "
                + "FROM estadisticas.sg_ext_estudiantes e "
                + "INNER JOIN estadisticas.sg_extracciones ex "
                + "ON ( e.ext_cabezal_fk = ex.ext_pk ) "
                + "WHERE  (e.ext_est_sexo_nom = 'Masculino' or e.ext_est_sexo_nom = 'Femenino') "
                + "AND ex.ext_anio = :anio "
                + "AND ex.ext_nombre_fk = :nombrePk   "
                + "AND e.ext_est_nivel = :nivelPk and e.ext_est_grado = :gradoPk "
                + "AND e.ext_est_realizo_parvularia is true "
                + "GROUP  BY e.ext_est_edad_al_30_oct_mat, desagregacion "
                + "UNION ALL "
                + "SELECT "
                + "e.ext_est_edad_al_30_oct_mat edad,"
                + " " + colDesagregacion + " AS desagregacion,"
                + "0  cantidad_matriculados,"
                + "Count(*) cantidad_total "
                + "FROM estadisticas.sg_ext_estudiantes e "
                + "INNER JOIN estadisticas.sg_extracciones ex "
                + " ON ( e.ext_cabezal_fk = ex.ext_pk ) "
                + " WHERE  (e.ext_est_sexo_nom = 'Masculino' or e.ext_est_sexo_nom = 'Femenino') "
                + " AND ex.ext_anio = :anio "
                + " AND ex.ext_nombre_fk = :nombrePk "
                + "AND e.ext_est_nivel = :nivelPk and e.ext_est_grado = :gradoPk "
                + " GROUP  BY e.ext_est_edad_al_30_oct_mat, desagregacion "
                + " ) z "
                + " group by z.desagregacion,z.cantidad_matriculados,z.cantidad_total ) e group by e.desagregacion) "
                + "x group by  x.desagregacion,x.cantidad_matriculados,x.cantidad_total ");

        q.addScalar("dato", new StringType());
        q.addScalar("desagregacion", new StringType());
        q.addScalar("cantidad", new DoubleType());
        q.setParameter("anio", filtro.getAnio());
        q.setParameter("gradoPk", Constantes.NIVEL_EDUCACION_BASICA_PRIMER_GRADO_REGULAR_PK);
        q.setParameter("nivelPk", Constantes.NIVEL_EDUCACION_BASICA_PK);
        q.setParameter("nombrePk", filtro.getNombrePk());
        q.setResultTransformer(Transformers.aliasToBean(EstGenerica.class));
        return q.list();
    }

    //8 - M-01
    public List<EstGenerica> matriculaPorNivelEducativo(FiltroEstadisticas filtro) throws GeneralException {
        Session session = em.unwrap(Session.class);

        String colDesagregacion = "'-'";
        if (filtro.getDesagregacion() != null) {
            if (filtro.getDesagregacion().equals(EnumDesagregacion.DEPARTAMENTO)) {
                colDesagregacion = "ext_est_sede_dep_nom";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.ZONA)) {
                colDesagregacion = "ext_est_sede_zon_nom";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_OFI_PRI)) {
                colDesagregacion = "e.ext_est_sede_tipo";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_PUB_PRI)) {
                colDesagregacion = "e.ext_sed_sector";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SEXO)) {
                colDesagregacion = "ext_est_sexo_nom";
            } else {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_DESAGREGACION_NO_SOPORTADA);
                throw be;
            }
        }

        SQLQuery q = session.createSQLQuery("select"
                + " ext_est_nivel_nom as dato,"
                + " " + colDesagregacion + " as desagregacion,"
                + " count(*) as cantidad from estadisticas.sg_ext_estudiantes e"
                + " INNER JOIN estadisticas.sg_extracciones ex ON (e.ext_cabezal_fk = ex.ext_pk)"
                + " where ex.ext_anio = :anio and ex.ext_nombre_fk = :nombrePk"
                + " group by ext_est_nivel_nom, ext_est_nivel_orden, desagregacion"
                + " order by ext_est_nivel_orden");
        q.addScalar("dato", new StringType());
        q.addScalar("desagregacion", new StringType());
        q.addScalar("cantidad", new LongType());
        q.setParameter("anio", filtro.getAnio());
        q.setParameter("nombrePk", filtro.getNombrePk());
        q.setResultTransformer(Transformers.aliasToBean(EstGenerica.class));
        return q.list();
    }

    //9 - M-02
    public List<EstGenerica> distribucionPorcentualDeLaMatriculaPorNivelEducativo(FiltroEstadisticas filtro) throws GeneralException {
        Session session = em.unwrap(Session.class);

        String colDesagregacion = "";
        if (filtro.getDesagregacion() == null) {
            SQLQuery q = session.createSQLQuery(
                    "select e.ext_est_nivel_nom dato, '-' desagregacion, (count(*)*1.0/"
                    + "(select count(*) from estadisticas.sg_ext_estudiantes f JOIN estadisticas.sg_extracciones ff ON ( f.ext_cabezal_fk = ff.ext_pk ) WHERE  ff.ext_anio = :anio AND ff.ext_nombre_fk = :nombrePk)"
                    + ")*100 cantidad from estadisticas.sg_ext_estudiantes e "
                    + "	JOIN estadisticas.sg_extracciones ex ON ( e.ext_cabezal_fk = ex.ext_pk ) "
                    + "	WHERE  ex.ext_anio = :anio AND ex.ext_nombre_fk = :nombrePk "
                    + "	GROUP BY e.ext_est_nivel_nom, e.ext_est_nivel_orden "
                    + " ORDER BY e.ext_est_nivel_orden");

            q.addScalar("dato", new StringType());
            q.addScalar("desagregacion", new StringType());
            q.addScalar("cantidad", new DoubleType());
            q.setParameter("anio", filtro.getAnio());
            q.setParameter("nombrePk", filtro.getNombrePk());
            q.setResultTransformer(Transformers.aliasToBean(EstGenerica.class));
            return q.list();
        } else if (filtro.getDesagregacion().equals(EnumDesagregacion.DEPARTAMENTO)) {
            colDesagregacion = "ext_est_sede_dep_nom";
        } else if (filtro.getDesagregacion().equals(EnumDesagregacion.ZONA)) {
            colDesagregacion = "ext_est_sede_zon_nom";
        } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_OFI_PRI)) {
            colDesagregacion = "ext_est_sede_tipo";
        } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_PUB_PRI)) {
            colDesagregacion = "ext_sed_sector";
        } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SEXO)) {
            colDesagregacion = "ext_est_sexo_nom";
        } else {
            BusinessException be = new BusinessException();
            be.addError(Errores.ERROR_DESAGREGACION_NO_SOPORTADA);
            throw be;
        }

        SQLQuery q = session.createSQLQuery("SELECT e.ext_est_nivel_nom dato, e." + colDesagregacion + " desagregacion, (count(*)*1.0/totales.cantidad)*100 as cantidad "
                + " from estadisticas.sg_ext_estudiantes e "
                + " JOIN estadisticas.sg_extracciones ex ON ( e.ext_cabezal_fk = ex.ext_pk ) "
                + "join (select e.ext_est_nivel_nom, count(*) as cantidad from estadisticas.sg_ext_estudiantes e "
                + "	JOIN estadisticas.sg_extracciones ex ON ( e.ext_cabezal_fk = ex.ext_pk ) "
                + "	WHERE  ex.ext_anio = :anio AND ex.ext_nombre_fk = :nombrePk "
                + "	GROUP BY e.ext_est_nivel_nom "
                + " ) as totales on e.ext_est_nivel_nom=totales.ext_est_nivel_nom"
                + " WHERE ex.ext_anio = :anio "
                + " AND ex.ext_nombre_fk = :nombrePk "
                + " GROUP  BY e.ext_est_nivel_nom, e." + colDesagregacion + ", totales.cantidad");

        q.addScalar("dato", new StringType());
        q.addScalar("desagregacion", new StringType());
        q.addScalar("cantidad", new DoubleType());
        q.setParameter("anio", filtro.getAnio());
        q.setParameter("nombrePk", filtro.getNombrePk());
        q.setResultTransformer(Transformers.aliasToBean(EstGenerica.class));
        return q.list();
    }

    //10 - M-03
    public List<EstGenerica> tasaBrutaDeMatriculaPorNivelEducativo(FiltroEstadisticas filtro) throws GeneralException {
        Session session = em.unwrap(Session.class);

        String colDesagregacion = "'-'";
        String colPoblacionDesagregacion = "'-'";
        if (filtro.getDesagregacion() != null) {
            if (!filtro.getDesagregacion().equals(EnumDesagregacion.SEXO)) {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_DESAGREGACION_NO_SOPORTADA);
                throw be;
            } else {
                colDesagregacion = "e.ext_est_sexo_nom";
                colPoblacionDesagregacion = "pob.pro_sexo_nom";
            }
        }

        SQLQuery q = session.createSQLQuery("select dato, desagregacion, (cantidad*1.0 / sum(pro_poblacion) * 100) as cantidad from"
                + " (select"
                + " e.ext_est_nivel_nom as dato,"
                + " " + colDesagregacion + "  as desagregacion,"
                + " e.ext_est_nivel_orden as orden,"
                + " MIN(e.ext_est_grado_edad_min) as edad_minima,"
                + " MAX(e.ext_est_grado_edad_min) as edad_maxima,"
                + " count(*) as cantidad from estadisticas.sg_ext_estudiantes e"
                + " INNER JOIN estadisticas.sg_extracciones ex ON (e.ext_cabezal_fk = ex.ext_pk)"
                + " where ex.ext_anio = :anio and ex.ext_nombre_fk = :nombrePk "
                + " group by ext_est_nivel_nom, desagregacion, orden"
                + " having MIN(e.ext_est_grado_edad_min) >= 0 and MAX(e.ext_est_grado_edad_min) > 0"
                + " order by orden) h"
                + " INNER JOIN estadisticas.sg_proyecciones_poblacion pob ON (" + colPoblacionDesagregacion + " = desagregacion and pob.pro_edad >= edad_minima and pob.pro_edad <= edad_maxima and pob.pro_anio = :anio)"
                + " group by dato, orden, desagregacion, cantidad"
                + " order by orden");

        q.addScalar("dato", new StringType());
        q.addScalar("desagregacion", new StringType());
        q.addScalar("cantidad", new DoubleType());
        q.setParameter("anio", filtro.getAnio());
        q.setParameter("nombrePk", filtro.getNombrePk());
        q.setResultTransformer(Transformers.aliasToBean(EstGenerica.class));
        return q.list();
    }

    //11 - M-04
    public List<EstGenerica> tasaNetaDeMatriculaPorNivelEducativo(FiltroEstadisticas filtro) throws GeneralException {
        Session session = em.unwrap(Session.class);

        String colDesagregacion = "'-'";
        String colPoblacionDesagregacion = "'-'";
        if (filtro.getDesagregacion() != null) {
            if (!filtro.getDesagregacion().equals(EnumDesagregacion.SEXO)) {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_DESAGREGACION_NO_SOPORTADA);
                throw be;
            } else {
                colDesagregacion = "e.ext_est_sexo_nom";
                colPoblacionDesagregacion = "pob.pro_sexo_nom";
            }
        }

        SQLQuery q = session.createSQLQuery("select dato, desagregacion, (cantidad*1.0 / sum(pro_poblacion) * 100) as cantidad from"
                + " (select h.dato, h.desagregacion, count(*) as cantidad, "
                + " MIN(nivel_edad_minima) as nivel_edad_minima, "
                + " MAX(nivel_edad_maxima) as nivel_edad_maxima, orden from"
                + " (select"
                + " e.ext_est_nivel_nom as dato, " + colDesagregacion + " as desagregacion, e.ext_est_nivel_orden orden,  e.ext_est_edad_al_30_oct_mat ext_est_edad_al_30_oct_mat"
                + " from estadisticas.sg_ext_estudiantes e"
                + " INNER JOIN estadisticas.sg_extracciones ex ON (e.ext_cabezal_fk = ex.ext_pk)"
                + " where ex.ext_anio = :anio and ex.ext_nombre_fk = :nombrePk) h"
                + " INNER JOIN"
                + " (select"
                + " e.ext_est_nivel_nom as dato,"
                + " MIN(e.ext_est_grado_edad_min) as nivel_edad_minima,"
                + " MAX(e.ext_est_grado_edad_min) as nivel_edad_maxima"
                + " from estadisticas.sg_ext_estudiantes e"
                + " INNER JOIN estadisticas.sg_extracciones ex ON (e.ext_cabezal_fk = ex.ext_pk)"
                + " where ex.ext_anio = :anio and ex.ext_nombre_fk = :nombrePk "
                + " group by ext_est_nivel_nom"
                + " having MIN(e.ext_est_grado_edad_min) >= 0 and MAX(e.ext_est_grado_edad_max) > 0) z"
                + " ON (h.dato = z.dato)"
                + " where h.ext_est_edad_al_30_oct_mat >= z.nivel_edad_minima and h.ext_est_edad_al_30_oct_mat <= z.nivel_edad_maxima"
                + " group by h.dato, h.desagregacion, h.orden order by h.orden) estudiantes_en_edad"
                + " INNER JOIN estadisticas.sg_proyecciones_poblacion pob ON (" + colPoblacionDesagregacion + " = estudiantes_en_edad.desagregacion and pob.pro_edad >= nivel_edad_minima and pob.pro_edad <= nivel_edad_maxima and pob.pro_anio = :anio)"
                + " group by dato, desagregacion, cantidad, orden"
                + " order by orden");

        q.addScalar("dato", new StringType());
        q.addScalar("desagregacion", new StringType());
        q.addScalar("cantidad", new DoubleType());
        q.setParameter("anio", filtro.getAnio());
        q.setParameter("nombrePk", filtro.getNombrePk());
        q.setResultTransformer(Transformers.aliasToBean(EstGenerica.class));
        return q.list();
    }

    //12 - M-05
    public List<EstGenerica> tasaEspecificaMatriculaPorGrado(FiltroEstadisticas filtro) throws GeneralException {
        Session session = em.unwrap(Session.class);

        String colDesagregacion = "'-'";
        String colPoblacionDesagregacion = "'-'";
        if (filtro.getDesagregacion() != null) {
            if (!filtro.getDesagregacion().equals(EnumDesagregacion.SEXO)) {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_DESAGREGACION_NO_SOPORTADA);
                throw be;
            } else {
                colDesagregacion = "e.ext_est_sexo_nom";
                colPoblacionDesagregacion = "pob.pro_sexo_nom";
            }
        }

        SQLQuery q = session.createSQLQuery("select dato, desagregacion, (cantidad*1.0 / sum(pro_poblacion) * 100) as cantidad from"
                + " (select"
                + "     e.ext_est_grado_nom as dato,"
                + "     " + colDesagregacion + "  as desagregacion,"
                + "     e.ext_est_nivel_nom, ext_est_ciclo_nom, ext_est_modalidad_educativa_nom, ext_est_modalidad_atencion_nom, ext_est_grado_nom, "
                + "     e.ext_est_nivel_orden AS orden_nivel, "
                + "     e.ext_est_ciclo_orden AS orden_ciclo, "
                + "     e.ext_est_mod_orden   AS orden_modalidad, "
                + "     e.ext_est_grado_orden AS orden_grado,"
                + "     MIN(e.ext_est_grado_edad_min) as edad_minima,"
                + "     count(*) as cantidad "
                + " from estadisticas.sg_ext_estudiantes e"
                + " INNER JOIN estadisticas.sg_extracciones ex ON (e.ext_cabezal_fk = ex.ext_pk)"
                + " where ex.ext_anio = :anio and ex.ext_nombre_fk = :nombrePk "
                + " and e.ext_est_edad_al_30_oct_mat = e.ext_est_grado_edad_min"
                + " group by "
                + "     e.ext_est_nivel_nom, "
                + "     e.ext_est_ciclo_nom, "
                + "     e.ext_est_modalidad_educativa_nom, "
                + "     e.ext_est_modalidad_atencion_nom, "
                + "     e.ext_est_grado_nom, "
                + "     desagregacion, "
                + "     orden_nivel, "
                + "     orden_ciclo, "
                + "     orden_modalidad, "
                + "     orden_grado"
                + " having MIN(e.ext_est_grado_edad_min) >= 0"
                + " ) h"
                + " INNER JOIN estadisticas.sg_proyecciones_poblacion pob ON (" + colPoblacionDesagregacion + " = desagregacion and pob.pro_edad = edad_minima and pob.pro_anio = :anio)"
                + " group by "
                + "     dato,"
                + "     ext_est_nivel_nom, "
                + "     ext_est_ciclo_nom, "
                + "     ext_est_modalidad_educativa_nom, "
                + "     ext_est_modalidad_atencion_nom, "
                + "     ext_est_grado_nom, "
                + "     desagregacion,"
                + "     cantidad, "
                + "	orden_nivel, "
                + "	orden_ciclo, "
                + "	orden_modalidad, "
                + "	orden_grado"
                + " ORDER BY"
                + " 	orden_nivel asc, "
                + "	orden_ciclo asc, "
                + "	orden_modalidad asc, "
                + "	orden_grado asc");

        q.addScalar("dato", new StringType());
        q.addScalar("desagregacion", new StringType());
        q.addScalar("cantidad", new DoubleType());
        q.setParameter("anio", filtro.getAnio());
        q.setParameter("nombrePk", filtro.getNombrePk());
        q.setResultTransformer(Transformers.aliasToBean(EstGenerica.class));
        return q.list();
    }

    //14 T-01
    public List<EstGenerica> porcentajeDeRepetidores(FiltroEstadisticas filtro) {
        Session session = em.unwrap(Session.class);

        String colDesagregacion = "'-'";
        if (filtro.getDesagregacion() != null) {
            if (filtro.getDesagregacion().equals(EnumDesagregacion.DEPARTAMENTO)) {
                colDesagregacion = "e.ext_est_sede_dep_nom";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.ZONA)) {
                colDesagregacion = "e.ext_est_sede_zon_nom";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_OFI_PRI)) {
                colDesagregacion = "e.ext_est_sede_tipo";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_PUB_PRI)) {
                colDesagregacion = "e.ext_sed_sector";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SEXO)) {
                colDesagregacion = "e.ext_est_sexo_nom";
            } else {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_DESAGREGACION_NO_SOPORTADA);
                throw be;
            }
        }

        SQLQuery q = session.createSQLQuery("Select dato, desagregacion, (case sum2 when 0 then 0 else (sum1 / sum2 * 100) end) AS cantidad from ("
                + " Select dato, desagregacion, sum(a) as sum1, sum(b) as sum2 from ("
                + " SELECT "
                + " e.ext_est_grado_nom as dato,"
                + " e.ext_est_nivel_nom, "
                + " e.ext_est_ciclo_nom, "
                + " e.ext_est_modalidad_educativa_nom, "
                + " e.ext_est_modalidad_atencion_nom, "
                + " e.ext_est_grado_nom, "
                + " " + colDesagregacion + " as desagregacion,"
                + "     e.ext_est_nivel_orden AS orden_nivel, "
                + "     e.ext_est_ciclo_orden AS orden_ciclo, "
                + "     e.ext_est_mod_orden   AS orden_modalidad, "
                + "     e.ext_est_grado_orden AS orden_grado,"
                + " count(*) a, 0 b from estadisticas.sg_ext_estudiantes e"
                + " INNER JOIN estadisticas.sg_extracciones ex ON (e.ext_cabezal_fk = ex.ext_pk)"
                + " where"
                + " e.ext_est_repetidor"
                + " and ex.ext_anio = :anio and ex.ext_nombre_fk = :nombrePk"
                + " group by e.ext_est_nivel_nom, e.ext_est_ciclo_nom, e.ext_est_modalidad_educativa_nom, e.ext_est_modalidad_atencion_nom, e.ext_est_grado_nom, desagregacion, orden_nivel, orden_ciclo, orden_modalidad, orden_grado"
                + " UNION ALL"
                + " SELECT "
                + " e.ext_est_grado_nom as dato,"
                + " e.ext_est_nivel_nom, "
                + " e.ext_est_ciclo_nom, "
                + " e.ext_est_modalidad_educativa_nom, "
                + " e.ext_est_modalidad_atencion_nom, "
                + " e.ext_est_grado_nom, "
                + " " + colDesagregacion + "  as desagregacion,"
                + " e.ext_est_nivel_orden AS orden_nivel, "
                + " e.ext_est_ciclo_orden AS orden_ciclo, "
                + " e.ext_est_mod_orden   AS orden_modalidad, "
                + " e.ext_est_grado_orden AS orden_grado,"
                + " 0 a, count(*) b from estadisticas.sg_ext_estudiantes e"
                + " INNER JOIN estadisticas.sg_extracciones ex ON (e.ext_cabezal_fk = ex.ext_pk)"
                + " where"
                + " ex.ext_anio = :anio and ex.ext_nombre_fk = :nombrePk"
                + " group by e.ext_est_nivel_nom, e.ext_est_ciclo_nom, e.ext_est_modalidad_educativa_nom, e.ext_est_modalidad_atencion_nom, e.ext_est_grado_nom, desagregacion, orden_nivel, orden_ciclo, orden_modalidad, orden_grado"
                + " ) z group by dato, ext_est_nivel_nom, ext_est_ciclo_nom, ext_est_modalidad_educativa_nom, ext_est_modalidad_atencion_nom, ext_est_grado_nom,  desagregacion, orden_nivel, orden_ciclo, orden_modalidad, orden_grado "
                + "order by orden_nivel, orden_ciclo, orden_modalidad, orden_grado) h");
        q.addScalar("dato", new StringType());
        q.addScalar("desagregacion", new StringType());
        q.addScalar("cantidad", new DoubleType());
        q.setParameter("anio", filtro.getAnio());
        q.setParameter("nombrePk", filtro.getNombrePk());
        q.setResultTransformer(Transformers.aliasToBean(EstGenerica.class));
        return q.list();
    }

    //15 T-02
    //IMPACTA EN 21(T-07)
    public List<EstGenerica> tasaDeRepeticion(FiltroEstadisticas filtro) {
        Session session = em.unwrap(Session.class);

        String colDesagregacion = "'-'";
        if (filtro.getDesagregacion() != null) {
            if (filtro.getDesagregacion().equals(EnumDesagregacion.DEPARTAMENTO)) {
                colDesagregacion = "e.ext_est_sede_dep_nom";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.ZONA)) {
                colDesagregacion = "e.ext_est_sede_zon_nom";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_OFI_PRI)) {
                colDesagregacion = "e.ext_est_sede_tipo";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_PUB_PRI)) {
                colDesagregacion = "e.ext_sed_sector";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SEXO)) {
                colDesagregacion = "e.ext_est_sexo_nom";
            } else {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_DESAGREGACION_NO_SOPORTADA);
                throw be;
            }
        }

        SQLQuery q = session.createSQLQuery("Select nombre_grado as dato, desagregacion, (case sum2 when 0 then 0 else (sum1 / sum2 * 100) end) AS cantidad from ("
                + " Select dato, nombre_grado, desagregacion, sum(a) as sum1, sum(b) as sum2 from ("
                + " SELECT ext_est_grado_nom as nombre_grado,"
                + " CONCAT(e.ext_est_nivel_nom, ' - ',"
                + " e.ext_est_ciclo_nom, ' - ',"
                + " e.ext_est_modalidad_educativa_nom, ' - ',"
                + " e.ext_est_modalidad_atencion_nom, ' - ',"
                + " e.ext_est_grado_nom) as dato, " + colDesagregacion + " as desagregacion,"
                + " e.ext_est_nivel_orden AS orden_nivel, "
                + " e.ext_est_ciclo_orden AS orden_ciclo, "
                + " e.ext_est_mod_orden   AS orden_modalidad, "
                + " e.ext_est_grado_orden AS orden_grado,"
                + " count(*) a, 0 b from estadisticas.sg_ext_estudiantes e"
                + " INNER JOIN estadisticas.sg_extracciones ex ON (e.ext_cabezal_fk = ex.ext_pk)"
                + " where"
                + " e.ext_est_repetidor"
                + " and ex.ext_anio = :anio and ex.ext_nombre_fk = :nombrePk"
                + " group by e.ext_est_nivel_nom, e.ext_est_ciclo_nom, e.ext_est_modalidad_educativa_nom, e.ext_est_modalidad_atencion_nom, e.ext_est_grado_nom, desagregacion, orden_nivel, orden_ciclo, orden_modalidad, orden_grado"
                + " UNION ALL"
                + " SELECT  ext_est_grado_nom as nombre_grado,"
                + " CONCAT(e.ext_est_nivel_nom, ' - ',"
                + " e.ext_est_ciclo_nom, ' - ',"
                + " e.ext_est_modalidad_educativa_nom, ' - ',"
                + " e.ext_est_modalidad_atencion_nom, ' - ',"
                + " e.ext_est_grado_nom) as dato, " + colDesagregacion + "  as desagregacion,"
                + " e.ext_est_nivel_orden AS orden_nivel, "
                + " e.ext_est_ciclo_orden AS orden_ciclo, "
                + " e.ext_est_mod_orden   AS orden_modalidad, "
                + " e.ext_est_grado_orden AS orden_grado,"
                + " 0 a, count(*) b from estadisticas.sg_ext_estudiantes e"
                + " INNER JOIN estadisticas.sg_extracciones ex ON (e.ext_cabezal_fk = ex.ext_pk)"
                + " where"
                + " ex.ext_anio = :anio_c and ex.ext_nombre_fk = :nombrePk_c"
                + " group by e.ext_est_nivel_nom, e.ext_est_ciclo_nom, e.ext_est_modalidad_educativa_nom, e.ext_est_modalidad_atencion_nom, e.ext_est_grado_nom, desagregacion, orden_nivel, orden_ciclo, orden_modalidad, orden_grado "
                + " ) z group by dato, nombre_grado, desagregacion, orden_nivel, orden_ciclo, orden_modalidad, orden_grado order by orden_nivel, orden_ciclo, orden_modalidad, orden_grado) h");
        q.addScalar("dato", new StringType());
        q.addScalar("desagregacion", new StringType());
        q.addScalar("cantidad", new DoubleType());
        q.setParameter("anio", filtro.getAnio());
        q.setParameter("nombrePk", filtro.getNombrePk());
        q.setParameter("anio_c", filtro.getAnioComparacion());
        q.setParameter("nombrePk_c", filtro.getNombrePkComparacion());
        q.setResultTransformer(Transformers.aliasToBean(EstGenerica.class));
        return q.list();
    }

    //18 T-04
    //Para obtener grados, se utiliza el orden que tienen los niveles, ciclos, modalidades y grados al momento de generar la estadística.
    //Para joinear ciclos actuales con anteriores, se utiliza el orden que tienen al momento de la extracción.
    public List<EstGenerica> tasaTransicionPorCiclo(FiltroEstadisticas filtro) {

        String colDesagregacion = "'-'";
        if (filtro.getDesagregacion() != null) {
            if (filtro.getDesagregacion().equals(EnumDesagregacion.DEPARTAMENTO)) {
                colDesagregacion = "e.ext_est_sede_dep_nom";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.ZONA)) {
                colDesagregacion = "e.ext_est_sede_zon_nom";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_OFI_PRI)) {
                colDesagregacion = "e.ext_est_sede_tipo";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_PUB_PRI)) {
                colDesagregacion = "e.ext_sed_sector";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SEXO)) {
                colDesagregacion = "e.ext_est_sexo_nom";
            } else {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_DESAGREGACION_NO_SOPORTADA);
                throw be;
            }
        }

        Session session = em.unwrap(Session.class);
        SQLQuery q = session.createSQLQuery("SELECT CONCAT(h.ext_est_nivel_nom, ' - ', h.ext_est_ciclo_nom) as dato, h.desagregacion, (case cantidad_ultimo_grado_ciclo_anterior when 0 then 0 else (cantidad_primer_grado*1.0 / cantidad_ultimo_grado_ciclo_anterior * 100) end) as cantidad FROM"
                + ""
                //Se obtiene la cantidad de estudiantes en el primer grado de cada ciclo
                + " (SELECT e.ext_est_org_curricular, e.ext_est_nivel, e.ext_est_nivel_nom, e.ext_est_nivel_orden as orden_nivel, e.ext_est_ciclo, e.ext_est_ciclo_nom, e.ext_est_ciclo_orden as orden_ciclo, " + colDesagregacion + " as desagregacion, count(*) as cantidad_primer_grado FROM  estadisticas.sg_ext_estudiantes e "
                + " INNER JOIN estadisticas.sg_extracciones ex ON ( e.ext_cabezal_fk = ex.ext_pk )"
                + " WHERE ex.ext_anio = :anio AND ex.ext_nombre_fk = :nombrePk AND (e.ext_est_repetidor is null or not e.ext_est_repetidor)  AND e.ext_est_grado IN"
                + " (SELECT gra_pk from centros_educativos.primeros_grados_por_modalidad)"
                + " group by e.ext_est_org_curricular, e.ext_est_nivel, e.ext_est_nivel_nom, orden_nivel, e.ext_est_ciclo, e.ext_est_ciclo_nom, orden_ciclo, desagregacion"
                + " order by orden_nivel) h"
                + ""
                + " INNER JOIN"
                + ""
                //Se obtiene la cantidad de estudiantes en el último grado de cada ciclo
                + " (SELECT e.ext_est_org_curricular, e.ext_est_nivel, e.ext_est_nivel_nom, e.ext_est_nivel_orden as orden_nivel, e.ext_est_ciclo, e.ext_est_ciclo_nom, e.ext_est_ciclo_orden as orden_ciclo, " + colDesagregacion + " as desagregacion, count(*) as cantidad_ultimo_grado_ciclo_anterior FROM  estadisticas.sg_ext_estudiantes e "
                + " INNER JOIN estadisticas.sg_extracciones ex ON ( e.ext_cabezal_fk = ex.ext_pk )"
                + " WHERE ex.ext_anio = :anio_c AND ex.ext_nombre_fk = :nombrePk_c AND (e.ext_est_repetidor is null or not e.ext_est_repetidor)  AND e.ext_est_grado IN"
                + " (SELECT gra_pk from centros_educativos.ultimos_grados_por_modalidad)"
                + " group by e.ext_est_org_curricular, e.ext_est_nivel, e.ext_est_nivel_nom, orden_nivel, e.ext_est_ciclo, e.ext_est_ciclo_nom, orden_ciclo, desagregacion"
                + " order by orden_nivel) z"
                + ""
                + " ON (h.ext_est_org_curricular = z.ext_est_org_curricular AND "
                + "	("
                //Se hace join de ciclo actual, con ciclo anterior. Hay dos alernativas:
                //1. Comparten el mismo nivel. Entonces ciclo actual tiene que ser el mayor inmediato a ciclo anterior.
                //2. El nivel de ciclo actual es mayor. Entonces el ciclo actual tiene que ser el menor, del nivel mayor inmediato a ciclo anterior. Ciclo anterior debe ser el mayor de los ciclos del nivel.
                + "		(h.ext_est_nivel = z.ext_est_nivel AND h.ext_est_ciclo = (select cic_pk from centros_educativos.sg_ciclos where cic_orden > z.orden_ciclo and cic_nivel = z.ext_est_nivel order by cic_orden asc limit 1))"
                + "		OR"
                + "		(h.ext_est_nivel = (select niv_pk from centros_educativos.sg_niveles where niv_orden > z.orden_nivel order by niv_orden asc limit 1)"
                + "		AND h.ext_est_ciclo = (select MIN(cic_pk) from centros_educativos.sg_ciclos where cic_nivel = h.ext_est_nivel and z.orden_ciclo = (select MAX(cic_orden) from centros_educativos.sg_ciclos where cic_nivel = z.ext_est_nivel))"
                + "		)"
                + "   	))");
        q.addScalar("dato", new StringType());
        q.addScalar("desagregacion", new StringType());
        q.addScalar("cantidad", new DoubleType());
        q.setParameter("anio", filtro.getAnio());
        q.setParameter("nombrePk", filtro.getNombrePk());
        q.setParameter("anio_c", filtro.getAnioComparacion());
        q.setParameter("nombrePk_c", filtro.getNombrePkComparacion());
        q.setResultTransformer(Transformers.aliasToBean(EstGenerica.class));
        return q.list();
    }

    //18 T-05
    //Para obtener grados, se utiliza el orden que tienen los niveles, ciclos, modalidades y grados al momento de generar la estadística.
    //Para joinear niveles actuales con anteriores, se utiliza el orden que tienen al momento de la extracción.
    public List<EstGenerica> tasaTransicionPorNivel(FiltroEstadisticas filtro) {

        String colDesagregacion = "'-'";
        if (filtro.getDesagregacion() != null) {
            if (filtro.getDesagregacion().equals(EnumDesagregacion.DEPARTAMENTO)) {
                colDesagregacion = "e.ext_est_sede_dep_nom";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.ZONA)) {
                colDesagregacion = "e.ext_est_sede_zon_nom";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_OFI_PRI)) {
                colDesagregacion = "e.ext_est_sede_tipo";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_PUB_PRI)) {
                colDesagregacion = "e.ext_sed_sector";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SEXO)) {
                colDesagregacion = "e.ext_est_sexo_nom";
            } else {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_DESAGREGACION_NO_SOPORTADA);
                throw be;
            }
        }

        Session session = em.unwrap(Session.class);
        SQLQuery q = session.createSQLQuery("SELECT h.ext_est_nivel_nom as dato, h.desagregacion, (case cantidad_ultimo_grado_nivel_anterior when 0 then 0 else (cantidad_primer_grado*1.0 / cantidad_ultimo_grado_nivel_anterior * 100) end) as cantidad  from"
                + ""
                //Se obtiene la cantidad de estudiantes en el primer grado de cada nivel
                + " (SELECT e.ext_est_org_curricular, e.ext_est_nivel, e.ext_est_nivel_nom, e.ext_est_nivel_orden as orden, " + colDesagregacion + " as desagregacion, count(*) as cantidad_primer_grado FROM  estadisticas.sg_ext_estudiantes e "
                + " INNER JOIN estadisticas.sg_extracciones ex ON ( e.ext_cabezal_fk = ex.ext_pk )"
                + " WHERE ex.ext_anio = :anio AND ex.ext_nombre_fk = :nombrePk AND (e.ext_est_repetidor is null or not e.ext_est_repetidor)  AND e.ext_est_grado IN"
                + " (SELECT gra_pk from centros_educativos.primeros_grados_por_nivel)"
                + " group by e.ext_est_org_curricular, e.ext_est_nivel, e.ext_est_nivel_nom, orden, desagregacion"
                + " order by orden) h"
                + " "
                + " INNER JOIN"
                + " "
                //Se obtiene la cantidad de estudiantes en el último grado de cada nivel
                + " (SELECT e.ext_est_org_curricular, e.ext_est_nivel, e.ext_est_nivel_nom, e.ext_est_nivel_orden as orden, " + colDesagregacion + " as desagregacion, count(*) as cantidad_ultimo_grado_nivel_anterior FROM  estadisticas.sg_ext_estudiantes e "
                + " INNER JOIN estadisticas.sg_extracciones ex ON ( e.ext_cabezal_fk = ex.ext_pk )"
                + " WHERE ex.ext_anio = :anio_c AND ex.ext_nombre_fk = :nombrePk_c AND e.ext_est_grado IN"
                + " (SELECT gra_pk from centros_educativos.ultimos_grados_por_nivel)"
                + " group by e.ext_est_org_curricular, e.ext_est_nivel, e.ext_est_nivel_nom, orden, desagregacion"
                + " order by orden) z"
                //Se hace join de nivel actual, con nivel anterior.
                //1. El nivel actual tiene que ser el mayor inmediato al nivel anterior.
                + " ON (h.ext_est_org_curricular = z.ext_est_org_curricular AND h.desagregacion = z.desagregacion AND h.ext_est_nivel = (select niv_pk from centros_educativos.sg_niveles where niv_orden > z.orden order by niv_orden asc limit 1))"
                + "");
        q.addScalar("dato", new StringType());
        q.addScalar("desagregacion", new StringType());
        q.addScalar("cantidad", new DoubleType());
        q.setParameter("anio", filtro.getAnio());
        q.setParameter("nombrePk", filtro.getNombrePk());
        q.setParameter("anio_c", filtro.getAnioComparacion());
        q.setParameter("nombrePk_c", filtro.getNombrePkComparacion());
        q.setResultTransformer(Transformers.aliasToBean(EstGenerica.class));
        return q.list();
    }

    //21 T-07
    //Restar la suma de las tasas de promoción y repetición de un grado determinado de 100
    //IMPACTA EN: 15(T-02) y 31(F-03)
    public List<EstGenerica> tasaDesercion(FiltroEstadisticas filtro) {

        String colDesagregacion = "'-'";
        if (filtro.getDesagregacion() != null) {
            if (filtro.getDesagregacion().equals(EnumDesagregacion.DEPARTAMENTO)) {
                colDesagregacion = "e.ext_est_sede_dep_nom";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.ZONA)) {
                colDesagregacion = "e.ext_est_sede_zon_nom";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_OFI_PRI)) {
                colDesagregacion = "e.ext_est_sede_tipo";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_PUB_PRI)) {
                colDesagregacion = "e.ext_sed_sector";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SEXO)) {
                colDesagregacion = "e.ext_est_sexo_nom";
            } else {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_DESAGREGACION_NO_SOPORTADA);
                throw be;
            }
        }

        Session session = em.unwrap(Session.class);
        SQLQuery q = session.createSQLQuery("select j1.nombre_grado as dato, j1.desagregacion, (100 - (j1.cantidad + j2.cantidad)) as cantidad from "
                + ""
                + " (Select dato, nombre_grado, desagregacion, (case sum2 when 0 then 0 else (sum1 / sum2 * 100) end) as cantidad from ("
                + "                Select dato, nombre_grado, desagregacion,  sum(a) as sum1, sum(b) as sum2 from ("
                + "                SELECT CONCAT(e.ext_est_nivel_nom, ' - ',"
                + "                e.ext_est_ciclo_nom, ' - ',"
                + "                e.ext_est_modalidad_educativa_nom, ' - ',"
                + "                e.ext_est_modalidad_atencion_nom, ' - ',"
                + "                e.ext_est_grado_nom) as dato,  " + colDesagregacion + " as desagregacion,"
                + "             e.ext_est_nivel_orden AS orden_nivel, "
                + "             e.ext_est_ciclo_orden AS orden_ciclo, "
                + "             e.ext_est_mod_orden   AS orden_modalidad, "
                + "             e.ext_est_grado_orden AS orden_grado,"
                + "             e.ext_est_grado_nom AS nombre_grado,"
                + "             count(*) a, 0 b from estadisticas.sg_ext_estudiantes e"
                + "                INNER JOIN estadisticas.sg_extracciones ex ON (e.ext_cabezal_fk = ex.ext_pk)"
                + "                where"
                + "                (e.ext_est_repetidor is null or not e.ext_est_repetidor)"
                + "                and ex.ext_anio = :anio and ex.ext_nombre_fk = :nombrePk"
                + "                group by e.ext_est_nivel_nom, e.ext_est_ciclo_nom, e.ext_est_modalidad_educativa_nom, e.ext_est_modalidad_atencion_nom, e.ext_est_grado_nom, desagregacion,"
                + " orden_nivel, orden_ciclo, orden_modalidad, orden_grado"
                + "                UNION ALL"
                + "                SELECT CONCAT(e.ext_est_nivel_nom, ' - ',"
                + "                e.ext_est_ciclo_nom, ' - ',"
                + "                e.ext_est_modalidad_educativa_nom, ' - ',"
                + "                e.ext_est_modalidad_atencion_nom, ' - ',"
                + "                e.ext_est_grado_nom) as dato,  " + colDesagregacion + "  as desagregacion,"
                + "             e.ext_est_nivel_orden AS orden_nivel, "
                + "             e.ext_est_ciclo_orden AS orden_ciclo, "
                + "             e.ext_est_mod_orden   AS orden_modalidad, "
                + "             e.ext_est_grado_orden AS orden_grado,"
                + "             e.ext_est_grado_nom AS nombre_grado,"
                + "             0 a, count(*) b from estadisticas.sg_ext_estudiantes e"
                + "                INNER JOIN estadisticas.sg_extracciones ex ON (e.ext_cabezal_fk = ex.ext_pk)"
                + "                where"
                + "                ex.ext_anio = :anio_c and ex.ext_nombre_fk = :nombrePk_c"
                + "                group by e.ext_est_nivel_nom, e.ext_est_ciclo_nom, e.ext_est_modalidad_educativa_nom, e.ext_est_modalidad_atencion_nom, e.ext_est_grado_nom, desagregacion,"
                + "                orden_nivel, orden_ciclo, orden_modalidad, orden_grado"
                + "                ) z group by dato, nombre_grado, desagregacion, orden_nivel, orden_ciclo, orden_modalidad, orden_grado order by orden_nivel, orden_ciclo, orden_modalidad, orden_grado) h ) j1"
                + "				"
                + " INNER JOIN	"
                + ""
                + " (Select dato, nombre_grado, desagregacion, (case sum2 when 0 then 0 else (sum1 / sum2 * 100) end) as cantidad from ("
                + "                Select dato, nombre_grado, desagregacion, sum(a) as sum1, sum(b) as sum2 from ("
                + "                SELECT CONCAT(e.ext_est_nivel_nom, ' - ',"
                + "                e.ext_est_ciclo_nom, ' - ',"
                + "                e.ext_est_modalidad_educativa_nom, ' - ',"
                + "                e.ext_est_modalidad_atencion_nom, ' - ',"
                + "                e.ext_est_grado_nom) as dato,  " + colDesagregacion + " as desagregacion, "
                + "             e.ext_est_nivel_orden AS orden_nivel, "
                + "             e.ext_est_ciclo_orden AS orden_ciclo, "
                + "             e.ext_est_mod_orden   AS orden_modalidad, "
                + "             e.ext_est_grado_orden AS orden_grado,"
                + "             e.ext_est_grado_nom AS nombre_grado,"
                + "             count(*) a, 0 b from estadisticas.sg_ext_estudiantes e"
                + "                INNER JOIN estadisticas.sg_extracciones ex ON (e.ext_cabezal_fk = ex.ext_pk)"
                + "                where"
                + "                e.ext_est_repetidor"
                + "                and ex.ext_anio = :anio and ex.ext_nombre_fk = :nombrePk"
                + "                group by e.ext_est_nivel_nom, e.ext_est_ciclo_nom, e.ext_est_modalidad_educativa_nom, e.ext_est_modalidad_atencion_nom, e.ext_est_grado_nom, desagregacion, "
                + "                orden_nivel, orden_ciclo, orden_modalidad, orden_grado"
                + "                UNION ALL"
                + "                SELECT CONCAT(e.ext_est_nivel_nom, ' - ',"
                + "                e.ext_est_ciclo_nom, ' - ',"
                + "                e.ext_est_modalidad_educativa_nom, ' - ',"
                + "                e.ext_est_modalidad_atencion_nom, ' - ',"
                + "                e.ext_est_grado_nom) as dato,  " + colDesagregacion + " as desagregacion,"
                + "             e.ext_est_nivel_orden AS orden_nivel, "
                + "             e.ext_est_ciclo_orden AS orden_ciclo, "
                + "             e.ext_est_mod_orden   AS orden_modalidad, "
                + "             e.ext_est_grado_orden AS orden_grado,"
                + "             e.ext_est_grado_nom AS nombre_grado,"
                + "             0 a, count(*) b from estadisticas.sg_ext_estudiantes e"
                + "                INNER JOIN estadisticas.sg_extracciones ex ON (e.ext_cabezal_fk = ex.ext_pk)"
                + "                where"
                + "                ex.ext_anio = :anio_c and ex.ext_nombre_fk = :nombrePk_c"
                + "                group by e.ext_est_nivel_nom, e.ext_est_ciclo_nom, e.ext_est_modalidad_educativa_nom, e.ext_est_modalidad_atencion_nom, e.ext_est_grado_nom, desagregacion, "
                + "                orden_nivel, orden_ciclo, orden_modalidad, orden_grado"
                + "                ) z group by dato, nombre_grado, desagregacion, orden_nivel, orden_ciclo, orden_modalidad, orden_grado order by orden_nivel, orden_ciclo, orden_modalidad, orden_grado) h1	) j2	"
                + "				"
                + " ON (j1.dato = j2.dato and j1.desagregacion = j2.desagregacion)");
        q.addScalar("dato", new StringType());
        q.addScalar("desagregacion", new StringType());
        q.addScalar("cantidad", new DoubleType());
        q.setParameter("anio", filtro.getAnio());
        q.setParameter("nombrePk", filtro.getNombrePk());
        q.setParameter("anio_c", filtro.getAnioComparacion());
        q.setParameter("nombrePk_c", filtro.getNombrePkComparacion());
        q.setResultTransformer(Transformers.aliasToBean(EstGenerica.class));
        return q.list();
    }

    //22 - CC-01
    public List<EstGenerica> porcentajeDeTrabajadores(FiltroEstadisticas filtro) throws GeneralException {

        String colDesagregacion = "'-'";
        String groupBy = "";
        String orderByExterno = "";
        String selectAdicional = "";
        String groupByExterno = "";
        if (filtro.getDesagregacion() != null) {
            if (filtro.getDesagregacion().equals(EnumDesagregacion.DEPARTAMENTO)) {
                colDesagregacion = "e.ext_est_sede_dep_nom";
                groupBy = " GROUP  BY " + colDesagregacion;
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.ZONA)) {
                colDesagregacion = "e.ext_est_sede_zon_nom";
                groupBy = " GROUP  BY " + colDesagregacion;
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_OFI_PRI)) {
                colDesagregacion = "e.ext_est_sede_tipo";
                groupBy = " GROUP  BY " + colDesagregacion;
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_PUB_PRI)) {
                colDesagregacion = "e.ext_sed_sector";
                groupBy = " GROUP  BY " + colDesagregacion;
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SEXO)) {
                colDesagregacion = "e.ext_est_sexo_nom";
                groupBy = " GROUP  BY " + colDesagregacion;
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.NIVEL)) {
                colDesagregacion = "e.ext_est_nivel_nom";
                groupBy = " GROUP  BY " + colDesagregacion;
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.GRADO)) {
                colDesagregacion = " e.ext_est_grado_nom";
                groupBy = " GROUP BY e.ext_est_nivel_nom, e.ext_est_ciclo_nom, e.ext_est_modalidad_educativa_nom, e.ext_est_modalidad_atencion_nom, e.ext_est_grado_nom,"
                        + " orden_nivel, orden_ciclo, orden_modalidad, orden_grado";
                orderByExterno = " ORDER BY orden_nivel, orden_ciclo, orden_modalidad, orden_grado ";
                selectAdicional = " e.ext_est_nivel_orden AS orden_nivel, "
                        + "             e.ext_est_ciclo_orden AS orden_ciclo, "
                        + "             e.ext_est_mod_orden   AS orden_modalidad, "
                        + "             e.ext_est_grado_orden AS orden_grado,";
                groupByExterno = ", orden_nivel, orden_ciclo, orden_modalidad, orden_grado ";
            } else {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_DESAGREGACION_NO_SOPORTADA);
                throw be;
            }
        }

        Session session = em.unwrap(Session.class);
        SQLQuery q = session.createSQLQuery("SELECT dato, desagregacion, (case sum2 when 0 then 0 else (sum1 / sum2 * 100) end) AS cantidad"
                + " FROM   (SELECT dato,"
                + " desagregacion,"
                + " Sum(a) AS sum1,"
                + " Sum(b) AS sum2"
                + " FROM   (SELECT '-' AS dato,"
                + " " + colDesagregacion + " AS desagregacion,"
                + selectAdicional
                + " Count(*) a, 0 b"
                + " FROM   estadisticas.sg_ext_estudiantes e INNER JOIN estadisticas.sg_extracciones ex ON ( e.ext_cabezal_fk = ex.ext_pk )"
                + " WHERE  e.ext_est_trabaja"
                + " AND ex.ext_anio = :anio AND ex.ext_nombre_fk = :nombrePk"
                + " " + groupBy
                + " UNION ALL"
                + " SELECT '-' AS dato,"
                + " " + colDesagregacion + " AS desagregacion,"
                + selectAdicional
                + " 0 a, Count(*) b"
                + " FROM   estadisticas.sg_ext_estudiantes e INNER JOIN estadisticas.sg_extracciones ex ON ( e.ext_cabezal_fk = ex.ext_pk )"
                + " WHERE  ex.ext_anio = :anio AND ex.ext_nombre_fk = :nombrePk"
                + " " + groupBy + ") z"
                + " GROUP  BY dato, desagregacion" + groupByExterno + orderByExterno + ") h");
        q.addScalar("dato", new StringType());
        q.addScalar("desagregacion", new StringType());
        q.addScalar("cantidad", new DoubleType());
        q.setParameter("anio", filtro.getAnio());
        q.setParameter("nombrePk", filtro.getNombrePk());
        q.setResultTransformer(Transformers.aliasToBean(EstGenerica.class));
        return q.list();
    }

    //23 - CC-02
    public List<EstGenerica> distribucionPorcentualDeEstudiantesSegunActividadLaboral(FiltroEstadisticas filtro) throws GeneralException {

        String colDesagregacion = "'-'";
        if (filtro.getDesagregacion() != null) {
            if (filtro.getDesagregacion().equals(EnumDesagregacion.DEPARTAMENTO)) {
                colDesagregacion = "e.ext_est_sede_dep_nom";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.ZONA)) {
                colDesagregacion = "e.ext_est_sede_zon_nom";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_OFI_PRI)) {
                colDesagregacion = "e.ext_est_sede_tipo";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_PUB_PRI)) {
                colDesagregacion = "e.ext_sed_sector";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SEXO)) {
                colDesagregacion = "e.ext_est_sexo_nom";
            } else {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_DESAGREGACION_NO_SOPORTADA);
                throw be;
            }
        }

        Session session = em.unwrap(Session.class);
        SQLQuery q = session.createSQLQuery("select (case when e.ext_est_trabajo_nom is null then 'otros' else e.ext_est_trabajo_nom end) dato, " + colDesagregacion + " desagregacion, (count(*)*1.0/totales.cantidad)*100 as cantidad  "
                + " from estadisticas.sg_ext_estudiantes e "
                + "  JOIN estadisticas.sg_extracciones ex ON ( e.ext_cabezal_fk = ex.ext_pk ) "
                + " join (select " + colDesagregacion + " as desagregacion1, count(*) as cantidad from estadisticas.sg_ext_estudiantes e "
                + "	JOIN estadisticas.sg_extracciones ex ON ( e.ext_cabezal_fk = ex.ext_pk )  "
                + "	WHERE  ex.ext_anio = :anio AND ex.ext_nombre_fk = :nombrePk AND e.ext_est_trabaja "
                + "	GROUP BY desagregacion1 "
                + " ) as totales on (" + colDesagregacion + "=totales.desagregacion1 ) "
                + " WHERE ex.ext_anio = :anio "
                + " AND ex.ext_nombre_fk = :nombrePk "
                + " AND e.ext_est_trabaja "
                + " GROUP  BY e.ext_est_trabajo_nom, desagregacion, totales.cantidad "
                + " ORDER BY e.ext_est_trabajo_nom, desagregacion, totales.cantidad ");

        q.addScalar("dato", new StringType());
        q.addScalar("desagregacion", new StringType());
        q.addScalar("cantidad", new DoubleType());
        q.setParameter("anio", filtro.getAnio());
        q.setParameter("nombrePk", filtro.getNombrePk());
        q.setResultTransformer(Transformers.aliasToBean(EstGenerica.class));
        return q.list();
    }

    //24 - CC-03
    public List<EstGenerica> porcentajeConDiscapacidad(FiltroEstadisticas filtro) throws GeneralException {

        String colDesagregacion = "'-'";
        String groupBy = "";
        String orderByExterno = "";
        String selectAdicional = "";
        String groupByExterno = "";
        if (filtro.getDesagregacion() != null) {
            if (filtro.getDesagregacion().equals(EnumDesagregacion.DEPARTAMENTO)) {
                colDesagregacion = "e.ext_est_sede_dep_nom";
                groupBy = " GROUP  BY " + colDesagregacion;
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.ZONA)) {
                colDesagregacion = "e.ext_est_sede_zon_nom";
                groupBy = " GROUP  BY " + colDesagregacion;
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_OFI_PRI)) {
                colDesagregacion = "e.ext_est_sede_tipo";
                groupBy = " GROUP  BY " + colDesagregacion;
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_PUB_PRI)) {
                colDesagregacion = "e.ext_sed_sector";
                groupBy = " GROUP  BY " + colDesagregacion;
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SEXO)) {
                colDesagregacion = "e.ext_est_sexo_nom";
                groupBy = " GROUP  BY " + colDesagregacion;
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.NIVEL)) {
                colDesagregacion = "e.ext_est_nivel_nom";
                groupBy = " GROUP  BY " + colDesagregacion;
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.GRADO)) {
                colDesagregacion = " e.ext_est_grado_nom";
                groupBy = " GROUP BY e.ext_est_nivel_nom, e.ext_est_ciclo_nom, e.ext_est_modalidad_educativa_nom, e.ext_est_modalidad_atencion_nom, e.ext_est_grado_nom,"
                        + " orden_nivel, orden_ciclo, orden_modalidad, orden_grado";
                orderByExterno = " ORDER BY orden_nivel, orden_ciclo, orden_modalidad, orden_grado ";
                selectAdicional = " e.ext_est_nivel_orden AS orden_nivel, "
                        + "             e.ext_est_ciclo_orden AS orden_ciclo, "
                        + "             e.ext_est_mod_orden   AS orden_modalidad, "
                        + "             e.ext_est_grado_orden AS orden_grado,";
                groupByExterno = ", orden_nivel, orden_ciclo, orden_modalidad, orden_grado ";
            } else {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_DESAGREGACION_NO_SOPORTADA);
                throw be;
            }
        }

        Session session = em.unwrap(Session.class);
        SQLQuery q = session.createSQLQuery("SELECT dato, desagregacion, (case sum2 when 0 then 0 else (sum1 / sum2 * 100) end) AS cantidad"
                + " FROM   (SELECT dato,"
                + " desagregacion,"
                + " Sum(a) AS sum1,"
                + " Sum(b) AS sum2"
                + " FROM   (SELECT '-' AS dato,"
                + " " + colDesagregacion + " AS desagregacion,"
                + selectAdicional
                + " Count(*) a, 0 b"
                + " FROM   estadisticas.sg_ext_estudiantes e INNER JOIN estadisticas.sg_extracciones ex ON ( e.ext_cabezal_fk = ex.ext_pk )"
                + " WHERE  e.ext_est_tiene_discapacidad"
                + " AND ex.ext_anio = :anio AND ex.ext_nombre_fk = :nombrePk"
                + " " + groupBy
                + " UNION ALL"
                + " SELECT '-' AS dato,"
                + " " + colDesagregacion + " AS desagregacion,"
                + selectAdicional
                + " 0 a, Count(*) b"
                + " FROM   estadisticas.sg_ext_estudiantes e INNER JOIN estadisticas.sg_extracciones ex ON ( e.ext_cabezal_fk = ex.ext_pk )"
                + " WHERE  ex.ext_anio = :anio AND ex.ext_nombre_fk = :nombrePk"
                + " " + groupBy + ") z"
                + " GROUP  BY dato, desagregacion " + groupByExterno + orderByExterno + ") h");
        q.addScalar("dato", new StringType());
        q.addScalar("desagregacion", new StringType());
        q.addScalar("cantidad", new DoubleType());
        q.setParameter("anio", filtro.getAnio());
        q.setParameter("nombrePk", filtro.getNombrePk());
        q.setResultTransformer(Transformers.aliasToBean(EstGenerica.class));
        return q.list();
    }

    //25 - CC-04
    public List<EstGenerica> distribucionPorcentualEstudiantesConDiscapacidad(FiltroEstadisticas filtro) throws GeneralException {

        String colDesagregacion = "'-'";
        if (filtro.getDesagregacion() != null) {
            if (filtro.getDesagregacion().equals(EnumDesagregacion.DEPARTAMENTO)) {
                colDesagregacion = "e.ext_est_sede_dep_nom";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.ZONA)) {
                colDesagregacion = "e.ext_est_sede_zon_nom";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_OFI_PRI)) {
                colDesagregacion = "e.ext_est_sede_tipo";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_PUB_PRI)) {
                colDesagregacion = "e.ext_sed_sector";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SEXO)) {
                colDesagregacion = "e.ext_est_sexo_nom";
            } else {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_DESAGREGACION_NO_SOPORTADA);
                throw be;
            }
        }

        Session session = em.unwrap(Session.class);
        SQLQuery q = session.createSQLQuery("Select h.dis_dis_nombre as dato, h.desagregacion, (cantidad*1.0 / cantidad_con_discapacidad * 100) as cantidad from"
                + " (select dis_dis_fk, dis_dis_nombre, " + colDesagregacion + " as desagregacion, count(*) as cantidad FROM  estadisticas.sg_ext_estudiantes e "
                + " INNER JOIN estadisticas.sg_extracciones ex ON ( e.ext_cabezal_fk = ex.ext_pk )"
                + " INNER JOIN estadisticas.sg_ext_est_discapacidades dis ON (ex.ext_pk = dis.dis_ext_cabezal_fk AND dis.dis_est_fk=e.ext_est_pk) "
                + " AND ex.ext_anio = :anio AND ex.ext_nombre_fk = :nombrePk"
                + " group by dis_dis_fk, dis_dis_nombre, desagregacion) h"
                + " INNER JOIN"
                + " (select " + colDesagregacion + " as desagregacion, count(*) as cantidad_con_discapacidad FROM  estadisticas.sg_ext_estudiantes e "
                + " INNER JOIN estadisticas.sg_extracciones ex ON ( e.ext_cabezal_fk = ex.ext_pk )"
                + " WHERE  e.ext_est_tiene_discapacidad"
                + " AND ex.ext_anio = :anio AND ex.ext_nombre_fk = :nombrePk"
                + " group by desagregacion) z"
                + " ON (h.desagregacion = z.desagregacion)");
        q.addScalar("dato", new StringType());
        q.addScalar("desagregacion", new StringType());
        q.addScalar("cantidad", new DoubleType());
        q.setParameter("anio", filtro.getAnio());
        q.setParameter("nombrePk", filtro.getNombrePk());
        q.setResultTransformer(Transformers.aliasToBean(EstGenerica.class));
        return q.list();
    }

    //26 - CC-05
    public List<EstGenerica> distribucionPorcentualEstudiantesSegunConvivenciaFamiliar(FiltroEstadisticas filtro) throws GeneralException {

        String colDesagregacion = "'-'";
        if (filtro.getDesagregacion() != null) {
            if (filtro.getDesagregacion().equals(EnumDesagregacion.DEPARTAMENTO)) {
                colDesagregacion = "e.ext_est_sede_dep_nom";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.ZONA)) {
                colDesagregacion = "e.ext_est_sede_zon_nom";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_OFI_PRI)) {
                colDesagregacion = "e.ext_est_sede_tipo";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_PUB_PRI)) {
                colDesagregacion = "e.ext_sed_sector";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SEXO)) {
                colDesagregacion = "e.ext_est_sexo_nom";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.NIVEL)) {
                colDesagregacion = "e.ext_est_nivel_nom";
            } else {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_DESAGREGACION_NO_SOPORTADA);
                throw be;
            }
        }

        Session session = em.unwrap(Session.class);
        SQLQuery q = session.createSQLQuery("Select h.tpa_tpa_nombre as dato, h.desagregacion, (cantidad *1.0 / cantidad_matriculas * 100) as cantidad from"
                + "                 (select tpa_tpa_fk, tpa_tpa_nombre,  " + colDesagregacion + "  as desagregacion, count(*) as cantidad FROM  estadisticas.sg_ext_estudiantes e "
                + "                 INNER JOIN estadisticas.sg_extracciones ex ON ( e.ext_cabezal_fk = ex.ext_pk )"
                + "                 INNER JOIN estadisticas.sg_ext_est_tipos_parentesco tpa ON (ex.ext_pk = tpa.tpa_ext_cabezal_fk AND tpa.tpa_est_fk = e.ext_est_pk) "
                + "                 AND ex.ext_anio = :anio AND ex.ext_nombre_fk = :nombrePk AND tpa.tpa_vive_con"
                + "                 group by tpa_tpa_fk, tpa_tpa_nombre, desagregacion) h"
                + "                 INNER JOIN"
                + "                 (select  " + colDesagregacion + "  as desagregacion, count(*) as cantidad_matriculas FROM  estadisticas.sg_ext_estudiantes e "
                + "                 INNER JOIN estadisticas.sg_extracciones ex ON ( e.ext_cabezal_fk = ex.ext_pk )"
                + "                 WHERE ex.ext_anio = :anio AND ex.ext_nombre_fk = :nombrePk"
                + "                 group by desagregacion) z"
                + "                 ON (h.desagregacion = z.desagregacion)");
        q.addScalar("dato", new StringType());
        q.addScalar("desagregacion", new StringType());
        q.addScalar("cantidad", new DoubleType());
        q.setParameter("anio", filtro.getAnio());
        q.setParameter("nombrePk", filtro.getNombrePk());
        q.setResultTransformer(Transformers.aliasToBean(EstGenerica.class));
        return q.list();
    }

    //27 - CC-06
    public List<EstGenerica> porcentajeConSobreedad(FiltroEstadisticas filtro) throws GeneralException {

        String colDesagregacion = "'-'";
        String groupBy = " ";
        if (filtro.getDesagregacion() != null) {
            if (filtro.getDesagregacion().equals(EnumDesagregacion.DEPARTAMENTO)) {
                colDesagregacion = "e.ext_est_sede_dep_nom";
                groupBy = " ,desagregacion ";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.ZONA)) {
                colDesagregacion = "e.ext_est_sede_zon_nom";
                groupBy = " ,desagregacion ";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_OFI_PRI)) {
                colDesagregacion = "e.ext_est_sede_tipo";
                groupBy = " ,desagregacion ";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_PUB_PRI)) {
                colDesagregacion = "e.ext_sed_sector";
                groupBy = " ,desagregacion ";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SEXO)) {
                colDesagregacion = "e.ext_est_sexo_nom";
                groupBy = " ,desagregacion ";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.CANTIDAD_ANIOS)) {
                colDesagregacion = " CASE WHEN e.ext_est_sobreedad_anios = 1 THEN '1' "
                        + "            WHEN e.ext_est_sobreedad_anios = 2 THEN '2' "
                        + "            WHEN e.ext_est_sobreedad_anios >= 3 THEN '3 +' "
                        + "       END ";
                groupBy = " ,desagregacion ";
            } else {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_DESAGREGACION_NO_SOPORTADA);
                throw be;
            }
        }

        Session session = em.unwrap(Session.class);
        SQLQuery q = session.createSQLQuery(
                " SELECT nombre_grado as dato, desagregacion, (case sum2 when 0 then 0 else (sum1 / sum2 * 100) end) AS cantidad   "
                + " FROM   (SELECT dato, nombre_grado, "
                + " desagregacion, "
                + " Sum(a) AS sum1, "
                + " Sum(b) AS sum2 "
                + " FROM   (SELECT e.ext_est_grado_nom as nombre_grado, CONCAT(e.ext_est_nivel_nom, ' - ',"
                + " e.ext_est_ciclo_nom, ' - ',"
                + " e.ext_est_modalidad_educativa_nom, ' - ',"
                + " e.ext_est_modalidad_atencion_nom, ' - ',"
                + " e.ext_est_grado_nom) AS dato, "
                + "     e.ext_est_nivel_orden AS orden_nivel, "
                + "     e.ext_est_ciclo_orden AS orden_ciclo, "
                + "     e.ext_est_mod_orden   AS orden_modalidad, "
                + "     e.ext_est_grado_orden AS orden_grado,"
                + " " + colDesagregacion + " AS desagregacion, "
                + " Count(*) a, 0 b "
                + " FROM   estadisticas.sg_ext_estudiantes e INNER JOIN estadisticas.sg_extracciones ex ON ( e.ext_cabezal_fk = ex.ext_pk )   "
                + " WHERE  e.ext_est_con_sobreedad "
                + " AND ex.ext_anio = :anio AND ex.ext_nombre_fk = :nombrePk "
                + " GROUP BY e.ext_est_nivel_nom, e.ext_est_ciclo_nom, e.ext_est_modalidad_educativa_nom, e.ext_est_modalidad_atencion_nom, e.ext_est_grado_nom, "
                + " orden_nivel , orden_ciclo, orden_modalidad, orden_grado" + groupBy
                + " UNION ALL "
                + " SELECT e.ext_est_grado_nom as nombre_grado, CONCAT(e.ext_est_nivel_nom, ' - ',"
                + " e.ext_est_ciclo_nom, ' - ',"
                + " e.ext_est_modalidad_educativa_nom, ' - ',"
                + " e.ext_est_modalidad_atencion_nom, ' - ',"
                + " e.ext_est_grado_nom) AS dato, "
                + "     e.ext_est_nivel_orden AS orden_nivel, "
                + "     e.ext_est_ciclo_orden AS orden_ciclo, "
                + "     e.ext_est_mod_orden   AS orden_modalidad, "
                + "     e.ext_est_grado_orden AS orden_grado,"
                + " " + colDesagregacion + " AS desagregacion,"
                + " 0 a, Count(*) b "
                + " FROM   estadisticas.sg_ext_estudiantes e INNER JOIN estadisticas.sg_extracciones ex ON ( e.ext_cabezal_fk = ex.ext_pk ) "
                + " WHERE  ex.ext_anio = :anio AND ex.ext_nombre_fk = :nombrePk "
                + " GROUP BY e.ext_est_nivel_nom, e.ext_est_ciclo_nom, e.ext_est_modalidad_educativa_nom, e.ext_est_modalidad_atencion_nom, e.ext_est_grado_nom, "
                + "orden_nivel , orden_ciclo, orden_modalidad, orden_grado" + groupBy
                + " ) z "
                + " GROUP  BY  z.dato, z.nombre_grado, z.orden_nivel, z.orden_ciclo, z.orden_modalidad, z.orden_grado,  z.desagregacion "
                + " ORDER  BY  z.orden_nivel, z.orden_ciclo, z.orden_modalidad, z.orden_grado, z.desagregacion) h "
        );
        q.addScalar("dato", new StringType());
        q.addScalar("desagregacion", new StringType());
        q.addScalar("cantidad", new DoubleType());
        q.setParameter("anio", filtro.getAnio());
        q.setParameter("nombrePk", filtro.getNombrePk());
        q.setResultTransformer(Transformers.aliasToBean(EstGenerica.class));
        return q.list();
    }

    // CC-07
    public List<EstGenerica> distribucionPorcentualEstudiantesSegunCausaRetiroCentroEducativo(FiltroEstadisticas filtro) throws GeneralException {

        String colDesagregacion = "'-'";
        if (filtro.getDesagregacion() != null) {
            if (filtro.getDesagregacion().equals(EnumDesagregacion.DEPARTAMENTO)) {
                colDesagregacion = "mre_est_sede_dep_nom";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.ZONA)) {
                colDesagregacion = "mre_est_sede_zon_nom";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_OFI_PRI)) {
                colDesagregacion = "mre_est_sede_tipo";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_PUB_PRI)) {
                colDesagregacion = "mre_est_sede_sector";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SEXO)) {
                colDesagregacion = "mre_est_sexo_nom";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.NIVEL)) {
                colDesagregacion = "mre_est_nivel_nom";
            } else {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_DESAGREGACION_NO_SOPORTADA);
                throw be;
            }
        }

        Session session = em.unwrap(Session.class);
        SQLQuery q = session.createSQLQuery("select h.mre_mre_nombre as dato, h.desagregacion, (h.cantidad *1.0 / z.cantidad * 100) as cantidad from ("
                + " "
                + " (select mre_mre_fk, mre_mre_nombre,   " + colDesagregacion + " as desagregacion, count(*) as cantidad "
                + " from estadisticas.sg_ext_est_mat_retiradas mre  "
                + " INNER JOIN estadisticas.sg_extracciones ex ON ( mre.mre_ext_cabezal_fk = ex.ext_pk ) "
                + " WHERE ex.ext_anio = :anio  "
                + " AND ex.ext_nombre_fk = :nombrePk  "
                + " group by mre_mre_fk, mre_mre_nombre, desagregacion) h"
                + " "
                + " INNER JOIN "
                + " "
                + " (select  " + colDesagregacion + "  as desagregacion, count(*) as cantidad "
                + " from estadisticas.sg_ext_est_mat_retiradas mre  "
                + " INNER JOIN estadisticas.sg_extracciones ex ON ( mre.mre_ext_cabezal_fk = ex.ext_pk ) "
                + " WHERE ex.ext_anio = :anio  "
                + " AND ex.ext_nombre_fk = :nombrePk  "
                + " group by desagregacion) z "
                + " "
                + " ON (h.desagregacion = z.desagregacion)) order by dato");
        
        
        q.addScalar("dato", new StringType());
        q.addScalar("desagregacion", new StringType());
        q.addScalar("cantidad", new DoubleType());
        q.setParameter("anio", filtro.getAnio());
        q.setParameter("nombrePk", filtro.getNombrePk());
        q.setResultTransformer(Transformers.aliasToBean(EstGenerica.class));
        return q.list();
    }

    //No se utiliza
    public List<EstGenerica> tasaBrutaDeIngresoAlSextoGrado(FiltroEstadisticas filtro) throws GeneralException {

        if (!filtro.getDesagregacion().equals(EnumDesagregacion.SEXO)) {
            BusinessException be = new BusinessException();
            be.addError(Errores.ERROR_DESAGREGACION_NO_SOPORTADA);
            throw be;
        }

        Session session = em.unwrap(Session.class);
        SQLQuery q = session.createSQLQuery("select 'sexo' dato,e.ext_est_sexo_nom desagregacion, (count(*)*1.0/totales.cantidad)*100 as cantidad "
                + " from estadisticas.sg_ext_estudiantes e  "
                + "   JOIN estadisticas.sg_extracciones ex ON ( e.ext_cabezal_fk = ex.ext_pk )  "
                + " join ("
                + "select "
                + " pro_sexo_nom as ext_est_sexo_nom, "
                + " pro_poblacion as cantidad "
                + " from estadisticas.sg_proyecciones_poblacion"
                + " where pro_anio = :anio and pro_edad=6 "
                + "  ) as totales on (e.ext_est_sexo_nom=totales.ext_est_sexo_nom )  "
                + " WHERE ex.ext_anio = :anio  "
                + " AND ex.ext_nombre_fk = :nombrePk  "
                + " AND e.ext_est_edad_al_30_oct_mat = 6  "
                + " AND e.ext_est_nivel = :nivelPk and e.ext_est_grado = :gradoPk"
                + "  GROUP  BY  e.ext_est_sexo_nom, totales.cantidad ");

        q.addScalar("dato", new StringType());
        q.addScalar("desagregacion", new StringType());
        q.addScalar("cantidad", new DoubleType());
        q.setParameter("anio", filtro.getAnio());
        q.setParameter("gradoPk", Constantes.NIVEL_EDUCACION_BASICA_SEXTO_GRADO_REGULAR_PK);
        q.setParameter("nivelPk", Constantes.NIVEL_EDUCACION_BASICA_PK); // ver si es de b'asica
        q.setParameter("nombrePk", filtro.getNombrePk());
        q.setResultTransformer(Transformers.aliasToBean(EstGenerica.class));
        return q.list();
    }

    //No se utiliza
    public List<EstGenerica> tasaDeIngresoEfectivoAlSeptimoGrado(FiltroEstadisticas filtro) throws GeneralException {

        String colDesagregacion = "";
        String desagregacionEtiq = "";
        if (filtro.getDesagregacion().equals(EnumDesagregacion.DEPARTAMENTO)) {
            colDesagregacion = "ext_est_sede_dep_nom";
            desagregacionEtiq = EnumDesagregacion.DEPARTAMENTO.getText();
        } else if (filtro.getDesagregacion().equals(EnumDesagregacion.ZONA)) {
            colDesagregacion = "ext_est_sede_zon_nom";
            desagregacionEtiq = EnumDesagregacion.ZONA.getText();
        } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_OFI_PRI)) {
            colDesagregacion = "ext_est_sede_tipo";
            desagregacionEtiq = EnumDesagregacion.SECTOR_OFI_PRI.getText();
        } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_PUB_PRI)) {
            colDesagregacion = "ext_sed_sector";
            desagregacionEtiq = EnumDesagregacion.SECTOR_PUB_PRI.getText();
        } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SEXO)) {
            colDesagregacion = "ext_est_sexo_nom";
            desagregacionEtiq = EnumDesagregacion.SEXO.getText();
        } else {
            BusinessException be = new BusinessException();
            be.addError(Errores.ERROR_DESAGREGACION_NO_SOPORTADA);
            throw be;
        }
        Session session = em.unwrap(Session.class);
        // validar que exista extraccin para año anterior
        SQLQuery qExiste = session.createSQLQuery("select 1 from estadisticas.sg_extracciones ex "
                + " where ex.ext_anio = :anio  "
                + " and ext_nombre_fk = :nombrePk  ");
        qExiste.setParameter("anio", filtro.getAnio() - 1);
        qExiste.setParameter("nombrePk", filtro.getNombrePk());
        List l = qExiste.list();
        if (l == null || l.size() == 0) {
            BusinessException be = new BusinessException();
            be.addError(Errores.ERROR_REQUIERE_EXTRACCION_ANIO_ANTERIOR);
            throw be;
        }

        SQLQuery q = session.createSQLQuery(" select '" + desagregacionEtiq + "' dato,e." + colDesagregacion + " desagregacion, (totales.cantidad*1.0/count(*)*1.0)*100 as cantidad "
                + "from estadisticas.sg_ext_estudiantes e  "
                + "   JOIN estadisticas.sg_extracciones ex ON ( e.ext_cabezal_fk = ex.ext_pk )  "
                + ""
                + "   join (select e." + colDesagregacion + ", count(*) as cantidad from estadisticas.sg_ext_estudiantes e "
                + "	JOIN estadisticas.sg_extracciones ex ON ( e.ext_cabezal_fk = ex.ext_pk )  "
                + "	WHERE  ex.ext_anio = :anio-1 AND ex.ext_nombre_fk = :nombrePk  and e.ext_est_grado=:gradoPk and e.ext_est_nivel=:nivelPk "
                + "	GROUP BY e." + colDesagregacion + ""
                + ") as totales on (e." + colDesagregacion + "=totales." + colDesagregacion + " ) "
                + " WHERE ex.ext_anio = :anio  "
                + " AND ex.ext_nombre_fk = :nombrePk  "
                + " AND e.ext_est_nivel = :nivelPk and e.ext_est_grado = :gradoPk"
                + " GROUP  BY  e." + colDesagregacion + ",totales.cantidad ");

        q.addScalar("dato", new StringType());
        q.addScalar("desagregacion", new StringType());
        q.addScalar("cantidad", new DoubleType());
        q.setParameter("anio", filtro.getAnio());
        q.setParameter("gradoPk", Constantes.NIVEL_EDUCACION_BASICA_SEPTIMO_GRADO_REGULAR_PK);
        q.setParameter("nivelPk", Constantes.NIVEL_EDUCACION_BASICA_PK); // ver si es de b'asica
        q.setParameter("nombrePk", filtro.getNombrePk());
        q.setResultTransformer(Transformers.aliasToBean(EstGenerica.class));
        return q.list();
    }

    //29 - F-01
    public List<EstGenerica> porcentajeEstudiantesAprobados(FiltroEstadisticas filtro) {

        String colDesagregacion = "'-'";
        if (filtro.getDesagregacion() != null) {
            if (filtro.getDesagregacion().equals(EnumDesagregacion.DEPARTAMENTO)) {
                colDesagregacion = "ext_est_sede_dep_nom";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.ZONA)) {
                colDesagregacion = "ext_est_sede_zon_nom";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_OFI_PRI)) {
                colDesagregacion = "ext_est_sede_tipo";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_PUB_PRI)) {
                colDesagregacion = "ext_sed_sector";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SEXO)) {
                colDesagregacion = "ext_est_sexo_nom";
            } else {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_DESAGREGACION_NO_SOPORTADA);
                throw be;
            }
        }

        Session session = em.unwrap(Session.class);
        SQLQuery q = session.createSQLQuery("Select grado_nombre as dato, desagregacion, (case sum2 when 0 then 0 else (sum1 / sum2 * 100) end) AS cantidad from ("
                + " Select dato, grado_nombre, desagregacion, orden_nivel, orden_ciclo, sum(a) as sum1, sum(b) as sum2 from ("
                + " SELECT e.ext_est_grado_nom as grado_nombre, "
                + " CONCAT(e.ext_est_nivel_nom, ' - ',"
                + "	e.ext_est_ciclo_nom, ' - ',"
                + "	e.ext_est_modalidad_educativa_nom, ' - ',"
                + "	e.ext_est_modalidad_atencion_nom, ' - ',"
                + "	e.ext_est_grado_nom) as dato, " + colDesagregacion + " as desagregacion,"
                + "     e.ext_est_nivel_orden as orden_nivel,"
                + "     e.ext_est_ciclo_orden as orden_ciclo,"
                + "     e.ext_est_mod_orden   AS orden_modalidad, "
                + "     e.ext_est_grado_orden AS orden_grado,"
                + " count(*) a, 0 b from estadisticas.sg_ext_estudiantes e"
                + "	INNER JOIN estadisticas.sg_extracciones ex ON (e.ext_cabezal_fk = ex.ext_pk)"
                + "	where"
                + "	e.ext_est_mat_promocion_grado = 'PROMOVIDO'"
                + "	and ex.ext_anio = :anio and ex.ext_nombre_fk = :nombrePk"
                + "	group by e.ext_est_nivel_nom, e.ext_est_ciclo_nom, e.ext_est_modalidad_educativa_nom, e.ext_est_modalidad_atencion_nom, e.ext_est_grado_nom, desagregacion, orden_nivel, orden_ciclo, orden_modalidad, orden_grado"
                + " UNION ALL "
                + " SELECT e.ext_est_grado_nom as grado_nombre, "
                + " CONCAT(e.ext_est_nivel_nom, ' - ',"
                + "	e.ext_est_ciclo_nom, ' - ',"
                + "	e.ext_est_modalidad_educativa_nom, ' - ',"
                + "	e.ext_est_modalidad_atencion_nom, ' - ',"
                + "	e.ext_est_grado_nom) as dato, " + colDesagregacion + " as desagregacion,"
                + "     e.ext_est_nivel_orden as orden_nivel,"
                + "     e.ext_est_ciclo_orden as orden_ciclo,"
                + "     e.ext_est_mod_orden   AS orden_modalidad, "
                + "     e.ext_est_grado_orden AS orden_grado,"
                + " 0 a, count(*) b from estadisticas.sg_ext_estudiantes e"
                + "	INNER JOIN estadisticas.sg_extracciones ex ON (e.ext_cabezal_fk = ex.ext_pk)"
                + "	where"
                + "	ex.ext_anio = :anio and ex.ext_nombre_fk = :nombrePk"
                + "	group by e.ext_est_nivel_nom, e.ext_est_ciclo_nom, e.ext_est_modalidad_educativa_nom, e.ext_est_modalidad_atencion_nom, e.ext_est_grado_nom, desagregacion, orden_nivel, orden_ciclo, orden_modalidad, orden_grado"
                + ") z group by dato, grado_nombre, desagregacion, orden_nivel, orden_ciclo, orden_modalidad, orden_grado order by orden_nivel, orden_ciclo, orden_modalidad, orden_grado) h");
        q.addScalar("dato", new StringType());
        q.addScalar("desagregacion", new StringType());
        q.addScalar("cantidad", new DoubleType());
        q.setParameter("anio", filtro.getAnio());
        q.setParameter("nombrePk", filtro.getNombrePk());
        q.setResultTransformer(Transformers.aliasToBean(EstGenerica.class));
        return q.list();
    }

    //30 - F-02
    public List<EstGenerica> tasaBrutaAprobacion(FiltroEstadisticas filtro) {

        String colDesagregacion = "'-'";
        String colPoblacionDesagregacion = "'-'";
        if (filtro.getDesagregacion() != null) {
            if (filtro.getDesagregacion().equals(EnumDesagregacion.SEXO)) {
                colDesagregacion = "e.ext_est_sexo_nom";
                colPoblacionDesagregacion = "pob.pro_sexo_nom";
            } else {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_DESAGREGACION_NO_SOPORTADA);
                throw be;
            }
        }

        Session session = em.unwrap(Session.class);
        SQLQuery q = session.createSQLQuery("SELECT dato,"
                + "       desagregacion,"
                + "       (cant*1.0 / sum(pro_poblacion) * 100) AS cantidad"
                + " FROM"
                + "  (SELECT e.ext_est_grado_nom AS dato,"
                + "          " + colDesagregacion + " AS desagregacion,"
                + "          e.ext_est_nivel_nom, e.ext_est_ciclo_nom, ext_est_modalidad_educativa_nom, ext_est_modalidad_atencion_nom, ext_est_grado_nom, "
                + "          e.ext_est_nivel_orden AS orden_nivel,"
                + "          e.ext_est_ciclo_orden as orden_ciclo,"
                + "          e.ext_est_mod_orden   AS orden_modalidad, "
                + "          e.ext_est_grado_orden AS orden_grado,"
                + "          MIN(e.ext_est_grado_edad_min) AS edad_minima,"
                + "          count(*) AS cant"
                + "   FROM estadisticas.sg_ext_estudiantes e"
                + "   INNER JOIN estadisticas.sg_extracciones ex ON (e.ext_cabezal_fk = ex.ext_pk)"
                + "   WHERE e.ext_est_mat_promocion_grado = 'PROMOVIDO'"
                + "      AND ex.ext_anio = :anio"
                + "      AND ex.ext_nombre_fk = :nombrePk"
                + "   GROUP BY e.ext_est_nivel_nom,"
                + "            e.ext_est_ciclo_nom,"
                + "            e.ext_est_modalidad_educativa_nom,"
                + "            e.ext_est_modalidad_atencion_nom,"
                + "            e.ext_est_grado_nom,"
                + "            desagregacion,"
                + "            orden_nivel, orden_ciclo, orden_modalidad, orden_grado"
                + "   HAVING MIN(e.ext_est_grado_edad_min) >= 0"
                + "   ORDER BY orden_nivel, orden_ciclo, orden_modalidad, orden_grado) h"
                + "   INNER JOIN estadisticas.sg_proyecciones_poblacion pob ON (" + colPoblacionDesagregacion + " = desagregacion"
                + "                                                          AND pob.pro_edad = edad_minima"
                + "                                                          AND pob.pro_anio = :anio)"
                + "    GROUP BY dato, "
                + "            ext_est_nivel_nom, "
                + "            ext_est_ciclo_nom, "
                + "            ext_est_modalidad_educativa_nom, "
                + "            ext_est_modalidad_atencion_nom, "
                + "            ext_est_grado_nom, "
                + "    desagregacion,"
                + "    cant,"
                + "    orden_nivel, orden_ciclo, orden_modalidad, orden_grado"
                + " ORDER BY orden_nivel, orden_ciclo, orden_modalidad, orden_grado");
        q.addScalar("dato", new StringType());
        q.addScalar("desagregacion", new StringType());
        q.addScalar("cantidad", new DoubleType());
        q.setParameter("anio", filtro.getAnio());
        q.setParameter("nombrePk", filtro.getNombrePk());
        q.setResultTransformer(Transformers.aliasToBean(EstGenerica.class));
        return q.list();
    }

    //31 - F-03
    //IMPACTA EN 21(T-07)
    public List<EstGenerica> tasaPromocion(FiltroEstadisticas filtro) {

        String colDesagregacion = "'-'";
        if (filtro.getDesagregacion() != null) {
            if (filtro.getDesagregacion().equals(EnumDesagregacion.DEPARTAMENTO)) {
                colDesagregacion = "e.ext_est_sede_dep_nom";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.ZONA)) {
                colDesagregacion = "e.ext_est_sede_zon_nom";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_OFI_PRI)) {
                colDesagregacion = "e.ext_est_sede_tipo";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_PUB_PRI)) {
                colDesagregacion = "e.ext_sed_sector";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SEXO)) {
                colDesagregacion = "e.ext_est_sexo_nom";
            } else {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_DESAGREGACION_NO_SOPORTADA);
                throw be;
            }
        }
        Session session = em.unwrap(Session.class);
        SQLQuery q = session.createSQLQuery("select z.nombre_grado as dato, z.desagregacion as desagregacion, (case z.b when 0 then 0 else (h.a::::decimal / z.b::::decimal * 100) end) AS cantidad  from "
                + " ((SELECT e.ext_est_grado_nom as nombre_grado,"
                + " e.ext_est_grado as grado_pk, "
                + " CONCAT(e.ext_est_nivel_nom, ' - ',"
                + " e.ext_est_ciclo_nom, ' - ',"
                + " e.ext_est_modalidad_educativa_nom, ' - ',"
                + " e.ext_est_modalidad_atencion_nom, ' - ',"
                + " e.ext_est_grado_nom) as dato, " + colDesagregacion + " as desagregacion,"
                + "     e.ext_est_nivel_orden as orden_nivel,"
                + "     e.ext_est_ciclo_orden as orden_ciclo,"
                + "     e.ext_est_mod_orden   AS orden_modalidad, "
                + "     e.ext_est_grado_orden AS orden_grado,"
                + " count(*) a, 0 b from estadisticas.sg_ext_estudiantes e"
                + " INNER JOIN estadisticas.sg_extracciones ex ON (e.ext_cabezal_fk = ex.ext_pk)"
                + " where"
                + " (e.ext_est_repetidor is null or not e.ext_est_repetidor)"
                + " and ex.ext_anio = :anio and ex.ext_nombre_fk = :nombrePk AND e.ext_est_nivel IN (3,4)" //BASICA y MEDIA
                + " group by e.ext_est_nivel_nom, e.ext_est_ciclo_nom, e.ext_est_modalidad_educativa_nom, e.ext_est_modalidad_atencion_nom, e.ext_est_grado_nom,"
                + " e.ext_est_grado, desagregacion, orden_nivel, orden_ciclo, orden_modalidad, orden_grado) h"
                + ""
                + " INNER JOIN "
                + ""
                + " (SELECT e.ext_est_grado_nom as nombre_grado,"
                + " e.ext_est_grado as grado_pk, "
                + " CONCAT(e.ext_est_nivel_nom, ' - ',"
                + " e.ext_est_ciclo_nom, ' - ',"
                + " e.ext_est_modalidad_educativa_nom, ' - ',"
                + " e.ext_est_modalidad_atencion_nom, ' - ',"
                + " e.ext_est_grado_nom) as dato, " + colDesagregacion + "  as desagregacion,"
                + "     e.ext_est_nivel_orden as orden_nivel,"
                + "     e.ext_est_ciclo_orden as orden_ciclo,"
                + "     e.ext_est_mod_orden   AS orden_modalidad, "
                + "     e.ext_est_grado_orden AS orden_grado,"
                + " 0 a, count(*) b from estadisticas.sg_ext_estudiantes e"
                + " INNER JOIN estadisticas.sg_extracciones ex ON (e.ext_cabezal_fk = ex.ext_pk)"
                + " where"
                + " ex.ext_anio = :anio_c and ex.ext_nombre_fk = :nombrePk_c AND e.ext_est_nivel IN (3,4)" //BASICA y MEDIA
                + " group by e.ext_est_nivel_nom, e.ext_est_ciclo_nom, e.ext_est_modalidad_educativa_nom, e.ext_est_modalidad_atencion_nom, e.ext_est_grado_nom,"
                + " e.ext_est_grado,  desagregacion , orden_nivel, orden_ciclo, orden_modalidad, orden_grado) z"
                + " "
                + " ON (h.desagregacion = z.desagregacion AND z.grado_pk IN (select rgp_grado_origen_fk from centros_educativos.sg_rel_grado_precedente where rgp_grado_destino_fk = h.grado_pk))"
                + " ) "
                + " order by h.orden_nivel, h.orden_ciclo, h.orden_modalidad, h.orden_grado");
        q.addScalar("dato", new StringType());
        q.addScalar("desagregacion", new StringType());
        q.addScalar("cantidad", new DoubleType());
        q.setParameter("anio", filtro.getAnio());
        q.setParameter("nombrePk", filtro.getNombrePk());
        q.setParameter("anio_c", filtro.getAnioComparacion());
        q.setParameter("nombrePk_c", filtro.getNombrePkComparacion());
        q.setResultTransformer(Transformers.aliasToBean(EstGenerica.class));
        return q.list();
    }

    //37 - RE-01
    public List<EstGenerica> centrosEducativosSegunNivelEducativo(FiltroEstadisticas filtro) throws GeneralException {
        // NO REQUIERE DESAGREGACIN, SE VALIDA DESDE WEB HTML
        Session session = em.unwrap(Session.class);
        SQLQuery q = session.createSQLQuery(" select nivel dato, null desagregacion, count(*) as cantidad from ( "
                + "select  e.ext_est_nivel_nom as nivel, e.ext_est_nivel_orden as orden, e.ext_est_sede "
                + "from estadisticas.sg_ext_estudiantes e   "
                + "INNER JOIN estadisticas.sg_extracciones ex ON ( e.ext_cabezal_fk = ex.ext_pk )  "
                + "WHERE ex.ext_anio = :anio AND ex.ext_nombre_fk = :nombrePk "
                + "group by nivel, orden, e.ext_est_sede) z group by nivel, orden order by orden");

        q.addScalar("dato", new StringType());
        q.addScalar("cantidad", new LongType());
        q.setParameter("anio", filtro.getAnio());
        q.setParameter("nombrePk", filtro.getNombrePk());
        q.setResultTransformer(Transformers.aliasToBean(EstGenerica.class));
        return q.list();
    }

    //39 - RE-03
    public List<EstGenerica> porcentajeDeEstudiantesConAccesoAInternet(FiltroEstadisticas filtro) throws GeneralException {
        String colDesagregacion = "'-'";
        if (filtro.getDesagregacion() != null) {
            if (filtro.getDesagregacion().equals(EnumDesagregacion.DEPARTAMENTO)) {
                colDesagregacion = "e.ext_est_sede_dep_nom";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.ZONA)) {
                colDesagregacion = "e.ext_est_sede_zon_nom";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_OFI_PRI)) {
                colDesagregacion = "e.ext_est_sede_tipo";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_PUB_PRI)) {
                colDesagregacion = "e.ext_sed_sector";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SEXO)) {
                colDesagregacion = "e.ext_est_sexo_nom";
            } else {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_DESAGREGACION_NO_SOPORTADA);
                throw be;
            }
        }
        Session session = em.unwrap(Session.class);
        SQLQuery q = session.createSQLQuery(
                " Select dato, desagregacion, (sum1 / sum2 * 100) as cantidad FROM ("
                + " SELECT dato, desagregacion, sum(a) as sum1, sum(b) as sum2 FROM ("
                + " SELECT e.ext_est_nivel_nom dato, " + colDesagregacion + " as desagregacion, e.ext_est_nivel_orden as orden, count(*) a, 0 b from estadisticas.sg_ext_estudiantes e"
                + " INNER JOIN estadisticas.sg_extracciones ex ON (e.ext_cabezal_fk = ex.ext_pk)"
                + " where e.ext_est_tiene_acceso_internet"
                + " and ex.ext_anio = :anio and ex.ext_nombre_fk = :nombrePk"
                + " group by dato, desagregacion, orden"
                + " UNION ALL"
                + " SELECT e.ext_est_nivel_nom dato, " + colDesagregacion + " as desagregacion, e.ext_est_nivel_orden as orden, 0 a, count(*) b from estadisticas.sg_ext_estudiantes e"
                + " INNER JOIN estadisticas.sg_extracciones ex ON (e.ext_cabezal_fk = ex.ext_pk)"
                + " where ex.ext_anio = :anio and ex.ext_nombre_fk = :nombrePk"
                + " group by dato, desagregacion, orden"
                + " ) z group by z.dato, z.desagregacion, z.orden order by z.orden) h");

        q.addScalar("dato", new StringType());
        q.addScalar("desagregacion", new StringType());
        q.addScalar("cantidad", new DoubleType());
        q.setParameter("anio", filtro.getAnio());
        q.setParameter("nombrePk", filtro.getNombrePk());
        q.setResultTransformer(Transformers.aliasToBean(EstGenerica.class));
        return q.list();
    }

    //40 - RE-04
    public List<EstGenerica> porcentajeDeDocentesConAccesoAInternet(FiltroEstadisticas filtro) throws GeneralException {
        String colDesagregacion = "'-'";
        if (filtro.getDesagregacion() != null) {
            if (filtro.getDesagregacion().equals(EnumDesagregacion.SEXO)) {
                colDesagregacion = "pers.ext_per_sexo_nom";
            } else {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_DESAGREGACION_NO_SOPORTADA);
                throw be;
            }
        }

        Session session = em.unwrap(Session.class);
        SQLQuery q = session.createSQLQuery(
                "  Select dato, desagregacion, (sum1 / sum2 * 100) as cantidad FROM ( "
                + " SELECT dato, desagregacion, sum(a) as sum1, sum(b) as sum2 FROM ( "
                + " SELECT '-' as dato, " + colDesagregacion + " as desagregacion, count(*) a, 0 b from estadisticas.sg_ext_personal  pers "
                + " INNER JOIN estadisticas.sg_extracciones ex ON (pers.ext_cabezal_fk = ex.ext_pk) "
                + " WHERE  ex.ext_anio = :anio and ex.ext_nombre_fk = :nombrePk and pers.ext_per_tiene_acceso_internet is true "
                + " group by dato, desagregacion "
                + " UNION ALL "
                + "  SELECT '-' as dato, " + colDesagregacion + " as desagregacion, 0 a, count(*) b from estadisticas.sg_ext_personal  pers "
                + " INNER JOIN estadisticas.sg_extracciones ex ON (pers.ext_cabezal_fk = ex.ext_pk) "
                + " WHERE  ex.ext_anio = :anio and ex.ext_nombre_fk = :nombrePk "
                + " group by dato, desagregacion "
                + " ) z group by z.dato, z.desagregacion order by z.dato, z.desagregacion) h");

        q.addScalar("dato", new StringType());
        q.addScalar("desagregacion", new StringType());
        q.addScalar("cantidad", new DoubleType());
        q.setParameter("anio", filtro.getAnio());
        q.setParameter("nombrePk", filtro.getNombrePk());
        q.setResultTransformer(Transformers.aliasToBean(EstGenerica.class));
        return q.list();
    }

    //41 - RE-05
    public List<EstGenerica> porcentajeDeEstudiantesConAccesoAComputadora(FiltroEstadisticas filtro) throws GeneralException {
        String colDesagregacion = "'-'";
        if (filtro.getDesagregacion() != null) {
            if (filtro.getDesagregacion().equals(EnumDesagregacion.DEPARTAMENTO)) {
                colDesagregacion = "e.ext_est_sede_dep_nom";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.ZONA)) {
                colDesagregacion = "e.ext_est_sede_zon_nom";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_OFI_PRI)) {
                colDesagregacion = "e.ext_est_sede_tipo";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_PUB_PRI)) {
                colDesagregacion = "e.ext_sed_sector";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SEXO)) {
                colDesagregacion = "e.ext_est_sexo_nom";
            } else {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_DESAGREGACION_NO_SOPORTADA);
                throw be;
            }
        }
        Session session = em.unwrap(Session.class);
        SQLQuery q = session.createSQLQuery(
                " Select dato, desagregacion, (sum1 / sum2 * 100) as cantidad FROM ("
                + " SELECT dato, desagregacion, sum(a) as sum1, sum(b) as sum2 FROM ("
                + " SELECT e.ext_est_nivel_nom dato, " + colDesagregacion + " as desagregacion, e.ext_est_nivel_orden as orden, count(*) a, 0 b from estadisticas.sg_ext_estudiantes e"
                + " INNER JOIN estadisticas.sg_extracciones ex ON (e.ext_cabezal_fk = ex.ext_pk)"
                + " where e.ext_est_tiene_computadora"
                + " and ex.ext_anio = :anio and ex.ext_nombre_fk = :nombrePk"
                + " group by dato, desagregacion, orden"
                + " UNION ALL"
                + " SELECT e.ext_est_nivel_nom dato, " + colDesagregacion + " as desagregacion, e.ext_est_nivel_orden as orden, 0 a, count(*) b from estadisticas.sg_ext_estudiantes e"
                + " INNER JOIN estadisticas.sg_extracciones ex ON (e.ext_cabezal_fk = ex.ext_pk)"
                + " where ex.ext_anio = :anio and ex.ext_nombre_fk = :nombrePk"
                + " group by dato, desagregacion, orden"
                + " ) z group by z.dato, z.desagregacion, z.orden order by z.orden) h");

        q.addScalar("dato", new StringType());
        q.addScalar("desagregacion", new StringType());
        q.addScalar("cantidad", new DoubleType());
        q.setParameter("anio", filtro.getAnio());
        q.setParameter("nombrePk", filtro.getNombrePk());
        q.setResultTransformer(Transformers.aliasToBean(EstGenerica.class));
        return q.list();
    }

    //43 - RE-07
    public List<EstGenerica> estudiantesPorSeccion(FiltroEstadisticas filtro) throws GeneralException {
        // NO REQUIERE DESAGREGACIN, SE VALIDA DESDE WEB HTML
        Session session = em.unwrap(Session.class);
        SQLQuery q = session.createSQLQuery(" Select dato, null desagregacion, (estudiantes / secciones) as cantidad from ( "
                + "select nivel as dato, null desagregacion, sum(a) as estudiantes, sum(b) as secciones from ( "
                + "select  e.ext_est_nivel_nom as nivel, e.ext_est_nivel_orden as orden, count(*) a, 0 b "
                + "from estadisticas.sg_ext_estudiantes e   "
                + "INNER JOIN estadisticas.sg_extracciones ex ON ( e.ext_cabezal_fk = ex.ext_pk )  "
                + "WHERE ex.ext_anio = :anio AND ex.ext_nombre_fk = :nombrePk "
                + "group by nivel, orden "
                + "UNION ALL "
                + "select nivel, orden, 0 a, count(*) b from "
                + "(select  e.ext_est_nivel_nom as nivel, e.ext_est_nivel_orden as orden, e.ext_est_secc "
                + "from estadisticas.sg_ext_estudiantes e   "
                + "INNER JOIN estadisticas.sg_extracciones ex ON ( e.ext_cabezal_fk = ex.ext_pk )  "
                + "WHERE ex.ext_anio = :anio AND ex.ext_nombre_fk = :nombrePk "
                + "group by nivel, orden, e.ext_est_secc) h group by nivel, orden) z group by dato, orden order by orden) z");

        q.addScalar("dato", new StringType());
        q.addScalar("cantidad", new DoubleType());
        q.setParameter("anio", filtro.getAnio());
        q.setParameter("nombrePk", filtro.getNombrePk());
        q.setResultTransformer(Transformers.aliasToBean(EstGenerica.class));
        return q.list();
    }

    //19 - T-09
    public List<EstGenerica> tasaBrutaIngresoPorGrado(FiltroEstadisticas filtro) throws GeneralException {
        String colDesagregacion = "'-'";
        String colPoblacionDesagregacion = "'-'";
        if (filtro.getDesagregacion() != null) {
            if (filtro.getDesagregacion().equals(EnumDesagregacion.SEXO)) {
                colDesagregacion = "e.ext_est_sexo_nom";
                colPoblacionDesagregacion = "pob.pro_sexo_nom";
            } else {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_DESAGREGACION_NO_SOPORTADA);
                throw be;
            }
        }
        Session session = em.unwrap(Session.class);
        SQLQuery q = session.createSQLQuery(" select nombre_grado as dato, desagregacion, (cantidad*1.0 / sum(pro_poblacion) * 100) as cantidad from"
                + "                (select e.ext_est_grado_nom as nombre_grado, "
                + "                CONCAT(e.ext_est_nivel_nom, ' - ',"
                + "                e.ext_est_ciclo_nom, ' - ',"
                + "                e.ext_est_modalidad_educativa_nom, ' - ',"
                + "                e.ext_est_modalidad_atencion_nom, ' - ',"
                + "                e.ext_est_grado_nom) as dato,"
                + "                " + colDesagregacion + "  as desagregacion,"
                + "                e.ext_est_nivel_orden AS orden_nivel,"
                + "             e.ext_est_ciclo_orden as orden_ciclo,"
                + "             e.ext_est_mod_orden   AS orden_modalidad, "
                + "             e.ext_est_grado_orden AS orden_grado,"
                + "                MIN(e.ext_est_grado_edad_min) as edad_minima,"
                + "                count(*) as cantidad from estadisticas.sg_ext_estudiantes e"
                + "                INNER JOIN estadisticas.sg_extracciones ex ON (e.ext_cabezal_fk = ex.ext_pk)"
                + "                where ex.ext_anio = :anio and ex.ext_nombre_fk = :nombrePk"
                + "                and (e.ext_est_repetidor is false or e.ext_est_repetidor is null)"
                + "                group by e.ext_est_nivel_nom, e.ext_est_ciclo_nom, e.ext_est_modalidad_educativa_nom, e.ext_est_modalidad_atencion_nom, e.ext_est_grado_nom, desagregacion,"
                + "                orden_nivel, orden_ciclo, orden_modalidad, orden_grado"
                + "                having MIN(e.ext_est_grado_edad_min) >= 0"
                + "                order by orden_nivel, orden_ciclo, orden_modalidad, orden_grado) h"
                + "                INNER JOIN estadisticas.sg_proyecciones_poblacion pob ON ( " + colPoblacionDesagregacion + " = desagregacion and pob.pro_edad = edad_minima and pob.pro_anio = :anio)"
                + "                group by dato, nombre_grado, desagregacion, cantidad, orden_nivel, orden_ciclo, orden_modalidad, orden_grado "
                + "                order by orden_nivel, orden_ciclo, orden_modalidad, orden_grado");

        q.addScalar("dato", new StringType());
        q.addScalar("desagregacion", new StringType());
        q.addScalar("cantidad", new DoubleType());
        q.setParameter("anio", filtro.getAnio());
        q.setParameter("nombrePk", filtro.getNombrePk());
        q.setResultTransformer(Transformers.aliasToBean(EstGenerica.class));
        return q.list();
    }

    //   32 - D-01
    public List<EstGenerica> distribucionDeDocentesSegunNivelEducativo(FiltroEstadisticas filtro) throws GeneralException {
        String colDesagregacion = "'-'";
        if (filtro.getDesagregacion() != null) {
            if (filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_OFI_PRI)) {
                colDesagregacion = "extTipo.pts_tipo";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_PUB_PRI)) {
                colDesagregacion = "extTipo.pts_sector";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SEXO)) {
                colDesagregacion = "pers.ext_per_sexo_nom";
            } else {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_DESAGREGACION_NO_SOPORTADA);
                throw be;
            }
        }

        Session session = em.unwrap(Session.class);
        SQLQuery q = session.createSQLQuery(" 	with total as (select count(*) total    "
                + "   from estadisticas.sg_ext_personal pers      "
                + " join estadisticas.sg_extracciones ext on ( pers.ext_cabezal_fk = ext.ext_pk )   "
                + " join estadisticas.sg_ext_pers_sec extSec on (extSec.sec_ext_cabezal_fk = ext.ext_pk AND extSec.sec_per_fk=pers.ext_per_pk)  "
                + (filtro.getDesagregacion() != null && filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_OFI_PRI) ? " join estadisticas.sg_ext_pers_tipo_sede extTipo on (extTipo.pts_ext_cabezal_fk=ext.ext_pk and extTipo.pts_per_fk=pers.ext_per_pk)  " : "")
                + (filtro.getDesagregacion() != null && filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_PUB_PRI) ? " join estadisticas.sg_ext_pers_sector_sede extTipo on (extTipo.pts_ext_cabezal_fk=ext.ext_pk and extTipo.pts_per_fk=pers.ext_per_pk)  " : "")
                + " 	INNER JOIN centros_educativos.sg_secciones sec ON (extSec.sec_sec_fk = sec.sec_pk)      "
                + " 	INNER JOIN centros_educativos.sg_servicio_educativo se ON (sec.sec_servicio_educativo_fk = se.sdu_pk)      "
                + "     INNER JOIN centros_educativos.sg_sedes sed ON (se.sdu_sede_fk = sed.sed_pk)      "
                + " 	INNER JOIN centros_educativos.sg_grados gra ON (se.sdu_grado_fk = gra.gra_pk)      "
                + " 	INNER JOIN centros_educativos.sg_rel_mod_ed_mod_aten relmodaten ON (gra.gra_relacion_modalidad_fk = relmodaten.rea_pk)      "
                + " 	INNER JOIN centros_educativos.sg_modalidades modedu ON (relmodaten.rea_modalidad_educativa_fk = modedu.mod_pk)      "
                + " 	INNER JOIN centros_educativos.sg_ciclos cic ON (modedu.mod_ciclo = cic.cic_pk)      "
                + " 	INNER JOIN centros_educativos.sg_niveles niv ON (cic.cic_nivel = niv.niv_pk)   "
                + " 		WHERE ext.ext_anio = :anio      "
                + " 	AND ext.ext_nombre_fk = :nombrePk)"
                + " 	 (select niv.niv_nombre dato, " + colDesagregacion + " desagregacion, (count(*)*1.0/total.total)*100 cantidad "
                + "   from estadisticas.sg_ext_personal pers     "
                + " join estadisticas.sg_extracciones ext on ( pers.ext_cabezal_fk = ext.ext_pk )     "
                + " join estadisticas.sg_ext_pers_sec extSec on (extSec.sec_ext_cabezal_fk = ext.ext_pk AND extSec.sec_per_fk=pers.ext_per_pk)     "
                + (filtro.getDesagregacion() != null && filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_OFI_PRI) ? " join estadisticas.sg_ext_pers_tipo_sede extTipo on (extTipo.pts_ext_cabezal_fk=ext.ext_pk and extTipo.pts_per_fk=pers.ext_per_pk)  " : "")
                + (filtro.getDesagregacion() != null && filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_PUB_PRI) ? " join estadisticas.sg_ext_pers_sector_sede extTipo on (extTipo.pts_ext_cabezal_fk=ext.ext_pk and extTipo.pts_per_fk=pers.ext_per_pk)  " : "")
                + " 	INNER JOIN centros_educativos.sg_secciones sec ON (extSec.sec_sec_fk = sec.sec_pk)     "
                + " 	INNER JOIN centros_educativos.sg_servicio_educativo se ON (sec.sec_servicio_educativo_fk = se.sdu_pk)     "
                + "     INNER JOIN centros_educativos.sg_sedes sed ON (se.sdu_sede_fk = sed.sed_pk)     "
                + " 	INNER JOIN centros_educativos.sg_grados gra ON (se.sdu_grado_fk = gra.gra_pk)     "
                + " 	INNER JOIN centros_educativos.sg_rel_mod_ed_mod_aten relmodaten ON (gra.gra_relacion_modalidad_fk = relmodaten.rea_pk)     "
                + " 	INNER JOIN centros_educativos.sg_modalidades modedu ON (relmodaten.rea_modalidad_educativa_fk = modedu.mod_pk)     "
                + " 	INNER JOIN centros_educativos.sg_ciclos cic ON (modedu.mod_ciclo = cic.cic_pk)     "
                + " 	INNER JOIN centros_educativos.sg_niveles niv ON (cic.cic_nivel = niv.niv_pk)  "
                + " 	cross join total "
                + " 	WHERE ext.ext_anio = :anio     "
                + " 	AND ext.ext_nombre_fk = :nombrePk    "
                + " 	group by niv.niv_orden,niv.niv_nombre,desagregacion, total.total "
                + " 	order by niv.niv_orden,niv.niv_nombre,desagregacion, total.total)  ");

        q.addScalar("dato", new StringType());
        q.addScalar("desagregacion", new StringType());
        q.addScalar("cantidad", new DoubleType());
        q.setParameter("nombrePk", filtro.getNombrePk());
        q.setParameter("anio", filtro.getAnio());
        q.setResultTransformer(Transformers.aliasToBean(EstGenerica.class));
        return q.list();
    }

    //   33 - D-02
    public List<EstGenerica> porcentajeDeDocentesCertificadosPorNivelEducativo(FiltroEstadisticas filtro) throws GeneralException {
        String colDesagregacion = "'-'";
        if (filtro.getDesagregacion() != null) {
            if (filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_OFI_PRI)) {
                colDesagregacion = "extTipo.pts_tipo";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_PUB_PRI)) {
                colDesagregacion = "extTipo.pts_sector";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SEXO)) {
                colDesagregacion = "pers.ext_per_sexo_nom";
            } else {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_DESAGREGACION_NO_SOPORTADA);
                throw be;
            }
        }

        Session session = em.unwrap(Session.class);
        SQLQuery q = session.createSQLQuery("   select  z.niv_nombre dato, z.desagregacion, sum(z.cdc)/(case sum(z.cd) when 0 then 1 else sum(z.cd) end)*100 cantidad from (  "
                + " (select niv.niv_orden,niv.niv_nombre, " + colDesagregacion + " desagregacion, count(*) cdc, 0 cd    "
                + "   from estadisticas.sg_ext_personal pers    "
                + " join estadisticas.sg_extracciones ext on ( pers.ext_cabezal_fk = ext.ext_pk )    "
                + " join estadisticas.sg_ext_pers_sec extSec on (extSec.sec_ext_cabezal_fk = ext.ext_pk AND extSec.sec_per_fk=pers.ext_per_pk)    "
                + (filtro.getDesagregacion() != null && filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_OFI_PRI) ? " join estadisticas.sg_ext_pers_tipo_sede extTipo on (extTipo.pts_ext_cabezal_fk=ext.ext_pk and extTipo.pts_per_fk=pers.ext_per_pk)  " : "")
                + (filtro.getDesagregacion() != null && filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_PUB_PRI) ? " join estadisticas.sg_ext_pers_sector_sede extTipo on (extTipo.pts_ext_cabezal_fk=ext.ext_pk and extTipo.pts_per_fk=pers.ext_per_pk)  " : "")
                + " 	INNER JOIN centros_educativos.sg_secciones sec ON (extSec.sec_sec_fk = sec.sec_pk)    "
                + " 	INNER JOIN centros_educativos.sg_servicio_educativo se ON (sec.sec_servicio_educativo_fk = se.sdu_pk)    "
                + "     INNER JOIN centros_educativos.sg_sedes sed ON (se.sdu_sede_fk = sed.sed_pk)    "
                + " 	INNER JOIN centros_educativos.sg_grados gra ON (se.sdu_grado_fk = gra.gra_pk)    "
                + " 	INNER JOIN centros_educativos.sg_rel_mod_ed_mod_aten relmodaten ON (gra.gra_relacion_modalidad_fk = relmodaten.rea_pk)    "
                + " 	INNER JOIN centros_educativos.sg_modalidades modedu ON (relmodaten.rea_modalidad_educativa_fk = modedu.mod_pk)    "
                + " 	INNER JOIN centros_educativos.sg_ciclos cic ON (modedu.mod_ciclo = cic.cic_pk)    "
                + " 	INNER JOIN centros_educativos.sg_niveles niv ON (cic.cic_nivel = niv.niv_pk)    "
                + " 	WHERE ext.ext_anio = :anio    "
                + " 	AND ext.ext_nombre_fk = :nombrePk   "
                + " 	and pers.ext_per_tiene_nip "
                + " 	group by niv.niv_orden,niv.niv_nombre,desagregacion)  "
                + " 	union all   "
                + "   (select niv.niv_orden,niv.niv_nombre, " + colDesagregacion + " desagregacion, 0 cdc, count(*) cd    "
                + "   from estadisticas.sg_ext_personal pers    "
                + " join estadisticas.sg_extracciones ext on ( pers.ext_cabezal_fk = ext.ext_pk )    "
                + " join estadisticas.sg_ext_pers_sec extSec on (extSec.sec_ext_cabezal_fk = ext.ext_pk AND extSec.sec_per_fk=pers.ext_per_pk)    "
                + (filtro.getDesagregacion() != null && filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_OFI_PRI) ? " join estadisticas.sg_ext_pers_tipo_sede extTipo on (extTipo.pts_ext_cabezal_fk=ext.ext_pk and extTipo.pts_per_fk=pers.ext_per_pk)  " : "")
                + (filtro.getDesagregacion() != null && filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_PUB_PRI) ? " join estadisticas.sg_ext_pers_sector_sede extTipo on (extTipo.pts_ext_cabezal_fk=ext.ext_pk and extTipo.pts_per_fk=pers.ext_per_pk)  " : "")
                + " 	INNER JOIN centros_educativos.sg_secciones sec ON (extSec.sec_sec_fk = sec.sec_pk)    "
                + " 	INNER JOIN centros_educativos.sg_servicio_educativo se ON (sec.sec_servicio_educativo_fk = se.sdu_pk)    "
                + "     INNER JOIN centros_educativos.sg_sedes sed ON (se.sdu_sede_fk = sed.sed_pk)    "
                + " 	INNER JOIN centros_educativos.sg_grados gra ON (se.sdu_grado_fk = gra.gra_pk)    "
                + " 	INNER JOIN centros_educativos.sg_rel_mod_ed_mod_aten relmodaten ON (gra.gra_relacion_modalidad_fk = relmodaten.rea_pk)    "
                + " 	INNER JOIN centros_educativos.sg_modalidades modedu ON (relmodaten.rea_modalidad_educativa_fk = modedu.mod_pk)    "
                + " 	INNER JOIN centros_educativos.sg_ciclos cic ON (modedu.mod_ciclo = cic.cic_pk)    "
                + " 	INNER JOIN centros_educativos.sg_niveles niv ON (cic.cic_nivel = niv.niv_pk)    "
                + " 	WHERE ext.ext_anio = :anio    "
                + " 	AND ext.ext_nombre_fk = :nombrePk   "
                + " 	group by niv.niv_orden,niv.niv_nombre,desagregacion))  z    "
                + " 	group by z.niv_orden,z.niv_nombre,z.desagregacion  "
                + " 	order by z.niv_orden,z.niv_nombre,z.desagregacion ");

        q.addScalar("dato", new StringType());
        q.addScalar("desagregacion", new StringType());
        q.addScalar("cantidad", new DoubleType());
        q.setParameter("nombrePk", filtro.getNombrePk());
        q.setParameter("anio", filtro.getAnio());
        q.setResultTransformer(Transformers.aliasToBean(EstGenerica.class));
        return q.list();
    }

    // 34 - D-03
    public List<EstGenerica> distribucionPorcentualDocentesSegunAniosServicio(FiltroEstadisticas filtro) throws GeneralException {
        String colDesagregacion = "'-'";
        if (filtro.getDesagregacion() != null) {
            if (filtro.getDesagregacion().equals(EnumDesagregacion.SEXO)) {
                colDesagregacion = "pers.ext_per_sexo_nom";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_OFI_PRI)) {
                colDesagregacion = "extTipo.pts_tipo";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_PUB_PRI)) {
                colDesagregacion = "extTipo.pts_sector";
            } else {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_DESAGREGACION_NO_SOPORTADA);
                throw be;
            }
        }
        Session session = em.unwrap(Session.class);
        SQLQuery q = session.createSQLQuery(" SELECT (CASE WHEN h.dato IS NULL THEN 'Sin dato' ELSE CAST(h.dato AS TEXT) END) as dato, h.desagregacion,"
                + "              (cantidad_por_anio*1.0/cantidad_total * 100) AS cantidad"
                + "         FROM ("
                + "        (SELECT pers.ext_per_antiguedad_func AS dato,"
                + "                " + colDesagregacion + " AS desagregacion,"
                + "                count(*) cantidad_por_anio"
                + "         FROM estadisticas.sg_ext_personal pers"
                + "         INNER JOIN estadisticas.sg_extracciones ext ON (pers.ext_cabezal_fk = ext.ext_pk)"
                + (filtro.getDesagregacion() != null && filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_OFI_PRI) ? " join estadisticas.sg_ext_pers_tipo_sede extTipo on (extTipo.pts_ext_cabezal_fk=ext.ext_pk and extTipo.pts_per_fk=pers.ext_per_pk)  " : "")
                + (filtro.getDesagregacion() != null && filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_PUB_PRI) ? " join estadisticas.sg_ext_pers_sector_sede extTipo on (extTipo.pts_ext_cabezal_fk=ext.ext_pk and extTipo.pts_per_fk=pers.ext_per_pk)  " : "")
                + "         WHERE ext.ext_nombre_fk = :nombrePk"
                + "           AND ext.ext_anio = :anio"
                + "         GROUP BY dato,"
                + "                  desagregacion) h"
                + "         INNER JOIN"
                + "        (SELECT " + colDesagregacion + " AS desagregacion,"
                + "                count(*) AS cantidad_total"
                + "         FROM estadisticas.sg_ext_personal pers"
                + "         INNER JOIN estadisticas.sg_extracciones ext ON (pers.ext_cabezal_fk = ext.ext_pk)"
                + (filtro.getDesagregacion() != null && filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_OFI_PRI) ? " join estadisticas.sg_ext_pers_tipo_sede extTipo on (extTipo.pts_ext_cabezal_fk=ext.ext_pk and extTipo.pts_per_fk=pers.ext_per_pk)  " : "")
                + (filtro.getDesagregacion() != null && filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_PUB_PRI) ? " join estadisticas.sg_ext_pers_sector_sede extTipo on (extTipo.pts_ext_cabezal_fk=ext.ext_pk and extTipo.pts_per_fk=pers.ext_per_pk)  " : "")
                + "         WHERE ext.ext_nombre_fk = :nombrePk"
                + "           AND ext.ext_anio = :anio"
                + "         GROUP BY desagregacion) z ON (h.desagregacion = z.desagregacion)) order by h.dato");

        q.addScalar("dato", new StringType());
        q.addScalar("desagregacion", new StringType());
        q.addScalar("cantidad", new DoubleType());
        q.setParameter("anio", filtro.getAnio());
        q.setParameter("nombrePk", filtro.getNombrePk());
        q.setResultTransformer(Transformers.aliasToBean(EstGenerica.class));
        return q.list();
    }

    // 35 - D-04
    public List<EstGenerica> promedioEstudiantesPorDocente(FiltroEstadisticas filtro) throws GeneralException {
        String colDesagregacion = "'-'";
        String colDesagregacionEst = "'-'";
        if (filtro.getDesagregacion() != null) {

            if (filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_OFI_PRI)) {
                colDesagregacion = "extTipo.pts_tipo";
                colDesagregacionEst = "e.ext_est_sede_tipo";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_PUB_PRI)) {
                colDesagregacion = "extTipo.pts_sector";
                colDesagregacionEst = "e.ext_sed_sector";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SEXO)) {
                colDesagregacion = "pers.ext_per_sexo_nom";
                colDesagregacionEst = "e.ext_est_sexo_nom";
            } else {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_DESAGREGACION_NO_SOPORTADA);
                throw be;
            }
        }
        Session session = em.unwrap(Session.class);
        SQLQuery q = session.createSQLQuery("   select  z.niv_nombre dato, z.desagregacion, sum(z.ca)/(case sum(z.cd) when 0 then 1 else sum(z.cd) end) cantidad from ( "
                + " (SELECT h1.niv_orden, "
                + "                h1.niv_nombre, "
                + "                h1.desagregacion, "
                + "                Count(*) cd, "
                + "                0        ca  "
                + "		 FROM ("
                + "                 select niv.niv_orden,niv.niv_nombre, " + colDesagregacion + " desagregacion  "
                + "                 from estadisticas.sg_ext_personal pers   "
                + "                 join estadisticas.sg_extracciones ext on ( pers.ext_cabezal_fk = ext.ext_pk )   "
                + "                 join estadisticas.sg_ext_pers_sec extSec on (extSec.sec_ext_cabezal_fk = ext.ext_pk AND extSec.sec_per_fk=pers.ext_per_pk)   "
                + (filtro.getDesagregacion() != null && filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_OFI_PRI) ? " join estadisticas.sg_ext_pers_tipo_sede extTipo on (extTipo.pts_ext_cabezal_fk=ext.ext_pk and extTipo.pts_per_fk=pers.ext_per_pk)  " : "")
                + (filtro.getDesagregacion() != null && filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_PUB_PRI) ? " join estadisticas.sg_ext_pers_sector_sede extTipo on (extTipo.pts_ext_cabezal_fk=ext.ext_pk and extTipo.pts_per_fk=pers.ext_per_pk)  " : "")
                + "                 INNER JOIN centros_educativos.sg_secciones sec ON (extSec.sec_sec_fk = sec.sec_pk)   "
                + "                 INNER JOIN centros_educativos.sg_servicio_educativo se ON (sec.sec_servicio_educativo_fk = se.sdu_pk)   "
                + "                 INNER JOIN centros_educativos.sg_sedes sed ON (se.sdu_sede_fk = sed.sed_pk)   "
                + "                 INNER JOIN centros_educativos.sg_grados gra ON (se.sdu_grado_fk = gra.gra_pk)   "
                + "                 INNER JOIN centros_educativos.sg_rel_mod_ed_mod_aten relmodaten ON (gra.gra_relacion_modalidad_fk = relmodaten.rea_pk)   "
                + "                 INNER JOIN centros_educativos.sg_modalidades modedu ON (relmodaten.rea_modalidad_educativa_fk = modedu.mod_pk)   "
                + "                 INNER JOIN centros_educativos.sg_ciclos cic ON (modedu.mod_ciclo = cic.cic_pk)   "
                + "                 INNER JOIN centros_educativos.sg_niveles niv ON (cic.cic_nivel = niv.niv_pk)   "
                + "                 WHERE ext.ext_anio = :anio   "
                + "                 AND ext.ext_nombre_fk = :nombrePk  "
                + "                 group by niv.niv_orden, niv.niv_nombre, pers.ext_per_pk, desagregacion) h1 "
                + "     GROUP  BY h1.niv_orden, "
                + "          h1.niv_nombre, "
                + "          h1.desagregacion "
                + "       )"
                + " 	union all  "
                + "   (select e.ext_est_nivel_orden,e.ext_est_nivel_nom dato, " + colDesagregacionEst + "  desagregacion, 0 cd, count(*) as ca   "
                + " 	from estadisticas.sg_ext_estudiantes e    "
                + " 	JOIN estadisticas.sg_extracciones ex ON ( e.ext_cabezal_fk = ex.ext_pk )     "
                + " 	WHERE ex.ext_anio = :anio   "
                + " 	AND ex.ext_nombre_fk = :nombrePk    "
                + " 	GROUP  BY  dato,e.ext_est_nivel_orden,e.ext_est_nivel,desagregacion))  z   "
                + " 	group by z.niv_orden,z.niv_nombre,z.desagregacion "
                + " 	order by z.niv_orden,z.niv_nombre,z.desagregacion");

        q.addScalar("dato", new StringType());
        q.addScalar("desagregacion", new StringType());
        q.addScalar("cantidad", new DoubleType());
        q.setParameter("anio", filtro.getAnio());
        q.setParameter("nombrePk", filtro.getNombrePk());
        q.setResultTransformer(Transformers.aliasToBean(EstGenerica.class));
        return q.list();
    }

    //   36 - D-05
    public List<EstGenerica> porcentajeDeDocentesPorGradoAcademicoAlcanzado(FiltroEstadisticas filtro) throws GeneralException {

        String colDesagregacion = "'-'";
        if (filtro.getDesagregacion() != null) {
            if (filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_OFI_PRI)) {
                colDesagregacion = "extTipo.pts_tipo";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_PUB_PRI)) {
                colDesagregacion = "extTipo.pts_sector";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SEXO)) {
                colDesagregacion = "pers.ext_per_sexo_nom";
            } else {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_DESAGREGACION_NO_SOPORTADA);
                throw be;
            }
        }

        Session session = em.unwrap(Session.class);
        SQLQuery q = session.createSQLQuery("select z.dato as dato, z.desagregacion, (sum(a)*1.0/sum(b)) cantidad from ( "
                + "(select pers.ext_per_nivel_educativo_nom dato, " + colDesagregacion + " desagregacion, count(*) a, 0 b "
                + " from estadisticas.sg_ext_personal pers   "
                + " join estadisticas.sg_extracciones ext on ( pers.ext_cabezal_fk = ext.ext_pk )  "
                + " join estadisticas.sg_ext_pers_sec extSec on (extSec.sec_ext_cabezal_fk = ext.ext_pk AND extSec.sec_per_fk=pers.ext_per_pk)  "
                + (filtro.getDesagregacion() != null && filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_OFI_PRI) ? " join estadisticas.sg_ext_pers_tipo_sede extTipo on (extTipo.pts_ext_cabezal_fk=ext.ext_pk and extTipo.pts_per_fk=pers.ext_per_pk)  " : "")
                + (filtro.getDesagregacion() != null && filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_PUB_PRI) ? " join estadisticas.sg_ext_pers_sector_sede extTipo on (extTipo.pts_ext_cabezal_fk=ext.ext_pk and extTipo.pts_per_fk=pers.ext_per_pk)  " : "")
                + " INNER JOIN centros_educativos.sg_secciones sec ON (extSec.sec_sec_fk = sec.sec_pk) "
                + " INNER JOIN centros_educativos.sg_servicio_educativo se ON (sec.sec_servicio_educativo_fk = se.sdu_pk) "
                + " INNER JOIN centros_educativos.sg_sedes sed ON (se.sdu_sede_fk = sed.sed_pk) "
                + " where ext.ext_nombre_fk = :nombrePk and ext.ext_anio=:anio  and pers.ext_per_nivel_educativo is not null     "
                + " group by pers.ext_per_nivel_educativo_nom,desagregacion  "
                + " order by pers.ext_per_nivel_educativo_nom,desagregacion) "
                + " union all  "
                + " ( select pers.ext_per_nivel_educativo_nom dato, " + colDesagregacion + " desagregacion, 0 a, count(*) b "
                + " from estadisticas.sg_ext_personal pers   "
                + " join estadisticas.sg_extracciones ext on ( pers.ext_cabezal_fk = ext.ext_pk )  "
                + " join estadisticas.sg_ext_pers_sec extSec on (extSec.sec_ext_cabezal_fk = ext.ext_pk AND extSec.sec_per_fk=pers.ext_per_pk)  "
                + (filtro.getDesagregacion() != null && filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_OFI_PRI) ? " join estadisticas.sg_ext_pers_tipo_sede extTipo on (extTipo.pts_ext_cabezal_fk=ext.ext_pk and extTipo.pts_per_fk=pers.ext_per_pk)  " : "")
                + (filtro.getDesagregacion() != null && filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_PUB_PRI) ? " join estadisticas.sg_ext_pers_sector_sede extTipo on (extTipo.pts_ext_cabezal_fk=ext.ext_pk and extTipo.pts_per_fk=pers.ext_per_pk)  " : "")
                + " INNER JOIN centros_educativos.sg_secciones sec ON (extSec.sec_sec_fk = sec.sec_pk) "
                + " INNER JOIN centros_educativos.sg_servicio_educativo se ON (sec.sec_servicio_educativo_fk = se.sdu_pk) "
                + " INNER JOIN centros_educativos.sg_sedes sed ON (se.sdu_sede_fk = sed.sed_pk) "
                + " where ext.ext_nombre_fk = :nombrePk and ext.ext_anio=:anio    "
                + " group by pers.ext_per_nivel_educativo_nom,desagregacion  "
                + " order by pers.ext_per_nivel_educativo_nom,desagregacion) ) z "
                + " where z.dato is not null "
                + " group by z.dato, z.desagregacion "
                + " order by z.dato, z.desagregacion"
        );

        q.addScalar("dato", new StringType());
        q.addScalar("desagregacion", new StringType());
        q.addScalar("cantidad", new DoubleType());
        q.setParameter("nombrePk", filtro.getNombrePk());
        q.setParameter("anio", filtro.getAnio());
        q.setResultTransformer(Transformers.aliasToBean(EstGenerica.class));
        return q.list();
    }

    //38 - RE-02
    public List<EstGenerica> porcentajeDeCentrosEducativosConAccesoServiciosBasicos(FiltroEstadisticas filtro) throws GeneralException {

        Session session = em.unwrap(Session.class);

        String colDesagregacion = "'-'";
        if (filtro.getDesagregacion() != null) {
            if (filtro.getDesagregacion().equals(EnumDesagregacion.DEPARTAMENTO)) {
                colDesagregacion = "sed.ext_sed_departamento_nom";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.ZONA)) {
                colDesagregacion = "sed.ext_sed_zona_nom";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_OFI_PRI)) {
                colDesagregacion = "sed.ext_sed_tipo";
            } else if (filtro.getDesagregacion().equals(EnumDesagregacion.SECTOR_PUB_PRI)) {
                colDesagregacion = "sed.ext_sed_sector";
            } else {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_DESAGREGACION_NO_SOPORTADA);
                throw be;
            }
        }

        SQLQuery q = session.createSQLQuery("with cantidad as ( "
                + "select z.dato, z.desagregacion, (sum(a)*1.0/sum(b))*100 cantidad from (select serv.sba_sba_nombre dato,  " + colDesagregacion + " desagregacion, count(*) a, 0 b from estadisticas.sg_ext_sedes sed  "
                + "join estadisticas.sg_extracciones ex on (ex.ext_pk=sed.ext_cabezal_fk) "
                + "join estadisticas.sg_ext_sede_servicios_basicos serv on (serv.sba_ext_cabezal_fk=ex.ext_pk and serv.sba_sede_fk=sed.ext_sed_pk) "
                + "where serv.sba_tiene_servicio is true and ex.ext_anio=:anio and ex.ext_nombre_fk=:nombrePk "
                + "group by dato, desagregacion "
                + "union all "
                + "select serv.sba_sba_nombre dato,  " + colDesagregacion + " desagregacion, 0 a, count(*) b from estadisticas.sg_ext_sedes sed  "
                + "join estadisticas.sg_extracciones ex on (ex.ext_pk=sed.ext_cabezal_fk) "
                + "join estadisticas.sg_ext_sede_servicios_basicos serv on (serv.sba_ext_cabezal_fk=ex.ext_pk and serv.sba_sede_fk=sed.ext_sed_pk) "
                + "where  ex.ext_anio=:anio and ex.ext_nombre_fk=:nombrePk "
                + "group by dato, desagregacion) z group by z.dato, z.desagregacion), "
                + "total as ( "
                + "select inf.sin_nombre dato, " + colDesagregacion + " desagregacion, 0 cantidad from estadisticas.sg_ext_sedes sed  "
                + "join estadisticas.sg_extracciones ex on (ex.ext_pk=sed.ext_cabezal_fk) "
                + "cross join catalogo.sg_servicios_infraestructura inf  "
                + "group by dato, desagregacion  "
                + "order by dato, desagregacion) "
                + "select dato, desagregacion, cantidad from cantidad "
                + "union  "
                + "select dato, desagregacion,cantidad from total where not exists (select 1 from cantidad where total.dato = dato and total.desagregacion = desagregacion) "
                + "group by dato, desagregacion,cantidad "
                + "order by dato, desagregacion,cantidad");

        q.addScalar("dato", new StringType());
        q.addScalar("desagregacion", new StringType());
        q.addScalar("cantidad", new DoubleType());
        q.setParameter("anio", filtro.getAnio());
        q.setParameter("nombrePk", filtro.getNombrePk());
        q.setResultTransformer(Transformers.aliasToBean(EstGenerica.class));
        return q.list();
    }

    public boolean existeExtraccionEstudiantes(Integer anio, Long nombrePk) {
        // validar que exista la extracción. Se verifica que para el dataset estudiantes exista extracción.
        Session session = em.unwrap(Session.class);
        SQLQuery existeExtr = session.createSQLQuery("SELECT 1 FROM estadisticas.sg_ext_estudiantes e "
                + "JOIN estadisticas.sg_extracciones ex ON (e.ext_cabezal_fk = ex.ext_pk) "
                + "WHERE ex.ext_anio = :anio AND ex.ext_nombre_fk=:nombrePk ");
        existeExtr.setParameter("anio", anio);
        existeExtr.setParameter("nombrePk", nombrePk);
        List ext = existeExtr.list();
        if (ext == null || ext.size() == 0) {
            BusinessException be = new BusinessException();
            be.addError(Errores.ERROR_NO_EXISTE_EXTRACCION);
            throw be;
        }
        return true;
    }

    public boolean existeExtraccionPersonales(Integer anio, Long nombrePk) {
        // validar que exista la extracción. Se verifica que para el dataset personal exista extracción.
        Session session = em.unwrap(Session.class);
        SQLQuery existeExtr = session.createSQLQuery("SELECT 1 FROM estadisticas.sg_ext_personal e "
                + "JOIN estadisticas.sg_extracciones ex ON (e.ext_cabezal_fk = ex.ext_pk) "
                + "WHERE ex.ext_anio = :anio AND ex.ext_nombre_fk=:nombrePk ");
        existeExtr.setParameter("anio", anio);
        existeExtr.setParameter("nombrePk", nombrePk);
        List ext = existeExtr.list();
        if (ext == null || ext.size() == 0) {
            BusinessException be = new BusinessException();
            be.addError(Errores.ERROR_NO_EXISTE_EXTRACCION);
            throw be;
        }
        return true;
    }

    public List<EstGenerica> porcentajeDeEstudiantesSegunNivelDeLogroEnPAES(FiltroEstadisticas filtro) throws GeneralException {
        if (filtro.getDesagregacion() != null) {
            BusinessException be = new BusinessException();
            be.addError(Errores.ERROR_DESAGREGACION_NO_SOPORTADA);
            throw be;
        }

        Session session = em.unwrap(Session.class);

//        SQLQuery qglobal = session.createSQLQuery("select dato, desagregacion, (h2.count::::decimal / h4.count::::decimal * 100) as cantidad from\n"
//                + "\n"
//                + "(select 'Global' as dato,\n"
//                + "    CASE\n"
//                + "      WHEN promedio <= 3.75 THEN 'Básico'\n"
//                + "      WHEN promedio <= 7.5 THEN 'Intermedio'\n"
//                + "      ELSE 'Superior'\n"
//                + "    end  AS desagregacion,\n"
//                + "    count(*)\n"
//                + "    from\n"
//                + "(select  calest.cae_estudiante_fk, AVG(calest.cae_calificacion::::decimal) promedio\n"
//                + "from centros_educativos.sg_calificaciones_estudiante calest\n"
//                + "INNER JOIN centros_educativos.sg_calificaciones cal ON (calest.cae_calificacion_fk = cal.cal_pk)\n"
//                + "INNER JOIN centros_educativos.sg_componente_plan_estudio cpe ON (cal.cal_componente_plan_estudio_fk = cpe.cpe_pk)\n"
//                + "INNER JOIN centros_educativos.sg_secciones sec ON (cal.cal_seccion_fk = sec.sec_pk)\n"
//                + "INNER JOIN centros_educativos.sg_anio_lectivo ale ON (sec.sec_anio_lectivo_fk = ale.ale_pk)\n"
//                + "where cpe.cpe_es_paes and cpe.cpe_tipo_paes = 'PAES' and ale.ale_anio = :anio and cal.cal_tipo_periodo_calificacion = 'APR'\n"
//                + "group by calest.cae_estudiante_fk) h1\n"
//                + "group by desagregacion) h2\n"
//                + "\n"
//                + "CROSS JOIN\n"
//                + "\n"
//                + "(select count(*) from\n"
//                + "(select  calest.cae_estudiante_fk\n"
//                + "from centros_educativos.sg_calificaciones_estudiante calest\n"
//                + "INNER JOIN centros_educativos.sg_calificaciones cal ON (calest.cae_calificacion_fk = cal.cal_pk)\n"
//                + "INNER JOIN centros_educativos.sg_componente_plan_estudio cpe ON (cal.cal_componente_plan_estudio_fk = cpe.cpe_pk)\n"
//                + "INNER JOIN centros_educativos.sg_secciones sec ON (cal.cal_seccion_fk = sec.sec_pk)\n"
//                + "INNER JOIN centros_educativos.sg_anio_lectivo ale ON (sec.sec_anio_lectivo_fk = ale.ale_pk)\n"
//                + "where cpe.cpe_es_paes and cpe.cpe_tipo_paes = 'PAES' and ale.ale_anio = :anio and cal.cal_tipo_periodo_calificacion = 'APR'\n"
//                + "group by calest.cae_estudiante_fk) h3) h4 ");
//
//        qglobal.addScalar("dato", new StringType());
//        qglobal.addScalar("desagregacion", new StringType());
//        qglobal.addScalar("cantidad", new DoubleType());
//        qglobal.setParameter("anio", filtro.getAnio());
//        qglobal.setResultTransformer(Transformers.aliasToBean(EstGenerica.class));
//
//        List<EstGenerica> genglobal = qglobal.list();
        SQLQuery qcomponentes = session.createSQLQuery("select h1.cpe_nombre as dato, h1.desagregacion, (h1.count::::decimal / h2.cantidad_total::::decimal * 100) as cantidad from "
                + "(select cpe.cpe_nombre, cpe.cpe_pk,            "
                + "		CASE "
                + "          WHEN calest.cae_calificacion::::decimal <= 3.75 THEN 'Básico' "
                + "          WHEN calest.cae_calificacion::::decimal <= 7.5 THEN 'Intermedio' "
                + "          ELSE 'Superior' "
                + "        end  AS desagregacion, "
                + "		count(*) "
                + "from centros_educativos.sg_calificaciones_estudiante calest "
                + "INNER JOIN centros_educativos.sg_calificaciones cal ON (calest.cae_calificacion_fk = cal.cal_pk) "
                + "INNER JOIN centros_educativos.sg_componente_plan_estudio cpe ON (cal.cal_componente_plan_estudio_fk = cpe.cpe_pk) "
                + "INNER JOIN centros_educativos.sg_secciones sec ON (cal.cal_seccion_fk = sec.sec_pk) "
                + "INNER JOIN centros_educativos.sg_anio_lectivo ale ON (sec.sec_anio_lectivo_fk = ale.ale_pk) "
                + "where cpe.cpe_es_paes and cpe_tipo_paes = 'PAES' and ale.ale_anio = :anio and cal.cal_tipo_periodo_calificacion = 'APR' "
                + "group by cpe.cpe_pk, desagregacion) h1 "
                + " "
                + "INNER JOIN "
                + " "
                + "(select cpe.cpe_nombre, cpe.cpe_pk,     "
                + "	   count(*) as cantidad_total "
                + "from centros_educativos.sg_calificaciones_estudiante calest "
                + "INNER JOIN centros_educativos.sg_calificaciones cal ON (calest.cae_calificacion_fk = cal.cal_pk) "
                + "INNER JOIN centros_educativos.sg_componente_plan_estudio cpe ON (cal.cal_componente_plan_estudio_fk = cpe.cpe_pk) "
                + "INNER JOIN centros_educativos.sg_secciones sec ON (cal.cal_seccion_fk = sec.sec_pk) "
                + "INNER JOIN centros_educativos.sg_anio_lectivo ale ON (sec.sec_anio_lectivo_fk = ale.ale_pk) "
                + "where cpe.cpe_es_paes and cpe_tipo_paes = 'PAES' and ale.ale_anio = :anio and cal.cal_tipo_periodo_calificacion = 'APR' "
                + "group by cpe.cpe_pk) h2 "
                + " "
                + "ON (h1.cpe_pk = h2.cpe_pk) ");

        qcomponentes.addScalar("dato", new StringType());
        qcomponentes.addScalar("desagregacion", new StringType());
        qcomponentes.addScalar("cantidad", new DoubleType());
        qcomponentes.setParameter("anio", filtro.getAnio());
        qcomponentes.setResultTransformer(Transformers.aliasToBean(EstGenerica.class));

        List<EstGenerica> gencomponentes = qcomponentes.list();

        //gencomponentes.addAll(genglobal);
        return gencomponentes;
    }
}
