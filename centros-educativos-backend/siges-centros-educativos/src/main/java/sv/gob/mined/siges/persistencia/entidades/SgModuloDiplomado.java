/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.enumerados.EnumCalculoNotaInstitucional;
import sv.gob.mined.siges.enumerados.EnumFuncionRedondeo;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgEscalaCalificacion;

@Entity
@Table(name = "sg_modulos_diplomado", uniqueConstraints = {
    @UniqueConstraint(name = "mdi_codigo_uk", columnNames = {"mdi_codigo"})}, schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "mdiPk", scope = SgModuloDiplomado.class)
@Audited
public class SgModuloDiplomado implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "mdi_pk")
    private Long mdiPk;
    
    @Size(max = 10)
    @Column(name = "mdi_codigo",length = 10)
    @AtributoCodigo
    private String mdiCodigo;
    
    @Size(max = 255)
    @Column(name = "mdi_nombre",length = 255)
    @AtributoNombre
    private String mdiNombre;
    
    @Size(max = 500)
    @Column(name = "mdi_descripcion",length = 500)
    private String mdiDescripcion;
    
    @JoinColumn(name = "mdi_diplomado_fk", referencedColumnName = "dip_pk")
    @ManyToOne
    private SgDiplomado mdiDiplomado;
    
    @JoinColumn(name = "mdi_escala_calificacion_fk", referencedColumnName = "eca_pk")
    @ManyToOne
    private SgEscalaCalificacion mdiEscalaCalificacion;  
    
    @Column(name = "mdi_calculo_nota_institucional")
    @Enumerated(value = EnumType.STRING)
    private EnumCalculoNotaInstitucional mdiCalculoNotaInstitucional;
    
    @Column(name = "mdi_funcion_redondeo")
    @Enumerated(value = EnumType.STRING)
    private EnumFuncionRedondeo mdiFuncionRedondeo;
    
    @Column(name = "mdi_precision")
    private Integer mdiPrecision;
    
    @Column(name = "mdi_periodos_calificacion")
    private Integer mdiPeriodosCalificacion;
    
    @Column(name = "mdi_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime mdiUltModFecha;
    
    @Size(max = 45)
    @Column(name = "mdi_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String mdiUltModUsuario;
    
    @Column(name = "mdi_version")
    @Version
    private Integer mdiVersion;
    
    

    public SgModuloDiplomado() {
    }

    public SgModuloDiplomado(Long mdiPk) {
        this.mdiPk = mdiPk;
    }

    public Long getMdiPk() {
        return mdiPk;
    }

    public void setMdiPk(Long mdiPk) {
        this.mdiPk = mdiPk;
    }

    public String getMdiCodigo() {
        return mdiCodigo;
    }

    public void setMdiCodigo(String mdiCodigo) {
        this.mdiCodigo = mdiCodigo;
    }

    public String getMdiNombre() {
        return mdiNombre;
    }

    public void setMdiNombre(String mdiNombre) {
        this.mdiNombre = mdiNombre;
    }

    public String getMdiDescripcion() {
        return mdiDescripcion;
    }

    public void setMdiDescripcion(String mdiDescripcion) {
        this.mdiDescripcion = mdiDescripcion;
    }

    public SgDiplomado getMdiDiplomado() {
        return mdiDiplomado;
    }

    public void setMdiDiplomado(SgDiplomado mdiDiplomado) {
        this.mdiDiplomado = mdiDiplomado;
    }

    public LocalDateTime getMdiUltModFecha() {
        return mdiUltModFecha;
    }

    public void setMdiUltModFecha(LocalDateTime mdiUltModFecha) {
        this.mdiUltModFecha = mdiUltModFecha;
    }

    public String getMdiUltModUsuario() {
        return mdiUltModUsuario;
    }

    public void setMdiUltModUsuario(String mdiUltModUsuario) {
        this.mdiUltModUsuario = mdiUltModUsuario;
    }

    public Integer getMdiVersion() {
        return mdiVersion;
    }

    public void setMdiVersion(Integer mdiVersion) {
        this.mdiVersion = mdiVersion;
    }

    public SgEscalaCalificacion getMdiEscalaCalificacion() {
        return mdiEscalaCalificacion;
    }

    public void setMdiEscalaCalificacion(SgEscalaCalificacion mdiEscalaCalificacion) {
        this.mdiEscalaCalificacion = mdiEscalaCalificacion;
    }

    public EnumCalculoNotaInstitucional getMdiCalculoNotaInstitucional() {
        return mdiCalculoNotaInstitucional;
    }

    public void setMdiCalculoNotaInstitucional(EnumCalculoNotaInstitucional mdiCalculoNotaInstitucional) {
        this.mdiCalculoNotaInstitucional = mdiCalculoNotaInstitucional;
    }

    public EnumFuncionRedondeo getMdiFuncionRedondeo() {
        return mdiFuncionRedondeo;
    }

    public void setMdiFuncionRedondeo(EnumFuncionRedondeo mdiFuncionRedondeo) {
        this.mdiFuncionRedondeo = mdiFuncionRedondeo;
    }

    public Integer getMdiPrecision() {
        return mdiPrecision;
    }

    public void setMdiPrecision(Integer mdiPrecision) {
        this.mdiPrecision = mdiPrecision;
    }

    public Integer getMdiPeriodosCalificacion() {
        return mdiPeriodosCalificacion;
    }

    public void setMdiPeriodosCalificacion(Integer mdiPeriodosCalificacion) {
        this.mdiPeriodosCalificacion = mdiPeriodosCalificacion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mdiPk != null ? mdiPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgModuloDiplomado)) {
            return false;
        }
        SgModuloDiplomado other = (SgModuloDiplomado) object;
        if ((this.mdiPk == null && other.mdiPk != null) || (this.mdiPk != null && !this.mdiPk.equals(other.mdiPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgModuloDiplomado[ mdiPk=" + mdiPk + " ]";
    }
    
}
