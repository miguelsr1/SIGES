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
import gob.mined.siap2.entities.enums.OrigenRiesgo;
import gob.mined.siap2.entities.enums.ValoracionRiesgo;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
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
import javax.persistence.Lob;
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
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_poa_riesgos_poa")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class POARiesgoPOA implements Serializable, ManejoLineaBaseTrabajo<POARiesgoPOA> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_poa_riesgos_poa_gen")
    @SequenceGenerator(name = "seq_poa_riesgos_poa_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_poa_riesgos_poa", allocationSize = 1)
    @Column(name = "prp_id")
    private Integer id;

    @Column(name = "prp_riesgo")
    private String riesgo;

    @Enumerated(EnumType.STRING)
    @Column(name = "prp_origen")
    private OrigenRiesgo origen;

    @Enumerated(EnumType.STRING)
    @Column(name = "prp_val_riesg")
    private ValoracionRiesgo valoracionDelRiesgo;

    @Lob
    @Column(name = "prp_acc_mitigacion")
    private String accionesDeMitigacion;

    @Lob
    @Column(name = "prp_acc_conting")
    private String accionesDeContingencia;

    @Column(name = "prp_responsable")
    private String nombreFuncionarioResponsable;

    //versionado
    //funciona como una lista doblemente encadenada donde anterior es la línea base y siguiente es la línea de trabajo
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prp_linea_base")
    private POARiesgoPOA lineaBase;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prp_linea_trabajo")
    private POARiesgoPOA lineaTrabajo;
    @Column(name = "prp_fecha_fijacion")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaFijacion;

    //Auditoria
    @Column(name = "prp_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "prp_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "prp_version")
    @Version
    private Integer version;

   
    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRiesgo() {
        return riesgo;
    }

    public void setRiesgo(String riesgo) {
        this.riesgo = riesgo;
    }

    public OrigenRiesgo getOrigen() {
        return origen;
    }

    public void setOrigen(OrigenRiesgo origen) {
        this.origen = origen;
    }

    public ValoracionRiesgo getValoracionDelRiesgo() {
        return valoracionDelRiesgo;
    }

    @Override
    public POARiesgoPOA getLineaBase() {
        return lineaBase;
    }

    @Override
    public void setLineaBase(POARiesgoPOA lineaBase) {
        this.lineaBase = lineaBase;
    }

    @Override
    public POARiesgoPOA getLineaTrabajo() {
        return lineaTrabajo;
    }

    @Override
    public void setLineaTrabajo(POARiesgoPOA lineaTrabajo) {
        this.lineaTrabajo = lineaTrabajo;
    }

    @Override
    public Date getFechaFijacion() {
        return fechaFijacion;
    }

    @Override
    public void setFechaFijacion(Date fechaFijacion) {
        this.fechaFijacion = fechaFijacion;
    }

    public void setValoracionDelRiesgo(ValoracionRiesgo valoracionDelRiesgo) {
        this.valoracionDelRiesgo = valoracionDelRiesgo;
    }

    public String getAccionesDeMitigacion() {
        return accionesDeMitigacion;
    }

    public void setAccionesDeMitigacion(String accionesDeMitigacion) {
        this.accionesDeMitigacion = accionesDeMitigacion;
    }

    public String getAccionesDeContingencia() {
        return accionesDeContingencia;
    }

    public void setAccionesDeContingencia(String accionesDeContingencia) {
        this.accionesDeContingencia = accionesDeContingencia;
    }

    public String getNombreFuncionarioResponsable() {
        return nombreFuncionarioResponsable;
    }

    public void setNombreFuncionarioResponsable(String nombreFuncionarioResponsable) {
        this.nombreFuncionarioResponsable = nombreFuncionarioResponsable;
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
        final POARiesgoPOA other = (POARiesgoPOA) obj;
        if (this.id != null) {
            return Objects.equals(this.id, other.id);
        }
        return (this == obj);
    }

}