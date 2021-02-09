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
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_po_val_seg")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class ValoresSeguimiento implements Serializable, ManejoLineaBaseTrabajo<ValoresSeguimiento> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_pog_p_val_seg_gen")
    @SequenceGenerator(name = "seq_pog_p_val_seg_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_pog_p_val_seg", allocationSize = 1)
    @Column(name = "pog_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "pog_t_seg")
    private ComboValorSeguimiento tipoValorSeguimiento;

    @Column(name = "pog_valor", columnDefinition = "Decimal(20,2)")
    private BigDecimal valor;

    @Column(name = "pog_posicion")
    private Integer posicion;

    
    @Column(name = "posicion")
    private Integer posicionjpa;
    //versionado
    //funcina como una lista doblemente encadenada donde anterior es la línea base y siguiente es la línea de trabajo
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pog_linea_base")
    private ValoresSeguimiento lineaBase;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pog_linea_trabajo")
    private ValoresSeguimiento lineaTrabajo;
    @Column(name = "pog_fecha_fijacion")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaFijacion;

    //Auditoria
    @Column(name = "pog_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "pog_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "pog_version")
    @Version
    private Integer version;

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public ValoresSeguimiento getLineaBase() {
        return lineaBase;
    }
    

    public void setLineaBase(ValoresSeguimiento lineaBase) {
        this.lineaBase = lineaBase;
    }

    public Date getFechaFijacion() {
        return fechaFijacion;
    }

    public void setFechaFijacion(Date fechaFijacion) {
        this.fechaFijacion = fechaFijacion;
    }

    public Integer getPosicionjpa() {
        return posicionjpa;
    }

    public void setPosicionjpa(Integer posicionjpa) {
        this.posicionjpa = posicionjpa;
    }

    public ValoresSeguimiento getLineaTrabajo() {
        return lineaTrabajo;
    }
    

    public void setLineaTrabajo(ValoresSeguimiento lineaTrabajo) {
        this.lineaTrabajo = lineaTrabajo;
    }

    public Integer getPosicion() {
        return posicion;
    }

    public void setPosicion(Integer posicion) {
        this.posicion = posicion;
    }

    public ComboValorSeguimiento getTipoValorSeguimiento() {
        return tipoValorSeguimiento;
    }

    public void setTipoValorSeguimiento(ComboValorSeguimiento tipoValorSeguimiento) {
        this.tipoValorSeguimiento = tipoValorSeguimiento;
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
        final ValoresSeguimiento other = (ValoresSeguimiento) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
