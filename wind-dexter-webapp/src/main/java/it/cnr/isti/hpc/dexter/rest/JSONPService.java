/**
 *  Copyright 2013 Diego Ceccarelli
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
package it.cnr.isti.hpc.dexter.rest;

import it.cnr.isti.hpc.dexter.common.ArticleDescription;
import it.cnr.isti.hpc.dexter.relatedness.Relatedness;
import it.cnr.isti.hpc.dexter.rest.domain.AnnotatedDocument;
import it.cnr.isti.hpc.dexter.rest.domain.EntitySpots;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.representation.Form;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

/**
 * @author Diego Ceccarelli <diego.ceccarelli@isti.cnr.it>
 * 
 *         Created on Feb 2, 2013
 */

@Path("jsonp")
@Api(value = "jsonp", description = "Dexter JSONP Rest Service")
public class JSONPService {

	RestService r = new RestService();

	private static final Logger logger = LoggerFactory
			.getLogger(JSONPService.class);

	private String addCallback(String callback, String json) {
		StringBuilder sb = new StringBuilder(callback);
		sb.append("(").append(json).append(");");
		return sb.toString();
	}

	private Response addCallback(String callback, Response res) {
		StringBuilder sb = new StringBuilder(callback);
		sb.append("(").append(res.getEntity()).append(");");
		String json = sb.toString();
		Response r = Response.status(res.getStatus()).entity(json).build();
		return r;
	}

	/**
	 * Performs the entity linking on a given text, annotating maximum n
	 * entities.
	 * 
	 * @param text
	 *            the text to annotate
	 * @param n
	 *            the maximum number of entities to annotate
	 * @returns an annotated document, containing the annotated text, and a list
	 *          entities detected.
	 */

	@GET
	@Path("/annotate")
	@ApiOperation(value = "Annotate a document with Wikipedia entities", response = AnnotatedDocument.class)
	@Produces({ MediaType.APPLICATION_JSON })
	public Response annotateGet(@Context UriInfo ui,
			@QueryParam("callback") @DefaultValue("callback") String callback,
			@QueryParam("text") String text,
			@QueryParam("n") @DefaultValue("5") String n,
			@QueryParam("spt") String spotter,
			@QueryParam("dsb") String disambiguator,
			@QueryParam("wn") @DefaultValue("false") String wikiNames,
			@QueryParam("debug") @DefaultValue("false") String dbg,

			@QueryParam("format") @DefaultValue("text") String format,
			@QueryParam("min-conf") @DefaultValue("0.5") String minConfidence) {
		return addCallback(callback, r.annotateGet(ui, text, n, spotter,
				disambiguator, wikiNames, dbg, format, minConfidence));

	}

	@POST
	@Path("/annotate")
	@ApiOperation(value = "Annotate a document with Wikipedia entities", response = AnnotatedDocument.class)
	@Produces({ MediaType.APPLICATION_JSON })
	public Response annotatePost(Form form,
			@FormParam("callback") @DefaultValue("callback") String callback,
			@FormParam("text") String text,
			@FormParam("n") @DefaultValue("5") String n,
			@FormParam("spt") String spotter,
			@FormParam("dsb") String disambiguator,
			@FormParam("wn") @DefaultValue("false") String wikiNames,
			@FormParam("debug") @DefaultValue("false") String dbg,
			@FormParam("format") @DefaultValue("text") String format,
			@QueryParam("min-conf") @DefaultValue("0.5") String minConfidence) {

		return addCallback(callback, r.annotatePost(form, text, n, spotter,
				disambiguator, wikiNames, dbg, format, minConfidence));

	}

	/**
	 * Given the Wiki-id of an entity, returns a snippet containing some
	 * sentences that describe the entity.
	 * 
	 * @param id
	 *            the Wiki-id of the entity
	 * @param title
	 *            (optional, false by default) "true" if the function only
	 *            should return the label of the entity for the Wiki-id,
	 *            otherwise it will return all the metadata available.
	 * 
	 * @returns a short description of the entity represented by the Wiki-id
	 */

	@GET
	@Path("/get-desc")
	@ApiOperation(value = "Provides the description of an entity", response = ArticleDescription.class)
	@Produces({ MediaType.APPLICATION_JSON })
	public String getDescription(
			@QueryParam("callback") @DefaultValue("callback") String callback,
			@QueryParam("id") String id,
			@QueryParam("title-only") @DefaultValue("false") String titleonly) {
		return addCallback(callback, r.getDescription(id, titleonly));

	}

	@GET
	@Path("/get-id")
	@ApiOperation(value = "Provides the wiki-id of an entity", response = ArticleDescription.class)
	@Produces({ MediaType.APPLICATION_JSON })
	public String getDescription(
			@QueryParam("callback") @DefaultValue("callback") String callback,
			@QueryParam("title") String title) {
		return addCallback(callback, r.getDescription(title));

	}

	@GET
	@Path("/get-spots")
	@ApiOperation(value = "Provides all the spots that could refer to the given entity", response = EntitySpots.class)
	@Produces({ MediaType.APPLICATION_JSON })
	public String getEntitySpots(
			@QueryParam("callback") @DefaultValue("callback") String callback,
			@QueryParam("id") String id, @QueryParam("title") String title,
			@QueryParam("wn") @DefaultValue("false") String wikiNames) {

		return addCallback(callback, r.getEntitySpots(id, title, wikiNames));

	}

	@GET
	@Path("/spot")
	@ApiOperation(value = "Detects all the mentions that could refer to an entity in the text", response = EntitySpots.class)
	@Produces({ MediaType.APPLICATION_JSON })
	public Response spotGet(@Context UriInfo ui,
			@QueryParam("callback") @DefaultValue("callback") String callback,
			@QueryParam("text") String text, @QueryParam("spt") String spt,
			@QueryParam("wn") @DefaultValue("false") String wikiNames,
			@QueryParam("debug") @DefaultValue("false") String dbg,
			@QueryParam("format") @DefaultValue("text") String format) {
		return addCallback(callback,
				r.spotGet(ui, text, spt, wikiNames, dbg, format));
	}

	@POST
	@Path("/spot")
	@ApiOperation(value = "Detects all the mentions that could refer to an entity in the text", response = EntitySpots.class)
	@Produces({ MediaType.APPLICATION_JSON })
	public Response spotPost(Form form,
			@FormParam("callback") @DefaultValue("callback") String callback,
			@FormParam("text") String text, @FormParam("spt") String spt,
			@FormParam("wn") @DefaultValue("false") String wikiNames,
			@FormParam("debug") @DefaultValue("false") String dbg,
			@FormParam("format") @DefaultValue("text") String format) {
		return addCallback(callback,
				r.spotPost(form, text, spt, wikiNames, dbg, format));
	}

	@GET
	@Path("/get-candidates")
	@ApiOperation(value = "Given a query, returns a list of candidates entities represented by the query", response = EntitySpots.class)
	@Produces({ MediaType.APPLICATION_JSON })
	public Response queryLucene(@Context UriInfo ui,
			@QueryParam("callback") @DefaultValue("callback") String callback,
			@QueryParam("field") @DefaultValue("content") String field,
			@QueryParam("n") @DefaultValue("10") String results,
			@QueryParam("query") String query) {
		Response res = addCallback(callback,
				r.queryLucene(ui, field, results, query));
		return res;

	}

	@GET
	@Path("/relatedness")
	@ApiOperation(value = "Return the semantic relatedness between two entities", response = Relatedness.class)
	@Produces({ MediaType.APPLICATION_JSON })
	public String relatedness(@Context UriInfo ui,
			@QueryParam("callback") @DefaultValue("callback") String callback,
			@QueryParam("e1") String e1, @QueryParam("e2") String e2,
			@QueryParam("rel") @DefaultValue("milnewitten") String rel,
			@QueryParam("wn") @DefaultValue("false") String wikiNames,
			@QueryParam("debug") @DefaultValue("false") String dbg) {

		return addCallback(callback,
				r.relatedness(ui, e1, e2, rel, wikiNames, dbg));

	}

	@GET
	@Path("/spot-relatedness")
	@ApiOperation(value = "Return the semantic relatedness between two entities", response = Relatedness.class)
	@Produces({ MediaType.APPLICATION_JSON })
	public String spotRelatedness(@Context UriInfo ui,
			@QueryParam("callback") @DefaultValue("callback") String callback,
			@QueryParam("s1") String s1id, @QueryParam("s2") String s2id,
			@QueryParam("e1") String s1candidates,
			@QueryParam("e2") String s2candidates,
			@QueryParam("rel") @DefaultValue("milnewitten") String rel,
			@QueryParam("wn") @DefaultValue("false") String wikiNames,
			@QueryParam("debug") @DefaultValue("false") String dbg) {
		return addCallback(callback, r.spotRelatedness(ui, s1id, s2id,
				s1candidates, s2candidates, rel, wikiNames, dbg));

	}

}
