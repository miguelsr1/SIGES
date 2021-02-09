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
public class InsumosNoUACIDataProvider implements IDataProvider, Serializable {

    private InsumoDelegate delegate;

    private Integer idAnioFiscal;
    private Integer idProyecto;
    private Integer idAC;
    private Integer idANP;
    private Integer idUT;
    private String codigo;
    private String codigoInterno;
    private String[] orderBy;
    private boolean[] ascending;

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    public InsumosNoUACIDataProvider() {

    }

    public InsumosNoUACIDataProvider(InsumoDelegate insumoDelegate, Integer idAnioFiscal, Integer idProyecto, Integer idAC, Integer idANP, Integer idUT, String codigo, String codigoInterno, String[] orderBy, boolean[] asc) {
        this.delegate = insumoDelegate;
        this.idAnioFiscal = idAnioFiscal;
        this.idProyecto = idProyecto;
        this.idAC = idAC;
        this.idANP = idANP;
        this.idUT = idUT;
        this.codigo = codigo;
        this.codigoInterno = codigoInterno;
        this.orderBy = orderBy;
        this.ascending = asc;
    }

    /**
     * Devuelve la lista de insumos No UACI según los filtros aplicados
     * @param startRowI
     * @param offsetI
     * @return 
     */
    @Override
    public List<Object> getBufferedData(Integer startRowI, Integer offsetI) {
        try {

            List l = delegate.getInsumosNoUACI(idAnioFiscal, idProyecto, idAC, idANP, idUT, codigo, codigoInterno,startRowI, startRowI + offsetI, orderBy, ascending);
            return l;
        } catch (Exception ex) {
            return new LinkedList();
        }
    }

    /**
     * Devuelve la cantidad de insumos No UACI según los filtros aplicados
     * @return 
     */
    @Override
    public Integer getCountData() {
        try {
            int count = ((int) delegate.countInsumosNoUACI(idAnioFiscal, idProyecto, idAC, idANP, idUT, codigo, codigoInterno));
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
