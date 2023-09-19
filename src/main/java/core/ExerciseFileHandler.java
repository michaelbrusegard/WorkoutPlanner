package core;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ExerciseFileHandler {
    
    private static PrintStream printer;
    private static BufferedReader reader;

    public static void write(String str) {
        try {
            printer = new PrintStream("Exercises.txt");
            printer.println(str);
            printer.flush();
        } catch (Exception e) {
            //TODO: bør byttes med alert
            e.printStackTrace();
        }
    }
    
    public static List<Exercise> read() throws IOException {
        String lines = ""; 
        try {
            reader = new BufferedReader(new FileReader("Exercises.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                lines += line;
            }
        } catch (Exception e) {
            //TODO: bør byttes med alert
            e.printStackTrace();
        }
        return Workout.makeList(lines);
        }

}