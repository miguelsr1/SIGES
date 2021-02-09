/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.dto.SgDetalleSede;
import sv.gob.mined.siges.persistencia.entidades.SgSistemaSede;
import sv.gob.mined.siges.enumerados.EnumSeccionEstado;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroSistemaSede;
import sv.gob.mined.siges.persistencia.entidades.centros.SgDireccion;
import sv.gob.mined.siges.persistencia.entidades.centros.SgSede;
import sv.gob.mined.siges.enumerados.TipoSede;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgDepartamento;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgMunicipio;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgTipoCalendarioEscolar;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgZona;

@Stateless
public class SistemaSedeBean {

    private static final Logger LOGGER = Logger.getLogger(SistemaSedeBean.class.getName());

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgSede
     * @throws GeneralException
     */
    public SgSede obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                String query = "SELECT sed.sed_pk " //0
                        + ",sed.sed_codigo " //1
                        + ",sed.sed_nombre " //2
                        + ",sed.sed_tipo " //3 
                        + ",sed.sed_version " //4
                        + ",sed_telefono " //5
                        + ",sed_telefono_movil " //6
                        + ",cal.tce_nombre " //7
                        + ",(select string_agg(jor.jla_nombre, ',') from "
                        + "centros_educativos.sg_sedes_jornadas sj "
                        + "inner join catalogo.sg_jornadas_laborales jor on (sj.jla_pk = jor.jla_pk) "
                        + "where sj.sed_pk = sed.sed_pk "
                        + ") jornadas " //8
                        + ",sed.sed_internet " //9
                        + ",zon.zon_nombre " //10
                        + ",dep.dep_nombre " //11
                        + ",mun.mun_nombre " //12
                        + " FROM centros_educativos.sg_sedes sed "
                        + " INNER JOIN catalogo.sg_tipos_calendario_escolar cal ON (sed.sed_tipo_calendario_fk = cal.tce_pk) "
                        + " INNER JOIN centros_educativos.sg_direcciones dir ON (sed.sed_direccion_fk = dir.dir_pk) "
                        + " INNER JOIN catalogo.sg_departamentos dep ON (dir.dir_departamento = dep.dep_pk) "
                        + " INNER JOIN catalogo.sg_municipios mun ON (dep.dep_pk = mun.mun_departamento_fk and mun.mun_pk = dir_municipio) "
                        + " INNER JOIN catalogo.sg_zonas zon ON (dir.dir_zona = zon.zon_pk) "
                        + " WHERE sed.sed_pk = :sedPk";

                Query q = em.createNativeQuery(query);
                q.setParameter("sedPk", id);

                Object[] ob = (Object[]) q.getSingleResult();

                SgDireccion dir = new SgDireccion();
                dir.setDirZona(new SgZona());
                dir.getDirZona().setZonNombre((String) ob[10]);
                dir.setDirDepartamento(new SgDepartamento());
                dir.getDirDepartamento().setDepNombre((String) ob[11]);
                dir.setDirMunicipio(new SgMunicipio());
                dir.getDirMunicipio().setMunNombre((String) ob[12]);

                SgSede sed = new SgSede();
                sed.setSedPk(((BigInteger) ob[0]).longValue());
                sed.setSedCodigo(String.valueOf(ob[1]));
                sed.setSedNombre(String.valueOf(ob[2]));
                sed.setSedTipo(TipoSede.valueOf((String) ob[3]));
                sed.setSedDireccion(dir);
                sed.setSedVersion((Integer) ob[4]);
                sed.setSedTelefono((String) ob[5]);
                sed.setSedTelefonoMovil((String) ob[6]);
                sed.setSedTipoCalendario(new SgTipoCalendarioEscolar());
                sed.getSedTipoCalendario().setTceNombre((String) ob[7]);
                sed.setJornadas((String) ob[8]);
                sed.setSedInternet((Boolean) ob[9]);

                List<SgDetalleSede> detalle = new ArrayList<>();

                //Se buscan los servicios educativos por nivel de la sede
                query = "select nivel.niv_orden, " //0
                        + "nivel.niv_pk, " //1
                        + "nivel.niv_nombre, " //2
                        + "servicio.sdu_pk, " //3
                        + "modalidad.mod_nombre, " //4
                        + "modalidad.mod_codigo, " //5
                        + "nivel.niv_codigo, " // 6
                        + "(select count(*) from centros_educativos.sg_modalidades sm where sm.mod_ciclo=ciclo.cic_pk) total_modalidades "
                        + //7
                        " from "
                        + " centros_educativos.sg_servicio_educativo servicio "
                        + " inner join centros_educativos.sg_sedes sede on sede.sed_pk = servicio.sdu_sede_fk "
                        + " inner join centros_educativos.sg_grados grado on grado.gra_pk = servicio.sdu_grado_fk "
                        + " inner join centros_educativos.sg_rel_mod_ed_mod_aten rel on rel.rea_pk = grado.gra_relacion_modalidad_fk "
                        + " inner join centros_educativos.sg_modalidades modalidad on modalidad.mod_pk = rel.rea_modalidad_educativa_fk "
                        + " inner join centros_educativos.sg_ciclos ciclo on ciclo.cic_pk = modalidad.mod_ciclo "
                        + " inner join centros_educativos.sg_niveles nivel on nivel.niv_pk = ciclo.cic_nivel "
                        + " where sede.sed_pk = :sedPk"
                        + " order by nivel.niv_orden asc";

                q = em.createNativeQuery(query);
                q.setParameter("sedPk", id);

                List<Long> serviciosPk = new ArrayList<>();

                List<Object[]> objs = (List<Object[]>) q.getResultList();
                for (Object[] var : objs) {
                    serviciosPk.add(((BigInteger) var[3]).longValue());

                    SgDetalleSede det = new SgDetalleSede();
                    det.setNivOrden((Integer) var[0]);
                    det.setNivPk(((BigInteger) var[1]).longValue());
                    det.setNivNombre((String) var[2]);
                    det.setSduPk(((BigInteger) var[3]).longValue());
                    det.setModalidad((String) var[4]);
                    det.setCodModalidad((String) var[5]);
                    det.setNivCodigo((String) var[6]);
                    det.setTotalModalidaes(((BigInteger) var[7]).longValue());

                    detalle.add(det);
                }

                //Buscar la cantidad de secciones abiertas por cada servicio educativo. Puede ser que no hayan servicios => no retorno nada (where false)
                query = "select sec_servicio_educativo_fk, count(*)  total "
                        + " from "
                        + " centros_educativos.sg_secciones "
                        + " where "
                        + (serviciosPk.size() > 0 ? " sec_servicio_educativo_fk in (:serviciosPk) " : " false ")
                        + " and sec_estado=:secEstado "
                        + " group by sec_servicio_educativo_fk ";
                q = em.createNativeQuery(query);
                if (serviciosPk.size() > 0) {
                    q.setParameter("serviciosPk", serviciosPk);
                }
                q.setParameter("secEstado", EnumSeccionEstado.ABIERTA.toString());

                objs = (List<Object[]>) q.getResultList();
                for (Object[] var : objs) {
                    detalle.stream().filter((det) -> (det.getSduPk().equals(((BigInteger) var[0]).longValue()))).forEachOrdered((det) -> {
                        det.setTotalSecciones(((BigInteger) var[1]).longValue());
                    });
                }

                //Buscar la cantidad de estudiantes con matricula activa por cada servicio educativo
                query = "select sec.sec_servicio_educativo_fk, "
                        + " count(sex.sex_codigo) filter (where sex.sex_codigo=:codFem) femenino,  "
                        + " count(sex.sex_codigo) filter (where sex.sex_codigo=:codMas) masculino  "
                        + " from "
                        + " centros_educativos.sg_estudiantes est "
                        + " inner join centros_educativos.sg_matriculas mat on (est.est_pk = mat.mat_estudiante_fk) "
                        + " inner join centros_educativos.sg_secciones sec on (sec.sec_pk = mat.mat_seccion_fk) "
                        + " inner join centros_educativos.sg_personas per on (per.per_pk = est.est_persona) "
                        + " inner join catalogo.sg_sexos sex on (sex.sex_pk = per.per_sexo_fk) "
                        + " where "
                        + (serviciosPk.size() > 0 ? " sec_servicio_educativo_fk in (:serviciosPk) " : " false ")
                        //     + "sec.sec_servicio_educativo_fk in (:serviciosPk) "
                        + " and sec.sec_estado = :secEstado "
                        + " group by sec.sec_servicio_educativo_fk";
                q = em.createNativeQuery(query);
                if (serviciosPk.size() > 0) {
                    q.setParameter("serviciosPk", serviciosPk);
                }
                q.setParameter("secEstado", EnumSeccionEstado.ABIERTA.toString());
                q.setParameter("codFem", Constantes.CODIGO_SEXO_FEMENINO);
                q.setParameter("codMas", Constantes.CODIGO_SEXO_MASCULINO);
                objs = (List<Object[]>) q.getResultList();
                for (Object[] var : objs) {
                    detalle.stream().filter((det) -> (det.getSduPk().equals(((BigInteger) var[0]).longValue()))).map((det) -> {
                        det.setTotalFemenino(((BigInteger) var[1]).longValue());
                        return det;
                    }).forEachOrdered((det) -> {
                        det.setTotalMasculino(((BigInteger) var[2]).longValue());
                    });
                }

                //Buscar la cantidad de docentes con matricula activa por cada servicio educativo
//                query = "select sec.sec_servicio_educativo_fk, "
//                        + " count(sex.sex_codigo) filter (where sex.sex_codigo=:codFem) femenino,  "
//                        + " count(sex.sex_codigo) filter (where sex.sex_codigo=:codMas) masculino  "
//                        + " from centros_educativos.sg_horarios_escolares hr "
//                        + " inner join centros_educativos.sg_secciones sec on (sec.sec_pk = hr.hes_seccion_fk) "
//                        + " inner join (select * from (select distinct(cd.cdo_pk),cd.cdo_horario_escolar_fk,cd.cdo_docente_fk from centros_educativos.sg_componentes_docentes cd) as e) as cddistinct on (cddistinct.cdo_horario_escolar_fk=hr.hes_pk) "
//                        + " inner join centros_educativos.sg_personal_sede_educativa pse on (pse.pse_pk = cddistinct.cdo_docente_fk) " // personal sede educativa 
//                        + " inner join centros_educativos.sg_personas per on (per.per_pk = pse.pse_persona_fk) "
//                        + " inner join catalogo.sg_sexos sex on (sex.sex_pk = per.per_sexo_fk) "
//                        + " where sec.sec_servicio_educativo_fk in (:serviciosPk) "
//                        + " and sec.sec_estado = :secEstado "
//                        + " group by sec.sec_servicio_educativo_fk";
// evita los docentes duplicados debido a que en horario escolar puede estar repetido.
                query = "with horarios_aux as "
                        + "(select * from  (select cd.cdo_docente_fk as docente_fk, cd.cdo_horario_escolar_fk as horario_fk,"
                        + "hr.hes_seccion_fk seccion_fk"
                        + " from centros_educativos.sg_horarios_escolares hr "
                        + " inner join centros_educativos.sg_componentes_docentes cd on (cd.cdo_horario_escolar_fk=hr.hes_pk)"
                        + " where cd.cdo_docente_fk is not null "
                        + " union "
                        + " select hr.hes_docente_fk, hr.hes_pk, hr.hes_seccion_fk "
                        + " from centros_educativos.sg_horarios_escolares hr "
                        + " where hr.hes_unico_docente is true) ss "
                        + " group by docente_fk, horario_fk, seccion_fk "
                        + " order by docente_fk, horario_fk)"
                        + " "
                        + " select sec_servicio_educativo_fk, "
                        + " count(sex_codigo) filter (where sex_codigo=:codFem) femenino,  "
                        + " count(sex_codigo) filter (where sex_codigo=:codMas) masculino "
                        + // la sub query debajo evita los duplicados al agrupar por servicio. Arriba se hace un count sin duplicados debido al group by
                        " from ("
                        + "select sec.sec_servicio_educativo_fk, "
                        + "	per.per_pk, sex.sex_codigo "
                        + " from horarios_aux hr "
                        + " inner join centros_educativos.sg_secciones sec on (sec.sec_pk = hr.seccion_fk) "
                        + " inner join centros_educativos.sg_personal_sede_educativa pse on (pse.pse_pk = hr.docente_fk)  "
                        + " inner join centros_educativos.sg_personas per on (per.per_pk = pse.pse_persona_fk) "
                        + " inner join catalogo.sg_sexos sex on (sex.sex_pk = per.per_sexo_fk) "
                        + " where "
                        + (serviciosPk.size() > 0 ? " sec.sec_servicio_educativo_fk in (:serviciosPk) " : " false ")
                        //      + "sec.sec_servicio_educativo_fk in (:serviciosPk) "
                        + "and  sec.sec_estado = :secEstado "
                        + " group by sec.sec_servicio_educativo_fk, per.per_pk,sex.sex_codigo "
                        + "  order by sec.sec_servicio_educativo_fk, per.per_pk,sex.sex_codigo "
                        + ") e group by sec_servicio_educativo_fk";
                q = em.createNativeQuery(query);
                if (serviciosPk.size() > 0) {
                    q.setParameter("serviciosPk", serviciosPk);
                }
                q.setParameter("secEstado", EnumSeccionEstado.ABIERTA.toString());
                q.setParameter("codFem", Constantes.CODIGO_SEXO_FEMENINO);
                q.setParameter("codMas", Constantes.CODIGO_SEXO_MASCULINO);
                objs = (List<Object[]>) q.getResultList();
                for (Object[] var : objs) {
                    detalle.stream().filter((det) -> (det.getSduPk().equals(((BigInteger) var[0]).longValue()))).map((det) -> {
                        det.setTotalDocentesFemenino(((BigInteger) var[1]).longValue());
                        return det;
                    }).forEachOrdered((det) -> {
                        det.setTotalDocentesMasculino(((BigInteger) var[2]).longValue());
                    });
                }

                //Si el ciclo de cada nivel tiene solo 1 modalidad, unir los resultados por nivel
                detalle.stream().filter((det) -> (det.getTotalModalidaes().equals(1L))).map((det) -> {
                    det.setCodModalidad("0");
                    return det;
                }).forEachOrdered((det) -> {
                    det.setModalidad(null);
                });
                List<SgDetalleSede> datosFinales = new ArrayList<>();
                detalle.forEach((det) -> {
                    SgDetalleSede d = datosFinales
                            .stream()
                            .filter((p) -> p.getNivPk().equals(det.getNivPk()) && p.getCodModalidad().equals(det.getCodModalidad()))
                            .findFirst()
                            .orElse(null);
                    if (d == null) {
                        datosFinales.add(det);
                    } else {
                        Long ts = (d.getTotalSecciones() != null ? d.getTotalSecciones() : 0) + (det.getTotalSecciones() != null ? det.getTotalSecciones() : 0);
                        Long tf = (d.getTotalFemenino() != null ? d.getTotalFemenino() : 0) + (det.getTotalFemenino() != null ? det.getTotalFemenino() : 0);
                        Long tm = (d.getTotalMasculino() != null ? d.getTotalMasculino() : 0) + (det.getTotalMasculino() != null ? det.getTotalMasculino() : 0);

                        Long tfd = (d.getTotalDocentesFemenino() != null ? d.getTotalDocentesFemenino() : 0) + (det.getTotalDocentesFemenino() != null ? det.getTotalDocentesFemenino() : 0);
                        Long tmd = (d.getTotalDocentesMasculino() != null ? d.getTotalDocentesMasculino() : 0) + (det.getTotalDocentesMasculino() != null ? det.getTotalDocentesMasculino() : 0);

                        d.setTotalSecciones(ts);
                        d.setTotalFemenino(tf);
                        d.setTotalMasculino(tm);

                        d.setTotalDocentesFemenino(tfd);
                        d.setTotalDocentesMasculino(tmd);

                        datosFinales.set(datosFinales.indexOf(d), d);
                    }
                });

                //Se setea la entidad en la sede.
                sed.setDetalles(datosFinales);

                return sed;
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return null;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param entity SgSistemaSede
     * @return SgSistemaSede
     * @throws GeneralException
     */
    public Boolean guardar(SgSistemaSede entity) throws GeneralException {
        try {
            if (entity != null) {
                em.createNativeQuery("INSERT INTO sistemas_integrados.sg_sistemas_sedes "
                        + "VALUES (:sinPk, :sedPk)")
                        .setParameter("sinPk", entity.getSinPk().getSinPk())
                        .setParameter("sedPk", entity.getSedPk().getSedPk())
                        .executeUpdate();
                return Boolean.TRUE;
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return Boolean.FALSE;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroSistemaSede filtro) throws GeneralException {
        try {
            BigInteger cant = (BigInteger) em.createNativeQuery("SELECT COUNT(*) from sistemas_integrados.sg_sistemas_sedes m "
                    + " where m.sin_pk = :sinPk")
                    .setParameter("sinPk", filtro.getSistema())
                    .getSingleResult();
            return cant.longValue();
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param sistemas
     * @return Long
     * @throws GeneralException
     */
    public HashMap<Long, Long> obtenerTotalPorSistema(List<Long> sistemas) throws GeneralException {
        try {

            HashMap<Long, Long> totales = new HashMap<>();
            List<Object[]> objs = (List<Object[]>) em.createNativeQuery("select m.sin_pk, COUNT(m.sed_pk) total "
                    + "from sistemas_integrados.sg_sistemas_sedes m  "
                    + "where m.sin_pk in (:sinPk) "
                    + "group by m.sin_pk")
                    .setParameter("sinPk", sistemas)
                    .getResultList();
            objs.forEach((ob) -> {
                totales.put(((BigInteger) ob[0]).longValue(), ((BigInteger) ob[1]).longValue());
            });

            return totales;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgSistemaSede
     * @throws GeneralException
     */
    public List<SgSede> obtenerPorFiltro(FiltroSistemaSede filtro) throws GeneralException {
        try {
            String query = "SELECT sed.sed_pk, sed.sed_codigo, "
                    + "sed.sed_nombre, sed.sed_tipo, sed.sed_version, "
                    + "dir.dir_latitud, dir.dir_Longitud,"
                    + " dep.dep_nombre,mun.mun_nombre"
                    + " FROM sistemas_integrados.sg_sistemas_sedes m "
                    + " INNER JOIN sistemas_integrados.sg_sistemas_integrados sis ON (m.sin_pk = sis.sin_pk) "
                    + " INNER JOIN centros_educativos.sg_sedes sed ON (m.sed_pk = sed.sed_pk) "
                    + " INNER JOIN centros_educativos.sg_direcciones dir ON (sed.sed_direccion_fk = dir.dir_pk)"
                    + " INNER JOIN catalogo.sg_departamentos dep on dep.dep_pk=dir.dir_departamento"
                    + " INNER JOIN catalogo.sg_municipios mun on mun.mun_pk=dir.dir_municipio "
                    + " WHERE m.sin_pk = :sinPk";
            String[] orden = filtro.getOrderBy();
            boolean[] asc = filtro.getAscending();
            if (orden != null && orden.length > 0) {
                query += " order by ";
                for (int i = 0; i < orden.length; i++) {
                    String a = " desc";
                    if (asc[i] == true) {
                        a = " asc ";
                    }
                    switch (orden[i]) {
                        case "sedPk":
                            query += " sed.sed_pk " + a;
                            break;
                        case "sedCodigo":
                            query += " sed.sed_codigo " + a;
                            break;
                        case "sedNombre":
                            query += " sed.sed_nombre " + a;
                            break;
                    }
                }
            }

            if (filtro.getMaxResults() != null) {
                query += " LIMIT " + filtro.getMaxResults();
            }

            if (filtro.getFirst() != null) {
                query += " OFFSET " + filtro.getFirst();
            }

            Query q = em.createNativeQuery(query);

            q.setParameter("sinPk", filtro.getSistema());

            List<SgSede> ret = new ArrayList<>();

            List<Object[]> objs = (List<Object[]>) q.getResultList();
            for (Object[] ob : objs) {
                SgDireccion dir = new SgDireccion();
                dir.setDirLatitud(ob[5] != null ? ((BigDecimal) ob[5]).doubleValue() : null);
                dir.setDirLongitud(ob[6] != null ? ((BigDecimal) ob[6]).doubleValue() : null);
                SgDepartamento dep = new SgDepartamento();
                dep.setDepNombre((String) ob[7]);
                SgMunicipio mun = new SgMunicipio();
                mun.setMunNombre((String) ob[8]);
                dir.setDirDepartamento(dep);
                dir.setDirMunicipio(mun);
                SgSede sed = new SgSede();
                sed.setSedPk(((BigInteger) ob[0]).longValue());
                sed.setSedCodigo(String.valueOf(ob[1]));
                sed.setSedNombre(String.valueOf(ob[2]));
                sed.setSedTipo(TipoSede.valueOf((String) ob[3]));
                sed.setSedVersion((Integer) ob[4]);
                sed.setSedDireccion(dir);

                ret.add(sed);
            }
            return ret;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Elimina el objeto con la id indicada
     *
     * @param sinPk
     * @param sedPk
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public void eliminar(Long sinPk, Long sedPk) throws GeneralException {
        if (sinPk != null && sedPk != null) {
            try {
                //Borrar calificaciones
                Query q = em.createNativeQuery("DELETE FROM sistemas_integrados.sg_sistemas_sedes "
                        + "WHERE sin_pk = :sinPk AND sed_pk = :sedPk");
                q.setParameter("sinPk", sinPk);
                q.setParameter("sedPk", sedPk);

                q.executeUpdate();
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
