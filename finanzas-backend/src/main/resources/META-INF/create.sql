CREATE SCHEMA IF NOT EXISTS finanzas;

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('TIENE_ACCESO_FINANZAS','W16',  '', 4, true, null, null,0);

INSERT INTO seguridad.sg_categorias_operacion values (12, 'fin', 'Finanzas','Finanzas',true,null,null,0);
	

--INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_XX','F1',  '', 12, true, null, null,0);
--INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_XX','F2',  '', 12, true, null, null,0);
--INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_XX','F3',  '', 12, true, null, null,0);

--INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_XX','FB1',  '', 12, true, null, null,0);



--1.0.0


--CREACION DE TABLA PARA LA GESTIÒN DE PRESUPUESTOS ESCOLARES
CREATE SEQUENCE IF NOT EXISTS finanzas.sg_presupuesto_escolar_pk_seq 
    INCREMENT 1 
    START 1 
    MINVALUE 1 
    MAXVALUE 9223372036854775807 
    CACHE 1     
    NO CYCLE;

CREATE TABLE IF NOT EXISTS finanzas.sg_presupuesto_escolar (
    pes_pk bigint NOT NULL DEFAULT nextval('finanzas.sg_presupuesto_escolar_pk_seq'::regclass), 
    pes_codigo character varying(45), 
    pes_habilitado boolean, 
    pes_nombre character varying(255), 
    pes_nombre_busqueda character varying(255), 
    pes_descripcion character varying(255), 
    pes_estado character varying(255),
    pes_sede_fk integer, 
    pes_ult_mod_fecha timestamp without time zone, 
    pes_ult_mod_usuario character varying(45), 
    pes_version integer, 
    CONSTRAINT presupuesto_escolar_pkey PRIMARY KEY (pes_pk), 
    CONSTRAINT pes_codigo_uk UNIQUE (pes_codigo), 
    CONSTRAINT pes_nombre_uk UNIQUE (pes_nombre)
);
COMMENT ON TABLE finanzas.sg_presupuesto_escolar IS 'Tabla para el registro de tipos de presupuestos escolares.';
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar.pes_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar.pes_codigo IS 'Código del registro.';
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar.pes_habilitado IS 'Indica si el registro se ecuentra habilitado';
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar.pes_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar.pes_descripcion IS 'Nombre del registro normalizado para descripción.';
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar.pes_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar.pes_descripcion IS 'Nombre del registro normalizado para descripción.';
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar.pes_estado IS 'Indica el estado del registros segun Enum';
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar.pes_sede_fk IS 'Indica la llave de la sede a la que pertenece el Presupuesto';
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar.pes_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar.pes_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar.pes_version IS 'Versión del registro.';


CREATE TABLE IF NOT EXISTS finanzas.sg_presupuesto_escolar_aud (
    pes_pk bigint not null, 
    pes_codigo character varying(45), 
    pes_habilitado boolean, 
    pes_nombre character varying(255), 
    pes_nombre_busqueda character varying(255), 
    pes_descripcion character varying(255), 
    pes_estado character varying(255),
    pes_sede_fk integer, 
    pes_ult_mod_fecha timestamp without time zone, 
    pes_ult_mod_usuario character varying(45), 
    pes_version integer, 
    rev bigint, 
    revtype smallint, 
    CONSTRAINT sg_presupuesto_escolar_aud_pkey PRIMARY KEY (pes_pk,rev), 
    CONSTRAINT sg_presupuesto_escolar_aud_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev)
);


--CREACION DE TABLA PARA LA GESTIÒN DE LOS MOVIMIENTOS DEL PRESUPUESTO ESCOLAR
CREATE SEQUENCE IF NOT EXISTS finanzas.sg_presupuesto_escolar_movimiento_pk_seq 
    INCREMENT 1 
    START 1 
    MINVALUE 1 
    MAXVALUE 9223372036854775807 
    CACHE 1     
    NO CYCLE;

CREATE TABLE IF NOT EXISTS finanzas.sg_presupuesto_escolar_movimiento (
    mov_pk bigint NOT NULL DEFAULT nextval('finanzas.sg_presupuesto_escolar_movimiento_pk_seq'::regclass), 
    mov_codigo character varying(45), 
    mov_presupuesto_fk integer, 
    mov_fuente_recursos character varying(255), 
    mov_tipo character varying(255), 
    mov_monto decimal,
    mov_ult_mod_fecha timestamp without time zone, 
    mov_ult_mod_usuario character varying(45), 
    mov_version integer, 
    CONSTRAINT sg_presupuesto_escolar_movimiento_pkey PRIMARY KEY (mov_pk), 
    CONSTRAINT mov_codigo_uk UNIQUE (mov_codigo), 
    CONSTRAINT mov_presupuesto_fk_uk UNIQUE (mov_presupuesto_fk)

);
COMMENT ON TABLE finanzas.sg_presupuesto_escolar_movimiento IS 'Tabla para el registro de los movimientos de los .';
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_movimiento.mov_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_movimiento.mov_codigo IS 'Código del registro.';
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_movimiento.mov_presupuesto_fk IS 'Indica la llave del presupuesto a la que pertenece el Movimiento';
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_movimiento.mov_fuente_recursos IS 'Indica la fuente del registro';
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_movimiento.mov_tipo IS 'Indica el tipo de registro';
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_movimiento.mov_monto IS 'Muestra el valor en $ por el cual el se realizo el registro';
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_movimiento.mov_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_movimiento.mov_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_movimiento.mov_version IS 'Versión del registro.';


CREATE TABLE IF NOT EXISTS finanzas.sg_presupuesto_escolar_movimiento_aud (
    mov_pk bigint not null, 
    mov_codigo character varying(45), 
    mov_presupuesto_fk integer, 
    mov_fuente_recursos character varying(255), 
    mov_tipo character varying(255), 
    mov_monto decimal,
    mov_ult_mod_fecha timestamp without time zone, 
    mov_ult_mod_usuario character varying(45), 
    mov_version integer, 
    rev bigint, 
    revtype smallint, 
    CONSTRAINT sg_presupuesto_escolar_movimiento_aud_pkey PRIMARY KEY (mov_pk,rev), 
    CONSTRAINT sg_presupuesto_escolar_movimiento_aud_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev)
);

-- RELACION DE PRESUPUESTO CON SEDES
ALTER TABLE finanzas.sg_presupuesto_escolar ADD CONSTRAINT sg_presupuesto_escolar_fkey FOREIGN KEY (pes_sede_fk) REFERENCES centros_educativos.sg_sedes(sed_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

-- RELACION DE MOVIMIENTOS CON PRESUPUESTOS
ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento ADD CONSTRAINT sg_presupuesto_escolar_movimiento_fkey FOREIGN KEY (mov_presupuesto_fk) REFERENCES finanzas.sg_presupuesto_escolar(pes_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE finanzas.sg_presupuesto_escolar ADD COLUMN pes_anio_lectivo_fk bigint;
ALTER TABLE finanzas.sg_presupuesto_escolar_aud ADD COLUMN pes_anio_lectivo_fk bigint;
ALTER TABLE finanzas.sg_presupuesto_escolar ADD CONSTRAINT sg_pes_anio_lectivo_fk FOREIGN KEY (pes_anio_lectivo_fk) REFERENCES centros_educativos.sg_anio_lectivo(ale_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;


ALTER TABLE finanzas.sg_presupuesto_escolar ALTER COLUMN pes_sede_fk  TYPE bigint;
ALTER TABLE finanzas.sg_presupuesto_escolar_aud ALTER COLUMN pes_sede_fk  TYPE bigint;
ALTER TABLE finanzas.sg_presupuesto_escolar ADD CONSTRAINT sg_pes_sede_fk FOREIGN KEY (pes_sede_fk) REFERENCES centros_educativos.sg_sedes(sed_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;


ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento ALTER COLUMN mov_presupuesto_fk  TYPE bigint;
ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento_aud ALTER COLUMN mov_presupuesto_fk  TYPE bigint;
ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento ADD CONSTRAINT sg_mov_presupuesto_fk FOREIGN KEY (mov_presupuesto_fk) REFERENCES finanzas.sg_presupuesto_escolar(pes_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento ALTER COLUMN mov_monto  TYPE numeric(10,2);
ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento_aud ALTER COLUMN mov_monto  TYPE numeric(10,2);


ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento DROP CONSTRAINT mov_presupuesto_fk_uk;
ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento DROP CONSTRAINT mov_codigo_uk;


-- 1.0.2
ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento ADD mov_origen character varying(2);
ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento_aud ADD mov_origen character varying(2);

ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento ADD COLUMN mov_actividad_pk bigint;
ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento_aud ADD COLUMN mov_actividad_pk bigint;
ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento ADD CONSTRAINT sg_mov_actividad_pk FOREIGN KEY (mov_actividad_pk) REFERENCES centros_educativos.sg_detalle_plan_escolar(dpe_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

-- Dirección Departamental
CREATE SEQUENCE IF NOT EXISTS finanzas.sg_direccion_departamental_ded_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS finanzas.sg_direccion_departamental (ded_pk bigint NOT NULL DEFAULT nextval('finanzas.sg_direccion_departamental_ded_pk_seq'::regclass),  ded_nombre character varying(255), ded_nombre_busqueda character varying(255),ded_habilitado boolean, ded_departamente_fk bigint,ded_ult_mod_fecha timestamp without time zone, ded_ult_mod_usuario character varying(45), ded_version integer, CONSTRAINT sg_direccion_departamental_pkey PRIMARY KEY (ded_pk),  CONSTRAINT ded_nombre_uk UNIQUE (ded_nombre), CONSTRAINT sg_ded_departamente_fk FOREIGN KEY (ded_departamente_fk) REFERENCES catalogo.sg_departamentos(dep_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
COMMENT ON TABLE finanzas.sg_direccion_departamental IS 'Tabla para el registro de dirección departamental.';
COMMENT ON COLUMN finanzas.sg_direccion_departamental.ded_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN finanzas.sg_direccion_departamental.ded_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN finanzas.sg_direccion_departamental.ded_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN finanzas.sg_direccion_departamental.ded_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN finanzas.sg_direccion_departamental.ded_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN finanzas.sg_direccion_departamental.ded_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS finanzas.sg_direccion_departamental_aud (ded_pk bigint NOT NULL,ded_departamente_fk bigint, ded_nombre character varying(255), ded_nombre_busqueda character varying(255),ded_habilitado boolean, ded_ult_mod_fecha timestamp without time zone, ded_ult_mod_usuario character varying(45), ded_version integer, rev bigint, revtype smallint);
ALTER TABLE finanzas.sg_direccion_departamental_aud ADD PRIMARY KEY (ded_pk, rev);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_DIRECCION_DEPARTAMENTAL','F7',  'Crear dirección departamental', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_DIRECCION_DEPARTAMENTAL','F8',  'Actualizar dirección departamental', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_DIRECCION_DEPARTAMENTAL','F9',  'Eliminar dirección departamental', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_DIRECCION_DEPARTAMENTAL','FM4',  '', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_DIRECCION_DEPARTAMENTAL','FB3',  '', 12, true, null, null,0);

--07.09.2020
--CREACION DE TABLA PARA LA GESTIÒN DE LAS TRANSFERENCIAS DE DIRECCIONES DEPARTAMENTALES
CREATE SEQUENCE IF NOT EXISTS finanzas.sg_transferencia_direccion_departamental_pk_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1
    NO CYCLE;

CREATE TABLE IF NOT EXISTS finanzas.sg_transferencia_direccion_departamental (
    tdd_pk bigint NOT NULL DEFAULT nextval('finanzas.sg_transferencia_direccion_departamental_pk_seq'::regclass),
    tdd_transferencia_fk integer,
    tdd_direccion_dep_fk integer,
    tdd_monto_autorizado decimal NOT NULL,
    tdd_monto_solicitado decimal NOT NULL,
    tdd_estado character varying(12) NOT NULL,
    tdd_ult_mod_fecha timestamp without time zone,
    tdd_ult_mod_usuario character varying(45),
    tdd_version integer,
    CONSTRAINT sg_transferencia_direccion_departamental_pkey PRIMARY KEY (tdd_pk),
    CONSTRAINT tdd_transferencia_fk_uk UNIQUE (tdd_transferencia_fk),
    CONSTRAINT tdd_direccion_dep_fk_uk UNIQUE (tdd_direccion_dep_fk)
);
COMMENT ON TABLE finanzas.sg_transferencia_direccion_departamental IS 'Tabla para el registro de las transferencias a dirección departamental.';
COMMENT ON COLUMN finanzas.sg_transferencia_direccion_departamental.tdd_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN finanzas.sg_transferencia_direccion_departamental.tdd_transferencia_fk IS 'Indica la llave de la transferencia a la que pertenece.';
COMMENT ON COLUMN finanzas.sg_transferencia_direccion_departamental.tdd_direccion_dep_fk IS 'Indica la llave de la dirección a la que pertenece la transferencia';
COMMENT ON COLUMN finanzas.sg_transferencia_direccion_departamental.tdd_monto_autorizado IS 'Muestra el valor en $ del monto autorizado del registro';
COMMENT ON COLUMN finanzas.sg_transferencia_direccion_departamental.tdd_monto_solicitado IS 'Muestra el valor en $ del monto solicitado del registro';
COMMENT ON COLUMN finanzas.sg_transferencia_direccion_departamental.tdd_estado IS 'Muesta el estado de la transferencia (Autorizado, Solicitado, Transferido, Cerrado)';
COMMENT ON COLUMN finanzas.sg_transferencia_direccion_departamental.tdd_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN finanzas.sg_transferencia_direccion_departamental.tdd_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN finanzas.sg_transferencia_direccion_departamental.tdd_version IS 'Versión del registro.';

CREATE TABLE IF NOT EXISTS finanzas.sg_transferencia_direccion_departamental_aud (
    tdd_pk bigint NOT NULL,
    tdd_transferencia_fk integer,
    tdd_direccion_dep_fk integer,
    tdd_monto_autorizado decimal,
    tdd_monto_solicitado decimal,
    tdd_estado character varying(12),
    tdd_ult_mod_fecha timestamp without time zone,
    tdd_ult_mod_usuario character varying(45),
    tdd_version integer,
    rev bigint,
    revtype smallint,
    CONSTRAINT sg_transferencia_direccion_departamental_aud_pkey PRIMARY KEY (tdd_pk,rev),
    CONSTRAINT sg_transferencia_direccion_departamental_aud_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev)
);

-- OPERACIONES DE TRANSFERENCIA
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_TRANSFERENCIA_DIRECCION_DEPARTAMENTAL','FB4',  '', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_TRANSFERENCIA_DIRECCION_DEPARTAMENTAL','F10',  'Crear transferencia a dirección departamental', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_TRANSFERENCIA_DIRECCION_DEPARTAMENTAL','F11',  'Actualizar transferencia a dirección departamental', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_TRANSFERENCIA_DIRECCION_DEPARTAMENTAL','F12',  'Eliminar transferencia a dirección departamental', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_TRANSFERENCIA_DIRECCION_DEPARTAMENTAL','FM5',  '', 12, true, null, null,0);

ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento ADD COLUMN mov_num_movimiento int;
ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento_aud ADD COLUMN mov_num_movimiento int;


ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento ADD COLUMN mov_fuente_ingreso_pk bigint;
ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento_aud ADD COLUMN mov_fuente_ingreso_pk bigint;

--CREACION DE TABLA PARA LA DOCUMENTACIÓN DE PRESUPUESTOS ESCOLARES
create sequence if not exists finanzas.sg_documentos_presupuesto_pk_seq 
    increment 1 
    start 1 
    minvalue 1 
    maxvalue 9223372036854775807 
    cache 1     
    no cycle;
   

create table if not exists finanzas.sg_documentos_presupuesto
(
    doc_pk bigint NOT NULL DEFAULT nextval('finanzas.sg_documentos_presupuesto_pk_seq'::regclass), 
    doc_presupuesto_fk bigint,
    doc_archivo_fk bigint,
    doc_tipo_documento varchar(255),
    doc_estado_documento varchar(255),
    doc_ult_mod_fecha timestamp without time zone, 
    doc_ult_mod_usuario character varying(45), 
    doc_version integer, 
    constraint sg_ejecucion_presupuesto_pkey PRIMARY KEY (doc_pk), 
    constraint presupuesto_escolar_fk foreign key (doc_presupuesto_fk) references finanzas.sg_presupuesto_escolar(pes_pk) match full on delete restrict on update cascade,
    constraint archivo_presupuesto_fk foreign key (doc_archivo_fk) references public.sg_archivos(ach_pk)
);

CREATE TABLE IF NOT EXISTS finanzas.sg_documentos_presupuesto_aud (
    doc_pk bigint NOT NULL, 
    doc_presupuesto_fk bigint,
    doc_archivo_fk bigint,
    doc_tipo_documento varchar(255),
    doc_estado_documento varchar(255),
    doc_ult_mod_fecha timestamp without time zone, 
    doc_ult_mod_usuario character varying(45), 
    doc_version integer, 
    rev bigint, 
    revtype smallint, 
    CONSTRAINT sg_documentos_presupuesto_aud_pkey PRIMARY KEY (doc_pk,rev), 
    CONSTRAINT sg_documentos_presupuesto_aud_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev)
);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_DOCUMENTOS','FB5',  '', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_DOCUMENTOS','F13',  'Crear documentos de presupuesto', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_DOCUMENTOS','F14',  'Actualizar documentos de presupuesto', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_DOCUMENTOS','F15',  'Eliminar documentos de presupuesto', 12, true, null, null,0);

-- 1.0.3
create sequence if not exists finanzas.sg_cuentas_bancarias_cbc_pk_seq increment 1 start 1 minvalue 1 maxvalue 2147483647 cache 1;

create table if not exists finanzas.sg_cuentas_bancarias (
cbc_pk bigint not null default nextval('finanzas.sg_cuentas_bancarias_cbc_pk_seq'::regclass),
cbc_numero_cuenta character varying(50),
cbc_habilitado boolean,
cbc_comentarios varchar(4000),
cbc_sede_fk int8,
cbc_ult_mod_fecha timestamp without time zone,
cbc_ult_mod_usuario character varying(45),
cbc_version integer,
constraint sg_cuentas_bancarias_pkey primary key (cbc_pk),
constraint sg_cuentas_bancarias_sede_fk FOREIGN KEY (cbc_sede_fk) REFERENCES centros_educativos.sg_sedes(sed_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE
);

COMMENT ON TABLE finanzas.sg_cuentas_bancarias IS 'Tabla para el registro de cuentas bancarias.';
COMMENT ON COLUMN finanzas.sg_cuentas_bancarias.cbc_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN finanzas.sg_cuentas_bancarias.cbc_numero_cuenta IS 'Número de cuenta.';
COMMENT ON COLUMN finanzas.sg_cuentas_bancarias.cbc_comentarios IS 'Comentarios de cuentas bancarias.';
COMMENT ON COLUMN finanzas.sg_cuentas_bancarias.cbc_sede_fk IS 'Sede educativa.';
COMMENT ON COLUMN finanzas.sg_cuentas_bancarias.cbc_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN finanzas.sg_cuentas_bancarias.cbc_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN finanzas.sg_cuentas_bancarias.cbc_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN finanzas.sg_cuentas_bancarias.cbc_version IS 'Versión del registro.';

create table if not exists finanzas.sg_cuentas_bancarias_aud (
cbc_pk bigint not null,
cbc_numero_cuenta character varying(50),
cbc_habilitado boolean,
cbc_comentarios varchar(4000),
cbc_sede_fk int8,
cbc_ult_mod_fecha timestamp without time zone,
cbc_ult_mod_usuario character varying(45),
cbc_version integer,
rev bigint,
revtype smallint);
ALTER TABLE finanzas.sg_cuentas_bancarias_aud ADD PRIMARY KEY (cbc_pk, rev);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_CUENTAS_BANCARIAS','FB6',  '', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_CUENTAS_BANCARIAS','F16',  'Crear cuentas bancarias', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_CUENTAS_BANCARIAS','F17',  'Actualizar cuentas bancarias', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_CUENTAS_BANCARIAS','F18',  'Eliminar cuentas bancarias', 12, true, null, null,0);

-- Movimiento Cuenta Bancaria
create sequence if not exists finanzas.sg_movimiento_cuenta_bancaria_mcb_pk_seq increment 1 start 1 minvalue 1 maxvalue 2147483647 cache 1;

create table if not exists finanzas.sg_movimiento_cuenta_bancaria (
mcb_pk bigint not null default nextval('finanzas.sg_movimiento_cuenta_bancaria_mcb_pk_seq'::regclass),
mcb_cuenta_fk int8,
mcb_fecha timestamp without time zone,
mcb_detalle character varying(255),
mcb_monto decimal,
mcb_tipo character varying(255), 
mcb_ult_mod_fecha timestamp without time zone,
mcb_ult_mod_usuario character varying(45),
mcb_version integer,
constraint sg_movimiento_cuenta_bancaria_pkey primary key (mcb_pk),
constraint sg_movimiento_cuentas_bancarias_fk FOREIGN KEY (mcb_cuenta_fk) REFERENCES finanzas.sg_cuentas_bancarias(cbc_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE
);


COMMENT ON TABLE finanzas.sg_movimiento_cuenta_bancaria IS 'Tabla para el registro de movimiento cuenta bancaria.';
COMMENT ON COLUMN finanzas.sg_movimiento_cuenta_bancaria.mcb_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN finanzas.sg_movimiento_cuenta_bancaria.mcb_cuenta_fk IS 'Cuenta bancaria a la que se le hará el movimiento.';
COMMENT ON COLUMN finanzas.sg_movimiento_cuenta_bancaria.mcb_fecha IS 'Fecha del movimiento.';
COMMENT ON COLUMN finanzas.sg_movimiento_cuenta_bancaria.mcb_monto IS 'Monto del movimiento';
COMMENT ON COLUMN finanzas.sg_movimiento_cuenta_bancaria.mcb_tipo IS 'Tipo de movimiento';
COMMENT ON COLUMN finanzas.sg_movimiento_cuenta_bancaria.mcb_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN finanzas.sg_movimiento_cuenta_bancaria.mcb_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN finanzas.sg_movimiento_cuenta_bancaria.mcb_version IS 'Versión del registro.';

create table if not exists finanzas.sg_movimiento_cuenta_bancaria_aud (
mcb_pk bigint not null,
mcb_cuenta_fk int8,
mcb_fecha timestamp without time zone,
mcb_detalle character varying(255),
mcb_monto decimal,
mcb_tipo character varying(255), 
mcb_ult_mod_fecha timestamp without time zone,
mcb_ult_mod_usuario character varying(45),
mcb_version integer,
rev bigint,
revtype smallint);

ALTER TABLE finanzas.sg_movimiento_cuenta_bancaria_aud ADD PRIMARY KEY (mcb_pk, rev);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_CUENTAS_BANCARIAS','FM6',  '', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_MOVIMIENTO_CUENTA_BANCARIA','FB7',  '', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_MOVIMIENTO_CUENTA_BANCARIA','F19',  'Crear un nuevo movimiento de cuenta', 12, true, null, null,0);

--Rubros 

ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento ADD COLUMN mov_rubro_pk bigint;
ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento_aud ADD COLUMN mov_rubro_pk bigint;
ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento ADD CONSTRAINT sg_mov_rubro_pk FOREIGN KEY (mov_rubro_pk) 
REFERENCES siap2.ss_rubro(ru_id) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;


-- AGREGANDO COLUMNA DE MONTO APROBADO 
ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento ADD COLUMN mov_monto_aprobado decimal;
ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento_aud ADD COLUMN mov_monto_aprobado decimal;
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_movimiento.mov_monto_aprobado IS 'Muestra el valor aprobado en $ por el cual el se realizo el registro';


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_RUBRO','FB8',  'Busca Rubro', 12, true, null, null,0);

-- 1.0.4
-- Transferencias a Centros Educativos
CREATE SEQUENCE IF NOT EXISTS finanzas.sg_transferencias_a_ced_tac_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS finanzas.sg_transferencias_a_ced 
(tac_pk bigint NOT NULL DEFAULT nextval('finanzas.sg_transferencias_a_ced_tac_pk_seq'::regclass), 
 tac_ced_fk integer, 
 tac_transferencia_fk integer,
 tac_transferencia_direccion_dep_fk integer,
 tac_monto_autorizado decimal NOT NULL, 
 tac_monto_solicitado decimal NOT NULL, 
 tac_estado character varying(12) NOT NULL,
 tac_ult_mod_fecha timestamp without time zone, 
 tac_ult_mod_usuario character varying(45), 
 tac_version integer, 
 CONSTRAINT sg_transferencias_a_ced_pkey PRIMARY KEY (tac_pk));

COMMENT ON TABLE finanzas.sg_transferencias_a_ced IS 'Tabla para el registro de transferencias a centros educativos.';
COMMENT ON COLUMN finanzas.sg_transferencias_a_ced.tac_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN finanzas.sg_transferencias_a_ced.tac_ced_fk IS 'Indica la llave del centro educativo (Sede) a la que pertenece el registro.';
COMMENT ON COLUMN finanzas.sg_transferencias_a_ced.tac_transferencia_fk IS 'Indica la llave de la transferencia a la que pertenece el registro.';
COMMENT ON COLUMN finanzas.sg_transferencias_a_ced.tac_transferencia_direccion_dep_fk IS 'Indica la llave de la transferencia dirección departamental a la que pertenece el registro.';
COMMENT ON COLUMN finanzas.sg_transferencias_a_ced.tac_monto_autorizado IS 'Muestra el valor en $ del monto autorizado del registro';
COMMENT ON COLUMN finanzas.sg_transferencias_a_ced.tac_monto_solicitado IS 'Muestra el valor en $ del monto solicitado del registro';
COMMENT ON COLUMN finanzas.sg_transferencias_a_ced.tac_estado IS 'Muesta el estado del registro';
COMMENT ON COLUMN finanzas.sg_transferencias_a_ced.tac_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN finanzas.sg_transferencias_a_ced.tac_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN finanzas.sg_transferencias_a_ced.tac_version IS 'Versión del registro.';

CREATE TABLE IF NOT EXISTS finanzas.sg_transferencias_a_ced_aud 
(tac_pk bigint NOT NULL, 
 tac_ced_fk integer, 
 tac_transferencia_fk integer,
 tac_transferencia_direccion_dep_fk integer,
 tac_monto_autorizado decimal NOT NULL, 
 tac_monto_solicitado decimal NOT NULL, 
 tac_estado character varying(12) NOT NULL, 
 tac_ult_mod_fecha timestamp without time zone, 
 tac_ult_mod_usuario character varying(45), 
 tac_version integer, 
 rev bigint, 
 revtype smallint);
ALTER TABLE finanzas.sg_transferencias_a_ced_aud ADD PRIMARY KEY (tac_pk, rev);


-- RELACION DE TRANSFERENCIA A CED CON CENTROS EDUCATIVOS(SEDES)
ALTER TABLE finanzas.sg_transferencias_a_ced ADD CONSTRAINT tac_ced_fkey FOREIGN KEY (tac_ced_fk) REFERENCES centros_educativos.sg_sedes(sed_pk) MATCH FULL ON DELETE CASCADE ON UPDATE CASCADE;

-- RELACION DE TRANSFERENCIA A CED CON TRANSFERENCIA DIRECCION DEP
ALTER TABLE finanzas.sg_transferencias_a_ced ADD CONSTRAINT tac_transferencia_direccion_dep_fkey FOREIGN KEY (tac_transferencia_direccion_dep_fk) REFERENCES finanzas.sg_transferencia_direccion_departamental(tdd_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_TRANSFERENCIAS_A_CED','FB10',  'Buscar Transferencias a Centros Educativos', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_TRANSFERENCIAS_A_CED','F20',  'Crear Transferencia a Centros Educativos', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_TRANSFERENCIAS_A_CED','F21',  'Actualizar Transferencia a Centros Educativos', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_TRANSFERENCIAS_A_CED','F22',  'Eliminar Transferencia a Centros Educativos', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_TRANSFERENCIA_A_CED','FM7',  'Menú a Transferencias a Centros Educativos', 12, true, null, null,0);


ALTER TABLE finanzas.sg_transferencias_a_ced  ADD CONSTRAINT sg_tced_trasnferencia_componente_fkey FOREIGN KEY (tac_transferencia_fk) REFERENCES siap2.ss_transferencias_componente(tc_id) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE finanzas.sg_transferencias_a_ced ALTER COLUMN tac_monto_solicitado DROP NOT NULL;

--AGREGANDO COLUMNA DE AREA DE INVERSION 
ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento ADD COLUMN mov_area_inversion_pk bigint;
ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento_aud ADD COLUMN mov_area_inversion_pk bigint;
ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento ADD CONSTRAINT sg_mov_area_inversion_pk FOREIGN KEY (mov_area_inversion_pk) 
REFERENCES siap2.ss_areas_inversion(ai_id) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_movimiento.mov_area_inversion_pk IS 'Indica la llave del area de inversion a la que pertenece el Movimiento';


-- CORRER LA TABLA DE DESEMBOLSO DE NUEVO
CREATE SEQUENCE IF NOT EXISTS finanzas.sg_desembolsos_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;

create table finanzas.sg_desembolsos (
	dsb_pk bigint not null default nextval('finanzas.sg_desembolsos_pk_seq'::regclass),
	 CONSTRAINT sg_desembolsos_pkey PRIMARY KEY (dsb_pk));

ALTER TABLE finanzas.sg_desembolsos ADD COLUMN dsb_ult_mod timestamp;
ALTER TABLE finanzas.sg_desembolsos ADD COLUMN dsb_ult_usuario varchar(255);
ALTER TABLE finanzas.sg_desembolsos ADD COLUMN dsb_version int8;
alter table finanzas.sg_desembolsos add column dsb_porcentaje decimal;
alter table finanzas.sg_desembolsos add column dsb_monto decimal;
alter table finanzas.sg_desembolsos add column dsb_estado varchar(1);

	
	create table finanzas.sg_desembolsos_aud (
	dsb_pk int8 not  null,
	dsb_ult_mod timestamp,
	dsb_ult_usuario varchar(255),
	dsb_version int8);
 
alter table finanzas.sg_desembolsos_aud add column dsb_porcentaje decimal;
alter table finanzas.sg_desembolsos_aud add column dsb_monto decimal;
alter table finanzas.sg_desembolsos_aud add column dsb_estado varchar(1);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_DESEMBOLSO','DS1',  'Crea Desembolsos', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_DESEMBOLSO','DS2',  'Actualiza Desembolsos', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_DESEMBOLSO','DS3',  'Elimina Desembolsos', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_DESEMBOLSO','DS4',  'Busca Desembolsos', 12, true, null, null,0);

-- 1.0.5
--AGREGANDO COLUMNA DE SUBAREA DE INVERSION 
ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento ADD COLUMN mov_subarea_inversion_pk bigint;
ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento_aud ADD COLUMN mov_subarea_inversion_pk bigint;
ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento ADD CONSTRAINT sg_mov_subarea_inversion_pk FOREIGN KEY (mov_subarea_inversion_pk) 
REFERENCES siap2.ss_areas_inversion(ai_id) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_movimiento.mov_subarea_inversion_pk IS 'Indica la llave de la subarea de inversion a la que pertenece el Movimiento';


--campo estado de desembolsos amplia largo
ALTER TABLE finanzas.sg_desembolsos ALTER COLUMN dsb_estado TYPE varchar(20) USING dsb_estado::varchar;

-- OPERACION AREA DE INVERSION
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_AREA_INVERSION','FB11',  'Buscar Areas de Inversion de Siap2', 12, true, null, null,0);


-- 1.0.7
-- Alter a la tabla de cuentas bancarias
alter table finanzas.sg_cuentas_bancarias 
add cbc_titular varchar(250),
add cbc_banco_fk int8;

alter table finanzas.sg_cuentas_bancarias_aud
add cbc_titular varchar(250),
add cbc_banco_fk int8;

alter table finanzas.sg_cuentas_bancarias add constraint cbc_cuentas_bancarias_bancos_fk foreign key (cbc_banco_fk) references catalogo.sg_bancos (bnc_pk);

COMMENT ON COLUMN finanzas.sg_cuentas_bancarias.cbc_titular IS 'Nombre del titular de la cuenta.';
COMMENT ON COLUMN finanzas.sg_cuentas_bancarias.cbc_banco_fk IS 'Banco en que se registró la cuenta.';


CREATE SEQUENCE IF NOT EXISTS finanzas.sg_solicitudes_transferencia_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;

create table finanzas.sg_solicitudes_transferencia (
	str_pk int8 not null DEFAULT nextval('finanzas.sg_solicitudes_transferencia_pk_seq'::regclass),
	str_sac_goes varchar(45),
	str_sac_ufi varchar(45),
	str_compromiso_presupuestario varchar(45),
	str_importe_total decimal(10,2),
	str_estado varchar(45),
	str_habilitado boolean,
	str_version int4,
	str_ult_mod_fecha timestamp,
	str_ult_mod_usuario varchar(45)
);

ALTER TABLE finanzas.sg_solicitudes_transferencia ADD PRIMARY KEY (str_pk);


COMMENT ON TABLE finanzas.sg_solicitudes_transferencia IS 'Tabla para el registro de transferencias a centros educativos.';
COMMENT ON COLUMN finanzas.sg_solicitudes_transferencia.str_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN finanzas.sg_solicitudes_transferencia.str_sac_goes IS 'Código SAC de Hacienda del registro.';
COMMENT ON COLUMN finanzas.sg_solicitudes_transferencia.str_sac_ufi IS 'Código SAC de la Unidad Financiera el registro.';
COMMENT ON COLUMN finanzas.sg_solicitudes_transferencia.str_compromiso_presupuestario IS 'Muestra el compromiso presupuestario del registro.';
COMMENT ON COLUMN finanzas.sg_solicitudes_transferencia.str_importe_total IS 'Muestra el valor del importe total del registro';
COMMENT ON COLUMN finanzas.sg_solicitudes_transferencia.str_estado IS 'Muesta el estado del registro';
COMMENT ON COLUMN finanzas.sg_solicitudes_transferencia.str_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN finanzas.sg_solicitudes_transferencia.str_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN finanzas.sg_solicitudes_transferencia.str_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN finanzas.sg_solicitudes_transferencia.str_version IS 'Versión del registro.';

create table finanzas.sg_solicitudes_transferencia_aud (
	str_pk int8 not null,
	str_sac_goes varchar(45),
	str_sac_ufi varchar(45),
	str_compromiso_presupuestario varchar(45),
	str_importe_total decimal(10,2),
	str_estado varchar(45),
	str_habilitado boolean,
	str_version int4,
	str_ult_mod_fecha timestamp,
	str_ult_mod_usuario varchar(45),
	rev int8,
	revtype int2
);
ALTER TABLE finanzas.sg_solicitudes_transferencia_aud ADD PRIMARY KEY (str_pk, rev);




--AGREGANDO COLUMNA PARA MOVIMIENTO DEL AÑO. 
ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento ADD COLUMN mov_enero decimal;
ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento_aud ADD COLUMN mov_enero decimal;
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_movimiento.mov_enero IS 'Indica el valor de egreso parea el Movimiento del mes de Enero';

ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento ADD COLUMN mov_febrero decimal;
ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento_aud ADD COLUMN mov_febrero decimal;
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_movimiento.mov_febrero IS 'Indica el valor de egreso parea el Movimiento del mes de Febrero';

ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento ADD COLUMN mov_marzo decimal;
ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento_aud ADD COLUMN mov_marzo decimal;
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_movimiento.mov_marzo IS 'Indica el valor de egreso parea el Movimiento del mes de Marzo';

ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento ADD COLUMN mov_abril decimal;
ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento_aud ADD COLUMN mov_abril decimal;
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_movimiento.mov_abril IS 'Indica el valor de egreso parea el Movimiento del mes de Abril';

ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento ADD COLUMN mov_mayo decimal;
ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento_aud ADD COLUMN mov_mayo decimal;
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_movimiento.mov_mayo IS 'Indica el valor de egreso parea el Movimiento del mes de Mayo';

ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento ADD COLUMN mov_junio decimal;
ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento_aud ADD COLUMN mov_junio decimal;
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_movimiento.mov_junio IS 'Indica el valor de egreso parea el Movimiento del mes de Junio';

ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento ADD COLUMN mov_julio decimal;
ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento_aud ADD COLUMN mov_julio decimal;
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_movimiento.mov_julio IS 'Indica el valor de egreso parea el Movimiento del mes de Julio';

ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento ADD COLUMN mov_agosto decimal;
ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento_aud ADD COLUMN mov_agosto decimal;
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_movimiento.mov_agosto IS 'Indica el valor de egreso parea el Movimiento del mes de Agosto';

ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento ADD COLUMN mov_septiembre decimal;
ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento_aud ADD COLUMN mov_septiembre decimal;
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_movimiento.mov_septiembre IS 'Indica el valor de egreso parea el Movimiento del mes de Septiembre';

ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento ADD COLUMN mov_octubre decimal;
ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento_aud ADD COLUMN mov_octubre decimal;
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_movimiento.mov_octubre IS 'Indica el valor de egreso parea el Movimiento del mes de Octubre';

ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento ADD COLUMN mov_noviembre decimal;
ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento_aud ADD COLUMN mov_noviembre decimal;
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_movimiento.mov_noviembre IS 'Indica el valor de egreso parea el Movimiento del mes de Noviembre';

ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento ADD COLUMN mov_diciembre decimal;
ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento_aud ADD COLUMN mov_diciembre decimal;
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_movimiento.mov_diciembre IS 'Indica el valor de egreso parea el Movimiento del mes de Diciembre';

ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento ADD COLUMN mov_total_anual decimal;
ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento_aud ADD COLUMN mov_total_anual decimal;
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_movimiento.mov_total_anual IS 'Indica el valor de egreso parea el Movimiento durantel el año';

--- VALOR POR DEFECTO 0
ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento ALTER COLUMN mov_enero SET DEFAULT 0;
ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento ALTER COLUMN mov_febrero SET DEFAULT 0;
ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento ALTER COLUMN mov_marzo SET DEFAULT 0;
ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento ALTER COLUMN mov_abril SET DEFAULT 0;
ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento ALTER COLUMN mov_mayo SET DEFAULT 0;
ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento ALTER COLUMN mov_junio SET DEFAULT 0;
ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento ALTER COLUMN mov_julio SET DEFAULT 0;
ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento ALTER COLUMN mov_agosto SET DEFAULT 0;
ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento ALTER COLUMN mov_septiembre SET DEFAULT 0;
ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento ALTER COLUMN mov_octubre SET DEFAULT 0;
ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento ALTER COLUMN mov_noviembre SET DEFAULT 0;
ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento ALTER COLUMN mov_diciembre SET DEFAULT 0;


--- SE AGREGA COLUMNA PARA CARGO DIRECTOR 
ALTER TABLE finanzas.sg_direccion_departamental ADD COLUMN ded_director_cargo character varying(255);
ALTER TABLE finanzas.sg_direccion_departamental_aud ADD COLUMN ded_director_cargo character varying(255);
COMMENT ON COLUMN finanzas.sg_direccion_departamental.ded_director_cargo IS 'Nombre del registro normalizado para cargo Director';

--- SE AGREGA COLUMNA PARA NOMBRE DIRECTOR
ALTER TABLE finanzas.sg_direccion_departamental ADD COLUMN ded_director_nombre character varying(255);
ALTER TABLE finanzas.sg_direccion_departamental_aud ADD COLUMN ded_director_nombre character varying(255);
COMMENT ON COLUMN finanzas.sg_direccion_departamental.ded_director_nombre IS 'Nombre del registro normalizado para nombre Director';

--- SE AGREGA COLUMNA PARA CARGO PAGADOR
ALTER TABLE finanzas.sg_direccion_departamental ADD COLUMN ded_pagador_cargo character varying(255);
ALTER TABLE finanzas.sg_direccion_departamental_aud ADD COLUMN ded_pagador_cargo character varying(255);
COMMENT ON COLUMN finanzas.sg_direccion_departamental.ded_pagador_cargo IS 'Nombre del registro normalizado para cargo Pagador';

--- SE AGREGA COLUMNA PARA NOMBRE PAGADOR
ALTER TABLE finanzas.sg_direccion_departamental ADD COLUMN ded_pagador_nombre character varying(255);
ALTER TABLE finanzas.sg_direccion_departamental_aud ADD COLUMN ded_pagador_nombre character varying(255);
COMMENT ON COLUMN finanzas.sg_direccion_departamental.ded_pagador_nombre IS 'Nombre del registro normalizado para nombre Pagador';


--- SE AGREGA COLUMNA PARA CARGO REFRENDARIO
ALTER TABLE finanzas.sg_direccion_departamental ADD COLUMN ded_refrendario_cargo character varying(255);
ALTER TABLE finanzas.sg_direccion_departamental_aud ADD COLUMN ded_refrendario_cargo character varying(255);
COMMENT ON COLUMN finanzas.sg_direccion_departamental.ded_refrendario_cargo IS 'Nombre del registro normalizado para cargo Refrendario';


--- SE AGREGA COLUMNA PARA NOMBRE REFRENDARIO
ALTER TABLE finanzas.sg_direccion_departamental ADD COLUMN ded_refrendario_nombre character varying(255);
ALTER TABLE finanzas.sg_direccion_departamental_aud ADD COLUMN ded_refrendario_nombre character varying(255);
COMMENT ON COLUMN finanzas.sg_direccion_departamental.ded_refrendario_nombre IS 'Nombre del registro normalizado para nombre Refrendario';

--Recibos
CREATE SEQUENCE IF NOT EXISTS finanzas.sg_recibos_rec_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
create table if not exists finanzas.sg_recibos (
rec_pk bigint not null default nextval('finanzas.sg_recibos_rec_pk_seq'::regclass),
rec_transferencia_fk bigint,
rec_archivo_fk bigint,
rec_fecha timestamp without time zone,
rec_monto decimal,
rec_ult_mod_fecha timestamp without time zone,
rec_ult_mod_usuario character varying(45),
rec_version integer,
constraint sg_recibos_pkey primary key (rec_pk),
constraint archivo_recibo_fk foreign key (rec_archivo_fk) references public.sg_archivos(ach_pk),
constraint transferencia_recibo_fk foreign key (rec_transferencia_fk) references finanzas.sg_transferencias_a_ced(tac_pk)
);

COMMENT ON TABLE finanzas.sg_recibos IS 'Tabla para el registro de recibos.';
COMMENT ON COLUMN finanzas.sg_recibos.rec_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN finanzas.sg_recibos.rec_transferencia_fk IS 'Id de transferencia a centro educativo.';
COMMENT ON COLUMN finanzas.sg_recibos.rec_archivo_fk IS 'Id de archivo.';
COMMENT ON COLUMN finanzas.sg_recibos.rec_fecha IS 'Fecha del registro.';
COMMENT ON COLUMN finanzas.sg_recibos.rec_monto IS 'Monto del recibo.';
COMMENT ON COLUMN finanzas.sg_recibos.rec_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN finanzas.sg_recibos.rec_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN finanzas.sg_recibos.rec_version IS 'Versión del registro.';

create table if not exists finanzas.sg_recibos_aud (
rec_pk bigint not null,
rec_transferencia_fk bigint,
rec_archivo_fk bigint,
rec_fecha timestamp without time zone,
rec_monto decimal,
rec_ult_mod_fecha timestamp without time zone,
rec_ult_mod_usuario character varying(45),
rec_version integer,
rev bigint,
revtype smallint);
ALTER TABLE finanzas.sg_recibos_aud ADD PRIMARY KEY (rec_pk, rev);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_RECIBOS','FB12',  'Buscar recibos', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_RECIBOS','F23',  'Crear recibos', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_RECIBOS','F24',  'Actualizar recibos', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_RECIBOS','F25',  'Eliminar recibos', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_RECIBOS','FM8',  'Menú a la gestión de recibos', 12, true, null, null,0);


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_CONSULTAS_ACUMULATIVAS ','FM10',  'Menú de consultas acumulativas', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_SOLICITUDES_DE_TRANSFERENCIA','FM11',  'Menú de solicitudes de Transferencia', 12, true, null, null,0);


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_SOLICITUD_DE_TRANSFERENCIA','F33',  'Crear Solicitud de Transferencia', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_SOLICITUD_DE_TRANSFERENCIA','F34',  'Actualizar Solicitud de Transferencia', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_SOLICITUD_DE_TRANSFERENCIA','F35',  'Eliminar Solicitud de Transferencia', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_SOLICITUD_DE_TRANSFERENCIA','FB15',  'Buscar Solicitud de Transferencia', 12, true, null, null,0);

-- Cuentas Bancarias Dirección Departamental
create sequence if not exists finanzas.sg_cuenta_bancaria_dd_cbd_pk_seq increment 1 start 1 minvalue 1 maxvalue 2147483647 cache 1;

create table if not exists finanzas.sg_cuenta_bancaria_dd (
cbd_pk bigint not null default nextval('finanzas.sg_cuenta_bancaria_dd_cbd_pk_seq'::regclass),
cbd_numero_cuenta character varying(50),
cbd_titular character varying(250),
cbd_banco_fk int8,
cbd_habilitado boolean,
cbd_comentarios varchar(4000),
cbd_dde_fk int8,
cbd_ult_mod_fecha timestamp without time zone,
cbd_ult_mod_usuario character varying(45),
cbd_version integer,
constraint sg_cuenta_bancaria_dd_pkey primary key (cbd_pk),
constraint sg_cuenta_bancaria_dd_banco foreign key (cbd_banco_fk) references catalogo.sg_bancos (bnc_pk)
);

COMMENT ON TABLE finanzas.sg_cuenta_bancaria_dd IS 'Tabla para el registro de cuentas bancarias dirección departamental.';
COMMENT ON COLUMN finanzas.sg_cuenta_bancaria_dd.cbd_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN finanzas.sg_cuenta_bancaria_dd.cbd_numero_cuenta IS 'Número de cuenta.';
COMMENT ON COLUMN finanzas.sg_cuenta_bancaria_dd.cbd_titular IS 'Titular de la cuenta.';
COMMENT ON COLUMN finanzas.sg_cuenta_bancaria_dd.cbd_banco_fk IS 'Banco al que pertenece la cuenta.';
COMMENT ON COLUMN finanzas.sg_cuenta_bancaria_dd.cbd_dde_fk IS 'Dirección departamental.';
COMMENT ON COLUMN finanzas.sg_cuenta_bancaria_dd.cbd_comentarios IS 'Comentarios.';
COMMENT ON COLUMN finanzas.sg_cuenta_bancaria_dd.cbd_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN finanzas.sg_cuenta_bancaria_dd.cbd_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN finanzas.sg_cuenta_bancaria_dd.cbd_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN finanzas.sg_cuenta_bancaria_dd.cbd_version IS 'Versión del registro.';

create table if not exists finanzas.sg_cuenta_bancaria_dd_aud (
cbd_pk bigint not null,
cbd_numero_cuenta character varying(50),
cbd_titular character varying(250),
cbd_banco_fk int8,
cbd_habilitado boolean,
cbd_comentarios varchar(4000),
cbd_dde_fk int8,
cbd_ult_mod_fecha timestamp without time zone,
cbd_ult_mod_usuario character varying(45),
cbd_version integer,
rev bigint,
revtype smallint);

ALTER TABLE finanzas.sg_cuenta_bancaria_dd_aud ADD PRIMARY KEY (cbd_pk, rev);

alter table finanzas.sg_presupuesto_escolar_movimiento add mov_techo_presupuestal bigint references siap2.ss_tope_presupestal(tp_id);
alter table finanzas.sg_presupuesto_escolar_movimiento_aud add mov_techo_presupuestal bigint;


drop table finanzas.sg_desembolsos_aud;

create table finanzas.sg_desembolsos_aud (
	dsb_pk int8 not  null,
	dsb_ult_mod timestamp,
	dsb_ult_usuario varchar(255),
	dsb_version int8,
	rev bigint, 
	revtype smallint);
 
alter table finanzas.sg_desembolsos_aud add column dsb_porcentaje decimal;
alter table finanzas.sg_desembolsos_aud add column dsb_monto decimal;
alter table finanzas.sg_desembolsos_aud add column dsb_estado varchar(1);


ALTER TABLE finanzas.sg_desembolsos_aud ADD PRIMARY KEY (dsb_pk, rev);

--1.0.8
--SE AGREGA CAMPO NIT PARA DIRECCIÓN DEPARTAMENTAL
alter table finanzas.sg_direccion_departamental add ded_nit varchar(50);
alter table finanzas.sg_direccion_departamental_aud add ded_nit varchar(50);
COMMENT ON COLUMN finanzas.sg_direccion_departamental.ded_nit IS 'Número de NIT de la dirección departamental.';

--1.0.9
-- Cuentas Bancarias Caja Chica
create sequence if not exists finanzas.sg_cuentas_bancarias_bcc_pk_seq increment 1 start 1 minvalue 1 maxvalue 2147483647 cache 1;

create table if not exists finanzas.sg_cuentas_bancarias_cc (
bcc_pk bigint not null default nextval('finanzas.sg_cuentas_bancarias_bcc_pk_seq'::regclass),
bcc_numero_cuenta character varying(50),
bcc_titular character varying(250),
bcc_habilitado boolean,
bcc_comentarios varchar(4000),
bcc_sede_fk int8,
bcc_ult_mod_fecha timestamp without time zone,
bcc_ult_mod_usuario character varying(45),
bcc_version integer,
constraint sg_cuentas_bancarias_cc_pkey primary key (bcc_pk),
constraint sg_cuentas_bancarias_cc_sede_fk FOREIGN KEY (bcc_sede_fk) REFERENCES centros_educativos.sg_sedes(sed_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE
);

COMMENT ON TABLE finanzas.sg_cuentas_bancarias_cc IS 'Tabla para el registro de cuentas bancarias.';
COMMENT ON COLUMN finanzas.sg_cuentas_bancarias_cc.bcc_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN finanzas.sg_cuentas_bancarias_cc.bcc_numero_cuenta IS 'Número de cuenta.';
COMMENT ON COLUMN finanzas.sg_cuentas_bancarias_cc.bcc_titular IS 'Titular de la cuenta.';
COMMENT ON COLUMN finanzas.sg_cuentas_bancarias_cc.bcc_comentarios IS 'Comentarios de cuentas bancarias.';
COMMENT ON COLUMN finanzas.sg_cuentas_bancarias_cc.bcc_sede_fk IS 'Sede educativa.';
COMMENT ON COLUMN finanzas.sg_cuentas_bancarias_cc.bcc_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN finanzas.sg_cuentas_bancarias_cc.bcc_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN finanzas.sg_cuentas_bancarias_cc.bcc_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN finanzas.sg_cuentas_bancarias_cc.bcc_version IS 'Versión del registro.';

create table if not exists finanzas.sg_cuentas_bancarias_cc_aud (
bcc_pk bigint not null,
bcc_numero_cuenta character varying(50),
bcc_titular character varying(250),
bcc_habilitado boolean,
bcc_comentarios varchar(4000),
bcc_sede_fk int8,
bcc_ult_mod_fecha timestamp without time zone,
bcc_ult_mod_usuario character varying(45),
bcc_version integer,
rev bigint,
revtype smallint);
ALTER TABLE finanzas.sg_cuentas_bancarias_cc_aud ADD PRIMARY KEY (bcc_pk, rev);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_CUENTAS_BANCARIAS_CAJA_CHICA','FM12',  'Menú de cuentas bancarias para caja chica de una sede', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_CUENTASBANCARIASCC','FB16',  'Buscar cuentas bancarias de caja chica para una sede', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_CUENTASBANCARIASCC','F36',  'Crear cuentas bancarias de caja chica para sedes', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_CUENTASBANCARIASCC','F37',  'Actualizar cuentas bancarias de caja chica para sedes', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_CUENTASBANCARIASCC','F38',  'Eliminar cuentas bancarias de caja chica para sedes', 12, true, null, null,0);

-- Movimietos de Caja Chica
create sequence if not exists finanzas.sg_movimiento_caja_chica_mcc_pk_seq increment 1 start 1 minvalue 1 maxvalue 2147483647 cache 1;

create table if not exists finanzas.sg_movimiento_caja_chica (
mcc_pk bigint not null default nextval('finanzas.sg_movimiento_caja_chica_mcc_pk_seq'::regclass),
mcc_cuenta_fk int8,
mcc_fecha timestamp without time zone,
mcc_detalle character varying(255),
mcc_monto decimal,
mcc_tipo character varying(255), 
mcc_ult_mod_fecha timestamp without time zone,
mcc_ult_mod_usuario character varying(45),
mcc_version integer,
constraint sg_movimiento_caja_chica_pkey primary key (mcc_pk),
constraint sg_movimiento_caja_chica_fk FOREIGN KEY (mcc_cuenta_fk) REFERENCES finanzas.sg_cuentas_bancarias_cc(bcc_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE
);


COMMENT ON TABLE finanzas.sg_movimiento_caja_chica IS 'Tabla para el registro de movimiento caja chica.';
COMMENT ON COLUMN finanzas.sg_movimiento_caja_chica.mcc_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN finanzas.sg_movimiento_caja_chica.mcc_cuenta_fk IS 'Cuenta bancaria a la que se le hará el movimiento.';
COMMENT ON COLUMN finanzas.sg_movimiento_caja_chica.mcc_fecha IS 'Fecha del movimiento.';
COMMENT ON COLUMN finanzas.sg_movimiento_caja_chica.mcc_monto IS 'Monto del movimiento';
COMMENT ON COLUMN finanzas.sg_movimiento_caja_chica.mcc_tipo IS 'Tipo de movimiento';
COMMENT ON COLUMN finanzas.sg_movimiento_caja_chica.mcc_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN finanzas.sg_movimiento_caja_chica.mcc_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN finanzas.sg_movimiento_caja_chica.mcc_version IS 'Versión del registro.';

create table if not exists finanzas.sg_movimiento_caja_chica_aud (
mcc_pk bigint not null,
mcc_cuenta_fk int8,
mcc_fecha timestamp without time zone,
mcc_detalle character varying(255),
mcc_monto decimal,
mcc_tipo character varying(255), 
mcc_ult_mod_fecha timestamp without time zone,
mcc_ult_mod_usuario character varying(45),
mcc_version integer,
rev bigint,
revtype smallint);

ALTER TABLE finanzas.sg_movimiento_caja_chica_aud ADD PRIMARY KEY (mcc_pk, rev);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_MOVIMIENTOSCAJACHICA','F39',  'Crear movimiento de caja chica', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_MOVIMIENTOSCAJACHICA','FB17',  'Buscar movimientos de caja chica', 12, true, null, null,0);


-- BORRANDO LA RELACION DE PRESUPUESTO CON MOVIMIENTO
ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento DROP CONSTRAINT sg_presupuesto_escolar_movimiento_fkey;

-- CREANDO NUVEA RELACION DELETE ON CASCADE
ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento 
ADD CONSTRAINT sg_presupuesto_escolar_movimiento_fkey 
FOREIGN KEY (mov_presupuesto_fk) REFERENCES finanzas.sg_presupuesto_escolar(pes_pk)
MATCH FULL ON UPDATE CASCADE ON DELETE CASCADE;


--Se agregan los campos de teléfono, fax e ip a dirección departamental
alter table finanzas.sg_direccion_departamental 
add ded_telefono varchar(20),
add ded_fax varchar(20),
add ded_ip_autorizada varchar(50);

alter table finanzas.sg_direccion_departamental_aud 
add ded_telefono varchar(20),
add ded_fax varchar(20),
add ded_ip_autorizada varchar(50);

COMMENT ON COLUMN finanzas.sg_direccion_departamental.ded_telefono IS 'Teléfono de la dirección departamental.';
COMMENT ON COLUMN finanzas.sg_direccion_departamental.ded_fax IS 'Fax de la dirección departamental.';
COMMENT ON COLUMN finanzas.sg_direccion_departamental.ded_ip_autorizada IS 'Ip autorizada para la dirección departamental.';

--Relación de cuentas bancarias y tipo de cuenta
create sequence if not exists finanzas.sg_cbc_tcb_pk_seq increment 1 start 1 minvalue 1 maxvalue 2147483647 cache 1;

create table if not exists finanzas.sg_rel_cbc_tcb (
    rel_pk bigint not null default nextval('finanzas.sg_cbc_tcb_pk_seq'::regclass),
	rel_cbc_pk bigint not null,
	rel_tcb_pk bigint not null,
	rel_ult_mod_fecha timestamp without time zone,
	rel_ult_mod_usuario character varying(45),
	rel_version integer,
	constraint sg_rel_cbc_tcb_pkey primary key (rel_pk),
	constraint rel_cuenta_bancaria_fkey foreign key (rel_cbc_pk) references finanzas.sg_cuentas_bancarias (cbc_pk),
	constraint rel_tipo_cuenta_fkey foreign key (rel_tcb_pk) references catalogo.sg_tipo_cuenta_bancaria (tcb_pk)
);

comment on table finanzas.sg_rel_cbc_tcb is 'Tabla para el registro de la relación de cuentas bancaria a sede y el tipo de cuenta bancaria.';
comment on column finanzas.sg_rel_cbc_tcb.rel_pk is 'Número correlativo autogenerado.';
comment on column finanzas.sg_rel_cbc_tcb.rel_cbc_pk is 'Número de referencia de la cuenta bancaria de una sede';
comment on column finanzas.sg_rel_cbc_tcb.rel_tcb_pk is 'Número de referencia del tipo de cuenta bancaria';
comment on column finanzas.sg_rel_cbc_tcb.rel_ult_mod_fecha is 'Última fecha en la que se modificó el registro.';
comment on column finanzas.sg_rel_cbc_tcb.rel_ult_mod_usuario is 'Último usuario que modificó el registro.';
comment on column finanzas.sg_rel_cbc_tcb.rel_version is 'Versión del registro.';

create table
	if not exists finanzas.sg_rel_cbc_tcb_aud (
    rel_pk bigint not null,
	rel_cbc_pk bigint not null,
	rel_tcb_pk bigint not null,
	rel_ult_mod_fecha timestamp without time zone,
	rel_ult_mod_usuario character varying(45),
	rel_version integer,
	rev bigint,
	revtype smallint
	
);
ALTER TABLE finanzas.sg_rel_cbc_tcb_aud ADD PRIMARY KEY (rel_pk, rev);




alter table finanzas.sg_cuentas_bancarias add column cbc_rel_tcb_fk bigint;
alter table finanzas.sg_cuentas_bancarias_aud add column cbc_rel_tcb_fk bigint;
COMMENT ON COLUMN finanzas.sg_cuentas_bancarias.cbc_rel_tcb_fk IS 'Número de referencia a la tabla intermedia para el tipo de cuenta bancaria del registro';

ALTER TABLE finanzas.sg_cuentas_bancarias 
ADD CONSTRAINT sg_cuentas_bancarias_tipo_cuenta_fkey 
FOREIGN KEY (cbc_rel_tcb_fk) REFERENCES finanzas.sg_rel_cbc_tcb(rel_pk)
MATCH FULL ON UPDATE CASCADE ON DELETE CASCADE;


-- 1.1.0
--Se cambia a plural el nombre de la tabla de dirección departamental
ALTER TABLE finanzas.sg_direccion_departamental RENAME TO sg_direcciones_departamentales;
ALTER TABLE finanzas.sg_direccion_departamental_aud RENAME TO sg_direcciones_departamentales_aud;

--Se renombra el campo de departamento
ALTER TABLE finanzas.sg_direcciones_departamentales RENAME COLUMN ded_departamente_fk TO ded_departamento_fk;
ALTER TABLE finanzas.sg_direcciones_departamentales_aud RENAME COLUMN ded_departamente_fk TO ded_departamento_fk;


-- 1.1.1
ALTER TABLE finanzas.sg_transferencias_a_ced  ADD CONSTRAINT sg_tced_trasnferencia_componente_fkey 
FOREIGN KEY (tac_transferencia_fk) REFERENCES siap2.ss_transferencias_componente(tc_id) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

-- 1.1.2
ALTER TABLE finanzas.sg_recibos DROP CONSTRAINT transferencia_recibo_fk;
ALTER TABLE finanzas.sg_recibos 
ADD constraint transferencia_recibo_fk foreign key (rec_transferencia_fk) references finanzas.sg_transferencias_a_ced(tac_pk)
MATCH FULL ON UPDATE CASCADE ON DELETE cascade;


ALTER TABLE finanzas.sg_transferencias_a_ced DROP CONSTRAINT tac_ced_fkey;
ALTER TABLE finanzas.sg_transferencias_a_ced ADD CONSTRAINT tac_ced_fkey FOREIGN KEY (tac_ced_fk) REFERENCES centros_educativos.sg_sedes(sed_pk) MATCH FULL ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento DROP CONSTRAINT sg_presupuesto_escolar_movimiento_fkey;
ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento 
ADD CONSTRAINT sg_presupuesto_escolar_movimiento_fkey 
FOREIGN KEY (mov_presupuesto_fk) REFERENCES finanzas.sg_presupuesto_escolar(pes_pk)
MATCH FULL ON UPDATE CASCADE ON DELETE cascade;



ALTER TABLE finanzas.sg_documentos_presupuesto DROP CONSTRAINT presupuesto_escolar_fk;
ALTER TABLE finanzas.sg_documentos_presupuesto DROP CONSTRAINT archivo_presupuesto_fk;

alter table finanzas.sg_documentos_presupuesto add constraint presupuesto_escolar_fk foreign key (doc_presupuesto_fk) references finanzas.sg_presupuesto_escolar(pes_pk) match full on delete cascade on update cascade;
alter table finanzas.sg_documentos_presupuesto add constraint archivo_presupuesto_fk foreign key (doc_archivo_fk) references public.sg_archivos(ach_pk) match full on delete cascade on update cascade;

ALTER TABLE finanzas.sg_movimiento_cuenta_bancaria DROP CONSTRAINT sg_movimiento_cuentas_bancarias_fk;
alter table finanzas.sg_movimiento_cuenta_bancaria add constraint sg_movimiento_cuentas_bancarias_fk FOREIGN KEY (mcb_cuenta_fk) REFERENCES finanzas.sg_cuentas_bancarias(cbc_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE


-- ELIMINAR EL NOT NUL DE LA COLUMNA MONTO SOLICITADO
ALTER TABLE finanzas.sg_transferencia_direccion_departamental ALTER COLUMN tdd_monto_solicitado DROP NOT NULL;


-- SETEANDO VALOR 0 A COLUMNAS DE MESES QUE SE ENCONTRABAN VACIAS

UPDATE finanzas.sg_presupuesto_escolar_movimiento 
SET    mov_enero = 0 
WHERE  mov_pk IN (SELECT mov_pk 
                 FROM   finanzas.sg_presupuesto_escolar_movimiento 
                 WHERE  mov_enero IS NULL); 

UPDATE finanzas.sg_presupuesto_escolar_movimiento 
SET    mov_febrero = 0 
WHERE  mov_pk IN (SELECT mov_pk 
                 FROM   finanzas.sg_presupuesto_escolar_movimiento 
                 WHERE  mov_febrero IS NULL); 

UPDATE finanzas.sg_presupuesto_escolar_movimiento 
SET    mov_marzo = 0 
WHERE  mov_pk IN (SELECT mov_pk 
                 FROM   finanzas.sg_presupuesto_escolar_movimiento 
                 WHERE  mov_marzo IS NULL); 

UPDATE finanzas.sg_presupuesto_escolar_movimiento 
SET    mov_abril = 0 
WHERE  mov_pk IN (SELECT mov_pk 
                 FROM   finanzas.sg_presupuesto_escolar_movimiento 
                 WHERE  mov_abril IS NULL); 

UPDATE finanzas.sg_presupuesto_escolar_movimiento 
SET    mov_mayo = 0 
WHERE  mov_pk IN (SELECT mov_pk 
                 FROM   finanzas.sg_presupuesto_escolar_movimiento 
                 WHERE  mov_mayo IS NULL); 

UPDATE finanzas.sg_presupuesto_escolar_movimiento 
SET    mov_junio = 0 
WHERE  mov_pk IN (SELECT mov_pk 
                 FROM   finanzas.sg_presupuesto_escolar_movimiento 
                 WHERE  mov_junio IS NULL); 

UPDATE finanzas.sg_presupuesto_escolar_movimiento 
SET    mov_julio = 0 
WHERE  mov_pk IN (SELECT mov_pk 
                 FROM   finanzas.sg_presupuesto_escolar_movimiento 
                 WHERE  mov_julio IS NULL); 

UPDATE finanzas.sg_presupuesto_escolar_movimiento 
SET    mov_agosto = 0 
WHERE  mov_pk IN (SELECT mov_pk 
                 FROM   finanzas.sg_presupuesto_escolar_movimiento 
                 WHERE  mov_agosto IS NULL); 

UPDATE finanzas.sg_presupuesto_escolar_movimiento 
SET    mov_septiembre = 0 
WHERE  mov_pk IN (SELECT mov_pk 
                 FROM   finanzas.sg_presupuesto_escolar_movimiento 
                 WHERE  mov_septiembre IS NULL); 

UPDATE finanzas.sg_presupuesto_escolar_movimiento 
SET    mov_octubre = 0 
WHERE  mov_pk IN (SELECT mov_pk 
                 FROM   finanzas.sg_presupuesto_escolar_movimiento 
                 WHERE  mov_octubre IS NULL); 

UPDATE finanzas.sg_presupuesto_escolar_movimiento 
SET    mov_noviembre = 0 
WHERE  mov_pk IN (SELECT mov_pk 
                 FROM   finanzas.sg_presupuesto_escolar_movimiento 
                 WHERE  mov_noviembre IS NULL); 

UPDATE finanzas.sg_presupuesto_escolar_movimiento 
SET    mov_diciembre = 0 
WHERE  mov_pk IN (SELECT mov_pk 
                 FROM   finanzas.sg_presupuesto_escolar_movimiento 
                 WHERE  mov_diciembre IS NULL); 

UPDATE finanzas.sg_presupuesto_escolar_movimiento 
SET    mov_total_anual = 0 
WHERE  mov_pk IN (SELECT mov_pk 
                 FROM   finanzas.sg_presupuesto_escolar_movimiento 
                 WHERE  mov_total_anual IS NULL); 


-- OPERACION AÑOS FISCALES
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version)
VALUES ('BUSCAR_ANIO_FISCAL','FB18',  'Buscar Años Fiscales', 12, true, null, null,0);

--CREANDO COLUMNA PARA AÑOS FISCALES
ALTER TABLE finanzas.sg_presupuesto_escolar ADD COLUMN pes_anio_fiscal_fk bigint;
ALTER TABLE finanzas.sg_presupuesto_escolar_aud ADD COLUMN pes_anio_fiscal_fk bigint;
ALTER TABLE finanzas.sg_presupuesto_escolar ADD CONSTRAINT sg_pes_anio_fiscal_fk FOREIGN KEY (pes_anio_fiscal_fk) 
REFERENCES siap2.ss_anio_fiscal(ani_id) MATCH FULL ON DELETE CASCADE ON UPDATE CASCADE;

-- BORRANDO COLUMNA DE AÑO LECTIVO
ALTER TABLE finanzas.sg_presupuesto_escolar DROP COLUMN pes_anio_lectivo_fk;

--- 1.1.4

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('GENERAR_PLANTILLA_CONVENIO_CDE','R20',  'Permiso para generar el convenio de CDE', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('GENERAR_PLANTILLA_CARTA_CONGELA_FONDOS_CDE','R21',  'Permiso para generar la carta para congelar fondos de CDE', 12, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('GENERAR_PLANTILLA_CONVENIO_CECE','R22',  'Permiso para generar la carta de CECE', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('GENERAR_PLANTILLA_CARTA_CONGELA_FONDOS_CECE','R23',  'Permiso para generar la carta para congelar fondos de CECE', 12, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('GENERAR_PLANTILLA_CONVENIO_CIE','R24',  'Permiso para generar el convenio de CIE', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('GENERAR_PLANTILLA_CARTA_CONGELA_FONDOS_CIE','R25',  'Permiso para generar la carta para congelar fondos de CIE', 12, true, null, null,0);


ALTER TABLE finanzas.sg_documentos_presupuesto DROP CONSTRAINT presupuesto_escolar_uk;

-- 1.1.5
--Se agrega el campo saldo a los movimientos de cuenta
ALTER TABLE finanzas.sg_movimiento_cuenta_bancaria ADD mcb_saldo numeric;
COMMENT ON COLUMN finanzas.sg_movimiento_cuenta_bancaria.mcb_saldo IS 'Campo para el cálculo del saldo final';

ALTER TABLE finanzas.sg_movimiento_cuenta_bancaria_aud ADD mcb_saldo numeric;

--Se elimina el campo de referencia a la tabla intermedia
alter table finanzas.sg_cuentas_bancarias drop constraint if exists sg_cuentas_bancarias_tipo_cuenta_fkey; 
alter table finanzas.sg_cuentas_bancarias drop column if exists cbc_rel_tcb_fk;
alter table finanzas.sg_cuentas_bancarias_aud drop column if exists cbc_rel_tcb_fk;

-- 1.1.8
-- SE CRAN OPERACIONES PARA CAMBIOS DE ESTADO

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CAMBIAR_PRESUPUESTO_ESTADO_FORMULADO','FP1',  'Permite cambiar Estado Aprobado', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CAMBIAR_PRESUPUESTO_ESTADO_EN_AJUSTE','FP2',  'Permite cambiar Estado En Ajueste', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CAMBIAR_PRESUPUESTO_ESTADO_APROBADO','FP3',  'Permite cambiar Estado en Aprobado', 12, true, null, null,0);
-- Movimiento para direcciones departamentales
create sequence if not exists finanzas.sg_movimientos_direccion_departamental_mdd_pk_seq increment 1 start 1 minvalue 1 maxvalue 2147483647 cache 1;

create table if not exists finanzas.sg_movimientos_direccion_departamental (
mdd_pk bigint not null default nextval('finanzas.sg_movimientos_direccion_departamental_mdd_pk_seq'::regclass),
mdd_cuenta_fk int8,
mdd_fecha timestamp without time zone,
mdd_detalle character varying(255),
mdd_monto decimal,
mdd_saldo decimal,
mdd_tipo character varying(255), 
mdd_ult_mod_fecha timestamp without time zone,
mdd_ult_mod_usuario character varying(45),
mdd_version integer,
constraint sg_movimientos_direccion_departamental_pkey primary key (mdd_pk),
constraint sg_movimientos_direccion_departamental_fk FOREIGN KEY (mdd_cuenta_fk) REFERENCES finanzas.sg_cuenta_bancaria_dd(cbd_pk)
);


COMMENT ON TABLE finanzas.sg_movimientos_direccion_departamental IS 'Tabla para el registro de movimiento cuenta bancaria.';
COMMENT ON COLUMN finanzas.sg_movimientos_direccion_departamental.mdd_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN finanzas.sg_movimientos_direccion_departamental.mdd_cuenta_fk IS 'Cuenta bancaria a la que se le hará el movimiento.';
COMMENT ON COLUMN finanzas.sg_movimientos_direccion_departamental.mdd_fecha IS 'Fecha del movimiento.';
COMMENT ON COLUMN finanzas.sg_movimientos_direccion_departamental.mdd_monto IS 'Monto del movimiento';
COMMENT ON COLUMN finanzas.sg_movimientos_direccion_departamental.mdd_saldo IS 'Saldo del movimiento';
COMMENT ON COLUMN finanzas.sg_movimientos_direccion_departamental.mdd_tipo IS 'Tipo de movimiento';
COMMENT ON COLUMN finanzas.sg_movimientos_direccion_departamental.mdd_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN finanzas.sg_movimientos_direccion_departamental.mdd_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN finanzas.sg_movimientos_direccion_departamental.mdd_version IS 'Versión del registro.';

create table if not exists finanzas.sg_movimientos_direccion_departamental_aud (
mdd_pk bigint not null,
mdd_cuenta_fk int8,
mdd_fecha timestamp without time zone,
mdd_detalle character varying(255),
mdd_monto decimal,
mdd_saldo decimal,
mdd_tipo character varying(255), 
mdd_ult_mod_fecha timestamp without time zone,
mdd_ult_mod_usuario character varying(45),
mdd_version integer,
rev bigint,
revtype smallint);

ALTER TABLE finanzas.sg_movimientos_direccion_departamental_aud ADD PRIMARY KEY (mdd_pk, rev);


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_MOVIMIENTO_DIRECCION_DEPARTAMENTAL','FB23',  'Buscar movimientos de direcciones departamentales', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_MOVIMIENTO_DIRECCION_DEPARTAMENTAL','F46',  '', 12, true, null, null,0);

--Se agrega el campo saldo a los movimientos de caja chica
ALTER TABLE finanzas.sg_movimiento_caja_chica ADD mcc_saldo numeric;
COMMENT ON COLUMN finanzas.sg_movimiento_caja_chica.mcc_saldo IS 'Campo para el cálculo del saldo final';

ALTER TABLE finanzas.sg_movimiento_caja_chica_aud ADD mcc_saldo numeric;

-- 1.1.9

ALTER TABLE finanzas.sg_solicitudes_transferencia ADD COLUMN str_transf_direccion_dep_fk bigint;

ALTER TABLE finanzas.sg_solicitudes_transferencia_aud ADD COLUMN str_transf_direccion_dep_fk bigint;

ALTER TABLE finanzas.sg_solicitudes_transferencia ADD CONSTRAINT str_transf_direccion_dep_fkey FOREIGN KEY (str_transf_direccion_dep_fk) 
REFERENCES finanzas.sg_transferencia_direccion_departamental(tdd_pk) MATCH FULL ON DELETE CASCADE ON UPDATE CASCADE;

COMMENT ON COLUMN finanzas.sg_solicitudes_transferencia.str_transf_direccion_dep_fk IS 'Número de referencia a registro de transferencia dirección departamental';

-- Requerimientos de Fondos a Centros Educativos
create sequence if not exists finanzas.sg_req_fond_ced_rfc_pk_seq increment 1 start 1 minvalue 1 maxvalue 2147483647 cache 1;

create table
	if not exists finanzas.sg_req_fond_ced (rfc_pk bigint not null default nextval('finanzas.sg_req_fond_ced_rfc_pk_seq'::regclass),
	rfc_sol_transferencia_fk bigint not null,
	rfc_transferencia_ced_fk bigint not null,
	rfc_monto decimal(10,2) not null,
	rfc_habilitado boolean,
	rfc_ult_mod_fecha timestamp without time zone,
	rfc_ult_mod_usuario character varying(45),
	rfc_version integer,
	constraint sg_req_fond_ced_pkey primary key (rfc_pk));

comment on
table
	finanzas.sg_req_fond_ced is 'Tabla para el registro de requerimientos de fondos a centros educativos.';

comment on
column finanzas.sg_req_fond_ced.rfc_pk is 'Número correlativo autogenerado.';

comment on
column finanzas.sg_req_fond_ced.rfc_sol_transferencia_fk is 'Número de referencia de la solicitud de transferencia del registro.';

comment on
column finanzas.sg_req_fond_ced.rfc_transferencia_ced_fk is 'Número de referencia de la transferencia de centro educativo del registro';

comment on
column finanzas.sg_req_fond_ced.rfc_monto is 'Cantidad del monto del requerimiento de fondo.';

comment on
column finanzas.sg_req_fond_ced.rfc_habilitado is 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';

comment on
column finanzas.sg_req_fond_ced.rfc_ult_mod_fecha is 'Última fecha en la que se modificó el registro.';

comment on
column finanzas.sg_req_fond_ced.rfc_ult_mod_usuario is 'Último usuario que modificó el registro.';

comment on
column finanzas.sg_req_fond_ced.rfc_version is 'Versión del registro.';

create table
	if not exists finanzas.sg_req_fond_ced_aud (rfc_pk bigint not null,
	rfc_sol_transferencia_fk bigint,
	rfc_transferencia_ced_fk bigint,
	rfc_monto decimal(10,2),
	rfc_habilitado boolean,
	rfc_ult_mod_fecha timestamp without time zone,
	rfc_ult_mod_usuario character varying(45),
	rfc_version integer,
	rev bigint,
	revtype smallint);

alter table
	finanzas.sg_req_fond_ced_aud add primary key (rfc_pk,
	rev);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_REQUERIMIENTO_FONDO_A_CED','FB22',  'Buscar Requerimiento de Fondo a Centros Educativos', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_REQUERIMIENTO_FONDO_A_CED','F43',  '', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_REQUERIMIENTO_FONDO_A_CED','F44',  '', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_REQUERIMIENTO_FONDO_A_CED','F45',  '', 12, true, null, null,0);

ALTER TABLE finanzas.sg_req_fond_ced ADD CONSTRAINT rfc_sol_transferencia_fkey FOREIGN KEY (rfc_sol_transferencia_fk) 
REFERENCES finanzas.sg_solicitudes_transferencia(str_pk) MATCH FULL ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE finanzas.sg_req_fond_ced ADD CONSTRAINT rfc_transferencia_ced_fkey FOREIGN KEY (rfc_transferencia_ced_fk) 
REFERENCES finanzas.sg_transferencias_a_ced(tac_pk) MATCH FULL ON DELETE CASCADE ON UPDATE CASCADE;

-- 1.2.2

ALTER TABLE finanzas.sg_solicitudes_transferencia ADD COLUMN str_cuenta_banc_dd_fk bigint;

ALTER TABLE finanzas.sg_solicitudes_transferencia_aud ADD COLUMN str_cuenta_banc_dd_fk bigint;

ALTER TABLE finanzas.sg_solicitudes_transferencia ADD CONSTRAINT str_cuenta_banc_dd_fkey FOREIGN KEY (str_cuenta_banc_dd_fk) 
REFERENCES finanzas.sg_cuenta_bancaria_dd(cbd_pk) MATCH FULL ON DELETE CASCADE ON UPDATE CASCADE;

COMMENT ON COLUMN finanzas.sg_solicitudes_transferencia.str_cuenta_banc_dd_fk IS 'Número de referencia a cuenta bancaria de dirección departamental del registro';

ALTER TABLE finanzas.sg_transferencias_a_ced_aud ALTER COLUMN tac_monto_solicitado DROP NOT NULL;
UPDATE finanzas.sg_presupuesto_escolar_movimiento 
SET    mov_abril = 0 
WHERE  mov_pk IN (SELECT mov_pk 
                 FROM   finanzas.sg_presupuesto_escolar_movimiento 
                 WHERE  mov_abril IS NULL); 

UPDATE finanzas.sg_presupuesto_escolar_movimiento 
SET    mov_mayo = 0 
WHERE  mov_pk IN (SELECT mov_pk 
                 FROM   finanzas.sg_presupuesto_escolar_movimiento 
                 WHERE  mov_mayo IS NULL); 

UPDATE finanzas.sg_presupuesto_escolar_movimiento 
SET    mov_junio = 0 
WHERE  mov_pk IN (SELECT mov_pk 
                 FROM   finanzas.sg_presupuesto_escolar_movimiento 
                 WHERE  mov_junio IS NULL); 

UPDATE finanzas.sg_presupuesto_escolar_movimiento 
SET    mov_julio = 0 
WHERE  mov_pk IN (SELECT mov_pk 
                 FROM   finanzas.sg_presupuesto_escolar_movimiento 
                 WHERE  mov_julio IS NULL); 

UPDATE finanzas.sg_presupuesto_escolar_movimiento 
SET    mov_agosto = 0 
WHERE  mov_pk IN (SELECT mov_pk 
                 FROM   finanzas.sg_presupuesto_escolar_movimiento 
                 WHERE  mov_agosto IS NULL); 

UPDATE finanzas.sg_presupuesto_escolar_movimiento 
SET    mov_septiembre = 0 
WHERE  mov_pk IN (SELECT mov_pk 
                 FROM   finanzas.sg_presupuesto_escolar_movimiento 
                 WHERE  mov_septiembre IS NULL); 

UPDATE finanzas.sg_presupuesto_escolar_movimiento 
SET    mov_octubre = 0 
WHERE  mov_pk IN (SELECT mov_pk 
                 FROM   finanzas.sg_presupuesto_escolar_movimiento 
                 WHERE  mov_octubre IS NULL); 

UPDATE finanzas.sg_presupuesto_escolar_movimiento 
SET    mov_noviembre = 0 
WHERE  mov_pk IN (SELECT mov_pk 
                 FROM   finanzas.sg_presupuesto_escolar_movimiento 
                 WHERE  mov_noviembre IS NULL); 

UPDATE finanzas.sg_presupuesto_escolar_movimiento 
SET    mov_diciembre = 0 
WHERE  mov_pk IN (SELECT mov_pk 
                 FROM   finanzas.sg_presupuesto_escolar_movimiento 
                 WHERE  mov_diciembre IS NULL); 

UPDATE finanzas.sg_presupuesto_escolar_movimiento 
SET    mov_total_anual = 0 
WHERE  mov_pk IN (SELECT mov_pk 
                 FROM   finanzas.sg_presupuesto_escolar_movimiento 
                 WHERE  mov_total_anual IS NULL); 


--CREACION DE TABLA PARA LA GESTIÒN DE LOS PLANES DE COMPRA DEL PRESUPUESTO ESCOLAR
CREATE SEQUENCE IF NOT EXISTS finanzas.sg_plan_compra_pk_seq 
    INCREMENT 1 
    START 1 
    MINVALUE 1 
    MAXVALUE 9223372036854775807 
    CACHE 1     
    NO CYCLE;

CREATE TABLE IF NOT EXISTS finanzas.sg_plan_compra (
    com_pk bigint NOT NULL DEFAULT nextval('finanzas.sg_plan_compra_pk_seq'::regclass),   
    com_movimiento_fk integer, 
    com_insumo_fk integer,
    com_cantidad decimal,
    com_unidad_medida character varying(255),
    com_precio_unitario decimal,
    com_monto_total decimal,
    com_fecha_estimada_compra timestamp without time zone,
    com_ult_mod_fecha timestamp without time zone, 
    com_ult_mod_usuario character varying(45), 
    com_version integer, 
    
    CONSTRAINT sg_plan_compra_pkey PRIMARY KEY (com_pk) 
    
);
COMMENT ON TABLE finanzas.sg_plan_compra IS 'Tabla para el registro de Plan de Compras';
COMMENT ON COLUMN finanzas.sg_plan_compra.com_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN finanzas.sg_plan_compra.com_movimiento_fk IS 'Código del registro.';
COMMENT ON COLUMN finanzas.sg_plan_compra.com_insumo_fk IS 'Indica la llave del insumo a la que pertenece el Plan de Compra';
COMMENT ON COLUMN finanzas.sg_plan_compra.com_cantidad IS 'Indica la cantidad del registro';
COMMENT ON COLUMN finanzas.sg_plan_compra.com_unidad_medida IS 'Indica la unidad de medida del registro';
COMMENT ON COLUMN finanzas.sg_plan_compra.com_precio_unitario IS 'Muestra el valor en $ por el cual el se realizo el registro';
COMMENT ON COLUMN finanzas.sg_plan_compra.com_monto_total IS 'Muestra el valor total en $ por el cual el se realizo el registro en relación a la cantidad';
COMMENT ON COLUMN finanzas.sg_plan_compra.com_fecha_estimada_compra IS 'Fecha en la que se estima adquirir el registro.';
COMMENT ON COLUMN finanzas.sg_plan_compra.com_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN finanzas.sg_plan_compra.com_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN finanzas.sg_plan_compra.com_version IS 'Versión del registro.';

-- AGREGANDO RELACION DE PLAN CON MOVIMIENTOS
ALTER TABLE finanzas.sg_plan_compra ADD CONSTRAINT sg_com_movimiento_fk FOREIGN KEY (com_movimiento_fk) 
REFERENCES finanzas.sg_presupuesto_escolar_movimiento(mov_pk) MATCH FULL ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE finanzas.sg_plan_compra ADD CONSTRAINT com_insumo_fk FOREIGN KEY (com_insumo_fk) 
REFERENCES siap2.ss_insumo (ins_id) MATCH FULL ON DELETE CASCADE ON UPDATE CASCADE;


--- TABLA DE AUDITORIA
CREATE TABLE IF NOT EXISTS finanzas.sg_plan_compra_aud (
    com_pk bigint NOT NULL DEFAULT nextval('finanzas.sg_plan_compra'::regclass), 
    com_movimiento_fk integer, 
    com_insumo_fk integer,
    com_cantidad decimal,
    com_unidad_medida character varying(255),
    com_precio_unitario decimal,
    com_monto_total decimal,
    com_fecha_estimada_compra timestamp without time zone,
    com_ult_mod_fecha timestamp without time zone, 
    com_ult_mod_usuario character varying(45), 
    com_version integer, 
    rev bigint, 
    revtype smallint, 
    CONSTRAINT sg_plan_compra_aud_pkey PRIMARY KEY (com_pk,rev), 
    CONSTRAINT sg_plan_compra_aud_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev)
);


ALTER TABLE finanzas.sg_plan_compra ADD com_codigo varchar(45) NULL;
ALTER TABLE finanzas.sg_plan_compra_aud ADD com_codigo varchar(45) NULL;

--SE CREA OPERACION PARA BUSQUEDA METODO ADQ
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) 
VALUES ('BUSCAR_METODO_ADQ','FB20',  'Busca Método ADQ', 12, true, null, null,0);

-- CAMBIANDO OPERACION 
UPDATE seguridad.sg_operaciones
SET ope_codigo='FB21'
WHERE ope_nombre='BUSCAR_PLAN_DE_COMPRAS' AND ope_categoria_operacion_fk=12;


--CREANDO COLUNMNA Y RELACION PLAN COMPRA CON METODO DE ADQ
ALTER TABLE finanzas.sg_plan_compra ADD COLUMN com_metodo_fk bigint;
ALTER TABLE finanzas.sg_plan_compra_aud ADD COLUMN com_metodo_fk bigint;
ALTER TABLE finanzas.sg_plan_compra ADD CONSTRAINT sg_com_metodo_fk FOREIGN KEY (com_metodo_fk) 
REFERENCES siap2.ss_metodo_adq(met_id) MATCH FULL ON DELETE CASCADE ON UPDATE CASCADE;


-- 1.2.5
-- ELIMINANDO TABLA DE COMPRAS

DROP TABLE finanzas.sg_plan_compra CASCADE;
DROP TABLE finanzas.sg_plan_compra_aud CASCADE;



--CREACION DE TABLA PARA LA GESTIÒN DE LOS PLANES DE COMPRA DEL PRESUPUESTO ESCOLAR
CREATE SEQUENCE IF NOT EXISTS finanzas.sg_plan_compra_com_pk_seq 
    INCREMENT 1 
    START 1 
    MINVALUE 1 
    MAXVALUE 9223372036854775807 
    CACHE 1     
    NO CYCLE;

CREATE TABLE IF NOT EXISTS finanzas.sg_plan_compra (
    com_pk bigint NOT NULL DEFAULT nextval('finanzas.sg_plan_compra_com_pk_seq'::regclass), 
    com_movimiento_fk integer,
    com_codigo character varying(45),
    com_insumo_fk integer,
    com_cantidad decimal,
    com_unidad_medida character varying(45),
    com_precio_unitario decimal,
    com_monto_total decimal,
    com_fecha_estimada_compra timestamp without time zone,
    com_metodo_fk integer,
    com_presupuesto_fk integer,
    com_ult_mod_fecha timestamp without time zone, 
    com_ult_mod_usuario character varying(45), 
    com_version integer, 
    
    CONSTRAINT sg_plan_compra_pkey PRIMARY KEY (com_pk) 
    
);
COMMENT ON TABLE finanzas.sg_plan_compra IS 'Tabla para el registro de Plan de Compras';
COMMENT ON COLUMN finanzas.sg_plan_compra.com_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN finanzas.sg_plan_compra.com_movimiento_fk IS 'Código del movimiento al que se relaciona el registro.';
COMMENT ON COLUMN finanzas.sg_plan_compra.com_codigo IS 'Código del registro.';
COMMENT ON COLUMN finanzas.sg_plan_compra.com_insumo_fk IS 'Indica la llave del insumo a la que pertenece el Plan de Compra';
COMMENT ON COLUMN finanzas.sg_plan_compra.com_cantidad IS 'Indica la cantidad del registro';
COMMENT ON COLUMN finanzas.sg_plan_compra.com_unidad_medida IS 'Indica la unidad de medida del registro';
COMMENT ON COLUMN finanzas.sg_plan_compra.com_precio_unitario IS 'Muestra el valor en $ por el cual el se realizo el registro';
COMMENT ON COLUMN finanzas.sg_plan_compra.com_monto_total IS 'Muestra el valor total en $ por el cual el se realizo el registro en relación a la cantidad';
COMMENT ON COLUMN finanzas.sg_plan_compra.com_fecha_estimada_compra IS 'Fecha en la que se estima adquirir el registro.';
COMMENT ON COLUMN finanzas.sg_plan_compra.com_metodo_fk IS 'Indica el método que relaciona el registro.';
COMMENT ON COLUMN finanzas.sg_plan_compra.com_presupuesto_fk IS 'Indica el presupuesto al que pertence el registro';
COMMENT ON COLUMN finanzas.sg_plan_compra.com_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN finanzas.sg_plan_compra.com_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN finanzas.sg_plan_compra.com_version IS 'Versión del registro.';

-- AGREGANDO RELACION DE PLAN CON MOVIMIENTOS
ALTER TABLE finanzas.sg_plan_compra ADD CONSTRAINT sg_com_movimiento_fk FOREIGN KEY (com_movimiento_fk) 
REFERENCES finanzas.sg_presupuesto_escolar_movimiento(mov_pk) MATCH FULL ON DELETE CASCADE ON UPDATE CASCADE;
-- AGREGANDO RELACION DE PLAN CON INSUMOS
ALTER TABLE finanzas.sg_plan_compra ADD CONSTRAINT com_insumo_fk FOREIGN KEY (com_insumo_fk) 
REFERENCES siap2.ss_insumo (ins_id) MATCH FULL ON DELETE CASCADE ON UPDATE CASCADE;
-- AGREGANDO RELACION DE PLAN PRESUPUESTOS ESCOLARES
ALTER TABLE finanzas.sg_plan_compra ADD CONSTRAINT sg_com_presupuesto_fk FOREIGN KEY (com_presupuesto_fk) 
REFERENCES finanzas.sg_presupuesto_escolar(pes_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

--- TABLA DE AUDITORIA
CREATE TABLE IF NOT EXISTS finanzas.sg_plan_compra_aud (
    com_pk bigint NOT NULL DEFAULT nextval('finanzas.sg_plan_compra'::regclass), 
    com_movimiento_fk integer, 
    com_codigo character varying(45),
    com_insumo_fk integer,
    com_cantidad decimal,
    com_unidad_medida character varying(255),
    com_precio_unitario decimal,
    com_monto_total decimal,
    com_fecha_estimada_compra timestamp without time zone,
    com_metodo_fk integer,
    com_presupuesto_fk integer,    
    com_ult_mod_fecha timestamp without time zone, 
    com_ult_mod_usuario character varying(45), 
    com_version integer, 
    rev bigint, 
    revtype smallint, 
    CONSTRAINT sg_plan_compra_aud_pkey PRIMARY KEY (com_pk,rev), 
    CONSTRAINT sg_plan_compra_aud_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev)
);


--1.2.6
--Comentarios de tablas y columnas faltantes
COMMENT ON COLUMN finanzas.sg_movimiento_caja_chica.mcc_detalle IS 'Campo de texto libre para agregar algún comentario sobre el movimiento.';
COMMENT ON COLUMN finanzas.sg_movimiento_cuenta_bancaria.mcb_detalle IS 'Campo de texto libre para agregar algún comentario sobre el movimiento.';
COMMENT ON COLUMN finanzas.sg_movimientos_direccion_departamental.mdd_detalle IS 'Campo de texto libre para agregar algún comentario sobre el movimiento.';
COMMENT ON TABLE finanzas.sg_cuentas_bancarias_cc IS 'Tabla para el registro de caja chica.';
COMMENT ON TABLE finanzas.sg_movimientos_direccion_departamental IS 'Tabla para el registro de movimiento cuenta bancaria de dirección departamental.';

--TABLAS DE AUDITORÍA
--sg_cuenta_bancaria_dd_aud
COMMENT ON TABLE finanzas.sg_cuenta_bancaria_dd_aud IS 'Tabla de auditoría de la tabla finanzas.sg_cuenta_bancaria_dd, el detalle de los campos se encuentra en la tabla principal.';

--sg_cuentas_bancarias_aud
COMMENT ON TABLE finanzas.sg_cuentas_bancarias_aud IS 'Tabla de auditoría de la tabla finanzas.sg_cuentas_bancarias, el detalle de los campos se encuentra en la tabla principal.';

--sg_cuentas_bancarias_cc_aud
COMMENT ON TABLE finanzas.sg_cuentas_bancarias_cc_aud IS 'Tabla de auditoría de la tabla finanzas.sg_cuentas_bancarias_cc, el detalle de los campos se encuentra en la tabla principal.';

--sg_movimiento_caja_chica_aud
COMMENT ON TABLE finanzas.sg_movimiento_caja_chica_aud IS 'Tabla de auditoría de la tabla finanzas.sg_movimiento_caja_chica, el detalle de los campos se encuentra en la tabla principal.';

--sg_movimiento_cuenta_bancaria_aud
COMMENT ON TABLE finanzas.sg_movimiento_cuenta_bancaria_aud IS 'Tabla de auditoría de la tabla finanzas.sg_movimiento_cuenta_bancaria, el detalle de los campos se encuentra en la tabla principal.';

--sg_movimientos_direccion_departamental_aud
COMMENT ON TABLE finanzas.sg_movimientos_direccion_departamental_aud IS 'Tabla de auditoría de la tabla finanzas.sg_movimientos_direccion_departamental, el detalle de los campos se encuentra en la tabla principal.';

--sg_recibos_aud
COMMENT ON TABLE finanzas.sg_recibos_aud IS 'Tabla de auditoría de la tabla finanzas.sg_recibos, el detalle de los campos se encuentra en la tabla principal.';

--sg_rel_cbc_tcb_aud
COMMENT ON TABLE finanzas.sg_rel_cbc_tcb_aud IS 'Tabla de auditoría de la tabla finanzas.sg_rel_cbc_tcb, el detalle de los campos se encuentra en la tabla principal.';


COMMENT ON TABLE finanzas.sg_plan_compra_aud IS 'Tabla de autoría de la tabla de finanazas.sg_plan_compra_aud, el detalle de los campos se encuentra en la tabla principal.';



-- 1.2.7
COMMENT ON TABLE finanzas.sg_desembolsos IS 'Tabla para el registros de desembolsos';
COMMENT ON COLUMN finanzas.sg_desembolsos.dsb_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN finanzas.sg_desembolsos.dsb_ult_mod IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN finanzas.sg_desembolsos.dsb_ult_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN finanzas.sg_desembolsos.dsb_version IS 'Versión del registro.';
COMMENT ON COLUMN finanzas.sg_desembolsos.dsb_porcentaje IS 'Indica el porcentaje del registro.';
COMMENT ON COLUMN finanzas.sg_desembolsos.dsb_monto IS 'Indica el monto del registro.';
COMMENT ON COLUMN finanzas.sg_desembolsos.dsb_estado IS 'Indica el estado del registro.';
COMMENT ON TABLE finanzas.sg_desembolsos_aud IS 'Tabla de auditoría de la tabla finanzas.sg_desembolsos, el detalle de los campos se encuentra en la tabla principal.';
COMMENT ON COLUMN finanzas.sg_direcciones_departamentales.ded_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN finanzas.sg_direcciones_departamentales.ded_departamento_fk IS 'Número de referencia a la llave primaria de departamento.';
COMMENT ON TABLE finanzas.sg_direcciones_departamentales_aud IS 'Tabla de auditoría de la tabla finanzas.sg_direcciones_departamentales, el detalle de los campos se encuentra en la tabla principal.';
COMMENT ON TABLE finanzas.sg_documentos_presupuesto_aud IS 'Tabla de auditoría de la tabla finanzas.sg_documentos_presupuesto, el detalle de los campos se encuentra en la tabla principal';
COMMENT ON TABLE finanzas.sg_documentos_presupuesto IS 'Tabla para el registros de documentos del presupuesto';
COMMENT ON COLUMN finanzas.sg_documentos_presupuesto.doc_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN finanzas.sg_documentos_presupuesto.doc_presupuesto_fk IS 'Número de referencia a llave primaria de un presupuesto.';
COMMENT ON COLUMN finanzas.sg_documentos_presupuesto.doc_archivo_fk IS 'Número de referencia a la llave primaria de un archivo.';
COMMENT ON COLUMN finanzas.sg_documentos_presupuesto.doc_tipo_documento IS 'Indica el tipo de documento del registro.';
COMMENT ON COLUMN finanzas.sg_documentos_presupuesto.doc_estado_documento IS 'Indica el estado del registro.';
COMMENT ON COLUMN finanzas.sg_documentos_presupuesto.doc_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN finanzas.sg_documentos_presupuesto.doc_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN finanzas.sg_documentos_presupuesto.doc_version IS 'Versión del registro.';
COMMENT ON TABLE finanzas.sg_req_fond_ced_aud IS 'Tabla de auditoría de la tabla finanzas.sg_req_fond_ced, el detalle de los campos se encuentra en la tabla principal';
COMMENT ON TABLE finanzas.sg_solicitudes_transferencia_aud IS 'Tabla de auditoría de la tabla finanzas.sg_solicitudes_transferencia,  el detalle de los campos se encuentra en la tabla principal';
COMMENT ON TABLE finanzas.sg_transferencia_direccion_departamental_aud IS 'Tabla de auditoría de la tabla finanzas.sg_transferencia_direccion_departamental,  el detalle de los campos se encuentra en la tabla principal';
COMMENT ON TABLE finanzas.sg_transferencias_a_ced_aud IS 'Tabla de auditoría de la tabla finanzas.sg_transferencias_a_ced,  el detalle de los campos se encuentra en la tabla principal';
COMMENT ON TABLE finanzas.sg_presupuesto_escolar_aud IS 'Tabla de auditoría de la tabla finanzas.sg_presupuesto_escolar,  el detalle de los campos se encuentra en la tabla principal';
COMMENT ON TABLE finanzas.sg_presupuesto_escolar_movimiento_aud IS 'Tabla de auditoría de la tabla finanzas.sg_presupuesto_escolar_movimiento,  el detalle de los campos se encuentra en la tabla principal';
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar.pes_anio_fiscal_fk IS 'Número de referencia a la llave primaria de un anio fiscal del registro.';

COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_movimiento.mov_origen IS 'Indica el Origen del registro.';
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_movimiento.mov_actividad_pk IS 'Indica la llave de la actividad  del registro.';
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_movimiento.mov_num_movimiento IS 'Indica el número correlativo del registro.';
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_movimiento.mov_fuente_ingreso_pk IS 'Indica la llave de la fuente de Ingreso del registro.';
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_movimiento.mov_rubro_pk IS 'Número de referencia a la tabla Rubros.';
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_movimiento.mov_techo_presupuestal IS 'Número de referencia al tope presupuestal de siap2.';

ALTER TABLE finanzas.sg_req_fond_ced ALTER COLUMN rfc_monto DROP NOT NULL;


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('GENERAR_REPORTE_REQUERIMIENTO_TRANSF_FONDOS','R27',  '', 5, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('GENERAR_REPORTE_DETALLE_REQUERIMIENTO_TRANSF_FONDOS','R28',  '', 5, true, null, null,0);


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_TRANSFERENCIAS','FM15',  '', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_TRANSFERENCIAS_COMPONENTES','FB9',  '', 12, true, null, null,0);


-- 1.2.9
-- Pagos

CREATE SEQUENCE IF NOT EXISTS finanzas.sg_pagos_pgs_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;

create table if not exists finanzas.sg_pagos (
pgs_pk bigint not null default nextval('finanzas.sg_pagos_pgs_pk_seq'::regclass),
pgs_factura_fk bigint not null,
pgs_modo_pago character varying(25) not null,
pgs_movimiento_cb_fk bigint,
pgs_movimiento_cc_fk bigint,
pgs_numero_cheque character varying(50),
pgs_fecha_pago timestamp without time zone,
pgs_importe numeric,
pgs_comentario character varying(255),
pgs_ult_mod_fecha timestamp without time zone,
pgs_ult_mod_usuario character varying(45),
pgs_version integer,
constraint sg_pagos_pkey primary key (pgs_pk),
constraint sg_mov_cuenta_bancaria_fk foreign key (pgs_movimiento_cb_fk) references sg_movimiento_cuenta_bancaria (mcb_pk),
constraint sg_mov_caja_chica_fk foreign key (pgs_movimiento_cc_fk) references sg_movimiento_caja_chica (mcc_pk)
);

COMMENT ON TABLE finanzas.sg_pagos IS 'Tabla para el registro de pagos.';
COMMENT ON COLUMN finanzas.sg_pagos.pgs_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN finanzas.sg_pagos.pgs_factura_fk IS 'Número de factura que se va a pagar.';
COMMENT ON COLUMN finanzas.sg_pagos.pgs_modo_pago IS 'Modo de pago (cheque o efectivo).';
COMMENT ON COLUMN finanzas.sg_pagos.pgs_movimiento_cb_fk IS 'Id de movimiento de cuenta bancaria.';
COMMENT ON COLUMN finanzas.sg_pagos.pgs_movimiento_cc_fk IS 'Id de movimiento de caja chica.';
COMMENT ON COLUMN finanzas.sg_pagos.pgs_numero_cheque IS 'Número de cheque.';
COMMENT ON COLUMN finanzas.sg_pagos.pgs_fecha_pago IS 'Fecha de pago.';
COMMENT ON COLUMN finanzas.sg_pagos.pgs_importe IS 'Importe de la factura.';
COMMENT ON COLUMN finanzas.sg_pagos.pgs_comentario IS 'Comentario (texto libre).';
COMMENT ON COLUMN finanzas.sg_pagos.pgs_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN finanzas.sg_pagos.pgs_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN finanzas.sg_pagos.pgs_version IS 'Versión del registro.';


create table if not exists finanzas.sg_pagos_aud (
pgs_pk bigint not null,
pgs_factura_fk bigint not null,
pgs_modo_pago character varying(25) not null,
pgs_movimiento_cb_fk bigint,
pgs_movimiento_cc_fk bigint,
pgs_numero_cheque character varying(50),
pgs_fecha_pago timestamp,
pgs_importe numeric,
pgs_comentario character varying(255),
pgs_ult_mod_fecha timestamp without time zone,
pgs_ult_mod_usuario character varying(45),
pgs_version integer,
rev bigint,
revtype smallint);
ALTER TABLE finanzas.sg_pagos_aud ADD PRIMARY KEY (pgs_pk, rev);

COMMENT ON TABLE finanzas.sg_pagos IS 'Tabla para el histórico de la tabla finanzas.sg_pagos. Los campos se detallan en la tabla principal.';


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_PAGOS','FB25',  'Buscar Pagos', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_PAGOS','F50',  'Crear pagos', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_PAGOS','F51',  'Actualizar pagos', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_PAGOS','F52',  'Eliminar pagos', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_PAGOS','FM14',  'Menú para la gestión de pagos', 12, true, null, null,0);




--CREACION DE TABLA PARA LA GESTIÒN DE FACTURAS
CREATE SEQUENCE IF NOT EXISTS finanzas.sg_facturas_fac_pk_seq 
    INCREMENT 1 
    START 1 
    MINVALUE 1 
    MAXVALUE 9223372036854775807 
    CACHE 1     
    NO CYCLE;

CREATE TABLE IF NOT EXISTS finanzas.sg_facturas (
    fac_pk bigint NOT NULL DEFAULT nextval('finanzas.sg_facturas_fac_pk_seq'::regclass), 
    fac_numero character varying(45), 
    fac_proveedor_fk integer,
    fac_fecha_factura timestamp without time zone,
    fac_sub_total decimal,
    fac_impuestos decimal, 
    fac_tipo_item character varying(45),
    fac_item_plan_compra integer,
    fac_item_movimiento integer,
    fac_estado character varying(45),
    fac_total decimal, 
    fac_ult_mod_fecha timestamp without time zone, 
    fac_ult_mod_usuario character varying(45), 
    fac_version integer, 
    CONSTRAINT factura_pkey PRIMARY KEY (fac_pk), 
    CONSTRAINT fac_numero_uk UNIQUE (fac_numero));
   
   
COMMENT ON TABLE finanzas.sg_facturas IS 'Tabla para el registro de Facturas.';
COMMENT ON COLUMN finanzas.sg_facturas.fac_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN finanzas.sg_facturas.fac_numero IS 'Factura del registro.';
COMMENT ON COLUMN finanzas.sg_facturas.fac_proveedor_fk IS 'Indica el proveedor del registro.';
COMMENT ON COLUMN finanzas.sg_facturas.fac_fecha_factura IS 'Indica la fecha registro.';
COMMENT ON COLUMN finanzas.sg_facturas.fac_sub_total IS 'Valor en $ del subtotal del registro.';
COMMENT ON COLUMN finanzas.sg_facturas.fac_impuestos IS 'Valor en $ que detalla los impuestos del registro.';
COMMENT ON COLUMN finanzas.sg_facturas.fac_tipo_item IS 'Nombre del ítem al que se relaciona el registro.';
COMMENT ON COLUMN finanzas.sg_facturas.fac_item_plan_compra IS 'Indica el ítem de plan de compra al que se relaciona el registro';
COMMENT ON COLUMN finanzas.sg_facturas.fac_item_movimiento IS 'Indica el ítem de presupuesto al que se relaciona el registro';
COMMENT ON COLUMN finanzas.sg_facturas.fac_estado IS 'Indica el estado del registro.';
COMMENT ON COLUMN finanzas.sg_facturas.fac_total IS 'Valor en $ que detalla los totaes del registro.';
COMMENT ON COLUMN finanzas.sg_facturas.fac_ult_mod_fecha IS 'Última fehca de modificación del registro.';
COMMENT ON COLUMN finanzas.sg_facturas.fac_ult_mod_usuario IS 'Último usuario que modifico el registro.';
COMMENT ON COLUMN finanzas.sg_facturas.fac_version IS 'Versión del registro.';


CREATE TABLE IF NOT EXISTS finanzas.sg_facturas_aud (
    fac_pk bigint not null, 
    fac_numero character varying(45), 
    fac_proveedor_fk integer,
    fac_fecha_factura timestamp without time zone,
    fac_sub_total decimal,
    fac_impuestos decimal, 
    fac_tipo_item character varying(45),
    fac_item_plan_compra integer,
    fac_item_movimiento integer,
    fac_estado character varying(45),
    fac_total decimal, 
    fac_ult_mod_fecha timestamp without time zone, 
    fac_ult_mod_usuario character varying(45), 
    fac_version integer, 
    rev bigint, 
    revtype smallint, 
    CONSTRAINT sg_facturas_aud_pkey PRIMARY KEY (fac_pk,rev), 
    CONSTRAINT sg_facturas_aud_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev)
);

COMMENT ON TABLE finanzas.sg_facturas_aud IS 'Tabla de auditoria para el registro de Facturas.';
COMMENT ON COLUMN finanzas.sg_facturas_aud.fac_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN finanzas.sg_facturas_aud.fac_numero IS 'Factura del registro.';
COMMENT ON COLUMN finanzas.sg_facturas_aud.fac_proveedor_fk IS 'Indica el proveedor del registro.';
COMMENT ON COLUMN finanzas.sg_facturas_aud.fac_fecha_factura IS 'Indica la fecha registro.';
COMMENT ON COLUMN finanzas.sg_facturas_aud.fac_sub_total IS 'Valor en $ del subtotal del registro.';
COMMENT ON COLUMN finanzas.sg_facturas_aud.fac_impuestos IS 'Valor en $ que detalla los impuestos del registro.';
COMMENT ON COLUMN finanzas.sg_facturas_aud.fac_tipo_item IS 'Nombre del ítem al que se relaciona el registro.';
COMMENT ON COLUMN finanzas.sg_facturas_aud.fac_item_plan_compra IS 'Indica el ítem de plan de compra al que se relaciona el registro';
COMMENT ON COLUMN finanzas.sg_facturas_aud.fac_item_movimiento IS 'Indica el ítem de presupuesto al que se relaciona el registro';
COMMENT ON COLUMN finanzas.sg_facturas_aud.fac_estado IS 'Indica el estado del registro.';
COMMENT ON COLUMN finanzas.sg_facturas_aud.fac_total IS 'Valor en $ que detalla los totaes del registro.';
COMMENT ON COLUMN finanzas.sg_facturas_aud.fac_ult_mod_fecha IS 'Última fehca de modificación del registro.';
COMMENT ON COLUMN finanzas.sg_facturas_aud.fac_ult_mod_usuario IS 'Último usuario que modifico el registro.';
COMMENT ON COLUMN finanzas.sg_facturas_aud.fac_version IS 'Versión del registro.';


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_FACTURAS','FB24',  'Buscar facturas', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_FACTURAS','F47',  'Crear Facturas', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_FACTURAS','F48',  'Actualizar Facturas', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_FACTURAS','F49',  'Eliminar Faturas', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_FACTURAS','FM13',  'Menú para la gestion de Facturas', 12, true, null, null,0);

-- SE CREA COLUMNA PARA SEDE EN FACTURAS
ALTER TABLE finanzas.sg_facturas ADD COLUMN fac_sede_fk bigint;
ALTER TABLE finanzas.sg_facturas_aud ADD COLUMN fac_sede_fk bigint;

-- REALCIONES FACTURAS
-- SEDE
ALTER TABLE finanzas.sg_facturas ADD CONSTRAINT sg_facturas_fkey FOREIGN KEY (fac_sede_fk) 
REFERENCES centros_educativos.sg_sedes(sed_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;
-- PROVEEDOR
ALTER TABLE finanzas.sg_facturas ADD CONSTRAINT sg_proveedor_fkey FOREIGN KEY (fac_proveedor_fk) 
REFERENCES siap2.ss_proveedor(pro_id) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;
-- PLAN DE COMPRA
ALTER TABLE finanzas.sg_facturas ADD CONSTRAINT sg_plan_compra_fkey FOREIGN KEY (fac_item_plan_compra) 
REFERENCES finanzas.sg_plan_compra(com_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;
-- MOVIMIENTO
ALTER TABLE finanzas.sg_facturas ADD CONSTRAINT sg_movimiento_fkey FOREIGN KEY (fac_item_movimiento) 
REFERENCES finanzas.sg_presupuesto_escolar_movimiento(mov_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

--Se agrega llave foránea a campo de factura en tabla de pagos
alter table finanzas.sg_pagos add constraint sg_pagos_facturas_fk foreign key (pgs_factura_fk) references finanzas.sg_facturas (fac_pk);

-- 1.3.1
ALTER TABLE finanzas.sg_facturas DROP CONSTRAINT fac_numero_uk;

ALTER TABLE siap2.ss_transferencias_componente ADD CONSTRAINT tc_transferencia_fkey FOREIGN KEY (tc_transferencia) 
REFERENCES siap2.ss_transferencias(tra_id) MATCH FULL ON DELETE CASCADE ON UPDATE CASCADE;

CREATE VIEW finanzas.detalle_req_fondo_view AS
    select rced.*,stra.*,tced.*,fun.*,sede.*  from finanzas.sg_req_fond_ced as rced
inner join finanzas.sg_solicitudes_transferencia stra on rced.rfc_sol_transferencia_fk=stra.str_pk
inner join finanzas.sg_transferencias_a_ced tced on rced.rfc_transferencia_ced_fk=tced.tac_pk
inner join siap2.ss_transferencias_componente tcom on tced.tac_transferencia_fk=tcom.tc_id
inner join siap2.ss_fuente_recursos fue on tcom.tc_fuente_recursos_fk=fue.fue_id
inner join siap2.ss_fuente_financiamiento fun on fue.fue_financiamiento=fun.fue_id
inner join centros_educativos.sg_sedes sede on tced.tac_ced_fk=sede.sed_pk;



-- Seteando valores 0 a montos aprobados diferentes de cero
UPDATE finanzas.sg_presupuesto_escolar_movimiento 
SET    mov_monto_aprobado = 0 
WHERE  mov_pk IN (SELECT mov_pk 
                 FROM   finanzas.sg_presupuesto_escolar_movimiento 
                 WHERE  mov_monto_aprobado IS NULL); 

-- Se coloca por defecto valor 0 en cada nuevo registro.
ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento ALTER COLUMN mov_monto_aprobado SET DEFAULT 0;

-- 1.3.3
-- AGREGANDO NOTA DE CREDITO 
ALTER TABLE finanzas.sg_facturas ADD fac_nota_credito varchar(45) NULL;
COMMENT ON COLUMN finanzas.sg_facturas.fac_nota_credito IS 'Indica la nota de credito asociada a la anulacion';

ALTER TABLE finanzas.sg_facturas_aud ADD fac_nota_credito varchar(45) NULL;
COMMENT ON COLUMN finanzas.sg_facturas_aud.fac_nota_credito IS 'Valor registrado en la tabla principal';


-- AGREGANDO FECHA DE NOTA DE CREDITO.
ALTER TABLE finanzas.sg_facturas add fac_fecha_nota_credito timestamp without time zone;
COMMENT ON COLUMN finanzas.sg_facturas.fac_fecha_nota_credito IS ' Indica la fecha de la nota de credito asociada a la anulacion';

ALTER TABLE finanzas.sg_facturas_aud add fac_fecha_nota_credito timestamp without time zone;
COMMENT ON COLUMN finanzas.sg_facturas_aud.fac_fecha_nota_credito IS 'Indica la fecha de la nota de credito asociada a la anulacion';

-- AGREGANDO OPERACION DE ANULACION FACTURA
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) 
VALUES ('ANULAR_FACTURAS_RECIBOS','F53',  'Anula Facturas y Recibos', 12, true, null, null,0);


-- 1.3.4
ALTER TABLE finanzas.sg_transferencia_direccion_departamental ADD tdd_beneficiarios INT NULL;
COMMENT ON COLUMN finanzas.sg_transferencia_direccion_departamental.tdd_beneficiarios IS 'Cantidad de beneficiarios';

ALTER TABLE finanzas.sg_transferencia_direccion_departamental_aud ADD tdd_beneficiarios INT NULL;


ALTER TABLE finanzas.sg_transferencias_a_ced ADD tac_beneficiarios INT NULL;
COMMENT ON COLUMN finanzas.sg_transferencias_a_ced.tac_beneficiarios IS 'Cantidad de beneficiarios del registro';

ALTER TABLE finanzas.sg_transferencias_a_ced_aud ADD tac_beneficiarios INT NULL;


ALTER TABLE finanzas.sg_recibos ADD rec_estado VARCHAR(12);
COMMENT ON COLUMN finanzas.sg_recibos.rec_estado IS 'Estado del registro';

ALTER TABLE finanzas.sg_recibos_aud ADD rec_estado VARCHAR(12);

ALTER TABLE finanzas.sg_transferencias_a_ced_aud ALTER COLUMN tac_monto_solicitado DROP NOT NULL;

-- 1.3.5
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_FUENTE_FIN','FB26',  'Buscar Fuente de Financiamiento', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_FUENTE_REC','FB27',  'Buscar Fuente de Recursos', 12, true, null, null,0);

--CREANCION DE SECUENCIAL PARA TABLA DE PROVEEDORES SIAP2 
CREATE SEQUENCE IF NOT EXISTS siap2.ss_provedor_pk_seq 
    INCREMENT 1 
    START 1
    MINVALUE 1 
    MAXVALUE 9223372036854775807 
    CACHE 1     
    NO CYCLE;

ALTER TABLE siap2.ss_proveedor ALTER COLUMN pro_id SET DEFAULT nextval('siap2.ss_provedor_pk_seq'::regclass);

-- CREANDO COLUMNA PROVEEDOR MINED EN TABLA DE PROVEEDOR
ALTER TABLE siap2.ss_proveedor ADD proveedor_mined bool NULL;
COMMENT ON COLUMN siap2.ss_proveedor.proveedor_mined IS 'Indica el tipo de proveedor del registro ';

-- CREANDO COLUMNA PROVEEDOR MINED EN TABLA DE PROVEEDOR AUD
ALTER TABLE siap2.ss_proveedor_hist ADD proveedor_mined bool NULL;
COMMENT ON COLUMN siap2.ss_proveedor.proveedor_mined IS 'Indica el tipo de proveedor del registro ';

-- OPERACIONES PARA CRUD PROVEEDORES
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_PROVEEDOR','F54',  'Crea Proveedorees', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_PROVEEDOR','F55',  'Actualiza Proveedores', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_PROVEEDOR','F56',  'Elimina Proveedores', 12, true, null, null,0);

-- 1.3.7
-- CREANDO TABLA DE TIPO DE RETIRO EN MOVIMIENTOS CUENTAS BANCARIAS
ALTER TABLE finanzas.sg_movimiento_cuenta_bancaria ADD mcb_tipo_retiro varchar NULL;
COMMENT ON COLUMN finanzas.sg_movimiento_cuenta_bancaria.mcb_tipo_retiro IS 'Tipo de retiro';

-- CREANDO TABLA DE TIPO DE RETIRO EN MOVIMIENTOS CUENTAS BANCARIAS AUDIT
ALTER TABLE finanzas.sg_movimiento_cuenta_bancaria_aud ADD mcb_tipo_retiro varchar NULL;
COMMENT ON COLUMN finanzas.sg_movimiento_cuenta_bancaria.mcb_tipo_retiro IS 'Comentario en tabla principal';

UPDATE finanzas.sg_presupuesto_escolar_movimiento SET    mov_monto_aprobado = 0
WHERE  mov_pk IN (SELECT mov_pk FROM   finanzas.sg_presupuesto_escolar_movimiento WHERE  mov_monto_aprobado IS NULL);



ALTER TABLE finanzas.sg_rel_cbc_tcb DROP CONSTRAINT rel_cuenta_bancaria_fkey; 


ALTER TABLE finanzas.sg_rel_cbc_tcb 
ADD CONSTRAINT rel_cuenta_bancaria_fkey 
FOREIGN KEY (rel_cbc_pk) REFERENCES finanzas.sg_cuentas_bancarias(cbc_pk)
MATCH FULL ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE finanzas.sg_direcciones_departamentales
ADD CONSTRAINT departamento_unique UNIQUE (ded_departamento_fk);





-- 1.4.1
ALTER TABLE finanzas.sg_rel_cbc_tcb DROP CONSTRAINT rel_cuenta_bancaria_fkey; 


ALTER TABLE finanzas.sg_rel_cbc_tcb 
ADD CONSTRAINT rel_cuenta_bancaria_fkey 
FOREIGN KEY (rel_cbc_pk) REFERENCES finanzas.sg_cuentas_bancarias(cbc_pk)
MATCH FULL ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE finanzas.sg_direcciones_departamentales
ADD CONSTRAINT departamento_unique UNIQUE (ded_departamento_fk);


ALTER TABLE finanzas.sg_movimiento_cuenta_bancaria DROP CONSTRAINT sg_movimiento_cuentas_bancarias_fk; 

ALTER TABLE finanzas.sg_movimiento_cuenta_bancaria 
ADD CONSTRAINT sg_movimiento_cuentas_bancarias_fkey 
FOREIGN KEY (mcb_cuenta_fk) REFERENCES finanzas.sg_cuentas_bancarias(cbc_pk)
MATCH FULL ON UPDATE CASCADE ON DELETE CASCADE;



-- AGREGANDO COLUMNA PARA GUARDA CHEQUE EN RETIRO CUENTAS BANCARIAS
ALTER TABLE finanzas.sg_movimiento_cuenta_bancaria ADD mcb_cheque_cb varchar NULL;
COMMENT ON COLUMN finanzas.sg_movimiento_cuenta_bancaria.mcb_cheque_cb IS 'Campo que almacena el número de cheque de un retito de cuenta bancaria';

-- AGREGANDO COLUMNA PARA GUARDA CHEQUE EN RETIRO CUENTAS BANCARIAS AUDITORIA
ALTER TABLE finanzas.sg_movimiento_cuenta_bancaria_aud ADD mcb_cheque_cb varchar NULL;
COMMENT ON COLUMN finanzas.sg_movimiento_cuenta_bancaria.mcb_cheque_cb IS 'Descripción en la tabla principal';

-- 1.4.2
-- SE AGREGA OPERACION PARA MENU DE PROVEEDORES 
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) 
VALUES ('MENU_PROVEEDORES','FM16',  'Menú para la vista de Proveedores administrados desde SIAP2', 12, true, null, null,0);


-- 1.4.3
ALTER TABLE finanzas.sg_facturas ADD fac_tipo_documento varchar(45) NOT NULL;
COMMENT ON COLUMN finanzas.sg_facturas.fac_tipo_documento IS 'Tipo de documento del registro';

ALTER TABLE finanzas.sg_facturas_aud ADD fac_tipo_documento varchar(45) NOT NULL;

-- SE CAMBIA NOMBRE DE COLUMNA IMPUESTOS POR DEDUCCIONES EN FACTURAS
ALTER TABLE finanzas.sg_facturas RENAME COLUMN fac_impuestos TO fac_deducciones;
ALTER TABLE finanzas.sg_facturas_aud RENAME COLUMN fac_impuestos TO fac_deducciones;

-- 1.5.0

-- SE CREA COLUMNA PARA DEFINIR SI UN CHEQUE HA SIDO COBRADO EN MOVIMIENTO CUENTAS BANCARIAS
ALTER TABLE finanzas.sg_movimiento_cuenta_bancaria ADD mcb_cheque_cobrado boolean NULL;
COMMENT ON COLUMN finanzas.sg_movimiento_cuenta_bancaria.mcb_cheque_cobrado IS 'Campo para definir un cheque cobrado.';

-- SE CREA COLUMNA PARA DEFINIR SI UN CHEQUE HA SIDO COBRADO EN MOVIMIENTO CUENTAS BANCARIAS AUDITORIA
ALTER TABLE finanzas.sg_movimiento_cuenta_bancaria_aud ADD mcb_cheque_cobrado boolean NULL;


-- OPERACION MENU CONCIALICION BANCARIA
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version)
VALUES ('MENU_CONCILIACION_BANCARIA','FM17',  'Menú para la gestión de conciliaciones bancarias', 12, true, null, null,0);

-- CREACION DEL SECUENCIAL DE LA TABLA DE CONCILIACIONES BANCARIAS
CREATE SEQUENCE IF NOT EXISTS finanzas.sg_sg_conciliaciones_bancarias_con_pk_seq 
    INCREMENT 1 
    START 1 
    MINVALUE 1 
    MAXVALUE 9223372036854775807 
    CACHE 1     
    NO CYCLE;

-- CREACION DE LA TABLA DE CONCILIACIONES BANCARIAS
CREATE TABLE IF NOT EXISTS finanzas.sg_conciliaciones_bancarias (
    con_pk bigint NOT NULL DEFAULT nextval('finanzas.sg_sg_conciliaciones_bancarias_con_pk_seq'::regclass), 
    con_cuenta_fk integer,
    con_fecha_desde timestamp without time zone,
    con_fecha_hasta timestamp without time zone,
    con_monto decimal,
    con_ult_mod_fecha timestamp without time zone, 
    con_ult_mod_usuario character varying(45), 
    con_version integer, 
    CONSTRAINT conciliacion_pkey PRIMARY KEY (con_pk)
);
   
   
COMMENT ON TABLE finanzas.sg_conciliaciones_bancarias IS 'Tabla para el registro de Conciliaciones Bancarias.';
COMMENT ON COLUMN finanzas.sg_conciliaciones_bancarias.con_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN finanzas.sg_conciliaciones_bancarias.con_cuenta_fk IS 'Indica la cuenta relacionada al registro.';
COMMENT ON COLUMN finanzas.sg_conciliaciones_bancarias.con_fecha_desde IS 'Indica la fecha desde cuando pertenece el registro';
COMMENT ON COLUMN finanzas.sg_conciliaciones_bancarias.con_fecha_hasta IS 'Indica la fecha hasta cuando pertenece el registro.';
COMMENT ON COLUMN finanzas.sg_conciliaciones_bancarias.con_monto IS 'Valor en $ del monto del registro.';
COMMENT ON COLUMN finanzas.sg_conciliaciones_bancarias.con_ult_mod_fecha IS 'Última fehca de modificación del registro.';
COMMENT ON COLUMN finanzas.sg_conciliaciones_bancarias.con_ult_mod_usuario IS 'Último usuario que modifico el registro.';
COMMENT ON COLUMN finanzas.sg_conciliaciones_bancarias.con_version IS 'Versión del registro.';

-- CREACION DE LA TABLA DE CONCILIACIONES BANCARIAS AUDITORIA
CREATE TABLE IF NOT EXISTS finanzas.sg_conciliaciones_bancarias_aud (
    con_pk bigint not null, 
    con_cuenta_fk integer,
    con_fecha_desde timestamp without time zone,
    con_fecha_hasta timestamp without time zone,
    con_monto decimal,
    con_ult_mod_fecha timestamp without time zone, 
    con_ult_mod_usuario character varying(45), 
    con_version integer, 
    rev bigint, 
    revtype smallint, 
    CONSTRAINT sg_conciliaciones_bancarias_aud_pkey PRIMARY KEY (con_pk,rev), 
    CONSTRAINT sg_conciliaciones_bancarias_aud_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev)
);

-- OPERACIONES CONCILIACIONES BANCARIAS
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_CONCILIACION_BANCARIA','F60',  'Crear Concialiacion Bancaria', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_CONCILIACION_BANCARIA','F61',  'Actualizar Conciliacion Bancaria', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_CONCILIACION_BANCARIA','F62',  'Elimina Conciliacion Bancaria', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_CONCILIACION_BANCARIA','FB29',  'Buscar Concialiacion Bancaria', 12, true, null, null,0);



-- OPERACIONES EDICION PRESUPUESTOS ESCOLARES APROBADOS
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_EDICION_PRESPUESTO_ESCOLAR_APROBADO','F63',  'permite Editar un presupuesto escolar ya aprobado', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_EDICION_PRESPUESTO_ESCOLAR_APROBADO','F64',  'Actualiza los cambios en presupuestos aprobados', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_EDICION_PRESPUESTO_ESCOLAR_APROBADO','F65',  'Elimina los cambios en prespuestos aprobados', 12, true, null, null,0);




CREATE SEQUENCE IF NOT EXISTS finanzas.sg_presupuesto_escolar_edicion_movimiento_pk_seq 
    INCREMENT 1 
    START 1 
    MINVALUE 1 
    MAXVALUE 9223372036854775807 
    CACHE 1     
    NO CYCLE;
--CREACION DE TABLA PARA LA GESTIÒN DE LA EDICIÓN DE MOVIMIENTOS DEL PRESUPUESTO ESCOLAR APROBADO
CREATE TABLE IF NOT EXISTS finanzas.sg_presupuesto_escolar_edicion_movimiento (
    mov_pk bigint NOT NULL DEFAULT nextval('finanzas.sg_presupuesto_escolar_edicion_movimiento_pk_seq'::regclass), 
    mov_codigo character varying(45), 
    mov_presupuesto_fk integer,
	mov_fuente_recursos varchar(255) NULL,
	mov_tipo varchar(255) NULL,
	mov_monto numeric(10,2) NULL,
	mov_ult_mod_fecha timestamp NULL,
	mov_ult_mod_usuario varchar(45) NULL,
	mov_version integer NULL,
	mov_origen varchar(2) NULL,
	mov_actividad_pk integer NULL,
	mov_num_movimiento integer NULL,
	mov_fuente_ingreso_pk integer NULL,
	mov_rubro_pk integer NULL,
	mov_monto_aprobado numeric NULL DEFAULT 0,
	mov_area_inversion_pk integer NULL,
	mov_subarea_inversion_pk integer NULL,
	mov_enero numeric NULL DEFAULT 0,
	mov_febrero numeric NULL DEFAULT 0,
	mov_marzo numeric NULL DEFAULT 0,
	mov_abril numeric NULL DEFAULT 0,
	mov_mayo numeric NULL DEFAULT 0,
	mov_junio numeric NULL DEFAULT 0,
	mov_julio numeric NULL DEFAULT 0,
	mov_agosto numeric NULL DEFAULT 0,
	mov_septiembre numeric NULL DEFAULT 0,
	mov_octubre numeric NULL DEFAULT 0,
	mov_noviembre numeric NULL DEFAULT 0,
	mov_diciembre numeric NULL DEFAULT 0,
	mov_total_anual numeric NULL,
	mov_techo_presupuestal integer NULL
);

-- Column comments

COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_edicion_movimiento.mov_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_edicion_movimiento.mov_codigo IS 'Código del registro.';
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_edicion_movimiento.mov_presupuesto_fk IS 'Indica la llave del presupuesto a la que pertenece el Movimiento';
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_edicion_movimiento.mov_fuente_recursos IS 'Indica la fuente del registro';
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_edicion_movimiento.mov_tipo IS 'Indica el tipo de registro';
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_edicion_movimiento.mov_monto IS 'Muestra el valor en $ por el cual el se realizo el registro';
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_edicion_movimiento.mov_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_edicion_movimiento.mov_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_edicion_movimiento.mov_version IS 'Versión del registro.';
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_edicion_movimiento.mov_origen IS 'Indica el Origen del registro.';
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_edicion_movimiento.mov_actividad_pk IS 'Indica la llave de la actividad  del registro.';
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_edicion_movimiento.mov_num_movimiento IS 'Indica el número correlativo del registro.';
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_edicion_movimiento.mov_fuente_ingreso_pk IS 'Indica la llave de la fuente de Ingreso del registro.';
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_edicion_movimiento.mov_rubro_pk IS 'Número de referencia a la tabla Rubros.';
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_edicion_movimiento.mov_monto_aprobado IS 'Muestra el valor aprobado en $ por el cual el se realizo el registro';
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_edicion_movimiento.mov_area_inversion_pk IS 'Indica la llave del area de inversion a la que pertenece el Movimiento';
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_edicion_movimiento.mov_subarea_inversion_pk IS 'Indica la llave de la subarea de inversion a la que pertenece el Movimiento';
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_edicion_movimiento.mov_enero IS 'Indica el valor de egreso parea el Movimiento del mes de Enero';
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_edicion_movimiento.mov_febrero IS 'Indica el valor de egreso parea el Movimiento del mes de Febrero';
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_edicion_movimiento.mov_marzo IS 'Indica el valor de egreso parea el Movimiento del mes de Marzo';
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_edicion_movimiento.mov_abril IS 'Indica el valor de egreso parea el Movimiento del mes de Abril';
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_edicion_movimiento.mov_mayo IS 'Indica el valor de egreso parea el Movimiento del mes de Mayo';
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_edicion_movimiento.mov_junio IS 'Indica el valor de egreso parea el Movimiento del mes de Junio';
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_edicion_movimiento.mov_julio IS 'Indica el valor de egreso parea el Movimiento del mes de Julio';
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_edicion_movimiento.mov_agosto IS 'Indica el valor de egreso parea el Movimiento del mes de Agosto';
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_edicion_movimiento.mov_septiembre IS 'Indica el valor de egreso parea el Movimiento del mes de Septiembre';
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_edicion_movimiento.mov_octubre IS 'Indica el valor de egreso parea el Movimiento del mes de Octubre';
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_edicion_movimiento.mov_noviembre IS 'Indica el valor de egreso parea el Movimiento del mes de Noviembre';
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_edicion_movimiento.mov_diciembre IS 'Indica el valor de egreso parea el Movimiento del mes de Diciembre';
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_edicion_movimiento.mov_total_anual IS 'Indica el valor de egreso parea el Movimiento durantel el año';
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_edicion_movimiento.mov_techo_presupuestal IS 'Número de referencia al tope presupuestal de siap2.';


--CREACION DE TABLA PARA LA GESTIÒN DE AUDITORIA DE LA EDICIÓN DE MOVIMIENTOS DEL PRESUPUESTO ESCOLAR APROBADO
CREATE TABLE IF NOT EXISTS finanzas.sg_presupuesto_escolar_edicion_movimiento_aud (
    mov_pk bigint not null, 
  mov_codigo character varying(45), 
    mov_presupuesto_fk integer,
	mov_fuente_recursos varchar(255) NULL,
	mov_tipo varchar(255) NULL,
	mov_monto numeric(10,2) NULL,
	mov_ult_mod_fecha timestamp NULL,
	mov_ult_mod_usuario varchar(45) NULL,
	mov_version integer NULL,
	mov_origen varchar(2) NULL,
	mov_actividad_pk integer NULL,
	mov_num_movimiento integer NULL,
	mov_fuente_ingreso_pk integer NULL,
	mov_rubro_pk integer NULL,
	mov_monto_aprobado numeric NULL DEFAULT 0,
	mov_area_inversion_pk integer NULL,
	mov_subarea_inversion_pk integer NULL,
	mov_enero numeric NULL DEFAULT 0,
	mov_febrero numeric NULL DEFAULT 0,
	mov_marzo numeric NULL DEFAULT 0,
	mov_abril numeric NULL DEFAULT 0,
	mov_mayo numeric NULL DEFAULT 0,
	mov_junio numeric NULL DEFAULT 0,
	mov_julio numeric NULL DEFAULT 0,
	mov_agosto numeric NULL DEFAULT 0,
	mov_septiembre numeric NULL DEFAULT 0,
	mov_octubre numeric NULL DEFAULT 0,
	mov_noviembre numeric NULL DEFAULT 0,
	mov_diciembre numeric NULL DEFAULT 0,
	mov_total_anual numeric NULL,
	mov_techo_presupuestal integer null,
    rev bigint, 
    revtype smallint, 
    CONSTRAINT sg_presupuesto_escolar_edicion_movimiento_aud_pkey PRIMARY KEY (mov_pk,rev), 
    CONSTRAINT sg_presupuesto_escolar_edicion_movimiento_aud_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev)
);




-- AGREGANDO COLUMNAS DE DOMICILIO Y PROFESION
ALTER TABLE finanzas.sg_direcciones_departamentales ADD ded_director_domicilio varchar(255) NULL;
COMMENT ON COLUMN finanzas.sg_direcciones_departamentales.ded_director_domicilio IS 'Campo que almacena el domicilio del director en el registro';

ALTER TABLE finanzas.sg_direcciones_departamentales ADD ded_director_profesion_fk bigint NULL;
COMMENT ON COLUMN finanzas.sg_direcciones_departamentales.ded_director_profesion_fk IS 'Campo que almacena la profesión del director en el registro';

ALTER TABLE finanzas.sg_direcciones_departamentales_aud ADD ded_director_domicilio varchar(255) NULL;
ALTER TABLE finanzas.sg_direcciones_departamentales_aud ADD ded_director_profesion_fk bigint NULL;


--CREACION DE TABLA PARA LAS TRANSFERENCIAS GLOBLAES POR DEPARTAMENTO
CREATE SEQUENCE IF NOT EXISTS finanzas.sg_transferencias_gdep_pk_seq 
    INCREMENT 1 
    START 1 
    MINVALUE 1 
    MAXVALUE 9223372036854775807 
    CACHE 1     
    NO CYCLE;

CREATE TABLE IF NOT EXISTS finanzas.sg_transferencias_gdep (
    tgd_pk bigint NOT NULL DEFAULT nextval('finanzas.sg_transferencias_gdep_pk_seq'::regclass), 
    tgd_transferencia_fk bigint NOT NULL,
    tgd_departamento_fk bigint NOT NULL,
    tgd_monto decimal,
    tgd_ult_mod_fecha timestamp without time zone, 
    tgd_ult_mod_usuario character varying(45), 
    tgd_version integer, 
    CONSTRAINT transferenciad_dep_pkey PRIMARY KEY (tgd_pk));
   
   
COMMENT ON TABLE finanzas.sg_transferencias_gdep IS 'Tabla para el registro de la transferencias globales por departamaneto.';
COMMENT ON COLUMN finanzas.sg_transferencias_gdep.tgd_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN finanzas.sg_transferencias_gdep.tgd_transferencia_fk IS 'Número de referencia de una transferencia (ss_transferencia) generada en SIAP2.';
COMMENT ON COLUMN finanzas.sg_transferencias_gdep.tgd_departamento_fk IS 'Número de referencia del departamento del registro.';
COMMENT ON COLUMN finanzas.sg_transferencias_gdep.tgd_monto IS 'Monto total de la transferencia del registro';
COMMENT ON COLUMN finanzas.sg_transferencias_gdep.tgd_ult_mod_fecha IS 'Última fehca de modificación del registro.';
COMMENT ON COLUMN finanzas.sg_transferencias_gdep.tgd_ult_mod_usuario IS 'Último usuario que modifico el registro.';
COMMENT ON COLUMN finanzas.sg_transferencias_gdep.tgd_version IS 'Versión del registro.';


CREATE TABLE IF NOT EXISTS finanzas.sg_transferencias_gdep_aud (
    tgd_pk bigint NOT NULL DEFAULT nextval('finanzas.sg_transferencias_gdep_pk_seq'::regclass), 
    tgd_transferencia_fk bigint,
    tgd_departamento_fk bigint,
    tgd_monto decimal,
    tgd_ult_mod_fecha timestamp without time zone, 
    tgd_ult_mod_usuario character varying(45), 
    tgd_version integer,
    rev bigint, 
    revtype smallint, 
    CONSTRAINT sg_transferencias_gdep_aud_pkey PRIMARY KEY (tgd_pk,rev), 
    CONSTRAINT sg_transferencias_gdep_aud_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev)
);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_TRANSFERENCIA_GLOBAL_DEP','FM18',  '', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_TRANSFERENCIAS_GLOBAL_DEP','FB28',  '', 12, true, null, null,0);
    
DROP VIEW finanzas.detalle_req_fondo_view;

--Cambio de nombre de tabla solicitud de transferencia

ALTER TABLE finanzas.sg_solicitudes_transferencia DROP COLUMN str_transf_direccion_dep_fk;
ALTER TABLE finanzas.sg_solicitudes_transferencia_aud DROP COLUMN str_transf_direccion_dep_fk;

truncate table finanzas.sg_solicitudes_transferencia cascade;

ALTER TABLE finanzas.sg_solicitudes_transferencia ADD str_transf_global_dep_fk bigint NOT NULL;
COMMENT ON COLUMN finanzas.sg_solicitudes_transferencia.str_transf_global_dep_fk IS 'Número de referencia a transferencia global dep.  en el registro';

ALTER TABLE finanzas.sg_solicitudes_transferencia_aud ADD str_transf_global_dep_fk bigint NULL;

ALTER TABLE finanzas.sg_solicitudes_transferencia 
ADD CONSTRAINT str_transf_global_dep_fkey 
FOREIGN KEY (str_transf_global_dep_fk) REFERENCES finanzas.sg_transferencias_gdep(tgd_pk)
MATCH FULL ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE finanzas.sg_solicitudes_transferencia 
ADD CONSTRAINT transferencia_unique UNIQUE (str_transf_global_dep_fk);


ALTER TABLE finanzas.sg_solicitudes_transferencia RENAME TO sg_requerimientos_fondo;
ALTER TABLE finanzas.sg_solicitudes_transferencia_aud RENAME TO sg_requerimientos_fondo_aud;



drop table finanzas.sg_desembolsos;
drop table finanzas.sg_desembolsos_aud;

--RECREAR LA TABLA DE DESEMBOLSO
create table finanzas.sg_desembolsos (
 	dsb_pk bigint not null default nextval('finanzas.sg_desembolsos_pk_seq'::regclass),
 	dsb_estado varchar(25),
 	dsb_porcentaje integer,
 	dsb_monto decimal,
 	dsb_ult_mod_fecha timestamp without time zone, 
    dsb_ult_mod_usuario character varying(45), 
    dsb_version integer,
	CONSTRAINT sg_desembolsos_pkey PRIMARY KEY (dsb_pk)
);

create table finanzas.sg_desembolsos_aud (
 	dsb_pk bigint not null default nextval('finanzas.sg_desembolsos_pk_seq'::regclass),
 	dsb_estado varchar(25),
 	dsb_porcentaje integer,
 	dsb_monto decimal,
        dsb_ult_mod_fecha timestamp without time zone, 
        dsb_ult_mod_usuario character varying(45), 
        dsb_version integer,
        rev bigint, 
        revtype smallint, 
        CONSTRAINT sg_desembolsos_aud_pkey PRIMARY KEY (dsb_pk,rev), 
        CONSTRAINT sg_desembolsos_aud_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev)
);

-- 1.6.0
drop table finanzas.sg_detalle_desembolsos;
drop table finanzas.sg_detalle_desembolsos_aud;

--CREACION DE TABLA DETALLE DE DESEMBOLSO
CREATE SEQUENCE IF NOT EXISTS finanzas.sg_detalle_desembolsos_pk_seq 
    INCREMENT 1 
    START 1 
    MINVALUE 1 
    MAXVALUE 9223372036854775807 
    CACHE 1     
    NO CYCLE;

create table finanzas.sg_detalle_desembolsos (
 	ded_pk bigint not null default nextval('finanzas.sg_detalle_desembolsos_pk_seq'::regclass),
        ded_desembolso_fk bigint not null,
 	ded_req_fondo_fk bigint not null,
        ded_fuente_recursos_fk bigint not null,
        ded_porcentaje real not null,
        ded_monto decimal not null,
 	ded_ult_mod_fecha timestamp without time zone, 
        ded_ult_mod_usuario character varying(45), 
        ded_version integer,
	CONSTRAINT sg_sg_detalle_desembolsos_pkey PRIMARY KEY (ded_pk)
);
COMMENT ON TABLE finanzas.sg_detalle_desembolsos IS 'Tabla para el registro de la transferencias globales por departamaneto.';
COMMENT ON COLUMN finanzas.sg_detalle_desembolsos.ded_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN finanzas.sg_detalle_desembolsos.ded_req_fondo_fk IS 'Número de referencia de un requerimiento de fondo';
COMMENT ON COLUMN finanzas.sg_detalle_desembolsos.ded_desembolso_fk IS 'Número de referencia del desembolso.';
COMMENT ON COLUMN finanzas.sg_detalle_desembolsos.ded_fuente_recursos_fk IS 'Número de referencia de la fuente de recursos.';
COMMENT ON COLUMN finanzas.sg_detalle_desembolsos.ded_porcentaje IS 'Porcentaje del desembolso de el requerimiento de fondo del registro';
COMMENT ON COLUMN finanzas.sg_detalle_desembolsos.ded_monto IS 'Monto del desembolso del requerimiento de fondo del registro';
COMMENT ON COLUMN finanzas.sg_detalle_desembolsos.ded_ult_mod_fecha IS 'Última fehca de modificación del registro.';
COMMENT ON COLUMN finanzas.sg_detalle_desembolsos.ded_ult_mod_usuario IS 'Último usuario que modifico el registro.';
COMMENT ON COLUMN finanzas.sg_detalle_desembolsos.ded_version IS 'Versión del registro.';

create table finanzas.sg_detalle_desembolsos_aud (ded_pk bigint not null default nextval('finanzas.sg_detalle_desembolsos_pk_seq'::regclass),
        ded_desembolso_fk bigint,
 	    ded_req_fondo_fk bigint,
        ded_fuente_recursos_fk bigint,
        ded_porcentaje real,
        ded_monto decimal,
 	    ded_ult_mod_fecha timestamp without time zone, 
        ded_ult_mod_usuario character varying(45), 
        ded_version integer,
	CONSTRAINT sg_sg_detalle_desembolsos_aud_pkey PRIMARY KEY (ded_pk)
);
COMMENT ON TABLE finanzas.sg_detalle_desembolsos_aud IS 'Tabla de auditoría para la transferencias globales por departamaneto.';




ALTER TABLE finanzas.sg_detalle_desembolsos 
ADD CONSTRAINT ded_req_fondo_fkey 
FOREIGN KEY (ded_req_fondo_fk) REFERENCES finanzas.sg_requerimientos_fondo(str_pk)
MATCH FULL ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE finanzas.sg_detalle_desembolsos 
ADD CONSTRAINT ded_desembolso_fkey 
FOREIGN KEY (ded_desembolso_fk) REFERENCES finanzas.sg_desembolsos(dsb_pk)
MATCH FULL ON UPDATE CASCADE ON DELETE CASCADE;


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_INSUMO','FB19', 'Busca Insumo', 12, true, null, null,0);

-- CREANDO COLUMNA ANULACION MOVIMIENTO
ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento ADD mov_anulacion bool NULL;
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_movimiento.mov_anulacion IS 'Bandera que muestras si aplica a anulación';

-- CREANDO COLUMNA ANULACION MOVIMIENTO AUD
ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento_aud ADD mov_anulacion bool NULL;
 
-- CREANDO COLUMNA DE FUENTE DE INGRESO TABLA 1 EN EGRESO TABLA 2
ALTER TABLE finanzas.sg_presupuesto_escolar_edicion_movimiento ADD mov_fuente_ingreso_original_pk bigint NULL;
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_edicion_movimiento.mov_fuente_ingreso_original_pk IS 'Indica la Fuente de ingreso de la tabla Original';

-- CREANDO COLUMNA ANULACION MOVIMIENTO AUD
ALTER TABLE finanzas.sg_presupuesto_escolar_edicion_movimiento_aud ADD mov_fuente_ingreso_original_pk bigint NULL;
 
-- OPERACION CAMBIAR ESTADO APROBADO MODIFICADO.
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) 
VALUES ('CAMBIAR_PRESUPUESTO_ESTADO_APROBADO_MODIFICADO','FP4',  'Permite cambiar estado Modificado ', 12, true, null, null,0);


CREATE SEQUENCE IF NOT EXISTS finanzas.sg_compromiso_presupuestario_por_req_fondo_pk_seq 
    INCREMENT 1 
    START 1 
    MINVALUE 1 
    MAXVALUE 9223372036854775807 
    CACHE 1     
    NO CYCLE;
--CREACION DE TABLA PARA LA GESTIÒN DE LOS COMPROMISOS PRESUPUESTARIOS POR REQUERIMIENTOS DE FONDOS
CREATE TABLE IF NOT EXISTS finanzas.sg_compromiso_presupuestario_por_req_fondo (
    cpr_pk bigint NOT NULL DEFAULT nextval('finanzas.sg_compromiso_presupuestario_por_req_fondo_pk_seq'::regclass),      
    cpr_requerimiento_fondo_fk integer,	
	cpr_numero_presupuestario varchar(255) NULL,
	cpr_ult_mod_fecha timestamp without time zone, 
    cpr_ult_mod_usuario character varying(45), 
    cpr_version integer, 
    CONSTRAINT compromiso_pkey PRIMARY KEY (cpr_pk)
);

-- Column comments
COMMENT ON TABLE finanzas.sg_compromiso_presupuestario_por_req_fondo IS 'Tabla para el registro de Compromisos Presupuestarios.';
COMMENT ON COLUMN finanzas.sg_compromiso_presupuestario_por_req_fondo.cpr_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN finanzas.sg_compromiso_presupuestario_por_req_fondo.cpr_requerimiento_fondo_fk IS 'Indica el Requerimiento relacionado al registro.';
COMMENT ON COLUMN finanzas.sg_compromiso_presupuestario_por_req_fondo.cpr_numero_presupuestario IS 'Indica el número presupuestario asociado al registro.';
COMMENT ON COLUMN finanzas.sg_compromiso_presupuestario_por_req_fondo.cpr_ult_mod_fecha IS 'Última fehca de modificación del registro.';
COMMENT ON COLUMN finanzas.sg_compromiso_presupuestario_por_req_fondo.cpr_ult_mod_usuario IS 'Último usuario que modifico el registro.';
COMMENT ON COLUMN finanzas.sg_compromiso_presupuestario_por_req_fondo.cpr_version IS 'Versión del registro.';

--CREACION DE TABLA PARA LA GESTIÒN DE AUDITORIA DE LOS COMPROMISOS PRESUPUESTARIOS POR REQUERIMIENTOS DE FONDOS
CREATE TABLE IF NOT EXISTS finanzas.sg_compromiso_presupuestario_por_req_fondo_aud (
    cpr_pk bigint not null, 
  	cpr_requerimiento_fondo_fk integer,	
	cpr_numero_presupuestario varchar(255) NULL,
	cpr_ult_mod_fecha timestamp without time zone, 
    cpr_ult_mod_usuario character varying(45), 
    cpr_version integer, 
    rev bigint, 
    revtype smallint, 
    CONSTRAINT sg_compromiso_presupuestario_por_req_fondo_aud_pkey PRIMARY KEY (cpr_pk,rev), 
    CONSTRAINT sg_compromiso_presupuestario_por_req_fondo_aud_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev)
);

-- RELACION DE COMPROMISOS PRESUPUESTARIOS CON REQUERIMIENTO DE FONDOS
ALTER TABLE finanzas.sg_compromiso_presupuestario_por_req_fondo ADD CONSTRAINT sg_compromiso_presupuestario_por_req_fondo_fkey 
FOREIGN KEY (cpr_requerimiento_fondo_fk) REFERENCES finanzas.sg_requerimientos_fondo(str_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

-- OPERACION PARA CARGAR SAC EN REQUERIMIENTO DE FONDO
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CARGAR_SAC_REQUERIMIENTO_FONDO','FP5',  'Permite cargar los numeros de sac de los requerimientos de fondo.', 12, true, null, null,0);

COMMENT ON COLUMN finanzas.sg_compromiso_presupuestario_por_req_fondo.cpr_fuente_recursos_fk IS 'Indica la Fuente de recurso a la que esta relacionada el registro.';


-- 1.7.0


-- Actual parameter values may differ, what you see is a default string representation of values
UPDATE seguridad.sg_operaciones
SET ope_codigo='FB31'
WHERE ope_pk=1847;


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CARGAR_DESEMBOLSOS','FP6',  'Permite cargar los desembolsos', 12, true, null, null,0);
DROP TABLE finanzas.sg_desembolsos_aud;
create table finanzas.sg_desembolsos_aud (
 	dsb_pk bigint not null,
 	dsb_estado varchar(25),
 	dsb_porcentaje integer,
 	dsb_monto decimal,
        dsb_ult_mod_fecha timestamp without time zone, 
        dsb_ult_mod_usuario character varying(45), 
        dsb_version integer,
        rev bigint, 
        revtype smallint, 
        CONSTRAINT sg_desembolsos_aud_pkey PRIMARY KEY (dsb_pk,rev), 
        CONSTRAINT sg_desembolsos_aud_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev)
);


DROP TABLE finanzas.sg_detalle_desembolsos_aud;
create table finanzas.sg_detalle_desembolsos_aud (
        ded_pk bigint not null,
        ded_desembolso_fk bigint,
        ded_req_fondo_fk bigint,
        ded_fuente_recursos_fk bigint,
        ded_porcentaje real,
        ded_monto decimal,
        ded_ult_mod_fecha timestamp without time zone, 
        ded_ult_mod_usuario character varying(45), 
        ded_version integer,
        rev bigint, 
        revtype smallint, 
        CONSTRAINT sg_sg_detalle_desembolsos_aud_pkey PRIMARY KEY (ded_pk,rev), 
        CONSTRAINT sg_sg_detalle_desembolsos_aud_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev)
);
COMMENT ON TABLE finanzas.sg_detalle_desembolsos_aud IS 'Tabla de auditoría para la transferencias globales por departamaneto.';

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_DETALLE_DESEMBOLSO','F66',  'Crear detalle de desembolso', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_DETALLE_DESEMBOLSO','F67',  'Actualizar  detalle de desembolso', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_DETALLE_DESEMBOLSO','F68',  'Elimina  detalle de desembolso', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_DETALLE_DESEMBOLSO','FB30',  'Buscar Detalle de desembolsos ', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CARGAR_DESEMBOLSOS','FP6',  'Permite cargar los desembolsos', 12, true, null, null,0);

-- SE AGREGA COLUMNA PARA FK FUENTE DE RECURSOS
ALTER TABLE finanzas.sg_compromiso_presupuestario_por_req_fondo ADD COLUMN cpr_fuente_recursos_fk bigint;
-- SE AGREGA COLUMNA PARA FK FUENTE DE RECURSOS AUD 
ALTER TABLE finanzas.sg_compromiso_presupuestario_por_req_fondo_aud ADD COLUMN cpr_fuente_recursos_fk bigint;

COMMENT ON COLUMN finanzas.sg_compromiso_presupuestario_por_req_fondo.cpr_fuente_recursos_fk IS 'Indica la Fuente de recurso a la que esta relacionada el registro.';


-- COLUMNA DE DESEMBOLSO EN MOVIMIENTO DE CUENTA BANCARIA DEPARTAMENTAL
ALTER TABLE finanzas.sg_movimientos_direccion_departamental ADD COLUMN mdd_det_desembolso_fk bigint;
ALTER TABLE finanzas.sg_movimientos_direccion_departamental_aud ADD COLUMN mdd_det_desembolso_fk bigint;

COMMENT ON COLUMN finanzas.sg_movimientos_direccion_departamental.mdd_det_desembolso_fk IS 'Número de referencia al desembolso del registro.';


-- Desembolsos a Centros Educativos
CREATE SEQUENCE IF NOT EXISTS finanzas.sg_desembolsos_ced_dce_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;

create table if not exists finanzas.sg_desembolsos_ced (
	dce_pk bigint not null default nextval('finanzas.sg_desembolsos_ced_dce_pk_seq'::regclass),
	dce_det_desembolso_fk bigint not null,
	dce_sede_fk bigint not null,
	dce_estado varchar(15) not null,
	dce_monto decimal,
	dce_ult_mod_fecha timestamp without time zone,
	dce_ult_mod_usuario character varying(45),
	dce_version integer,
	constraint sg_desembolsos_ced_pkey primary key (dce_pk));

COMMENT ON TABLE finanzas.sg_desembolsos_ced IS 'Tabla para el registro de desembolsos a centros educativos.';
COMMENT ON COLUMN finanzas.sg_desembolsos_ced.dce_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN finanzas.sg_desembolsos_ced.dce_det_desembolso_fk IS 'Número de referencia al detalle de desembolso del registro.';
COMMENT ON COLUMN finanzas.sg_desembolsos_ced.dce_estado IS 'Estado del registro.';
COMMENT ON COLUMN finanzas.sg_desembolsos_ced.dce_monto IS 'Monto del registro.';
COMMENT ON COLUMN finanzas.sg_desembolsos_ced.dce_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN finanzas.sg_desembolsos_ced.dce_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN finanzas.sg_desembolsos_ced.dce_version IS 'Versión del registro.';

create table if not exists finanzas.sg_desembolsos_ced_aud (
	dce_pk bigint not null,
	dce_det_desembolso_fk bigint,
	dce_sede_fk bigint,
	dce_estado varchar(15),
	dce_monto decimal,
	dce_ult_mod_fecha timestamp without time zone,
	dce_ult_mod_usuario character varying(45),
	dce_version integer,
	rev bigint,
	revtype smallint,
	CONSTRAINT sg_desembolsos_ced_aud_pkey PRIMARY KEY (dce_pk,rev), 
    CONSTRAINT sg_desembolsos_ced_aud_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev)
);

COMMENT ON TABLE finanzas.sg_desembolsos_ced IS 'Tabla de auditoria para el registro de desembolsos a centros educativos.';

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_DESEMBOLSOS','FM19',  'Menú de Desembolsos', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_DESEMBOLSO_DETALLE','FM20',  'Menú de Desembolsos Detalle', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_DESEMBOLSO_CED','FM21',  'Menú de Desembolsos Centros Educativos', 12, true, null, null,0);


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_DESEMBOLSO_CED','F69',  'Crear Desembolso a Centro Educativo', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_DESEMBOLSO_CED','F70',  'Actualizar Desembolso a Centro Educativo', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_DESEMBOLSO_CED','F71',  'Eliminar Desembolso a Centro Educativo', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_DESEMBOLSO_CED','FB32',  'Buscar Desembolso de Centros Educativos', 12, true, null, null,0);


-- RELACION CON DETALLE DE DESEMBOLSOS
ALTER TABLE finanzas.sg_desembolsos_ced ADD CONSTRAINT dce_det_desembolso_fkey
FOREIGN KEY (dce_det_desembolso_fk) REFERENCES finanzas.sg_detalle_desembolsos(ded_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;


-- RELACION CON SEDE
ALTER TABLE finanzas.sg_desembolsos_ced ADD CONSTRAINT dce_sede_fkey
FOREIGN KEY (dce_sede_fk) REFERENCES centros_educativos.sg_sedes(sed_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;


COMMENT ON COLUMN finanzas.sg_compromiso_presupuestario_por_req_fondo.cpr_fuente_recursos_fk IS 'Indica la Fuente de recurso a la que esta relacionada el registro.';

--1.8.0
-- Número de transacción
ALTER TABLE finanzas.sg_movimientos_direccion_departamental ADD COLUMN mdd_transaccion varchar(45);
ALTER TABLE finanzas.sg_movimientos_direccion_departamental_aud ADD COLUMN mdd_transaccion varchar(45);

COMMENT ON COLUMN finanzas.sg_movimientos_direccion_departamental.mdd_transaccion IS 'Indica el número de transacción del registro.';


-- BORRANDO COLUMNA DE AÑO LECTIVO
ALTER TABLE finanzas.sg_desembolsos_ced DROP COLUMN dce_sede_fk;
ALTER TABLE finanzas.sg_desembolsos_ced_aud DROP COLUMN dce_sede_fk;

-- Requerimiento de Fondo en Desembolsos a Ced
ALTER TABLE finanzas.sg_desembolsos_ced ADD COLUMN dce_req_fondo_ced_fk bigint;
ALTER TABLE finanzas.sg_desembolsos_ced_aud ADD COLUMN dce_req_fondo_ced_fk bigint;

COMMENT ON COLUMN finanzas.sg_desembolsos_ced.dce_req_fondo_ced_fk IS 'Indica el número de requerimiento de fondo de sede del registro.';

-- Número de transacción de movimientos de cuentas bancarias de sedes
ALTER TABLE finanzas.sg_movimiento_cuenta_bancaria ADD COLUMN mcb_transaccion varchar(45);
ALTER TABLE finanzas.sg_movimiento_cuenta_bancaria_aud ADD COLUMN mcb_transaccion varchar(45);

COMMENT ON COLUMN finanzas.sg_movimiento_cuenta_bancaria.mcb_transaccion IS 'Indica el número de transacción del registro.';

-- COLUMNA DE DESEMBOLSO EN MOVIMIENTO DE CUENTA BANCARIA DEPARTAMENTAL
ALTER TABLE finanzas.sg_movimiento_cuenta_bancaria ADD COLUMN mcb_desembolso_ced_fk bigint;
ALTER TABLE finanzas.sg_movimiento_cuenta_bancaria_aud ADD COLUMN mcb_desembolso_ced_fk bigint;

COMMENT ON COLUMN finanzas.sg_movimiento_cuenta_bancaria.mcb_desembolso_ced_fk IS 'Número de referencia del desembolso a ced del registro.';


-- RELACIÓN DE REQUERIMIENTO DE FONDO EN DESEMBOLSO A CED
ALTER TABLE finanzas.sg_desembolsos_ced ADD CONSTRAINT dce_req_fondo_ced_fkey
FOREIGN KEY (dce_req_fondo_ced_fk) REFERENCES finanzas.sg_req_fond_ced(rfc_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;


-- RELACIÓN DE DESEMBOLSO A CED EN MOVIMIENTOS DE CUENTA BANCARIA 
ALTER TABLE finanzas.sg_movimiento_cuenta_bancaria ADD CONSTRAINT mcb_desembolso_ced_fkey
FOREIGN KEY (mcb_desembolso_ced_fk) REFERENCES finanzas.sg_desembolsos_ced(dce_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;



-- COLUMNA PARA FUENTE DE INGRESOS EN CUENTAS BANCARIAS SEDES
ALTER TABLE finanzas.sg_movimiento_cuenta_bancaria ADD COLUMN mcb_mov_fuente_ingresos_fk bigint;
COMMENT ON COLUMN finanzas.sg_movimiento_cuenta_bancaria.mcb_mov_fuente_ingresos_fk IS 'Fuente de Ingreso a la que pertenece el registro';

-- COLUMNA PARA FUENTE DE INGRESOS EN CUENTAS BANCARIAS SEDES AUDITORIA
ALTER TABLE finanzas.sg_movimiento_cuenta_bancaria_aud ADD COLUMN mcb_mov_fuente_ingresos_fk bigint;

-- RELACION DE MOVIMIENTOS PRESUPUESTO CON INGRESOS CUENTAS BANCARIAS SEDES
ALTER TABLE finanzas.sg_movimiento_cuenta_bancaria ADD CONSTRAINT sg_fuente_ingreso_fk FOREIGN KEY (mcb_mov_fuente_ingresos_fk) 
REFERENCES finanzas.sg_presupuesto_escolar_movimiento (mov_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;



-- CREANDO COLUMNA ATORIZACION PRESUPUESTOS
ALTER TABLE finanzas.sg_presupuesto_escolar ADD pes_edicion boolean;
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar.pes_edicion IS 'Bandera que muestras si aplica a Edición';

-- CREANDO COLUMNA ATORIZACION PRESUPUESTOS AUD
ALTER TABLE finanzas.sg_presupuesto_escolar_aud ADD pes_edicion boolean;

-- OPERACION AUTORIZAR EDICION PRESUPUESTOS
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) 
VALUES ('AUTORIZAR_EDICION_PRESUPUESTO','FP7',  'Permite Autorizar a Editar el Presupuesto Escolar', 12, true, null, null,0);



-- TABLA AUTORIZACION EDICION PRESUPUESTOS
CREATE SEQUENCE IF NOT EXISTS finanzas.sg_autorizacion_edicion_presupuestos_fk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;

create table if not exists finanzas.sg_autorizacion_edicion_presupuestos (
	aut_pk bigint not null default nextval('finanzas.sg_autorizacion_edicion_presupuestos_fk_seq'::regclass),
	aut_presupuesto_fk bigint not null,
	aut_usuario_validado_fk bigint not null,
	aut_ult_mod_fecha timestamp without time zone,
	aut_ult_mod_usuario character varying(45),
	aut_version integer,
	constraint sg_sg_autorizacion_edicion_presupuestos_pkey primary key (aut_pk));

COMMENT ON TABLE finanzas.sg_autorizacion_edicion_presupuestos IS 'Tabla para el registro de desembolsos a centros educativos.';
COMMENT ON COLUMN finanzas.sg_autorizacion_edicion_presupuestos.aut_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN finanzas.sg_autorizacion_edicion_presupuestos.aut_presupuesto_fk IS 'Número de referencia al presupuesto del registro.';
COMMENT ON COLUMN finanzas.sg_autorizacion_edicion_presupuestos.aut_usuario_validado_fk IS 'Número de referencia al usuario validado del registro.';
COMMENT ON COLUMN finanzas.sg_autorizacion_edicion_presupuestos.aut_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN finanzas.sg_autorizacion_edicion_presupuestos.aut_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN finanzas.sg_autorizacion_edicion_presupuestos.aut_version IS 'Versión del registro.';


-- TABLA AUTORIZACION EDICION PRESUPUESTOS_AUD

create table if not exists finanzas.sg_autorizacion_edicion_presupuestos_aud (
	aut_pk bigint not null,
	aut_presupuesto_fk bigint,
	aut_usuario_validado_fk bigint,
	aut_ult_mod_fecha timestamp without time zone,
	aut_ult_mod_usuario character varying(45),
	aut_version integer,
	rev bigint,
	revtype smallint,
	CONSTRAINT sg_autorizacion_edicion_presupuestos_aud_pkey PRIMARY KEY (aut_pk,rev), 
    CONSTRAINT sg_autorizacion_edicion_presupuestosd_aud_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev)
);

COMMENT ON TABLE finanzas.sg_autorizacion_edicion_presupuestos_aud IS 'Tabla de auditoria para el registro de las atorizaciones de Edicion Presupuesto.';




-- OPERACION BUSCAR AUTORIZACION EDICION DE PRESUPUESTOS
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) 
VALUES ('BUSCAR_AUTORIZACION_EDICION_PRESUPUESTO','FB34',  'Busca Autorizaciones para la edición de presupuestos', 12, true, null, null,0);

-- OPERACIONES CRUD AUTORIZACION EDICION DE PRESUPUESTOS
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) 
VALUES ('CREAR_AUTORIZACION_EDICION_PRESUPUESTO','F75',  'Crea autorización para editar presupuestos aprobados', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) 
VALUES ('ACTUALIZAR_AUTORIZACION_EDICION_PRESUPUESTO','F76',  'Actualiza autorización edición presupuestos', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) 
VALUES ('ELIMINAR_AUTORIZACION_EDICION_PRESUPUESTO','F77',  'Elimina autorización de edción presupuestos', 12, true, null, null,0);

ALTER TABLE finanzas.sg_presupuesto_escolar ALTER COLUMN pes_edicion SET DEFAULT false;

ALTER TABLE finanzas.sg_movimiento_caja_chica RENAME TO sg_cajas_chicas;
ALTER TABLE finanzas.sg_movimiento_caja_chica_aud RENAME TO sg_cajas_chicas_aud;

-- 1.8.x
ALTER TABLE finanzas.sg_compromiso_presupuestario_por_req_fondo DROP CONSTRAINT sg_compromiso_presupuestario_por_req_fondo_fkey;

ALTER TABLE finanzas.sg_compromiso_presupuestario_por_req_fondo ADD CONSTRAINT sg_compromiso_presupuestario_por_req_fondo_fkey 
FOREIGN KEY (cpr_requerimiento_fondo_fk) REFERENCES finanzas.sg_requerimientos_fondo(str_pk) MATCH FULL ON UPDATE CASCADE ON DELETE cascade;

ALTER TABLE finanzas.sg_requerimientos_fondo DROP CONSTRAINT transferencia_unique;

ALTER TABLE finanzas.sg_transferencias_a_ced_aud ALTER COLUMN tac_estado DROP NOT NULL;
ALTER TABLE finanzas.sg_transferencias_a_ced_aud ALTER COLUMN tac_monto_autorizado DROP NOT NULL;



-- 1.9.0
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_LIQUIDACION','F72',  'Crear liquidación', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_LIQUIDACION','F73',  'Actualizar liquidación', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_LIQUIDACION','F74',  'Eliminar liquidación', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_LIQUIDACION','FB33',  'Buscar liquidación', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_LIQUIDACION','FM22',  'Menú de liquidación', 12, true, null, null,0);


-- Liquidaciones
CREATE SEQUENCE IF NOT EXISTS finanzas.sg_liquidaciones_liq_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;

create table if not exists finanzas.sg_liquidaciones (
	liq_pk bigint not null default nextval('finanzas.sg_liquidaciones_liq_pk_seq'::regclass),
	liq_componente_pk bigint not null,
	liq_sede_pk bigint not null,
	liq_anio_pk bigint not null,
	liq_estado varchar(30) not null,
	liq_ult_mod_fecha timestamp without time zone,
	liq_ult_mod_usuario character varying(45),
	liq_version integer,
	constraint sg_liquidaciones_pkey primary key (liq_pk));


COMMENT ON TABLE finanzas.sg_liquidaciones IS 'Tabla para el registro de liquidaciones.';
COMMENT ON COLUMN finanzas.sg_liquidaciones.liq_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN finanzas.sg_liquidaciones.liq_componente_pk IS 'Número de referencia del componente del registro.';
COMMENT ON COLUMN finanzas.sg_liquidaciones.liq_sede_pk IS 'Número de referencia de la sede del registro.';
COMMENT ON COLUMN finanzas.sg_liquidaciones.liq_anio_pk IS 'Número de referencia del anio del registro.';
COMMENT ON COLUMN finanzas.sg_liquidaciones.liq_estado IS 'Indica el estado del registro';
COMMENT ON COLUMN finanzas.sg_liquidaciones.liq_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN finanzas.sg_liquidaciones.liq_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN finanzas.sg_liquidaciones.liq_version IS 'Versión del registro.';


create table if not exists finanzas.sg_liquidaciones_aud (
	liq_pk bigint not null,
	liq_componente_pk bigint,
	liq_sede_pk bigint,
	liq_anio_pk bigint,
	liq_estado varchar(30),
	liq_ult_mod_fecha timestamp without time zone,
	liq_ult_mod_usuario character varying(45),
	liq_version integer,
	rev bigint,
	revtype smallint,
	CONSTRAINT sg_desembolsos_ced_audpkey PRIMARY KEY (liq_pk,rev), 
    CONSTRAINT sg_desembolsos_ced_aud_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev));



-- COLUMNA DE DESEMBOLSO EN MOVIMIENTO DE CUENTA BANCARIA DEPARTAMENTAL
ALTER TABLE finanzas.sg_movimiento_cuenta_bancaria ADD COLUMN mcb_estado_liquidacion varchar(30);
ALTER TABLE finanzas.sg_movimiento_cuenta_bancaria_aud ADD COLUMN mcb_estado_liquidacion varchar(30);

COMMENT ON COLUMN finanzas.sg_movimiento_cuenta_bancaria.mcb_estado_liquidacion IS 'Indica el estado de liquidación del registro.';


-- CREANDO COLUMNA ATORIZACION PRESUPUESTOS
ALTER TABLE finanzas.sg_presupuesto_escolar ADD pes_edicion boolean;
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar.pes_edicion IS 'Bandera que muestras si aplica a Edición';

-- CREANDO COLUMNA ATORIZACION PRESUPUESTOS AUD
ALTER TABLE finanzas.sg_presupuesto_escolar_aud ADD pes_edicion boolean;

-- OPERACION AUTORIZAR EDICION PRESUPUESTOS
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) 
VALUES ('AUTORIZAR_EDICION_PRESUPUESTO','FP7',  'Permite Autorizar a Editar el Presupuesto Escolar', 12, true, null, null,0);



-- TABLA AUTORIZACION EDICION PRESUPUESTOS
CREATE SEQUENCE IF NOT EXISTS finanzas.sg_autorizacion_edicion_presupuestos_fk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;

create table if not exists finanzas.sg_autorizacion_edicion_presupuestos (
	aut_pk bigint not null default nextval('finanzas.sg_autorizacion_edicion_presupuestos_fk_seq'::regclass),
	aut_presupuesto_fk bigint not null,
	aut_usuario_validado_fk bigint not null,
	aut_ult_mod_fecha timestamp without time zone,
	aut_ult_mod_usuario character varying(45),
	aut_version integer,
	constraint sg_sg_autorizacion_edicion_presupuestos_pkey primary key (aut_pk));

COMMENT ON TABLE finanzas.sg_autorizacion_edicion_presupuestos IS 'Tabla para el registro de desembolsos a centros educativos.';
COMMENT ON COLUMN finanzas.sg_autorizacion_edicion_presupuestos.aut_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN finanzas.sg_autorizacion_edicion_presupuestos.aut_presupuesto_fk IS 'Número de referencia al presupuesto del registro.';
COMMENT ON COLUMN finanzas.sg_autorizacion_edicion_presupuestos.aut_usuario_validado_fk IS 'Número de referencia al usuario validado del registro.';
COMMENT ON COLUMN finanzas.sg_autorizacion_edicion_presupuestos.aut_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN finanzas.sg_autorizacion_edicion_presupuestos.aut_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN finanzas.sg_autorizacion_edicion_presupuestos.aut_version IS 'Versión del registro.';


-- TABLA AUTORIZACION EDICION PRESUPUESTOS_AUD

create table if not exists finanzas.sg_autorizacion_edicion_presupuestos_aud (
	aut_pk bigint not null,
	aut_presupuesto_fk bigint,
	aut_usuario_validado_fk bigint,
	aut_ult_mod_fecha timestamp without time zone,
	aut_ult_mod_usuario character varying(45),
	aut_version integer,
	rev bigint,
	revtype smallint,
	CONSTRAINT sg_autorizacion_edicion_presupuestos_aud_pkey PRIMARY KEY (aut_pk,rev), 
    CONSTRAINT sg_autorizacion_edicion_presupuestosd_aud_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev)
);

COMMENT ON TABLE finanzas.sg_autorizacion_edicion_presupuestos_aud IS 'Tabla de auditoria para el registro de las atorizaciones de Edicion Presupuesto.';




-- OPERACION BUSCAR AUTORIZACION EDICION DE PRESUPUESTOS
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) 
VALUES ('BUSCAR_AUTORIZACION_EDICION_PRESUPUESTO','FB34',  'Busca Autorizaciones para la edición de presupuestos', 12, true, null, null,0);

-- OPERACIONES CRUD AUTORIZACION EDICION DE PRESUPUESTOS
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) 
VALUES ('CREAR_AUTORIZACION_EDICION_PRESUPUESTO','F75',  'Crea autorización para editar presupuestos aprobados', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) 
VALUES ('ACTUALIZAR_AUTORIZACION_EDICION_PRESUPUESTO','F76',  'Actualiza autorización edición presupuestos', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) 
VALUES ('ELIMINAR_AUTORIZACION_EDICION_PRESUPUESTO','F77',  'Elimina autorización de edción presupuestos', 12, true, null, null,0);

ALTER TABLE finanzas.sg_presupuesto_escolar ALTER COLUMN pes_edicion SET DEFAULT false;

ALTER TABLE finanzas.sg_movimiento_caja_chica RENAME TO sg_cajas_chicas;
ALTER TABLE finanzas.sg_movimiento_caja_chica_aud RENAME TO sg_cajas_chicas_aud;

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_MOVIMIENTO_CUENTA_BANCARIA','F78',  'Actualiza un movimiento de la cuenta bancaria ', 12, true, null, null,0);


-- 1.11.0

-- RELACION CON DETALLE DE DESEMBOLSOS
ALTER TABLE finanzas.sg_desembolsos_ced DROP CONSTRAINT dce_det_desembolso_fkey;

ALTER TABLE finanzas.sg_desembolsos_ced ADD CONSTRAINT dce_det_desembolso_fkey
FOREIGN KEY (dce_det_desembolso_fk) REFERENCES finanzas.sg_detalle_desembolsos(ded_pk) MATCH FULL ON DELETE CASCADE ON UPDATE CASCADE;

-- RELACION CON REQUERIMIENTO DE FONDO CED
ALTER TABLE finanzas.sg_desembolsos_ced DROP CONSTRAINT dce_req_fondo_ced_fkey;

ALTER TABLE finanzas.sg_desembolsos_ced ADD CONSTRAINT dce_req_fondo_ced_fkey
FOREIGN KEY (dce_req_fondo_ced_fk) REFERENCES finanzas.sg_req_fond_ced(rfc_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;


ALTER TABLE finanzas.sg_movimiento_cuenta_bancaria DROP CONSTRAINT mcb_desembolso_ced_fkey;

ALTER TABLE finanzas.sg_movimiento_cuenta_bancaria ADD CONSTRAINT mcb_desembolso_ced_fkey
FOREIGN KEY (mcb_desembolso_ced_fk) REFERENCES finanzas.sg_desembolsos_ced(dce_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;


ALTER TABLE finanzas.sg_cajas_chicas RENAME TO sg_movimiento_caja_chica;
ALTER TABLE finanzas.sg_cajas_chicas_aud RENAME TO sg_movimiento_caja_chica_aud;

ALTER TABLE finanzas.sg_cuentas_bancarias_cc RENAME TO sg_cajas_chicas;
ALTER TABLE finanzas.sg_cuentas_bancarias_cc_aud RENAME TO sg_cajas_chicas_aud;



-- Movimientos liquidacion
CREATE SEQUENCE IF NOT EXISTS finanzas.sg_movimientos_liquidacion_mlq_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;

create table if not exists finanzas.sg_movimientos_liquidacion (
        mlq_pk bigint not null default nextval('finanzas.sg_movimientos_liquidacion_mlq_pk_seq'::regclass),
	mlq_movimiento_pk bigint not null,
	mlq_liquidacion_pk bigint not null,
	mlq_evaluado boolean,
	mlq_comentario character varying(4000),
	mlq_ult_mod_fecha timestamp without time zone,
	mlq_ult_mod_usuario character varying(45),
	mlq_version integer,
	constraint sg_movimientos_liquidacion_pkey primary key (mlq_pk));

COMMENT ON TABLE finanzas.sg_movimientos_liquidacion IS 'Tabla para el registro de movimientos liquidacion.';
COMMENT ON COLUMN finanzas.sg_movimientos_liquidacion.mlq_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN finanzas.sg_movimientos_liquidacion.mlq_movimiento_pk IS 'Número de referencia al movimiento del registro';
COMMENT ON COLUMN finanzas.sg_movimientos_liquidacion.mlq_liquidacion_pk IS 'Número de referencia de la liquidación';
COMMENT ON COLUMN finanzas.sg_movimientos_liquidacion.mlq_evaluado IS 'Indica si el registro se correcto en la liquidacion (true) o incorrecto(false).';
COMMENT ON COLUMN finanzas.sg_movimientos_liquidacion.mlq_comentario IS 'Comentario ';
COMMENT ON COLUMN finanzas.sg_movimientos_liquidacion.mlq_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN finanzas.sg_movimientos_liquidacion.mlq_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN finanzas.sg_movimientos_liquidacion.mlq_version IS 'Versión del registro.';


create table if not exists finanzas.sg_movimientos_liquidacion_aud (
	mlq_pk bigint not null,
	mlq_movimiento_pk bigint,
	mlq_liquidacion_pk bigint,
	mlq_evaluado boolean,
	mlq_comentario character varying(4000),
	mlq_ult_mod_fecha timestamp without time zone,
	mlq_ult_mod_usuario character varying(45),
	mlq_version integer,
	rev bigint,
	revtype smallint,
	CONSTRAINT sg_movimientos_liquidacion_aud_pkey PRIMARY KEY (mlq_pk,rev), 
        CONSTRAINT sg_movimientos_liquidacion_aud_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev)
);



-- COLUMNA DE DESEMBOLSO EN MOVIMIENTO DE CUENTA BANCARIA DEPARTAMENTAL
ALTER TABLE finanzas.sg_movimientos_liquidacion ADD COLUMN mlq_tipo_movimiento varchar(1);
ALTER TABLE finanzas.sg_movimientos_liquidacion_aud ADD COLUMN mlq_tipo_movimiento varchar(1);

COMMENT ON COLUMN finanzas.sg_movimientos_liquidacion.mlq_tipo_movimiento IS 'Indica el tipo de movimiento del registro.';


-- FUNCION PARA VALIDAR EL NUMERO DE MOVIMIENTO EN INGRESO DE UNA TRANSFERENCIA 
CREATE OR REPLACE FUNCTION finanzas.validar_numero_movimiento_transferencia()
 RETURNS trigger
 LANGUAGE plpgsql
AS $function$
declare
numero_movimiento bigint;
begin
	-- VALIDO QUE EL MOVIMIENTO SEA DE UNA TRANSFERENCIA Y QUE ESTE ASIGNADA A PRESUPUESTO
    IF new.mov_origen = 'T' and new.mov_presupuesto_fk is not null then    
    -- VARIFICO EL NUMERO DE MOVIMIENTO MAXIMO GUARDADO EN LA TABLA.
 
    numero_movimiento:= (SELECT MAX(mov_num_movimiento) from finanzas.sg_presupuesto_escolar_movimiento 
    where mov_tipo= 'I' AND mov_presupuesto_fk = new.mov_presupuesto_fk );

     			-- SI viene menor es porq ya se encuentra , asi que se incrementa 1
 			IF new.mov_num_movimiento <= numero_movimiento then
 			
				--REALIZO EL UPDATE 
				UPDATE finanzas.sg_presupuesto_escolar_movimiento
					SET mov_num_movimiento= (numero_movimiento + 1)
				WHERE mov_pk = new.mov_pk; 			
 			END IF;
    END IF;
    RETURN new;
END;
$function$;


-- TRIGGER AFTER INSERT TRANSFERENCIA

CREATE TRIGGER validar_numero_movimiento_transferencia
AFTER insert 
ON finanzas.sg_presupuesto_escolar_movimiento FOR EACH ROW
EXECUTE PROCEDURE finanzas.validar_numero_movimiento_transferencia();



INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_MOVIMIENTOS_LIQUIDACION','FB35',  'Busca Movimientos Liquidacion', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_MOVIMIENTOS_LIQUIDACION','F79',  'Crea los movimientos de liquidación', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_MOVIMIENTOS_LIQUIDACION','F80',  'Actualiza los movimientos de liquidación', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_MOVIMIENTOS_LIQUIDACION','F81',  'Elimina los movimientos de liquidación', 12, true, null, null,0);

-- 1.12.0
-- AGREGANDO VALIDACION DE NIT UNICO EN PROVEEDORES
ALTER TABLE siap2.ss_proveedor
ADD CONSTRAINT nit_unique UNIQUE (pro_nit);

-- SE COLOCA POR DEFECTO TRUE CONSIDERANDO INGRESOS DESDE SIAP2
ALTER TABLE siap2.ss_proveedor ALTER COLUMN proveedor_mined SET DEFAULT true;

-- SETANDO VALOR TRUE A PROVEEDORES DE SIAP CON COLUMNA PROVEEDOR_MINED NULL.
UPDATE siap2.ss_proveedor
SET  proveedor_mined= true 
WHERE pro_id  in (select pro_id from siap2.ss_proveedor 
where proveedor_mined is null);

-- 1.13.0

ALTER TABLE finanzas.sg_movimientos_liquidacion ADD CONSTRAINT mlq_movimiento_fkey
FOREIGN KEY (mlq_movimiento_pk) REFERENCES finanzas.sg_movimiento_cuenta_bancaria(mcb_pk) MATCH FULL ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE finanzas.sg_movimientos_liquidacion ADD CONSTRAINT mlq_liquidacion_fkey
FOREIGN KEY (mlq_liquidacion_pk) REFERENCES finanzas.sg_liquidaciones(liq_pk) MATCH FULL ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE finanzas.sg_liquidaciones ADD COLUMN liq_sub_componente_fk bigint not null;
COMMENT ON COLUMN finanzas.sg_liquidaciones.liq_sub_componente_fk IS 'Número de referencia del subcomponente del registro';

ALTER TABLE finanzas.sg_liquidaciones_aud ADD COLUMN liq_sub_componente_fk bigint;



-- Liquidaciones otros ingresos
CREATE SEQUENCE IF NOT EXISTS finanzas.sg_liquidaciones_otros_ingresos_loi_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;

create table if not exists finanzas.sg_liquidaciones_otros_ingresos (
        loi_pk bigint not null default nextval('finanzas.sg_liquidaciones_otros_ingresos_loi_pk_seq'::regclass),
	loi_sede_fk bigint not null,
	loi_anio_fk bigint not null,
	loi_estado varchar(30) not null,
	loi_ult_mod_fecha timestamp without time zone,
	loi_ult_mod_usuario character varying(45),
	loi_version integer,
	constraint sg_liquidaciones_otros_ingresos_pkey primary key (loi_pk));


COMMENT ON TABLE finanzas.sg_liquidaciones_otros_ingresos IS 'Tabla para el registro de liquidaciones otros ingresos.';
COMMENT ON COLUMN finanzas.sg_liquidaciones_otros_ingresos.loi_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN finanzas.sg_liquidaciones_otros_ingresos.loi_sede_fk IS 'Número de referencia de la sede del registro.';
COMMENT ON COLUMN finanzas.sg_liquidaciones_otros_ingresos.loi_anio_fk IS 'Número de referencia del año del registro.';
COMMENT ON COLUMN finanzas.sg_liquidaciones_otros_ingresos.loi_estado IS 'Estado del registro';
COMMENT ON COLUMN finanzas.sg_liquidaciones_otros_ingresos.loi_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN finanzas.sg_liquidaciones_otros_ingresos.loi_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN finanzas.sg_liquidaciones_otros_ingresos.loi_version IS 'Versión del registro.';

create table if not exists finanzas.sg_liquidaciones_otros_ingresos_aud (
        loi_pk bigint not null,
	loi_sede_fk bigint,
	loi_anio_fk bigint,
	loi_estado varchar(30),
	loi_ult_mod_fecha timestamp without time zone,
	loi_ult_mod_usuario character varying(45),
	loi_version integer,
	rev bigint,
	revtype smallint,
        CONSTRAINT sg_liquidaciones_otros_ingresos_aud_pkey PRIMARY KEY (loi_pk,rev), 
        CONSTRAINT sg_liquidaciones_otros_ingresos_aud_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev)
);



-- Movimientos liquidaciones de otros ingresos
CREATE SEQUENCE IF NOT EXISTS finanzas.sg_movimientos_liquidacion_otros_mlo_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;

create table if not exists finanzas.sg_movimientos_liquidacion_otros (
        mlo_pk bigint not null default nextval('finanzas.sg_movimientos_liquidacion_otros_mlo_pk_seq'::regclass),
	mlo_movimiento_fk bigint not null,
	mlo_liquidacion_otro_fk bigint not null,
	mlo_evaluado boolean,
	mlo_comentario character varying(4000),
	mlo_ult_mod_fecha timestamp without time zone,
	mlo_ult_mod_usuario character varying(45),
	mlo_version integer,
	constraint sg_movimientos_liquidacion_otros_pkey primary key (mlo_pk));

COMMENT ON TABLE finanzas.sg_movimientos_liquidacion_otros IS 'Tabla para el registro de movimientos liquidaciones de otros ingresos.';
COMMENT ON COLUMN finanzas.sg_movimientos_liquidacion_otros.mlo_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN finanzas.sg_movimientos_liquidacion_otros.mlo_movimiento_fk IS 'Número de referencia del movimiento de cuenta bancaria del registro.';
COMMENT ON COLUMN finanzas.sg_movimientos_liquidacion_otros.mlo_liquidacion_otro_fk IS 'Número de referencia de la liquidacion de otro ingreso del registro.';
COMMENT ON COLUMN finanzas.sg_movimientos_liquidacion_otros.mlo_comentario IS 'Comentario de la evaluación del registro';
COMMENT ON COLUMN finanzas.sg_movimientos_liquidacion_otros.mlo_evaluado IS 'Indica si el registro se encuentra correcto(true) o no correcto(false).';
COMMENT ON COLUMN finanzas.sg_movimientos_liquidacion_otros.mlo_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN finanzas.sg_movimientos_liquidacion_otros.mlo_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN finanzas.sg_movimientos_liquidacion_otros.mlo_version IS 'Versión del registro.';

create table if not exists finanzas.sg_movimientos_liquidacion_otros_aud (
        mlo_pk bigint not null,
	mlo_movimiento_fk bigint,
	mlo_liquidacion_otro_fk bigint,
	mlo_evaluado boolean,
	mlo_comentario character varying(4000),
	mlo_ult_mod_fecha timestamp without time zone,
	mlo_ult_mod_usuario character varying(45),
	mlo_version integer,
	rev bigint,
	revtype smallint,
        CONSTRAINT sg_movimientos_liquidacion_otros_aud_pkey  PRIMARY KEY (mlo_pk,rev),
        CONSTRAINT sg_movimientos_liquidacion_otros_aud_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev)
);


-- COLUMNA DE DESEMBOLSO EN MOVIMIENTO DE CUENTA BANCARIA DEPARTAMENTAL
ALTER TABLE finanzas.sg_movimientos_liquidacion_otros ADD COLUMN mlo_tipo_movimiento varchar(1);
ALTER TABLE finanzas.sg_movimientos_liquidacion_otros_aud ADD COLUMN mlo_tipo_movimiento varchar(1);

COMMENT ON COLUMN finanzas.sg_movimientos_liquidacion_otros.mlo_tipo_movimiento IS 'Indica el tipo de movimiento del registro.';


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_LIQUIDACION_OTRO_INGRESO','F82',  'Crear liquidación de otros ingresos', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_LIQUIDACION_OTRO_INGRESO','F83',  'Actualizar liquidación de otros ingresos', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_LIQUIDACION_OTRO_INGRESO','F84',  'Eliminar liquidación de otros ingresos', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_MOVIMIENTOS_LIQUIDACION_OTROS','F85',  'Crea los movimientos de liquidación de otros ingresos', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_MOVIMIENTOS_LIQUIDACION_OTROS','F86',  'Actualiza los movimientos de liquidación de otros ingresos', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_MOVIMIENTOS_LIQUIDACION_OTROS','F87',  'Elimina los movimientos de liquidación de otros ingresos', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_LIQUIDACION_OTRO_INGRESO','FB36',  'Buscar liquidación de otros ingresos', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_MOVIMIENTOS_LIQUIDACION_OTROS','FB37',  'Busca Movimientos Liquidacion de otros ingresos', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_LIQUIDACION_OTROS_INGRESOS','FM23',  'Menú de liquidación de otros ingresos', 12, true, null, null,0);


-- Evaluaciones de Liquidaciones
CREATE SEQUENCE IF NOT EXISTS finanzas.sg_evaluaciones_liquidaciones_elq_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;

create table if not exists finanzas.sg_evaluaciones_liquidaciones (
        elq_pk bigint not null default nextval('finanzas.sg_evaluaciones_liquidaciones_elq_pk_seq'::regclass),
        elq_liquidacion_fk bigint not null,
	elq_reembolso_monto decimal,
	elq_reembolso_cheque character varying(50),
	elq_reintegro_monto decimal,
	elq_comentario character varying(4000),
	elq_ult_mod_fecha timestamp without time zone,
	elq_ult_mod_usuario character varying(45),
	elq_version integer,
	constraint sg_evaluaciones_liquidaciones_pkey primary key (elq_pk));


COMMENT ON TABLE finanzas.sg_evaluaciones_liquidaciones IS 'Tabla para el registro de evaluaciones de liquidaciones.';
COMMENT ON COLUMN finanzas.sg_evaluaciones_liquidaciones.elq_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN finanzas.sg_evaluaciones_liquidaciones.elq_liquidacion_fk IS 'Número de referencia de la liquidacion del registro.';
COMMENT ON COLUMN finanzas.sg_evaluaciones_liquidaciones.elq_reembolso_monto IS 'Monto del reembolso del registro.';
COMMENT ON COLUMN finanzas.sg_evaluaciones_liquidaciones.elq_reembolso_cheque IS 'Número de cheque del reembolso del registro';
COMMENT ON COLUMN finanzas.sg_evaluaciones_liquidaciones.elq_reintegro_monto IS 'Monto del reintegro del registro';
COMMENT ON COLUMN finanzas.sg_evaluaciones_liquidaciones.elq_comentario IS 'Comentario del registro';
COMMENT ON COLUMN finanzas.sg_evaluaciones_liquidaciones.elq_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN finanzas.sg_evaluaciones_liquidaciones.elq_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN finanzas.sg_evaluaciones_liquidaciones.elq_version IS 'Versión del registro.';

create table if not exists finanzas.sg_evaluaciones_liquidaciones_aud (
        elq_pk bigint not null,
        elq_liquidacion_fk bigint,
	elq_reembolso_monto decimal,
	elq_reembolso_cheque character varying(50),
	elq_reintegro_monto decimal,
	elq_comentario character varying(4000),
	elq_ult_mod_fecha timestamp without time zone,
	elq_ult_mod_usuario character varying(45),
	elq_version integer,
	rev bigint,
	revtype smallint,
        CONSTRAINT sg_evaluaciones_liquidaciones_aud_pkey PRIMARY KEY (elq_pk, rev),
        CONSTRAINT sg_evaluaciones_liquidaciones_aud_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev)
);


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_EVALUACION_LIQUIDACION','FB38',  'Buscar evaluacion liquidacion', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_EVALUACION_LIQUIDACION','F88',  '', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_EVALUACION_LIQUIDACION','F89',  '', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_EVALUACION_LIQUIDACION','F90',  '', 12, true, null, null,0);




ALTER TABLE finanzas.sg_movimientos_liquidacion_otros ADD CONSTRAINT mlo_movimiento_fkey
FOREIGN KEY (mlo_movimiento_fk) REFERENCES finanzas.sg_movimiento_cuenta_bancaria(mcb_pk) MATCH FULL ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE finanzas.sg_movimientos_liquidacion_otros ADD CONSTRAINT mlo_liquidacion_otro_fkey
FOREIGN KEY (mlo_liquidacion_otro_fk) REFERENCES finanzas.sg_liquidaciones_otros_ingresos(loi_pk) MATCH FULL ON DELETE CASCADE ON UPDATE CASCADE;

-- QUERY PARA ASIGNAR AREAS DE INVERSION. 
UPDATE finanzas.sg_presupuesto_escolar_movimiento
set mov_area_inversion_pk = subquery.padre_id
from (select padre.ai_id padre_id from siap2.ss_areas_inversion hija
inner join siap2.ss_areas_inversion padre on hija.ai_area_padre=padre.ai_id) as subquery
where mov_area_inversion_pk  is null and mov_tipo = 'E';


-- SETEANDO FALSE CHEQUE COBRADO
ALTER TABLE finanzas.sg_movimiento_cuenta_bancaria ALTER COLUMN mcb_cheque_cobrado SET DEFAULT false;



-- 1.13.6

-- OPERACION VISTA MENU CHEQUERAS
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) 
VALUES ('MENU_GESTION_CHEQUERAS','FM24',  'Menú para la gestión de chequeras', 12, true, null, null,0);


-- OPERACIONES CRUD CHEQUERAS
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_CHEQUERA','F91',  'Crea chequera', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_CHEQUERA','F92',  'Actualiza chequera', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_CHEQUERA','F93',  'Elimina Chequer', 12, true, null, null,0);




--CREACION DE TABLA PARA LA GESTIÒN DE FACTURAS
CREATE SEQUENCE IF NOT EXISTS finanzas.sg_chequeras_che_pk_seq 
    INCREMENT 1 
    START 1 
    MINVALUE 1 
    MAXVALUE 9223372036854775807 
    CACHE 1     
    NO CYCLE;



--CREACION DE TABLA PARA LA GESTIÒN DE CHEQUERAS
CREATE TABLE IF NOT EXISTS finanzas.sg_chequeras (
    che_pk bigint NOT NULL DEFAULT nextval('finanzas.sg_chequeras_che_pk_seq'::regclass), 
    che_cuenta_bancaria_fk bigint,    
    che_fecha_chequera timestamp without time zone,
    che_serie character varying(12),   
    che_numero_inicial character varying(12),
    che_numero_final character varying(12),
    che_ult_mod_fecha timestamp without time zone, 
    che_ult_mod_usuario character varying(45), 
    che_version integer, 
    CONSTRAINT chequera_pkey PRIMARY KEY (che_pk), 
    CONSTRAINT che_serie_uk UNIQUE (che_serie),
    CONSTRAINT che_numero_inicial_uk UNIQUE (che_numero_inicial),
    CONSTRAINT che_numero_final_uk UNIQUE (che_numero_final));   
   
COMMENT ON TABLE finanzas.sg_chequeras IS 'Tabla para el registro de Chequeras.';
COMMENT ON COLUMN finanzas.sg_chequeras.che_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN finanzas.sg_chequeras.che_cuenta_bancaria_fk IS 'Indica el valor de la cuenta bancaria a la que esta relacionada la chequera.';
COMMENT ON COLUMN finanzas.sg_chequeras.che_fecha_chequera IS 'Indica la fecha registro.';
COMMENT ON COLUMN finanzas.sg_chequeras.che_serie IS 'Indica la serie de la chequera.';
COMMENT ON COLUMN finanzas.sg_chequeras.che_numero_inicial IS 'Indica la serie inicial.';
COMMENT ON COLUMN finanzas.sg_chequeras.che_numero_final IS 'Indica la serie final.';
COMMENT ON COLUMN finanzas.sg_chequeras.che_ult_mod_fecha IS 'Última fehca de modificación del registro.';
COMMENT ON COLUMN finanzas.sg_chequeras.che_ult_mod_usuario IS 'Último usuario que modifico el registro.';
COMMENT ON COLUMN finanzas.sg_chequeras.che_version IS 'Versión del registro.';


--CREACION DE TABLA PARA LA GESTIÒN DE FACTURAS AUD
CREATE TABLE IF NOT EXISTS finanzas.sg_chequeras_aud (
    che_pk bigint not null, 
   	che_cuenta_bancaria_fk bigint,
    che_fecha_chequera timestamp without time zone,
    che_serie character varying(12),
    che_numero_inicial character varying(12),
    che_numero_final character varying(12),
    che_ult_mod_fecha timestamp without time zone, 
    che_ult_mod_usuario character varying(45), 
    che_version integer,  
    rev bigint, 
    revtype smallint, 
    CONSTRAINT sg_chequeras_aud_pkey PRIMARY KEY (che_pk,rev), 
    CONSTRAINT sg_chequeras_aud_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev)
);


-- OPERACION PARA LA BUSQUEDA DE CHEQUERAS
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_CHERQUERA','FB39',  'Busca chequeras', 12, true, null, null,0);


-- 1.14.0

--CREACION DE TABLA PARA LA GESTIÒN DE LOS ENCABEZADOS DE PLANES DE COMPRA DEL PRESUPUESTO ESCOLAR
CREATE SEQUENCE IF NOT EXISTS finanzas.sg_encabezado_plan_compra_pk_seq 
    INCREMENT 1 
    START 1 
    MINVALUE 1 
    MAXVALUE 9223372036854775807 
    CACHE 1     
    NO CYCLE;

CREATE TABLE IF NOT EXISTS finanzas.sg_encabezado_plan_compra (
    pla_pk bigint NOT NULL DEFAULT nextval('finanzas.sg_encabezado_plan_compra_pk_seq'::regclass),   
    pla_presupuesto_fk integer,
    pla_estado character varying(45),
    pla_comentario character varying(8000),
    pla_ult_mod_fecha timestamp without time zone, 
    pla_ult_mod_usuario character varying(45), 
    pla_version integer, 
    
    CONSTRAINT sg_encabezado_plan_compra_pkey PRIMARY KEY (pla_pk) 
    
);
COMMENT ON TABLE finanzas.sg_encabezado_plan_compra IS 'Tabla para el registro de Plan de Compras';
COMMENT ON COLUMN finanzas.sg_encabezado_plan_compra.pla_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN finanzas.sg_encabezado_plan_compra.pla_presupuesto_fk IS 'Código del presupuesto relacionado al registro.';
COMMENT ON COLUMN finanzas.sg_encabezado_plan_compra.pla_estado IS 'Indica el estado del Plan de Compra';
COMMENT ON COLUMN finanzas.sg_encabezado_plan_compra.pla_comentario IS 'Muestra comentarios del registro';
COMMENT ON COLUMN finanzas.sg_encabezado_plan_compra.pla_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN finanzas.sg_encabezado_plan_compra.pla_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN finanzas.sg_encabezado_plan_compra.pla_version IS 'Versión del registro.';

--- TABLA DE AUDITORIA TABLA PARA LA GESTIÒN DE LOS ENCABEZADOS DE PLANES DE COMPRA DEL PRESUPUESTO ESCOLAR
CREATE TABLE IF NOT EXISTS finanzas.sg_encabezado_plan_compra_aud (
    pla_pk bigint not null, 
 	pla_presupuesto_fk integer,
    pla_estado character varying(45),
    pla_comentario character varying(8000),
    pla_ult_mod_fecha timestamp without time zone, 
    pla_ult_mod_usuario character varying(45), 
    pla_version integer,  
    rev bigint, 
    revtype smallint, 
    CONSTRAINT sg_encabezado_plan_compra_aud_pkey PRIMARY KEY (pla_pk,rev), 
    CONSTRAINT sg_encabezado_plan_compra_aud_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev)
);

-- OPERACION QUE PERMITE OBSERVAR PLAN DE COMPRAS
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('OBSERVAR_PLAN_COMPRAS','FP8',  'Permite cambiar estado Observado', 12, true, null, null,0);

-- OPERACION QUE PERMITE APROBAR PLAN DE COMPRAS
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('APROBAR_PLAN_COMPRAS','FP9',  'Permite cambiar estado Aprobado', 12, true, null, null,0);

-- OPERACION QUE PERMITE AJUSTAR PLAN DE COMPRAS
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('AJUSTE_PLAN_COMPRAS','FP10',  'Permite cambiar estado En ajuste', 12, true, null, null,0);


-- SE AGREGA COLUMNA PARA VALIDAR CONCILIACION BANCARIA
ALTER TABLE finanzas.sg_movimiento_cuenta_bancaria ADD mcb_aplica_conciliacion boolean NULL DEFAULT true;
COMMENT ON COLUMN finanzas.sg_movimiento_cuenta_bancaria.mcb_aplica_conciliacion IS 'Columna para validar Si aplica o no a conciliación bancaria.';
-- SE AGREGA COLUMNA PARA VALIDAR CONCILIACION BANCARIA AUD
ALTER TABLE finanzas.sg_movimiento_cuenta_bancaria_aud ADD mcb_aplica_conciliacion boolean NULL DEFAULT true;

-- RELACION DE MOVIMIENTOS PRESUPUESTO CON INGRESOS CUENTAS BANCARIAS SEDES
ALTER TABLE finanzas.sg_transferencia_direccion_departamental ADD CONSTRAINT tdd_direccion_dep_fkey FOREIGN KEY (tdd_direccion_dep_fk) 
REFERENCES finanzas.finanzas.sg_direcciones_departamentales (mov_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

-- RESTRICCION DE NIT EN PAGADURIAS
ALTER TABLE finanzas.sg_direcciones_departamentales
ADD CONSTRAINT nit_pagaduria_unique UNIQUE (ded_nit);


-- RELACION DE PAGADURIAS CON CUENTAS DEPARTAMENTALES 
ALTER TABLE finanzas.sg_cuenta_bancaria_dd ADD CONSTRAINT cbd_dde_fk_fkey FOREIGN KEY (cbd_dde_fk) 
REFERENCES finanzas.sg_direcciones_departamentales(ded_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

-- RESTRICCION DE NUMERO DE CUENTA BANCARIA
ALTER TABLE finanzas.sg_cuentas_bancarias
ADD CONSTRAINT cbc_numero_cuenta_uk UNIQUE (cbc_numero_cuenta);

-- 1.17.0
-- Evaluación Items de Liquidacion
CREATE SEQUENCE IF NOT EXISTS finanzas.sg_evaluacion_liquidacion_items_eil_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;

create table if not exists finanzas.sg_evaluacion_liquidacion_items (
	eil_pk bigint not null default nextval('finanzas.sg_evaluacion_liquidacion_items_eil_pk_seq'::regclass),
	eil_liq_fk bigint not null,
	eil_item_fk bigint not null,
	eil_marcado boolean not null,
	eil_ult_mod_fecha timestamp without time zone,
	eil_ult_mod_usuario character varying(45),
	eil_version integer,
	constraint sg_evaluacion_liquidacion_items_pkey primary key (eil_pk));

ALTER TABLE finanzas.sg_evaluacion_liquidacion_items ADD CONSTRAINT eil_liq_fkey FOREIGN KEY (eil_liq_fk) 
REFERENCES finanzas.sg_liquidaciones(liq_pk) MATCH FULL ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE finanzas.sg_evaluacion_liquidacion_items ADD CONSTRAINT eil_item_fkey FOREIGN KEY (eil_item_fk) 
REFERENCES catalogo.sg_items_liquidacion(ili_pk) MATCH FULL ON DELETE CASCADE ON UPDATE CASCADE;

COMMENT ON TABLE finanzas.sg_evaluacion_liquidacion_items IS 'Tabla para el registro de evaluación items de liquidacion.';
COMMENT ON COLUMN finanzas.sg_evaluacion_liquidacion_items.eil_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN finanzas.sg_evaluacion_liquidacion_items.eil_liq_fk IS 'Número de referencia de liquidación del registro.';
COMMENT ON COLUMN finanzas.sg_evaluacion_liquidacion_items.eil_item_fk IS 'Número de referencia item de la liquidación del registro.';
COMMENT ON COLUMN finanzas.sg_evaluacion_liquidacion_items.eil_marcado IS 'Indica si el registro se encuentra marcado(true) o sin marcar(false).';
COMMENT ON COLUMN finanzas.sg_evaluacion_liquidacion_items.eil_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN finanzas.sg_evaluacion_liquidacion_items.eil_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN finanzas.sg_evaluacion_liquidacion_items.eil_version IS 'Versión del registro.';


create table if not exists finanzas.sg_evaluacion_liquidacion_items_aud (
	eil_pk bigint not null,
	eil_liq_fk bigint,
	eil_item_fk bigint,
	eil_marcado boolean,
	eil_ult_mod_fecha timestamp without time zone,
	eil_ult_mod_usuario character varying(45),
	eil_version integer,
	rev bigint,
	revtype smallint,
        CONSTRAINT sg_evaluacion_liquidacion_items_aud_pk PRIMARY KEY (eil_pk, rev),
        CONSTRAINT sg_evaluacion_liquidacion_items_aud_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev)
);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_EVALUACION_ITEM_LIQUIDACION','F94',  '', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_EVALUACION_ITEM_LIQUIDACION','F95',  '', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_EVALUACION_ITEM_LIQUIDACION','F96',  '', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_EVALUACION_ITEM_LIQUIDACION','FB40',  '', 12, true, null, null,0);

ALTER TABLE finanzas.sg_presupuesto_escolar_movimiento ADD mov_editado bool NULL DEFAULT false;
COMMENT ON COLUMN finanzas.sg_presupuesto_escolar_movimiento.mov_editado IS 'Permite identificar si el registro fue editado';

-- 1.17.0
-- RESTRICCION DE NUMERO DE CUENTA BANCARIA DIRECCION DE PARTAMENTAL
ALTER TABLE finanzas.sg_cuenta_bancaria_dd
ADD CONSTRAINT cbd_numero_cuenta_uk UNIQUE (cbd_numero_cuenta);

-- MENU PLAN DE COMPRAS
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version)
VALUES ('MENU_PLAN_COMPRAS','FM25',  'Menú para la gestión de plan de de compras', 12, true, null, null,0);

-- MENU REPORTE LIBRO DE BANCO 
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) 
VALUES ('MENU_REPORTE_LIBRO_BANCO','FM26',  'Menú para reportes libro de banco', 12, true, null, null,0);


-- MENU REPORTE CAJAS CHICAS
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) 
VALUES ('MENU_REPORTE_LIBRO_CAJA_CHICA','FM27',  'Menú para reportes cajas chicas', 12, true, null, null,0);


-- OPERACIONES PARA MENU DE INGRESOS EGRESOS
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) 
VALUES ('MENU_REPORTE_INGRESOS_EGRESOS','FM28',  'Menú para la gestion de ingresos y egresos', 12, true, null, null,0);

INSERT INTO seguridad.sg_roles_operacion (rop_operacion_fk, rop_rol_fk, rop_cascada, rop_habilitado, rop_ult_mod_fecha, rop_ult_mod_usuario, rop_version) 
VALUES((select ope_pk from seguridad.sg_operaciones where ope_codigo='FM28'),'172', false, true, current_timestamp, 'casuser', 0);

INSERT INTO seguridad.sg_roles_operacion (rop_operacion_fk, rop_rol_fk, rop_cascada, rop_habilitado, rop_ult_mod_fecha, rop_ult_mod_usuario, rop_version) 
VALUES((select ope_pk from seguridad.sg_operaciones where ope_codigo='FM28'),'171', false, true, current_timestamp, 'casuser', 0);

-- OPERACIONES RESTARNTES POR USUARION CAJA CHICA Y CUENTA BANCARIA
INSERT INTO seguridad.sg_roles_operacion (rop_operacion_fk, rop_rol_fk, rop_cascada, rop_habilitado, rop_ult_mod_fecha, rop_ult_mod_usuario, rop_version) 
VALUES((select ope_pk from seguridad.sg_operaciones where ope_codigo='FM26'),'172', false, true, current_timestamp, 'casuser', 0);
INSERT INTO seguridad.sg_roles_operacion (rop_operacion_fk, rop_rol_fk, rop_cascada, rop_habilitado, rop_ult_mod_fecha, rop_ult_mod_usuario, rop_version) 
VALUES((select ope_pk from seguridad.sg_operaciones where ope_codigo='FM27'),'172', false, true, current_timestamp, 'casuser', 0);

INSERT INTO seguridad.sg_roles_operacion (rop_operacion_fk, rop_rol_fk, rop_cascada, rop_habilitado, rop_ult_mod_fecha, rop_ult_mod_usuario, rop_version) 
VALUES((select ope_pk from seguridad.sg_operaciones where ope_codigo='FM26'),'171', false, true, current_timestamp, 'casuser', 0);
INSERT INTO seguridad.sg_roles_operacion (rop_operacion_fk, rop_rol_fk, rop_cascada, rop_habilitado, rop_ult_mod_fecha, rop_ult_mod_usuario, rop_version) 
VALUES((select ope_pk from seguridad.sg_operaciones where ope_codigo='FM27'),'171', false, true, current_timestamp, 'casuser', 0);


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MODIFICAR_COMPROMISOS_PRESUPUESTARIOS','FP11',  'Permite modificar el Compromiso Pres. del Requerimiento', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CAMBIAR_REQ_ESTADO_ENVIAR_UFI','FP12',  'Permite cambiar Estado Ufi al Req.', 12, true, null, null,0);



ALTER TABLE finanzas.sg_movimiento_cuenta_bancaria ADD mcb_cheque_anulado bool NULL;

ALTER TABLE finanzas.sg_movimiento_cuenta_bancaria_aud ADD mcb_cheque_anulado bool NULL;


-- 1.20.0

-- Evaluación Liquidacion Otros
CREATE SEQUENCE IF NOT EXISTS finanzas.sg_evaluaciones_liquidaciones_otros_eli_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
create table if not exists finanzas.sg_evaluaciones_liquidaciones_otros (
        eli_pk bigint not null default nextval('finanzas.sg_evaluaciones_liquidaciones_otros_eli_pk_seq'::regclass),
	eli_liquidacion_otros_fk bigint,
	eli_reembolso_monto decimal,
	eli_reembolso_cheque character varying(50),
	eli_reintegro_monto decimal,
	eli_comentario character varying(4000),
	eli_ult_mod_fecha timestamp without time zone,
	eli_ult_mod_usuario character varying(45),
	eli_version integer,
	constraint sg_evaluaciones_liquidaciones_otros_pkey primary key (eli_pk)
);

ALTER TABLE finanzas.sg_evaluaciones_liquidaciones_otros ADD CONSTRAINT eli_liquidacion_otros_fkey FOREIGN KEY (eli_liquidacion_otros_fk) 
REFERENCES finanzas.sg_liquidaciones_otros_ingresos(loi_pk) MATCH FULL ON DELETE CASCADE ON UPDATE CASCADE;

COMMENT ON TABLE finanzas.sg_evaluaciones_liquidaciones_otros IS 'Tabla para el registro de evaluación liquidacion otros.';
COMMENT ON COLUMN finanzas.sg_evaluaciones_liquidaciones_otros.eli_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN finanzas.sg_evaluaciones_liquidaciones_otros.eli_liquidacion_otros_fk IS 'Número de referencia de la liquidacion de otros del registro.';
COMMENT ON COLUMN finanzas.sg_evaluaciones_liquidaciones_otros.eli_reembolso_monto IS 'Monto del reembolso del registro.';
COMMENT ON COLUMN finanzas.sg_evaluaciones_liquidaciones_otros.eli_reembolso_cheque IS 'Número de cheque del reembolso del registro.';
COMMENT ON COLUMN finanzas.sg_evaluaciones_liquidaciones_otros.eli_reintegro_monto IS 'Monto del reintegro del registro.';
COMMENT ON COLUMN finanzas.sg_evaluaciones_liquidaciones_otros.eli_comentario IS 'Comentario del registro.';
COMMENT ON COLUMN finanzas.sg_evaluaciones_liquidaciones_otros.eli_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN finanzas.sg_evaluaciones_liquidaciones_otros.eli_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN finanzas.sg_evaluaciones_liquidaciones_otros.eli_version IS 'Versión del registro.';

create table if not exists finanzas.sg_evaluaciones_liquidaciones_otros_aud (
        eli_pk bigint not null,
	eli_liquidacion_otros_fk bigint,
	eli_reembolso_monto decimal,
	eli_reembolso_cheque character varying(50),
	eli_reintegro_monto decimal,
	eli_comentario character varying(4000),
	eli_ult_mod_fecha timestamp without time zone,
	eli_ult_mod_usuario character varying(45),
	eli_version integer,
	rev bigint,
	revtype smallint
        CONSTRAINT sg_evaluaciones_liquidaciones_otros_aud_pk PRIMARY KEY (eli_pk, rev),
        CONSTRAINT sg_evaluaciones_liquidaciones_otros_aud_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev)
);


-- Evaluación Items de Liquidacion Otros ingresos
CREATE SEQUENCE IF NOT EXISTS finanzas.sg_evaluacion_liquidacion_otros_items_elo_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;

create table if not exists finanzas.sg_evaluacion_liquidacion_otros_items (
	elo_pk bigint not null default nextval('finanzas.sg_evaluacion_liquidacion_otros_items_elo_pk_seq'::regclass),
        elo_loi_fk bigint not null,
	elo_item_fk bigint not null,
	elo_marcado boolean not null,
	elo_ult_mod_fecha timestamp without time zone,
	elo_ult_mod_usuario character varying(45),
	elo_version integer,
	constraint sg_evaluacion_liquidacion_items_pkey primary key (elo_pk));

ALTER TABLE finanzas.sg_evaluacion_liquidacion_otros_items ADD CONSTRAINT elo_loi_fkey FOREIGN KEY (elo_loi_fk) 
REFERENCES finanzas.sg_liquidaciones_otros_ingresos(loi_pk) MATCH FULL ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE finanzas.sg_evaluacion_liquidacion_items ADD CONSTRAINT elo_item_fkey FOREIGN KEY (elo_item_fk) 
REFERENCES catalogo.sg_items_liquidacion(ili_pk) MATCH FULL ON DELETE CASCADE ON UPDATE CASCADE;

COMMENT ON TABLE finanzas.sg_evaluacion_liquidacion_items IS 'Tabla para el registro de evaluación items de liquidacion.';
COMMENT ON COLUMN finanzas.sg_evaluacion_liquidacion_items.elo_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN finanzas.sg_evaluacion_liquidacion_items.elo_loi_fk IS 'Número de referencia de liquidación de otros ingresos del registro.';
COMMENT ON COLUMN finanzas.sg_evaluacion_liquidacion_items.elo_item_fk IS 'Número de referencia item de la liquidación del registro.';
COMMENT ON COLUMN finanzas.sg_evaluacion_liquidacion_items.elo_marcado IS 'Indica si el registro se encuentra marcado(true) o sin marcar(false).';
COMMENT ON COLUMN finanzas.sg_evaluacion_liquidacion_items.elo_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN finanzas.sg_evaluacion_liquidacion_items.elo_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN finanzas.sg_evaluacion_liquidacion_items.elo_version IS 'Versión del registro.';


create table if not exists finanzas.sg_evaluacion_liquidacion_otros_items_aud (
	elo_pk bigint not null,
	elo_loi_fk bigint,
	elo_item_fk bigint,
	elo_marcado boolean,
	elo_ult_mod_fecha timestamp without time zone,
	elo_ult_mod_usuario character varying(45),
	elo_version integer,
	rev bigint,
	revtype smallint,
        CONSTRAINT sg_evaluacion_liquidacion_otros_items_aud_pk PRIMARY KEY (elo_pk, rev),
        CONSTRAINT sg_evaluacion_liquidacion_otros_items_aud_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev)
);



INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_EVALUACION_LIQUIDACION_OTRO','FB41',  'Buscar evaluacion liquidacion otros', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_EVALUACION_ITEM_LIQUIDACION_OTRO','FB42',  'Buscar evaluacion items liquidacion otros', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_EVALUACION_LIQUIDACION_OTRO','F97',  '', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_EVALUACION_LIQUIDACION_OTRO','F98',  '', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_EVALUACION_LIQUIDACION_OTRO','F99',  '', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_EVALUACION_ITEM_LIQUIDACION_OTRO','F100',  '', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_EVALUACION_ITEM_LIQUIDACION_OTRO','F101',  '', 12, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_EVALUACION_ITEM_LIQUIDACION_OTRO','F102',  '', 12, true, null, null,0);


-- CREANDO COLUMNA PARA REGISTRO DE PROVEEDOR EN CUENTA BANCARIA
ALTER TABLE finanzas.sg_movimiento_cuenta_bancaria ADD mcb_proveedor varchar(500) NULL;
ALTER TABLE finanzas.sg_movimiento_cuenta_bancaria_aud ADD mcb_proveedor varchar(500) NULL;


-- CAMBIANDO COLUMNA CHEQUE A INT		
ALTER TABLE finanzas.sg_pagos ALTER COLUMN pgs_numero_cheque TYPE int8 USING pgs_numero_cheque::int8;
ALTER TABLE finanzas.sg_pagos_aud ALTER COLUMN pgs_numero_cheque TYPE int8 USING pgs_numero_cheque::int8;



create or replace view finanzas.vwLibroBanco as     
select
    mov.mcb_cuenta_fk,
    mov.mcb_fecha,    
    case when mov.mcb_cheque_cb is null then ('N/A')
    else mov.mcb_cheque_cb end as mcb_cheque_cb ,    
    mov.mcb_detalle,
    case when tra.tac_ced_fk is null then (select sed_nombre from centros_educativos.sg_sedes where sed_pk=cu.cbc_sede_fk)
    else (select sed_nombre from centros_educativos.sg_sedes where sed_pk=cu.cbc_sede_fk) end as anombreDe,
    mov.mcb_tipo,
    mov.mcb_monto,
    mov.mcb_saldo,
    mov.mcb_pk,
    mov.mcb_ult_mod_fecha
from
    finanzas.sg_movimiento_cuenta_bancaria mov
inner join finanzas.sg_cuentas_bancarias cu on
    cu.cbc_pk = mov.mcb_cuenta_fk
left join finanzas.sg_desembolsos_ced dce on 
   mov.mcb_desembolso_ced_fk= dce.dce_pk
left join finanzas.sg_req_fond_ced rce on 
   dce.dce_req_fondo_ced_fk=rce.rfc_pk
left join finanzas.sg_transferencias_a_ced tra on 
  rce.rfc_transferencia_ced_fk=tra.tac_pk
left join finanzas.sg_presupuesto_escolar_movimiento movp on
  mov.mcb_mov_fuente_ingresos_fk=movp.mov_pk
left join finanzas.sg_presupuesto_escolar pre on
   movp.mov_presupuesto_fk=pre.pes_pk
where mcb_tipo='HABER'
union all 
select
    mov.mcb_cuenta_fk,
    mov.mcb_fecha,
    mov.mcb_cheque_cb,
    mov.mcb_detalle,
    pv.pro_nombre as anombreDe,
    mov.mcb_tipo ,
    mov.mcb_monto,
    mov.mcb_saldo,
    mov.mcb_pk,
    mov.mcb_ult_mod_fecha
from
    finanzas.sg_movimiento_cuenta_bancaria mov
inner join finanzas.sg_cuentas_bancarias cu on
    cu.cbc_pk = mov.mcb_cuenta_fk
left outer join finanzas.sg_pagos pa on
    pa.pgs_movimiento_cb_fk = mov.mcb_pk
left outer join finanzas.sg_facturas fa on
    fa.fac_pk = pa.pgs_factura_fk
left outer join siap2.ss_proveedor pv on
    pv.pro_id = fa.fac_proveedor_fk
where mcb_tipo='DEBE' and mcb_cheque_anulado is null;


create or replace view  finanzas.vwLibroCajaChica as   
select
    mov.mcc_cuenta_fk ,
    mov.mcc_fecha ,
    sed.sed_nombre ,
    mov.mcc_detalle ,    
    mov.mcc_tipo ,
    mov.mcc_monto ,
    mov.mcc_saldo ,
    mov.mcc_pk ,
    mov.mcc_ult_mod_fecha 
from
    finanzas.sg_movimiento_caja_chica mov
inner join finanzas.sg_cajas_chicas cu on
    cu.bcc_pk = mov.mcc_cuenta_fk 
    inner join centros_educativos.sg_sedes sed on sed.sed_pk = cu.bcc_sede_fk 
where mcc_tipo ='HABER'
union all 
select
    mov.mcc_cuenta_fk ,
    mov.mcc_fecha,
    mov.mcc_detalle ,
    pv.pro_nombre as anombreDe,
    mov.mcc_tipo ,
    mov.mcc_monto ,
    mov.mcc_saldo ,
    mov.mcc_pk ,
    mov.mcc_ult_mod_fecha 
from
    finanzas.sg_movimiento_caja_chica mov
inner join finanzas.sg_cajas_chicas cu on
    cu.bcc_pk = mov.mcc_cuenta_fk 
left outer join finanzas.sg_pagos pa on
    pa.pgs_movimiento_cc_fk = mov.mcc_pk 
left outer join finanzas.sg_facturas fa on
    fa.fac_pk = pa.pgs_factura_fk
left outer join siap2.ss_proveedor pv on
    pv.pro_id = fa.fac_proveedor_fk
where mcc_tipo ='DEBE';




create or replace view finanzas.vwDesembolsoNoDepositados as
select tced.tac_pk,req.rfc_pk,sed.sed_pk,sed.sed_codigo,sed.sed_nombre,sed.sed_habilitado, 
CASE WHEN rec.rec_pk IS NULL then false else true end as recibo,
CASE WHEN oae.oae_miembros_vigentes IS NULL then false else oae.oae_miembros_vigentes end as oae_miembros_vigentes, 
CASE WHEN oae.oae_miembros_vigentes IS NULL then NULL else oae.oae_fecha_vencimiento end as oae_fecha_vencimiento, 
CASE WHEN (select doc_pk from finanzas.sg_documentos_presupuesto where doc_presupuesto_fk=pre.pes_pk and doc_tipo_documento='CONVENIO') IS NULL  
then 'PENDIENTE' else (select doc_estado_documento from finanzas.sg_documentos_presupuesto where doc_presupuesto_fk=pre.pes_pk and doc_tipo_documento='CONVENIO') end as convenio, 
CASE WHEN (select doc_pk from finanzas.sg_documentos_presupuesto where doc_presupuesto_fk=pre.pes_pk and doc_tipo_documento='CONGELACION') IS NULL  
then 'PENDIENTE' else (select doc_estado_documento from finanzas.sg_documentos_presupuesto where doc_presupuesto_fk=pre.pes_pk and doc_tipo_documento='CONGELACION') end as ccf,
req.rfc_monto,
CASE WHEN (select ROUND(sum(dce_monto),2) from finanzas.sg_desembolsos_ced where dce_req_fondo_ced_fk=req.rfc_pk) is null 
then 0.00 else (select ROUND(sum(dce_monto),2) from finanzas.sg_desembolsos_ced where dce_req_fondo_ced_fk=req.rfc_pk) end as monto_desembolsado,
rfd.str_pk,fue.fue_id
from finanzas.sg_req_fond_ced req
inner join finanzas.sg_requerimientos_fondo rfd on req.rfc_sol_transferencia_fk=rfd.str_pk
inner join finanzas.sg_transferencias_a_ced tced on req.rfc_transferencia_ced_fk=tced.tac_pk
inner join siap2.ss_transferencias_componente tco on tced.tac_transferencia_fk=tco.tc_id
inner join siap2.ss_fuente_recursos fue on tco.tc_fuente_recursos_fk=fue.fue_id
inner join centros_educativos.sg_sedes sed on tced.tac_ced_fk=sed.sed_pk
left join centros_educativos.sg_organismo_administracion_escolar oae on sed.sed_pk=oae.oae_sede_fk and oae.oae_estado='APROBADO' 
left join finanzas.sg_presupuesto_escolar pre on pre.pes_sede_fk=tced.tac_ced_fk and pre.pes_estado='APROBADO'
left join finanzas.sg_recibos rec on tced.tac_pk=rec.rec_transferencia_fk;


-- BORRADO DE RESTRICCIONES EN CHEQUERA
ALTER TABLE finanzas.sg_chequeras DROP CONSTRAINT che_serie_uk;
ALTER TABLE finanzas.sg_chequeras DROP CONSTRAINT che_numero_inicial_uk;
ALTER TABLE finanzas.sg_chequeras DROP CONSTRAINT che_numero_final_uk;



select pes.pes_codigo , pes.pes_nombre from finanzas.sg_presupuesto_escolar_movimiento spem
inner join finanzas.sg_presupuesto_escolar pes on pes.pes_pk = spem.mov_presupuesto_fk
where mov_origen = 'T' and mov_monto_aprobado = 0  and pes_estado = 'EN_PROCESO';



select ai_aplica_plan_compras, ai_nombre , ai_id from siap2.ss_areas_inversion where ai_nombre like '%Material Gastable (sub)%'


ALTER TABLE finanzas.sg_chequeras DROP CONSTRAINT che_serie_uk;
ALTER TABLE finanzas.sg_chequeras DROP CONSTRAINT che_numero_inicial_uk;
ALTER TABLE finanzas.sg_chequeras DROP CONSTRAINT che_numero_final_uk;

--- 1.23.0
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_CUENTAS_BANCARIAS_DDE','FM29',  '', 12, true, null, null,0);
-- OPERACION ACTUALIZAR CAMPOS PROVEEDOR 
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CAMBIAR_DATOS_PROVEEDOR','FP13',  'Permite modificar los datos de nombre y nit de proveedores', 12, true, null, null,0);

-- OPERACIONES PARA OPERACION DE ACTUALIZAR CAMPOS PROVEEDOR POR ROLES DE USUARIOS
-- ROL SEDE
INSERT INTO seguridad.sg_roles_operacion (rop_operacion_fk, rop_rol_fk, rop_cascada, rop_habilitado, rop_ult_mod_fecha, rop_ult_mod_usuario, rop_version) VALUES((select ope_pk from seguridad.sg_operaciones where ope_codigo='FP13'),'172', false, true, current_timestamp, 'casuser', 0);
-- ROL DIRECCION DEPARTAMENTAL
INSERT INTO seguridad.sg_roles_operacion (rop_operacion_fk, rop_rol_fk, rop_cascada, rop_habilitado, rop_ult_mod_fecha, rop_ult_mod_usuario, rop_version) VALUES((select ope_pk from seguridad.sg_operaciones where ope_codigo='FP13'),'171', false, true, current_timestamp, 'casuser', 0);






-- 1.24.0
update finanzas.sg_cuentas_bancarias set cbc_banco_fk=19 where cbc_banco_fk=21;
update finanzas.sg_cuentas_bancarias set cbc_banco_fk=19 where cbc_banco_fk=17;
update finanzas.sg_cuentas_bancarias set cbc_banco_fk=19 where cbc_banco_fk=18;
update finanzas.sg_cuentas_bancarias set cbc_banco_fk=19 where cbc_banco_fk=20;
update finanzas.sg_cuentas_bancarias set cbc_banco_fk=19 where cbc_banco_fk=16;

alter table finanzas.sg_cuentas_bancarias add constraint cbc_cuentas_bancarias_bancos_fk foreign key (cbc_banco_fk) references catalogo.sg_bancos (bnc_pk);

update finanzas.sg_cuentas_bancarias set cbc_numero_cuenta=CONCAT('XXX','-',cbc_pk) where cbc_numero_cuenta like '%XXX%';

select * from finanzas.sg_cuentas_bancarias where cbc_numero_cuenta='123'

ALTER TABLE finanzas.sg_cuentas_bancarias
ADD CONSTRAINT num_cuenta_unique UNIQUE (cbc_numero_cuenta);


-- CAMBIANDO COLUMNA CHEQUE A INT		
ALTER TABLE finanzas.sg_cuentas_bancarias ADD COLUMN cbc_otro_ingreso  boolean;
ALTER TABLE finanzas.sg_cuentas_bancarias_aud ADD COLUMN cbc_otro_ingreso  boolean;
COMMENT ON COLUMN finanzas.sg_cuentas_bancarias.cbc_otro_ingreso IS 'Indica si el registro es  de otro ingreso marcado(true).';

-- OPERACION BUSQUEDA PROVEEDORES PARA EDICION EN PROVEEDORES
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_PROVEEDOR','FB43',  'Buscar Proveedores', 12, true, null, null,0);

-- COLUMNA PARA EL REGISTRO DE LA CHEQUERA EN PAGOS
ALTER TABLE finanzas.sg_pagos ADD pgs_chequera_fk int8 NULL;
COMMENT ON COLUMN finanzas.sg_pagos.pgs_chequera_fk IS 'Id de la chequera a la que pertenece el cheque';

-- COLUMNA PARA EL REGISTRO DE LA CHEQUERA EN PAGOS AUD
ALTER TABLE finanzas.sg_pagos_aud ADD pgs_chequera_fk int8 NULL;

-- RELACION PAGOS CON CHEQUERAS
alter table finanzas.sg_pagos add constraint sg_pagos_chequera_fk foreign key (pgs_chequera_fk) references finanzas.sg_chequeras (che_pk);


-- CAMBIANDO COLUMNA CHEQUE A INT		
ALTER TABLE finanzas.sg_movimientos_liquidacion ADD COLUMN mlq_reintegro decimal;
ALTER TABLE finanzas.sg_movimientos_liquidacion_aud ADD COLUMN mlq_reintegro decimal;
COMMENT ON COLUMN finanzas.sg_movimientos_liquidacion.mlq_reintegro IS 'Monto de reintegro del registro.';

