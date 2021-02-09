package gob.mined.siap2.persistence.dao.imp;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siges.entities.data.impl.SgPresupuestoEscolar;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.persistence.dao.imp.eclipselink.EclipselinkJpaDAOImp;
import gob.mined.siges.entities.data.impl.SgMovimientosEdicion;
import gob.mined.siges.entities.data.impl.SgPresupuestoEscolarMovimiento;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Query;



@JPADAO
public class PresupuestoEscolarDAO extends EclipselinkJpaDAOImp<SgPresupuestoEscolar, Integer> implements  Serializable{

    /**
     * Este método busca un registro de PresupuestoEscolar filtrado por Anio y Sede
     * @param anio
     * @param idSede
     * @return
     * @throws DAOGeneralException
     */
    public SgPresupuestoEscolar getPresupuestoEscolarByAnioYSede(Integer anio, Integer idSede) throws DAOGeneralException {
        List<SgPresupuestoEscolar> lista = entityManager
                .createQuery("select g from SgPresupuestoEscolar g where g.anioFiscal.anio = :anio AND g.sede.id = :idSede")
                .setParameter("anio", anio)
                .setParameter("idSede", idSede)
                .getResultList();
        if(lista != null && !lista.isEmpty() && lista.size() > 0) {
            return lista.get(0);
        } return null;
    }

    /**
     * Este método busca un registro de PresupuestoEscolar filtrado por Anio y Sede
     * @param anio
     * @param idSede
     * @return
     * @throws DAOGeneralException
     */
    public SgPresupuestoEscolarMovimiento getMovimientoPresupuestoByPresupuestoId(Integer presupuesto) throws DAOGeneralException {
        List<SgPresupuestoEscolarMovimiento> lista = entityManager
                .createQuery("select g from SgPresupuestoEscolarMovimiento g where g.topePresupuestal.id = :techo")
                .setParameter("techo", presupuesto)
                .getResultList();
        if(lista != null && !lista.isEmpty() && lista.size() > 0) {
            return lista.get(0);
        } return null;
    }
    
    /**
     * Este método busca un registro de SgMovimientosEdicion filtrado por id de movimiento escolar
     * @param movimiento
     * @param anio
     * @param idSede
     * @return
     * @throws DAOGeneralException
     */
    public SgMovimientosEdicion getMovimientoEdicionByMovimientoId(Integer movimiento) throws DAOGeneralException {
        List<SgMovimientosEdicion> lista = entityManager
                .createQuery("select g from SgMovimientosEdicion g where g.originalEditado.id = :movId")
                .setParameter("movId", movimiento).setMaxResults(1)
                .getResultList();
        if(lista != null && !lista.isEmpty() && lista.size() > 0) {
            return lista.get(0);
        } return null;
    }
    
    
    /**
     * Método utilizado para guardar el registro de auditoria sobre la entidad PresupuestoEscolar
     * 
     * @param presupuestoEscolar 
     */
    public void guardarAuditoriaPrespuestoEscolar(SgPresupuestoEscolar presupuestoEscolar) {
            Query a = entityManager.createNativeQuery("select nextval ('hibernate_sequence')");
            Long seq = (Long) a.getSingleResult();

            Query est = entityManager.createNativeQuery("INSERT INTO revinfo (rev, revtstmp, usuario) values(?1, extract(epoch from now()), ?2)");
            est.setParameter("1", seq);
            est.setParameter("2", presupuestoEscolar.getUltUsuario());
            est.executeUpdate();

            Query rev = entityManager.createNativeQuery(
                    "INSERT INTO finanzas.sg_presupuesto_escolar_aud"
                    + "(pes_pk, pes_codigo, pes_habilitado, pes_nombre, pes_nombre_busqueda, pes_descripcion, pes_estado, pes_sede_fk, pes_ult_mod_fecha, pes_ult_mod_usuario, pes_version, pes_anio_fiscal_fk, rev, revtype)"
                    + "VALUES(?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9, ?10, ?11, ?12, ?13, ?14)"
            );

            rev.setParameter("1", presupuestoEscolar.getPk());
            rev.setParameter("2", presupuestoEscolar.getCodigo());
            rev.setParameter("3", presupuestoEscolar.getHabilitado());
            rev.setParameter("4", presupuestoEscolar.getNombre());
            rev.setParameter("5", presupuestoEscolar.getNombreBusqueda());
            rev.setParameter("6", presupuestoEscolar.getDescripcion());
            rev.setParameter("7", presupuestoEscolar.getEstado().getText());
            rev.setParameter("8", presupuestoEscolar.getSede().getId());
            rev.setParameter("9", presupuestoEscolar.getUltMod());
            rev.setParameter("10", presupuestoEscolar.getUltUsuario());
            rev.setParameter("11", presupuestoEscolar.getVersion());
            rev.setParameter("12", presupuestoEscolar.getAnioFiscal().getId());
            rev.setParameter("13", seq);
            rev.setParameter("14", 1);
            rev.executeUpdate();
    }
    
    /**
     * Método utilizado para guardar el registro de auditoria sobre la entidad PresupuestoEscolarMovimiento
     * 
     * @param presupuestoEscolarMovimiento 
     */
    public void guardarAuditoriaPrespuestoEscolarMovimiento(SgPresupuestoEscolarMovimiento presupuestoEscolarMovimiento) {
            Query a = entityManager.createNativeQuery("select nextval ('hibernate_sequence')");
            Long seq = (Long) a.getSingleResult();

            Query est = entityManager.createNativeQuery("INSERT INTO revinfo (rev, revtstmp, usuario) values(?1, extract(epoch from now()), ?2)");
            est.setParameter("1", seq);
            est.setParameter("2", presupuestoEscolarMovimiento.getUltUsuario());
            est.executeUpdate();

            Query rev = entityManager.createNativeQuery(
                    "INSERT INTO finanzas.sg_presupuesto_escolar_movimiento_aud"
                    + "(mov_pk, mov_monto, mov_monto_aprobado, mov_num_movimiento, mov_origen, mov_presupuesto_fk, mov_tipo, mov_techo_presupuestal, mov_ult_mod_fecha, mov_ult_mod_usuario, mov_version,mov_anulacion,mov_editado, rev, revtype)"
                    + "VALUES(?1, ?2::decimal, ?3::decimal, ?4::decimal, ?5, ?6, ?7, ?8, ?9, ?10, ?11, ?12, ?13,?14,?15)"
            );

            rev.setParameter("1", presupuestoEscolarMovimiento.getId());
            rev.setParameter("2", presupuestoEscolarMovimiento.getMonto());
            rev.setParameter("3", presupuestoEscolarMovimiento.getMontoAprobado());
            rev.setParameter("4", presupuestoEscolarMovimiento.getNumMovimiento());
            rev.setParameter("5", presupuestoEscolarMovimiento.getOrigen());
            rev.setParameter("6", presupuestoEscolarMovimiento.getPresupuestoEscolar().getPk());
            rev.setParameter("7", presupuestoEscolarMovimiento.getTipo());
            rev.setParameter("8", presupuestoEscolarMovimiento.getTopePresupuestal().getId());
            rev.setParameter("9", presupuestoEscolarMovimiento.getUltMod());
            rev.setParameter("10", presupuestoEscolarMovimiento.getUltUsuario());
            rev.setParameter("11", presupuestoEscolarMovimiento.getVersion());
            rev.setParameter("12", presupuestoEscolarMovimiento.getAnulacion());
            rev.setParameter("13", presupuestoEscolarMovimiento.geteEditado());
            rev.setParameter("14", seq);
            rev.setParameter("15", 1);
            rev.executeUpdate();
    }
    
    /**
     * Método utilizado para guardar el registro de auditoria sobre la entidad PresupuestoEscolarMovimiento
     * 
     * @param presupuestoEscolarMovimiento 
     */
    public void guardarAuditoriaPrespuestoEscolarMovimientoEdicion(SgMovimientosEdicion movimientosEdicion) {
            Query a = entityManager.createNativeQuery("select nextval ('hibernate_sequence')");
            Long seq = (Long) a.getSingleResult();

            Query est = entityManager.createNativeQuery("INSERT INTO revinfo (rev, revtstmp, usuario) values(?1, extract(epoch from now()), ?2)");
            est.setParameter("1", seq);
            est.setParameter("2", movimientosEdicion.getUltmodUsuario());
            est.executeUpdate();

            Query rev = entityManager.createNativeQuery(
                    "INSERT INTO finanzas.sg_presupuesto_escolar_edicion_movimiento_aud"
                    + "(mov_pk, mov_codigo,mov_num_movimiento, mov_presupuesto_fk,mov_tipo,mov_monto,mov_monto_aprobado,mov_origen,mov_original_editado,mov_techo_presupuestal,"
                            + "mov_ult_mod_fecha,mov_ult_mod_usuario,mov_version, rev, revtype)"
                    + "VALUES(?1,?2,?3,?4,?5,?6::decimal, ?7::decimal, ?8, ?9, ?10, ?11, ?12, ?13, ?14, ?15)"
            );

            rev.setParameter("1", movimientosEdicion.getId());
            rev.setParameter("2", movimientosEdicion.getCodigo());
            rev.setParameter("3", movimientosEdicion.getNumMoviento());
            rev.setParameter("4", movimientosEdicion.getPresupuestoFk().getPk());
            rev.setParameter("5", movimientosEdicion.getTipo());
            rev.setParameter("6", movimientosEdicion.getMonto());
            rev.setParameter("7", movimientosEdicion.getMontoAprobado());
            rev.setParameter("8", movimientosEdicion.getOrigen());
            rev.setParameter("9", movimientosEdicion.getOriginalEditado().getId());
            rev.setParameter("10", movimientosEdicion.getTechoPresupuestal().getId());
            rev.setParameter("11", movimientosEdicion.getUltmodFecha());
            rev.setParameter("12", movimientosEdicion.getUltmodUsuario());
            rev.setParameter("13", movimientosEdicion.getVersion());
            rev.setParameter("14", seq);
            rev.setParameter("15", 1);
            rev.executeUpdate();
    }
}
