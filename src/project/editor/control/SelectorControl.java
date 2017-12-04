package project.editor.control;

import java.util.Arrays;

import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import project.editor.utils.EditorConstants;
import project.editor.utils.EditorUtils;

/**
 * Singleton class that controls the layer selection popup menu
 *
 * @author Christos
 *
 */
public final class SelectorControl
{
	private static SelectorControl instance;

	private static final String LBL_SELECT_LAYER = "Select a layer:";
	private static final int POPUP_WIDTH = 150;

	private Popup popup;
	private Stage stage;
	private boolean isUICreated = false;
	private boolean isMeantToBeVisible = true;

	// Prevents external instantiation
	private SelectorControl()
	{
	}

	public static SelectorControl getInstance()
	{
		if (instance == null)
		{
			instance = new SelectorControl();
		}
		return instance;
	}

	private void initialiseUI()
	{
		isUICreated = true;

		popup = new Popup();

		final VBox content = new VBox();
		content.setPrefSize(POPUP_WIDTH, -1);
		final GridPane topBar = new GridPane();

		final Label lblTitle = new Label(LBL_SELECT_LAYER);
		final Button btnClose = new Button("x");// null, new ImageView(new Image("file:data/Untitled.png")));

		btnClose.setOnAction(event -> {
			hide();
			isMeantToBeVisible = false;
		});

		final ListView<Object> layerList = new ListView<>(); // TODO Add images to layer list
		layerList.setItems(FXCollections.observableArrayList(
				Arrays.asList(EditorConstants.STRING_LAYER_POLYSILICON,
						EditorConstants.STRING_LAYER_DIFFUSION_P,
						EditorConstants.STRING_LAYER_DIFFUSION_N,
						EditorConstants.STRING_LAYER_METAL_ONE,
						EditorConstants.STRING_LAYER_METAL_TWO,
						EditorConstants.STRING_LAYER_METAL_THREE,
						EditorConstants.STRING_LAYER_METAL_FOUR,
						EditorConstants.STRING_LAYER_METAL_FIVE,
						EditorConstants.STRING_LAYER_VIA,
						EditorConstants.STRING_LAYER_PIN)));
		layerList.setPrefHeight(23 * layerList.getItems().size() + 2); // TODO better way of getting size

		topBar.getChildren().addAll(lblTitle, btnClose);
		GridPane.setMargin(lblTitle, new Insets(5, 0, 5, 10));
		GridPane.setMargin(btnClose, new Insets(5, 10, 5, 0));
		GridPane.setHalignment(lblTitle, HPos.LEFT);
		GridPane.setHalignment(btnClose, HPos.RIGHT);
		GridPane.setValignment(lblTitle, VPos.CENTER);
		GridPane.setValignment(btnClose, VPos.CENTER);
		GridPane.setHgrow(lblTitle, Priority.ALWAYS);

		content.getChildren().addAll(topBar, layerList);
		content.setStyle("-fx-background-color: lightgray;");

		popup.getContent().add(content);

		EditorUtils.makeWindowDraggableByNode(popup, topBar);
		EditorUtils.makeWindowDraggableByNode(popup, lblTitle);
	}

	public void show()
	{
		if (stage != null)
		{
			if (!isUICreated)
			{
				instance.initialiseUI();
				popup.setX(stage.getX() + stage.getWidth() - POPUP_WIDTH);
				popup.setY(stage.getY() + 64);
			}

			if (!popup.isShowing() && isMeantToBeVisible)
			{
				popup.show(stage);
			}
		}
	}

	public void hide()
	{
		if (popup != null && popup.isShowing())
		{
			popup.hide();
		}
	}

	public void updateOwner(final Stage stage, final boolean forceUpdate)
	{
		if (this.stage == null || forceUpdate)
		{
			this.stage = stage;
			hide();
			show();
		}
	}

	public boolean hasOwner(final Stage stage)
	{
		return popup.getOwnerWindow() == stage;
	}

	public void setMeantToBeVisible(final boolean meantToBeVisible)
	{
		this.isMeantToBeVisible = meantToBeVisible;
	}
}
