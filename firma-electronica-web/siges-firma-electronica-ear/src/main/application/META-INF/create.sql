CREATE SCHEMA pfea;

--
-- TOC entry 190 (class 1259 OID 24717)
-- Name: seq_configuraciones; Type: SEQUENCE; Schema: pfea; Owner: postgres
--

CREATE SEQUENCE pfea.seq_configuraciones
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 194 (class 1259 OID 32786)
-- Name: seq_documentos; Type: SEQUENCE; Schema: pfea; Owner: postgres
--

CREATE SEQUENCE pfea.seq_documentos
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 191 (class 1259 OID 24723)
-- Name: seq_textos; Type: SEQUENCE; Schema: pfea; Owner: postgres
--

CREATE SEQUENCE pfea.seq_textos
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



SET default_tablespace = '';

--
-- TOC entry 188 (class 1259 OID 24663)
-- Name: ss_configuraciones; Type: TABLE; Schema: pfea; Owner: postgres
--

CREATE TABLE pfea.ss_configuraciones (
    cnf_id integer NOT NULL,
    cnf_codigo character varying(255) NOT NULL,
    cnf_descripcion character varying(255) NOT NULL,
    cnf_valor character varying(4000),
    est_ultima_modificacion date,
    est_ultimo_usuario character varying(255),
    est_version integer
);



--
-- TOC entry 186 (class 1259 OID 24592)
-- Name: ss_configuraciones_aud; Type: TABLE; Schema: pfea; Owner: postgres
--

CREATE TABLE pfea.ss_configuraciones_aud (
    cnf_id integer NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    cnf_codigo character varying(255),
    cnf_descripcion character varying(255),
    cnf_valor character varying(4000),
    est_ultima_modificacion date,
    est_ultimo_usuario character varying(255),
    est_version integer
);


--
-- TOC entry 193 (class 1259 OID 32776)
-- Name: ss_documentos; Type: TABLE; Schema: pfea; Owner: postgres
--

CREATE TABLE pfea.ss_documentos (
    doc_id integer NOT NULL,
    doc_descripcion character varying(255),
    doc_version integer,
    doc_documentotipo character varying(255),
    doc_eliminado boolean NOT NULL,
    doc_estado_documento character varying(255),
    doc_fecha_creacion timestamp without time zone,
    doc_fecha_eliminacion timestamp without time zone,
    doc_fecha_expiracion timestamp without time zone,
    doc_firmado boolean NOT NULL,
    doc_identificador_archivo character varying(255),
    doc_motivo character varying(255),
    doc_nombre_archivo character varying(255),
    doc_nombre_original character varying(255),
    doc_token character varying(255),
    doc_ultima_modificacion timestamp without time zone,
    doc_ultimo_usuario character varying(255),
    doc_origen character varying(255),
    doc_copiade character varying(255),
    doc_identificador character varying(255),
    doc_retorno character varying(1000),
    doc_respuesta character varying(1000),
    doc_codigopac character varying(5),
    doc_nubeabitab_pid character varying(50),
    doc_ultima_accion character varying(4000),
    doc_retorno_navegacion character varying(1000),
    doc_respuesta_error boolean DEFAULT false
);


--
-- TOC entry 192 (class 1259 OID 32768)
-- Name: ss_documentos_aud; Type: TABLE; Schema: pfea; Owner: postgres
--

CREATE TABLE pfea.ss_documentos_aud (
    doc_id integer NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    doc_descripcion character varying(255),
    doc_version integer,
    doc_documentotipo character varying(255),
    doc_eliminado boolean,
    doc_estado_documento character varying(255),
    doc_fecha_creacion timestamp without time zone,
    doc_fecha_eliminacion timestamp without time zone,
    doc_fecha_expiracion timestamp without time zone,
    doc_firmado boolean,
    doc_identificador_archivo character varying(255),
    doc_motivo character varying(255),
    doc_nombre_archivo character varying(255),
    doc_nombre_original character varying(255),
    doc_token character varying(255),
    doc_ultima_modificacion timestamp without time zone,
    doc_ultimo_usuario character varying(255),
    doc_origen character varying(255),
    doc_copiade character varying(255),
    doc_identificador character varying(255),
    doc_retorno character varying(1000),
    doc_respuesta character varying(1000),
    doc_codigopac character varying(5),
    doc_nubeabitab_pid character varying(50),
    doc_ultima_accion character varying(4000),
    doc_retorno_navegacion character varying(1000),
    doc_respuesta_error boolean DEFAULT false
);


--
-- TOC entry 189 (class 1259 OID 24693)
-- Name: ss_textos; Type: TABLE; Schema: pfea; Owner: postgres
--

CREATE TABLE pfea.ss_textos (
    tex_id integer NOT NULL,
    tex_codigo character varying(255) NOT NULL,
    tex_valor character varying(4000),
    est_ultima_modificacion date,
    est_ultimo_usuario character varying(255),
    est_version integer
);


--
-- TOC entry 187 (class 1259 OID 24626)
-- Name: ss_textos_aud; Type: TABLE; Schema: pfea; Owner: postgres
--

CREATE TABLE pfea.ss_textos_aud (
    tex_id integer NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    tex_codigo character varying(255),
    tex_valor character varying(4000),
    est_ultima_modificacion date,
    est_ultimo_usuario character varying(255),
    est_version integer
);


--
-- TOC entry 2039 (class 2606 OID 24599)
-- Name: ss_configuraciones_aud aud_ss_configuraciones_pkey; Type: CONSTRAINT; Schema: pfea; Owner: postgres
--

ALTER TABLE ONLY pfea.ss_configuraciones_aud
    ADD CONSTRAINT aud_ss_configuraciones_pkey PRIMARY KEY (cnf_id, rev);


--
-- TOC entry 2051 (class 2606 OID 32775)
-- Name: ss_documentos_aud aud_ss_docs_bandeja_pkey; Type: CONSTRAINT; Schema: pfea; Owner: postgres
--

ALTER TABLE ONLY pfea.ss_documentos_aud
    ADD CONSTRAINT aud_ss_docs_bandeja_pkey PRIMARY KEY (doc_id, rev);


--
-- TOC entry 2041 (class 2606 OID 24633)
-- Name: ss_textos_aud aud_ss_textos_pkey; Type: CONSTRAINT; Schema: pfea; Owner: postgres
--

ALTER TABLE ONLY pfea.ss_textos_aud
    ADD CONSTRAINT aud_ss_textos_pkey PRIMARY KEY (tex_id, rev);


--
-- TOC entry 2043 (class 2606 OID 73729)
-- Name: ss_configuraciones ss_configuraciones_codigo_uk; Type: CONSTRAINT; Schema: pfea; Owner: postgres
--

ALTER TABLE ONLY pfea.ss_configuraciones
    ADD CONSTRAINT ss_configuraciones_codigo_uk UNIQUE (cnf_codigo);


--
-- TOC entry 2045 (class 2606 OID 24670)
-- Name: ss_configuraciones ss_configuraciones_pkey; Type: CONSTRAINT; Schema: pfea; Owner: postgres
--

ALTER TABLE ONLY pfea.ss_configuraciones
    ADD CONSTRAINT ss_configuraciones_pkey PRIMARY KEY (cnf_id);


--
-- TOC entry 2053 (class 2606 OID 32783)
-- Name: ss_documentos ss_docs_bandeja_pkey; Type: CONSTRAINT; Schema: pfea; Owner: postgres
--

ALTER TABLE ONLY pfea.ss_documentos
    ADD CONSTRAINT ss_docs_bandeja_pkey PRIMARY KEY (doc_id);


--
-- TOC entry 2047 (class 2606 OID 57347)
-- Name: ss_textos ss_textos_codigo_uk; Type: CONSTRAINT; Schema: pfea; Owner: postgres
--

ALTER TABLE ONLY pfea.ss_textos
    ADD CONSTRAINT ss_textos_codigo_uk UNIQUE (tex_codigo);


--
-- TOC entry 2049 (class 2606 OID 24700)
-- Name: ss_textos ss_textos_pkey; Type: CONSTRAINT; Schema: pfea; Owner: postgres
--

ALTER TABLE ONLY pfea.ss_textos
    ADD CONSTRAINT ss_textos_pkey PRIMARY KEY (tex_id);


--
-- TOC entry 2055 (class 2606 OID 32785)
-- Name: ss_documentos uk_9vat9swr7u2ftl90dqbkr4cei; Type: CONSTRAINT; Schema: pfea; Owner: postgres
--

ALTER TABLE ONLY pfea.ss_documentos
    ADD CONSTRAINT uk_9vat9swr7u2ftl90dqbkr4cei UNIQUE (doc_identificador_archivo);


--
-- TOC entry 2057 (class 2606 OID 65537)
-- Name: ss_documentos uk_doc_identificador; Type: CONSTRAINT; Schema: pfea; Owner: postgres
--

ALTER TABLE ONLY pfea.ss_documentos
    ADD CONSTRAINT uk_doc_identificador UNIQUE (doc_identificador);


--
-- TOC entry 2059 (class 2606 OID 32794)
-- Name: ss_documentos uk_doc_identificador_archivo; Type: CONSTRAINT; Schema: pfea; Owner: postgres
--

ALTER TABLE ONLY pfea.ss_documentos
    ADD CONSTRAINT uk_doc_identificador_archivo UNIQUE (doc_identificador_archivo);


-- Completed on 2020-02-21 11:44:03 -03

--
-- PostgreSQL database dump complete
--

INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (1, 'labels.acceder', 'Acceder', '2018-11-05', 'MVDLIBRE', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (2, 'labels.menu.Inicio', 'Inicio', '2018-11-05', 'MVDLIBRE', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (6, 'messages.credencialesIncorrectas', 'Credenciales incorrectas', '2018-11-05', 'MVDLIBRE', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (8, 'labels.salir', 'Salir', '2018-11-05', 'uy-ci-90000018', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (13, 'labels.todos', 'Todos', '2018-11-05', 'uy-ci-90000018', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (14, 'labels.consultar', 'Consultar', '2018-11-05', 'uy-ci-90000018', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (19, 'labels.eliminar', 'Eliminar', '2018-11-05', 'uy-ci-90000018', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (20, 'labels.seguroQueDeseaEliminar', 'Confirma la eliminación', '2018-11-05', 'uy-ci-90000018', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (21, 'labels.confirmar', 'Confirmar', '2018-11-05', 'uy-ci-90000018', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (22, 'labels.cancelar', 'Cancelar', '2018-11-05', 'uy-ci-90000018', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (23, 'labels.firmaPublica', 'Firma pública', '2018-12-05', 'MVDLIBRE', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (24, 'labels.adjuntarNuevoArchivoABandeja', 'Subir nuevo archivo', '2018-12-05', 'MVDLIBRE', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (25, 'labels.documento', 'Documento', '2018-12-05', 'MVDLIBRE', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (26, 'labels.buscarArchivo', 'Buscar archivo', '2018-12-05', 'MVDLIBRE', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (27, 'messages.archivoDemasiadoGrande', 'El archivo es demasiado grande', '2018-12-05', 'MVDLIBRE', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (28, 'labels.guardarArchivosi', 'Guardar achivo', '2018-12-05', 'MVDLIBRE', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (41, 'labels.visualizar', 'Visualizar', '2018-12-05', 'uy-ci-90000018', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (42, 'labels.enviar', 'Enviar', '2018-12-05', 'uy-ci-90000018', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (43, 'messages.aunNoTieneDocumentosAsociados', 'No hay documentos asociados', '2018-12-05', 'uy-ci-90000018', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (44, 'labels.menu.BandejaArchivosExpirados', 'Documentos expirados', '2018-12-05', 'uy-ci-90000018', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (45, 'labels.expiro', 'Expiró', '2018-12-05', 'uy-ci-90000018', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (46, 'labels.motivo', 'Motivo', '2018-12-05', 'uy-ci-90000018', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (47, 'labels.regresar', 'Regresar', '2018-12-05', 'uy-ci-90000018', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (48, 'labels.cancelarEliminarDocumento', 'Cancelar eliminar documento', '2018-12-05', 'uy-ci-90000018', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (49, 'labels.losCamposIndicadosSonRequeridos', 'Los campos indicados con * son requeridos', '2018-12-05', 'uy-ci-90000018', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (50, 'labels.adjuntarDocs', 'Adjuntar documentos', '2018-12-05', 'uy-ci-90000018', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (51, 'labels.descripcionDeDocumento', 'Descripción', '2018-12-05', 'uy-ci-90000018', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (53, 'labels.agregar', 'Añadir', '2018-12-05', 'uy-ci-90000018', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (54, 'labels.nombreOriginal', 'Nombre original', '2018-12-05', 'uy-ci-90000018', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (55, 'labels.descripcion', 'Descripción', '2018-12-05', 'uy-ci-90000018', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (56, 'labels.firmar', 'Firmar', '2018-12-05', 'uy-ci-90000018', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (57, 'labels.descargar', 'Descargar', '2018-12-05', 'uy-ci-90000018', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (29, 'labels.cancelarArchivo', 'Cancelar archivo', '2018-12-05', 'MVDLIBRE', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (30, 'messages.aunNoSeHanCreadoArchivos', 'No se han cargado archivos', '2018-12-05', 'uy-ci-90000018', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (31, 'messages.noExistenElementosParaLosCriteriosUtilizados', 'No hay elementos para mostrar', '2018-12-05', 'uy-ci-90000018', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (32, 'labels.menu.BandejaArchivos', 'Bandeja de archivos', '2018-12-05', 'uy-ci-90000018', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (33, 'labels.nuevo', 'Nuevo', '2018-12-05', 'uy-ci-90000018', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (34, 'labels.nombre', 'Nombre', '2018-12-05', 'uy-ci-90000018', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (35, 'labels.fechaDeCreacion', 'Fecha de creación', '2018-12-05', 'uy-ci-90000018', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (36, 'labels.fechaDeExpiracion', 'Fecha de expiración', '2018-12-05', 'uy-ci-90000018', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (37, 'labels.fechaDeEliminacion', 'Fecha de eliminación', '2018-12-05', 'uy-ci-90000018', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (38, 'labels.fechaDeCancelacion', 'Fecha de cancelación', '2018-12-05', 'uy-ci-90000018', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (39, 'labels.estado', 'Estado', '2018-12-05', 'uy-ci-90000018', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (40, 'labels.descarga', 'Descarga', '2018-12-05', 'uy-ci-90000018', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (66, 'label.firmaDeDocumento', 'Firma de documento', '2018-12-12', 'uy-ci-33286742', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (68, 'labels.DescargaDeArchivoAgesic', 'Descargar archivo para firmar', '2018-12-12', 'uy-ci-33286742', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (78, 'labels.Descargado', 'Descargado', '2018-12-14', 'MVDLIBRE', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (70, 'labels.DescargarAplicativo', 'Descargar aplicativo de firma', '2018-12-12', 'uy-ci-33286742', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (67, 'labels.Instalacion', 'Instalación', '2018-12-12', 'uy-ci-33286742', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (71, 'labels.SubirArchivo', 'Subir archivo', '2018-12-14', 'MVDLIBRE', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (69, 'labels.Verificacion', 'Verificación', '2018-12-12', 'uy-ci-33286742', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (76, 'labels.aceptar', 'Aceptar', '2018-12-14', 'MVDLIBRE', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (91, 'labels.agesicDownloadFile', 'Descargar archivo', '2018-12-14', 'uy-ci-33286742', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (87, 'labels.apellidos', 'Apellidos', '2018-12-14', 'uy-ci-33286742', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (64, 'labels.atras', 'Atrás', '2018-12-12', 'MVDLIBRE', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (59, 'labels.banner.agesic', 'Agesic', '2018-12-06', 'MVDLIBRE', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (88, 'labels.buscar', 'Buscar', '2018-12-14', 'uy-ci-33286742', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (62, 'labels.completarFormulario', 'Completar formulario', '2018-12-12', 'MVDLIBRE', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (63, 'labels.confirmarSolicitud', 'Confirmar solicitud', '2018-12-12', 'MVDLIBRE', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (84, 'labels.docPaisEmisor', 'País emisor', '2018-12-14', 'uy-ci-33286742', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (73, 'labels.esperePorFavor', 'Espere por favor...', '2018-12-14', 'MVDLIBRE', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (72, 'labels.headerModal.Procesando', 'Procesando...', '2018-12-14', 'MVDLIBRE', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (74, 'labels.headerModal.cargaArchivo', 'Cargar archivo', '2018-12-14', 'MVDLIBRE', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (80, 'labels.headerModal.enviarArchivo', 'Enviar archivo', '2018-12-14', 'uy-ci-33286742', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (89, 'labels.limpiar', 'Limpiar', '2018-12-14', 'uy-ci-33286742', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (75, 'labels.messages.archivoSubido', 'Archivo subido', '2018-12-14', 'MVDLIBRE', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (90, 'labels.noHayResultados', 'No hay resultados', '2018-12-14', 'uy-ci-33286742', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (86, 'labels.nombres', 'Nombres', '2018-12-14', 'uy-ci-33286742', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (85, 'labels.nroDoc', 'Número de documento', '2018-12-14', 'uy-ci-33286742', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (81, 'labels.seleccionarUsuarioDestino', 'Seleccionar usuario receptor', '2018-12-14', 'uy-ci-33286742', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (65, 'labels.siguiente', 'Siguiente', '2018-12-12', 'MVDLIBRE', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (79, 'labels.softwareDownloadLink', 'Enlace de descarga del aplicativo', '2018-12-14', 'uy-ci-33286742', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (83, 'labels.tipoDocumento', 'Tipo de documento', '2018-12-14', 'uy-ci-33286742', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (77, 'labels.agesicDownloadLink', 'Descargar archivo para firmar', '2018-12-14', 'MVDLIBRE', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (95, 'labels.descargadoEInstalado', 'Ya lo tengo descargado e instalado', '2018-12-14', 'uy-ci-33286742', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (102, 'labels.archivoFirmadoRecibidoCorrectamente', 'El documento firmado ha sido recibido correctamente', '2018-12-14', 'uy-ci-33286742', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (101, 'labels.confirmacion', 'Confirmación', '2018-12-14', 'uy-ci-33286742', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (100, 'messages.debeFirmarElDocumentoParaContinuar', 'Debe firmar el documento con el aplicativo para poder continuar', '2018-12-14', 'uy-ci-33286742', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (99, 'labels.FirmarArchivoConAplicativo', 'Firmar el archivo con el aplicativo', '2018-12-14', 'uy-ci-33286742', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (98, 'labels.firmeElDocumentoConElAplicativo', 'Firme el documento con el aplicativo', '2018-12-14', 'uy-ci-33286742', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (97, 'labels.esperando', 'Esperando el documento firmado', '2018-12-14', 'uy-ci-33286742', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (96, 'messages.debeDescargarEInstalarElAplicativoDeFirma', 'Debe descargar e instalar el aplicativo de firma para poder continuar', '2018-12-14', 'uy-ci-33286742', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (109, 'labels.ArchivoDisco', 'Archivo en disco', '2018-12-17', 'uy-ci-33286742', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (105, 'labels.CedulaIdentidadElectronica', 'Cédula de identidad electrónica', '2018-12-17', 'uy-ci-33286742', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (104, 'labels.SeleccioneElMetodoDeFirma', 'Seleccione el método de firma', '2018-12-17', 'uy-ci-33286742', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (103, 'labels.MetodoDefirma', 'Método de firma', '2018-12-17', 'uy-ci-33286742', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (117, 'labels.Firma', 'Firma', '2018-12-17', 'uy-ci-33286742', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (116, 'labels.Archivo', 'Archivo', '2018-12-17', 'uy-ci-33286742', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (114, 'labels.Metodo', 'Método', '2018-12-17', 'uy-ci-33286742', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (118, 'labels.Confirmacion', 'Confirmación', '2018-12-17', 'uy-ci-33286742', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (123, 'messages.metodoFirmaDescripcionArchivo', 'Esta opción le permite firmar con un certificado digital almacenado <strong>localizado en su equipo</strong>.', '2018-12-17', 'uy-ci-33286742', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (110, 'messages.debeSeleccionarUnMetodoDeFirma', 'Debe seleccionar un método de firma', '2018-12-17', 'uy-ci-33286742', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (115, 'labels.Aplicativo', 'Aplicativo', '2018-12-17', 'uy-ci-33286742', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (113, 'labels.cerrar', 'Cerrar', '2018-12-17', 'uy-ci-33286742', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (112, 'messages.debeFirmarElDocumentoConElAplicativoDeFirma', 'Debe firmar el documento con el aplicativo', '2018-12-17', 'uy-ci-33286742', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (119, 'messages.metodoFirmaDescripcionGeneral', 'Seleccione un método de firma de los listados a la izquierda.<br/>Al seleccionar un método aquí podrá ver una descripción del mismo.', '2018-12-17', 'uy-ci-33286742', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (127, 'labels.SubiryFirmar', 'Subir documento y firmarlo', '2018-12-18', 'PFEA', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (131, 'labels.origen', 'Origen', '2018-12-20', 'uy-ci-33286742', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (132, 'labels.exportarTabla', 'Exportar tabla', '2018-12-20', 'uy-ci-33286742', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (135, 'labels.filtrar', 'Filtrar', '2018-12-20', 'uy-ci-33286742', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (136, 'labels.EnviarArchivo', 'Enviar archivo', '2018-12-20', 'uy-ci-33286742', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (137, 'tabla.exportar.labels.Archivos', 'Archivos', '2018-12-20', 'uy-ci-33286742', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (138, 'labels.menu.BandejaArchivosPublicosTitle', 'Bandeja de archivos públicos', '2018-12-21', 'PFEA', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (139, 'labels.menu.BandejaArchivosPublicos', 'Bandeja de archivos públicos', '2018-12-21', 'PFEA', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (171, 'labels.headerModal.TrasferirArchivos', 'Transferir archivos', '2018-12-31', 'uy-ci-33286742', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (172, 'labels.messages.archivoEnviadoOK', 'Archivo enviado correctamente', '2018-12-31', 'uy-ci-33286742', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (176, 'messages.confirmaQueDeseaEliminarElDocumento', 'Confirma que desea eliminar el documento', '2019-01-07', 'uy-ci-33286742', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (177, 'messages.debeIndicarLasTresPartesDelDocumento', 'Debe indicar las tres partes del documento del receptor', '2019-01-07', 'uy-ci-33286742', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (178, 'messages.debeIdentificarAlReceptorDelDocumento', 'Debe identificar al receptor del documento', '2019-01-07', 'uy-ci-33286742', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (209, 'messages.usuarioNoEncontrado', 'Usuario no encontrado', '2019-01-07', 'uy-ci-33286742', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (210, 'messages.noSeEncuentraUnUsuarioConElDocumentoIngresado', 'No se encuentra un usuario con el documento ingresado; sin embargo, puede que el ciudadano no se haya registrado en la Plataforma de Firma Electrónica aún. Si está seguro que los datos ingresados son correctos puede confirmar el envío del documento.', '2019-01-07', 'uy-ci-33286742', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (211, 'messages.usuarioEncontrado', 'Usuario encontrado', '2019-01-07', 'uy-ci-33286742', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (212, 'messages.seHaEncontradoUnUsuarioConElDocumentoIngresado', 'El documento ingresado corresponde a {nombre}. Si considera que esto es correcto puede confirmar el envío del documento.', '2019-01-07', 'uy-ci-33286742', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (244, 'labels.signedDownloadLink', 'Descargar el documento firmado', '2019-01-10', 'PFEA', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (245, 'labels.firmarNuevo', 'Firmar otro documento', '2019-01-10', 'PFEA', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (246, 'labels.firmarNuevoArchivo', 'Firmar otro documento', '2019-01-10', 'PFEA', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (247, 'labels.refrescar', 'Refrescar', '2019-01-16', 'uy-ci-33286742', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (250, 'messages.documentoCargadoCorrectamente', 'El documento se ha cargado correctamente', '2019-01-16', 'uy-ci-33286742', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (249, 'messages.documentoNoEsPdf', 'El archivo seleccionado no es de tipo PDF', '2019-01-16', 'uy-ci-33286742', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (248, 'messages.archivoNoSeleccionadoOVacio', 'No ha seleccionado un archivo o el archivo seleccionado está vacío', '2019-01-16', 'uy-ci-33286742', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (251, 'labels.logoutExitoso', 'Cierre de sesión exitoso', '2019-02-04', 'V0.5', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (252, 'labels.volverPaginaInicio', 'Volver a la página de inicio', '2019-02-04', 'V0.5', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (289, 'labels.validarFirma', 'Validar firma', '2019-02-19', 'V0.2', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (290, 'labels.resultadoValidacionFirma', 'Resultado de la validación', '2019-02-19', 'V0.2', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (291, 'messages.laFirmaDelDocumentoEsCorrecta', 'La firma del documento es correcta', '2019-02-19', 'V0.2', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (292, 'messages.laFirmaDelDocumentoNoEsCorrecta', 'La firma del documento no es correcta', '2019-02-19', 'V0.2', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (294, 'messages.envioDocumentoAPacConfirmado', 'El documento fue enviado', '2019-02-19', 'V0.2', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (296, 'messages.codigoSeguridad', 'El código de seguridad es', '2019-02-20', 'V0.2', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (300, 'messages.confirmaEnvioDocumentoAPac', 'Confirma el envío del documento a PAC', '2019-02-20', 'V0.2', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (306, 'labels.codigoPac', 'Código de seguridad', '2019-02-20', 'V0.2', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (307, 'labels.seleccionarDocumento', 'Seleccionar documento', '2019-02-20', 'V0.2', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (308, 'messages.debeCompletarTodosLosCampos', 'Debe completar todos los campos', '2019-02-20', 'V0.2', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (312, 'messages.documentoNoEncontrado', 'No se encontró ningún documento', '2019-02-20', 'V0.2', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (324, 'labels.info', 'Info. de firmas', '2019-02-21', 'V0.2', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (325, 'messages.elDocumentoNoContieneFirmas', 'El documento no contiene firmas digitales', '2019-02-21', 'V0.2', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (327, 'messages.elDocumentoContieneFirmas', 'El documento contiene {0} firmas', '2019-02-21', 'V0.2', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (331, 'labels.numero', 'Número', '2019-02-21', 'V0.2', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (332, 'labels.fecha', 'Fecha', '2019-02-21', 'V0.2', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (336, 'labels.enviarArchivoAPac', 'Enviar archivo a PACs', '2019-02-21', 'V0.2', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (337, 'labels.infoFirmas', 'Información de firmas del documento', '2019-02-21', 'V0.2', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (338, 'labels.bandejaDeDocumentos', 'Bandeja de documentos', '2019-02-21', 'V0.2', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (340, 'messages.sistemaOperativoNoSoportado', 'El sistema operativo usado no está soportado por la plataforma', '2019-02-21', 'V0.2', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (343, 'labels.valida', 'Válida', '2019-02-28', 'V0.3', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (344, 'labels.mensaje', 'Mensaje', '2019-02-28', 'V0.3', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (349, 'labels.documentosVigentes', 'Documentos vigentes', '2019-03-01', 'V0.3', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (350, 'labels.subirDocumento', 'Subir documento', '2019-03-01', 'V0.3', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (351, 'labels.actualizar', 'Actualizar', '2019-03-01', 'V0.3', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (352, 'labels.eligeMetodoFirma', 'Elegí el método de firma', '2019-03-01', 'V0.3', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (353, 'labels.pasoMetodo', 'Elección de método', '2019-03-01', 'V0.3', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (354, 'labels.pasoAplicativo', 'Descarga de aplicativo', '2019-03-01', 'V0.3', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (355, 'labels.pasoFirma', 'Firma en aplicativo', '2019-03-01', 'V0.3', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (356, 'labels.pasoConfirmacion', 'Confirmación', '2019-03-01', 'V0.3', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (357, 'labels.cerrarSesion', 'Cerrar sesión', '2019-03-01', 'V0.3', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (358, 'labels.inicio', 'Inicio', '2019-03-21', 'V0.4', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (359, 'labels.administracion', 'Administración', '2019-03-21', 'V0.4', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (374, 'labels.codigo', 'Código', '2019-03-21', 'V0.4', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (388, 'labels.', 'Confirmar', '2019-03-21', 'V0.4', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (402, 'labels.anadir', 'Añadir', '2019-03-21', 'V0.4', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (406, 'messages.UsuarioPacSeHaEncontradoUnUsuarioConElDocumentoIngresadoUsuarioPac', 'El documento ingresado corresponde a {nombre}. Si considera que esto es correcto puede confirmarlo como usuario PAC.', '2019-03-21', 'V0.4', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (407, 'messages.UsuarioPacNoSeEncuentraUnUsuarioConElDocumentoIngresado', 'No se encuentra un usuario con el documento ingresado; sin embargo, puede que aún no se haya registrado. Si está seguro que los datos son correctos puede confirmarlo como usuario PAC.', '2019-03-21', 'V0.4', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (420, 'messages.instalacionWindows', '<p>Para firmar un documento deberá instalar en su equipo un aplicativo de firma</p>
<p>Si ya lo tiene instalado, simplemente marque la casilla "<i>Ya lo tengo descargado e instalado</i>", en caso contrario, realice el siguiente procedimiento:</p>
<ol>
  <li>Descargue el instalador haciendo clic en el enlace de la izquierda.</li>
  <li>Ejecute el instalador realizando doble clic sobre él.</li>
  <li>Marque en la opción de la izquierda de esta página que ya lo tiene descargado e instalado.</li>
</ol>
<p>En el siguiente paso se descargará un archivo de tipo ".firmasiges", por favor realice doble clic sobre el mismo para lanzar el aplicativo de firma.</p>', '2019-03-29', 'V0.4', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (421, 'messages.instalacionLinux', '<p>Para firmar un documento deberá instalar en su equipo un aplicativo de firma</p>
<p>Si ya lo tiene instalado, simplemente marque la casilla "<i>Ya lo tengo descargado e instalado</i>", en caso contrario, realice el siguiente procedimiento:</p>
<ol>
  <li>Descargue el archivo comprimido que contiene al aplicativo haciendo clic en el enlace de la izquierda.</li>
  <li>Descomprima el archivo descargado en la ubicación de su preferencia.</li>
  <li>Marque en la opción de la izquierda de esta página que ya lo tiene descargado e instalado.</li>
</ol>
<p>Usando el explorador de archivos, abra la carpeta descomprimida en el paso anterior y haga doble clic sobre el archivo FirmaSIGESLinux.<p>
<p>En el siguiente paso se descargará un archivo de tipo ".firmasiges", este archivo  es el que deberá seleccionar en el aplicativo de firma.</p', '2019-03-29', 'V0.4', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (422, 'messages.instalacionMac', '<p>Para firmar un documento deberá instalar en su equipo un aplicativo de firma</p>
<p>Si ya lo tiene instalado, simplemente marque la casilla "<i>Ya lo tengo descargado e instalado</i>", en caso contrario, realice el siguiente procedimiento:</p>
<ol>
  <li>Descargue el archivo comprimido que contiene al aplicativo haciendo clic en el enlace de la izquierda.</li>
  <li>Marque en la opción de la izquierda de esta página que ya lo tiene descargado e instalado.</li>
</ol>
<p>En el siguiente paso se descargará un archivo de tipo ".firmasiges"; realice doble clic sobre el mismo y seleccione en "Abrir con" la aplicación FirmaSiges.app descargada.</p>', '2019-03-29', 'V0.4', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (424, 'labels.subiryValidar', 'Subir documento y validar firmas', '2019-05-02', 'V0.5', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (430, 'labels.emisor', 'Emisor', '2019-05-02', 'V0.5', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (431, 'labels.certificado', 'Certificado', '2019-05-02', 'V0.5', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (434, 'labels.cancelarEnvioDocumento', 'Cancelar envío de documento', '2019-05-02', 'V0.5', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (435, 'messages.confirmaQueDeseaCancelarElEnvioAUsuario', 'Confirma que desea cancelar el envío del documento al usuario', '2019-05-02', 'V0.5', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (443, 'labels.documentos_terceros', 'Documentos de terceros', '2019-09-30', 'V0.6', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (442, 'labels.documentos_propios', 'Documentos propios', '2019-09-30', 'V0.6', 0);
INSERT INTO pfea.ss_textos (tex_id, tex_codigo, tex_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (446, 'labels.subiryValidarTransfronteriza', 'Validar firma transfronteriza', '2020-01-22', 'V0.7', 0);


INSERT INTO pfea.ss_configuraciones (cnf_id, cnf_codigo, cnf_descripcion, cnf_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (17, 'FIRMA_VALIDAR_CONFIANZA_TRUSTORE_PASS', 'Contraseña del truststore para validar la confianza del certificado usado para firmar', 'sofis2uy', '2019-02-19', 'V0.2', 0);
INSERT INTO pfea.ss_configuraciones (cnf_id, cnf_codigo, cnf_descripcion, cnf_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (19, 'FIRMA_VALIDAR_CRL_URL', 'Ruta a la CRL para validar el certificado usado para firmar', 'https://www.google.com.uy', '2019-02-19', 'V0.2', 0);
INSERT INTO pfea.ss_configuraciones (cnf_id, cnf_codigo, cnf_descripcion, cnf_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (3, 'UPDATE_URL', 'URL base para la actualización del aplicativo ', 'http://192.168.1.129:8079', NULL, NULL, NULL);
INSERT INTO pfea.ss_configuraciones (cnf_id, cnf_codigo, cnf_descripcion, cnf_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (2, 'SIGES_FIRMA_WS', 'URL del servicio web', 'http://192.168.1.129:8079/pfea/AgesicFirmaWS', '2018-12-14', 'CONFIG', 0);
INSERT INTO pfea.ss_configuraciones (cnf_id, cnf_codigo, cnf_descripcion, cnf_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (16, 'FIRMA_VALIDAR_CONFIANZA_TRUSTORE_PATH', 'Ruta al truststore para validar la confianza del certificado usado para firmar', '/opt/wildfly/standalone/binarios/pfea-validacion.jks', '2019-02-19', 'V0.2', 0);
INSERT INTO pfea.ss_configuraciones (cnf_id, cnf_codigo, cnf_descripcion, cnf_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (18, 'FIRMA_VALIDAR_CRL', 'Validar la CRL del certificado usado para firmar', 'true', '2019-02-19', 'V0.2', 0);
INSERT INTO pfea.ss_configuraciones (cnf_id, cnf_codigo, cnf_descripcion, cnf_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (15, 'FIRMA_VALIDAR_CONFIANZA', 'Validar la confianza del certificado usado para firmar', 'true', '2019-02-19', 'V0.2', 0);
INSERT INTO pfea.ss_configuraciones (cnf_id, cnf_codigo, cnf_descripcion, cnf_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (21, 'SOFTWARE_PFEA_INFO', 'URL del archivo que indica la versión actual del aplicativo de firma', '/pfea.info', '2019-03-27', 'V0.4', 0);
INSERT INTO pfea.ss_configuraciones (cnf_id, cnf_codigo, cnf_descripcion, cnf_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (22, 'SOFTWARE_PFEA_WINDOWS', 'URL del enlace para la descarga del aplicativo de firma para MS Windows', '/pfea-windows.exe', '2019-03-27', 'V0.4', 0);
INSERT INTO pfea.ss_configuraciones (cnf_id, cnf_codigo, cnf_descripcion, cnf_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (23, 'SOFTWARE_PFEA_LINUX', 'URL del enlace para la descarga del aplicativo de firma para Linux', '/pfea-linux.zip', '2019-03-27', 'V0.4', 0);
INSERT INTO pfea.ss_configuraciones (cnf_id, cnf_codigo, cnf_descripcion, cnf_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (25, 'SOFTWARE_PFEA_UPDATE', 'URL del enlace para la descarga de la actualización del aplicativo de firma', '/pfea-update.zip', '2019-03-27', 'V0.4', 0);
INSERT INTO pfea.ss_configuraciones (cnf_id, cnf_codigo, cnf_descripcion, cnf_valor, est_ultima_modificacion, est_ultimo_usuario, est_version) VALUES (24, 'SOFTWARE_PFEA_MAC', 'URL del enlace para la descarga del aplicativo de firma para MAC', '/pfea-mac.zip', '2019-03-27', 'V0.4', 0);




INSERT INTO pfea.ss_textos (tex_id,tex_codigo,tex_valor,est_ultima_modificacion,est_ultimo_usuario,est_version) VALUES (447, 'messages.menuFirmaElectronica','Mensaje menú firma',now(),'V0.7',0);
INSERT INTO pfea.ss_textos (tex_id,tex_codigo,tex_valor,est_ultima_modificacion,est_ultimo_usuario,est_version) VALUES (448, 'labels.tituloFirmaSiges','Firma Electrónica en SIGES',now(),'V0.7',0);


-- 1.0.0

alter table pfea.ss_configuraciones rename column est_ultimo_usuario to cnf_ultimo_usuario;
alter table pfea.ss_configuraciones_aud rename column est_ultimo_usuario to cnf_ultimo_usuario;

alter table pfea.ss_configuraciones rename column est_ultima_modificacion to cnf_ultima_modificacion;
alter table pfea.ss_configuraciones_aud rename column est_ultima_modificacion to cnf_ultima_modificacion;

alter table pfea.ss_configuraciones rename column est_version to cnf_version;
alter table pfea.ss_configuraciones_aud rename column est_version to cnf_version;


alter table pfea.ss_configuraciones add column cnf_descripcion_busqueda character varying (255);
alter table pfea.ss_configuraciones_aud add column cnf_descripcion_busqueda character varying (255);

alter table pfea.ss_configuraciones add column cnf_es_editor boolean;
alter table pfea.ss_configuraciones_aud add column cnf_es_editor boolean;



INSERT INTO pfea.ss_configuraciones (cnf_id, cnf_codigo, cnf_descripcion, cnf_descripcion_busqueda, cnf_valor, cnf_ultima_modificacion, cnf_ultimo_usuario, cnf_version) VALUES (26, 'RECARGAR_TEXTOS_FRONTEND', 'Recargar textos pfea frontend', 'recargar textos pfea frontend', 'false', '2020-03-03', 'admin', 0);


alter table pfea.ss_textos rename column est_ultimo_usuario to tex_ultimo_usuario;
alter table pfea.ss_textos_aud rename column est_ultimo_usuario to tex_ultimo_usuario;

alter table pfea.ss_textos rename column est_ultima_modificacion to tex_ultima_modificacion;
alter table pfea.ss_textos_aud rename column est_ultima_modificacion to tex_ultima_modificacion;

alter table pfea.ss_textos rename column est_version to tex_version;
alter table pfea.ss_textos_aud rename column est_version to tex_version;


INSERT INTO pfea.ss_configuraciones (cnf_id, cnf_codigo, cnf_descripcion, cnf_descripcion_busqueda, cnf_valor, cnf_ultima_modificacion, cnf_ultimo_usuario, cnf_version) VALUES (27, 'EXPIRAR_DOCUMENTOS_MINUTOS', 'Tiempo en minutos para que expiren los documentos', 'tiempo en minutos para que expiren los documentos', '60', '2020-03-03', 'admin', 0);


-- 1.1.0 


INSERT INTO pfea.ss_configuraciones (cnf_id, cnf_codigo, cnf_descripcion, cnf_descripcion_busqueda, cnf_valor, cnf_ultima_modificacion, cnf_ultimo_usuario, cnf_version) VALUES (29, 'FIRMA_CLIENTE_VALIDAR_CRL', 'Verificación de CRL en applet habilitada', 'verificación de crl en applet habilitada', 'false', '2020-03-09', 'admin', 0);

UPDATE pfea.ss_configuraciones set cnf_valor = 'https://ejbca-ce-ejbca.okd.paas.infra.sofis.lat/ejbca/publicweb/webdist/certdist?cmd=crl&issuer=CN%3DSIGESTESTINGCA' where cnf_codigo = 'FIRMA_VALIDAR_CRL_URL';

ALTER TABLE pfea.ss_documentos ADD COLUMN doc_enviado_por_usuario character varying (255);
ALTER TABLE pfea.ss_documentos_aud ADD COLUMN doc_enviado_por_usuario character varying (255);
ALTER TABLE pfea.ss_documentos ADD COLUMN doc_debe_firmar_usuario character varying (255);
ALTER TABLE pfea.ss_documentos_aud ADD COLUMN doc_debe_firmar_usuario character varying (255);


INSERT INTO pfea.ss_configuraciones (cnf_id, cnf_codigo, cnf_descripcion, cnf_descripcion_busqueda, cnf_valor, cnf_ultima_modificacion, cnf_ultimo_usuario, cnf_version) VALUES (30, 'TRUSTORE_CLIENTE_CA_NAME', 'Nombre del trustore de la CA en el applet', 'nombre del trustore de la ca en el applet', 'validacion-test', '2020-03-09', 'admin', 0);
INSERT INTO pfea.ss_configuraciones (cnf_id, cnf_codigo, cnf_descripcion, cnf_descripcion_busqueda, cnf_valor, cnf_ultima_modificacion, cnf_ultimo_usuario, cnf_version) VALUES (31, 'TRUSTORE_CLIENTE_CA_VALIDATION_ENABLED', 'Validar trustore de la CA en el applet', 'validar trustore de la ca en el applet', 'true', '2020-03-09', 'admin', 0);


