package gob.mined.siap2.persistence.dao.imp;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.entities.constantes.Constantes;
import gob.mined.siges.entities.data.impl.SgAfBienDepreciable;
import gob.mined.siges.entities.data.impl.SgAfTipoBienes;
import gob.mined.siap2.persistence.dao.imp.eclipselink.EclipselinkJpaDAOImp;
import java.util.List;
import javax.persistence.Query;



@JPADAO
public class BienesDepreciablesDAO extends EclipselinkJpaDAOImp {
    /**
     * Método utilizado para guardar el registro de auditoria sobre la entidad SgAfBienDepreciable
     * 
     * @param bien 
     */
    public void guardarAuditoriaBienDepreciable(SgAfBienDepreciable bien) {
            Query a = entityManager.createNativeQuery("select nextval ('hibernate_sequence')");
            Long seq = (Long) a.getSingleResult();

            Query est = entityManager.createNativeQuery("INSERT INTO revinfo (rev, revtstmp, usuario) values(?1, extract(epoch from now()), ?2)");
            est.setParameter("1", seq);
            est.setParameter("2", bien.getUltUsuario());
            est.executeUpdate();

            Query rev = entityManager.createNativeQuery(
                    "INSERT INTO " + Constantes.SCHEMA_ACTIVO_FIJO + ".sg_af_bienes_depreciables_aud"
                    +  "(bde_pk, bde_codigo_correlativo, bde_codigo_inventario, bde_descripcion, bde_documento_adquisicion, bde_fecha_adquisicion,bde_fecha_creacion, bde_valor_adquisicion, bde_proveedor, bde_cantidad_lote, "
                    +  "bde_calidad_fk, bde_tipo_bien_fk,bde_forma_adquisicion_fk, bde_fuente_financiamiento_fk, bde_estado_fk, bde_ult_mod_usuario, bde_es_lote,bde_es_valor_estimado, bde_numero_partida,"
                    +  "bde_num_correlativo, bde_vida_util, bde_depreciado,bde_tipo_bien_categoria_vinculada, bde_categoria_fk, bde_completado, bde_depreciado_completo, bde_version,bde_bien_es_fuente_siap,bde_es_lote_siap,bde_numero_correlativo_siap,bde_observacion,rev, revtype) "
                    + "VALUES(?1,?2,?3,?4,?5,?6,?7,?8,?9,?10,"
                            + "?11,?12,?13,?14,?15,?16,?17,?18,?19,?20,"
                            + "?21,?22,?23,?24,?25,?26,?27,?28,?29,?30,?31,?32,?33)"
            );
                    
            rev.setParameter("1", bien.getBdePk());
            rev.setParameter("2", bien.getBdeCodigoCorrelativo());
            rev.setParameter("3", bien.getBdeCodigoInventario());
            rev.setParameter("4", bien.getBdeDescripcion());
            rev.setParameter("5", bien.getBdeDocumentoAdquisicion());
            rev.setParameter("6", bien.getBdeFechaAdquisicion());
            rev.setParameter("7", bien.getBdeFechaCreacion());
            rev.setParameter("8", bien.getBdeValorAdquisicion());
            rev.setParameter("9", bien.getBdeProveedor());
            rev.setParameter("10", bien.getBdeCantidadLote());
            
            rev.setParameter("11", bien.getBdeEstadoCalidad() != null ? bien.getBdeEstadoCalidad().getEcaPk() : null);
            rev.setParameter("12", bien.getBdeTipoBien() != null ? bien.getBdeTipoBien().getTbiPk() : null);
            rev.setParameter("13", bien.getBdeFormaAdquisicion() != null ? bien.getBdeFormaAdquisicion().getFadPk() : null);
            rev.setParameter("14", bien.getBdeFuenteFinanciamiento() != null ? bien.getBdeFuenteFinanciamiento().getFfiPk() : null);
            rev.setParameter("15", bien.getBdeEstadosBienes() != null ? bien.getBdeEstadosBienes().getEbiPk() : null);
            rev.setParameter("16", bien.getUltUsuario());
            rev.setParameter("17", bien.getBdeEsLote());
            rev.setParameter("18", bien.getBdeEsValorEstimado());
            rev.setParameter("19", bien.getBdeNumeroPartida());
            rev.setParameter("20", bien.getBdeNumCorrelativo());
            
            rev.setParameter("21", bien.getBdeVidaUtil());
            rev.setParameter("22", bien.getBdeDepreciado());
            rev.setParameter("23", bien.getBdeTipoBienCategoriaVinculada());
            rev.setParameter("24", bien.getBdeCategoriaFk() != null ? bien.getBdeCategoriaFk().getCabPk() : null);
            rev.setParameter("25", bien.getBdeCompletado());
            rev.setParameter("26", bien.getBdeDepreciadoCompleto());
            rev.setParameter("27", bien.getBdeVersion());
            rev.setParameter("28", bien.getBdeBienEsFuenteSiap());
            rev.setParameter("29", bien.getBdeEsLoteSiap());
            rev.setParameter("30", bien.getBdeNumCorrelativoSiap());
            rev.setParameter("31", bien.getBdeDescripcion());
            rev.setParameter("32", seq);
            rev.setParameter("33", 1);
            
            rev.executeUpdate();
    }
    
    /**
     * Este método devuelve todos los tipos de bienes que cumplen la restricción query
     * (vigentes)
     *
     * @return lista de tipos de bienes.
     */
    public List<SgAfTipoBienes> obtenerTiposBienPorQuery(String query) {
        return entityManager.createQuery("SELECT t "
                + " FROM SgAfTipoBienes t"
                + " WHERE t.tbiNombreBusqueda like (:query) or t.tbiCodigo like (:query)"
                + " ORDER BY t.tbiCodigo asc")
                 .setParameter("query", "%"+query.trim().toLowerCase()+"%")
                .setMaxResults(11)
                .getResultList();
    }

    public Integer obtenerUltimoCorrelativo(Long tipoBienId) {
        return (Integer) entityManager.createQuery("SELECT max(b.bdeNumCorrelativoSiap) "
            + " FROM SgAfBienDepreciable b"
            + " WHERE b.bdeTipoBien.tbiPk = :tipoBienId")
             .setParameter("tipoBienId", tipoBienId)
            .setMaxResults(1)
            .getSingleResult();
    }
}
