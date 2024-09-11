package at.spengergasse.decision.domain.project;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static at.spengergasse.decision.domain.project.ProjectAssertUtil.*;

class ProjectAssertUtilTest {


    // isValidAlternativeJudgment --------------------------------------------

    @Test
    void isValidAlternativeJudgment_shouldWork_whenValuesAreValid() {

        // Given
        int[][] judgments = {{2, 3, 1}, {3, 4, 5}};
        Set<String> alternatives = Set.of("A1", "A2");
        Set<String> criteria = Set.of("C1", "C2", "C3");

        Assertions.assertDoesNotThrow(() -> {
            checkAlternativeJudgments(judgments, alternatives, criteria);
        });
    }

    @Test
    void isValidAlternativeJudgment_shouldThrowException_whenValuesAreNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            checkAlternativeJudgments(null, Set.of(), Set.of());
        });
    }

    @Test
    void isValidAlternativeJudgment_shouldThrowException_whenValuesOutOfRange() {
        int[][] judgments = {{-1, 0, 1}, {1, 6, 7}};
        Set<String> alternatives = Set.of("A1", "A2");
        Set<String> criteria = Set.of("C1", "C2", "C3");

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            checkAlternativeJudgments(judgments, alternatives, criteria);
        });
    }

    @Test
    void isValidAlternativeJudgment_shouldThrowException_whenValuesMismatchedAlternatives() {
        int[][] judgments = {{2, 3, 1}, {3, 4, 5}, {3, 4, 5}};
        Set<String> alternatives = Set.of("A1", "A2");
        Set<String> criteria = Set.of("C1", "C2", "C3");

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            checkAlternativeJudgments(judgments, alternatives, criteria);
        });
    }

    @Test
    void isValidAlternativeJudgment_shouldThrowException_whenValuesMismatchedCriteria() {
        int[][] judgments = {{2, 3}, {3, 4}};
        Set<String> alternatives = Set.of("A1", "A2");
        Set<String> criteria = Set.of("C1", "C2", "C3");

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            checkAlternativeJudgments(judgments, alternatives, criteria);
        });
    }


    // isValidCriteriaAssessment --------------------------------------------


    @Test
    void isValidCriteriaAssessment_shouldWork_whenValuesAreValid() {
        double[][] criteriaAssessment = {{1.0, 2.0, 1/3.0}, {1/2.0, 1.0, 5.0}, {3.0, 1/5.0, 1.0}};
        Set<String> criteria = Set.of("C1", "C2", "C3");

        checkCriteriaAssessments(criteriaAssessment, criteria);
    }

    @Test
    void isValidCriteriaAssessment_shouldThrowException_whenValuesAreNull() {
        Set<String> criteria = Set.of("C1", "C2");
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            checkCriteriaAssessments(null, criteria);
        });
    }

    @Test
    void isValidCriteriaAssessment_shouldThrowException_whenValuesAreEmpty() {
        Set<String> criteria = Set.of("C1", "C2");
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            checkCriteriaAssessments(new double[0][0], criteria);
        });
    }

    @Test
    void isValidCriteriaAssessment_shouldThrowException_whenNotSquareMatrix() {
        double[][] criteriaAssessment = {{1.0, 2.0}, {1/2.0, 1.0, 5.0}};
        Set<String> criteria = Set.of("C1", "C2", "C3");

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            checkCriteriaAssessments(criteriaAssessment, criteria);
        });
    }

    @Test
    void isValidCriteriaAssessment_shouldThrowException_whenDiagonalNotOne() {
        double[][] criteriaAssessment = {{2.0, 2.0, 1/3.0}, {1/2.0, 1.0, 5.0}, {3.0, 1/5.0, 1.0}};
        Set<String> criteria = Set.of("C1", "C2", "C3");
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            checkCriteriaAssessments(criteriaAssessment, criteria);
        });
    }

    @Test
    void isValidCriteriaAssessment_shouldThrowException_whenNotReciprocal() {
        double[][] criteriaAssessment = {{1.0, 2.0, 1/3.0}, {1/3.0, 1.0, 5.0}, {3.0, 1/5.0, 1.0}};
        Set<String> criteria = Set.of("C1", "C2", "C3");

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            checkCriteriaAssessments(criteriaAssessment, criteria);
        });
    }
}