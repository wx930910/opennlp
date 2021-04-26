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

package opennlp.tools.ml.maxent.quasinewton;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Test;

import opennlp.tools.ml.maxent.quasinewton.LineSearch.LineSearchResult;

public class LineSearchTest {
	public Function mockFunction2() {
		Function mockInstance = mock(Function.class);
		when(mockInstance.getDimension()).thenReturn(1);
		when(mockInstance.gradientAt(any(double[].class))).thenAnswer((stubInvo) -> {
			double[] x = stubInvo.getArgument(0);
			return new double[] { 2 * x[0] };
		});
		when(mockInstance.valueAt(any(double[].class))).thenAnswer((stubInvo) -> {
			double[] x = stubInvo.getArgument(0);
			return StrictMath.pow(x[0], 2);
		});
		return mockInstance;
	}

	public Function mockFunction1() {
		Function mockInstance = mock(Function.class);
		when(mockInstance.valueAt(any(double[].class))).thenAnswer((stubInvo) -> {
			double[] x = stubInvo.getArgument(0);
			return StrictMath.pow(x[0] - 2, 2) + 4;
		});
		when(mockInstance.gradientAt(any(double[].class))).thenAnswer((stubInvo) -> {
			double[] x = stubInvo.getArgument(0);
			return new double[] { 2 * (x[0] - 2) };
		});
		when(mockInstance.getDimension()).thenReturn(1);
		return mockInstance;
	}

	private static final double TOLERANCE = 0.01;

	@Test
	public void testLineSearchDeterminesSaneStepLength1() {
		Function objectiveFunction = mockFunction1();
		// given
		double[] testX = new double[] { 0 };
		double testValueX = objectiveFunction.valueAt(testX);
		double[] testGradX = objectiveFunction.gradientAt(testX);
		double[] testDirection = new double[] { 1 };
		// when
		LineSearchResult lsr = LineSearchResult.getInitialObject(testValueX, testGradX, testX);
		LineSearch.doLineSearch(objectiveFunction, testDirection, lsr, 1.0);
		double stepSize = lsr.getStepSize();
		// then
		boolean succCond = TOLERANCE < stepSize && stepSize <= 1;
		Assert.assertTrue(succCond);
	}

	@Test
	public void testLineSearchDeterminesSaneStepLength2() {
		Function objectiveFunction = mockFunction2();
		// given
		double[] testX = new double[] { -2 };
		double testValueX = objectiveFunction.valueAt(testX);
		double[] testGradX = objectiveFunction.gradientAt(testX);
		double[] testDirection = new double[] { 1 };
		// when
		LineSearchResult lsr = LineSearchResult.getInitialObject(testValueX, testGradX, testX);
		LineSearch.doLineSearch(objectiveFunction, testDirection, lsr, 1.0);
		double stepSize = lsr.getStepSize();
		// then
		boolean succCond = TOLERANCE < stepSize && stepSize <= 1;
		Assert.assertTrue(succCond);
	}

	@Test
	public void testLineSearchFailsWithWrongDirection1() {
		Function objectiveFunction = mockFunction1();
		// given
		double[] testX = new double[] { 0 };
		double testValueX = objectiveFunction.valueAt(testX);
		double[] testGradX = objectiveFunction.gradientAt(testX);
		double[] testDirection = new double[] { -1 };
		// when
		LineSearchResult lsr = LineSearchResult.getInitialObject(testValueX, testGradX, testX);
		LineSearch.doLineSearch(objectiveFunction, testDirection, lsr, 1.0);
		double stepSize = lsr.getStepSize();
		// then
		boolean succCond = TOLERANCE < stepSize && stepSize <= 1;
		Assert.assertFalse(succCond);
		Assert.assertEquals(0.0, stepSize, TOLERANCE);
	}

	@Test
	public void testLineSearchFailsWithWrongDirection2() {
		Function objectiveFunction = mockFunction2();
		// given
		double[] testX = new double[] { -2 };
		double testValueX = objectiveFunction.valueAt(testX);
		double[] testGradX = objectiveFunction.gradientAt(testX);
		double[] testDirection = new double[] { -1 };
		// when
		LineSearchResult lsr = LineSearchResult.getInitialObject(testValueX, testGradX, testX);
		LineSearch.doLineSearch(objectiveFunction, testDirection, lsr, 1.0);
		double stepSize = lsr.getStepSize();
		// then
		boolean succCond = TOLERANCE < stepSize && stepSize <= 1;
		Assert.assertFalse(succCond);
		Assert.assertEquals(0.0, stepSize, TOLERANCE);
	}

	@Test
	public void testLineSearchFailsWithWrongDirection3() {
		Function objectiveFunction = mockFunction1();
		// given
		double[] testX = new double[] { 4 };
		double testValueX = objectiveFunction.valueAt(testX);
		double[] testGradX = objectiveFunction.gradientAt(testX);
		double[] testDirection = new double[] { 1 };
		// when
		LineSearchResult lsr = LineSearchResult.getInitialObject(testValueX, testGradX, testX);
		LineSearch.doLineSearch(objectiveFunction, testDirection, lsr, 1.0);
		double stepSize = lsr.getStepSize();
		// then
		boolean succCond = TOLERANCE < stepSize && stepSize <= 1;
		Assert.assertFalse(succCond);
		Assert.assertEquals(0.0, stepSize, TOLERANCE);
	}

	@Test
	public void testLineSearchFailsWithWrongDirection4() {
		Function objectiveFunction = mockFunction2();
		// given
		double[] testX = new double[] { 2 };
		double testValueX = objectiveFunction.valueAt(testX);
		double[] testGradX = objectiveFunction.gradientAt(testX);
		double[] testDirection = new double[] { 1 };
		// when
		LineSearchResult lsr = LineSearchResult.getInitialObject(testValueX, testGradX, testX);
		LineSearch.doLineSearch(objectiveFunction, testDirection, lsr, 1.0);
		double stepSize = lsr.getStepSize();
		// then
		boolean succCond = TOLERANCE < stepSize && stepSize <= 1;
		Assert.assertFalse(succCond);
		Assert.assertEquals(0.0, stepSize, TOLERANCE);
	}

	@Test
	public void testLineSearchFailsAtMinimum1() {
		Function objectiveFunction = mockFunction2();
		// given
		double[] testX = new double[] { 0 };
		double testValueX = objectiveFunction.valueAt(testX);
		double[] testGradX = objectiveFunction.gradientAt(testX);
		double[] testDirection = new double[] { -1 };
		// when
		LineSearchResult lsr = LineSearchResult.getInitialObject(testValueX, testGradX, testX);
		LineSearch.doLineSearch(objectiveFunction, testDirection, lsr, 1.0);
		double stepSize = lsr.getStepSize();
		// then
		boolean succCond = TOLERANCE < stepSize && stepSize <= 1;
		Assert.assertFalse(succCond);
		Assert.assertEquals(0.0, stepSize, TOLERANCE);
	}

	@Test
	public void testLineSearchFailsAtMinimum2() {
		Function objectiveFunction = mockFunction2();
		// given
		double[] testX = new double[] { 0 };
		double testValueX = objectiveFunction.valueAt(testX);
		double[] testGradX = objectiveFunction.gradientAt(testX);
		double[] testDirection = new double[] { 1 };
		// when
		LineSearchResult lsr = LineSearchResult.getInitialObject(testValueX, testGradX, testX);
		LineSearch.doLineSearch(objectiveFunction, testDirection, lsr, 1.0);
		double stepSize = lsr.getStepSize();
		// then
		boolean succCond = TOLERANCE < stepSize && stepSize <= 1;
		Assert.assertFalse(succCond);
		Assert.assertEquals(0.0, stepSize, TOLERANCE);
	}
}
