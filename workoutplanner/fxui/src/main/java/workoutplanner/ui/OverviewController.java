package workoutplanner.ui;

import java.io.IOException;
import java.util.Date;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import workoutplanner.fxutil.GridBuilder;
import workoutplanner.fxutil.Overview;
import workoutplanner.fxutil.UIUtils;

/**
 * <h1>OverviewController.</h1>
 * <p>
 * The OverviewController class is responsible for managing the user
 * interface and user interactions related to the overview of the workout.
 * It allows users to save and cancel workout information.
 * </p>
 *
 * @since 1.0.0
 * @author Erlend Løken Sæveraas + Michael Brusegard
 * @version 2.0.0
 */
public class OverviewController extends Controller {
  /**
   * Imported VBox from javaFx, used to contain the viewable page.
   */
  @FXML
  private VBox saveWorkoutNameBox;
  /**
   * Imported HBox from javaFx, used to display the workout information.
   */
  @FXML
  private HBox workoutInfoBox;
  /**
   * Imported GridPane from javaFx, used to make a grid scrollable.
   */
  @FXML
  private ScrollPane scrollPane;
  /**
   * Imported Button from javaFx, used for cancel function.
   */
  @FXML
  private Button cancelButton;
  /**
   * Imported Button from javaFx, used for save function.
   */
  @FXML
  private Button saveButton;
  /**
   * Imported Button from javaFx, used for adding exercises.
   */
  @FXML
  private Button addExercisesButton;
  /**
   * Imported TextField from javaFx, used for naming workout.
   */
  @FXML
  private TextField inputName;
  /**
   * Imported TextField from javaFx, used for displaying workout name.
   */
  @FXML
  private Text name;
  /**
   * Local int variable, used to define size of display-font for workout-name.
   */
  private static final int FONTSIZE = 18;
  /**
   * Local double variable, used to define the x-position of data in the cell.
   */
  private static final double LAYOUTX = -10;
  /**
   * Local int variable, used to define the y-position of data in the cell.
   */
  private static final int LAYOUTY = 15;
  /**
   * Local boolean variable, used to kep track of whether the workout is saved or
   * not when updating the workout.
   */
  private boolean isSaved;

  /**
   * Cancels the current operation and navigates to the Homepage.
   * <p>
   * This method is responsible for canceling the current operation,
   * and it validates the input data.
   * If the validation succeeds, it navigates to the Homepage by loading
   * Home.fxml.
   * </p>
   *
   * @throws IOException If an I/O error occurs while loading Home.fxml.
   */
  @FXML
  public void cancel() throws IOException {
    if (Overview.validateOverview(false, true, this.inputName)) {
      if (Overview.checkIfCancel()) {
        getMainController().getUser().removeCurrentWorkout();
        getMainController().showFXML("Home");
      }
    }
  }

  /**
   * Saves the workout data and initializes the PlanController with the workout.
   * <p>
   * This method is responsible for saving the workout data, name and date,
   * and initializing the PlanController with the workout information.
   * The PlanController is then used to display the workout in the PlanView
   * page.
   * </p>
   *
   * @throws IOException If an I/O error occurs while loading PlanView.fxml.
   */
  @FXML
  public void save() throws IOException {
    if (Overview.validateOverview(true, false, this.inputName)) {
      getMainController().getUser().getCurrentWorkout().setName(inputName.getText());
      getMainController().getUser().getCurrentWorkout().setDate(new Date());
      getMainController().showFXML("WorkoutView");
    }
  }

  @FXML
  private void returnAllWorkouts() {
    getMainController().showFXML("WorkoutView");
  }

  @FXML
  private void addExercises() {
    int index = getMainController().getUser().getCurrentWorkoutIndex();
    getMainController().showFXML("ExerciseView", index);
  }

  /**
   * Creates and populates a grid within the ScrollPane
   * to display workout exercises.
   * <p>
   * This method sets up a GridPane to represent the overview of exercises
   * within the provided ScrollPane.
   * It populates the grid with ExerciseCell elements,
   * each displaying information about an exercise from the workout.
   * </p>
   *
   * @implNote The exercises are retrieved from the associated workout.
   */
  public void init(int workoutIndex) {
    if (workoutIndex != -1) {
      isSaved = true;
      saveWorkoutNameBox.setVisible(false);
      workoutInfoBox.setVisible(true);
      name.setText(getMainController().getUser().getWorkouts().get(workoutIndex).getName());
    } else {
      isSaved = false;
      workoutInfoBox.setVisible(false);
      saveWorkoutNameBox.setVisible(true);
    }
    saveWorkoutNameBox.managedProperty().bind(saveWorkoutNameBox.visibleProperty());
    workoutInfoBox.managedProperty().bind(workoutInfoBox.visibleProperty());
    // Clear name and scrollpane
    inputName.clear();
    scrollPane.setContent(new Group());
    // Create grid
    new GridBuilder(scrollPane,
        getMainController().getUser().getCurrentWorkout().getExercises(), this::createCell);
  }

  /**
   * Constructs an ExerciseCell with the provided Exercise instance.
   * <p>
   * This constructor initializes an ExerciseCell
   * with the given Exercise instance,
   * allowing the cell to display information related to the exercise.
   * It also triggers the creation of the data for the exercise cell.
   * </p>
   *
   * @param exerciseIndex The index of the Exercise to be displayed in the cell.
   */
  private Group createCell(int exerciseIndex) {
    Group cell = new Group();

    // Create text elements
    Text name = new Text(
        getMainController().getUser().getCurrentWorkout().getExercises().get(exerciseIndex).name() + ":");
    name.setLayoutX(LAYOUTX);
    name.setFont(new Font(FONTSIZE));
    Text sets = new Text(
        "Sets: " + getMainController().getUser().getCurrentWorkout().getExercises().get(exerciseIndex).sets());
    Text reps = new Text(
        "Reps: " + getMainController().getUser().getCurrentWorkout().getExercises().get(exerciseIndex).repMin()
            + " - " + getMainController().getUser().getCurrentWorkout().getExercises().get(exerciseIndex).repMax());
    Text weight = new Text(
        "Weight: " + getMainController().getUser().getCurrentWorkout().getExercises().get(exerciseIndex).weight()
            + "kg");

    // Move buttons
    Button moveLeftButton = new Button("Move Left");
    Button moveRightButton = new Button("Move Right");
    moveLeftButton.setOnAction(event -> move(exerciseIndex, true));
    moveRightButton.setOnAction(event -> move(exerciseIndex, false));

    // HBox for the move buttons
    HBox moveButtonBox = new HBox(moveLeftButton, moveRightButton);
    moveButtonBox.setAlignment(Pos.CENTER);
    moveButtonBox.setSpacing(10);

    // Delete button
    Button deleteButton = new Button("Delete");
    deleteButton.setOnAction(event -> delete(exerciseIndex));

    // VBox for all the buttons
    VBox buttonBox = new VBox(moveButtonBox, deleteButton);

    // Add all elements to the cell with correct layout
    cell.getChildren().addAll(name, sets, reps, weight, buttonBox);
    for (int i = 1; i < cell.getChildren().size(); i++) {
      cell.getChildren().get(i).setLayoutY(i * LAYOUTY);
    }
    return cell;
  }

  private void move(int exerciseIndex, boolean left) {
    // Move the exercise
    getMainController().getUser().getCurrentWorkout().moveExercise(exerciseIndex, left);
    // Reload the overview with the new order
    if (isSaved) {
      init(getMainController().getUser().getCurrentWorkoutIndex());
    } else {
      init(-1);
    }
  }

  private void delete(int exerciseIndex) {
    if (getMainController().getUser().getCurrentWorkout().getExerciseCount() == 1) {
      UIUtils.showAlert("Error",
          "Cannot delete the last exercise in a workout.",
          AlertType.ERROR);
      return;
    }

    if (UIUtils.showConfirmation("Delete Exercise",
        "Are you sure you want to delete "
            + getMainController().getUser().getCurrentWorkout().getExercises().get(exerciseIndex).name()
            + "? ")) {
      getMainController().getUser().getCurrentWorkout().removeExercise(exerciseIndex);

      // Reload the overview now that the exercise has been deleted
      if (isSaved) {
        init(getMainController().getUser().getCurrentWorkoutIndex());
      } else {
        init(-1);
      }
    }
  }
}