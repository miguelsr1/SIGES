/*
 * SIGES
 * Desarrollado por: Sofis Solutions
 *
 */
package sv.gob.mined.siges.persistencia.entidades.siap2;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgTipoCuentaBancaria;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "ss_ges_pres_es", schema = Constantes.SCHEMA_SIAP2)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "gesId", scope = SsTopePresupuestal.class)
@Audited
/**
 * Entidad correspondiente a los presupuestos.
 */
public class SsGesPresEs implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ges_id", nullable = false)
    private Long gesId;

    @Column(name = "ges_cod")
    @AtributoCodigo
    @AtributoNormalizable
    private String gesCod;

    @Column(name = "ges_nombre")
    @AtributoNombre
    @AtributoNormalizable
    private String gesNombre;

    @Column(name = "ges_descripcion")
    private String gesDescripcion;
    
    @Column(name = "ges_convenio")
    private String gesConvenio;
    
    @Column(name = "ges_proyecto")
    private String gesProyecto;
    
    @Column(name = "ges_categoria")
    private String gesCategoria;
    
    @Column(name = "ges_subactividad")
    private String gesSubactividad;
    
    @Column(name = "ges_categoria_gasto_convenio")
    private Integer categoriaGastoConvenio;
    
    @ManyToOne
    @JoinColumn(name = "ges_tipo_cuenta_bancaria")
    private SgTipoCuentaBancaria tipoCuentaBancaria;
   
    @ManyToOne
    @JoinColumn(name = "ges_tipo_transferencia")
    private SsTipoTransferencia tipoTransferencia;

    @Column(name = "ges_habilitado")
    private Boolean gesHabilitado;
     
    @JoinColumn(name = "ges_categoria_componente", referencedColumnName = "cpe_id")
    @ManyToOne
    private SsCategoriaPresupuestoEscolar gesCategoriaComponente;

    @Column(name = "ges_ult_mod")
    @AtributoUltimaModificacion
    private LocalDate gesUltMod;

    @Column(name = "ges_ult_usuario")
    @AtributoUltimoUsuario
    private String gesUltUsuario;

    @Column(name = "ges_version")
    @Version
    private Long gesVersion;

    public SsGesPresEs() {
    }

    public Long getGesId() {
        return gesId;
    }

    public void setGesId(Long gesId) {
        this.gesId = gesId;
    }

    public String getGesCod() {
        return gesCod;
    }

    public void setGesCod(String gesCod) {
        this.gesCod = gesCod;
    }

    public String getGesNombre() {
        return gesNombre;
    }

    public void setGesNombre(String gesNombre) {
        this.gesNombre = gesNombre;
    }

    public String getGesDescripcion() {
        return gesDescripcion;
    }

    public void setGesDescripcion(String gesDescripcion) {
        this.gesDescripcion = gesDescripcion;
    }

    public String getGesProyecto() {
        return gesProyecto;
    }

    public void setGesProyecto(String gesProyecto) {
        this.gesProyecto = gesProyecto;
    }

    public String getGesSubactividad() {
        return gesSubactividad;
    }

    public void setGesSubactividad(String gesSubactividad) {
        this.gesSubactividad = gesSubactividad;
    }

    public String getGesCategoria() {
        return gesCategoria;
    }

    public void setGesCategoria(String gesCategoria) {
        this.gesCategoria = gesCategoria;
    }

    public String getGesConvenio() {
        return gesConvenio;
    }

    public void setGesConvenio(String gesConvenio) {
        this.gesConvenio = gesConvenio;
    }

    public Boolean getGesHabilitado() {
        return gesHabilitado;
    }

    public void setGesHabilitado(Boolean gesHabilitado) {
        this.gesHabilitado = gesHabilitado;
    }

    public SsCategoriaPresupuestoEscolar getGesCategoriaComponente() {
        return gesCategoriaComponente;
    }

    public void setGesCategoriaComponente(SsCategoriaPresupuestoEscolar gesCategoriaComponente) {
        this.gesCategoriaComponente = gesCategoriaComponente;
    }

    public LocalDate getGesUltMod() {
        return gesUltMod;
    }

    public void setGesUltMod(LocalDate gesUltMod) {
        this.gesUltMod = gesUltMod;
    }

    public String getGesUltUsuario() {
        return gesUltUsuario;
    }

    public void setGesUltUsuario(String gesUltUsuario) {
        this.gesUltUsuario = gesUltUsuario;
    }

    public Long getGesVersion() {
        return gesVersion;
    }

    public void setGesVersion(Long gesVersion) {
        this.gesVersion = gesVersion;
    }

    public SsTipoTransferencia getTipoTransferencia() {
        return tipoTransferencia;
    }

    public void setTipoTransferencia(SsTipoTransferencia tipoTransferencia) {
        this.tipoTransferencia = tipoTransferencia;
    }


    public Integer getCategoriaGastoConvenio() {
        return categoriaGastoConvenio;
    }

    public void setCategoriaGastoConvenio(Integer categoriaGastoConvenio) {
        this.categoriaGastoConvenio = categoriaGastoConvenio;
    }

    public SgTipoCuentaBancaria getTipoCuentaBancaria() {
        return tipoCuentaBancaria;
    }

    public void setTipoCuentaBancaria(SgTipoCuentaBancaria tipoCuentaBancaria) {
        this.tipoCuentaBancaria = tipoCuentaBancaria;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
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
        final SsGesPresEs other = (SsGesPresEs) obj;
        if (!Objects.equals(this.gesId, other.gesId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SsGesPresEs{" + "gesId=" + gesId + ", gesCod=" + gesCod + ", gesNombre=" + gesNombre + ", gesDescripcion=" + gesDescripcion + ", gesProyecto=" + gesProyecto + ", gesSubactividad=" + gesSubactividad + ", gesCategoria=" + gesCategoria + ", gesConvenio=" + gesConvenio + ", gesHabilitado=" + gesHabilitado + ", gesCategoriaComponente=" + gesCategoriaComponente + ", gesUltMod=" + gesUltMod + ", gesUltUsuario=" + gesUltUsuario + ", gesVersion=" + gesVersion + '}';
    }

}
