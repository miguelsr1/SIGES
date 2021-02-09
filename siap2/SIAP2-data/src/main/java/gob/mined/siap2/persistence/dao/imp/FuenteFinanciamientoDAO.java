package gob.mined.siap2.persistence.dao.imp;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.entities.data.impl.FuenteFinanciamiento;
import gob.mined.siap2.entities.data.impl.FuenteRecursos;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.persistence.dao.imp.eclipselink.EclipselinkJpaDAOImp;
import java.io.Serializable;
import java.util.List;



@JPADAO
public class FuenteFinanciamientoDAO extends EclipselinkJpaDAOImp<FuenteFinanciamiento, Integer> implements  Serializable{
 
    /**
     * Este método devuelve todos los registros de FuenteFinanciamiento filtrados por Subcomponente y LineaPresupuestaria
     * 
     * @param idLinea
     * @param idComp
     * @return
     * @throws DAOGeneralException 
     */
    public List<FuenteFinanciamiento> getFuentesFinanciamientoByRelacionPresAnioFinanciamiento(Integer idLinea, Integer idComp) throws DAOGeneralException {

        return (List<FuenteFinanciamiento>) entityManager
                .createQuery("select distinct(ff) from RelacionPresAnioFinanciamiento rp JOIN rp.fuenteFinanciamiento ff "
                        + "where rp.relAnioPresupuesto.subCuenta.id = :idLinea and rp.relAnioPresupuesto.componentePresupuestoEscolar.id = :idComp")
                .setParameter("idLinea", idLinea)
                .setParameter("idComp", idComp)
                .getResultList();
    }
 
    /**
     * Este método devuelve todos los registros de FuenteRecursos filtrados por FuenteFinanciamiento, Subcomponente y LineaPresupuestaria
     * 
     * @param idLinea
     * @param idComp
     * @param idFuenteF
     * @return
     * @throws DAOGeneralException 
     */
    public List<FuenteRecursos> getFuentesRecursosByRelaciones(Integer idLinea, Integer idComp, Integer idFuenteF) throws DAOGeneralException {
        return (List<FuenteRecursos>) entityManager
                .createQuery("select fr from RelacionPresAnioFinanciamiento rp JOIN rp.fuenteRecursos fr where rp.relAnioPresupuesto.subCuenta.id = :idLinea and rp.relAnioPresupuesto.componentePresupuestoEscolar.id = :idComp and rp.fuenteFinanciamiento.id = :idFuenteF")
                .setParameter("idLinea", idLinea)
                .setParameter("idComp", idComp)
                .setParameter("idFuenteF", idFuenteF)
                .getResultList();
    }
    
}
