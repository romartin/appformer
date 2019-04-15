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

package org.uberfire.preferences.backend.prova;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.errai.bus.server.annotations.Service;
import org.uberfire.preferences.shared.prova.ProvaDTO;
import org.uberfire.preferences.shared.prova.ProvaService;

@ApplicationScoped
@Service
public class ProvaServiceImpl implements ProvaService {

    @Override
    public void doNothing(ProvaDTO p, long millis, boolean error) {
        System.out.println("doNothing");
        sleep(millis);
        handleError(error, "doNothing");
        System.out.println("doNothing [" + p.name() + "]");
    }

    @Override
    public String transform(ProvaDTO p, long millis, boolean error) {
        System.out.println("toString");
        sleep(millis);
        handleError(error, "toString");
        System.out.println("toString [" + p.name() + "]");
        return p.name();
    }

    @Override
    public ProvaDTO transform(String s, long millis, boolean error) {
        System.out.println("toDTO");
        sleep(millis);
        handleError(error, "toDTO");
        System.out.println("toDTO [" + s + "]");
        return ProvaDTO.valueOf(s);
    }

    private void handleError(boolean error, String context) {
        if (error) {
            throw new RuntimeException("Error on [" + context + "]");
        }
    }

    private static void sleep(long millis) {
        if (millis > 0) {
            try {
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
