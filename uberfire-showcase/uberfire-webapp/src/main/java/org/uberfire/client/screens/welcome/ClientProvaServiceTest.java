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

package org.uberfire.client.screens.welcome;

import java.util.function.Consumer;
import java.util.function.Function;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.google.gwt.core.client.GWT;
import elemental2.promise.Promise;
import org.jboss.errai.bus.client.api.messaging.Message;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.ErrorCallback;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.uberfire.client.promise.Promises;
import org.uberfire.preferences.shared.prova.ProvaDTO;
import org.uberfire.preferences.shared.prova.ProvaService;

@ApplicationScoped
public class ClientProvaServiceTest {

    private static long MILLIS = 1500;

    @Inject
    private Caller<ProvaService> service;

    @Inject
    private Promises promises;

    public void testToString() {
        _testToStringPromiseAsyncQueue();
    }

    private void _testToStringPromiseAsyncQueue() {

        GWT.log("BEFORE");

        final ProvaDTO instance = ProvaDTO.VALOR1;

        Promise toStringPromise = runToString(instance);

        GWT.log("AFTER");

        toStringPromise.then(value -> {
            GWT.log("_testToStringPromise1 = [" + value + "]");
            return runToDTO((String) value);
        })
                .catch_(o -> {
                    GWT.log("_testToStringPromise-error1 = [" + o.toString() + "]");
                    return promises.resolve(ProvaDTO.VALOR2);
                })
                .then(value -> {
                    ProvaDTO value1 = (ProvaDTO) value;
                    GWT.log("_testToStringPromise2 = [" + value1.name() + "]");
                    // return null;
                    return runDoNothing(value1);
                }).catch_(o -> {
            GWT.log("_testToStringPromise-error2 = [" + o.toString() + "]");
            return null;
            //return null;
        });

        GWT.log("RESOLVED");
    }

    private Promise runDoNothing(ProvaDTO value) {
        return promises.promisify(service,
                                  new Consumer<ProvaService>() {
                                      @Override
                                      public void accept(ProvaService provaService) {
                                          GWT.log("calling remote doNothing service...");
                                          provaService.doNothing(value, MILLIS, false);
                                      }
                                  });
    }

    private Promise<ProvaDTO> runToDTO(String value) {
        return promises.promisify(service,
                                  new Function<ProvaService, ProvaDTO>() {
                                      @Override
                                      public ProvaDTO apply(ProvaService provaService) {
                                          GWT.log("calling remote toDTO service...");
                                          return provaService.transform(value, MILLIS, true);
                                      }
                                  });
    }

    private Promise<String> runToString(ProvaDTO value) {
        return promises.promisify(service,
                                  new Function<ProvaService, String>() {
                                      @Override
                                      public String apply(ProvaService provaService) {
                                          GWT.log("calling remote toString service...");
                                          return provaService.transform(value, MILLIS, false);
                                      }
                                  });
    }

    private void _testToStringPromiseRaw() {
        GWT.log("BEFORE");

        Promise<String> promise = new Promise<String>(new Promise.PromiseExecutorCallbackFn<String>() {
            @Override
            public void onInvoke(ResolveCallbackFn<String> resolve, RejectCallbackFn reject) {
                GWT.log("CALLING SERVICE");
                service.call(new RemoteCallback<String>() {
                    @Override
                    public void callback(String s) {
                        GWT.log("REMOTE CALLBACK OK");
                        resolve.onInvoke(s);
                    }
                }, new ErrorCallback<Message>() {
                    @Override
                    public boolean error(Message message, Throwable throwable) {
                        GWT.log("REMOTE CALLBACK ERROR");
                        reject.onInvoke(throwable);
                        return false;
                    }
                }).transform(ProvaDTO.VALOR2, 1000, false);
            }
        });

        GWT.log("AFTER");
    }

    public void testToDTO() {

        GWT.log("BEFORE");

        service.call(new RemoteCallback<ProvaDTO>() {
            @Override
            public void callback(ProvaDTO value) {
                GWT.log("testToDTO = [" + value.name() + "]");
            }
        }, new ErrorCallback<Message>() {
            @Override
            public boolean error(Message message, Throwable throwable) {
                onError(message, throwable);
                return false;
            }
        }).transform("VALOR2", MILLIS, false);

        GWT.log("AFTER");
    }

    private static void onError(Message message, Throwable throwable) {
        GWT.log("ERROR: " + throwable.toString());
    }
}
