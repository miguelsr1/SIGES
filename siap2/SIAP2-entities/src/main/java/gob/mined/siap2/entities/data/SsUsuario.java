/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data;

import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import gob.mined.siap2.utils.generalutils.StringsUtils;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import gob.mined.siap2.entities.constantes.Constantes;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import javax.persistence.Cacheable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_usuario")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
@XmlRootElement
@Cacheable(false)
public class SsUsuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @SequenceGenerator(name = "seq_usuario", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_usuario", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_usuario")
    @Basic(optional = false)
    @Column(name = "usu_id")
    private Integer usuId;


    @Column(name = "usu_cod")
    private String usuCod;
    @Column(name = "usu_correo_electronico")
    private String usuCorreoElectronico;
    @Column(name = "usu_cuenta_bloqueada")
    private Boolean usuCuentaBloqueada;

    @Column(name = "usu_direccion")
    private String usuDireccion;

    @Basic(optional = false)
    @NotNull
    @Column(name = "usu_fecha_password", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date usuFechaPassword;

    @Column(name = "usu_intentos_fallidos")
    private Integer usuIntentosFallidos;
    @Column(name = "usu_nro_doc")
    private String usuNroDoc;
    @Column(name = "usu_oficina_por_defecto")
    private Integer usuOficinaPorDefecto;


    @Column(name = "usu_password")
    private String usuPassword;
    @Basic(optional = false)
    @Column(name = "usu_primer_apellido")
    private String usuPrimerApellido;
    @Basic(optional = false)
    @Column(name = "usu_primer_nombre")
    private String usuPrimerNombre;
    @Column(name = "usu_segundo_apellido")
    private String usuSegundoApellido;
    @Column(name = "usu_segundo_nombre")
    private String usuSegundoNombre;
    @Column(name = "usu_telefono")
    private String usuTelefono;

    @Column(name = "usu_cambio_password")
    private Boolean usuCambioPassword;    

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuOfiRolesUsuario", orphanRemoval = true)
    private Collection<SsUsuOfiRoles> ssUsuOfiRolesCollection;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            schema = Constantes.SCHEMA_SIAP2,
            name = "ss_rel_usu_rol",
            joinColumns = {
                @JoinColumn(name = "rel_usu_id", referencedColumnName = "usu_id")},
            inverseJoinColumns = {
                @JoinColumn(name = "rel_rol_id", referencedColumnName = "rol_id")})
    private List<SsRol> rolesAdministrados;

    @Column(name = "usu_adm_general")
    private Boolean usuAdmGeneral;
    
    @Column(name = "usu_prefijo")
    private String usuPrefijo;//ejemplo: Sr. Sra. Lic. Ing.
    @Column(name = "usu_cargo")
    private String usuCargo;
    
    @Column(name="usu_desde")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date usuDesde;
    
    @Column(name="usu_hasta")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date usuHasta;
    
    @OneToMany(mappedBy = "uniUsuario")
    private List<UnidadTecnica> unidadTecnicas;
    
    //Auditoria
    @Column(name = "usu_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "usu_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;
    
    @Column(name = "usu_version")
    @Version
    private Integer usuVersion;
    
    @Column(name="usu_creado_por")
    private String usuCreadoPor;
    
    @Transient
    private String nombreCompleto;

    public SsUsuario() {
    }

    public SsUsuario(Integer usuId) {
        this.usuId = usuId;
    }


    public Integer getUsuId() {
        return usuId;
    }

    public void setUsuId(Integer usuId) {
        this.usuId = usuId;
    }

    public String getUsuCod() {
        return usuCod;
    }

    public void setUsuCod(String usuCod) {
        this.usuCod = usuCod;
    }

    public Boolean getUsuCambioPassword() {
        return usuCambioPassword;
    }

    public void setUsuCambioPassword(Boolean usuCambioPassword) {
        this.usuCambioPassword = usuCambioPassword;
    }

    public String getUsuCorreoElectronico() {
        return usuCorreoElectronico;
    }

    public void setUsuCorreoElectronico(String usuCorreoElectronico) {
        this.usuCorreoElectronico = usuCorreoElectronico;
    }

    public Boolean isUsuCuentaBloqueada() {
        return usuCuentaBloqueada;
    }

    public Boolean getUsuCuentaBloqueada() {
        return usuCuentaBloqueada;
    }

    public void setUsuCuentaBloqueada(Boolean usuCuentaBloqueada) {
        this.usuCuentaBloqueada = usuCuentaBloqueada;
    }

    public String getUsuDireccion() {
        return usuDireccion;
    }

    public void setUsuDireccion(String usuDireccion) {
        this.usuDireccion = usuDireccion;
    }

    public Date getUsuFechaPassword() {
        return usuFechaPassword;
    }

    public void setUsuFechaPassword(Date usuFechaPassword) {
        this.usuFechaPassword = usuFechaPassword;
    }

    public Integer getUsuIntentosFallidos() {
        return usuIntentosFallidos;
    }

    public void setUsuIntentosFallidos(Integer usuIntentosFallidos) {
        this.usuIntentosFallidos = usuIntentosFallidos;
    }

    public String getUsuNroDoc() {
        return usuNroDoc;
    }

    public void setUsuNroDoc(String usuNroDoc) {
        this.usuNroDoc = usuNroDoc;
    }

    public Integer getUsuOficinaPorDefecto() {
        return usuOficinaPorDefecto;
    }

    public void setUsuOficinaPorDefecto(Integer usuOficinaPorDefecto) {
        this.usuOficinaPorDefecto = usuOficinaPorDefecto;
    }

    public String getUsuPassword() {
        return usuPassword;
    }

    public void setUsuPassword(String usuPassword) {
        this.usuPassword = usuPassword;
    }

    public String getUsuPrimerApellido() {
        return usuPrimerApellido;
    }

    public void setUsuPrimerApellido(String usuPrimerApellido) {
        this.usuPrimerApellido = usuPrimerApellido;
    }

    public String getUsuPrimerNombre() {
        return usuPrimerNombre;
    }

    public void setUsuPrimerNombre(String usuPrimerNombre) {
        this.usuPrimerNombre = usuPrimerNombre;
    }

    public String getUsuSegundoApellido() {
        return usuSegundoApellido;
    }

    public void setUsuSegundoApellido(String usuSegundoApellido) {
        this.usuSegundoApellido = usuSegundoApellido;
    }

    public String getUsuSegundoNombre() {
        return usuSegundoNombre;
    }

    public void setUsuSegundoNombre(String usuSegundoNombre) {
        this.usuSegundoNombre = usuSegundoNombre;
    }

    public String getUsuTelefono() {
        return usuTelefono;
    }

    public void setUsuTelefono(String usuTelefono) {
        this.usuTelefono = usuTelefono;
    }

    public Integer getUsuVersion() {
        return usuVersion;
    }

    public void setUsuVersion(Integer usuVersion) {
        this.usuVersion = usuVersion;
    }

    public Collection<SsUsuOfiRoles> getSsUsuOfiRolesCollection() {
        return ssUsuOfiRolesCollection;
    }

    public void setSsUsuOfiRolesCollection(Collection<SsUsuOfiRoles> ssUsuOfiRolesCollection) {
        this.ssUsuOfiRolesCollection = ssUsuOfiRolesCollection;
    }

    public List<SsRol> getRolesAdministrados() {
        return rolesAdministrados;
    }

    public void setRolesAdministrados(List<SsRol> rolesAdministrados) {
        this.rolesAdministrados = rolesAdministrados;
    }

    public Boolean isUsuAdmGeneral() {
        return usuAdmGeneral;
    }

    public Boolean getUsuAdmGeneral() {
        return usuAdmGeneral;
    }

    public void setUsuAdmGeneral(Boolean usuAdmGeneral) {
        this.usuAdmGeneral = usuAdmGeneral;
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
    
    public String getUsuPrefijo() {
        return usuPrefijo;
    }

    public void setUsuPrefijo(String usuPrefijo) {
        this.usuPrefijo = usuPrefijo;
    }

    public String getUsuCargo() {
        return usuCargo;
    }
    
    public void setUsuCargo(String usuCargo) {
        this.usuCargo = usuCargo;
    }

    public Date getUsuDesde() {
        return usuDesde;
    }

    public void setUsuDesde(Date usuDesde) {
        this.usuDesde = usuDesde;
    }

    public Date getUsuHasta() {
        return usuHasta;
    }

    public void setUsuHasta(Date usuHasta) {
        this.usuHasta = usuHasta;
    }
    
    public String getNombreCompleto() {
        nombreCompleto="";
        if (usuPrimerNombre!=null) {
            nombreCompleto=usuPrimerNombre.trim();
        }
        if (usuPrimerApellido!=null) {
            nombreCompleto+=" "+usuPrimerApellido;
        }
        nombreCompleto=StringsUtils.normalizarString(nombreCompleto);
        return nombreCompleto;
    }
    public String getNombresApellidosCompletons() {
        nombreCompleto="";
        if(usuPrefijo != null) {
            nombreCompleto = usuPrefijo.trim();
        }
        if (usuPrimerNombre!=null) {
            nombreCompleto +=" "+ usuPrimerNombre.trim();
        }
        if (usuSegundoNombre!=null) {
            nombreCompleto+=" "+usuSegundoNombre.trim();
        }
        if (usuPrimerApellido!=null) {
            nombreCompleto+=" "+usuPrimerApellido.trim();
        }
        if (usuSegundoApellido!=null) {
            nombreCompleto+=" "+usuSegundoApellido.trim();
        }
        nombreCompleto=StringsUtils.normalizarString(nombreCompleto);
        return nombreCompleto;
    }
    public String getUsuCreadoPor() {
        return usuCreadoPor;
    }

    public void setUsuCreadoPor(String usuCreadoPor) {
        this.usuCreadoPor = usuCreadoPor;
    }
    
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (usuId != null ? usuId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SsUsuario)) {
            return false;
        }
        SsUsuario other = (SsUsuario) object;
        if ((this.usuId == null && other.usuId != null) || (this.usuId != null && !this.usuId.equals(other.usuId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getClass().getCanonicalName() + "[ usuId=" + usuId + " ]";
    }

    public List<UnidadTecnica> getUnidadTecnicas() {
        return unidadTecnicas;
    }

    public void setUnidadTecnicas(List<UnidadTecnica> unidadTecnicas) {
        this.unidadTecnicas = unidadTecnicas;
    }


    
}
