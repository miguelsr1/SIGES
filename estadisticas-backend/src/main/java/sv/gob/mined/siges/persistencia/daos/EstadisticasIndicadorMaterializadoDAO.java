package sv.gob.mined.siges.persistencia.daos;

import com.sofis.persistence.dao.imp.hibernate.HibernateJpaDAOImp;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DoubleType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import sv.gob.mined.siges.dto.EstGenerica;
import sv.gob.mined.siges.enumerados.EnumTipoNumericoValorEstadistica;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.persistencia.entidades.SgExtraccion;

/**
 *
 * @author usuario
 */
public class EstadisticasIndicadorMaterializadoDAO extends HibernateJpaDAOImp<SgExtraccion, Integer> implements Serializable {

    private EntityManager em;

    public EstadisticasIndicadorMaterializadoDAO(EntityManager em) throws Exception {
        super(em);
        this.em = em;
    }

  
    public List<EstGenerica> obtenerEstadisticaDeIndicadorMaterializado(Long indicadorMaterializadoPk, Boolean desagregar, EnumTipoNumericoValorEstadistica tipoNumerico) throws GeneralException {
        Session session = em.unwrap(Session.class);

        String colDesagregacion = "'-'";
        if (desagregar != null){
            colDesagregacion = "t.tup_desagregacion";
        } 
        
        String query = "select t.tup_identificador as dato,"
                + " " + colDesagregacion + " as desagregacion,"
                + " t.tup_valor as cantidad"
                + " from estadisticas.sg_tuplas_indicador_materializado t"
                + " where t.tup_indicador_materializado_fk = :indicadorPk";
        
        SQLQuery q = session.createSQLQuery(query);
               
        q.addScalar("dato", new StringType());
        q.addScalar("desagregacion", new StringType());
        q.addScalar("cantidad", tipoNumerico.DECIMAL.equals(tipoNumerico) ? new DoubleType() : new LongType());
        q.setParameter("indicadorPk", indicadorMaterializadoPk);

        q.setResultTransformer(Transformers.aliasToBean(EstGenerica.class));
        return q.list();
    }
}
