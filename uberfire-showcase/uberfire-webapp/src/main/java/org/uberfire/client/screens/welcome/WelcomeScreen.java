/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.uberfire.client.screens.welcome;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import org.uberfire.client.annotations.WorkbenchContextId;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchScreen;
import org.uberfire.client.screens.welcome.sessions.SessionInitializersTest;

@Dependent
@WorkbenchScreen(identifier = "welcome")
public class WelcomeScreen
        extends Composite {

    @Inject
    private SessionInitializersTest initializersTest;

    @Inject
    private ClientProvaServiceTest provaService;

    private FlowPanel panel;

    @PostConstruct
    public void init() {
        panel = new FlowPanel();
        Button bit = new Button(" ** Init ** ");
        bit.addClickHandler(clickEvent -> initializersTest.initialize());
        panel.add(bit);
        Button bts = new Button(" ** toString ** ");
        bts.addClickHandler(clickEvent -> provaService.testToString());
        panel.add(bts);
        Button btdto = new Button(" ** toDTO ** ");
        btdto.addClickHandler(clickEvent -> provaService.testToDTO());
        panel.add(btdto);
        initWidget(panel);
    }

    @WorkbenchPartTitle
    public String getTitle() {
        return "Welcome";
    }

    @WorkbenchContextId
    public String getMyContextRef() {
        return "welcomeContext";
    }
}
