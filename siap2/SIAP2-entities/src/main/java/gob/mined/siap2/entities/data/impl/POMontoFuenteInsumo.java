/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data.impl;

import com.mined.siap2.interfaces.ManejoLineaBaseTrabajo;
import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import gob.mined.siap2.entities.constantes.Constantes;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.persistence.Version;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_po_mont_fuent_i")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class POMontoFuenteInsumo implements Serializable, ManejoLineaBaseTrabajo<POMontoFuenteInsumo> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_pogp_montft_i_gen")
    @SequenceGenerator(name = "seq_pogp_montft_i_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_pogp_montft_i", allocationSize = 1)
    @Column(name = "pog_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "pom_insumo")
    private POInsumos insumo;

    @ManyToOne
    @JoinColumn(name = "pom_fuente")
    private OrigenDistribuccionMontoInsumo fuente;

    @Column(name = "pom_monto", columnDefinition = "Decimal(20,2)")
    private BigDecimal monto;
    
    @Column(name = "pom_certificado", columnDefinition = "Decimal(20,2)")
    private BigDecimal certificado;
    
    
    @Column(name = "pom_reserva_fondos", columnDefinition = "Decimal(20,2)")
    private BigDecimal reservaFondos;
    
    @ManyToOne
    @JoinColumn(name = "pom_cert_disp")
    private Archivo certificadodeDisponibilidadPresupuestaria;
    
    @Column(name = "pom_cert_disp_aprobado")
    private Boolean certificadoDisponibilidadPresupuestariaAprobada;
    
    @ManyToOne
    @JoinColumn(name = "pom_cert_disp_presup")
    private CertificadoDisponibilidadPresupuestaria certificadoDisponibilidadPresupuestaria;
   
    @OneToMany(mappedBy = "montoFuenteInsumo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<POMontoFuenteInsumoFlujoCajaMensual> montosFuentesInsumosFCM;
    
    @Column(name = "pom_monto_descertif", columnDefinition = "Decimal(20,2)")
    private BigDecimal montoDescertificado;
    
    //versionado
    //funcina como una lista doblemente encadenada donde anterior es la línea base y siguiente es la línea de trabajo
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pom_linea_base")
    private POMontoFuenteInsumo lineaBase;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pom_linea_trabajo")
    private POMontoFuenteInsumo lineaTrabajo;
    @Column(name = "ver_fecha_fijacion")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaFijacion;
    
    @Transient
    private BigDecimal totalProgramacionFinanciera;

    //Auditoria
    @Column(name = "pom_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "pom_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "pom_version")
    @Version
    private Integer version;

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public Integer getId() {
        return id;
    }

    public Archivo getCertificadodeDisponibilidadPresupuestaria() {
        return certificadodeDisponibilidadPresupuestaria;
    }

    public void setCertificadodeDisponibilidadPresupuestaria(Archivo certificadodeDisponibilidadPresupuestaria) {
        this.certificadodeDisponibilidadPresupuestaria = certificadodeDisponibilidadPresupuestaria;
    }

    public Boolean getCertificadoDisponibilidadPresupuestariaAprobada() {
        return certificadoDisponibilidadPresupuestariaAprobada;
    }

    public void setCertificadoDisponibilidadPresupuestariaAprobada(Boolean certificadoDisponibilidadPresupuestariaAprobada) {
        this.certificadoDisponibilidadPresupuestariaAprobada = certificadoDisponibilidadPresupuestariaAprobada;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUltUsuario() {
        return ultUsuario;
    }

    public Date getFechaFijacion() {
        return fechaFijacion;
    }

    public void setFechaFijacion(Date fechaFijacion) {
        this.fechaFijacion = fechaFijacion;
    }
    

    public POMontoFuenteInsumo getLineaBase() {
        return lineaBase;
    }

    public void setLineaBase(POMontoFuenteInsumo lineaBase) {
        this.lineaBase = lineaBase;
    }

    public BigDecimal getCertificado() {
        return certificado;
    }

    public void setCertificado(BigDecimal certificado) {
        this.certificado = certificado;
    }

    public POMontoFuenteInsumo getLineaTrabajo() {
        return lineaTrabajo;
    }

    public void setLineaTrabajo(POMontoFuenteInsumo lineaTrabajo) {
        this.lineaTrabajo = lineaTrabajo;
    }

    public OrigenDistribuccionMontoInsumo getFuente() {
        return fuente;
    }

    public void setFuente(OrigenDistribuccionMontoInsumo fuente) {
        this.fuente = fuente;
    }

    public BigDecimal getReservaFondos() {
        return reservaFondos;
    }

    public void setReservaFondos(BigDecimal reservaFondos) {
        this.reservaFondos = reservaFondos;
    }
    
    public POInsumos getInsumo() {
        return insumo;
    }

    public void setInsumo(POInsumos insumo) {
        this.insumo = insumo;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
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
    
    public CertificadoDisponibilidadPresupuestaria getCertificadoDisponibilidadPresupuestaria() {
        return certificadoDisponibilidadPresupuestaria;
    }

    public void setCertificadoDisponibilidadPresupuestaria(CertificadoDisponibilidadPresupuestaria certificadoDisponibilidadPresupuestaria) {
        this.certificadoDisponibilidadPresupuestaria = certificadoDisponibilidadPresupuestaria;
    }
    
    public List<POMontoFuenteInsumoFlujoCajaMensual> getMontosFuentesInsumosFCM() {
        return montosFuentesInsumosFCM;
    }

    public void setMontosFuentesInsumosFCM(List<POMontoFuenteInsumoFlujoCajaMensual> montosFuentesInsumosFCM) {
        this.montosFuentesInsumosFCM = montosFuentesInsumosFCM;
    }
    
    public BigDecimal getTotalProgramacionFinanciera() {
        return totalProgramacionFinanciera;
    }

    public void setTotalProgramacionFinanciera(BigDecimal totalProgramacionFinanciera) {
        this.totalProgramacionFinanciera = totalProgramacionFinanciera;
    }

    public BigDecimal getMontoDescertificado() {
        return montoDescertificado;
    }

    public void setMontoDescertificado(BigDecimal montoDescertificado) {
        this.montoDescertificado = montoDescertificado;
    }
    
    // </editor-fold>
    
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
        final POMontoFuenteInsumo other = (POMontoFuenteInsumo) obj;
        if (this.id!= null && other.id!= null) {
            return Objects.equals(this.id, other.id);
        }
        return this == other;
    }

}
