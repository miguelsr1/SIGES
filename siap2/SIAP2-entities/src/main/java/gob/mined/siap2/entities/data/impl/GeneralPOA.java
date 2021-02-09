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
import gob.mined.siap2.entities.enums.EstadoPOA;
import gob.mined.siap2.entities.enums.TipoPOA;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
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
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_poa_general")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "poa_tipo", discriminatorType = DiscriminatorType.STRING, length = 25)
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public abstract class GeneralPOA implements Serializable, ManejoLineaBaseTrabajo<GeneralPOA> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_poa_general_gen")
    @SequenceGenerator(name = "seq_poa_general_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_poa_general", allocationSize = 1)
    @Column(name = "poa_id")
    protected Integer id;

    // update/insert is managed by discriminator mechanics
    @Column(name = "poa_tipo", insertable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private TipoPOA tipo;

    @Enumerated(EnumType.STRING)
    @Column(name = "poa_estado")
    private EstadoPOA estado;

    @ManyToOne
    @JoinColumn(name = "poa_unidad_tecnica")
    private UnidadTecnica unidadTecnica;

    @ManyToOne
    @JoinColumn(name = "poa_anio_fiscal_id")
    protected AnioFiscal anioFiscal;

    
    @Column(name = "poa_cierre_anual")
    private Boolean cierreAnual;
    
    //versionado
    //funcina como una lista doblemente encadenada donde anterior es la línea base y siguiente es la línea de trabajo
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poa_linea_base")
    protected GeneralPOA lineaBase;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poa_linea_trabajo")
    protected GeneralPOA lineaTrabajo;
    @Column(name = "poa_fecha_fijacion")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    protected Date fechaFijacion;
    

    //Auditoria
    @Column(name = "poa_ult_usuario")
    @AtributoUltimoUsuario
    private String poaUsuario;

    @Column(name = "poa_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date poaMod;

    @Column(name = "poa_version")
    @Version
    private Integer version;
    
    
    public String getNombre() {
        return null;
    }
    

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TipoPOA getTipo() {
        return tipo;
    }

    public void setTipo(TipoPOA tipo) {
        this.tipo = tipo;
    }

    public EstadoPOA getEstado() {
        return estado;
    }

    public AnioFiscal getAnioFiscal() {
        return anioFiscal;
    }

    public void setAnioFiscal(AnioFiscal anioFiscal) {
        this.anioFiscal = anioFiscal;
    }

    public void setEstado(EstadoPOA estado) {
        this.estado = estado;
    }

    public UnidadTecnica getUnidadTecnica() {
        return unidadTecnica;
    }

    public void setUnidadTecnica(UnidadTecnica unidadTecnica) {
        this.unidadTecnica = unidadTecnica;
    }
    
    public Boolean getCierreAnual() {
        return cierreAnual;
    }

   
    public void setCierreAnual(Boolean cierreAnual) {
        this.cierreAnual = cierreAnual;
    }

    @Override
    public GeneralPOA getLineaBase() {
        return lineaBase;
    }

    @Override
    public void setLineaBase(GeneralPOA lineaBase) {
        this.lineaBase = lineaBase;
    }

    @Override
    public GeneralPOA getLineaTrabajo() {
        return lineaTrabajo;
    }

    @Override
    public void setLineaTrabajo(GeneralPOA lineaTrabajo) {
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

    public String getPoaUsuario() {
        return poaUsuario;
    }

    public void setPoaUsuario(String poaUsuario) {
        this.poaUsuario = poaUsuario;
    }

    public Date getPoaMod() {
        return poaMod;
    }

    public void setPoaMod(Date poaMod) {
        this.poaMod = poaMod;
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
        final GeneralPOA other = (GeneralPOA) obj;
        if (this.id != null || other.id != null) {
            return Objects.equals(this.id, other.id);
        }
        return (this == obj);
    }
}
