/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Stateless;
import org.eclipse.microprofile.opentracing.Traced;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import sv.gob.mined.siges.enumerados.EnumMes;
import sv.gob.mined.siges.enumerados.EnumRiesgoAlerta;
import sv.gob.mined.siges.enumerados.EnumVariableAlerta;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.graficos.DataColumna;
import sv.gob.mined.siges.graficos.Escala;
import sv.gob.mined.siges.graficos.GraficoColumnas;
import sv.gob.mined.siges.graficos.GraficoRadar;
import sv.gob.mined.siges.graficos.Tick;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgDepartamento;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgMunicipio;

@Stateless
@Traced
public class GraficosBean {

    @PersistenceContext
    private EntityManager em;

    /**
     *
     * @return GraficoColumnas
     * @throws GeneralException
     */
    public GraficoColumnas obtenerEvolucionEncuestasYMatriculas(Long departamento) throws GeneralException {
        try {
            GraficoColumnas gc = new GraficoColumnas();

            if (departamento != null) {

                String departamentosq = "select m from SgMunicipio m where m.munHabilitado = true and m.munDepartamento.depPk = :depPk order by m.munNombre";
                List<SgMunicipio> municipios = em.createQuery(departamentosq, SgMunicipio.class).setParameter("depPk", departamento).getResultList();

                String querye = " select mun.mun_pk, sum(cantidad) as cantidad  "
                        + "from centros_educativos.sg_encuestas_matriculas_abiertas_por_dep_mun_sexo t "
                        + " INNER JOIN catalogo.sg_municipios mun ON (t.dir_municipio = mun.mun_pk)"
                        + " where t.dir_departamento = :depPk"
                        + " group by mun.mun_pk"
                        + " order by mun.mun_nombre";

                Query qe = em.createNativeQuery(querye).setParameter("depPk", departamento);

                String querym = " select mun.mun_pk, sum(cantidad) as cantidad  "
                        + "from centros_educativos.sg_matriculas_abiertas_por_dep_mun_sexo t "
                        + " INNER JOIN catalogo.sg_municipios mun ON (t.dir_municipio = mun.mun_pk)"
                        + " where t.dir_departamento = :depPk"
                        + " group by mun.mun_pk"
                        + " order by mun.mun_nombre";

                Query qm = em.createNativeQuery(querym).setParameter("depPk", departamento);

                HashMap<Long, Long> municipiosCantidadEncuestas = new HashMap<>();
                HashMap<Long, Long> dmunicipiosCantidadMatriculas = new HashMap<>();

                List<Object[]> rese = qe.getResultList();
                for (Object[] ob : rese) {
                    Long munPk = ((BigInteger) ob[0]).longValue();
                    Long cantidad = ((BigDecimal) ob[1]).longValue();
                    municipiosCantidadEncuestas.putIfAbsent(munPk, cantidad);
                }

                List<Object[]> resm = qm.getResultList();
                for (Object[] ob : resm) {
                    Long munPk = ((BigInteger) ob[0]).longValue();
                    Long cantidad = ((BigDecimal) ob[1]).longValue();
                    dmunicipiosCantidadMatriculas.putIfAbsent(munPk, cantidad);
                }
                for (SgMunicipio m : municipios) {

                    Long cantMat = dmunicipiosCantidadMatriculas.get(m.getMunPk());
                    Long cantEnc = municipiosCantidadEncuestas.get(m.getMunPk());

                    Double cantMatD = cantMat != null ? cantMat.doubleValue() : 0;
                    Double cantEncD = cantEnc != null ? cantEnc.doubleValue() : 0;

                    Double[] cantidades = new Double[]{cantMatD, cantEncD};

                    gc.getValores().add(new DataColumna(m.getMunNombre(), cantidades));

                }
                gc.setEtiquetasValor(new String[]{"Matrículas", "Encuestas"});

                gc.setEtiquetaRotulo("Municipios");

                return gc;

            } else {

                String departamentosq = "select d from SgDepartamento d where d.depHabilitado = true order by d.depNombre";
                List<SgDepartamento> departamentos = em.createQuery(departamentosq, SgDepartamento.class).getResultList();

                String querye = " select dep.dep_pk, sum(cantidad) as cantidad  "
                        + "from centros_educativos.sg_encuestas_matriculas_abiertas_por_dep_mun_sexo t "
                        + " INNER JOIN catalogo.sg_departamentos dep ON (t.dir_departamento = dep.dep_pk)"
                        + " group by dep.dep_pk"
                        + " order by dep.dep_nombre";

                Query qe = em.createNativeQuery(querye);

                String querym = " select dep.dep_pk, sum(cantidad) as cantidad  "
                        + "from centros_educativos.sg_matriculas_abiertas_por_dep_mun_sexo t "
                        + " INNER JOIN catalogo.sg_departamentos dep ON (t.dir_departamento = dep.dep_pk)"
                        + " group by dep.dep_pk"
                        + " order by dep.dep_nombre";

                Query qm = em.createNativeQuery(querym);

                HashMap<Long, Long> departamentosCantidadEncuestas = new HashMap<>();
                HashMap<Long, Long> departamentosCantidadMatriculas = new HashMap<>();

                List<Object[]> rese = qe.getResultList();
                for (Object[] ob : rese) {
                    Long depPk = ((BigInteger) ob[0]).longValue();
                    Long cantidad = ((BigDecimal) ob[1]).longValue();
                    departamentosCantidadEncuestas.putIfAbsent(depPk, cantidad);
                }

                List<Object[]> resm = qm.getResultList();
                for (Object[] ob : resm) {
                    Long depPk = ((BigInteger) ob[0]).longValue();
                    Long cantidad = ((BigDecimal) ob[1]).longValue();
                    departamentosCantidadMatriculas.putIfAbsent(depPk, cantidad);
                }
                for (SgDepartamento d : departamentos) {

                    Long cantMat = departamentosCantidadMatriculas.get(d.getDepPk());
                    Long cantEnc = departamentosCantidadEncuestas.get(d.getDepPk());

                    Double cantMatD = cantMat != null ? cantMat.doubleValue() : 0;
                    Double cantEncD = cantEnc != null ? cantEnc.doubleValue() : 0;

                    Double[] cantidades = new Double[]{cantMatD, cantEncD};

                    gc.getValores().add(new DataColumna(d.getDepNombre(), cantidades));

                }
                gc.setEtiquetasValor(new String[]{"Matrículas", "Encuestas"});

                gc.setEtiquetaRotulo("Departamentos");

                return gc;

            }

        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     *
     * @return GraficoColumnas
     * @throws GeneralException
     */
    public GraficoColumnas obtenerEvolucionEncuestasPorSexo(Long departamento) throws GeneralException {
        try {
            GraficoColumnas gc = new GraficoColumnas();

            if (departamento != null) {

                String departamentosq = "select m from SgMunicipio m where m.munHabilitado = true and m.munDepartamento.depPk = :depPk order by m.munNombre";
                List<SgMunicipio> municipios = em.createQuery(departamentosq, SgMunicipio.class).setParameter("depPk", departamento).getResultList();

                String query = " select mun.mun_pk, sex.sex_pk, sex.sex_nombre, sum(cantidad) as cantidad  "
                        + "from centros_educativos.sg_encuestas_matriculas_abiertas_por_dep_mun_sexo t "
                        + " INNER JOIN catalogo.sg_municipios mun ON (t.dir_municipio = mun.mun_pk)"
                        + " INNER JOIN catalogo.sg_sexos sex ON (t.per_sexo_fk = sex.sex_pk) "
                        + " where t.dir_departamento = :depPk"
                        + " group by mun.mun_pk, sex.sex_pk"
                        + " order by mun.mun_nombre";

                Query q = em.createNativeQuery(query).setParameter("depPk", departamento);

                List<Object[]> res = q.getResultList();

                HashMap<Long, HashMap<Long, Long>> municipiosSexoCantidad = new HashMap<>();

                List<Long> sexosOrdenados = new ArrayList<>();
                HashMap<Long, String> sexosNombres = new HashMap<>();

                for (Object[] ob : res) {

                    Long munPk = ((BigInteger) ob[0]).longValue();
                    Long sexPk = ((BigInteger) ob[1]).longValue();
                    String sexNombre = (String) ob[2];
                    Long cantidad = ((BigDecimal) ob[3]).longValue();

                    if (!sexosOrdenados.contains(sexPk)) {
                        sexosOrdenados.add(sexPk);
                    }

                    sexosNombres.putIfAbsent(sexPk, sexNombre);
                    municipiosSexoCantidad.computeIfAbsent(munPk, s -> new HashMap<>()).putIfAbsent(sexPk, cantidad);

                }

                if (sexosOrdenados.isEmpty()) {
                    return gc;
                }

                for (SgMunicipio m : municipios) {

                    HashMap<Long, Long> sexosCantidad = municipiosSexoCantidad.get(m.getMunPk());

                    Double[] cantidades = new Double[sexosOrdenados.size()];
                    Arrays.fill(cantidades, 0d);

                    for (Long sexPk : sexosOrdenados) {
                        if (sexosCantidad != null && sexosCantidad.get(sexPk) != null) {
                            cantidades[sexosOrdenados.indexOf(sexPk)] = sexosCantidad.get(sexPk).doubleValue();
                        }
                    }

                    gc.getValores().add(new DataColumna(m.getMunNombre(), cantidades));

                }

                String[] sexosNombresArray = new String[sexosOrdenados.size()];

                for (Long sexPk : sexosOrdenados) {
                    sexosNombresArray[sexosOrdenados.indexOf(sexPk)] = sexosNombres.get(sexPk);
                }
                gc.setEtiquetasValor(sexosNombresArray);

                gc.setEtiquetaRotulo("Municipios");

                return gc;

            } else {

                String departamentosq = "select d from SgDepartamento d where d.depHabilitado = true order by d.depNombre";
                List<SgDepartamento> departamentos = em.createQuery(departamentosq, SgDepartamento.class).getResultList();

                String query = " select dep.dep_pk, sex.sex_pk, sex.sex_nombre, sum(cantidad) as cantidad  "
                        + "from centros_educativos.sg_encuestas_matriculas_abiertas_por_dep_mun_sexo t "
                        + " INNER JOIN catalogo.sg_departamentos dep ON (t.dir_departamento = dep.dep_pk)"
                        + " INNER JOIN catalogo.sg_sexos sex ON (t.per_sexo_fk = sex.sex_pk) "
                        + " group by dep.dep_pk, sex.sex_pk"
                        + " order by dep.dep_nombre";

                Query q = em.createNativeQuery(query);

                List<Object[]> res = q.getResultList();

                HashMap<Long, HashMap<Long, Long>> departamentosSexoCantidad = new HashMap<>();

                List<Long> sexosOrdenados = new ArrayList<>();
                HashMap<Long, String> sexosNombres = new HashMap<>();

                for (Object[] ob : res) {

                    Long depPk = ((BigInteger) ob[0]).longValue();
                    Long sexPk = ((BigInteger) ob[1]).longValue();
                    String sexNombre = (String) ob[2];
                    Long cantidad = ((BigDecimal) ob[3]).longValue();

                    if (!sexosOrdenados.contains(sexPk)) {
                        sexosOrdenados.add(sexPk);
                    }

                    sexosNombres.putIfAbsent(sexPk, sexNombre);
                    departamentosSexoCantidad.computeIfAbsent(depPk, s -> new HashMap<>()).putIfAbsent(sexPk, cantidad);

                }

                for (SgDepartamento d : departamentos) {

                    HashMap<Long, Long> sexosCantidad = departamentosSexoCantidad.get(d.getDepPk());

                    Double[] cantidades = new Double[sexosOrdenados.size()];
                    Arrays.fill(cantidades, 0d);

                    for (Long sexPk : sexosOrdenados) {
                        if (sexosCantidad != null && sexosCantidad.get(sexPk) != null) {
                            cantidades[sexosOrdenados.indexOf(sexPk)] = sexosCantidad.get(sexPk).doubleValue();
                        }
                    }

                    gc.getValores().add(new DataColumna(d.getDepNombre(), cantidades));

                }

                String[] sexosNombresArray = new String[sexosOrdenados.size()];

                for (Long sexPk : sexosOrdenados) {
                    sexosNombresArray[sexosOrdenados.indexOf(sexPk)] = sexosNombres.get(sexPk);
                }
                gc.setEtiquetasValor(sexosNombresArray);

                gc.setEtiquetaRotulo("Departamentos");

                return gc;

            }

        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve alertas actuales de estudiante
     *
     * @param estudiantePk Long
     * @return GraficoColumnas
     * @throws GeneralException
     */
    public GraficoRadar obtenerAlertasActuales(Long estudiantePk) throws GeneralException {
        try {
            if (estudiantePk != null) {

                GraficoRadar gc = new GraficoRadar();

                String query = "select  ale_variable, ale_riesgo  from alertas.sg_alertas where ale_estudiante_fk = :estPk";

                Query q = em.createNativeQuery(query)
                        .setParameter("estPk", estudiantePk);

                List<Object[]> res = q.getResultList();

                HashMap<EnumVariableAlerta, Integer> posiciones = new HashMap<>();
                posiciones.put(EnumVariableAlerta.EMBARAZO, 0);
                posiciones.put(EnumVariableAlerta.MADRE, 1);
                posiciones.put(EnumVariableAlerta.ACOMPANIADO, 2);
                posiciones.put(EnumVariableAlerta.MATRIMONIO, 3);
                posiciones.put(EnumVariableAlerta.ASISTENCIA, 4);
                posiciones.put(EnumVariableAlerta.SOBREEDAD, 5);
                posiciones.put(EnumVariableAlerta.TRABAJO, 6);
                posiciones.put(EnumVariableAlerta.MANIFESTACION_VIOLENCIA, 7);
                posiciones.put(EnumVariableAlerta.DESEMPENIO, 8);

                Double[] valores = new Double[9];
                Arrays.fill(valores, 0d);

                for (Object[] ob : res) {

                    EnumVariableAlerta variable = EnumVariableAlerta.valueOf((String) ob[0]);
                    EnumRiesgoAlerta riesgo = EnumRiesgoAlerta.valueOf((String) ob[1]);

                    valores[posiciones.get(variable)] = Double.valueOf(riesgo.getValor());
                }
                gc.setValores(valores);
                gc.setEtiquetasValor(new String[]{"Embarazo", "Madre/Padre", "Acompañado", "Matrimonio", "Asistencia", "Sobreedad", "Trabajo", "Manifestación de Violencia", "Desempeño"});
                return gc;
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return null;
    }

    /**
     *
     * @param estudiantePk Long
     * @return GraficoColumnas
     * @throws GeneralException
     */
    public GraficoColumnas obtenerAsistenciasPorAnio(Long estudiantePk) throws GeneralException {
        try {
            if (estudiantePk != null) {

                GraficoColumnas gc = new GraficoColumnas();

                //TODO: query dinamica segun año actual
                String query = "";

                for (int i = 2018; i <= LocalDate.now().getYear(); i++) {

                    query += " select " + i + " as anio, asistencias,inasistencias_justificadas, inasistencias_no_justificadas  "
                            + "from centros_educativos.sg_estudiantes_asistencias_" + i + " t "
                            + "where estudiante = :estPk ";

                    if (i < LocalDate.now().getYear()) {
                        query += " UNION ALL ";
                    }
                }

                Query q = em.createNativeQuery(query)
                        .setParameter("estPk", estudiantePk);

                List<Object[]> res = q.getResultList();

                for (Object[] ob : res) {

                    Integer anio = (Integer) ob[0];
                    Double asistencias = ((BigInteger) ob[1]).doubleValue();
                    Double inasistenciasJustificadas = ((BigInteger) ob[2]).doubleValue();
                    Double inasistenciasInjustificadas = ((BigInteger) ob[3]).doubleValue();

                    gc.getValores().add(new DataColumna(anio, new Double[]{asistencias, inasistenciasJustificadas, inasistenciasInjustificadas}));
                }

                gc.setEtiquetasValor(new String[]{"Asistencias", "Inasistencias justificadas", "Inasistencias injustificadas"});
                gc.setEtiquetaRotulo("Años");

                return gc;
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return null;
    }

    /**
     *
     * @param estudiantePk Long
     * @param anio Integer
     * @return GraficoColumnas
     * @throws GeneralException
     */
    public GraficoColumnas obtenerAsistenciasPorMesEnAnio(Long estudiantePk, Integer anio) throws GeneralException {
        try {
            if (estudiantePk != null) {

                GraficoColumnas gc = new GraficoColumnas();

                String query = " select mes, asistencias,inasistencias_justificadas, inasistencias_no_justificadas  "
                        + "from centros_educativos.sg_estudiantes_asistencias_meses_" + anio + " t "
                        + "where estudiante = :estPk ";

                Query q = em.createNativeQuery(query)
                        .setParameter("estPk", estudiantePk);

                List<Object[]> res = q.getResultList();

                for (Object[] ob : res) {

                    Integer mes = (Integer) ob[0];
                    Double asistencias = ((BigInteger) ob[1]).doubleValue();
                    Double inasistenciasJustificadas = ((BigInteger) ob[2]).doubleValue();
                    Double inasistenciasInjustificadas = ((BigInteger) ob[3]).doubleValue();

                    gc.getValores().add(new DataColumna(EnumMes.getByNum(mes).getShortText(), new Double[]{asistencias, inasistenciasJustificadas, inasistenciasInjustificadas}));
                }

                gc.setEtiquetasValor(new String[]{"Asistencias", "Inasistencias justificadas", "Inasistencias injustificadas"});
                gc.setEtiquetaRotulo("Meses");

                return gc;
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return null;
    }

    /**
     *
     * @param estudiantePk Long
     * @return GraficoColumnas
     * @throws GeneralException
     */
    public GraficoColumnas obtenerMotivosInasistenciasPorAnio(Long estudiantePk) throws GeneralException {
        try {
            if (estudiantePk != null) {

                GraficoColumnas gc = new GraficoColumnas();

                //TODO: query dinamica segun año actual
                String query = "";

                for (int i = 2018; i <= LocalDate.now().getYear(); i++) {

                    query += " select " + i + " as anio, min_pk, min_nombre, sum(cantidad)::::integer as cantidad  "
                            + " from centros_educativos.sg_estudiantes_mot_inasistencias_meses_" + i + " t "
                            + " where estudiante = :estPk "
                            + " group by anio, min_pk, min_nombre";

                    if (i < LocalDate.now().getYear()) {
                        query += " UNION ALL ";
                    }
                }

                query += " order by anio";

                Query q = em.createNativeQuery(query)
                        .setParameter("estPk", estudiantePk);

                List<Long> motivosInasistenciaOrdenados = new ArrayList<>();
                HashMap<Long, String> motivosNombre = new HashMap<>();
                HashMap<Integer, HashMap<Long, Double>> aniosMotivosCantidad = new HashMap<>();
                List<Integer> aniosOrdenados = new ArrayList<>();

                List<Object[]> res = q.getResultList();

                for (Object[] ob : res) {

                    Integer anio = (Integer) ob[0];
                    Long minPk = ((BigInteger) ob[1]).longValue();
                    String minNombre = (String) ob[2];
                    Double cantidad = ((Integer) ob[3]).doubleValue();

                    if (!motivosInasistenciaOrdenados.contains(minPk)) {
                        motivosInasistenciaOrdenados.add(minPk);
                    }
                    if (!aniosOrdenados.contains(anio)) {
                        aniosOrdenados.add(anio);
                    }
                    motivosNombre.putIfAbsent(minPk, minNombre);
                    aniosMotivosCantidad.computeIfAbsent(anio, s -> new HashMap<>()).putIfAbsent(minPk, cantidad);
                }

                for (Integer anio : aniosOrdenados) {

                    HashMap<Long, Double> motivosCantidad = aniosMotivosCantidad.get(anio);

                    Double[] motivos = new Double[motivosInasistenciaOrdenados.size()];
                    for (Long motPk : motivosCantidad.keySet()) {
                        motivos[motivosInasistenciaOrdenados.indexOf(motPk)] = motivosCantidad.get(motPk);
                    }

                    gc.getValores().add(new DataColumna(anio, motivos));

                }

                String[] motivosNombresNombres = new String[motivosInasistenciaOrdenados.size()];

                for (Long motPk : motivosInasistenciaOrdenados) {
                    motivosNombresNombres[motivosInasistenciaOrdenados.indexOf(motPk)] = motivosNombre.get(motPk);
                }
                gc.setEtiquetasValor(motivosNombresNombres);

                gc.setEtiquetaRotulo("Motivos");

                return gc;
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return null;
    }

    /**
     *
     * @param estudiantePk Long
     * @param anio Integer
     * @return GraficoColumnas
     * @throws GeneralException
     */
    public GraficoColumnas obtenerMotivosInasistenciasPorMesEnAnio(Long estudiantePk, Integer anio) throws GeneralException {
        try {
            if (estudiantePk != null) {

                GraficoColumnas gc = new GraficoColumnas();

                String query = " select mes, min_pk, min_nombre, cantidad::::integer"
                        + " from centros_educativos.sg_estudiantes_mot_inasistencias_meses_" + anio + " t "
                        + " where estudiante = :estPk"
                        + " order by mes ";

                Query q = em.createNativeQuery(query)
                        .setParameter("estPk", estudiantePk);

                List<Long> motivosInasistenciaOrdenados = new ArrayList<>();
                HashMap<Long, String> motivosNombre = new HashMap<>();
                HashMap<Integer, HashMap<Long, Double>> mesesMotivosCantidad = new HashMap<>();

                List<Object[]> res = q.getResultList();

                for (Object[] ob : res) {

                    Integer mes = (Integer) ob[0];
                    Long minPk = ((BigInteger) ob[1]).longValue();
                    String minNombre = (String) ob[2];
                    Double cantidad = ((Integer) ob[3]).doubleValue();

                    if (!motivosInasistenciaOrdenados.contains(minPk)) {
                        motivosInasistenciaOrdenados.add(minPk);
                    }
                    motivosNombre.putIfAbsent(minPk, minNombre);
                    mesesMotivosCantidad.computeIfAbsent(mes, s -> new HashMap<>()).putIfAbsent(minPk, cantidad);
                }

                for (int i = 1; i <= 12; i++) {

                    if (mesesMotivosCantidad.containsKey(i)) {

                        HashMap<Long, Double> motivosCantidad = mesesMotivosCantidad.get(i);

                        Double[] motivos = new Double[motivosInasistenciaOrdenados.size()];
                        for (Long motPk : motivosCantidad.keySet()) {
                            motivos[motivosInasistenciaOrdenados.indexOf(motPk)] = motivosCantidad.get(motPk);
                        }

                        gc.getValores().add(new DataColumna(EnumMes.getByNum(i).getShortText(), motivos));
                    }

                }

                String[] motivosNombresNombres = new String[motivosInasistenciaOrdenados.size()];

                for (Long motPk : motivosInasistenciaOrdenados) {
                    motivosNombresNombres[motivosInasistenciaOrdenados.indexOf(motPk)] = motivosNombre.get(motPk);
                }
                gc.setEtiquetasValor(motivosNombresNombres);

                gc.setEtiquetaRotulo("Motivos");

                return gc;
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return null;
    }

    /**
     * Obtener histórico de calificaciones APR numéricas para estudiante por
     * grado
     *
     * @param estudiantePk Long
     * @return GraficoColumnas
     * @throws GeneralException
     */
    public GraficoColumnas obtenerCalificacionesNumericasAprobacionEstudiantePorGrado(Long estudiantePk) throws GeneralException {
        try {
            if (estudiantePk != null) {

                GraficoColumnas gc = new GraficoColumnas();

                String querys = "select gra.gra_pk, gra.gra_nombre, AVG(calest.cae_calificacion::::decimal) "
                        + "                        from centros_educativos.sg_calificaciones_estudiante calest  "
                        + "                        INNER JOIN centros_educativos.sg_calificaciones cabezal ON (calest.cae_calificacion_fk = cabezal.cal_pk) "
                        + "                        INNER JOIN centros_educativos.sg_secciones sec ON (cabezal.cal_seccion_fk = sec.sec_pk) "
                        + "                        INNER JOIN centros_educativos.sg_servicio_educativo sdu ON (sec.sec_servicio_educativo_fk = sdu.sdu_pk) "
                        + "                        INNER JOIN centros_educativos.sg_grados gra ON (sdu.sdu_grado_fk = gra.gra_pk) "
                        + "                        INNER JOIN centros_educativos.sg_componente_plan_estudio cpe ON (cpe.cpe_pk = cabezal.cal_componente_plan_estudio_fk) "
                        + "                        INNER JOIN centros_educativos.sg_componente_plan_grado cpg ON (cpg.cpg_componente_plan_estudio_fk = cpe.cpe_pk AND cpg.cpg_grado_fk = gra.gra_pk AND cpg.cpg_plan_estudio_fk = sec.sec_plan_estudio_fk and (cpg.cpg_exclusivo_seccion is null or cpg.cpg_exclusivo_seccion = sec.sec_pk)) "
                        + "                        where calest.cae_calificacion is not null and (cpg.cpg_aplica_grafica_evolucion) and cabezal.cal_tipo_periodo_calificacion = 'APR' "
                        + "                        AND calest.cae_estudiante_fk = :estPk "
                        + "		           group by gra.gra_pk "
                        + "                        order by gra_orden";

                Query q = em.createNativeQuery(querys)
                        .setParameter("estPk", estudiantePk);

                List<Object[]> res = q.getResultList();

                for (Object[] ob : res) {

                    String graNombre = (String) ob[1];
                    Double calificacion = ((BigDecimal) ob[2]).doubleValue();

                    gc.getValores().add(new DataColumna(graNombre, new Double[]{calificacion}));
                }

                gc.setEtiquetasValor(new String[]{"Promedio"});
                gc.setEtiquetaRotulo("Grados");

                return gc;
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return null;
    }

    /**
     * Obtener histórico de calificaciones APR numéricas para estudiante por
     * grado comparadas contra promedio nacional
     *
     * @param estudiantePk Long
     * @return GraficoColumnas
     * @throws GeneralException
     */
    public GraficoColumnas obtenerCalificacionesNumericasAprobacionEstudiantePorGradoComparadoContraPromedioNacionalYSede(Long estudiantePk) throws GeneralException {
        try {
            if (estudiantePk != null) {

                GraficoColumnas gc = new GraficoColumnas();

                String querys = "select ale.ale_anio, gra.gra_pk, gra.gra_nombre, sdu.sdu_sede_fk, AVG(calest.cae_calificacion::::decimal) "
                        + "                        from centros_educativos.sg_calificaciones_estudiante calest  "
                        + "                        INNER JOIN centros_educativos.sg_calificaciones cabezal ON (calest.cae_calificacion_fk = cabezal.cal_pk) "
                        + "                        INNER JOIN centros_educativos.sg_secciones sec ON (cabezal.cal_seccion_fk = sec.sec_pk) "
                        + "                        INNER JOIN centros_educativos.sg_anio_lectivo ale ON (sec.sec_anio_lectivo_fk = ale.ale_pk) "
                        + "                        INNER JOIN centros_educativos.sg_servicio_educativo sdu ON (sec.sec_servicio_educativo_fk = sdu.sdu_pk) "
                        + "                        INNER JOIN centros_educativos.sg_grados gra ON (sdu.sdu_grado_fk = gra.gra_pk) "
                        + "                        INNER JOIN centros_educativos.sg_componente_plan_estudio cpe ON (cpe.cpe_pk = cabezal.cal_componente_plan_estudio_fk) "
                        + "                        INNER JOIN centros_educativos.sg_componente_plan_grado cpg ON (cpg.cpg_componente_plan_estudio_fk = cpe.cpe_pk AND cpg.cpg_grado_fk = gra.gra_pk AND cpg.cpg_plan_estudio_fk = sec.sec_plan_estudio_fk and (cpg.cpg_exclusivo_seccion is null or cpg.cpg_exclusivo_seccion = sec.sec_pk)) "
                        + "                        where calest.cae_calificacion is not null and (cpg.cpg_aplica_grafica_evolucion) and cabezal.cal_tipo_periodo_calificacion = 'APR' "
                        + "                        AND calest.cae_estudiante_fk = :estPk "
                        + "		           group by ale.ale_anio, gra.gra_pk, sdu.sdu_sede_fk"
                        + "                        order by gra_orden";

                Query q = em.createNativeQuery(querys)
                        .setParameter("estPk", estudiantePk);

                List<Object[]> res = q.getResultList();

                for (Object[] ob : res) {
                    Integer anio = (Integer) ob[0];
                    Long graPk = ((BigInteger) ob[1]).longValue();
                    String graNombre = (String) ob[2];
                    Long sedPk = ((BigInteger) ob[3]).longValue();
                    Double calificacion = ((BigDecimal) ob[4]).doubleValue();
                    Double promedioNacional = null;
                    Double promedioSede = null;

                    String queryn = "select gra_pk, avg(promedio::::decimal) as promedio from centros_educativos.sg_promedios_grados_cal_apr_" + anio
                            + " where gra_pk = :graPk "
                            + " group by gra_pk";
                    Query qn = em.createNativeQuery(queryn).setParameter("graPk", graPk);

                    List<Object[]> r = qn.getResultList();
                    if (!r.isEmpty()) {
                        Object[] o = r.get(0);
                        promedioNacional = ((BigDecimal) o[1]).doubleValue();
                    }

                    String querysed = "select gra_pk, avg(promedio::::decimal) as promedio from centros_educativos.sg_promedios_grados_sedes_cal_apr_" + anio
                            + " where gra_pk = :graPk and sed_pk = :sedPk"
                            + " group by gra_pk, sed_pk";
                    Query qs = em.createNativeQuery(querysed).setParameter("graPk", graPk).setParameter("sedPk", sedPk);

                    List<Object[]> rs = qs.getResultList();
                    if (!rs.isEmpty()) {
                        Object[] o = rs.get(0);
                        promedioSede = ((BigDecimal) o[1]).doubleValue();
                    }

                    gc.getValores().add(new DataColumna(graNombre, new Double[]{calificacion, promedioNacional, promedioSede}));
                }

                gc.setEtiquetasValor(new String[]{"Media estudiante", "Media nacional", "Media sede"});
                gc.setEtiquetaRotulo("Grados");

                return gc;
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return null;
    }

    /**
     * Obtener calificaciones ORD numéricas actuales para estudiante por grado y
     * componente
     *
     * @param estudiantePk Long
     * @param componentePlanEstudioPk Long
     * @return GraficoColumnas
     * @throws GeneralException
     */
    public GraficoColumnas obtenerCalificacionesNumericasOrdinariasActualesEstudiantePorGradoYComponente(Long estudiantePk, Long componentePlanEstudioPk) throws GeneralException {
        try {
            if (estudiantePk != null && componentePlanEstudioPk != null) {

                GraficoColumnas gc = new GraficoColumnas();

                String querys = "select rf.rfe_codigo, gra.gra_pk, gra.gra_nombre, cpe.cpe_pk, cpe.cpe_nombre, calest.cae_calificacion"
                        + "                        from centros_educativos.sg_calificaciones_estudiante calest  "
                        + "                        INNER JOIN centros_educativos.sg_calificaciones cabezal ON (calest.cae_calificacion_fk = cabezal.cal_pk) 	"
                        + "                        INNER JOIN centros_educativos.sg_secciones sec ON (cabezal.cal_seccion_fk = sec.sec_pk) "
                        + "			   INNER JOIN centros_educativos.sg_rango_fecha rf ON (cabezal.cal_rango_fecha_fk = rf.rfe_pk) "
                        + "                        INNER JOIN centros_educativos.sg_servicio_educativo sdu ON (sec.sec_servicio_educativo_fk = sdu.sdu_pk) "
                        + "			   INNER JOIN centros_educativos.sg_sedes sed ON (sed.sed_pk = sdu.sdu_sede_fk) "
                        + "			   INNER JOIN catalogo.sg_tipos_calendario_escolar tce ON (sed.sed_tipo_calendario_fk = tce.tce_pk) "
                        + "			   INNER JOIN centros_educativos.sg_calendarios calendarios ON (calendarios.cal_pk = (select cal_pk from centros_educativos.sg_calendarios cal where cal.cal_tipo_calendario_fk = tce.tce_pk AND cal.cal_habilitado order by cal_fecha_inicio desc limit 1)) "
                        + "                        INNER JOIN centros_educativos.sg_grados gra ON (sdu.sdu_grado_fk = gra.gra_pk) "
                        + "                        INNER JOIN centros_educativos.sg_componente_plan_estudio cpe ON (cpe.cpe_pk = cabezal.cal_componente_plan_estudio_fk) "
                        + "                        INNER JOIN centros_educativos.sg_componente_plan_grado cpg ON (cpg.cpg_componente_plan_estudio_fk = cpe.cpe_pk AND cpg.cpg_grado_fk = gra.gra_pk AND cpg.cpg_plan_estudio_fk = sec.sec_plan_estudio_fk and (cpg.cpg_exclusivo_seccion is null or cpg.cpg_exclusivo_seccion = sec.sec_pk)) "
                        + "                        where calest.cae_calificacion is not null and (cpg.cpg_aplica_grafica_evolucion) and cabezal.cal_tipo_periodo_calificacion = 'ORD' "
                        + "                        AND calest.cae_estudiante_fk = :estPk and cpe.cpe_pk = :cpePk "
                        + "                        and rfe_fecha_desde >= cal_fecha_inicio AND rfe_fecha_desde <= cal_fecha_fin "
                        + "                        order by rf.rfe_fecha_desde, gra.gra_orden, cpe.cpe_nombre ";

                Query q = em.createNativeQuery(querys)
                        .setParameter("estPk", estudiantePk)
                        .setParameter("cpePk", componentePlanEstudioPk);

                List<Object[]> res = q.getResultList();

                for (Object[] ob : res) {

                    String periodo = (String) ob[0];
                    String componentePlanNombre = (String) ob[4];
                    Double calificacion = Double.parseDouble((String) ob[5]);

                    gc.getValores().add(new DataColumna(periodo, new Double[]{calificacion}));
                    gc.setEtiquetasValor(new String[]{componentePlanNombre});
                }

                gc.setEtiquetaRotulo("Periodos");

                return gc;
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return null;
    }

    /**
     * Obtener histórico de calificaciones APR numéricas para estudiante por
     * grado y componente
     *
     * @param estudiantePk Long
     * @param componentePlanEstudioPk Long
     * @return GraficoColumnas
     * @throws GeneralException
     */
    public GraficoColumnas obtenerCalificacionesNumericasAprobacionEstudiantePorGradoYComponente(Long estudiantePk, Long componentePlanEstudioPk) throws GeneralException {
        try {
            if (estudiantePk != null) {

                GraficoColumnas gc = new GraficoColumnas();

                String where = "";

                if (componentePlanEstudioPk != null) {
                    where += " and cpe.cpe_pk = :cpePk";
                }

                String querys = "select gra.gra_pk, gra.gra_nombre, cpe.cpe_pk, cpe.cpe_nombre, calest.cae_calificacion "
                        + "                from centros_educativos.sg_calificaciones_estudiante calest  "
                        + "                INNER JOIN centros_educativos.sg_calificaciones cabezal ON (calest.cae_calificacion_fk = cabezal.cal_pk) "
                        + "                INNER JOIN centros_educativos.sg_secciones sec ON (cabezal.cal_seccion_fk = sec.sec_pk) "
                        + "                INNER JOIN centros_educativos.sg_servicio_educativo sdu ON (sec.sec_servicio_educativo_fk = sdu.sdu_pk) "
                        + "                INNER JOIN centros_educativos.sg_grados gra ON (sdu.sdu_grado_fk = gra.gra_pk) "
                        + "                INNER JOIN centros_educativos.sg_componente_plan_estudio cpe ON (cpe.cpe_pk = cabezal.cal_componente_plan_estudio_fk) "
                        + "                INNER JOIN centros_educativos.sg_componente_plan_grado cpg ON (cpg.cpg_componente_plan_estudio_fk = cpe.cpe_pk AND cpg.cpg_grado_fk = gra.gra_pk AND cpg.cpg_plan_estudio_fk = sec.sec_plan_estudio_fk and (cpg.cpg_exclusivo_seccion is null or cpg.cpg_exclusivo_seccion = sec.sec_pk)) "
                        + "                where calest.cae_calificacion is not null and (cpg.cpg_aplica_grafica_evolucion) and cabezal.cal_tipo_periodo_calificacion = 'APR' "
                        + "                AND calest.cae_estudiante_fk = :estPk " + where
                        + "		   order by gra_orden, cpe_nombre";

                HashMap<Long, String> componentes = new HashMap<>();
                HashMap<Long, String> grados = new HashMap<>();
                List<Long> gradosOrdenados = new ArrayList<>();
                List<Long> componentesOrdenados = new ArrayList<>();
                HashMap<Long, HashMap<Long, Double>> gradosComponentesCalificaciones = new HashMap<>();

                Query q = em.createNativeQuery(querys)
                        .setParameter("estPk", estudiantePk);

                if (componentePlanEstudioPk != null) {
                    q.setParameter("cpePk", componentePlanEstudioPk);
                }

                List<Object[]> res = q.getResultList();

                for (Object[] ob : res) {

                    Long graPk = ((BigInteger) ob[0]).longValue();
                    String graNombre = (String) ob[1];
                    Long componentePlanPk = ((BigInteger) ob[2]).longValue();
                    String componentePlanNombre = (String) ob[3];
                    Double calificacion = Double.parseDouble((String) ob[4]);

                    componentes.putIfAbsent(componentePlanPk, componentePlanNombre);
                    grados.putIfAbsent(graPk, graNombre);
                    if (!gradosOrdenados.contains(graPk)) {
                        gradosOrdenados.add(graPk);
                    }
                    if (!componentesOrdenados.contains(componentePlanPk)) {
                        componentesOrdenados.add(componentePlanPk);
                    }
                    gradosComponentesCalificaciones.computeIfAbsent(graPk, g -> new HashMap<>()).putIfAbsent(componentePlanPk, calificacion);
                }

                String[] componentesNombres = new String[componentesOrdenados.size()];

                for (Long pkComponente : componentesOrdenados) {
                    componentesNombres[componentesOrdenados.indexOf(pkComponente)] = componentes.get(pkComponente);
                }
                gc.setEtiquetasValor(componentesNombres);
                gc.setEtiquetaRotulo("Grados");
                for (Long gpk : gradosOrdenados) {

                    HashMap<Long, Double> componentesCalificaciones = gradosComponentesCalificaciones.get(gpk);
                    Double[] calificaciones = new Double[componentesOrdenados.size()];
                    for (Long pkComponente : componentesCalificaciones.keySet()) {
                        calificaciones[componentesOrdenados.indexOf(pkComponente)] = componentesCalificaciones.get(pkComponente);
                    }
                    gc.getValores().add(new DataColumna(grados.get(gpk), calificaciones));
                }

                return gc;
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return null;
    }

    /**
     * Obtener histórico de calificaciones APR conceptuales para estudiante por
     * grado y componente
     *
     * @param estudiantePk Long
     * @param componentePlanEstudioPk Long
     * @return GraficoColumnas
     * @throws GeneralException
     */
    public GraficoColumnas obtenerCalificacionesConceptualesAprobacionEstudiantePorGradoYComponente(Long estudiantePk, Long componentePlanEstudioPk) throws GeneralException {
        try {
            if (estudiantePk != null) {

                GraficoColumnas gc = new GraficoColumnas();
                gc.setSeries(new ArrayList<>());
                gc.setEscalas(new ArrayList<>());

                String where = "";

                if (componentePlanEstudioPk != null) {
                    where += " and cpe.cpe_pk = :cpePk";
                }

                String querys = "select gra.gra_pk, gra.gra_nombre, cpe.cpe_pk, cpe.cpe_nombre, calconceptual.cal_orden, calconceptual.cal_escala_fk "
                        + "                from centros_educativos.sg_calificaciones_estudiante calest  "
                        + "                INNER JOIN centros_educativos.sg_calificaciones cabezal ON (calest.cae_calificacion_fk = cabezal.cal_pk) "
                        + "                INNER JOIN catalogo.sg_calificaciones calconceptual ON (calconceptual.cal_pk = calest.cae_calificacion_conceptual_fk) "
                        + "                INNER JOIN centros_educativos.sg_secciones sec ON (cabezal.cal_seccion_fk = sec.sec_pk) "
                        + "                INNER JOIN centros_educativos.sg_servicio_educativo sdu ON (sec.sec_servicio_educativo_fk = sdu.sdu_pk) "
                        + "                INNER JOIN centros_educativos.sg_sedes sed ON (sed.sed_pk = sdu.sdu_sede_fk) "
                        + "                INNER JOIN centros_educativos.sg_grados gra ON (sdu.sdu_grado_fk = gra.gra_pk) "
                        + "                INNER JOIN centros_educativos.sg_componente_plan_estudio cpe ON (cpe.cpe_pk = cabezal.cal_componente_plan_estudio_fk) "
                        + "                INNER JOIN centros_educativos.sg_componente_plan_grado cpg ON (cpg.cpg_componente_plan_estudio_fk = cpe.cpe_pk AND cpg.cpg_grado_fk = gra.gra_pk AND cpg.cpg_plan_estudio_fk = sec.sec_plan_estudio_fk and (cpg.cpg_exclusivo_seccion is null or cpg.cpg_exclusivo_seccion = sec.sec_pk)) "
                        + "                where cpg.cpg_aplica_grafica_evolucion and cabezal.cal_tipo_periodo_calificacion = 'APR' "
                        + "                AND calest.cae_estudiante_fk = :estPk " + where
                        + "		   order by gra_orden, cpe_nombre";

                HashMap<Long, String> componentes = new HashMap<>();
                HashMap<Long, String> grados = new HashMap<>();
                List<Long> gradosOrdenados = new ArrayList<>();
                List<Long> componentesOrdenados = new ArrayList<>();
                HashMap<Long, HashMap<Long, Double>> gradosComponentesCalificaciones = new HashMap<>();
                HashMap<Long, Long> componentesEscalas = new HashMap<>();

                List<Long> escalas = new ArrayList<>();

                Query q = em.createNativeQuery(querys)
                        .setParameter("estPk", estudiantePk);

                if (componentePlanEstudioPk != null) {
                    q.setParameter("cpePk", componentePlanEstudioPk);
                }

                List<Object[]> res = q.getResultList();

                for (Object[] ob : res) {

                    Long graPk = ((BigInteger) ob[0]).longValue();
                    String graNombre = (String) ob[1];
                    Long componentePlanPk = ((BigInteger) ob[2]).longValue();
                    String componentePlanNombre = (String) ob[3];
                    Double calificacionOrden = Double.valueOf((int) ob[4]);
                    Long escalaPk = ((BigInteger) ob[5]).longValue();

                    if (!escalas.contains(escalaPk)) {
                        escalas.add(escalaPk);
                    }

                    componentes.putIfAbsent(componentePlanPk, componentePlanNombre);
                    grados.putIfAbsent(graPk, graNombre);
                    if (!gradosOrdenados.contains(graPk)) {
                        gradosOrdenados.add(graPk);
                    }
                    if (!componentesOrdenados.contains(componentePlanPk)) {
                        componentesOrdenados.add(componentePlanPk);
                    }
                    gradosComponentesCalificaciones.computeIfAbsent(graPk, g -> new HashMap<>()).putIfAbsent(componentePlanPk, calificacionOrden);
                    componentesEscalas.putIfAbsent(componentePlanPk, escalaPk);
                }

                String[] componentesNombres = new String[componentesOrdenados.size()];

                for (Long pkComponente : componentesOrdenados) {
                    componentesNombres[componentesOrdenados.indexOf(pkComponente)] = componentes.get(pkComponente);
                    gc.getSeries().add(escalas.indexOf(componentesEscalas.get(pkComponente)));
                }
                gc.setEtiquetasValor(componentesNombres);
                gc.setEtiquetaRotulo("Grados");
                for (Long gpk : gradosOrdenados) {

                    HashMap<Long, Double> componentesCalificaciones = gradosComponentesCalificaciones.get(gpk);
                    Double[] calificaciones = new Double[componentesOrdenados.size()];
                    for (Long pkComponente : componentesCalificaciones.keySet()) {
                        calificaciones[componentesOrdenados.indexOf(pkComponente)] = componentesCalificaciones.get(pkComponente);
                    }
                    gc.getValores().add(new DataColumna(grados.get(gpk), calificaciones));
                }

                for (Long escPk : escalas) {
                    Escala e = new Escala();

                    Query qq = em.createNativeQuery("select cal.cal_orden, cal.cal_valor, esc.eca_nombre from catalogo.sg_calificaciones cal "
                            + " INNER JOIN catalogo.sg_escalas_calificacion esc ON (cal.cal_escala_fk = esc.eca_pk)"
                            + " where eca_pk = :escPk "
                            + " order by cal_orden")
                            .setParameter("escPk", escPk);

                    List<Object[]> ret = qq.getResultList();

                    for (Object[] o : ret) {
                        Double calOrden = Double.valueOf((int) o[0]);
                        String calValor = (String) o[1];
                        String title = (String) o[2];

                        e.setLabel(title);

                        Tick t = new Tick();
                        t.setValor(calOrden);
                        t.setLabel(calValor);
                        e.getTicks().add(t);
                    }

                    gc.getEscalas().add(e);

                }

                return gc;
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return null;
    }

}
