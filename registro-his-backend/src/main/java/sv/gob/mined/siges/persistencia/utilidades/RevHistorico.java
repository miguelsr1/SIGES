/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.utilidades;

import org.hibernate.envers.RevisionType;
import sv.gob.mined.siges.persistencia.entidades.RevEntity;

/**
 *
 * @author Sofis Solutions
 */
public class RevHistorico {  

    private Object obj;
    private Long objPk;
    private RevEntity revEntity;
    private RevisionType revType;

    public Long getObjPk() {
        return objPk;
    }

    public void setObjPk(Long objPk) {
        this.objPk = objPk;
    }
    
    public RevEntity getRevEntity() {
        return revEntity;
    }

    public void setRevEntity(RevEntity revEntity) {
        this.revEntity = revEntity;
    }

    public RevisionType getRevType() {
        return revType;
    }

    public void setRevType(RevisionType revType) {
        this.revType = revType;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
    
    
}
