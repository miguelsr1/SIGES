/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data.impl;

import com.mined.siap2.interfaces.BaseCodiguera;
import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import gob.mined.siap2.entities.enums.TipoFactura;
import gob.mined.siap2.entities.enums.TipoImpuesto;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import gob.mined.siap2.entities.constantes.Constantes;
import javax.persistence.Temporal;
import javax.persistence.Version;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_impuestos")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class Impuesto implements BaseCodiguera, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_impuestos_gen")
    @SequenceGenerator(name = "seq_impuestos_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_impuestos", allocationSize = 1)
    @Column(name = "imp_id")
    private Integer id;

    @Column(name = "imp_codigo")
    private String codigo;

    @Column(name = "imp_nombre")
    private String nombre;
    
    @Column(name = "imp_habilitado")
    private Boolean habilitado=Boolean.TRUE;
    
    @Column(name = "imp_orden")
    private Integer orden;  
           
    @Enumerated(EnumType.STRING)
    @Column(name = "imp_tipo")
    private TipoImpuesto tipoImpuesto;
            
    @Enumerated(EnumType.STRING)
    @Column(name = "imp_tipo_factura")
    private TipoFactura tipoFactura;
        
    @Column(name = "imp_valor", columnDefinition = "Decimal(20,2)")
    private BigDecimal valor;
            
    @Column(name = "imp_prcj_ret_pj_nac", columnDefinition = "Decimal(20,2)")
    private BigDecimal porcentajeRetencionPersonaJuridicaNacional;
        
    @Column(name = "imp_prcj_ret_pf_nac", columnDefinition = "Decimal(20,2)")
    private BigDecimal porcentajeRetencionPersonaFisicaNacional;
    
    
    @Column(name = "imp_prcj_ret_pj_ext", columnDefinition = "Decimal(20,2)")
    private BigDecimal porcentajeRetencionPersonaJuridicaExtranjera;
        
    @Column(name = "imp_prcj_ret_pf_ext", columnDefinition = "Decimal(20,2)")
    private BigDecimal porcentajeRetencionPersonaFisicaExtranjera;
        
    @Column(name = "imp_por_def_en_contrato")
    private Boolean porDefectoEnContrato=Boolean.FALSE;
           
    //Auditoria
    @Column(name = "imp_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "imp_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "imp_version")
    @Version
    private Integer version;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(Boolean habilitado) {
        this.habilitado = habilitado;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public TipoImpuesto getTipoImpuesto() {
        return tipoImpuesto;
    }

    public void setTipoImpuesto(TipoImpuesto tipoImpuesto) {
        this.tipoImpuesto = tipoImpuesto;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }    

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUltUsuario() {
        return ultUsuario;
    }

    public TipoFactura getTipoFactura() {
        return tipoFactura;
    }

    public void setTipoFactura(TipoFactura tipoFactura) {
        this.tipoFactura = tipoFactura;
    }

    public BigDecimal getPorcentajeRetencionPersonaJuridicaNacional() {
        return porcentajeRetencionPersonaJuridicaNacional;
    }

    public void setPorcentajeRetencionPersonaJuridicaNacional(BigDecimal porcentajeRetencionPersonaJuridicaNacional) {
        this.porcentajeRetencionPersonaJuridicaNacional = porcentajeRetencionPersonaJuridicaNacional;
    }

    public BigDecimal getPorcentajeRetencionPersonaFisicaNacional() {
        return porcentajeRetencionPersonaFisicaNacional;
    }

    public void setPorcentajeRetencionPersonaFisicaNacional(BigDecimal porcentajeRetencionPersonaFisicaNacional) {
        this.porcentajeRetencionPersonaFisicaNacional = porcentajeRetencionPersonaFisicaNacional;
    }

    public BigDecimal getPorcentajeRetencionPersonaFisicaExtranjera() {
        return porcentajeRetencionPersonaFisicaExtranjera;
    }

    public void setPorcentajeRetencionPersonaFisicaExtranjera(BigDecimal porcentajeRetencionPersonaFisicaExtranjera) {
        this.porcentajeRetencionPersonaFisicaExtranjera = porcentajeRetencionPersonaFisicaExtranjera;
    }

    public BigDecimal getPorcentajeRetencionPersonaJuridicaExtranjera() {
        return porcentajeRetencionPersonaJuridicaExtranjera;
    }

    public void setPorcentajeRetencionPersonaJuridicaExtranjera(BigDecimal porcentajeRetencionPersonaJuridicaExtranjera) {
        this.porcentajeRetencionPersonaJuridicaExtranjera = porcentajeRetencionPersonaJuridicaExtranjera;
    }
    
    public Boolean getPorDefectoEnContrato() {
        return porDefectoEnContrato;
    }

    public void setPorDefectoEnContrato(Boolean porDefectoEnContrato) {
        this.porDefectoEnContrato = porDefectoEnContrato;
    }

    public void setUltUsuario(String ultUsuario) {
        this.ultUsuario = ultUsuario;
    }

    public Date getUltMod() {
        return ultMod;
    }

    public void setUltMod(Date ultMod) {
        this.ultMod = ultMod;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Impuesto other = (Impuesto) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
}
