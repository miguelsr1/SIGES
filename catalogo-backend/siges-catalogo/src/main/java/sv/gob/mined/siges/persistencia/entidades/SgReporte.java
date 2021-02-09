/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.UniqueConstraint;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_reportes", schema = Constantes.SCHEMA_CATALOGO, uniqueConstraints = {
    @UniqueConstraint(name = "rep_codigo_uk", columnNames = {"rep_codigo"})
    ,
    @UniqueConstraint(name = "rep_nombre_uk", columnNames = {"rep_nombre"})})
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "repPk", scope = SgReporte.class)
@Audited
public class SgReporte implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "rep_pk", nullable = false)
    private Long repPk;

    @Size(max = 45)
    @Column(name = "rep_codigo", length = 45)
    @AtributoCodigo
    private String repCodigo;

    @Size(max = 255)
    @Column(name = "rep_nombre", length = 255)
    @AtributoNormalizable
    private String repNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "rep_nombre_busqueda", length = 255)
    private String repNombreBusqueda;
    
    @Size(max = 1000)
    @Column(name = "rep_descripcion", length = 1000)
    private String repDescripcion;
    
    @Size(max = 100)
    @Column(name = "rep_operacion_asociada", length = 100)
    private String repOperacionAsociada;
    
    @OneToOne
    @JoinColumn(name = "rep_plantilla_fk")
    private SgPlantilla repPlantilla;
    

    @Column(name = "rep_habilitado")
    @AtributoHabilitado
    private Boolean repHabilitado;

    @Column(name = "rep_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime repUltModFecha;

    @Size(max = 45)
    @Column(name = "rep_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String repUltModUsuario;

    @Column(name = "rep_version")
    @Version
    private Integer repVersion;

    public SgReporte() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.repNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.repNombre);
    }

    public Long getRepPk() {
        return repPk;
    }

    public void setRepPk(Long repPk) {
        this.repPk = repPk;
    }

    public String getRepCodigo() {
        return repCodigo;
    }

    public void setRepCodigo(String repCodigo) {
        this.repCodigo = repCodigo;
    }

    public String getRepNombre() {
        return repNombre;
    }

    public void setRepNombre(String repNombre) {
        this.repNombre = repNombre;
    }

    public String getRepNombreBusqueda() {
        return repNombreBusqueda;
    }

    public void setRepNombreBusqueda(String repNombreBusqueda) {
        this.repNombreBusqueda = repNombreBusqueda;
    }

    public Boolean getRepHabilitado() {
        return repHabilitado;
    }

    public void setRepHabilitado(Boolean repHabilitado) {
        this.repHabilitado = repHabilitado;
    }

    public LocalDateTime getRepUltModFecha() {
        return repUltModFecha;
    }

    public void setRepUltModFecha(LocalDateTime repUltModFecha) {
        this.repUltModFecha = repUltModFecha;
    }

    public String getRepUltModUsuario() {
        return repUltModUsuario;
    }

    public void setRepUltModUsuario(String repUltModUsuario) {
        this.repUltModUsuario = repUltModUsuario;
    }

    public Integer getRepVersion() {
        return repVersion;
    }

    public void setRepVersion(Integer repVersion) {
        this.repVersion = repVersion;
    }

    public String getRepDescripcion() {
        return repDescripcion;
    }

    public void setRepDescripcion(String repDescripcion) {
        this.repDescripcion = repDescripcion;
    }

    public String getRepOperacionAsociada() {
        return repOperacionAsociada;
    }

    public void setRepOperacionAsociada(String repOperacionAsociada) {
        this.repOperacionAsociada = repOperacionAsociada;
    }

    public SgPlantilla getRepPlantilla() {
        return repPlantilla;
    }

    public void setRepPlantilla(SgPlantilla repPlantilla) {
        this.repPlantilla = repPlantilla;
    }
      
    @Override
    public int hashCode() {
        return Objects.hashCode(this.repPk);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SgReporte other = (SgReporte) obj;
        if (!Objects.equals(this.repPk, other.repPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgReporte{" + "repPk=" + repPk + ", repCodigo=" + repCodigo + ", repNombre=" + repNombre + ", repNombreBusqueda=" + repNombreBusqueda + ", repHabilitado=" + repHabilitado + ", repUltModFecha=" + repUltModFecha + ", repUltModUsuario=" + repUltModUsuario + ", repVersion=" + repVersion + '}';
    }
    
    

}
