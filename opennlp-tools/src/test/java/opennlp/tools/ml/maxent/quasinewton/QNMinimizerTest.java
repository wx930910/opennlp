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

public class QNMinimizerTest {

	public Function mockFunction2() {
		Function mockInstance = mock(Function.class);
		when(mockInstance.valueAt(any(double[].class))).thenAnswer((stubInvo) -> {
			double[] x = stubInvo.getArgument(0);
			return StrictMath.pow(1 - x[0], 2) + 100 * StrictMath.pow(x[1] - StrictMath.pow(x[0], 2), 2);
		});
		when(mockInstance.getDimension()).thenReturn(2);
		when(mockInstance.gradientAt(any(double[].class))).thenAnswer((stubInvo) -> {
			double[] x = stubInvo.getArgument(0);
			double[] g = new double[2];
			g[0] = -2 * (1 - x[0]) - 400 * (x[1] - StrictMath.pow(x[0], 2)) * x[0];
			g[1] = 200 * (x[1] - StrictMath.pow(x[0], 2));
			return g;
		});
		return mockInstance;
	}

	public Function mockFunction1() {
		Function mockInstance = mock(Function.class);
		when(mockInstance.getDimension()).thenReturn(2);
		when(mockInstance.gradientAt(any(double[].class))).thenAnswer((stubInvo) -> {
			double[] x = stubInvo.getArgument(0);
			return new double[] { 2 * (x[0] - 1), 2 * (x[1] - 5) };
		});
		when(mockInstance.valueAt(any(double[].class))).thenAnswer((stubInvo) -> {
			double[] x = stubInvo.getArgument(0);
			return StrictMath.pow(x[0] - 1, 2) + StrictMath.pow(x[1] - 5, 2) + 10;
		});
		return mockInstance;
	}

	@Test
	public void testQuadraticFunction() {
		QNMinimizer minimizer = new QNMinimizer();
		Function f = mockFunction1();
		double[] x = minimizer.minimize(f);
		double minValue = f.valueAt(x);

		Assert.assertEquals(x[0], 1.0, 1e-5);
		Assert.assertEquals(x[1], 5.0, 1e-5);
		Assert.assertEquals(minValue, 10.0, 1e-10);
	}

	@Test
	public void testRosenbrockFunction() {
		QNMinimizer minimizer = new QNMinimizer();
		Function f = mockFunction2();
		double[] x = minimizer.minimize(f);
		double minValue = f.valueAt(x);

		Assert.assertEquals(x[0], 1.0, 1e-5);
		Assert.assertEquals(x[1], 1.0, 1e-5);
		Assert.assertEquals(minValue, 0, 1e-10);
	}
}
