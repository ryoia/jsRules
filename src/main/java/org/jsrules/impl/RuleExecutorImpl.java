/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jsrules.impl;

import java.util.Map;
import java.util.Map.Entry;
import org.jsrules.Rule;
import org.jsrules.RuleExecutor;
import org.jsrules.exception.InvalidParameterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Paul
 * @param <T>
 */
public class RuleExecutorImpl<T> implements RuleExecutor<T> {
    private static final Logger LOG = LoggerFactory.getLogger(RuleExecutorImpl.class);
            
    private final Rule<T> rule;
    
    public RuleExecutorImpl(Rule<T> rule) {
        this.rule = rule;
    }

    @Override
    public T execute(Map<String, Object> parameters) throws InvalidParameterException {
        if (!validateParameters(parameters)) {
            throw new InvalidParameterException();
        }
        
        return rule.getResult();
    }
    
    /**
     * Validate that all expected parameters are present and are instances of the
     * correct class.
     * 
     * Extraneous parameters are ignored
     * 
     * @param parameters
     * @return 
     */
    private boolean validateParameters(Map<String,Object> parameters) {      
        boolean valid = true;
                
        for(Entry<String, Class> entry:rule.getParameters().entrySet()) {
            String key = entry.getKey();
            Object parameter = parameters.get(key);
            Class expectedClass = entry.getValue();
            if (parameter == null) {
                LOG.error("Expected Parameter {} is missing");
                valid = false;
            } else if (!expectedClass.isInstance(parameter)) {
                LOG.error("Parameter {} is invalid | Expected class: {} | Parameter Class: {}",
                        key, expectedClass, parameter.getClass());
                valid = false;
            }
        }
        
        return valid;
    }
}
