package gob.mined.siap2.persistence.dao.imp;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siges.entities.data.impl.SgOrganizacionCurricular;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.persistence.dao.imp.eclipselink.EclipselinkJpaDAOImp;
import java.util.List;

@JPADAO
public class SgOrganizacionesCurricularDAO extends EclipselinkJpaDAOImp<SgOrganizacionCurricular, Integer>{
 
  
    
    /**
     * Este método devuelve todos los registros de SgOrganizacionCurricular
     * @return
     * @throws DAOGeneralException 
     */
    public List<SgOrganizacionCurricular> getSgOrganizaciones() throws DAOGeneralException{
        return (List<SgOrganizacionCurricular>) entityManager.createQuery("select o from SgOrganizacionCurricular o").getResultList();
    }
    
    
    /**
     * Este método devuelve todos los registros de SgOrganizacionCurricular que se encuentren Habilitados
     * @return
     * @throws DAOGeneralException 
     */
    public List<SgOrganizacionCurricular> getSgOrganizacionesHabilitados() throws DAOGeneralException{
        return (List<SgOrganizacionCurricular>) entityManager.createQuery("select o from SgOrganizacionCurricular o where o.habilitado = true").getResultList();
    }
}
