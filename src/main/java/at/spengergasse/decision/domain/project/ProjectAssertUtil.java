package at.spengergasse.decision.domain.project;

import java.util.Set;

public abstract class ProjectAssertUtil {

    // The epsilon value for comparing floating point numbers to avoid floating point arithmetic errors
    private static final double EPSILON = 0.000001;


    /**
     * Checks if the `alternativeJudgments` is valid for the project
     */
    public static void checkAlternativeJudgments(
        int[][] alternativeJudgments,
        Set<String> alternatives,
        Set<String> criteria
    ) {
        // 1. Check if the alternativeJudgments is not null or empty
        if (alternativeJudgments == null || alternativeJudgments.length == 0) {
            throw new IllegalArgumentException("The alternativeJudgments cannot be null or empty");
        }

        // 2. Check if the alternativeJudgments has the same number of rows as the number of alternatives
        if (alternativeJudgments.length != alternatives.size()) {
            throw new IllegalArgumentException(
                    "The alternativeJudgments must have the same number of rows as the number of alternatives");
        }

        // 3. Check if the alternativeJudgments has the same number of columns as the number of criteria
        if (alternativeJudgments[0].length != criteria.size()) {
            throw new IllegalArgumentException(
                    "The alternativeJudgments must have the same number of columns as the number of criteria");
        }

        // 4. Check if the alternativeJudgments has numbers between 1 and 5
        for (int[] row : alternativeJudgments) {
            for (double value : row) {
                if (value < 1 || value > 5) {
                    throw new IllegalArgumentException(
                        "The alternativeJudgments must have numbers between 1 and 5");
                }
            }
        }
    }


    /**
     * Checks if a `criteriaAssessment` aka `pairwise comparison matrix` is valid for the project
     */
    public static void checkCriteriaAssessments(
        double[][] criteriaAssessment,
        Set<String> criteria
    ) {

        // 1. Check if the criteriaAssessment is not null or empty
        if (criteriaAssessment == null || criteriaAssessment.length == 0) {
            throw new IllegalArgumentException("The criteriaAssessment cannot be null or empty");
        }

        // 2. Check if the vote has the same number of rows and columns
        if (criteriaAssessment.length != criteriaAssessment[0].length) {
            throw new IllegalArgumentException(
                "The criteriaAssessment must be a square matrix with the same number of rows and columns");
        }

        // 3. Check if the vote has the same number of rows as the number of criteria
        if (criteriaAssessment.length != criteria.size()) {
            throw new IllegalArgumentException(
                    "The criteriaAssessment must have the same number of rows as the number of criteria");
        }

        // 4. Check if this is a valid pairwise comparison matrix
        for (int i = 0; i < criteriaAssessment.length; i++) {
            for (int j = 0; j < criteriaAssessment[i].length; j++) {
                // Check if the diagonal values are 1
                if (i == j && criteriaAssessment[i][j] != 1) {
                    throw new IllegalArgumentException(
                        "The diagonal values of the criteriaAssessment must be 1");
                }
                // Check non-diagonal values are reciprocal aka `a_ij = 1/a_ji`
                if (i != j && Math.abs(criteriaAssessment[i][j] - (1 / criteriaAssessment[j][i])) > EPSILON) {
                    throw new IllegalArgumentException(
                        "The criteriaAssessment must be a pairwise comparison matrix");
                }
            }
        }
    }

}
