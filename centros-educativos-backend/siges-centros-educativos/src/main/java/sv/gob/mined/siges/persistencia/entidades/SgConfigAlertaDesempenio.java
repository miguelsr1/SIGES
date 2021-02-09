/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_config_alerta_desempenio", schema = Constantes.SCHEMA_ALERTAS)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "cnfPk", scope = SgConfigAlertaDesempenio.class)
public class SgConfigAlertaDesempenio implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cnf_pk")
    private Long cnfPk;
    
    @JoinColumn(name = "cnf_cabezal")
    @ManyToOne
    private SgConfigAlerta cnfCabezal;
    
    @JoinColumn(name = "cnf_ciclo_fk")
    @OneToOne
    private SgCiclo cnfCategoria;
    
    @Column(name = "cnf_riesgo_muy_alto")
    private Integer cnfRiesgoMuyAlto;
    
    @Column(name = "cnf_riesgo_alto")
    private Integer cnfRiesgoAlto;
    
    @Column(name = "cnf_riesgo_medio")
    private Integer cnfRiesgoMedio;
    
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

    public SgConfigAlertaDesempenio() {
    }

    public SgConfigAlerta getCnfCabezal() {
        return cnfCabezal;
    }

    public void setCnfCabezal(SgConfigAlerta cnfCabezal) {
        this.cnfCabezal = cnfCabezal;
    }

    public SgCiclo getCnfCategoria() {
        return cnfCategoria;
    }

    public void setCnfCategoria(SgCiclo cnfCategoria) {
        this.cnfCategoria = cnfCategoria;
    }

    public Integer getCnfRiesgoMuyAlto() {
        return cnfRiesgoMuyAlto;
    }

    public void setCnfRiesgoMuyAlto(Integer cnfRiesgoMuyAlto) {
        this.cnfRiesgoMuyAlto = cnfRiesgoMuyAlto;
    }

    public Integer getCnfRiesgoAlto() {
        return cnfRiesgoAlto;
    }

    public void setCnfRiesgoAlto(Integer cnfRiesgoAlto) {
        this.cnfRiesgoAlto = cnfRiesgoAlto;
    }

    public Integer getCnfRiesgoMedio() {
        return cnfRiesgoMedio;
    }

    public void setCnfRiesgoMedio(Integer cnfRiesgoMedio) {
        this.cnfRiesgoMedio = cnfRiesgoMedio;
    }

    public Long getCnfPk() {
        return cnfPk;
    }

    public void setCnfPk(Long cnfPk) {
        this.cnfPk = cnfPk;
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
