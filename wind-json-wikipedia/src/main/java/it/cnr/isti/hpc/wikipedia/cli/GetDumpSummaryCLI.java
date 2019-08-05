/**
 *  Copyright 2011 Diego Ceccarelli
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package it.cnr.isti.hpc.wikipedia.cli;

import it.cnr.isti.hpc.cli.AbstractCommandLineInterface;
import it.cnr.isti.hpc.io.reader.JsonRecordParser;
import it.cnr.isti.hpc.io.reader.RecordReader;
import it.cnr.isti.hpc.log.ProgressLogger;
import it.cnr.isti.hpc.wikipedia.article.Article;
import it.cnr.isti.hpc.wikipedia.article.ArticleSummarizer;

/**
 * Takes the JSON dump and produce a summary file containing, a file where each
 * line contains: <br/>
 * <br/>
 * <code>
 * type <tab> wid <tab> wikititle <tab> redirect/short summary
 * </code> <br/>
 * <br/>
 * 
 * The last field contains the redirection is type is redirect, otherwise the
 * short summary
 * 
 * 
 * @author Diego Ceccarelli, diego.ceccarelli@isti.cnr.it created on 21/nov/2011
 */
public class GetDumpSummaryCLI extends AbstractCommandLineInterface {

	private static String[] params = new String[] { INPUT, OUTPUT };

	private static final String USAGE = "java -cp $jar "
			+ GetDumpSummaryCLI.class
			+ " -input wikipedia-json-dump -output titles";

	private final static String TAB = "\t";

	public GetDumpSummaryCLI(String[] args) {
		super(args, params, USAGE);
	}

	public static void main(String[] args) {

		GetDumpSummaryCLI cli = new GetDumpSummaryCLI(args);
		ProgressLogger pl = new ProgressLogger("dumped {} titles", 10000);
		cli.openOutput();
		RecordReader<Article> reader = new RecordReader<Article>(
				cli.getInput(), new JsonRecordParser<Article>(Article.class));
		ArticleSummarizer summarizer = new ArticleSummarizer();

		for (Article a : reader) {
			pl.up();
			cli.writeInOutput(a.getTypeName());
			cli.writeInOutput(TAB);
			cli.writeInOutput(String.valueOf(a.getWikiId()));
			cli.writeInOutput(TAB);
			cli.writeInOutput(a.getWikiTitle());
			cli.writeInOutput(TAB);
			if (a.isRedirect()) {
				cli.writeInOutput("-> " + a.getRedirect());
			} else {
				cli.writeInOutput(summarizer.getSummary(a));
			}
			cli.writeInOutput("\n");

		}
		cli.closeOutput();
	}
}
