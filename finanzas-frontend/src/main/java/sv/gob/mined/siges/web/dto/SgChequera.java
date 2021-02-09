/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "chePk", scope = SgChequera.class)
public class SgChequera implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long chePk;

    private SgCuentasBancarias cheCuentaBancariaFk;

    private LocalDate cheFechaChequera;

    private String cheSerie;

    private SgSede cheSedeFk;

    private Integer cheNumeroInicial;

    private Integer cheNumeroFinal;

    private LocalDateTime cheUltMod;

    private String cheUltUsuario;

    private Integer cheVersion;

    public SgChequera() {
    }

    @JsonIgnore
    public String getSerieComplete() {
        return this.cheSerie;
    }

    // <editor-fold defaultstate="collapsed" desc="Getters-Setters">
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getChePk() {
        return chePk;
    }

    public SgCuentasBancarias getCheCuentaBancariaFk() {
        return cheCuentaBancariaFk;
    }

    public void setCheCuentaBancariaFk(SgCuentasBancarias cheCuentaBancariaFk) {
        this.cheCuentaBancariaFk = cheCuentaBancariaFk;
    }

    public LocalDate getCheFechaChequera() {
        return cheFechaChequera;
    }

    public void setCheFechaChequera(LocalDate cheFechaChequera) {
        this.cheFechaChequera = cheFechaChequera;
    }

    public LocalDateTime getCheUltMod() {
        return cheUltMod;
    }

    public Integer getCheNumeroInicial() {
        return cheNumeroInicial;
    }

    public void setCheNumeroInicial(Integer cheNumeroInicial) {
        this.cheNumeroInicial = cheNumeroInicial;
    }

    public Integer getCheNumeroFinal() {
        return cheNumeroFinal;
    }

    public void setCheNumeroFinal(Integer cheNumeroFinal) {
        this.cheNumeroFinal = cheNumeroFinal;
    }

    public String getCheSerie() {
        return cheSerie;
    }

    public void setCheSerie(String cheSerie) {
        this.cheSerie = cheSerie;
    }

    public SgSede getCheSedeFk() {
        return cheSedeFk;
    }

    public void setCheSedeFk(SgSede cheSedeFk) {
        this.cheSedeFk = cheSedeFk;
    }

    public String getCheUltUsuario() {
        return cheUltUsuario;
    }

    public Integer getCheVersion() {
        return cheVersion;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Hash-Equals">
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (chePk != null ? chePk.hashCode() : 0);
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
        final SgChequera other = (SgChequera) obj;
        if (!Objects.equals(this.chePk, other.chePk)) {
            return false;
        }
        return true;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="toString">

    @Override
    public String toString() {
        return "com.sofis.entidades.SgChequera[ chePk=" + chePk + " ]";
    }
    // </editor-fold>
}
