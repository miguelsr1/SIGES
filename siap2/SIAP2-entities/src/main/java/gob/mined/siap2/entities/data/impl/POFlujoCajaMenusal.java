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
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_po_fluj_caj")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class POFlujoCajaMenusal implements Serializable, ManejoLineaBaseTrabajo<POFlujoCajaMenusal> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_pog_p_fluj_caj_gen")
    @SequenceGenerator(name = "seq_pog_p_fluj_caj_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_pog_p_fluj_caj", allocationSize = 1)
    @Column(name = "pof_id")
    private Integer id;

    @Column(name = "pof_mes")
    private Integer mes;

    @Column(name = "pof_monto", columnDefinition = "Decimal(20,2)")
    private BigDecimal monto;

    //versionado
    //funcina como una lista doblemente encadenada donde anterior es la línea base y siguiente es la línea de trabajo
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pof_linea_base")
    private POFlujoCajaMenusal lineaBase;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pof_linea_trabajo")
    private POFlujoCajaMenusal lineaTrabajo;
    @Column(name = "pog_fecha_fijacion")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaFijacion;

    @OneToMany(mappedBy = "mesPago", cascade = CascadeType.ALL)
    private List<ActaContrato> actas;
    
    @OneToMany(mappedBy = "flujoCajaMensual", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<POMontoFuenteInsumoFlujoCajaMensual> montosFuentesInsumosFCM;
    
    //Auditoria
    @Column(name = "pof_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "pof_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "pof_version")
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

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public POFlujoCajaMenusal getLineaBase() {
        return lineaBase;
    }

    public void setLineaBase(POFlujoCajaMenusal lineaBase) {
        this.lineaBase = lineaBase;
    }

    public Date getFechaFijacion() {
        return fechaFijacion;
    }

    public void setFechaFijacion(Date fechaFijacion) {
        this.fechaFijacion = fechaFijacion;
    }

    public POFlujoCajaMenusal getLineaTrabajo() {
        return lineaTrabajo;
    }

    public void setLineaTrabajo(POFlujoCajaMenusal lineaTrabajo) {
        this.lineaTrabajo = lineaTrabajo;
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
    
    public List<ActaContrato> getActas() {
        return actas;
    }

    public void setActas(List<ActaContrato> actas) {
        this.actas = actas;
    }

    public List<POMontoFuenteInsumoFlujoCajaMensual> getMontosFuentesInsumosFCM() {
        return montosFuentesInsumosFCM;
    }

    public void setMontosFuentesInsumosFCM(List<POMontoFuenteInsumoFlujoCajaMensual> montosFuentesInsumosFCM) {
        this.montosFuentesInsumosFCM = montosFuentesInsumosFCM;
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
        final POFlujoCajaMenusal other = (POFlujoCajaMenusal) obj;
        if (this.id != null && other.id != null) {
            return this.id.equals(other.id);
        }
        return this == other;
    }

}
