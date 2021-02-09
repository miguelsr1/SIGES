/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

/**
 *
 * @author Sofis Solutions
 */
public class RevHistorico {

    private Long objPk;
    private RevEntity revEntity;
    private RevisionType revType;

    public RevEntity getRevEntity() {
        return revEntity;
    }

    public void setRevEntity(RevEntity revEntity) {
        this.revEntity = revEntity;
    }

    public Long getObjPk() {
        return objPk;
    }

    public void setObjPk(Long objPk) {
        this.objPk = objPk;
    }

    public RevisionType getRevType() {
        return revType;
    }

    public void setRevType(RevisionType revType) {
        this.revType = revType;
    }

}
