/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data;


import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import gob.mined.siap2.utils.generalutils.StringsUtils;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import gob.mined.siap2.entities.constantes.Constantes;
import javax.persistence.Temporal;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD) 
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_errores")
@NamedQueries({@NamedQuery(name = "Error.obtenerPorDescripcion", query = "SELECT e FROM Error e where e.errDescripcion LIKE :errDescripcion"),
              @NamedQuery(name = "Error.obtenerPorCodigo", query = "SELECT e FROM Error e where e.errCodigo LIKE :errCodigo")})
/**
 * Esta entidad corresponde a los mensajes de error utilizados en el sistema.
 */
public class Error implements Serializable{
    
    private static final long serialVersionUID = 1L;

    @SequenceGenerator(name = "seq_errores", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_errores", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_errores")
    @Basic(optional = false)
    /**
     * Id de la entidad.
     */
    @Column(name = "err_id")
    private Integer id;
    @Column(name="err_codigo")
    private String errCodigo;
    @Column(name="err_descripcion")
    private String errDescripcion;
    
    //Audit
    @Column(name="err_ult_usuario")
    @AtributoUltimoUsuario
    private String errUltUsuario;
    @Column(name="err_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date errUltMod;
    @Column(name="err_ult_origen")
    private String errUltOrigen;
    @Column(name="err_version")
    @Version
    private Integer errVersion;

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getErrCodigo() {
        return errCodigo;
    }

    public void setErrCodigo(String errCodigo) {
        this.errCodigo = errCodigo;
    }

    public String getErrDescripcion() {
        return errDescripcion;
    }

    public void setErrDescripcion(String errDescripcion) {
        this.errDescripcion = errDescripcion;
    }

    public String getErrUltUsuario() {
        return errUltUsuario;
    }

    public void setErrUltUsuario(String errUltUsuario) {
        this.errUltUsuario = errUltUsuario;
    }

    public Date getErrUltMod() {
        return errUltMod;
    }

    public void setErrUltMod(Date errUltMod) {
        this.errUltMod = errUltMod;
    }

    public String getErrUltOrigen() {
        return errUltOrigen;
    }

    public void setErrUltOrigen(String errUltOrigen) {
        this.errUltOrigen = errUltOrigen;
    }

    public Integer getErrVersion() {
        return errVersion;
    }

    public void setErrVersion(Integer errVersion) {
        this.errVersion = errVersion;
    }
    // </editor-fold>
    
     @PrePersist
    @PreUpdate
    public void preGrabar()  {
       this.errUltMod = new Date();
       this.errCodigo=StringsUtils.normalizarStringMayuscula(errCodigo);
       this.errDescripcion=StringsUtils.normalizarString(errDescripcion);
    }  
}
