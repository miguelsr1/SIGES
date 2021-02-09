/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data;



import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimaOrigen;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import gob.mined.siap2.entities.constantes.Constantes;
import javax.persistence.Temporal;
import javax.persistence.Version;
import org.eclipse.persistence.annotations.Customizer;

@Entity
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_configuraciones")
@NamedQueries({@NamedQuery(name = "Configuracion.obtenerPorDescripcion", query = "SELECT c FROM Configuracion c where c.cnfDescripcion LIKE :cnfDescripcion"),
              @NamedQuery(name = "Configuracion.obtenerPorCodigo", query = "SELECT c FROM Configuracion c where c.cnfCodigo LIKE :cnfCodigo")})
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
/**
 * Entidad que se corresponde con los datos de configuración del sistema.
 */
public class Configuracion implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @SequenceGenerator(name = "seq_configuraciones", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_configuraciones", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_configuraciones")
    @Basic(optional = false)
    @Column(name = "cnf_id")
    /**
     * Id de la entidad.
     */
    private Integer id;
    @Column(name="cnf_codigo")
    /**
     * Código del elemento de la configuración. Este código no debería modificarse, ya que el sistema busca los datos según este código.
     */
    private String cnfCodigo;
    @Column(name="cnf_descripcion")
    /**
     * Descripción del elemento de configuración.
     */
    private String cnfDescripcion;
    @Column(name="cnf_valor",length = 1500)
    /**
     * Valor asociado al código de configuración.
     */
    private String cnfValor;
    @Column(name="cnf_protegido")
    /**
     * Indica si es un dato protegido.
     */
    private Boolean cnfProtegido;
    /**
     * Indica si admite valores html. No se utiliza en esta versión del sistema.
     */
    @Column(name="cnf_html")
    private Boolean cnfHtml;
    
    //Audit
    @Column(name="cnf_ult_usuario")
    @AtributoUltimoUsuario
    /**
     * Código del usuario que realizó la última modificación a la entidad.
     */
    private String cnfUltUsuario;
    @Column(name="cnf_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    /**
     * Fecha en que se realizó la última modificación.
     */
    private Date cnfUltMod;
    
    @AtributoUltimaOrigen
    @Column(name="cnf_ult_origen")
    /**
     * Origen desde donde se realizó la última modificación.
     * En esta versión este valor es siempre el mismo.
     */
    private String cnfUltOrigen;
    @Column(name="cnf_version")
    @Version
    /**
     * Versión. 
     */
    private Integer cnfVersion;
    
           

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCnfCodigo() {
        return cnfCodigo;
    }

    public void setCnfCodigo(String cnfCodigo) {
        this.cnfCodigo = cnfCodigo;
    }

    public String getCnfDescripcion() {
        return cnfDescripcion;
    }

    public void setCnfDescripcion(String cnfDescripcion) {
        this.cnfDescripcion = cnfDescripcion;
    }

    public String getCnfValor() {
        return cnfValor;
    }

    public void setCnfValor(String cnfValor) {
        this.cnfValor = cnfValor;
    }

    public Boolean isCnfProtegido() {
        return cnfProtegido;
    }

    public void setCnfProtegido(Boolean cnfProtegido) {
        this.cnfProtegido = cnfProtegido;
    }

    public Boolean getCnfHtml() {
        return cnfHtml;
    }

    public void setCnfHtml(Boolean cnfHtml) {
        this.cnfHtml = cnfHtml;
    }

    public String getCnfUltUsuario() {
        return cnfUltUsuario;
    }

    public void setCnfUltUsuario(String cnfUltUsuario) {
        this.cnfUltUsuario = cnfUltUsuario;
    }

    public Date getCnfUltMod() {
        return cnfUltMod;
    }

    public void setCnfUltMod(Date cnfUltMod) {
        this.cnfUltMod = cnfUltMod;
    }

    public String getCnfUltOrigen() {
        return cnfUltOrigen;
    }

    public void setCnfUltOrigen(String cnfUltOrigen) {
        this.cnfUltOrigen = cnfUltOrigen;
    }

    public Integer getCnfVersion() {
        return cnfVersion;
    }

    public void setCnfVersion(Integer cnfVersion) {
        this.cnfVersion = cnfVersion;
    }

    // </editor-fold>
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Configuracion)) {
            return false;
        }
        Configuracion other = (Configuracion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getClass().getCanonicalName() + "[ id=" + id + " ]";
    }
    

}
