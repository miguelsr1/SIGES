/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package com.mined.siap2.descriptors;

import gob.mined.siap2.entities.constantes.Constantes;
import org.eclipse.persistence.config.DescriptorCustomizer;
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.history.HistoryPolicy;
import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.mappings.ManyToManyMapping;


/**
 * Esta clase permite gestionar las tablas históricas de las entidades del sistema.
 * @author sofis
 */
public class HistoryCustomizer implements DescriptorCustomizer {

    /**
     * Este método procesa el historial de cada entidad.
     * Cada entidad debe tener una tabla igual con _hist como sufijo.
     * En esa tabla, se agregan los datos start_date y end_date.
     * @param descriptor 
     */
    @Override
    public void customize(ClassDescriptor descriptor) {
        
        HistoryPolicy policy = new HistoryPolicy(); 
        policy.addHistoryTableName(Constantes.SCHEMA_SIAP2+"."+descriptor.getTableName()+"_hist");
        policy.addStartFieldName("START_DATE");
        policy.addEndFieldName("END_DATE");
        descriptor.setHistoryPolicy(policy);
        
        for (DatabaseMapping dm : descriptor.getMappings()) {
            //Las relaciones ManyToMany y las OneToMany con JoinTable las reconoce como ManyToMany.
            if (dm.isManyToManyMapping()){
                ManyToManyMapping mapping = (ManyToManyMapping) dm;
                HistoryPolicy policy2 = new HistoryPolicy();
                String tablaRelacion = mapping.getRelationTable().getName();
                policy2.addHistoryTableName(Constantes.SCHEMA_SIAP2+"."+tablaRelacion, Constantes.SCHEMA_SIAP2+"."+tablaRelacion+"_hist");
                policy2.addStartFieldName("START_DATE");
                policy2.addEndFieldName("END_DATE");
                mapping.setHistoryPolicy(policy2);
            } /** else if(dm.isOneToOneMapping()) {
                OneToOneMapping mapping = (OneToOneMapping) dm;
 
                ExpressionBuilder emp = new ExpressionBuilder(mapping.getReferenceClass());
                //Expression exp = emp.get("id").equal(mapping.getReferenceClass());
            
		mapping.setSelectionCriteria(emp.getBaseExpression());
            }  else if(dm.isOneToManyMapping()) {
                OneToManyMapping mapping = (OneToManyMapping) dm;
 
                ExpressionBuilder emp = new ExpressionBuilder();
                Expression exp = emp.get("id").equal(mapping.getReferenceClass());
            
		mapping.setSelectionCriteria(exp);
            }**/
        }
        
    }
    
}
