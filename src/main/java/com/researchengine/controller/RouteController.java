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
	public ModelAndView indexForm() {
		return new ModelAndView("index", "command", new IndexForm());
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public String indexSubmit(@ModelAttribute IndexForm indexForm, Model model) {
		model.addAttribute("message", indexForm.getDocTF());

		return "retrieval";
	}

}
