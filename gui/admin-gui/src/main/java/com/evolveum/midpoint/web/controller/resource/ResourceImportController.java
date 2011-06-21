/*
 * Copyright (c) 2011 Evolveum
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
 * Portions Copyrighted 2011 [name of copyright owner]
 */
package com.evolveum.midpoint.web.controller.resource;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.evolveum.midpoint.api.logging.LoggingUtils;
import com.evolveum.midpoint.api.logging.Trace;
import com.evolveum.midpoint.logging.TraceManager;
import com.evolveum.midpoint.web.bean.ResourceListItem;
import com.evolveum.midpoint.web.bean.TaskStatus;
import com.evolveum.midpoint.web.controller.TemplateController;
import com.evolveum.midpoint.web.controller.util.ControllerUtil;
import com.evolveum.midpoint.web.model.ObjectTypeCatalog;
import com.evolveum.midpoint.web.model.ResourceManager;
import com.evolveum.midpoint.web.util.FacesUtils;
import com.evolveum.midpoint.xml.ns._public.common.common_1.OperationResultType;
import com.evolveum.midpoint.xml.ns._public.common.common_1.TaskStatusType;

/**
 * 
 * @author lazyman
 * 
 */
@Controller("resourceImport")
@Scope("session")
public class ResourceImportController implements Serializable {

	public static final String PAGE_NAVIGATION = "/resource/import?faces-redirect=true";
	public static final String NAVIGATION_LEFT = "leftImportStatus";
	private static final long serialVersionUID = 7495585784483264092L;
	private static final Trace LOGGER = TraceManager.getTrace(ResourceImportController.class);
	@Autowired(required = true)
	private ObjectTypeCatalog objectTypeCatalog;
	@Autowired(required = true)
	private transient TemplateController template;
	private ResourceListItem resource;
	private TaskStatus status;

	public ResourceListItem getResource() {
		return resource;
	}

	public void setResource(ResourceListItem resource) {
		this.resource = resource;
	}

	public TaskStatus getStatus() {
		if (status == null) {
			status = new TaskStatus(null);
		}
		return status;
	}

	public String initController() {
		String nextPage = null;
		try {
			ResourceManager manager = ControllerUtil.getResourceManager(objectTypeCatalog);
			OperationResultType resultType = new OperationResultType();
			TaskStatusType statusType = manager.getImportStatus(getResource().getOid());
			if (statusType != null) {
				this.status = new TaskStatus(statusType);
				nextPage = PAGE_NAVIGATION;
			} else {
				FacesUtils.addErrorMessage("Couldn't get import status. TODO: resultType handling.");
			}
		} catch (Exception ex) {
			LoggingUtils.logException(LOGGER, "Couldn't get import status for resource {}", ex, getResource()
					.getName());
			FacesUtils.addErrorMessage("Couldn't get import status for resource '" + getResource().getName()
					+ "'.", ex);
		}

		if (PAGE_NAVIGATION.equals(nextPage)) {
			template.setSelectedLeftId(NAVIGATION_LEFT);
		}
		return nextPage;
	}

	public String backPerformed() {
		template.setSelectedLeftId(ResourceDetailsController.NAVIGATION_LEFT);
		return ResourceDetailsController.PAGE_NAVIGATION;
	}
}
