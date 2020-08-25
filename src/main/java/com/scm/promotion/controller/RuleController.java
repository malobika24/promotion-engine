package com.scm.promotion.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scm.promotion.model.Rule;
import com.scm.promotion.service.rules.RuleEngine;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/rules")
@RequiredArgsConstructor
public class RuleController {
	private final RuleEngine ruleEngine;

	@PostMapping("/add")
	public ResponseEntity<Void> registerRules(@RequestBody List<Rule> rules) {
		ruleEngine.registerRules(rules);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
}
