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
import java.time.LocalDate;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgMotivoAnulacionTitulo;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgTituloAnterior;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_reposicion_titulo", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "retPk", scope = SgReposicionTitulo.class)
@Audited
public class SgReposicionTitulo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ret_pk", nullable = false)
    private Long retPk;

  
    @Column(name = "ret_nombre_estudiante_partida")
    private String retNombreEstudiantePartida;
    
    @Column(name = "ret_nombre_estudiante_partida_busqueda")
    private String retNombreEstudiantePartidaBusqueda;

    @Column(name = "ret_anio")
    private Integer retAnio;
    
    @Column(name = "ret_sede_nombre")
    private String retSedeNombre;
    
    @Column(name = "ret_sede_nombre_busqueda")
    private String retSedeNombreBusqueda;
    
    @Column(name = "ret_estado_reposicion")
    private String retEstadoReposicion;
    
    @Column(name = "ret_usuario_envia_imprimir")
    private String retUsuarioEnviaImprimir;
   


    @Column(name = "ret_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime retUltModFecha;

    @Size(max = 45)
    @Column(name = "ret_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String retUltModUsuario;

    @Column(name = "ret_version")
    @Version
    private Integer retVersion;
    
    @JoinColumn(name = "ret_sede_fk", referencedColumnName = "sed_pk")
    @ManyToOne
    private SgSede retSede;
    
    @OneToOne(mappedBy = "simReposicionTitulo",cascade ={ CascadeType.PERSIST})
    private SgSolicitudImpresion retSolicitudImpresion;
    
    @Column(name = "ret_anulada")
    private Boolean retAnulada;
    
    @JoinColumn(name = "ret_motivo_anulacion_titulo_fk", referencedColumnName = "mat_pk")
    @ManyToOne
    private SgMotivoAnulacionTitulo retMotivoAnulacion;
    
     
    @Column(name = "ret_observacion_anulada")
    private String retObservacionAnulada;     
    
    @Column(name = "ret_opcion_bachillerato")
    private String retOpcionBachillerato;   
    
    //En caso de ser true se completa el catalogo retTituloAnterior2008, sino se completa retNombreTituloPosterior2008
    @Column(name = "ret_es_anterior_2008")
    private Boolean retEsAnterior2008;   
    
    @JoinColumn(name = "ret_titulo_anterior_2008_fk", referencedColumnName = "tan_pk")
    @ManyToOne
    private SgTituloAnterior retTituloAnterior2008;
    
    @Column(name = "ret_nombre_titulo_posterior_2008")
    private String retNombreTituloPosterior2008;  
    
    @Column(name = "ret_dui_solicitante")
    private String retDuiSolicitante;   
    
    @Column(name = "ret_fecha_legalizacion_titulo")
    private LocalDate retFechaLegalizacionTitulo;
    
    @PrePersist
    @PreUpdate
    public void preSave() {
        if(!StringUtils.isBlank(retNombreEstudiantePartida)){
            retNombreEstudiantePartidaBusqueda= SofisStringUtils.normalizarParaBusqueda(this.retNombreEstudiantePartida);
        }
        if(!StringUtils.isBlank(retSedeNombre)){
            retSedeNombreBusqueda= SofisStringUtils.normalizarParaBusqueda(this.retSedeNombre);
        }
    }

    public SgReposicionTitulo() {
    }


    public Long getRetPk() {
        return retPk;
    }

    public void setRetPk(Long retPk) {
        this.retPk = retPk;
    }

    public String getRetNombreEstudiantePartida() {
        return retNombreEstudiantePartida;
    }

    public void setRetNombreEstudiantePartida(String retNombreEstudiantePartida) {
        this.retNombreEstudiantePartida = retNombreEstudiantePartida;
    }

    public Integer getRetAnio() {
        return retAnio;
    }

    public void setRetAnio(Integer retAnio) {
        this.retAnio = retAnio;
    }

    public String getRetSedeNombre() {
        return retSedeNombre;
    }

    public void setRetSedeNombre(String retSedeNombre) {
        this.retSedeNombre = retSedeNombre;
    }

    public String getRetEstadoReposicion() {
        return retEstadoReposicion;
    }

    public void setRetEstadoReposicion(String retEstadoReposicion) {
        this.retEstadoReposicion = retEstadoReposicion;
    }

    public String getRetUsuarioEnviaImprimir() {
        return retUsuarioEnviaImprimir;
    }

    public void setRetUsuarioEnviaImprimir(String retUsuarioEnviaImprimir) {
        this.retUsuarioEnviaImprimir = retUsuarioEnviaImprimir;
    }

    public LocalDateTime getRetUltModFecha() {
        return retUltModFecha;
    }

    public void setRetUltModFecha(LocalDateTime retUltModFecha) {
        this.retUltModFecha = retUltModFecha;
    }

    public String getRetUltModUsuario() {
        return retUltModUsuario;
    }

    public void setRetUltModUsuario(String retUltModUsuario) {
        this.retUltModUsuario = retUltModUsuario;
    }

    public Integer getRetVersion() {
        return retVersion;
    }

    public void setRetVersion(Integer retVersion) {
        this.retVersion = retVersion;
    }

    public SgSede getRetSede() {
        return retSede;
    }

    public void setRetSede(SgSede retSede) {
        this.retSede = retSede;
    }

    public SgSolicitudImpresion getRetSolicitudImpresion() {
        return retSolicitudImpresion;
    }

    public void setRetSolicitudImpresion(SgSolicitudImpresion retSolicitudImpresion) {
        this.retSolicitudImpresion = retSolicitudImpresion;
    }

    public Boolean getRetAnulada() {
        return retAnulada;
    }

    public void setRetAnulada(Boolean retAnulada) {
        this.retAnulada = retAnulada;
    }

    public SgMotivoAnulacionTitulo getRetMotivoAnulacion() {
        return retMotivoAnulacion;
    }

    public void setRetMotivoAnulacion(SgMotivoAnulacionTitulo retMotivoAnulacion) {
        this.retMotivoAnulacion = retMotivoAnulacion;
    }

    public String getRetObservacionAnulada() {
        return retObservacionAnulada;
    }

    public void setRetObservacionAnulada(String retObservacionAnulada) {
        this.retObservacionAnulada = retObservacionAnulada;
    }

    public String getRetNombreEstudiantePartidaBusqueda() {
        return retNombreEstudiantePartidaBusqueda;
    }

    public void setRetNombreEstudiantePartidaBusqueda(String retNombreEstudiantePartidaBusqueda) {
        this.retNombreEstudiantePartidaBusqueda = retNombreEstudiantePartidaBusqueda;
    }

    public String getRetSedeNombreBusqueda() {
        return retSedeNombreBusqueda;
    }

    public void setRetSedeNombreBusqueda(String retSedeNombreBusqueda) {
        this.retSedeNombreBusqueda = retSedeNombreBusqueda;
    }

    public String getRetOpcionBachillerato() {
        return retOpcionBachillerato;
    }

    public void setRetOpcionBachillerato(String retOpcionBachillerato) {
        this.retOpcionBachillerato = retOpcionBachillerato;
    }

    public SgTituloAnterior getRetTituloAnterior2008() {
        return retTituloAnterior2008;
    }

    public void setRetTituloAnterior2008(SgTituloAnterior retTituloAnterior2008) {
        this.retTituloAnterior2008 = retTituloAnterior2008;
    }

    public String getRetDuiSolicitante() {
        return retDuiSolicitante;
    }

    public void setRetDuiSolicitante(String retDuiSolicitante) {
        this.retDuiSolicitante = retDuiSolicitante;
    }

    public LocalDate getRetFechaLegalizacionTitulo() {
        return retFechaLegalizacionTitulo;
    }

    public void setRetFechaLegalizacionTitulo(LocalDate retFechaLegalizacionTitulo) {
        this.retFechaLegalizacionTitulo = retFechaLegalizacionTitulo;
    }

    public Boolean getRetEsAnterior2008() {
        return retEsAnterior2008;
    }

    public void setRetEsAnterior2008(Boolean retEsAnterior2008) {
        this.retEsAnterior2008 = retEsAnterior2008;
    }

    public String getRetNombreTituloPosterior2008() {
        return retNombreTituloPosterior2008;
    }

    public void setRetNombreTituloPosterior2008(String retNombreTituloPosterior2008) {
        this.retNombreTituloPosterior2008 = retNombreTituloPosterior2008;
    }

    
 
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.retPk);
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
        final SgReposicionTitulo other = (SgReposicionTitulo) obj;
        if (!Objects.equals(this.retPk, other.retPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgReposicionTitulo{" + "retPk=" + retPk + ", retUltModFecha=" + retUltModFecha + ", retUltModUsuario=" + retUltModUsuario + ", retVersion=" + retVersion + '}';
    }
    
    

}
