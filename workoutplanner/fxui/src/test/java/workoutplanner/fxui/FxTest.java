package workoutplanner.fxui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.testfx.api.FxAssert.verifyThat;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.testfx.framework.junit5.ApplicationTest;
import workoutplanner.fxutil.UiUtils;


public abstract class FxTest extends ApplicationTest {
  /**
   * Local int variable, used to define the minimum width of the stage.
   */
  private static final int MINWIDTH = 600;
  /**
   * Local int variable, used to define the minimum height of the stage.
   */
  private static final int MINHEIGHT = 428;

  protected ListView listView;

  @Override
  public void start(final Stage primaryStage) throws IOException {
    // Set the app title
    primaryStage.setTitle("Workout Planner");
    // Set the application icon
    InputStream inputStream = this.getClass().getResourceAsStream("/icon.png");
    assert inputStream != null;
    Image icon = new Image(inputStream);
    primaryStage.getIcons().add(icon);
    // Set the minimum width and height for the stage
    primaryStage.setMinWidth(MINWIDTH); // Set the minimum width
    primaryStage.setMinHeight(MINHEIGHT); // Set the minimum height
    FXMLLoader fxmlLoader = new FXMLLoader(this.getClass()
            .getResource("Main.fxml"));
    primaryStage.setScene(new Scene(fxmlLoader.load()));
    primaryStage.show();
  }

  public void clickAndCheckAlert(final String title, final String message,
                                 final Alert.AlertType alertType) {
    Alert alertButton = UiUtils.getAlert();
    verifyThat("#alertButton", (button) -> !button.isDisabled());
    assertEquals(title, alertButton.getTitle());
    assertEquals(message, alertButton.getContentText());
    assertEquals(alertType, alertButton.getAlertType());
    clickOn("#alertButton");
  }

  public void clickAndCheckConfirmation(final String title,
                                        final String message) {
    Alert.AlertType alertType = Alert.AlertType.CONFIRMATION;
    Alert confirmationAlert = UiUtils.getAlert();
    assertEquals(title, confirmationAlert.getTitle());
    assertEquals(message, confirmationAlert.getContentText());
    assertEquals(alertType, confirmationAlert.getAlertType());
    Optional<ButtonType> okButton = confirmationAlert.getButtonTypes().stream()
            .filter(buttonType -> buttonType.getText().equals("OK"))
            .findFirst();
    if (okButton.isPresent()) {
      ButtonType buttonType = okButton.get();
      Button okNode = (Button)
              confirmationAlert.getDialogPane().lookupButton(buttonType);
      if (okNode != null) {
        clickOn(okNode);
      } else {
        fail("OK button not found in the Alert");
      }
    } else {
      fail("OK button not found in the Alert");
    }
  }

  public <T> T getNode(final Class<T> expectedType, final String id) {
    Node node = lookup("#" + id).query();
    if (expectedType.isAssignableFrom(node.getClass())) {
      return expectedType.cast(node);
    } else {
      fail("Couldn't find node with id: " + id + " of type "
              + expectedType.getSimpleName());
      return null;
    }
  }

}
