/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.utilidades;

import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.StandardBasicTypes;

/**
 *
 * @author Sofis Solutions
 */
public class CustomPostgresDialect extends org.hibernate.dialect.PostgreSQL94Dialect {
    
    public CustomPostgresDialect() {
        super();
        registerFunction("ilike", new SQLFunctionTemplate(StandardBasicTypes.BOOLEAN, "?1 ilike ?2", false));
    }
}
