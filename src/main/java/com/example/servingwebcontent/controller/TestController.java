package com.example.servingwebcontent.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.servingwebcontent.entity.CountryEntity;
import com.example.servingwebcontent.form.TestForm;
import com.example.servingwebcontent.repository.CountryEntityMapper;

@Controller
public class TestController {

	@Autowired
	private CountryEntityMapper mapper;

	
	@GetMapping("/list2")
	public String list(TestForm testForm) {
		/*
		 * String names = "countrys"; List<CountryEntity> list =
		 * mapper.select(SelectDSLCompleter.allRows());
		 * 
		 */
		//model.addAttribute("testForm", new TestForm());
		return "list";
	}
	/**
	 * Represents a sequence of characters. In this context, it is used to return a
	 * JSON representation of a CountryEntity object.
	 */
	
	@PostMapping("/create")
	@ResponseBody
	public String createTestCountry(TestForm testForm) {
		CountryEntity entity  = new CountryEntity();
		entity.setMstcountrycd(testForm.getCd());
		entity.setMstcountrynanme(testForm.getName());
		mapper.insert(entity);
		testForm.setCd("");
		testForm.setName("");
		return "This is return front msg";
	}

}
