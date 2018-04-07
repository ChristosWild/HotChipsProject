package project.editor.control;

import java.util.Optional;

import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 * Singleton class that controls the technology file editor popup
 *
 * @author Christos
 *
 */
public class TechnologyControl // TODO not singleton any more
{
	private static final String TITLE_DIALOG = "Edit Technology File";

	private static TechnologyControl instance;

	private boolean isUICreated = false;
	private Dialog<ButtonType> dialog;

	// Singleton class - Prevents external instantiation
	private TechnologyControl()
	{
	}

	public static TechnologyControl getInstance()
	{
		if (instance == null)
		{
			instance = new TechnologyControl();
		}
		return instance;
	}

	private void initialiseUI() // TODO technology file - default material = aluminium, silicon dioxide
	{
		dialog = new Dialog<ButtonType>();
		dialog.setTitle(TITLE_DIALOG);
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.APPLY);

		final GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 20, 20, 20));

		final TextField lambda = new TextField();
		lambda.setPromptText("Lambda");
		final TextField next = new TextField();
		next.setPromptText("NEXT");

		final ColorPicker cp = new ColorPicker();

		grid.add(new Label("Lambda:"), 0, 0);
		grid.add(lambda, 1, 0);
		grid.add(new Label("microns"), 2, 0);
		grid.add(new Label("next:"), 0, 1);
		grid.add(next, 1, 1);
		grid.add(new Label("units"), 2, 1);
		grid.add(new Label("col"), 0, 2);
		grid.add(cp, 1, 2);

		dialog.getDialogPane().setContent(grid);
	}

	public void show()
	{
		if (!isUICreated)
		{
			initialiseUI();
		}

		dialog.setResultConverter(button -> button);

		final Optional<ButtonType> result = dialog.showAndWait();

		if (result.get() == ButtonType.APPLY)
		{
			// TODO write to file
		}
	}
}
