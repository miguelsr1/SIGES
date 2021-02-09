/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sofis.security.DaoOperation;
import com.sofis.security.DataSecurity;
import com.sofis.security.OperationSecurity;
import com.sofis.sofisform.to.CriteriaTO;
import com.sofis.sofisform.to.MatchCriteriaTO;
import com.sofis.utils.CriteriaTOUtils;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.SeguridadAmbitos;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgAfEmpleados;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgAfEstadosBienes;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgAfEstadosTraslado;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgAfUnidadesAdministrativas;
import sv.gob.mined.siges.persistencia.entidades.centroseducativos.SgSede;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_af_traslados", uniqueConstraints = {
    @UniqueConstraint(name = "codigo_traslado_sede_uk", columnNames = {"tra_sede_origen_fk","tra_codigo_traslado","tra_anio"}),
    @UniqueConstraint(name = "codigo_traslado_unidad_uk", columnNames = {"tra_unidad_adm_origen_fk","tra_codigo_traslado","tra_anio"})},schema = Constantes.SCHEMA_ACTIVO_FIJO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "traPk", scope = SgAfTraslado.class)
public class SgAfTraslado implements Serializable, DataSecurity {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tra_pk")
    private Long traPk;

    @Size(max = 10)
    @Column(name = "tra_codigo_traslado", length = 10)
    private String traCodigoTraslado;

    @Column(name = "tra_fecha_solicitud")
    private LocalDate traFechaSolicitud;

    @Column(name = "tra_fecha_autoriza")
    private LocalDateTime traFechaAutoriza;

    @Column(name = "tra_fecha_traslado")
    private LocalDate traFechaTraslado;

    @Column(name = "tra_fecha_retorno")
    private LocalDate traFechaRetono;

    @Column(name = "tra_correlativo")
    private Integer traCorrelativo;
    
    @Column(name = "tra_anio")
    private Integer traAnio; 
    
    @Size(max = 100)
    @Column(name = "tra_unidad_destino", length = 100)
    private String traUnidadDestino;

    @Column(name = "tra_fecha_recepcion")
    private LocalDate traFechaRecepcion;

    @JoinColumn(name = "tra_tipo_traslado_fk", referencedColumnName = "etr_pk")
    @ManyToOne
    private SgAfEstadosTraslado traTipoTrasladoFk;

    @Size(max = 100)
    @Column(name = "tra_nombre_autoriza", length = 100)
    private String traNombreAutoriza;

    @Size(max = 100)
    @Column(name = "tra_nombre_recibe", length = 100)
    private String traNombreRecibe;

    @Size(max = 100)
    @Column(name = "tra_nombre_repr_af", length = 100)
    private String traNombreReprAf;

    @Size(max = 100)
    @Column(name = "tra_cargo_autoriza", length = 100)
    private String traCargoAutoriza;

    @Size(max = 100)
    @Column(name = "tra_cargo_recibe", length = 100)
    private String traCargoRecibe;

    @Size(max = 150)
    @Column(name = "tra_observacion", length = 150)
    private String traObservacion;

    @JoinColumn(name = "tra_sede_origen_fk", referencedColumnName = "sed_pk")
    @ManyToOne
    private SgSede traSedeOrigenFk;

    @JoinColumn(name = "tra_sede_destino_fk", referencedColumnName = "sed_pk")
    @ManyToOne
    private SgSede traSedeDestinoFk;

    @JoinColumn(name = "tra_unidad_adm_origen_fk", referencedColumnName = "uad_pk")
    @ManyToOne
    private SgAfUnidadesAdministrativas traUnidadAdmOrigenFk;

    @JoinColumn(name = "tra_unidad_adm_destino_fk", referencedColumnName = "uad_pk")
    @ManyToOne
    private SgAfUnidadesAdministrativas traUnidadAdmDestinoFk;

    @JoinColumn(name = "tra_estado_fk", referencedColumnName = "ebi_pk")
    @ManyToOne
    private SgAfEstadosBienes traEstadoFk;

    @Column(name = "tra_fecha_creacion")
    private LocalDateTime traFechaCreacion;

    @Size(max = 45)
    @Column(name = "tra_usuario_creacion", length = 45)
    private String traUsuarioCreacion;

    @Column(name = "tra_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime traUltModFecha;

    @Size(max = 45)
    @Column(name = "tra_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String traUltModUsuario;

    @Column(name = "tra_version")
    @Version
    private Integer traVersion;


    @JoinColumn(name = "tra_empleado_bienes_fk", referencedColumnName = "emp_pk")
    @ManyToOne
    private SgAfEmpleados traEmpleadoBienes;
    
    @Size(max = 100)
    @Column(name = "tra_asignadoa_bienes", length = 100)
    private String traAsignadoABienes;
    
    @NotAudited
    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "tdeTrasladoFk", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SgAfTrasladosDetalle> sgAfTrasladosDetalle;

    @NotAudited
    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "mrtTrasladoFk", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SgAfMotivoRechazoTraslado> sgAfMotivosRechazoTraslado;

    public SgAfTraslado() {
        this.sgAfTrasladosDetalle = new ArrayList();
        this.sgAfMotivosRechazoTraslado = new ArrayList();
    }

    public Long getTraPk() {
        return traPk;
    }

    public void setTraPk(Long traPk) {
        this.traPk = traPk;
    }

    public String getTraCodigoTraslado() {
        return traCodigoTraslado;
    }

    public void setTraCodigoTraslado(String traCodigoTraslado) {
        this.traCodigoTraslado = traCodigoTraslado;
    }

    public LocalDate getTraFechaSolicitud() {
        return traFechaSolicitud;
    }

    public void setTraFechaSolicitud(LocalDate traFechaSolicitud) {
        this.traFechaSolicitud = traFechaSolicitud;
    }

    public LocalDateTime getTraFechaAutoriza() {
        return traFechaAutoriza;
    }

    public void setTraFechaAutoriza(LocalDateTime traFechaAutoriza) {
        this.traFechaAutoriza = traFechaAutoriza;
    }

    public LocalDate getTraFechaTraslado() {
        return traFechaTraslado;
    }

    public void setTraFechaTraslado(LocalDate traFechaTraslado) {
        this.traFechaTraslado = traFechaTraslado;
    }

    public LocalDate getTraFechaRecepcion() {
        return traFechaRecepcion;
    }

    public void setTraFechaRecepcion(LocalDate traFechaRecepcion) {
        this.traFechaRecepcion = traFechaRecepcion;
    }

    public SgAfEstadosTraslado getTraTipoTrasladoFk() {
        return traTipoTrasladoFk;
    }

    public void setTraTipoTrasladoFk(SgAfEstadosTraslado traTipoTrasladoFk) {
        this.traTipoTrasladoFk = traTipoTrasladoFk;
    }

    public String getTraNombreAutoriza() {
        return traNombreAutoriza;
    }

    public void setTraNombreAutoriza(String traNombreAutoriza) {
        this.traNombreAutoriza = traNombreAutoriza;
    }

    public String getTraNombreRecibe() {
        return traNombreRecibe;
    }

    public void setTraNombreRecibe(String traNombreRecibe) {
        this.traNombreRecibe = traNombreRecibe;
    }

    public String getTraNombreReprAf() {
        return traNombreReprAf;
    }

    public void setTraNombreReprAf(String traNombreReprAf) {
        this.traNombreReprAf = traNombreReprAf;
    }

    public String getTraCargoAutoriza() {
        return traCargoAutoriza;
    }

    public void setTraCargoAutoriza(String traCargoAutoriza) {
        this.traCargoAutoriza = traCargoAutoriza;
    }

    public String getTraCargoRecibe() {
        return traCargoRecibe;
    }

    public void setTraCargoRecibe(String traCargoRecibe) {
        this.traCargoRecibe = traCargoRecibe;
    }

    public String getTraObservacion() {
        return traObservacion;
    }

    public void setTraObservacion(String traObservacion) {
        this.traObservacion = traObservacion;
    }

    public SgSede getTraSedeOrigenFk() {
        return traSedeOrigenFk;
    }

    public void setTraSedeOrigenFk(SgSede traSedeOrigenFk) {
        this.traSedeOrigenFk = traSedeOrigenFk;
    }

    public SgSede getTraSedeDestinoFk() {
        return traSedeDestinoFk;
    }

    public void setTraSedeDestinoFk(SgSede traSedeDestinoFk) {
        this.traSedeDestinoFk = traSedeDestinoFk;
    }

    public SgAfUnidadesAdministrativas getTraUnidadAdmOrigenFk() {
        return traUnidadAdmOrigenFk;
    }

    public void setTraUnidadAdmOrigenFk(SgAfUnidadesAdministrativas traUnidadAdmOrigenFk) {
        this.traUnidadAdmOrigenFk = traUnidadAdmOrigenFk;
    }

    public SgAfUnidadesAdministrativas getTraUnidadAdmDestinoFk() {
        return traUnidadAdmDestinoFk;
    }

    public void setTraUnidadAdmDestinoFk(SgAfUnidadesAdministrativas traUnidadAdmDestinoFk) {
        this.traUnidadAdmDestinoFk = traUnidadAdmDestinoFk;
    }

    public SgAfEstadosBienes getTraEstadoFk() {
        return traEstadoFk;
    }

    public void setTraEstadoFk(SgAfEstadosBienes traEstadoFk) {
        this.traEstadoFk = traEstadoFk;
    }

    public LocalDateTime getTraFechaCreacion() {
        return traFechaCreacion;
    }

    public void setTraFechaCreacion(LocalDateTime traFechaCreacion) {
        this.traFechaCreacion = traFechaCreacion;
    }

    public String getTraUsuarioCreacion() {
        return traUsuarioCreacion;
    }

    public void setTraUsuarioCreacion(String traUsuarioCreacion) {
        this.traUsuarioCreacion = traUsuarioCreacion;
    }

    public LocalDateTime getTraUltModFecha() {
        return traUltModFecha;
    }

    public void setTraUltModFecha(LocalDateTime traUltModFecha) {
        this.traUltModFecha = traUltModFecha;
    }

    public String getTraUltModUsuario() {
        return traUltModUsuario;
    }

    public void setTraUltModUsuario(String traUltModUsuario) {
        this.traUltModUsuario = traUltModUsuario;
    }

    public Integer getTraVersion() {
        return traVersion;
    }

    public void setTraVersion(Integer traVersion) {
        this.traVersion = traVersion;
    }

    public List<SgAfTrasladosDetalle> getSgAfTrasladosDetalle() {
        return sgAfTrasladosDetalle;
    }

    public void setSgAfTrasladosDetalle(List<SgAfTrasladosDetalle> sgAfTrasladosDetalle) {
        if(this.sgAfTrasladosDetalle != null) {
            this.sgAfTrasladosDetalle.clear();
            this.sgAfTrasladosDetalle.addAll(sgAfTrasladosDetalle);
        }
    }

    public List<SgAfMotivoRechazoTraslado> getSgAfMotivosRechazoTraslado() {
        return sgAfMotivosRechazoTraslado;
    }

    public void setSgAfMotivosRechazoTraslado(List<SgAfMotivoRechazoTraslado> sgAfMotivosRechazoTraslado) {
        if(this.sgAfMotivosRechazoTraslado != null) {
            this.sgAfMotivosRechazoTraslado.clear();
            this.sgAfMotivosRechazoTraslado.addAll(sgAfMotivosRechazoTraslado);
        }
    }

    public LocalDate getTraFechaRetono() {
        return traFechaRetono;
    }

    public void setTraFechaRetono(LocalDate traFechaRetono) {
        this.traFechaRetono = traFechaRetono;
    }

    public String getTraUnidadDestino() {
        return traUnidadDestino;
    }

    public void setTraUnidadDestino(String traUnidadDestino) {
        this.traUnidadDestino = traUnidadDestino;
    }

    public Integer getTraCorrelativo() {
        return traCorrelativo;
    }

    public void setTraCorrelativo(Integer traCorrelativo) {
        this.traCorrelativo = traCorrelativo;
    }

    public Integer getTraAnio() {
        return traAnio;
    }

    public void setTraAnio(Integer traAnio) {
        this.traAnio = traAnio;
    }

    public SgAfEmpleados getTraEmpleadoBienes() {
        return traEmpleadoBienes;
    }

    public void setTraEmpleadoBienes(SgAfEmpleados traEmpleadoBienes) {
        this.traEmpleadoBienes = traEmpleadoBienes;
    }

    public String getTraAsignadoABienes() {
        return traAsignadoABienes;
    }

    public void setTraAsignadoABienes(String traAsignadoABienes) {
        this.traAsignadoABienes = traAsignadoABienes;
    }

    @Override
    public String securityAmbitCreate() {
        return null;
    }

    @Override
    public CriteriaTO securityAmbitNavigation(OperationSecurity o, DaoOperation daoop) {
        if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.DEPARTAMENTAL.name())) {
            return CriteriaTOUtils.createORTO(
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "traSedeOrigenFk.sedDireccion.dirDepartamento.depPk", o.getContext()),
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "traUnidadAdmOrigenFk.uadUnidadActivoFijoFk.uafDepartamento.depPk", o.getContext()),
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "traSedeDestinoFk.sedDireccion.dirDepartamento.depPk", o.getContext()),
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "traUnidadAdmDestinoFk.uadUnidadActivoFijoFk.uafDepartamento.depPk", o.getContext())
            );
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.SEDE.name())) {
            return CriteriaTOUtils.createORTO(
                        CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "traSedeOrigenFk.sedPk", o.getContext()),
                        CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "traSedeDestinoFk.sedPk", o.getContext()),
                        CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "traUnidadAdmDestinoFk.uadPk", o.getContext())
                    );
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.UNIDAD_ADMINISTRATIVA.name())) {
            return CriteriaTOUtils.createORTO(
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "traUnidadAdmOrigenFk.uadPk", o.getContext()),
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "traUnidadAdmDestinoFk.uadPk", o.getContext()),
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "traSedeDestinoFk.sedPk", o.getContext())
                    );
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.MINED.name())) {
            return null;
        } else {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "traPk", -1L);
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (traPk != null ? traPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgAfTraslado)) {
            return false;
        }
        SgAfTraslado other = (SgAfTraslado) object;
        if ((this.traPk == null && other.traPk != null) || (this.traPk != null && !this.traPk.equals(other.traPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgAfTraslados[ traPk=" + traPk + " ]";
    }

}
