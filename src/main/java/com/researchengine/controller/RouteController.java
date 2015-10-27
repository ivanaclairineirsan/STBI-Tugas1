package com.researchengine.controller;

import com.researchengine.model.Documents;
import com.researchengine.model.Queries;
import com.researchengine.model.RetrievedDocument;
import com.researchengine.model.form.IndexForm;
import com.researchengine.model.form.SearchForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;

@Controller
public class RouteController {

	private final String INVERTED_FILE_PATH = "E:\\Documents\\1 Projects\\1 Ongoing\\IF4042 Search Engine\\data\\iFile.txt";
	private final String IDF_FILE_PATH = "E:\\Documents\\1 Projects\\1 Ongoing\\IF4042 Search Engine\\data\\idf.txt";

	private Documents docs;
	private Queries queries;
	private IndexForm indexForm;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView indexView() {
		return new ModelAndView("index", "command", new IndexForm());
	}

	@RequestMapping(value = "experimental", method = RequestMethod.GET)
	public ModelAndView experimentalView() {
		ModelAndView m = new ModelAndView("experimental");

		queries = new Queries(docs.docList);
		queries.loadRelevanceJudgement(indexForm.getRjString());
		queries.loadQueries(indexForm.getQueryString());
		queries.loadInvertedFile(INVERTED_FILE_PATH);

		ArrayList<RetrievedDocument> results = queries.search(indexForm.getQueryTF(), indexForm.getQueryIDF(), indexForm.getQueryNormalization(), indexForm.getQueryStemming(), indexForm.getSwString(), IDF_FILE_PATH);
		m.addObject("docs", results);

		return m;
	}

	@RequestMapping(value = "interactive", method = RequestMethod.GET)
	public ModelAndView interactiveView() {
		return new ModelAndView("interactive", "command", new SearchForm());
	}

	@RequestMapping(value = "interactive", method = RequestMethod.POST)
	public ModelAndView interactiveSearch(@ModelAttribute SearchForm searchForm) {
		ModelAndView m = new ModelAndView("interactive", "command", new SearchForm());

		queries = new Queries(docs.docList);
		queries.loadRelevanceJudgement(indexForm.getRjString());
        queries.createQuery(searchForm.getQuery());
		queries.loadInvertedFile(INVERTED_FILE_PATH);

		ArrayList<RetrievedDocument> results = queries.search(indexForm.getQueryTF(), indexForm.getQueryIDF(), indexForm.getQueryNormalization(), indexForm.getQueryStemming(), indexForm.getSwString(), IDF_FILE_PATH);
		m.addObject("docs", results);

		return m;
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ModelAndView indexSubmit(@ModelAttribute IndexForm indexForm, HttpServletRequest req) {
		this.indexForm = indexForm;

		ModelAndView m = new ModelAndView("index", "command", new IndexForm(req.getServletContext().getRealPath("/")));

		if (indexForm.getDocString().equals("") || indexForm.getQueryString().equals("") ||
				indexForm.getRjString().equals("") || indexForm.getSwString().equals("")) {
			m.addObject("message", "Please choose file location!");
			return m;
		}

		docs = new Documents();
		docs.loadDocuments(indexForm.getDocString());
		docs.removeStopWord(indexForm.getSwString());
        docs.doStemming(indexForm.getDocStemming());

		docs.setInvertedTerms(indexForm.getDocTF(), indexForm.getDocIDF());
        docs.saveToFile(INVERTED_FILE_PATH);
        docs.calculateIDF();
        docs.saveToFileIDF(IDF_FILE_PATH);

		m.addObject("message", "Successfully created inverted file!");
		return m;
	}

}
