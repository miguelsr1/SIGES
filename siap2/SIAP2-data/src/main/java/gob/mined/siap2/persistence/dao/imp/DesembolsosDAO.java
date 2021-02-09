package gob.mined.siap2.persistence.dao.imp;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.entities.data.impl.DesembolsoTransferenciaComponente;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.persistence.dao.imp.eclipselink.EclipselinkJpaDAOImp;
import gob.mined.siges.entities.data.impl.SgDesembolso;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Query;


@JPADAO
public class DesembolsosDAO extends EclipselinkJpaDAOImp<DesembolsoTransferenciaComponente, Integer> implements  Serializable{
    
    
    /**
     * Este método busca un registro de Desembolso filtrado por su ID
     * @param id
     * @return
     * @throws DAOGeneralException 
     */
    public DesembolsoTransferenciaComponente getDesembolsoById(Integer id) throws DAOGeneralException {
        return (DesembolsoTransferenciaComponente) entityManager
                .createQuery("select g from DesembolsoTransferenciaComponente g where g.pk = :id")
                .setParameter("id", id)
                .getSingleResult();
    }
    
    
    /**
     * Este método obtiene el listado de desembolsos filtrado por el ID de TransferenciaComponente
     * @param id
     * @return 
     */
    public List<DesembolsoTransferenciaComponente> getAllDesembolsByTransferenciaComponente(Integer id){
        return (List<DesembolsoTransferenciaComponente>) entityManager.createQuery("select a from DesembolsoTransferenciaComponente a where a.transferenciasComponente.id = :id")
                .setParameter("id", id)
                .getResultList();
    }
    
    
    /**
     * Metodo utilizado para guardar los registros de Auditoria de Desembolsos
     * @param sgDesembolso 
     */
    public void guardarAuditoriaDesembolso(SgDesembolso sgDesembolso) {
        Query a = entityManager.createNativeQuery("select nextval ('hibernate_sequence')");
        Long seq = (Long) a.getSingleResult();

        Query est = entityManager.createNativeQuery("INSERT INTO revinfo (rev, revtstmp, usuario) values(?1, extract(epoch from now()), ?2)");
        est.setParameter("1", seq);
        est.setParameter("2", sgDesembolso.getUltUsuario());
        est.executeUpdate();

        Query rev = entityManager.createNativeQuery(
                "INSERT INTO finanzas.sg_desembolsos_aud"
                + "(dsb_pk, dsb_ult_mod, dsb_ult_usuario, dsb_version, dsb_porcentaje, dsb_monto, dsb_estado, rev, revtype)"
                + "VALUES(?1, ?2, ?3, ?4, ?5::decimal, ?6::decimal, ?7, ?8, ?9)"
        );

        rev.setParameter("1", sgDesembolso.getPk());
        rev.setParameter("2", sgDesembolso.getUltMod());
        rev.setParameter("3", sgDesembolso.getUltUsuario());
        rev.setParameter("4", sgDesembolso.getVersion());
        rev.setParameter("5", sgDesembolso.getPorcentaje());
        rev.setParameter("6", sgDesembolso.getMonto());
        rev.setParameter("7", sgDesembolso.getEstado().name());
        rev.setParameter("8", seq);
        rev.setParameter("9", 1);
        rev.executeUpdate();
    }
}
