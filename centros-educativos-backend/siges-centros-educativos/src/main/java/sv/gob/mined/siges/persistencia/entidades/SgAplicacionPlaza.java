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
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumEstadoAplicacionPlaza;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgCalidadIngresoAplicantes;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgMotivosSeleccionPLaza;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_aplicaciones_plaza", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "aplPk", scope = SgAplicacionPlaza.class)
@Audited
public class SgAplicacionPlaza implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "apl_pk")
    private Long aplPk;

    @JoinColumn(name = "apl_plaza_fk", referencedColumnName = "spl_pk")
    @ManyToOne
    private SgSolicitudPlaza aplPlaza;

    @JoinColumn(name = "apl_cia_fk", referencedColumnName = "cia_pk")
    @ManyToOne
    private SgCalidadIngresoAplicantes aplCalidadAplicantes;
    
    @JoinColumn(name = "apl_personal_fk", referencedColumnName = "pse_pk")
    @ManyToOne
    private SgPersonalSedeEducativa aplPersonal;
    
    @Size(max = 25)
    @Column(name = "apl_codigo_usuario", length = 25)
    private String aplCodigoUsuario;
    
    @Column(name = "apl_estado")
    @Enumerated(value = EnumType.STRING)
    private EnumEstadoAplicacionPlaza aplEstado;
    
    @Size(max = 500)
    @Column(name = "apl_observacion")
    private String aplObservacion;
    
    @Column(name = "apl_fecha_aplico")
    private LocalDateTime aplFechaAplico;
    
    @Column(name = "apl_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime aplUltModFecha;
    
    @Size(max = 45)
    @Column(name = "apl_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String aplUltModUsuario;
    
    @Column(name = "apl_version")
    @Version
    private Integer aplVersion;
    
    @Column(name = "apl_seleccionado_en_matriz")
    private Boolean aplSeleccionadoEnMatriz;
    
    @Column(name = "apl_rev_personal_cuando_aplica")
    private Long aplRevPersonalCuandoAplica;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "epaAplicacionPlazaFk")
    private List<SgEspecialidadesPersonalAlAplicar> aplEspecialidadesAlAplicar;   
    
    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "sg_motivo_aplicacion_plaza",
            schema = Constantes.SCHEMA_CENTRO_EDUCATIVO,
            joinColumns = @JoinColumn(name = "apl_pk"),
            inverseJoinColumns = @JoinColumn(name = "msp_pk"))
    private List<SgMotivosSeleccionPLaza> aplMotivosSeleccionPLaza;

    @Transient
    private SgAplicacionPlaza plazaSeleccionado;
    
    public SgAplicacionPlaza() {
    }

    public SgAplicacionPlaza(Long aplPk) {
        this.aplPk = aplPk;
    }

    public Long getAplPk() {
        return aplPk;
    }

    public void setAplPk(Long aplPk) {
        this.aplPk = aplPk;
    }

    public String getAplCodigoUsuario() {
        return aplCodigoUsuario;
    }

    public void setAplCodigoUsuario(String aplCodigoUsuario) {
        this.aplCodigoUsuario = aplCodigoUsuario;
    }

    public SgSolicitudPlaza getAplPlaza() {
        return aplPlaza;
    }

    public void setAplPlaza(SgSolicitudPlaza aplPlaza) {
        this.aplPlaza = aplPlaza;
    }

    public SgPersonalSedeEducativa getAplPersonal() {
        return aplPersonal;
    }

    public void setAplPersonal(SgPersonalSedeEducativa aplPersonal) {
        this.aplPersonal = aplPersonal;
    }

    public EnumEstadoAplicacionPlaza getAplEstado() {
        return aplEstado;
    }

    public void setAplEstado(EnumEstadoAplicacionPlaza aplEstado) {
        this.aplEstado = aplEstado;
    }

    public String getAplObservacion() {
        return aplObservacion;
    }

    public void setAplObservacion(String aplObservacion) {
        this.aplObservacion = aplObservacion;
    }

    public LocalDateTime getAplFechaAplico() {
        return aplFechaAplico;
    }

    public void setAplFechaAplico(LocalDateTime aplFechaAplico) {
        this.aplFechaAplico = aplFechaAplico;
    }

    public LocalDateTime getAplUltModFecha() {
        return aplUltModFecha;
    }

    public void setAplUltModFecha(LocalDateTime aplUltModFecha) {
        this.aplUltModFecha = aplUltModFecha;
    }

    public String getAplUltModUsuario() {
        return aplUltModUsuario;
    }

    public void setAplUltModUsuario(String aplUltModUsuario) {
        this.aplUltModUsuario = aplUltModUsuario;
    }

    public Integer getAplVersion() {
        return aplVersion;
    }

    public void setAplVersion(Integer aplVersion) {
        this.aplVersion = aplVersion;
    }

    public Boolean getAplSeleccionadoEnMatriz() {
        return aplSeleccionadoEnMatriz;
    }

    public void setAplSeleccionadoEnMatriz(Boolean aplSeleccionadoEnMatriz) {
        this.aplSeleccionadoEnMatriz = aplSeleccionadoEnMatriz;
    }

    public Long getAplRevPersonalCuandoAplica() {
        return aplRevPersonalCuandoAplica;
    }

    public void setAplRevPersonalCuandoAplica(Long aplRevPersonalCuandoAplica) {
        this.aplRevPersonalCuandoAplica = aplRevPersonalCuandoAplica;
    }

    public List<SgEspecialidadesPersonalAlAplicar> getAplEspecialidadesAlAplicar() {
        return aplEspecialidadesAlAplicar;
    }

    public void setAplEspecialidadesAlAplicar(List<SgEspecialidadesPersonalAlAplicar> aplEspecialidadesAlAplicar) {
        this.aplEspecialidadesAlAplicar = aplEspecialidadesAlAplicar;
    }

    public List<SgMotivosSeleccionPLaza> getAplMotivosSeleccionPLaza() {
        return aplMotivosSeleccionPLaza;
    }

    public void setAplMotivosSeleccionPLaza(List<SgMotivosSeleccionPLaza> aplMotivosSeleccionPLaza) {
        this.aplMotivosSeleccionPLaza = aplMotivosSeleccionPLaza;
    }

    public SgAplicacionPlaza getPlazaSeleccionado() {
        return plazaSeleccionado;
    }

    public void setPlazaSeleccionado(SgAplicacionPlaza plazaSeleccionado) {
        this.plazaSeleccionado = plazaSeleccionado;
    }

    public SgCalidadIngresoAplicantes getAplCalidadAplicantes() {
        return aplCalidadAplicantes;
    }

    public void setAplCalidadAplicantes(SgCalidadIngresoAplicantes aplCalidadAplicantes) {
        this.aplCalidadAplicantes = aplCalidadAplicantes;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (aplPk != null ? aplPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgAplicacionPlaza)) {
            return false;
        }
        SgAplicacionPlaza other = (SgAplicacionPlaza) object;
        if ((this.aplPk == null && other.aplPk != null) || (this.aplPk != null && !this.aplPk.equals(other.aplPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgAplicacionPlaza[ aplPk=" + aplPk + " ]";
    }
    
}
