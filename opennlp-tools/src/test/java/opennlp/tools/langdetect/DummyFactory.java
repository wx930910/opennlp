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

package opennlp.tools.langdetect;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import opennlp.tools.ngram.NGramModel;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.util.StringList;
import opennlp.tools.util.normalizer.CharSequenceNormalizer;

public class DummyFactory extends LanguageDetectorFactory {

	public CharSequenceNormalizer mockCharSequenceNormalizer1() {
		CharSequenceNormalizer mockInstance = mock(CharSequenceNormalizer.class);
		when(mockInstance.normalize(any(CharSequence.class))).thenAnswer((stubInvo) -> {
			CharSequence text = stubInvo.getArgument(0);
			return text.toString().toUpperCase();
		});
		return mockInstance;
	}

	public DefaultLanguageDetectorContextGenerator mockDefaultLanguageDetectorContextGenerator1(int min, int max,
			CharSequenceNormalizer... normalizers) {
		DefaultLanguageDetectorContextGenerator mockInstance = spy(
				new DefaultLanguageDetectorContextGenerator(min, max, normalizers));
		doAnswer((stubInvo) -> {
			CharSequence document = stubInvo.getArgument(0);
			String[] superContext = (String[]) stubInvo.callRealMethod();
			List<String> context = new ArrayList(Arrays.asList(superContext));
			document = mockInstance.normalizer.normalize(document);
			SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
			String[] words = tokenizer.tokenize(document.toString());
			NGramModel tokenNgramModel = new NGramModel();
			if (words.length > 0) {
				tokenNgramModel.add(new StringList(words), 1, 3);
				Iterator tokenNgramIterator = tokenNgramModel.iterator();
				while (tokenNgramIterator.hasNext()) {
					StringList tokenList = (StringList) tokenNgramIterator.next();
					if (tokenList.size() > 0) {
						context.add("tg=" + tokenList.toString());
					}
				}
			}
			return context.toArray(new String[context.size()]);
		}).when(mockInstance).getContext(any(CharSequence.class));
		return mockInstance;
	}

	public DummyFactory() {
		super();
	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	public LanguageDetectorContextGenerator getContextGenerator() {
		return mockDefaultLanguageDetectorContextGenerator1(2, 5, mockCharSequenceNormalizer1());
	}
}
