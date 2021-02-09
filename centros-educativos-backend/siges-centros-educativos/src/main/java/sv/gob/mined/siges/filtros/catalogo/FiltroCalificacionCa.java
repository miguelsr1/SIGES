/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros.catalogo;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class FiltroCalificacionCa implements Serializable {
    

    private Long ecaCalPk;
    
    private String calCodigo;
    private String calCodigoExacto;

    private String calValor;

    private Integer calOrden;

    private Boolean calHabilitado;

    private String calEscala;
    private String calEscalaExacto;
    private Long calEscalaCalificacionPk;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    public FiltroCalificacionCa() {
    }

    public String getCalCodigo() {
        return calCodigo;
    }

    public void setCalCodigo(String calCodigo) {
        this.calCodigo = calCodigo;
    }

    public String getCalCodigoExacto() {
        return calCodigoExacto;
    }

    public void setCalCodigoExacto(String calCodigoExacto) {
        this.calCodigoExacto = calCodigoExacto;
    }

    public String getCalValor() {
        return calValor;
    }

    public void setCalValor(String calValor) {
        this.calValor = calValor;
    }

    public Integer getCalOrden() {
        return calOrden;
    }

    public void setCalOrden(Integer calOrden) {
        this.calOrden = calOrden;
    }

    public Boolean getCalHabilitado() {
        return calHabilitado;
    }

    public void setCalHabilitado(Boolean calHabilitado) {
        this.calHabilitado = calHabilitado;
    }

    public String getCalEscala() {
        return calEscala;
    }

    public void setCalEscala(String calEscala) {
        this.calEscala = calEscala;
    }

    public String getCalEscalaExacto() {
        return calEscalaExacto;
    }

    public void setCalEscalaExacto(String calEscalaExacto) {
        this.calEscalaExacto = calEscalaExacto;
    }

    public Long getEcaCalPk() {
        return ecaCalPk;
    }

    public void setEcaCalPk(Long ecaCalPk) {
        this.ecaCalPk = ecaCalPk;
    }

    public Long getCalEscalaCalificacionPk() {
        return calEscalaCalificacionPk;
    }

    public void setCalEscalaCalificacionPk(Long calEscalaCalificacionPk) {
        this.calEscalaCalificacionPk = calEscalaCalificacionPk;
    }

        
    public Long getFirst() {
        return first;
    }

    public void setFirst(Long first) {
        this.first = first;
    }

    public Long getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(Long maxResults) {
        this.maxResults = maxResults;
    }

    public String[] getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String[] orderBy) {
        this.orderBy = orderBy;
    }

    public boolean[] getAscending() {
        return ascending;
    }

    public void setAscending(boolean[] ascending) {
        this.ascending = ascending;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.ecaCalPk);
        hash = 97 * hash + Objects.hashCode(this.calCodigo);
        hash = 97 * hash + Objects.hashCode(this.calCodigoExacto);
        hash = 97 * hash + Objects.hashCode(this.calValor);
        hash = 97 * hash + Objects.hashCode(this.calOrden);
        hash = 97 * hash + Objects.hashCode(this.calHabilitado);
        hash = 97 * hash + Objects.hashCode(this.calEscala);
        hash = 97 * hash + Objects.hashCode(this.calEscalaExacto);
        hash = 97 * hash + Objects.hashCode(this.calEscalaCalificacionPk);
        hash = 97 * hash + Objects.hashCode(this.first);
        hash = 97 * hash + Objects.hashCode(this.maxResults);
        hash = 97 * hash + Arrays.deepHashCode(this.orderBy);
        hash = 97 * hash + Arrays.hashCode(this.ascending);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FiltroCalificacionCa other = (FiltroCalificacionCa) obj;
        if (!Objects.equals(this.calCodigo, other.calCodigo)) {
            return false;
        }
        if (!Objects.equals(this.calCodigoExacto, other.calCodigoExacto)) {
            return false;
        }
        if (!Objects.equals(this.calValor, other.calValor)) {
            return false;
        }
        if (!Objects.equals(this.calEscala, other.calEscala)) {
            return false;
        }
        if (!Objects.equals(this.calEscalaExacto, other.calEscalaExacto)) {
            return false;
        }
        if (!Objects.equals(this.ecaCalPk, other.ecaCalPk)) {
            return false;
        }
        if (!Objects.equals(this.calOrden, other.calOrden)) {
            return false;
        }
        if (!Objects.equals(this.calHabilitado, other.calHabilitado)) {
            return false;
        }
        if (!Objects.equals(this.calEscalaCalificacionPk, other.calEscalaCalificacionPk)) {
            return false;
        }
        if (!Objects.equals(this.first, other.first)) {
            return false;
        }
        if (!Objects.equals(this.maxResults, other.maxResults)) {
            return false;
        }
        if (!Arrays.deepEquals(this.orderBy, other.orderBy)) {
            return false;
        }
        if (!Arrays.equals(this.ascending, other.ascending)) {
            return false;
        }
        return true;
    }

   
    
    
    

    
}
