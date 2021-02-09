package gob.mined.siap2.persistence.dao.imp;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.entities.data.impl.TransferenciaDireccionDepartamental;
import gob.mined.siap2.entities.data.impl.TransferenciasACed;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.persistence.dao.imp.eclipselink.EclipselinkJpaDAOImp;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Query;


@JPADAO
public class TransferenciasACedDAO extends EclipselinkJpaDAOImp<TransferenciasACed, Integer> implements  Serializable{
    
    
    /**
     * Este método busca un registro de PresupuestoEscolar filtrado por Anio y Sede
     * @param idTransfComp
     * @param idSede
     * @return
     * @throws DAOGeneralException 
     */
    public TransferenciasACed getTransferenciaACedByComponenteYSede(Integer idTransfComp, Integer idSede) throws DAOGeneralException {
        return (TransferenciasACed) entityManager
                .createQuery("select g from TransferenciasACed g where g.ced.id = :idSede and g.transferencia.id = :idTransfComp")
                .setParameter("idTransfComp", idTransfComp)
                .setParameter("idSede", idSede)
                .getSingleResult();
    }
    
    /**
     * Este método busca un registro de PresupuestoEscolar filtrado por Anio y Sede
     * @param idTc
     * @param idSede
     * @param idTDD
     * @return
     * @throws DAOGeneralException 
     */
    public TransferenciasACed getTransferenciaACedByComponenteSedeTDD(Integer idTc, Integer idSede, Integer idTDD) throws DAOGeneralException {
        return (TransferenciasACed) entityManager
                .createQuery("select g from TransferenciasACed g where g.ced.id = :idSede and g.transferencia.id = :idTransfComp and g.transferenciaDireccionDep.pk = :idTDD")
                .setParameter("idTransfComp", idTc)
                .setParameter("idSede", idSede)
                .setParameter("idTDD", idTDD)
                .getSingleResult();
    }
    
    /**
     * Este método obtiene el total de las tranaferencias de una sede por año y departamental
     * @param idTc
     * @param idSede
     * @param idTDD
     * @return
     * @throws DAOGeneralException 
     */
    public BigDecimal getTotalMontoTransferenciaACedByComponenteSedeTDD(Integer componenteId, Integer subComponenteId, Integer unidadId, Integer lineaId, Integer fuenteRecursosId,Integer anioId, Integer idSede) throws DAOGeneralException {
        return (BigDecimal) entityManager
                .createQuery("select sum(g.montoAutorizado) from TransferenciasACed g where "
                        + "g.transferencia.componente.id = :componenteId "
                        + "and g.transferencia.subcomponente.id = :subComponenteId "
                        + "and g.transferencia.unidadPresupuestaria.id = :unidadId "
                        + "and g.transferencia.lineaPresupuestaria.id = :lineaId "
                        + "and g.transferencia.fuenteRecursos.id = :fuenteRecursosId "
                        + "and g.transferencia.anioFiscal.id = :anioId "
                        + "and g.ced.id = :idSede")
                .setParameter("componenteId", componenteId)
                .setParameter("subComponenteId", subComponenteId)
                .setParameter("unidadId", unidadId)
                .setParameter("lineaId", lineaId)
                .setParameter("fuenteRecursosId", fuenteRecursosId)
                .setParameter("anioId", anioId)
                .setParameter("idSede", idSede)
                .getSingleResult();
    }
    
    /**
     * Este método busca todos los registros que pertenezcan a una TransferenciaComponente
     * @param idTransfComp
     * @return
     * @throws DAOGeneralException 
     */
    public List<TransferenciasACed> getTransferenciasACedByTransfComp(Integer idTransfComp) throws DAOGeneralException {
        return (List<TransferenciasACed>) entityManager
                .createQuery("select g from TransferenciasACed g where g.transferencia.id = :idTransfComp")
                .setParameter("idTransfComp", idTransfComp)
                .getResultList();
    }
    
    /**
     * Metodo utilizado para guardar los registros de auditoria de la tabla TransferenciasACed
     * @param transferenciasACed 
     */
    public void guardarAuditoriaTransferenciasACed(TransferenciasACed transferenciasACed) {
        Query a = entityManager.createNativeQuery("select nextval ('hibernate_sequence')");
        Long seq = (Long) a.getSingleResult();

        Query est = entityManager.createNativeQuery("INSERT INTO revinfo (rev, revtstmp, usuario) values(?1, extract(epoch from now()), ?2)");
        est.setParameter("1", seq);
        est.setParameter("2", transferenciasACed.getUltModUsuario());
        est.executeUpdate();

        Query rev = entityManager.createNativeQuery(
                "INSERT INTO finanzas.sg_transferencias_a_ced_aud("
                + "tac_pk, tac_ced_fk, tac_transferencia_fk, tac_transferencia_direccion_dep_fk, tac_monto_autorizado, "
                + "tac_monto_solicitado, tac_estado, tac_ult_mod_fecha, tac_ult_mod_usuario, tac_version, rev, revtype"
                + ")"
                + "VALUES(?1, ?2, ?3, ?4, ?5::decimal, ?6::decimal, ?7, ?8, ?9, ?10, ?11, ?12)"
        );

        rev.setParameter("1", transferenciasACed.getId());
        rev.setParameter("2", transferenciasACed.getCed().getId());
        rev.setParameter("3", transferenciasACed.getTransferencia().getId());
        rev.setParameter("4", transferenciasACed.getTransferenciaDireccionDep().getPk());
        rev.setParameter("5", transferenciasACed.getMontoAutorizado());
        rev.setParameter("6", transferenciasACed.getMontoSolicitado());
        rev.setParameter("7", transferenciasACed.getEstado().name());
        rev.setParameter("8", transferenciasACed.getUltModFecha());
        rev.setParameter("9", transferenciasACed.getUltModUsuario());
        rev.setParameter("10", transferenciasACed.getVersion());
        rev.setParameter("11", seq);
        rev.setParameter("12", 1);
        rev.executeUpdate();
    }
    
    /**
     * Metodo utilizado para guardar los registros de auditoria de la tabla TransferenciaDireccionDepartamental
     * @param transferenciasDD 
     */
    public void guardarAuditoriaTransferenciasDepartamental(TransferenciaDireccionDepartamental transferenciasDD) {
        Query a = entityManager.createNativeQuery("select nextval ('hibernate_sequence')");
        Long seq = (Long) a.getSingleResult();

        Query est = entityManager.createNativeQuery("INSERT INTO revinfo (rev, revtstmp, usuario) values(?1, extract(epoch from now()), ?2)");
        est.setParameter("1", seq);
        est.setParameter("2", transferenciasDD.getUltModUsuario());
        est.executeUpdate();

        Query rev = entityManager.createNativeQuery(
                "INSERT INTO finanzas.sg_transferencia_direccion_departamental_aud"
                + "(tdd_pk, tdd_transferencia_fk, tdd_direccion_dep_fk, tdd_monto_autorizado, tdd_monto_solicitado, tdd_estado, tdd_ult_mod_fecha, tdd_ult_mod_usuario, tdd_version, rev, revtype)"
                + "VALUES(?1, ?2, ?3, ?4::decimal, ?5::decimal, ?6, ?7, ?8, ?9, ?10, ?11)"
        );

        rev.setParameter("1", transferenciasDD.getPk());
        rev.setParameter("2", transferenciasDD.getTransferenciasComponente().getId());
        rev.setParameter("3", transferenciasDD.getDireccionDep().getPk());
        rev.setParameter("4", transferenciasDD.getMontoAutorizado());
        rev.setParameter("5", transferenciasDD.getMontoSolicitado());
        rev.setParameter("6", transferenciasDD.getEstado().name());
        rev.setParameter("7", transferenciasDD.getUltModFecha());
        rev.setParameter("8", transferenciasDD.getUltModUsuario());
        rev.setParameter("9", transferenciasDD.getVersion());
        rev.setParameter("10", seq);
        rev.setParameter("11", 1);
        rev.executeUpdate();
    }
}
