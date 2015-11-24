package com.researchengine.controller;

import com.researchengine.model.Documents;
import com.researchengine.model.Queries;
import com.researchengine.model.RetrievedDocument;
import com.researchengine.model.form.IndexForm;
import com.researchengine.model.form.RetrievalAjax;
import com.researchengine.model.form.SearchForm;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Map;

@Controller
public class RouteController {

    private final String INVERTED_FILE_PATH = "E:\\Documents\\1 Projects\\1 Ongoing\\STBI-Tugas1\\data\\iFile.txt";
    private final String IDF_FILE_PATH = "E:\\Documents\\1 Projects\\1 Ongoing\\STBI-Tugas1\\data\\idf.txt";

    private Documents docs;
    private Queries queries;
    private IndexForm indexForm;
    private ArrayList<RetrievedDocument> results;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView indexView() {
        return new ModelAndView("index", "command", new IndexForm());
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ModelAndView indexSubmit(@ModelAttribute IndexForm indexForm, HttpServletRequest req) {
        this.indexForm = indexForm;

        ModelAndView m = new ModelAndView("index", "command", new IndexForm());

        if (indexForm.getDocFile().equals("") || indexForm.getQueryFile().equals("") ||
                indexForm.getRjFile().equals("") || indexForm.getSwFile().equals("")) {
            m.addObject("message", "Please choose file locations!");
            return m;
        }

        docs = new Documents(indexForm.getDocFile(), indexForm.getSwFile(), INVERTED_FILE_PATH, IDF_FILE_PATH,
                indexForm.getDocTFInt(), indexForm.getDocIDFInt(), indexForm.getDocStemmingInt());

        m.addObject("message", "Inverted file has been successfully created!");
        return m;
    }

    @RequestMapping(value = "experimental", method = RequestMethod.GET)
    public ModelAndView experimentalView() {
        ModelAndView m = new ModelAndView("experimental");

        queries = new Queries(docs.docList);
        queries.loadInvertedFile(INVERTED_FILE_PATH);
        queries.loadRelevanceJudgement(indexForm.getRjFile());
        queries.loadQueries(indexForm.getQueryFile());

        results = queries.searchAll(indexForm.getQueryTFInt(), indexForm.getQueryIDFInt(),
                indexForm.getDocNormalizationInt(), indexForm.getQueryStemmingInt(), indexForm.getSwFile(), IDF_FILE_PATH,
                indexForm.getRfMethodInt(), indexForm.getTopSInt(), indexForm.getQueryExpansionInt(),
                indexForm.getRfMethodInt() == 3 ? 1 : 0, indexForm.getTopNInt(), indexForm.getSecondRetrievalInt());

        for (RetrievedDocument rd : results) {
            queries.secondRetrieval(indexForm.getRfMethodInt() == 3 ? 1 : 0, indexForm.getTopSInt(), indexForm.getTopNInt(), rd,
                    indexForm.getQueryExpansionInt(), indexForm.getRfMethodInt());
        }

        m.addObject("docs", results);

        return m;
    }

    @RequestMapping(value = "interactive", method = RequestMethod.GET)
    public ModelAndView interactiveView() {
        return new ModelAndView("interactive", "command", new SearchForm());
    }

    @RequestMapping(value = "interactive", method = RequestMethod.POST)
    public ModelAndView interactiveSearch(@ModelAttribute SearchForm searchForm) {
        ModelAndView m = new ModelAndView("interactive", "command", new RetrievalAjax());

        queries = new Queries(docs.docList);
        queries.loadInvertedFile(INVERTED_FILE_PATH);
        queries.loadRelevanceJudgement(indexForm.getRjFile());
        queries.createQuery(searchForm.getQuery());

        results = queries.searchAll(indexForm.getQueryTFInt(), indexForm.getQueryIDFInt(),
                indexForm.getDocNormalizationInt(), indexForm.getQueryStemmingInt(), indexForm.getSwFile(), IDF_FILE_PATH,
                indexForm.getRfMethodInt(), indexForm.getTopSInt(), indexForm.getQueryExpansionInt(),
                indexForm.getRfMethodInt() == 3 ? 1 : 0, indexForm.getTopNInt(), indexForm.getSecondRetrievalInt());

        if (indexForm.getRfMethodInt() == 3) {
            m = new ModelAndView("reretrieval");

            Map<String, Double> oldTerms = results.get(0).weightedTerms;
            queries.secondRetrieval(indexForm.getRfMethodInt() == 3 ? 1 : 0, indexForm.getTopSInt(), indexForm.getTopNInt(), results.get(0),
                    indexForm.getQueryExpansionInt(), indexForm.getRfMethodInt());

            m.addObject("oldTerms", oldTerms);
            m.addObject("docs", results);
        } else {
            m.addObject("docs", results);
            m.addObject("queryString", searchForm.getQuery());
        }

        return m;
    }

    @RequestMapping(value = "reretrieval", method = RequestMethod.POST)
    public ModelAndView reretrievalSearch(@ModelAttribute RetrievalAjax retrievalAjax) {
        ModelAndView m = new ModelAndView("reretrieval");

        System.out.print("relevant ");
        for (int i = 0; i < retrievalAjax.getRelevant().length; ++i) {
            System.out.print(retrievalAjax.getRelevant()[i] + " ");
        }
        System.out.println("");

        System.out.print("notRelevant ");
        for (int i = 0; i < retrievalAjax.getNotRelevant().length; ++i) {
            System.out.print(retrievalAjax.getNotRelevant()[i] + " ");
        }
        System.out.println("");

        Map<String, Double> oldTerms = results.get(0).weightedTerms;
        for (int i = 0; i < retrievalAjax.getRelevant().length; ++i) {
            results.get(0).addToRelevant(retrievalAjax.getRelevant()[i]);
        }
        for (int i = 0; i < retrievalAjax.getNotRelevant().length; ++i) {
            results.get(0).addToRelevant(retrievalAjax.getNotRelevant()[i]);
        }
        queries.secondRetrieval(indexForm.getRfMethodInt() == 3 ? 1 : 0, indexForm.getTopSInt(), indexForm.getTopNInt(), results.get(0),
                indexForm.getQueryExpansionInt(), indexForm.getRfMethodInt());

        m.addObject("oldTerms", oldTerms);
        m.addObject("docs", results);

        for (Map.Entry<String, Double> entry : results.get(0).weightedTerms.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }

        return m;
    }

}
