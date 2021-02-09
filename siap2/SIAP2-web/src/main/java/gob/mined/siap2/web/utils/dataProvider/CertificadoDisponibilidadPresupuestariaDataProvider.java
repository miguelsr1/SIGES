/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.utils.dataProvider;

import gob.mined.siap2.web.utils.dataProvider.IDataProvider;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.web.delegates.impl.InsumoDelegate;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;


@Named
public class CertificadoDisponibilidadPresupuestariaDataProvider implements IDataProvider, Serializable {

    private InsumoDelegate delegate;
    

 
    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);
    

    public CertificadoDisponibilidadPresupuestariaDataProvider() {
       
    }

    public CertificadoDisponibilidadPresupuestariaDataProvider(InsumoDelegate delegate) {
        this.delegate = delegate;

    }

    @Override
    public List<Object> getBufferedData(Integer startRowI, Integer offsetI) {
        try {
            
            List l = delegate.getCertificadoDisponibilidadPresupuestariaInsumos(startRowI, startRowI + offsetI);            
            return l;
        } catch (Exception ex) {      
            return new LinkedList();
        }
    }

    @Override
    public Integer getCountData() {
        try {
            int count = ((int)delegate.countCertificadoDisponibilidadPresupuestariaInsumos());
            return count;
        } catch (Exception ex) {                 
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            return 0;
        }
    }
    
    @Override
    public void setOrderBy(String[] orderBy, boolean[] asc) {
    }
}
