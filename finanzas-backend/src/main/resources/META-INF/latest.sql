-- 1.24.2
-- COLUMNA DE OTRO INGRESO DE CAJA CHICA
ALTER TABLE finanzas.sg_cajas_chicas ADD COLUMN bcc_otro_ingreso  boolean;
ALTER TABLE finanzas.sg_cajas_chicas_aud ADD COLUMN bcc_otro_ingreso  boolean;
COMMENT ON COLUMN finanzas.sg_cajas_chicas.bcc_otro_ingreso IS 'Indica si el registro es  de otro ingreso marcado(true).';

ALTER TABLE finanzas.sg_cajas_chicas ADD COLUMN bcc_subcomponente_fk  bigint;
ALTER TABLE finanzas.sg_cajas_chicas_aud ADD COLUMN bcc_subcomponente_fk  bigint;
COMMENT ON COLUMN finanzas.sg_cajas_chicas.bcc_subcomponente_fk IS 'Número de referencia al subcomponente del registro.';


ALTER TABLE finanzas.sg_cajas_chicas ADD COLUMN bcc_anio_fk  bigint;
ALTER TABLE finanzas.sg_cajas_chicas_aud ADD COLUMN bcc_anio_fk  bigint;
COMMENT ON COLUMN finanzas.sg_cajas_chicas.bcc_ano_fk IS 'Número de referencia al año electivo del registro.';

ALTER TABLE finanzas.sg_cajas_chicas ADD COLUMN bcc_cuenta_banc_fk  bigint;
ALTER TABLE finanzas.sg_cajas_chicas_aud ADD COLUMN bcc_cuenta_banc_fk  bigint;
COMMENT ON COLUMN finanzas.sg_cajas_chicas.bcc_cuenta_banc_fk IS 'Origen de fondos, número de referencia a la cuenta bancaria del registro.';


ALTER TABLE finanzas.sg_cajas_chicas ADD CONSTRAINT bcc_subcomponente_fkey FOREIGN KEY (bcc_subcomponente_fk) 
REFERENCES siap2.ss_ges_pres_es(ges_id) MATCH FULL ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE finanzas.sg_cajas_chicas ADD CONSTRAINT bcc_anio_fkey FOREIGN KEY (bcc_anio_fk) 
REFERENCES centros_educativos.sg_anio_lectivo(ale_pk) MATCH FULL ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE finanzas.sg_cajas_chicas ADD CONSTRAINT bcc_cuenta_banc_fkey FOREIGN KEY (bcc_cuenta_banc_fk) 
REFERENCES finanzas.sg_cuentas_bancarias(cbc_pk) MATCH FULL ON DELETE CASCADE ON UPDATE CASCADE;

--1.24.6
-- CREACION DE COLUMNA SEDE PARA CHEQUERAS
ALTER TABLE finanzas.sg_chequeras ADD che_sede_fk int8 NULL;
ALTER TABLE finanzas.sg_chequeras_aud ADD che_sede_fk int8 NULL;

-- CREACION DE COLUMNA CHEQUERA EN MOVIMIENTO CAJA CHICA
ALTER TABLE finanzas.sg_movimiento_caja_chica ADD mcc_chequera_fk int8 NULL;
COMMENT ON COLUMN finanzas.sg_movimiento_caja_chica.mcc_chequera_fk IS 'Valor que indica la chequera a la que esta relacionada';
-- CREACION DE COLUMNA CHEQUERA EN MOVIMIENTO CAJA CHICA AUD
ALTER TABLE finanzas.sg_movimiento_caja_chica_aud ADD mcc_chequera_fk int8 NULL;

-- CREACION DE COLUMNA CHEQUE EN MOVIMIENTO CAJA CHICA
ALTER TABLE finanzas.sg_movimiento_caja_chica ADD mcc_cheque varchar NULL;
COMMENT ON COLUMN finanzas.sg_movimiento_caja_chica.mcc_cheque IS 'Cheque relacionado al ingreso';
-- CREACION DE COLUMNA CHEQUE EN MOVIMIENTO CAJA CHICA AUD
ALTER TABLE finanzas.sg_movimiento_caja_chica_aud ADD mcc_cheque varchar NULL;

-- CREACION DE COLUMNA TIPO DE INGRESO EN CAJA CHICA
ALTER TABLE finanzas.sg_movimiento_caja_chica ADD mcc_tipo_ingreso varchar NULL;
COMMENT ON COLUMN finanzas.sg_movimiento_caja_chica.mcc_tipo_ingreso IS 'Indica el tipo de ingreso realizado';

-- CREACION DE COLUMNA TIPO DE INGRESO EN CAJA CHICA AUD
ALTER TABLE finanzas.sg_movimiento_caja_chica_aud ADD mcc_tipo_ingreso varchar NULL;


--1.25.1
-- CREACION DE LA COLUMNA PARA OBSERVACIONES DE PRESUPUESTO
ALTER TABLE finanzas.sg_presupuesto_escolar ADD pes_observacion varchar NULL;
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar.pes_observacion IS 'Indica las observaciones realizadas al registro.';

-- CREACION DE LA COLUMNA PARA OBSERVACIONES DE PRESUPUESTO AUD
ALTER TABLE finanzas.sg_presupuesto_escolar_aud ADD pes_observacion varchar NULL;

-- CREACION DE LA COLUMNA TIPO DE DESEMBOLSO PARA DESEMBOLSO CED
ALTER TABLE finanzas.sg_desembolsos_ced ADD dsb_tipo_desembolso varchar NULL;
COMMENT ON COLUMN finanzas.sg_desembolsos_ced.dsb_tipo_desembolso IS 'Indica el tipo de desembolso realizado';

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('EVALUAR_PRESUPUESTO','FP14',  'Permite evaluar el presupuesto escolar', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('OBSERVAR_PRESUPUESTO','FP15',  'Permite observar el presupuesto escolar', 12, true, null, null,0);


ALTER TABLE finanzas.sg_movimientos_liquidacion ADD COLUMN mlq_movimiento_cc_pk  bigint;
ALTER TABLE finanzas.sg_movimientos_liquidacion_aud ADD COLUMN mlq_movimiento_cc_pk  bigint;
COMMENT ON COLUMN finanzas.sg_movimientos_liquidacion.mlq_movimiento_cc_pk IS 'Número de referencia al movimiento de caja chica del registro.';

-- CREACION DE LA COLUMNA TIPO DE DESEMBOLSO PARA DESEMBOLSO CED AUD
ALTER TABLE finanzas.sg_desembolsos_ced_aud ADD dsb_tipo_desembolso varchar NULL;


-- OPERACION PARA CERRAR PRESUPUESTO APROBADO
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CERRAR_PRESUPUESTO','FP16',  'Permite cerrar un presupuesto aprobado', 12, true, null, null,0);


ALTER TABLE finanzas.sg_movimientos_liquidacion ALTER COLUMN mlq_movimiento_pk DROP NOT NULL;

-- 1.25.2
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('IMPRIMIR_DOCUMENTOS_PRESUPUESTOS','FP17',  'Permite imprimir documentos del presupuesto', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CAMBIAR_ESTADOS_PRESUPUESTO','FP18',  'Permite imprimir documentos del presupuesto', 12, true, null, null,0);