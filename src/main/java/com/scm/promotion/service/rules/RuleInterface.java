package com.scm.promotion.service.rules;

public interface RuleInterface<Input,Output> {
	
	Output fireRules(Input input);
}
