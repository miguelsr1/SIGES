/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import gob.mined.siap2.entities.enums.EstadoPoliza;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_polizas_de_concen")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class PolizaDeConcentracion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_pol_de_con_gen")
    @SequenceGenerator(name = "seq_pol_de_con_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_pol_de_con", allocationSize = 1)
    @Column(name = "pol_id")
    protected Integer id;

    @ManyToOne
    @JoinColumn(name = "pol_fuente_id")
    private POMontoFuenteInsumo fuente;
        
    @Column(name = "pol_monto", columnDefinition = "Decimal(20,2)")
    private BigDecimal monto;

    @Lob
    @Column(name = "pol_descripcion")
    private String descripcion;

    @Column(name = "pol_prov_nombre")
    private String proveedorNombre;

    @Column(name = "pol_prov_razon_social")
    private String proveedorRazonSocial;

    @Column(name = "pol_prov_nit")
    private String proveedorNIT;

    @Column(name = "pol_prov_direccion")
    private String proveedorDireccion;

    @Column(name = "pol_prov_telefono")
    private String proveedorTelefono;

    @Column(name = "pol_prov_correo")
    private String proveedorCorreo;

    @Enumerated(EnumType.STRING)
    @Column(name = "pol_estado")
    private EstadoPoliza estado;
        
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
        schema = Constantes.SCHEMA_SIAP2,
        name = "ss_rel_pol_impuesto",
        joinColumns = {
            @JoinColumn(name = "pol_id")},
        inverseJoinColumns = {
            @JoinColumn(name = "imp_id")}
    )
    private List<Impuesto> impuestos;
        
    @Column(name = "pol_numero")
    private Integer numeroPoliza;
                    
    @Column(name = "pol_prov_nro_cta_banc")
    private String proveedorNroCuentaBancaria;
    
    @Column(name = "pol_fech_emision")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaEmision;
        
    @OneToMany(mappedBy = "polizaConcentracion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FacturaPolizaConcentracion> facturas;
    
    @ManyToOne
    @JoinColumn(name = "pol_pago_fuente_fcm")
    private POMontoFuenteInsumoFlujoCajaMensual pagoFuenteFCM;
    
    //Auditoria
    @Column(name = "pol_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "pol_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "pol_version")
    @Version
    private Integer version;

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public POMontoFuenteInsumo getFuente() {
        return fuente;
    }

    public void setFuente(POMontoFuenteInsumo fuente) {
        this.fuente = fuente;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getProveedorNombre() {
        return proveedorNombre;
    }

    public void setProveedorNombre(String proveedorNombre) {
        this.proveedorNombre = proveedorNombre;
    }

    public String getProveedorRazonSocial() {
        return proveedorRazonSocial;
    }

    public void setProveedorRazonSocial(String proveedorRazonSocial) {
        this.proveedorRazonSocial = proveedorRazonSocial;
    }

    public String getProveedorNIT() {
        return proveedorNIT;
    }

    public void setProveedorNIT(String proveedorNIT) {
        this.proveedorNIT = proveedorNIT;
    }

    public String getProveedorDireccion() {
        return proveedorDireccion;
    }

    public void setProveedorDireccion(String proveedorDireccion) {
        this.proveedorDireccion = proveedorDireccion;
    }

    public String getProveedorTelefono() {
        return proveedorTelefono;
    }

    public void setProveedorTelefono(String proveedorTelefono) {
        this.proveedorTelefono = proveedorTelefono;
    }

    public String getProveedorCorreo() {
        return proveedorCorreo;
    }

    public void setProveedorCorreo(String proveedorCorreo) {
        this.proveedorCorreo = proveedorCorreo;
    }
    
    public EstadoPoliza getEstado() {
        return estado;
    }

    public void setEstado(EstadoPoliza estado) {
        this.estado = estado;
    }

    public List<Impuesto> getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(List<Impuesto> impuestos) {
        this.impuestos = impuestos;
    }
    
    public String getUltUsuario() {
        return ultUsuario;
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

    public Integer getNumeroPoliza() {
        return numeroPoliza;
    }

    public void setNumeroPoliza(Integer numeroPoliza) {
        this.numeroPoliza = numeroPoliza;
    }

    public String getProveedorNroCuentaBancaria() {
        return proveedorNroCuentaBancaria;
    }

    public void setProveedorNroCuentaBancaria(String nroCuentaBancaria) {
        this.proveedorNroCuentaBancaria = nroCuentaBancaria;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }
    
    public List<FacturaPolizaConcentracion> getFacturas() {
        return facturas;
    }

    public void setFacturas(List<FacturaPolizaConcentracion> facturas) {
        this.facturas = facturas;
    }

    public POMontoFuenteInsumoFlujoCajaMensual getPagoFuenteFCM() {
        return pagoFuenteFCM;
    }

    public void setPagoFuenteFCM(POMontoFuenteInsumoFlujoCajaMensual pagoFuenteFCM) {
        this.pagoFuenteFCM = pagoFuenteFCM;
    }
            
    // </editor-fold>
        
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.id);
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
        final PolizaDeConcentracion other = (PolizaDeConcentracion) obj;
        if ((this.id != null) && (other.id != null)) {
            return Objects.equals(this.id, other.id);
        }

        return (this == obj);
    }

}
