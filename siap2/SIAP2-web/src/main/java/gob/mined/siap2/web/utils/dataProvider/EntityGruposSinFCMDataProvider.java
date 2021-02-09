/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.utils.dataProvider;

import gob.mined.siap2.web.utils.dataProvider.IDataProvider;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.web.delegates.impl.PACDelegate;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;


@Named
public class EntityGruposSinFCMDataProvider implements IDataProvider, Serializable {


    private AnioFiscal anio;
    private PACDelegate delegate;
    
    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);
    

    public EntityGruposSinFCMDataProvider() {
       
    }

    public EntityGruposSinFCMDataProvider( PACDelegate delegate, AnioFiscal anio) {
        this.delegate= delegate;
        this.anio=anio;
    }

    @Override
    public List<Object> getBufferedData(Integer startRowI, Integer offsetI) {
        try {
            List<Object> l = delegate.getPACGrupoSinFCM(anio.getId(), startRowI, startRowI + offsetI);
            return l;
        } catch (Exception ex) {      
            return new LinkedList();
        }
    }

    @Override
    public Integer getCountData() {
        try {
            int count = ((int)delegate.countPACGrupoSinFCM(anio.getId()));
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
