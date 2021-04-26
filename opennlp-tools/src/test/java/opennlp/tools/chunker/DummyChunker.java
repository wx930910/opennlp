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

package opennlp.tools.chunker;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;

/**
 * This dummy chunker implementation reads a file formatted as described at
 * <a hraf="http://www.cnts.ua.ac.be/conll2000/chunking/output.html/">] to
 * simulate a Chunker. The file has samples of sentences, with target and
 * predicted values.
 */
public class DummyChunker {

	public static Chunker mockChunker1(DummyChunkSampleStream aSampleStream) {
		DummyChunkSampleStream mockFieldVariableMSampleStream;
		Chunker mockInstance = mock(Chunker.class);
		mockFieldVariableMSampleStream = aSampleStream;
		when(mockInstance.chunk(any(String[].class), any(String[].class))).thenAnswer((stubInvo) -> {
			String[] toks = stubInvo.getArgument(0);
			String[] tags = stubInvo.getArgument(1);
			try {
				ChunkSample predsSample = mockFieldVariableMSampleStream.read();
				for (int i = 0; i < toks.length; i++) {
					if (!toks[i].equals(predsSample.getSentence()[i]) || !tags[i].equals(predsSample.getTags()[i])) {
						throw new RuntimeException("The streams are not sync!" + "\n expected sentence: "
								+ Arrays.toString(toks) + "\n expected tags: " + Arrays.toString(tags)
								+ "\n predicted sentence: " + Arrays.toString(predsSample.getSentence())
								+ "\n predicted tags: " + Arrays.toString(predsSample.getTags()));
					}
				}
				return predsSample.getPreds();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});
		return mockInstance;
	}

}
