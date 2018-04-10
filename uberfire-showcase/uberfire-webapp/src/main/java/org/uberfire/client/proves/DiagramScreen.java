/*
 * Copyright 2018 Red Hat, Inc. and/or its affiliates.
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

package org.uberfire.client.proves;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import org.uberfire.client.annotations.WorkbenchMenu;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.annotations.WorkbenchScreen;
import org.uberfire.workbench.model.menu.MenuFactory;
import org.uberfire.workbench.model.menu.Menus;

@Dependent
@WorkbenchScreen(identifier = "DiagramScreen", preferredWidth = 400)
public class DiagramScreen extends Composite {

    public static int currentEvent = 0;

    @Inject
    private SessionManager sessionManager;

    @Inject
    private Event<SomeEvent> someEven;

    private HTML widget = new HTML("Diagram Screen View");

    private ClientSession session1;
    private ClientSession session2;

    @PostConstruct
    public void init() {
        initWidget(widget);
    }

    public void createSession1() {
        log("Creating session [" + session1 + "]");
        session1 = sessionManager.newSession();
    }

    public void createSession2() {
        session2 = sessionManager.newSession();
        log("Created session [" + session2 + "]");
    }

    public void destroySession1() {
        log("Destroying session [" + session1 + "]");
        sessionManager.destroy(session1);
    }

    public void destroyWrongWaySession1() {
        log("Destroyomg in WRONG way the session [" + session1 + "]");
        session1.destroy();
    }

    public void destroySession2() {
        log("Destroying session [" + session2 + "]");
        sessionManager.destroy(session2);
    }

    public void fireSomeEvent() {
        String id = "Event" + ++currentEvent;
        someEven.fire(new SomeEvent(id));
        log("Fired event [" + id + "]");
    }

    @WorkbenchPartTitle
    public String getTitle() {
        return "Diagram Screen";
    }

    @WorkbenchMenu
    public Menus getMenu() {
        return MenuFactory
                .newTopLevelMenu("Create Session#1")
                .respondsWith(this::createSession1)
                .endMenu()
                .newTopLevelMenu("Destroy Session#1")
                .respondsWith(this::destroySession1)
                .endMenu()
                .newTopLevelMenu("Destroy WRONG Session#1")
                .respondsWith(this::destroyWrongWaySession1)
                .endMenu()
                .newTopLevelMenu("Create Session#2")
                .respondsWith(this::createSession2)
                .endMenu()
                .newTopLevelMenu("Destroy Session#2")
                .respondsWith(this::destroySession2)
                .endMenu()
                .newTopLevelMenu("Fire event")
                .respondsWith(this::fireSomeEvent)
                .endMenu()
                .build();
    }

    @WorkbenchPartView
    public Widget getView() {
        return this;
    }

    private static void log(String message) {
        GWT.log(message);
    }
}