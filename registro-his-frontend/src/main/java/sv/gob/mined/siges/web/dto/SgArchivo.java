/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Transient;
import sv.gob.mined.siges.utils.SofisDateUtils;

/**
 *
 * @author Sofis Solutions
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "achPk", scope = SgArchivo.class)
public class SgArchivo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long achPk;

    private String achNombre;

    private String achDescripcion;

    private String achContentType;

    private String achTmpPath;

    private String achExt;

    private LocalDateTime achUltmodFecha;

    private String achUltmodUsuario;

    private Integer achVersion;

    @Transient
    private Boolean achShowTmpFile;

    public SgArchivo() {
    }

    public SgArchivo(Long achPk) {
        this.achPk = achPk;
    }
    

    public Long getAchPk() {
        return achPk;
    }

    public void setAchPk(Long achPk) {
        this.achPk = achPk;
    }

    public String getAchNombre() {
        return achNombre;
    }

    public void setAchNombre(String achNombre) {
        this.achNombre = achNombre;
    }

    public String getAchDescripcion() {
        return achDescripcion;
    }

    public void setAchDescripcion(String achDescripcion) {
        this.achDescripcion = achDescripcion;
    }

    public String getAchContentType() {
        return achContentType;
    }

    public void setAchContentType(String achContentType) {
        this.achContentType = achContentType;
    }

    public String getAchExt() {
        return achExt;
    }

    public void setAchExt(String achExt) {
        this.achExt = achExt;
    }

    public LocalDateTime getAchUltmodFecha() {
        return achUltmodFecha;
    }

    public Date getAchUltmodFechaAsDate() {
        if (achUltmodFecha != null) {
            return SofisDateUtils.asDate(achUltmodFecha);
        }
        return null;
    }

    public void setAchUltmodFecha(LocalDateTime achUltmodFecha) {
        this.achUltmodFecha = achUltmodFecha;
    }

    public String getAchUltmodUsuario() {
        return achUltmodUsuario;
    }

    public void setAchUltmodUsuario(String achUltmodUsuario) {
        this.achUltmodUsuario = achUltmodUsuario;
    }

    public Integer getAchVersion() {
        return achVersion;
    }

    public void setAchVersion(Integer achVersion) {
        this.achVersion = achVersion;
    }

    public String getAchTmpPath() {
        return achTmpPath;
    }

    public void setAchTmpPath(String achTmpPath) {
        this.achTmpPath = achTmpPath;
    }

    public Boolean getAchShowTmpFile() {
        return achShowTmpFile != null ? achShowTmpFile : Boolean.FALSE;
    }

    public void setAchShowTmpFile(Boolean achShowTmpFile) {
        this.achShowTmpFile = achShowTmpFile;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.achPk);
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
        final SgArchivo other = (SgArchivo) obj;
        if (!Objects.equals(this.achPk, other.achPk)) {
            return false;
        }
        return true;
    }

}
