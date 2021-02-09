/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumEstadoImportado;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_archivos_bit_matriculas", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners({EntidadListener.class})
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "abmPk", scope = SgArchivoBitMatricula.class)
public class SgArchivoBitMatricula implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "abm_pk")
    private Long abmPk;
    
    @JoinColumn(name = "abm_sede_fk", referencedColumnName = "sed_pk")
    @ManyToOne
    private SgSede abmSede;
    
    @Basic(optional = false)
    @Column(name = "abm_dia_ingreso")
    private Integer abmDiaIngreso;
    
    @Basic(optional = false)
    @Column(name = "abm_mes_ingreso")
    private Integer abmMesIngreso;
    
    @Column(name = "abm_dia_egreso")
    private Integer abmDiaEgreso;
    
    @Column(name = "abm_mes_egreso")
    private Integer abmMesEgreso;
    
    @JoinColumn(name = "abm_archivo_fk", referencedColumnName = "ach_pk")
    @ManyToOne(cascade = CascadeType.ALL)
    private SgArchivo abmArchivo;
    
    @Column(name = "abm_estado")
    @Enumerated(value = EnumType.STRING)
    private EnumEstadoImportado abmEstado;
    
    @Size(max = 1000)
    @Column(name = "abm_descripcion", length = 1000)
    private String abmDescripcion; 
    
    @Column(name = "abm_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime abmUltModFecha;
    
    @Size(max = 45)
    @Column(name = "abm_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String abmUltModUsuario;
    
    @Column(name = "abm_version")
    @Version
    private Integer abmVersion;

    public SgArchivoBitMatricula() {
    }

    public SgArchivoBitMatricula(Long abmPk) {
        this.abmPk = abmPk;
    }

    public Long getAbmPk() {
        return abmPk;
    }

    public void setAbmPk(Long abmPk) {
        this.abmPk = abmPk;
    }

    public SgSede getAbmSede() {
        return abmSede;
    }

    public void setAbmSede(SgSede abmSede) {
        this.abmSede = abmSede;
    }

    public Integer getAbmDiaIngreso() {
        return abmDiaIngreso;
    }

    public void setAbmDiaIngreso(Integer abmDiaIngreso) {
        this.abmDiaIngreso = abmDiaIngreso;
    }

    public Integer getAbmMesIngreso() {
        return abmMesIngreso;
    }

    public void setAbmMesIngreso(Integer abmMesIngreso) {
        this.abmMesIngreso = abmMesIngreso;
    }

    public Integer getAbmDiaEgreso() {
        return abmDiaEgreso;
    }

    public void setAbmDiaEgreso(Integer abmDiaEgreso) {
        this.abmDiaEgreso = abmDiaEgreso;
    }

    public Integer getAbmMesEgreso() {
        return abmMesEgreso;
    }

    public void setAbmMesEgreso(Integer abmMesEgreso) {
        this.abmMesEgreso = abmMesEgreso;
    }

    public SgArchivo getAbmArchivo() {
        return abmArchivo;
    }

    public void setAbmArchivo(SgArchivo abmArchivo) {
        this.abmArchivo = abmArchivo;
    }

    public EnumEstadoImportado getAbmEstado() {
        return abmEstado;
    }

    public void setAbmEstado(EnumEstadoImportado abmEstado) {
        this.abmEstado = abmEstado;
    }

    public String getAbmDescripcion() {
        return abmDescripcion;
    }

    public void setAbmDescripcion(String abmDescripcion) {
        this.abmDescripcion = abmDescripcion;
    }

    public LocalDateTime getAbmUltModFecha() {
        return abmUltModFecha;
    }

    public void setAbmUltModFecha(LocalDateTime abmUltModFecha) {
        this.abmUltModFecha = abmUltModFecha;
    }

    public String getAbmUltModUsuario() {
        return abmUltModUsuario;
    }

    public void setAbmUltModUsuario(String abmUltModUsuario) {
        this.abmUltModUsuario = abmUltModUsuario;
    }

    public Integer getAbmVersion() {
        return abmVersion;
    }

    public void setAbmVersion(Integer abmVersion) {
        this.abmVersion = abmVersion;
    }
    


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (abmPk != null ? abmPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgArchivoBitMatricula)) {
            return false;
        }
        SgArchivoBitMatricula other = (SgArchivoBitMatricula) object;
        if ((this.abmPk == null && other.abmPk != null) || (this.abmPk != null && !this.abmPk.equals(other.abmPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgArchivoBitMatricula[ abmPk=" + abmPk + " ]";
    }
    
}
