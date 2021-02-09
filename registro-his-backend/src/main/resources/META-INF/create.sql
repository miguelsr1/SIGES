INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('TIENE_ACCESO_REGISTRO_HISTORICO','W8',  'Descripción', 4, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_RH_ETIQUETA','HB1',  '', 8, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_RH_ETIQUETA','H1',  '', 8, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_RH_ETIQUETA','H2',  '', 8, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_RH_ETIQUETA','H3',  '', 8, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_RH_ETIQUETAS','HM1',  '', 8, true, null, null,0);

CREATE SCHEMA IF NOT EXISTS registro_historico;

CREATE SEQUENCE IF NOT EXISTS registro_historico.sg_rh_etiqueta_eti_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS registro_historico.sg_rh_etiqueta (eti_pk bigint NOT NULL DEFAULT nextval('registro_historico.sg_rh_etiqueta_eti_pk_seq'::regclass), eti_nombre character varying(255), eti_habilitado boolean, eti_apellido character varying(255), eti_dui character varying(255), eti_nie character varying(255), eti_libro integer, eti_pagina integer, eti_nombre_sede character varying(600), eti_anio integer, eti_ult_mod_fecha timestamp without time zone, eti_ult_mod_usuario character varying(45), eti_version integer, 
CONSTRAINT sg_rh_etiqueta_pkey PRIMARY KEY (eti_pk), CONSTRAINT eti_nom_ape_lib_pag_anio_uk UNIQUE (eti_nombre,eti_apellido,eti_libro,eti_pagina,eti_anio));		
COMMENT ON TABLE registro_historico.sg_rh_etiqueta IS 'Tabla para el registro de etiquetas para los libros de registro históricos.';
COMMENT ON COLUMN registro_historico.sg_rh_etiqueta.eti_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN registro_historico.sg_rh_etiqueta.eti_apellido IS 'Apellido del estudiante que está registrado en el libro al que pertenece la etiqueta.';
COMMENT ON COLUMN registro_historico.sg_rh_etiqueta.eti_nombre IS 'Nombre del estudiante que está registrado en el libro al que pertenece la etiqueta.';
COMMENT ON COLUMN registro_historico.sg_rh_etiqueta.eti_dui IS 'Número de DUI del estudiante que está registrado en libros al que pertenece la etiqueta.';
COMMENT ON COLUMN registro_historico.sg_rh_etiqueta.eti_nie IS 'Número de NIE del estudiante que está registrado en libros al que pertenece la etiqueta.';
COMMENT ON COLUMN registro_historico.sg_rh_etiqueta.eti_libro IS 'Número de identificación establecido por MINED';
COMMENT ON COLUMN registro_historico.sg_rh_etiqueta.eti_pagina IS 'Número de la página del libro donde están el registro del estudiante.';
COMMENT ON COLUMN registro_historico.sg_rh_etiqueta.eti_nombre_sede IS 'Nombre de la sede educativa que está registrada en libro.';
COMMENT ON COLUMN registro_historico.sg_rh_etiqueta.eti_anio IS 'Año al que pertenece los registros que contiene en el libro.';
COMMENT ON COLUMN registro_historico.sg_rh_etiqueta.eti_habilitado IS 'Indica si la etiqueta se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN registro_historico.sg_rh_etiqueta.eti_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN registro_historico.sg_rh_etiqueta.eti_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN registro_historico.sg_rh_etiqueta.eti_version IS 'Versión del tipo de calendario escolar';		
CREATE TABLE IF NOT EXISTS registro_historico.sg_rh_etiqueta_aud (eti_pk bigint NOT NULL, eti_nombre character varying(255), eti_habilitado boolean, eti_apellido character varying(255), eti_dui character varying(255), eti_nie character varying(255), eti_libro integer, eti_pagina integer, eti_nombre_sede character varying(600), eti_anio integer, eti_ult_mod_fecha timestamp without time zone, eti_ult_mod_usuario character varying(45), eti_version integer, rev bigint, revtype smallint);



ALTER TABLE registro_historico.sg_rh_etiqueta ADD COLUMN eti_sede_fk bigint;
ALTER TABLE registro_historico.sg_rh_etiqueta ADD COLUMN eti_nivel_fk bigint;
ALTER TABLE registro_historico.sg_rh_etiqueta ADD COLUMN eti_municipio_fk bigint;
ALTER TABLE registro_historico.sg_rh_etiqueta ADD COLUMN eti_folio character varying(25);

ALTER TABLE registro_historico.sg_rh_etiqueta_aud ADD COLUMN eti_sede_fk bigint;
ALTER TABLE registro_historico.sg_rh_etiqueta_aud ADD COLUMN eti_nivel_fk bigint;
ALTER TABLE registro_historico.sg_rh_etiqueta_aud ADD COLUMN eti_municipio_fk bigint;
ALTER TABLE registro_historico.sg_rh_etiqueta_aud ADD COLUMN eti_folio character varying(25);

COMMENT ON COLUMN registro_historico.sg_rh_etiqueta.eti_sede_fk IS 'Clave foránea que hace referencia a sede educativa.';
COMMENT ON COLUMN registro_historico.sg_rh_etiqueta.eti_nivel_fk IS 'Clave foránea que hace referencia a nivel educativo.';
COMMENT ON COLUMN registro_historico.sg_rh_etiqueta.eti_municipio_fk IS 'Clave foránea que hace referencia a municipio.';
COMMENT ON COLUMN registro_historico.sg_rh_etiqueta.eti_folio IS 'Número de folio asignado por MINED';

-- 0.1.1

ALTER TABLE registro_historico.sg_rh_etiqueta ADD CONSTRAINT sg_eti_sede_fkey FOREIGN KEY (eti_sede_fk) REFERENCES centros_educativos.sg_sedes(sed_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE registro_historico.sg_rh_etiqueta ADD CONSTRAINT sg_eti_nivel_fkey FOREIGN KEY (eti_nivel_fk) REFERENCES centros_educativos.sg_niveles(niv_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;


-- 0.1.4

-- Cambio de nombre de los campos siguientes.
alter table registro_historico.sg_rh_etiqueta rename column eti_nivel_fk to eti_nivel;
alter table registro_historico.sg_rh_etiqueta_aud rename column eti_nivel_fk to eti_nivel;

-- Nuevos campos
ALTER TABLE registro_historico.sg_rh_etiqueta ADD COLUMN eti_fecha timestamp without time zone;
ALTER TABLE registro_historico.sg_rh_etiqueta ADD COLUMN eti_correlativo integer;
ALTER TABLE registro_historico.sg_rh_etiqueta ADD COLUMN eti_folio_mined character varying(25);
ALTER TABLE registro_historico.sg_rh_etiqueta ADD COLUMN eti_link_archivo character varying(500);

ALTER TABLE registro_historico.sg_rh_etiqueta_aud ADD COLUMN eti_fecha  timestamp without time zone;
ALTER TABLE registro_historico.sg_rh_etiqueta_aud ADD COLUMN eti_correlativo integer;
ALTER TABLE registro_historico.sg_rh_etiqueta_aud ADD COLUMN eti_folio_mined character varying(25);
ALTER TABLE registro_historico.sg_rh_etiqueta_aud ADD COLUMN eti_link_archivo character varying(500);


COMMENT ON COLUMN registro_historico.sg_rh_etiqueta.eti_fecha IS 'Fecha del registro.';
COMMENT ON COLUMN registro_historico.sg_rh_etiqueta.eti_correlativo IS 'Número correlativo ingresado por el usuario';
COMMENT ON COLUMN registro_historico.sg_rh_etiqueta.eti_folio_mined IS 'Número de folio';
COMMENT ON COLUMN registro_historico.sg_rh_etiqueta.eti_link_archivo IS 'Almacena un ruta relativa donde se encuentra el archivo a descargar';

-- 2.0.0

-- Nuevo campo
ALTER TABLE registro_historico.sg_rh_etiqueta ADD COLUMN eti_nombre_libro character varying(100);
ALTER TABLE registro_historico.sg_rh_etiqueta_aud ADD COLUMN eti_nombre_libro character varying(100);

COMMENT ON COLUMN registro_historico.sg_rh_etiqueta.eti_nombre_libro IS 'Nombre del archivo que corresponde al libro digitalizado.';


ALTER TABLE registro_historico.sg_rh_etiqueta ADD COLUMN eti_pagina bigint;
ALTER TABLE registro_historico.sg_rh_etiqueta_aud ADD COLUMN eti_pagina bigint;

COMMENT ON COLUMN registro_historico.sg_rh_etiqueta.eti_pagina IS 'Clave foránea que hace referencia a la página del registro histórico.';

ALTER TABLE registro_historico.sg_rh_etiqueta drop constraint eti_nom_ape_lib_pag_anio_uk cascade;



CREATE SEQUENCE IF NOT EXISTS registro_historico.sg_rh_pagina_pag_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS registro_historico.sg_rh_pagina (
	pag_pk bigint NOT NULL DEFAULT nextval('registro_historico.sg_rh_pagina_pag_pk_seq'::regclass), 
	pag_habilitado boolean, 
	pag_libro integer, 
	pag_pagina integer, 
	pag_anio integer, 
	pag_ult_mod_fecha timestamp without time zone, 
	pag_ult_mod_usuario character varying(45), 
	pag_version integer, 
	pag_nivel bigint,
	pag_nombre_libro character varying(100),
	pag_ruta character varying(100),
CONSTRAINT sg_rh_pagina_pkey PRIMARY KEY (pag_pk), 
CONSTRAINT pag_lib_pag_anio_nivel_uk UNIQUE (pag_libro,pag_pagina,pag_anio,pag_nivel));
		
COMMENT ON TABLE registro_historico.sg_rh_pagina IS 'Tabla para el registro de etiquetas para los libros de registro históricos.';
COMMENT ON COLUMN registro_historico.sg_rh_pagina.pag_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN registro_historico.sg_rh_pagina.pag_libro IS 'Número de identificación establecido por MINED';
COMMENT ON COLUMN registro_historico.sg_rh_pagina.pag_pagina IS 'Número de la página del libro donde están el registro del estudiante.';
COMMENT ON COLUMN registro_historico.sg_rh_pagina.pag_anio IS 'Año al que pertenece los registros que contiene en el página.';
COMMENT ON COLUMN registro_historico.sg_rh_pagina.pag_habilitado IS 'Indica si la etiqueta se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN registro_historico.sg_rh_pagina.pag_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN registro_historico.sg_rh_pagina.pag_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN registro_historico.sg_rh_pagina.pag_version IS 'Versión del tipo de calendario escolar';		
COMMENT ON COLUMN registro_historico.sg_rh_pagina.pag_nombre_libro IS 'Nombre del archivo que corresponde al libro digitalizado.';
COMMENT ON COLUMN registro_historico.sg_rh_pagina.pag_ruta IS 'Ruta de donde se encuetra el página.';
COMMENT ON COLUMN registro_historico.sg_rh_pagina.pag_nivel IS 'Clave foránea que hace referencia a nivel educativo.';


CREATE TABLE IF NOT EXISTS registro_historico.sg_rh_pagina_aud (
	pag_pk bigint NOT NULL, 
	pag_habilitado boolean, 
	pag_libro integer, 	
	pag_pagina integer, 
	pag_anio integer, 
	pag_ult_mod_fecha timestamp without time zone, 
	pag_ult_mod_usuario character varying(45), 
	pag_version integer, 
	pag_nivel bigint,
	pag_nombre_libro character varying(100), 
	pag_ruta character varying(100),
	rev bigint, 
	revtype smallint);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_DATOS_RH_ETIQUETA','HP1',  '', 8, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_RH_PAGINA','HB2',  '', 8, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_DATOS_RH_PAGINA','HP3',  '', 8, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('EDITAR_DATOS_RH_PAGINA','HP4',  '', 8, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_RH_PAGINA','H4',  '', 8, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_RH_PAGINA','H5',  '', 8, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_RH_PAGINA','H6',  '', 8, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_RH_PAGINA','HM2',  '', 8, true, null, null,0);

--0.2.2
 alter table registro_historico.sg_rh_etiqueta alter column eti_pagina type bigint;
 alter table registro_historico.sg_rh_etiqueta_aud alter column eti_pagina type bigint;
  

--0.3.0

ALTER TABLE registro_historico.sg_rh_etiqueta ADD CONSTRAINT sg_eti_pagina_fk FOREIGN KEY (eti_pagina) REFERENCES registro_historico.sg_rh_pagina(pag_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;
-- Incorporaciones
CREATE SEQUENCE IF NOT EXISTS registro_historico.sg_incorporacion_inc_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS registro_historico.sg_incorporaciones (
inc_pk bigint NOT NULL DEFAULT nextval('registro_historico.sg_incorporacion_inc_pk_seq'::regclass),
inc_dui character varying(50),  
inc_carne_residente character varying(100), 
inc_pasaporte character varying (100),
inc_pasaporte_pais_emisor integer,
inc_primer_nombre character varying(100), 
inc_segundo_nombre character varying(100), 
inc_tercer_nombre character varying(100), 
inc_primer_apellido character varying(100), 
inc_segundo_apellido character varying(100), 
inc_tercer_apellido character varying(100),
inc_nombre_busqueda character varying (650),
inc_fecha_nacimiento timestamp without time zone,
inc_sexo_fk integer,
inc_estado_civil_fk integer,
inc_nacionalidad_fk integer,
inc_nombre_sede character varying (500),
inc_ult_grado_estudio character varying (250),  
inc_anio_estudio integer,
 inc_ciudad character varying(250),   
 inc_numero_tramite character varying(250),
 inc_numero_resolucion character varying(250),
 inc_fecha_aprobacion timestamp without time zone,
 inc_ult_mod_fecha timestamp without time zone,
 inc_ult_mod_usuario character varying(45),
 inc_version integer,
 CONSTRAINT sg_incorporacion_pkey PRIMARY KEY (inc_pk),
 CONSTRAINT inc_dui_uk UNIQUE (inc_dui),
 CONSTRAINT inc_carne_residente_uk UNIQUE (inc_carne_residente),
 CONSTRAINT inc_pasaporte_pais_uk UNIQUE (inc_pasaporte, inc_pasaporte_pais_emisor));

ALTER TABLE registro_historico.sg_incorporaciones ADD CONSTRAINT inc_numero_resolucion_uk UNIQUE (inc_numero_resolucion);
ALTER TABLE registro_historico.sg_incorporaciones ADD CONSTRAINT inc_numero_tramite_uk UNIQUE (inc_numero_tramite);

CREATE TABLE IF NOT EXISTS registro_historico.sg_incorporaciones_aud (
inc_pk bigint NOT NULL,
inc_dui character varying(50),  
inc_carne_residente character varying(100), 
inc_pasaporte character varying (100),
inc_pasaporte_pais_emisor integer,
inc_primer_nombre character varying(100), 
inc_segundo_nombre character varying(100), 
inc_tercer_nombre character varying(100), 
inc_primer_apellido character varying(100), 
inc_segundo_apellido character varying(100), 
inc_tercer_apellido character varying(100),
inc_nombre_busqueda character varying (650),
inc_fecha_nacimiento timestamp without time zone,
inc_sexo_fk integer,
inc_estado_civil_fk integer,
inc_nacionalidad_fk integer,
inc_nombre_sede character varying (500),
inc_ult_grado_estudio character varying (250),  
inc_anio_estudio integer,
 inc_ciudad character varying(250),   
 inc_numero_tramite character varying(250),
 inc_numero_resolucion character varying(250),
 inc_fecha_aprobacion timestamp without time zone,
 inc_ult_mod_fecha timestamp without time zone,
 inc_ult_mod_usuario character varying(45),
 inc_version integer,
rev bigint, 
revtype smallint);
ALTER TABLE registro_historico.sg_incorporaciones_aud ADD PRIMARY KEY (inc_pk, rev);






INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_INCORPORACIONES','H7',  '', 8, true, null, null,0);	
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_INCORPORACIONES','H8',  '', 8, true, null, null,0);	
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_INCORPORACIONES','H9',  '', 8, true, null, null,0);	
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_INCORPORACIONES','HB3',  '', 8, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_INCORPORACIONES','HM3',  '', 8, true, null, null,0);