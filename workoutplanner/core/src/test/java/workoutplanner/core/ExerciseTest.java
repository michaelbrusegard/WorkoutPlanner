package workoutplanner.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ExerciseTest {

    private Exercise exercise;

    @BeforeEach
    public void setUp() {
        exercise = new Exercise("Back Squat", 3, 8, 12, 100);
    }

    @Test
    public void testGetName() {
        assertEquals("Back Squat", exercise.name());
    }

    @Test
    public void testGetSets() {
        assertEquals(3, exercise.sets());
    }

    @Test
    public void testGetRepMin() {
        assertEquals(8, exercise.repMin());
    }

    @Test
    public void testGetRepMax() {
        assertEquals(12, exercise.repMax());
    }

    @Test
    public void testGetWeight() {
        assertEquals(100, exercise.weight());
    }

    @Test
    public void testExerciseConstructor() {
        Exercise newExercise = new Exercise("Bench Press", 4, 6, 10, 135);
        assertEquals("Bench Press", newExercise.name());
        assertEquals(4, newExercise.sets());
        assertEquals(6, newExercise.repMin());
        assertEquals(10, newExercise.repMax());
        assertEquals(135, newExercise.weight());
    }
}
