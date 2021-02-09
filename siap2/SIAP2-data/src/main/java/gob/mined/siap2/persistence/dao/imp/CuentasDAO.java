package gob.mined.siap2.persistence.dao.imp;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.entities.data.impl.Cuentas;
import gob.mined.siap2.entities.data.impl.TransferenciasComponente;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.persistence.dao.imp.eclipselink.EclipselinkJpaDAOImp;
import java.io.Serializable;
import java.util.List;
import javax.persistence.NoResultException;

@JPADAO
public class CuentasDAO extends EclipselinkJpaDAOImp<Cuentas, Integer> implements Serializable {
    
    /**
     * Este método devuelve un registro de Cuentas filtrado por código
     *
     * @param codigo
     * @return
     * @throws DAOGeneralException 
     */
    public Cuentas getCuentaByCodigo(String codigo) throws DAOGeneralException {
        try {
        return (Cuentas) entityManager
                .createQuery("select g from Cuentas g where g.codigo = :codigo")
                .setParameter("codigo", codigo)
                .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
    
    /**
     * Este método devuelve un registro de Cuentas filtrado por código
     *
     * @param codigo
     * @return
     * @throws DAOGeneralException 
     */
    public Cuentas getCuentaByCodigoYAnioFiscal(String codigo,Integer anio) throws DAOGeneralException {
        try {
            List<Cuentas> resultado = entityManager
                    .createQuery("select g from Cuentas g where g.codigo = :codigo and g.cuentaPadre.anioFiscal.id = :anio")
                    .setParameter("codigo", codigo).setParameter("anio", anio)
                    .getResultList();
            if(resultado != null && !resultado.isEmpty() && resultado.size() > 0) {
                return resultado.get(0);
            }
        } catch (NoResultException nre) {
            return null;
        }
        return null;
    }
    
    /**
     * Este método devuelve todos los registros de Cuentas
     *
     * @return
     * @throws DAOGeneralException 
     */
    public List<Cuentas> getCuentas() throws DAOGeneralException {
        return (List<Cuentas>) entityManager.createQuery("select g from Cuentas g").getResultList();
    }
    
    /**
     * Este método devuelve todos los registros de Cuentas que tengan el mismo
     * registro padre
     *
     * @param idPadre
     * @return
     * @throws DAOGeneralException 
     */
    public List<Cuentas> getCuentasByIdPadre(Integer idPadre) throws DAOGeneralException {
        return (List<Cuentas>) entityManager.createQuery("select g from Cuentas g where g.cuentaPadre.id = :idPadre")
                .setParameter("idPadre", idPadre).getResultList();
    }
    
    /**
     * Este método devuelve todos los registros de Cuentas que tengan el mismo AnioFiscal
     *
     * @param idAnio
     * @return
     * @throws DAOGeneralException 
     */
    public List<Cuentas> getCuentasByAnioFiscal(Integer idAnio) throws DAOGeneralException {
        return (List<Cuentas>) entityManager.createQuery("select g from Cuentas g where g.anioFiscal.id = :idAnio")
                .setParameter("idAnio", idAnio).getResultList();
    }
    
    public List<Cuentas> getCuentasByAnioFiscalAndCodigo(Integer idAnio, String codigo) throws DAOGeneralException {
        return (List<Cuentas>) entityManager.createQuery("select g from Cuentas g where g.anioFiscal.id = :idAnio and g.codigo = :codigo")
                .setParameter("idAnio", idAnio).setParameter("codigo", codigo).getResultList();
    }
    
    public Cuentas getCuentaByAnioFiscalAndCodigo(Integer idAnio, String codigo) throws DAOGeneralException {
        try {
            List<Cuentas> listado = (List<Cuentas>) entityManager.createQuery("select g from Cuentas g where g.anioFiscal.id = :idAnio and g.codigo = :codigo and g.cuentaPadre is null")
                .setParameter("idAnio", idAnio).setParameter("codigo", codigo).getResultList();
            if(listado != null && !listado.isEmpty() && listado.size() > 0) {
                return listado.get(0);
            }
        } catch (NoResultException nre) {
            return null;
        }
        return null;
    }
    /**
     * Este método devuelve una lista de registros de Cuentas que sean registros
     * padre y que estén habilitados
     *
     * @return 
     */
    public List<Cuentas> getCuentasHijasHabilitadasByIdPadre(Integer idPadre){
        return (List<Cuentas>) entityManager.createQuery("Select c from Cuentas c where c.habilitado = TRUE and c.cuentaPadre.id = :idPadre ")
                .setParameter("idPadre", idPadre)
                .getResultList();
    }
    
    /**
     * Este método devuelve una lista de registros de Cuentas que sean registros padre y que estén habilitados
     * @return 
     */
    public List<Cuentas> getCuentasPadresHabilitados() {
        return entityManager.createQuery("SELECT g FROM Cuentas g WHERE g.cuentaPadre is null and g.habilitado = TRUE ").getResultList();
    }
    
    /**
     * Este método devuelve una lista de registros de Cuentas que sean registros
     * padre y que estén habilitados
     *
     * @return 
     */
    public List<Cuentas> getCuentasHijasHabilitadas() {
        return entityManager.createQuery("SELECT g FROM Cuentas g WHERE g.cuentaPadre is not null and g.habilitado = TRUE ").getResultList();
    }
    
    /**
     * Este método devuelve una lista de registros de Cuentas que no tengan relaciones con padre
     * habilitados
     *
     * @return 
     */
    public List<Cuentas> getCuentasPadres() {
        return entityManager.createQuery("SELECT g FROM Cuentas g WHERE g.cuentaPadre IS NULL").getResultList();
    }
    
    /**
     * Este método devuelve una lista de registros de Cuentas que tengan relacion con componenteAnioFiscal
     * habilitados
     *
     * @param idCuenta
     * @return 
     */
    public List<Cuentas> getCuentaRelacionComponente(Integer idCuenta) {
        return entityManager.createQuery("SELECT sc FROM RelacionGesPresEsAnioFiscal g JOIN g.subCuenta sc where sc.id = :idCuenta ")
                .setParameter("idCuenta", idCuenta)
                .getResultList();
    }
    
    /**
     * Este método devuelve una lista de registros de Cuentas que tengan relacion con TransferenciaComponente
     * habilitados
     *
     * @param idCuenta
     * @return 
     */
    public List<TransferenciasComponente> getCuentaTransferenciaComponente(Integer idCuenta) {
        return entityManager.createQuery("SELECT g FROM TransferenciasComponente g where g.unidadPresupuestaria.id = :idCuenta or g.lineaPresupuestaria.id = :idCuenta")
                .setParameter("idCuenta", idCuenta)
                .getResultList();
    }

}
