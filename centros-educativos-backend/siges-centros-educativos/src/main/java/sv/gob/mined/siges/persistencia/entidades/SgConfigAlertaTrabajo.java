/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Basic;
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumCategoriaAlertaTrabajo;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgTipoTrabajo;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_config_alerta_trabajo", schema = Constantes.SCHEMA_ALERTAS)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "cnfPk", scope = SgConfigAlertaTrabajo.class)
public class SgConfigAlertaTrabajo implements Serializable {
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cnf_pk")
    private Long cnfPk;
    
    @JoinColumn(name = "cnf_cabezal")
    @ManyToOne
    private SgConfigAlerta cnfCabezal;
    
    @Column(name = "cnf_categoria_trabajo")
    @Enumerated(EnumType.STRING)
    private EnumCategoriaAlertaTrabajo cnfCategoria;
    
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="sg_cnf_trabajo_r_muy_alto",
            joinColumns = @JoinColumn( name="cnf_pk"),
            inverseJoinColumns = @JoinColumn( name="ttr_pk"),
            schema = Constantes.SCHEMA_ALERTAS
    )
    @Fetch(FetchMode.SUBSELECT)
    private List<SgTipoTrabajo> cnfRiesgoMuyAlto;
    
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="sg_cnf_trabajo_r_alto",
            joinColumns = @JoinColumn( name="cnf_pk"),
            inverseJoinColumns = @JoinColumn( name="ttr_pk"),
            schema = Constantes.SCHEMA_ALERTAS
    )
    @Fetch(FetchMode.SUBSELECT)
    private List<SgTipoTrabajo> cnfRiesgoAlto;
    
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="sg_cnf_trabajo_r_medio",
            joinColumns = @JoinColumn( name="cnf_pk"),
            inverseJoinColumns = @JoinColumn( name="ttr_pk"),
            schema = Constantes.SCHEMA_ALERTAS
    )
    @Fetch(FetchMode.SUBSELECT)
    private List<SgTipoTrabajo> cnfRiesgoMedio;
    
    @Column(name = "cnf_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime cnfUltModFecha;

    @Size(max = 45)
    @Column(name = "cnf_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String cnfUltModUsuario;
    
    @Column(name = "cnf_version")
    @Version
    private Integer cnfVersion;
    

    public EnumCategoriaAlertaTrabajo getCnfCategoria() {
        return cnfCategoria;
    }

    public void setCnfCategoria(EnumCategoriaAlertaTrabajo cnfCategoria) {
        this.cnfCategoria = cnfCategoria;
    }

    public Long getCnfPk() {
        return cnfPk;
    }

    public void setCnfPk(Long cnfPk) {
        this.cnfPk = cnfPk;
    }

    public SgConfigAlerta getCnfCabezal() {
        return cnfCabezal;
    }

    public void setCnfCabezal(SgConfigAlerta cnfCabezal) {
        this.cnfCabezal = cnfCabezal;
    }

    public List<SgTipoTrabajo> getCnfRiesgoMuyAlto() {
        return cnfRiesgoMuyAlto;
    }

    public void setCnfRiesgoMuyAlto(List<SgTipoTrabajo> cnfRiesgoMuyAlto) {
        this.cnfRiesgoMuyAlto = cnfRiesgoMuyAlto;
    }

    public List<SgTipoTrabajo> getCnfRiesgoAlto() {
        return cnfRiesgoAlto;
    }

    public void setCnfRiesgoAlto(List<SgTipoTrabajo> cnfRiesgoAlto) {
        this.cnfRiesgoAlto = cnfRiesgoAlto;
    }

    public List<SgTipoTrabajo> getCnfRiesgoMedio() {
        return cnfRiesgoMedio;
    }

    public void setCnfRiesgoMedio(List<SgTipoTrabajo> cnfRiesgoMedio) {
        this.cnfRiesgoMedio = cnfRiesgoMedio;
    }

    public LocalDateTime getCnfUltModFecha() {
        return cnfUltModFecha;
    }

    public void setCnfUltModFecha(LocalDateTime cnfUltModFecha) {
        this.cnfUltModFecha = cnfUltModFecha;
    }

    public String getCnfUltModUsuario() {
        return cnfUltModUsuario;
    }

    public void setCnfUltModUsuario(String cnfUltModUsuario) {
        this.cnfUltModUsuario = cnfUltModUsuario;
    }

    public Integer getCnfVersion() {
        return cnfVersion;
    }

    public void setCnfVersion(Integer cnfVersion) {
        this.cnfVersion = cnfVersion;
    }

   

}
