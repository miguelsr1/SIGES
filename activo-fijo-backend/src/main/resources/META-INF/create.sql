CREATE SCHEMA IF NOT EXISTS activo_fijo;


--NUEVA CATEGORIA--ACTIVO FIJO
INSERT INTO seguridad.sg_categorias_operacion(cop_pk, cop_codigo, cop_nombre, cop_descripcion, cop_habilitado, cop_ult_mod_fecha, cop_ult_mod_usuario, cop_version) VALUES(5, 'af', 'Activo Fijo', 'Activo Fijo', true, null, null, 0);


-- 
--PERMISOS DE VISULIAZACIÓN DE MENUS
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_CARGO_BIENES','AM1',  '', 5, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_DESCARGO_BIENES','AM2',  '', 5, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_TRASLADO_BIENES','AM3',  '', 5, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_CALCULAR_DEPRECIACION','AM4',  '', 5, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_LIQUIDACION_PROYECTOS','AM5',  '', 5, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_REPORTE_DESCARGO_BIENES','AM6',  '', 5, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_REPORTE_VEHICULOS','AM7',  '', 5, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_REPORTE_ACTA_RESPONSABILIDAD','AM8',  '', 5, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_REPORTE_CONTROL_CORRELATIVOS','AM9',  '', 5, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_REPORTE_BIENES_SUBCUENTAS_CONTABLES','AM10',  '', 5, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_REPORTE_DEPRECIACION_BIEN','AM11',  '', 5, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_REPORTE_ACTUALIZACION_INVENTARIO','AM12',  '', 5, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_REPORTE_DEPRECIACION_TIPO_BIEN','AM13',  '', 5, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_REPORTE_BIENES_INGRESADOS_EDITADOS','AM14',  '', 5, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_REPORTE_NOTIFICACION_CUMPLIMIENTO','AM15',  '', 5, true, null, null,0);

--PERMISOS DE VISUALIZACION DE CAMPOS
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_CAMPO_TIPO_UNIDAD','AP1',  '', 5, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_CAMPO_UNIDAD_ACTIVO_FIJO','AP2',  '', 5, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_CAMPO_UNIDAD_ADMINISTRATIVA','AP3',  '', 5, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_CAMPO_CALIDAD','AP4',  '', 5, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_CAMPO_FUENTE','AP5',  '', 5, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_CAMPO_ASIGNADO_A','AP6',  '', 5, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_CAMPO_NO_SERIE','AP7',  '', 5, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_CAMPO_CODIGO_INVENTARIO','AP8',  '', 5, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_CAMPO_FECHA_ADQUISICION','AP9',  '', 5, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_CAMPO_FECHA_CREACION','AP10',  '', 5, true, null, null,0);


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_CAMPO_ESTADO','AP11',  '', 5, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_CAMPO_ACTIVO_FIJO','AP12',  '', 5, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_CAMPO_TIPO_BIEN','AP13',  '', 5, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_CAMPO_CATEGORIA','AP14',  '', 5, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_CAMPO_PROYECTO','AP15',  '', 5, true, null, null,0);


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_CAMPO_MARCA','AP16',  '', 5, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_CAMPO_MODELO','AP17',  '', 5, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_CAMPO_VALOR_ADQUISICION','AP18',  '', 5, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_CAMPO_PRECIO','AP19',  '', 5, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_CAMPO_ESTADO_SOLICITUD','AP20',  '', 5, true, null, null,0);


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_CAMPO_ACTIVOS','AP21',  '', 5, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_CAMPO_NUMERO_SOLICITUD','AP22',  '', 5, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_CAMPO_FECHA_SOLICITUD','AP23',  '', 5, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_CAMPO_FORMA_ADQUISICION','AP24',  '', 5, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_CAMPO_MUNICIPIO','AP25',  '', 5, true, null, null,0);


--VISUALIZACIÓN DE BOTONES
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_BOTON_IMPRIMIR','AP26',  '', 5, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_BOTON_REPORTE','AP27',  '', 5, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_BOTON_ACTA','AP28',  '', 5, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_BOTON_CONSTANCIA','AP29',  '', 5, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_BOTON_SOLVENCIA','AP30',  '', 5, true, null, null,0);

--VISUALIZACIÓN DE PANELES
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_PANEL_ORIGEN','AP31',  '', 5, true, null, null,0);

--SEGURIDAD
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('TIENE_ACCESO_ACTIVO_FIJO','W5',  '', 4, true, null, null,0);

--OPERACIONES---
--BIENES DEPRECIABLES---
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_BIENES_DEPRECIABLES','AFB1',  '', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_BIEN_DEPRECIABLE','AF1',  '', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_BIEN_DEPRECIABLE','AF2',  '', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_BIEN_DEPRECIABLE','AF3',  '', 5, true, null, null,0);


--VISUALIZACION DE CAMPOS DE FECHAS                                                                        
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_CAMPO_FECHA_MODIFICACION', 'AP32', 'Permiso que permite visualizar el campo Fecha de Modificación', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_CAMPO_USUARIO_MODIFICACION','AP33',  'Permiso que permite ver el campo Usuario de Modificación', 5, true, null, null,0);

--PERMISOS PARA EDITAR BIENES
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('EDITAR_CORRELATIVO','AP34',  'Permiso que permite editar todos los correlativos', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('EDITAR_CORRELATIVO_VALOR_ADQUISICION_AF','AP35',  'Permiso que permite editar correlativos de activo fijo', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_CAMPO_NUMERO_PARTIDA','AP36',  'Permiso que permite visualizar el campo Número de Partida', 5, true, null, null,0);


-- 1.2.0

CREATE SCHEMA IF NOT EXISTS activo_fijo;

--TABLA BIENES DEPRECIABLES (ACTIVOS)
CREATE SEQUENCE activo_fijo.sg_af_bienes_depreciables_pk_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
    NO CYCLE;

CREATE TABLE activo_fijo.sg_af_bienes_depreciables (
	bde_pk bigserial NOT NULL, -- Número correlativo autogenerado.
	bde_codigo_correlativo varchar(10) NOT NULL, -- Código Correlativo por tipo de bien
	bde_codigo_inventario varchar(20) NOT NULL, -- Código de Inventario.
	bde_asignado_a varchar(100) NULL, -- Indica la asignación de un Registro.
	bde_descripcion varchar(750) NULL, -- Descripción del Registro.
	bde_observacion varchar(150) NULL, -- Observación del Registro.
	bde_marca varchar(150) NULL, -- Marca del Registro.
	bde_modelo varchar(100) NULL, -- Modelo del Registro.
	bde_no_serie varchar(100) NULL, -- Número de serie del Registro.
	bde_no_placa varchar(20) NULL, -- Número de placa del Registro.
	bde_no_motor varchar(100) NULL, -- Número de motor del Registro.
	bde_anio int4 NULL, -- Anio del Registro.
	bde_no_chasis varchar(100) NULL, -- Número de chasis del Registro.
	bde_no_vin varchar(20) NULL, -- Número VIN del Registro.
	bde_color_carroceria varchar(50) NULL, -- Color de Carroceria del Registro.
	bde_documento_adquisicion varchar(100) NULL, -- Documento de adquisición.
	bde_fecha_adquisicion timestamp NOT NULL, -- Fecha de adquisición del Registro
	bde_fecha_creacion timestamp NOT NULL, -- Fecha de creación del Registro
	bde_valor_adquisicion numeric NOT NULL, -- Valor de adquisición del Registro
	bde_proveedor varchar(100) NULL, -- Proveedor de servicio
	bde_cantidad_lote int4 NULL, -- Cantidad a replicar a partir del Registro
	bde_unidad_administrativa_fk int8 NULL, -- Unidad Administrativa a la que pertenece el Registro
	bde_calidad_fk int8 NOT NULL, -- Calidad o estado del Registro
	bde_tipo_bien_fk int8 NOT NULL, -- Tipo de bien del Registro
	bde_forma_adquisicion_fk int8 NULL, -- Forma en que se adquirió el Registro
	bde_fuente_financiamiento_fk int8 NOT NULL, -- Fuente de Financiamiento del Registro
	bde_proyecto_fk int8 NULL, -- Proyecto en que se adquirió el Registro
	bde_estado_fk int8 NOT NULL, -- Estado del Registro
	bde_ult_mod_fecha timestamp NULL, -- Última fecha en la que se modificó el registro.
	bde_ult_mod_usuario varchar(45) NULL, -- Último usuario que modificó el registro.
	bde_version int4 NULL, -- Versión del registro
	bde_es_lote bool NULL, -- Indica si se procesa en lote o no
	bde_es_valor_estimado bool NULL, -- Indica si el valor del Registro es estimado o no
	bde_numero_partida varchar(30) NULL, -- Número de partida del registro
	bde_estado_proceso_lote int2 NULL,
	bde_sede_fk int8 NULL,
	bde_num_correlativo int4 NULL,
	bde_codigo_unidad varchar(10) NULL,
	bde_fecha_descargo timestamp NULL,
	bde_valor_residual numeric(20,2) NULL,
	bde_vida_util numeric(20) NULL,
	bde_fecha_reg_contable timestamp NULL,
	bde_fecha_recepcion timestamp NULL,
	bde_observacion_dde varchar(150) NULL,
	bde_empleado_fk int8 NULL,
	CONSTRAINT bienes_depreciables_pk PRIMARY KEY (bde_pk),
	CONSTRAINT codigo_inventario_uk UNIQUE (bde_codigo_inventario),
	CONSTRAINT bde_empleado_fk FOREIGN KEY (bde_empleado_fk) REFERENCES catalogo.sg_af_empleados(emp_pk),
	CONSTRAINT calidad_fk FOREIGN KEY (bde_calidad_fk) REFERENCES catalogo.sg_af_estados_calidad(eca_pk),
	CONSTRAINT estado_fk FOREIGN KEY (bde_estado_fk) REFERENCES catalogo.sg_af_estados_bienes(ebi_pk),
	CONSTRAINT fomas_adquisicion_fk FOREIGN KEY (bde_forma_adquisicion_fk) REFERENCES catalogo.sg_af_fomas_adquisicion(fad_pk),
	CONSTRAINT fuente_financiamiento_fk FOREIGN KEY (bde_fuente_financiamiento_fk) REFERENCES catalogo.sg_af_fuente_financiamiento(ffi_pk),
	CONSTRAINT proyecto_fk FOREIGN KEY (bde_proyecto_fk) REFERENCES catalogo.sg_af_proyectos(pro_pk),
	CONSTRAINT sede_fk FOREIGN KEY (bde_sede_fk) REFERENCES centros_educativos.sg_sedes(sed_pk),
	CONSTRAINT tipo_bien_fk FOREIGN KEY (bde_tipo_bien_fk) REFERENCES catalogo.sg_af_tipo_bienes(tbi_pk),
	CONSTRAINT unidad_administrativa_fk FOREIGN KEY (bde_unidad_administrativa_fk) REFERENCES catalogo.sg_af_unidades_administrativas(uad_pk)
);
COMMENT ON TABLE activo_fijo.sg_af_bienes_depreciables IS 'Tabla para el registro de Bienes Depreciables (Activos).';

ALTER TABLE activo_fijo.sg_af_bienes_depreciables ALTER bde_pk SET DEFAULT nextval('activo_fijo.sg_af_bienes_depreciables_pk_seq');

-- Column comments

COMMENT ON COLUMN activo_fijo.sg_af_bienes_depreciables.bde_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN activo_fijo.sg_af_bienes_depreciables.bde_codigo_correlativo IS 'Código Correlativo por tipo de bien';
COMMENT ON COLUMN activo_fijo.sg_af_bienes_depreciables.bde_codigo_inventario IS 'Código de Inventario.';
COMMENT ON COLUMN activo_fijo.sg_af_bienes_depreciables.bde_asignado_a IS 'Indica la asignación de un Registro.';
COMMENT ON COLUMN activo_fijo.sg_af_bienes_depreciables.bde_descripcion IS 'Descripción del Registro.';
COMMENT ON COLUMN activo_fijo.sg_af_bienes_depreciables.bde_observacion IS 'Observación del Registro.';
COMMENT ON COLUMN activo_fijo.sg_af_bienes_depreciables.bde_marca IS 'Marca del Registro.';
COMMENT ON COLUMN activo_fijo.sg_af_bienes_depreciables.bde_modelo IS 'Modelo del Registro.';
COMMENT ON COLUMN activo_fijo.sg_af_bienes_depreciables.bde_no_serie IS 'Número de serie del Registro.';
COMMENT ON COLUMN activo_fijo.sg_af_bienes_depreciables.bde_no_placa IS 'Número de placa del Registro.';
COMMENT ON COLUMN activo_fijo.sg_af_bienes_depreciables.bde_no_motor IS 'Número de motor del Registro.';
COMMENT ON COLUMN activo_fijo.sg_af_bienes_depreciables.bde_anio IS 'Anio del Registro.';
COMMENT ON COLUMN activo_fijo.sg_af_bienes_depreciables.bde_no_chasis IS 'Número de chasis del Registro.';
COMMENT ON COLUMN activo_fijo.sg_af_bienes_depreciables.bde_no_vin IS 'Número VIN del Registro.';
COMMENT ON COLUMN activo_fijo.sg_af_bienes_depreciables.bde_color_carroceria IS 'Color de Carroceria del Registro.';
COMMENT ON COLUMN activo_fijo.sg_af_bienes_depreciables.bde_documento_adquisicion IS 'Documento de adquisición.';
COMMENT ON COLUMN activo_fijo.sg_af_bienes_depreciables.bde_fecha_adquisicion IS 'Fecha de adquisición del Registro';
COMMENT ON COLUMN activo_fijo.sg_af_bienes_depreciables.bde_fecha_creacion IS 'Fecha de creación del Registro';
COMMENT ON COLUMN activo_fijo.sg_af_bienes_depreciables.bde_valor_adquisicion IS 'Valor de adquisición del Registro';
COMMENT ON COLUMN activo_fijo.sg_af_bienes_depreciables.bde_proveedor IS 'Proveedor de servicio';
COMMENT ON COLUMN activo_fijo.sg_af_bienes_depreciables.bde_cantidad_lote IS 'Cantidad a replicar a partir del Registro';
COMMENT ON COLUMN activo_fijo.sg_af_bienes_depreciables.bde_unidad_administrativa_fk IS 'Unidad Administrativa a la que pertenece el Registro';
COMMENT ON COLUMN activo_fijo.sg_af_bienes_depreciables.bde_calidad_fk IS 'Calidad o estado del Registro';
COMMENT ON COLUMN activo_fijo.sg_af_bienes_depreciables.bde_tipo_bien_fk IS 'Tipo de bien del Registro';
COMMENT ON COLUMN activo_fijo.sg_af_bienes_depreciables.bde_forma_adquisicion_fk IS 'Forma en que se adquirió el Registro';
COMMENT ON COLUMN activo_fijo.sg_af_bienes_depreciables.bde_fuente_financiamiento_fk IS 'Fuente de Financiamiento del Registro';
COMMENT ON COLUMN activo_fijo.sg_af_bienes_depreciables.bde_proyecto_fk IS 'Proyecto en que se adquirió el Registro';
COMMENT ON COLUMN activo_fijo.sg_af_bienes_depreciables.bde_estado_fk IS 'Estado del Registro';
COMMENT ON COLUMN activo_fijo.sg_af_bienes_depreciables.bde_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN activo_fijo.sg_af_bienes_depreciables.bde_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN activo_fijo.sg_af_bienes_depreciables.bde_version IS 'Versión del registro';
COMMENT ON COLUMN activo_fijo.sg_af_bienes_depreciables.bde_es_lote IS 'Indica si se procesa en lote o no';
COMMENT ON COLUMN activo_fijo.sg_af_bienes_depreciables.bde_es_valor_estimado IS 'Indica si el valor del Registro es estimado o no';
COMMENT ON COLUMN activo_fijo.sg_af_bienes_depreciables.bde_numero_partida IS 'Número de partida del registro';


CREATE TABLE activo_fijo.sg_af_bienes_depreciables_aud (
	bde_pk bigserial NOT NULL,
	bde_codigo_correlativo varchar(10) NOT NULL,
	bde_codigo_inventario varchar(20) NOT NULL,
	bde_asignado_a varchar(100) NULL,
	bde_descripcion varchar(750) NULL,
	bde_observacion varchar(150) NULL,
	bde_marca varchar(50) NULL,
	bde_modelo varchar(100) NULL,
	bde_no_serie varchar(100) NULL,
	bde_no_placa varchar(20) NULL,
	bde_no_motor varchar(100) NULL,
	bde_anio int4 NULL,
	bde_no_chasis varchar(50) NULL,
	bde_no_vin varchar(20) NULL,
	bde_color_carroceria varchar(50) NULL,
	bde_documento_adquisicion varchar(100) NULL,
	bde_fecha_adquisicion timestamp NOT NULL,
	bde_fecha_creacion timestamp NOT NULL,
	bde_valor_adquisicion numeric NOT NULL,
	bde_proveedor varchar(100) NULL,
	bde_cantidad_lote int4 NULL,
	bde_unidad_administrativa_fk int8 NULL,
	bde_calidad_fk int8 NOT NULL,
	bde_tipo_bien_fk int8 NOT NULL,
	bde_forma_adquisicion_fk int8 NULL,
	bde_fuente_financiamiento_fk int8 NOT NULL,
	bde_proyecto_fk int8 NULL,
	bde_estado_fk int8 NOT NULL,
	bde_ult_mod_fecha timestamp NULL,
	bde_ult_mod_usuario varchar(45) NULL,
	bde_version int4 NULL,
	bde_es_lote bool NULL,
	bde_es_valor_estimado bool NULL,
	bde_unidad_administrativa_id int8 NULL,
	rev int8 NOT NULL,
	revtype int2 NULL,
	bde_numero_partida varchar(30) NULL,
	bde_estado_proceso_lote int2 NULL,
	bde_sede_fk int8 NULL,
	bde_num_correlativo int4 NULL,
	bde_codigo_unidad varchar(10) NULL,
	bde_fecha_descargo timestamp NULL,
	bde_valor_residual numeric(20,2) NULL,
	bde_vida_util numeric(20) NULL,
	bde_fecha_reg_contable timestamp NULL,
	bde_fecha_recepcion timestamp NULL,
	bde_observacion_dde varchar(150) NULL,
	bde_empleado_fk int8 NULL,
	CONSTRAINT sg_af_bienes_depreciables_aud_pkey PRIMARY KEY (bde_pk, rev)
);

--TABLA DESCARGOS

CREATE SEQUENCE activo_fijo.sg_af_descargos_pk_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
    NO CYCLE;

CREATE TABLE activo_fijo.sg_af_descargos (
	des_pk bigserial NOT NULL, -- Número correlativo autogenerado.
	des_codigo_descargo varchar(50) NULL, -- Código de descargo del registro
	des_nombre_coord_adm varchar(150) NULL, -- Nombre del coordinador
	des_nombre_encargado_af varchar(150) NULL, -- Nombre del encargado de activo fijo
	des_nombre_director varchar(150) NULL, -- Nombre del director del registro
	des_nombre_autoriza varchar(150) NULL, -- Nombre de quien autoriza el descargo
	des_cargo_autoriza varchar(150) NULL,
	des_observacion_fallo varchar(250) NULL, -- Observación del fallo del descargo
	des_fecha_creacion timestamp NULL, -- Fecha de creación del registro
	des_fecha_solicitud timestamp NULL, -- Fecha de solicitud de descargo
	des_fecha_descargo timestamp NULL, -- Fecha de descargo
	des_fecha_fallo timestamp NULL, -- Fecha de fallo del descargo
	des_tipo_descargo_fk int8 NULL, -- Tipo de Descargo
	des_sede_fk int8 NULL, -- LLave foranea de la sede o centro educativo
	des_unidad_administrativa_fk int8 NULL, -- LLave foranea de la Unidad Administrativa
	des_estado_fk int8 NULL, -- LLave foranea del estado del descargo
	des_activo bool NULL, -- Determina si un bien es activo mayor al limite minimo de activo fijo
	des_usuario_creacion varchar(45) NULL, -- Fecha en la que se creó el registro.
	des_ult_mod_fecha timestamp NULL, -- Última fecha en la que se modificó el registro.
	des_ult_mod_usuario varchar(45) NULL, -- Último usuario que modificó el registro.
	des_version int2 NULL, -- Versión del registro
	des_codigo_ua varchar(10) NULL,
	des_codigo_ce varchar(10) NULL,
	des_fecha_envio_solicitud timestamp NULL,
	CONSTRAINT descargos_pk PRIMARY KEY (des_pk),
	CONSTRAINT estado_fk FOREIGN KEY (des_estado_fk) REFERENCES catalogo.sg_af_estados_bienes(ebi_pk),
	CONSTRAINT sede_fk FOREIGN KEY (des_sede_fk) REFERENCES centros_educativos.sg_sedes(sed_pk),
	CONSTRAINT tipo_descargo_fk FOREIGN KEY (des_tipo_descargo_fk) REFERENCES catalogo.sg_af_estados_descargo(ede_pk),
	CONSTRAINT unidad_administrativa_fk FOREIGN KEY (des_unidad_administrativa_fk) REFERENCES catalogo.sg_af_unidades_administrativas(uad_pk)
);
COMMENT ON TABLE activo_fijo.sg_af_descargos IS 'Tabla para el registro de los descargos.';

ALTER TABLE activo_fijo.sg_af_descargos ALTER des_pk SET DEFAULT nextval('activo_fijo.sg_af_descargos_pk_seq');

-- Column comments

COMMENT ON COLUMN activo_fijo.sg_af_descargos.des_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN activo_fijo.sg_af_descargos.des_codigo_descargo IS 'Código de descargo del registro';
COMMENT ON COLUMN activo_fijo.sg_af_descargos.des_nombre_coord_adm IS 'Nombre del coordinador';
COMMENT ON COLUMN activo_fijo.sg_af_descargos.des_nombre_encargado_af IS 'Nombre del encargado de activo fijo';
COMMENT ON COLUMN activo_fijo.sg_af_descargos.des_nombre_director IS 'Nombre del director del registro';
COMMENT ON COLUMN activo_fijo.sg_af_descargos.des_nombre_autoriza IS 'Nombre de quien autoriza el descargo';
COMMENT ON COLUMN activo_fijo.sg_af_descargos.des_observacion_fallo IS 'Observación del fallo del descargo';
COMMENT ON COLUMN activo_fijo.sg_af_descargos.des_fecha_creacion IS 'Fecha de creación del registro';
COMMENT ON COLUMN activo_fijo.sg_af_descargos.des_fecha_solicitud IS 'Fecha de solicitud de descargo';
COMMENT ON COLUMN activo_fijo.sg_af_descargos.des_fecha_descargo IS 'Fecha de descargo';
COMMENT ON COLUMN activo_fijo.sg_af_descargos.des_fecha_fallo IS 'Fecha de fallo del descargo';
COMMENT ON COLUMN activo_fijo.sg_af_descargos.des_tipo_descargo_fk IS 'Tipo de Descargo';
COMMENT ON COLUMN activo_fijo.sg_af_descargos.des_sede_fk IS 'LLave foranea de la sede o centro educativo';
COMMENT ON COLUMN activo_fijo.sg_af_descargos.des_unidad_administrativa_fk IS 'LLave foranea de la Unidad Administrativa';
COMMENT ON COLUMN activo_fijo.sg_af_descargos.des_estado_fk IS 'LLave foranea del estado del descargo';
COMMENT ON COLUMN activo_fijo.sg_af_descargos.des_activo IS 'Determina si un bien es activo mayor al limite minimo de activo fijo';
COMMENT ON COLUMN activo_fijo.sg_af_descargos.des_usuario_creacion IS 'Fecha en la que se creó el registro.';
COMMENT ON COLUMN activo_fijo.sg_af_descargos.des_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN activo_fijo.sg_af_descargos.des_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN activo_fijo.sg_af_descargos.des_version IS 'Versión del registro';

CREATE TABLE activo_fijo.sg_af_descargos_aud (
	des_pk int8 NOT NULL,
	des_codigo_descargo varchar(50) NULL,
	des_nombre_coord_adm varchar(150) NULL,
	des_nombre_encargado_af varchar(150) NULL,
	des_nombre_director varchar(150) NULL,
	des_nombre_autoriza varchar(150) NULL,
	des_cargo_autoriza varchar(150) NULL,
	des_observacion_fallo varchar(250) NULL,
	des_fecha_creacion timestamp NULL,
	des_fecha_solicitud timestamp NULL,
	des_fecha_descargo timestamp NULL,
	des_fecha_fallo timestamp NULL,
	des_tipo_descargo_fk int8 NULL,
	des_sede_fk int8 NULL,
	des_unidad_administrativa_fk int8 NULL,
	des_estado_fk int8 NULL,
	des_activo bool NULL,
	des_usuario_creacion varchar(45) NULL,
	des_ult_mod_fecha timestamp NULL,
	des_ult_mod_usuario varchar(45) NULL,
	des_version int2 NULL,
	rev int8 NOT NULL,
	revtype int2 NULL,
	des_fecha_envio_solicitud timestamp NULL,
	CONSTRAINT sg_af_descargos_aud_pkey PRIMARY KEY (des_pk, rev)
);

--TABLA DESCARGOS DETALLE
CREATE SEQUENCE activo_fijo.sg_af_descargos_detalle_pk_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
    NO CYCLE;

CREATE TABLE activo_fijo.sg_af_descargos_detalle (
	dde_pk bigserial NOT NULL, -- Número correlativo autogenerado.
	dde_bienes_depreciables_fk int8 NOT NULL, -- LLave foranea de Bien depreciable
	dde_descargo_fk int8 NOT NULL, -- LLave foranea del descargo
	dde_ult_mod_fecha timestamp NULL, -- Última fecha en la que se modificó el registro.
	dde_ult_mod_usuario varchar(45) NULL, -- Último usuario que modificó el registro.
	dde_version int2 NULL, -- Versión del registro
	CONSTRAINT descargos_detalle_pk PRIMARY KEY (dde_pk),
	CONSTRAINT bienes_depreciables_fk FOREIGN KEY (dde_bienes_depreciables_fk) REFERENCES activo_fijo.sg_af_bienes_depreciables(bde_pk),
	CONSTRAINT descargo_fk FOREIGN KEY (dde_descargo_fk) REFERENCES activo_fijo.sg_af_descargos(des_pk)
);
COMMENT ON TABLE activo_fijo.sg_af_descargos_detalle IS 'Tabla para el registro del detalle de los descargos.';

ALTER TABLE activo_fijo.sg_af_descargos_detalle ALTER dde_pk SET DEFAULT nextval('activo_fijo.sg_af_descargos_detalle_pk_seq');

-- Column comments

COMMENT ON COLUMN activo_fijo.sg_af_descargos_detalle.dde_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN activo_fijo.sg_af_descargos_detalle.dde_bienes_depreciables_fk IS 'LLave foranea de Bien depreciable';
COMMENT ON COLUMN activo_fijo.sg_af_descargos_detalle.dde_descargo_fk IS 'LLave foranea del descargo';
COMMENT ON COLUMN activo_fijo.sg_af_descargos_detalle.dde_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN activo_fijo.sg_af_descargos_detalle.dde_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN activo_fijo.sg_af_descargos_detalle.dde_version IS 'Versión del registro';

CREATE TABLE activo_fijo.sg_af_descargos_detalle_aud (
	dde_pk int8 NOT NULL,
	dde_bienes_depreciables_fk int8 NOT NULL,
	dde_descargo_fk int8 NOT NULL,
	dde_ult_mod_fecha timestamp NULL,
	dde_ult_mod_usuario varchar(45) NULL,
	dde_version int2 NULL,
	rev int8 NOT NULL,
	revtype int2 NULL,
	CONSTRAINT sg_af_descargos_detalle_aud_aud_pkey PRIMARY KEY (dde_pk, rev)
);

--TABLA LOTES BIENES
CREATE SEQUENCE activo_fijo.sg_af_lote_bienes_pk_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
    NO CYCLE;

CREATE TABLE activo_fijo.sg_af_lote_bienes (
	lbi_id bigserial NOT NULL, -- Número correlativo autogenerado.
	lbi_bienes_depreciables_fk int8 NOT NULL, -- LLave foranea de Bien depreciable
	lbi_ultimo_cod_inventario varchar(20) NULL, -- Ultimo código de inventario.
	lbi_ultimo_correlativo varchar(10) NULL, -- Ultimo correlativo del invenatrio.
	lbi_ult_mod_fecha timestamp NULL, -- Última fecha en la que se modificó el registro.
	lbi_ult_mod_usuario varchar(45) NULL, -- Último usuario que modificó el registro.
	lbi_version int2 NULL, -- Versión del registro
	CONSTRAINT sg_af_lote_bienes_pk PRIMARY KEY (lbi_id)
);
COMMENT ON TABLE activo_fijo.sg_af_lote_bienes IS 'Tabla para el registro de Lote de bienes a procesar.';

ALTER TABLE activo_fijo.sg_af_lote_bienes ALTER lbi_id SET DEFAULT nextval('activo_fijo.sg_af_lote_bienes_pk_seq');
-- Column comments

COMMENT ON COLUMN activo_fijo.sg_af_lote_bienes.lbi_id IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN activo_fijo.sg_af_lote_bienes.lbi_bienes_depreciables_fk IS 'LLave foranea de Bien depreciable';
COMMENT ON COLUMN activo_fijo.sg_af_lote_bienes.lbi_ultimo_cod_inventario IS 'Ultimo código de inventario.';
COMMENT ON COLUMN activo_fijo.sg_af_lote_bienes.lbi_ultimo_correlativo IS 'Ultimo correlativo del invenatrio.';
COMMENT ON COLUMN activo_fijo.sg_af_lote_bienes.lbi_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN activo_fijo.sg_af_lote_bienes.lbi_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN activo_fijo.sg_af_lote_bienes.lbi_version IS 'Versión del registro';

CREATE TABLE activo_fijo.sg_af_lote_bienes_aud (
	lbi_id int8 NOT NULL,
	lbi_bienes_depreciables_fk int8 NOT NULL,
	lbi_ultimo_cod_inventario varchar(20) NULL,
	lbi_ultimo_correlativo varchar(10) NULL,
	lbi_ult_mod_fecha timestamp NULL,
	lbi_ult_mod_usuario varchar(45) NULL,
	lbi_version int2 NULL,
	rev int8 NOT NULL,
	revtype int2 NULL,
	CONSTRAINT sg_af_lote_bienes_aud_aud_pkey PRIMARY KEY (lbi_id, rev)
);


--TABLA TRASLADOS
CREATE SEQUENCE activo_fijo.sg_af_traslados_pk_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
    NO CYCLE;

CREATE TABLE activo_fijo.sg_af_traslados (
	tra_pk bigserial NOT NULL, -- Número correlativo autogenerado.
	tra_codigo_traslado varchar(10) NULL, -- Código de traslado del registro
	tra_fecha_solicitud timestamp NULL, -- Fecha de la solicitud de traslado
	tra_fecha_autoriza timestamp NULL, -- Nombre de la persona que autoriza el traslado
	tra_fecha_traslado timestamp NULL, -- Fecha que se aprueba el traslado
	tra_fecha_recepcion timestamp NULL,
	tra_tipo_traslado_fk int8 NULL, -- Tipo de traslado
	tra_nombre_autoriza varchar(100) NULL, -- Nombre de quien autoriza el traslado
	tra_nombre_recibe varchar(100) NULL, -- Nombre de quien recibe el traslado
	tra_nombre_repr_af varchar(100) NULL, -- Nombre del representante de activo fijo
	tra_cargo_autoriza varchar(150) NULL, -- Cargo de la persona que autoriza
	tra_cargo_recibe varchar(150) NULL, -- Cargo de la persona que recibe el traslado
	tra_observacion varchar(250) NULL, -- Observación del traslado
	tra_sede_origen_fk int8 NULL, -- LLave foranea de la sede o centro educativo origen
	tra_sede_destino_fk int8 NULL, -- LLave foranea de la sede o centro educativo destino
	tra_unidad_adm_origen_fk int8 NULL, -- LLave foranea de la Unidad Administrativa origen
	tra_unidad_adm_destino_fk int8 NULL, -- LLave foranea de la Unidad Administrativa destino
	tra_estado_fk int8 NULL, -- LLave foranea del estado del traslado
	tra_fecha_creacion timestamp NULL,
	tra_usuario_creacion varchar(45) NULL, -- Fecha en la que se creó el registro.
	tra_ult_mod_fecha timestamp NULL, -- Última fecha en la que se modificó el registro.
	tra_ult_mod_usuario varchar(45) NULL, -- Último usuario que modificó el registro.
	tra_version int2 NULL, -- Versión del registro
	CONSTRAINT traslados_pk PRIMARY KEY (tra_pk),
	CONSTRAINT estado_fk FOREIGN KEY (tra_estado_fk) REFERENCES catalogo.sg_af_estados_bienes(ebi_pk),
	CONSTRAINT sede_destino_fk FOREIGN KEY (tra_sede_destino_fk) REFERENCES centros_educativos.sg_sedes(sed_pk),
	CONSTRAINT sede_origen_fk FOREIGN KEY (tra_sede_origen_fk) REFERENCES centros_educativos.sg_sedes(sed_pk),
	CONSTRAINT tipo_traslado_fk FOREIGN KEY (tra_tipo_traslado_fk) REFERENCES catalogo.sg_af_estados_traslado(etr_pk),
	CONSTRAINT unidad_adm_destino_fk FOREIGN KEY (tra_unidad_adm_destino_fk) REFERENCES catalogo.sg_af_unidades_administrativas(uad_pk),
	CONSTRAINT unidad_adm_origen_fk FOREIGN KEY (tra_unidad_adm_origen_fk) REFERENCES catalogo.sg_af_unidades_administrativas(uad_pk)
);
COMMENT ON TABLE activo_fijo.sg_af_traslados IS 'Tabla para el registro de los traslados.';

ALTER TABLE activo_fijo.sg_af_traslados ALTER tra_pk SET DEFAULT nextval('activo_fijo.sg_af_traslados_pk_seq');
-- Column comments

COMMENT ON COLUMN activo_fijo.sg_af_traslados.tra_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN activo_fijo.sg_af_traslados.tra_codigo_traslado IS 'Código de traslado del registro';
COMMENT ON COLUMN activo_fijo.sg_af_traslados.tra_fecha_solicitud IS 'Fecha de la solicitud de traslado';
COMMENT ON COLUMN activo_fijo.sg_af_traslados.tra_fecha_autoriza IS 'Nombre de la persona que autoriza el traslado';
COMMENT ON COLUMN activo_fijo.sg_af_traslados.tra_fecha_traslado IS 'Fecha que se aprueba el traslado';
COMMENT ON COLUMN activo_fijo.sg_af_traslados.tra_tipo_traslado_fk IS 'Tipo de traslado';
COMMENT ON COLUMN activo_fijo.sg_af_traslados.tra_nombre_autoriza IS 'Nombre de quien autoriza el traslado';
COMMENT ON COLUMN activo_fijo.sg_af_traslados.tra_nombre_recibe IS 'Nombre de quien recibe el traslado';
COMMENT ON COLUMN activo_fijo.sg_af_traslados.tra_nombre_repr_af IS 'Nombre del representante de activo fijo';
COMMENT ON COLUMN activo_fijo.sg_af_traslados.tra_cargo_autoriza IS 'Cargo de la persona que autoriza';
COMMENT ON COLUMN activo_fijo.sg_af_traslados.tra_cargo_recibe IS 'Cargo de la persona que recibe el traslado';
COMMENT ON COLUMN activo_fijo.sg_af_traslados.tra_observacion IS 'Observación del traslado';
COMMENT ON COLUMN activo_fijo.sg_af_traslados.tra_sede_origen_fk IS 'LLave foranea de la sede o centro educativo origen';
COMMENT ON COLUMN activo_fijo.sg_af_traslados.tra_sede_destino_fk IS 'LLave foranea de la sede o centro educativo destino';
COMMENT ON COLUMN activo_fijo.sg_af_traslados.tra_unidad_adm_origen_fk IS 'LLave foranea de la Unidad Administrativa origen';
COMMENT ON COLUMN activo_fijo.sg_af_traslados.tra_unidad_adm_destino_fk IS 'LLave foranea de la Unidad Administrativa destino';
COMMENT ON COLUMN activo_fijo.sg_af_traslados.tra_estado_fk IS 'LLave foranea del estado del traslado';
COMMENT ON COLUMN activo_fijo.sg_af_traslados.tra_usuario_creacion IS 'Fecha en la que se creó el registro.';
COMMENT ON COLUMN activo_fijo.sg_af_traslados.tra_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN activo_fijo.sg_af_traslados.tra_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN activo_fijo.sg_af_traslados.tra_version IS 'Versión del registro';

CREATE TABLE activo_fijo.sg_af_traslados_aud (
	tra_pk int8 NOT NULL,
	tra_codigo_traslado varchar(50) NULL,
	tra_fecha_solicitud timestamp NULL,
	tra_fecha_autoriza timestamp NULL,
	tra_fecha_traslado timestamp NULL,
	tra_fecha_recepcion timestamp NULL,
	tra_tipo_traslado_fk int8 NULL,
	tra_nombre_autoriza varchar(100) NULL,
	tra_nombre_recibe varchar(100) NULL,
	tra_nombre_repr_af varchar(100) NULL,
	tra_cargo_autoriza varchar(150) NULL,
	tra_cargo_recibe varchar(150) NULL,
	tra_observacion varchar(250) NULL,
	tra_sede_origen_fk int8 NULL,
	tra_sede_destino_fk int8 NULL,
	tra_unidad_adm_origen_fk int8 NULL,
	tra_unidad_adm_destino_fk int8 NULL,
	tra_estado_fk int8 NULL,
	tra_fecha_creacion timestamp NULL,
	tra_usuario_creacion varchar(45) NULL,
	tra_ult_mod_fecha timestamp NULL,
	tra_ult_mod_usuario varchar(45) NULL,
	tra_version int2 NULL,
	rev int8 NOT NULL,
	revtype int2 NULL,
	CONSTRAINT sg_af_traslados_aud_pkey PRIMARY KEY (tra_pk, rev)
);

--TABLA TRASLADOS DETALLE
CREATE SEQUENCE activo_fijo.sg_af_traslados_detalle_pk_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
    NO CYCLE;

CREATE TABLE activo_fijo.sg_af_traslados_detalle (
	tde_pk bigserial NOT NULL, -- Número correlativo autogenerado.
	tde_bienes_depreciables_fk int8 NOT NULL, -- LLave foranea del bien a trasladar
	tde_traslado_fk int8 NOT NULL, -- LLave foranea del traslado
	tde_ult_mod_fecha timestamp NULL,
	tde_ult_mod_usuario varchar(45) NULL, -- Última fecha en la que se modificó el registro.
	tde_version int2 NULL, -- Versión del registro
	CONSTRAINT traslado_detalle_pk PRIMARY KEY (tde_pk),
	CONSTRAINT bienes_depreciables_fk FOREIGN KEY (tde_bienes_depreciables_fk) REFERENCES activo_fijo.sg_af_bienes_depreciables(bde_pk),
	CONSTRAINT traslado_fk FOREIGN KEY (tde_traslado_fk) REFERENCES activo_fijo.sg_af_traslados(tra_pk)
);
COMMENT ON TABLE activo_fijo.sg_af_traslados_detalle IS 'Tabla para el detalle  de los traslados.';

ALTER TABLE activo_fijo.sg_af_traslados_detalle ALTER tde_pk SET DEFAULT nextval('activo_fijo.sg_af_traslados_detalle_pk_seq');
-- Column comments

COMMENT ON COLUMN activo_fijo.sg_af_traslados_detalle.tde_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN activo_fijo.sg_af_traslados_detalle.tde_bienes_depreciables_fk IS 'LLave foranea del bien a trasladar';
COMMENT ON COLUMN activo_fijo.sg_af_traslados_detalle.tde_traslado_fk IS 'LLave foranea del traslado';
COMMENT ON COLUMN activo_fijo.sg_af_traslados_detalle.tde_ult_mod_usuario IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN activo_fijo.sg_af_traslados_detalle.tde_version IS 'Versión del registro';


CREATE TABLE activo_fijo.sg_af_traslados_detalle_aud (
	tde_pk int8 NOT NULL,
	tde_bienes_depreciables_fk int8 NOT NULL,
	tde_traslado_fk int8 NOT NULL,
	tde_ult_mod_fecha timestamp NULL,
	tde_ult_mod_usuario varchar(45) NULL,
	tde_version int2 NULL,
	rev int8 NOT NULL,
	revtype int2 NULL,
	CONSTRAINT sg_af_traslados_detalle_aud_pkey PRIMARY KEY (tde_pk, rev)
);


--OPERACIONES PARA PERMISOS DE DE FUENTES DE FINANCIAMIENTO
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('FUENTE_MINED','AP37',  'Permiso para visualizar las fuentes de financiamiento del MINED Central', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('FUENTE_DEPARTAMENTAL','AP38',  'Permiso para visualizar las fuentes de financiamiento de las departamentales', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('FUENTE_CENTRO_EDUCATIVO','AP39',  'Permiso para visualizar las fuentes de financiamiento de centros educativos', 5, true, null, null,0);

--OPERACIONES PARA LA CREACION DE SOLICITUDES DE DESCARGO
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_SOLICITUDES_DESCARGO','AFB3',  'Permiso para buscar las solicitudes de descargo', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_SOLICITUD_DESCARGO','AF5',  'Permiso para crear solicitudes de descargo', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_SOLICITUD_DESCARGO','AF6',  'Permiso para actualizar solicitudes de descargo', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_SOLICITUD_DESCARGO','AF7',  'Permiso para eliminar las solicitudes de descargo', 5, true, null, null,0);

--OPERACION PARA LA ELIMINACION DE BIENES QUE SON ACTIVOS FIJOS
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_BIEN_VALOR_ADQUISICION_MAYOR_LIMITE_AF','AF4',  'Permiso para eliminar bienes con valor de adquisición mayor o igual al limite para ser activo fijo', 5, true, null, null,0);

--OPERACION DE VISUALIZACION DE COMBO DE EMPLEADOS
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_COMBO_ASIGNADO_A','AP40',  'Permiso para visualizar combo de empleados de las Unidades Administrativas', 5, true, null, null,0);

--OPERACIONES PARA LA CREACION DE SOLICITUDES DE TRASLADO
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_SOLICITUDES_TRASLADO_BIENES','AFB4',  'Permiso para buscar las solicitudes de traslado', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_SOLICITUD_TRASLADO_BIENES','AF8',  'Permiso para crear solicitudes de traslado', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_SOLICITUD_TRASLADO_BIENES','AF9',  'Permiso para actualizar solicitudes de traslado', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_SOLICITUD_TRASLADO_BIENES','AF10',  'Permiso para eliminar las solicitudes de traslado', 5, true, null, null,0);


--1.3.0

--Agregar permisos para solicitudes que aun no se hgan enviado
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_SOLICITUD_PROCESO_DESCARGO','AF11',  'Permiso para eliminar las solicitudes de descargo que aun no se han enviado', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_SOLICITUD_PROCESO_TRASLADO_BIENES','AF12',  'Permiso para eliminar las solicitudes de traslado que aun no se han enviado', 5, true, null, null,0);

--Agregar permisos para buscar, crear, actualizar y eliminar registros de depreciacion maestro y su detalle
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_DEPRECIACION_BIENES','AFB5',  'Permiso para busqueda de depreciación de bienes.', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_DEPRECIACION_MAESTRO','AFB6',  'Permiso para busqueda del maestro de depreciación de bienes.', 5, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_DEPRECIACION_BIEN','AF13',  'Permiso para crear depreciacion de bienes.', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_DEPRECIACION_BIEN','AF14',  'Permiso para actualizar depreciacion de bienes.', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_DEPRECIACION_BIEN','AF15',  'Permiso eliminar depreciacion de bienes.', 5, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_DEPRECIACION_MAESTRO','AF16',  'Permiso para crear el maestro de depreciacion de bienes.', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_DEPRECIACION_MAESTRO','AF17',  'Permiso para actualizar el maestro  de depreciacion de bienes.', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_DEPRECIACION_MAESTRO','AF18',  'Permiso para eliminar el maestro  de depreciacion de bienes.', 5, true, null, null,0);

--Agregar permiso para actualizar vida util del bien
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_VIDA_UTIL','AF19',  'Permiso para editar vida útil de un bien.', 5, true, null, null,0);

--Agregar permisos para buscar, crear, actualizar y eliminar registros de liquidación de proyectos
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_LIQUIDACIONES_PROYECTOS','AFB7',  'Permiso para busqueda de maestro de liquidación de proyectos.', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_LIQUIDACION_PROYECTO','AF20',  'Permiso para crear maestro de liquidación de proyectos.', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_LIQUIDACION_PROYECTO','AF21',  'Permiso para actualizar maestro de liquidación de proyectos.', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_LIQUIDACION_PROYECTO','AF22',  'Permiso para eliminar maestro de liquidación de proyectos.', 5, true, null, null,0);

--Agrega columna para el manejo de los estados de la solicitud
ALTER TABLE activo_fijo.sg_af_bienes_depreciables ADD bde_estado_solicitud_fk int8 NULL;
ALTER TABLE activo_fijo.sg_af_bienes_depreciables_aud ADD bde_estado_solicitud_fk int8 NULL;

ALTER TABLE activo_fijo.sg_af_bienes_depreciables ADD CONSTRAINT estado_solicitud_fk FOREIGN KEY (bde_estado_solicitud_fk) REFERENCES catalogo.sg_af_estados_bienes(ebi_pk);


--Agrega columna para para los traslados que no son definitivos
ALTER TABLE activo_fijo.sg_af_traslados ADD tra_fecha_retorno timestamp NULL;
ALTER TABLE activo_fijo.sg_af_traslados ADD tra_unidad_destino varchar(100) NULL;

ALTER TABLE activo_fijo.sg_af_traslados_aud ADD tra_fecha_retorno timestamp NULL;
ALTER TABLE activo_fijo.sg_af_traslados_aud ADD tra_unidad_destino varchar(100) NULL;

-- Column comments
COMMENT ON COLUMN activo_fijo.sg_af_traslados.tra_fecha_retorno IS 'Fecha de retorno del o los bienes.';
COMMENT ON COLUMN activo_fijo.sg_af_traslados.tra_unidad_destino IS 'Unidad de destino de los bienes.';


--TABLA PARA EL LA BITACORA DE PROCESOS DE DEPRECIACION A EJECUTAR
CREATE SEQUENCE activo_fijo.sg_af_depreciaciones_maestro_pk_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
    NO CYCLE;

CREATE TABLE activo_fijo.sg_af_depreciaciones_maestro (
	dma_pk int8 NOT NULL,
	dma_anio_proceso int2 not null,
	dma_mes_proceso int2 null,
	dma_fuente_financiamiento_fk int8 null,
	dma_proyecto_fk int8 null,
	dma_codigo_inventario varchar(20) null,

	dma_fecha_creacion timestamp NULL,
	dma_fecha_inicio_procesamiento timestamp NULL,
	dma_fecha_final_procesamiento timestamp NULL,
	dma_estado varchar(20) not null,
	dma_ult_mod_fecha timestamp NULL,
	dma_ult_mod_usuario varchar(45) NULL,
	dma_version int2 NULL,
	CONSTRAINT depreciacion_maestro PRIMARY KEY (dma_pk),
	CONSTRAINT fuente_financiamiento_fk FOREIGN KEY (dma_fuente_financiamiento_fk) REFERENCES catalogo.sg_af_fuente_financiamiento(ffi_pk),
	CONSTRAINT proyecto_fk FOREIGN KEY (dma_proyecto_fk) REFERENCES catalogo.sg_af_proyectos(pro_pk)
);

COMMENT ON TABLE activo_fijo.sg_af_depreciaciones_maestro IS 'Tabla para el registro del maestro de la depreciacion de los bienes.';

ALTER TABLE activo_fijo.sg_af_depreciaciones_maestro ALTER dma_pk SET DEFAULT nextval('activo_fijo.sg_af_depreciaciones_maestro_pk_seq');  
  


COMMENT ON COLUMN activo_fijo.sg_af_depreciaciones_maestro.dma_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN activo_fijo.sg_af_depreciaciones_maestro.dma_anio_proceso IS 'Año de proceso de depreciación';
COMMENT ON COLUMN activo_fijo.sg_af_depreciaciones_maestro.dma_mes_proceso IS 'Mes de proceso de depreciación';
COMMENT ON COLUMN activo_fijo.sg_af_depreciaciones_maestro.dma_fuente_financiamiento_fk IS 'LLave foranea de la fuente de financiamiento';
COMMENT ON COLUMN activo_fijo.sg_af_depreciaciones_maestro.dma_proyecto_fk IS 'LLave foranea del proyecto';
COMMENT ON COLUMN activo_fijo.sg_af_depreciaciones_maestro.dma_codigo_inventario IS 'Código de inventario';

COMMENT ON COLUMN activo_fijo.sg_af_depreciaciones_maestro.dma_fecha_creacion IS 'Fecha de creación del registro';
COMMENT ON COLUMN activo_fijo.sg_af_depreciaciones_maestro.dma_fecha_inicio_procesamiento IS 'Fecha de inicio de proceso';
COMMENT ON COLUMN activo_fijo.sg_af_depreciaciones_maestro.dma_fecha_final_procesamiento IS 'Fecha de final de proceso';
COMMENT ON COLUMN activo_fijo.sg_af_depreciaciones_maestro.dma_estado IS 'Estado del proceso';

COMMENT ON COLUMN activo_fijo.sg_af_depreciaciones_maestro.dma_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN activo_fijo.sg_af_depreciaciones_maestro.dma_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN activo_fijo.sg_af_depreciaciones_maestro.dma_version IS 'Versión del registro';

CREATE TABLE activo_fijo.sg_af_depreciaciones_maestro_aud (
	dma_pk int8 NOT NULL,
	dma_anio_proceso int2 not null,
	dma_mes_proceso int2 null,
	dma_fuente_financiamiento_fk int8 null,
	dma_proyecto_fk int8 null,
	dma_codigo_inventario varchar(20) null,
	dma_fecha_creacion timestamp NULL,
	dma_fecha_inicio_procesamiento timestamp NULL,
	dma_fecha_final_procesamiento timestamp NULL,
	dma_estado varchar(20) not null,
	dma_ult_mod_fecha timestamp NULL,
	dma_ult_mod_usuario varchar(45) NULL,
	dma_version int2 NULL,
	rev int8 NOT NULL,
	revtype int2 NULL,
	CONSTRAINT sg_af_depreciaciones_maestro_aud_pkey PRIMARY KEY (dma_pk, rev)
);


--TABLA PARA EL MANEJO DE DEPRECIACIONES
CREATE SEQUENCE activo_fijo.sg_af_depreciaciones_pk_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
    NO CYCLE;

CREATE TABLE activo_fijo.sg_af_depreciaciones (
	dep_pk bigserial NOT NULL, 
	dep_anio int2 not null,
	dep_mes int2 not null,
	dep_monto_depreciacion numeric not null,
	dep_no_dias_depreciados int2,
	dep_fecha_calculo timestamp not null,
	dep_bienes_depreciables_fk int8 not null,
	dep_fuente_financiamiento_fk int8 not null,
	dep_proyecto_fk int8 null,
	dep_ult_mod_fecha timestamp NULL,
	dep_ult_mod_usuario varchar(45) NULL,
	dep_version int2 NULL,
	CONSTRAINT depreciaciones_pk PRIMARY KEY (dep_pk),
	CONSTRAINT fuente_financiamiento_fk FOREIGN KEY (dep_fuente_financiamiento_fk) REFERENCES catalogo.sg_af_fuente_financiamiento(ffi_pk),
	CONSTRAINT proyecto_fk FOREIGN KEY (dep_proyecto_fk) REFERENCES catalogo.sg_af_proyectos(pro_pk),
	CONSTRAINT bienes_depreciables_fk FOREIGN KEY (dep_bienes_depreciables_fk) REFERENCES activo_fijo.sg_af_bienes_depreciables(bde_pk)
);
COMMENT ON TABLE activo_fijo.sg_af_depreciaciones IS 'Tabla para el registro de la depreciacion de los bienes.';

ALTER TABLE activo_fijo.sg_af_depreciaciones ALTER dep_pk SET DEFAULT nextval('activo_fijo.sg_af_depreciaciones_pk_seq');  
  

COMMENT ON COLUMN activo_fijo.sg_af_depreciaciones.dep_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN activo_fijo.sg_af_depreciaciones.dep_anio IS 'Año de depreciacion';
COMMENT ON COLUMN activo_fijo.sg_af_depreciaciones.dep_mes IS 'Mes de depreciacion';
COMMENT ON COLUMN activo_fijo.sg_af_depreciaciones.dep_monto_depreciacion IS 'Monto de depreciacion';
COMMENT ON COLUMN activo_fijo.sg_af_depreciaciones.dep_no_dias_depreciados IS 'Número de días de depreciacion';
COMMENT ON COLUMN activo_fijo.sg_af_depreciaciones.dep_fecha_calculo IS 'Fecha de cálculo de depreciacion';
COMMENT ON COLUMN activo_fijo.sg_af_depreciaciones.dep_bienes_depreciables_fk IS 'LLave foranea de Bien depreciable';
COMMENT ON COLUMN activo_fijo.sg_af_depreciaciones.dep_fuente_financiamiento_fk IS 'LLave foranea de la fuente de financiamiento';
COMMENT ON COLUMN activo_fijo.sg_af_depreciaciones.dep_proyecto_fk IS 'LLave foranea del proyecto';
COMMENT ON COLUMN activo_fijo.sg_af_depreciaciones.dep_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN activo_fijo.sg_af_depreciaciones.dep_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN activo_fijo.sg_af_depreciaciones.dep_version IS 'Versión del registro';

CREATE TABLE activo_fijo.sg_af_depreciaciones_aud (
	dep_pk bigserial NOT NULL, 
	dep_anio int2 not null,
	dep_mes int2 not null,
	dep_monto_depreciacion numeric not null,
	dep_no_dias_depreciados int2,
	dep_fecha_calculo timestamp not null,
	dep_bienes_depreciables_fk int8 not null,
	dep_fuente_financiamiento_fk int8 not null,
	dep_proyecto_fk int8 null,
	dep_ult_mod_fecha timestamp NULL,
	dep_ult_mod_usuario varchar(45) NULL,
	dep_version int2 NULL,
	rev int8 NOT NULL,
	revtype int2 NULL,
	CONSTRAINT sg_af_depreciaciones_aud_pkey PRIMARY KEY (dep_pk, rev)
);


--TABLA PARA EL MANEJO DE LIQUIDACION DE PROYECTOS
CREATE SEQUENCE activo_fijo.sg_af_liquidacion_proyecto_pk_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
    NO CYCLE;

CREATE TABLE activo_fijo.sg_af_liquidacion_proyecto (
	lpr_pk bigserial NOT NULL, 
	lpr_fuente_financiamiento_origen_fk int8 NULL, 
	lpr_fuente_financiamiento_destino_fk int8 NULL,
	lpr_proyecto_fk int8 NULL,
	lpr_fecha_liquidacion timestamp NULL,
	lpr_fecha_creacion timestamp NULL, 
	lpr_fecha_inicio_procesamiento timestamp NULL,
	lpr_fecha_final_procesamiento timestamp NULL, 
	lpr_estado varchar(20) NOT NULL, 
	lpr_ult_mod_fecha timestamp NULL, 
	lpr_ult_mod_usuario varchar(45) NULL,
	lpr_version int2 NULL,
	CONSTRAINT liq_proyecto_maestro PRIMARY KEY (lpr_pk),
	CONSTRAINT fuente_financiamiento_origen_fk FOREIGN KEY (lpr_fuente_financiamiento_origen_fk) REFERENCES catalogo.sg_af_fuente_financiamiento(ffi_pk),
	CONSTRAINT fuente_financiamiento_destino_fk FOREIGN KEY (lpr_fuente_financiamiento_destino_fk) REFERENCES catalogo.sg_af_fuente_financiamiento(ffi_pk),
	CONSTRAINT proyecto_fk FOREIGN KEY (lpr_proyecto_fk) REFERENCES catalogo.sg_af_proyectos(pro_pk)
);

COMMENT ON TABLE activo_fijo.sg_af_liquidacion_proyecto IS 'Tabla para el registro del maestro de liquidación de proyectos.';

ALTER TABLE activo_fijo.sg_af_liquidacion_proyecto ALTER lpr_pk SET DEFAULT nextval('activo_fijo.sg_af_liquidacion_proyecto_pk_seq');  
-- Column comments

COMMENT ON COLUMN activo_fijo.sg_af_liquidacion_proyecto.lpr_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN activo_fijo.sg_af_liquidacion_proyecto.lpr_fuente_financiamiento_origen_fk IS 'LLave foranea de la fuente de financiamiento de origen';
COMMENT ON COLUMN activo_fijo.sg_af_liquidacion_proyecto.lpr_fuente_financiamiento_destino_fk IS 'LLave foranea de la fuente de financiamiento de destino';
COMMENT ON COLUMN activo_fijo.sg_af_liquidacion_proyecto.lpr_proyecto_fk IS 'LLave foranea del proyecto';
COMMENT ON COLUMN activo_fijo.sg_af_liquidacion_proyecto.lpr_fecha_liquidacion IS 'Fecha de liquidación de proyecto';
COMMENT ON COLUMN activo_fijo.sg_af_liquidacion_proyecto.lpr_fecha_creacion IS 'Fecha de creación del registro';
COMMENT ON COLUMN activo_fijo.sg_af_liquidacion_proyecto.lpr_fecha_inicio_procesamiento IS 'Fecha de inicio de proceso';
COMMENT ON COLUMN activo_fijo.sg_af_liquidacion_proyecto.lpr_fecha_final_procesamiento IS 'Fecha de final de proceso';
COMMENT ON COLUMN activo_fijo.sg_af_liquidacion_proyecto.lpr_estado IS 'Estado del proceso';
COMMENT ON COLUMN activo_fijo.sg_af_liquidacion_proyecto.lpr_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN activo_fijo.sg_af_liquidacion_proyecto.lpr_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN activo_fijo.sg_af_liquidacion_proyecto.lpr_version IS 'Versión del registro';


CREATE TABLE activo_fijo.sg_af_liquidacion_proyecto_aud (
	lpr_pk bigserial NOT NULL, 
	lpr_fuente_financiamiento_origen_fk int8 NULL, 
	lpr_fuente_financiamiento_destino_fk int8 NULL,
	lpr_proyecto_fk int8 NULL,
	lpr_fecha_liquidacion timestamp NULL,
	lpr_fecha_creacion timestamp NULL, 
	lpr_fecha_inicio_procesamiento timestamp NULL,
	lpr_fecha_final_procesamiento timestamp NULL, 
	lpr_estado varchar(20) NOT NULL, 
	lpr_ult_mod_fecha timestamp NULL, 
	lpr_ult_mod_usuario varchar(45) NULL,
	lpr_version int2 NULL,
	rev int8 NOT NULL,
	revtype int2 NULL,
	CONSTRAINT sg_af_liquidacion_proyecto_aud_pkey PRIMARY KEY (lpr_pk, rev)
);

-- 1.4.0

--PERMISOS DE VISUALUZACION DE MENUS PARA REPORTE DE CONSTANCIA Y SOLVENCIA
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_REPORTE_CONSTANCIA','AM16',  'Permiso para menu de reporte de constancia', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_REPORTE_SOLVENCIA','AM17',  'Permiso para menu de reporte de solvencia', 5, true, null, null,0);

--PERMISO PARA ELIMINAR BIEN CON LIQUIDACION
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_BIEN_DEPRECIABLE_CON_DEPRECIACION','AF23',  'Permiso para eliminar un bien que ya tiene depreciacion', 5, true, null, null,0);

--PERMISO PARA VINCULAR BIEN A OTRA CATEGORIA
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VINCULAR_OTRA_CATEGORIA_BIEN','AF24',  'Permiso para vincular otra categoria al bien, esta categoria puede no ser la misma de la cual depende el tipo de bien', 5, true, null, null,0);


ALTER TABLE activo_fijo.sg_af_bienes_depreciables ADD bde_depreciado bool NULL;
ALTER TABLE activo_fijo.sg_af_bienes_depreciables_aud ADD bde_depreciado bool NULL;
COMMENT ON COLUMN activo_fijo.sg_af_bienes_depreciables_aud.bde_depreciado IS 'Bandera que determina si un bien ha sido depreciado o no';

ALTER TABLE activo_fijo.sg_af_bienes_depreciables ADD bde_tipo_bien_categoria_vinculada bool NULL;
ALTER TABLE activo_fijo.sg_af_bienes_depreciables_aud ADD bde_tipo_bien_categoria_vinculada bool NULL;
COMMENT ON COLUMN activo_fijo.sg_af_bienes_depreciables.bde_tipo_bien_categoria_vinculada IS 'Bandera que determina si un bien esta vinculado a la categoria o no';

ALTER TABLE activo_fijo.sg_af_bienes_depreciables ADD bde_categoria_fk int8 NULL;
ALTER TABLE activo_fijo.sg_af_bienes_depreciables_aud ADD bde_categoria_fk int8 NULL;
COMMENT ON COLUMN activo_fijo.sg_af_bienes_depreciables_aud.bde_categoria_fk IS 'Categoría de un bien. Esta categoría es opcional(En caso un tipo de bien sea necesario cambiarlo de categoría)';


-- 1.7.0

--PERMISO PARA VINCULAR BIEN A OTRA CATEGORIA
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_DATOS_ADQUISICION','AF25',  'Permiso para actualizar valor y fecha de adquisición', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_DATOS_ADQUISICION_MENOR_AF','AF26',  'Permiso para actualizar valor y fecha de adquisición para los bienes que no son activos fijos', 5, true, null, null,0);


--PERMISO PARA VER ALERTAS DE LOTE, TRASLADO Y DESCARGO
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PUEDE_VER_ALERTAS_TRASLADOS_BIENES','AP41',  'Permiso para ver las alertas de traslados de bienes por aprobar', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PUEDE_VER_ALERTAS_DESCARGOS_BIENES','AP42',  'Permiso para ver las alertas de descargos de bienes por aprobar', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PUEDE_VER_ALERTAS_LOTE_CREADO','AP44',  'Permiso para ver las alertas de lotes creados', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PUEDE_VER_ALERTAS_NOTIFICACION_CUMPLIMIENTO','AP43',  'Permiso para ver las alertas de notificación de cumplimiento', 5, true, null, null,0);
     
                             

--PERMISOS PARA AUTORIZAR DESCARGOS Y TRASLADOS
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('AUTORIZAR_DESCARGO_BIENES','AF27',  'Permiso para autorizar los descargos de bienes', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('AUTORIZAR_TRASLADO_BIENES','AF28',  'Permiso para autorizar los traslados de bienes', 5, true, null, null,0);

--PERMISOS PARA TRABAJAR SOLICITUDES RECHAZADAS
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_SOLICITUDES_DESCARGO_BIENES_RECHAZADAS','AF35',  'Permiso para ver las solicitudes de descargo rechazadas', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_SOLICITUDES_TRASLADO_BIENES_RECHAZADAS','AF36',  'Permiso para ver las solicitudes de traslado rechazadas', 5, true, null, null,0);
 
                              
--PERMISOS PARA CRUD DE LOTES
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_LOTE_BIENES','AFB8',  'Permiso para la búsqueda de lotes', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_LOTE_BIENES','AF29',  'Permiso la creación de lotes de bienes', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_LOTE_BIENES','AF30',  'Permiso para actualizar lote de bienes', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_LOTE_BIENES','AF31',  'Permiso para eliminar lote de bienes', 5, true, null, null,0);
                
--PERMISOS PARA CRUD DE NOTIFICACIONES DE CUMPLIMIENTO
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_NOTIFICACIONES_CUMPLIMIENTO','AFB9',  'Permiso para la búsqueda de notificaciones de incumpliminto', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_NOTIFICACION_CUMPLIMIENTO','AF32',  'Permiso la creación de notificaciones de incumplimiento', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_NOTIFICACION_CUMPLIMIENTO','AF33',  'Permiso para actualizar notificaciones de incumplimiento', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_NOTIFICACION_CUMPLIMIENTO','AF34',  'Permiso para eliminar notificaciones de incumplimiento', 5, true, null, null,0);

--TABLA LOTE UPDATE CAMPOS
ALTER TABLE activo_fijo.sg_af_lote_bienes ADD lbi_codigo_inventario_padre VARCHAR(20) NULL;
ALTER TABLE activo_fijo.sg_af_lote_bienes ADD lbi_primer_cod_inventario VARCHAR(20) NULL;
ALTER TABLE activo_fijo.sg_af_lote_bienes ADD lbi_estado VARCHAR(20) NULL;
ALTER TABLE activo_fijo.sg_af_lote_bienes ADD lbi_fecha_inicio_procesamiento timestamp NULL;
ALTER TABLE activo_fijo.sg_af_lote_bienes ADD lbi_fecha_final_procesamiento timestamp NULL;
ALTER TABLE activo_fijo.sg_af_lote_bienes ADD lbi_sede_fk int8 NULL;
ALTER TABLE activo_fijo.sg_af_lote_bienes ADD lbi_unidad_administrativa_fk int8 NULL;
ALTER TABLE activo_fijo.sg_af_lote_bienes ADD lbi_cantidad_bienes_replicar int2 NULL;


ALTER TABLE activo_fijo.sg_af_lote_bienes ADD CONSTRAINT unidad_administrativa_fk FOREIGN KEY (lbi_unidad_administrativa_fk) REFERENCES catalogo.sg_af_unidades_administrativas(uad_pk);
ALTER TABLE activo_fijo.sg_af_lote_bienes ADD CONSTRAINT sede_fk FOREIGN KEY (lbi_sede_fk) REFERENCES centros_educativos.sg_sedes(sed_pk);

-- Column comments
COMMENT ON COLUMN activo_fijo.sg_af_lote_bienes.lbi_codigo_inventario_padre IS 'Código de inventario del bien padre.';
COMMENT ON COLUMN activo_fijo.sg_af_lote_bienes.lbi_primer_cod_inventario IS 'Primer código de inventario creado.';
COMMENT ON COLUMN activo_fijo.sg_af_lote_bienes.lbi_estado IS 'Estado del lote.';
COMMENT ON COLUMN activo_fijo.sg_af_lote_bienes.lbi_fecha_inicio_procesamiento IS 'Fecha inicial de procesamiento del lote.';
COMMENT ON COLUMN activo_fijo.sg_af_lote_bienes.lbi_fecha_final_procesamiento IS 'Fecha final de procesamiento de lote.';
COMMENT ON COLUMN activo_fijo.sg_af_lote_bienes.lbi_sede_fk IS 'Sede de la que se debe replicar los bienes.';
COMMENT ON COLUMN activo_fijo.sg_af_lote_bienes.lbi_unidad_administrativa_fk IS 'Unidad dde la que se debe replicar los bienes.';

ALTER TABLE activo_fijo.sg_af_lote_bienes_aud ADD lbi_codigo_inventario_padre VARCHAR(20) NULL;
ALTER TABLE activo_fijo.sg_af_lote_bienes_aud ADD lbi_primer_cod_inventario VARCHAR(20) NULL;
ALTER TABLE activo_fijo.sg_af_lote_bienes_aud ADD lbi_estado VARCHAR(20) NULL;
ALTER TABLE activo_fijo.sg_af_lote_bienes_aud ADD lbi_fecha_inicio_procesamiento timestamp NULL;
ALTER TABLE activo_fijo.sg_af_lote_bienes_aud ADD lbi_fecha_final_procesamiento timestamp NULL;
ALTER TABLE activo_fijo.sg_af_lote_bienes_aud ADD lbi_sede_fk int8 NULL;
ALTER TABLE activo_fijo.sg_af_lote_bienes_aud ADD lbi_unidad_administrativa_fk int8 NULL;
ALTER TABLE activo_fijo.sg_af_lote_bienes_aud ADD lbi_cantidad_bienes_replicar int2 NULL;

ALTER TABLE activo_fijo.sg_af_lote_bienes DROP COLUMN lbi_bienes_depreciables_fk;
ALTER TABLE activo_fijo.sg_af_lote_bienes DROP COLUMN lbi_ultimo_correlativo;

ALTER TABLE activo_fijo.sg_af_lote_bienes_aud DROP COLUMN lbi_bienes_depreciables_fk;
ALTER TABLE activo_fijo.sg_af_lote_bienes_aud DROP COLUMN lbi_ultimo_correlativo;

--TABLA NOTIFICACIONES DE CUMPLIMIENTO
CREATE SEQUENCE activo_fijo.sg_af_notificaciones_cumplimiento_pk_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
    NO CYCLE;

CREATE TABLE activo_fijo.sg_af_notificaciones_cumplimiento (
	ncu_id bigserial NOT NULL,
	ncu_leida boolean null,
	ncu_numero_notificacion int2 not null,
	ncu_documento int8 null,
	ncu_anio int2 not null,
	ncu_sede_fk int8 NULL,
	ncu_unidad_administrativa_fk int8 NULL,
	ncu_ult_mod_fecha timestamp NULL, 
	ncu_ult_mod_usuario varchar(45) NULL, 
	ncu_version int2 NULL,
	CONSTRAINT notificaciones_cumplimiento_pk PRIMARY KEY (ncu_id),
	CONSTRAINT sede_fk FOREIGN KEY (ncu_sede_fk) REFERENCES centros_educativos.sg_sedes(sed_pk),
	CONSTRAINT unidad_administrativa_fk FOREIGN KEY (ncu_unidad_administrativa_fk) REFERENCES catalogo.sg_af_unidades_administrativas(uad_pk)
);

COMMENT ON TABLE activo_fijo.sg_af_notificaciones_cumplimiento IS 'Tabla para el detalle  de las notificaciones enviadas.';

ALTER TABLE activo_fijo.sg_af_notificaciones_cumplimiento ALTER ncu_id SET DEFAULT nextval('activo_fijo.sg_af_notificaciones_cumplimiento_pk_seq');
-- Column comments

COMMENT ON COLUMN activo_fijo.sg_af_notificaciones_cumplimiento.ncu_id IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN activo_fijo.sg_af_notificaciones_cumplimiento.ncu_leida IS 'Si la notificación ha sido leida o no';
COMMENT ON COLUMN activo_fijo.sg_af_notificaciones_cumplimiento.ncu_numero_notificacion IS 'Número de notificación';
COMMENT ON COLUMN activo_fijo.sg_af_notificaciones_cumplimiento.ncu_documento IS 'Documento o notificación';
COMMENT ON COLUMN activo_fijo.sg_af_notificaciones_cumplimiento.ncu_anio IS 'Año de la notificación';
COMMENT ON COLUMN activo_fijo.sg_af_notificaciones_cumplimiento.ncu_sede_fk IS 'Sede a quien se envía la notificación';
COMMENT ON COLUMN activo_fijo.sg_af_notificaciones_cumplimiento.ncu_unidad_administrativa_fk IS 'Unidad a quien se envía la notificación';
COMMENT ON COLUMN activo_fijo.sg_af_notificaciones_cumplimiento.ncu_ult_mod_usuario IS 'Último usuario que se modificó el registro.';
COMMENT ON COLUMN activo_fijo.sg_af_notificaciones_cumplimiento.ncu_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN activo_fijo.sg_af_notificaciones_cumplimiento.ncu_version IS 'Versión del registro';


CREATE TABLE activo_fijo.sg_af_notificaciones_cumplimiento_aud (
	ncu_id bigserial NOT NULL,
	ncu_leida boolean null,
	ncu_numero_notificacion int2 not null,
	ncu_documento int8 null,
	ncu_anio int2 not null,
	ncu_sede_fk int8 NULL,
	ncu_unidad_administrativa_fk int8 NULL,
	ncu_ult_mod_fecha timestamp NULL, 
	ncu_ult_mod_usuario varchar(45) NULL, 
	ncu_version int2 NULL,
	rev int8 NOT NULL,
	revtype int2 NULL,
	CONSTRAINT notificaciones_cumplimiento_aud_aud_pkey PRIMARY KEY (ncu_id, rev)
);

--TABLA MOTIVOS DE RECHAZO DE UNA SOLICITUD DE DESCARGO
CREATE SEQUENCE activo_fijo.sg_af_motivos_rechazo_descargo_pk_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
    NO CYCLE;

CREATE TABLE activo_fijo.sg_af_motivos_rechazo_descargo (
	mrd_id bigserial NOT NULL,
	mrd_motivo_rechazo varchar(500) not NULL, 
	mrd_descargo_fk int8 NULL,
	mrd_ult_mod_fecha timestamp NULL,
	mrd_ult_mod_usuario varchar(45) NULL,
	mrd_version int2 NULL,
	CONSTRAINT motivos_rechazo_descargo PRIMARY KEY (mrd_id),
	CONSTRAINT descargo_fk FOREIGN KEY (mrd_descargo_fk) REFERENCES activo_fijo.sg_af_descargos(des_pk)
);

COMMENT ON TABLE activo_fijo.sg_af_motivos_rechazo_descargo IS 'Tabla para el detalle  de los motivos de rechazo';

ALTER TABLE activo_fijo.sg_af_motivos_rechazo_descargo ALTER mrd_id SET DEFAULT nextval('activo_fijo.sg_af_motivos_rechazo_descargo_pk_seq');
-- Column comments

COMMENT ON COLUMN activo_fijo.sg_af_motivos_rechazo_descargo.mrd_id IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN activo_fijo.sg_af_motivos_rechazo_descargo.mrd_motivo_rechazo IS 'Motivo de rechazo';
COMMENT ON COLUMN activo_fijo.sg_af_motivos_rechazo_descargo.mrd_descargo_fk IS 'LLave foranea del descargo.';
COMMENT ON COLUMN activo_fijo.sg_af_motivos_rechazo_descargo.mrd_ult_mod_usuario IS 'Último usuario que se modificó el registro.';
COMMENT ON COLUMN activo_fijo.sg_af_motivos_rechazo_descargo.mrd_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN activo_fijo.sg_af_motivos_rechazo_descargo.mrd_version IS 'Versión del registro';


CREATE TABLE activo_fijo.sg_af_motivos_rechazo_descargo_aud (
	mrd_id bigserial NOT NULL,
	mrd_motivo_rechazo varchar(500) not NULL, 
	mrd_descargo_fk int8 NULL,
	mrd_ult_mod_fecha timestamp NULL,
	mrd_ult_mod_usuario varchar(45) NULL,
	mrd_version int2 NULL,
	rev int8 NOT NULL,
	revtype int2 NULL,
	CONSTRAINT motivos_rechazo_descargo_aud_pkey PRIMARY KEY (mrd_id, rev)
);

--TABLA MOTIVOS DE RECHAZO DE UNA SOLICITUD DE TRASLADO
CREATE SEQUENCE activo_fijo.sg_af_motivos_rechazo_traslado_pk_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
    NO CYCLE;

CREATE TABLE activo_fijo.sg_af_motivos_rechazo_traslado (
	mrt_id bigserial NOT NULL,
	mrt_motivo_rechazo varchar(500) not NULL, 
	mrt_traslado_fk int8 NULL,
	mrt_ult_mod_fecha timestamp NULL,
	mrt_ult_mod_usuario varchar(45) NULL,
	mrt_version int2 NULL,
	CONSTRAINT motivos_rechazo_traslado PRIMARY KEY (mrt_id),
	CONSTRAINT traslado_fk FOREIGN KEY (mrt_traslado_fk) REFERENCES activo_fijo.sg_af_traslados(tra_pk)
);

COMMENT ON TABLE activo_fijo.sg_af_motivos_rechazo_traslado IS 'Tabla para el detalle  de los motivos de rechazo';

ALTER TABLE activo_fijo.sg_af_motivos_rechazo_traslado ALTER mrt_id SET DEFAULT nextval('activo_fijo.sg_af_motivos_rechazo_traslado_pk_seq');
-- Column comments

COMMENT ON COLUMN activo_fijo.sg_af_motivos_rechazo_traslado.mrt_id IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN activo_fijo.sg_af_motivos_rechazo_traslado.mrt_motivo_rechazo IS 'Motivo de rechazo';
COMMENT ON COLUMN activo_fijo.sg_af_motivos_rechazo_traslado.mrt_traslado_fk IS 'LLave foranea del descargo.';
COMMENT ON COLUMN activo_fijo.sg_af_motivos_rechazo_traslado.mrt_ult_mod_usuario IS 'Último usuario que se modificó el registro.';
COMMENT ON COLUMN activo_fijo.sg_af_motivos_rechazo_traslado.mrt_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN activo_fijo.sg_af_motivos_rechazo_traslado.mrt_version IS 'Versión del registro';


CREATE TABLE activo_fijo.sg_af_motivos_rechazo_traslado_aud (
	mrt_id bigserial NOT NULL,
	mrt_motivo_rechazo varchar(500) not NULL, 
	mrt_traslado_fk int8 NULL,
	mrt_ult_mod_fecha timestamp NULL,
	mrt_ult_mod_usuario varchar(45) NULL,
	mrt_version int2 NULL,
	rev int8 NOT NULL,
	revtype int2 NULL,
	CONSTRAINT motivos_rechazo_traslado_aud_pkey PRIMARY KEY (mrt_id, rev)
);



--CAMPOS PARA GUARDAR SI UN BIEN HA SIDO RECHAZADO, Y SU IBSERVACION
ALTER TABLE activo_fijo.sg_af_traslados_detalle ADD tde_rechazado boolean NULL;
ALTER TABLE activo_fijo.sg_af_traslados_detalle ADD tde_observacion varchar(50) NULL;

ALTER TABLE activo_fijo.sg_af_traslados_detalle_aud ADD tde_rechazado boolean NULL;
ALTER TABLE activo_fijo.sg_af_traslados_detalle_aud ADD tde_observacion varchar(50) NULL;

COMMENT ON COLUMN activo_fijo.sg_af_traslados_detalle.tde_rechazado IS 'Si un bien es rechazado o no.';
COMMENT ON COLUMN activo_fijo.sg_af_traslados_detalle.tde_observacion IS 'Observación de rechazo.';

-- 1.7.2

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_UNIDADES_ADMINISTRATIVAS','AFB11',  '', 5, true, null, null,0);


-- 1.7.5

--PERMISOS PARA CRUD DE SOLVENCIAS EMITIDAS
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_SOLVENCIAS_UNIDADES','AFB10',  'Permiso para la búsqueda de solvencias', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_SOLVENCIA_UNIDAD','AF37',  'Permiso la creación de solvencias', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_SOLVENCIA_UNIDAD','AF38',  'Permiso para actualizar solvencias', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_SOLVENCIA_UNIDAD','AF39',  'Permiso para eliminar solvencias', 5, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_DEPRECIACIONES','AFB12',  'Permiso para busqueda de depreciaciones', 5, true, null, null,0);


--TABLA DE SOLVENCIAS EMITIDAS
CREATE SEQUENCE activo_fijo.sg_af_solvencias_pk_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
    NO CYCLE;

CREATE TABLE activo_fijo.sg_af_solvencias (
	sol_pk int8 NOT NULL,
	sol_sede_fk int8 NULL,
	sol_unidad_administrativa_fk int8 NULL,
	sol_fecha_solvencia timestamp NULL,
	sol_anio int2 null,
	sol_ult_mod_fecha timestamp NULL,
	sol_ult_mod_usuario varchar(45) NULL,
	sol_version int2 NULL,
	CONSTRAINT solvencias PRIMARY KEY (sol_pk),
	CONSTRAINT sede_fk FOREIGN KEY (sol_sede_fk) REFERENCES centros_educativos.sg_sedes(sed_pk),
	CONSTRAINT unidad_administrativa_fk FOREIGN KEY (sol_unidad_administrativa_fk) REFERENCES catalogo.sg_af_unidades_administrativas(uad_pk)
);

COMMENT ON TABLE activo_fijo.sg_af_solvencias IS 'Tabla para el detalle de las solvencias emitidas';

ALTER TABLE activo_fijo.sg_af_solvencias ALTER sol_pk SET DEFAULT nextval('activo_fijo.sg_af_solvencias_pk_seq');
-- Column comments

COMMENT ON COLUMN activo_fijo.sg_af_solvencias.sol_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN activo_fijo.sg_af_solvencias.sol_sede_fk IS 'Sede a quien se crea la solvencia';
COMMENT ON COLUMN activo_fijo.sg_af_solvencias.sol_unidad_administrativa_fk IS 'Unidad a quien se crea la solvencia';
COMMENT ON COLUMN activo_fijo.sg_af_solvencias.sol_fecha_solvencia IS 'Fecha de emisión de la solvencia.';
COMMENT ON COLUMN activo_fijo.sg_af_solvencias.sol_anio IS 'Año de solvencia';
COMMENT ON COLUMN activo_fijo.sg_af_solvencias.sol_ult_mod_usuario IS 'Último usuario que se modificó el registro.';
COMMENT ON COLUMN activo_fijo.sg_af_solvencias.sol_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN activo_fijo.sg_af_solvencias.sol_version IS 'Versión del registro';


CREATE TABLE activo_fijo.sg_af_solvencias_aud (
	sol_pk int8 NOT NULL,
	sol_sede_fk int8 NULL,
	sol_unidad_administrativa_fk int8 NULL,
	sol_fecha_solvencia timestamp NULL,
	sol_anio int2 null,
	sol_ult_mod_fecha timestamp NULL,
	sol_ult_mod_usuario varchar(45) NULL,
	sol_version int2 NULL,
	rev int8 NOT NULL,
	revtype int2 NULL,
	CONSTRAINT sg_af_solvencias_aud_pkey PRIMARY KEY (sol_pk, rev)
);
 
-- 1.8.0
ALTER TABLE activo_fijo.sg_af_bienes_depreciables ADD bde_inmueble_id int8 NULL;
ALTER TABLE activo_fijo.sg_af_bienes_depreciables_aud ADD bde_inmueble_id int8 NULL;

ALTER TABLE activo_fijo.sg_af_bienes_depreciables ADD bde_completado bool NULL;
ALTER TABLE activo_fijo.sg_af_bienes_depreciables_aud ADD bde_completado bool NULL;

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PUEDE_VER_ALERTAS_BIENES_NO_COMPLETADOS','AP45',  'Permiso para visualizar las alertas de bienes no completados.', 5, true, null, null,0);

--1.8.5
ALTER TABLE activo_fijo.sg_af_bienes_depreciables ALTER COLUMN bde_fuente_financiamiento_fk DROP NOT NULL;
ALTER TABLE activo_fijo.sg_af_bienes_depreciables_aud ALTER COLUMN bde_fuente_financiamiento_fk DROP NOT NULL;

ALTER TABLE activo_fijo.sg_af_bienes_depreciables ADD CONSTRAINT categoria_fk FOREIGN KEY (bde_categoria_fk) REFERENCES catalogo.sg_af_categoria_bienes(cab_pk);

--INDICES PARA CONSULTA DE BIENES
--JOIN CON TABLAS
CREATE INDEX IF NOT EXISTS bde_estado_fk_idx ON activo_fijo.sg_af_bienes_depreciables USING btree (bde_estado_fk) ;
CREATE INDEX IF NOT EXISTS bde_categoria_fk_idx ON activo_fijo.sg_af_bienes_depreciables USING btree (bde_categoria_fk) ;
CREATE INDEX IF NOT EXISTS bde_empleado_fk_idx ON activo_fijo.sg_af_bienes_depreciables USING btree (bde_empleado_fk) ;
CREATE INDEX IF NOT EXISTS bde_calidad_fk_idx ON activo_fijo.sg_af_bienes_depreciables USING btree (bde_calidad_fk) ;
CREATE INDEX IF NOT EXISTS bde_estado_solicitud_fk_idx ON activo_fijo.sg_af_bienes_depreciables USING btree (bde_estado_solicitud_fk) ;
CREATE INDEX IF NOT EXISTS bde_forma_adquisicion_fk_idx ON activo_fijo.sg_af_bienes_depreciables USING btree (bde_forma_adquisicion_fk) ;
CREATE INDEX IF NOT EXISTS bde_fuente_financiamiento_fk_idx ON activo_fijo.sg_af_bienes_depreciables USING btree (bde_fuente_financiamiento_fk) ;
CREATE INDEX IF NOT EXISTS bde_proyecto_fk_idx ON activo_fijo.sg_af_bienes_depreciables USING btree (bde_proyecto_fk) ;
CREATE INDEX IF NOT EXISTS bde_sede_fk_idx ON activo_fijo.sg_af_bienes_depreciables USING btree (bde_sede_fk) ;
CREATE INDEX IF NOT EXISTS bde_tipo_bien_fk_idx ON activo_fijo.sg_af_bienes_depreciables USING btree (bde_tipo_bien_fk) ;
CREATE INDEX IF NOT EXISTS bde_unidad_administrativa_fk_idx ON activo_fijo.sg_af_bienes_depreciables USING btree (bde_unidad_administrativa_fk) ;
--CAMPOS DE BUSQUEDA
CREATE INDEX IF NOT EXISTS bde_codigo_inventario_idx ON activo_fijo.sg_af_bienes_depreciables USING btree (bde_codigo_inventario) ;
CREATE INDEX IF NOT EXISTS bde_asignado_a_idx ON activo_fijo.sg_af_bienes_depreciables USING btree (bde_asignado_a) ;
CREATE INDEX IF NOT EXISTS bde_marca_idx ON activo_fijo.sg_af_bienes_depreciables USING btree (bde_marca) ;
CREATE INDEX IF NOT EXISTS bde_modelo_idx ON activo_fijo.sg_af_bienes_depreciables USING btree (bde_modelo) ;
CREATE INDEX IF NOT EXISTS bde_no_serie_idx ON activo_fijo.sg_af_bienes_depreciables USING btree (bde_no_serie) ;
CREATE INDEX IF NOT EXISTS bde_valor_adquisicion_idx ON activo_fijo.sg_af_bienes_depreciables USING btree (bde_valor_adquisicion) ;
CREATE INDEX IF NOT EXISTS bde_fecha_adquisicion_idx ON activo_fijo.sg_af_bienes_depreciables USING btree (bde_fecha_adquisicion) ;
CREATE INDEX IF NOT EXISTS bde_fecha_creacion_idx ON activo_fijo.sg_af_bienes_depreciables USING btree (bde_fecha_creacion) ;
CREATE INDEX IF NOT EXISTS bde_ult_mod_fecha_idx ON activo_fijo.sg_af_bienes_depreciables USING btree (bde_ult_mod_fecha) ;
CREATE INDEX IF NOT EXISTS bde_ult_mod_usuario_idx ON activo_fijo.sg_af_bienes_depreciables USING btree (bde_ult_mod_usuario) ;
--para búsqueda de bienes que son vehiculos
CREATE INDEX IF NOT EXISTS bde_no_placa_idx ON activo_fijo.sg_af_bienes_depreciables USING btree (bde_no_placa) ;
CREATE INDEX IF NOT EXISTS bde_no_motor_idx ON activo_fijo.sg_af_bienes_depreciables USING btree (bde_no_motor) ;
CREATE INDEX IF NOT EXISTS bde_no_chasis_idx ON activo_fijo.sg_af_bienes_depreciables USING btree (bde_no_chasis) ;


--INDICES DESCARGOS
--JOIN CON TABLAS
CREATE INDEX IF NOT EXISTS des_estado_fk_idx ON activo_fijo.sg_af_descargos  USING btree (des_estado_fk) ;
CREATE INDEX IF NOT EXISTS des_sede_fk_idx ON activo_fijo.sg_af_descargos  USING btree (des_sede_fk) ;
CREATE INDEX IF NOT EXISTS des_tipo_descargo_fk_idx ON activo_fijo.sg_af_descargos  USING btree (des_tipo_descargo_fk) ;
CREATE INDEX IF NOT EXISTS des_unidad_administrativa_fk_idx ON activo_fijo.sg_af_descargos  USING btree (des_unidad_administrativa_fk);
--CAMPOS DE BUSQUEDA
CREATE INDEX IF NOT EXISTS des_codigo_descargo_idx ON activo_fijo.sg_af_descargos  USING btree (des_codigo_descargo) ;
CREATE INDEX IF NOT EXISTS des_fecha_solicitud_idx ON activo_fijo.sg_af_descargos  USING btree (des_fecha_solicitud) ;
CREATE INDEX IF NOT EXISTS des_fecha_descargo_idx ON activo_fijo.sg_af_descargos  USING btree (des_fecha_descargo) ;
CREATE INDEX IF NOT EXISTS des_activo_idx ON activo_fijo.sg_af_descargos  USING btree (des_activo);

--INDICES DESCARGOS DETALLE
--JOIN CON TABLAS
CREATE INDEX IF NOT EXISTS dde_bienes_depreciables_fk_idx ON activo_fijo.sg_af_descargos_detalle  USING btree (dde_bienes_depreciables_fk) ;
CREATE INDEX IF NOT EXISTS dde_descargo_fk_idx ON activo_fijo.sg_af_descargos_detalle  USING btree (dde_descargo_fk);

--INDICES TRASLADOS
--JOIN CON TABLAS
CREATE INDEX IF NOT EXISTS tra_estado_fk_idx ON activo_fijo.sg_af_traslados  USING btree (tra_estado_fk) ;
CREATE INDEX IF NOT EXISTS tra_sede_destino_fk_idx ON activo_fijo.sg_af_traslados  USING btree (tra_sede_destino_fk) ;
CREATE INDEX IF NOT EXISTS tra_sede_origen_fk_idx ON activo_fijo.sg_af_traslados  USING btree (tra_sede_origen_fk) ;
CREATE INDEX IF NOT EXISTS tra_tipo_traslado_fk_idx ON activo_fijo.sg_af_traslados  USING btree (tra_tipo_traslado_fk);
CREATE INDEX IF NOT EXISTS tra_unidad_adm_destino_fk_idx ON activo_fijo.sg_af_traslados  USING btree (tra_unidad_adm_destino_fk) ;
CREATE INDEX IF NOT EXISTS tra_unidad_adm_origen_fk_idx ON activo_fijo.sg_af_traslados  USING btree (tra_unidad_adm_origen_fk);
--CAMPOS DE BUSQUEDA
CREATE INDEX IF NOT EXISTS tra_fecha_traslado_idx ON activo_fijo.sg_af_traslados  USING btree (tra_fecha_traslado) ;
CREATE INDEX IF NOT EXISTS tra_fecha_solicitud_idx ON activo_fijo.sg_af_traslados  USING btree (tra_fecha_solicitud) ;
CREATE INDEX IF NOT EXISTS tra_codigo_traslado_idx ON activo_fijo.sg_af_traslados  USING btree (tra_codigo_traslado) ;

--INDICES TRASLADOS DETALLE
--JOIN CON TABLAS
CREATE INDEX IF NOT EXISTS tde_bienes_depreciables_fk_idx ON activo_fijo.sg_af_traslados_detalle  USING btree (tde_bienes_depreciables_fk) ;
CREATE INDEX IF NOT EXISTS tde_traslado_fk_idx ON activo_fijo.sg_af_traslados_detalle  USING btree (tde_traslado_fk);

-- 1.9.0

--PERMISOS PARA ENVIAR, AUTORIZAR Y RECHAZAR SOLICITUDES DE DESCARGO Y TRASLADOS
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ENVIAR_DESCARGO_BIENES','AF40',  'Permiso para el envio de solicitudes de descargo.', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('RECHAZAR_DESCARGO_BIENES','AF41',  'Permiso para el rechazo de solicitudes de descargo', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ENVIAR_TRASLADO_BIENES','AF42',  'Permiso para el envio de traslados de solicitudes de traslado.', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('RECHAZAR_TRASLADO_BIENES','AF43',  'Permiso para el rechazo de solicitudes de traslado de bienes.', 5, true, null, null,0);
--PERMISO PARA ELIMINAR BIENES MENORES AL VALOR LIMITE DE ACTIVO FIJO
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_BIEN_VALOR_ADQUISICION_MENOR_LIMITE_AF','AF44',  'Permiso para eliminar bienes menores al valor limite de activo fijo.', 5, true, null, null,0);

--PERMISO PARA VER SOLICITUDES DE TRASLADO QUE HAN VENCIDO
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_SOLICITUDES_TRASLADO_VENCIDAS','AF45',  'Permiso para ver alertas de solicitudes de traslado que han vencido.', 5, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_SOLICITUDES_TRASLADO_APROBADAS','AF46',  'Permiso para ver alertas de solicitudes de traslado aprobadas.', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_SOLICITUDES_DESCARGO_APROBADAS','AF47',  'Permiso para ver alertas de solicitudes de descargo aprobadas.', 5, true, null, null,0);

--PERMISO PARA PROCESAR TAREAS DE LIQUIDACION DE PROYECTOS Y CALCULO DE DEPRECIACION
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PROCESAR_TAREAS_DEPRECIACION','AF48',  'Permiso para procesar tareas de depreciacion.', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PROCESAR_TAREAS_LIQUIDACION_PROYECTOS','AF49',  'Permiso para procesar tareas de liquidacion de proyectos.', 5, true, null, null,0);

ALTER TABLE activo_fijo.sg_af_descargos ADD des_opcion_descargo_fk int8 NULL;
ALTER TABLE activo_fijo.sg_af_descargos_aud ADD des_opcion_descargo_fk int8 NULL;
COMMENT ON COLUMN activo_fijo.sg_af_descargos.des_opcion_descargo_fk IS 'LLave foranea de la opción de descargo';

ALTER TABLE activo_fijo.sg_af_descargos ADD CONSTRAINT opcion_descargo_fk FOREIGN KEY (des_opcion_descargo_fk) REFERENCES catalogo.sg_af_opciones_descargo(ode_pk);


--CAMPO PARA ALMACENAR CORRELATIVO POR UNIDAD ADMINISTRATIVA
ALTER TABLE activo_fijo.sg_af_traslados ADD tra_correlativo int2 NULL;
ALTER TABLE activo_fijo.sg_af_traslados ADD tra_anio int2 NULL;

ALTER TABLE activo_fijo.sg_af_traslados_aud ADD tra_correlativo int2 NULL;
ALTER TABLE activo_fijo.sg_af_traslados_aud ADD tra_anio int2 NULL;

--CLAVE UNICA PARA CODIGOS DE TRASLADO
ALTER TABLE activo_fijo.sg_af_traslados ADD CONSTRAINT codigo_traslado_sede_uk UNIQUE (tra_sede_origen_fk, tra_codigo_traslado,tra_anio);
ALTER TABLE activo_fijo.sg_af_traslados ADD CONSTRAINT codigo_traslado_unidad_uk UNIQUE (tra_unidad_adm_origen_fk, tra_codigo_traslado,tra_anio);

--ACTUALIZAR CATEGORIAS DE LOS BIENES(PARA QUE TOME DEL TIPO DE BIEN LA CATEGORIA Y LA SETEE EN EL CAMPO bde_categoria_fk)
UPDATE activo_fijo.sg_af_bienes_depreciables
SET bde_categoria_fk = tb1.tbi_categoria_bienes_fk
FROM (SELECT tbi_categoria_bienes_fk,tb.tbi_pk
      FROM catalogo.sg_af_tipo_bienes tb) AS tb1
WHERE activo_fijo.sg_af_bienes_depreciables.bde_tipo_bien_fk =tb1.tbi_pk;

--1.9.1
--SE ELIMINA EL NOT NULL DE LOS CAMPOS DE MOTIVO DE RECHAZO, TANTO PARA DESCARGO COMO PARA TRASLADO
ALTER TABLE activo_fijo.sg_af_motivos_rechazo_traslado ALTER COLUMN mrt_motivo_rechazo DROP NOT NULL;
ALTER TABLE activo_fijo.sg_af_motivos_rechazo_traslado_aud ALTER COLUMN mrt_motivo_rechazo DROP NOT NULL;

--SE AGREGAN CAMPOS PARA EL DEFINIR A QUIEN SERAN ASIGNADOS LOS BIENES TRASLADADOS
ALTER TABLE activo_fijo.sg_af_traslados ADD tra_empleado_bienes_fk int8 NULL;
ALTER TABLE activo_fijo.sg_af_traslados ADD tra_asignadoa_bienes VARCHAR(100) NULL;

ALTER TABLE activo_fijo.sg_af_traslados_aud ADD tra_empleado_bienes_fk int8 NULL;
ALTER TABLE activo_fijo.sg_af_traslados_aud ADD tra_asignadoa_bienes VARCHAR(100) NULL;

ALTER TABLE activo_fijo.sg_af_traslados ADD CONSTRAINT empleado_bienes_fk FOREIGN KEY (tra_empleado_bienes_fk) REFERENCES catalogo.sg_af_empleados(emp_pk);

-- 1.10.0

--Campo para determinar si un bien ha sido depreciado completamente
ALTER TABLE activo_fijo.sg_af_bienes_depreciables ADD bde_depreciado_completo bool null default false;
ALTER TABLE activo_fijo.sg_af_bienes_depreciables_aud ADD bde_depreciado_completo bool null default false;

COMMENT ON COLUMN activo_fijo.sg_af_bienes_depreciables.bde_depreciado_completo IS 'Determina si un bien ha sido depreciado completamente o no.';

--PERMISOS DE VISUALIZACION DE CAMPOS
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_COLUMNA_UNIDAD_ADMINISTRATIVA','AP46',  'Permiso para ver columna de unidad administrativa', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_COLUMNA_CODIGO_INVENTARIO','AP47',  'Permiso para ver columna de codigo de inventario.', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_COLUMNA_ESTADO','AP48',  'Permiso para ver columna de estado del bien.', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_COLUMNA_ESTADO_SOLICITUD','AP49',  'Permiso para ver columna estado de solicitud.', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_COLUMNA_DESCRIPCION','AP50',  'Permiso para ver columna de descripcion.', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_COLUMNA_MARCA','AP51',  'Permiso para ver columna de marca.', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_COLUMNA_MODELO','AP52',  'Permiso para ver columna de modelo.', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_COLUMNA_SERIE','AP53',  'Permiso paraver columna de serie.', 5, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_COLUMNA_FECHA_ADQUISICION','AP54',  'Permiso para ver columna de fecha de adquisicion.', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_COLUMNA_VALOR_ADQUISICION','AP55',  'Permiso para ver columna de valor de adquisicion.', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_COLUMNA_ASIGNADO_A','AP56',  'Permiso para ver columna de asignado a.', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_COLUMNA_FUENTE','AP57',  'Permiso para ver columna de fuente de financiamiento.', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_COLUMNA_PROYECTO','AP58',  'Permiso para ver columna de proyecto.', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_COLUMNA_CATEGORIA','AP59',  'Permiso para ver columna de categoria.', 5, true, null, null,0);

--PERMISO PARA AUTORIZAR DESCARGOS QUE NO SON ACTIVO FIJO
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('AUTORIZAR_DESCARGO_BIENES_VALOR_ADQUISICION_MENOR_LIMITE_AF','AF50',  'Permiso para autorizar descargos que no son activo fijo.', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('RECHAZAR_DESCARGO_BIENES_VALOR_ADQUISICION_MENOR_LIMITE_AF','AF51',  'Permiso para rechazar descargos que no son activo fijo.', 5, true, null, null,0);

-- 1.11.2

--PERMISOS PARA MODIFICAR TIPO DE BIEN, FUENTE DE FINANCIAMIENTO Y PROYECTO
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MODIFICAR_FUENTE_FINANCIAMIENTO','AF52',  'Permiso para actualizar fuente de financiamiento.', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MODIFICAR_FUENTE_FINANCIAMIENTO_VALOR_ADQUISICION_MENOR_LIMITE_AF','AF53',  'Permiso para actualizar fuente de financiamiento para bienes menores al limite.', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MODIFICAR_PROYECTO','AF54',  'Permiso para actualizar proyecto.', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MODIFICAR_PROYECTO_VALOR_ADQUISICION_MENOR_LIMITE_AF','AF55',  'Permiso para actualizar proyecto de bienes menores al limite.', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MODIFICAR_TIPO_BIEN','AF56',  'Permiso actualizar el tipo de bien.', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MODIFICAR_TIPO_BIEN_VALOR_ADQUISICION_MENOR_LIMITE_AF','AF57',  'Permiso para actualizar el tipo de bien para bienes menores al limite.', 5, true, null, null,0);

-- 1.12.0
--PERMISOS PARA ENVIAR,RECHAZAR Y APROBAR TRASLADOS, ESTOS PERMISOS APLICAN PARA TODAS LAS SOLICITUDES(PERMISOS QUE DEBERIAN TENER LOS ADMINISTRADORES Y DEPARTAMENTALES)
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ENVIAR_TODOS_TRASLADO_BIENES','AF58',  'Permiso para autorizar todos los traslados.', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('RECHAZAR_TODOS_TRASLADO_BIENES','AF59',  'Permiso para rechazar todos los traslados.', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('AUTORIZAR_TODOS_TRASLADO_BIENES','AF60',  'Permiso para autorizar todos los traslados.', 5, true, null, null,0);


-- 1.15.0

--CAMPO PARA GUARDAR EL ULTIMO CORRELATIVO DE BIENES
ALTER TABLE activo_fijo.sg_af_lote_bienes ADD lbi_ultimo_correlativo numeric  null;
ALTER TABLE activo_fijo.sg_af_lote_bienes_aud ADD lbi_ultimo_correlativo numeric  null;
COMMENT ON COLUMN activo_fijo.sg_af_lote_bienes.lbi_ultimo_correlativo IS 'último correlativo de bien creado';


--PERMISOS PARA EJECUTAR TAREAS DE PROCESAMIENTO DE LOTES,DEPRECIACION Y LIQUIDACION DE PROYECTOS
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('EJECUTAR_PROCESAMIENTO_LOTES','AF61',  'Permiso para ejecutar tarea de procesamiento de lotes.', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('EJECUTAR_PROCESAMIENTO_DEPRECIACION','AF62',  'Permiso para ejecutar tarea de procesamiento de depreciacion.', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('EJECUTAR_PROCESAMIENTO_LIQUIDACION_PROYECTOS','AF63',  'Permiso para ejecutar tarea de liquidacion de proyectos.', 5, true, null, null,0);


ALTER TABLE activo_fijo.sg_af_depreciaciones ADD dep_ultimo_monto_depreciacion numeric  null;
ALTER TABLE activo_fijo.sg_af_depreciaciones ADD dep_opreacion varchar(10)  null;
ALTER TABLE activo_fijo.sg_af_depreciaciones ADD dep_procesado bool null;

ALTER TABLE activo_fijo.sg_af_depreciaciones_aud ADD dep_ultimo_monto_depreciacion numeric  null;
ALTER TABLE activo_fijo.sg_af_depreciaciones_aud ADD dep_opreacion varchar(10)  null;
ALTER TABLE activo_fijo.sg_af_depreciaciones_aud ADD dep_procesado bool null;


COMMENT ON COLUMN activo_fijo.sg_af_depreciaciones_aud.dep_ultimo_monto_depreciacion IS 'Ultimo monto de depreciación';
COMMENT ON COLUMN activo_fijo.sg_af_depreciaciones_aud.dep_opreacion IS 'Operación realizada al registro';
COMMENT ON COLUMN activo_fijo.sg_af_depreciaciones_aud.dep_procesado IS 'Si el registro ha sido procesado(true), caso contrario false';


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_COLUMNA_UNIDAD_ACTIVO_FIJO','AP60',  'Permiso para visualizar la columna unidad de activo fijo.', 5, true, null, null,0);

--PERMISOS PARA ELIMINAR TODAS SOLICITUDES DE DESCARGO QUE NO HAN SIDO ENVIADAS
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_TODAS_SOLICITUD_PROCESO_TRASLADO_BIENES','AF64',  'Permiso para eliminar todas las solicitudes de descargo que no se han enviado a aprobación.', 5, true, null, null,0);

--SE CREAN O ACTUALIZAN LOS CATALOGOS DE OPCIONES DE DESCARGO
INSERT INTO catalogo.sg_af_opciones_descargo (ode_codigo, ode_habilitado, ode_nombre, ode_nombre_busqueda, ode_ult_mod_fecha, ode_ult_mod_usuario, ode_version) VALUES('CDES', true, 'COMISIÓN DE DESCARGO', 'comision de descargo', '2019-08-21 16:12:25.239', 'casuser', 0) ON CONFLICT (ode_nombre) DO UPDATE SET ode_codigo = 'CDES';

INSERT INTO catalogo.sg_af_opciones_descargo (ode_codigo, ode_habilitado, ode_nombre, ode_nombre_busqueda, ode_ult_mod_fecha, ode_ult_mod_usuario, ode_version) VALUES('APER', true, 'ACUERDO DE PERMUTA', 'acuerdo de permuta', '2019-08-21 16:12:37.615', 'casuser', 0) ON CONFLICT (ode_nombre)  DO UPDATE SET ode_codigo = 'APER';

--TABLA DE ACTAS DE DESCARGO EMITIDAS
CREATE SEQUENCE activo_fijo.sg_af_acta_descargo_pk_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
    NO CYCLE;

CREATE TABLE activo_fijo.sg_af_actas_descargo (
	ade_pk int8 NOT NULL, -- Número correlativo autogenerado.
	ade_numero_acuerdo varchar(20) NOT NULL, -- Número de acuerdo del acta de descargo
	ade_fecha_acuerdo timestamp NOT NULL, -- Fecha de acuerdo del acta.
	ade_se_autoriza varchar(500) NULL, -- Descripción de lo que se autoriza
	ade_ult_mod_fecha timestamp NULL, -- Última fecha en la que se modificó el registro.
	ade_ult_mod_usuario varchar(45) NULL, -- Último usuario que se modificó el registro.
	ade_version int2 NULL, -- Versión del registro
	CONSTRAINT sg_af_actas_descargo_pk PRIMARY KEY (ade_pk)
);
COMMENT ON TABLE activo_fijo.sg_af_actas_descargo IS 'Tabla para el detalle de las actas de descargo emitidas';

ALTER TABLE activo_fijo.sg_af_actas_descargo ALTER ade_pk SET DEFAULT nextval('activo_fijo.sg_af_acta_descargo_pk_seq');
-- Column comments

COMMENT ON COLUMN activo_fijo.sg_af_actas_descargo.ade_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN activo_fijo.sg_af_actas_descargo.ade_numero_acuerdo IS 'Número de acuerdo del acta de descargo';
COMMENT ON COLUMN activo_fijo.sg_af_actas_descargo.ade_fecha_acuerdo IS 'Fecha de acuerdo del acta.';
COMMENT ON COLUMN activo_fijo.sg_af_actas_descargo.ade_se_autoriza IS 'Descripción de lo que se autoriza';
COMMENT ON COLUMN activo_fijo.sg_af_actas_descargo.ade_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN activo_fijo.sg_af_actas_descargo.ade_ult_mod_usuario IS 'Último usuario que se modificó el registro.';
COMMENT ON COLUMN activo_fijo.sg_af_actas_descargo.ade_version IS 'Versión del registro';

CREATE TABLE activo_fijo.sg_af_actas_descargo_aud (
    ade_pk int8 NOT NULL,
    ade_numero_acuerdo varchar(20) not NULL,
    ade_fecha_acuerdo timestamp not NULL,
    ade_se_autoriza varchar(500) NULL,
    ade_ult_mod_fecha timestamp NULL,
    ade_ult_mod_usuario varchar(45) NULL,
    ade_version int2 NULL,
    rev int8 NOT NULL,
    revtype int2 NULL,
    CONSTRAINT sg_af_actas_descargo_aud_pkey PRIMARY KEY (ade_pk, rev)
);

ALTER TABLE activo_fijo.sg_af_descargos ADD des_acta_descargo_fk int8  null;
ALTER TABLE activo_fijo.sg_af_descargos_aud ADD des_acta_descargo_fk int8  null;
ALTER TABLE activo_fijo.sg_af_descargos ADD CONSTRAINT acta_descargo_fk FOREIGN KEY (des_acta_descargo_fk) 
REFERENCES activo_fijo.sg_af_actas_descargo(ade_pk) MATCH FULL ON UPDATE CASCADE ON DELETE restrict;



--SE AGREGA EL CAMPO PARA GUARDAR EL ID DEL LOTE.
ALTER TABLE activo_fijo.sg_af_bienes_depreciables ADD bde_lote_id int8 NULL;
ALTER TABLE activo_fijo.sg_af_bienes_depreciables_aud ADD bde_lote_id int8 NULL;

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_CAMPO_DUI','AP61',  'Permiso para visualizar el campo de DUI', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_CAMPO_NOMBRE_EMPLEADO','AP62',  'Permiso para visualizar el campo nombre de empleado.', 5, true, null, null,0);

UPDATE catalogo.sg_af_estados_traslado SET etr_codigo='01' WHERE etr_nombre='REPARACIÓN';
UPDATE catalogo.sg_af_estados_traslado SET etr_codigo='02' WHERE etr_nombre='PRÉSTAMO';
UPDATE catalogo.sg_af_estados_traslado SET etr_codigo='03' WHERE etr_nombre='DEFINITIVO';

UPDATE catalogo.sg_af_estados_traslado SET etr_codigo='1' WHERE etr_codigo='01';
UPDATE catalogo.sg_af_estados_traslado SET etr_codigo='2' WHERE etr_codigo='02';
UPDATE catalogo.sg_af_estados_traslado SET etr_codigo='3' WHERE etr_codigo='03';

--1.15.6
--ACTUALIZACIÓN DE UNIDAD DE ACTIVO FIJO ṔARA UNIDAD CENTRAL
UPDATE catalogo.sg_af_unidades_activo_fijo SET uaf_departamento_fk=15 WHERE uaf_pk=1;

ALTER TABLE catalogo.sg_af_proyectos ALTER COLUMN pro_codigo TYPE varchar(8) USING pro_codigo::varchar;

--SE AGREGA EL CAMPO PARA SABER SI EL BIEN ES FUENTE DE SIAP
ALTER TABLE activo_fijo.sg_af_bienes_depreciables ADD bde_bien_es_fuente_siap bsed_codigooolean default false;
ALTER TABLE activo_fijo.sg_af_bienes_depreciables_aud ADD bde_bien_es_fuente_siap boolean default false;

ALTER TABLE activo_fijo.sg_af_bienes_depreciables ADD bde_es_lote_siap boolean default false;
ALTER TABLE activo_fijo.sg_af_bienes_depreciables_aud ADD bde_es_lote_siap boolean default false;

ALTER TABLE activo_fijo.sg_af_bienes_depreciables ADD bde_numero_correlativo_siap int4 null;
ALTER TABLE activo_fijo.sg_af_bienes_depreciables_aud ADD bde_numero_correlativo_siap int4 null;

UPDATE catalogo.sg_af_fuente_financiamiento SET ffi_codigo='S/F' WHERE ffi_codigo='0';
UPDATE catalogo.sg_af_fuente_financiamiento SET ffi_codigo='GOES' WHERE ffi_codigo='1';
UPDATE catalogo.sg_af_fuente_financiamiento SET ffi_codigo='FPROPIOS' WHERE ffi_codigo='2';
UPDATE catalogo.sg_af_fuente_financiamiento SET ffi_codigo='PROYEC1' WHERE ffi_codigo='3';
UPDATE catalogo.sg_af_fuente_financiamiento SET ffi_codigo='DONAC' WHERE ffi_codigo='5';
UPDATE catalogo.sg_af_fuente_financiamiento SET ffi_codigo='PROY. LIQ.' WHERE ffi_codigo='7';
UPDATE catalogo.sg_af_fuente_financiamiento SET ffi_codigo='G N/R' WHERE ffi_codigo='8';


update catalogo.sg_af_unidades_administrativas set uad_nombre=trim(uad_nombre),uad_cargo_director=trim(uad_cargo_director),
        uad_nombre_busqueda=trim(uad_nombre_busqueda),uad_cargo_responsable=trim(uad_cargo_responsable),uad_direccion=trim(uad_direccion),
        uad_nombre_director=trim(uad_nombre_director),uad_responsable=trim(uad_responsable),uad_telefono=trim(uad_telefono);


ALTER TABLE activo_fijo.sg_af_traslados ADD tra_codigo_ua_origen varchar(10) NULL;
ALTER TABLE activo_fijo.sg_af_traslados ADD tra_codigo_ce_origen varchar(10) NULL;
ALTER TABLE activo_fijo.sg_af_traslados ADD tra_codigo_ua_destino varchar(10) NULL;
ALTER TABLE activo_fijo.sg_af_traslados ADD tra_codigo_ce_destino varchar(10) NULL;

ALTER TABLE activo_fijo.sg_af_traslados_aud ADD tra_codigo_ua_origen varchar(10) NULL;
ALTER TABLE activo_fijo.sg_af_traslados_aud ADD tra_codigo_ce_origen varchar(10) NULL;
ALTER TABLE activo_fijo.sg_af_traslados_aud ADD tra_codigo_ua_destino varchar(10) NULL;
ALTER TABLE activo_fijo.sg_af_traslados_aud ADD tra_codigo_ce_destino varchar(10) NULL;

--ACTUALIZACIÓN DE REGISTROS DE TIPOS DE DESCARGOS
UPDATE catalogo.sg_af_estados_descargo SET ede_nombre='INSERVIBLE', ede_nombre_busqueda='inservible' WHERE ede_pk=1;
UPDATE catalogo.sg_af_estados_descargo SET ede_nombre='OBSOLESCENCIA', ede_nombre_busqueda='obsolencia' WHERE ede_pk=2;
UPDATE catalogo.sg_af_estados_descargo SET ede_nombre='ROBO Y/O HURTO', ede_nombre_busqueda='robo y/o hurto' WHERE ede_pk=3;