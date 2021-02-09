/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import sv.gob.mined.siges.web.enumerados.EnumEstadoDocumento;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "recPk", scope = SgRecibos.class)
public class SgRecibos implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long recPk;

    private SgTransferenciaACed recTransferencia;
    
    private SgArchivo recArchivo;
    
    private EnumEstadoDocumento recEstado;
    
    private LocalDate recFecha;
    
    private BigDecimal recMonto;

    private LocalDateTime recUltModFecha;

    private String recUltModUsuario;

    private Integer recVersion;
    
    
    public SgRecibos() {
    }

    public Long getRecPk() {
        return recPk;
    }

    public void setRecPk(Long recPk) {
        this.recPk = recPk;
    }

    public SgTransferenciaACed getRecTransferencia() {
        return recTransferencia;
    }

    public void setRecTransferencia(SgTransferenciaACed recTransferencia) {
        this.recTransferencia = recTransferencia;
    }

    public SgArchivo getRecArchivo() {
        return recArchivo;
    }

    public void setRecArchivo(SgArchivo recArchivo) {
        this.recArchivo = recArchivo;
    }

    public LocalDate getRecFecha() {
        return recFecha;
    }

    public void setRecFecha(LocalDate recFecha) {
        this.recFecha = recFecha;
    }

    public BigDecimal getRecMonto() {
        return recMonto;
    }

    public void setRecMonto(BigDecimal recMonto) {
        this.recMonto = recMonto;
    }

    public LocalDateTime getRecUltModFecha() {
        return recUltModFecha;
    }

    public void setRecUltModFecha(LocalDateTime recUltModFecha) {
        this.recUltModFecha = recUltModFecha;
    }

    public String getRecUltModUsuario() {
        return recUltModUsuario;
    }

    public void setRecUltModUsuario(String recUltModUsuario) {
        this.recUltModUsuario = recUltModUsuario;
    }

    public Integer getRecVersion() {
        return recVersion;
    }

    public void setRecVersion(Integer recVersion) {
        this.recVersion = recVersion;
    }

    public EnumEstadoDocumento getRecEstado() {
        return recEstado;
    }

    public void setRecEstado(EnumEstadoDocumento recEstado) {
        this.recEstado = recEstado;
    }
   
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (recPk != null ? recPk.hashCode() : 0);
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
        final SgRecibos other = (SgRecibos) obj;
        if (!Objects.equals(this.recPk, other.recPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgRecibos{" + "recPk=" + recPk + ", recTransferencia=" + recTransferencia + ", recArchivo=" + recArchivo + ", recEstado=" + recEstado + ", recFecha=" + recFecha + ", recMonto=" + recMonto + '}';
    }


    
    
}
