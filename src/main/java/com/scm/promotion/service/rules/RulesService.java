package com.scm.promotion.service.rules;


import java.util.Arrays;
import java.util.List;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.scm.promotion.model.Item;

/**
 * Singleton class that wraps
 * the Drools KieSession and is responsible
 * for loading rules and firing rules.
 */
@Component
public class RulesService {

    private KieServices ks;
    private KieContainer kContainer;
    private KieSession kieSession;

    @Bean("RulesService")
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public RulesService getRulesService() {
        return new RulesService();
    }

    /**
     * Function to initialize the kieSession.
     * This will be initialized on
     * application startup.
     */
    public void initializeRules() {

        this.ks = KieServices.Factory.get();
        this.kContainer = ks.getKieClasspathContainer();
        //this.kieSession =  kContainer.newKieSession();
    }

    public int fireRules(List<Item> items) {
    	this.kieSession =  kContainer.newKieSession();
    	for(Item item: items) {
    		this.kieSession.insert(item);
    	}
    	//List<String> skuCodes =Arrays.asList("A","B");
    	//this.kieSession.insert(skuCodes);
    	
        int fired = this.kieSession.fireAllRules();
        this.kieSession.dispose();
        return fired;

    }
}