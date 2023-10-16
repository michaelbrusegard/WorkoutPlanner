package workoutplanner.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import workoutplanner.core.Workout;
import workoutplanner.fxutil.Controller;
import workoutplanner.fxutil.PageLoader;
import workoutplanner.fxutil.UIUtils;

/**
 *  <h1>OverviewController.</h1>
 *  <p>
 *    The OverviewController class is responsible for managing the user
 *    interface and user interactions related to the overview of the workout.
 *    It allows users to save and cancel workout information.
 *  </p>
 *
 * @since 1.0.0
 * @author Erlend Løken Sæveraas
 * @version 1.4.0
 */
public class OverviewController implements Controller {
  /**
   * Local workout variable, used to edit a workout.
   */
  private Workout workout;
  /**
   * Local int variable, used to limit characters in Input-field.
   */
  private static final int CHARLIMIT = 20;
  /**
   * Local int variable, used to define size of display-font for workout-name.
   */
  private static final int NAMEFONTSIZE = 40;
  /**
   * Local int variable, used to define size of display-font for workout-date.
   */
  private static final int DATEFONTSIZE = 20;
  /**
   * Imported VBox from javaFx, used to contain the viewable page.
   */
  @FXML
  private VBox saveWorkoutNameBox;
  /**
   * Imported ScrollPane from javaFx, used to make a grid scrollable.
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
   * Imported TextField from javaFx, used for naming workout.
   */
  @FXML
  private TextField inpName;

  /**
   * Cancels the current operation and navigates to the Homepage.
   * <p>
   *   This method is responsible for canceling the current operation,
   *   and it validates the input data.
   *   If the validation succeeds, it navigates to the Homepage by loading
   *   Home.fxml.
   * </p>
   *
   * @throws IOException If an I/O error occurs while loading Home.fxml.
   */
  @FXML
  public void cancel() throws IOException {
    if (this.validateOverview(false, true)) {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
      PageLoader.pageLoader(loader, cancelButton);
    }
  }

  /**
   * Saves the workout data and initializes the PlanController with the workout.
   * <p>
   *   This method is responsible for saving the workout data, name and date,
   *   and initializing the PlanController with the workout information.
   *   The PlanController is then used to display the workout in the PlanView
   *   page.
   * </p>
   *
   * @throws IOException If an I/O error occurs while loading PlanView.fxml.
   */
  @FXML
  public void save() throws IOException {
    if (this.validateOverview(true, false)) {
      workout.setName(inpName.getText());
      workout.setDate(new Date());
      FXMLLoader loader = new FXMLLoader(getClass()
              .getResource("PlanView.fxml"));
      PlanController planController = (PlanController) PageLoader
              .pageLoader(loader, saveButton);
      planController.init(new ArrayList<>(List.of(workout)));
    }
  }

  private boolean validateOverview(final boolean saved, final boolean close) {
    if (inpName.getText().isEmpty() && saved) {
      UIUtils.showAlert("Empty input-field",
              "Input-field cannot be empty",
              AlertType.ERROR);
      return false;
    } else if (inpName.getText().length() >= CHARLIMIT) {
      UIUtils.showAlert("Too many characters in input-field",
              "Name shouldn't be more than 20 characters",
              AlertType.ERROR);
      return false;
    } else if (saved) {
      UIUtils.showAlert("Save successful",
              "Workout saved successfully",
              AlertType.INFORMATION);
      return false;
    } else if (close) {
      UIUtils.showAlert("Cancellation successful",
              "Workout deleted, bringing you back to home",
              AlertType.INFORMATION);
      return false;
    }
    return true;
  }

  /**
   * Initializes the OverviewController with the provided workout and
   * updates the user interface.
   * <p>
   *   This method sets the internal 'workout' field
   *   to the provided 'theWorkout' and utilizes the OverviewGridHandler
   *   to create and update the workout overview grid within the user interface.
   * </p>
   *
   * @param inputWorkout The workout to be displayed and managed in the
   *                     overview.
   */
  public void init(final Workout inputWorkout) {
    this.workout = inputWorkout;
    OverviewGridHandler ogh = new OverviewGridHandler(scrollPane, this.workout);
    ogh.createGrid();
  }

  /**
   * Loads the overview from a workout plan.
   * <p>
   *   This method is responsible for gathering the information from a workout
   *   and displaying it. It gathers the workout name and date,
   *   as well as all the exercises that pertain to that workout.
   * </p>
   */
  public void loadOverviewFromPlan() {
    saveWorkoutNameBox.getChildren().clear();
    Text name = new Text(workout.getName());
    name.setFont(new Font(NAMEFONTSIZE));
    Text date = new Text(workout.getDateAsString());
    date.setFont(new Font(DATEFONTSIZE));
    saveWorkoutNameBox.getChildren().addAll(name, date, cancelButton);
  }
}
