/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.catalog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Default {@link org.apache.camel.catalog.CamelComponentCatalog}.
 */
public class DefaultCamelComponentCatalog implements CamelComponentCatalog {

    private static final String COMPONENTS_CATALOG = "org/apache/camel/catalog/components.properties";
    private static final String COMPONENTS_JSON = "org/apache/camel/catalog/components";

    @Override
    public List<String> findComponentNames() {
        List<String> names = new ArrayList<String>();

        InputStream is = DefaultCamelComponentCatalog.class.getClassLoader().getResourceAsStream(COMPONENTS_CATALOG);
        if (is != null) {
            try {
                loadLines(is, names);
            } catch (IOException e) {
                // ignore
            }
        }
        return names;
    }

    @Override
    public String componentJSonSchema(String name) {
        String file = COMPONENTS_JSON + "/" + name + ".json";

        InputStream is = DefaultCamelComponentCatalog.class.getClassLoader().getResourceAsStream(file);
        if (is != null) {
            try {
                return loadText(is);
            } catch (IOException e) {
                // ignore
            }
        }

        return null;
    }

    /**
     * Loads the entire stream into memory as a String and returns it.
     * <p/>
     * <b>Notice:</b> This implementation appends a <tt>\n</tt> as line
     * terminator at the of the text.
     * <p/>
     * Warning, don't use for crazy big streams :)
     */
    private static void loadLines(InputStream in, List<String> lines) throws IOException {
        InputStreamReader isr = new InputStreamReader(in);
        try {
            BufferedReader reader = new LineNumberReader(isr);
            while (true) {
                String line = reader.readLine();
                if (line != null) {
                    lines.add(line);
                } else {
                    break;
                }
            }
        } finally {
            isr.close();
            in.close();
        }
    }

    /**
     * Loads the entire stream into memory as a String and returns it.
     * <p/>
     * <b>Notice:</b> This implementation appends a <tt>\n</tt> as line
     * terminator at the of the text.
     * <p/>
     * Warning, don't use for crazy big streams :)
     */
    public static String loadText(InputStream in) throws IOException {
        StringBuilder builder = new StringBuilder();
        InputStreamReader isr = new InputStreamReader(in);
        try {
            BufferedReader reader = new LineNumberReader(isr);
            while (true) {
                String line = reader.readLine();
                if (line != null) {
                    builder.append(line);
                    builder.append("\n");
                } else {
                    break;
                }
            }
            return builder.toString();
        } finally {
            isr.close();
            in.close();
        }
    }

}
