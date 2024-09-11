package at.spengergasse.decision.algorithm;

import at.spengergasse.decision.domain.project.Vote;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class AHPProcessTest {

    @Test
    public void toCombinedCriteriaAssessment_shouldTransformVotesCorrectly() {
        // Given: A list of votes with criteria assessments
        List<Vote> mockVotes = List.of(
            new Vote("project-id", "voter-id-1", new double[][]{{1, 2}, {0.5, 1}}),
            new Vote("project-id", "voter-id-2", new double[][]{{1, 1}, {1, 1}})
        );

        // When: Transforming the votes into a combined criteria assessment
        double[][][] actual = Vote.toCriteriaAssessment(mockVotes);

        // Then: The combined criteria assessment should match the expected outcome
        double[][][] expected = {{{1, 2}, {0.5, 1}}, {{1, 1}, {1, 1}}};
        for (int i = 0; i < expected.length; i++) {
            for (int j = 0; j < expected[i].length; j++) {
                assertArrayEquals(expected[i][j], actual[i][j], 0.01);
            }
        }
    }


    @Test
    public void execute_shouldCalculateFinalScoresCorrectly() {
        // Given: A set of alternative judgments and criteria assessments
        int[][] alternativeJudgments = {{9, 8}, {7, 9}};
        double[][][] criteriaAssessment = {{{1, 2}, {0.5, 1}}, {{1, 1}, {1, 1}}};

        // When: Executing the AHP process with given inputs
        double[] actual = AHPProcess.execute(alternativeJudgments, criteriaAssessment);

        // Then: The final scores should match the expected outcomes
        double[] expected = {8.585, 7.83};
        assertArrayEquals(expected, actual, 0.01);
    }


    @Test
    public void aggregateCriteria_shouldReturnCorrectResult() {
        double[][][] input = {{{1, 2}, {0.5, 1}}, {{1, 1}, {1, 1}}};
        double[][] expected = {{1, 1.5}, {0.75, 1}};
        double[][] result = AHPProcess.aggregateCriteria(input);
        for (int i = 0; i < expected.length; i++) {
            assertArrayEquals(expected[i], result[i], 0.01);
        }
    }

    @Test
    public void normalizeConsensusMatrix_shouldReturnCorrectResult() {
        double[][] input = {{1, 1.5}, {0.75, 1}};
        double[][] expected = {{0.57, 0.6}, {0.43, 0.4}};
        double[][] result = AHPProcess.normalizeConsensusMatrix(input);
        for (int i = 0; i < expected.length; i++) {
            assertArrayEquals(expected[i], result[i], 0.01);
        }
    }

    @Test
    public void calculateCriteriaWeights_shouldReturnCorrectResult() {
        double[][] input = {{0.57, 0.6}, {0.43, 0.4}};
        double[] expected = {0.585, 0.415};
        double[] result = AHPProcess.calculateCriteriaWeights(input);
        assertArrayEquals(expected, result, 0.01);
    }

    @Test
    public void scoreAlternatives_shouldReturnCorrectResult() {
        double[] weights = {0.585, 0.415};
        int[][] assessments = {{9, 8}, {7, 9}};
        double[] expected = {8.585, 7.83};
        double[] result = AHPProcess.scoreAlternatives(weights, assessments);
        assertArrayEquals(expected, result, 0.01);
    }
}
