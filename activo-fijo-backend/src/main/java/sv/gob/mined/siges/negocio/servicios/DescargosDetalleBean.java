/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.opentracing.Traced;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.dto.DetalleDescargo;
import sv.gob.mined.siges.enumerados.TipoUnidad;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroDescargosDetalle;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.DescargosDetalleDAO;
import sv.gob.mined.siges.persistencia.entidades.SgAfDescargoDetalle;

@Stateless
@Traced
public class DescargosDetalleBean {
    
    @PersistenceContext
    private EntityManager em;
    
    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgAfDescargoDetalle
     * @throws GeneralException
     * 
     */
    public SgAfDescargoDetalle obtenerPorId(Long id, Boolean dataSecurity) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgAfDescargoDetalle> codDAO = new CodigueraDAO<>(em, SgAfDescargoDetalle.class);
                if (BooleanUtils.isTrue(dataSecurity)){
                    return codDAO.obtenerPorId(id, ConstantesOperaciones.BUSCAR_SOLICITUDES_DESCARGO);
                } else {
                    return codDAO.obtenerPorId(id, null);
                }
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return null;
    }

    /**
     * Devuelve si el objeto existe
     *
     * @param id Long
     * @return Boolean
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Boolean objetoExistePorId(Long id, Boolean dataSecurity) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgAfDescargoDetalle> codDAO = new CodigueraDAO<>(em, SgAfDescargoDetalle.class);
                if (BooleanUtils.isTrue(dataSecurity)){
                    return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_SOLICITUDES_DESCARGO);
                } else {
                    return codDAO.objetoExistePorId(id, null);
                }
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }
    
    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroDescargosDetalle
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroDescargosDetalle filtro) throws GeneralException {
        try {
            DescargosDetalleDAO codDAO = new DescargosDetalleDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroDescargosDetalle
     * @return Lista de <SgAfDescargosDetalle>
     * @throws GeneralException
     */
    public List<SgAfDescargoDetalle> obtenerPorFiltro(FiltroDescargosDetalle filtro) throws GeneralException {
        try {
            DescargosDetalleDAO codDAO = new DescargosDetalleDAO(em);
            return codDAO.obtenerPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgAfDescargoDetalle
     * @throws GeneralException
     * 
     */
    public DetalleDescargo obtenerPorIdDTO(Long id) throws GeneralException {
        if (id != null) {
            try {
                FiltroDescargosDetalle filtro = new FiltroDescargosDetalle();
                filtro.setBienId(id);
                List<DetalleDescargo> lista = obtenerDetallePorFiltro(filtro);
                if(lista != null) {
                    return lista.get(0);
                }
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return null;
    }
    
    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroDescargosDetalle
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltroDTO(FiltroDescargosDetalle filtro) throws GeneralException {
        try {
            String queryDescargo = "select count(datos.id) as conteo " +
                " from (select b.bde_pk as id, " +
                "          b.bde_codigo_inventario as codigoInventario," +
                "	   b.bde_descripcion as descripcion," +
                "	   b.bde_marca as marca," +
                "	   b.bde_modelo as modelo," +
                "	   b.bde_no_serie as noserie," +
                "	   b.bde_fecha_adquisicion as fechaAdquisicion," +
                "	   b.bde_valor_adquisicion as valoradquisicion," +
                "	   b.bde_fecha_descargo as fechaDescargo," +
                "          d.des_fecha_solicitud as fechaSolicitudDes," +
                "          to_char(d.des_fecha_solicitud, 'DD/MM/YYYY') as fechaSolicitud," +
                "	   to_char(d.des_fecha_creacion, 'DD/MM/YYYY') as fechaCreacion," +
                "	   depre.depreciacion as depreciacion," +
                "	   cat.cab_nombre as nombreCategoria," +
                "	   CASE WHEN b.bde_unidad_administrativa_fk IS NOT NULL THEN 'UA'" +
                "         	WHEN b.bde_sede_fk IS NOT NULL THEN 'CE'" +
                "       END AS tipo_unidad," +
                "       CASE when b.bde_unidad_administrativa_fk is not null then uad.uad_nombre " +
                "           when b.bde_sede_fk is not null then sed.sed_nombre" +
                "	end as nombre_unidad," +
                "	case when b.bde_unidad_administrativa_fk is not null then uad.uad_codigo " +
                "           when b.bde_sede_fk is not null then sed.sed_codigo " +
                "	end as codigo_unidad," +
                "       case when b.bde_unidad_administrativa_fk is not null then uad.uad_direccion\n" +
                "	    when b.bde_sede_fk is not null then dir.dir_direccion\n" +
                "	end as direccion_unidad," + 
                "       CASE WHEN b.bde_unidad_administrativa_fk IS NOT NULL THEN b.bde_unidad_administrativa_fk" +
                "             WHEN b.bde_sede_fk IS NOT NULL THEN b.bde_sede_fk" +
                "       END AS id_unidad," +
                "       coalesce(cat2.cab_clasificacion_bienes_fk,cat.cab_clasificacion_bienes_fk) as clasificacion_id," +
                "       coalesce(b.bde_categoria_fk,tbi.tbi_categoria_bienes_fk) as categoria_id," +
                "       uaf.uaf_pk as uaf_id," +
                "       coalesce(uaf.uaf_departamento_fk,depa.dep_pk) as depa_id," +
                "       d.des_estado_fk estado_id," +
                "       d.des_pk as descargo_id," +
                "       edes.ede_nombre as tipoDecargadoNombre," +
                "       d.des_activo as activo," +
                "       d.des_codigo_descargo as codigo_descargo" +
                " from activo_fijo.sg_af_descargos_detalle det" +
                "	inner join activo_fijo.sg_af_descargos d on (d.des_pk = det.dde_descargo_fk)" +
                "	inner join activo_fijo.sg_af_bienes_depreciables b on (det.dde_bienes_depreciables_fk = b.bde_pk)" +
                "	inner JOIN catalogo.sg_af_tipo_bienes tbi ON tbi.tbi_pk = b.bde_tipo_bien_fk" +
                "       inner join catalogo.sg_af_estados_descargo edes on (edes.ede_pk = d.des_tipo_descargo_fk)" +
                "       left outer JOIN catalogo.sg_af_categoria_bienes cat ON cat.cab_pk = tbi.tbi_categoria_bienes_fk" +
                "       left outer join catalogo.sg_af_categoria_bienes cat2 on (cat2.cab_pk = b.bde_categoria_fk)" +
                "	left outer JOIN catalogo.sg_af_unidades_administrativas uad on (b.bde_unidad_administrativa_fk = uad.uad_pk) " +
                "	left outer JOIN catalogo.sg_af_unidades_activo_fijo uaf on (uad.uad_unidad_activo_fijo_fk = uaf.uaf_pk)" +
                "	left outer JOIN centros_educativos.sg_sedes sed on (sed.sed_pk = b.bde_sede_fk)" +
                "	left outer JOIN centros_educativos.sg_direcciones dir on (dir.dir_pk = sed.sed_direccion_fk)" +
                "	left outer JOIN catalogo.sg_departamentos depa on (depa.dep_pk = dir.dir_departamento)" +
                "	left outer join(" +
                "		select bien.bde_pk,sum(dep.dep_monto_depreciacion) as depreciacion" +
                "		from activo_fijo.sg_af_depreciaciones dep " +
                "        inner join activo_fijo.sg_af_bienes_depreciables bien on (bien.bde_pk = dep.dep_bienes_depreciables_fk)" +
                "        group by bien.bde_pk" +
                "        ) depre on (depre.bde_pk = det.dde_bienes_depreciables_fk)) datos" ;
 
            filtro.setFirst(null);
            filtro.setMaxResults(null);
            filtro.setOrderBy(null);
            filtro.setAscending(null);
            queryDescargo = queryDescargo +" "+  generarWhere(filtro);
            Query query = em.createNativeQuery(queryDescargo);
            query = setParametros(filtro, query);
            BigInteger result = (BigInteger) query.getSingleResult();
            
            return result.longValue();
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroDescargosDetalle
     * @return Lista de <DetalleDescargo>
     * @throws GeneralException
     */
    public List<DetalleDescargo> obtenerDetallePorFiltro(FiltroDescargosDetalle filtro) throws GeneralException {
        try {
            Session session = em.unwrap(Session.class);
            
            String queryDescargo = "select datos.id, datos.codigoInventario," +
                "   datos.descripcion," +
                "   datos.marca," +
                "   datos.modelo," +
                "   datos.noserie," +
                "   to_char(datos.fechaAdquisicion, 'DD/MM/YYYY') as fechaAdquisicion," +
                "   coalesce(datos.valoradquisicion,0.0) as valoradquisicion," +
                "   to_char(datos.fechaDescargo, 'DD/MM/YYYY') as fechaDescargo, " +
                "   coalesce(datos.fechaSolicitud, datos.fechaCreacion) as fechaSolicitud, " +
                "   to_char(datos.fechaFallo, 'DD/MM/YYYY') as fechaFallo, " +
                "   coalesce(datos.depreciacion,0.0) as depreciacion," +
                "   datos.nombreCategoria," +
                "   datos.tipoDecargadoNombre, " +
                "   datos.tipo_unidad as tipoUnidad," +
                "   datos.nombre_unidad as nombreUnidad," +
                "   datos.codigo_unidad as codigoUnidad, " +
                "   datos.direccion_unidad as direccionUnidad, " +
                "   datos.codigo_descargo as codigoSolicitud " +
                " from (select b.bde_pk as id, " +
                "          b.bde_codigo_inventario as codigoInventario," +
                "	   b.bde_descripcion as descripcion," +
                "	   b.bde_marca as marca," +
                "	   b.bde_modelo as modelo," +
                "	   b.bde_no_serie as noserie," +
                "	   b.bde_fecha_adquisicion as fechaAdquisicion," +
                "	   b.bde_valor_adquisicion as valoradquisicion," +
                "	   b.bde_fecha_descargo as fechaDescargo," +
                "	   d.des_fecha_solicitud as fechaSolicitudDes," +   
                "          to_char(d.des_fecha_solicitud, 'DD/MM/YYYY') as fechaSolicitud," +
                "	   to_char(d.des_fecha_creacion, 'DD/MM/YYYY') as fechaCreacion," +
                "          d.des_fecha_fallo as fechaFallo," +
                "	   depre.depreciacion as depreciacion," +
                "	   cat.cab_nombre as nombreCategoria," +
                "	   CASE WHEN b.bde_unidad_administrativa_fk IS NOT NULL THEN 'UA'" +
                "         	WHEN b.bde_sede_fk IS NOT NULL THEN 'CE'" +
                "       END AS tipo_unidad," +
                "       CASE when b.bde_unidad_administrativa_fk is not null then uad.uad_nombre " +
                "           when b.bde_sede_fk is not null then sed.sed_nombre" +
                "	end as nombre_unidad," +
                "	case when b.bde_unidad_administrativa_fk is not null then uad.uad_codigo " +
                "           when b.bde_sede_fk is not null then sed.sed_codigo " +
                "	end as codigo_unidad," +
                "       case when b.bde_unidad_administrativa_fk is not null then uad.uad_direccion\n" +
                "	    when b.bde_sede_fk is not null then dir.dir_direccion\n" +
                "	end as direccion_unidad," + 
                "       CASE WHEN b.bde_unidad_administrativa_fk IS NOT NULL THEN b.bde_unidad_administrativa_fk" +
                "             WHEN b.bde_sede_fk IS NOT NULL THEN b.bde_sede_fk" +
                "       END AS id_unidad," +
                "       coalesce(cat2.cab_clasificacion_bienes_fk,cat.cab_clasificacion_bienes_fk) as clasificacion_id," +
                "       coalesce(b.bde_categoria_fk,tbi.tbi_categoria_bienes_fk) as categoria_id," +
                "       uaf.uaf_pk as uaf_id," +
                "       coalesce(uaf.uaf_departamento_fk,depa.dep_pk) as depa_id," +
                "       d.des_estado_fk estado_id," +
                "       d.des_pk as descargo_id," +
                "       edes.ede_nombre as tipoDecargadoNombre," +
                "       d.des_activo as activo," +
                "       d.des_codigo_descargo as codigo_descargo" +
                " from activo_fijo.sg_af_descargos_detalle det" +
                "	inner join activo_fijo.sg_af_descargos d on (d.des_pk = det.dde_descargo_fk)" +
                "	inner join activo_fijo.sg_af_bienes_depreciables b on (det.dde_bienes_depreciables_fk = b.bde_pk)" +
                "	inner JOIN catalogo.sg_af_tipo_bienes tbi ON tbi.tbi_pk = b.bde_tipo_bien_fk" +
                "       inner join catalogo.sg_af_estados_descargo edes on (edes.ede_pk = d.des_tipo_descargo_fk)" +
                "       left outer JOIN catalogo.sg_af_categoria_bienes cat ON cat.cab_pk = tbi.tbi_categoria_bienes_fk" +
                "       left outer join catalogo.sg_af_categoria_bienes cat2 on (cat2.cab_pk = b.bde_categoria_fk)" +
                "	left outer JOIN catalogo.sg_af_unidades_administrativas uad on (b.bde_unidad_administrativa_fk = uad.uad_pk) " +
                "	left outer JOIN catalogo.sg_af_unidades_activo_fijo uaf on (uad.uad_unidad_activo_fijo_fk = uaf.uaf_pk)" +
                "	left outer JOIN centros_educativos.sg_sedes sed on (sed.sed_pk = b.bde_sede_fk)" +
                "	left outer JOIN centros_educativos.sg_direcciones dir on (dir.dir_pk = sed.sed_direccion_fk)" +
                "	left outer JOIN catalogo.sg_departamentos depa on (depa.dep_pk = dir.dir_departamento)" +
                "	left outer join(" +
                "		select bien.bde_pk,sum(dep.dep_monto_depreciacion) as depreciacion" +
                "		from activo_fijo.sg_af_depreciaciones dep " +
                "        inner join activo_fijo.sg_af_bienes_depreciables bien on (bien.bde_pk = dep.dep_bienes_depreciables_fk)" +
                "        group by bien.bde_pk" +
                "        ) depre on (depre.bde_pk = det.dde_bienes_depreciables_fk)) datos" ;
            
            queryDescargo = queryDescargo +" "+ generarWhere(filtro);

            SQLQuery query = session.createSQLQuery(queryDescargo);
            
            query.addScalar("id", new LongType());
            query.addScalar("codigoInventario", new StringType());
            query.addScalar("descripcion", new StringType());
            query.addScalar("marca", new StringType());
            query.addScalar("modelo", new StringType());
            query.addScalar("noserie", new StringType());
            query.addScalar("fechaAdquisicion", new StringType());
            query.addScalar("valoradquisicion", new BigDecimalType());
            query.addScalar("fechaDescargo", new StringType()); 
            query.addScalar("depreciacion", new BigDecimalType());
            query.addScalar("nombreCategoria", new StringType()); 
            query.addScalar("tipoDecargadoNombre", new StringType());  
            query.addScalar("tipoUnidad", new StringType());
            query.addScalar("nombreUnidad", new StringType());
            query.addScalar("codigoUnidad", new StringType());
            query.addScalar("direccionUnidad", new StringType());
            query.addScalar("fechaSolicitud", new StringType());
            query.addScalar("codigoSolicitud", new StringType());
            query.addScalar("fechaFallo", new StringType());
            query = setParametros(filtro, query);
  
            query.setResultTransformer(Transformers.aliasToBean(DetalleDescargo.class));
            
            return query.list();
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    private String generarWhere(FiltroDescargosDetalle filtro) {
        String queryDescargo = ""; 
        String where = "";
        if(filtro.getTipoUnidad() != null) {
            where += " datos.tipo_unidad = :tipounidad";
            if(TipoUnidad.UNIDAD_ADMINISTRATIVA.equals(filtro.getTipoUnidad()) && filtro.getUnidadActivoFijoId() != null) {
                where += " AND datos.uaf_id = :uafid ";
            } else if(TipoUnidad.CENTRO_ESCOLAR.equals(filtro.getTipoUnidad()) && filtro.getDepartamentoId() != null) {
                where += " AND datos.depa_id = :depid ";
            }
        } else if(filtro.getDepartamentoId() != null) {
            where += " datos.depa_id = :depid";
        }

        if(filtro.getBienId() != null) {
            if(StringUtils.isNotBlank(where.trim()))
                where += " AND ";
            where += " datos.id = :id";
        }

        if(filtro.getUnidadAdministrativaId() != null) {
            if(StringUtils.isNotBlank(where.trim()))
                where += " AND ";
            where += " datos.id_unidad = :idunidad";
        }
        if(filtro.getDescargoId() != null) {
            if(StringUtils.isNotBlank(where.trim()))
                where += " AND ";
            where += " datos.descargo_id = :descargoId";
        }
        if(filtro.getClasificacionId() != null) {
            if(StringUtils.isNotBlank(where.trim()))
                where += " AND ";
            where += " datos.clasificacion_Id = :idClasificacion";
        }

        if(filtro.getCategoriaId() != null) {
            if(StringUtils.isNotBlank(where.trim()))
                where += " AND ";
            where += " datos.categoria_id = :idCategoria";
        }

        if(filtro.getEstadoId()!= null) {
            if(StringUtils.isNotBlank(where.trim()))
                where += " AND ";
            where += " datos.estado_id = :idEstado";
        }

        if(filtro.getCodigoInventario()!= null) {
            if(StringUtils.isNotBlank(where.trim()))
                where += " AND ";
            where += " datos.codigoInventario = :codInventario";
        }/**
        if(filtro.getNumeroActa() != null) {
            if(StringUtils.isNotBlank(where.trim()))
                where += " AND ";
            where += " datos.codigo_descargo = :numActa";
        }**/

        if(filtro.getActivos() != null) {
            if(StringUtils.isNotBlank(where.trim()))
                where += " AND ";
            where += " datos.activo = :activo";
        }
        if(filtro.getCodigoDescargo() != null) {
            if(StringUtils.isNotBlank(where.trim()))
                where += " AND ";
            where += " datos.codigo_descargo = :codigoDescargo";
        }
        
        if(filtro.getFechaSolicitudDesde() != null) {
            if(StringUtils.isNotBlank(where.trim()))
                where += " AND ";
            where += " datos.fechaSolicitudDes >= :fechaSolicitudDesde";
        }
        if(filtro.getFechaSolicitudHasta() != null) {
            if(StringUtils.isNotBlank(where.trim()))
                where += " AND ";
            where += " datos.fechaSolicitudDes <= :fechaSolicitudHasta";
        }
        
        if(filtro.getFechaDescargoDesde() != null) {
            if(StringUtils.isNotBlank(where.trim()))
                where += " AND ";
            where += " datos.fechaDescargo >= :fechaDescargoDesde";
        }
        if(filtro.getFechaDescargoHasta() != null) {
            if(StringUtils.isNotBlank(where.trim()))
                where += " AND ";
            where += " datos.fechaDescargo <= :fechaDescargoHasta";
        }
        if(StringUtils.isNotBlank(where.trim())) {
            where = " WHERE " + where;
        }

        queryDescargo = queryDescargo +  where ;


        if(filtro.getOrderBy() != null && filtro.getOrderBy().length > 0) {
            queryDescargo = queryDescargo + " order by ";
            for(String campo : filtro.getOrderBy()) {
                queryDescargo = queryDescargo + " datos." + campo;
            }
        }

        if(filtro.getAscending() != null && filtro.getAscending().length > 0) {
            if(filtro.getAscending()[0]) {
                queryDescargo = queryDescargo + " asc";
            } else {
                queryDescargo = queryDescargo + " desc";
            }

        }

        if(filtro.getMaxResults() != null) {
            queryDescargo = queryDescargo + " limit :limite";
        }
        if(filtro.getFirst() != null) {
            queryDescargo = queryDescargo + " OFFSET :inicio";
        }
        return queryDescargo;
    }
    private SQLQuery setParametros(FiltroDescargosDetalle filtro, SQLQuery query) {
        if(filtro.getTipoUnidad() != null) {
            if(TipoUnidad.UNIDAD_ADMINISTRATIVA.equals(filtro.getTipoUnidad())) {
                query.setParameter("tipounidad", "UA");
                if(filtro.getUnidadActivoFijoId() != null) {
                    query.setParameter("uafid", filtro.getUnidadActivoFijoId());
                }
            } else if(TipoUnidad.CENTRO_ESCOLAR.equals(filtro.getTipoUnidad())) {
                query.setParameter("tipounidad", "CE");
                if(filtro.getDepartamentoId() != null) {
                    query.setParameter("depid", filtro.getDepartamentoId());
                }
            }
        } else if(filtro.getDepartamentoId() != null) {
            query.setParameter("depid", filtro.getDepartamentoId() );
        }
        if(filtro.getBienId() != null) {
            query.setParameter("id", filtro.getBienId() );
        }
        if(filtro.getUnidadAdministrativaId() != null) {
            query.setParameter("idunidad", filtro.getUnidadAdministrativaId());
        }
        if(filtro.getDescargoId() != null) {
            query.setParameter("descargoId", filtro.getDescargoId());
        }
        if(filtro.getClasificacionId() != null) {
            query.setParameter("idClasificacion", filtro.getClasificacionId());
        }

        if(filtro.getCategoriaId() != null) {
            query.setParameter("idCategoria", filtro.getCategoriaId());
        }
        if(filtro.getEstadoId()!= null) {
            query.setParameter("idEstado", filtro.getEstadoId());
        }

        if(filtro.getCodigoInventario()!= null) {
            query.setParameter("codInventario", filtro.getCodigoInventario());
        }/**
        if(filtro.getNumeroActa() != null) {
            query.setParameter("numActa", filtro.getNumeroActa());
        }**/
        if(filtro.getActivos() != null) {
            query.setParameter("activo", filtro.getActivos());
        }
        
        if(filtro.getCodigoDescargo() != null) {
            query.setParameter("codigoDescargo", filtro.getCodigoDescargo());
        }
        
        if(filtro.getFechaSolicitudDesde() != null) {
            query.setParameter("fechaSolicitudDesde", filtro.getFechaSolicitudDesde());
        }
        if(filtro.getFechaSolicitudHasta() != null) {
            query.setParameter("fechaSolicitudHasta", filtro.getFechaSolicitudHasta());
        }
        if(filtro.getFechaDescargoDesde() != null) {
            query.setParameter("fechaDescargoDesde", filtro.getFechaDescargoDesde());
        }
        if(filtro.getFechaDescargoHasta() != null) {
            query.setParameter("fechaDescargoHasta", filtro.getFechaDescargoHasta());
        }
        if(filtro.getMaxResults() != null) {
            query.setParameter("limite", filtro.getMaxResults());
        }
        if(filtro.getFirst() != null) {
            query.setParameter("inicio", filtro.getFirst());
        }
        return query;
    }
    
    private Query setParametros(FiltroDescargosDetalle filtro, Query query) {
        if(filtro.getTipoUnidad() != null) {
            if(TipoUnidad.UNIDAD_ADMINISTRATIVA.equals(filtro.getTipoUnidad())) {
                query.setParameter("tipounidad", "UA");
                if(filtro.getUnidadActivoFijoId() != null) {
                    query.setParameter("uafid", filtro.getUnidadActivoFijoId());
                }
            } else if(TipoUnidad.CENTRO_ESCOLAR.equals(filtro.getTipoUnidad())) {
                query.setParameter("tipounidad", "CE");
                if(filtro.getDepartamentoId() != null) {
                    query.setParameter("depid", filtro.getDepartamentoId());
                }
            }
        } else if(filtro.getDepartamentoId() != null) {
            query.setParameter("depid", filtro.getDepartamentoId() );
        }
        if(filtro.getBienId() != null) {
            query.setParameter("id", filtro.getBienId() );
        }
        if(filtro.getUnidadAdministrativaId() != null) {
            query.setParameter("idunidad", filtro.getUnidadAdministrativaId());
        }
        if(filtro.getDescargoId() != null) {
            query.setParameter("descargoId", filtro.getDescargoId());
        }
        if(filtro.getClasificacionId() != null) {
            query.setParameter("idClasificacion", filtro.getClasificacionId());
        }

        if(filtro.getCategoriaId() != null) {
            query.setParameter("idCategoria", filtro.getCategoriaId());
        }
        if(filtro.getEstadoId()!= null) {
            query.setParameter("idEstado", filtro.getEstadoId());
        }

        if(filtro.getCodigoInventario()!= null) {
            query.setParameter("codInventario", filtro.getCodigoInventario());
        }/**
        if(filtro.getNumeroActa() != null) {
            query.setParameter("numActa", filtro.getNumeroActa());
        }**/

        if(filtro.getActivos() != null) {
            query.setParameter("activo", filtro.getActivos());
        }

        if(filtro.getCodigoDescargo() != null) {
            query.setParameter("codigoDescargo", filtro.getCodigoDescargo());
        }
        
        if(filtro.getFechaSolicitudDesde() != null) {
            query.setParameter("fechaSolicitudDesde", filtro.getFechaSolicitudDesde());
        }
        if(filtro.getFechaSolicitudHasta() != null) {
            query.setParameter("fechaSolicitudHasta", filtro.getFechaSolicitudHasta());
        }
        
        if(filtro.getFechaDescargoDesde() != null) {
            query.setParameter("fechaDescargoDesde", filtro.getFechaDescargoDesde());
        }
        if(filtro.getFechaDescargoHasta() != null) {
            query.setParameter("fechaDescargoHasta", filtro.getFechaDescargoHasta());
        }
        if(filtro.getMaxResults() != null) {
            query.setParameter("limite", filtro.getMaxResults());
        }
        if(filtro.getFirst() != null) {
            query.setParameter("inicio", filtro.getFirst());
        }
        return query;
    }
    
    /**
     * Elimina el objeto con la id indicada
     *
     * @param id Long
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public void eliminar(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgAfDescargoDetalle> codDAO = new CodigueraDAO<>(em, SgAfDescargoDetalle.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }
    
    /**
     * Elimina el objeto indicado
     * @param det objeto a eliminar
     * @throws GeneralException 
     */
    @Interceptors(AuditInterceptor.class)
    public void eliminar(SgAfDescargoDetalle det) throws GeneralException {
        if (det != null) {
            try {
                CodigueraDAO<SgAfDescargoDetalle> codDAO = new CodigueraDAO<>(em, SgAfDescargoDetalle.class);
                codDAO.eliminar(det, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }
    
    /**
     * Elimina el o los bienes de un detalle de descargo
     * @param idBien 
     */
    public void eliminarPorIdBien(Long idBien) {
        if (idBien != null) {
            try {
                FiltroDescargosDetalle filtroDescargo = new FiltroDescargosDetalle();
                filtroDescargo.setBienId(idBien);
                List<SgAfDescargoDetalle> listaDescargo = obtenerPorFiltro(filtroDescargo);//Obtendrá como maximo un registro
                if(listaDescargo != null && !listaDescargo.isEmpty()) {
                    for(SgAfDescargoDetalle desDetalle : listaDescargo) {
                       eliminar(desDetalle);
                    }
                }
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }
}