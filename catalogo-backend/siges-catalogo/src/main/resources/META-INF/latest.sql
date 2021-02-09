ALTER TABLE catalogo.sg_menu_pentaho_aud DROP CONSTRAINT sg_menu_pentaho_aud_pkey;

ALTER TABLE catalogo.sg_menu_pentaho_aud ADD CONSTRAINT sg_menu_pentaho_aud_pkey PRIMARY KEY (mpe_pk, rev);
