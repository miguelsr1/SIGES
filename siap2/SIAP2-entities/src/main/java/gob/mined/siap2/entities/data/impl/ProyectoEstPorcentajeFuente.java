/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_proy_est_por_fuente")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class ProyectoEstPorcentajeFuente implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_proy_est_mont_fuente_gen")
    @SequenceGenerator(name = "seq_proy_est_mont_fuente_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_proy_est_mont_fuente", allocationSize = 1)
    @Column(name = "pro_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "pro_proy_estruc")
    private ProyectoEstructura proyectoEstructura;

    @ManyToOne
    @JoinColumn(name = "pro_fuente")
    private ProyectoAporte fuente;

    @Column(name = "pro_monto", columnDefinition = "Decimal(20,2)")
    private BigDecimal monto;
    @Column(name = "pro_temp_monto", columnDefinition = "Decimal(20,2)")
    private BigDecimal montoEnConstruccion;
    
    @Column(name = "pro_posicion")
    protected Integer posicion;

    //Auditoria
    @Column(name = "cat_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "cat_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "cat_version")
    @Version
    private Integer version;

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUltUsuario() {
        return ultUsuario;
    }

    public ProyectoAporte getFuente() {
        return fuente;
    }

    public void setFuente(ProyectoAporte fuente) {
        this.fuente = fuente;
    }

    public ProyectoEstructura getProyectoEstructura() {
        return proyectoEstructura;
    }

    public void setProyectoEstructura(ProyectoEstructura proyectoEstructura) {
        this.proyectoEstructura = proyectoEstructura;
    }

    public Integer getPosicion() {
        return posicion;
    }

    public void setPosicion(Integer posicion) {
        this.posicion = posicion;
    }

    public BigDecimal getMontoEnConstruccion() {
        return montoEnConstruccion;
    }

    public void setMontoEnConstruccion(BigDecimal montoEnConstruccion) {
        this.montoEnConstruccion = montoEnConstruccion;
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
        final ProyectoEstPorcentajeFuente other = (ProyectoEstPorcentajeFuente) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
