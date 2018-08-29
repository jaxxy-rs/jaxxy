/*
 * Copyright (c) 2018 The Jaxxy Authors.
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

package org.jaxxy.cors;

public class AccessControlHeaders {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    //
    // Request Headers
    //
    public static final String ORIGIN = "Origin";
    public static final String REQUEST_METHOD = "Access-Control-Request-Method";
    public static final String REQUEST_HEADERS = "Access-Control-Request-Headers";

    //
    // Response Headers
    //
    public static final String EXPOSE_HEADERS = "Access-Control-Expose-Headers";
    public static final String MAX_AGE = "Access-Control-Max-Age";
    public static final String ALLOW_METHODS = "Access-Control-Allow-Methods";
    public static final String ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
    public static final String ALLOW_HEADERS = "Access-Control-Allow-Headers";
    public static final String ALLOW_ORIGIN = "Access-Control-Allow-Origin";

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    private AccessControlHeaders() {
    }
}
