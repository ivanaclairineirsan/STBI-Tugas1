package com.researchengine.controller;

import com.researchengine.model.form.IndexForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RouteController {

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView indexView() {
		return new ModelAndView("index", "command", new IndexForm());
	}

	@RequestMapping(value = "experimental", method = RequestMethod.GET)
	public String experimentalView() {
		return "experimental";
	}

	@RequestMapping(value = "interactive", method = RequestMethod.GET)
	public String interactiveView() {
		return "interactive";
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ModelAndView indexSubmit(@ModelAttribute IndexForm indexForm, Model model) {
		ModelAndView m = new ModelAndView("index", "command", new IndexForm());
		m.addObject("message", indexForm.getDocTF());

		return m;
	}

}
