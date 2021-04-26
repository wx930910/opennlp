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

package opennlp.tools.util;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class MockInputStreamFactory {

	public static InputStreamFactory mockInputStreamFactory1(String str) throws IOException {
		File mockFieldVariableInputSourceFile;
		String mockFieldVariableInputSourceStr;
		Charset mockFieldVariableCharset;
		InputStreamFactory mockInstance = mock(InputStreamFactory.class);
		mockFieldVariableInputSourceFile = null;
		mockFieldVariableInputSourceStr = str;
		mockFieldVariableCharset = StandardCharsets.UTF_8;
		when(mockInstance.createInputStream()).thenAnswer((stubInvo) -> {
			if (mockFieldVariableInputSourceFile != null) {
				return mockInstance.getClass().getClassLoader()
						.getResourceAsStream(mockFieldVariableInputSourceFile.getPath());
			} else {
				return new ByteArrayInputStream(mockFieldVariableInputSourceStr.getBytes(mockFieldVariableCharset));
			}
		});
		return mockInstance;
	}

	public static InputStreamFactory mockInputStreamFactory2(File file) {
		File mockFieldVariableInputSourceFile;
		String mockFieldVariableInputSourceStr;
		Charset mockFieldVariableCharset;
		InputStreamFactory mockInstance = mock(InputStreamFactory.class);
		mockFieldVariableInputSourceFile = file;
		mockFieldVariableInputSourceStr = null;
		mockFieldVariableCharset = null;
		try {
			when(mockInstance.createInputStream()).thenAnswer((stubInvo) -> {
				if (mockFieldVariableInputSourceFile != null) {
					return mockInstance.getClass().getClassLoader()
							.getResourceAsStream(mockFieldVariableInputSourceFile.getPath());
				} else {
					return new ByteArrayInputStream(mockFieldVariableInputSourceStr.getBytes(mockFieldVariableCharset));
				}
			});
		} catch (Throwable exception) {
			exception.printStackTrace();
		}
		return mockInstance;
	}
}
