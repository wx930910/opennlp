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

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.spy;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import opennlp.tools.ml.model.Event;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;

public class FootballEventStream {
	public static ObjectStream<Event> mockObjectStream1() throws IOException {
		ObjectStream<String> mockFieldVariableTextStream;
		ObjectStream<Event> mockInstance = spy(ObjectStream.class);
		mockFieldVariableTextStream = new PlainTextByLineStream(
				URLInputStreamFactory.mockInputStreamFactory1(
						mockInstance.getClass().getResource("/opennlp/tools/ml/maxent/football.dat")),
				StandardCharsets.US_ASCII);
		doAnswer((stubInvo) -> {
			String line = mockFieldVariableTextStream.read();
			if (line == null)
				return null;
			String[] tokens = line.split("\\s+");
			return new Event(tokens[tokens.length - 1], Arrays.copyOf(tokens, tokens.length - 1));
		}).when(mockInstance).read();
		doAnswer((stubInvo) -> {
			mockFieldVariableTextStream.reset();
			return null;
		}).when(mockInstance).reset();
		return mockInstance;
	}
}
