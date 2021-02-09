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
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_pog_mont_fuent_i")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class POGMontoFuenteInsumo implements Serializable, ManejoLineaBaseTrabajo<POGMontoFuenteInsumo> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_pog_montft_i_gen")
    @SequenceGenerator(name = "seq_pog_montft_i_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_pog_montft_i", allocationSize = 1)
    @Column(name = "pog_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "pog_ins_en_anio")
    private POGInsumoAnio insumo;

    @ManyToOne
    @JoinColumn(name = "pog_fuente")
    private ProyectoAporte fuente;

    @Column(name = "pog_monto", columnDefinition = "Decimal(20,2)")
    private BigDecimal monto;

    //versionado
    //funcina como una lista doblemente encadenada donde anterior es la línea base y siguiente es la línea de trabajo
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ver_linea_base")
    private POGMontoFuenteInsumo lineaBase;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ver_linea_trabajo")
    private POGMontoFuenteInsumo lineaTrabajo;
    @Column(name = "ver_fecha_fijacion")
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

    public String getUltUsuario() {
        return ultUsuario;
    }

    public Date getFechaFijacion() {
        return fechaFijacion;
    }

    public void setFechaFijacion(Date fechaFijacion) {
        this.fechaFijacion = fechaFijacion;
    }

    public POGMontoFuenteInsumo getLineaBase() {
        return lineaBase;
    }

    public void setLineaBase(POGMontoFuenteInsumo lineaBase) {
        this.lineaBase = lineaBase;
    }

    public POGMontoFuenteInsumo getLineaTrabajo() {
        return lineaTrabajo;
    }

    public void setLineaTrabajo(POGMontoFuenteInsumo lineaTrabajo) {
        this.lineaTrabajo = lineaTrabajo;
    }

    public ProyectoAporte getFuente() {
        return fuente;
    }

    public void setFuente(ProyectoAporte fuente) {
        this.fuente = fuente;
    }

    public POGInsumoAnio getInsumo() {
        return insumo;
    }

    public void setInsumo(POGInsumoAnio insumo) {
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
        final POGMontoFuenteInsumo other = (POGMontoFuenteInsumo) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
