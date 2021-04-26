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

package opennlp.tools.ml;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import opennlp.tools.ml.model.MaxentModel;
import opennlp.tools.util.BeamSearchContextGenerator;
import opennlp.tools.util.Sequence;

public class BeamSearchTest {

	public static MaxentModel mockMaxentModel1(String[] outcomes) {
		Map<String, Integer> mockFieldVariableOutcomeIndexMap = new HashMap<>();
		double mockFieldVariableBestOutcomeProb = 0.8d;
		double mockFieldVariableOtherOutcomeProb;
		String[] mockFieldVariableOutcomes;
		MaxentModel mockInstance = mock(MaxentModel.class);
		mockFieldVariableOutcomes = outcomes;
		for (int i = 0; i < outcomes.length; i++) {
			mockFieldVariableOutcomeIndexMap.put(outcomes[i], i);
		}
		mockFieldVariableOtherOutcomeProb = 0.2d / (outcomes.length - 1);
		when(mockInstance.eval(any(String[].class))).thenAnswer((stubInvo) -> {
			String[] context = stubInvo.getArgument(0);
			double[] probs = new double[mockFieldVariableOutcomes.length];
			for (int i = 0; i < probs.length; i++) {
				if (mockFieldVariableOutcomes[i].equals(context[0])) {
					probs[i] = mockFieldVariableBestOutcomeProb;
				} else {
					probs[i] = mockFieldVariableOtherOutcomeProb;
				}
			}
			return probs;
		});
		when(mockInstance.eval(any(String[].class), any(double[].class))).thenAnswer((stubInvo) -> {
			String[] context = stubInvo.getArgument(0);
			return mockInstance.eval(context);
		});
		when(mockInstance.getNumOutcomes()).thenAnswer((stubInvo) -> {
			return mockFieldVariableOutcomes.length;
		});
		when(mockInstance.getOutcome(anyInt())).thenAnswer((stubInvo) -> {
			int i = stubInvo.getArgument(0);
			return mockFieldVariableOutcomes[i];
		});
		when(mockInstance.eval(any(String[].class), any(float[].class))).thenAnswer((stubInvo) -> {
			String[] context = stubInvo.getArgument(0);
			return mockInstance.eval(context);
		});
		return mockInstance;
	}

	public static BeamSearchContextGenerator<String> mockBeamSearchContextGenerator1(String[] outcomeSequence) {
		String[] mockFieldVariableOutcomeSequence;
		BeamSearchContextGenerator<String> mockInstance = mock(BeamSearchContextGenerator.class);
		mockFieldVariableOutcomeSequence = outcomeSequence;
		when(mockInstance.getContext(anyInt(), any(String[].class), any(String[].class), any(Object[].class)))
				.thenAnswer((stubInvo) -> {
					int index = stubInvo.getArgument(0);
					return new String[] { mockFieldVariableOutcomeSequence[index] };
				});
		return mockInstance;
	}

	/**
	 * Tests that beam search does not fail to detect an empty sequence.
	 */
	@Test
	public void testBestSequenceZeroLengthInput() {

		String[] sequence = new String[0];
		BeamSearchContextGenerator<String> cg = BeamSearchTest.mockBeamSearchContextGenerator1(sequence);

		String[] outcomes = new String[] { "1", "2", "3" };
		MaxentModel model = BeamSearchTest.mockMaxentModel1(outcomes);

		BeamSearch<String> bs = new BeamSearch<>(3, model);

		Sequence seq = bs.bestSequence(sequence, null, cg,
				(int i, String[] inputSequence, String[] outcomesSequence, String outcome) -> true);

		Assert.assertNotNull(seq);
		Assert.assertEquals(sequence.length, seq.getOutcomes().size());
	}

	/**
	 * Tests finding a sequence of length one.
	 */
	@Test
	public void testBestSequenceOneElementInput() {
		String[] sequence = { "1" };
		BeamSearchContextGenerator<String> cg = BeamSearchTest.mockBeamSearchContextGenerator1(sequence);

		String[] outcomes = new String[] { "1", "2", "3" };
		MaxentModel model = BeamSearchTest.mockMaxentModel1(outcomes);

		BeamSearch<String> bs = new BeamSearch<>(3, model);

		Sequence seq = bs.bestSequence(sequence, null, cg,
				(int i, String[] inputSequence, String[] outcomesSequence, String outcome) -> true);

		Assert.assertNotNull(seq);
		Assert.assertEquals(sequence.length, seq.getOutcomes().size());
		Assert.assertEquals("1", seq.getOutcomes().get(0));
	}

	/**
	 * Tests finding the best sequence on a short input sequence.
	 */
	@Test
	public void testBestSequence() {
		String[] sequence = { "1", "2", "3", "2", "1" };
		BeamSearchContextGenerator<String> cg = BeamSearchTest.mockBeamSearchContextGenerator1(sequence);

		String[] outcomes = new String[] { "1", "2", "3" };
		MaxentModel model = BeamSearchTest.mockMaxentModel1(outcomes);

		BeamSearch<String> bs = new BeamSearch<>(2, model);

		Sequence seq = bs.bestSequence(sequence, null, cg,
				(int i, String[] inputSequence, String[] outcomesSequence, String outcome) -> true);

		Assert.assertNotNull(seq);
		Assert.assertEquals(sequence.length, seq.getOutcomes().size());
		Assert.assertEquals("1", seq.getOutcomes().get(0));
		Assert.assertEquals("2", seq.getOutcomes().get(1));
		Assert.assertEquals("3", seq.getOutcomes().get(2));
		Assert.assertEquals("2", seq.getOutcomes().get(3));
		Assert.assertEquals("1", seq.getOutcomes().get(4));
	}

	/**
	 * Tests finding the best sequence on a short input sequence.
	 */
	@Test
	public void testBestSequenceWithValidator() {
		String[] sequence = { "1", "2", "3", "2", "1" };
		BeamSearchContextGenerator<String> cg = BeamSearchTest.mockBeamSearchContextGenerator1(sequence);

		String[] outcomes = new String[] { "1", "2", "3" };
		MaxentModel model = BeamSearchTest.mockMaxentModel1(outcomes);

		BeamSearch<String> bs = new BeamSearch<>(2, model, 0);

		Sequence seq = bs.bestSequence(sequence, null, cg,
				(int i, String[] inputSequence, String[] outcomesSequence, String outcome) -> !"2".equals(outcome));
		Assert.assertNotNull(seq);
		Assert.assertEquals(sequence.length, seq.getOutcomes().size());
		Assert.assertEquals("1", seq.getOutcomes().get(0));
		Assert.assertNotSame("2", seq.getOutcomes().get(1));
		Assert.assertEquals("3", seq.getOutcomes().get(2));
		Assert.assertNotSame("2", seq.getOutcomes().get(3));
		Assert.assertEquals("1", seq.getOutcomes().get(4));
	}
}
