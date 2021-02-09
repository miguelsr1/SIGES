package gob.mined.siap2.persistence.dao.imp;


import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.entities.constantes.Constantes;
import gob.mined.siges.entities.data.impl.SgPresupuestoEscolarMovimiento;
import gob.mined.siges.entities.data.impl.SgSede;
import gob.mined.siap2.entities.data.impl.TopePresupuestal;
import gob.mined.siap2.entities.data.impl.TopePresupuestalGroup;
import gob.mined.siap2.entities.data.impl.TotalesMatriculas;
import gob.mined.siap2.entities.enums.EstadoTopePresupuestal;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.persistence.dao.imp.eclipselink.EclipselinkJpaDAOImp;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;


@JPADAO
public class TopePresupuestalDAO extends EclipselinkJpaDAOImp<TopePresupuestal, Integer> implements  Serializable{
    
    
    
    /**
     * Este método busca un registro de TopePresupuestal filtrado por ID
     * @param idCodigo
     * @return
     * @throws DAOGeneralException 
     */
    public TopePresupuestal getTopePresupuestalById(Integer idCodigo) throws DAOGeneralException {
        return (TopePresupuestal) entityManager
                .createQuery("select g from TopePresupuestal g where g.id = :id")
                .setParameter("id", idCodigo)
                .getSingleResult();
    }
    
    /**
     * Este método busca un registro de TopePresupuestal filtrado por ID Componente
     * @param id
     * @return
     * @throws DAOGeneralException 
     */
    public List<TopePresupuestal> getTopePresupuestalByGesPresEsId(Integer id) throws DAOGeneralException {
        return (List<TopePresupuestal>) entityManager
                .createQuery("select g from TopePresupuestal g where g.gesPresEs.id = :id")
                .setParameter("id", id)
                .getResultList();
    }
    
    /**
     * Este método busca un registro de TopePresupuestal filtrado por SubCuenta ID
     * @param id
     * @return
     * @throws DAOGeneralException 
     */
    public List<TopePresupuestal> getTopePresupuestalBySubcuentaId(Integer id) throws DAOGeneralException {
        return (List<TopePresupuestal>) entityManager
                .createQuery("select g from TopePresupuestal g where g.subCuenta.id = :id")
                .setParameter("id", id)
                .getResultList();
    }
    
    /**
     * Este método busca un registro de TopePresupuestal filtrado por AnioFiscal ID
     * @param id
     * @return
     * @throws DAOGeneralException 
     */
    public List<TopePresupuestal> getTopePresupuestalByAnioId(Integer id) throws DAOGeneralException {
        return (List<TopePresupuestal>) entityManager
                .createQuery("select g from TopePresupuestal g where g.anioFiscal.id = :id")
                .setParameter("id", id)
                .getResultList();
    }
    
    /**
     * Este método busca un registro de TopePresupuestal filtrado por AnioFiscal ANIO
     * @param anio
     * @return
     * @throws DAOGeneralException 
     */
    public List<TopePresupuestal> getTopePresupuestalByAnio(Integer anio) throws DAOGeneralException {
        return (List<TopePresupuestal>) entityManager
                .createQuery("select g from TopePresupuestal g where g.anioFiscal.anio = :anio")
                .setParameter("anio", anio)
                .getResultList();
    }
    
    /**
     * Este método busca todos los registros de TopePresupuestal 
     * @return
     * @throws DAOGeneralException 
     */
    public List<TopePresupuestal> getTopePresupuestal() throws DAOGeneralException {
        return (List<TopePresupuestal>) entityManager
                .createQuery("select g from TopePresupuestal g ")
                .getResultList();
    }
    
    /**
     * Este método agrupa los resultados de TopePresupuestal
     * @param idAnio
     * @param idCategoria
     * @param idComponente
     * @return 
     * @throws gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException 
     */
    public List<TopePresupuestalGroup> getTopePresupuestalAgrupado(Integer idAnio, Integer idCategoria, Integer idComponente) throws DAOGeneralException{
        try {
            List<TopePresupuestalGroup> listado = new ArrayList<TopePresupuestalGroup>();
            StringBuilder builder = new StringBuilder();
            builder.append(
                "SELECT cp.cu_codigo,cp.cu_nombre,sc.cu_codigo, sc.cu_nombre, sum(stp.tp_monto_formulacion), sum(stp.tp_monto_aprobado) "+
                "FROM " + Constantes.SCHEMA_SIAP2 +".ss_tope_presupestal stp "+
                "INNER JOIN "+ Constantes.SCHEMA_SIAP2 + ".ss_cuentas sc ON sc.cu_id = stp.tp_sub_cuenta "+
                "LEFT JOIN "+ Constantes.SCHEMA_SIAP2 + ".ss_cuentas cp ON sc.cu_cuenta_padre = cp.cu_id "+        
                "LEFT JOIN "+ Constantes.SCHEMA_SIAP2 + ".ss_ges_pres_es gpe ON gpe.ges_id = tp_componente "+
                "LEFT JOIN "+ Constantes.SCHEMA_SIAP2 +".ss_categoria_presupuesto_escolar cpe ON cpe.cpe_id = gpe.ges_categoria_componente "+
                "WHERE 1 = 1 ");
                if(idAnio != null && idAnio != 0)
                    builder.append("AND tp_anio_fiscal = ?1 ");
                if(idComponente != null && idComponente != 0)
                    builder.append("AND tp_componente = ?2 ");
                if(idCategoria != null && idCategoria != 0)
                    builder.append("AND cpe.cpe_id = ?3 ");
                builder.append(" GROUP BY cp.cu_codigo,cp.cu_nombre, sc.cu_codigo, sc.cu_nombre ");
                
                Query q = entityManager.createNativeQuery(builder.toString());
                if(idAnio != null && idAnio != 0)
                    q.setParameter("1", idAnio);
                if(idComponente != null && idComponente != 0)
                    q.setParameter("2", idComponente);
                if(idCategoria != null && idCategoria != 0)
                    q.setParameter("3", idCategoria);
 
            TopePresupuestalGroup tp;
            for(Object obj : q.getResultList()){
                Object[] obar = (Object[]) obj;
                tp = new TopePresupuestalGroup();
                tp.setCodigoCuenta((String) obar[0]);
                tp.setNombreCuenta((String) obar[1]);
                tp.setCodigoSubCuenta((String) obar[2]);
                tp.setNombreSubCuenta((String) obar[3]);
                tp.setMontoFormulacion((BigDecimal) obar[4]);
                tp.setMontoAprobado((BigDecimal) obar[5]);
                listado.add(tp);
            }
            return listado;
        } catch (Exception e) {
            throw e;
        }
    }
 
    /**
     * Este método agrupa los resultados de TopePresupuestal
     * @param idAnio
     * @param idCategoria
     * @param idComponente
     * @return 
     * @throws gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException 
     */
    public List<TopePresupuestalGroup> getTopePresupuestalFuentesAgrupado(Integer idPresupuesto) throws DAOGeneralException{
        try {
            List<TopePresupuestalGroup> listado = new ArrayList<TopePresupuestalGroup>();
            StringBuilder builder = new StringBuilder();
            builder.append(
                "SELECT tp.tp_fuente_financiamiento,tp.tp_fuente_recursos, sum(tp.tp_monto_formulacion),sum(tp.tp_monto_aprobado) "+
                "FROM " + Constantes.SCHEMA_SIAP2 +".ss_tope_presupestal tp  "+
                "WHERE 1 = 1 ");
                if(idPresupuesto != null && idPresupuesto != 0)
                    builder.append("AND tp.tp_rel_ges_pres = ?1 ");

                builder.append(" GROUP BY tp.tp_fuente_financiamiento,tp.tp_fuente_recursos ");
                
                Query q = entityManager.createNativeQuery(builder.toString());
                if(idPresupuesto != null && idPresupuesto != 0)
                    q.setParameter("1", idPresupuesto);

            TopePresupuestalGroup tp;
            for(Object obj : q.getResultList()){
                Object[] obar = (Object[]) obj;
                tp = new TopePresupuestalGroup();
                Integer idFuente = -1;
                Integer idFuenteRecursos = -1; 
                if(obar[0] != null) {
                    idFuente = Integer.parseInt(String.valueOf(obar[0]));
                }
                if(obar[1] != null) {
                    idFuenteRecursos = Integer.parseInt(String.valueOf(obar[1]));
                }
                
                tp.setIdFuente(idFuente);
                tp.setIdFuenteRecursos(idFuenteRecursos);
                tp.setMontoFormulacion((BigDecimal) obar[2]);
                tp.setMontoAprobado((BigDecimal) obar[3]);
                listado.add(tp);
            }
            return listado;
        } catch (Exception e) {
            throw e;
        } 
    }
    
    /**
     * Este método agrupa los resultados de TopePresupuestal
     * @param idAnio
     * @param idCategoria
     * @param idComponente
     * @return 
     * @throws gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException 
     */
    public List<TopePresupuestalGroup> getTotalPresupuestalByPresupuestoId(Integer idPresupuesto) throws DAOGeneralException{
        try {
            List<TopePresupuestalGroup> listado = new ArrayList<TopePresupuestalGroup>();
            StringBuilder builder = new StringBuilder();
            builder.append(
                "SELECT sum(tp.tp_monto_formulacion),sum(tp.tp_monto_aprobado) "+
                "FROM " + Constantes.SCHEMA_SIAP2 +".ss_tope_presupestal tp  "+
                "WHERE 1 = 1 ");
                if(idPresupuesto != null && idPresupuesto != 0)
                    builder.append("AND tp.tp_rel_ges_pres = ?1 ");
                
                Query q = entityManager.createNativeQuery(builder.toString());
                if(idPresupuesto != null && idPresupuesto != 0)
                    q.setParameter("1", idPresupuesto);

            TopePresupuestalGroup tp;
            for(Object obj : q.getResultList()){
                Object[] obar = (Object[]) obj;
                tp = new TopePresupuestalGroup();
                tp.setMontoFormulacion((BigDecimal) obar[2]);
                tp.setMontoAprobado((BigDecimal) obar[3]);
                listado.add(tp);
            }
            return listado;
        } catch (Exception e) {
            throw e;
        } 
    }
    
    public TotalesMatriculas getCantidadMatriculasPorPresupuesto(Integer idPresupuesto) throws DAOGeneralException{
        try {
            StringBuilder builder = new StringBuilder();
            builder.append(
                "SELECT sum(tp_cant_matricula),sum(tp_cant_matricula_aprobada) "+
                "FROM " + Constantes.SCHEMA_SIAP2 +".ss_tope_presupestal tp  "+
                "WHERE 1 = 1 ");
                if(idPresupuesto != null && idPresupuesto != 0)
                    builder.append("AND tp.tp_rel_ges_pres = ?1 ");

                Query q = entityManager.createNativeQuery(builder.toString());
                if(idPresupuesto != null && idPresupuesto != 0)
                    q.setParameter("1", idPresupuesto);

            TotalesMatriculas tp = new TotalesMatriculas();
            tp.setMatriculasFormuladas(0);
            tp.setMatriculasAprobadas(0);
            for(Object obj : q.getResultList()){
                Object[] obar = (Object[]) obj;
                Integer matriculasFormuladas = 0;
                Integer matriculasAprobadas = 0; 
                if(obar[0] != null) {
                    matriculasFormuladas = obtenerCantidad(obar[0]);
                }
                if(obar[1] != null) {
                    matriculasAprobadas = obtenerCantidad(obar[1]);
                }
                
                tp.setMatriculasFormuladas(matriculasFormuladas);
                tp.setMatriculasAprobadas(matriculasAprobadas);
            }
            return tp;
        } catch (Exception e) {
            throw e;
        } 
    }
    
    
    public Integer obtenerCantidad(Object obj) {
        Integer resultado = 0;
        if(obj instanceof BigDecimal) {
            resultado = ((BigDecimal) obj).intValue();
        } else if(obj instanceof Integer) {
            resultado = (Integer)obj;
        }
        return resultado;
    }
    /**
     * Este método verifica si existe un registro de sg_sede y obtiene su PK
     * @param codigo
     * @return 
     * @throws gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException 
     */
    public SgSede getSedeByCodigo(String codigo) throws DAOGeneralException {
        try {
            return (SgSede) entityManager
                .createQuery("select g from SgSede g where g.codigo = :cod")
                .setParameter("cod", codigo)
                .getSingleResult();
        } catch (Exception e) {
            throw e;
        }
    }
    
    /**
     * Este método buscan un registro de SgPresupuestoEscolarMovimiento filtrado por ID
     * @param id
     * @return 
     * @throws gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException 
     */
    public SgPresupuestoEscolarMovimiento getMovimientoById(Integer id) throws DAOGeneralException{
        return (SgPresupuestoEscolarMovimiento) entityManager
                .createQuery("select g from SgPresupuestoEscolarMovimiento g where g.id = :id")
                .setParameter("id", id)
                .getSingleResult();
    }
    
    /**
     * Este método buscan el numero de movimiento Maximo actual de la tabla SgPresupuestoEscolarMovimiento
     * @param idPresupuesto
     * @return 
     * @throws gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException 
     */
    public Integer getMaxMovimiento(Integer idPresupuesto) throws DAOGeneralException{
        return (Integer) entityManager
                .createNativeQuery("select max(g.mov_num_movimiento) from finanzas.sg_presupuesto_escolar_movimiento g where g.mov_presupuesto_fk = ?1")
                .setParameter("1", idPresupuesto)
                .getSingleResult();
    }
  
    /**
     * Este método busca todos los registros de TechoPresupuestal filtrados para la generacion de Transferencias a centros educativos
     * @param idComp
     * @param idSubComp
     * @param idUniPres
     * @param idLinPres
     * @return 
     * @throws gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException 
     */
    public List<TopePresupuestal> getTopePresupuestalByTransferencias(Integer idComp, Integer idSubComp, Integer idUniPres, Integer idLinPres) throws DAOGeneralException{
        return (List<TopePresupuestal>) entityManager.createQuery("Select g from TopePresupuestal g where 1=1 "
                + " and g.gesPresEs.categoriaPresupuestoEscolar.id = :idComp "
                + " and g.gesPresEs.id = :idSubComp "
                + " and g.subCuenta.cuentaPadre.id = :idUniPres "
                + " and g.subCuenta.id = :idLinPres "
                + " and g.estado = :aprobacion")
                .setParameter("idComp", idComp)
                .setParameter("idSubComp", idSubComp)
                .setParameter("idUniPres", idUniPres)
                .setParameter("idLinPres", idLinPres)
                .setParameter("aprobacion", EstadoTopePresupuestal.APROBACION)
                .getResultList();
    }
  
    /**
     * Este método busca la cantidad de topes existentes que posean el mismo componente, subcuenta y sede
     * @param idComp
     * @param idSub
     * @param idSede
     * @return 
     * @throws gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException 
     */
    public List<TopePresupuestal> getCountTopePresupuestalByMatch(Integer idComp, Integer idSub, Integer idSede) throws DAOGeneralException{
        return  (List<TopePresupuestal>) entityManager.createQuery("Select t from TopePresupuestal t "
                + "where t.gesPresEs.id = :idComp and t.subCuenta.id = :idSub and t.sede.id = :idSede")
                .setParameter("idComp", idComp)
                .setParameter("idSub", idSub)
                .setParameter("idSede", idSede)
                .getResultList();
    }

    /**
     * Este método obtiene todos los registros de TopePresupuestal cuyos componentes pertenezcan a una misma categoria parametrizada
     * @param idComp
     * @return
     * @throws DAOGeneralException 
     */
    public List<TopePresupuestal> getTopePresupuestalByIdCategoriaPresupuestoEscolar(Integer idComp) throws DAOGeneralException{
        return (List<TopePresupuestal>) entityManager
                .createQuery("select g from TopePresupuestal g where g.gesPresEs.categoriaPresupuestoEscolar.id = :idComp")
                .setParameter("idComp", idComp)
                .getResultList();
    }

    /**
     * Este método obtiene todos los registros de TopePresupuestal cuyos componentes se relacionen a un mismo subComponente
     * @param idComp
     * @return
     * @throws DAOGeneralException 
     */
    public List<TopePresupuestal> getTopePresupuestalByIdComponentePresupuestoEscolar(Integer idComp) throws DAOGeneralException{
        return (List<TopePresupuestal>) entityManager
                .createQuery("select g from TopePresupuestal g where g.gesPresEs.id = :idComp")
                .setParameter("idComp", idComp)
                .getResultList();
    }
    
    /**
     * Este método obtiene todos los registros de TopePresupuestal cuyos componentes se relacionen a un mismo subComponente
     * @param idCat
     * @return
     * @throws DAOGeneralException 
     */
    public List<TopePresupuestal> getTopePresupuestalByIdCategoriaComponente(Integer idCat) throws DAOGeneralException{
        return (List<TopePresupuestal>) entityManager
                .createQuery("select g from TopePresupuestal g where g.gesPresEs.categoriaPresupuestoEscolar.id = :idCat")
                .setParameter("idCat", idCat)
                .getResultList();
    }
    
    
}
