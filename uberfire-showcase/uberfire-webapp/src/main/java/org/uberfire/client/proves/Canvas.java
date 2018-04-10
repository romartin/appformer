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
import javax.annotation.PreDestroy;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;

import com.google.gwt.core.client.GWT;

@Dependent
public class Canvas {

    public String uuid;

    @PostConstruct
    public void init() {
        this.uuid = UUIDGenerator.get("canvas");
    }

    void onEvent(@Observes SomeEvent event) {
        GWT.log(this + " - EVENT OBSERVED [" + event.text + "]");
    }

    @PreDestroy
    public void destroy() {
        GWT.log("DESTROYING " + this);
        uuid = null;
    }

    @Override
    public String toString() {
        return "Canvas#" + uuid;
    }
}
