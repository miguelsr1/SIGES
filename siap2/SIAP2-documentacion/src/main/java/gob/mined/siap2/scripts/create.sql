-- 6.1.0


ALTER TABLE ss_insumo ADD COLUMN ins_aplica_a_centros_educativos boolean;
ALTER TABLE ss_insumo_hist ADD COLUMN ins_aplica_a_centros_educativos boolean;

UPDATE ss_insumo set ins_aplica_a_centros_educativos = false;

INSERT INTO ss_textos (tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id) values (nextval('seq_textos'), 'labels.aplicaACentrosEducativos', true, '2019-10-08 17:00:00', 'SCRIPT', 'admin', 'Aplica a centros educativos', 0, 1);

INSERT INTO public.ss_unidad_tecnica(
	uni_id, uni_nombre, uni_ult_mod, uni_ult_usuario, uni_version, uni_padre, uni_direccion, uni_codigo, uni_usuario, uni_lista_padres)
	VALUES (nextval('seq_unidad_tec'), 'Raíz', '2019-10-10 15:00:00', 'admin', 0, null, null, null, null, null);


-- 6.1.1


ALTER TABLE ss_anio_fiscal ALTER ani_ejecucion TYPE bool USING ani_ejecucion::text::boolean;
ALTER TABLE ss_anio_fiscal_hist ALTER ani_ejecucion TYPE bool USING ani_ejecucion::text::boolean;


-- 6.3.0

---------------- CATEGORIA PRESUPUESTO ESCOLAR ----------------

CREATE TABLE siap2.ss_categoria_presupuesto_escolar(
	cpe_id bigint primary key,
	cpe_codigo varchar,
	cpe_nombre varchar,
	cpe_habilitado boolean,
	cpe_ult_mod timestamp without time zone,
	cpe_ult_usuario varchar,
	cpe_version bigint
);


CREATE SEQUENCE IF NOT EXISTS siap2.seq_cat_presupuesto_escolar INCREMENT 1 START 1 MINVALUE 1 no maxvalue CACHE 1 no cycle;

CREATE TABLE siap2.ss_categoria_presupuesto_escolar_hist(
	cpe_id bigint,
	cpe_codigo varchar,
	cpe_nombre varchar,
	cpe_habilitado boolean,
	cpe_ult_mod timestamp without time zone,
	cpe_ult_usuario varchar,
	cpe_version bigint,
	start_date timestamp without time zone,
	end_date timestamp without time zone
);


---------------- AREAS INVERSION ----------------


CREATE TABLE siap2.ss_areas_inversion(
	ai_id bigint primary key,
	ai_codigo varchar(5) unique not null,
	ai_nombre varchar(100) unique not null,
	ai_habilitado boolean default true,
	ai_asociado_plan_anual_compras boolean default true,
	ai_descripcion varchar(4000),
	ai_orden integer,
	ai_area_padre bigint references siap2.ss_areas_inversion(ai_id),
	ai_ult_mod timestamp without time zone,
	ai_ult_usuario varchar,
	ai_version bigint
);

CREATE SEQUENCE siap2.seq_areas_inversion no maxvalue minvalue 1 no cycle start 1;

CREATE TABLE siap2.ss_areas_inversion_hist(
	ai_id bigint,
	ai_codigo varchar(5),
	ai_nombre varchar(100),
	ai_habilitado boolean default true,
	ai_asociado_plan_anual_compras boolean default true,
	ai_descripcion varchar(4000),
	ai_orden integer,
	ai_area_padre bigint,
	ai_ult_mod timestamp without time zone,
	ai_ult_usuario varchar,
	ai_version bigint,
	start_date timestamp without time zone,
	end_date timestamp without time zone
);




---------------- TIPO CREDITO ----------------


CREATE TABLE siap2.ss_tipo_credito(
	tc_id bigint PRIMARY KEY,
	tc_cod varchar,
	tc_nombre varchar,
	tc_habilitado boolean,
	tc_orden integer,
	tc_ult_mod timestamp WITHOUT time ZONE,
	tc_ult_usuario varchar,
	tc_version bigint
);
CREATE TABLE siap2.ss_tipo_credito_hist(
	tc_id bigint PRIMARY KEY,
	tc_cod varchar,
	tc_nombre varchar,
	tc_habilitado boolean,
	tc_orden integer,
	tc_ult_mod timestamp WITHOUT time ZONE,
	tc_ult_usuario varchar,
	tc_version bigint,
	start_date timestamp without time zone,
	end_date timestamp without time zone
);



CREATE SEQUENCE siap2.seq_tipo_credito no maxvalue minvalue 1 no cycle start 1;

insert into siap2.ss_tipo_credito(tc_id, tc_cod, tc_nombre, tc_habilitado, tc_ult_mod, tc_ult_usuario, tc_version) 
values(NEXTVAL('siap2.seq_tipo_credito'), 'Transferencia', 'Transferencia', true, current_timestamp, 'admin', 0);

insert into siap2.ss_tipo_credito(tc_id, tc_cod, tc_nombre, tc_habilitado, tc_ult_mod, tc_ult_usuario, tc_version) 
values(NEXTVAL('siap2.seq_tipo_credito'), 'Donación', 'Donación', true, current_timestamp, 'admin', 0);

insert into siap2.ss_tipo_credito(tc_id, tc_cod, tc_nombre, tc_habilitado, tc_ult_mod, tc_ult_usuario, tc_version) 
values(NEXTVAL('siap2.seq_tipo_credito'), 'Fondos Propios', 'Fondos Propios', true, current_timestamp, 'admin', 0);





---------------- COMPONENTE DE GESTION DE PRESUPUESTO ESCOLAR ----------------

CREATE TABLE siap2.ss_ges_pres_es(
	ges_id bigint primary key,
	ges_cod varchar,
	ges_nombre varchar,
	ges_descripcion varchar,
	ges_habilitado boolean,
	ges_tipo integer,
	ges_parametro integer,
	ges_cant_uni_med numeric(20,2),
	ges_uni_med integer,
	ges_mto_minimo boolean,
	ges_ctdad_anio integer,
	ges_fuente_financiamiento bigint references siap2.ss_fuente_financiamiento(fue_id),
	ges_fuente_recursos bigint references siap2.ss_fuente_recursos(fue_id),
	ges_tipo_credito integer references siap2.ss_tipo_credito(tc_id),
	ges_unidad_tecnica integer references siap2.ss_unidad_tecnica(uni_id),
	ges_usuario_responsable integer references siap2.ss_usuario(usu_id),
	ges_categoria_componente integer references siap2.ss_categoria_presupuesto_escolar(cpe_id),
	ges_areas_inversion integer references siap2.ss_areas_inversion(ai_id),
	ges_centros_educativos boolean,
	ges_centros_educativos_privados_sub boolean,
	ges_centros_educativos_privados_no_sub boolean,
	ges_circulos_familia boolean,
	ges_sedes_educame boolean,
	ges_circulos_alfabetizacion boolean,
	ges_incluir_sedes_adscritas boolean,
	ges_tipo_ejecucion integer,
	ges_ult_mod timestamp without time zone,
	ges_ult_usuario varchar,
	ges_version bigint
);
CREATE SEQUENCE siap2.seq_ges_pres_es no maxvalue minvalue 1 no cycle start 1;


CREATE TABLE siap2.ss_ges_pres_es_hist(
	ges_id bigint,
	ges_cod varchar,
	ges_nombre varchar,
	ges_descripcion varchar,
	ges_habilitado boolean,
	ges_tipo integer,
	ges_parametro integer,
	ges_cant_uni_med numeric(20,2),
	ges_uni_med integer,
	ges_mto_minimo boolean,
	ges_ctdad_anio integer,
	ges_fuente_financiamiento bigint,
	ges_fuente_recursos bigint ,
	ges_tipo_credito integer,
	ges_unidad_tecnica integer,
	ges_usuario_responsable integer,
	ges_categoria_componente integer,
	ges_areas_inversion integer,
	ges_tipo_ejecucion integer,
	ges_centros_educativos boolean,
	ges_centros_educativos_privados_sub boolean,
	ges_centros_educativos_privados_no_sub boolean,
	ges_circulos_familia boolean,
	ges_sedes_educame boolean,
	ges_circulos_alfabetizacion boolean,
	ges_incluir_sedes_adscritas boolean,
	ges_ult_mod timestamp without time zone,
	ges_ult_usuario varchar,
	ges_version bigint,
	start_date timestamp without time zone,
	end_date timestamp without time zone
);


---------------- RELACION: COMPONENTE <-> ANIO FISCAL ----------------

CREATE TABLE siap2.ss_rel_ges_pres_es_anio_fiscal(
	id bigint primary key,
	id_ges_pres_es bigint references siap2.ss_ges_pres_es(ges_id),
	id_anio_fiscal bigint references siap2.ss_anio_fiscal (ani_id),
	cifrado_presupuestario varchar(100),
	CONSTRAINT unk UNIQUE(id_ges_pres_es, id_anio_fiscal)
);

CREATE SEQUENCE siap2.seq_rel_gespreses_aniofiscal no maxvalue minvalue 1 no cycle start 1;


CREATE TABLE siap2.ss_rel_ges_pres_es_anio_fiscal_hist(
	id bigint primary key,
	id_ges_pres_es bigint references siap2.ss_ges_pres_es(ges_id),
	id_anio_fiscal bigint references siap2.ss_anio_fiscal (ani_id),
	cifrado_presupuestario varchar(100),
	start_date timestamp without time zone,
	end_date timestamp without time zone
);


---------------- RELACION: AREAS DE INVERSION <-> INSUMO ----------------

CREATE TABLE siap2.ss_rel_areas_inversion_insumo(
	id bigint primary key,
	id_area_inversion bigint references siap2.ss_areas_inversion(ai_id),
	id_insumo bigint references siap2.ss_insumo (ins_id),
	CONSTRAINT unk_rel_areas_inversion_insumo UNIQUE(id_area_inversion, id_insumo)
);

CREATE SEQUENCE siap2.seq_rel_areas_inver_insumo no maxvalue minvalue 1 no cycle start 1;

CREATE TABLE siap2.ss_rel_areas_inversion_insumo_hist(
	id bigint primary key,
	id_area_inversion bigint references siap2.ss_areas_inversion(ai_id),
	id_insumo bigint references siap2.ss_insumo (ins_id),
	start_date timestamp without time zone,
	end_date timestamp without time zone
);


---------------- RUBRO ----------------

CREATE TABLE siap2.ss_rubro(
	ru_id bigint primary key,
	ru_nombre varchar,
	ru_codigo varchar unique,
	ru_habilitado boolean,
	ru_ult_mod timestamp without time zone,
	ru_ult_usuario varchar,
	ru_version bigint
);

CREATE SEQUENCE siap2.seq_ss_rubro no maxvalue minvalue 1 no cycle start 1;

CREATE TABLE siap2.ss_rubro_hist(
	ru_id bigint,
	ru_nombre varchar,
	ru_codigo varchar,
	ru_habilitado boolean,
	ru_ult_mod timestamp without time zone,
	ru_ult_usuario varchar,
	ru_version bigint,
	start_date timestamp without time zone,
	end_date timestamp without time zone
);

---------------- SUBRUBRO ----------------

CREATE TABLE siap2.ss_sub_rubro(
	sru_id bigint primary key,
	sru_rubro bigint references siap2.ss_rubro(ru_id),
	sru_nombre varchar,
	sru_codigo varchar unique,
	sru_habilitado boolean,
	sru_ult_mod timestamp without time zone,
	sru_ult_usuario varchar,
	sru_version bigint
);

CREATE SEQUENCE siap2.seq_ss_sub_rubro no maxvalue minvalue 1 no cycle start 1;

CREATE TABLE siap2.ss_sub_rubro_hist(
	sru_id bigint,
	sru_rubro bigint,
	sru_nombre varchar,
	sru_codigo varchar,
	sru_habilitado boolean,
	sru_ult_mod timestamp without time zone,
	sru_ult_usuario varchar,
	sru_version bigint,
	start_date timestamp without time zone,
	end_date timestamp without time zone
);


---------------- BENEFICIARIOS ----------------

CREATE TABLE siap2.ss_beneficiarios(
	b_id bigint,
	b_organizacion_curricular bigint,
	b_nivel bigint,
	b_ciclo bigint,
	b_modalidad bigint,
	b_modalidad_atencion bigint,
	b_sub_modalidad bigint,
	b_ges_pres_es bigint, 
	v_valor boolean
);

CREATE SEQUENCE siap2.seq_beneficiarios no maxvalue minvalue 1 no cycle start 1;

CREATE TABLE siap2.ss_beneficiarios_hist(
	b_id bigint,
	b_organizacion_curricular bigint,
	b_nivel bigint,
	b_ciclo bigint,
	b_modalidad bigint,
	b_modalidad_atencion bigint,
	b_sub_modalidad bigint,
	v_valor boolean,
	b_ges_pres_es bigint,
	start_date timestamp without time zone,
	end_date timestamp without time zone
);





INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.siap2.seq_textos'), 'labels.ComponentePresupuestoEscolar', true, now(), 'ANP-AGENPORT', 'admin', 'Componente de gestión escolar', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.menu.PresupuestoEscolar', true, now(), 'ANP-AGENPORT', 'admin', 'Componente de Gestión de Presupuesto Escolar', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ComponentePresupuestoEscolar.MontoFijoParaTodosLosCentros', true, now(), 'ANP-AGENPORT', 'admin', 'Monto Fijo Para Todos Los Centros', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ComponentePresupuestoEscolar.MontoFijoPorCentro', true, now(), 'ANP-AGENPORT', 'admin', 'Monto Fijo PorCentro', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.Parametro', true, now(), 'ANP-AGENPORT', 'admin', 'Parámetro', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ComponentePresupuestoEscolar.MatriculaPorNivel', true, now(), 'ANP-AGENPORT', 'admin', 'Matricula Por Nivel', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ComponentePresupuestoEscolar.CantidadDocentes', true, now(), 'ANP-AGENPORT', 'admin', 'Cantidad Docentes', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.CantidadUnidadMedida', true, now(), 'ANP-AGENPORT', 'admin', 'Cantidad por unidad de medida', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ComponentePresupuestoEscolar.Docente', true, now(), 'ANP-AGENPORT', 'admin', 'Docente', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ComponentePresupuestoEscolar.Estudiante', true, now(), 'ANP-AGENPORT', 'admin', 'Estudiante', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ComponentePresupuestoEscolar.Centro', true, now(), 'ANP-AGENPORT', 'admin', 'Centro', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.MontoMinimo', true, now(), 'ANP-AGENPORT', 'admin', 'Monto Mínimo', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.CantidadAnio', true, now(), 'ANP-AGENPORT', 'admin', 'Cantidad al año', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.CategoriaPresupuestoEscolar', true, now(), 'ANP-AGENPORT', 'admin', 'Categorías de presupuesto escolar', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.CentrosEducativosOficiales', true, now(), 'ANP-AGENPORT', 'admin', 'Centros educativos oficiales', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.CentrosSubvencionados', true, now(), 'ANP-AGENPORT', 'admin', 'Centros educativos privados subvencionados', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.CentrosNoSubvecionados', true, now(), 'ANP-AGENPORT', 'admin', 'Centros educativos privados no subvencionados', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.CirculosFamilia', true, now(), 'ANP-AGENPORT', 'admin', 'Círculos de familia', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.SedesEducame', true, now(), 'ANP-AGENPORT', 'admin', 'Sedes edúcame', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.CirculosAlfabetizacion', true, now(), 'ANP-AGENPORT', 'admin', 'Círculos de alfabetización', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.TiposdeSede', true, now(), 'ANP-AGENPORT', 'admin', 'Tipos de Sede', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.SedesAdscritas', true, now(), 'ANP-AGENPORT', 'admin', 'Sedes Adscritas', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.IncluirSedesAdscritas', true, now(), 'ANP-AGENPORT', 'admin', 'Incluir sedes adscritas ', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.TipoEjecucion', true, now(), 'ANP-AGENPORT', 'admin', 'Tipo de ejecución', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'TipoEjecucion.NIVEL_CENTRAL', true, now(), 'ANP-AGENPORT', 'admin', 'Nivel central', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'TipoEjecucion.DEPARTAMENTAL', true, now(), 'ANP-AGENPORT', 'admin', 'Departamental', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'TipoEjecucion.CENTRO_EDUCATIVO', true, now(), 'ANP-AGENPORT', 'admin', 'Centro educativo', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.CifradoPresupuestario', true, now(), 'ANP-AGENPORT', 'admin', 'Cifrado presupuestario', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.TipoCredito', true, now(), 'ANP-AGENPORT', 'admin', 'Tipo de crédito', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.menu.TipoCredito', true, now(), 'ANP-AGENPORT', 'admin', 'Tipos de Crédito', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.UsuarioResponsable', true, now(), 'ANP-AGENPORT', 'admin', 'Usuario Responsable', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.CrearGestionPresupuestoEscolar', true, now(), 'ANP-AGENPORT', 'admin', 'Crear gestión de presupuesto escolar', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.EditarGestionPresupuestoEscolar', true, now(), 'ANP-AGENPORT', 'admin', 'Editar gestión de presupuesto escolar', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.AreasInversion', true, now(), 'ANP-AGENPORT', 'admin', 'Áreas de Inversión', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.SubAreasInversion', true, now(), 'ANP-AGENPORT', 'admin', 'Subáreas de Inversión', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.CrearSubAreasInversion', true, now(), 'ANP-AGENPORT', 'admin', 'Crear Subáreas de Inversión', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.CrearAreasInversion', true, now(), 'ANP-AGENPORT', 'admin', 'Crear Áreas de Inversión', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.RelacionAII', true, now(), 'ANP-AGENPORT', 'admin', 'Insumos asociados', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERROR_CODIGO_INSUMO_REPETIDO', true, now(), 'ANP-AGENPORT', 'admin', 'Error: Ya existe una subárea con dicho código de insumo', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_CODIGO_DE_INSUMO_INGRESADO_NO_ES_VALIDO', true, now(), 'ANP-AGENPORT', 'admin', 'Error: El código de Insumo ingresado es inválido', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_SUBMODALIDAD_SIN_RELACION', true, now(), 'ANP-AGENPORT', 'admin', 'Error: No existe una gestión de presupuesto a la cual asignar los valores seleccionados', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.DatosGenerales', true, now(), 'ANP-AGENPORT', 'admin', 'Datos generales', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.Beneficiarios', true, now(), 'ANP-AGENPORT', 'admin', 'Beneficiarios', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.Rubros', true, now(), 'ANP-AGENPORT', 'admin', 'Rubros', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.Subrubros', true, now(), 'ANP-AGENPORT', 'admin', 'Subrubros', 0, 1);



--CREACION DE OPERACION GESTION COMPONENTE ESCOLAR
INSERT INTO siap2.ss_operacion
(ope_id, ope_codigo, ope_descripcion, ope_nombre, ope_origen, ope_tipocampo, ope_user_code, ope_version, ope_vigente, ope_categoria_id)
VALUES(NEXTVAL('siap2.seq_operacion'), 'COMPONENTE_GESTION_PRESUPUESTO_ESCOLAR', 'Componente de gestíon de presupuesto escolar', 'Componente de Gestion de presupuesto escolar', 'SIAP2', 'TIPO_UNICO', 0, NULL, true, NULL);

--CREACION DE OPERACION TIPOS_DE_CREDITO
INSERT INTO siap2.ss_operacion
(ope_id, ope_codigo, ope_descripcion, ope_nombre, ope_origen, ope_tipocampo, ope_user_code, ope_version, ope_vigente, ope_categoria_id)
VALUES(NEXTVAL('siap2.seq_operacion'), 'TIPOS_DE_CREDITO', 'Catálogo de tipos de crédito', 'Catálogo de tipos de crédito', 'SIAP2', 0, 0, NULL, true, NULL);

--AREAS DE INVERSION
INSERT INTO siap2.ss_operacion
(ope_id, ope_codigo, ope_descripcion, ope_nombre, ope_origen, ope_tipocampo, ope_user_code, ope_version, ope_vigente, ope_categoria_id)
VALUES(NEXTVAL('siap2.seq_operacion'), 'AREAS_DE_INVERSION', 'Catálogo de áreas de inversión', 'atálogo de áreas de inversión', 'SIAP2', 0, 0, NULL, true, NULL);

--SUBAREAS DE INVERSION
INSERT INTO siap2.ss_operacion
(ope_id, ope_codigo, ope_descripcion, ope_nombre, ope_origen, ope_tipocampo, ope_user_code, ope_version, ope_vigente, ope_categoria_id)
VALUES(NEXTVAL('siap2.seq_operacion'), 'CREAR_AREAS_DE_INVERSION', 'Creación y edición de áreas de inversión', 'Creación y edición de áreas de inversión', 'SIAP2', 0, 0, NULL, true, NULL);

--CREAR_SUB_AREAS_DE_INVERSION
INSERT INTO siap2.ss_operacion
(ope_id, ope_codigo, ope_descripcion, ope_nombre, ope_origen, ope_tipocampo, ope_user_code, ope_version, ope_vigente, ope_categoria_id)
VALUES(NEXTVAL('siap2.seq_operacion'), 'CREAR_SUB_AREAS_DE_INVERSION', 'Creación y edición de subáreas de inversión', 'Creación y edición de subáreas de inversión', 'SIAP2', 0, 0, NULL, true, NULL);

--CREAR_COMPONENTES
INSERT INTO siap2.ss_operacion
(ope_id, ope_codigo, ope_descripcion, ope_nombre, ope_origen, ope_tipocampo, ope_user_code, ope_version, ope_vigente, ope_categoria_id)
VALUES(NEXTVAL('siap2.seq_operacion'), 'CREAR_EDITAR_GESTION_PRESUPUESTO_ESCOLAR', 'Creación y edición de componente de gestíon de presupuesto escolar', 'Creación y edición de componente de gestíon de presupuesto escolar', 'SIAP2', 'TIPO_UNICO', 0, NULL, true, NULL);

--CREAR_CATEGORIA_DE_COMPONENTES
INSERT INTO siap2.ss_operacion
(ope_id, ope_codigo, ope_descripcion, ope_nombre, ope_origen, ope_tipocampo, ope_user_code, ope_version, ope_vigente, ope_categoria_id)
VALUES(NEXTVAL('siap2.seq_operacion'), 'CATEGORIA_PRESUPUESTO_ESCOLAR', 'Catálogo de categoría de presupuesto escolar', 'Catálogo de categoría de presupuesto escolar', 'SIAP2', 'TIPO_UNICO', 0, NULL, true, NULL);

--CREAR_RUBROS
INSERT INTO siap2.ss_operacion
(ope_id, ope_codigo, ope_descripcion, ope_nombre, ope_origen, ope_tipocampo, ope_user_code, ope_version, ope_vigente, ope_categoria_id)
VALUES(NEXTVAL('siap2.seq_operacion'), 'RUBROS', 'Rubros', 'Rubros', 'SIAP2', 'TIPO_UNICO', 0, NULL, true, NULL);

--CREAR_SUB_RUBOS
INSERT INTO siap2.ss_operacion
(ope_id, ope_codigo, ope_descripcion, ope_nombre, ope_origen, ope_tipocampo, ope_user_code, ope_version, ope_vigente, ope_categoria_id)
VALUES(NEXTVAL('siap2.seq_operacion'), 'SUB_RUBROS', 'SubRubros', 'SubRubros', 'SIAP2', 'TIPO_UNICO', 0, NULL, true, NULL);


--PERMISOS PARA EL ROL 'ADMIN' POR DEFECTO PARA DESARROLLO
/*
INSERT INTO siap2.ss_rel_rol_operacion
(rel_rol_operacion_id, rel_rol_operacion_editable, rel_rol_operacion_origen, rel_rol_operacion_visible, rel_rol_operacion_operacion_id, rel_rol_operacion_rol_id)
VALUES(NEXTVAL('siap2.seq_rel_rol_operac'), true, NULL, true, CURRVAL('siap2.seq_operacion'), 1);
*/


INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.Cuentas', true, now(), 'ANP-AGENPORT', 'admin', 'Cuentas', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.SubCuentas', true, now(), 'ANP-AGENPORT', 'admin', 'Subcuentas', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.SubCuenta', true, now(), 'ANP-AGENPORT', 'admin', 'Subcuenta', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.CrearCuentas', true, now(), 'ANP-AGENPORT', 'admin', 'Creación de Cuentas', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.CrearSubCuentas', true, now(), 'ANP-AGENPORT', 'admin', 'Creación de Subcuentas', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.TopePresupuestal', true, now(), 'ANP-AGENPORT', 'admin', 'Tope Presupuestal', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.CrearTopePresupuestal', true, now(), 'ANP-AGENPORT', 'admin', 'Crear tope presupuestal', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.menu.TopePresupuestal', true, now(), 'ANP-AGENPORT', 'admin', 'Mantenimiento de Tope Presupuestal', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.CrearEditarTopePresupuestal', true, now(), 'ANP-AGENPORT', 'admin', 'Creación y edición de Tope presupuestal', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.Cuenta', true, now(), 'ANP-AGENPORT', 'admin', 'Cuenta', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'EstadoTopePresupuestal.EN_PROCESO', true, now(), 'ANP-AGENPORT', 'admin', 'En proceso', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'EstadoTopePresupuestal.FORMULACION', true, now(), 'ANP-AGENPORT', 'admin', 'Formulación', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'EstadoTopePresupuestal.APROBACION', true, now(), 'ANP-AGENPORT', 'admin', 'Aprobación', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'EstadoTopePresupuestal.CERRADO', true, now(), 'ANP-AGENPORT', 'admin', 'Cerrado', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.MontoAprobado', true, now(), 'ANP-AGENPORT', 'admin', 'Monto aprobado', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.TotalPresupuestos', true, now(), 'ANP-AGENPORT', 'admin', 'Total presupuestos', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.MontoFormulacion', true, now(), 'ANP-AGENPORT', 'admin', 'Monto Formulación', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.menu.TotalPresupuestos', true, now(), 'ANP-AGENPORT', 'admin', 'Consulta total de presupuestos', 0, 1);



---------------- CUENTAS ----------------

create table siap2.ss_cuentas(
	cu_id bigint primary key,
	cu_codigo varchar(5) unique not null,
	cu_nombre varchar(100) unique not null,
	cu_habilitado boolean default true,
	cu_descripcion varchar(4000),
	cu_orden integer,
	cu_cuenta_padre bigint references siap2.ss_cuentas(cu_id),
	cu_ult_mod timestamp without time zone,
	cu_ult_usuario varchar,
	cu_version bigint
);


create sequence siap2.seq_cuentas no maxvalue minvalue 1 no cycle start 1;

create table siap2.ss_cuentas_hist(
	cu_id bigint,
	cu_codigo varchar(5) not null,
	cu_nombre varchar(100)not null,
	cu_habilitado boolean default true,
	cu_descripcion varchar(4000),
	cu_orden integer,
	cu_cuenta_padre bigint,
	cu_ult_mod timestamp without time zone,
	cu_ult_usuario varchar,
	cu_version bigint,
	start_date timestamp without time zone,
	end_date timestamp without time zone
);


---------------- TOPE PRESUPUESTAL ----------------


create table siap2.ss_tope_presupestal(
	tp_id bigint primary key,
	tp_componente bigint references siap2.ss_ges_pres_es(ges_id),
	tp_sub_cuenta bigint references siap2.ss_cuentas(cu_id),
	tp_monto_formulacion DECIMAL(15,2),
	tp_monto_aprobado DECIMAL(15,2),
	tp_estado integer,
	tp_anio_fiscal bigint references siap2.ss_anio_fiscal(ani_id),
	tp_ult_mod timestamp without time zone,
	tp_ult_usuario varchar,
	tp_version bigint
);

create sequence siap2.seq_tope_presupuestal no maxvalue minvalue 1 no cycle start 1;

create table siap2.ss_tope_presupestal_hist(
	tp_id bigint,
	tp_componente bigint,
	tp_sub_cuenta bigint,
	tp_monto_formulacion DECIMAL(15,2),
	tp_monto_aprobado DECIMAL(15,2),
	tp_estado integer,
	tp_anio_fiscal bigint,
	tp_ult_mod timestamp without time zone,
	tp_ult_usuario varchar,
	tp_version bigint,
	start_date timestamp without time zone,
	end_date timestamp without time zone
);

--CONSULTA_CUENTAS
INSERT INTO siap2.ss_operacion
(ope_id, ope_codigo, ope_descripcion, ope_nombre, ope_origen, ope_tipocampo, ope_user_code, ope_version, ope_vigente, ope_categoria_id)
VALUES(NEXTVAL('siap2.seq_operacion'), 'CUENTAS', 'Cuentas', 'Cuentas', 'SIAP2', 'TIPO_UNICO', 0, NULL, true, NULL);


--CREAR_CUENTAS
INSERT INTO siap2.ss_operacion
(ope_id, ope_codigo, ope_descripcion, ope_nombre, ope_origen, ope_tipocampo, ope_user_code, ope_version, ope_vigente, ope_categoria_id)
VALUES(NEXTVAL('siap2.seq_operacion'), 'CREAR_EDITAR_CUENTAS', 'Crear Cuentas', 'SubRubros', 'SIAP2', 'TIPO_UNICO', 0, NULL, true, NULL);

--CREAR_SUB_CUENTAS
INSERT INTO siap2.ss_operacion
(ope_id, ope_codigo, ope_descripcion, ope_nombre, ope_origen, ope_tipocampo, ope_user_code, ope_version, ope_vigente, ope_categoria_id)
VALUES(NEXTVAL('siap2.seq_operacion'), 'CREAR_EDITAR_SUB_CUENTAS', 'Crear SubRubros', 'SubRubros', 'SIAP2', 'TIPO_UNICO', 0, NULL, true, NULL);

--CONSULTA_TOPE_PRESUPUESTAL
INSERT INTO siap2.ss_operacion
(ope_id, ope_codigo, ope_descripcion, ope_nombre, ope_origen, ope_tipocampo, ope_user_code, ope_version, ope_vigente, ope_categoria_id)
VALUES(NEXTVAL('siap2.seq_operacion'), 'TOPE_PRESUPUESTAL', 'TopePresupuestal', 'TopePresupuestal', 'SIAP2', 'TIPO_UNICO', 0, NULL, true, NULL);

--CREAR_EDITAR_TOPE_PRESUPUESTAL
INSERT INTO siap2.ss_operacion
(ope_id, ope_codigo, ope_descripcion, ope_nombre, ope_origen, ope_tipocampo, ope_user_code, ope_version, ope_vigente, ope_categoria_id)
VALUES(NEXTVAL('siap2.seq_operacion'), 'CREAR_EDITAR_TOPE_PRESUPUESTAL', 'CrearEditarTopePresupuestal', 'CrearEditarTopePresupuestal', 'SIAP2', 'TIPO_UNICO', 0, NULL, true, NULL);

--CONSULTA_TOTAL_PRESUPUESTOS
INSERT INTO siap2.ss_operacion
(ope_id, ope_codigo, ope_descripcion, ope_nombre, ope_origen, ope_tipocampo, ope_user_code, ope_version, ope_vigente, ope_categoria_id)
VALUES(NEXTVAL('siap2.seq_operacion'), 'CONSULTA_TOTAL_PRESUPUESTOS', 'ConsultaTotalPresupuestos', 'ConsultaTotalPresupuestos', 'SIAP2', 'TIPO_UNICO', 0, NULL, true, NULL);

-- 6.3.1


UPDATE siap2.ss_textos SET tex_valor = 'Subcomponentes' where tex_codigo = 'labels.menu.PresupuestoEscolar';
UPDATE siap2.ss_textos SET tex_valor = 'Crear subcomponente' where tex_codigo = 'labels.CrearGestionPresupuestoEscolar';
UPDATE siap2.ss_textos SET tex_valor = 'Editar subcomponente' where tex_codigo = 'labels.EditarGestionPresupuestoEscolar';

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.Subactividad', true, now(), 'ANP-AGENPORT', 'admin', 'Subactividad', 0, 1);


ALTER TABLE siap2.ss_ges_pres_es ADD COLUMN ges_proyecto character varying(255);
ALTER TABLE siap2.ss_ges_pres_es_hist ADD COLUMN ges_proyecto character varying(255);

ALTER TABLE siap2.ss_ges_pres_es ADD COLUMN ges_subactividad character varying(255);
ALTER TABLE siap2.ss_ges_pres_es_hist ADD COLUMN ges_subactividad character varying(255);

ALTER TABLE siap2.ss_ges_pres_es ADD COLUMN ges_categoria character varying(255);
ALTER TABLE siap2.ss_ges_pres_es_hist ADD COLUMN ges_categoria character varying(255);

ALTER TABLE siap2.ss_ges_pres_es ADD COLUMN ges_convenio character varying(255);
ALTER TABLE siap2.ss_ges_pres_es_hist ADD COLUMN ges_convenio character varying(255);

ALTER TABLE siap2.ss_ges_pres_es ADD COLUMN ges_tipo_subcomponente character varying(50);
ALTER TABLE siap2.ss_ges_pres_es_hist ADD COLUMN ges_tipo_subcomponente character varying(50);



create table siap2.ss_rel_componentepresupescolar_areainv(
	ges_id bigint references siap2.ss_ges_pres_es(ges_id),
	ai_id bigint references siap2.ss_areas_inversion(ai_id),
        PRIMARY KEY (ges_id, ai_id)
);

create table siap2.ss_rel_componentepresupescolar_areainv_hist(
	ges_id bigint,
	ai_id bigint,
	start_date timestamp without time zone,
	end_date timestamp without time zone
);


INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.Subareas', true, now(), 'ANP-AGENPORT', 'admin', 'Subáreas', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.Subarea', true, now(), 'ANP-AGENPORT', 'admin', 'Subárea', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.Area', true, now(), 'ANP-AGENPORT', 'admin', 'Área', 0, 1);


INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.Tiposubcomponente', true, now(), 'ANP-AGENPORT', 'admin', 'Tipo de subcomponente', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.TipoTransferencia', true, now(), 'ANP-AGENPORT', 'admin', 'Tipo de transferencia', 0, 1);




ALTER TABLE siap2.ss_rel_ges_pres_es_anio_fiscal ADD COLUMN id_sub_cuenta bigint;
ALTER TABLE siap2.ss_rel_ges_pres_es_anio_fiscal_hist ADD COLUMN id_sub_cuenta bigint;

create table siap2.ss_transferencias(
	st_id bigint primary key,
	st_tope integer,
	st_monto DECIMAL(15,2),
	st_fecha_solicitado timestamp without time zone,
	st_estado integer,
	st_ult_mod timestamp without time zone,
	st_ult_usuario varchar,
	st_version bigint
);

create sequence siap2.seq_transferencias no maxvalue minvalue 1 no cycle start 1;


create table siap2.ss_transferencias_hist(
	st_id bigint,
	st_tope integer,
	st_monto DECIMAL(15,2),
	st_fecha_solicitado timestamp without time zone,
	st_estado integer,
	st_ult_mod timestamp without time zone,
	st_ult_usuario varchar,
	st_version bigint,
	start_date timestamp without time zone,
	end_date timestamp without time zone
);


INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.Transferencias', true, now(), 'ANP-AGENPORT', 'admin', 'Transferencias', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.FechaSolicitado', true, now(), 'ANP-AGENPORT', 'admin', 'Fecha solicitado', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'EstadoTransferencias.SOLICITADA', true, now(), 'ANP-AGENPORT', 'admin', 'SOLICITADA', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'EstadoTransferencias.REALIZADA', true, now(), 'ANP-AGENPORT', 'admin', 'REALIZADA', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'EstadoTransferencias.CERRADA', true, now(), 'ANP-AGENPORT', 'admin', 'CERRADA', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.CentroEducativo', true, now(), 'ANP-AGENPORT', 'admin', 'Centro educativo', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.Sede', true, now(), 'ANP-AGENPORT', 'admin', 'Sede', 0, 1);



INSERT INTO siap2.ss_operacion
(ope_id, ope_codigo, ope_descripcion, ope_nombre, ope_origen, ope_tipocampo, ope_user_code, ope_version, ope_vigente, ope_categoria_id)
VALUES(NEXTVAL('siap2.seq_operacion'), 'CONSULTA_TRANSFERENCIAS', 'ConsultaTransferencias', 'ConsultaTransferencias', 'SIAP2', 'TIPO_UNICO', 0, NULL, true, NULL);


ALTER TABLE siap2.ss_tope_presupestal ADD COLUMN tp_sede bigint;
ALTER TABLE siap2.ss_tope_presupestal_hist ADD COLUMN tp_sede bigint;

ALTER TABLE siap2.ss_beneficiarios ADD COLUMN b_ges_pres_es_anio_fiscal bigint;
ALTER TABLE siap2.ss_beneficiarios_hist ADD COLUMN b_ges_pres_es_anio_fiscal bigint;



INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.Denominacion', true, now(), 'ANP-AGENPORT', 'admin', 'Denominación', 0, 1);


ALTER TABLE siap2.ss_rel_ges_pres_es_anio_fiscal ADD COLUMN clasificacion integer;
ALTER TABLE siap2.ss_rel_ges_pres_es_anio_fiscal_hist ADD COLUMN clasificacion integer;

ALTER TABLE siap2.ss_rel_ges_pres_es_anio_fiscal ADD COLUMN objeto_especifico_gasto character varying(255);
ALTER TABLE siap2.ss_rel_ges_pres_es_anio_fiscal_hist ADD COLUMN objeto_especifico_gasto character varying(255);


-- 6.4.0
ALTER TABLE siap2.ss_cuentas ALTER cu_nombre TYPE character varying(500);
ALTER TABLE siap2.ss_cuentas_hist ALTER cu_nombre TYPE character varying(500); 

ALTER TABLE siap2.ss_cuentas DROP CONSTRAINT ss_cuentas_cu_nombre_key;
ALTER TABLE siap2.ss_areas_inversion DROP CONSTRAINT ss_areas_inversion_ai_nombre_key;

--CREACION DE RELACION DE TOPE PRESUPUESTAL CON MOVIMIENTO (JTRUJILLO - 08/JAN/2020)
ALTER TABLE siap2.ss_tope_presupestal ADD tp_movimiento bigint REFERENCES finanzas.sg_presupuesto_escolar_movimiento(mov_pk);
ALTER TABLE siap2.ss_tope_presupestal_hist ADD tp_movimiento bigint;

UPDATE siap2.ss_textos set tex_valor = 'Unidades presupuestarias' where tex_codigo = 'labels.Cuentas';
UPDATE siap2.ss_textos set tex_valor = 'Líneas presupuestarias' where tex_codigo = 'labels.SubCuentas';
UPDATE siap2.ss_textos set tex_valor = 'Línea presupuestaria' where tex_codigo = 'labels.SubCuenta';
UPDATE siap2.ss_textos set tex_valor = 'Unidad presupuestaria' where tex_codigo = 'labels.Cuenta';
UPDATE siap2.ss_textos set tex_valor = 'Creación de unidades presupuestarias' where tex_codigo = 'labels.CrearCuentas';
UPDATE siap2.ss_textos set tex_valor = 'Creación de líneas presupuestarias' where tex_codigo = 'labels.CrearSubCuentas';


INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.BusquedaAvanzada', true, now(), 'ANP-AGENPORT', 'admin', 'Búsqueda Avanzada', 0, 1);

UPDATE siap2.ss_textos set tex_valor = 'Tope Presupuestal' where tex_codigo = 'labels.menu.TopePresupuestal';

ALTER TABLE siap2.ss_ges_pres_es ADD COLUMN ges_tipo_transferencia character varying(50);
ALTER TABLE siap2.ss_ges_pres_es_hist ADD COLUMN ges_tipo_transferencia character varying(50);

ALTER TABLE siap2.ss_ges_pres_es ALTER ges_tipo TYPE character varying(50);
ALTER TABLE siap2.ss_ges_pres_es_hist ALTER ges_tipo TYPE character varying(50); 

ALTER TABLE siap2.ss_ges_pres_es ALTER ges_parametro TYPE character varying(50);
ALTER TABLE siap2.ss_ges_pres_es_hist ALTER ges_parametro TYPE character varying(50); 

ALTER TABLE siap2.ss_ges_pres_es ALTER ges_uni_med TYPE character varying(50);
ALTER TABLE siap2.ss_ges_pres_es_hist ALTER ges_uni_med TYPE character varying(50); 



ALTER TABLE siap2.ss_ges_pres_es DROP COLUMN ges_tipo_subcomponente;
ALTER TABLE siap2.ss_ges_pres_es_hist DROP COLUMN ges_tipo_subcomponente; 


INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.CrearTopePresupuestalArchivo', true, now(), 'ANP-AGENPORT', 'admin', 'Creación de tope presupuestal con archivo', 0, 1);


INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.CargarDesdeArchivo', true, now(), 'ANP-AGENPORT', 'admin', 'Cargar desde archivo', 0, 1);

UPDATE siap2.ss_textos set tex_valor = 'Subcomponente' where tex_codigo = 'labels.ComponentePresupuestoEscolar';

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_CODIGO_SEDE_INEXISTENTE', true, now(), 'ANP-AGENPORT', 'admin', 'Código de sede {0} inexistente.', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_CODIGO_SUBCUENTA_INEXISTENTE', true, now(), 'ANP-AGENPORT', 'admin', 'Código de línea presupuestaria {0} inexistente', 0, 1);


-- 6.5.0

ALTER TABLE siap2.ss_rel_ges_pres_es_anio_fiscal ADD COLUMN monto_por_matricula DECIMAL(20,2);
ALTER TABLE siap2.ss_rel_ges_pres_es_anio_fiscal_hist ADD COLUMN monto_por_matricula DECIMAL(20,2);

ALTER TABLE siap2.ss_rel_ges_pres_es_anio_fiscal ADD COLUMN monto_minimo DECIMAL(20,2);
ALTER TABLE siap2.ss_rel_ges_pres_es_anio_fiscal_hist ADD COLUMN monto_minimo DECIMAL(20,2);

ALTER TABLE siap2.ss_rel_ges_pres_es_anio_fiscal ADD COLUMN deducir_ges_pres_es bigint;
ALTER TABLE siap2.ss_rel_ges_pres_es_anio_fiscal_hist ADD COLUMN deducir_ges_pres_es bigint;


ALTER TABLE siap2.ss_rel_ges_pres_es_anio_fiscal ADD COLUMN fecha_matricula timestamp without time zone;
ALTER TABLE siap2.ss_rel_ges_pres_es_anio_fiscal_hist ADD COLUMN fecha_matricula timestamp without time zone;

ALTER TABLE siap2.ss_rel_ges_pres_es_anio_fiscal ADD COLUMN ult_mod timestamp without time zone;
ALTER TABLE siap2.ss_rel_ges_pres_es_anio_fiscal_hist ADD COLUMN ult_mod timestamp without time zone;    

ALTER TABLE siap2.ss_rel_ges_pres_es_anio_fiscal ADD COLUMN ult_usuario varchar(255);
ALTER TABLE siap2.ss_rel_ges_pres_es_anio_fiscal_hist ADD COLUMN ult_usuario varchar(255);    


ALTER TABLE siap2.ss_rel_ges_pres_es_anio_fiscal ADD COLUMN version integer;
ALTER TABLE siap2.ss_rel_ges_pres_es_anio_fiscal_hist ADD COLUMN version integer;         



INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.MontoPorMatricula', true, now(), 'ANP-AGENPORT', 'admin', 'Monto por matrícula', 0, 1);


ALTER TABLE siap2.ss_rel_ges_pres_es_anio_fiscal DROP CONSTRAINT unk;


ALTER TABLE siap2.ss_rel_ges_pres_es_anio_fiscal ADD CONSTRAINT rel_ges_pres_anio_fiscal_ges_fkey FOREIGN KEY (id_ges_pres_es) REFERENCES siap2.ss_ges_pres_es(ges_id) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE siap2.ss_rel_ges_pres_es_anio_fiscal ADD CONSTRAINT rel_ges_pres_anio_fiscal_deducir_ges_fkey FOREIGN KEY (deducir_ges_pres_es) REFERENCES siap2.ss_ges_pres_es(ges_id) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.Deducir', true, now(), 'ANP-AGENPORT', 'admin', 'Deducir', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.FechaMatricula', true, now(), 'ANP-AGENPORT', 'admin', 'Fecha de matrícula', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.GenerarTecho', true, now(), 'ANP-AGENPORT', 'admin', 'Generar techo', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_FECHA_MATRICULA_VACIA', true, now(), 'ANP-AGENPORT', 'admin', 'Debe completar la fecha de la matrícula', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_MONTO_POR_MATRICULA_VACIO', true, now(), 'ANP-AGENPORT', 'admin', 'Debe completar el monto por matrícula', 0, 1);

--CAMBIO DE NOMBRE A COLUMNA TOPE > TOPE_PRESUPUESTAL (JTRUJILLO - 08/JAN/2020)
alter table siap2.ss_transferencias rename column st_tope to st_tope_presupuestal;
alter table siap2.ss_transferencias_hist rename column st_tope to st_tope_presupuestal;

--CREACION DE LLAVE FORANEA DE TOPE_PRESUPUESTAL (JTRUJILLO - 08/JAN/2020)
alter table siap2.ss_transferencias add constraint ss_transferencia_tope_fk 
foreign key (st_tope_presupuestal) references siap2.ss_tope_presupestal(tp_id);

--ACTUALIZACION DE NOMBRE TOPE_PRESUPUESTAL > TECHO_PRESUPUESTAL
update siap2.ss_textos set tex_codigo = 'labels.TechoPresupuestal', tex_valor = 'Techo Presupuestal' 
where tex_codigo = 'labels.TopePresupuestal';


INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.UnPresup', true, now(), 'ANP-AGENPORT', 'admin', 'Un. Presup.', 0, 1);


INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.LinPresup', true, now(), 'ANP-AGENPORT', 'admin', 'Lín. Presup.', 0, 1);



INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.TechoFormul', true, now(), 'ANP-AGENPORT', 'admin', 'Techo Formul.', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.TechoAprob', true, now(), 'ANP-AGENPORT', 'admin', 'Techo Aprob.', 0, 1);

--ACTUALIZACION DE NOMBRE DE MANTENIMIENTO TOPE_ŔESUPUESTAL > TECHO_PRESUPUESTAL
update siap2.ss_textos set tex_codigo = 'labels.menu.TechoPresupuestal', tex_valor = 'Mantenimiento de Techo Presupuestal' 
where tex_codigo = 'labels.menu.TopePresupuestal';



---------------- TRANSFERENCIAS COMPONENTE ----------------

CREATE TABLE siap2.ss_transferencias_componente(
	tc_id bigint primary key,
	tc_componente bigint,
	tc_subcomponente bigint,
	tc_unidad_presupuestaria bigint,
	tc_linea_presupuestaria bigint,
	tc_anio_fiscal bigint,
	tc_porcentaje decimal,
	tc_estado integer,
	tc_ult_mod timestamp without time zone,
	tc_ult_usuario varchar,
	tc_version bigint,
	CONSTRAINT transComponente_catPresupuesto_fk FOREIGN KEY (tc_componente) REFERENCES siap2.ss_categoria_presupuesto_escolar(cpe_id),
	CONSTRAINT transSubcomponente_componente_fk FOREIGN KEY (tc_subcomponente) REFERENCES siap2.ss_ges_pres_es(ges_id),
	CONSTRAINT transUnidad_cuenta_fk FOREIGN KEY (tc_unidad_presupuestaria) REFERENCES siap2.ss_cuentas(cu_id),
	CONSTRAINT transUnidad_subCuenta_fk FOREIGN KEY (tc_linea_presupuestaria) REFERENCES siap2.ss_cuentas(cu_id),
	CONSTRAINT transUnidad_anioFiscal_fk FOREIGN KEY (tc_anio_fiscal) REFERENCES siap2.ss_anio_fiscal(ani_id)
);

CREATE SEQUENCE siap2.seq_transferencias_componente NO MAXVALUE MINVALUE 1 NO CYCLE START 1;

CREATE TABLE siap2.ss_transferencias_componente_hist(
	tc_id bigint,
	tc_componente bigint,
	tc_subcomponente bigint,
	tc_unidad_presupuestaria bigint,
	tc_linea_presupuestaria bigint,
	tc_anio_fiscal bigint,
	tc_porcentaje decimal,
	tc_estado integer,
	tc_ult_mod timestamp without time zone,
	tc_ult_usuario varchar,
	tc_version bigint,
	start_date timestamp without time zone,
	end_date timestamp without time zone
);


INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.TransferenciasComponente', true, now(), 'ANP-AGENPORT', 'admin', 'Transferencia de Componente', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.UnidadPresupuestaria', true, now(), 'ANP-AGENPORT', 'admin', 'Unidad Presupuestaria', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.LineaPresupuestaria', true, now(), 'ANP-AGENPORT', 'admin', 'Linea Presupuestaria', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.CrearTransferenciasComponente', true, now(), 'ANP-AGENPORT', 'admin', 'Crear transferencia de componente', 0, 1);

--CONSULTA_DE_TRANSFERENCIA_
INSERT INTO siap2.ss_operacion
(ope_id, ope_codigo, ope_descripcion, ope_nombre, ope_origen, ope_tipocampo, ope_user_code, ope_version, ope_vigente, ope_categoria_id)
VALUES(NEXTVAL('siap2.seq_operacion'), 'CONSULTA_TRANSFERENCIA_COMPONENTE', 'TransferenciaComponente', 'TransferenciaComponente', 'SIAP2', 'TIPO_UNICO', 0, NULL, true, NULL);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.CrearEditarTechoPresupuestal', true, now(), 'ANP-AGENPORT', 'admin', 'Creación y edición de Techo presupuestal', 0, 1);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.GenerarTransferenciasCed', true, now(), 'ANP-AGENPORT', 'admin', 'Generar Transferencias a Centros educativos', 0, 1);


ALTER TABLE siap2.ss_rel_ges_pres_es_anio_fiscal_hist  DROP CONSTRAINT ss_rel_ges_pres_es_anio_fiscal_hist_pkey;

-- 6.6.0


--MENU_TRANSFERENCIAS_ESCOLARES
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.menu.TransferenciasEscolares', true, now(), 'ANP-AGENPORT', 'admin', 'Transferencias Escolares', 0, 1);


--OPERACION_PARA_ACCESO_A_MENU_TRANSFERENCIAS_ESCOLARES
INSERT INTO siap2.ss_operacion
(ope_id, ope_codigo, ope_descripcion, ope_nombre, ope_origen, ope_tipocampo, ope_user_code, ope_version, ope_vigente, ope_categoria_id)
VALUES(NEXTVAL('siap2.seq_operacion'), 'MENU_TRANSFERENCIAS_ESCOLARES', 'MenuTransferenciasEscolares', 'MenuTransferenciasEscolares', 'SIAP2', 'TIPO_UNICO', 0, NULL, true, NULL);

--TITULO_DESEMBOLSO_COMPONENTE
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.DesembolsosComponente', true, now(), 'ANP-AGENPORT', 'admin', 'Desembolsos de transferencias por componentes', 0, 1);

--TITULO_DESEMBOLSO_COMPONENTE
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.FechaDesembolso', true, now(), 'ANP-AGENPORT', 'admin', 'Fecha desembolso', 0, 1);


--ACCESO_A_PAGINA_DE_DESEMBOLSO_POR_TRANSFERENCIA_COMPONENTE
INSERT INTO siap2.ss_operacion
(ope_id, ope_codigo, ope_descripcion, ope_nombre, ope_origen, ope_tipocampo, ope_user_code, ope_version, ope_vigente, ope_categoria_id)
VALUES(NEXTVAL('siap2.seq_operacion'), 'CONSULTA_DESEMBOLSO', 'PaginaDesembolsos', 'PaginaDesembolsos', 'SIAP2', 'TIPO_UNICO', 0, NULL, true, NULL);

--TITULO_DESEMBOLSO
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.Desembolso', true, now(), 'ANP-AGENPORT', 'admin', 'Desembolso', 0, 1);


--ERROR_TRANSFERENCIA_COMPONENTE_VACIO
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_TRANSFERENCIA_COMPONENTE_VACIO', true, now(), 'ANP-AGENPORT', 'admin', 'ERR_TRANSFERENCIA_COMPONENTE_VACIO', 0, 1);



---------------- DESEMBOLSO TRANSFERENCIA COMPONENTE ----------------

create table siap2.ss_desembolso_transferencia_componente(
	des_id bigint,
	des_transferencia_desembolso bigint,
	des_porcentaje decimal,
	des_fecha_desembolso TIMESTAMP WITHOUT TIME zone,
	des_estado CHARACTER VARYING(12),
	des_ult_mod timestamp without time zone,
	des_ult_usuario varchar,
	des_version bigint,
	constraint desembolso_transferencia_pk primary key (des_id),
	constraint desembolso_transferencia_componente_fk foreign key (des_transferencia_desembolso) references siap2.ss_transferencias_componente(tc_id)
);

CREATE SEQUENCE IF NOT EXISTS siap2.seq_desembolso_transferencia INCREMENT 1 START 1 MINVALUE 1 no maxvalue CACHE 1 no cycle;

create table siap2.ss_desembolso_transferencia_componente_hist(
	des_id bigint,
	des_transferencia_desembolso bigint,
	des_porcentaje decimal,
	des_fecha_desembolso TIMESTAMP WITHOUT TIME zone,
	des_estado CHARACTER VARYING(12),
	des_ult_mod timestamp without time zone,
	des_ult_usuario varchar,
	des_version bigint,
	start_date timestamp without time zone,
	end_date timestamp without time zone
);

-- 6.6.1

ALTER TABLE siap2.ss_rel_ges_pres_es_anio_fiscal DROP COLUMN clasificacion;
ALTER TABLE siap2.ss_rel_ges_pres_es_anio_fiscal_hist DROP COLUMN clasificacion;

create table siap2.ss_rel_gespresanio_clasificacion(
	id bigint references siap2.ss_rel_ges_pres_es_anio_fiscal(id),
	cla_pk bigint references catalogo.sg_clasificaciones(cla_pk),
        PRIMARY KEY (id, cla_pk)
);

create table siap2.ss_rel_gespresanio_clasificacion_hist(
	id bigint,
	cla_pk bigint,
	start_date timestamp without time zone,
	end_date timestamp without time zone
);

ALTER TABLE siap2.ss_rel_ges_pres_es_anio_fiscal ADD COLUMN tipo character varying (50);
ALTER TABLE siap2.ss_rel_ges_pres_es_anio_fiscal_hist ADD COLUMN tipo character varying (50);

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.ActualizarTecho', true, now(), 'ANP-AGENPORT', 'admin', 'Actualizar techo', 0, 1);


ALTER TABLE siap2.ss_rel_ges_pres_es_anio_fiscal ADD COLUMN monto_por_matricula_aprobacion DECIMAL(20,2);
ALTER TABLE siap2.ss_rel_ges_pres_es_anio_fiscal_hist ADD COLUMN monto_por_matricula_aprobacion DECIMAL(20,2);

ALTER TABLE siap2.ss_rel_ges_pres_es_anio_fiscal ADD COLUMN monto_minimo_aprobacion DECIMAL(20,2);
ALTER TABLE siap2.ss_rel_ges_pres_es_anio_fiscal_hist ADD COLUMN monto_minimo_aprobacion DECIMAL(20,2);


INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.MontoPorMatriculaAprobacion', true, now(), 'ANP-AGENPORT', 'admin', 'Monto por matrícula aprobación', 0, 1);


INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.MontoMinimoAprobacion', true, now(), 'ANP-AGENPORT', 'admin', 'Monto mínimo aprobación', 0, 1);


-- 6.8.0

--MENSAJE DE ERROR PARA DESEMBOLSOS
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_LIMITE_PORCENTAJES_ALCANZADO', true, now(), 'ANP-AGENPORT', 'admin', 'Se alcanzó el límite de porcentaje en desembosos', 0, 1);

--LABEL DE NOMBRE DE SISTEMA
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'NOMBRE_SISTEMA', true, now(), 'ANP-AGENPORT', 'admin', 'SIAP', 0, 1);

--MENSAJE DE ERROR PARA NINGUN REGISTRO SELECCIONADO
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_NINGUN_REGISTRO_SELECCIONADO', true, now(), 'ANP-AGENPORT', 'admin', 'No se ha seleccionado ningún registro', 0, 1);

--MENSAJE DE ERROR PARA NINGUN ESTADO SELECCIONADO
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_NINGUN_ESTADO_SELECCIONADO', true, now(), 'ANP-AGENPORT', 'admin', 'No se ha seleccionado ningún estado', 0, 1);

--LABEL PARA ID DE TRANSFERENCIA
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.NumeroTransferencia', true, now(), 'ANP-AGENPORT', 'admin', 'Número de transferencia', 0, 1);

--MENSAJE DE ERROR PARA NINGUN ESTADO SELECCIONADO
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_CODIGO_DE_CENTRO_EDUCATIVO_NO_VALIDO', true, now(), 'ANP-AGENPORT', 'admin', 'El código de centro educativo ingresado, no es válido', 0, 1);

--MENSAJE DE ERROR PARA PORCENTAJE INVALIDO
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_PORCENTAJE_INGRESADO_NO_VALIDO', true, now(), 'ANP-AGENPORT', 'admin', 'El porcentaje ingresado no es válido', 0, 1);

--LABEL DE GENERACION DE DESEMBOLSOS POR CENTRO
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.DesembolsosPorCentro', true, now(), 'ANP-AGENPORT', 'admin', 'Generar desembolsos por centro', 0, 1);

--LABEL SUB AREAS HABILITADAS
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.SubareasHabilitadas', true, now(), 'ANP-AGENPORT', 'admin', 'Subáreas habilitadas', 0, 1);

--MENSAJE DE ERROR PARA MONTO PAROBADO NO VALIDO
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_MONTO_APROBADO_NO_VALIDO', true, now(), 'ANP-AGENPORT', 'admin', 'El monto aprobado del registro, no es válido', 0, 1);

--MENSAJE DE ERROR PARA CENTRO EDUCATIVO NO ENCONTRADO
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_CENTRO_EDUCATIVO_NO_ENCONTRADO', true, now(), 'ANP-AGENPORT', 'admin', 'No se encontró un registro de centro educativo', 0, 1);

--MENSAJE DE ERROR PARA DIRECCION DEPARTAMENTAL NO ENCONTRADA
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_DIRECCION_DEPARTAMENTAL_NO_ENCONTRADA', true, now(), 'ANP-AGENPORT', 'admin', 'No se encontró un registro de dirección departamental', 0, 1);



ALTER TABLE siap2.ss_beneficiarios DROP COLUMN b_ges_pres_es;
ALTER TABLE siap2.ss_beneficiarios_hist DROP COLUMN b_ges_pres_es;


UPDATE siap2.ss_ges_pres_es set ges_tipo_ejecucion = null;
ALTER TABLE siap2.ss_ges_pres_es ALTER ges_tipo_ejecucion TYPE character varying(50);
ALTER TABLE siap2.ss_ges_pres_es_hist ALTER ges_tipo_ejecucion TYPE character varying(50); 

-- 6.10.0

INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_SEDES_VACIO', true, now(), 'ANP-AGENPORT', 'admin', 'Debe ingresar al menos una sede', 0, 1);


INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_BENEFICIARIOS_VACIO', true, now(), 'ANP-AGENPORT', 'admin', 'Debe ingresar al menos un beneficiario', 0, 1);





ALTER TABLE siap2.ss_rel_ges_pres_es_anio_fiscal ADD COLUMN monto_total_formulado DECIMAL(20,2);
ALTER TABLE siap2.ss_rel_ges_pres_es_anio_fiscal_hist ADD COLUMN monto_total_formulado DECIMAL(20,2);


ALTER TABLE siap2.ss_rel_ges_pres_es_anio_fiscal ADD COLUMN monto_total_aprobado DECIMAL(20,2);
ALTER TABLE siap2.ss_rel_ges_pres_es_anio_fiscal_hist ADD COLUMN monto_total_aprobado DECIMAL(20,2);


ALTER TABLE siap2.ss_rel_ges_pres_es_anio_fiscal DROP COLUMN cifrado_presupuestario;
ALTER TABLE siap2.ss_rel_ges_pres_es_anio_fiscal_hist DROP COLUMN cifrado_presupuestario;


INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.MontoTotalFormulado', true, now(), 'ANP-AGENPORT', 'admin', 'Monto total formulado', 0, 1);


INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.MontoTotalAprobado', true, now(), 'ANP-AGENPORT', 'admin', 'Monto total aprobado', 0, 1);


CREATE TABLE siap2.ss_rel_pres_anio_financiamiento(
	id bigint primary key,
	rel_anio_presupuesto integer references siap2.ss_rel_ges_pres_es_anio_fiscal(id),
	fuente_financiamiento integer references siap2.ss_fuente_financiamiento (fue_id),
	monto_cifrado_presupuestario DECIMAL(20,2),
        ult_mod timestamp without time zone,
	ult_usuario varchar,
	version bigint,
	CONSTRAINT rel_anio_financiamiento_uk UNIQUE(rel_anio_presupuesto, fuente_financiamiento)
);

CREATE SEQUENCE siap2.seq_rel_presanio_financiamiento INCREMENT 1 START 1 MINVALUE 1 no maxvalue CACHE 1 no cycle;


CREATE TABLE siap2.ss_rel_pres_anio_financiamiento_hist(
	id bigint,
	rel_anio_presupuesto integer,
	fuente_financiamiento integer,
	monto_cifrado_presupuestario DECIMAL(20,2),
        ult_mod timestamp without time zone,
	ult_usuario varchar,
	version bigint,
	start_date timestamp without time zone,
	end_date timestamp without time zone
);


INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.FuentesFinanciamiento', true, now(), 'ANP-AGENPORT', 'admin', 'Fuentes de Financiamiento', 0, 1);

-- 6.11.0

ALTER TABLE siap2.ss_rel_pres_anio_financiamiento DROP COLUMN monto_cifrado_presupuestario;
ALTER TABLE siap2.ss_rel_pres_anio_financiamiento_hist DROP COLUMN monto_cifrado_presupuestario;

ALTER TABLE siap2.ss_rel_pres_anio_financiamiento ADD COLUMN cifrado_presupuestario character varying (100);
ALTER TABLE siap2.ss_rel_pres_anio_financiamiento_hist ADD COLUMN cifrado_presupuestario character varying (100);

ALTER TABLE siap2.ss_rel_pres_anio_financiamiento ADD COLUMN monto_total_formulado decimal(20,2); 
ALTER TABLE siap2.ss_rel_pres_anio_financiamiento_hist ADD COLUMN monto_total_formulado decimal(20,2);

ALTER TABLE siap2.ss_rel_pres_anio_financiamiento ADD COLUMN monto_total_aprobado decimal(20,2);
ALTER TABLE siap2.ss_rel_pres_anio_financiamiento_hist ADD COLUMN monto_total_aprobado decimal(20,2);


CREATE TABLE siap2.ss_rel_acta_categoria(
	rel_id integer primary key,
	rel_acta_id integer references siap2.ss_pago_contrato(pag_id),
	rel_categoria_id integer references siap2.ss_categoria_convenio (cat_id),
	rel_monto_goes DECIMAL(20,2),
        rel_monto_no_goes DECIMAL(20,2),
        rel_ult_mod timestamp without time zone,
	rel_ult_usuario varchar,
	rel_version integer
);


CREATE SEQUENCE IF NOT EXISTS siap2.seq_rel_acta_categoria INCREMENT 1 START 1 MINVALUE 1 no maxvalue CACHE 1 no cycle;

CREATE TABLE siap2.ss_rel_acta_categoria_hist(
	rel_id integer,
	rel_acta_id integer,
	rel_categoria_id integer,
	rel_monto_goes DECIMAL(20,2),
        rel_monto_no_goes DECIMAL(20,2),
        rel_ult_mod timestamp without time zone,
	rel_ult_usuario varchar,
	rel_version integer,
	start_date timestamp without time zone,
	end_date timestamp without time zone
);



--LABEL EXPORTAR AREAS
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.ExportarAreas', true, now(), 'ANP-AGENPORT', 'admin', 'Exportar Areas', 0, 1);

--ADICION DE CAMPO CONCEPTO A GESTION PRESUPUESTO ESCOLAR
alter table siap2.ss_ges_pres_es add ges_concepto varchar(100);
alter table siap2.ss_ges_pres_es_hist add ges_concepto varchar(100);

--ADICION DE RELACION CON TIPO_CUENTA_BANCARIA
alter table siap2.ss_ges_pres_es add ges_tipo_cuenta_bancaria bigint;
alter table siap2.ss_ges_pres_es_hist add ges_tipo_cuenta_bancaria bigint;
alter table siap2.ss_ges_pres_es add constraint subcomponente_tipocuentabancaria_fk foreign key (ges_tipo_cuenta_bancaria) references catalogo.sg_tipo_cuenta_bancaria(tcb_pk);

--ADICION DE CAMPO A TABLA AREAS_INVERSION
alter table siap2.ss_areas_inversion add ai_aplica_plan_compras boolean;
alter table siap2.ss_areas_inversion_hist add ai_aplica_plan_compras boolean;

--LABEL APLICA PLAN COMPRAS
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.AplicaPlanCompras', true, now(), 'ANP-AGENPORT', 'admin', 'Aplica plan compras', 0, 1);

--ADICION DE RELACION DE TRANSFERENCIA_COMPONENTE CON FUENTE_FINANCIAMIENTO
alter table siap2.ss_transferencias_componente add tc_fuente_financiamiento_fk bigint;
alter table siap2.ss_transferencias_componente_hist add tc_fuente_financiamiento_fk bigint;
alter table siap2.ss_transferencias_componente add constraint transferenciacomponente_fuentefinanciamiento_fk foreign key (tc_fuente_financiamiento_fk) references siap2.ss_fuente_financiamiento(fue_id);

--ELIMINACION DE COLUMNA CANTIDAD_UNIDAD_MEDIDA
alter table siap2.ss_ges_pres_es drop column ges_cant_uni_med;
alter table siap2.ss_ges_pres_es_hist drop column ges_cant_uni_med;

--CREAION DE COLUMNAS FOMURLACION_CE Y AJUSTE_CE
alter table siap2.ss_anio_fiscal add ani_formulacion_ce boolean;
alter table siap2.ss_anio_fiscal add ani_ajuste_ce boolean;
alter table siap2.ss_anio_fiscal_hist add ani_formulacion_ce boolean;
alter table siap2.ss_anio_fiscal_hist add ani_ajuste_ce boolean;

--TEXTO FORMULACION_CE
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.FormulacionCE', true, now(), 'ANP-AGENPORT', 'admin', 'Formulación CE', 0, 1);

--TEXTO AJUSTE_CE
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.AjusteCE', true, now(), 'ANP-AGENPORT', 'admin', 'Ajuste CE', 0, 1);

--TEXTO ANULAR
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.Anular', true, now(), 'ANP-AGENPORT', 'admin', 'Anular', 0, 1);

--ADICION DE CAMPO ANIO_FISCAL A TABLA CUENTAS
alter table siap2.ss_cuentas add cu_anio_fiscal bigint;
alter table siap2.ss_cuentas_hist add cu_anio_fiscal bigint;
alter table siap2.ss_cuentas add constraint cuentas_aniofiscal_fk foreign key (cu_anio_fiscal) references siap2.ss_anio_fiscal(ani_id);

--ADICION DE CAMPOS IMPORTE_FIJO Y REMANENTE A LA TABLA TRANSFERENCIA_COMPONENTE
alter table siap2.ss_transferencias_componente add tc_importe_fijo_centro decimal;
alter table siap2.ss_transferencias_componente add tc_remanente boolean;

alter table siap2.ss_transferencias_componente_hist add tc_importe_fijo_centro decimal;
alter table siap2.ss_transferencias_componente_hist add tc_remanente boolean;

--TEXTO IMPORTE_FIJO_CENTRO
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.ImporteFijoCentro', true, now(), 'ANP-AGENPORT', 'admin', 'Importe fijo por centro', 0, 1);

--TEXTO REMANENTE
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.Remanente', true, now(), 'ANP-AGENPORT', 'admin', 'Remanente', 0, 1);

--ERROR NO SELECCION PORCENTAJE
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_OPCION_PORCENTAJE_NO_SELECCION', true, now(), 'ANP-AGENPORT', 'admin', 'No se seleccionó una opción de porcentaje', 0, 1);


-- 6.14.0


--ERROR REGISTROS HIJOS EN TOPE
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_NO_ELIMINAR_REGISTROS_TOPE', true, now(), 'ANP-AGENPORT', 'admin', 'No se puede eliminar, existen registros hijos relacionados con Techo Presupuestal', 0, 1);

--ERROR REGISTRO A ELIMINAR NO ENCONTRADO
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_REGISTRO_NO_ENCONTRADO', true, now(), 'ANP-AGENPORT','admin', 'No se encontró el registro seleccionado a eliminar', 0, 1);

--ERROR REGISTROS EN TRANFERENCIA COMPONENTE
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_NO_ELIMINAR_REGISTROS_TRANSFERENCIA_COMPONENTE', true, now(), 'ANP-AGENPORT', 'admin', 'No se puede eliminar, existen registros hijos relacionados con Transferencia Componente', 0, 1);

--ERROR TECHO EXISTENTE ENCONTRADO
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_TECHO_EXISTENTE_ENCONTRADO', true, now(), 'ANP-AGENPORT', 'admin', 'Ya existe un registro de TechoPresupuestal con el mismo Subcomponente, Linea presupuestaria y sede', 0, 1);

--ERROR DURANTE GENERACION RESULTADOS
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_GENERACION_RESULTADOS', true, now(), 'ANP-AGENPORT', 'admin', 'Ocurrió un error durante la generación de resultados', 0, 1);

--ERROR SUBCOMPONENTE_NO_SELECCIONADO
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_SUBCOMPONENTE_NO_SELECCIONADO', true, now(), 'ANP-AGENPORT', 'admin', 'No se seleccionó ningún Subcomponente', 0, 1);

--ERROR LINEA_PRESUPUESTARIA_NO_SELECCIONADA
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_LINEA_PRESUPUESTARIA_NO_SELECCIONADA', true, now(), 'ANP-AGENPORT', 'admin', 'No se seleccionó una linea presupuestaria', 0, 1);

--ERROR ESTADO_NO_SELECCIONADO
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_ESTADO_NO_SELECCIONADO', true, now(), 'ANP-AGENPORT', 'admin', 'No se seleccionó un estado', 0, 1);

--ERROR ANIO_FISCAL_NO_SELECCIONADO
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_ANIO_FISCAL_NO_SELECCIONADO', true, now(), 'ANP-AGENPORT', 'admin', 'No se seleccionó un año fiscal', 0, 1);

--Creacion de relacion con fuente de recursos
alter table siap2.ss_rel_pres_anio_financiamiento add fuente_recursos bigint;
alter table siap2.ss_rel_pres_anio_financiamiento_hist add fuente_recursos bigint;
alter table siap2.ss_rel_pres_anio_financiamiento add constraint relpresaniofinanciamiento_fuentefinanciamiento_fk foreign key (fuente_recursos) references siap2.ss_fuente_recursos(fue_id);

--ERROR MONTO_APROBADO NO INGRESADO
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_MONTO_APROBADO_NO_INGRESADO', true, now(), 'ANP-AGENPORT', 'admin', 'No se ingresó un monto aprobado', 0, 1);

--RELACION DE TRANSFERENCIA_COMPONENTE CON DIRECCION_DEPARTAMENTAL
alter table siap2.ss_transferencias_componente add tc_direccion_departamental bigint;
alter table siap2.ss_transferencias_componente_hist add tc_direccion_departamental bigint;
alter table siap2.ss_transferencias_componente add constraint transferenciacomponente_direcciondepartamental_fk foreign key (tc_direccion_departamental) references finanzas.sg_direcciones_departamentales(ded_pk);

--TEXTO DIRECCION DEPARTAMENTAL
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.DireccionDepartamental', true, now(), 'ANP-AGENPORT', 'admin', 'Dirección departamental', 0, 1);

--ERROR DIRECCION DEPARTAMENTAL NO SELECCIONADA
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_DIRECCION_DEPARTAMENTAL_NO_SELECCIONADA', true, now(), 'ANP-AGENPORT', 'admin', 'No se seleccionó una dirección departamental', 0, 1);

--ERROR TECHOS_NO_ENCONTRADOS
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_TECHOS_NO_ENCONTRADOS', true, now(), 'ANP-AGENPORT', 'admin', 'No se encontraron techos presupuestales a procesar', 0, 1);

--ACTUALIZACION DE MENSAJE DE ERROR GENERAL
update siap2.ss_textos st set tex_valor = 'Ocurrió un error al procesar la acción, intente nuevamente o contacte a soporte técnico' where tex_codigo = 'ERR_GRAL';

--RELACION DE TRANSFERENCIA_COMPONENTE CON FUENTE_RECURSOS
alter table siap2.ss_transferencias_componente add tc_fuente_recursos_fk bigint;
alter table siap2.ss_transferencias_componente_hist add tc_fuente_recursos_fk bigint;
alter table siap2.ss_transferencias_componente add constraint transferenciacomponente_fuenterecursos_fk foreign key (tc_fuente_recursos_fk) references siap2.ss_fuente_recursos(fue_id);

--ELIMINACION DE COLUMNAS FUENTE_FINANCIAMIENTO, FUENTE_RECURSOS
alter table siap2.ss_ges_pres_es drop column ges_fuente_financiamiento;
alter table siap2.ss_ges_pres_es drop column ges_fuente_recursos;

--TEXTO MONTO_POR_MATRICULA_EN_FORMULACION
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.MontoPorMatriculaEnFormulacion', true, now(), 'ANP-AGENPORT', 'admin', 'Monto por matrícula (En formulación)', 0, 1);

--TEXTO MONTO_MINIMO_EN_FORMULACION
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.MontoMinimoEnFormulacion', true, now(), 'ANP-AGENPORT', 'admin', 'Monto Mínimo (En formulación)', 0, 1);

--ERROR MONTO_FORMULADO_MENOR
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_MONTO_TOTAL_FORMULADO_MENOR', true, now(), 'ANP-AGENPORT', 'admin', 'El monto presupuestal formulado no puede ser menor al monto total de fuentes de financiamiento', 0, 1);

--ERROR MONTO_APROBADO_MENOR
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_MONTO_TOTAL_APROBADO_MENOR', true, now(), 'ANP-AGENPORT', 'admin', 'El monto presupuestal aprobado no puede ser menor al monto total de fuentes de financiamiento', 0, 1);


-- 6.15.1

--ERROR FUENTE_FINANCIAMIENTO_NO_SELECCIONADA
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_FUENTE_FINANCIAMIENTO_NO_SELECCIONADA', true, now(), 'ANP-AGENPORT', 'admin', 'No se seleccionó una fuente de financiamiento', 0, 1);

--ERROR FUENTE_RECURSOS_NO_SELECCIONADA
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_FUENTE_RECURSOS_NO_SELECCIONADA', true, now(), 'ANP-AGENPORT', 'admin', 'No se seleccionó una fuente de recursos', 0, 1);

--ERROR TIPO_MONTO_NO_INGRESADO
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_TIPO_MONTO_NO_INGRESADO', true, now(), 'ANP-AGENPORT', 'admin', 'No se seleccionó el tipo de monto a cargar', 0, 1);

--TABLA PARA RANGOS DE MATRICULA
create table siap2.ss_rangos_de_matricula(
	id bigint,
	id_rel_anio_presupuesto bigint,
	rango decimal,
	valor integer,
	ult_mod timestamp without time zone,
	ult_usuario varchar,
	version bigint,
	constraint rangosmatricula_pk primary key (id),
	constraint rangosmatricula_relaniopresupuesto_fk foreign key (id_rel_anio_presupuesto) references siap2.ss_rel_ges_pres_es_anio_fiscal(id)
);

CREATE SEQUENCE IF NOT EXISTS siap2.seq_rangos_matricula INCREMENT 1 START 1 MINVALUE 1 no maxvalue CACHE 1 no cycle;

create table siap2.ss_rangos_de_matricula_hist(
	id bigint,
	id_rel_anio_presupuesto bigint,
	rango decimal,
	valor integer,
	ult_mod timestamp without time zone,
	ult_usuario varchar,
	version bigint,
	start_date timestamp without time zone,
	end_date timestamp without time zone
);


--TEXTO RANGOS DE MATRICULA
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.RangosMatricula', true, now(), 'ANP-AGENPORT', 'admin', 'Rangos de matrícula', 0, 1);

--TEXTO RANGO
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.Rango', true, now(), 'ANP-AGENPORT', 'admin', 'Rango', 0, 1);

--TEXTO CANTIDAD DE RANGOS
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.CantidadRangos', true, now(), 'ANP-AGENPORT', 'admin', 'Catidad de rangos', 0, 1);

--TEXTO RANGOS
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.Rangos', true, now(), 'ANP-AGENPORT', 'admin', 'Rangos', 0, 1);

--TEXTO CTDAD_RANGO_MATRICULA_NO_INGRESADOANGOS
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_CTDAD_RANGO_MATRICULA_NO_INGRESADO', true, now(), 'ANP-AGENPORT', 'admin', 'No se ingresó la cantidad de rangos a crear', 0, 1);

--TEXTO NINGUN_RANGO_MATRICULA_CREADO
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_NINGUN_RANGO_MATRICULA_CREADO', true, now(), 'ANP-AGENPORT', 'admin', 'No se creará ningun rango de matricula', 0, 1);

--TEXTO OEG_NO_INGRESADO
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_OEG_NO_INGRESADO', true, now(), 'ANP-AGENPORT', 'admin', 'No se ingresó un objeto específico de gasto', 0, 1);

--TEXTO NUMERO UNIDAD
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.NumeroUnidad', true, now(), 'ANP-AGENPORT', 'admin', 'Número de unidad presupuestaria', 0, 1);

--TEXTO NOMBRE UNIDAD
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.NombreUnidad', true, now(), 'ANP-AGENPORT', 'admin', 'Nombre de la unidad presupuestaria', 0, 1);

--ERRO CUENTA_RELACION_TRANSFERENCIA
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_CUENTA_RELACION_TRANSFERENCIA', true, now(), 'ANP-AGENPORT', 'admin', 'La cuenta seleccionada posee una relación con transferencia componente', 0, 1);

--ERRO CUENTA_RELACION_COMPONENTE
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_CUENTA_RELACION_COMPONENTE', true, now(), 'ANP-AGENPORT', 'admin', 'La cuenta seleccionada posee una relación con componente año fiscal', 0, 1);

--ERRO NO_POSIBLE_ELIMINAR
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_NO_POSIBLE_ELIMINAR', true, now(), 'ANP-AGENPORT', 'admin', 'No fué posible eliminar el registro seleccionado', 0, 1);

--TEXTO NOMBRE_LINEA
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.NombreLinea', true, now(), 'ANP-AGENPORT', 'admin', 'Nombre de la Línea de trabajo presupuestaria', 0, 1);

--TEXTO NOMBRE_LINEA
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.NumeroLinea', true, now(), 'ANP-AGENPORT', 'admin', 'Número de Línea de trabajo presupuestaria', 0, 1);

--TEXTO COMPONENTE_RELACION_PRESUPUESTO_SUBCOMPONENTE
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_COMPONENTE_RELACION_PRESUPUESTO_SUBCOMPONENTE', true, now(), 'ANP-AGENPORT', 'admin', 'No se puede eliminar, existen registros relacionados con Presupuesto componente', 0, 1);

--TEXTO MONTO A DEDUCIR
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.MontoDeducir', true, now(), 'ANP-AGENPORT', 'admin', 'Monto a deducir', 0, 1);

comment on table SIAP2.ss_categoria_presupuesto_escolar is 'Categorias de componentes de gestion de presupuesto escolar';
comment on column SIAP2.ss_categoria_presupuesto_escolar.cpe_codigo  is 'Codigo identificador del registro';
comment on column SIAP2.ss_categoria_presupuesto_escolar.cpe_habilitado  is 'Estado del registro';
comment on column SIAP2.ss_categoria_presupuesto_escolar.cpe_id  is 'Correlativo';
comment on column SIAP2.ss_categoria_presupuesto_escolar.cpe_nombre  is 'Nombre del registro';
comment on column SIAP2.ss_categoria_presupuesto_escolar.cpe_ult_mod  is 'Última fecha de modificación de registro';
comment on column SIAP2.ss_categoria_presupuesto_escolar.cpe_ult_usuario  is 'Último usuario en modificar registro';
comment on column SIAP2.ss_categoria_presupuesto_escolar.cpe_version  is 'Número de version del registro';


comment on table SIAP2.ss_areas_inversion is 'Catálogo de áreas de inversion de Inversión';
comment on column SIAP2.ss_areas_inversion.ai_aplica_plan_compras  is 'Identificador registro aplica a plan anual de compras';
comment on column SIAP2.ss_areas_inversion.ai_area_padre  is 'Registro recursivo padre del registro actual';
comment on column SIAP2.ss_areas_inversion.ai_asociado_plan_anual_compras  is '';
comment on column SIAP2.ss_areas_inversion.ai_codigo  is 'Codigo identificador del registro';
comment on column SIAP2.ss_areas_inversion.ai_descripcion  is 'Descripción del registro';
comment on column SIAP2.ss_areas_inversion.ai_habilitado  is 'Estado del registro';
comment on column SIAP2.ss_areas_inversion.ai_id  is 'Correlativo';
comment on column SIAP2.ss_areas_inversion.ai_nombre  is 'Nombre del registro';
comment on column SIAP2.ss_areas_inversion.ai_orden  is 'Posición de orden del registro';
comment on column SIAP2.ss_areas_inversion.ai_ult_mod is 'Última fecha de modificación de registro';
comment on column SIAP2.ss_areas_inversion.ai_ult_usuario  is 'Último usuario en modificar registro';
comment on column SIAP2.ss_areas_inversion.ai_version  is 'Número de version del registro';


comment on table SIAP2.ss_tipo_credito is 'Catálogo de áreas de inversion de Inversión';
comment on column SIAP2.ss_tipo_credito.tc_cod  is 'Codigo identificador del registro';
comment on column SIAP2.ss_tipo_credito.tc_habilitado  is 'Estado del registro';
comment on column SIAP2.ss_tipo_credito.tc_id  is 'Correlativo';
comment on column SIAP2.ss_tipo_credito.tc_nombre  is 'Nombre del registro';
comment on column SIAP2.ss_tipo_credito.tc_orden  is 'Posición de orden del registro';
comment on column SIAP2.ss_tipo_credito.tc_ult_mod  is 'Última fecha de modificación de registro';
comment on column SIAP2.ss_tipo_credito.tc_ult_usuario  is 'Último usuario en modificar registro';
comment on column SIAP2.ss_tipo_credito.tc_version  is 'Número de version del registro';


comment on table SIAP2.ss_ges_pres_es is 'Subcomponentes de gestion de prCodigo identificador del registroesupuesto escolar';
comment on column SIAP2.ss_ges_pres_es.ges_areas_inversion  is 'Areas de inversion  la que aplica el registro';
comment on column SIAP2.ss_ges_pres_es.ges_categoria  is 'Descripción de categoría';
comment on column SIAP2.ss_ges_pres_es.ges_categoria_componente  is 'Categoria de componente de presupuesto escolar';
comment on column SIAP2.ss_ges_pres_es.ges_centros_educativos  is 'Aplica centros educativos';
comment on column SIAP2.ss_ges_pres_es.ges_centros_educativos_privados_no_sub  is 'Aplica centros educativos no subvencionados';
comment on column SIAP2.ss_ges_pres_es.ges_centros_educativos_privados_sub  is 'Aplica centros educativos subvencionados';
comment on column SIAP2.ss_ges_pres_es.ges_circulos_alfabetizacion  is 'Aplica circulos de alfabetización';
comment on column SIAP2.ss_ges_pres_es.ges_circulos_familia  is 'Aplica circulos de familia';
comment on column SIAP2.ss_ges_pres_es.ges_cod  is 'Codigo identificador del registro';
comment on column SIAP2.ss_ges_pres_es.ges_concepto  is 'Descripción concepto';
comment on column SIAP2.ss_ges_pres_es.ges_convenio  is 'Aplica convenio';
comment on column SIAP2.ss_ges_pres_es.ges_ctdad_anio  is 'Identificador de cantidad de veces al año';
comment on column SIAP2.ss_ges_pres_es.ges_descripcion  is 'Descripción del registro';
comment on column SIAP2.ss_ges_pres_es.ges_habilitado  is 'Estado del registro';
comment on column SIAP2.ss_ges_pres_es.ges_id  is 'Correlativo';
comment on column SIAP2.ss_ges_pres_es.ges_incluir_sedes_adscritas  is 'Aplica incluir sedes adscritas';
comment on column SIAP2.ss_ges_pres_es.ges_mto_minimo  is 'Aplica monto minimo';
comment on column SIAP2.ss_ges_pres_es.ges_nombre  is 'Nombre del registro';
comment on column SIAP2.ss_ges_pres_es.ges_parametro  is 'Parametro de relacion';
comment on column SIAP2.ss_ges_pres_es.ges_proyecto  is 'Proyecto relacionado';
comment on column SIAP2.ss_ges_pres_es.ges_sedes_educame  is 'Aplica a sedes edúcame';
comment on column SIAP2.ss_ges_pres_es.ges_subactividad  is 'Subactividad relacionada';
comment on column SIAP2.ss_ges_pres_es.ges_tipo  is 'Tipo de recurso';
comment on column SIAP2.ss_ges_pres_es.ges_tipo_credito  is 'Tipo de credito';
comment on column SIAP2.ss_ges_pres_es.ges_tipo_cuenta_bancaria  is 'Tipo de cuenta bancaria';
comment on column SIAP2.ss_ges_pres_es.ges_tipo_ejecucion  is 'Tipo de ejecución';
comment on column SIAP2.ss_ges_pres_es.ges_tipo_transferencia  is 'Tipo de transferencia';
comment on column SIAP2.ss_ges_pres_es.ges_ult_mod  is 'Última fecha de modificación de registro';
comment on column SIAP2.ss_ges_pres_es.ges_ult_usuario  is 'Último usuario en modificar registro';
comment on column SIAP2.ss_ges_pres_es.ges_uni_med  is 'Unidad de medida';
comment on column SIAP2.ss_ges_pres_es.ges_unidad_tecnica  is 'Unidad técnica a la que aplica';
comment on column SIAP2.ss_ges_pres_es.ges_usuario_responsable  is 'Usuario responsable de la unidad tecnica';
comment on column SIAP2.ss_ges_pres_es.ges_version  is 'Número de version del registro';



comment on table SIAP2.ss_rel_ges_pres_es_anio_fiscal is 'Tabla de relacion de Subcomponente de gestion de presupuesto escolar y anio fiscal';
comment on column SIAP2.ss_rel_ges_pres_es_anio_fiscal.deducir_ges_pres_es  is 'Subcomponente a deducir';
comment on column SIAP2.ss_rel_ges_pres_es_anio_fiscal.fecha_matricula  is 'Fecha de matrícula';
comment on column SIAP2.ss_rel_ges_pres_es_anio_fiscal.id  is 'Correlativo';
comment on column SIAP2.ss_rel_ges_pres_es_anio_fiscal.id_anio_fiscal  is 'Relación con AñoFiscal';
comment on column SIAP2.ss_rel_ges_pres_es_anio_fiscal.id_ges_pres_es  is 'Relación con Subcomponente';
comment on column SIAP2.ss_rel_ges_pres_es_anio_fiscal.id_sub_cuenta  is 'Relación con LineaPresupuestaria';
comment on column SIAP2.ss_rel_ges_pres_es_anio_fiscal.monto_minimo  is 'Monto minimo';
comment on column SIAP2.ss_rel_ges_pres_es_anio_fiscal.monto_minimo_aprobacion is 'Monto minimo aprobación';
comment on column SIAP2.ss_rel_ges_pres_es_anio_fiscal.monto_por_matricula  is 'Monto por matrícula';
comment on column SIAP2.ss_rel_ges_pres_es_anio_fiscal.monto_por_matricula_aprobacion  is 'Monto por matrícula aprobación';
comment on column SIAP2.ss_rel_ges_pres_es_anio_fiscal.monto_total_aprobado  is 'Monto total aprobado';
comment on column SIAP2.ss_rel_ges_pres_es_anio_fiscal.monto_total_formulado  is 'Monto total formulado';
comment on column SIAP2.ss_rel_ges_pres_es_anio_fiscal.objeto_especifico_gasto  is 'Objeto específico de gasto';
comment on column SIAP2.ss_rel_ges_pres_es_anio_fiscal.tipo  is 'Descripción de tipo';
comment on column SIAP2.ss_rel_ges_pres_es_anio_fiscal.ult_mod  is 'Ultima fecha de modificación';
comment on column SIAP2.ss_rel_ges_pres_es_anio_fiscal.ult_usuario  is 'Último usuario modificador';
comment on column SIAP2.ss_rel_ges_pres_es_anio_fiscal.version  is 'Última version del registro';


comment on table SIAP2.ss_rubro is 'Catálogo de rubros';
comment on column SIAP2.ss_rubro.ru_codigo  is 'Código itentificador del registro';
comment on column SIAP2.ss_rubro.ru_habilitado  is 'Estado del registro';
comment on column SIAP2.ss_rubro.ru_id  is 'Correlativo';
comment on column SIAP2.ss_rubro.ru_nombre  is 'Nombre del registro';
comment on column SIAP2.ss_rubro.ru_ult_mod  is 'Última fecha de modificación';
comment on column SIAP2.ss_rubro.ru_ult_usuario  is 'Último usuario modificador';
comment on column SIAP2.ss_rubro.ru_version  is 'Última version del registro';



comment on table SIAP2.ss_beneficiarios is 'Catalogo de beneficiarios';
comment on column SIAP2.ss_beneficiarios.b_ciclo  is 'Relación con SgCiclo';
comment on column SIAP2.ss_beneficiarios.b_ges_pres_es_anio_fiscal  is 'Relación con Subcomponente';
comment on column SIAP2.ss_beneficiarios.b_id  is 'Correlativo';
comment on column SIAP2.ss_beneficiarios.b_modalidad  is 'Relación con modalidad educativa';
comment on column SIAP2.ss_beneficiarios.b_modalidad_atencion  is 'Relación con modalidad de atención';
comment on column SIAP2.ss_beneficiarios.b_nivel  is 'Relación con nivel';
comment on column SIAP2.ss_beneficiarios.b_organizacion_curricular  is 'Relación on orgaización curricular';
comment on column SIAP2.ss_beneficiarios.b_sub_modalidad  is 'Relación con sub modalidad de atención';
comment on column SIAP2.ss_beneficiarios.v_valor  is 'valor';


comment on table SIAP2.ss_cuentas is 'Catálogo de cuentas';
comment on column SIAP2.ss_cuentas.cu_anio_fiscal  is 'Relación con año fiscal';
comment on column SIAP2.ss_cuentas.cu_codigo  is 'Código identificador del registro';
comment on column SIAP2.ss_cuentas.cu_cuenta_padre  is 'Relacion recursiva con un registro padre';
comment on column SIAP2.ss_cuentas.cu_descripcion  is 'Descripción del registro';
comment on column SIAP2.ss_cuentas.cu_habilitado  is 'Estado del registro';
comment on column SIAP2.ss_cuentas.cu_id  is 'Correlativo';
comment on column SIAP2.ss_cuentas.cu_orden  is 'Codigo de orden del registro';
comment on column SIAP2.ss_cuentas.cu_ult_mod  is 'Última fecha de modificación';
comment on column SIAP2.ss_cuentas.cu_ult_usuario  is 'Último usuario modificador';
comment on column SIAP2.ss_cuentas.cu_version  is 'Última version del registro';


comment on table SIAP2.ss_tope_presupestal is 'Detalle de techos presupuestales';
comment on column SIAP2.ss_tope_presupestal.tp_anio_fiscal  is 'Relación con año fiscal';
comment on column SIAP2.ss_tope_presupestal.tp_componente  is 'Relación con componente de presupuesto escolar';
comment on column SIAP2.ss_tope_presupestal.tp_estado  is 'Estado del registro';
comment on column SIAP2.ss_tope_presupestal.tp_id  is 'Correlativo';
comment on column SIAP2.ss_tope_presupestal.tp_monto_aprobado  is 'Monto en aprobación';
comment on column SIAP2.ss_tope_presupestal.tp_monto_formulacion  is 'Monto en formulación';
comment on column SIAP2.ss_tope_presupestal.tp_movimiento  is 'Relación con presupuesto escolar movimiento';
comment on column SIAP2.ss_tope_presupestal.tp_sede  is 'Relación con sede';
comment on column SIAP2.ss_tope_presupestal.tp_sub_cuenta  is 'Relación con Cuenta';
comment on column SIAP2.ss_tope_presupestal.tp_ult_mod  is 'Última fecha de modificación';
comment on column SIAP2.ss_tope_presupestal.tp_ult_usuario  is 'Último usuario modificador';
comment on column SIAP2.ss_tope_presupestal.tp_version  is 'Última version del registro';


comment on table SIAP2.ss_transferencias_componente is 'Detalle de transferencias de techos presupuestales por subcomponentes';
comment on column SIAP2.ss_transferencias_componente.tc_anio_fiscal  is 'Relación con año fiscal';
comment on column SIAP2.ss_transferencias_componente.tc_componente  is 'Relación con categoria de presupuesto escolar';
comment on column SIAP2.ss_transferencias_componente.tc_direccion_departamental  is 'Relación con dirección departamental';
comment on column SIAP2.ss_transferencias_componente.tc_estado  is 'Estado del registro';
comment on column SIAP2.ss_transferencias_componente.tc_fuente_financiamiento_fk  is 'Fuente de financiamiento asociada al presupuesto';
comment on column SIAP2.ss_transferencias_componente.tc_fuente_recursos_fk  is 'Fuente de recursos asociada al presupuesto';
comment on column SIAP2.ss_transferencias_componente.tc_id  is 'Correlativo';
comment on column SIAP2.ss_transferencias_componente.tc_importe_fijo_centro  is 'Opción importe fijo por centro';
comment on column SIAP2.ss_transferencias_componente.tc_linea_presupuestaria  is 'Relación con Cuenta';
comment on column SIAP2.ss_transferencias_componente.tc_porcentaje  is 'Porcentaje de asignación';
comment on column SIAP2.ss_transferencias_componente.tc_remanente  is 'Opción remanente';
comment on column SIAP2.ss_transferencias_componente.tc_subcomponente  is 'Relación con subcomponente presupuesto escolar';
comment on column SIAP2.ss_transferencias_componente.tc_ult_mod  is 'Última fecha de modificación';
comment on column SIAP2.ss_transferencias_componente.tc_ult_usuario  is 'Último usuario modificador';
comment on column SIAP2.ss_transferencias_componente.tc_unidad_presupuestaria  is 'Relación con Cuenta';
comment on column SIAP2.ss_transferencias_componente.tc_version  is 'Última version del registro';


comment on table SIAP2.ss_desembolso_transferencia_componente is 'Detalle de desembolsos por transferencia';
comment on column SIAP2.ss_desembolso_transferencia_componente.des_estado  is 'Estado del registro';
comment on column SIAP2.ss_desembolso_transferencia_componente.des_fecha_desembolso  is 'Fecha de desembolso';
comment on column SIAP2.ss_desembolso_transferencia_componente.des_id  is 'Correlativo';
comment on column SIAP2.ss_desembolso_transferencia_componente.des_porcentaje  is 'Porcentaje de desembolso';
comment on column SIAP2.ss_desembolso_transferencia_componente.des_transferencia_desembolso  is 'Relación con transferencia componente';
comment on column SIAP2.ss_desembolso_transferencia_componente.des_ult_mod  is 'Última fecha de modificación';
comment on column SIAP2.ss_desembolso_transferencia_componente.des_ult_usuario  is 'Último usuario modificador';
comment on column SIAP2.ss_desembolso_transferencia_componente.des_version  is 'Última version del registro';


comment on table SIAP2.ss_rel_pres_anio_financiamiento is 'Tabla de relación entre Subcomponente-AñoFiscal y Fuente de financiamiento';
comment on column SIAP2.ss_rel_pres_anio_financiamiento.cifrado_presupuestario  is 'Código de cifrado presupuestario';
comment on column SIAP2.ss_rel_pres_anio_financiamiento.fuente_financiamiento  is 'Relación con fuente de financiamiento';
comment on column SIAP2.ss_rel_pres_anio_financiamiento.fuente_recursos  is 'Relación con fuente de recursos';
comment on column SIAP2.ss_rel_pres_anio_financiamiento.id  is 'Correlativo';
comment on column SIAP2.ss_rel_pres_anio_financiamiento.monto_total_aprobado  is 'Monto total aprobado';
comment on column SIAP2.ss_rel_pres_anio_financiamiento.monto_total_formulado  is 'Monto total formulado';
comment on column SIAP2.ss_rel_pres_anio_financiamiento.rel_anio_presupuesto  is 'Relacion con Subcomponente-AñoFiscal';
comment on column SIAP2.ss_rel_pres_anio_financiamiento.ult_mod  is 'Última fecha de modificación';
comment on column SIAP2.ss_rel_pres_anio_financiamiento.ult_usuario  is 'Último usuario modificador';
comment on column SIAP2.ss_rel_pres_anio_financiamiento."version"  is 'Última version del registro';


comment on table SIAP2.ss_rangos_de_matricula is 'Tabla de detalle de rangos de matrícula';
comment on column SIAP2.ss_rangos_de_matricula.id  is 'Correlativo';
comment on column SIAP2.ss_rangos_de_matricula.id_rel_anio_presupuesto  is 'Relación con Subcomponente-AñoFiscal';
comment on column SIAP2.ss_rangos_de_matricula.rango  is 'Rango';
comment on column SIAP2.ss_rangos_de_matricula.ult_mod  is 'Última fecha de modificación';
comment on column SIAP2.ss_rangos_de_matricula.ult_usuario  is 'Último usuario modificador';
comment on column SIAP2.ss_rangos_de_matricula.valor  is 'Valor';
comment on column SIAP2.ss_rangos_de_matricula."version"  is 'Última version del registro';


comment on table SIAP2.ss_rel_gespresanio_clasificacion is 'Clasificación del presupuesto escola';
comment on column SIAP2.ss_rel_gespresanio_clasificacion.cla_pk  is 'Correlativo';
comment on column SIAP2.ss_rel_gespresanio_clasificacion.id  is 'Relación con clasificaciones';


comment on table SIAP2.ss_rel_componentepresupescolar_areainv is 'Tabla en la cual se establece cuáles son las áreas de inversión habilitadas para un subcomponente';
comment on column SIAP2.ss_rel_componentepresupescolar_areainv.ai_id is 'Correlativo';
comment on column SIAP2.ss_rel_componentepresupescolar_areainv.ges_id is 'Relación con subcomponente';

comment on table SIAP2.ss_rel_areas_inversion_insumo is 'Relación entre las subáreas de inversión y los insumos que se pueden aplicar a dicha subárea';
comment on column SIAP2.ss_rel_areas_inversion_insumo.id is 'Correlativo';
comment on column SIAP2.ss_rel_areas_inversion_insumo.id_area_inversion is 'Relación con Área de inversión';
comment on column SIAP2.ss_rel_areas_inversion_insumo.id_insumo is 'Relación con insumo';

--TEXTO PREVISUALIZAR
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.Previsualizar', true, now(), 'ANP-AGENPORT', 'admin', 'Previsualizar', 0, 1);

--ELIMINACION DE RELACION CON FUENTE_FINANCIAMIENTO Y DIRECCION_DEPARTAMENTAL
alter table siap2.ss_transferencias_componente drop column tc_fuente_financiamiento_fk;
alter table siap2.ss_transferencias_componente drop column tc_direccion_departamental;

--TEXTO PREVISUALIZACION TRANSFERENCIAS COMPONENTE
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.TransferenciasComponentePreview', true, now(), 'ANP-AGENPORT', 'admin', 'Previsualización de transferencias por componente', 0, 1);

--TEXTO CODIGO PROYECTO
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.CodigoProyecto', true, now(), 'ANP-AGENPORT', 'admin', 'Código de proyecto', 0, 1);

--OPERACION TRANSFERENCIA COMPONENTE PREVISUALIZACION
INSERT INTO siap2.ss_operacion
(ope_id, ope_codigo, ope_descripcion, ope_nombre, ope_origen, ope_tipocampo, ope_user_code, ope_version, ope_vigente, ope_categoria_id)
VALUES(NEXTVAL('siap2.seq_operacion'), 'CONSULTA_TRANSFERENCIA_COMPONENTE_PREVISUALIZACION', 'Previsualización de transferencia por componente', 'Previsualización de transferencia por componente', 'SIAP2', 'TIPO_UNICO', 0, NULL, true, NULL);

--TEXTO CODIGO DE SEDE
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.CodigoSede', true, now(), 'ANP-AGENPORT', 'admin', 'Código de Sede', 0, 1);

--TEXTO MONTO AUTORIZADO
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.MontoAutorizado', true, now(), 'ANP-AGENPORT', 'admin', 'Monto autorizado', 0, 1);

--TEXTO NOMBRE SEDE
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.NombreSede', true, now(), 'ANP-AGENPORT', 'admin', 'Nombre de Sede', 0, 1);

--GENERACION_TRANSFERENCIAS_ADVERTENCIA_CONTINUAR
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'GENERACION_TRANSFERENCIAS_ADVERTENCIA_CONTINUAR', true, now(), 'ANP-AGENPORT', 'admin', 'Se generarán las transferencias para las sedes seleccionadas, desea coninuar?', 0, 1);

--TEXTO TIPO DE PROCESO
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.TipoProceso', true, now(), 'ANP-AGENPORT', 'admin', 'Tipo de proceso', 0, 1);

--TEXTO CREACION
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.Creacion', true, now(), 'ANP-AGENPORT', 'admin', 'Creación', 0, 1);

--TEXTO ACTUALIZACION
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.Actualizacion', true, now(), 'ANP-AGENPORT', 'admin', 'Actualización', 0, 1);


--RECREACION DE TABLA TRANSFERENCIAS
drop table siap2.ss_transferencias;
drop table siap2.ss_transferencias_hist;

create table siap2.ss_transferencias (
	tra_id bigint,
	tra_id_anio_fiscal bigint,
	tra_id_subcomponente bigint,
	tra_id_linea_presupuestaria bigint,
	tra_porcentaje decimal,
	tra_estado integer,
	tra_importe_fijo_centro decimal,
	tra_remanente boolean,
	tra_ult_mod timestamp without time zone,
	tra_ult_usuario varchar,
	tra_version bigint,
	constraint transferencias_pk primary key (tra_id),
	constraint transferencias_aniofiscal_fk foreign key (tra_id_anio_fiscal) references siap2.ss_anio_fiscal(ani_id),
	constraint transferencias_subcomponente_fk foreign key (tra_id_subcomponente) references siap2.ss_ges_pres_es(ges_id),
	constraint transferencias_lineapresupuestaria_fk foreign key(tra_id_linea_presupuestaria) references siap2.ss_cuentas(cu_id)
);
create table siap2.ss_transferencias_hist (
	tra_id bigint,
	tra_id_anio_fiscal bigint,
	tra_id_subcomponente bigint,
	tra_id_linea_presupuestaria bigint,
	tra_porcentaje decimal,
	tra_estado integer,
	tra_importe_fijo_centro decimal,
	tra_remanente boolean,
	tra_ult_mod timestamp without time zone,
	tra_ult_usuario varchar,
	tra_version bigint,
        start_date timestamp without time zone,
	end_date timestamp without time zone
);

--CREACION DE RELACION CON TRANSFERENCIAS
ALTER TABLE siap2.ss_transferencias_componente ADD COLUMN tc_transferencia bigint;
ALTER TABLE siap2.ss_transferencias_componente ADD CONSTRAINT tc_transferencia_fkey FOREIGN KEY (tc_transferencia) REFERENCES siap2.ss_transferencias(tra_id) MATCH FULL ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE siap2.ss_transferencias_componente_hist ADD COLUMN tc_transferencia bigint;


--TEXTO PERSUPUESTO_NO_TIENE_REL_FINANCIAMIENTO
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_PERSUPUESTO_NO_TIENE_REL_FINANCIAMIENTO', true, now(), 'ANP-AGENPORT', 'admin','Un registro de Presupuesto del Subcomponente seleccionado, no tiene una fuente de financiamiento y fuente de recursos asociada', 0, 1);

--TEXTO NO_EXISTE_REL_COMPONENTE_ANIO_FISCAL
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_NO_EXISTE_REL_COMPONENTE_ANIO_FISCAL', true, now(), 'ANP-AGENPORT', 'admin','No se encontró un Presupuesto de Subcomponente con los atributos de la transferencia', 0, 1);

--TEXTO GENERAR TRANSFERENCIAS POR COMPONENTE
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.GenerarTransferenciaComponente', true, now(), 'ANP-AGENPORT', 'admin','Generar Transferencias por componente', 0, 1);

--6.16.0


comment on table SIAP2.ss_transferencias is 'Relación entre las subáreas de inversión y los insumos que se pueden aplicar a dicha subárea';
comment on column SIAP2.ss_transferencias.tra_estado is 'Estado del registro';
comment on column SIAP2.ss_transferencias.tra_id is 'Correlativo';
comment on column SIAP2.ss_transferencias.tra_id_anio_fiscal is 'Relación con año fiscal';
comment on column SIAP2.ss_transferencias.tra_id_linea_presupuestaria is 'Relación con Cuenta';
comment on column SIAP2.ss_transferencias.tra_id_subcomponente is 'Relación con subcomponente presupuesto escolar';
comment on column SIAP2.ss_transferencias.tra_importe_fijo_centro is 'Opción importe fijo por centro';
comment on column SIAP2.ss_transferencias.tra_porcentaje is 'Porcentaje de asignación';
comment on column SIAP2.ss_transferencias.tra_remanente is 'Opción remanente';
comment on column SIAP2.ss_transferencias.tra_ult_mod is 'Última fecha de modificación';
comment on column SIAP2.ss_transferencias.tra_ult_usuario is 'Último usuario modificador';
comment on column SIAP2.ss_transferencias.tra_version is 'Última version del registro';

comment on column SIAP2.ss_transferencias_componente.tc_transferencia is 'Relación con Transferencia';

--TEXTO RANGOS_NO_ENCONTRADOS
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_RANGOS_NO_ENCONTRADOS', true, now(), 'ANP-AGENPORT', 'admin','No se encontraron registros de rangos de matrícula para un presupuesto del Subcomponente seleccionado', 0, 1);

--ADICION CAMPOS TABLA IMPUESTOS	
alter table siap2.ss_impuestos add imp_prcj_ret_pj_nac Decimal(20,2);
alter table siap2.ss_impuestos add imp_prcj_ret_pf_nac Decimal(20,2);
alter table siap2.ss_impuestos add imp_prcj_ret_pj_ext Decimal(20,2);
alter table siap2.ss_impuestos add imp_prcj_ret_pf_ext Decimal(20,2);

--TEXTO FUENTE_FINANCIAMIENTO_PRESUPUESTO_EXISTENTE
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_FUENTE_FINANCIAMIENTO_PRESUPUESTO_EXISTENTE', true, now(), 'ANP-AGENPORT', 'admin','La fuente de financiamiento seleccionada ya ha sido creada para el presupuesto actual', 0, 1);

-- ELIMNINACION DE CONSTRAINT UNIQUE PARA PRESUPUESTO Y FUENTE FINANCIAMIENTO
alter table siap2.ss_rel_pres_anio_financiamiento drop constraint rel_anio_financiamiento_uk;

--CREACION DE COLUMNA PARA CANTIDAD DE MATRICULAS POR CENTRO
alter table siap2.ss_rel_ges_pres_es_anio_fiscal add cantidad_matriculas integer;
alter table siap2.ss_rel_ges_pres_es_anio_fiscal_hist add cantidad_matriculas integer;
comment on column siap2.ss_rel_ges_pres_es_anio_fiscal.cantidad_matriculas is 'Cantidad de matriculas por centro educativo';

--TEXTO MATRICULAS
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.Matriculas', true, now(), 'ANP-AGENPORT', 'admin','Matrículas', 0, 1);

--TEXTO CORRESPONDE PROYECTO
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.CorrespondeProyecto', true, now(), 'ANP-AGENPORT', 'admin','Corresponde a proyecto', 0, 1);

--TEXTO SUBCOMPONENTE CONVENIO
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.SubcomponenteConvenio', true, now(), 'ANP-AGENPORT', 'admin','Subcomponente del convenio', 0, 1);

--TEXTO COMPONENTE CONVENIO
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.ComponenteConvenio', true, now(), 'ANP-AGENPORT', 'admin','Componente del convenio', 0, 1);

--TEXTO CATEGORIA GASTO CONVENIO
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.CategoriaGastoConvenio', true, now(), 'ANP-AGENPORT', 'admin','Categoría de gasto del convenio', 0, 1);

--COLUMNA CATEGORIA_GASTO_CONVENIO
alter table siap2.ss_ges_pres_es add ges_categoria_gasto_convenio integer;
alter table siap2.ss_ges_pres_es_hist add ges_categoria_gasto_convenio integer;
comment on column siap2.ss_ges_pres_es.ges_categoria_gasto_convenio is 'Categoria de gasto del convenio';


--6.17.0


--TEXTO ERROR ELIMINACION TECHO
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_ELIMINE_LOS_MOVIMIENTOS_DE_PRESUPUESTO_DE_TECHO', true, now(), 'ANP-AGENPORT', 'admin',
'No se puede eliminar, el registro seleccionado posee movimientos de presupuesto escolar creados', 0, 1);

--TEXTO DESEMBOLSO SIN TECHOS
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 
'ERR_DESEMBOLSO_SIN_TECHOS', true, now(), 'ANP-AGENPORT', 'admin',
'No se pueden generar desembolsos, no se encontró ningún Techo Presupuestal para el registro de Transferencia por Componente seleccionado', 0, 1);

--TEXTO DESEMBOLSOS GENERALES
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 
'labels.DesembolsosGenerales', true, now(), 'ANP-AGENPORT', 'admin',
'Desembolsos generales', 0, 1);

--TEXTO CIERRE_ANIO
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 
'labels.CierreAnio', true, now(), 'ANP-AGENPORT', 'admin',
'Desea cerrar los presupuestos para el año fiscal seleccionado', 0, 1);

--TEXTO CIERRE POA ANIO FISCAL
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 
'labels.AnioFiscalCerrado', true, now(), 'ANP-AGENPORT', 'admin',
'Los registros POA del año fiscal seleccionado, se han cerrado correctamente', 0, 1);

--TEXTO CIERRE_PRESUPUESTOS_ANIO
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 
'labels.CierrePresupuestosAnio', true, now(), 'ANP-AGENPORT', 'admin',
'Cierre de presupuestos por año fiscal', 0, 1);

--RELACIONES DE COMPROMISO_PRESTARIO
alter table siap2.ss_compromisos_prestario add com_contrato_oc bigint;
alter table siap2.ss_compromisos_prestario_hist add com_contrato_oc bigint;
alter table siap2.ss_compromisos_prestario add constraint compromisosprestario_contratooc_fk foreign key (com_contrato_oc) references siap2.ss_contrato_oc(con_id);

alter table siap2.ss_compromisos_prestario add com_fuente_recursos bigint;
alter table siap2.ss_compromisos_prestario_hist add com_fuente_recursos bigint;
alter table siap2.ss_compromisos_prestario add constraint compromisosprestario_fuenterecursos_fk foreign key (com_fuente_recursos) references siap2.ss_fuente_recursos(fue_id);

--TEXTO GENERAR TRANSFERENCIAS_COMPONENTE
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 
'labels.GenerarTransferenciasComponente', true, now(), 'ANP-AGENPORT', 'admin',
'Generar Transferencias por Componentes', 0, 1);

--TEXTO GENERAR TRANSFERENCIAS_COMPONENTE ADVERTENCIA
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 
'GENERACION_TRANSFERENCIAS_COMPONENTE_ADVERTENCIA_CONTINUAR', true, now(), 'ANP-AGENPORT', 'admin',
'Se generarán las transferencias por componente según fuentes de financiamiento, desea continuar?', 0, 1);

--COLUMNA DESCRIPCION PARA TRANSFERENCIAS
alter table siap2.ss_transferencias  add tra_descripcion character varying(250);
alter table siap2.ss_transferencias_hist  add tra_descripcion character varying(250);
comment on column siap2.ss_transferencias.tra_descripcion is 'Descripción de la transferencia';

--COLUMNA DESCRIPCION PARA TRANSFERENCIAS POR COMPONENTE
alter table siap2.ss_transferencias_componente  add tc_descripcion character varying(250);
alter table siap2.ss_transferencias_componente_hist  add tc_descripcion character varying(250);
comment on column siap2.ss_transferencias_componente.tc_descripcion is 'Descripción de la transferencia por componente';


--TEXTO ERROR ELIMINACION TECHO
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_ELIMINE_LOS_MOVIMIENTOS_DE_PRESUPUESTO_DE_TECHO', true, now(), 'ANP-AGENPORT', 'admin',
'No se puede eliminar, el registro seleccionado posee movimientos de presupuesto escolar creados', 0, 1);

--TEXTO DESEMBOLSO SIN TECHOS
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 
'ERR_DESEMBOLSO_SIN_TECHOS', true, now(), 'ANP-AGENPORT', 'admin',
'No se pueden generar desembolsos, no se encontró ningún Techo Presupuestal para el registro de Transferencia por Componente seleccionado', 0, 1);

--TEXTO DESEMBOLSOS GENERALES
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 
'labels.DesembolsosGenerales', true, now(), 'ANP-AGENPORT', 'admin',
'Desembolsos generales', 0, 1);

--TEXTO CIERRE_ANIO
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 
'labels.CierreAnio', true, now(), 'ANP-AGENPORT', 'admin',
'Desea cerrar los presupuestos para el año fiscal seleccionado', 0, 1);

--TEXTO CIERRE POA ANIO FISCAL
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 
'labels.AnioFiscalCerrado', true, now(), 'ANP-AGENPORT', 'admin',
'Los registros POA del año fiscal seleccionado, se han cerrado correctamente', 0, 1);

--TEXTO CIERRE_PRESUPUESTOS_ANIO
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 
'labels.CierrePresupuestosAnio', true, now(), 'ANP-AGENPORT', 'admin',
'Cierre de presupuestos por año fiscal', 0, 1);

--RELACIONES DE COMPROMISO_PRESTARIO
alter table siap2.ss_compromisos_prestario add com_contrato_oc bigint;
alter table siap2.ss_compromisos_prestario_hist add com_contrato_oc bigint;
alter table siap2.ss_compromisos_prestario add constraint compromisosprestario_contratooc_fk foreign key (com_contrato_oc) references siap2.ss_contrato_oc(con_id);

alter table siap2.ss_compromisos_prestario add com_fuente_recursos bigint;
alter table siap2.ss_compromisos_prestario_hist add com_fuente_recursos bigint;
alter table siap2.ss_compromisos_prestario add constraint compromisosprestario_fuenterecursos_fk foreign key (com_fuente_recursos) references siap2.ss_fuente_recursos(fue_id);

--TEXTO GENERAR TRANSFERENCIAS_COMPONENTE
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 
'labels.GenerarTransferenciasComponente', true, now(), 'ANP-AGENPORT', 'admin',
'Generar Transferencias por Componentes', 0, 1);

--TEXTO GENERAR TRANSFERENCIAS_COMPONENTE ADVERTENCIA
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 
'GENERACION_TRANSFERENCIAS_COMPONENTE_ADVERTENCIA_CONTINUAR', true, now(), 'ANP-AGENPORT', 'admin',
'Se generarán las transferencias por componente según fuentes de financiamiento, desea continuar?', 0, 1);

--COLUMNA DESCRIPCION PARA TRANSFERENCIAS
alter table siap2.ss_transferencias  add tra_descripcion character varying(250);
alter table siap2.ss_transferencias_hist  add tra_descripcion character varying(250);
comment on column siap2.ss_transferencias.tra_descripcion is 'Descripción de la transferencia';

--COLUMNA DESCRIPCION PARA TRANSFERENCIAS POR COMPONENTE
alter table siap2.ss_transferencias_componente  add tc_descripcion character varying(250);
alter table siap2.ss_transferencias_componente_hist  add tc_descripcion character varying(250);
comment on column siap2.ss_transferencias_componente.tc_descripcion is 'Descripción de la transferencia por componente';

--CONSTRAINT UNIQUE PARA CODIGO DE COMPONENTE
alter table siap2.ss_categoria_presupuesto_escolar add constraint codigo_unico unique (cpe_codigo);

--ACTUALIZCION DE MENSAJE DE ERROR
update siap2.ss_textos set tex_valor = 'No fue posible eliminar el registro seleccionado' where tex_codigo  = 'ERR_NO_POSIBLE_ELIMINAR';

--TEXTO CODIGO SIN ESPACIO
update siap2.ss_textos set tex_valor = 'Código' where tex_codigo  = 'labels.Codigo';

--TEXTO TIPO SIN ESPACIO
update siap2.ss_textos set tex_valor = 'Tipo' where tex_codigo  = 'labels.Tipo';

--ERROR OEG NO VALIDO
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 
'ERR_OEG_NO_INGRESADO_NO_VALIDO', true, now(), 'ANP-AGENPORT', 'admin',
'El objeto específico de gasto ingresado no es válido', 0, 1);

--TEXTO NOMBRE COMPLEMENTO
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 
'labels.NombreComplemento', true, now(), 'ANP-AGENPORT', 'admin',
'Nombre de complemento', 0, 1);

--COLUMNA NOMBRE_COMPLEMENTO
alter table siap2.ss_rel_ges_pres_es_anio_fiscal add nombre_complemento character varying(250);
alter table siap2.ss_rel_ges_pres_es_anio_fiscal_hist add nombre_complemento character varying(250);
comment on column siap2.ss_rel_ges_pres_es_anio_fiscal.nombre_complemento is 'Nombre de complemento del tipo de presupuesto';

--TEXTO NOMBRE COMPLEMENTO
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 
'ERR_NOMBRE_COMPLEMENTO_NO_INGRESADO', true, now(), 'ANP-AGENPORT', 'admin',
'No se ha ingresado un nombre de complemento', 0, 1);

--ERROR PRESUPUESTO BASE
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 
'ERR_BASE_PRESUPUESTO_ANIO_YA_CREADA', true, now(), 'ANP-AGENPORT', 'admin',
'Ya existe un presupuesto base para el año fiscal seleccionado', 0, 1);

--RELACION RELACION_ANIO_FISCAL <> CUENTA
alter table siap2.ss_rel_ges_pres_es_anio_fiscal add constraint relacionaniofiscal_subcuenta_fk foreign key(id_sub_cuenta) references siap2.ss_cuentas(cu_id);

--RELACION 
alter table siap2.ss_transferencias add tra_relacion_anio_financiamiento bigint;
alter table siap2.ss_transferencias_hist add tra_relacion_anio_financiamiento bigint;
alter table siap2.ss_transferencias add constraint transferencias_relacionaniofinanciamiento_fk foreign key(tra_relacion_anio_financiamiento) references siap2.ss_rel_pres_anio_financiamiento(id);
comment on column siap2.ss_transferencias.tra_relacion_anio_financiamiento is 'Relacion con fuente de financiamiento por presupuesto escolar y anio fiscal';

--ERROR RELACION_ANIO_FINANCIAMIENTO_NO_ENCONTRADA
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 
'ERR_RELACION_ANIO_FINANCIAMIENTO_NO_ENCONTRADA', true, now(), 'ANP-AGENPORT', 'admin',
'No se encontró registro de relación de fuente de financiamiento', 0, 1);

--RELACION DE TECHO_PRESUPUESTAL <> FUENTE_FINANCIAMIENTO
alter table siap2.ss_tope_presupestal add tp_fuente_financiamiento bigint;
alter table siap2.ss_tope_presupestal_hist add tp_fuente_financiamiento bigint;
alter table siap2.ss_tope_presupestal add constraint techopresupuestal_fuentefinanciamiento_fk foreign key (tp_fuente_financiamiento) references siap2.ss_fuente_financiamiento(fue_id);
comment on column siap2.ss_tope_presupestal.tp_fuente_financiamiento is 'Relación con fuente de financiamiento';

--RELACION DE TECHO_PRESUPUESTAL <> FUENTE_RECURSOS
alter table siap2.ss_tope_presupestal add tp_fuente_recursos bigint;
alter table siap2.ss_tope_presupestal_hist add tp_fuente_recursos bigint;
alter table siap2.ss_tope_presupestal add constraint techopresupuestal_fuenterecursos_fk foreign key (tp_fuente_recursos) references siap2.ss_fuente_recursos(fue_id);
comment on column siap2.ss_tope_presupestal.tp_fuente_recursos is 'Relación con fuente de recursos';


--ELIMINACION DE COLUMNA RELACION_FINANCIAMIENTO
alter table siap2.ss_transferencias drop column tra_relacion_anio_financiamiento;
alter table siap2.ss_transferencias_hist drop column tra_relacion_anio_financiamiento;

--RELACION 
alter table siap2.ss_transferencias add tra_relacion_componente_anio_fiscal bigint;
alter table siap2.ss_transferencias_hist add tra_relacion_componente_anio_fiscal bigint;
alter table siap2.ss_transferencias add constraint transferencias_relacionaniofinanciamiento_fk foreign key(tra_relacion_componente_anio_fiscal) references siap2.ss_rel_ges_pres_es_anio_fiscal (id);
comment on column siap2.ss_transferencias.tra_relacion_componente_anio_fiscal is 'Relacion con presupuesto escolar y anio fiscal';

--ERROR RELACION_PRESUPUESTO_ANIO_FISCAL_NO_ENCONTRADA
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 
'ERR_RELACION_PRESUPUESTO_ANIO_FISCAL_NO_ENCONTRADA', true, now(), 'ANP-AGENPORT', 'admin',
'No se encontró registro de presupuesto por año fiscal', 0, 1);

--ERROR TIPO_CLASIFICACION_PRESUPUESTO_NO_SELECCIONADO
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 
'ERR_TIPO_CLASIFICACION_PRESUPUESTO_NO_SELECCIONADO', true, now(), 'ANP-AGENPORT', 'admin',
'No se seleccionó un tipo de clasificación de presupuesto', 0, 1);

--TEXTO FUENTES_FINANCIAMIENTO_POR_PRESUPUESTO
INSERT INTO siap2.ss_textos
(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 
'labels.FuentesFinanciamientoPorPresupuesto', true, now(), 'ANP-AGENPORT', 'admin',
'Fuentes de financiamiento por presupuesto', 0, 1);


--6.21.0

--TEXTO DE AYUDA PARA POAS
INSERT INTO siap2.ss_texto_ayuda (tex_id, tex_codigo, tex_habilitado, tex_nombre, tex_ult_mod, tex_ult_usuario, tex_version, tex_idioma_id) 
VALUES(1059, 'ayuda.CrearEditarProgramaPresupuestario', true, '', '2019-10-03 17:04:29.754', 'admin', 2, 1);


--TEXTO UNIDAD OPERATIVA
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.UnidadOperativa', true, now(), 'SCRIPT', 'admin','Unidad operativa', 0, 1);

--TEXTO MENU UNIDADES OPERATIVAS
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.menu.unidades_operativas', true, now(), 'SCRIPT', 'admin','Unidades Operativas', 0, 1);

--TEXTO DE CONSULTA DE UNDIDADES OPERATIVAS
INSERT INTO siap2.ss_textos (tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id) 
VALUES(NEXTVAL('siap2.seq_textos'), 'consultaPOA', true, now(), 'SCRIPT', 'admin', 'Planes Operativos Anuales', 0, 1);

INSERT INTO siap2.ss_textos (tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id) 
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.ConsultaPOA', true, now(), 'SCRIPT', 'admin', 'Planes Operativos Anuales', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.menu.poa', true, now(), 'SCRIPT', 'admin','POA', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.menu.seguimientopoa', true, now(), 'SCRIPT', 'admin','Seguimiento de POA', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.menu.programaciontrimestral', true, now(), 'SCRIPT', 'admin','Programación Trimestral', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.habilitacionProgramacionTrimestral', true, now(), 'SCRIPT', 'admin','Habilitación de programación trimestral', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.unidad_tecnica_especifica', true, now(), 'SCRIPT', 'admin','Unidad Técnica específica', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.EditarPOA', true, now(), 'SCRIPT', 'admin','Plan Operativo Anual', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.rangoPrimerTrimestre', true, now(), 'SCRIPT', 'admin','Rango Primer Trimestre', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.rangoSegundoTrimestre', true, now(), 'SCRIPT', 'admin','Rango Segundo Trimestre', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.rangoTercerTrimestre', true, now(), 'SCRIPT', 'admin','Rango Tercer Trimestre', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.rangoCuartoTrimestre', true, now(), 'SCRIPT', 'admin','Rango Cuarto Trimestre', 0, 1);


INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.gestionPOA', true, now(), 'SCRIPT', 'admin','Gestión de Planes Operativos Anuales', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.verPlanOperativoAnual', true, now(), 'SCRIPT', 'admin','Ver Plan Operativo Anual', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.verMetaPlanOperativoAnual', true, now(), 'SCRIPT', 'admin','Ver Meta de Plan Operativo Anual', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.evaluarPlanOperativoAnual', true, now(), 'SCRIPT', 'admin','Evaluar Plan Operativo Anual', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'crearEditarPOA', true, now(), 'SCRIPT', 'admin','Crear Editar Plan Operativo Anual', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.MetasPOA', true, now(), 'SCRIPT', 'admin','Metas del Plan Operativo Anual(POA)', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.MetaPOA', true, now(), 'SCRIPT', 'admin','Meta del Plan Operativo Anual(POA)', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.NuevaMetaPOA', true, now(), 'SCRIPT', 'admin','Nueva Meta del Plan Operativo Anual', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.EditarMetaPOA', true, now(), 'SCRIPT', 'admin','Editar Tarea de POA', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.MetaAnual', true, now(), 'SCRIPT', 'admin','Meta Anual', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.Indicador', true, now(), 'SCRIPT', 'admin','Indicador', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.VinculacionUnidadOperativo', true, now(), 'SCRIPT', 'admin','Vinculación con una unidad operativa', 0, 1);


INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.ProgramacionTrimestralMetalAnual', true, now(), 'SCRIPT', 'admin','Programación trimestral', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.ProgramacionTrimestralMetalAnualValorReal', true, now(), 'SCRIPT', 'admin','Programación trimestral-Valor Real', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.PorcentajeAvancePrimerTrimestre', true, now(), 'SCRIPT', 'admin','Porcentaje de avance-Primer Trimestre', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.PorcentajeAvanceSegundoTrimestre', true, now(), 'SCRIPT', 'admin','Porcentaje de avance-Segundo Trimestre', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.PorcentajeAvanceTercerTrimestre', true, now(), 'SCRIPT', 'admin','Porcentaje de avance-Tercer Trimestre', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.PorcentajeAvanceCuartoTrimestre', true, now(), 'SCRIPT', 'admin','Porcentaje de avance-Cuarto Trimestre', 0, 1);


INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.MedioVerificacion', true, now(), 'SCRIPT', 'admin','Medio de verificación', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.ValorInicial', true, now(), 'SCRIPT', 'admin','Valor Inicial', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.ValorReal', true, now(), 'SCRIPT', 'admin','Valor Real', 0, 1);


INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.MedioVerificacionIndicador', true, now(), 'SCRIPT', 'admin','Medio de verificación del indicador', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.ActividadesMetaActual', true, now(), 'SCRIPT', 'admin','Actividades de la meta anual', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.NuevaActividad', true, now(), 'SCRIPT', 'admin','Nueva Actividad', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.PeriodoEjecusionDesde', true, now(), 'SCRIPT', 'admin','Período de ejecución Desde', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.PeriodoEjecusionHasta', true, now(), 'SCRIPT', 'admin','Período de ejecución Hasta', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.FuncionarioResponsableMeta', true, now(), 'SCRIPT', 'admin','Funcionario responsable', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.UnidadTecnicaResponsable', true, now(), 'SCRIPT', 'admin','Unidad Técnica Responsable', 0, 1);
 
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.ActividadMetaActual', true, now(), 'SCRIPT', 'admin','Actividad de Meta Actual', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.PrimerTrimestre', true, now(), 'SCRIPT', 'admin','Primer Trimestre', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.SegundoTrimestre', true, now(), 'SCRIPT', 'admin','Segundo Trimestre', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.TercerTrimestre', true, now(), 'SCRIPT', 'admin','Tercer Trimestre', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.CuartoTrimestre', true, now(), 'SCRIPT', 'admin','Cuarto Trimestre', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.FormaMedicion', true, now(), 'SCRIPT', 'admin','Forma de medición', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.Evaluar', true, now(), 'SCRIPT', 'admin','Evaluar', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id) 
VALUES(NEXTVAL('siap2.seq_textos'), 'TipoNotificacion.NUEVO_POA_PARA_EVALUAR', true, now(), 'SCRIPT', 'admin', 'Nuevo POA para evaluar de [unidadTecnica]', 2, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id) 
VALUES(NEXTVAL('siap2.seq_textos'), 'TipoNotificacion.POA_FUE_APROBADO', true, now(), 'SCRIPT', 'admin', 'POA fue aprobado', 2, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id) 
VALUES(NEXTVAL('siap2.seq_textos'), 'TipoNotificacion.POA_FUE_RECHAZADO', true, now(), 'SCRIPT', 'admin', 'POA fue rechazado', 2, 1);


INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.seguimientoPOA', true, now(), 'SCRIPT', 'admin','Seguimiento de Plan Operativo Anual', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.seguimientoPOAs', true, now(), 'SCRIPT', 'admin','Seguimiento de Planes Operativos Anuales', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.ActualizarEstado', true, now(), 'SCRIPT', 'admin','Actualizar Estado', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.POAActualizarEstado', true, now(), 'SCRIPT', 'admin','PLan Operativo Anual-Actualizar Estado', 0, 1);


INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.gestionProgramacionTrimestral', true, now(), 'SCRIPT', 'admin',' Gestión de Programación Trimestral', 0, 1);


--Columna para saber si la unidad es operativa  o no
alter table siap2.ss_unidad_tecnica add uni_operativa boolean default false;
alter table siap2.ss_unidad_tecnica_hist add uni_operativa boolean default false;

--PERMISO PARA VISUALIZACIÓN DE MENÚ DE UNIDADES OPERATIVAS
INSERT INTO siap2.ss_operacion(ope_id, ope_codigo, ope_descripcion, ope_nombre, ope_origen, ope_tipocampo, ope_user_code, ope_version, ope_vigente, ope_categoria_id)
VALUES(NEXTVAL('siap2.seq_operacion'), 'MENU_UNIDADES_OPERATIVAS', 'Operación que permite visualizar el menú de las unidades operativas.', 'Unidades Operativas.', 'SCRIPT', 0, 0, NULL, true, NULL);

INSERT INTO siap2.ss_operacion(ope_id, ope_codigo, ope_descripcion, ope_nombre, ope_origen, ope_tipocampo, ope_user_code, ope_version, ope_vigente, ope_categoria_id)
VALUES(NEXTVAL('siap2.seq_operacion'), 'CONSULTA_PLAN_OPERATIVO_ANUAL', 'Operación que permite consultar los Planes Operativos Anuales.', 'Consulta de POAs.', 'SCRIPT', 0, 0, NULL, true, NULL);

INSERT INTO siap2.ss_operacion(ope_id, ope_codigo, ope_descripcion, ope_nombre, ope_origen, ope_tipocampo, ope_user_code, ope_version, ope_vigente, ope_categoria_id)
VALUES(NEXTVAL('siap2.seq_operacion'), 'CREAR_EDITAR_PLAN_OPERATIVO_ANUAL', 'Operación que permite crear y editar Planes Operativos Anuales.', 'Crear - Editar POAs.', 'SCRIPT', 0, 0, NULL, true, NULL);

INSERT INTO siap2.ss_operacion(ope_id, ope_codigo, ope_descripcion, ope_nombre, ope_origen, ope_tipocampo, ope_user_code, ope_version, ope_vigente, ope_categoria_id)
VALUES(NEXTVAL('siap2.seq_operacion'), 'EVALUAR_PLAN_OPERATIVO_ANUAL', 'Operación que permite evaluar los Planes Operativos Anuales.', 'Crear - Editar POAs.', 'SCRIPT', 0, 0, NULL, true, NULL);

INSERT INTO siap2.ss_operacion(ope_id, ope_codigo, ope_descripcion, ope_nombre, ope_origen, ope_tipocampo, ope_user_code, ope_version, ope_vigente, ope_categoria_id)
VALUES(NEXTVAL('siap2.seq_operacion'), 'ENVIAR_PLAN_OPERATIVO_ANUAL', 'Operación que permite Enviar los Planes Operativos Anuales.', 'Crear - Editar POAs.', 'SCRIPT', 0, 0, NULL, true, NULL);

INSERT INTO siap2.ss_operacion(ope_id, ope_codigo, ope_descripcion, ope_nombre, ope_origen, ope_tipocampo, ope_user_code, ope_version, ope_vigente, ope_categoria_id)
VALUES(NEXTVAL('siap2.seq_operacion'), 'ELIMINAR_PLAN_OPERATIVO_ANUAL', 'Operación que permite eliminar POAs.', 'Eliminar Planes Operativos Anuales.', 'SCRIPT', 0, 0, NULL, true, NULL);

INSERT INTO siap2.ss_operacion(ope_id, ope_codigo, ope_descripcion, ope_nombre, ope_origen, ope_tipocampo, ope_user_code, ope_version, ope_vigente, ope_categoria_id)
VALUES(NEXTVAL('siap2.seq_operacion'), 'VER_PLAN_OPERATIVO_ANUAL', 'Operación que permite Ver el historicode  POAs.', 'Ver Plan Operativo Anual.', 'SCRIPT', 0, 0, NULL, true, NULL);

INSERT INTO siap2.ss_operacion(ope_id, ope_codigo, ope_descripcion, ope_nombre, ope_origen, ope_tipocampo, ope_user_code, ope_version, ope_vigente, ope_categoria_id)
VALUES(NEXTVAL('siap2.seq_operacion'), 'CONSULTA_SEGUIMIENTO_PLAN_OPERATIVO_ANUAL', 'Operación que permite dar seguimiento a los POA.', 'Consulta de Seguimiento de Plan Operativo Anual.', 'SCRIPT', 0, 0, NULL, true, NULL);

INSERT INTO siap2.ss_operacion(ope_id, ope_codigo, ope_descripcion, ope_nombre, ope_origen, ope_tipocampo, ope_user_code, ope_version, ope_vigente, ope_categoria_id)
VALUES(NEXTVAL('siap2.seq_operacion'), 'SEGUIMIENTO_PLAN_OPERATIVO_ANUAL', 'Operación que permite dar seguimiento a los POA.', 'Seguimiento de Plan Operativo Anual.', 'SCRIPT', 0, 0, NULL, true, NULL);

INSERT INTO siap2.ss_operacion(ope_id, ope_codigo, ope_descripcion, ope_nombre, ope_origen, ope_tipocampo, ope_user_code, ope_version, ope_vigente, ope_categoria_id)
VALUES(NEXTVAL('siap2.seq_operacion'), 'REGISTRAR_POA_APOYO', 'Operación que permite registrar un apoyo en el POA.', 'Registrar Apoyo para el Plan Operativo Anual.', 'SCRIPT', 0, 0, NULL, true, NULL);

INSERT INTO siap2.ss_operacion(ope_id, ope_codigo, ope_descripcion, ope_nombre, ope_origen, ope_tipocampo, ope_user_code, ope_version, ope_vigente, ope_categoria_id)
VALUES(NEXTVAL('siap2.seq_operacion'), 'PROGRAMACION_TRIMESTRAL_PLAN_OPERATIVO_ANUAL', 'Operación que permite registrar la programación trimestral de años fiscales para cada POA', 'Registrar programación trimestral del POA.', 'SCRIPT', 0, 0, NULL, true, NULL);




--MENSAJES DE ERROR ERR_USUARIO_NO_TIENE_PERMISO_REGISTRAR_APOYO_EN_UNIDAD_TECNICA_POA
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_DEBE_AGREGAR_ALMENOS_UNA_TAREA_POA', true, now(), 'ANP-AGENPORT', 'admin','Debe agregar almenos una tarea al Plan Operativo Anual', 0, 1);

--MENSAJES DE ERROR ERR_USUARIO_NO_TIENE_PERMISO_REGISTRAR_APOYO_EN_UNIDAD_TECNICA_POA
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_DEBE_AGREGAR_ALMENOS_UNA_ACTIVIDAD_A_TAREA_POA', true, now(), 'ANP-AGENPORT', 'admin','Debe agregar almenos una actividad a la tarea del Plan Operativo Anual', 0, 1);


--MENSAJES DE ERROR ERR_USUARIO_NO_TIENE_PERMISO_REGISTRAR_APOYO_EN_UNIDAD_TECNICA_POA
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_USUARIO_NO_TIENE_PERMISO_REGISTRAR_APOYO_EN_UNIDAD_TECNICA_POA', true, now(), 'ANP-AGENPORT', 'admin','El usuario no tiene permiso en la unidad técnica del POA para guardar el seguimiento', 0, 1);

--MENSAJES DE ERROR EN VALIDACIÓN DE INGRESO DE POA
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_UNIDAD_TECNICA_VACIA', true, now(), 'ANP-AGENPORT', 'admin','No se seleccionó una unidad técnica', 0, 1);

--ERROR TIPO_CLASIFICACION_PRESUPUESTO_NO_SELECCIONADO
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_PROGRAMA_INSTITUCIONAL_VACIO', true, now(), 'ANP-AGENPORT', 'admin','No se seleccionó un programa institucional', 0, 1);

--ERROR NO SE GUARDARAN CAMBIOS DE LA TAREA
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.Si_no_guarda_el_poa_todos_los_cambios_a_las_tarea_se_perderan', true, now(), 'ANP-AGENPORT', 'admin','Si no guarda el Plan Operativo Anual, todos los cambios se perderán', 0, 1);

--ERROR ERR_META_ANUAL_VACIO
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_META_ANUAL_VACIO', true, now(), 'ANP-AGENPORT', 'admin','Debe completar la Meta Anual', 0, 1);

--ERROR ERR_INDICADOR_VACIO
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_INDICADOR_VACIO', true, now(), 'ANP-AGENPORT', 'admin','No se seleccionó un Indicador', 0, 1);

--ERROR ERR_MEDIO_VERIFICACION_VACIO
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_MEDIO_VERIFICACION_VACIO', true, now(), 'ANP-AGENPORT', 'admin','Debe completar el Medio de verificación del indicador', 0, 1);

--ERROR ERR_PROGRAMA_TRIMESTRAL_VACIO
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_PROGRAMA_TRIMESTRAL_VACIO', true, now(), 'ANP-AGENPORT', 'admin','Debe completar al menos un trimestre del programa trimestral de la meta', 0, 1);

--ERROR ERR_META_ANUAL_LARGO_500
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_META_ANUAL_LARGO_500', true, now(), 'ANP-AGENPORT', 'admin','La meta anual no debe superar los 500 caracteres.', 0, 1);

--ERROR ERR_MEDIO_VERIFICACION_LARGO_500
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_MEDIO_VERIFICACION_LARGO_500', true, now(), 'ANP-AGENPORT', 'admin','El medio de verificación no debe superar los 500 caracteres.', 0, 1);

--ERROR ERR_METAS_DATOS_INCOMPLETOS
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_METAS_DATOS_INCOMPLETOS', true, now(), 'ANP-AGENPORT', 'admin','Meta Anual con datos incompletos.', 0, 1);


--ERROR ERR_NOMBRE_VACIO
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_NOMBRE_VACIO', true, now(), 'ANP-AGENPORT', 'admin','Debe completar el nombre.', 0, 1);

--ERROR ERR_NOMBRE_LARGO_500
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_NOMBRE_LARGO_500', true, now(), 'ANP-AGENPORT', 'admin','El nombre no puede superar los 500 caracteres.', 0, 1);

--ERROR ERR_PERIOD0_EJECUCION_DESDE_VACIO
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_PERIOD0_EJECUCION_DESDE_VACIO', true, now(), 'ANP-AGENPORT', 'admin','Debe completar el periodo de ejecución desde.', 0, 1);

--ERROR ERR_PERIOD0_EJECUCION_HASTA_VACIO
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_PERIOD0_EJECUCION_HASTA_VACIO', true, now(), 'ANP-AGENPORT', 'admin','Debe completar el periodo de ejecución hasta.', 0, 1);

--ERROR ERR_FORMA_MEDICION_VACIO
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_FORMA_MEDICION_VACIO', true, now(), 'ANP-AGENPORT', 'admin','Debe completar la forma de medición', 0, 1);

--ERROR ERR_PERIOD0_EJECUCION_FECHA_MAYOR_MENOR
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_PERIOD0_EJECUCION_FECHA_MAYOR_MENOR', true, now(), 'ANP-AGENPORT', 'admin','La fecha desde el periodo de ejecución no debe ser mayor que la fecha hasta.', 0, 1);

--ERROR ERR_FUNCIONARIO_RESPONSABLE_VACIO
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_FUNCIONARIO_RESPONSABLE_VACIO', true, now(), 'ANP-AGENPORT', 'admin','Debe completar el funcionario responsable.', 0, 1);

--ERROR ERR_FUNCIONARIO_RESPONSABLE_LARGO_50
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_FUNCIONARIO_RESPONSABLE_LARGO_50', true, now(), 'ANP-AGENPORT', 'admin','El funcionario responsable no debe superar los 50 caracteres.', 0, 1);

--ERROR ERR_ACTIVIDADES_DATOS_INCOMPLETOS
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_ACTIVIDADES_DATOS_INCOMPLETOS', true, now(), 'ANP-AGENPORT', 'admin','Actividad con datos incompletos.', 0, 1);


--ERROR ERR_FECHA_DESDE_FUERA_ANIO_FISCAL
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_FECHA_DESDE_FUERA_ANIO_FISCAL', true, now(), 'ANP-AGENPORT', 'admin','La fecha desde del periodo de ejecución está fuera del año fiscal ingresado.', 0, 1);

--ERROR ERR_FECHA_HASTA_FUERA_ANIO_FISCA
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_FECHA_HASTA_FUERA_ANIO_FISCAL', true, now(), 'ANP-AGENPORT', 'admin','La fecha hasta del periodo de ejecución está fuera del año fiscal ingresado.', 0, 1);


--ERROR ERR_PROGRAMA_TRIMESTRAL_VACIO
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_PROGRAMA_TRIMESTRAL_VACIO', true, now(), 'ANP-AGENPORT', 'admin','Debe completar al menos un trimestre del programa trimestral real de la meta', 0, 1);


--ERROR ERR_PORCENTAJE_PRIMER_TRIMESTRE_INVALIDO
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_PORCENTAJE_PRIMER_TRIMESTRE_INVALIDO', true, now(), 'ANP-AGENPORT', 'admin','El porcentaje del primer trimestre debe ser un valor entre 0 y 100', 0, 1);

--ERROR ERR_PORCENTAJE_SEGUNDO_TRIMESTRE_INVALIDO
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_PORCENTAJE_SEGUNDO_TRIMESTRE_INVALIDO', true, now(), 'ANP-AGENPORT', 'admin','El porcentaje del segundo trimestre debe ser un valor entre 0 y 100', 0, 1);


--ERROR ERR_PORCENTAJE_TERCER_TRIMESTRE_INVALIDO
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_PORCENTAJE_TERCER_TRIMESTRE_INVALIDO', true, now(), 'ANP-AGENPORT', 'admin','El porcentaje del tercer trimestre debe ser un valor entre 0 y 100', 0, 1);


--ERROR ERR_PORCENTAJE_CUARTO_TRIMESTRE_INVALIDO
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_PORCENTAJE_CUARTO_TRIMESTRE_INVALIDO', true, now(), 'ANP-AGENPORT', 'admin','El porcentaje del cuarto trimestre debe ser un valor entre 0 y 100', 0, 1);

--ERROR ERR_PRIMER_TRIMESTRE_FECHA_DESDE_MAYOR_FECHA_HASTA
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_PRIMER_TRIMESTRE_FECHA_DESDE_MAYOR_FECHA_HASTA', true, now(), 'ANP-AGENPORT', 'admin','Primer Trimestre - Fecha desde es mayor que fecha hasta', 0, 1);

--ERROR ERR_SEGUNDO_TRIMESTRE_FECHA_DESDE_MAYOR_FECHA_HASTA
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_SEGUNDO_TRIMESTRE_FECHA_DESDE_MAYOR_FECHA_HASTA', true, now(), 'ANP-AGENPORT', 'admin','Segundo Trimestre - Fecha desde es mayor que fecha hasta', 0, 1);

--ERROR ERR_TERCER_TRIMESTRE_FECHA_DESDE_MAYOR_FECHA_HASTA
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_TERCER_TRIMESTRE_FECHA_DESDE_MAYOR_FECHA_HASTA', true, now(), 'ANP-AGENPORT', 'admin','Tercer Trimestre - Fecha desde es mayor que fecha hasta', 0, 1);

--ERROR ERR_CUARTO_TRIMESTRE_FECHA_DESDE_MAYOR_FECHA_HASTA
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_CUARTO_TRIMESTRE_FECHA_DESDE_MAYOR_FECHA_HASTA', true, now(), 'ANP-AGENPORT', 'admin','Cuarto Trimestre - Fecha desde es mayor que fecha hasta', 0, 1);



--ERROR ERR_PRIMER_TRIMESTRE_FECHA_DESDE_BLIGATORIO
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_PRIMER_TRIMESTRE_FECHA_DESDE_BLIGATORIO', true, now(), 'ANP-AGENPORT', 'admin','Primer Trimestre - Fecha desde obligatorio.', 0, 1);

--ERROR ERR_SEGUNDO_TRIMESTRE_FECHA_DESDE_BLIGATORIO
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_SEGUNDO_TRIMESTRE_FECHA_DESDE_BLIGATORIO', true, now(), 'ANP-AGENPORT', 'admin','Segundo Trimestre - Fecha desde obligatorio.', 0, 1);

--ERROR ERR_TERCER_TRIMESTRE_FECHA_DESDE_BLIGATORIO
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_TERCER_TRIMESTRE_FECHA_DESDE_BLIGATORIO', true, now(), 'ANP-AGENPORT', 'admin','Tercer Trimestre - Fecha desde obligatorio.', 0, 1);

--ERROR ERR_CUARTO_TRIMESTRE_FECHA_DESDE_BLIGATORIO
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_CUARTO_TRIMESTRE_FECHA_DESDE_BLIGATORIO', true, now(), 'ANP-AGENPORT', 'admin','Cuarto Trimestre - Fecha desde obligatorio.', 0, 1);


--ERROR ERR_PRIMER_TRIMESTRE_FECHA_HASTA_BLIGATORIO
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_PRIMER_TRIMESTRE_FECHA_HASTA_BLIGATORIO', true, now(), 'ANP-AGENPORT', 'admin','Primer Trimestre - Fecha hasta obligatorio.', 0, 1);

--ERROR ERR_SEGUNDO_TRIMESTRE_FECHA_DESDE_BLIGATORIO
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_SEGUNDO_TRIMESTRE_FECHA_HASTA_BLIGATORIO', true, now(), 'ANP-AGENPORT', 'admin','Segundo Trimestre - Fecha hasta obligatorio.', 0, 1);

--ERROR ERR_TERCER_TRIMESTRE_FECHA_HASTA_BLIGATORIO
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_TERCER_TRIMESTRE_FECHA_HASTA_BLIGATORIO', true, now(), 'ANP-AGENPORT', 'admin','Tercer Trimestre - Fecha hasta obligatorio.', 0, 1);

--ERROR ERR_CUARTO_TRIMESTRE_FECHA_HASTA_BLIGATORIO
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_CUARTO_TRIMESTRE_FECHA_HASTA_BLIGATORIO', true, now(), 'ANP-AGENPORT', 'admin','Cuarto Trimestre - Fecha hasta obligatorio.', 0, 1);

--ERROR ERR_PRIMER_TRIMESTRE_FECHA_FUERA_RANGO
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_PRIMER_TRIMESTRE_FECHA_FUERA_RANGO', true, now(), 'ANP-AGENPORT', 'admin','Primer Trimestre - Fecha desde y Fecha Hasta diferente del año seleccionado.', 0, 1);

--ERROR ERR_SEGUNDO_TRIMESTRE_FECHA_FUERA_RANGO
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_SEGUNDO_TRIMESTRE_FECHA_FUERA_RANGO', true, now(), 'ANP-AGENPORT', 'admin','Segundo Trimestre - Fecha desde-Fecha Hasta diferente del año seleccionado.', 0, 1);

--ERROR ERR_TERCER_TRIMESTRE_FECHA_FUERA_RANGO
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_TERCER_TRIMESTRE_FECHA_FUERA_RANGO', true, now(), 'ANP-AGENPORT', 'admin','Tercer Trimestre - Fecha desde-Fecha Hasta diferente del año seleccionado.', 0, 1);

--ERROR ERR_CUARTO_TRIMESTRE_FECHA_FUERA_RANGO
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_CUARTO_TRIMESTRE_FECHA_FUERA_RANGO', true, now(), 'ANP-AGENPORT', 'admin','Cuarto Trimestre - Fecha desde-Fecha Hasta diferente del año seleccionado.', 0, 1);


--ERROR ERR_PRIMER_TRIMESTRE_FECHA_DESDE_FUERA_RANGO
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_PRIMER_TRIMESTRE_FECHA_DESDE_FUERA_RANGO', true, now(), 'ANP-AGENPORT', 'admin','Primer Trimestre - Fecha desde diferente del año seleccionado.', 0, 1);

--ERROR ERR_SEGUNDO_TRIMESTRE_FECHA_DESDE_FUERA_RANGO
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_SEGUNDO_TRIMESTRE_FECHA_DESDE_FUERA_RANGO', true, now(), 'ANP-AGENPORT', 'admin','Segundo Trimestre - Fecha desde diferente del año seleccionado.', 0, 1);

--ERROR ERR_TERCER_TRIMESTRE_FECHA_DESDE_FUERA_RANGO
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_TERCER_TRIMESTRE_FECHA_DESDE_FUERA_RANGO', true, now(), 'ANP-AGENPORT', 'admin','Tercer Trimestre - Fecha desde diferente del año seleccionado.', 0, 1);

--ERROR ERR_CUARTO_TRIMESTRE_FECHA_DESDE_FUERA_RANGO
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_CUARTO_TRIMESTRE_FECHA_DESDE_FUERA_RANGO', true, now(), 'ANP-AGENPORT', 'admin','Cuarto Trimestre - Fecha desde diferente del año seleccionado.', 0, 1);


--ERROR ERR_PRIMER_TRIMESTRE_FECHA_HASTA_FUERA_RANGO
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_PRIMER_TRIMESTRE_FECHA_HASTA_FUERA_RANGO', true, now(), 'ANP-AGENPORT', 'admin','Primer Trimestre - Fecha hasta diferente del año seleccionado.', 0, 1);

--ERROR ERR_SEGUNDO_TRIMESTRE_FECHA_HASTA_FUERA_RANGO
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_SEGUNDO_TRIMESTRE_FECHA_HASTA_FUERA_RANGO', true, now(), 'ANP-AGENPORT', 'admin','Segundo Trimestre - Fecha hasta diferente del año seleccionado.', 0, 1);

--ERROR ERR_TERCER_TRIMESTRE_FECHA_HASTA_FUERA_RANGO
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_TERCER_TRIMESTRE_FECHA_HASTA_FUERA_RANGO', true, now(), 'ANP-AGENPORT', 'admin','Tercer Trimestre - Fecha hasta diferente del año seleccionado.', 0, 1);

--ERROR ERR_CUARTO_TRIMESTRE_FECHA_HASTA_FUERA_RANGO
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_CUARTO_TRIMESTRE_FECHA_HASTA_FUERA_RANGO', true, now(), 'ANP-AGENPORT', 'admin','Cuarto Trimestre - Fecha hasta diferente del año seleccionado.', 0, 1);


--DLL TABLAS PLAN OPERATIVO ANUAL(POA)
CREATE SEQUENCE siap2.poa_seq START WITH 1 increment by 1 minvalue 1 maxvalue 9999999999999;

CREATE TABLE siap2.ss_poa (
	poa_id int8 NOT NULL,
	poa_unidad_tecnica int8 NULL,
	poa_prog_institucional int8 NULL,
	poa_anio int8 NULL,
	poa_estado varchar NULL,
	poa_ult_mod timestamp NULL,
        poa_fecha_ult_mod_seguimiento timestamp NULL,
	poa_ult_usuario varchar NULL,
	poa_version int8 NULL,
	CONSTRAINT poa_pk PRIMARY KEY (poa_id),
	CONSTRAINT anio_fk FOREIGN KEY (poa_anio) REFERENCES siap2.ss_anio_fiscal(ani_id),
	CONSTRAINT prog_oinstitucional_fk FOREIGN KEY (poa_prog_institucional) REFERENCES siap2.ss_programa(pro_id),
	CONSTRAINT unidad_tecnica_fk FOREIGN KEY (poa_unidad_tecnica) REFERENCES siap2.ss_unidad_tecnica(uni_id)
);

CREATE TABLE siap2.ss_poa_hist (
	poa_id int8 NOT NULL,
	poa_unidad_tecnica int8 NULL,
	poa_prog_institucional int8 NULL,
	poa_anio int8 NULL,
	poa_estado varchar NULL,
	poa_ult_mod timestamp NULL,
        poa_fecha_ult_mod_seguimiento timestamp NULL,
	poa_ult_usuario varchar NULL,
	poa_version int8 NULL,
	START_DATE TIMESTAMP,
	END_DATE TIMESTAMP
);

--DLL DETALLE DEL PLAN OPERATIVO ANUAL
CREATE SEQUENCE siap2.poa_metas_anuales_seq START WITH 1 increment by 1 minvalue 1 maxvalue 9999999999999;

CREATE TABLE siap2.ss_poa_metas_anuales (
	pma_id int8 NOT NULL,
	pma_meta_anual varchar(500) NULL,
	pma_indicador int8 NULL,
	pma_vinculado_up bool NULL DEFAULT false,
	pma_medio_verificacion_indicador varchar(500) NULL,
	pma_programa_t1 numeric(19) NULL,
	pma_programa_t2 numeric(19) NULL,
	pma_programa_t3 numeric(19) NULL,
	pma_programa_t4 numeric(19) NULL,
	pma_poa_fk int8 NULL,
	pma_tipo_medicion varchar(30) NULL,
	pma_programa_real_t1 numeric(19) NULL DEFAULT 0,
	pma_programa_real_t2 numeric(19) NULL DEFAULT 0,
	pma_programa_real_t3 numeric(19) NULL DEFAULT 0,
	pma_programa_real_t4 numeric(19) NULL DEFAULT 0,
	pma_alcance_limitaciones varchar(500) NULL,
	pma_medio_verificacion varchar(500) NULL,
	pma_fecha_ult_mod_seguimiento timestamp NULL,
	pma_fecha_ult_mod_seguimiento_t1 timestamp NULL,
	pma_fecha_ult_mod_seguimiento_t2 timestamp NULL,
	pma_fecha_ult_mod_seguimiento_t3 timestamp NULL,
	pma_fecha_ult_mod_seguimiento_t4 timestamp NULL,
        pma_ult_mod timestamp NULL,
	pma_ult_usuario varchar NULL,
	pma_version int8 NULL,
	CONSTRAINT poa_meta_anual_pk PRIMARY KEY (pma_id),
	CONSTRAINT indicadorl_fk FOREIGN KEY (pma_indicador) REFERENCES siap2.ss_indicador(ind_id),
	CONSTRAINT poa_fk FOREIGN KEY (pma_poa_fk) REFERENCES siap2.ss_poa(poa_id)
);

CREATE TABLE siap2.ss_poa_metas_anuales_hist (
	pma_id int8 NOT NULL,
	pma_meta_anual varchar(500) NULL,
	pma_indicador int8 NULL,
	pma_vinculado_up bool NULL DEFAULT false,
	pma_medio_verificacion_indicador varchar(500) NULL,
	pma_programa_t1 numeric(19) NULL,
	pma_programa_t2 numeric(19) NULL,
	pma_programa_t3 numeric(19) NULL,
	pma_programa_t4 numeric(19) NULL,
	pma_poa_fk int8 NULL,
	pma_tipo_medicion varchar(30) NULL,
	pma_programa_real_t1 numeric(19) NULL DEFAULT 0,
	pma_programa_real_t2 numeric(19) NULL DEFAULT 0,
	pma_programa_real_t3 numeric(19) NULL DEFAULT 0,
	pma_programa_real_t4 numeric(19) NULL DEFAULT 0,
	pma_alcance_limitaciones varchar(500) NULL,
	pma_medio_verificacion varchar(500) NULL,
	pma_fecha_ult_mod_seguimiento timestamp NULL,
	pma_fecha_ult_mod_seguimiento_t1 timestamp NULL,
	pma_fecha_ult_mod_seguimiento_t2 timestamp NULL,
	pma_fecha_ult_mod_seguimiento_t3 timestamp NULL,
	pma_fecha_ult_mod_seguimiento_t4 timestamp NULL,
        pma_ult_mod timestamp NULL,
	pma_ult_usuario varchar NULL,
	pma_version int8 NULL,
        start_date timestamp NULL,
	end_date timestamp NULL
);


--DLL ACTIVIDAD META DEL POA
CREATE SEQUENCE siap2.poa_actividades_metas_seq START WITH 1 increment by 1 minvalue 1 maxvalue 9999999999999;

CREATE TABLE siap2.ss_poa_actividades_metas (
	pam_id int8 NOT NULL,
	pam_nombre varchar(500) NULL,
	pam_funcionario_responsable varchar(50) NULL,
	pam_periodo_ejecucion_desde timestamp NULL,
	pam_periodo_ejecucion_hasta timestamp NULL,
	pam_unidad_tecnica_responsable int8 NULL,
	pam_meta_anual int8 NULL,
	pam_ult_mod timestamp NULL,
	pam_ult_usuario varchar NULL,
	pam_version int8 NULL,
	pam_porcentaje_avance_t1 numeric(19) NULL DEFAULT 0,
	pam_porcentaje_avance_t2 numeric(19) NULL DEFAULT 0,
	pam_porcentaje_avance_t3 numeric(19) NULL DEFAULT 0,
	pam_porcentaje_avance_t4 numeric(19) NULL DEFAULT 0,
	CONSTRAINT poa_actividad_meta_pk PRIMARY KEY (pam_id),
	CONSTRAINT poa_meta_fk FOREIGN KEY (pam_meta_anual) REFERENCES siap2.ss_poa_metas_anuales(pma_id),
	CONSTRAINT unidad_tecnica_res_fk FOREIGN KEY (pam_unidad_tecnica_responsable) REFERENCES siap2.ss_unidad_tecnica(uni_id)
);

CREATE TABLE siap2.ss_poa_actividades_metas_hist (
	pam_id int8 NOT NULL,
	pam_nombre varchar(500) NULL,
	pam_funcionario_responsable varchar(50) NULL,
	pam_periodo_ejecucion_desde timestamp NULL,
	pam_periodo_ejecucion_hasta timestamp NULL,
	pam_unidad_tecnica_responsable int8 NULL,
	pam_meta_anual int8 NULL,
	pam_ult_mod timestamp NULL,
	pam_ult_usuario varchar NULL,
	pam_version int8 NULL,
	pam_porcentaje_avance_t1 numeric(19) NULL DEFAULT 0,
	pam_porcentaje_avance_t2 numeric(19) NULL DEFAULT 0,
	pam_porcentaje_avance_t3 numeric(19) NULL DEFAULT 0,
	pam_porcentaje_avance_t4 numeric(19) NULL DEFAULT 0,
        start_date timestamp NULL,
	end_date timestamp NULL
);

--DLL RIESGO POA
CREATE SEQUENCE siap2.seq_poa_riesgos_poa START WITH 1 increment by 1 minvalue 1 maxvalue 9999999999999;

CREATE TABLE siap2.ss_poa_riesgos_poa (
	prp_id int8 NOT NULL,
	prp_acc_conting text NULL,
	prp_acc_mitigacion text NULL,
	prp_fecha_fijacion timestamp NULL,
	prp_responsable varchar NULL,
	prp_origen varchar NULL,
	prp_riesgo varchar NULL,
	prp_ult_mod timestamp NULL,
	prp_ult_usuario varchar NULL,
	prp_val_riesg varchar NULL,
	prp_linea_base int8 NULL,
	prp_linea_trabajo int8 NULL,
        prp_version int8 NULL,
	CONSTRAINT ss_poa_riesgos_poa_pk PRIMARY KEY (prp_id)
);

CREATE TABLE siap2.ss_poa_riesgos_poa_hist (
	prp_id int8 NOT NULL,
	prp_acc_conting text NULL,
	prp_acc_mitigacion text NULL,
	prp_fecha_fijacion timestamp NULL,
	prp_responsable varchar NULL,
	prp_origen varchar NULL,
	prp_riesgo varchar NULL,
	prp_ult_mod timestamp NULL,
	prp_ult_usuario varchar NULL,
	prp_val_riesg varchar NULL,
        prp_linea_base int8 NULL,
	prp_linea_trabajo int8 NULL,
	prp_version int8 NULL,
	START_DATE TIMESTAMP,
	END_DATE TIMESTAMP
);

--DLL RELACION POA-RIESGO

CREATE TABLE siap2.ss_rel_poa_riesgos (
	poa int8 NOT NULL,
	poa_riesgo int8 NOT NULL,
	CONSTRAINT ss_rel_poa_riesgos_pk PRIMARY KEY (poa,poa_riesgo)
);

CREATE TABLE siap2.ss_rel_poa_riesgos_hist (
        poa int8 NOT NULL,
	poa_riesgo int8 NOT NULL,
	START_DATE TIMESTAMP,
	END_DATE TIMESTAMP
);

--DLL RIESGO PERIODOS SEGUIMIENTO
CREATE SEQUENCE siap2.seq_poa_programacion_trimestral START WITH 1 increment by 1 minvalue 1 maxvalue 9999999999999;

CREATE TABLE siap2.ss_poa_programacion_trimestral (
	ppt_id int8 NOT NULL,
	ppt_anio int8 NULL,
	ppt_fecha_desde_t1 timestamp NULL,
	ppt_fecha_hasta_t1 timestamp NULL,
	ppt_fecha_desde_t2 timestamp NULL,
	ppt_fecha_hasta_t2 timestamp NULL,
	ppt_fecha_desde_t3 timestamp NULL,
	ppt_fecha_hasta_t3 timestamp NULL,
	ppt_fecha_desde_t4 timestamp NULL,
	ppt_fecha_hasta_t4 timestamp NULL,
	ppt_ult_mod timestamp NULL,
	ppt_ult_usuario varchar NULL,
	ppt_version int8 NULL,
	CONSTRAINT pps_pk PRIMARY KEY (ppt_id),
	CONSTRAINT anio_fk FOREIGN KEY (ppt_anio) REFERENCES siap2.ss_anio_fiscal(ani_id)
);

CREATE TABLE siap2.ss_poa_programacion_trimestral_hist (
	ppt_id int8 NOT NULL,
	ppt_anio int8 NULL,
	ppt_fecha_desde_t1 timestamp NULL,
	ppt_fecha_hasta_t1 timestamp NULL,
	ppt_fecha_desde_t2 timestamp NULL,
	ppt_fecha_hasta_t2 timestamp NULL,
	ppt_fecha_desde_t3 timestamp NULL,
	ppt_fecha_hasta_t3 timestamp NULL,
	ppt_fecha_desde_t4 timestamp NULL,
	ppt_fecha_hasta_t4 timestamp NULL,
	ppt_ult_mod timestamp NULL,
	ppt_ult_usuario varchar NULL,
	ppt_version int8 NULL,
	START_DATE TIMESTAMP,
	END_DATE TIMESTAMP
);

-- PROGRAMA-NOMBRE BUSUQEDA
alter table siap2.ss_programa add pro_nombre_busqueda character varying(250);
alter table siap2.ss_programa_hist add pro_nombre_busqueda character varying(250);
update siap2.ss_programa set pro_nombre_busqueda=LOWER(pro_nombre);

-- INDICADOR-NOMBRE BUSUQEDA
alter table siap2.ss_indicador add ind_nombre_busqueda character varying(250);
alter table siap2.ss_indicador_hist add ind_nombre_busqueda character varying(250);
update siap2.ss_indicador set ind_nombre_busqueda=LOWER(ind_nombre);

-- UNIDAD TECNICA-NOMBRE BUSUQEDA
alter table siap2.ss_unidad_tecnica add uni_nombre_busqueda character varying(250);
alter table siap2.ss_unidad_tecnica_hist add uni_nombre_busqueda character varying(250);
update siap2.ss_unidad_tecnica set uni_nombre_busqueda=LOWER(uni_nombre);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.departamento', true, now(), 'SCRIPT', 'admin','Departamento', 0, 1);

-- 6.21.2
INSERT INTO siap2.ss_operacion(ope_id, ope_codigo, ope_descripcion, ope_nombre, ope_origen, ope_tipocampo, ope_user_code, ope_version, ope_vigente, ope_categoria_id)
VALUES(NEXTVAL('siap2.seq_operacion'), 'VER_METAS_DE_INDICADORES_POA', 'Permiso para ver metas de indicadores de cada POA', 'Ver metas de indicadores de cada POA.', 'SCRIPT', 0, 0, NULL, true, NULL);

INSERT INTO siap2.ss_operacion(ope_id, ope_codigo, ope_descripcion, ope_nombre, ope_origen, ope_tipocampo, ope_user_code, ope_version, ope_vigente, ope_categoria_id)
VALUES(NEXTVAL('siap2.seq_operacion'), 'VER_VALORES_DE_INDICADORES_POA', 'Permiso para ver los valores de indicadores de cada POA', 'Ver valores de indicadores de cada POA.', 'SCRIPT', 0, 0, NULL, true, NULL);

--ERROR ERR_NOMBRE_LARGO_100
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_NOMBRE_LARGO_100', true, now(), 'ANP-AGENPORT', 'admin','El nombre no debe superar los 100 caracteres.', 0, 1);


alter table siap2.ss_poa  add poa_nombre varchar(100);
alter table siap2.ss_poa_hist  add poa_nombre varchar(100);

alter table siap2.ss_poa  add poa_nombre_busqueda varchar(100);
alter table siap2.ss_poa_hist  add poa_nombre_busqueda varchar(100);

INSERT INTO siap2.ss_operacion(ope_id, ope_codigo, ope_descripcion, ope_nombre, ope_origen, ope_tipocampo, ope_user_code, ope_version, ope_vigente, ope_categoria_id)
VALUES(NEXTVAL('siap2.seq_operacion'), 'VER_MATRIZ_DE_RIESGOS_POA', 'Operación que permite ver la matriz de riesgos de POA', 'Ver matriz de riesgos del POA.', 'SCRIPT', 0, 0, NULL, true, NULL);


--6.21.3
alter table siap2.ss_tope_presupestal add tp_cant_matricula numeric(15,0) NULL;
alter table siap2.ss_tope_presupestal_hist  add tp_cant_matricula numeric(15,0) NULL;

--INSERT PARA TABLA LOCK
INSERT INTO siap2.ss_lock (loc_id, descripcion) VALUES(1, 'LOCK_CONTROL_DE_INGRESO');
INSERT INTO siap2.ss_lock (loc_id, descripcion) VALUES(2, 'LOCK_MODIFICACION_CUPOS_PUERTA');
INSERT INTO siap2.ss_lock (loc_id, descripcion) VALUES(3, 'LOCK_CUPOS_PUERTA');
INSERT INTO siap2.ss_lock (loc_id, descripcion) VALUES(4, 'LOCK_CALCULO_REPORTE');
INSERT INTO siap2.ss_lock (loc_id, descripcion) VALUES(5, 'LOCK_CODIGUERAS');
INSERT INTO siap2.ss_lock (loc_id, descripcion) VALUES(6, 'LOCK_SECUENCIA_PROCESO_ADQUISICION');
INSERT INTO siap2.ss_lock (loc_id, descripcion) VALUES(7, 'LOCK_SECUENCIA_CONTRATO_ORDEN_COMPRA');
INSERT INTO siap2.ss_lock (loc_id, descripcion) VALUES(8, 'LOCK_GENERAR_BLOQUEOS_DE_PAC');
INSERT INTO siap2.ss_lock (loc_id, descripcion) VALUES(9, 'LOCK_GENERAR_BLOQUEOS_PEP');
INSERT INTO siap2.ss_lock (loc_id, descripcion) VALUES(10, 'LOCK_SECUENCIA_PAGO_CONTRATO');
INSERT INTO siap2.ss_lock (loc_id, descripcion) VALUES(11, 'LOCK_SECUENCIA_SOLICITUD_PAGO_ACTA');
INSERT INTO siap2.ss_lock (loc_id, descripcion) VALUES(12, 'LOCK_GENERACION_QUEDAN');
INSERT INTO siap2.ss_lock (loc_id, descripcion) VALUES(13, 'LOCK_SECUENCIA_MODIFICATIVA_CONTRATO');

INSERT INTO siap2.ss_lock_hist (loc_id, descripcion) VALUES(1, 'LOCK_CONTROL_DE_INGRESO');
INSERT INTO siap2.ss_lock_hist (loc_id, descripcion) VALUES(2, 'LOCK_MODIFICACION_CUPOS_PUERTA');
INSERT INTO siap2.ss_lock_hist (loc_id, descripcion) VALUES(3, 'LOCK_CUPOS_PUERTA');
INSERT INTO siap2.ss_lock_hist (loc_id, descripcion) VALUES(4, 'LOCK_CALCULO_REPORTE');
INSERT INTO siap2.ss_lock_hist (loc_id, descripcion) VALUES(5, 'LOCK_CODIGUERAS');
INSERT INTO siap2.ss_lock_hist (loc_id, descripcion) VALUES(6, 'LOCK_SECUENCIA_PROCESO_ADQUISICION');
INSERT INTO siap2.ss_lock_hist (loc_id, descripcion) VALUES(7, 'LOCK_SECUENCIA_CONTRATO_ORDEN_COMPRA');
INSERT INTO siap2.ss_lock_hist (loc_id, descripcion) VALUES(8, 'LOCK_GENERAR_BLOQUEOS_DE_PAC');
INSERT INTO siap2.ss_lock_hist (loc_id, descripcion) VALUES(9, 'LOCK_GENERAR_BLOQUEOS_PEP');
INSERT INTO siap2.ss_lock_hist (loc_id, descripcion) VALUES(10, 'LOCK_SECUENCIA_PAGO_CONTRATO');
INSERT INTO siap2.ss_lock_hist (loc_id, descripcion) VALUES(11, 'LOCK_SECUENCIA_SOLICITUD_PAGO_ACTA');
INSERT INTO siap2.ss_lock_hist (loc_id, descripcion) VALUES(12, 'LOCK_GENERACION_QUEDAN');
INSERT INTO siap2.ss_lock_hist (loc_id, descripcion) VALUES(13, 'LOCK_SECUENCIA_MODIFICATIVA_CONTRATO');

alter table siap2.ss_rel_padqitem_padqins  add rel_duracion_ins_contrato numeric(15,0) NULL;
alter table siap2.ss_rel_padqitem_padqins_hist  add rel_duracion_ins_contrato numeric(15,0) NULL;

--6.21.4
insert into siap2.ss_secuencias(sec_id,sec_nro,sec_codigo,sec_descripcion,sec_ult_mod,sec_ult_usuario,sec_version) VALUES(1,0,'SEC_NUM_CER_DISP_PRESUP','SEC_NUM_CER_DISP_PRESUP',null,'SCRIPT',1);

insert into siap2.ss_secuencias(sec_id,sec_nro,sec_codigo,sec_descripcion,sec_ult_mod,sec_ult_usuario,sec_version) VALUES(2,0,'SEC_NUM_POLIZA_CONCENTRACION','SEC_NUM_POLIZA_CONCENTRACION',null,'SCRIPT',1);

INSERT INTO siap2.ss_configuraciones (cnf_id, cnf_codigo, cnf_descripcion, cnf_html, cnf_protegido, cnf_ult_mod, cnf_ult_origen, cnf_ult_usuario, cnf_valor, cnf_version) 
VALUES(nextval('siap2.seq_configuraciones'), 'REPORTE_AREA_PAC', 'Reporte del area del PAC', false, false, NULL, NULL, NULL, 'REPORTE AREA PAC', 1);

INSERT INTO siap2.ss_configuraciones (cnf_id, cnf_codigo, cnf_descripcion, cnf_html, cnf_protegido, cnf_ult_mod, cnf_ult_origen, cnf_ult_usuario, cnf_valor, cnf_version) 
VALUES(nextval('siap2.seq_configuraciones'), 'REPORTE_TITULO_PAC', 'Reporte del Titulo del PAC', false, false, NULL, NULL, NULL, 'REPORTE TITULO PAC', 1);

INSERT INTO siap2.ss_configuraciones (cnf_id, cnf_codigo, cnf_descripcion, cnf_html, cnf_protegido, cnf_ult_mod, cnf_ult_origen, cnf_ult_usuario, cnf_valor, cnf_version) 
VALUES(nextval('siap2.seq_configuraciones'), 'REPORTE_AREA_DOCUMENTO_ORDEN_COMPRA', 'Reporte area documento orden de compra', false, false, NULL, NULL, NULL, 'REPORTE AREA DOCUMENTO ORDEN COMPRA', 1);

INSERT INTO siap2.ss_configuraciones (cnf_id, cnf_codigo, cnf_descripcion, cnf_html, cnf_protegido, cnf_ult_mod, cnf_ult_origen, cnf_ult_usuario, cnf_valor, cnf_version) 
VALUES(nextval('siap2.seq_configuraciones'), 'REPORTE_TITULO_DOCUMENTO_ORDEN_COMPRA', 'Reporte titulo documento orden de compra', false, false, NULL, NULL, NULL, 'REPORTE TITULO DOCUMENTO ORDEN COMPRA', 1);

INSERT INTO siap2.ss_configuraciones (cnf_id, cnf_codigo, cnf_descripcion, cnf_html, cnf_protegido, cnf_ult_mod, cnf_ult_origen, cnf_ult_usuario, cnf_valor, cnf_version) 
VALUES(nextval('siap2.seq_configuraciones'), 'REPORTE_AREA_INVITACION_PROVEEDRES', 'Reporte area invitacion proveedores', false, false, NULL, NULL, NULL, 'REPORTE AREA INVITACION PROVEEDRES', 1);

INSERT INTO siap2.ss_configuraciones (cnf_id, cnf_codigo, cnf_descripcion, cnf_html, cnf_protegido, cnf_ult_mod, cnf_ult_origen, cnf_ult_usuario, cnf_valor, cnf_version) 
VALUES(nextval('siap2.seq_configuraciones'), 'REPORTE_TITULO_INVITACION_PROVEEDRES', 'Reporte titulo invitacion proveedores', false, false, NULL, NULL, NULL, 'REPORTE TITULO INVITACION PROVEEDRES', 1);

INSERT INTO siap2.ss_configuraciones (cnf_id, cnf_codigo, cnf_descripcion, cnf_html, cnf_protegido, cnf_ult_mod, cnf_ult_origen, cnf_ult_usuario, cnf_valor, cnf_version) 
VALUES(nextval('siap2.seq_configuraciones'), 'REPORTE_AREA_RESERVA_FONDOS', 'Reporte area reserva de fondos', false, false, NULL, NULL, NULL, 'REPORTE AREA RESERVA FONDOS', 1);

INSERT INTO siap2.ss_configuraciones (cnf_id, cnf_codigo, cnf_descripcion, cnf_html, cnf_protegido, cnf_ult_mod, cnf_ult_origen, cnf_ult_usuario, cnf_valor, cnf_version) 
VALUES(nextval('siap2.seq_configuraciones'), 'REPORTE_TITULO_RESERVA_FONDOS', 'Reporte titulo reserva de fondos', false, false, NULL, NULL, NULL, 'REPORTE TITULO RESERVA FONDOS', 1);

INSERT INTO siap2.ss_configuraciones (cnf_id, cnf_codigo, cnf_descripcion, cnf_html, cnf_protegido, cnf_ult_mod, cnf_ult_origen, cnf_ult_usuario, cnf_valor, cnf_version) 
VALUES(nextval('siap2.seq_configuraciones'), 'REPORTE_AREA_PAC_GLOBAL', 'Reporte area PAC global', false, false, NULL, NULL, NULL, 'REPORTE AREA PAC GLOBAL', 1);

INSERT INTO siap2.ss_configuraciones (cnf_id, cnf_codigo, cnf_descripcion, cnf_html, cnf_protegido, cnf_ult_mod, cnf_ult_origen, cnf_ult_usuario, cnf_valor, cnf_version) 
VALUES(nextval('siap2.seq_configuraciones'), 'REPORTE_TITULO_PAC_GLOBAL', 'Reporte del Titulo del PAC global', false, false, NULL, NULL, NULL, 'REPORTE TITULO PAC GLOBAL', 1);

INSERT INTO siap2.ss_configuraciones (cnf_id, cnf_codigo, cnf_descripcion, cnf_html, cnf_protegido, cnf_ult_mod, cnf_ult_origen, cnf_ult_usuario, cnf_valor, cnf_version) 
VALUES(nextval('siap2.seq_configuraciones'), 'REPORTE_AREA_ACTA_RECEPCION', 'Reporte area PAC recepción', false, false, NULL, NULL, NULL, 'REPORTE AREA ACTA RECEPCION', 1);

INSERT INTO siap2.ss_configuraciones (cnf_id, cnf_codigo, cnf_descripcion, cnf_html, cnf_protegido, cnf_ult_mod, cnf_ult_origen, cnf_ult_usuario, cnf_valor, cnf_version) 
VALUES(nextval('siap2.seq_configuraciones'), 'REPORTE_TITULO_ACTA_RECEPCION', 'Reporte del Titulo acta de recepción', false, false, NULL, NULL, NULL, 'REPORTE TITULO ACTA RECEPCION', 1);

INSERT INTO siap2.ss_configuraciones (cnf_id, cnf_codigo, cnf_descripcion, cnf_html, cnf_protegido, cnf_ult_mod, cnf_ult_origen, cnf_ult_usuario, cnf_valor, cnf_version) 
VALUES(nextval('siap2.seq_configuraciones'), 'REPORTE_AREA_ACTA_ANTICIPO', 'Reporte area acta anticipo', false, false, NULL, NULL, NULL, 'REPORTE AREA ACTA ANTICIPO', 1);

INSERT INTO siap2.ss_configuraciones (cnf_id, cnf_codigo, cnf_descripcion, cnf_html, cnf_protegido, cnf_ult_mod, cnf_ult_origen, cnf_ult_usuario, cnf_valor, cnf_version) 
VALUES(nextval('siap2.seq_configuraciones'), 'REPORTE_TITULO_ACTA_ANTICIPO', 'Reporte del Titulo acta de anticipo', false, false, NULL, NULL, NULL, 'REPORTE TITULO ACTA ANTICIPO', 1);

INSERT INTO siap2.ss_configuraciones (cnf_id, cnf_codigo, cnf_descripcion, cnf_html, cnf_protegido, cnf_ult_mod, cnf_ult_origen, cnf_ult_usuario, cnf_valor, cnf_version) 
VALUES(nextval('siap2.seq_configuraciones'), 'REPORTE_AREA_ACTA_DEVOLUCION', 'Reporte area acta de devolución', false, false, NULL, NULL, NULL, 'REPORTE AREA ACTA DEVOLUCION', 1);

INSERT INTO siap2.ss_configuraciones (cnf_id, cnf_codigo, cnf_descripcion, cnf_html, cnf_protegido, cnf_ult_mod, cnf_ult_origen, cnf_ult_usuario, cnf_valor, cnf_version) 
VALUES(nextval('siap2.seq_configuraciones'), 'REPORTE_TITULO_ACTA_DEVOLUCION', 'Reporte del Titulo acta de devolución', false, false, NULL, NULL, NULL, 'REPORTE TITULO ACTA DEVOLUCION', 1);

INSERT INTO siap2.ss_configuraciones (cnf_id, cnf_codigo, cnf_descripcion, cnf_html, cnf_protegido, cnf_ult_mod, cnf_ult_origen, cnf_ult_usuario, cnf_valor, cnf_version) 
VALUES(nextval('siap2.seq_configuraciones'), 'REPORTE_AREA_QUEDAN', 'Reporte area quedan', false, false, NULL, NULL, NULL, 'REPORTE AREA QUEDAN', 1);

INSERT INTO siap2.ss_configuraciones (cnf_id, cnf_codigo, cnf_descripcion, cnf_html, cnf_protegido, cnf_ult_mod, cnf_ult_origen, cnf_ult_usuario, cnf_valor, cnf_version) 
VALUES(nextval('siap2.seq_configuraciones'), 'REPORTE_TITULO_QUEDAN', 'Reporte título quedan', false, false, NULL, NULL, NULL, 'REPORTE TITULO QUEDAN', 1);

INSERT INTO siap2.ss_configuraciones (cnf_id, cnf_codigo, cnf_descripcion, cnf_html, cnf_protegido, cnf_ult_mod, cnf_ult_origen, cnf_ult_usuario, cnf_valor, cnf_version) 
VALUES(nextval('siap2.seq_configuraciones'), 'REPORTE_AREA_ANP_GRAL', 'Reporte area ANP general', false, false, NULL, NULL, NULL, 'REPORTE AREA ANP GRAL', 1);

INSERT INTO siap2.ss_configuraciones (cnf_id, cnf_codigo, cnf_descripcion, cnf_html, cnf_protegido, cnf_ult_mod, cnf_ult_origen, cnf_ult_usuario, cnf_valor, cnf_version) 
VALUES(nextval('siap2.seq_configuraciones'), 'REPORTE_TITULO_ANP_GRAL', 'Reporte título ANP general', false, false, NULL, NULL, NULL, 'REPORTE TITULO ANP GRAL', 1);

INSERT INTO siap2.ss_configuraciones (cnf_id, cnf_codigo, cnf_descripcion, cnf_html, cnf_protegido, cnf_ult_mod, cnf_ult_origen, cnf_ult_usuario, cnf_valor, cnf_version) 
VALUES(nextval('siap2.seq_configuraciones'), 'REPORTE_AREA_AC_GRAL', 'Reporte area AC general', false, false, NULL, NULL, NULL, 'REPORTE AREA AC GRAL', 1);

INSERT INTO siap2.ss_configuraciones (cnf_id, cnf_codigo, cnf_descripcion, cnf_html, cnf_protegido, cnf_ult_mod, cnf_ult_origen, cnf_ult_usuario, cnf_valor, cnf_version) 
VALUES(nextval('siap2.seq_configuraciones'), 'REPORTE_TITULO_AC_GRAL', 'Reporte título ac general', false, false, NULL, NULL, NULL, 'REPORTE TITULO AC GRAL', 1);

INSERT INTO siap2.ss_configuraciones (cnf_id, cnf_codigo, cnf_descripcion, cnf_html, cnf_protegido, cnf_ult_mod, cnf_ult_origen, cnf_ult_usuario, cnf_valor, cnf_version) 
VALUES(nextval('siap2.seq_configuraciones'), 'REPORTE_AREA_ORDEN_INICIO', 'Reporte título orden inicio', false, false, NULL, NULL, NULL, 'REPORTE AREA ORDEN INICIO', 1);

INSERT INTO siap2.ss_configuraciones (cnf_id, cnf_codigo, cnf_descripcion, cnf_html, cnf_protegido, cnf_ult_mod, cnf_ult_origen, cnf_ult_usuario, cnf_valor, cnf_version) 
VALUES(nextval('siap2.seq_configuraciones'), 'REPORTE_TITULO_ORDEN_INICIO', 'Reporte título orden inicio', false, false, NULL, NULL, NULL, 'REPORTE TITULO ORDEN INICIO', 1);

INSERT INTO siap2.ss_configuraciones (cnf_id, cnf_codigo, cnf_descripcion, cnf_html, cnf_protegido, cnf_ult_mod, cnf_ult_origen, cnf_ult_usuario, cnf_valor, cnf_version) 
VALUES(nextval('siap2.seq_configuraciones'), 'REPORTE_AREA_COMPROBANTE_REC_EXP_PAGO', 'Reporte area comprobante rec exp pago', false, false, NULL, NULL, NULL, 'REPORTE AREA COMPROBANTE REC EXP PAGO', 1);

INSERT INTO siap2.ss_configuraciones (cnf_id, cnf_codigo, cnf_descripcion, cnf_html, cnf_protegido, cnf_ult_mod, cnf_ult_origen, cnf_ult_usuario, cnf_valor, cnf_version) 
VALUES(nextval('siap2.seq_configuraciones'), 'REPORTE_TITULO_COMPROBANTE_REC_EXP_PAGO', 'Reporte título comprobante rec exp pago', false, false, NULL, NULL, NULL, 'REPORTE TITULO COMPROBANTE REC EXP PAGO', 1);

INSERT INTO siap2.ss_configuraciones (cnf_id, cnf_codigo, cnf_descripcion, cnf_html, cnf_protegido, cnf_ult_mod, cnf_ult_origen, cnf_ult_usuario, cnf_valor, cnf_version) 
VALUES(nextval('siap2.seq_configuraciones'), 'REPORTE_AREA_RETENCION_IMPUESTO_POR_PROYECTO', 'Reporte area retención impuesto por proyecto', false, false, NULL, NULL, NULL, 'REPORTE AREA RETENCION IMPUESTO POR PROYECTO', 1);

INSERT INTO siap2.ss_configuraciones (cnf_id, cnf_codigo, cnf_descripcion, cnf_html, cnf_protegido, cnf_ult_mod, cnf_ult_origen, cnf_ult_usuario, cnf_valor, cnf_version) 
VALUES(nextval('siap2.seq_configuraciones'), 'REPORTE_TITULO_RETENCION_IMPUESTO_POR_PROYECTO', 'Reporte título retención impuesto por proyecto', false, false, NULL, NULL, NULL, 'REPORTE TITULO RETENCION IMPUESTO POR PROYECTO', 1);

INSERT INTO siap2.ss_configuraciones (cnf_id, cnf_codigo, cnf_descripcion, cnf_html, cnf_protegido, cnf_ult_mod, cnf_ult_origen, cnf_ult_usuario, cnf_valor, cnf_version) 
VALUES(nextval('siap2.seq_configuraciones'), 'REPORTE_AREA_RETENCION_IMPUESTO_POR_PROVEEDOR', 'Reporte area retención impuesto por proveedor', false, false, NULL, NULL, NULL, 'REPORTE AREA RETENCION IMPUESTO POR PROVEEDOR', 1);

INSERT INTO siap2.ss_configuraciones (cnf_id, cnf_codigo, cnf_descripcion, cnf_html, cnf_protegido, cnf_ult_mod, cnf_ult_origen, cnf_ult_usuario, cnf_valor, cnf_version) 
VALUES(nextval('siap2.seq_configuraciones'), 'REPORTE_TITULO_RETENCION_IMPUESTO_POR_PROVEEDOR', 'Reporte título retención inpuesto por proveedor', false, false, NULL, NULL, NULL, 'REPORTE TITULO RETENCION IMPUESTO POR PROVEEDOR', 1);

INSERT INTO siap2.ss_configuraciones (cnf_id, cnf_codigo, cnf_descripcion, cnf_html, cnf_protegido, cnf_ult_mod, cnf_ult_origen, cnf_ult_usuario, cnf_valor, cnf_version) 
VALUES(nextval('siap2.seq_configuraciones'), 'REPORTE_AREA_FICHA_CONTRATO', 'Reporte area ficha contrato', false, false, NULL, NULL, NULL, 'REPORTE AREA FICHA CONTRATO', 1);

INSERT INTO siap2.ss_configuraciones (cnf_id, cnf_codigo, cnf_descripcion, cnf_html, cnf_protegido, cnf_ult_mod, cnf_ult_origen, cnf_ult_usuario, cnf_valor, cnf_version) 
VALUES(nextval('siap2.seq_configuraciones'), 'REPORTE_TITULO_FICHA_CONTRATO', 'Reporte título ficha contrato', false, false, NULL, NULL, NULL, 'REPORTE TITULO FICHA CONTRATO', 1);

INSERT INTO siap2.ss_configuraciones (cnf_id, cnf_codigo, cnf_descripcion, cnf_html, cnf_protegido, cnf_ult_mod, cnf_ult_origen, cnf_ult_usuario, cnf_valor, cnf_version) 
VALUES(nextval('siap2.seq_configuraciones'), 'REPORTE_AREA_COMPROMISO_PRESUPUESTARIO', 'Reporte area compromiso presupuestario', false, false, NULL, NULL, NULL, 'REPORTE AREA COMPROMISO PRESUPUESTARIO', 1);

INSERT INTO siap2.ss_configuraciones (cnf_id, cnf_codigo, cnf_descripcion, cnf_html, cnf_protegido, cnf_ult_mod, cnf_ult_origen, cnf_ult_usuario, cnf_valor, cnf_version) 
VALUES(nextval('siap2.seq_configuraciones'), 'REPORTE_TITULO_COMPROMISO_PRESUPUESTARIO', 'Reporte título compromiso presupuestario', false, false, NULL, NULL, NULL, 'REPORTE TITULO COMPROMISO PRESUPUESTARIO', 1);

INSERT INTO siap2.ss_configuraciones (cnf_id, cnf_codigo, cnf_descripcion, cnf_html, cnf_protegido, cnf_ult_mod, cnf_ult_origen, cnf_ult_usuario, cnf_valor, cnf_version) 
VALUES(nextval('siap2.seq_configuraciones'), 'REPORTE_AREA_COMPROMISO_PARA_PROGRAMACION_PAGOS', 'Reporte area compromiso para programación de pagos', false, false, NULL, NULL, NULL, 'REPORTE AREA COMPROMISO PARA PROGRAMACION PAGOS', 1);

INSERT INTO siap2.ss_configuraciones (cnf_id, cnf_codigo, cnf_descripcion, cnf_html, cnf_protegido, cnf_ult_mod, cnf_ult_origen, cnf_ult_usuario, cnf_valor, cnf_version) 
VALUES(nextval('siap2.seq_configuraciones'), 'REPORTE_TITULO_COMPROMISO_PARA_PROGRAMACION_PAGOS', 'Reporte título de compromiso para programación de pagos', false, false, NULL, NULL, NULL, 'REPORTE TITULO COMPROMISO PARA PROGRAMACION PAGOS', 1);

INSERT INTO siap2.ss_configuraciones (cnf_id, cnf_codigo, cnf_descripcion, cnf_html, cnf_protegido, cnf_ult_mod, cnf_ult_origen, cnf_ult_usuario, cnf_valor, cnf_version) 
VALUES(nextval('siap2.seq_configuraciones'), 'REPORTE_AREA_CERTIFICADO_DISPONIBILIDAD_PROCESO', 'Reporte area certificado de disponibilidad del proceso', false, false, NULL, NULL, NULL, 'REPORTE AREA CERTIFICADO DISPONIBILIDAD PROCESO', 1);

INSERT INTO siap2.ss_configuraciones (cnf_id, cnf_codigo, cnf_descripcion, cnf_html, cnf_protegido, cnf_ult_mod, cnf_ult_origen, cnf_ult_usuario, cnf_valor, cnf_version) 
VALUES(nextval('siap2.seq_configuraciones'), 'REPORTE_TITULO_CERTIFICADO_DISPONIBILIDAD_PROCESO', 'Reporte título certificado de disponibilidad del proceso', false, false, NULL, NULL, NULL, 'REPORTE TITULO CERTIFICADO DISPONIBILIDAD PROCESO', 1);

--6.21.5
ALTER TABLE siap2.ss_insumo ADD COLUMN ins_corresponde_activo_fijo boolean default false;
ALTER TABLE siap2.ss_insumo_hist ADD COLUMN ins_corresponde_activo_fijo boolean default false;

INSERT INTO siap2.ss_textos (tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id) values (nextval('siap2.seq_textos'), 'labels.correspondeActivoFijo', true, '2019-10-08 17:00:00', 'SCRIPT', 'admin', 'Corresponde activo fijo', 0, 1);

INSERT INTO siap2.ss_textos (tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id) values (nextval('siap2.seq_textos'), 'labels.tipoBien', true, '2019-10-08 17:00:00', 'SCRIPT', 'admin', 'Tipo de bien', 0, 1);

INSERT INTO siap2.ss_textos (tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id) values (nextval('siap2.seq_textos'), 'ERR_TIPO_BIEN_VACIO', true, '2019-10-08 17:00:00', 'SCRIPT', 'admin', 'Debe completar el tipo de bien', 0, 1);

INSERT INTO siap2.ss_textos (tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id) values (nextval('siap2.seq_textos'), 'ERR_CATEGORIA_BIEN_VACIO', true, '2019-10-08 17:00:00', 'SCRIPT', 'admin', 'Debe completar la categoria del bien', 0, 1);

INSERT INTO siap2.ss_textos (tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id) 
values (nextval('siap2.seq_textos'), 'labels.BienesActivoFijoCreadosDeNumeroActa', true, '2019-10-08 17:00:00', 'SCRIPT', 'admin', 'Se han creado bienes em activo fijo.', 0, 1);



ALTER TABLE siap2.ss_contrato_oc DROP COLUMN con_fechas_desde_oi;
ALTER TABLE siap2.ss_contrato_oc_hist DROP COLUMN con_fechas_desde_oi;

ALTER TABLE siap2.ss_contrato_oc ADD con_fechas_desde_oi boolean default false;
ALTER TABLE siap2.ss_contrato_oc_hist ADD con_fechas_desde_oi boolean default false;

ALTER TABLE siap2.ss_pago_contrato DROP COLUMN pag_con_detalle;
ALTER TABLE siap2.ss_pago_contrato_hist DROP COLUMN pag_con_detalle;

ALTER TABLE siap2.ss_pago_contrato ADD pag_con_detalle boolean default false;
ALTER TABLE siap2.ss_pago_contrato_hist ADD pag_con_detalle boolean default false;

ALTER TABLE siap2.ss_pago_contrato ADD pag_con_bienes_activo_fijo boolean default false;
ALTER TABLE siap2.ss_pago_contrato_hist ADD pag_con_bienes_activo_fijo boolean default false;


ALTER TABLE siap2.ss_insumo ADD COLUMN ins_tipo_bien int8 null;
ALTER TABLE siap2.ss_insumo_hist ADD COLUMN ins_tipo_bien int8 null;

--6.21.6

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.habPlanificacion', true, now(), 'SCRIPT', 'admin','Hab. Planificicacón', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.habEjecucion', true, now(), 'SCRIPT', 'admin','Hab. Ejecución', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.OAE', true, now(), 'SCRIPT', 'admin','Organismos de Administración escolar', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.tipoOEA', true, now(), 'SCRIPT', 'admin','Tipos de Organismo', 0, 1);

ALTER TABLE siap2.ss_rel_ges_pres_es_anio_fiscal ADD COLUMN descripcion varchar(100) null;
ALTER TABLE siap2.ss_rel_ges_pres_es_anio_fiscal_hist ADD COLUMN descripcion varchar(100) null;

--RELACION TIPO ORGANISMO-PRESUPUESTO ESCOLAR

CREATE TABLE siap2.ss_rel_ges_pres_es_tipo_organismo_escolar (
	ges_id int8 NOT NULL,
	toa_pk int8 NOT NULL
);

CREATE TABLE siap2.ss_rel_ges_pres_es_tipo_organismo_escolar_hist (
	ges_id int8 NULL,
	toa_pk int8 NULL,
	start_date timestamp NULL,
	end_date timestamp NULL
);
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.menu.SeguimientoIndicadores', true, now(), 'SCRIPT', 'admin','Seguimiento de Indicadores', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.SeguimientoIndicadores', true, now(), 'SCRIPT', 'admin','Seguimiento de Indicadores', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.unidadesTecnicas', true, now(), 'SCRIPT', 'admin','Unidades Técnicas', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.porcentajeAvanceActividades', true, now(), 'SCRIPT', 'admin','Porcentaje de Avance - Actividades', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.ultimoPeriodoReportado', true, now(), 'SCRIPT', 'admin','Ult. período reportado', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.porAvance', true, now(), 'SCRIPT', 'admin','% Av', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.ultPeriodo', true, now(), 'SCRIPT', 'admin','Ult. Per.', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.poa', true, now(), 'SCRIPT', 'admin','POA', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.conocidoPor', true, now(), 'SCRIPT', 'admin','Conocido Por', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.menu.ObjetosEspecificosGasto', true, now(), 'SCRIPT', 'admin','Objetos Específicos del Gasto', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.ObjetosEspecificosGasto', true, now(), 'SCRIPT', 'admin','Objetos Específicos del Gasto', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.TipoIngresoDatosCalculo', true, now(), 'SCRIPT', 'admin','Tipo de ingreso de datos para el cálculo', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.TipoIngreso', true, now(), 'SCRIPT', 'admin','Tipo de ingreso', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.Seguro_desea_eliminarSubComponente', true, now(), 'SCRIPT', 'admin','¿Seguro desea eliminar el subcomponente ', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.Seguro_desea_eliminar_rubro', true, now(), 'SCRIPT', 'admin','¿Seguro desea eliminar el rubro ', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.ReceptorTransferencia', true, now(), 'SCRIPT', 'admin','Receptor de la transferencia', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.NombreSubcomponente', true, now(), 'SCRIPT', 'admin','Nombre del subcomponente', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.CodigoSubcomponente', true, now(), 'SCRIPT', 'admin','Código del subcomponente', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.MontoDeducirCentroEducativo', true, now(), 'SCRIPT', 'admin','Monto a deducir por centro educativo', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.menu.TiposTransferencia', true, now(), 'SCRIPT', 'admin','Tipos de Transferencia', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.TiposTransferencia', true, now(), 'SCRIPT', 'admin','Tipos de Transferencia', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.TipoTransferencia', true, now(), 'SCRIPT', 'admin','Tipo de Transferencia', 0, 1);

INSERT INTO siap2.ss_operacion(ope_id, ope_codigo, ope_descripcion, ope_nombre, ope_origen, ope_tipocampo, ope_user_code, ope_version, ope_vigente, ope_categoria_id)
VALUES(NEXTVAL('siap2.seq_operacion'), 'VER_SEGUIMIENTO_INDICADORES_POA', 'Operación que permite dar seguimiento a los idnciadores del POA', 'Ver Seguimiento de indicadores del POA.', 'SCRIPT', 0, 0, NULL, true, NULL);

INSERT INTO siap2.ss_operacion(ope_id, ope_codigo, ope_descripcion, ope_nombre, ope_origen, ope_tipocampo, ope_user_code, ope_version, ope_vigente, ope_categoria_id)
VALUES(NEXTVAL('siap2.seq_operacion'), 'CONSULTAR_CATALOGO_OBJETOS_ESPECFICOS_GASTO', 'Operación que permite consultar los objetos especificos del gasto', 'Ver Objetos especificos del gasto.', 'SCRIPT', 0, 0, NULL, true, NULL);

INSERT INTO siap2.ss_operacion(ope_id, ope_codigo, ope_descripcion, ope_nombre, ope_origen, ope_tipocampo, ope_user_code, ope_version, ope_vigente, ope_categoria_id)
VALUES(NEXTVAL('siap2.seq_operacion'), 'CONSULTA_TIPOS_TRANSFERENCIA', 'Operación que permite consultar los tipos de transferencia', 'Consulta de tipos de trasnferencia.', 'SCRIPT', 0, 0, NULL, true, NULL);


alter table siap2.ss_poa_actividades_metas add pam_ult_peri_modificado numeric(15,0) NULL;
alter table siap2.ss_poa_actividades_metas_hist add pam_ult_peri_modificado numeric(15,0) NULL;

ALTER TABLE siap2.ss_insumo ADD COLUMN ins_conocido_por  varchar(100) null;
ALTER TABLE siap2.ss_insumo_hist ADD COLUMN ins_conocido_por  varchar(100) null;

CREATE SEQUENCE siap2.seq_tipos_transferencia START WITH 1 increment by 1 minvalue 1 maxvalue 9999999999999;

CREATE TABLE siap2.ss_tipos_transferencia (
	tt_id int8 NOT NULL, -- Correlativo
	tt_nombre varchar NULL, -- Nombre del registro
	tt_codigo varchar NULL, -- Código itentificador del registro
	tt_habilitado bool NULL, -- Estado del registro
	tt_ult_mod timestamp NULL, -- Última fecha de modificación
	tt_ult_usuario varchar NULL, -- Último usuario modificador
	tt_version int8 NULL -- Última version del registro
);
COMMENT ON TABLE siap2.ss_tipos_transferencia IS 'Catálogo de tipos de tranferencia';

-- Column comments

COMMENT ON COLUMN siap2.ss_tipos_transferencia.tt_id IS 'Correlativo';
COMMENT ON COLUMN siap2.ss_tipos_transferencia.tt_nombre IS 'Nombre del registro';
COMMENT ON COLUMN siap2.ss_tipos_transferencia.tt_codigo IS 'Código itentificador del registro';
COMMENT ON COLUMN siap2.ss_tipos_transferencia.tt_habilitado IS 'Estado del registro';
COMMENT ON COLUMN siap2.ss_tipos_transferencia.tt_ult_mod IS 'Última fecha de modificación';
COMMENT ON COLUMN siap2.ss_tipos_transferencia.tt_ult_usuario IS 'Último usuario modificador';
COMMENT ON COLUMN siap2.ss_tipos_transferencia.tt_version IS 'Última version del registro';

CREATE TABLE siap2.ss_tipos_transferencia_hist (
	tt_id int8 NOT NULL,
	tt_nombre varchar NULL,
	tt_codigo varchar NULL,
	tt_habilitado bool NULL,
	tt_ult_mod timestamp NULL,
	tt_ult_usuario varchar NULL,
	tt_version int8 NULL,
	start_date timestamp NULL,
	end_date timestamp NULL
);

UPDATE siap2.ss_ges_pres_es SET ges_tipo_transferencia = NULL;
ALTER TABLE siap2.ss_ges_pres_es ALTER COLUMN ges_tipo_transferencia TYPE int8 USING ges_tipo_transferencia::int8;

UPDATE siap2.ss_ges_pres_es_hist SET ges_tipo_transferencia = NULL;
ALTER TABLE siap2.ss_ges_pres_es_hist ALTER COLUMN ges_tipo_transferencia TYPE int8 USING ges_tipo_transferencia::int8;

--6.21.7
ALTER TABLE siap2.ss_impuestos_hist ADD COLUMN imp_prcj_ret_pj_ext numeric(20,2);
ALTER TABLE siap2.ss_impuestos_hist ADD COLUMN imp_prcj_ret_pf_ext numeric(20,2);

ALTER TABLE siap2.ss_impuestos_hist ADD COLUMN imp_prcj_ret_pj_nac numeric(20,2);
ALTER TABLE siap2.ss_impuestos_hist ADD COLUMN imp_prcj_ret_pf_nac numeric(20,2);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.tipoPresupuesto', true, now(), 'SCRIPT', 'admin','Tipo de Presupuesto', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.Nivel', true, now(), 'SCRIPT', 'admin','Nivel', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.ModalidadEducativa', true, now(), 'SCRIPT', 'admin','Modalidad Educativa', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.ModalidadAtención', true, now(), 'SCRIPT', 'admin','Modalidad de atención', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.SubmodalidadAtención', true, now(), 'SCRIPT', 'admin','Submodalidad atención', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.Detalle', true, now(), 'SCRIPT', 'admin','Detalle', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.MatriculasAprobadas', true, now(), 'SCRIPT', 'admin','Matriculas Aprobadas', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.MatriculasFormuladas', true, now(), 'SCRIPT', 'admin','Matriculas Formuladas', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.SubcomponentePresupuestoSedes', true, now(), 'SCRIPT', 'admin','Subcomponente Presupuesto - Sedes', 0, 1);


--6.21.8

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_PARA_EMITIR_QUEDAN_MONTO_TOTAL_FACTURAS_0_DEBE_COINCIDIR_CON_MONTO_ACTAS_MENOS_PORC_ANT_1', true, now(), 'SCRIPT', 'admin','Los montos de las facturas no coinciden', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.tipoOAE', true, now(), 'SCRIPT', 'admin','Tipo de OAE', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.NivelEducativo', true, now(), 'SCRIPT', 'admin','Nivel educativo', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.AnioOrigen', true, now(), 'SCRIPT', 'admin','Año Origen', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.AnioDestino', true, now(), 'SCRIPT', 'admin','Año Destino', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.DuplicarUnidadesPresupuestarias', true, now(), 'SCRIPT', 'admin','Duplicar Unidades Presupuestarias', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.Duplicar', true, now(), 'SCRIPT', 'admin','Duplicar', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.Sedes', true, now(), 'SCRIPT', 'admin','Sedes', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.CodigoArea', true, now(), 'SCRIPT', 'admin','Código del área', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.NombreArea', true, now(), 'SCRIPT', 'admin','Nombre del área', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.CodigoSubArea', true, now(), 'SCRIPT', 'admin','Código de la subárea', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.NombreSubArea', true, now(), 'SCRIPT', 'admin','Nombre de la subárea', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.DetalleFormulado', true, now(), 'SCRIPT', 'admin','Detalle Formulado', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.DetalleAprobado', true, now(), 'SCRIPT', 'admin','Detalle Aprobado', 0, 1);




INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_ANIO_ORIGEN_VACIO', true, now(), 'SCRIPT', 'admin','Debe completar el año fiscal de origen', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_ANIO_DESTINO_VACIO', true, now(), 'SCRIPT', 'admin','Debe completar el año fiscal de destino', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_ANIO_ORIGEN_IGUAL_ANIO_DESTINO', true, now(), 'SCRIPT', 'admin','El año fiscal de origen es igual al año fiscal de destino. Los años deben ser diferentes', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERROR_CODIGO_REPETIDO', true, now(), 'SCRIPT', 'admin','El código del registro ingresado está duplicado', 0, 1);

INSERT INTO siap2.ss_configuraciones (cnf_id, cnf_codigo, cnf_descripcion, cnf_html, cnf_protegido, cnf_ult_mod, cnf_ult_origen, cnf_ult_usuario, cnf_valor, cnf_version) 
VALUES(nextval('siap2.seq_configuraciones'), 'CODIGO_PAIS_PROVEEDOR_EL_SALVADOR', 'Código de pais para los proveedores de el salvador', false, false, NULL, NULL, NULL, 'CODIGO PAIS PROVEEDOR EL SALVADOR', 1);


alter table siap2.ss_tope_presupestal add tp_cant_matricula_aprobada int4 NULL default 0;
alter table siap2.ss_tope_presupestal_hist add tp_cant_matricula_aprobada int4 NULL default 0;

alter table siap2.ss_rel_ges_pres_es_anio_fiscal add cantidad_matriculas_aprobadas int4 NULL default 0;
alter table siap2.ss_rel_ges_pres_es_anio_fiscal_hist add cantidad_matriculas_aprobadas int4 NULL default 0;

ALTER TABLE siap2.ss_fuente_financiamiento ADD CONSTRAINT fuente_uk UNIQUE (fue_codigo);
ALTER TABLE siap2.ss_fuente_recursos ADD CONSTRAINT fuente_recursos_uk UNIQUE (fue_codigo);


alter table siap2.ss_tope_presupestal add tp_rel_ges_pres int8 NULL;
alter table siap2.ss_tope_presupestal_hist  add tp_rel_ges_pres int8 NULL;

ALTER TABLE siap2.ss_tope_presupestal ADD CONSTRAINT rel_ges_pres_fk FOREIGN KEY (tp_rel_ges_pres) REFERENCES siap2.ss_rel_ges_pres_es_anio_fiscal(id);

update siap2.ss_ges_pres_es set ges_tipo='CALCULO_POR_SISTEMA' where ges_tipo not in ('CARGA_DESDE_ARCHIVO');

update siap2.ss_fuente_recursos set fue_codigo=TRIM(fue_codigo), fue_nombre=TRIM(fue_nombre);

--6.21.10

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_DESCRIPCION_VACIO', true, now(), 'SCRIPT', 'admin','Debe completar la descripción', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_CODIGO_FUENTE_RECURSO_INEXISTENTE', true, now(), 'SCRIPT', 'admin','Código de fuente de recursos {0} inexistente', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_TRANSFERENCIA_INEXISTENTE_EN_COMPONENTE', true, now(), 'SCRIPT', 'admin','El número de tranfeerencia {0} no pertenece al presupuesto "{1}"', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_CODIGO_FUENTE_RECURSO_INEXISTENTE_EN_COMPONENTE', true, now(), 'SCRIPT', 'admin','Código de fuente de recursos {0} no pertenece al presupuesto "{1}"', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_PRESUPUESTO_SIN_FUENTES_FINANCIAMIENTO', true, now(), 'SCRIPT', 'admin','El presupuesto {0} no cuenta con fuentes de financiamiento', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_COMPONENTE_SIN_FUENTES_RECURSOS', true, now(), 'SCRIPT', 'admin','El presupuesto {0} no cuenta con fuentes de financiamiento', 0, 1);


INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_CODIGO_SUBCUENTA_INEXISTENTE_EN_PRESUPUESTO', true, now(), 'SCRIPT', 'admin','Código de cuenta {0} no pertenece al presupuesto "{1}"', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_TRANSFERENCIA_INEXISTENTE', true, now(), 'SCRIPT', 'admin','Número de tranferencia {0} inexistente', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_GNRAL_DELETE_TOPES', true, now(), 'SCRIPT', 'admin','Error al inhabilitar los techos presupuestales. Verifique que los movimientos del presupuesto escolar no estén asociados a otro objeto.', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_GNRAL_SAVE_UPDATE_TOPES', true, now(), 'SCRIPT', 'admin','Error al actualizar los techos presupuestale', 0, 1);



INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.CargarFormulacion', true, now(), 'SCRIPT', 'admin','Cargar Formulación', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.CargarAprobacion', true, now(), 'SCRIPT', 'admin','Cargar Aprobación', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.CrearTransferencialArchivo', true, now(), 'SCRIPT', 'admin','Creación de transferencia con archivo', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.InhabilitarTechos', true, now(), 'SCRIPT', 'admin','Inhabilitar Techos', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.ActualizarTechosAprobados', true, now(), 'SCRIPT', 'admin','Actualizar Techos Aprobados', 0, 1);

INSERT INTO siap2.ss_operacion(ope_id, ope_codigo, ope_descripcion, ope_nombre, ope_origen, ope_tipocampo, ope_user_code, ope_version, ope_vigente, ope_categoria_id)
VALUES(NEXTVAL('siap2.seq_operacion'), 'CREAR_EDITAR_TRANSFERENCIA', 'Operación que permite crear o editar los tipos de transferencia desde archivo', 'Crear o editar transferencias desde archivo.', 'SCRIPT', 0, 0, NULL, true, NULL);


INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.Transferencia', true, now(), 'SCRIPT', 'admin','Transferencia', 0, 1);

--6.21.12
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.PorcentajeCumplimiento', true, now(), 'SCRIPT', 'admin','% Cump.', 0, 1);

INSERT INTO siap2.ss_configuraciones (cnf_id, cnf_codigo, cnf_descripcion, cnf_html, cnf_protegido, cnf_ult_mod, cnf_ult_origen, cnf_ult_usuario, cnf_valor, cnf_version) 
VALUES(nextval('siap2.seq_configuraciones'), 'REPORTE_TITULO_METAS_POA', 'REPORTE DE SEGUIMIENTO METAS ESTRATEGICAS DE PLAN DE TRABAJO', false, false, NULL, NULL, NULL, 'REPORTE DE SEGUIMIENTO METAS ESTRATEGICAS DE PLAN DE TRABAJO', 1);

ALTER TABLE siap2.ss_categoria_presupuesto_escolar ADD COLUMN cpe_nombre_busqueda varchar null;
ALTER TABLE siap2.ss_categoria_presupuesto_escolar_hist ADD COLUMN cpe_nombre_busqueda varchar null;

update siap2.ss_categoria_presupuesto_escolar set cpe_nombre_busqueda=LOWER(cpe_nombre);
update siap2.ss_categoria_presupuesto_escolar_hist set cpe_nombre_busqueda=LOWER(cpe_nombre);


ALTER TABLE siap2.ss_ges_pres_es ADD COLUMN ges_nombre_busqueda varchar null;
ALTER TABLE siap2.ss_ges_pres_es_hist ADD COLUMN ges_nombre_busqueda varchar null;

update siap2.ss_ges_pres_es set ges_nombre_busqueda=LOWER(ges_nombre);
update siap2.ss_ges_pres_es_hist set ges_nombre_busqueda=LOWER(ges_nombre);

ALTER TABLE siap2.ss_fuente_financiamiento ADD COLUMN fue_nombre_busqueda varchar null;
ALTER TABLE siap2.ss_fuente_financiamiento_hist ADD COLUMN fue_nombre_busqueda varchar null;

update siap2.ss_fuente_financiamiento set fue_nombre_busqueda=LOWER(fue_nombre);
update siap2.ss_fuente_financiamiento_hist set fue_nombre_busqueda=LOWER(fue_nombre);

ALTER TABLE siap2.ss_fuente_recursos ADD COLUMN fue_nombre_busqueda varchar null;
ALTER TABLE siap2.ss_fuente_recursos_hist ADD COLUMN fue_nombre_busqueda varchar null;

update siap2.ss_fuente_recursos set fue_nombre_busqueda=LOWER(fue_nombre);
update siap2.ss_fuente_recursos_hist set fue_nombre_busqueda=LOWER(fue_nombre);


ALTER TABLE siap2.ss_cuentas ADD COLUMN cu_nombre_busqueda varchar null;
ALTER TABLE siap2.ss_cuentas_hist ADD COLUMN cu_nombre_busqueda varchar null;

update siap2.ss_cuentas set cu_nombre_busqueda=LOWER(cu_nombre);
update siap2.ss_cuentas_hist set cu_nombre_busqueda=LOWER(cu_nombre);

ALTER TABLE siap2.ss_rel_ges_pres_es_anio_fiscal ADD COLUMN descripcion_busqueda varchar null;
ALTER TABLE siap2.ss_rel_ges_pres_es_anio_fiscal_hist ADD COLUMN descripcion_busqueda varchar null;

update siap2.ss_rel_ges_pres_es_anio_fiscal set descripcion_busqueda=LOWER(descripcion);
update siap2.ss_rel_ges_pres_es_anio_fiscal_hist set descripcion_busqueda=LOWER(descripcion);


ALTER TABLE siap2.ss_fuente_financiamiento ADD COLUMN fue_codigo_busqueda varchar null;
ALTER TABLE siap2.ss_fuente_financiamiento_hist ADD COLUMN fue_codigo_busqueda varchar null;

update siap2.ss_fuente_financiamiento set fue_codigo_busqueda=LOWER(fue_codigo);
update siap2.ss_fuente_financiamiento_hist set fue_codigo_busqueda=LOWER(fue_codigo);

ALTER TABLE siap2.ss_fuente_financiamiento ADD CONSTRAINT codigo_fuente_uk UNIQUE (fue_codigo_busqueda);

ALTER TABLE siap2.ss_cuentas ADD COLUMN cu_codigo_busqueda varchar null;
ALTER TABLE siap2.ss_cuentas_hist ADD COLUMN cu_codigo_busqueda varchar null;
update siap2.ss_cuentas set cu_codigo_busqueda=LOWER(cu_codigo);
update siap2.ss_cuentas_hist set cu_codigo_busqueda=LOWER(cu_codigo);

ALTER TABLE siap2.ss_cuentas ADD CONSTRAINT codigo_cuentas_uk UNIQUE (cu_codigo_busqueda,cu_anio_fiscal);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERROR_CODIGO_REPETIDO_ANIO', true, now(), 'SCRIPT', 'admin','El código para el año del registro ingresado está duplicado', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.TotalTransferencias', true, now(), 'SCRIPT', 'admin','Total Transferencias', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.menu.TotalTransferencias', true, now(), 'SCRIPT', 'admin','Consulta total de transferencias', 0, 1);


--CONSULTA_TOTAL_PRESUPUESTOS
INSERT INTO siap2.ss_operacion
(ope_id, ope_codigo, ope_descripcion, ope_nombre, ope_origen, ope_tipocampo, ope_user_code, ope_version, ope_vigente, ope_categoria_id)
VALUES(NEXTVAL('siap2.seq_operacion'), 'CONSULTA_TOTAL_TRANSFERENCIAS', 'Consulta de total de tranferencias', 'Consulta de Total de transferencias', 'SIAP2', 'TIPO_UNICO', 0, NULL, true, NULL);

--6.21.13

INSERT INTO siap2.ss_configuraciones (cnf_id, cnf_codigo, cnf_descripcion, cnf_html, cnf_protegido, cnf_ult_mod, cnf_ult_origen, cnf_ult_usuario, cnf_valor, cnf_version) 
VALUES(nextval('siap2.seq_configuraciones'), 'REPORTE_TITULO_PEP_POA', 'PEP del POA "{1}"', false, false, NULL, NULL, NULL, 'PEP del POA', 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.CerrarPOA', true, now(), 'SCRIPT', 'admin','Cerrar POA', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_CIERRE_ANUAL_POA_POR_ACTIVIDADES_SIN_FINALIZAR', true, now(), 'SCRIPT', 'admin','El POA tiene actividades sin finalizar.', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_CIERRE_ANUAL_POA_POR_INSUMOS_PENDIENTES', true, now(), 'SCRIPT', 'admin','El POA tiene insumos en proceso.', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_CIERRE_ANUAL_ANIO_FISCAL_NO_FINALIZADO', true, now(), 'SCRIPT', 'admin','El año fiscal está finalizado.', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.Seguro_desea_cerrar_poa', true, now(), 'SCRIPT', 'admin','	¿Seguro desea cerrar el POA?.', 0, 1);


ALTER TABLE siap2.ss_po_insumo ADD COLUMN poi_habilitado boolean default true;
ALTER TABLE siap2.ss_po_insumo_hist ADD COLUMN poi_habilitado boolean default true;

--PERMISOS PARA CERRAR POA PROYECTO
INSERT INTO siap2.ss_operacion
(ope_id, ope_codigo, ope_descripcion, ope_nombre, ope_origen, ope_tipocampo, ope_user_code, ope_version, ope_vigente, ope_categoria_id)
VALUES(NEXTVAL('siap2.seq_operacion'), 'CERRAR_POA_PROYECTO_UNIDAD_RESPONSABLE', 'Cierra el POA del Proyecto de la unidad técnica responsable', 'Cierra el POA del Proyecto de la unidad técnica responsables', 'SIAP2', 'TIPO_UNICO', 0, NULL, true, NULL);

--6.21.18
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.CargarTransferencias', true, now(), 'SCRIPT', 'admin','Cargar Transferencias', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.EjecucionProceso', true, now(), 'SCRIPT', 'admin','Ejecución de Proceso', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.ProcesoTechosDesdeArchivoIniciado', true, now(), 'SCRIPT', 'admin',
'Ha iniciado la generación de los techos desde archivo. Verifique su bandeja de tareas para ver la ejecución del proceso.', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.ProcesoTransferenciasDesdeArchivoIniciado', true, now(), 'SCRIPT', 'admin',
'Ha iniciado la generación de transferencias desde archivo. Verifique su bandeja de tareas para ver la ejecución del proceso.', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'TipoNotificacion.EJECUCION_PROCESO_TRANSFERENCIAS_ARCHIVO', true, now(), 'SCRIPT', 'admin','Ejecución de proceso de generación de tranferencias desde archivo.', 0, 1);
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'TipoNotificacion.EJECUCION_PROCESO_TECHOS_ARCHIVO', true, now(), 'SCRIPT', 'admin','Ejecución de proceso de generación de techos desde archivo.', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'TipoNotificacion.EJECUCION_PROCESO_TECHOS_SISTEMA', true, now(), 'SCRIPT', 'admin','Ejecución de proceso de generación de techos desde sistema.', 0, 1);


INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.DeshacerTechos', true, now(), 'SCRIPT', 'admin','Deshacer techos', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.DeshacerAprobados', true, now(), 'SCRIPT', 'admin','Deshacer aprobados', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.MatriculasFormulacion', true, now(), 'SCRIPT', 'admin','Matriculas Formulación', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_NOMBRE_LARGO_255', true, now(), 'SCRIPT', 'admin','El nombre no debe superar los 255 caracteres.', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_USUARIO_VACIO', true, now(), 'SCRIPT', 'admin','Debe completar el usuario.', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_TIPO_CALCULO_VACIO', true, now(), 'SCRIPT', 'admin','Debe completar el tipo de ingreso de datos para el cálculo.', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_COMPONENTE_VACIO', true, now(), 'SCRIPT', 'admin','Debe completar el componente.', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_LINEA_PRESUPUESTARIA_VACIO', true, now(), 'SCRIPT', 'admin','Debe completar la línea presupuestaria.', 0, 1);

--6.21.20

ALTER TABLE siap2.ss_rel_ges_pres_es_anio_fiscal ADD COLUMN proceso_en_curso boolean default false;
ALTER TABLE siap2.ss_rel_ges_pres_es_anio_fiscal_hist ADD COLUMN proceso_en_curso boolean default false;

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_YA_EXISTE_PROCESO_PRESUPUESTO_EJECUTANDOSE', true, now(), 'SCRIPT', 'admin','Ya existe un proceso ejecutandose para el presupuesto "{0}"', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.DesbloquearProceso', true, now(), 'SCRIPT', 'admin','Desbloquear Proceso', 0, 1);

INSERT INTO siap2.ss_operacion
(ope_id, ope_codigo, ope_descripcion, ope_nombre, ope_origen, ope_tipocampo, ope_user_code, ope_version, ope_vigente, ope_categoria_id)
VALUES(NEXTVAL('siap2.seq_operacion'), 'DESBLOQUEAR_PROCESO_TECHOS_TRANFERENCIAS', 'Permiso para desbloquear procesos de ejecución de techos y tranferencias', 'Permiso para desbloquear procesos de ejecución de techos y tranferencias', 'SIAP2', 'TIPO_UNICO', 0, NULL, true, NULL);


INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_SEGUIMIENTO_ALCANCE_LIMITACIONES_VACIO', true, now(), 'SCRIPT', 'admin','Debe completar el alcance y limitaciones.', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_SEGUIMIENTO_MEDIO_VERIFICACION_VACIO', true, now(), 'SCRIPT', 'admin','Debe completar el medio de verificación.', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_PRESUPUESTO_FORMULADO', true, now(), 'SCRIPT', 'admin','El presupuesto se encuentra en formulación.', 0, 1);
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_PRESUPUESTO_CERRADO', true, now(), 'SCRIPT', 'admin','El presupuesto se ha cerrado.', 0, 1);

--6.21.21
--TABLA - DETALLE DE MATRICULAS PARA LOS TOPES

CREATE SEQUENCE IF NOT EXISTS siap2.seq_tope_detalle_matriculas INCREMENT 1 START 1 MINVALUE 1 no maxvalue CACHE 1 no cycle;

CREATE TABLE siap2.ss_tope_detalle_matriculas (
	tdm_id int8 NOT NULL,
	tdm_tope int8 NOT NULL,
	tdm_nivel int8 NULL,
	tdm_mod_educativa int8 NULL,
	tdm_mod_atencion int8 NULL,
	tdm_cant_matriculas int2,
	tdm_ult_mod timestamp NULL,
	tdm_ult_usuario varchar NULL,
	tdm_version int8 NULL,
	CONSTRAINT tope_det_matriculas_pk PRIMARY KEY (tdm_id),
	CONSTRAINT tdm_tope_fk FOREIGN KEY (tdm_tope) REFERENCES siap2.ss_tope_presupestal(tp_id)
);

CREATE TABLE siap2.ss_tope_detalle_matriculas_hist (
	tdm_id int8 NOT NULL,
	tdm_tope int8 NOT NULL,
	tdm_nivel int8 NULL,
	tdm_mod_educativa int8 NULL,
	tdm_mod_atencion int8 NULL,
	tdm_cant_matriculas int2,
	tdm_ult_mod timestamp NULL,
	tdm_ult_usuario varchar NULL,
	tdm_version int8 NULL,
	start_date timestamp NULL,
	end_date timestamp NULL
);

--6.21.22

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.DetalleTechoPresupuestal', true, now(), 'SCRIPT', 'admin','Detalle Techo Presupuestal', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.AdicionInsumoDesdeArchivo', true, now(), 'SCRIPT', 'admin','Adición de Insumos con archivo', 0, 1);


INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_DEBE_SELECIONAR_AREA_INVERSION', true, now(), 'SCRIPT', 'admin','Debe seleccionar una área de iversión.', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_DEBE_SELECIONAR_SUB_AREA_INVERSION', true, now(), 'SCRIPT', 'admin','Debe seleccionar una subárea de iversión.', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_CODIGO_INSUMO_INEXISTENTE', true, now(), 'SCRIPT', 'admin','Código de insumpo {0} inexistente.', 0, 1);


INSERT INTO siap2.ss_operacion
(ope_id, ope_codigo, ope_descripcion, ope_nombre, ope_origen, ope_tipocampo, ope_user_code, ope_version, ope_vigente, ope_categoria_id)
VALUES(NEXTVAL('siap2.seq_operacion'), 'AGREGAR_INSUMOS_SUBAREAS_DE_INVERSION', 'Permiso para agregar insumos a sub areas de inversión desde archivo', 'Permiso para agregar insumos a sub areas de inversión desde archivo', 'SIAP2', 'TIPO_UNICO', 0, NULL, true, NULL);


ALTER TABLE siap2.ss_tope_presupestal ALTER COLUMN tp_cant_matricula TYPE int4 USING tp_cant_matricula::int4;

ALTER TABLE siap2.ss_areas_inversion ADD COLUMN ai_nombre_busqueda varchar null;
ALTER TABLE siap2.ss_areas_inversion_hist ADD COLUMN ai_nombre_busqueda varchar null;
update siap2.ss_areas_inversion set ai_nombre_busqueda=LOWER(ai_nombre);
update siap2.ss_areas_inversion_hist set ai_nombre_busqueda=LOWER(ai_nombre);

--6.21.23
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.menu.ManualUsuario', true, now(), 'SCRIPT', 'admin','Manual de Usuario', 0, 1);

INSERT INTO siap2.ss_configuraciones (cnf_id, cnf_codigo, cnf_descripcion, cnf_html, cnf_protegido, cnf_ult_mod, cnf_ult_origen, cnf_ult_usuario, cnf_valor, cnf_version) 
VALUES(nextval('siap2.seq_configuraciones'), 'URL_MANUAL_USUARIO', 'URL de ubicación de manual de usuario', false, false, NULL, NULL, NULL, '', 1);


ALTER TABLE siap2.ss_notificaciones ALTER COLUMN not_asignado_a TYPE varchar USING not_asignado_a::varchar;
ALTER TABLE siap2.ss_notificaciones_hist ALTER COLUMN not_asignado_a TYPE varchar USING not_asignado_a::varchar;

--6.21.33
INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'ERR_NO_EXISTE_DIRECCION_DEPARTAMENTAl_SEDE', true, now(), 'SCRIPT', 'admin','No existe dirección departamental para la sede de código {0}.', 0, 1);

INSERT INTO siap2.ss_textos(tex_id, tex_codigo, tex_habilitado, tex_ult_mod, tex_ult_origen, tex_ult_usuario, tex_valor, tex_version, tex_idi_id)
VALUES(NEXTVAL('siap2.seq_textos'), 'labels.TipoCuentaBancaria', true, now(), 'SCRIPT', 'admin','Tipo de cuenta bancaria', 0, 1);

--6.21.34
ALTER TABLE siap2.ss_cuentas DROP CONSTRAINT ss_cuentas_cu_codigo_key;

INSERT INTO siap2.ss_operacion(ope_id, ope_codigo, ope_descripcion, ope_nombre, ope_origen, ope_tipocampo, ope_user_code, ope_version, ope_vigente, ope_categoria_id)
VALUES(NEXTVAL('siap2.seq_operacion'), 'GESTION_POA_UNIDADES_APOYO', 'Operación que permite visualizar las unidades técnicas.', 'Operación que permite visualizar las unidades técnicas.', 'SCRIPT', 0, 0, NULL, true, NULL);

