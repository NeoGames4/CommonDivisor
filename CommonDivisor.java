/**
 * The following program contains an approximate algorithm for finding a common divisor.
 * See `determineCD(…)` method for further information.
 * @author Mika Thein
 * @see #determineGCD(double, double, double, double[])
 */
public class CommonDivisor {

  // Example use
	public static final double[] values = {15.137, 6.647, 3.551, 6.792, 13.351, 7.290, 10.788, 5.637, 7.028, 2.603, 5.390, 9.319, 7.650, 4.642, 6.286, 12.299, 9.319};
	
	public static void main(String[] args) {
		System.out.println(determineCD(1, 4, 0.001, values));
	}
	
	/**
	 * The following program contains an approximate algorithm for finding a common divisor with brute force.
	 * It is neither optimized for efficiency nor checked for correctness.
	 * The algorithm does not take size ratios and uncertainties into account.
	 * It is therefore recommended that you critically check the results for plausibility yourself.
	 * I created the algorithm for my use case just to solve my problem as quickly and easily as I could.
	 * @param from Lower bound of divisors to try.
	 * @param to Upper bound of divisors to try.
	 * @param steps Step width between divisors to try.
	 * @param values The set of data from which the common divisor is to be determined.
	 * @return a common divisor.
	 */
	public static double determineCD(double from, double to, double steps, double[] values) {
		if(values.length <= 0) {
			throw new RuntimeException("The value set must not be empty.");
		}
		if(to < from) {
			throw new RuntimeException("The interval between `from` and `to` may not be empty.");
		}
		
		// The row index m handles the m-th component of the `values` array, the column index n the n-th step between `from` and `to`.
		// The cell `evaluation[m][n]` then contains the evaluation of how well `n*steps+from` matches `values[m]` as a divisor (from 0 to 1).
		double[][] evaluation = new double[values.length][(int) ((to-from)/steps)];
		
		// Iterates over each step and enters the evaluation in the evaluation matrix.
		// `i` corresponds to the observed value, `j` to the attempted divisor.
		for(int i = 0; i < values.length; i++) {
			for(int j = 0; j*steps+from < to; j++) {
				double result = values[i]/(j*steps+from); // Divides the observed value with the attempted divisor.
				double rating = Math.abs(Math.round(result)-result); // Calculates the distance to the closest integer.
				evaluation[i][j] = rating;
			}
		}
		
		// Averages the ratings of each attempted divider by calculating the arithmetic mean of each column and saving them in `averageRatings`.
		double[] averageRatings = new double[evaluation[0].length];
		for(int i = 0; i<averageRatings.length; i++) {
			int n = values.length;
			double sum = 0;
			for(int j = 0; j < n; j++) {
				sum += evaluation[j][i];
			}
			averageRatings[i] = sum/n;
		}
		
		// Calculates the smallest average rating of each attempted divisor.
		int minRatingIndex = 0;
		for(int i = 0; i<averageRatings.length; i++) {
			if(averageRatings[i] < averageRatings[minRatingIndex]) minRatingIndex = i;
		}
		
		return minRatingIndex*steps+from;
	}

}
