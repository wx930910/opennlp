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

package opennlp.tools.formats;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Objects;

import opennlp.tools.util.InputStreamFactory;

public class ResourceAsStreamFactory {

	public static InputStreamFactory mockInputStreamFactory1(Class<?> clazz, String name) {
		Class<?> mockFieldVariableClazz;
		String mockFieldVariableName;
		InputStreamFactory mockInstance = mock(InputStreamFactory.class);
		mockFieldVariableClazz = Objects.requireNonNull(clazz, "callz must not be null");
		mockFieldVariableName = Objects.requireNonNull(name, "name must not be null");
		try {
			when(mockInstance.createInputStream()).thenAnswer((stubInvo) -> {
				return mockFieldVariableClazz.getResourceAsStream(mockFieldVariableName);
			});
		} catch (Throwable exception) {
			exception.printStackTrace();
		}
		return mockInstance;
	}
}
