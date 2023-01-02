/*
 * Copyright (c) 2018-2023 The Jaxxy Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jaxxy.example.service;

import org.jaxxy.example.HelloProtos;

import java.util.stream.IntStream;

public class DefaultHelloService implements HelloService {
//----------------------------------------------------------------------------------------------------------------------
// HelloService Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public HelloResponse hello(String name, int n) {
        final HelloResponse.HelloResponseBuilder builder = HelloResponse.builder();
        IntStream.range(0, n).forEach(i -> builder.greeting(String.format("Hello, %s (%d)!", name, i)));
        return builder.build();
    }

    @Override
    public HelloResponse helloSmile(String name, int n) {
        return hello(name, n);
    }

    @Override
    public HelloResponse helloYaml(String name, int n) {
        return hello(name, n);
    }

    @Override
    public HelloProtos.HelloResponse helloProto(String name, int n) {
        final HelloProtos.HelloResponse.Builder builder = HelloProtos.HelloResponse.newBuilder();
        IntStream.range(0, n).forEach(i -> builder.addGreetings(String.format("Hello, %s (%d)!", name, i)));
        return builder.build();
    }
}
