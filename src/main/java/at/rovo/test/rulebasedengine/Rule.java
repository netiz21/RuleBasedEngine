/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.rovo.test.rulebasedengine;

import at.rovo.test.rulebasedengine.dispatcher.ActionDispatcher;
import at.rovo.test.rulebasedengine.dispatcher.NullActionDispatcher;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Roman Vottner
 */
public class Rule
{
    private String name;
    private List<Expression> expressions;
    private ActionDispatcher dispatcher;
    
    public static class Builder
    {
        private List<Expression> expressions = new ArrayList<>();
        private ActionDispatcher dispatcher = new NullActionDispatcher();
        private String name = "unknown";
        
        public Builder withExpression(Expression expr)
        {
            expressions.add(expr);
            return this;
        }
        
        public Builder withExpression(String expr)
        {
            expressions.add(ExpressionParser.fromString(expr));
            return this;
        }
        
        public Builder withDispatcher(ActionDispatcher dispatcher)
        {
            this.dispatcher = dispatcher;
            return this;
        }
        
        public Builder withName(String name)
        {
            this.name = name;
            return this;
        }
        
        public Rule build()
        {
            return new Rule(name, expressions, dispatcher);
        }
    }
    
    private Rule(String name, List<Expression> expressions, ActionDispatcher dispatcher)
    {
        this.name = name;
        this.expressions = expressions;
        this.dispatcher = dispatcher;
    }
        
    public String getName()
    {
        return name;
    }
    
    public boolean eval(Map<String, ?> bindings)
    {
        boolean result = false;
        for (Expression expression : expressions)
        {
            boolean eval = expression.interpret(bindings);
            if (eval)
            {
                result = true;
                dispatcher.fire();
            }
        }
        return result;
    }
    
    @Override
    public String toString() 
    {
        return name;
    }
}
