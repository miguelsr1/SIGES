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
public class EstadosInsumosDataProvider implements IDataProvider, Serializable {

    private InsumoDelegate delegate;
    
    private Integer idInsumo;
    private Integer idAnioFiscal;
    private Integer idPOA;
    private Integer idProyecto;
    private Integer idProcesoAdq;
    private Integer idOEG;    
    private Integer idProgramaPresupuestario;
    private Integer idSubprogramaPresupuestario;
    private Boolean noUACI;
    private String codigoSIIP;
    private String[] orderBy;
    private boolean[] ascending;
    
    
    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);
    

    public EstadosInsumosDataProvider() {
       
    }

    public EstadosInsumosDataProvider(InsumoDelegate delegate, Boolean noUACI, String codigoSIIP, Integer idOEG, Integer idInsumo, Integer idAnioFiscal, Integer idPOA, Integer idProyecto, Integer idProcesoAdq, Integer idProgramaPresupuestario, Integer idSubprogramaPresupuestario, String[] orderBy, boolean[] ascending) {
        this.delegate = delegate;
        this.idInsumo = idInsumo;
        this.idAnioFiscal = idAnioFiscal;
        this.idPOA = idPOA;
        this.idProyecto = idProyecto;
        this.idProcesoAdq = idProcesoAdq;
        this.idOEG = idOEG;
        this.noUACI = noUACI;
        this.codigoSIIP = codigoSIIP;
        this.idProgramaPresupuestario = idProgramaPresupuestario;
        this.idSubprogramaPresupuestario = idSubprogramaPresupuestario;
        this.orderBy = orderBy;
        this.ascending = ascending;
    }

      
    
    @Override
    public List<Object> getBufferedData(Integer startRowI, Integer offsetI) {
        try {
            
            List l = delegate.getEstadosInsumos(noUACI, codigoSIIP, idOEG, idInsumo, idAnioFiscal, idPOA, idProyecto, idProcesoAdq, idProgramaPresupuestario, idSubprogramaPresupuestario, startRowI, startRowI + offsetI,  orderBy, ascending);            
            return l;
        } catch (Exception ex) {      
            return new LinkedList();
        }
    }

    @Override
    public Integer getCountData() {
        try {
            int count = ((int)delegate.countEstadosInsumos(noUACI, codigoSIIP, idOEG, idInsumo, idAnioFiscal, idPOA, idProyecto, idProcesoAdq, idProgramaPresupuestario, idSubprogramaPresupuestario));
            return count;
        } catch (Exception ex) {                 
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            return 0;
        }
    }
    
    @Override
    public void setOrderBy(String[] orderBy, boolean[] asc) {
        this.orderBy = orderBy;
        this.ascending = asc;
    }
}
