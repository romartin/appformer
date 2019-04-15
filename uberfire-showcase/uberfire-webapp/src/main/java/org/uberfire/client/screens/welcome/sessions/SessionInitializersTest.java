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

import java.util.Arrays;
import java.util.stream.StreamSupport;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.google.gwt.core.client.GWT;
import elemental2.promise.Promise;
import org.jboss.errai.ioc.client.api.ManagedInstance;
import org.uberfire.client.promise.Promises;
import org.uberfire.client.screens.welcome.sessions.SessionInitializer.Metadata;

@ApplicationScoped
public class SessionInitializersTest {

    @Inject
    private Promises promises;

    @Inject
    private ManagedInstance<SessionInitializer> initializerInstances;

    public void initialize() {

        GWT.log("Initializing session....");

        final Metadata metadata = () -> "001";

        Promise<Metadata>[] p = StreamSupport.stream(initializerInstances.spliterator(), false)
                .map(i -> i.init(metadata))
                .toArray(Promise[]::new);

        /*promises[0].then(o -> {
            GWT.log("Completed-1 [" + o + "]");
            return promises[1];
        }).then(o -> {
            GWT.log("Completed-2 [" + o + "]");
            return null;
        });*/

        all(metadata, p)
                .catch_(o -> {
                    GWT.log("Error1 [" + o + "]");
                    return promises.resolve(metadata);
                })
                .then(o -> {
                    GWT.log("Completed [" + o.getUUID() + "]");
                    return promises.resolve(metadata);
                })
                .catch_(o -> {
                    GWT.log("Error2 [" + o + "]");
                    return promises.resolve(metadata);
                });

        GWT.log("Initializing session method end");
    }

    private final <O> Promise<O> all(final Metadata metadata,
                                     final Promise<O>... promises) {
        return Arrays.stream(promises).reduce(new Promise<O>(new Promise.PromiseExecutorCallbackFn<O>() {
            @Override
            public void onInvoke(ResolveCallbackFn<O> resolveCallbackFn, RejectCallbackFn rejectCallbackFn) {
                resolveCallbackFn.onInvoke((O) metadata);
            }
        }), (p1, p2) -> p1.then(ignore -> p2));
    }
}
