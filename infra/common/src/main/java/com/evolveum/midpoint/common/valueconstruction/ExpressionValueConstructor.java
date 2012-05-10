/*
 * Copyright (c) 2012 Evolveum
 *
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at
 * http://www.opensource.org/licenses/cddl1 or
 * CDDLv1.0.txt file in the source code distribution.
 * See the License for the specific language governing
 * permission and limitations under the License.
 *
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 *
 * Portions Copyrighted 2012 [name of copyright owner]
 */
package com.evolveum.midpoint.common.valueconstruction;

import com.evolveum.midpoint.common.expression.Expression;
import com.evolveum.midpoint.common.expression.ExpressionFactory;
import com.evolveum.midpoint.prism.Item;
import com.evolveum.midpoint.prism.ItemDefinition;
import com.evolveum.midpoint.prism.PrismProperty;
import com.evolveum.midpoint.prism.PrismPropertyDefinition;
import com.evolveum.midpoint.prism.PrismValue;
import com.evolveum.midpoint.prism.PropertyPath;
import com.evolveum.midpoint.prism.PrismPropertyValue;
import com.evolveum.midpoint.prism.delta.PrismValueDeltaSetTriple;
import com.evolveum.midpoint.prism.delta.ItemDelta;
import com.evolveum.midpoint.prism.xml.XmlTypeConverter;
import com.evolveum.midpoint.prism.xml.XsdTypeMapper;
import com.evolveum.midpoint.schema.constants.ExpressionConstants;
import com.evolveum.midpoint.schema.result.OperationResult;
import com.evolveum.midpoint.util.exception.ExpressionEvaluationException;
import com.evolveum.midpoint.util.exception.ObjectNotFoundException;
import com.evolveum.midpoint.util.exception.SchemaException;
import com.evolveum.midpoint.xml.ns._public.common.common_1.ExpressionType;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Radovan Semancik
 */
public class ExpressionValueConstructor implements ValueConstructor {

    private ExpressionFactory factory;

    ExpressionValueConstructor(ExpressionFactory factory) {
        this.factory = factory;
    }

    /* (non-Javadoc)
      * @see com.evolveum.midpoint.common.valueconstruction.ValueConstructor#construct(com.evolveum.midpoint.schema.processor.PropertyDefinition, com.evolveum.midpoint.schema.processor.Property)
      */
    @Override
    public <V extends PrismValue> PrismValueDeltaSetTriple<V> construct(JAXBElement<?> constructorElement, ItemDefinition outputDefinition,
			Item<V> input, ItemDelta<V> inputDelta, Map<QName, Object> variables,
			boolean conditionResultOld, boolean conditionResultNew,
			String contextDescription, OperationResult result) throws SchemaException,
			ExpressionEvaluationException, ObjectNotFoundException {

        Object constructorTypeObject = constructorElement.getValue();
        if (!(constructorTypeObject instanceof ExpressionType)) {
            throw new IllegalArgumentException("Expression value constructor cannot handle elements of type " + constructorTypeObject.getClass().getName());
        }
        ExpressionType constructorType = (ExpressionType) constructorTypeObject;

        Collection<V> expressionResultsOld = null;
        if (conditionResultOld) {
        	expressionResultsOld = evaluateExpression(constructorType, outputDefinition, input, inputDelta, variables, true, contextDescription, result);
        }
        
        Collection<V> expressionResultsNew = null;
        if (conditionResultNew) {
        	expressionResultsNew = evaluateExpression(constructorType, outputDefinition, input, inputDelta, variables, false, contextDescription, result);
        }

        PrismValueDeltaSetTriple<V> valueDeltaTriple = PrismValueDeltaSetTriple.diff(expressionResultsOld, expressionResultsNew);
        
        return valueDeltaTriple;        
    }

    
    private <V extends PrismValue> Collection<V> evaluateExpression(ExpressionType constructorType, ItemDefinition outputDefinition,
			Item<V> input, ItemDelta<V> inputDelta, Map<QName, Object> variables, boolean old, String contextDescription, OperationResult result) throws ExpressionEvaluationException, ObjectNotFoundException, SchemaException {
    	Expression expression = factory.createExpression(constructorType, contextDescription);

    	if (old) {
    		expression.addVariableDefinitionsOld(variables);
    	} else {
    		expression.addVariableDefinitionsNew(variables);
    	}
    	
//		if (!expression.hasVariableDefinition(ExpressionConstants.VAR_INPUT)) {
//			expression.addVariableDefinition(ExpressionConstants.VAR_INPUT, input);
//		}


        QName typeName = outputDefinition.getTypeName();
        Class<Object> type = XsdTypeMapper.toJavaType(typeName);
        Item<V> output = outputDefinition.instantiate();
        if (!(output instanceof PrismProperty)) {
        	throw new UnsupportedOperationException("Expression can only result in a property, not "+output.getClass());
        }
        
        Collection<PrismValue> outputValues = new ArrayList<PrismValue>(); 

        if (outputDefinition.isMultiValue()) {
            List<PrismPropertyValue<Object>> resultValues = expression.evaluateList(type, result);
            outputValues.addAll(resultValues);
        } else {
            PrismPropertyValue<Object> resultValue = expression.evaluateScalar(type, result);
            if (resultValue != null) {
            	outputValues.add(resultValue);
            }
        }
        
        return (Collection<V>) outputValues;
    }
    
}
