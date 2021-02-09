/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 *  Entidades
 */
@AnalyzerDefs({
    @AnalyzerDef(
            name = "Analizador_Nombres",
            tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
            filters = {
                @TokenFilterDef(factory = StandardFilterFactory.class)
                ,
                @TokenFilterDef(factory = ASCIIFoldingFilterFactory.class)
                ,
                @TokenFilterDef(factory = LowerCaseFilterFactory.class)
            })
    ,
    @AnalyzerDef(
            name = "Analizador_Nombres_Sort",
            tokenizer = @TokenizerDef(factory = KeywordTokenizerFactory.class),
            filters = {
                @TokenFilterDef(factory = ASCIIFoldingFilterFactory.class)
                ,
                @TokenFilterDef(factory = LowerCaseFilterFactory.class)
            })
    ,
    @AnalyzerDef(
            name = "Analizador_Nombres_Phonetic",
            tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
            filters = {
                @TokenFilterDef(factory = StandardFilterFactory.class)
                ,
                @TokenFilterDef(factory = ASCIIFoldingFilterFactory.class)
                ,
                @TokenFilterDef(factory = LowerCaseFilterFactory.class)
                ,
                @TokenFilterDef(factory = PhoneticFilterFactory.class, params = {
                    @Parameter(name = "encoder", value = "DoubleMetaphone")
                    , 
                    @Parameter(name = "inject", value = "false")
                })
            })
})
package sv.gob.mined.siges.persistencia.entidades;

import org.apache.lucene.analysis.core.KeywordTokenizerFactory;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilterFactory;
import org.apache.lucene.analysis.phonetic.PhoneticFilterFactory;
import org.apache.lucene.analysis.standard.StandardFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.hibernate.search.annotations.AnalyzerDef;
import org.hibernate.search.annotations.AnalyzerDefs;
import org.hibernate.search.annotations.Parameter;
import org.hibernate.search.annotations.TokenFilterDef;
import org.hibernate.search.annotations.TokenizerDef;

