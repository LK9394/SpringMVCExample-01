package com.example.servingwebcontent.controller;

import java.util.List;
import java.util.Optional;

import org.mybatis.dynamic.sql.select.SelectDSLCompleter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import com.example.servingwebcontent.entity.CountryEntity;
import com.example.servingwebcontent.form.CountryForm;
import com.example.servingwebcontent.form.CountrySearchForm;
import com.example.servingwebcontent.repository.CountryEntityMapper;
import com.example.servingwebcontent.utils.CommonResult;
import com.google.gson.Gson;

@Controller
public class CountryController {

	@Autowired
	private CountryEntityMapper mapper;
	

	@Autowired
	private CommonResult result;

	@Autowired
	private Gson gson;
	
	@GetMapping("/country")
	public String init(Model model) {
		model.addAttribute("searchForm",new CountrySearchForm());
		model.addAttribute("countryForm",new CountryForm());
		return "country/country";
	}
	
	
	@GetMapping("/list1")
	public String list(Model model) {
		String names = "countrys";
		List<CountryEntity> list = mapper.select(SelectDSLCompleter.allRows());
		
		model.addAttribute(names, list);
		return "list";
	}
	
	/**
	 * Represents a sequence of characters. In this context, it is used to return a
	 * JSON representation of a CountryEntity object.
	 */
	@PostMapping("/country/getCountry")
	@ResponseBody
	public String getCountry(@Validated CountrySearchForm countrySearchForm, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}

		/**
		 * Optional object containing the result of the database query for the country
		 * with the specified country code.
		 */
		Optional<CountryEntity> countryEntity = mapper.selectByPrimaryKey(countrySearchForm.getMstCountryCD());
		if (countryEntity == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

		return new Gson().toJson(countryEntity.get());
	}
	
	
	@GetMapping("/country/create")
	@ResponseBody
	public String create(CountryForm countryForm,BindingResult bindingResult) {
		
		
		System.out.println("Received request: " + countryForm);

	    if (bindingResult.hasErrors()) {
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "请求参数错误");
	    }

	    // 检查国家编码是否为空
	    String countryCode = countryForm.getCd();
	    if (countryCode == null || countryCode.isEmpty()) {
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "国家编码不能为空。");
	    }
		
		
		CountryEntity entity = new CountryEntity();
		entity.setMstcountrycd(countryCode);
		entity.setMstcountrynanme(countryForm.getName());
		 mapper.insert(entity);
		
		
		 result.setStatus(0);
			result.setMessage("数据加入成功");

			return gson.toJson(result);
	}

	/*
	 * 创建一个方法，监听/country/createCountry，
	 * 实现根据请求的参数创建一个CountryEntity对象，并将其插入到数据库中。
	 */
	@PostMapping("/country/createCountry")
	@ResponseBody
	public String createCountry(CountryForm countryForm) {
		// Method body goes here
		// For example, you might save the countryEntity to the database
		// Then return a success message or the saved entity
		CountryEntity countryEntity = new CountryEntity();
		countryEntity.setMstcountrycd(countryForm.getCd());
		countryEntity.setMstcountrynanme(countryForm.getName());
		
		mapper.insert(countryEntity);
		
		countryForm.setCd("");
		countryForm.setName("");
		return "Country created successfully";
	}
	
	@PostMapping("/country/update")
	@ResponseBody
	public String update(CountryForm countryForm) {
		CountryEntity countryEntity = new CountryEntity();
		countryEntity.setMstcountrycd(countryForm.getCd());
		countryEntity.setMstcountrynanme(countryForm.getName());
		
		mapper.updateByPrimaryKey(countryEntity);
		result.setStatus(0);
		result.setMessage("数据更新成功");

		return gson.toJson(result);
	}
	
	@PostMapping("/country/delete")
	@ResponseBody
	public String delete(CountryForm countryForm) {
		
		
		mapper.deleteByPrimaryKey(countryForm.getCd());
		result.setStatus(0);
		result.setMessage("数据删除成功");		

		return gson.toJson(result);
	}
	

}
