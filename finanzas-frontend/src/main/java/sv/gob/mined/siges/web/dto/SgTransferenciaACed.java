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
import java.time.LocalDateTime;
import java.util.Objects;
import sv.gob.mined.siges.web.dto.siap2.SsTransferenciaComponente;
import sv.gob.mined.siges.web.enumerados.EnumEstadoDocumento;
import sv.gob.mined.siges.web.enumerados.EnumTransferenciaEstado;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "tacPk", scope = SgTransferenciaACed.class)
public class SgTransferenciaACed implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long tacPk;

    private SgSede tacCedFk;

    private SsTransferenciaComponente tacTransferenciaFk;

    private SgTransferenciaDireccionDep tacTransferenciaDireccionDepFk;
    
    private SgRecibos recibo;
    
    private BigDecimal tacMontoAutorizado;

    private BigDecimal tacMontoSolicitado;
    
    private EnumTransferenciaEstado tacEstado;
    
    private LocalDateTime tacUltModFecha;

    private String tacUltModUsuario;
    
    private Integer tacBeneficiarios;

    private Integer tacVersion;
    
    private Boolean seleccionado;
    
    private BigDecimal montoReqFondo;
    
    private SgReqFondoCed tacReqFondoCed;
    
    private EnumEstadoDocumento estadoCcf;
    
    private EnumEstadoDocumento estadoConv;
    
    public SgTransferenciaACed() {
        
    }

    public Long getTacPk() {
        return tacPk;
    }

    public void setTacPk(Long tacPk) {
        this.tacPk = tacPk;
    }

    public SgSede getTacCedFk() {
        return tacCedFk;
    }

    public void setTacCedFk(SgSede tacCedFk) {
        this.tacCedFk = tacCedFk;
    }

    public SsTransferenciaComponente getTacTransferenciaFk() {
        return tacTransferenciaFk;
    }

    public void setTacTransferenciaFk(SsTransferenciaComponente tacTransferenciaFk) {
        this.tacTransferenciaFk = tacTransferenciaFk;
    }


    public SgTransferenciaDireccionDep getTacTransferenciaDireccionDepFk() {
        return tacTransferenciaDireccionDepFk;
    }

    public void setTacTransferenciaDireccionDepFk(SgTransferenciaDireccionDep tacTransferenciaDireccionDepFk) {
        this.tacTransferenciaDireccionDepFk = tacTransferenciaDireccionDepFk;
    }

    public SgRecibos getRecibo() {
        return recibo;
    }

    public void setRecibo(SgRecibos recibo) {
        this.recibo = recibo;
    }
    

    public BigDecimal getTacMontoAutorizado() {
        return tacMontoAutorizado;
    }

    public void setTacMontoAutorizado(BigDecimal tacMontoAutorizado) {
        this.tacMontoAutorizado = tacMontoAutorizado;
    }

    public BigDecimal getTacMontoSolicitado() {
        return tacMontoSolicitado;
    }

    public void setTacMontoSolicitado(BigDecimal tacMontoSolicitado) {
        this.tacMontoSolicitado = tacMontoSolicitado;
    }

    public EnumTransferenciaEstado getTacEstado() {
        return tacEstado;
    }

    public void setTacEstado(EnumTransferenciaEstado tacEstado) {
        this.tacEstado = tacEstado;
    }

   
    public LocalDateTime getTacUltModFecha() {
        return tacUltModFecha;
    }

    public void setTacUltModFecha(LocalDateTime tacUltModFecha) {
        this.tacUltModFecha = tacUltModFecha;
    }

    public String getTacUltModUsuario() {
        return tacUltModUsuario;
    }

    public void setTacUltModUsuario(String tacUltModUsuario) {
        this.tacUltModUsuario = tacUltModUsuario;
    }

    public Integer getTacVersion() {
        return tacVersion;
    }

    public void setTacVersion(Integer tacVersion) {
        this.tacVersion = tacVersion;
    }

    public Boolean getSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(Boolean seleccionado) {
        this.seleccionado = seleccionado;
    }

    public BigDecimal getMontoReqFondo() {
        return montoReqFondo;
    }

    public void setMontoReqFondo(BigDecimal montoReqFondo) {
        this.montoReqFondo = montoReqFondo;
    }

    public SgReqFondoCed getTacReqFondoCed() {
        return tacReqFondoCed;
    }

    public void setTacReqFondoCed(SgReqFondoCed tacReqFondoCed) {
        this.tacReqFondoCed = tacReqFondoCed;
    }

    public EnumEstadoDocumento getEstadoCcf() {
        return estadoCcf;
    }

    public void setEstadoCcf(EnumEstadoDocumento estadoCcf) {
        this.estadoCcf = estadoCcf;
    }

    public EnumEstadoDocumento getEstadoConv() {
        return estadoConv;
    }

    public void setEstadoConv(EnumEstadoDocumento estadoConv) {
        this.estadoConv = estadoConv;
    }

    public Integer getTacBeneficiarios() {
        return tacBeneficiarios;
    }

    public void setTacBeneficiarios(Integer tacBeneficiarios) {
        this.tacBeneficiarios = tacBeneficiarios;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tacPk != null ? tacPk.hashCode() : 0);
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
        final SgTransferenciaACed other = (SgTransferenciaACed) obj;
        if (!Objects.equals(this.tacPk, other.tacPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgTransferenciaACed{" + "tacPk=" + tacPk + '}';
    }


   
    
}
