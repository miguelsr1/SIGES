package gob.mined.siap2.persistence.dao.imp;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.entities.data.impl.RelacionGesPresEsAnioFiscal;
import gob.mined.siap2.entities.enums.TipoPresupuestoAnio;
import gob.mined.siap2.persistence.dao.imp.eclipselink.EclipselinkJpaDAOImp;
import java.io.Serializable;
import java.util.List;

@JPADAO
public class RelacionGesPresEsAnioFiscalDAO extends EclipselinkJpaDAOImp<RelacionGesPresEsAnioFiscal, Integer> implements  Serializable{
    
    
    /**
     * Método utilizado para obtener los registros de Presupuesto con sus fuentes de financiamiento asociadas
     * 
     * @param idSubcomponente
     * @param idAnioFiscal
     * @param idLineaPresupuestaria
     * @return 
     */
    public List<RelacionGesPresEsAnioFiscal> getRelacionesAnioFiscalConFinanciamiento(Integer idSubcomponente, Integer idAnioFiscal, Integer idLineaPresupuestaria){
        return entityManager.createQuery("Select ap from RelacionPresAnioFinanciamiento g LEFT JOIN g.relAnioPresupuesto ap "
                + "WHERE 1 = 1 "
                + "and ap.anioFiscal.id = :idAnioFiscal "
                + "and ap.componentePresupuestoEscolar.id = :idSubcomponente "
                + "and ap.subCuenta.id = :idLineaPresupuestaria")
                .setParameter("idAnioFiscal",idAnioFiscal)
                .setParameter("idSubcomponente",idSubcomponente)
                .setParameter("idLineaPresupuestaria",idLineaPresupuestaria)
                .getResultList();
    }
    
    /**
     * Método utilizado para obtener los registros de Presupuesto con sus fuentes de financiamiento asociadas
     * 
     * @param idSubcomponente
     * @param idAnioFiscal
     * @param idLineaPresupuestaria
     * @return 
     */
    public List<RelacionGesPresEsAnioFiscal> getRelacionesAnioFiscalPorSubcomponente(Integer idSubcomponente, TipoPresupuestoAnio tipo, Integer idAnioFiscal){
        return entityManager.createQuery("Select g from RelacionGesPresEsAnioFiscal g "
                + "WHERE g.componentePresupuestoEscolar.id = :idSubcomponente and g.tipo = :tipo and g.anioFiscal.id = :idAnioFiscal")
                .setParameter("idSubcomponente",idSubcomponente).setParameter("tipo", tipo).setParameter("idAnioFiscal", idAnioFiscal)
                .getResultList();
    }
    
    /**
     * Método utilizado para obtener los registros de Presupuesto con sus fuentes de financiamiento asociadas
     * 
     * @param idSubcomponente
     * @param idAnioFiscal
     * @param idLineaPresupuestaria
     * @return 
     */
    public List<RelacionGesPresEsAnioFiscal> getRelacionesAnioFiscalPorSubcomponente(Integer idSubcomponente, Integer idAnioFiscal){
        return entityManager.createQuery("Select g from RelacionGesPresEsAnioFiscal g "
                + "WHERE g.componentePresupuestoEscolar.id = :idSubcomponente and g.anioFiscal.id = :idAnioFiscal")
                .setParameter("idSubcomponente",idSubcomponente).setParameter("idAnioFiscal", idAnioFiscal)
                .getResultList();
    }
}
