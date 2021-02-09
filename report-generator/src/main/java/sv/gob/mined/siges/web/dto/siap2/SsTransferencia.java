/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.siap2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * Clase para la representacion de los datos de Transferencia
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "traId", scope = SsTransferencia.class)
public class SsTransferencia implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long traId;
    
    private SsGesPresEs traSubcomponente;

    private SsCuenta traLineaPresupuestaria;

    private BigDecimal traPorcentaje;
    
    private BigDecimal traImporteFijoCentro;
    
    private Boolean traRemanente;
    
    private Date traUltModFecha;
    
    private String traUltModUsuario;
    

    private Integer traVersion;
    
    
    public SsTransferencia() {
    }

    public Long getTraId() {
        return traId;
    }

    public void setTraId(Long traId) {
        this.traId = traId;
    }


    public SsGesPresEs getTraSubcomponente() {
        return traSubcomponente;
    }

    public void setTraSubcomponente(SsGesPresEs traSubcomponente) {
        this.traSubcomponente = traSubcomponente;
    }

    public SsCuenta getTraLineaPresupuestaria() {
        return traLineaPresupuestaria;
    }

    public void setTraLineaPresupuestaria(SsCuenta traLineaPresupuestaria) {
        this.traLineaPresupuestaria = traLineaPresupuestaria;
    }

    public BigDecimal getTraPorcentaje() {
        return traPorcentaje;
    }

    public void setTraPorcentaje(BigDecimal traPorcentaje) {
        this.traPorcentaje = traPorcentaje;
    }

    public BigDecimal getTraImporteFijoCentro() {
        return traImporteFijoCentro;
    }

    public void setTraImporteFijoCentro(BigDecimal traImporteFijoCentro) {
        this.traImporteFijoCentro = traImporteFijoCentro;
    }

    public Boolean getTraRemanente() {
        return traRemanente;
    }

    public void setTraRemanente(Boolean traRemanente) {
        this.traRemanente = traRemanente;
    }

    public Date getTraUltModFecha() {
        return traUltModFecha;
    }

    public void setTraUltModFecha(Date traUltModFecha) {
        this.traUltModFecha = traUltModFecha;
    }

    public String getTraUltModUsuario() {
        return traUltModUsuario;
    }

    public void setTraUltModUsuario(String traUltModUsuario) {
        this.traUltModUsuario = traUltModUsuario;
    }

    public Integer getTraVersion() {
        return traVersion;
    }

    public void setTraVersion(Integer traVersion) {
        this.traVersion = traVersion;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.traId);
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
        final SsTransferencia other = (SsTransferencia) obj;
        if (!Objects.equals(this.traId, other.traId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SsTransferencia{" + "traId=" + traId + ", traSubcomponente=" + traSubcomponente + ", traLineaPresupuestaria=" + traLineaPresupuestaria + ", traPorcentaje=" + traPorcentaje + ", traImporteFijoCentro=" + traImporteFijoCentro + ", traRemanente=" + traRemanente + ", traUltModFecha=" + traUltModFecha + ", traUltModUsuario=" + traUltModUsuario + '}';
    }

    
    
}
