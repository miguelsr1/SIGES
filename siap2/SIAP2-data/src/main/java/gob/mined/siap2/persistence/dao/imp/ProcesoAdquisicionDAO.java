/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.persistence.dao.imp;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.PAC;
import gob.mined.siap2.entities.data.impl.PACGrupo;
import gob.mined.siap2.entities.data.impl.POInsumos;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicion;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicionInsumo;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicionItem;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicionItemProvOferta;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicionLote;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicionProveedor;
import gob.mined.siap2.entities.data.impl.Proveedor;
import gob.mined.siap2.entities.enums.EstadoItem;
import gob.mined.siap2.entities.enums.EstadoPAC;
import gob.mined.siap2.persistence.dao.imp.eclipselink.EclipselinkJpaDAOImp;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Query;

/**
 * Esta clase incluye los métodos de acceso a datos asociados a un proceso de adquisición.
 * @author Sofis Solutions
 */
@JPADAO
public class ProcesoAdquisicionDAO extends EclipselinkJpaDAOImp<ProcesoAdquisicion, Integer> {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);
    /**
     * Este método permite obtener PAC con Insumos de un proceso de adquisición según distintos criterios.
     * @param rubro rubro del insumo
     * @param codigoIns código del insumo
     * @param nombreIns subcadena en el nombre del insumo
     * @param unidadTecId unidad técnica
     * @param IdPac id del PAC
     * @param nombrePac subcadena en el nombre del PAC
     * @param idPacGrupo id del grupo del PAC
     * @param nombreGrupo subcadena en el nombre del PAC
     * @return 
     */
    public List<PAC> obtenerPacsConInsumosParaProcesosDeAdquisicion(Integer rubro, String codigoIns, String nombreIns, Integer unidadTecId, Integer IdPac, String nombrePac, Integer idPacGrupo, String nombreGrupo) {

        String sql = "Select DISTINCT pg.pac "
                + "from PACGrupo pg,  pg.insumos pin "
                + "where pg.pac.estado =:estadoCerrado ";
        if (rubro != null) {

            sql += "and pin.insumo.objetoEspecificoGasto.rubro=:rubro ";
        }
        if (nombreIns != null) {
            sql += "and pin.insumo.nombre LIKE :nombreins ";
        }

        if (codigoIns != null) {
            sql += "and pin.insumo.codigo LIKE :codigoins ";
        }

        if (unidadTecId != null) {
            sql += "and pin.unidadTecnica.id =:unidadId ";
        }

        if (IdPac != null) {
            sql += "and pg.pac.id =:idpac ";
        }
        if (nombrePac != null) {
            sql += "and pg.pac.nombre LIKE :nombrepac ";

        }
        if (idPacGrupo != null) {
            sql += "and pg.id =:idgrupo ";
        }
        if (nombreGrupo != null) {
            sql += "and pg.nombre LIKE :nombreGrupo ";
        }

        sql += "and pin.procesoInsumo IS NULL order by pg.pac.id";

        Query q = entityManager.createQuery(sql);

        if (rubro != null) {

            q.setParameter("rubro", rubro);
        }
        if (nombreIns != null) {
            q.setParameter("nombreins", "%" + nombreIns + "%");
        }

        if (codigoIns != null) {
            q.setParameter("codigoins", "%" + codigoIns + "%");
        }

        if (unidadTecId != null) {
            q.setParameter("unidadId", unidadTecId);
        }
        if (IdPac != null) {
            q.setParameter("idpac", IdPac);
        }
        if (nombrePac != null) {
            q.setParameter("nombrepac", "%" + nombrePac + "%");

        }
        if (idPacGrupo != null) {
            q.setParameter("idgrupo", idPacGrupo);
        }
        if (nombreGrupo != null) {
            q.setParameter("nombreGrupo", "%" + nombreGrupo + "%");
        }

        q.setParameter("estadoCerrado", EstadoPAC.CERRADO);
        return (List<PAC>) q.getResultList();
    }

    /**
     * Este método envuelve la lista de grupos de PAC según distintos criterios.
     * @param pacId id del PAC
     * @param rubro rubro del insumo
     * @param codigoIns código del insumo
     * @param nombreIns subcadena en el nombre del insumo
     * @param unidadTecId id de la unidad técnica
     * @param idPacGrupo id del grupo
     * @param nombreGrupo subcadena en el nombre del grupo.
     * @return 
     */
    public List<PACGrupo> obtenerPacsGruposConInsumosParaProcesosDeAdquisicionPorPacId(Integer pacId, Integer rubro, String codigoIns, String nombreIns, Integer unidadTecId, Integer idPacGrupo, String nombreGrupo) {

        String sql = "Select DISTINCT pg "
                + "from PACGrupo pg,  pg.insumos pin "
                + "where pg.pac.id=:pacid ";

        if (rubro != null) {

            sql += "and pin.insumo.objetoEspecificoGasto.rubro=:rubro ";
        }
        if (nombreIns != null) {
            sql += "and pin.insumo.nombre LIKE :nombreins ";
        }

        if (codigoIns != null) {
            sql += "and pin.insumo.codigo LIKE :codigoins ";
        }

        if (unidadTecId != null) {
            sql += "and pin.unidadTecnica.id =:unidadId ";
        }

        if (idPacGrupo != null) {
            sql += "and pg.id =:idgrupo ";
        }

        if (nombreGrupo != null) {
            sql += "and pg.nombre LIKE :nombreGrupo ";
        }

        sql += "and pin.procesoInsumo IS NULL order by pg.pac.id";

        Query q = entityManager.createQuery(sql);

        if (rubro != null) {

            q.setParameter("rubro", rubro);
        }
        if (nombreIns != null) {
            q.setParameter("nombreins", "%" + nombreIns + "%");
        }

        if (codigoIns != null) {
            q.setParameter("codigoins", "%" + codigoIns + "%");
        }

        if (unidadTecId != null) {
            q.setParameter("unidadId", unidadTecId);
        }
        if (idPacGrupo != null) {
            q.setParameter("idgrupo", idPacGrupo);
        }
        if (nombreGrupo != null) {
            q.setParameter("nombreGrupo", "%" + nombreGrupo + "%");
        }

        q.setParameter("pacid", pacId);

        return (List<PACGrupo>) q.getResultList();
    }

    /**
     * Este método devuelve la lista de insumos en un POA
     * @param pacGrupoId id del grupo
     * @param rubro rubro de los insumos
     * @param codigoIns código del insumo
     * @param nombreIns subcadena en el nombre del insumo
     * @param unidadTecId id de la unidad técnica
     * @return lista de insumo en un POA
     */
    public List<POInsumos> obtenerPoInsumosParaProcesosDeAdquisicionPorPacGrupoId(Integer pacGrupoId, Integer rubro, String codigoIns, String nombreIns, Integer unidadTecId) {

        String sql = "Select pin "
                + "from PACGrupo pg,  pg.insumos pin "
                + "where pg.id=:pacgrupoid ";

        if (rubro != null) {

            sql += "and pin.insumo.objetoEspecificoGasto.rubro=:rubro ";
        }
        if (nombreIns != null) {
            sql += "and pin.insumo.nombre LIKE :nombreins ";
        }

        if (codigoIns != null) {
            sql += "and pin.insumo.codigo LIKE :codigoins ";
        }

        if (unidadTecId != null) {
            sql += "and pin.unidadTecnica.id =:unidadId ";
        }

        sql += "and pin.procesoInsumo IS NULL order by pg.pac.id";

        Query q = entityManager.createQuery(sql);

        if (rubro != null) {

            q.setParameter("rubro", rubro);
        }
        if (nombreIns != null) {
            q.setParameter("nombreins", "%" + nombreIns + "%");
        }

        if (codigoIns != null) {
            q.setParameter("codigoins", "%" + codigoIns + "%");
        }

        if (unidadTecId != null) {
            q.setParameter("unidadId", unidadTecId);
        }

        q.setParameter("pacgrupoid", pacGrupoId);

        return (List<POInsumos>) q.getResultList();
    }

    
    /**
     * Este método devuelve la lista de grupos de PAC de un determinado proceso de adquisición
     * @param proId id del proceso.
     * @return lista de grupos del proceso.
     */
    public List<PACGrupo> obtenerPacGrupoPorInsumosDelProceso(Integer proId) {
        Query q = entityManager.createQuery("Select pg "
                + "from PACGrupo pg,  pg.insumos pin "
                + "where pin.id IN(SELECT insumosProceso.poInsumo.id "
                + "FROM ProcesoAdquisicion pa, pa.insumos insumosProceso "
                + "WHERE pa.id=:proid ) order by pg.pac.id");

        q.setParameter("proid", proId);

        return (List<PACGrupo>) q.getResultList();

    }

    /**
     * Este método devuelve la lista de proveedores asociados a un proceso.
     * @param proveedorId id del proveedor
     * @param nombreProv subcadena en el nombre del proveedor
     * @param proId id del proceso
     * @return lista de proveedores que cumplen el criterio indicado
     */
    public List<Proveedor> obtenerProveedoresNoAsociadosAlProcesoFiltro(Integer proveedorId, String nombreProv, Integer proId) {
        String sql = "Select p "
                + "From Proveedor p "
                + "Where p.id NOT IN (SELECT pro.proveedor.id "
                + "From ProcesoAdquisicionProveedor pro "
                + "Where pro.procesoAdquisicion.id=:proid) "
                + "AND p.proveedorMined = TRUE ";
        if (proveedorId != null) {
            sql += "and p.idOferente=:proveedorid ";
        }
        if (nombreProv != null) {
            sql += "and p.nombreComercial LIKE :nombreProv ";
        }

        sql += "order by p.nombreComercial";

        Query q = entityManager.createQuery(sql);

        if (proveedorId != null) {

            q.setParameter("proveedorid", proveedorId);
        }
        if (nombreProv != null) {
            q.setParameter("nombreProv", nombreProv);

        }
        q.setParameter("proid", proId);

        return (List<Proveedor>) q.getResultList();
    }

    /**
     * Este método devuelve la lista de proveedores no asociados a un proceso
     * @param proId id del proceso
     * @return lista de proveedores.
     */
    public List<Proveedor> obtenerProveedoresNoAsociadosAlProceso(Integer proId) {
        String sql = "Select p "
                + "From Proveedor p "
                + "Where p.id NOT IN (SELECT pro.proveedor.id "
                + "From ProcesoAdquisicionProveedor pro "
                + "Where pro.procesoAdquisicion.id=:proid) "
                + "AND p.proveedorMined = TRUE ";

        sql += "order by p.nombreComercial";

        Query q = entityManager.createQuery(sql);

        q.setParameter("proid", proId);

        return (List<Proveedor>) q.getResultList();
    }

    /**
     * Este método devuelve la lista de proveedores asociados a un proceso de adquisición.
     * @param proId id del proceso
     * @return lista de proveedores del proceso.
     */
    public List<Proveedor> obtenerProveedoresAsociadosAlProceso(Integer proId) {
        String sql = "Select p "
                + "From Proveedor p "
                + "Where p.id IN (SELECT pro.proveedor.id "
                + "From ProcesoAdquisicionProveedor pro "
                + "Where pro.procesoAdquisicion.id=:proid) "
                + "AND p.proveedorMined = TRUE ";

        sql += "order by p.nombreComercial";

        Query q = entityManager.createQuery(sql);

        q.setParameter("proid", proId);

        return (List<Proveedor>) q.getResultList();
    }

    /**
     * Este método devuelve los ítems de un proceso de adquisición
     * @param proId id del proceso de adquisición.
     * @return lista de ítems de un proceso de adquisición.
     */
    public List<ProcesoAdquisicionItem> obtenerItemsDelProceso(Integer proId) {

        Query q = entityManager.createQuery("Select item "
                + "From ProcesoAdquisicionItem item "
                + "Where item.id IN (SELECT proItem.id "
                + "From ProcesoAdquisicionItem proItem "
                + "Where proItem.procesoAdquisicion.id=:proid) "
                + "or item.id IN (SELECT it.id "
                + "FROM ProcesoAdquisicionLote lot, lot.items it "
                + "WHERE lot.procesoAdquisicion.id=:proid) order by item.nombre");

        q.setParameter("proid", proId);

        return (List<ProcesoAdquisicionItem>) q.getResultList();
    }

    /**
     * Este método devuelve los ítems de un proceso de adquisición para los que no hay contrato u orden de compra asociado.
     * @param proId id del proceso de adquisición
     * @return lista de ítems.
     */
    public List<ProcesoAdquisicionItem> obtenerItemsDelProcesoSinContrato(Integer proId) {

        Query q = entityManager.createQuery("Select item "
                + "From ProcesoAdquisicionItem item "
                + "Where (item.id IN (SELECT proItem.id "
                + "From ProcesoAdquisicionItem proItem "
                + "Where proItem.procesoAdquisicion.id=:proid) "
                + "or item.id IN (SELECT it.id "
                + "FROM ProcesoAdquisicionLote lot, lot.items it "
                + "WHERE lot.procesoAdquisicion.id=:proid)) "
                + "and item.contrato IS NULL order by item.nombre");

        q.setParameter("proid", proId);

        return (List<ProcesoAdquisicionItem>) q.getResultList();
    }

    /**
     * Este método devuelve una lista con los ítems de un proceso de adquisición que están adjudicados.
     * @param proId id del proceso de adquisición.
     * @return lista de ítems.
     */
    public List<ProcesoAdquisicionItem> obtenerItemsDelProcesoAdjudicados(Integer proId) {

        Query q = entityManager.createQuery("Select item "
                + "From ProcesoAdquisicionItem item "
                + "Where item.estado=:estadoAdjudicado and "
                + "item.id IN (SELECT proItem.id "
                + "From ProcesoAdquisicionItem proItem "
                + "Where proItem.procesoAdquisicion.id=:proid) "
                + "or item.id IN (SELECT it.id "
                + "FROM ProcesoAdquisicionLote lot, lot.items it "
                + "WHERE lot.procesoAdquisicion.id=:proid) order by item.nombre");

        q.setParameter("proid", proId);
        q.setParameter("estadoAdjudicado", EstadoItem.ADJUDICADO);
        return (List<ProcesoAdquisicionItem>) q.getResultList();
    }

    /**
     * Este método devuelve los lotes con ítems adjudicados en un proceso de adquisición.
     * @param proId id del proceso
     * @return lista de lotes adjudicados.
     */
    public List<ProcesoAdquisicionLote> obtenerLotesConItemsAdjudicados(Integer proId) {
        return (List<ProcesoAdquisicionLote>) entityManager.createQuery(
                "SELECT DISTINCT (lot) "
                + " FROM ProcesoAdquisicionLote lot "
                + " WHERE lot.procesoAdquisicion.id=:proid  "
                + " AND  EXISTS "
                + "(SELECT it FROM lot.items it WHERE  item.estado=:estadoAdjudicado )"
                + " ORDER BY lot.id")
                .setParameter("proid", proId)
                .setParameter("estadoAdjudicado", EstadoItem.ADJUDICADO)
                .getResultList();
    }

    /**
     * Este método devuelve los proveedores adjudicados en un proceso de adquisición.
     * @param proId id del proceso
     * @return lista de proveedores adjudicados.
     */
    public List<ProcesoAdquisicionProveedor> obtenerProveedoresGanadoresItemProceso(Integer proId) {

        Query q = entityManager.createQuery("Select DISTINCT oferta.procesoAdquisicionProveedor "
                + "From ProcesoAdquisicionItem item, item.ofertasProvedor oferta"
                + "Where item.id IN (SELECT proItem.id "
                + "From ProcesoAdquisicionItem proItem "
                + "Where proItem.procesoAdquisicion.id=:proid) "
                + "or item.id IN (SELECT it.id "
                + "FROM ProcesoAdquisicionLote lot, lot.items it "
                + "WHERE lot.procesoAdquisicion.id=:proid) "
                + "and oferta.id=item.proveedorOfertaAdjudicadaId order by item.nombre");

        q.setParameter("proid", proId);

        return (List<ProcesoAdquisicionProveedor>) q.getResultList();
    }

    /**
     * Este método devuelve un ítem en un proceso de adquisición
     * @param idInsumoProdAdq id del insumo
     * @return 
     */
    public ProcesoAdquisicionItem obtenerItemDelInsumo(Integer idInsumoProdAdq) {
 
        Query q = entityManager.createQuery("select item "
                + "from RelacionProAdqItemInsumo relacion, ProcesoAdquisicionItem item "
                + "where relacion.item = item and "
                + "relacion.insumo.id =:idInsumo");
        q.setParameter("idInsumo", idInsumoProdAdq);

        List<ProcesoAdquisicionItem> l = q.getResultList();
        if (l.isEmpty()) {
            return null;
        }
        return l.get(0);

    }

    /**
     * Este método devuelve las ofertas para un ítem específico.
     * @param itemId id del ítem
     * @return lista de ofertas para el ítem indicado
     */
    public List<ProcesoAdquisicionItemProvOferta> obtenerOfertasPorItemId(Integer itemId) {
        Query q = entityManager.createQuery("Select oferta "
                + "From ProcesoAdquisicionItemProvOferta oferta "
                + "Where oferta.procesoAdquisicionItem.id=:itemId ");

        q.setParameter("itemId", itemId);

        return (List<ProcesoAdquisicionItemProvOferta>) q.getResultList();

    }

    /**
     * Este método devuelve la cantidad de ítems de un proceso
     * @param proId id del proceso
     * @return cantidad de ítems del proceso.
     */
    public Long obtenerCantidadItemsDelProceso(Integer proId) {

        Query q = entityManager.createQuery("Select count(*) "
                + "From ProcesoAdquisicionItem item "
                + "Where item.id IN (SELECT proItem.id "
                + "From ProcesoAdquisicionItem proItem "
                + "Where proItem.procesoAdquisicion.id=:proid) "
                + "or item.id IN (SELECT it.id "
                + "FROM ProcesoAdquisicionLote lot, lot.items it "
                + "WHERE lot.procesoAdquisicion.id=:proid) order by item.nombre");

        q.setParameter("proid", proId);

        return (Long) q.getSingleResult();
    }

    /**
     * Este método devuelve el último número de ítem en el proceso.
     * @param proId id del proceso
     * @return número del último ítem.
     */
    public Integer obtenerUltimoNroItemsDelProceso(Integer proId) {
        try {
            Query q = entityManager.createQuery("Select item.nroItem "
                    + "From ProcesoAdquisicionItem item "
                    + "Where item.id IN (SELECT proItem.id "
                    + "From ProcesoAdquisicionItem proItem "
                    + "Where proItem.procesoAdquisicion.id=:proid) "
                    + "or item.id IN (SELECT it.id "
                    + "FROM ProcesoAdquisicionLote lot, lot.items it "
                    + "WHERE lot.procesoAdquisicion.id=:proid) order by item.nroItem desc");

            q.setParameter("proid", proId);
            List<Integer> nrosItem = (List<Integer>) q.getResultList();
            if (nrosItem.isEmpty()) {
                return 0;
            } else {
                return nrosItem.get(0);
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return 0;
        }

    }

    /**
     * Este método devuelve los insumos en un determinado ítem.
     * @param idItem id del ítem
     * @return lista de insumos
     */
    public List<ProcesoAdquisicionInsumo> obtenerInsumosDelItem(Integer idItem) {
        return entityManager.createQuery("select insumo "
                + "from RelacionProAdqItemInsumo relacion, ProcesoAdquisicionInsumo insumo "
                + "where relacion.insumo = insumo and "
                + "relacion.item.id =:idItem")
                .setParameter("idItem", idItem)
                .getResultList();
    }

    /**
     * Este método determina si existe un proceso que satisface un determinado criterio.
     * @param idProAdq id del proceso
     * @param nombre subcadena en el nombre
     * @param IdAnioFiscal id del año fiscal.
     * @return true = existe 
     */
    public boolean existeProcesoByNombreAnio(Integer idProAdq, String nombre, Integer IdAnioFiscal) {
        List result = entityManager.createQuery("SELECT proAdq "
                + " FROM ProcesoAdquisicion proAdq"
                + " WHERE proAdq.nombre =:nombre"
                + " AND proAdq.anio.id =:IdAnioFiscal"
                + " AND proAdq.id !=:idProAdq")
                .setParameter("nombre", nombre)
                .setParameter("IdAnioFiscal", IdAnioFiscal)
                .setParameter("idProAdq", idProAdq)
                .getResultList();
        return !result.isEmpty();
    }

    /**
     * Este método devuelve los insumos de un proceso.
     * @param idProceso id del proceso
     * @return  lista de insumos del proceso.
     */
    public List<ProcesoAdquisicionInsumo> obtenerInsumos(Integer idProceso) {
        return entityManager.createQuery("SELECT insumo "
                + "FROM ProcesoAdquisicionInsumo insumo "
                + "WHERE insumo.procesoAdquisicion.id = :idProceso")
                .setParameter("idProceso", idProceso)
                .getResultList();
    }

    /**
     * Esta consulta verifica que para un proceso distinto (al pasado por parámetro),
     * el insumo (pasado por parámetro) esta también asociado. 
     * En caso que el insumo esté asociado a un solo proceso debería devolver la lista vacía
     * @param idInsumoDelProceso
     * @param idProceso
     * @return 
     */
    public boolean insumoEstaEnOtroProceso(Integer idInsumoDelProceso, Integer idProceso) {
        List result = entityManager.createQuery("SELECT insumo "
                + " FROM ProcesoAdquisicionInsumo insumo"
                + " WHERE insumo.id =:idInsumoDelProceso"
                + " AND insumo.procesoAdquisicion.id !=:idProceso")
                .setParameter("idInsumoDelProceso", idInsumoDelProceso)
                .setParameter("idProceso", idProceso)
                .getResultList();
        return !result.isEmpty();
    }
    
    /**
     * Este método determina si un insumo está en un proceso distinto al proceso indicado
     * @param idPoInsumoDelProceso id del proceso
     * @param idInsumo id del insumo.
     * @return 
     */
    public boolean poInsumoEstaEnOtroInsumoProceso(Integer idPoInsumoDelProceso, Integer idInsumo) {
        List result = entityManager.createQuery("SELECT poInsumo "
                + " FROM POInsumos poInsumo"
                + " WHERE poInsumo.id =:idPoInsumoDelProceso"
                + " AND poInsumo.procesoInsumo != null"
                + " AND poInsumo.procesoInsumo.id != :idInsumo")
                .setParameter("idPoInsumoDelProceso", idPoInsumoDelProceso)
                .setParameter("idInsumo", idInsumo)
                .getResultList();
        return !result.isEmpty();
    }
    
    /**
     * Este método devuelve una lista con los últimos 5 precios adjudicados del insumo.
     * @param idInsumo
     * @return 
     */
    public List<BigDecimal> obtenerUltimos5PreciosInsumoAdjudicado(Integer idInsumo){
        List<BigDecimal> result = entityManager.createQuery("SELECT rel.montoUnitAdjudicado "
                +"from RelacionProAdqItemInsumo rel "
                +"where rel.insumo.insumo.id = :idInsumo "
                +"order by rel.fechaContratacion desc")
                .setParameter("idInsumo", idInsumo)
                .getResultList();
        return result;
    }
    
    /**
     * Esta consulta verifica que para un proceso distinto (al pasado por parámetro),
     * el insumo (pasado por parámetro) esta también asociado. 
     * En caso que el insumo esté asociado a un solo proceso debería devolver la lista vacía
     * @param idInsumoDelProceso
     * @param idProceso
     * @return 
     */
    public Boolean insumoEstaEnProceso(Integer idPoInsumo) {
        Query q = entityManager.createQuery("SELECT procesoInsumo "
                + "FROM ProcesoAdquisicionInsumo procesoInsumo "
                + "WHERE procesoInsumo.poInsumo.id = :idPoInsumo and procesoInsumo.poInsumo.habilitado=TRUE")
                .setParameter("idPoInsumo", idPoInsumo);
                
        List<ProcesoAdquisicionInsumo> l = q.getResultList();
        return !l.isEmpty();
    }

    /**
     * Devuelve la lista de los POInsumos que no están agregados en un proceso de adquisición, están en un PAC cerrado
     * y tienen asociado un insumo que está contenido en la lista pasada por parámetro
     * @param listaIdInsumosPermitidos
     * @return 
     */
    public List<POInsumos> getPoInsumosDisponiblesParaModificativasContratoOC(Set<Integer> listaIdInsumosPermitidos, Set<Integer> listaIdPoInsumosNoPermitidos) {
        
        String sql = "Select pin "
                + "from POInsumos pin "
                + "where pin.pacGrupo.pac.estado =:estadoCerrado "
                + "and pin.procesoInsumo IS NULL "
                + "and pin.modificativa IS NULL ";
                if(listaIdInsumosPermitidos!=null && !listaIdInsumosPermitidos.isEmpty()){
                    sql += "and pin.insumo.id IN :listaIdInsumosPermitidos ";
                }
                if(listaIdPoInsumosNoPermitidos!=null && !listaIdPoInsumosNoPermitidos.isEmpty()){
                    sql += "and pin.id NOT IN :listaIdPoInsumosNoPermitidos ";
                }
                sql += "order by pin.id";

        Query q = entityManager.createQuery(sql);        

        q.setParameter("estadoCerrado", EstadoPAC.CERRADO);
        if(listaIdInsumosPermitidos!=null && !listaIdInsumosPermitidos.isEmpty()){
            q.setParameter("listaIdInsumosPermitidos", listaIdInsumosPermitidos);
        }
        if(listaIdPoInsumosNoPermitidos!=null && !listaIdPoInsumosNoPermitidos.isEmpty()){
            q.setParameter("listaIdPoInsumosNoPermitidos", listaIdPoInsumosNoPermitidos);         
        }
        return (List<POInsumos>) q.getResultList();
    }


}
