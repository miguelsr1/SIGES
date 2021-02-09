UPDATE pfea.ss_configuraciones set cnf_ultimo_usuario = 'admin';
UPDATE pfea.ss_textos set tex_ultimo_usuario = 'admin';
TRUNCATE pfea.ss_documentos;


INSERT INTO pfea.ss_configuraciones (cnf_id, cnf_codigo, cnf_descripcion, cnf_descripcion_busqueda, cnf_valor, cnf_ultima_modificacion, cnf_ultimo_usuario, cnf_version) VALUES (32, 'FIRMA_VALIDAR_CRL_URL_INTERNA', 'Ruta interna a la CRL para validar el certificado usado para firmar', 'ruta interna a la crl para validar el certificado usado para firmar', 'https://ejbca-ce-ejbca.okd.paas.infra.sofis.lat/ejbca/publicweb/webdist/certdist?cmd=crl&issuer=CN%3DSIGESTESTINGCA', '2020-04-09', 'admin', 0);
