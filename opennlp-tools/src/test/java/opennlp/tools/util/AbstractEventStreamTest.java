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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import opennlp.tools.ml.model.Event;

/**
 * Tests for the {@link AbstractEventStream} class.
 */
public class AbstractEventStreamTest {

	public AbstractEventStream<RESULT> mockAbstractEventStream1(ObjectStream<RESULT> samples) {
		AbstractEventStream<RESULT> mockInstance = mock(AbstractEventStream.class,
				withSettings().useConstructor(samples).defaultAnswer(Mockito.CALLS_REAL_METHODS));
		doAnswer((stubInvo) -> {
			RESULT sample = stubInvo.getArgument(0);
			if (RESULT.EVENTS.equals(sample)) {
				List<Event> events = new ArrayList<>();
				events.add(new Event("test", new String[] { "f1", "f2" }));
				return events.iterator();
			} else if (RESULT.EMPTY.equals(sample)) {
				List<Event> emptyList = Collections.emptyList();
				return emptyList.iterator();
			} else {
				Assert.fail();
				return null;
			}
		}).when(mockInstance).createEvents(any(RESULT.class));
		return mockInstance;
	}

	/**
	 * Checks if the {@link AbstractEventStream} behavior is correctly if the
	 * {@link AbstractEventStream#createEvents(Object)} method return iterators with
	 * events and empty iterators.
	 */
	@Test
	public void testStandardCase() throws IOException {

		List<RESULT> samples = new ArrayList<>();
		samples.add(RESULT.EVENTS);
		samples.add(RESULT.EMPTY);
		samples.add(RESULT.EVENTS);

		try (AbstractEventStream<RESULT> eventStream = mockAbstractEventStream1(
				new CollectionObjectStream<>(samples))) {
			int eventCounter = 0;

			while (eventStream.read() != null) {
				eventCounter++;
			}

			Assert.assertEquals(2, eventCounter);
		}
	}

	/**
	 * Checks if the {@link AbstractEventStream} behavior is correctly if the
	 * {@link AbstractEventStream#createEvents(Object)} method only returns empty
	 * iterators.
	 */
	@Test
	public void testEmtpyEventStream() throws IOException {
		List<RESULT> samples = new ArrayList<>();
		samples.add(RESULT.EMPTY);

		try (AbstractEventStream<RESULT> eventStream = mockAbstractEventStream1(
				new CollectionObjectStream<>(samples))) {
			Assert.assertNull(eventStream.read());

			// now check if it can handle multiple empty event iterators
			samples.add(RESULT.EMPTY);
			samples.add(RESULT.EMPTY);
		}
		try (AbstractEventStream<RESULT> eventStream = mockAbstractEventStream1(
				new CollectionObjectStream<>(samples))) {
			Assert.assertNull(eventStream.read());
		}
	}

	private enum RESULT {
		EVENTS, EMPTY
	}
}
