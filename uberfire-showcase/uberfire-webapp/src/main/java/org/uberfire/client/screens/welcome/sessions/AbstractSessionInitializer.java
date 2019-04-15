/*
 * Copyright 2019 Red Hat, Inc. and/or its affiliates.
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

package org.uberfire.client.screens.welcome.sessions;

import java.util.function.Consumer;
import java.util.function.Function;

import javax.inject.Inject;

import com.google.gwt.core.client.GWT;
import elemental2.promise.Promise;
import org.jboss.errai.common.client.api.Caller;
import org.uberfire.client.promise.Promises;
import org.uberfire.preferences.shared.prova.ProvaDTO;
import org.uberfire.preferences.shared.prova.ProvaService;

public abstract class AbstractSessionInitializer implements SessionInitializer {

    @Inject
    private Caller<ProvaService> service;

    @Inject
    private Promises promises;

    @Override
    public Promise<Metadata> init(Metadata metadata) {
        GWT.log("Running [" + this.getClass().getSimpleName() + "]");
        //return runDoNothingButReturnSomeValue(getDTO(), metadata);
        return runDoNothing(getDTO())
                .then(o -> promises.resolve(metadata));
    }

    protected abstract ProvaDTO getDTO();

    protected boolean isError() {
        return false;
    }

    protected long getMillis() {
        return MILLIS;
    }

    protected Promise<Metadata> runDoNothingButReturnSomeValue(final ProvaDTO value,
                                                               final Metadata metadata) {
        return promises.promisify(service,
                                  new Function<ProvaService, Metadata>() {
                                      @Override
                                      public Metadata apply(final ProvaService provaService) {
                                          GWT.log("calling remote doNothing service...");
                                          provaService.doNothing(value, getMillis(), isError());
                                          return metadata;
                                      }
                                  });
    }

    protected Promise runDoNothing(ProvaDTO value) {
        return promises.promisify(service,
                                  new Consumer<ProvaService>() {
                                      @Override
                                      public void accept(ProvaService provaService) {
                                          GWT.log("calling remote doNothing service...");
                                          provaService.doNothing(value, getMillis(), isError());
                                      }
                                  });
    }

    protected Promise<ProvaDTO> runToDTO(String value) {
        return promises.promisify(service,
                                  new Function<ProvaService, ProvaDTO>() {
                                      @Override
                                      public ProvaDTO apply(ProvaService provaService) {
                                          GWT.log("calling remote toDTO service...");
                                          return provaService.transform(value, getMillis(), isError());
                                      }
                                  });
    }

    protected Promise<String> runToString(ProvaDTO value) {
        return promises.promisify(service,
                                  new Function<ProvaService, String>() {
                                      @Override
                                      public String apply(ProvaService provaService) {
                                          GWT.log("calling remote toString service...");
                                          return provaService.transform(value, getMillis(), isError());
                                      }
                                  });
    }
}
