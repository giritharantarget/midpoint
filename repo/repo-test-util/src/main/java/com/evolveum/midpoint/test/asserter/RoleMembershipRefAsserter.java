/**
 * Copyright (c) 2018 Evolveum and contributors
 *
 * This work is dual-licensed under the Apache License 2.0 
 * and European Union Public License. See LICENSE file for details.
 */
package com.evolveum.midpoint.test.asserter;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertNull;
import static org.testng.AssertJUnit.assertTrue;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import com.evolveum.midpoint.prism.PrismObject;
import com.evolveum.midpoint.prism.PrismReferenceValue;
import com.evolveum.midpoint.prism.delta.ObjectDelta;
import com.evolveum.midpoint.schema.DeltaConvertor;
import com.evolveum.midpoint.schema.constants.ObjectTypes;
import com.evolveum.midpoint.test.IntegrationTestTools;
import com.evolveum.midpoint.test.util.TestUtil;
import com.evolveum.midpoint.util.exception.ObjectNotFoundException;
import com.evolveum.midpoint.util.exception.SchemaException;
import com.evolveum.midpoint.xml.ns._public.common.common_3.FocusType;
import com.evolveum.midpoint.xml.ns._public.common.common_3.ObjectType;
import com.evolveum.midpoint.xml.ns._public.common.common_3.OperationResultStatusType;
import com.evolveum.midpoint.xml.ns._public.common.common_3.PendingOperationExecutionStatusType;
import com.evolveum.midpoint.xml.ns._public.common.common_3.PendingOperationType;
import com.evolveum.midpoint.xml.ns._public.common.common_3.ShadowType;
import com.evolveum.prism.xml.ns._public.types_3.ObjectDeltaType;

/**
 * @author semancik
 *
 */
public class RoleMembershipRefAsserter<R> extends ObjectReferenceAsserter<FocusType,R> {
	
	public RoleMembershipRefAsserter(PrismReferenceValue refVal) {
		super(refVal, FocusType.class);
	}
	
	public RoleMembershipRefAsserter(PrismReferenceValue refVal, String detail) {
		super(refVal, FocusType.class, detail);
	}
	
	public RoleMembershipRefAsserter(PrismReferenceValue refVal, PrismObject<? extends FocusType> resolvedTarget, R returnAsserter, String detail) {
		super(refVal, FocusType.class, resolvedTarget, returnAsserter, detail);
	}
	
	@Override
	public RoleMembershipRefAsserter<R> assertOid() {
		super.assertOid();
		return this;
	}
	
	@Override
	public RoleMembershipRefAsserter<R> assertOid(String expected) {
		super.assertOid(expected);
		return this;
	}
	
	@Override
	public RoleMembershipRefAsserter<R> assertOidDifferentThan(String expected) {
		super.assertOidDifferentThan(expected);
		return this;
	}
	
	public ShadowAsserter<RoleMembershipRefAsserter<R>> shadow() {
		ShadowAsserter<RoleMembershipRefAsserter<R>> asserter = new ShadowAsserter<>((PrismObject<ShadowType>)getRefVal().getObject(), this, "shadow in reference "+desc());
		copySetupTo(asserter);
		return asserter;
	}

	@Override
	public FocusAsserter<FocusType, RoleMembershipRefAsserter<R>> target()
			throws ObjectNotFoundException, SchemaException {
		return new FocusAsserter<>(getResolvedTarget(), this, "object resolved from "+desc());
	}
	
	@Override
	public FocusAsserter<FocusType, RoleMembershipRefAsserter<R>> resolveTarget()
			throws ObjectNotFoundException, SchemaException {
		PrismObject<FocusType> object = resolveTargetObject();
		return new FocusAsserter<>(object, this, "object resolved from "+desc());
	}

}
