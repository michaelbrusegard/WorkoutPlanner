package workoutplanner.ui;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import workoutplanner.core.Exercise;
import workoutplanner.core.UserAccess;
import workoutplanner.core.Workout;

/**
 * Uses REST API to access and manipulate the user object.
 */

public class RemoteUserAccess implements UserAccess {

  private final URI baseUrl;

  private final ObjectMapper objectMapper = new ObjectMapper();

  private static final int OK_RESPONSE_CODE = 200;

  public RemoteUserAccess(final URI inputUrl) {
    baseUrl = inputUrl;
  }

  private Reader httpGetRequest(final String path) throws IOException {
    URI url = baseUrl.resolve("/user" + path);
    HttpURLConnection connection = (HttpURLConnection) url.toURL().openConnection();
    connection.setRequestProperty("Accept", "application/json");

    InputStream responseStream = connection.getInputStream();
    return new InputStreamReader(responseStream, StandardCharsets.UTF_8);
  }

  private HttpURLConnection httpPutRequest(final String path)
      throws IOException {
    URI url = baseUrl.resolve("/user" + path);
    HttpURLConnection connection = (HttpURLConnection) url.toURL().openConnection();
    connection.setDoOutput(true);
    connection.setRequestProperty("Content-Type", "application/json");
    connection.setRequestMethod("PUT");
    return connection;
  }

  private HttpURLConnection httpDeleteRequest(final String path)
      throws IOException {
    URI url = baseUrl.resolve("/user" + path);
    HttpURLConnection connection = (HttpURLConnection) url.toURL().openConnection();
    connection.setDoOutput(true);
    connection.setRequestProperty("Content-Type", "application/json");
    connection.setRequestMethod("DELETE");
    return connection;
  }

  private void handleError(final HttpURLConnection connection) throws IOException {
    int responseCode = connection.getResponseCode();

    if (responseCode == OK_RESPONSE_CODE)
      return;

    InputStream errorStream = connection.getErrorStream();

    if (errorStream != null) {
      try (BufferedReader reader = new BufferedReader(new InputStreamReader(errorStream, StandardCharsets.UTF_8))) {
        StringBuilder responseBody = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
          responseBody.append(line);
        }

        throw new RuntimeException(
            responseCode + " " + connection.getResponseMessage() + ": " + responseBody.toString());
      }
    } else {
      throw new RuntimeException(responseCode + " " + connection.getResponseMessage());
    }
  }

  @Override
  public List<String> getExerciseList() {
    try {
      Reader reader = httpGetRequest("/exercise-list");
      List<String> exercises = objectMapper.readValue(reader, new TypeReference<List<String>>() {
      });
      return exercises;
    } catch (IOException e) {
      throw new RuntimeException("Error getting exercise list.", e);
    }
  }

  @Override
  public Workout getCurrentWorkout() {
    Reader reader = null;
    try {
      reader = httpGetRequest("/current-workout");
      return objectMapper.readValue(reader, Workout.class);
    } catch (IOException e) {
      throw new RuntimeException("Error getting current workout.", e);
    }
  }

  @Override
  public List<Workout> getWorkouts() {
    try {
      Reader reader = httpGetRequest("/workouts");
      List<Workout> workouts = objectMapper.readValue(reader, new TypeReference<List<Workout>>() {
      });
      return workouts;
    } catch (IOException e) {
      throw new RuntimeException("Error getting workouts.", e);
    }
  }

  @Override
  public void setCurrentWorkout(final int workoutIndex) {
    try {
      HttpURLConnection connection = httpPutRequest("/current-workout/"
          + workoutIndex);
      handleError(connection);
    } catch (IOException e) {
      throw new RuntimeException("Error setting current workout.", e);
    }
  }

  @Override
  public void addExerciseToCurrentWorkout(final String inputName,
      final int sets,
      final int repMin,
      final int repMax,
      final int weight) {
    try {
      HttpURLConnection connection = httpPutRequest("/current-workout/exercise");
      String json = objectMapper.writeValueAsString(new Exercise(inputName, sets, repMin, repMax, weight));
      OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
      writer.write(json);
      writer.flush();
      writer.close();
      handleError(connection);
    } catch (IOException e) {
      throw new RuntimeException("Error adding exercise to current workout.", e);
    }
  }

  @Override
  public void moveExerciseInCurrentWorkout(final int exerciseIndex,
      final boolean left) {
    try {
      HttpURLConnection connection = httpPutRequest("/current-workout/exercise/"
          + exerciseIndex + "?left=" + left);
      handleError(connection);
    } catch (IOException e) {
      throw new RuntimeException("Error moving exercise in current workout.", e);
    }
  }

  @Override
  public void saveCurrentWorkout(final String name, final String date) {
    try {
      HttpURLConnection connection = httpPutRequest("/current-workout/save?name="
          + URLEncoder.encode(name, StandardCharsets.UTF_8) + "&date="
          + URLEncoder.encode(date, StandardCharsets.UTF_8));
      handleError(connection);
    } catch (IOException e) {
      throw new RuntimeException("Error saving current workout.", e);
    }
  }

  @Override
  public void removeWorkout(final int workoutIndex) {
    try {
      HttpURLConnection connection = httpDeleteRequest("/workout/"
          + workoutIndex);
      handleError(connection);
    } catch (IOException e) {
      throw new RuntimeException("Error removing workout.", e);
    }
  }

  @Override
  public void removeCurrentWorkout() {
    try {
      HttpURLConnection connection = httpDeleteRequest("/current-workout");
      handleError(connection);
    } catch (IOException e) {
      throw new RuntimeException("Error removing current workout.", e);
    }
  }

  @Override
  public void removeExerciseFromCurrentWorkout(final int exerciseIndex) {
    try {
      HttpURLConnection connection = httpDeleteRequest("/current-workout/exercise/" + exerciseIndex);
      handleError(connection);
    } catch (IOException e) {
      throw new RuntimeException("Error removing exercise from current workout.", e);
    }
  }
}
