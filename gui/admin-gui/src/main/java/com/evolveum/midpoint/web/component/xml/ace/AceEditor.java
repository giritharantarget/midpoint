/*
 * Copyright (c) 2010-2013 Evolveum
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.evolveum.midpoint.web.component.xml.ace;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.IModel;

public class AceEditor extends TextArea<String> {

    private static final String EDITOR_SUFFIX = "_edit";

    private String editorId;
    private boolean readonly = false;

    public AceEditor(String id, IModel<String> model) {
        super(id, model);
        this.editorId = getMarkupId() + EDITOR_SUFFIX;
        setOutputMarkupId(true);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        StringBuilder sb = new StringBuilder();
        sb.append("initEditor('").append(getMarkupId()).append("',").append(readonly).append(");");

        response.render(OnDomReadyHeaderItem.forScript(sb.toString()));
    }

    public void setReadonly(boolean readonly) {
        setReadonly(null, readonly);
    }

    public void setReadonly(AjaxRequestTarget target, boolean readonly) {
        this.readonly = readonly;

        if (target == null) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("refreshReadonly('").append(getMarkupId()).append("',").append(readonly).append(");");

        target.appendJavaScript(sb.toString());
    }
}
