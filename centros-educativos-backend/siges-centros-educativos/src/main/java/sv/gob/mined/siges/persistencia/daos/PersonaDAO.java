/**
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
import java.lang.reflect.Field;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField.Type;
import org.apache.lucene.search.SortField;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.transform.Transformers;
import sv.gob.mined.siges.filtros.FiltroPersona;
import sv.gob.mined.siges.persistencia.entidades.SgIdentificacion;
import sv.gob.mined.siges.persistencia.entidades.SgPersona;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgDiscapacidad;
import sv.gob.mined.siges.utils.SofisStringUtils;

public class PersonaDAO extends HibernateJpaDAOImp<SgPersona, Integer> implements Serializable {

    private EntityManager em;

    public PersonaDAO(EntityManager em) throws Exception {
        super(em);
        this.em = em;
    }

    private List<CriteriaTO> generarCriteria(FiltroPersona filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getPerPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "perPk", filtro.getPerPk());
            criterios.add(criterio);
        }

        if (filtro.getPerPks() != null && !filtro.getPerPks().isEmpty()) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "perPk", filtro.getPerPks());
            criterios.add(criterio);
        }

        if(filtro.getPerTieneDUI() != null && filtro.getPerTieneDUI()) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NOT_NULL, "perDui", 1);
            criterios.add(criterio);
        }
        if (!StringUtils.isBlank(filtro.getPerDUINombreCompleto())) {
            List<CriteriaTO> nombreDUIOR = new ArrayList();
            MatchCriteriaTO criterioNombre = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "perNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerDUINombreCompleto()));
            nombreDUIOR.add(criterioNombre);

            MatchCriteriaTO criterioDUI = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "perDui", SofisStringUtils.normalizarParaBusqueda(filtro.getPerDUINombreCompleto()));
            nombreDUIOR.add(criterioDUI);

            if (!nombreDUIOR.isEmpty()) {
                CriteriaTO[] arrCriterioOR = nombreDUIOR.toArray(new CriteriaTO[nombreDUIOR.size()]);
                CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
                criterios.add(criterioOR);
            }
        }
        if (!StringUtils.isBlank(filtro.getPerNombreCompleto())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "perNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerNombreCompleto()));
            criterios.add(criterio);
        }

        if (filtro.getPerFechaNacimiento() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "perFechaNacimiento", filtro.getPerFechaNacimiento());
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getPerPrimerNombre())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.START_WITH, "perPrimerNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerPrimerNombre()));
            criterios.add(criterio);
        }
        if (!StringUtils.isBlank(filtro.getPerSegundoNombre())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.START_WITH, "perSegundoNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerSegundoNombre()));
            criterios.add(criterio);
        }
        if (!StringUtils.isBlank(filtro.getPerTercerNombre())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.START_WITH, "perTercerNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerTercerNombre()));
            criterios.add(criterio);
        }
        if (!StringUtils.isBlank(filtro.getPerPrimerApellido())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.START_WITH, "perPrimerApellidoBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerPrimerApellido()));
            criterios.add(criterio);
        }
        if (!StringUtils.isBlank(filtro.getPerSegundoApellido())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.START_WITH, "perSegundoApellidoBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerSegundoApellido()));
            criterios.add(criterio);
        }
        if (!StringUtils.isBlank(filtro.getPerTercerApellido())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.START_WITH, "perTercerApellidoBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerTercerApellido()));
            criterios.add(criterio);
        }

        if (filtro.getIgnorarPersonasPk() != null) {
            for (Long pk : filtro.getIgnorarPersonasPk()) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NOT_EQUALS, "perPk", pk);
                criterios.add(criterio);
            }
        }

        //IDENTIFICACIONES
        List<CriteriaTO> indentificacionesOR = new ArrayList();
        if (filtro.getNie() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "perNie", filtro.getNie());
            indentificacionesOR.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getDui())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "perDui", filtro.getDui());
            indentificacionesOR.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getNit())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "perNit", filtro.getNit());
            indentificacionesOR.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getInpep())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "perInpep", filtro.getInpep());
            indentificacionesOR.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getIsss())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "perIsss", filtro.getIsss());
            indentificacionesOR.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getNup())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "perNup", filtro.getNup());
            indentificacionesOR.add(criterio);
        }

        if (filtro.getNip() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "perNip", filtro.getNip());
            indentificacionesOR.add(criterio);
        }

        if (filtro.getCun() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "perCun", filtro.getCun());
            indentificacionesOR.add(criterio);
        }
        if (filtro.getPerFechaNacimientoDesde() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATEREQUAL, "perFechaNacimiento", filtro.getPerFechaNacimientoDesde());
            criterios.add(criterio);
        }
        if (filtro.getPerFechaNacimientoHasta() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.LESSEQUAL, "perFechaNacimiento", filtro.getPerFechaNacimientoHasta());
            criterios.add(criterio);
        }

        //PARTIDA
        List<CriteriaTO> criteriosPartida = new ArrayList();
        if (filtro.getNroPartida() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "perPartidaNacimiento", filtro.getNroPartida());
            criteriosPartida.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getFolioPartida())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "perPartidaNacimientoFolio", filtro.getFolioPartida().trim());
            criteriosPartida.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getLibroPartida())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "perPartidaNacimientoLibro", filtro.getLibroPartida().trim());
            criteriosPartida.add(criterio);
        }

        if (criteriosPartida.size() > 1) {
            indentificacionesOR.add(CriteriaTOUtils.createANDTO(criteriosPartida.toArray(new CriteriaTO[criteriosPartida.size()])));
        } else if (criteriosPartida.size() == 1) {
            indentificacionesOR.add(criteriosPartida.get(0));
        }
        if (filtro.getNies() != null && !filtro.getNies().isEmpty()) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "perNie", filtro.getNies());
            criterios.add(criterio);
        }

        //OTRAS IDEN
        if (filtro.getOtrasIden() != null && !filtro.getOtrasIden().isEmpty()) {
            filtro.setSeNecesitaDistinct(Boolean.TRUE);
            for (SgIdentificacion iden : filtro.getOtrasIden()) {

                List<CriteriaTO> criteriosOtraIden = new ArrayList();
                if (!StringUtils.isBlank(iden.getIdeNumeroDocumento())) {
                    MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "perIdentificaciones.ideNumeroDocumento", iden.getIdeNumeroDocumento().trim());
                    criteriosOtraIden.add(criterio);
                }

                if (iden.getIdePaisEmisor() != null) {
                    MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "perIdentificaciones.idePaisEmisor", iden.getIdePaisEmisor());
                    criteriosOtraIden.add(criterio);
                }

                if (iden.getIdeTipoDocumento() != null) {
                    MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "perIdentificaciones.ideTipoDocumento", iden.getIdeTipoDocumento());
                    criteriosOtraIden.add(criterio);
                }

                if (criteriosOtraIden.size() > 1) {
                    indentificacionesOR.add(CriteriaTOUtils.createANDTO(criteriosOtraIden.toArray(new CriteriaTO[criteriosOtraIden.size()])));
                } else if (criteriosOtraIden.size() == 1) {
                    indentificacionesOR.add(criteriosOtraIden.get(0));
                }

            }

        }

        if (!indentificacionesOR.isEmpty()) {
            CriteriaTO[] arrCriterioOR = indentificacionesOR.toArray(new CriteriaTO[indentificacionesOR.size()]);
            CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
            criterios.add(criterioOR);
        }

        return criterios;
    }

    public List<SgPersona> obtenerPorFiltro(FiltroPersona filtro) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);

            String[] orderBy = filtro.getOrderBy();
            boolean[] asc = filtro.getAscending();

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.findEntityByCriteria(SgPersona.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), filtro.getSeNecesitaDistinct(), null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroPersona filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgPersona.class, criterio, filtro.getSeNecesitaDistinct(), null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    ///START LUCENE//////
    public List<SgPersona> obtenerPorFiltroLucene(FiltroPersona filtro) throws DAOGeneralException {

        try {

            FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(super.getEm());
            QueryBuilder qb = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(SgPersona.class).get();
            BooleanQuery.Builder booleanQuery = this.generarQuery(filtro, qb);
            FullTextQuery persistenceQuery = fullTextEntityManager.createFullTextQuery(booleanQuery.build(), SgPersona.class);

            if (filtro.getIncluirCampos() != null) {
                persistenceQuery = persistenceQuery.setProjection("perPk");
            }

            persistenceQuery.setFirstResult(filtro.getFirst().intValue());
            persistenceQuery.setMaxResults(filtro.getMaxResults().intValue());

            if (filtro.getOrderBy() != null) {
                SortField[] fields = new SortField[filtro.getOrderBy().length];
                for (int i = 0; i < filtro.getOrderBy().length; i++) {
                    Field f = SgPersona.class.getDeclaredField(filtro.getOrderBy()[i]);
                    Type tipoCampo = null;
                    if (f.getType() == String.class) {
                        tipoCampo = Type.STRING;
                    } else if (f.getType() == Long.class) {
                        tipoCampo = Type.LONG;
                    }
                    SortField sf = new SortField(filtro.getOrderBy()[i] + "Sort", tipoCampo, filtro.getAscending()[i]);
                    fields[i] = sf;
                }
                persistenceQuery.setSort(new Sort(fields));
            }

            if (filtro.getIncluirCampos() != null) {

                //Para utilizar el incluirCampos, debemos realizar una query normal a BD. 
                //Las proyecciones en la query de hibernate search requieren que los campos esten marcados como stored.
                //Por esa razón, con hibernate search solamente levantamos la PK del elemento, y luego con dicha pk hacemos una query separada a la base.
                List<SgPersona> pers = persistenceQuery.setResultTransformer(Transformers.aliasToBean(SgPersona.class)).getResultList();
                List<Long> perPks = pers.stream().map(p -> p.getPerPk()).collect(Collectors.toList());

                if (perPks.isEmpty()) {
                    return new ArrayList<>();
                } else {

                    FiltroPersona fil = new FiltroPersona();
                    fil.setIncluirCampos(filtro.getIncluirCampos());
                    fil.setPerPks(perPks);

                    return this.obtenerPorFiltro(fil);

                }
            } else {
                return persistenceQuery.getResultList();
            }
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltroLucene(FiltroPersona filtro) throws DAOGeneralException {
        try {
            FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(super.getEm());
            QueryBuilder qb = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(SgPersona.class).get();
            BooleanQuery.Builder booleanQuery = this.generarQuery(filtro, qb);
            FullTextQuery persistenceQuery = fullTextEntityManager.createFullTextQuery(booleanQuery.build(), SgPersona.class);
            return (long) persistenceQuery.getResultSize();
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public BooleanQuery.Builder generarQuery(FiltroPersona filtro, QueryBuilder qb) {

        List<Query> listoq = new ArrayList<Query>();

        if (!StringUtils.isBlank(filtro.getPerNombreCompleto())) {
            String nombreCompleto = SofisStringUtils.normalizarParaBusqueda(filtro.getPerNombreCompleto());
            String[] tokens = nombreCompleto.split(" ");
            BooleanQuery.Builder nombresCompletosBooleanQuery = new BooleanQuery.Builder();
            for (String t : tokens) {
                Query q1 = qb
                        .keyword()
                        .fuzzy()
                        .withEditDistanceUpTo(2)
                        .withPrefixLength(0)
                        .boostedTo(1.2f)
                        .onFields("perPrimerNombreBusqueda", "perSegundoNombreBusqueda", "perPrimerApellidoBusqueda", "perSegundoApellidoBusqueda")
                        .matching(t)
                        .createQuery();
                Query q2 = qb
                        .keyword()
                        .onFields("perPrimerNombreBusquedaPhonetic", "perSegundoNombreBusquedaPhonetic", "perPrimerApellidoBusquedaPhonetic", "perSegundoApellidoBusquedaPhonetic")
                        .matching(t)
                        .createQuery();
                nombresCompletosBooleanQuery.add(q1, BooleanClause.Occur.SHOULD);
                nombresCompletosBooleanQuery.add(q2, BooleanClause.Occur.SHOULD);
            }
            nombresCompletosBooleanQuery.setMinimumNumberShouldMatch(tokens.length + 1);
            listoq.add(nombresCompletosBooleanQuery.build());
        }

        //NOMBRES INDEPENDIENTES 
        List<Query> queryNombres = new ArrayList<>();
        if (!StringUtils.isBlank(filtro.getPerPrimerNombre())) {
            Query q = qb
                    .keyword()
                    .fuzzy()
                    .withEditDistanceUpTo(2)
                    .withPrefixLength(0)
                    .boostedTo(1.6f)
                    .onFields("perPrimerNombreBusqueda")
                    .matching(SofisStringUtils.normalizarParaBusqueda(filtro.getPerPrimerNombre()))
                    .createQuery();
            Query q1 = qb
                    .keyword()
                    .onFields("perPrimerNombreBusquedaPhonetic")
                    .matching(SofisStringUtils.normalizarParaBusqueda(filtro.getPerPrimerNombre()))
                    .createQuery();
            queryNombres.add(q);
            queryNombres.add(q1);
        }
        if (!StringUtils.isBlank(filtro.getPerSegundoNombre())) {
            Query q = qb
                    .keyword()
                    .fuzzy()
                    .withEditDistanceUpTo(2)
                    .withPrefixLength(0)
                    .boostedTo(1.2f)
                    .onFields("perSegundoNombreBusqueda")
                    .matching(SofisStringUtils.normalizarParaBusqueda(filtro.getPerSegundoNombre()))
                    .createQuery();
            Query q1 = qb
                    .keyword()
                    .onFields("perSegundoNombreBusquedaPhonetic")
                    .matching(SofisStringUtils.normalizarParaBusqueda(filtro.getPerSegundoNombre()))
                    .createQuery();
            queryNombres.add(q);
            queryNombres.add(q1);
        }
        if (!StringUtils.isBlank(filtro.getPerPrimerApellido())) {
            Query q = qb
                    .keyword()
                    .fuzzy()
                    .withEditDistanceUpTo(2)
                    .withPrefixLength(0)
                    .boostedTo(1.6f)
                    .onFields("perPrimerApellidoBusqueda")
                    .matching(SofisStringUtils.normalizarParaBusqueda(filtro.getPerPrimerApellido()))
                    .createQuery();
            Query q1 = qb
                    .keyword()
                    .onFields("perPrimerApellidoBusquedaPhonetic")
                    .matching(SofisStringUtils.normalizarParaBusqueda(filtro.getPerPrimerApellido()))
                    .createQuery();
            queryNombres.add(q);
            queryNombres.add(q1);
        }
        if (!StringUtils.isBlank(filtro.getPerSegundoApellido())) {
            Query q = qb
                    .keyword()
                    .fuzzy()
                    .withEditDistanceUpTo(2)
                    .withPrefixLength(0)
                    .boostedTo(1.2f)
                    .onField("perSegundoApellidoBusqueda")
                    .matching(SofisStringUtils.normalizarParaBusqueda(filtro.getPerSegundoApellido()))
                    .createQuery();
            Query q1 = qb
                    .keyword()
                    .onFields("perSegundoApellidoBusquedaPhonetic")
                    .matching(SofisStringUtils.normalizarParaBusqueda(filtro.getPerSegundoApellido()))
                    .createQuery();
            queryNombres.add(q);
            queryNombres.add(q1);
        }

        if (!StringUtils.isBlank(filtro.getPerTercerNombre())) {
            Query q = qb
                    .keyword()
                    .fuzzy()
                    .withEditDistanceUpTo(2)
                    .withPrefixLength(0)
                    .onField("perTercerNombreBusqueda")
                    .matching(SofisStringUtils.normalizarParaBusqueda(filtro.getPerTercerNombre()))
                    .createQuery();
            queryNombres.add(q);
        }

        if (!StringUtils.isBlank(filtro.getPerTercerApellido())) {
            Query q = qb
                    .keyword()
                    .fuzzy()
                    .withEditDistanceUpTo(2)
                    .withPrefixLength(0)
                    .onField("perTercerApellidoBusqueda")
                    .matching(SofisStringUtils.normalizarParaBusqueda(filtro.getPerTercerApellido()))
                    .createQuery();
            queryNombres.add(q);
        }

        if (!queryNombres.isEmpty()) {
            BooleanQuery.Builder nombresBooleanQuery = new BooleanQuery.Builder();
            for (Query q : queryNombres) {
                nombresBooleanQuery.add(q, BooleanClause.Occur.SHOULD);
            }
            Integer minNumberShouldMatch = (queryNombres.size() / 2) + 1;
            nombresBooleanQuery.setMinimumNumberShouldMatch(minNumberShouldMatch);
            listoq.add(nombresBooleanQuery.build());
        }

        //FIN NOMBRES INDEPENDIENTES
        if (filtro.getPerSexoPk() != null) {
            Query q = qb
                    .keyword()
                    .onField("perSexo.sexPk")
                    .matching(filtro.getPerSexoPk())
                    .createQuery();
            listoq.add(q);
        }

        if (filtro.getNie() != null) {
            Query q = qb
                    .keyword()
                    .onField("perNie")
                    .matching(filtro.getNie())
                    .createQuery();
            listoq.add(q);
        }

        if (filtro.getDui() != null) {
            Query q = qb
                    .keyword()
                    .onField("perDui")
                    .matching(filtro.getDui())
                    .createQuery();
            listoq.add(q);
        }

        if (StringUtils.isNotBlank(filtro.getNip())) {
            Query q = qb
                    .keyword()
                    .onField("perNip")
                    .matching(filtro.getNip())
                    .createQuery();
            listoq.add(q);
        }

        if (StringUtils.isNotBlank(filtro.getNit())) {
            Query q = qb
                    .keyword()
                    .onField("perNit")
                    .matching(filtro.getNit())
                    .createQuery();
            listoq.add(q);
        }

        if (filtro.getPerFechaNacimiento() != null) {
            Query q = qb
                    .keyword()
                    .onField("perFechaNacimiento")
                    .matching(filtro.getPerFechaNacimiento().atStartOfDay(ZoneId.systemDefault()))
                    .createQuery();
            listoq.add(q);
        }

        if (filtro.getPerFechaNacimientoDesde() != null) {
            Query q = qb
                    .range()
                    .onField("perFechaNacimiento")
                    .above(filtro.getPerFechaNacimientoDesde().atStartOfDay(ZoneId.systemDefault()))
                    .createQuery();
            listoq.add(q);
        }

        if (filtro.getPerFechaNacimientoHasta() != null) {
            Query q = qb
                    .range()
                    .onField("perFechaNacimiento")
                    .below(filtro.getPerFechaNacimientoHasta().atStartOfDay(ZoneId.systemDefault()))
                    .createQuery();
            listoq.add(q);
        }

        List<Query> listoqmn = new ArrayList<Query>();
        if (filtro.getIgnorarPersonasPk() != null) {
            for (Long pk : filtro.getIgnorarPersonasPk()) {
                Query q = qb
                        .keyword()
                        .onField("perPk")
                        .matching(pk)
                        .createQuery();
                listoqmn.add(q);
            }
        }

        BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();

        for (org.apache.lucene.search.Query query : listoq) {
            booleanQuery.add(query, BooleanClause.Occur.MUST);
        }
        for (org.apache.lucene.search.Query query : listoqmn) {
            booleanQuery.add(query, BooleanClause.Occur.MUST_NOT);
        }

        if (listoq.isEmpty() && listoqmn.isEmpty()) {
            booleanQuery.add(new MatchAllDocsQuery(), BooleanClause.Occur.MUST);
        }

        return booleanQuery;

    }

    public List<SgDiscapacidad> obtenerDiscapacidades(Long perPk) {
        String query = "select dis.* from "
                + "centros_educativos.sg_personas_discapacidades spd\n"
                + "join catalogo.sg_discapacidades dis on spd.dis_pk = dis.dis_pk\n"
                + "where spd.per_pk = :perPk";
        javax.persistence.Query q = em.createNativeQuery(query, SgDiscapacidad.class);
        q.setParameter("perPk", perPk);

        List<SgDiscapacidad> objs = q.getResultList();
        return objs;
    }

}
