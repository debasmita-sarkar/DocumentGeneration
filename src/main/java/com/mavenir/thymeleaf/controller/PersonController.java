package com.mavenir.thymeleaf.controller;

import java.io.IOException;
import java.util.Arrays;

import com.mavenir.thymeleaf.model.Accessory;
import com.mavenir.thymeleaf.model.AddOn;
import com.mavenir.thymeleaf.model.Device;
import com.mavenir.thymeleaf.model.Plan;
import com.mavenir.thymeleaf.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.StringTemplateResolver;


@Controller
public class PersonController {
	@Autowired
	QuoteService service;
	@Autowired
	private TemplateEngine templateEngine;

	@GetMapping
	public String getPeople(Model model) throws IOException {
		Context context = new Context();
		String title = "Quote";
		context.setVariable("title", title);
		context.setVariable("plans", Arrays.asList(
			new Plan("FTTH Plan", 1000),
			new Plan("Mobility Plan", 2000),
			new Plan("Plan DEF", 1700)

		));
		context.setVariable("listOfDevices", Arrays.asList(
			new Device("IPhone", "256gb", "red"),
			new Device("IPhone", "128 gb", "Black")
		));
		context.setVariable("accessories", Arrays.asList(
			new AddOn("IPhone Charger"),
			new AddOn("IPhone headphone")
		));
		context.setVariable("addons", Arrays.asList(
			new Accessory("VAS product")
		));

// Get the plain HTML with the resolved ${name} variable!
		String html = templateEngine.process("index", context);
//		String title = "Quote";
//		model.addAttribute("title",title);
//		model.addAttribute("plans", Arrays.asList(
//			new Person("FTTH Plan", 1000),
//			new Person("Mobility Plan", 2000),
//			new Person("Plan DEF", 1700)
//
//				));
//		//show one list for device
//		model.addAttribute("listOfDevices", Arrays.asList(
//			new Device("IPhone", "256gb","red"),
//			new Device("IPhone", "128 gb","Black")
//		));
//
//		//show one list for Accessories
//		model.addAttribute("accessories", Arrays.asList(
//			new AddOn("IPhone Charger"),
//			new AddOn("IPhone headphone")
//		));
//
//		//show one list for Accessories
//		model.addAttribute("addons", Arrays.asList(
//			new Accessory("VAS product")
//		));
		//return "index";
		service.createPDF(html);
		return html;
	}
	@Bean
	@Primary
	public TemplateEngine textTemplateEngine() {
		TemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(templateResolver()); return templateEngine;
	}

	private ITemplateResolver templateResolver() {
		StringTemplateResolver templateResolver = new StringTemplateResolver();
		templateResolver.setTemplateMode(TemplateMode.TEXT); return templateResolver; }

}