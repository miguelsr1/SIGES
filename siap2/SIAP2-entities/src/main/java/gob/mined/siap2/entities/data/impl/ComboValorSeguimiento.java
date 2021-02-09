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
import gob.mined.siap2.entities.enums.TipoSeguimiento;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
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
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_po_com_val_seg")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class ComboValorSeguimiento implements Serializable, ManejoLineaBaseTrabajo<ComboValorSeguimiento> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_pogp_cvalseg_gen")
    @SequenceGenerator(name = "seq_pogp_cvalseg_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_pogp_cvalseg", allocationSize = 1)
    @Column(name = "pog_id")
    private Integer id;

    @Column(name = "pog_anio")
    private Integer anio;

    @Enumerated(EnumType.STRING)
    @Column(name = "pog_tipo_seg")
    private TipoSeguimiento tipoSeguimiento;

    @OneToMany(mappedBy = "tipoValorSeguimiento", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "posicion")
    private List<ValoresSeguimiento> valores;

    //versionado
    //funcina como una lista doblemente encadenada donde anterior es la línea base y siguiente es la línea de trabajo
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pog_linea_base")
    private ComboValorSeguimiento lineaBase;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pog_linea_trabajo")
    private ComboValorSeguimiento lineaTrabajo;
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

    public ComboValorSeguimiento getLineaBase() {
        return lineaBase;
    }

    public void setLineaBase(ComboValorSeguimiento lineaBase) {
        this.lineaBase = lineaBase;
    }

    public ComboValorSeguimiento getLineaTrabajo() {
        return lineaTrabajo;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Date getFechaFijacion() {
        return fechaFijacion;
    }

    public void setFechaFijacion(Date fechaFijacion) {
        this.fechaFijacion = fechaFijacion;
    }

    public void setLineaTrabajo(ComboValorSeguimiento lineaTrabajo) {
        this.lineaTrabajo = lineaTrabajo;
    }

    public List<ValoresSeguimiento> getValores() {
        return valores;
    }

    public void setValores(List<ValoresSeguimiento> valores) {
        this.valores = valores;
    }

    public TipoSeguimiento getTipoSeguimiento() {
        return tipoSeguimiento;
    }

    public void setTipoSeguimiento(TipoSeguimiento tipoSeguimiento) {
        this.tipoSeguimiento = tipoSeguimiento;
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
        final ComboValorSeguimiento other = (ComboValorSeguimiento) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
