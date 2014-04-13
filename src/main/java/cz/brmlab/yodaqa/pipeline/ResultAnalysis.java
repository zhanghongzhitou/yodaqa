package cz.brmlab.yodaqa.pipeline;

import de.tudarmstadt.ukp.dkpro.core.berkeleyparser.BerkeleyParser;
import de.tudarmstadt.ukp.dkpro.core.languagetool.LanguageToolSegmenter;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordLemmatizer;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.cas.CAS;
import org.apache.uima.fit.component.CasDumpWriter;
import org.apache.uima.fit.factory.AggregateBuilder;
import org.apache.uima.resource.ResourceInitializationException;

import cz.brmlab.yodaqa.annotator.result.SentenceFilter;
import cz.brmlab.yodaqa.annotator.result.CanBySentence;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createPrimitiveDescription;

/**
 * Annotate the SearchResultCAS.
 *
 * This is an aggregate AE that will run a variety of annotators on the
 * SearchResultCAS, first preparing it for answer generation and then
 * actually producing some CandiateAnswer annotations. */

public class ResultAnalysis /* XXX: extends AggregateBuilder ? */ {
	public static AnalysisEngineDescription createEngineDescription() throws ResourceInitializationException {
		AggregateBuilder builder = new AggregateBuilder();

		/* A bunch of DKpro-bound NLP processors (these are
		 * the giants we stand on the shoulders of) */
		/* The mix below corresponds to what we use in
		 * QuestionAnalysis, refer there for details. */

		/* Token features: */
		// LanguageToolsSegmenter is the only one capable of dealing
		// with incomplete sentences e.g. separated by paragraphs etc.
		builder.add(createPrimitiveDescription(LanguageToolSegmenter.class),
			CAS.NAME_DEFAULT_SOFA, "Result");

		/* At this point, we can filter the source to keep
		 * only sentences and tokens we care about: */
		builder.add(createPrimitiveDescription(SentenceFilter.class));

		/* Constituent features and POS features: */
		//builder.add(createPrimitiveDescription(BerkeleyParser.class),
		//	CAS.NAME_DEFAULT_SOFA, "Passages");

		/* POS features: */
		// Generated by BerkeleyParser

		/* Lemma features: */
		//builder.add(createPrimitiveDescription(StanfordLemmatizer.class),
		//	CAS.NAME_DEFAULT_SOFA, "Passages");

		/* Dependency features: */
		// no need for now


		/* Okay! Now, we can proceed with our key tasks. */

		// TODO :-)
		builder.add(createPrimitiveDescription(CanBySentence.class),
			CAS.NAME_DEFAULT_SOFA, "Passages");


		/* Some debug dumps of the intermediate CAS. */
		/* builder.add(createPrimitiveDescription(
			CasDumpWriter.class,
			CasDumpWriter.PARAM_OUTPUT_FILE, "/tmp/yodaqa-racas.txt")); */

		return builder.createAggregateDescription();
	}
}
