/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.utils.dataProvider;

import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.enums.EstadoCertificadoDispPresupuestaria;
import gob.mined.siap2.web.delegates.impl.CertificadoPresupuestarioDelegate;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import javax.inject.Named;


@Named
public class CertificadoDispPresDataProvider implements IDataProvider, Serializable {





    private Integer numero;
    private Date fecha;
    private EstadoCertificadoDispPresupuestaria estado;
    private Integer idPOInsumo;
    private Integer idFuenteRecursos;
    private Integer idFuenteFinanciamiento;
    private Integer idProcesoAdq;
    private Integer idProgramaPres;
    private Integer idSubProgramaPres;
    private Integer idProyecto;
    private Integer idAccCentral;
    private Integer idAsigNp;




    private String[] orderBy = null;
    private boolean[] ascending = null;
    private CertificadoPresupuestarioDelegate delegate;
    
    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);
    
    public CertificadoDispPresDataProvider() {       
    }
    

    public CertificadoDispPresDataProvider(CertificadoPresupuestarioDelegate delegate, Integer numero, Date fecha, EstadoCertificadoDispPresupuestaria estado, Integer idPOInsumo, Integer idFuenteRecursos, Integer idFuenteFinanciamiento, Integer idProcesoAdq, Integer idProgramaPres, Integer idSubProgramaPres, Integer idProyecto, Integer idAccCentral, Integer idAsigNp,  String[] orderBy,  boolean[] ascending ) {
        this.numero = numero;
        this.fecha = fecha;
        this.estado = estado;
        this.idPOInsumo = idPOInsumo;
        this.idFuenteRecursos = idFuenteRecursos;
        this.idFuenteFinanciamiento = idFuenteFinanciamiento;
        this.idProcesoAdq = idProcesoAdq;
        this.idProgramaPres = idProgramaPres;
        this.idSubProgramaPres = idSubProgramaPres;
        this.idProyecto = idProyecto;
        this.idAccCentral = idAccCentral;
        this.idAsigNp = idAsigNp;
        this.delegate = delegate;
        this.orderBy= orderBy;
        this.ascending= ascending;
    }


    public List<Object> getBufferedData(Integer startRow,
            Integer offset) {
        try {
            List l = delegate.getCertificadoDispPresupuestaria( numero, fecha, estado, idPOInsumo, idFuenteRecursos, idFuenteFinanciamiento, idProcesoAdq, idProgramaPres, idSubProgramaPres, idProyecto, idAccCentral, idAsigNp, startRow,  startRow + offset,  orderBy, ascending);
            return l;
            
        } catch (Exception ex) {            
             return new LinkedList();
        }
    }

    @Override
    public Integer getCountData() {
        try {
            Long resultado = delegate.countCertificadoDispPresupuestaria(numero, fecha, estado, idPOInsumo, idFuenteRecursos, idFuenteFinanciamiento, idProcesoAdq, idProgramaPres, idSubProgramaPres, idProyecto, idAccCentral, idAsigNp);
            return resultado.intValue();
        } catch (Exception ex) {
            
            return 0;
        }
    }
    

    @Override
    public void setOrderBy(String[] orderBy, boolean[] asc) {
        this.orderBy = orderBy;
        this.ascending = asc;
    }
}
