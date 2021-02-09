CREATE SCHEMA IF NOT EXISTS infraestructuras;

update seguridad.sg_operaciones set ope_codigo='INF01' where ope_nombre='CREAR_INMUEBLE_O_TERRENO';
update seguridad.sg_operaciones set ope_codigo='INF02' where ope_nombre='ACTUALIZAR_INMUEBLE_O_TERRENO';
update seguridad.sg_operaciones set ope_codigo='INF03' where ope_nombre='ELIMINAR_INMUEBLE_O_TERRENO';
update seguridad.sg_operaciones set ope_codigo='INF04' where ope_nombre='BUSCAR_INMUEBLE_O_TERRENO';

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_INMUEBLES_O_TERRENOS','EM54',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_INMUEBLE_O_TERRENO','INF01',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_INMUEBLE_O_TERRENO','INF02',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_INMUEBLE_O_TERRENO','INF03',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_INMUEBLE_O_TERRENO','INF04',  '', 1, true, null, null,0);

CREATE SEQUENCE infraestructuras.sg_inmuebles_sedes_pk_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
    NO CYCLE;


CREATE TABLE infraestructuras.sg_inmuebles_sedes (
	tis_pk int8 NOT NULL, -- Número correlativo autogenerado.
        tis_codigo varchar(10) NOT NULL, -- Codigo del registro
	tis_propietario bool NULL, -- Indica si el mined es propietario(true) o no(false).
	tis_area_total numeric(10,2) NULL, -- Indica el area total en metros cuadrados.
	tis_area_disponible numeric(10,2) NULL, -- Indica el area en metros cuadrados que no ha esta construida.
	tis_observaciones varchar(1000) NULL, -- Observaciones.
	tis_terreno_principal bool NULL, -- Indica si es el terreno principal de la sede.
	tis_sede_fk int8 NULL, -- LLave foranea de la Sede.
        tis_direccion_fk int8 NULL, --LLave foranea de la Dirección.
	tis_ult_mod_fecha timestamp NULL, -- Última fecha en la que se modificó el registro.
	tis_ult_mod_usuario varchar(45) NULL, -- Último usuario que modificó el registro.
	tis_version int2 NULL, -- Versión del registro
	CONSTRAINT inmuebles_sedes_pk PRIMARY KEY (tis_pk),
	CONSTRAINT tis_codigo_uk UNIQUE (tis_codigo),
	CONSTRAINT sg_inmuebles_sedes_fk FOREIGN KEY (tis_sede_fk) REFERENCES centros_educativos.sg_sedes(sed_pk),
	CONSTRAINT tis_direccion_fk FOREIGN KEY (tis_direccion_fk) REFERENCES centros_educativos.sg_direcciones(dir_pk)
);
CREATE INDEX sg_inmuebles_sedes_tis_sede_fk_idx ON infraestructuras.sg_inmuebles_sedes USING btree (tis_sede_fk);

-- Column comments

COMMENT ON COLUMN infraestructuras.sg_inmuebles_sedes.tis_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN infraestructuras.sg_inmuebles_sedes.tis_codigo IS 'Código del registro.';
COMMENT ON COLUMN infraestructuras.sg_inmuebles_sedes.tis_propietario IS 'Indica si el mined es propietario(true) o no(false).';
COMMENT ON COLUMN infraestructuras.sg_inmuebles_sedes.tis_area_total IS 'Indica el area total en metros cuadrados.';
COMMENT ON COLUMN infraestructuras.sg_inmuebles_sedes.tis_area_disponible IS 'Indica el area en metros cuadrados que no ha esta construida.';
COMMENT ON COLUMN infraestructuras.sg_inmuebles_sedes.tis_observaciones IS 'Observaciones.';
COMMENT ON COLUMN infraestructuras.sg_inmuebles_sedes.tis_terreno_principal IS 'Indica si es el terreno principal de la sede.';
COMMENT ON COLUMN infraestructuras.sg_inmuebles_sedes.tis_sede_fk IS 'LLave foranea de la Sede.';
COMMENT ON COLUMN infraestructuras.sg_inmuebles_sedes.tis_direccion_fk IS 'LLave foranea de la Dirección.';
COMMENT ON COLUMN infraestructuras.sg_inmuebles_sedes.tis_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN infraestructuras.sg_inmuebles_sedes.tis_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN infraestructuras.sg_inmuebles_sedes.tis_version IS 'Versión del registro';

ALTER TABLE infraestructuras.sg_inmuebles_sedes ALTER tis_pk SET DEFAULT nextval('infraestructuras.sg_inmuebles_sedes_pk_seq');

CREATE TABLE infraestructuras.sg_inmuebles_sedes_aud (
	tis_pk int8 NOT NULL,
        tis_codigo varchar(10) NOT NULL,
	tis_propietario bool NULL,
	tis_area_total numeric(10,2) NULL,
	tis_area_disponible numeric(10,2) NULL, 
	tis_observaciones varchar(1000) NULL, 
	tis_terreno_principal bool NULL, 
	tis_sede_fk int8 NULL,
        tis_direccion_fk int8 NULL,
	tis_ult_mod_fecha timestamp NULL, 
	tis_ult_mod_usuario varchar(45) NULL, 
	tis_version int2 NULL,
	rev int8 NOT NULL,
	revtype int2 NULL,
	CONSTRAINT sg_inmuebles_sedes_aud_pkey PRIMARY KEY (tis_pk, rev)
);

CREATE SEQUENCE infraestructuras.sg_inmuebles_vulnerabilidades_pk_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
    NO CYCLE;



--VULNERABILIDADES DE INMUEBLES
CREATE TABLE infraestructuras.sg_inmuebles_vulnerabilidades (
	ivu_pk bigserial NOT NULL, -- Número correlativo autogenerado.
	ivu_inmuebles_sedes_fk int8 NULL, -- LLave foranea de Inlueble Sedes.
	ivu_vulnerabilidad_fk int8 NULL, -- LLave Foranea de Vulnerabildiades.
	ivu_ult_mod_fecha timestamp NULL, -- Última fecha en la que se modificó el registro.
	ivu_ult_mod_usuario varchar(45) NULL, -- Último usuario que modificó el registro.
	ivu_version int2 NULL, -- Versión del registro
	CONSTRAINT sg_inmuebles_vulnerabilidades_pk PRIMARY KEY (ivu_pk),
	CONSTRAINT inmuebles_sedes_fk FOREIGN KEY (ivu_inmuebles_sedes_fk) REFERENCES infraestructuras.sg_inmuebles_sedes(tis_pk),
	CONSTRAINT vulnerabilidad_fk FOREIGN KEY (ivu_vulnerabilidad_fk) references catalogo.sg_vulnerabilidades(vul_pk)
);

-- Column comments
COMMENT ON TABLE infraestructuras.sg_inmuebles_vulnerabilidades IS 'Tabla para el registro de vulnerabilidades.';
COMMENT ON COLUMN infraestructuras.sg_inmuebles_vulnerabilidades.ivu_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN infraestructuras.sg_inmuebles_vulnerabilidades.ivu_inmuebles_sedes_fk IS 'Llave foránea de Inmueble sedes.';
COMMENT ON COLUMN infraestructuras.sg_inmuebles_vulnerabilidades.ivu_vulnerabilidad_fk IS 'Llave foránea de vulnerabildiades.';
COMMENT ON COLUMN infraestructuras.sg_inmuebles_vulnerabilidades.ivu_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN infraestructuras.sg_inmuebles_vulnerabilidades.ivu_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN infraestructuras.sg_inmuebles_vulnerabilidades.ivu_version IS 'Versión del registro';


ALTER TABLE infraestructuras.sg_inmuebles_vulnerabilidades ALTER ivu_pk SET DEFAULT nextval('infraestructuras.sg_inmuebles_vulnerabilidades_pk_seq');

CREATE TABLE infraestructuras.sg_inmuebles_vulnerabilidades_aud (
	ivu_pk int8 NOT NULL,
	ivu_inmuebles_sedes_fk int8 NULL,
	ivu_vulnerabilidad_fk int8 NULL,
	ivu_ult_mod_fecha timestamp NULL,
	ivu_ult_mod_usuario varchar(45) NULL,
	ivu_version int2 NULL,
	rev int8 NOT NULL,
    revtype int2 NULL,
    CONSTRAINT sg_inmuebles_vulnerabilidades_aud_pkey PRIMARY KEY (ivu_pk, rev)
);

ALTER TABLE infraestructuras.sg_inmuebles_sedes ADD COLUMN tis_estado_inmueble_fk bigint;
COMMENT ON COLUMN infraestructuras.sg_inmuebles_sedes.tis_estado_inmueble_fk IS 'Clave foránea a la fórmula';
ALTER TABLE infraestructuras.sg_inmuebles_sedes_aud ADD COLUMN tis_estado_inmueble_fk bigint;
ALTER TABLE infraestructuras.sg_inmuebles_sedes ADD CONSTRAINT sg_tis_estado_inmueble_fk FOREIGN KEY (tis_estado_inmueble_fk) REFERENCES catalogo.sg_estado_inmueble(ein_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;



--RELACION INMUEBLE Y ESPACIO COMUN
CREATE SEQUENCE infraestructuras.sg_rel_inmueble_espacio_comun_pk_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
    NO CYCLE;


CREATE TABLE infraestructuras.sg_rel_inmueble_espacio_comun (
	rie_pk int8 NOT NULL, -- Número correlativo autogenerado.  
        rie_inmueble_sede_fk bigint,
        rie_espacio_comun_fk bigint,
        rie_cantidad numeric,
	rie_ult_mod_fecha timestamp NULL, -- Última fecha en la que se modificó el registro.
	rie_ult_mod_usuario varchar(45) NULL, -- Último usuario que modificó el registro.
	rie_version int2 NULL, -- Versión del registro
	CONSTRAINT rel_inmueble_espacio_comun_pk PRIMARY KEY (rie_pk),
    CONSTRAINT sg_rie_inmueble_sede_fk FOREIGN KEY (rie_inmueble_sede_fk) REFERENCES infraestructuras.sg_inmuebles_sedes(tis_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT sg_rie_espacio_comun_fk FOREIGN KEY (rie_espacio_comun_fk) REFERENCES catalogo.sg_espacios_comunes(eco_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE
      
	
);

-- Column comments

COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_espacio_comun.rie_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_espacio_comun.rie_inmueble_sede_fk IS 'Relación con inmueble sede.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_espacio_comun.rie_espacio_comun_fk IS 'Releción con espacio común.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_espacio_comun.rie_cantidad IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_espacio_comun.rie_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_espacio_comun.rie_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_espacio_comun.rie_version IS 'Versión del registro';

ALTER TABLE infraestructuras.sg_rel_inmueble_espacio_comun ALTER rie_pk SET DEFAULT nextval('infraestructuras.sg_rel_inmueble_espacio_comun_pk_seq');

CREATE TABLE infraestructuras.sg_rel_inmueble_espacio_comun_aud (
	rie_pk int8 NOT NULL,
    rie_inmueble_sede_fk bigint,
    rie_espacio_comun_fk bigint,
    rie_cantidad numeric,
	rie_ult_mod_fecha timestamp NULL, 
	rie_ult_mod_usuario varchar(45) NULL, 
	rie_version int2 NULL,
	rev int8 NOT NULL,
	revtype int2 NULL,
	CONSTRAINT sg_rel_inmueble_espacio_comun_aud_pkey PRIMARY KEY (rie_pk, rev)
);


--RELACION ENTRE INMUEBLE Y SERVICIO
CREATE SEQUENCE infraestructuras.sg_rel_inmueble_servicio_pk_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
    NO CYCLE;


CREATE TABLE infraestructuras.sg_rel_inmueble_servicio (
	ris_pk int8 NOT NULL, -- Número correlativo autogenerado.  
        ris_inmueble_sede_fk bigint,
        ris_servicio_fk bigint,
	ris_ult_mod_fecha timestamp NULL, -- Última fecha en la que se modificó el registro.
	ris_ult_mod_usuario varchar(45) NULL, -- Último usuario que modificó el registro.
	ris_version int2 NULL, -- Versión del registro
	CONSTRAINT sg_ris_inmueble_servicio_pk PRIMARY KEY (ris_pk),
    CONSTRAINT sg_ris_inmueble_sede_fk FOREIGN KEY (ris_inmueble_sede_fk) REFERENCES infraestructuras.sg_inmuebles_sedes(tis_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT sg_ris_servicio_fk FOREIGN KEY (ris_servicio_fk) REFERENCES catalogo.sg_servicios_basicos(sba_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE
      
	
);

-- Column comments

COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_servicio.ris_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_servicio.ris_inmueble_sede_fk IS 'Relación con inmueble sede.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_servicio.ris_servicio_fk IS 'Releción con espacio común.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_servicio.ris_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_servicio.ris_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_servicio.ris_version IS 'Versión del registro';

ALTER TABLE infraestructuras.sg_rel_inmueble_servicio ALTER ris_pk SET DEFAULT nextval('infraestructuras.sg_rel_inmueble_servicio_pk_seq');

CREATE TABLE infraestructuras.sg_rel_inmueble_servicio_aud (
	ris_pk int8 NOT NULL,
        ris_inmueble_sede_fk bigint,
        ris_servicio_fk bigint,
	ris_ult_mod_fecha timestamp NULL, 
	ris_ult_mod_usuario varchar(45) NULL, 
	ris_version int2 NULL,
	rev int8 NOT NULL,
	revtype int2 NULL,
	CONSTRAINT sg_rel_inmueble_servicio_aud_pkey PRIMARY KEY (ris_pk, rev)
);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_REL_INMUEBLE_ESPACIO_COMUN','INF05',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_REL_INMUEBLE_ESPACIO_COMUN','INF06',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_REL_INMUEBLE_ESPACIO_COMUN','INF07',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_REL_INMUEBLE_SERVICIO_BASICO','INF08',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_REL_INMUEBLE_SERVICIO_BASICO','INF09',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_REL_INMUEBLE_SERVICIO_BASICO','INF10',  '', 1, true, null, null,0);


ALTER TABLE infraestructuras.sg_inmuebles_sedes ADD COLUMN tis_propietario_terreno_fk bigint;
COMMENT ON COLUMN infraestructuras.sg_inmuebles_sedes.tis_propietario_terreno_fk IS 'Propietario de terreno';
ALTER TABLE infraestructuras.sg_inmuebles_sedes_aud ADD COLUMN tis_propietario_terreno_fk bigint;
ALTER TABLE infraestructuras.sg_inmuebles_sedes ADD CONSTRAINT sg_tis_propietario_terreno_fk FOREIGN KEY (tis_propietario_terreno_fk) REFERENCES catalogo.sg_propietarios_terreno(pdt_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE infraestructuras.sg_inmuebles_sedes ADD COLUMN tis_detalle_propietario varchar(200); 
COMMENT ON COLUMN infraestructuras.sg_inmuebles_sedes.tis_detalle_propietario IS 'Detalle propietario';
ALTER TABLE infraestructuras.sg_inmuebles_sedes_aud ADD COLUMN tis_detalle_propietario varchar(200) ;



ALTER TABLE infraestructuras.sg_inmuebles_sedes ADD COLUMN tis_fuente_financiamiento_fk bigint;
COMMENT ON COLUMN infraestructuras.sg_inmuebles_sedes.tis_fuente_financiamiento_fk IS 'Fuente de financiamiento de registro';
ALTER TABLE infraestructuras.sg_inmuebles_sedes_aud ADD COLUMN tis_fuente_financiamiento_fk bigint;
ALTER TABLE infraestructuras.sg_inmuebles_sedes ADD CONSTRAINT sg_tis_fuente_financiamiento_fk FOREIGN KEY (tis_fuente_financiamiento_fk) REFERENCES catalogo.sg_fuente_financiamiento(ffi_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE infraestructuras.sg_inmuebles_sedes ADD COLUMN tis_valor_adquisicion numeric(10,2); 
COMMENT ON COLUMN infraestructuras.sg_inmuebles_sedes.tis_valor_adquisicion IS 'Valor adquisicion';
ALTER TABLE infraestructuras.sg_inmuebles_sedes_aud ADD COLUMN tis_valor_adquisicion numeric(10,2) ;

ALTER TABLE infraestructuras.sg_inmuebles_sedes ADD COLUMN tis_fecha_adquisicion DATE; 
COMMENT ON COLUMN infraestructuras.sg_inmuebles_sedes.tis_fecha_adquisicion IS 'Fecha adquisicion';
ALTER TABLE infraestructuras.sg_inmuebles_sedes_aud ADD COLUMN tis_fecha_adquisicion DATE ;

--1.1.0

update seguridad.sg_operaciones set ope_codigo='MINF01' where ope_nombre='MENU_INMUEBLES_O_TERRENOS';

-- Rel Inmueble Archivo
CREATE SEQUENCE IF NOT EXISTS infraestructuras.sg_rel_inmueble_archivo_ria_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS infraestructuras.sg_rel_inmueble_archivo (ria_pk bigint NOT NULL DEFAULT nextval('infraestructuras.sg_rel_inmueble_archivo_ria_pk_seq'::regclass),ria_inmueble_sede_fk bigint, ria_archivo_fk bigint, ria_ult_mod_fecha timestamp without time zone, ria_ult_mod_usuario character varying(45), ria_version integer, CONSTRAINT sg_rel_inmueble_archivo_pkey PRIMARY KEY (ria_pk),CONSTRAINT sg_ria_inmueble_sede_fk FOREIGN KEY (ria_inmueble_sede_fk) REFERENCES infraestructuras.sg_inmuebles_sedes(tis_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,CONSTRAINT sg_ria_archivo_fk FOREIGN KEY (ria_archivo_fk) REFERENCES sg_archivos(ach_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
COMMENT ON TABLE infraestructuras.sg_rel_inmueble_archivo IS 'Tabla para el registro de rel inmueble archivo.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_archivo.ria_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_archivo.ria_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_archivo.ria_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_archivo.ria_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS infraestructuras.sg_rel_inmueble_archivo_aud (ria_pk bigint NOT NULL,ria_inmueble_sede_fk bigint, ria_archivo_fk bigint, ria_ult_mod_fecha timestamp without time zone, ria_ult_mod_usuario character varying(45), ria_version integer, rev bigint, revtype smallint);
ALTER TABLE infraestructuras.sg_rel_inmueble_archivo_aud ADD PRIMARY KEY (ria_pk, rev);


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_REL_INMUEBLE_ARCHIVO','INF11',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_REL_INMUEBLE_ARCHIVO','INF12',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_REL_INMUEBLE_ARCHIVO','INF13',  '', 1, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('TIENE_ACCESO_INFRAESTRUCTURA','W7',  '', 4, true, null, null,0);


-- Edificios
CREATE SEQUENCE IF NOT EXISTS infraestructuras.sg_edificios_edi_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS infraestructuras.sg_edificios (edi_pk bigint NOT NULL DEFAULT nextval('infraestructuras.sg_edificios_edi_pk_seq'::regclass), edi_codigo character varying(10),  edi_nombre character varying(255),edi_nombre_busqueda character varying(255), edi_inmueble_sede_fk bigint,edi_tipo_construccion bigint,edi_inversion numeric(10,2), edi_cantidad_niveles numeric,edi_observaciones character varying(500), edi_area numeric(10,2) , edi_latitud numeric(11,9),edi_longitud numeric(11,9),edi_ult_mod_fecha timestamp without time zone, edi_ult_mod_usuario character varying(45), edi_version integer, CONSTRAINT sg_edificios_pkey PRIMARY KEY (edi_pk), CONSTRAINT edi_codigo_uk UNIQUE (edi_codigo),CONSTRAINT sg_edi_inmueble_sede_fk FOREIGN KEY (edi_inmueble_sede_fk) REFERENCES infraestructuras.sg_inmuebles_sedes(tis_pk),CONSTRAINT sg_edi_tipo_construccion_fk FOREIGN KEY (edi_tipo_construccion) REFERENCES catalogo.sg_tipo_construccion(tco_pk));
COMMENT ON TABLE infraestructuras.sg_edificios IS 'Tabla para el registro de edificios.';
COMMENT ON COLUMN infraestructuras.sg_edificios.edi_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN infraestructuras.sg_edificios.edi_codigo IS 'Código del registro.';
COMMENT ON COLUMN infraestructuras.sg_edificios.edi_inversion IS 'inversion del registro.';
COMMENT ON COLUMN infraestructuras.sg_edificios.edi_cantidad_niveles IS 'Cantidad de niveles.';
COMMENT ON COLUMN infraestructuras.sg_edificios.edi_observaciones IS 'Observaciones.';
COMMENT ON COLUMN infraestructuras.sg_edificios.edi_area IS 'Area.';
COMMENT ON COLUMN infraestructuras.sg_edificios.edi_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN infraestructuras.sg_edificios.edi_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN infraestructuras.sg_edificios.edi_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS infraestructuras.sg_edificios_aud (edi_pk bigint NOT NULL, edi_codigo character varying(45),  edi_nombre character varying(255), edi_nombre_busqueda character varying(255),edi_inmueble_sede_fk bigint,edi_tipo_construccion bigint,edi_inversion numeric(10,2), edi_cantidad_niveles numeric,edi_observaciones character varying(500), edi_area numeric(10,2) ,edi_latitud numeric(11,9),edi_longitud numeric(11,9), edi_ult_mod_fecha timestamp without time zone, edi_ult_mod_usuario character varying(45), edi_version integer, rev bigint, revtype smallint);
ALTER TABLE infraestructuras.sg_edificios_aud ADD PRIMARY KEY (edi_pk, rev);



-- Aulas
CREATE SEQUENCE IF NOT EXISTS infraestructuras.sg_aulas_aul_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS infraestructuras.sg_aulas (aul_pk bigint NOT NULL DEFAULT nextval('infraestructuras.sg_aulas_aul_pk_seq'::regclass), aul_codigo character varying(45),  aul_nombre character varying(255), aul_nombre_busqueda character varying(255),aul_nivel integer, aul_observaciones character varying(500), aul_edificio_fk bigint, aul_ult_mod_fecha timestamp without time zone, aul_ult_mod_usuario character varying(45), aul_version integer, CONSTRAINT sg_aulas_pkey PRIMARY KEY (aul_pk), CONSTRAINT aul_codigo_uk UNIQUE (aul_codigo), CONSTRAINT aul_nombre_uk UNIQUE (aul_nombre),CONSTRAINT sg_aul_edificio_fk FOREIGN KEY (aul_edificio_fk) REFERENCES infraestructuras.sg_edificios(edi_pk));
COMMENT ON TABLE infraestructuras.sg_aulas IS 'Tabla para el registro de aulas.';
COMMENT ON COLUMN infraestructuras.sg_aulas.aul_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN infraestructuras.sg_aulas.aul_codigo IS 'Código del registro.';
COMMENT ON COLUMN infraestructuras.sg_aulas.aul_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN infraestructuras.sg_aulas.aul_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN infraestructuras.sg_aulas.aul_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN infraestructuras.sg_aulas.aul_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN infraestructuras.sg_aulas.aul_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS infraestructuras.sg_aulas_aud (aul_pk bigint NOT NULL, aul_codigo character varying(45),  aul_nombre character varying(255), aul_nombre_busqueda character varying(255), aul_nivel integer, aul_observaciones character varying(500), aul_edificio_fk bigint,aul_ult_mod_fecha timestamp without time zone, aul_ult_mod_usuario character varying(45), aul_version integer, rev bigint, revtype smallint);
ALTER TABLE infraestructuras.sg_aulas_aud ADD PRIMARY KEY (aul_pk, rev);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_EDIFICIO','INF14',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_EDIFICIO','INF15',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_EDIFICIO','INF16',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_AULAS','INF17',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_AULAS','INF18',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_AULAS','INF19',  '', 1, true, null, null,0);


-- Rel Edificio Espacio
CREATE SEQUENCE IF NOT EXISTS infraestructuras.sg_rel_edificio_espacio_ree_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS infraestructuras.sg_rel_edificio_espacio (ree_pk bigint NOT NULL DEFAULT nextval('infraestructuras.sg_rel_edificio_espacio_ree_pk_seq'::regclass),ree_edificio_fk bigint,ree_espacio_comun_fk bigint,ree_cantidad numeric, ree_ult_mod_fecha timestamp without time zone, ree_ult_mod_usuario character varying(45), ree_version integer, CONSTRAINT sg_rel_edificio_espacio_pkey PRIMARY KEY (ree_pk),CONSTRAINT sg_ree_edificio_fk FOREIGN KEY (ree_edificio_fk) REFERENCES infraestructuras.sg_edificios(edi_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,CONSTRAINT sg_ree_espacio_comun_fk FOREIGN KEY (ree_espacio_comun_fk) REFERENCES catalogo.sg_espacios_comunes(eco_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
COMMENT ON TABLE infraestructuras.sg_rel_edificio_espacio IS 'Tabla para el registro de rel edificio espacio.';
COMMENT ON COLUMN infraestructuras.sg_rel_edificio_espacio.ree_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN infraestructuras.sg_rel_edificio_espacio.ree_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN infraestructuras.sg_rel_edificio_espacio.ree_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN infraestructuras.sg_rel_edificio_espacio.ree_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS infraestructuras.sg_rel_edificio_espacio_aud (ree_pk bigint NOT NULL,ree_edificio_fk bigint,ree_espacio_comun_fk bigint,ree_cantidad numeric, ree_ult_mod_fecha timestamp without time zone, ree_ult_mod_usuario character varying(45), ree_version integer, rev bigint, revtype smallint);
ALTER TABLE infraestructuras.sg_rel_edificio_espacio_aud ADD PRIMARY KEY (ree_pk, rev);

-- Rel Edificio Servicio
CREATE SEQUENCE IF NOT EXISTS infraestructuras.sg_rel_edificio_servicio_res_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS infraestructuras.sg_rel_edificio_servicio (res_pk bigint NOT NULL DEFAULT nextval('infraestructuras.sg_rel_edificio_servicio_res_pk_seq'::regclass),res_edificio_fk bigint,res_servicio_fk bigint, res_ult_mod_fecha timestamp without time zone, res_ult_mod_usuario character varying(45), res_version integer, CONSTRAINT sg_rel_edificio_servicio_pkey PRIMARY KEY (res_pk),CONSTRAINT sg_res_edificio_fk FOREIGN KEY (res_edificio_fk) REFERENCES infraestructuras.sg_edificios(edi_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,CONSTRAINT sg_res_servicio_fk FOREIGN KEY (res_servicio_fk) REFERENCES catalogo.sg_servicios_basicos(sba_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
COMMENT ON TABLE infraestructuras.sg_rel_edificio_servicio IS 'Tabla para el registro de rel edificio servicio.';
COMMENT ON COLUMN infraestructuras.sg_rel_edificio_servicio.res_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN infraestructuras.sg_rel_edificio_servicio.res_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN infraestructuras.sg_rel_edificio_servicio.res_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN infraestructuras.sg_rel_edificio_servicio.res_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS infraestructuras.sg_rel_edificio_servicio_aud (res_pk bigint NOT NULL,res_edificio_fk bigint,res_servicio_fk bigint,  res_ult_mod_fecha timestamp without time zone, res_ult_mod_usuario character varying(45), res_version integer, rev bigint, revtype smallint);
ALTER TABLE infraestructuras.sg_rel_edificio_servicio_aud ADD PRIMARY KEY (res_pk, rev);

-- Rel Aula Espacio
CREATE SEQUENCE IF NOT EXISTS infraestructuras.sg_rel_aula_espacio_rae_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS infraestructuras.sg_rel_aula_espacio (rae_pk bigint NOT NULL DEFAULT nextval('infraestructuras.sg_rel_aula_espacio_rae_pk_seq'::regclass),rae_aula_fk bigint,rae_espacio_comun_fk bigint,rae_cantidad numeric, rae_ult_mod_fecha timestamp without time zone, rae_ult_mod_usuario character varying(45), rae_version integer, CONSTRAINT sg_rae_aula_espacio_pkey PRIMARY KEY (rae_pk),CONSTRAINT sg_rae_aula_fk FOREIGN KEY (rae_aula_fk) REFERENCES infraestructuras.sg_aulas(aul_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,CONSTRAINT sg_rae_espacio_comun_fk FOREIGN KEY (rae_espacio_comun_fk) REFERENCES catalogo.sg_espacios_comunes(eco_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
COMMENT ON TABLE infraestructuras.sg_rel_aula_espacio IS 'Tabla para el registro de rel aula espacio.';
COMMENT ON COLUMN infraestructuras.sg_rel_aula_espacio.rae_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN infraestructuras.sg_rel_aula_espacio.rae_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN infraestructuras.sg_rel_aula_espacio.rae_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN infraestructuras.sg_rel_aula_espacio.rae_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS infraestructuras.sg_rel_aula_espacio_aud (rae_pk bigint NOT NULL, rae_aula_fk bigint,rae_espacio_comun_fk bigint, rae_cantidad numeric,rae_ult_mod_fecha timestamp without time zone, rae_ult_mod_usuario character varying(45), rae_version integer, rev bigint, revtype smallint);
ALTER TABLE infraestructuras.sg_rel_aula_espacio_aud ADD PRIMARY KEY (rae_pk, rev);


-- Rel Aula Servicio
CREATE SEQUENCE IF NOT EXISTS infraestructuras.sg_rel_aula_servicio_ras_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS infraestructuras.sg_rel_aula_servicio (ras_pk bigint NOT NULL DEFAULT nextval('infraestructuras.sg_rel_aula_servicio_ras_pk_seq'::regclass),ras_aula_fk bigint,ras_servicio_fk bigint, ras_ult_mod_fecha timestamp without time zone, ras_ult_mod_usuario character varying(45), ras_version integer, CONSTRAINT sg_rel_aula_servicio_pkey PRIMARY KEY (ras_pk),CONSTRAINT sg_ras_aula_fk FOREIGN KEY (ras_aula_fk) REFERENCES infraestructuras.sg_aulas(aul_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,CONSTRAINT sg_ras_servicio_fk FOREIGN KEY (ras_servicio_fk) REFERENCES catalogo.sg_servicios_basicos(sba_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
COMMENT ON TABLE infraestructuras.sg_rel_aula_servicio IS 'Tabla para el registro de rel aula servicio.';
COMMENT ON COLUMN infraestructuras.sg_rel_aula_servicio.ras_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN infraestructuras.sg_rel_aula_servicio.ras_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN infraestructuras.sg_rel_aula_servicio.ras_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN infraestructuras.sg_rel_aula_servicio.ras_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS infraestructuras.sg_rel_aula_servicio_aud (ras_pk bigint NOT NULL,ras_aula_fk bigint,ras_servicio_fk bigint, ras_ult_mod_fecha timestamp without time zone, ras_ult_mod_usuario character varying(45), ras_version integer, rev bigint, revtype smallint);
ALTER TABLE infraestructuras.sg_rel_aula_servicio_aud ADD PRIMARY KEY (ras_pk, rev);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_REL_AULA_ESPACIO','INF20',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_REL_AULA_ESPACIO','INF21',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_REL_AULA_ESPACIO','INF22',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_REL_AULA_SERVICIO','INF23',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_REL_AULA_SERVICIO','INF24',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_REL_AULA_SERVICIO','INF25',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_REL_EDIFICIO_ESPACIO','INF26',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_REL_EDIFICIO_ESPACIO','INF27',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_REL_EDIFICIO_ESPACIO','INF28',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_REL_EDIFICIO_SERVICIO','INF29',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_REL_EDIFICIO_SERVICIO','INF30',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_REL_EDIFICIO_SERVICIO','INF31',  '', 1, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_REL_INMUEBLE_ESPACIO_COMUN','INFP1',  'Ver pestaña de espacio complementario en inmueble', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_REL_INMUEBLE_SERVICIO_BASICO','INFP2',  'Ver pestaña de servicio basico en inmueble', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_REL_INMUEBLE_ARCHIVO','INFP3',  'Ver pestaña de galeria de imagenes en inmueble', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_REL_AULA_ESPACIO','INFP4',  'Ver pestaña de espacio complementario en aula', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_REL_AULA_SERVICIO','INFP5',  'Ver pestaña de servicio basico en aula', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_REL_EDIFICIO_ESPACIO','INFP6',  'Ver pestaña de espacio complementario en edificio', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_REL_EDIFICIO_SERVICIO','INFP7',  'Ver pestaña de servicio basico en edificio', 1, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_EDIFICIO','INFB01',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_AULA','INFB02',  '', 1, true, null, null,0);

--1.1.2
ALTER TABLE infraestructuras.sg_rel_aula_espacio ADD CONSTRAINT sg_rae_aula_espacio_uk UNIQUE (rae_aula_fk,rae_espacio_comun_fk);
ALTER TABLE infraestructuras.sg_rel_aula_servicio ADD CONSTRAINT sg_rel_aula_servicio_uk UNIQUE (ras_aula_fk,ras_servicio_fk);
ALTER TABLE infraestructuras.sg_rel_edificio_espacio ADD CONSTRAINT sg_rel_edificio_espacio_uk UNIQUE (ree_edificio_fk,ree_espacio_comun_fk);
ALTER TABLE infraestructuras.sg_rel_edificio_servicio ADD CONSTRAINT sg_rel_edificio_servicio_uk UNIQUE (res_edificio_fk,res_servicio_fk);
ALTER TABLE infraestructuras.sg_rel_inmueble_espacio_comun ADD CONSTRAINT rel_inmueble_espacio_comun_uk UNIQUE (rie_inmueble_sede_fk,rie_espacio_comun_fk);
ALTER TABLE infraestructuras.sg_rel_inmueble_servicio ADD CONSTRAINT sg_ris_inmueble_servicio_uk UNIQUE (ris_inmueble_sede_fk,ris_servicio_fk);

update seguridad.sg_operaciones set ope_codigo='INFM01' where ope_nombre='MENU_INMUEBLES_O_TERRENOS';
update seguridad.sg_operaciones set ope_codigo='INFM02' where ope_nombre='MENU_EDIFICIOS';
update seguridad.sg_operaciones set ope_codigo='INFM03' where ope_nombre='MENU_AULAS';
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_INMUEBLES_O_TERRENOS','INFM01',  'Permite ver opción Inmueble en menú', 7, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_EDIFICIOS','INFM02',  'Permite ver opción Edificios en menú', 7, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_AULAS','INFM03',  'Permite ver opción Aulas en menú', 7, true, null, null,0);

update seguridad.sg_operaciones set ope_descripcion='Habilita creación de inmuebles' where ope_codigo='INF01';
update seguridad.sg_operaciones set ope_descripcion='Habilita actualización de inmuebles' where ope_codigo='INF02';
update seguridad.sg_operaciones set ope_descripcion='Habilita eliminación inmuebles' where ope_codigo='INF03';
update seguridad.sg_operaciones set ope_descripcion='Habilita busqueda de inmueble' where ope_codigo='INF04';
update seguridad.sg_operaciones set ope_descripcion='Habilita agregar espacio complementario a inmueble' where ope_codigo='INF05';
update seguridad.sg_operaciones set ope_descripcion='Habilita actualizar espacio complementario de inmueble' where ope_codigo='INF06';
update seguridad.sg_operaciones set ope_descripcion='Habilita eliminar espacio complementario de inmueble' where ope_codigo='INF07';
update seguridad.sg_operaciones set ope_descripcion='Habilita agregar servicio a inmueble' where ope_codigo='INF08';
update seguridad.sg_operaciones set ope_descripcion='Habilita actualizar servicio a inmueble' where ope_codigo='INF09';
update seguridad.sg_operaciones set ope_descripcion='Habilita eliminar servicio a inmueble' where ope_codigo='INF10';
update seguridad.sg_operaciones set ope_descripcion='Habilita agregar imagen a galería de imagenes de inmueble' where ope_codigo='INF11';
update seguridad.sg_operaciones set ope_descripcion='Habilita actualizar imagen de galería de imagenes de a inmueble' where ope_codigo='INF12';
update seguridad.sg_operaciones set ope_descripcion='Habilita eliminar imagen de galería de imagenes a inmueble' where ope_codigo='INF13';
update seguridad.sg_operaciones set ope_descripcion='Habilita creación de edificios' where ope_codigo='INF14';
update seguridad.sg_operaciones set ope_descripcion='Habilita actualización de edificios' where ope_codigo='INF15';
update seguridad.sg_operaciones set ope_descripcion='Habilita eliminación de edificios' where ope_codigo='INF16';
update seguridad.sg_operaciones set ope_descripcion='Habilita creación de aula' where ope_codigo='INF17';
update seguridad.sg_operaciones set ope_descripcion='Habilita actualización de aula' where ope_codigo='INF18';
update seguridad.sg_operaciones set ope_descripcion='Habilita eliminar aula' where ope_codigo='INF19';
update seguridad.sg_operaciones set ope_descripcion='Habilita agregar espacio complementario a aula' where ope_codigo='INF20';
update seguridad.sg_operaciones set ope_descripcion='Habilita actualizar espacio complementario de aula' where ope_codigo='INF21';
update seguridad.sg_operaciones set ope_descripcion='Habilita eliminar espacio complementario de aula' where ope_codigo='INF22';
update seguridad.sg_operaciones set ope_descripcion='Habilita agregar servicio a aula' where ope_codigo='INF23';
update seguridad.sg_operaciones set ope_descripcion='Habilita actualizar servicio de aula' where ope_codigo='INF24';
update seguridad.sg_operaciones set ope_descripcion='Habilita eliminar servicio de aula' where ope_codigo='INF25';
update seguridad.sg_operaciones set ope_descripcion='Habilita agregar espacio complementario a edificio' where ope_codigo='INF26';
update seguridad.sg_operaciones set ope_descripcion='Habilita actualizar espacio complementario de edificio' where ope_codigo='INF27';
update seguridad.sg_operaciones set ope_descripcion='Habilita eliminar espacio complementario de edificio' where ope_codigo='INF28';
update seguridad.sg_operaciones set ope_descripcion='Habilita agregar servicio a edificio' where ope_codigo='INF29';
update seguridad.sg_operaciones set ope_descripcion='Habilita actualizar servicio de edificio' where ope_codigo='INF30';
update seguridad.sg_operaciones set ope_descripcion='Habilita eliminar servicio de edificio' where ope_codigo='INF31';


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_REL_INMUEBLE_ESPACIO_COMUN','INFB03',  'Habilita búsqueda de espacios complementarios asociados a inmuebles.', 7, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_REL_INMUEBLE_SERVICIO_BASICO','INFB04',  'Habilita búsqueda de servicios asociados a inmueble.', 7, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_REL_INMUEBLE_ARCHIVO','INFB05',  'Habilita búsqueda de galería de imágenes de inmueble.', 7, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_REL_AULA_ESPACIO','INFB06',  'Habilita búsqueda de espacios complementarios asociados a aula.', 7, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_REL_AULA_SERVICIO','INFB07',  'Habilita búsqueda de servicios asociados a aula', 7, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_REL_EDIFICIO_ESPACIO','INFB08',  'Habilita búsqueda de espacios complementarios asociados a edificios', 7, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_REL_EDIFICIO_SERVICIO','INFB09',  'Habilita búsqueda de servicios asociados a edificios', 7, true, null, null,0);

update seguridad.sg_operaciones set ope_categoria_operacion_fk=7  WHERE ope_codigo like 'INF%';

--1.1.3
CREATE SEQUENCE IF NOT EXISTS infraestructuras.sg_proceso_legalizacion_ple_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS infraestructuras.sg_proceso_legalizacion (ple_pk bigint NOT NULL DEFAULT nextval('infraestructuras.sg_proceso_legalizacion_ple_pk_seq'::regclass),
ple_numero_presentacion numeric(4),
ple_fecha_presentacion DATE,
ple_presentante character varying(255),
ple_estado bigint,
ple_observaciones_presentacion character varying(4000),
ple_codigo_anterior character varying(10),
ple_otorgante character varying(255),
ple_notario character varying(255),
ple_naturaleza_contrato bigint,
ple_numero_escritura character varying(255),
ple_plazo_anio numeric(3),
ple_libro_protocolo character varying(255),
ple_fecha_contrato DATE,
ple_numero_antecedente numeric(10),
ple_libro character varying(10),
ple_matricula character varying(10),
ple_numero_inscripcion numeric(10),
ple_fecha_inscripcion DATE,
ple_asiento character varying(10),
ple_numero_matricula character varying(10),
ple_fecha_matricula date,
ple_observaciones_inscripcion character varying(4000),
ple_fecha_asignacion date,
ple_colaborador_asignado character varying(255),
ple_numero_expediente numeric,
ple_estado_proceso bigint,
ple_observaciones_estado character varying(255),
ple_numero_acuerdo numeric(10),
ple_fecha_acuerdo date,
ple_ministerio_otorgante bigint,
 ple_ult_mod_fecha timestamp without time zone, ple_ult_mod_usuario character varying(45), ple_version integer, CONSTRAINT sg_proceso_legalizacion_pkey PRIMARY KEY (ple_pk),
CONSTRAINT sg_ple_estado_fk FOREIGN KEY (ple_estado) REFERENCES catalogo.sg_estado_proceso_legalizacion(epl_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,
CONSTRAINT sg_ple_naturaleza_contrato FOREIGN KEY (ple_naturaleza_contrato) REFERENCES catalogo.sg_inf_naturaleza_contrato(nac_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,
CONSTRAINT sg_ple_estado_proceso_fk FOREIGN KEY (ple_estado_proceso) REFERENCES catalogo.sg_estado_proceso_legalizacion(epl_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,
CONSTRAINT sg_ple_ministerio_otorgante_fk FOREIGN KEY (ple_ministerio_otorgante) REFERENCES catalogo.sg_inf_ministerio_otorgante(mio_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
COMMENT ON TABLE infraestructuras.sg_proceso_legalizacion IS 'Tabla para el registro de rel aula servicio.';
COMMENT ON COLUMN infraestructuras.sg_proceso_legalizacion.ple_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN infraestructuras.sg_proceso_legalizacion.ple_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN infraestructuras.sg_proceso_legalizacion.ple_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN infraestructuras.sg_proceso_legalizacion.ple_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS infraestructuras.sg_proceso_legalizacion_aud (ple_pk bigint NOT NULL
,ple_numero_presentacion numeric(4),
ple_fecha_presentacion DATE,
ple_presentante character varying(255),
ple_estado bigint,
ple_observaciones_presentacion character varying(4000),
ple_codigo_anterior character varying(10),
ple_otorgante character varying(255),
ple_notario character varying(255),
ple_naturaleza_contrato bigint,
ple_numero_escritura character varying(255),
ple_plazo_anio numeric(3),
ple_libro_protocolo character varying(255),
ple_fecha_contrato DATE,
ple_numero_antecedente numeric(10),
ple_libro character varying(10),
ple_matricula character varying(10),
ple_numero_inscripcion numeric(10),
ple_fecha_inscripcion DATE,
ple_asiento character varying(10),
ple_numero_matricula character varying(10),
ple_fecha_matricula date,
ple_observaciones_inscripcion character varying(4000),
ple_fecha_asignacion date,
ple_colaborador_asignado character varying(255),
ple_numero_expediente numeric,
ple_estado_proceso bigint,
ple_observaciones_estado character varying(255),
ple_numero_acuerdo numeric(10),
ple_fecha_acuerdo date,
ple_ministerio_otorgante bigint,
 ple_ult_mod_fecha timestamp without time zone, ple_ult_mod_usuario character varying(45), ple_version integer,  rev bigint, revtype smallint);
ALTER TABLE infraestructuras.sg_proceso_legalizacion_aud ADD PRIMARY KEY (ple_pk, rev);

ALTER TABLE infraestructuras.sg_inmuebles_sedes ADD COLUMN tis_proceso_legalizacion_fk bigint;
COMMENT ON COLUMN infraestructuras.sg_inmuebles_sedes.tis_proceso_legalizacion_fk IS 'Proceso legalizacion del registro';
ALTER TABLE infraestructuras.sg_inmuebles_sedes_aud ADD COLUMN tis_proceso_legalizacion_fk bigint;
ALTER TABLE infraestructuras.sg_inmuebles_sedes ADD CONSTRAINT sg_tis_proceso_legalizacion_fk FOREIGN KEY (tis_proceso_legalizacion_fk) REFERENCES infraestructuras.sg_proceso_legalizacion(ple_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;


--1.1.4
ALTER TABLE infraestructuras.sg_rel_inmueble_espacio_comun ADD COLUMN rie_bueno numeric(3);
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_espacio_comun.rie_bueno IS 'Cantidad de bueno';
ALTER TABLE infraestructuras.sg_rel_inmueble_espacio_comun_aud ADD COLUMN rie_bueno numeric(3);

ALTER TABLE infraestructuras.sg_rel_inmueble_espacio_comun ADD COLUMN rie_regular numeric(3);
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_espacio_comun.rie_regular IS 'Cantidad de regular';
ALTER TABLE infraestructuras.sg_rel_inmueble_espacio_comun_aud ADD COLUMN rie_regular numeric(3);

ALTER TABLE infraestructuras.sg_rel_inmueble_espacio_comun ADD COLUMN rie_malo numeric(3);
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_espacio_comun.rie_malo IS 'Cantidad de malo';
ALTER TABLE infraestructuras.sg_rel_inmueble_espacio_comun_aud ADD COLUMN rie_malo numeric(3);

ALTER TABLE infraestructuras.sg_rel_inmueble_espacio_comun ADD COLUMN rie_descripcion character varying(250);
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_espacio_comun.rie_descripcion IS 'Descripcion';
ALTER TABLE infraestructuras.sg_rel_inmueble_espacio_comun_aud ADD COLUMN rie_descripcion character varying(250);


ALTER TABLE infraestructuras.sg_rel_edificio_espacio ADD COLUMN ree_bueno numeric(3);
COMMENT ON COLUMN infraestructuras.sg_rel_edificio_espacio.ree_bueno IS 'Cantidad de bueno';
ALTER TABLE infraestructuras.sg_rel_edificio_espacio_aud ADD COLUMN ree_bueno numeric(3);

ALTER TABLE infraestructuras.sg_rel_edificio_espacio ADD COLUMN ree_regular numeric(3);
COMMENT ON COLUMN infraestructuras.sg_rel_edificio_espacio.ree_regular IS 'Cantidad de regular';
ALTER TABLE infraestructuras.sg_rel_edificio_espacio_aud ADD COLUMN ree_regular numeric(3);

ALTER TABLE infraestructuras.sg_rel_edificio_espacio ADD COLUMN ree_malo numeric(3);
COMMENT ON COLUMN infraestructuras.sg_rel_edificio_espacio.ree_malo IS 'Cantidad de malo';
ALTER TABLE infraestructuras.sg_rel_edificio_espacio_aud ADD COLUMN ree_malo numeric(3);

ALTER TABLE infraestructuras.sg_rel_edificio_espacio ADD COLUMN ree_descripcion character varying(250);
COMMENT ON COLUMN infraestructuras.sg_rel_edificio_espacio.ree_descripcion IS 'Descripcion';
ALTER TABLE infraestructuras.sg_rel_edificio_espacio_aud ADD COLUMN ree_descripcion character varying(250);


ALTER TABLE infraestructuras.sg_rel_aula_espacio ADD COLUMN rae_bueno numeric(3);
COMMENT ON COLUMN infraestructuras.sg_rel_aula_espacio.rae_bueno IS 'Cantidad de bueno';
ALTER TABLE infraestructuras.sg_rel_aula_espacio_aud ADD COLUMN rae_bueno numeric(3);

ALTER TABLE infraestructuras.sg_rel_aula_espacio ADD COLUMN rae_regular numeric(3);
COMMENT ON COLUMN infraestructuras.sg_rel_aula_espacio.rae_regular IS 'Cantidad de regular';
ALTER TABLE infraestructuras.sg_rel_aula_espacio_aud ADD COLUMN rae_regular numeric(3);

ALTER TABLE infraestructuras.sg_rel_aula_espacio ADD COLUMN rae_malo numeric(3);
COMMENT ON COLUMN infraestructuras.sg_rel_aula_espacio.rae_malo IS 'Cantidad de malo';
ALTER TABLE infraestructuras.sg_rel_aula_espacio_aud ADD COLUMN rae_malo numeric(3);

ALTER TABLE infraestructuras.sg_rel_aula_espacio ADD COLUMN rae_descripcion character varying(250);
COMMENT ON COLUMN infraestructuras.sg_rel_aula_espacio.rae_descripcion IS 'Descripcion';
ALTER TABLE infraestructuras.sg_rel_aula_espacio_aud ADD COLUMN rae_descripcion character varying(250);

--1.1.5

ALTER TABLE infraestructuras.sg_rel_aula_espacio DROP COLUMN rae_cantidad;
ALTER TABLE infraestructuras.sg_rel_aula_espacio_aud DROP COLUMN rae_cantidad;
ALTER TABLE infraestructuras.sg_rel_edificio_espacio DROP COLUMN ree_cantidad;
ALTER TABLE infraestructuras.sg_rel_edificio_espacio_aud DROP COLUMN ree_cantidad;
ALTER TABLE infraestructuras.sg_rel_inmueble_espacio_comun DROP COLUMN rie_cantidad;
ALTER TABLE infraestructuras.sg_rel_inmueble_espacio_comun_aud DROP COLUMN rie_cantidad;

--1.1.6
-- Rel Inmueble Servicio Sanitario
CREATE SEQUENCE IF NOT EXISTS infraestructuras.sg_rel_inmueble_servicio_sanitario_rit_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS infraestructuras.sg_rel_inmueble_servicio_sanitario (rit_pk bigint NOT NULL DEFAULT nextval('infraestructuras.sg_rel_inmueble_servicio_sanitario_rit_pk_seq'::regclass),rit_bueno numeric(3),rit_malo numeric(3),rit_regular numeric(3),rit_ninos numeric(3),rit_ninas numeric(3),rit_maestros numeric(3),rit_administrativos numeric(3),rit_inmueble_sede_fk bigint, rit_servicio_sanitario_fk bigint, rit_ult_mod_fecha timestamp without time zone, rit_ult_mod_usuario character varying(45), rit_version integer, CONSTRAINT sg_rel_inmueble_servicio_sanitario_pkey PRIMARY KEY (rit_pk),CONSTRAINT sg_rit_inmueble_sede_fk FOREIGN KEY (rit_inmueble_sede_fk) REFERENCES infraestructuras.sg_inmuebles_sedes(tis_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,CONSTRAINT sg_rit_servicio_sanitario_fk FOREIGN KEY (rit_servicio_sanitario_fk) REFERENCES catalogo.sg_tipos_servicio_sanitario(tss_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
COMMENT ON TABLE infraestructuras.sg_rel_inmueble_servicio_sanitario IS 'Tabla para el registro de rel inmueble servicio sanitario.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_servicio_sanitario.rit_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_servicio_sanitario.rit_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_servicio_sanitario.rit_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_servicio_sanitario.rit_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS infraestructuras.sg_rel_inmueble_servicio_sanitario_aud (rit_pk bigint NOT NULL,rit_bueno numeric(3),rit_malo numeric(3),rit_regular numeric(3),rit_ninos numeric(3),rit_ninas numeric(3),rit_maestros numeric(3),rit_administrativos numeric(3),rit_inmueble_sede_fk bigint, rit_servicio_sanitario_fk bigint, rit_ult_mod_fecha timestamp without time zone, rit_ult_mod_usuario character varying(45), rit_version integer, rev bigint, revtype smallint);
ALTER TABLE infraestructuras.sg_rel_inmueble_servicio_sanitario_aud ADD PRIMARY KEY (rit_pk, rev);


-- Rel Edificio Servicio Sanitario
CREATE SEQUENCE IF NOT EXISTS infraestructuras.sg_rel_edificio_servicio_sanitario_ret_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS infraestructuras.sg_rel_edificio_servicio_sanitario (ret_pk bigint NOT NULL DEFAULT nextval('infraestructuras.sg_rel_edificio_servicio_sanitario_ret_pk_seq'::regclass),ret_bueno numeric(3),ret_malo numeric(3),ret_regular numeric(3),ret_ninos numeric(3),ret_ninas numeric(3),ret_maestros numeric(3),ret_administrativos numeric(3),ret_edificio_fk bigint,ret_servicio_sanitario_fk bigint, ret_ult_mod_fecha timestamp without time zone, ret_ult_mod_usuario character varying(45), ret_version integer, CONSTRAINT sg_rel_edificio_servicio_sanitario_pkey PRIMARY KEY (ret_pk),CONSTRAINT sg_ret_edificio_fk FOREIGN KEY (ret_edificio_fk) REFERENCES infraestructuras.sg_edificios(edi_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,CONSTRAINT sg_ret_servicio_sanitario_fk FOREIGN KEY (ret_servicio_sanitario_fk) REFERENCES catalogo.sg_tipos_servicio_sanitario(tss_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
COMMENT ON TABLE infraestructuras.sg_rel_edificio_servicio_sanitario IS 'Tabla para el registro de rel edificio servicio sanitario.';
COMMENT ON COLUMN infraestructuras.sg_rel_edificio_servicio_sanitario.ret_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN infraestructuras.sg_rel_edificio_servicio_sanitario.ret_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN infraestructuras.sg_rel_edificio_servicio_sanitario.ret_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN infraestructuras.sg_rel_edificio_servicio_sanitario.ret_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS infraestructuras.sg_rel_edificio_servicio_sanitario_aud (ret_pk bigint NOT NULL,ret_bueno numeric(3),ret_malo numeric(3),ret_regular numeric(3),ret_ninos numeric(3),ret_ninas numeric(3),ret_maestros numeric(3),ret_administrativos numeric(3),ret_edificio_fk bigint,ret_servicio_sanitario_fk bigint, ret_ult_mod_fecha timestamp without time zone, ret_ult_mod_usuario character varying(45), ret_version integer, rev bigint, revtype smallint);
ALTER TABLE infraestructuras.sg_rel_edificio_servicio_sanitario_aud ADD PRIMARY KEY (ret_pk, rev);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_REL_EDIFICIO_SERVICIO_SANITARIO','INF32',  'Habilita crear relacion entre edificio y servicio sanitario', 7, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_REL_EDIFICIO_SERVICIO_SANITARIO','INF33',  'Habilita actualizar relacion entre edificio y servicio sanitario', 7, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_REL_EDIFICIO_SERVICIO_SANITARIO','INF34',  'Habilita eliminar relacion entre edificio y servicio sanitario', 7, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_REL_INMUEBLE_SERVICIO_SANITARIO','INF35',  'Habilita crear relacion entre inmueble y servicio sanitario', 7, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_REL_INMUEBLE_SERVICIO_SANITARIO','INF36',  'Habilita actualizar relacion entre inmueble y servicio sanitario', 7, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_REL_INMUEBLE_SERVICIO_SANITARIO','INF37',  'Habilita eliminar relacion entre inmueble y servicio sanitario', 7, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_REL_EDIFICIO_SERVICIO_SANITARIO','INFB10',  'Habilita buscar relacion entre edificio y servicio sanitario', 7, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_REL_INMUEBLE_SERVICIO_SANITARIO','INFB11',  'Habilita buscar relacion entre inmueble y servicio sanitario', 7, true, null, null,0);


ALTER TABLE infraestructuras.sg_aulas ADD COLUMN aul_estado character varying(50);
ALTER TABLE infraestructuras.sg_aulas_aud ADD COLUMN aul_estado character varying(50);

ALTER TABLE infraestructuras.sg_aulas ADD COLUMN aul_tipo character varying(50);
ALTER TABLE infraestructuras.sg_aulas_aud ADD COLUMN aul_tipo character varying(50);

ALTER TABLE infraestructuras.sg_aulas ADD COLUMN aul_area numeric(10,2);
ALTER TABLE infraestructuras.sg_aulas_aud ADD COLUMN aul_area numeric(10,2);


ALTER TABLE infraestructuras.sg_rel_inmueble_archivo ADD COLUMN ria_publicable boolean;
ALTER TABLE infraestructuras.sg_rel_inmueble_archivo_aud ADD COLUMN ria_publicable boolean;

ALTER TABLE infraestructuras.sg_rel_inmueble_archivo ADD COLUMN ria_tipo_imagen_fk bigint;
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_archivo.ria_tipo_imagen_fk IS 'Fuente de financiamiento de registro';
ALTER TABLE infraestructuras.sg_rel_inmueble_archivo_aud ADD COLUMN ria_tipo_imagen_fk bigint;
ALTER TABLE infraestructuras.sg_rel_inmueble_archivo ADD CONSTRAINT sg_ria_tipo_imagen_fk FOREIGN KEY (ria_tipo_imagen_fk) REFERENCES catalogo.sg_inf_tipo_imagen(tii_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

CREATE TABLE IF NOT EXISTS infraestructuras.sg_aula_tipologia_construccion (aul_pk bigint NOT NULL,tic_pk bigint NOT NULL, CONSTRAINT sg_aula_tiplogia_construccion_aula_fk FOREIGN KEY (aul_pk) REFERENCES infraestructuras.sg_aulas(aul_pk), CONSTRAINT sg_aula_tiplogia_construccion_tipologia_fk FOREIGN KEY (tic_pk) REFERENCES catalogo.sg_inf_tipologia_construccion (tic_pk));
CREATE TABLE IF NOT EXISTS infraestructuras.sg_aula_tipologia_construccion_aud(aul_pk bigint NOT NULL,tic_pk bigint NOT NULL, rev bigint, revtype smallint);

--1.1.7
ALTER TABLE infraestructuras.sg_proceso_legalizacion ADD COLUMN ple_asiento_antecedente character varying(50);
COMMENT ON COLUMN infraestructuras.sg_proceso_legalizacion.ple_asiento_antecedente IS 'Asiento antecedente';
ALTER TABLE infraestructuras.sg_proceso_legalizacion_aud ADD COLUMN ple_asiento_antecedente character varying(50);

ALTER TABLE infraestructuras.sg_proceso_legalizacion ADD COLUMN ple_matricula_antecedente character varying(50);
COMMENT ON COLUMN infraestructuras.sg_proceso_legalizacion.ple_matricula_antecedente IS 'Matricula antecedente';
ALTER TABLE infraestructuras.sg_proceso_legalizacion_aud ADD COLUMN ple_matricula_antecedente character varying(50);

ALTER TABLE infraestructuras.sg_proceso_legalizacion ADD COLUMN ple_libro_antecedente character varying(50);
COMMENT ON COLUMN infraestructuras.sg_proceso_legalizacion.ple_libro_antecedente IS 'Libro antecedente';
ALTER TABLE infraestructuras.sg_proceso_legalizacion_aud ADD COLUMN ple_libro_antecedente character varying(50);

ALTER TABLE infraestructuras.sg_proceso_legalizacion ADD COLUMN ple_numero_inscripcion_libro numeric(10);
COMMENT ON COLUMN infraestructuras.sg_proceso_legalizacion.ple_numero_inscripcion_libro IS 'Numero inscripcion libro';
ALTER TABLE infraestructuras.sg_proceso_legalizacion_aud ADD COLUMN ple_numero_inscripcion_libro numeric(10);

ALTER TABLE infraestructuras.sg_proceso_legalizacion ADD COLUMN ple_libro_inscripcion_libro character varying(50);
COMMENT ON COLUMN infraestructuras.sg_proceso_legalizacion.ple_libro_inscripcion_libro IS 'Libro inscripcion libro';
ALTER TABLE infraestructuras.sg_proceso_legalizacion_aud ADD COLUMN ple_libro_inscripcion_libro character varying(50);

ALTER TABLE infraestructuras.sg_proceso_legalizacion ADD COLUMN ple_fecha_inscripcion_libro DATE;
COMMENT ON COLUMN infraestructuras.sg_proceso_legalizacion.ple_fecha_inscripcion_libro IS 'Fecha inscripcion libro';
ALTER TABLE infraestructuras.sg_proceso_legalizacion_aud ADD COLUMN ple_fecha_inscripcion_libro DATE;

ALTER TABLE infraestructuras.sg_proceso_legalizacion ADD COLUMN ple_matricula_inscripcion_matricula character varying(50);
COMMENT ON COLUMN infraestructuras.sg_proceso_legalizacion.ple_matricula_inscripcion_matricula IS 'Matricula inscripcion sistema matrícula';
ALTER TABLE infraestructuras.sg_proceso_legalizacion_aud ADD COLUMN ple_matricula_inscripcion_matricula character varying(50);

ALTER TABLE infraestructuras.sg_proceso_legalizacion ADD COLUMN ple_asiento_inscripcion_matricula character varying(50);
COMMENT ON COLUMN infraestructuras.sg_proceso_legalizacion.ple_asiento_inscripcion_matricula IS 'Asiento Inscripción matrícula';
ALTER TABLE infraestructuras.sg_proceso_legalizacion_aud ADD COLUMN ple_asiento_inscripcion_matricula character varying(50);

ALTER TABLE infraestructuras.sg_proceso_legalizacion ADD COLUMN ple_fecha_inscripcion_matricula character varying(50);
COMMENT ON COLUMN infraestructuras.sg_proceso_legalizacion.ple_fecha_inscripcion_matricula IS 'Fecha inscripcion matrícula';
ALTER TABLE infraestructuras.sg_proceso_legalizacion_aud ADD COLUMN ple_fecha_inscripcion_matricula character varying(50);

ALTER TABLE infraestructuras.sg_proceso_legalizacion ADD COLUMN ple_observaciones character varying(4000);
COMMENT ON COLUMN infraestructuras.sg_proceso_legalizacion.ple_observaciones IS 'Observaciones';
ALTER TABLE infraestructuras.sg_proceso_legalizacion_aud ADD COLUMN ple_observaciones character varying(4000);

--1.2.1
CREATE OR REPLACE FUNCTION infraestructuras.numero_correlativo_inmueble_sede() RETURNS TRIGGER AS
$BODY$
BEGIN
    UPDATE infraestructuras.sg_inmuebles_sedes set tis_num_correlativo = 
       (select coalesce( (SELECT MAX(tis_num_correlativo)+1 from infraestructuras.sg_inmuebles_sedes where tis_sede_fk=NEW.tis_sede_fk),1))
    WHERE tis_pk=NEW.tis_pk;
RETURN new;
END;
$BODY$
language plpgsql;

CREATE TRIGGER numero_correlativo_inmueble_sede_trigger
AFTER INSERT ON infraestructuras.sg_inmuebles_sedes
FOR EACH ROW 
EXECUTE PROCEDURE infraestructuras.numero_correlativo_inmueble_sede();

DROP TABLE IF EXISTS infraestructuras.sg_lock;



--1.3.0


-- Mejoras
CREATE SEQUENCE IF NOT EXISTS infraestructuras.sg_mejoras_mej_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS infraestructuras.sg_mejoras (mej_pk bigint NOT NULL DEFAULT nextval('infraestructuras.sg_mejoras_mej_pk_seq'::regclass),mej_inmueble bigint,mej_edificio bigint,mej_fuente_financiamiento bigint, mej_fecha date,mej_tipo_mejora bigint,mej_descripcion character varying(4000),mej_costo numeric(10,2), mej_ult_mod_fecha timestamp without time zone, mej_ult_mod_usuario character varying(45), mej_version integer, CONSTRAINT sg_mejoras_pkey PRIMARY KEY (mej_pk),CONSTRAINT sg_mej_fuente_financiamiento FOREIGN KEY (mej_fuente_financiamiento) REFERENCES catalogo.sg_fuente_financiamiento(ffi_pk)  MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_mej_tipo_mejora FOREIGN KEY (mej_tipo_mejora) REFERENCES catalogo.sg_inf_tipo_mejora(tme_pk)  MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,CONSTRAINT sg_mej_edificio FOREIGN KEY (mej_edificio) REFERENCES infraestructuras.sg_edificios(edi_pk)  MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,CONSTRAINT sg_mej_inmueble FOREIGN KEY (mej_inmueble) REFERENCES infraestructuras.sg_inmuebles_sedes(tis_pk)  MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
COMMENT ON TABLE infraestructuras.sg_mejoras IS 'Tabla para el registro de mejoras.';
COMMENT ON COLUMN infraestructuras.sg_mejoras.mej_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN infraestructuras.sg_mejoras.mej_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN infraestructuras.sg_mejoras.mej_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS infraestructuras.sg_mejoras_aud (mej_pk bigint NOT NULL,mej_inmueble bigint,mej_edificio bigint, mej_fuente_financiamiento bigint,mej_fecha date,mej_tipo_mejora bigint,mej_descripcion character varying(4000),mej_costo numeric(10,2), mej_ult_mod_fecha timestamp without time zone, mej_ult_mod_usuario character varying(45), mej_version integer, rev bigint, revtype smallint);
ALTER TABLE infraestructuras.sg_mejoras_aud ADD PRIMARY KEY (mej_pk, rev);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_MEJORAS','INF41',  'Habilita crear mejoras', 7, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_MEJORAS','INF42',  'Habilita actualizar mejoras', 7, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_MEJORAS','INF43',  'Habilita eliminar mejoras', 7, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_MEJORAS','INFB13',  'Habilita buscar mejoras', 7, true, null, null,0);


-- Rel Inmueble Accesibilidad
CREATE SEQUENCE IF NOT EXISTS infraestructuras.sg_rel_inmueble_accesibilidad_iac_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS infraestructuras.sg_rel_inmueble_accesibilidad (iac_pk bigint NOT NULL DEFAULT nextval('infraestructuras.sg_rel_inmueble_accesibilidad_iac_pk_seq'::regclass),iac_inmueble_sede_fk bigint,iac_accesibilidad_fk bigint, iac_bueno numeric(3), iac_malo numeric(3), iac_regular numeric(3),  iac_descripcion character varying(255), iac_ult_mod_fecha timestamp without time zone, iac_ult_mod_usuario character varying(45), iac_version integer, CONSTRAINT sg_rel_inmueble_accesibilidad_pkey PRIMARY KEY (iac_pk),
CONSTRAINT sg_iac_inmueble_sede_fk FOREIGN KEY (iac_inmueble_sede_fk) REFERENCES infraestructuras.sg_inmuebles_sedes(tis_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,
CONSTRAINT sg_iac_accesibilidad_fk FOREIGN KEY (iac_accesibilidad_fk) REFERENCES catalogo.sg_inf_accesibilidad (acc_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
COMMENT ON TABLE infraestructuras.sg_rel_inmueble_accesibilidad IS 'Tabla para el registro de rel inmueble accesibilidad.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_accesibilidad.iac_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_accesibilidad.iac_bueno IS 'Cantidad de bueno.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_accesibilidad.iac_malo IS 'Cantidad de malo.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_accesibilidad.iac_regular IS 'Cantidad de regular.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_accesibilidad.iac_descripcion IS 'Descripcion de registro.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_accesibilidad.iac_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_accesibilidad.iac_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_accesibilidad.iac_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS infraestructuras.sg_rel_inmueble_accesibilidad_aud (iac_pk bigint NOT NULL,iac_inmueble_sede_fk bigint,iac_accesibilidad_fk bigint,iac_bueno numeric(3), iac_malo numeric(3), iac_regular numeric(3),  iac_descripcion character varying(255), iac_ult_mod_fecha timestamp without time zone, iac_ult_mod_usuario character varying(45), iac_version integer, rev bigint, revtype smallint);
ALTER TABLE infraestructuras.sg_rel_inmueble_accesibilidad_aud ADD PRIMARY KEY (iac_pk, rev);


CREATE TABLE IF NOT EXISTS infraestructuras.sg_inmueble_tipo_abastecimiento (tis_pk bigint NOT NULL,tab_pk bigint NOT NULL, CONSTRAINT sg_inmueble_tipo_abastecimiento_inmueble_fk FOREIGN KEY (tis_pk) REFERENCES infraestructuras.sg_inmuebles_sedes(tis_pk), 
CONSTRAINT sg_inmueble_tipo_abastecimiento_tipo_abastecimiento_fk FOREIGN KEY (tab_pk) REFERENCES catalogo.sg_inf_tipo_abastecimiento(tab_pk));
CREATE TABLE IF NOT EXISTS infraestructuras.sg_inmueble_tipo_abastecimiento_aud(tis_pk bigint NOT NULL,tab_pk bigint NOT NULL, rev bigint, revtype smallint);


-- Rel Inmueble Abastecimiento Agua
CREATE SEQUENCE IF NOT EXISTS infraestructuras.sg_rel_inmueble_abastecimiento_agua_iaa_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS infraestructuras.sg_rel_inmueble_abastecimiento_agua (iaa_pk bigint NOT NULL DEFAULT nextval('infraestructuras.sg_rel_inmueble_abastecimiento_agua_iaa_pk_seq'::regclass),
iaa_inmueble_sede_fk bigint,iaa_abastecimiento_agua bigint, iaa_bueno numeric(3), iaa_malo numeric(3), iaa_regular numeric(3),  iaa_descripcion character varying(255), iaa_ult_mod_fecha timestamp without time zone, iaa_ult_mod_usuario character varying(45), iaa_version integer, 
CONSTRAINT sg_rel_inmueble_abastecimiento_agua_pkey PRIMARY KEY (iaa_pk),
CONSTRAINT sg_iaa_inmueble_sede_fk FOREIGN KEY (iaa_inmueble_sede_fk) REFERENCES infraestructuras.sg_inmuebles_sedes(tis_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,
CONSTRAINT sg_iaa_abastecimiento_agua FOREIGN KEY (iaa_abastecimiento_agua) REFERENCES catalogo.sg_inf_tratamiento_agua(tra_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
COMMENT ON TABLE infraestructuras.sg_rel_inmueble_abastecimiento_agua IS 'Tabla para el registro de rel inmueble abastecimiento agua.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_abastecimiento_agua.iaa_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_abastecimiento_agua.iaa_bueno IS 'Cantidad de bueno.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_abastecimiento_agua.iaa_malo IS 'Cantidad de malo.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_abastecimiento_agua.iaa_regular IS 'Cantidad de regular.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_abastecimiento_agua.iaa_descripcion IS 'Descripcion de registro.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_abastecimiento_agua.iaa_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_abastecimiento_agua.iaa_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_abastecimiento_agua.iaa_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS infraestructuras.sg_rel_inmueble_abastecimiento_agua_aud (iaa_pk bigint NOT NULL,iaa_abastecimiento_agua bigint,iaa_inmueble_sede_fk bigint,iaa_bueno numeric(3), iaa_malo numeric(3), iaa_regular numeric(3),  iaa_descripcion character varying(255), iaa_ult_mod_fecha timestamp without time zone, iaa_ult_mod_usuario character varying(45), iaa_version integer, rev bigint, revtype smallint);
ALTER TABLE infraestructuras.sg_rel_inmueble_abastecimiento_agua_aud ADD PRIMARY KEY (iaa_pk, rev);

-- Rel Inmueble Almacenamiento Agua
CREATE SEQUENCE IF NOT EXISTS infraestructuras.sg_rel_inmueble_almacenamiento_agua_ial_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS infraestructuras.sg_rel_inmueble_almacenamiento_agua (ial_pk bigint NOT NULL DEFAULT nextval('infraestructuras.sg_rel_inmueble_almacenamiento_agua_ial_pk_seq'::regclass),ial_inmueble_sede_fk bigint, ial_almacenamiento_agua bigint,ial_bueno numeric(3), ial_malo numeric(3), ial_regular numeric(3),  ial_descripcion character varying(255), ial_ult_mod_fecha timestamp without time zone, ial_ult_mod_usuario character varying(45), ial_version integer, 
CONSTRAINT sg_rel_inmueble_almacenamiento_agua_pkey PRIMARY KEY (ial_pk),
CONSTRAINT sg_ial_inmueble_sede_fk FOREIGN KEY (ial_inmueble_sede_fk) REFERENCES infraestructuras.sg_inmuebles_sedes(tis_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,
CONSTRAINT sg_ial_almacenamiento_agua FOREIGN KEY (ial_almacenamiento_agua) REFERENCES catalogo.sg_inf_tratamiento_agua(tra_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
COMMENT ON TABLE infraestructuras.sg_rel_inmueble_almacenamiento_agua IS 'Tabla para el registro de rel inmueble almacenamiento agua.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_almacenamiento_agua.ial_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_almacenamiento_agua.ial_bueno IS 'Cantidad de bueno.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_almacenamiento_agua.ial_malo IS 'Cantidad de malo.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_almacenamiento_agua.ial_regular IS 'Cantidad de regular.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_almacenamiento_agua.ial_descripcion IS 'Descripcion de registro.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_almacenamiento_agua.ial_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_almacenamiento_agua.ial_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_almacenamiento_agua.ial_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS infraestructuras.sg_rel_inmueble_almacenamiento_agua_aud (ial_pk bigint NOT NULL,ial_inmueble_sede_fk bigint,ial_almacenamiento_agua bigint, ial_bueno numeric(3), ial_malo numeric(3), ial_regular numeric(3),  ial_descripcion character varying(255), ial_ult_mod_fecha timestamp without time zone, ial_ult_mod_usuario character varying(45), ial_version integer, rev bigint, revtype smallint);
ALTER TABLE infraestructuras.sg_rel_inmueble_almacenamiento_agua_aud ADD PRIMARY KEY (ial_pk, rev);

-- Rel Inmueble Tipo Drenaje
CREATE SEQUENCE IF NOT EXISTS infraestructuras.sg_rel_inmueble_tipo_drenaje_itd_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS infraestructuras.sg_rel_inmueble_tipo_drenaje (itd_pk bigint NOT NULL DEFAULT nextval('infraestructuras.sg_rel_inmueble_tipo_drenaje_itd_pk_seq'::regclass), itd_inmueble_sede_fk bigint,itd_tipo_drenaje bigint,itd_bueno numeric(3), itd_malo numeric(3), itd_regular numeric(3),  itd_descripcion character varying(255), itd_ult_mod_fecha timestamp without time zone, itd_ult_mod_usuario character varying(45), itd_version integer, CONSTRAINT sg_rel_inmueble_tipo_drenaje_pkey PRIMARY KEY (itd_pk),
CONSTRAINT sg_itd_inmueble_sede_fk FOREIGN KEY (itd_inmueble_sede_fk) REFERENCES infraestructuras.sg_inmuebles_sedes(tis_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,
CONSTRAINT sg_itd_tipo_drenaje FOREIGN KEY (itd_tipo_drenaje) REFERENCES catalogo.sg_inf_tratamiento_agua(tra_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
COMMENT ON TABLE infraestructuras.sg_rel_inmueble_tipo_drenaje IS 'Tabla para el registro de rel inmueble tipo drenaje.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_tipo_drenaje.itd_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_tipo_drenaje.itd_bueno IS 'Cantidad de bueno.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_tipo_drenaje.itd_malo IS 'Cantidad de malo.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_tipo_drenaje.itd_regular IS 'Cantidad de regular.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_tipo_drenaje.itd_descripcion IS 'Descripcion de registro.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_tipo_drenaje.itd_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_tipo_drenaje.itd_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_tipo_drenaje.itd_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS infraestructuras.sg_rel_inmueble_tipo_drenaje_aud (itd_pk bigint NOT NULL,itd_inmueble_sede_fk bigint,itd_tipo_drenaje bigint,itd_bueno numeric(3), itd_malo numeric(3), itd_regular numeric(3),  itd_descripcion character varying(255), itd_ult_mod_fecha timestamp without time zone, itd_ult_mod_usuario character varying(45), itd_version integer, rev bigint, revtype smallint);
ALTER TABLE infraestructuras.sg_rel_inmueble_tipo_drenaje_aud ADD PRIMARY KEY (itd_pk, rev);

-- Rel Inmueble Manejo Desechos
CREATE SEQUENCE IF NOT EXISTS infraestructuras.sg_rel_inmueble_manejo_desechos_imd_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS infraestructuras.sg_rel_inmueble_manejo_desechos (imd_pk bigint NOT NULL DEFAULT nextval('infraestructuras.sg_rel_inmueble_manejo_desechos_imd_pk_seq'::regclass),imd_inmueble_sede_fk bigint,imd_manejo_desechos bigint, imd_bueno numeric(3), imd_malo numeric(3), imd_regular numeric(3),  imd_descripcion character varying(255), imd_ult_mod_fecha timestamp without time zone, imd_ult_mod_usuario character varying(45), imd_version integer, CONSTRAINT sg_rel_inmueble_manejo_desechos_pkey PRIMARY KEY (imd_pk),
CONSTRAINT sg_imd_inmueble_sede_fk FOREIGN KEY (imd_inmueble_sede_fk) REFERENCES infraestructuras.sg_inmuebles_sedes(tis_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,
CONSTRAINT sg_imd_manejo_desechos FOREIGN KEY (imd_manejo_desechos) REFERENCES catalogo.sg_inf_tipo_manejo_desechos(tmd_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
COMMENT ON TABLE infraestructuras.sg_rel_inmueble_manejo_desechos IS 'Tabla para el registro de rel inmueble manejo desechos.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_manejo_desechos.imd_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_manejo_desechos.imd_bueno IS 'Cantidad de bueno.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_manejo_desechos.imd_malo IS 'Cantidad de malo.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_manejo_desechos.imd_regular IS 'Cantidad de regular.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_manejo_desechos.imd_descripcion IS 'Descripcion de registro.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_manejo_desechos.imd_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_manejo_desechos.imd_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_manejo_desechos.imd_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS infraestructuras.sg_rel_inmueble_manejo_desechos_aud (imd_pk bigint NOT NULL,imd_inmueble_sede_fk bigint,imd_manejo_desechos bigint,imd_bueno numeric(3), imd_malo numeric(3), imd_regular numeric(3),  imd_descripcion character varying(255), imd_ult_mod_fecha timestamp without time zone, imd_ult_mod_usuario character varying(45), imd_version integer, rev bigint, revtype smallint);
ALTER TABLE infraestructuras.sg_rel_inmueble_manejo_desechos_aud ADD PRIMARY KEY (imd_pk, rev);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_REL_INMUEBLE_ACCESIBILIDAD','INF44',  '', 7, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_REL_INMUEBLE_ACCESIBILIDAD','INF45',  '', 7, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_REL_INMUEBLE_ACCESIBILIDAD','INF46',  '', 7, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_REL_INMUEBLE_ABASTECIMIENTO_AGUA','INF47',  '', 7, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_REL_INMUEBLE_ABASTECIMIENTO_AGUA','INF48',  '', 7, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_REL_INMUEBLE_ABASTECIMIENTO_AGUA','INF49',  '', 7, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_REL_INMUEBLE_ALMACENAMIENTO_AGUA','INF50',  '', 7, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_REL_INMUEBLE_ALMACENAMIENTO_AGUA','INF51',  '', 7, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_REL_INMUEBLE_ALMACENAMIENTO_AGUA','INF52',  '', 7, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_REL_INMUEBLE_MANEJO_DESECHOS','INF53',  '', 7, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_REL_INMUEBLE_MANEJO_DESECHOS','INF54',  '', 7, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_REL_INMUEBLE_MANEJO_DESECHOS','INF55',  '', 7, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_REL_INMUEBLE_TIPO_DRENAJE','INF56',  '', 7, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_REL_INMUEBLE_TIPO_DRENAJE','INF57',  '', 7, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_REL_INMUEBLE_TIPO_DRENAJE','INF58',  '', 7, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_REL_INMUEBLE_TIPO_ABASTECIMIENTO','INF59',  '', 7, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_REL_INMUEBLE_TIPO_ABASTECIMIENTO','INF60',  '', 7, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_REL_INMUEBLE_TIPO_ABASTECIMIENTO','INF61',  '', 7, true, null, null,0);


ALTER TABLE infraestructuras.sg_inmuebles_sedes ADD COLUMN tis_tiene_transformador boolean;
ALTER TABLE infraestructuras.sg_inmuebles_sedes_aud ADD COLUMN tis_tiene_transformador boolean;

ALTER TABLE infraestructuras.sg_inmuebles_sedes ADD COLUMN tis_capacidad_transformador integer;
ALTER TABLE infraestructuras.sg_inmuebles_sedes_aud ADD COLUMN tis_capacidad_transformador integer;

ALTER TABLE infraestructuras.sg_inmuebles_sedes ADD COLUMN tis_condiciones_instalaciones_elec character varying(50);
ALTER TABLE infraestructuras.sg_inmuebles_sedes_aud ADD COLUMN tis_condiciones_instalaciones_elec character varying(50);

ALTER TABLE infraestructuras.sg_inmuebles_sedes ADD COLUMN tis_posee_area_acopio boolean;
ALTER TABLE infraestructuras.sg_inmuebles_sedes_aud ADD COLUMN tis_posee_area_acopio boolean;

ALTER TABLE infraestructuras.sg_inmuebles_sedes ADD COLUMN tis_condiciones_acceso character varying(4000);
ALTER TABLE infraestructuras.sg_inmuebles_sedes_aud ADD COLUMN tis_condiciones_acceso character varying(4000);

--1.4.0

-- SgLock nros correlativos inmuebles
CREATE SEQUENCE IF NOT EXISTS infraestructuras.sg_lock_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS infraestructuras.sg_lock (lck_pk bigint NOT NULL DEFAULT nextval('infraestructuras.sg_lock_pk_seq'::regclass),lck_entidad character varying(45), CONSTRAINT sg_lock_pkey PRIMARY KEY (lck_pk) );
    COMMENT ON TABLE infraestructuras.sg_lock IS 'Tabla para impedir nros correlativos iguales con usuarios concurrentes.';
    COMMENT ON COLUMN infraestructuras.sg_lock.lck_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN infraestructuras.sg_lock.lck_entidad IS 'Referencia a la entidad a bloquear.';

-- inserto entidad a bloquear
insert into infraestructuras.sg_lock(lck_entidad) values ('SgInmueblesSedes'); 

-- columna con nro correlativo autogenerado
ALTER TABLE infraestructuras.sg_inmuebles_sedes ADD tis_num_correlativo bigint NULL;
ALTER TABLE infraestructuras.sg_inmuebles_sedes_aud ADD tis_num_correlativo bigint NULL;

-- migrar nros correlativos actuales
update infraestructuras.sg_inmuebles_sedes
set tis_num_correlativo=aux.rnum
from 
(select ins.tis_pk,ins.tis_sede_fk,row_number() OVER (PARTITION BY ins.tis_sede_fk ORDER BY ins.tis_pk) AS rnum from infraestructuras.sg_inmuebles_sedes ins order by ins.tis_pk) as aux
where aux.tis_pk=infraestructuras.sg_inmuebles_sedes.tis_pk;

update infraestructuras.sg_inmuebles_sedes_aud
set tis_num_correlativo=aux.rnum
from 
(select ins.tis_pk,ins.tis_sede_fk,row_number() OVER (PARTITION BY ins.tis_sede_fk ORDER BY ins.tis_pk) AS rnum from infraestructuras.sg_inmuebles_sedes ins order by ins.tis_pk) as aux
where aux.tis_pk=infraestructuras.sg_inmuebles_sedes_aud.tis_pk;


ALTER TABLE infraestructuras.sg_inmuebles_sedes ALTER COLUMN tis_codigo DROP NOT NULL;
ALTER TABLE infraestructuras.sg_inmuebles_sedes ALTER COLUMN tis_num_correlativo SET NOT NULL;
ALTER TABLE infraestructuras.sg_inmuebles_sedes_aud ALTER COLUMN tis_codigo DROP NOT NULL;
ALTER TABLE infraestructuras.sg_inmuebles_sedes_aud ALTER COLUMN tis_num_correlativo SET NOT NULL;

ALTER TABLE infraestructuras.sg_inmuebles_sedes ADD tis_activo_fijo bool default false;
ALTER TABLE infraestructuras.sg_inmuebles_sedes_aud ADD tis_activo_fijo bool default false;

-- RelInmuebleItemObraExterior
CREATE SEQUENCE IF NOT EXISTS infraestructuras.sg_rel_inmueble_item_obra_exterior_rix_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS infraestructuras.sg_rel_inmueble_item_obra_exterior (rix_pk bigint NOT NULL DEFAULT nextval('infraestructuras.sg_rel_inmueble_item_obra_exterior_rix_pk_seq'::regclass),rix_inmueble_sede_fk bigint,rix_item_obra_exterior_fk bigint, rix_bueno numeric(3), rix_malo numeric(3), rix_regular numeric(3),  rix_descripcion character varying(255), rix_ult_mod_fecha timestamp without time zone, rix_ult_mod_usuario character varying(45), rix_version integer, CONSTRAINT sg_rel_inmueble_item_obra_exterior_pkey PRIMARY KEY (rix_pk),
CONSTRAINT sg_rix_inmueble_sede_fk FOREIGN KEY (rix_inmueble_sede_fk) REFERENCES infraestructuras.sg_inmuebles_sedes(tis_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,
CONSTRAINT sg_rix_item_obra_exterior_fk FOREIGN KEY (rix_item_obra_exterior_fk) REFERENCES catalogo.sg_inf_item_obra_exterior(ioe_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
COMMENT ON TABLE infraestructuras.sg_rel_inmueble_item_obra_exterior IS 'Tabla para el registro de relinmuebleitemobraexterior.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_item_obra_exterior.rix_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_item_obra_exterior.rix_bueno IS 'Cantidad de bueno.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_item_obra_exterior.rix_malo IS 'Cantidad de malo.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_item_obra_exterior.rix_regular IS 'Cantidad de regular.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_item_obra_exterior.rix_descripcion IS 'Descripcion de registro.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_item_obra_exterior.rix_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_item_obra_exterior.rix_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_item_obra_exterior.rix_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS infraestructuras.sg_rel_inmueble_item_obra_exterior_aud (rix_pk bigint NOT NULL,rix_inmueble_sede_fk bigint,rix_item_obra_exterior_fk bigint,rix_bueno numeric(3), rix_malo numeric(3), rix_regular numeric(3),  rix_descripcion character varying(255), rix_ult_mod_fecha timestamp without time zone, rix_ult_mod_usuario character varying(45), rix_version integer, rev bigint, revtype smallint);
ALTER TABLE infraestructuras.sg_rel_inmueble_item_obra_exterior_aud ADD PRIMARY KEY (rix_pk, rev);


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_REL_INMUEBLE_ITEM_OBRA_EXTERIOR','INF62',  'Crear relacion entre inmueble e item de obra exterior', 7, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_REL_INMUEBLE_ITEM_OBRA_EXTERIOR','INF63',  'Actualizar relacion entre inmueble e item de obra exterior', 7, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_REL_INMUEBLE_ITEM_OBRA_EXTERIOR','INF64',  'Eliminar relacion entre inmueble e item de obra exterior', 7, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_REL_INMUEBLE_ITEM_OBRA_EXTERIOR','INFB14',  'Buscar relacion entre inmueble e item de obra exterior', 7, true, null, null,0);


ALTER TABLE infraestructuras.sg_inmuebles_sedes ADD COLUMN tis_unidad_administrativa_fk bigint;
COMMENT ON COLUMN infraestructuras.sg_inmuebles_sedes.tis_unidad_administrativa_fk IS 'Unidad administrativa inmueble';
ALTER TABLE infraestructuras.sg_inmuebles_sedes_aud ADD COLUMN tis_unidad_administrativa_fk bigint;
ALTER TABLE infraestructuras.sg_inmuebles_sedes ADD CONSTRAINT sg_tis_unidad_administrativa_fk FOREIGN KEY (tis_unidad_administrativa_fk) REFERENCES catalogo.sg_af_unidades_administrativas(uad_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE infraestructuras.sg_inmuebles_sedes ADD COLUMN tis_pertenece_sede boolean;
COMMENT ON COLUMN infraestructuras.sg_inmuebles_sedes.tis_pertenece_sede IS 'Pertenece sede';
ALTER TABLE infraestructuras.sg_inmuebles_sedes_aud ADD COLUMN tis_pertenece_sede boolean;

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('RADIO_BUTTON_UNIDADES_ADMINISTRATIVAS','INF65',  'Habilita radio button en pantalla de inmueble', 7, true, null, null,0);

update infraestructuras.sg_inmuebles_sedes set tis_pertenece_sede=true;

--1.4.12

ALTER TABLE infraestructuras.sg_inmuebles_sedes ALTER COLUMN tis_codigo DROP NOT NULL;
ALTER TABLE infraestructuras.sg_inmuebles_sedes DROP CONSTRAINT tis_codigo_uk;
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_OBRAS_EXTERIORES','INFB15',  'Habilita búsqueda de obras exteriores', 7, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_LEGALIZACION','INFB16',  'Habilita búsqueda legalizacion', 7, true, null, null,0);


--1.4.13
update infraestructuras.sg_inmuebles_sedes set tis_codigo=tis_pk;

CREATE OR REPLACE FUNCTION infraestructuras.update_codigo_inmueble() RETURNS TRIGGER AS
$BODY$
BEGIN
    UPDATE infraestructuras.sg_inmuebles_sedes set tis_codigo= new.tis_pk where tis_pk = new.tis_pk;
    RETURN new;
END;
$BODY$
language plpgsql;


CREATE TRIGGER update_codigo_inmueble_trigger
AFTER INSERT ON infraestructuras.sg_inmuebles_sedes
FOR EACH ROW
EXECUTE PROCEDURE infraestructuras.update_codigo_inmueble();

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('EDITAR_DATOS_INFRAESTRUCTURA','INF66',  'Permite editar los datos: área disponible construcción, terreno principal, vulnerabilidades  ', 7, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('EDITAR_DATOS_LEGALIZACION','INF67',  'Permite editar los datos: área total, valor adquisición, fecha de adquisición, propietario mineducyt, propietario, detalles, descripción, dirección', 7, true, null, null,0);



-- tabla rel inmueble documentos
CREATE SEQUENCE IF NOT EXISTS infraestructuras.sg_rel_inmueble_documentos_rid_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS infraestructuras.sg_rel_inmueble_documento (
rid_pk bigint NOT NULL DEFAULT nextval('infraestructuras.sg_rel_inmueble_documentos_rid_pk_seq'::regclass),
rid_inmueble_sede_fk bigint,
rid_documento_fk bigint,
rid_ult_mod_fecha timestamp without time zone,
rid_ult_mod_usuario character varying(45), rid_version integer,
CONSTRAINT sg_rel_inmueble_documento_pkey PRIMARY KEY (rid_pk), 
CONSTRAINT sg_rid_inmueble_sede_fk FOREIGN KEY (rid_inmueble_sede_fk) REFERENCES infraestructuras.sg_inmuebles_sedes(tis_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,
CONSTRAINT sg_rid_documento_fk FOREIGN KEY (rid_documento_fk) REFERENCES sg_archivos(ach_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
COMMENT ON TABLE infraestructuras.sg_rel_inmueble_documento IS 'Tabla para el registro de rel inmueble documento.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_documento.rid_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_documento.rid_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_documento.rid_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_documento.rid_version IS 'Versión del registro.';

CREATE TABLE IF NOT EXISTS infraestructuras.sg_rel_inmueble_documento_aud (rid_pk bigint NOT NULL,rid_inmueble_sede_fk bigint, rid_documento_fk bigint, rid_ult_mod_fecha timestamp without time zone, rid_ult_mod_usuario character varying(45), rid_version integer, rev bigint, revtype smallint);
ALTER TABLE infraestructuras.sg_rel_inmueble_documento_aud ADD PRIMARY KEY (rid_pk, rev);

-- op. buscar archivos en inmuebles
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_REL_EDIFICIO_DOCUMENTOS','INFB12', 'Habilita buscar relación entre inmueble y documentos', 7, true, null, null,0);
-- ops crud inmuebles documento
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_REL_INMUEBLE_DOCUMENTO','INF38', 'Habilita crear relacion entre inmueble y documentos', 7, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_REL_INMUEBLE_DOCUMENTO','INF39', 'Habilita actualizar relacion entre inmueble y documentos', 7, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_REL_INMUEBLE_DOCUMENTO','INF40', 'Habilita eliminar relacion entre inmueble y documentos', 7, true, null, null,0);

ALTER TABLE infraestructuras.sg_rel_inmueble_documento ADD COLUMN rid_descripcion varchar(4000) NULL;
ALTER TABLE infraestructuras.sg_rel_inmueble_documento_aud ADD COLUMN rid_descripcion varchar(4000) NULL;

ALTER TABLE infraestructuras.sg_rel_inmueble_documento ADD COLUMN rid_tipo_documento_fk bigint;
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_documento.rid_tipo_documento_fk IS 'Clave foránea al tipo documento';
ALTER TABLE infraestructuras.sg_rel_inmueble_documento_aud ADD COLUMN rid_tipo_documento_fk bigint;
ALTER TABLE infraestructuras.sg_rel_inmueble_documento ADD CONSTRAINT sg_tid_tipo_documento_fk FOREIGN KEY (rid_tipo_documento_fk) REFERENCES catalogo.sg_inf_tipo_documento(tid_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE infraestructuras.sg_rel_inmueble_documento ADD COLUMN rid_nombre varchar(245) NULL;
ALTER TABLE infraestructuras.sg_rel_inmueble_documento_aud ADD COLUMN rid_nombre varchar(245) NULL;

--1.4.18
CREATE OR REPLACE FUNCTION infraestructuras.numero_correlativo_inmueble_sede()
 RETURNS trigger
 LANGUAGE plpgsql
AS $function$
begin	
	
	IF NEW.tis_sede_fk is not null THEN
	    UPDATE infraestructuras.sg_inmuebles_sedes set tis_num_correlativo = 
	       (select coalesce( (SELECT MAX(tis_num_correlativo)+1 from infraestructuras.sg_inmuebles_sedes where tis_sede_fk=NEW.tis_sede_fk),1))
	    WHERE tis_pk=NEW.tis_pk;
	else     
		IF NEW.tis_unidad_administrativa_fk is not null THEN
	    UPDATE infraestructuras.sg_inmuebles_sedes set tis_num_correlativo = 
	       (select coalesce( (SELECT MAX(tis_num_correlativo)+1 from infraestructuras.sg_inmuebles_sedes where tis_unidad_administrativa_fk=NEW.tis_unidad_administrativa_fk),1))
	    WHERE tis_pk=NEW.tis_pk;
	    else
	    	UPDATE infraestructuras.sg_inmuebles_sedes set tis_num_correlativo =1;
	    end if;
	end if;
 return new;
END;
$function$


--1.6.0

-- Rel Inmueble Unidad Resp
CREATE SEQUENCE IF NOT EXISTS infraestructuras.sg_rel_inmueble_unidad_resp_riu_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS infraestructuras.sg_rel_inmueble_unidad_resp (riu_pk bigint NOT NULL DEFAULT nextval('infraestructuras.sg_rel_inmueble_unidad_resp_riu_pk_seq'::regclass), 
riu_inmueble_fk bigint, riu_tipo_unidad character varying(50),riu_sede_fk bigint, riu_unidad_administrativa_fk bigint,riu_ult_mod_fecha timestamp without time zone, riu_ult_mod_usuario character varying(45), riu_version integer, 
CONSTRAINT sg_rel_inmueble_unidad_resp_pkey PRIMARY KEY (riu_pk),
CONSTRAINT sg_riu_inmueble_fk FOREIGN KEY (riu_inmueble_fk) REFERENCES infraestructuras.sg_inmuebles_sedes(tis_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,
CONSTRAINT sg_riu_sede_fk FOREIGN KEY (riu_sede_fk) REFERENCES centros_educativos.sg_sedes(sed_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,
CONSTRAINT sg_riu_unidad_administrativa_fk FOREIGN KEY (riu_unidad_administrativa_fk) REFERENCES catalogo.sg_af_unidades_administrativas(uad_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE
);
COMMENT ON TABLE infraestructuras.sg_rel_inmueble_unidad_resp IS 'Tabla para el registro de rel inmueble unidad resp.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_unidad_resp.riu_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_unidad_resp.riu_tipo_unidad IS 'Enumerado, describe si es sede o unidad adminsitrativa.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_unidad_resp.riu_sede_fk IS 'Sede de registro.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_unidad_resp.riu_unidad_administrativa_fk IS 'Unidad administrativa de registro.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_unidad_resp.riu_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_unidad_resp.riu_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN infraestructuras.sg_rel_inmueble_unidad_resp.riu_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS infraestructuras.sg_rel_inmueble_unidad_resp_aud (riu_pk bigint NOT NULL,riu_inmueble_fk bigint, riu_tipo_unidad character varying(50),riu_sede_fk bigint, riu_unidad_administrativa_fk bigint,riu_ult_mod_fecha timestamp without time zone, riu_ult_mod_usuario character varying(45), riu_version integer, rev bigint, revtype smallint);
ALTER TABLE infraestructuras.sg_rel_inmueble_unidad_resp_aud ADD PRIMARY KEY (riu_pk, rev);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_REL_INMUEBLE_UNIDAD_RESP','INF68',  'Crear relacion entre inmueble unidad resp', 7, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_REL_INMUEBLE_UNIDAD_RESP','INF69',  'Actualizar relacion entre inmueble y unidad resp', 7, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_REL_INMUEBLE_UNIDAD_RESP','INF70',  'Eliminar relacion entre inmueble y unidad resp', 7, true, null, null,0);

ALTER TABLE infraestructuras.sg_inmuebles_sedes ADD COLUMN tis_existe_otras_sedes_unidad_admi boolean default false;
COMMENT ON COLUMN infraestructuras.sg_inmuebles_sedes.tis_existe_otras_sedes_unidad_admi IS 'Boolean indica si existen otras sedes en inmueble';
ALTER TABLE infraestructuras.sg_inmuebles_sedes_aud ADD COLUMN tis_existe_otras_sedes_unidad_admi boolean;



CREATE OR REPLACE FUNCTION infraestructuras.insert_rel_inmueble_unidad_resp() RETURNS TRIGGER AS
$BODY$
BEGIN
    UPDATE infraestructuras.sg_inmuebles_sedes set tis_existe_otras_sedes_unidad_admi = true where tis_pk = new.riu_inmueble_fk;
    RETURN new;
END;
$BODY$
language plpgsql;

CREATE TRIGGER insert_rel_inmueble_unidad_resp_trigger
AFTER INSERT ON infraestructuras.sg_rel_inmueble_unidad_resp
FOR EACH ROW
EXECUTE PROCEDURE infraestructuras.insert_rel_inmueble_unidad_resp();

--1.6.1
CREATE TABLE IF NOT EXISTS infraestructuras.sg_edificio_rel_inmueble_unidad_resp (edi_pk bigint NOT NULL,riu_pk bigint NOT NULL, CONSTRAINT sg_edificio_rel_inmueble_unidad_resp_edificio_fk FOREIGN KEY (edi_pk) REFERENCES infraestructuras.sg_edificios (edi_pk), CONSTRAINT sg_edificio_rel_inmueble_unidad_resp_rel_inmueble_unidad_resp_fk  FOREIGN KEY (riu_pk) REFERENCES infraestructuras.sg_rel_inmueble_unidad_resp(riu_pk));
CREATE TABLE IF NOT EXISTS infraestructuras.sg_edificio_rel_inmueble_unidad_resp_aud(edi_pk bigint NOT NULL,riu_pk bigint NOT NULL, rev bigint, revtype smallint);
