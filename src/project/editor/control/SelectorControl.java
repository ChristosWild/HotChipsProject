package project.editor.control;

import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import project.editor.utils.EditorUtils;
import project.editor.utils.Layer;

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
	private static final int POPUP_OFFSET_X = -23;
	private static final int POPUP_OFFSET_Y = 71;

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

		final GridPane topBar = new GridPane();
		initialiseTopBar(topBar);

		final ListView<String> layerList = new ListView<>();
		initialiseLayerList(layerList);

		content.getChildren().addAll(topBar, layerList);
		content.setPrefSize(POPUP_WIDTH, -1);
		content.setStyle("-fx-background-color: lightgray;");

		popup.getContent().add(content);

		EditorUtils.makeWindowDraggableByNode(popup, topBar);
	}

	private void initialiseTopBar(final GridPane topBar)
	{
		final Label lblTitle = new Label(LBL_SELECT_LAYER);
		final Button btnClose = new Button("x");// null, new ImageView(new Image("file:data/Untitled.png")));

		btnClose.setOnAction(event -> {
			hide();
			isMeantToBeVisible = false;
		});

		topBar.getChildren().addAll(lblTitle, btnClose);
		GridPane.setMargin(lblTitle, new Insets(5, 0, 5, 10));
		GridPane.setMargin(btnClose, new Insets(5, 10, 5, 0));
		GridPane.setHalignment(lblTitle, HPos.LEFT);
		GridPane.setHalignment(btnClose, HPos.RIGHT);
		GridPane.setValignment(lblTitle, VPos.CENTER);
		GridPane.setValignment(btnClose, VPos.CENTER);
		GridPane.setHgrow(lblTitle, Priority.ALWAYS);

		EditorUtils.makeWindowDraggableByNode(popup, lblTitle);
	}

	private void initialiseLayerList(final ListView<String> layerList)
	{
		layerList.setItems(FXCollections.observableArrayList(
				Layer.METAL_ONE.getDisplayName(),
				Layer.METAL_TWO.getDisplayName(),
				Layer.METAL_THREE.getDisplayName(),
				Layer.METAL_FOUR.getDisplayName(),
				Layer.METAL_FIVE.getDisplayName(),
				Layer.DIFFUSION_N.getDisplayName(),
				Layer.DIFFUSION_P.getDisplayName(),
				Layer.POLYSILICON.getDisplayName(),
				Layer.VIA.getDisplayName(),
				Layer.PIN.getDisplayName()));

		layerList.setCellFactory(list -> new ListCell<String>()
		{
			@Override
			public void updateItem(final String label, final boolean empty)
			{
				super.updateItem(label, empty);
				if (empty)
				{
					setText(null);
					setGraphic(null);
				} else
				{
					setText(label);
					setGraphic(new ImageView(Layer.getLayerImageFromName(label)));
				}
			}
		});

		layerList.setPrefHeight(23 * layerList.getItems().size() + 2); // TODO better way of getting size
		layerList.getSelectionModel().select(0);
	}

	public void show()
	{
		if (stage != null && !Double.isNaN(stage.getX()))
		{
			if (!isUICreated)
			{
				instance.initialiseUI();
				popup.setX(stage.getX() + stage.getWidth() - POPUP_WIDTH + POPUP_OFFSET_X);
				popup.setY(stage.getY() + POPUP_OFFSET_Y);
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

	public Layer getSelectedLayer()
	{
		return null; // TODO
	}
}
