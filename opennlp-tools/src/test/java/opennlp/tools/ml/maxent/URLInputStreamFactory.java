/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package opennlp.tools.ml.maxent;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URL;

import opennlp.tools.util.InputStreamFactory;

public class URLInputStreamFactory {

	public static InputStreamFactory mockInputStreamFactory1(URL url) {
		URL mockFieldVariableUrl;
		InputStreamFactory mockInstance = mock(InputStreamFactory.class);
		mockFieldVariableUrl = url;
		try {
			when(mockInstance.createInputStream()).thenAnswer((stubInvo) -> {
				return mockFieldVariableUrl.openStream();
			});
		} catch (Throwable exception) {
			exception.printStackTrace();
		}
		return mockInstance;
	}

}
