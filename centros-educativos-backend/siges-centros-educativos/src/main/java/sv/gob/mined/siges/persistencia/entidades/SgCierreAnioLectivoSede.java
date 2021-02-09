/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sofis.security.DaoOperation;
import com.sofis.security.DataSecurity;
import com.sofis.security.OperationSecurity;
import com.sofis.sofisform.to.CriteriaTO;
import com.sofis.sofisform.to.MatchCriteriaTO;
import com.sofis.utils.CriteriaTOUtils;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumAmbito;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_cierre_anio_lectivo_sede", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "calPk", scope = SgCierreAnioLectivoSede.class)
public class SgCierreAnioLectivoSede implements DataSecurity, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cal_pk", nullable = false)
    private Long calPk;

    @JoinColumn(name = "cal_ale_fk", referencedColumnName = "ale_pk")
    @ManyToOne(optional = false)
    private SgAnioLectivo calAnioLectivo;

    @JoinColumn(name = "cal_sed_fk", referencedColumnName = "sed_pk")
    @ManyToOne(optional = false)
    private SgSede calSede;

    @Column(name = "cal_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime calUltModFecha;

    @Size(max = 45)
    @Column(name = "cal_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String calUltModUsuario;

    @Column(name = "cal_version")
    @Version
    private Integer calVersion;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "rpcCierreAnioLectivoSede", fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    @NotAudited
    private List<SgRelPreguntaCierreAnioSede> calRelPreguntaCierreAnio;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "rcsCierreAnioLectivoSede", fetch = FetchType.LAZY)
    @NotAudited
    private List<SgResumenCierreSeccion> calResumenSecciones;

    @Column(name = "cal_promociones_pendientes_masc")
    private Integer calPromocionesPendientesMasc;

    @Column(name = "cal_promociones_pendientes_fem")
    private Integer calPromocionesPendientesFem;

    @Column(name = "cal_promovidos_masc")
    private Integer calPromovidosMasc;

    @Column(name = "cal_promovidos_fem")
    private Integer calPromovidosFem;

    @Column(name = "cal_no_promovidos_masc")
    private Integer calNoPromovidosMasc;

    @Column(name = "cal_no_promovidos_fem")
    private Integer calNoPromovidosFem;

    @Column(name = "cal_asistencias")
    private Integer calAsistencias;

    @Column(name = "cal_inasistencias")
    private Integer calInasistencias;

    @Column(name = "cal_inasistencias_just")
    private Integer calInasistenciasJust;

    @Column(name = "cal_solicitudes_titulos_masc")
    private Integer calSolicitudesTitulosMasc;

    @Column(name = "cal_solicitudes_titulos_fem")
    private Integer calSolicitudesTitulosFem;

    @Column(name = "cal_fecha_cierre")
    private LocalDateTime calFechaCierre;

    @Size(max = 45)
    @Column(name = "cal_usuario_cierre", length = 45)
    private String calUsuarioCierre;

    @JoinColumn(name = "cal_archivo_firmado_fk")
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private SgArchivo calArchivoFirmado;

    @Column(name = "cal_firmado")
    private Boolean calFirmado;

    @Column(name = "cal_firma_transaction_id")
    private String calFirmaTransactionId; // Transaction id

    @Transient
    private String calTransactionSignatureUrl; // Url a donde redirigir para firmar

    @Transient
    private String calTransactionReturnUrl; // Url a donde redirigir luego de firma

    public SgCierreAnioLectivoSede() {
    }

    @JsonIgnore
    public void inferirDatosDeSecciones() {
        calPromocionesPendientesMasc = 0;
        calPromocionesPendientesFem = 0;
        calPromovidosMasc = 0;
        calPromovidosFem = 0;
        calNoPromovidosMasc = 0;
        calNoPromovidosFem = 0;
        calAsistencias = 0;
        calInasistencias = 0;
        calInasistenciasJust = 0;
        calSolicitudesTitulosMasc = 0;
        calSolicitudesTitulosFem = 0;

        for (SgResumenCierreSeccion cal : calResumenSecciones) {
            calPromocionesPendientesMasc += cal.getRcsPromocionesPendientesMasc();
            calPromocionesPendientesFem += cal.getRcsPromocionesPendientesFem();
            calPromovidosMasc += cal.getRcsPromovidosMasc();
            calPromovidosFem += cal.getRcsPromovidosFem();
            calNoPromovidosMasc += cal.getRcsNoPromovidosMasc();
            calNoPromovidosFem += cal.getRcsNoPromovidosFem();
            calAsistencias += cal.getRcsAsistencias();
            calInasistencias += cal.getRcsInasistencias();
            calInasistenciasJust += cal.getRcsInasistenciasJust();
            calSolicitudesTitulosMasc += cal.getRcsSolicitudesTitulosMasc();
            calSolicitudesTitulosFem += cal.getRcsSolicitudesTitulosFem();
        }

    }

    public Long getCalPk() {
        return calPk;
    }

    public void setCalPk(Long calPk) {
        this.calPk = calPk;
    }

    public SgAnioLectivo getCalAnioLectivo() {
        return calAnioLectivo;
    }

    public void setCalAnioLectivo(SgAnioLectivo calAnioLectivo) {
        this.calAnioLectivo = calAnioLectivo;
    }

    public SgSede getCalSede() {
        return calSede;
    }

    public void setCalSede(SgSede calSede) {
        this.calSede = calSede;
    }

    public LocalDateTime getCalUltModFecha() {
        return calUltModFecha;
    }

    public void setCalUltModFecha(LocalDateTime calUltModFecha) {
        this.calUltModFecha = calUltModFecha;
    }

    public String getCalUltModUsuario() {
        return calUltModUsuario;
    }

    public void setCalUltModUsuario(String calUltModUsuario) {
        this.calUltModUsuario = calUltModUsuario;
    }

    public Integer getCalVersion() {
        return calVersion;
    }

    public void setCalVersion(Integer calVersion) {
        this.calVersion = calVersion;
    }

    public List<SgRelPreguntaCierreAnioSede> getCalRelPreguntaCierreAnio() {
        return calRelPreguntaCierreAnio;
    }

    public void setCalRelPreguntaCierreAnio(List<SgRelPreguntaCierreAnioSede> calRelPreguntaCierreAnio) {
        this.calRelPreguntaCierreAnio = calRelPreguntaCierreAnio;
    }

    public List<SgResumenCierreSeccion> getCalResumenSecciones() {
        return calResumenSecciones;
    }

    public void setCalResumenSecciones(List<SgResumenCierreSeccion> calResumenSecciones) {
        this.calResumenSecciones = calResumenSecciones;
    }

    public Integer getCalPromocionesPendientesMasc() {
        return calPromocionesPendientesMasc;
    }

    public void setCalPromocionesPendientesMasc(Integer calPromocionesPendientesMasc) {
        this.calPromocionesPendientesMasc = calPromocionesPendientesMasc;
    }

    public Integer getCalPromocionesPendientesFem() {
        return calPromocionesPendientesFem;
    }

    public void setCalPromocionesPendientesFem(Integer calPromocionesPendientesFem) {
        this.calPromocionesPendientesFem = calPromocionesPendientesFem;
    }

    public Integer getCalPromovidosMasc() {
        return calPromovidosMasc;
    }

    public void setCalPromovidosMasc(Integer calPromovidosMasc) {
        this.calPromovidosMasc = calPromovidosMasc;
    }

    public Integer getCalPromovidosFem() {
        return calPromovidosFem;
    }

    public void setCalPromovidosFem(Integer calPromovidosFem) {
        this.calPromovidosFem = calPromovidosFem;
    }

    public Integer getCalNoPromovidosMasc() {
        return calNoPromovidosMasc;
    }

    public void setCalNoPromovidosMasc(Integer calNoPromovidosMasc) {
        this.calNoPromovidosMasc = calNoPromovidosMasc;
    }

    public Integer getCalNoPromovidosFem() {
        return calNoPromovidosFem;
    }

    public void setCalNoPromovidosFem(Integer calNoPromovidosFem) {
        this.calNoPromovidosFem = calNoPromovidosFem;
    }

    public Integer getCalAsistencias() {
        return calAsistencias;
    }

    public void setCalAsistencias(Integer calAsistencias) {
        this.calAsistencias = calAsistencias;
    }

    public Integer getCalInasistencias() {
        return calInasistencias;
    }

    public void setCalInasistencias(Integer calInasistencias) {
        this.calInasistencias = calInasistencias;
    }

    public Integer getCalInasistenciasJust() {
        return calInasistenciasJust;
    }

    public void setCalInasistenciasJust(Integer calInasistenciasJust) {
        this.calInasistenciasJust = calInasistenciasJust;
    }

    public Integer getCalSolicitudesTitulosMasc() {
        return calSolicitudesTitulosMasc;
    }

    public void setCalSolicitudesTitulosMasc(Integer calSolicitudesTitulosMasc) {
        this.calSolicitudesTitulosMasc = calSolicitudesTitulosMasc;
    }

    public Integer getCalSolicitudesTitulosFem() {
        return calSolicitudesTitulosFem;
    }

    public void setCalSolicitudesTitulosFem(Integer calSolicitudesTitulosFem) {
        this.calSolicitudesTitulosFem = calSolicitudesTitulosFem;
    }

    public LocalDateTime getCalFechaCierre() {
        return calFechaCierre;
    }

    public void setCalFechaCierre(LocalDateTime calFechaCierre) {
        this.calFechaCierre = calFechaCierre;
    }

    public String getCalUsuarioCierre() {
        return calUsuarioCierre;
    }

    public void setCalUsuarioCierre(String calUsuarioCierre) {
        this.calUsuarioCierre = calUsuarioCierre;
    }

    public SgArchivo getCalArchivoFirmado() {
        return calArchivoFirmado;
    }

    public void setCalArchivoFirmado(SgArchivo calArchivoFirmado) {
        this.calArchivoFirmado = calArchivoFirmado;
    }

    public Boolean getCalFirmado() {
        return calFirmado;
    }

    public void setCalFirmado(Boolean calFirmado) {
        this.calFirmado = calFirmado;
    }

    public String getCalFirmaTransactionId() {
        return calFirmaTransactionId;
    }

    public void setCalFirmaTransactionId(String calFirmaTransactionId) {
        this.calFirmaTransactionId = calFirmaTransactionId;
    }

    public String getCalTransactionSignatureUrl() {
        return calTransactionSignatureUrl;
    }

    public void setCalTransactionSignatureUrl(String calTransactionSignatureUrl) {
        this.calTransactionSignatureUrl = calTransactionSignatureUrl;
    }

    public String getCalTransactionReturnUrl() {
        return calTransactionReturnUrl;
    }

    public void setCalTransactionReturnUrl(String calTransactionReturnUrl) {
        this.calTransactionReturnUrl = calTransactionReturnUrl;
    }
    
    

    @Override
    public String securityAmbitCreate() {
        return "calSede";
    }

    @Override
    public CriteriaTO securityAmbitNavigation(OperationSecurity o, DaoOperation daoop) {
        if (o.getAmbit().equalsIgnoreCase(EnumAmbito.DEPARTAMENTAL.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "calSede.sedDireccion.dirDepartamento.depPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.SISTEMA_INTEGRADO.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "calSede.sedSistemas.sinPk.sinPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.SEDE.name())) {
            return CriteriaTOUtils.createORTO(
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "calSede.sedPk", o.getContext()),
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "calSede.sedSedeAdscritaDe.sedPk", o.getContext())
            );
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.MINED.name())) {
            return null;
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.PARTICION_SEDE.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN_SUBQUERY, "calSede.sedPk", o.getRegla());
        } else {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedPk", -1L);
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.calPk);
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
        final SgCierreAnioLectivoSede other = (SgCierreAnioLectivoSede) obj;
        if (!Objects.equals(this.calPk, other.calPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgCierreAnioLectivoSede{" + "calPk=" + calPk + '}';
    }
}
