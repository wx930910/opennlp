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

package opennlp.tools.util.featuregen;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import opennlp.tools.namefind.TokenNameFinder;
import opennlp.tools.util.Span;

public class InSpanGeneratorTest {

	public static TokenNameFinder mockTokenNameFinder1(String theName) {
		String mockFieldVariableTheName;
		TokenNameFinder mockInstance = mock(TokenNameFinder.class);
		mockFieldVariableTheName = theName;
		when(mockInstance.find(any(String[].class))).thenAnswer((stubInvo) -> {
			String[] tokens = stubInvo.getArgument(0);
			for (int i = 0; i < tokens.length; i++) {
				if (mockFieldVariableTheName.equals(tokens[i])) {
					return new Span[] { new Span(i, i + 1, "person") };
				}
			}
			return new Span[] {};
		});
		return mockInstance;
	}

	@Test
	public void test() {

		List<String> features = new ArrayList<>();

		String[] testSentence = new String[] { "Every", "John", "has", "its", "day", "." };

		AdaptiveFeatureGenerator generator = new InSpanGenerator("john",
				InSpanGeneratorTest.mockTokenNameFinder1("John"));

		generator.createFeatures(features, testSentence, 0, null);
		Assert.assertEquals(0, features.size());

		features.clear();
		generator.createFeatures(features, testSentence, 1, null);
		Assert.assertEquals(2, features.size());
		Assert.assertEquals("john:w=dic", features.get(0));
		Assert.assertEquals("john:w=dic=John", features.get(1));
	}
}
