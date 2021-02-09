/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.opentracing.Traced;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.StringType;
import sv.gob.mined.siges.dto.DepreciacionContable;
import sv.gob.mined.siges.enumerados.TipoUnidad;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroBienesDepreciables;

@Stateless
@Traced
public class DepreciacionSubcuentasContablesBean {

    private static final Logger LOGGER = Logger.getLogger(DepreciacionSubcuentasContablesBean.class.getName());
    
    @Inject
    @ConfigProperty(name = "valor-activo-fijo-limite", defaultValue = "600")
    private BigDecimal valorActivoFijoLimite;
    
    @PersistenceContext
    private EntityManager em;

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroBienesDepreciables
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroBienesDepreciables filtro) throws GeneralException {
        try {
            return Long.parseLong(String.valueOf(obtenerCategoriasPorFiltro(filtro).size()));
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroBienesDepreciables
     * @return Lista de <SgAfDepreciacionSubcuentasContables>
     * @throws GeneralException
     */
    public List<DepreciacionContable> obtenerCategoriasPorFiltro(FiltroBienesDepreciables filtro) throws GeneralException {
        try {
            
            Session session = em.unwrap(Session.class);
            
            String groupbyFinal=" group by datos.cat_nombre";
           
            String groupBy = " GROUP BY cat_1.cab_pk";
            
            String queryFrom2 = "";
            String queryFrom3 = "";
            String queryFrom = "select datos.cat_nombre as catNombre, " +
                                    "Sum(datos.total_valor) as totalValor," +
                                    "Sum(datos.total_valor_residual) as totalValorResidual, " +
                                    "Sum(datos.depreciacion_acumulada) as depreciacionAcumulada, " +
                                    "(Sum(datos.total_valor) - Sum(datos.depreciacion_acumulada)) as pendienteDepreciar " +
                                    " from (SELECT cat.cab_pk AS cat_pk" +
                                    ",cat.cab_nombre AS cat_nombre" +
                                    ",coalesce(datosbienes.valor ,0) AS total_valor" +
                                    ",coalesce(datosbienes.valor_residual ,0) AS total_valor_residual" +
                                    ",coalesce(datosbienes.depreciacion_acumulada ,0) as depreciacion_acumulada " ;
            
            queryFrom2 =  " FROM catalogo.sg_af_categoria_bienes cat" +
                 " JOIN ( SELECT cat_1.cab_pk" +
                 ",sum(bien.bde_valor_adquisicion) AS valor" +
                 ",sum(bien.bde_valor_residual) AS valor_residual" +
                 ",sum(dep.dep_monto_depreciacion) AS depreciacion_acumulada " ;

            queryFrom3 =    " FROM activo_fijo.sg_af_bienes_depreciables bien " +
                 " LEFT JOIN catalogo.sg_af_tipo_bienes tbi ON tbi.tbi_pk = bien.bde_tipo_bien_fk" +
                 " LEFT JOIN catalogo.sg_af_categoria_bienes cat_1 ON cat_1.cab_pk = tbi.tbi_categoria_bienes_fk" +
                 " LEFT JOIN activo_fijo.sg_af_depreciaciones dep ON bien.bde_pk = dep.dep_bienes_depreciables_fk " +
                 " LEFT JOIN catalogo.sg_af_unidades_administrativas uad on (bien.bde_unidad_administrativa_fk = uad.uad_pk) " +
                 " LEFT JOIN catalogo.sg_af_unidades_activo_fijo uaf on (uad.uad_unidad_activo_fijo_fk = uaf.uaf_pk) " +
                 " LEFT JOIN centros_educativos.sg_sedes sed on (sed.sed_pk = bien.bde_sede_fk) " +
                 " LEFT JOIN centros_educativos.sg_direcciones dir on (dir.dir_pk = sed.sed_direccion_fk) " +
                 " LEFT JOIN catalogo.sg_departamentos depa on (depa.dep_pk = dir.dir_departamento) " ;
            
            
            String where = "";
            Boolean tipoUnidad = false;
            if(filtro.getTipoUnidad() != null) {
                queryFrom += ",datosbienes.tipo_unidad" ;
                queryFrom2 +=  ",CASE WHEN bien.bde_unidad_administrativa_fk IS NOT NULL THEN 'UA' WHEN bien.bde_sede_fk IS NOT NULL THEN 'CE' END AS tipo_unidad";
                where += " datos.tipo_unidad = :tipounidad";
                tipoUnidad = true;
                if(TipoUnidad.UNIDAD_ADMINISTRATIVA.equals(filtro.getTipoUnidad()) && filtro.getUnidadActivoFijoId() != null) {
                    where += " AND datos.uaf_id = :uafid ";
                    groupBy +=",uaf.uaf_pk";
                    queryFrom += ",datosbienes.uaf_id" ;
                    queryFrom2 += ",uaf.uaf_pk uaf_id" ;
                } else if(TipoUnidad.CENTRO_ESCOLAR.equals(filtro.getTipoUnidad()) && filtro.getDepartamentoId() != null) {
                    where += " AND datos.depa_id = :depid ";
                    groupBy +=",depa.dep_pk,uaf.uaf_departamento_fk";
                    queryFrom += ",datosbienes.depa_id" ;
                    queryFrom2 += ",coalesce(uaf.uaf_departamento_fk,depa.dep_pk) as depa_id" ;
                }
                
            } else if(filtro.getDepartamentoId() != null) {
                where += " datos.depa_id = :depid";
                groupBy +=",depa.dep_pk,uaf.uaf_departamento_fk";
                queryFrom += ",datosbienes.depa_id" ;
                queryFrom2 += ",coalesce(uaf.uaf_departamento_fk,depa.dep_pk) as depa_id" ;
            }
            
            if(filtro.getUnidadAdministrativaId() != null) {
                if(StringUtils.isNotBlank(where.trim()))
                    where += " AND ";
                where += " datos.id_unidad = :idunidad";
                queryFrom += ",datosbienes.id_unidad" ;
                tipoUnidad = true;
                queryFrom2 +=  ",CASE WHEN bien.bde_unidad_administrativa_fk IS NOT NULL THEN bien.bde_unidad_administrativa_fk WHEN bien.bde_sede_fk IS NOT NULL THEN bien.bde_sede_fk END AS id_unidad" ;
            }
            
            if(filtro.getClasificacionId() != null) {
                if(StringUtils.isNotBlank(where.trim()))
                    where += " AND ";
                where += " datos.clasificacion_Id = :idClasificacion";
                queryFrom += ",cat.cab_clasificacion_bienes_fk AS clasificacion_Id" ;
                
            }
            if(tipoUnidad) {
                groupBy +=",bien.bde_sede_fk,bien.bde_unidad_administrativa_fk";
            }
            if(filtro.getCategoriaId() != null) {
                if(StringUtils.isNotBlank(where.trim()))
                    where += " AND ";
                where += " datos.cat_pk = :idCategoria";
                
            }
            
            if(filtro.getFuenteId() != null) {
                if(StringUtils.isNotBlank(where.trim()))
                    where += " AND ";
                where += " datos.fuente_id = :fuenteId";
                groupBy +=",bien.bde_fuente_financiamiento_fk";
                queryFrom +=",datosbienes.fuente_id" ;
                queryFrom2 += ",bien.bde_fuente_financiamiento_fk as fuente_id" ;
            }
            
            if(filtro.getProyectoId() != null) {
                if(StringUtils.isNotBlank(where.trim()))
                    where += " AND ";
                where += " datos.proyecto_id = :proyectoId";
                groupBy +=",bien.bde_proyecto_fk";
                queryFrom +=",datosbienes.proyecto_id" ;
                queryFrom2 += ",bien.bde_proyecto_fk as proyecto_id" ;
            }
          
           if(filtro.getFechaAdquisicionDesde() != null || filtro.getFechaAdquisicionHasta() != null) {
               if(filtro.getFechaAdquisicionDesde() != null) {
                    if(StringUtils.isNotBlank(where.trim()))
                        where += " AND ";
                    where += " datos.fechaAdquisicion >= :fechaAdquisicionDesde";
                }
                if(filtro.getFechaAdquisicionHasta() != null) {
                    if(StringUtils.isNotBlank(where.trim()))
                        where += " AND ";
                    where += " datos.fechaAdquisicion <= :fechaAdquisicionHasta";
                }
                groupBy +=",bien.bde_fecha_adquisicion";
                queryFrom +=",datosbienes.fechaAdquisicion" ;
                queryFrom2 += ",bien.bde_fecha_adquisicion as fechaAdquisicion" ;
                
           }
            
           if(filtro.getValorAdquisicionDesde() != null || filtro.getValorAdquisicionHasta()!= null || filtro.getActivos() != null) {
               if(filtro.getValorAdquisicionDesde() != null) {
                    if(StringUtils.isNotBlank(where.trim()))
                        where += " AND ";
                    where += " datos.valorAdquisicion >= :valorAdquisicionDesde";
                }
                if(filtro.getValorAdquisicionHasta()!= null) {
                    if(StringUtils.isNotBlank(where.trim()))
                        where += " AND ";
                    where += " datos.valorAdquisicion <= :valorAdquisicionHasta";
                }
                
                if(filtro.getActivos() != null) {
                    if(StringUtils.isNotBlank(where.trim()))
                        where += " AND ";
                    if(!filtro.getActivos()) {//Menores de valor limite
                        where += " datos.valorAdquisicion < :valorAdquisicion";
                    } else {//Mayores o iguales a valor limite
                        where += " datos.valorAdquisicion >= :valorAdquisicion";
                    }

                }
                
                groupBy +=",bien.bde_valor_adquisicion";
                queryFrom +=",datosbienes.valorAdquisicion" ;
                queryFrom2 += ",bien.bde_valor_adquisicion as valorAdquisicion" ;
           }
        
            
            if(StringUtils.isNotBlank(where.trim())) {
                where = " WHERE " + where;
            }
            
            queryFrom += queryFrom2  + queryFrom3 + groupBy +") datosbienes ON datosbienes.cab_pk = cat.cab_pk ) datos " + where + groupbyFinal;
            if(filtro.getMaxResults() != null) {
                queryFrom = queryFrom + " limit :limite";
            }
            if(filtro.getFirst() != null) {
                queryFrom = queryFrom + " OFFSET :inicio";
            }
            
            LOGGER.info("Query: " + queryFrom);
            SQLQuery query = session.createSQLQuery(queryFrom);
            
            query.addScalar("catNombre", new StringType());
            query.addScalar("totalValor", new BigDecimalType());
            query.addScalar("totalValorResidual", new BigDecimalType());
            query.addScalar("depreciacionAcumulada", new BigDecimalType());
            query.addScalar("pendienteDepreciar", new BigDecimalType());   
            
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
            
            if(filtro.getUnidadAdministrativaId() != null) {
                query.setParameter("idunidad", filtro.getUnidadAdministrativaId());
            }
            
            if(filtro.getClasificacionId() != null) {
                query.setParameter("idClasificacion", filtro.getClasificacionId());
            }
            
            if(filtro.getCategoriaId() != null) {
                query.setParameter("idCategoria", filtro.getCategoriaId());
            }
            if(filtro.getFuenteId() != null) {
                query.setParameter("fuenteId", filtro.getFuenteId());
            }
            
            if(filtro.getProyectoId() != null) {
                query.setParameter("proyectoId", filtro.getProyectoId());
            }
            
            if(filtro.getFechaAdquisicionDesde() != null) {
                query.setParameter("fechaAdquisicionDesde", filtro.getFechaAdquisicionDesde());
            }
            if(filtro.getFechaAdquisicionHasta() != null) {
                query.setParameter("fechaAdquisicionHasta", filtro.getFechaAdquisicionHasta());
            }
            
            if(filtro.getValorAdquisicionDesde() != null) {
                 query.setParameter("valorAdquisicionDesde", filtro.getValorAdquisicionDesde());
            }
            if(filtro.getValorAdquisicionHasta()!= null) {
                query.setParameter("valorAdquisicionHasta", filtro.getValorAdquisicionHasta());
            }
            
            if(filtro.getActivos() != null) {
                query.setParameter("valorAdquisicion", valorActivoFijoLimite);
            }
            
            if(filtro.getMaxResults() != null) {
                query.setParameter("limite", filtro.getMaxResults());
            }
            if(filtro.getFirst() != null) {
                query.setParameter("inicio", filtro.getFirst());
            }
            
            query.setResultTransformer(Transformers.aliasToBean(DepreciacionContable.class));
            
            return query.list();
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
}