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
import javax.persistence.Version;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_po_mont_fuent_i_fcm")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class POMontoFuenteInsumoFlujoCajaMensual implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_po_montft_i_fcm_gen")
    @SequenceGenerator(name = "seq_po_montft_i_fcm_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_po_montft_i_fcm_gen", allocationSize = 1)
    
    @Column(name = "pom_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "pom_mon_fue_ins")
    private POMontoFuenteInsumo montoFuenteInsumo;
    
    @ManyToOne
    @JoinColumn(name = "pom_flu_caj_men")
    private POFlujoCajaMenusal flujoCajaMensual;

    @Column(name = "pom_monto", columnDefinition = "Decimal(20,2)")
    private BigDecimal monto;  
    
    @Column(name = "pom_monto_cert", columnDefinition = "Decimal(20,2)")
    private BigDecimal montoCertificado;  
    
    @OneToMany(mappedBy = "pagoFuenteFCM", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PolizaDeConcentracion> polizasConcentracion;
    
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
    
    public void setId(Integer id) {
        this.id = id;
    }

    public POMontoFuenteInsumo getMontoFuenteInsumo() {
        return montoFuenteInsumo;
    }

    public void setMontoFuenteInsumo(POMontoFuenteInsumo montoFuenteInsumo) {
        this.montoFuenteInsumo = montoFuenteInsumo;
    }

    public POFlujoCajaMenusal getFlujoCajaMensual() {
        return flujoCajaMensual;
    }

    public void setFlujoCajaMensual(POFlujoCajaMenusal flujoCajaMensual) {
        this.flujoCajaMensual = flujoCajaMensual;
    }
        
    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
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

    public BigDecimal getMontoCertificado() {
        return montoCertificado;
    }

    public void setMontoCertificado(BigDecimal montoCertificado) {
        this.montoCertificado = montoCertificado;
    }

    public List<PolizaDeConcentracion> getPolizasConcentracion() {
        return polizasConcentracion;
    }

    public void setPolizasConcentracion(List<PolizaDeConcentracion> polizasConcentracion) {
        this.polizasConcentracion = polizasConcentracion;
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
        final POMontoFuenteInsumoFlujoCajaMensual other = (POMontoFuenteInsumoFlujoCajaMensual) obj;
        if (this.id!= null && other.id!= null) {
            return Objects.equals(this.id, other.id);
        }
        return this == other;
    }

}
