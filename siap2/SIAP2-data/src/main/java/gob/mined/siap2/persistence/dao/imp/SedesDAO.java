package gob.mined.siap2.persistence.dao.imp;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.entities.data.impl.Beneficiarios;
import gob.mined.siges.entities.data.impl.SgSede;
import gob.mined.siap2.entities.data.impl.TopePresupuestal;
import gob.mined.siap2.entities.data.impl.TopePresupuestalMatriculasDetalleGroup;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.persistence.dao.imp.eclipselink.EclipselinkJpaDAOImp;
import gob.mined.siap2.utils.generalutils.BooleanUtils;
import gob.mined.siges.entities.data.impl.SgModalidad;
import gob.mined.siges.entities.data.impl.SgModalidadAtencion;
import gob.mined.siges.entities.data.impl.SgNivel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.Query;

@JPADAO
public class SedesDAO extends EclipselinkJpaDAOImp<TopePresupuestal, Integer> implements Serializable {

    /**
     * Este método verifica si existe un registro de sg_sede y obtiene su PK
     *
     * @param codigo
     * @return
     * @throws gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException
     */
    public SgSede getSedeByCodigo(String codigo) throws DAOGeneralException {
        try {
            return (SgSede) entityManager
                    .createQuery("select g from SgSede g where g.codigo = :cod")
                    .setParameter("cod", codigo)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        } catch (Exception e) {
            throw e;
        }
    }

    public List<Object[]> getCantidadMatriculasPorSede(Date fechaMatricula, List<Integer> sedesPk, List<Beneficiarios> beneficiarios, Boolean incluirAdscritas) {
        try {
            
            String sedesPkString = "";

            for (int i = 0; i < sedesPk.size(); i++) {

                if (i == sedesPk.size() - 1) {
                    sedesPkString += sedesPk.get(i);
                } else {
                    sedesPkString += sedesPk.get(i) + ",";
                }
            }

            String sedesWhere = "";
            if (BooleanUtils.isTrue(incluirAdscritas)) {
                sedesWhere = " sede.sed_pk IN ( s.sed_pk ) OR sede.sed_centro_adscrito IN ( s.sed_pk )";
            } else {
                sedesWhere = " sede.sed_pk IN ( s.sed_pk ) ";
            }

            String whereModalidades = " AND (";

            for (Beneficiarios b : beneficiarios) {
                if (b.getSgSubModalidad() != null) {
                    whereModalidades += " OR ( relmodaten.rea_modalidad_educativa_fk = " + b.getSgModalidades().getId() + " AND relmodaten.rea_sub_modalidad_atencion_fk = " + b.getSgSubModalidad().getId() + " )";
                } else {
                    whereModalidades += " OR ( relmodaten.rea_modalidad_educativa_fk = " + b.getSgModalidades().getId() + " AND relmodaten.rea_modalidad_atencion_fk = " + b.getSgModalidadAtencion().getId() + " )";
                }
            }

            whereModalidades += ")";
            whereModalidades = whereModalidades.replaceFirst("OR", "");

            String query = "SELECT s.sed_pk AS sed_pk,"
                    + "       (SELECT Count(*)"
                    + "        FROM   centros_educativos.sg_matriculas mat"
                    + "               INNER JOIN centros_educativos.sg_secciones sec ON ( mat.mat_seccion_fk = sec.sec_pk )"
                    + "               INNER JOIN centros_educativos.sg_servicio_educativo sdu ON ( sdu.sdu_pk = sec.sec_servicio_educativo_fk )"
                    + "               INNER JOIN centros_educativos.sg_sedes sede ON ( sdu.sdu_sede_fk = sede.sed_pk )"
                    + "               INNER JOIN centros_educativos.sg_grados gra ON ( sdu.sdu_grado_fk = gra.gra_pk )"
                    + "               INNER JOIN centros_educativos.sg_rel_mod_ed_mod_aten relmodaten ON ( gra.gra_relacion_modalidad_fk = relmodaten.rea_pk )"
                    + "        WHERE  mat.mat_anulada = false and mat.mat_fecha_ingreso <= ? and (mat.mat_fecha_hasta is null or mat.mat_fecha_hasta >= ?)" + whereModalidades
                    + "               AND (" + sedesWhere+ ")"
                    + "        )"
                    + " FROM   centros_educativos.sg_sedes s"
                    + " WHERE  s.sed_pk IN (" + sedesPkString + ")  ";


            return entityManager.createNativeQuery(query)
                    .setParameter(1, fechaMatricula)
                    .setParameter(2, fechaMatricula)
                    .getResultList();

        } catch (Exception e) {
            throw e;
        }
    }

    public List<Object[]> getCantidadMatriculasPorSede(List<Integer> sedesPk, Boolean incluirAdscritas) {
        try {
            
            String sedesPkString = "";

            for (int i = 0; i < sedesPk.size(); i++) {

                if (i == sedesPk.size() - 1) {
                    sedesPkString += sedesPk.get(i);
                } else {
                    sedesPkString += sedesPk.get(i) + ",";
                }
            }

            String sedesWhere = "";
            if (BooleanUtils.isTrue(incluirAdscritas)) {
                sedesWhere = " sede.sed_pk IN ( s.sed_pk ) OR sede.sed_centro_adscrito IN ( s.sed_pk )";
            } else {
                sedesWhere = " sede.sed_pk IN ( s.sed_pk ) ";
            }


            String query = "SELECT s.sed_pk AS sed_pk,"
                    + "       (SELECT Count(*)"
                    + "        FROM   centros_educativos.sg_matriculas mat"
                    + "               INNER JOIN centros_educativos.sg_secciones sec ON ( mat.mat_seccion_fk = sec.sec_pk )"
                    + "               INNER JOIN centros_educativos.sg_servicio_educativo sdu ON ( sdu.sdu_pk = sec.sec_servicio_educativo_fk )"
                    + "               INNER JOIN centros_educativos.sg_sedes sede ON ( sdu.sdu_sede_fk = sede.sed_pk )"
                    + "               INNER JOIN centros_educativos.sg_grados gra ON ( sdu.sdu_grado_fk = gra.gra_pk )"
                    + "               INNER JOIN centros_educativos.sg_rel_mod_ed_mod_aten relmodaten ON ( gra.gra_relacion_modalidad_fk = relmodaten.rea_pk )"
                    + "        WHERE  mat.mat_anulada = false and mat.mat_fecha_ingreso <= ? and (mat.mat_fecha_hasta is null or mat.mat_fecha_hasta >= ?)" 
                    + "               AND (" + sedesWhere+ ")"
                    + "        )"
                    + " FROM   centros_educativos.sg_sedes s"
                    + " WHERE  s.sed_pk IN (" + sedesPkString + ")  ";


            return entityManager.createNativeQuery(query).getResultList();

        } catch (Exception e) {
            throw e;
        }
    }
    public List<TopePresupuestalMatriculasDetalleGroup> getDetalleCantidadMatriculasPorSede(Integer sedesPk, Date fechaMatricula, List<Beneficiarios> benef) {
        List<TopePresupuestalMatriculasDetalleGroup> resultado = new ArrayList();
        try {
            
            String whereModalidades = " AND (";

            for (Beneficiarios b : benef) {
                if (b.getSgSubModalidad() != null) {
                    whereModalidades += " OR ( relmodaten.rea_modalidad_educativa_fk = " + b.getSgModalidades().getId() + " AND relmodaten.rea_sub_modalidad_atencion_fk = " + b.getSgSubModalidad().getId() + " )";
                } else {
                    whereModalidades += " OR ( relmodaten.rea_modalidad_educativa_fk = " + b.getSgModalidades().getId() + " AND relmodaten.rea_modalidad_atencion_fk = " + b.getSgModalidadAtencion().getId() + " )";
                }
            }

            whereModalidades += ")";
            whereModalidades = whereModalidades.replaceFirst("OR", "");
            
            
            String query = "select nivel.niv_pk,nivel.niv_version,modedu.mod_pk,modedu.mod_version,modate.mat_pk,modate.mat_version, Count(*) " +
                "FROM   centros_educativos.sg_matriculas mat " +
                " INNER JOIN centros_educativos.sg_secciones sec ON ( mat.mat_seccion_fk = sec.sec_pk )" +
                " INNER JOIN centros_educativos.sg_servicio_educativo sdu ON ( sdu.sdu_pk = sec.sec_servicio_educativo_fk )" +
                " INNER JOIN centros_educativos.sg_sedes sede ON ( sdu.sdu_sede_fk = sede.sed_pk )" +
                " INNER JOIN centros_educativos.sg_grados gra ON ( sdu.sdu_grado_fk = gra.gra_pk )" +
                " INNER JOIN centros_educativos.sg_rel_mod_ed_mod_aten relmodaten ON ( gra.gra_relacion_modalidad_fk = relmodaten.rea_pk )" +
                " INNER JOIN centros_educativos.sg_modalidades modedu on (modedu.mod_pk=relmodaten.rea_modalidad_educativa_fk)" +
                " INNER JOIN catalogo.sg_modalidades_atencion modate on (modate.mat_pk = relmodaten.rea_modalidad_atencion_fk )" +
                " INNER JOIN centros_educativos.sg_ciclos ciclo on (ciclo.cic_pk=modedu.mod_ciclo)" +
                " INNER JOIN centros_educativos.sg_niveles nivel on (nivel.niv_pk=ciclo.cic_nivel)" +
                " WHERE mat.mat_anulada = false and mat.mat_fecha_ingreso <= ? and (mat.mat_fecha_hasta is null or mat.mat_fecha_hasta >= ?) "
                    + " and sede.sed_pk = " + sedesPk + whereModalidades +
                "group by nivel.niv_pk,nivel.niv_version,modedu.mod_pk,modedu.mod_version,modate.mat_pk,modate.mat_version";
            Query q = entityManager.createNativeQuery(query)
            .setParameter(1, fechaMatricula)
            .setParameter(2, fechaMatricula);
                    
            TopePresupuestalMatriculasDetalleGroup tp;
            for(Object obj : q.getResultList()){
                Object[] obar = (Object[]) obj;
                Long nivId = (Long)(obar[0]);
                Long modId = (Long)(obar[2]);
                Long modAtId = (Long)(obar[4]);
                Long matriculas = (Long)(obar[6]);
                
                SgNivel nivel = new SgNivel();
                nivel.setId(nivId.intValue());
                nivel.setVersion((Integer) obar[1]);
                
                SgModalidad modalidadEducativa = new SgModalidad();
                modalidadEducativa.setId(modId.intValue());
                modalidadEducativa.setVersion((Integer) obar[3]);
                
                SgModalidadAtencion modalidadAtencion = new SgModalidadAtencion();
                modalidadAtencion.setId(modAtId.intValue());
                modalidadAtencion.setVersion((Integer) obar[5]);
                
                tp = new TopePresupuestalMatriculasDetalleGroup();
                tp.setNivel(nivel);
                tp.setModalidadEducativa(modalidadEducativa);
                tp.setModalidadAtencion(modalidadAtencion);
                tp.setCantidadMatriculas(matriculas.intValue());
                resultado.add(tp);
            }

            return resultado;
        } catch (Exception e) {
            throw e;
        }
    }
    
    
}
