package gob.mined.siap2.persistence.dao.imp;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.entities.constantes.Constantes;
import gob.mined.siges.entities.data.impl.SgAfLoteBienes;
import gob.mined.siap2.persistence.dao.imp.eclipselink.EclipselinkJpaDAOImp;
import java.io.Serializable;
import javax.persistence.Query;



@JPADAO
public class LoteBienesDAO extends EclipselinkJpaDAOImp<SgAfLoteBienes, Long> implements  Serializable{
    /**
     * Método utilizado para guardar el registro de auditoria sobre la entidad SgAfBienDepreciable
     * 
     * @param bien 
     */
    public void guardarAuditoriaLoteBienes(SgAfLoteBienes lote) {
            Query a = entityManager.createNativeQuery("select nextval ('hibernate_sequence')");
            Long seq = (Long) a.getSingleResult();

            Query est = entityManager.createNativeQuery("INSERT INTO revinfo (rev, revtstmp, usuario) values(?1, extract(epoch from now()), ?2)");
            est.setParameter("1", seq);
            est.setParameter("2", lote.getUltMod());
            est.executeUpdate();

            Query rev = entityManager.createNativeQuery(
                    "INSERT INTO " + Constantes.SCHEMA_ACTIVO_FIJO + ".sg_af_lote_bienes_aud "
                    +  "(lbi_id,lbi_codigo_inventario_padre,lbi_estado,lbi_cantidad_bienes_replicar, lbi_ult_mod_usuario, lbi_version, rev, revtype) "
                    + "VALUES(?1, ?2, ?3, ?4, ?5, ?6,?7, ?8)"
            );
            
            rev.setParameter("1", lote.getLbiId());
            rev.setParameter("2", lote.getLbiCodigoInventarioPadre());
            rev.setParameter("3", lote.getLbiEstado().getText());
            rev.setParameter("4", lote.getLbiCantidadBienesReplicar());
            rev.setParameter("5", lote.getUltMod());
            rev.setParameter("6", lote.getLbiVersion());
            rev.setParameter("7", seq);
            rev.setParameter("8", 1);
            rev.executeUpdate();
    }
}