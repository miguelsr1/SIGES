--Identificacion
INSERT INTO catalogo.sg_tipos_identificacion("tin_codigo", "tin_nombre", "tin_nombre_busqueda", "tin_habilitado", "tin_ult_mod_fecha", "tin_ult_mod_usuario", "tin_version") VALUES ('1', 'Pasaporte', 'pasaporte', '1', CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_tipos_identificacion("tin_codigo", "tin_nombre", "tin_nombre_busqueda", "tin_habilitado", "tin_ult_mod_fecha", "tin_ult_mod_usuario", "tin_version") VALUES ('2', 'NIE', 'nie', '1', CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_tipos_identificacion("tin_codigo", "tin_nombre", "tin_nombre_busqueda", "tin_habilitado", "tin_ult_mod_fecha", "tin_ult_mod_usuario", "tin_version") VALUES ('3', 'CUN', 'cun', '1', CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_tipos_identificacion("tin_codigo", "tin_nombre", "tin_nombre_busqueda", "tin_habilitado", "tin_ult_mod_fecha", "tin_ult_mod_usuario", "tin_version") VALUES ('4', 'DUI', 'dui', '1', CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_tipos_identificacion_aud ("tin_pk","tin_codigo", "tin_nombre", "tin_nombre_busqueda", "tin_habilitado", "tin_ult_mod_fecha", "tin_ult_mod_usuario", "tin_version", "rev", "revtype") select tin_pk, tin_codigo, tin_nombre, tin_nombre_busqueda,tin_habilitado, tin_ult_mod_fecha, tin_ult_mod_usuario, tin_version, 1, 0 from catalogo.sg_tipos_identificacion;

--Sexo
INSERT INTO catalogo.sg_sexos("sex_codigo", "sex_nombre", "sex_nombre_busqueda", "sex_habilitado", "sex_ult_mod_fecha", "sex_ult_mod_usuario", "sex_version") VALUES ('1', 'Masculino', 'masculino', '1', CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_sexos("sex_codigo", "sex_nombre", "sex_nombre_busqueda", "sex_habilitado", "sex_ult_mod_fecha", "sex_ult_mod_usuario", "sex_version") VALUES ('2', 'Femenino', 'femenino', '1', CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_sexos("sex_codigo", "sex_nombre", "sex_nombre_busqueda", "sex_habilitado", "sex_ult_mod_fecha", "sex_ult_mod_usuario", "sex_version") VALUES ('3', 'Sin datos', 'sin datos', '1', CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_sexos_aud ("rev", "revtype", "sex_pk", "sex_codigo", "sex_nombre", "sex_nombre_busqueda", "sex_habilitado", "sex_ult_mod_fecha", "sex_ult_mod_usuario", "sex_version") select 1, 0, sex_pk, sex_codigo, sex_nombre, sex_nombre_busqueda, sex_habilitado, sex_ult_mod_fecha, sex_ult_mod_usuario, sex_version from catalogo.sg_sexos;

--Parentesco
INSERT INTO catalogo.sg_tipos_parentesco("tpa_nombre", "tpa_nombre_busqueda", "tpa_habilitado", "tpa_ult_mod_fecha", "tpa_ult_mod_usuario", "tpa_version") VALUES ('Padre', 'padre', '1', CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_tipos_parentesco("tpa_nombre", "tpa_nombre_busqueda", "tpa_habilitado", "tpa_ult_mod_fecha", "tpa_ult_mod_usuario", "tpa_version") VALUES ('Madre', 'madre', '1', CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_tipos_parentesco("tpa_nombre", "tpa_nombre_busqueda", "tpa_habilitado", "tpa_ult_mod_fecha", "tpa_ult_mod_usuario", "tpa_version") VALUES ('Abuelo', 'abuelo', '1', CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_tipos_parentesco("tpa_nombre", "tpa_nombre_busqueda", "tpa_habilitado", "tpa_ult_mod_fecha", "tpa_ult_mod_usuario", "tpa_version") VALUES ('Abuela', 'abuela', '1', CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_tipos_parentesco("tpa_nombre", "tpa_nombre_busqueda", "tpa_habilitado", "tpa_ult_mod_fecha", "tpa_ult_mod_usuario", "tpa_version") VALUES ('Tio', 'tio', '1', CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_tipos_parentesco("tpa_nombre", "tpa_nombre_busqueda", "tpa_habilitado", "tpa_ult_mod_fecha", "tpa_ult_mod_usuario", "tpa_version") VALUES ('Tia', 'tia', '1', CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_tipos_parentesco("tpa_nombre", "tpa_nombre_busqueda", "tpa_habilitado", "tpa_ult_mod_fecha", "tpa_ult_mod_usuario", "tpa_version") VALUES ('Hermano', 'hermano', '1', CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_tipos_parentesco("tpa_nombre", "tpa_nombre_busqueda", "tpa_habilitado", "tpa_ult_mod_fecha", "tpa_ult_mod_usuario", "tpa_version") VALUES ('Hermana', 'Hermana', '1', CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_tipos_parentesco("tpa_nombre", "tpa_nombre_busqueda", "tpa_habilitado", "tpa_ult_mod_fecha", "tpa_ult_mod_usuario", "tpa_version") VALUES ('Sin dato', 'sindato', '1', CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_tipos_parentesco_aud ("rev", "revtype", "tpa_pk", "tpa_codigo", "tpa_nombre", "tpa_nombre_busqueda", "tpa_habilitado", "tpa_ult_mod_fecha", "tpa_ult_mod_usuario", "tpa_version") select 1, 0, tpa_pk, tpa_codigo, tpa_nombre, tpa_nombre_busqueda, tpa_habilitado, tpa_ult_mod_fecha, tpa_ult_mod_usuario, tpa_version from catalogo.sg_tipos_parentesco;

--Telefono
INSERT INTO catalogo.sg_tipos_telefono("tto_nombre", "tto_nombre_busqueda", "tto_habilitado", "tto_ult_mod_fecha", "tto_ult_mod_usuario", "tto_version") VALUES ('Fijo', 'fijo', '1', CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_tipos_telefono("tto_nombre", "tto_nombre_busqueda", "tto_habilitado", "tto_ult_mod_fecha", "tto_ult_mod_usuario", "tto_version") VALUES ('Movil', 'movil', '1', CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_tipos_telefono("tto_nombre", "tto_nombre_busqueda", "tto_habilitado", "tto_ult_mod_fecha", "tto_ult_mod_usuario", "tto_version") VALUES ('Fax', 'fax', '1', CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_tipos_telefono_aud ("rev", "revtype", "tto_pk", "tto_codigo", "tto_nombre", "tto_nombre_busqueda", "tto_habilitado", "tto_ult_mod_fecha", "tto_ult_mod_usuario", "tto_version") select 1, 0, tto_pk, tto_codigo, tto_nombre, tto_nombre_busqueda, tto_habilitado, tto_ult_mod_fecha, tto_ult_mod_usuario, tto_version from catalogo.sg_tipos_telefono;

--Estado Civil
INSERT INTO catalogo.sg_estados_civil("eci_nombre", "eci_nombre_busqueda", "eci_habilitado", "eci_ult_mod_fecha", "eci_ult_mod_usuario", "eci_version") VALUES ('Soltero', 'soltero', '1', CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_estados_civil("eci_nombre", "eci_nombre_busqueda", "eci_habilitado", "eci_ult_mod_fecha", "eci_ult_mod_usuario", "eci_version") VALUES ('Casado', 'casado', '1', CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_estados_civil("eci_nombre", "eci_nombre_busqueda", "eci_habilitado", "eci_ult_mod_fecha", "eci_ult_mod_usuario", "eci_version") VALUES ('Viudo', 'viudo', '1', CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_estados_civil("eci_nombre", "eci_nombre_busqueda", "eci_habilitado", "eci_ult_mod_fecha", "eci_ult_mod_usuario", "eci_version") VALUES ('Divorciado', 'divorciado', '1', CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_estados_civil("eci_nombre", "eci_nombre_busqueda", "eci_habilitado", "eci_ult_mod_fecha", "eci_ult_mod_usuario", "eci_version") VALUES ('Comprometido', 'comprometido', '1', CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_estados_civil("eci_nombre", "eci_nombre_busqueda", "eci_habilitado", "eci_ult_mod_fecha", "eci_ult_mod_usuario", "eci_version") VALUES ('Sin Dato', 'sin dato', '1', CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_estados_civil_aud ("rev", "revtype", "eci_pk", "eci_codigo", "eci_nombre", "eci_nombre_busqueda", "eci_habilitado", "eci_ult_mod_fecha", "eci_ult_mod_usuario", "eci_version") select 1, 0, eci_pk, eci_codigo, eci_nombre, eci_nombre_busqueda, eci_habilitado, eci_ult_mod_fecha, eci_ult_mod_usuario, eci_version from catalogo.sg_estados_civil;

--Jornada Laboral
INSERT INTO catalogo.sg_jornadas_laborales("jla_nombre", "jla_nombre_busqueda", "jla_habilitado", "jla_ult_mod_fecha", "jla_ult_mod_usuario", "jla_version") VALUES ('Matutino', 'matutino', '1', CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_jornadas_laborales("jla_nombre", "jla_nombre_busqueda", "jla_habilitado", "jla_ult_mod_fecha", "jla_ult_mod_usuario", "jla_version") VALUES ('Vespertino', 'vespertino', '1', CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_jornadas_laborales("jla_nombre", "jla_nombre_busqueda", "jla_habilitado", "jla_ult_mod_fecha", "jla_ult_mod_usuario", "jla_version") VALUES ('Nocturno', 'nocturno', '1', CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_jornadas_laborales_aud ("rev", "revtype", "jla_pk", "jla_codigo", "jla_nombre", "jla_nombre_busqueda", "jla_habilitado", "jla_ult_mod_fecha", "jla_ult_mod_usuario", "jla_version") select 1, 0, jla_pk, jla_codigo, jla_nombre, jla_nombre_busqueda, jla_habilitado, jla_ult_mod_fecha, jla_ult_mod_usuario, jla_version from catalogo.sg_jornadas_laborales;

--Instituciones
INSERT INTO catalogo.sg_instituciones("ins_codigo", "ins_nombre", "ins_nombre_busqueda", "ins_habilitado", "ins_ult_mod_fecha", "ins_ult_mod_usuario", "ins_version") VALUES ('PU', 'PÚBLICA', 'publica', '1', CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_instituciones("ins_codigo", "ins_nombre", "ins_nombre_busqueda", "ins_habilitado", "ins_ult_mod_fecha", "ins_ult_mod_usuario", "ins_version") VALUES ('PR', 'PRIVADA', 'privada', '1', CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_instituciones_aud("rev", "revtype", "ins_pk", "ins_codigo", "ins_nombre", "ins_nombre_busqueda", "ins_habilitado", "ins_ult_mod_fecha", "ins_ult_mod_usuario", "ins_version") select 1, 0, ins_pk, ins_codigo, ins_nombre, ins_nombre_busqueda, ins_habilitado, ins_ult_mod_fecha, ins_ult_mod_usuario, ins_version from catalogo.sg_instituciones;

--Caserio
--------------------------------------------------------------------------------
-- No hay datos para esté catalogo 
--------------------------------------------------------------------------------

--Tipo sangre
INSERT INTO catalogo.sg_tipos_sangre("tsa_codigo", "tsa_nombre", "tsa_nombre_busqueda", "tsa_habilitado", "tsa_ult_mod_fecha", "tsa_ult_mod_usuario", "tsa_version") VALUES ('1', 'A Negativo','a negativo', '1', CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_tipos_sangre("tsa_codigo", "tsa_nombre", "tsa_nombre_busqueda", "tsa_habilitado", "tsa_ult_mod_fecha", "tsa_ult_mod_usuario", "tsa_version") VALUES ('2', 'A Positivo','a positivo', '1', CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_tipos_sangre("tsa_codigo", "tsa_nombre", "tsa_nombre_busqueda", "tsa_habilitado", "tsa_ult_mod_fecha", "tsa_ult_mod_usuario", "tsa_version") VALUES ('3', 'AB Negativo','ab negativo', '1', CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_tipos_sangre("tsa_codigo", "tsa_nombre", "tsa_nombre_busqueda", "tsa_habilitado", "tsa_ult_mod_fecha", "tsa_ult_mod_usuario", "tsa_version") VALUES ('4', 'AB Positivo','ab positivo', '1', CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_tipos_sangre("tsa_codigo", "tsa_nombre", "tsa_nombre_busqueda", "tsa_habilitado", "tsa_ult_mod_fecha", "tsa_ult_mod_usuario", "tsa_version") VALUES ('5', 'B Negativo','b negativo', '1', CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_tipos_sangre("tsa_codigo", "tsa_nombre", "tsa_nombre_busqueda", "tsa_habilitado", "tsa_ult_mod_fecha", "tsa_ult_mod_usuario", "tsa_version") VALUES ('6', 'B Positivo','b positivo', '1', CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_tipos_sangre("tsa_codigo", "tsa_nombre", "tsa_nombre_busqueda", "tsa_habilitado", "tsa_ult_mod_fecha", "tsa_ult_mod_usuario", "tsa_version") VALUES ('7', 'O Negativo','o negativo', '1', CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_tipos_sangre("tsa_codigo", "tsa_nombre", "tsa_nombre_busqueda", "tsa_habilitado", "tsa_ult_mod_fecha", "tsa_ult_mod_usuario", "tsa_version") VALUES ('8', 'O Positivo','o positivo', '1', CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_tipos_sangre_aud ("rev", "revtype", "tsa_pk", "tsa_codigo", "tsa_nombre","tsa_nombre_busqueda", "tsa_habilitado", "tsa_ult_mod_fecha", "tsa_ult_mod_usuario", "tsa_version") SELECT 1, 0, tsa_pk, tsa_codigo, tsa_nombre,tsa_nombre_busqueda, tsa_habilitado, tsa_ult_mod_fecha, tsa_ult_mod_usuario, tsa_version FROM catalogo.sg_tipos_sangre;

--Etiquetas
INSERT INTO catalogo.sg_etiquetas("eti_codigo", "eti_valor", "eti_habilitado", "eti_ult_mod_fecha", "eti_ult_mod_usuario", "eti_version") VALUES ('gestionInstitucion', 'Gestión de Instituciones', true, CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_etiquetas_aud ("rev", "revtype", "eti_pk", "eti_codigo", "eti_valor", "eti_habilitado", "eti_ult_mod_fecha", "eti_ult_mod_usuario", "eti_version") select 1, 0, "eti_pk", "eti_codigo", "eti_valor", "eti_habilitado", "eti_ult_mod_fecha", "eti_ult_mod_usuario", "eti_version" from catalogo.sg_etiquetas;


--SectorEconomico
INSERT INTO catalogo.sg_sectores_economicos("sec_codigo", "sec_habilitado", "sec_nombre", "sec_nombre_busqueda", "sec_ult_mod_fecha", "sec_ult_mod_usuario", "sec_version") VALUES ('1',true, 'Agroindustriales', 'agroindustriales', CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_sectores_economicos("sec_codigo", "sec_habilitado", "sec_nombre", "sec_nombre_busqueda", "sec_ult_mod_fecha", "sec_ult_mod_usuario", "sec_version") VALUES ('2',true, 'Turismo', 'turismo', CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_sectores_economicos("sec_codigo", "sec_habilitado", "sec_nombre", "sec_nombre_busqueda", "sec_ult_mod_fecha", "sec_ult_mod_usuario", "sec_version") VALUES ('3',true, 'Metal – Mecánica', 'metal - mecanica', CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_sectores_economicos("sec_codigo", "sec_habilitado", "sec_nombre", "sec_nombre_busqueda", "sec_ult_mod_fecha", "sec_ult_mod_usuario", "sec_version") VALUES ('4',true, 'Electricidad – Electrónica', 'electricidad – electronica', CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_sectores_economicos("sec_codigo", "sec_habilitado", "sec_nombre", "sec_nombre_busqueda", "sec_ult_mod_fecha", "sec_ult_mod_usuario", "sec_version") VALUES ('5',true, 'Administración y Comercio', 'administracion y comercio', CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_sectores_economicos("sec_codigo", "sec_habilitado", "sec_nombre", "sec_nombre_busqueda", "sec_ult_mod_fecha", "sec_ult_mod_usuario", "sec_version") VALUES ('6',true, 'Logística y Comercio Internacional', 'logistica y comercio internacional', CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_sectores_economicos("sec_codigo", "sec_habilitado", "sec_nombre", "sec_nombre_busqueda", "sec_ult_mod_fecha", "sec_ult_mod_usuario", "sec_version") VALUES ('7',true, 'Construcción', 'construccion', CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_sectores_economicos("sec_codigo", "sec_habilitado", "sec_nombre", "sec_nombre_busqueda", "sec_ult_mod_fecha", "sec_ult_mod_usuario", "sec_version") VALUES ('8',true, 'Salud', 'salud', CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_sectores_economicos("sec_codigo", "sec_habilitado", "sec_nombre", "sec_nombre_busqueda", "sec_ult_mod_fecha", "sec_ult_mod_usuario", "sec_version") VALUES ('9',true, 'Informática', 'informatica', CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_sectores_economicos("sec_codigo", "sec_habilitado", "sec_nombre", "sec_nombre_busqueda", "sec_ult_mod_fecha", "sec_ult_mod_usuario", "sec_version") VALUES ('10',true, 'Cultural', 'cultural', CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_sectores_economicos_aud ("rev", "revtype", "sec_pk", "sec_codigo", "sec_habilitado", "sec_nombre", "sec_nombre_busqueda", "sec_ult_mod_fecha", "sec_ult_mod_usuario", "sec_version") select 1, 0, "sec_pk", "sec_codigo", "sec_habilitado", "sec_nombre", "sec_nombre_busqueda", "sec_ult_mod_fecha", "sec_ult_mod_usuario", "sec_version" from catalogo.sg_sectores_economicos;

--ModalidadesAtencion
INSERT INTO catalogo.sg_modalidades_atencion("mat_codigo", "mat_nombre", "mat_nombre_busqueda", "mat_descripcion", "mat_habilitado",  "mat_ult_mod_fecha", "mat_ult_mod_usuario", "mat_version") VALUES ('1', 'REG', 'reg', 'Regular', true, CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_modalidades_atencion("mat_codigo", "mat_nombre", "mat_nombre_busqueda", "mat_descripcion", "mat_habilitado",  "mat_ult_mod_fecha", "mat_ult_mod_usuario", "mat_version") VALUES ('2', 'VFC', 'vfc', 'Vía Familiar Comunitaria', true, CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_modalidades_atencion("mat_codigo", "mat_nombre", "mat_nombre_busqueda", "mat_descripcion", "mat_habilitado",  "mat_ult_mod_fecha", "mat_ult_mod_usuario", "mat_version") VALUES ('3', 'MF', 'mf', ' Modalidades Flexibles', true, CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_modalidades_atencion("mat_codigo", "mat_nombre", "mat_nombre_busqueda", "mat_descripcion", "mat_habilitado",  "mat_ult_mod_fecha", "mat_ult_mod_usuario", "mat_version") VALUES ('4', 'NOCT', 'noct', 'Nocturna', true, CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_modalidades_atencion_aud ("rev", "revtype", "mat_pk", "mat_codigo", "mat_nombre", "mat_nombre_busqueda", "mat_descripcion", "mat_habilitado",  "mat_ult_mod_fecha", "mat_ult_mod_usuario", "mat_version") select 1, 0, "mat_pk", "mat_codigo", "mat_nombre", "mat_nombre_busqueda", "mat_descripcion", "mat_habilitado",  "mat_ult_mod_fecha", "mat_ult_mod_usuario", "mat_version" from catalogo.sg_modalidades_atencion;

--SubModalidades
INSERT INTO catalogo.sg_sub_modalidades("smo_codigo", "smo_modalidad_fk", "smo_nombre", "smo_nombre_busqueda", "smo_descripcion", "smo_habilitado",  "smo_ult_mod_fecha", "smo_ult_mod_usuario", "smo_version") VALUES ('1', '3', 'MFDIST', 'mfdist', 'Distancia', true, CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_sub_modalidades("smo_codigo", "smo_modalidad_fk", "smo_nombre", "smo_nombre_busqueda", "smo_descripcion", "smo_habilitado",  "smo_ult_mod_fecha", "smo_ult_mod_usuario", "smo_version") VALUES ('2', '3', 'MFSEMI', 'mfsemi', 'Semipresencial', true, CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_sub_modalidades("smo_codigo", "smo_modalidad_fk", "smo_nombre", "smo_nombre_busqueda", "smo_descripcion", "smo_habilitado",  "smo_ult_mod_fecha", "smo_ult_mod_usuario", "smo_version") VALUES ('3', '3', 'MFNOCT', 'mfnoct', 'Nocturna', true, CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_sub_modalidades("smo_codigo", "smo_modalidad_fk", "smo_nombre", "smo_nombre_busqueda", "smo_descripcion", "smo_habilitado",  "smo_ult_mod_fecha", "smo_ult_mod_usuario", "smo_version") VALUES ('4', '3', 'MFACEL', 'mfacel', 'Educación Acelerada', true, CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_sub_modalidades("smo_codigo", "smo_modalidad_fk", "smo_nombre", "smo_nombre_busqueda", "smo_descripcion", "smo_habilitado",  "smo_ult_mod_fecha", "smo_ult_mod_usuario", "smo_version") VALUES ('5', '3', 'MFVIRT', 'mfvirt', 'Virtual', true, CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_sub_modalidades("smo_codigo", "smo_modalidad_fk", "smo_nombre", "smo_nombre_busqueda", "smo_descripcion", "smo_habilitado",  "smo_ult_mod_fecha", "smo_ult_mod_usuario", "smo_version") VALUES ('6', '3', 'MFTNAC', 'mftnac', 'Tutorías para nivelación académica', true, CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_sub_modalidades("smo_codigo", "smo_modalidad_fk", "smo_nombre", "smo_nombre_busqueda", "smo_descripcion", "smo_habilitado",  "smo_ult_mod_fecha", "smo_ult_mod_usuario", "smo_version") VALUES ('7', '3', 'MFPS', 'mfps', 'Pruebas de suficiencia', true, CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_sub_modalidades_aud ("rev", "revtype", "smo_pk", "smo_codigo", "smo_modalidad_fk", "smo_nombre", "smo_nombre_busqueda", "smo_descripcion", "smo_habilitado",  "smo_ult_mod_fecha", "smo_ult_mod_usuario", "smo_version") select 1, 0, "smo_pk", "smo_codigo", "smo_modalidad_fk", "smo_nombre", "smo_nombre_busqueda", "smo_descripcion", "smo_habilitado",  "smo_ult_mod_fecha", "smo_ult_mod_usuario", "smo_version" from catalogo.sg_sub_modalidades;


--ProgramasEducativos
INSERT INTO catalogo.sg_programas_educativos("ped_codigo", "ped_habilitado", "ped_nombre", "ped_nombre_busqueda", "ped_descripcion", "ped_ult_mod_fecha", "ped_ult_mod_usuario", "ped_version") VALUES ('1', true, 'PILET', 'pilet', 'Programa de Integración Lineal de Estudios Técnicos', CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_programas_educativos("ped_codigo", "ped_habilitado", "ped_nombre", "ped_nombre_busqueda", "ped_descripcion", "ped_ult_mod_fecha", "ped_ult_mod_usuario", "ped_version") VALUES ('2', true, 'APREMAT', 'apremat', 'Apoyo al Proceso de Reforma de la Educación Media en el Área Técnica', CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_programas_educativos("ped_codigo", "ped_habilitado", "ped_nombre", "ped_nombre_busqueda", "ped_descripcion", "ped_ult_mod_fecha", "ped_ult_mod_usuario", "ped_version") VALUES ('3', true, 'MEGATEC', 'megatec', 'Modelo Educativo Gradual de Aprendizaje Técnico y Tecnológico', CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_programas_educativos_aud ("rev", "revtype", "ped_pk", "ped_codigo", "ped_habilitado", "ped_nombre", "ped_nombre_busqueda", "ped_descripcion", "ped_ult_mod_fecha", "ped_ult_mod_usuario", "ped_version") select 1, 0, "ped_pk", "ped_codigo", "ped_habilitado", "ped_nombre", "ped_nombre_busqueda", "ped_descripcion", "ped_ult_mod_fecha", "ped_ult_mod_usuario", "ped_version" from catalogo.sg_programas_educativos;

--Clasificaciones
INSERT INTO catalogo.sg_clasificaciones("cla_codigo", "cla_habilitado", "cla_nombre", "cla_nombre_busqueda",  "cla_ult_mod_fecha", "cla_ult_mod_usuario", "cla_version") VALUES ('01', true, 'Escuela de Educación Parvularia', 'escuela de educacion parvularia',  CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_clasificaciones("cla_codigo", "cla_habilitado", "cla_nombre", "cla_nombre_busqueda",  "cla_ult_mod_fecha", "cla_ult_mod_usuario", "cla_version") VALUES ('02', true, 'Centro Escolar', 'centro escolar',  CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_clasificaciones("cla_codigo", "cla_habilitado", "cla_nombre", "cla_nombre_busqueda",  "cla_ult_mod_fecha", "cla_ult_mod_usuario", "cla_version") VALUES ('03', true, 'Instituto Nacional', 'instituto nacional',  CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_clasificaciones("cla_codigo", "cla_habilitado", "cla_nombre", "cla_nombre_busqueda",  "cla_ult_mod_fecha", "cla_ult_mod_usuario", "cla_version") VALUES ('04', true, 'Escuela de Educación Especial', 'escuela de educacion especial',  CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_clasificaciones("cla_codigo", "cla_habilitado", "cla_nombre", "cla_nombre_busqueda",  "cla_ult_mod_fecha", "cla_ult_mod_usuario", "cla_version") VALUES ('05', true, 'Complejo Educativo', 'complejo educativo',  CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_clasificaciones_aud ("rev", "revtype", "cla_pk", "cla_codigo", "cla_nombre", "cla_nombre_busqueda", "cla_habilitado", "cla_ult_mod_fecha", "cla_ult_mod_usuario", "cla_version") select 1, 0, cla_pk, cla_codigo, cla_nombre, cla_nombre_busqueda, cla_habilitado, cla_ult_mod_fecha, cla_ult_mod_usuario, cla_version from catalogo.sg_clasificaciones;

--Escala Calificacion
INSERT INTO catalogo.sg_escalas_calificacion(eca_codigo, eca_habilitado, eca_nombre, eca_nombre_busqueda, eca_tipo_escala, eca_valor_minimo, eca_ult_mod_fecha, eca_ult_mod_usuario, eca_version)VALUES('1', true, 'eca1', 'eca1', 'NUMERICA', null, '2018-08-20 15:42:45', 'admin', 0);

--Calificacion
INSERT INTO catalogo.sg_calificaciones(cal_codigo, cal_escala_fk, cal_valor, cal_orden, cal_habilitado, cal_ult_mod_fecha, cal_ult_mod_usuario, cal_version)VALUES('1', 1, '1', 1, true, CURRENT_TIMESTAMP, 'admin', 0);
INSERT INTO catalogo.sg_calificaciones(cal_codigo, cal_escala_fk, cal_valor, cal_orden, cal_habilitado, cal_ult_mod_fecha, cal_ult_mod_usuario, cal_version)VALUES('2', 1, '2', 2, true, CURRENT_TIMESTAMP, 'admin', 0);
INSERT INTO catalogo.sg_calificaciones(cal_codigo, cal_escala_fk, cal_valor, cal_orden, cal_habilitado, cal_ult_mod_fecha, cal_ult_mod_usuario, cal_version)VALUES('3', 1, '3', 3, true, CURRENT_TIMESTAMP, 'admin', 0);
INSERT INTO catalogo.sg_calificaciones(cal_codigo, cal_escala_fk, cal_valor, cal_orden, cal_habilitado, cal_ult_mod_fecha, cal_ult_mod_usuario, cal_version)VALUES('4', 1, '4', 4, true, CURRENT_TIMESTAMP, 'admin', 0);
INSERT INTO catalogo.sg_calificaciones(cal_codigo, cal_escala_fk, cal_valor, cal_orden, cal_habilitado, cal_ult_mod_fecha, cal_ult_mod_usuario, cal_version)VALUES('5', 1, '5', 5, true, CURRENT_TIMESTAMP, 'admin', 0);


INSERT INTO catalogo.sg_configuraciones("con_codigo", "con_nombre", "con_valor", "con_nombre_busqueda", "con_ult_mod_fecha", "con_ult_mod_usuario", "con_version") VALUES ('TERMINOS_CONDICIONES', 'Términos y condiciones', '[Términos y condiciones]', 'terminos y condiciones', CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_configuraciones_aud ("con_pk","con_codigo", "con_nombre", "con_valor", "con_nombre_busqueda", "con_ult_mod_fecha", "con_ult_mod_usuario", "con_version", "rev", "revtype") select con_pk, con_codigo, con_nombre, con_valor, con_nombre_busqueda, con_ult_mod_fecha, con_ult_mod_usuario, con_version, 1, 0 from catalogo.sg_configuraciones;

INSERT INTO catalogo.sg_plantilla("pla_codigo", "pla_nombre", "pla_nombre_busqueda", "pla_habilitado", "pla_ult_mod_fecha", "pla_ult_mod_usuario", "pla_version") VALUES ('TITULO_ESTUDIANTE', 'Título estudiante', 'titulo estudiante', true, CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_plantilla("pla_codigo", "pla_nombre", "pla_nombre_busqueda", "pla_habilitado", "pla_ult_mod_fecha", "pla_ult_mod_usuario", "pla_version") VALUES ('CARNET_ESTUDIANTE', 'Carnet estudiante', 'carnet estudiante', true, CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_plantilla_aud ("pla_pk", "pla_codigo", "pla_nombre", "pla_nombre_busqueda",  "pla_habilitado", "pla_ult_mod_fecha", "pla_ult_mod_usuario", "pla_version", "rev", "revtype") select pla_pk, pla_codigo, pla_nombre, pla_nombre_busqueda, pla_habilitado, pla_ult_mod_fecha, pla_ult_mod_usuario, pla_version, 1, 0 from catalogo.sg_plantilla;