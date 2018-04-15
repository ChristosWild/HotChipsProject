package project.editor.control;

import java.util.Optional;

import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import project.editor.controller.EditorController;

public class TechnologyControl
{
	private static final String TITLE_DIALOG = "Edit Technology File";

	private final EditorController editorController;

	private boolean isUICreated = false;
	private Dialog<ButtonType> dialog;

	private double lambdaUm;
	private String metal;
	private double thresholdVoltage;
	private double oxideThicknessNm; // Silicon dioxide // FIXME confirm 400nm thickness. 600? 300? 500?

	public TechnologyControl(final EditorController editorController)
	{
		this(editorController, 2.5, "Aluminium", 1.5, 400);
	}

	public TechnologyControl(final EditorController editorController, final double lambdaUm, final String metal,
			final double thresholdVoltage, final double oxideThicknessNm)
	{
		this.editorController = editorController;
		this.lambdaUm = lambdaUm;
		this.metal = metal;
		this.thresholdVoltage = thresholdVoltage;
		this.oxideThicknessNm = oxideThicknessNm;
	}


	private void initialiseUI()
	{
		dialog = new Dialog<ButtonType>();
		dialog.setTitle(TITLE_DIALOG);
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.APPLY);

		final GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 20, 20, 20));

		final TextField fieldLambda = new TextField();
		fieldLambda.setPromptText("Lambda");
		fieldLambda.setText(lambdaUm + "");

		final TextField fieldMetal = new TextField();
		fieldMetal.setPromptText("Metal");
		fieldMetal.setText(metal);

		final TextField fieldThreshold = new TextField();
		fieldThreshold.setPromptText("Threshold");
		fieldThreshold.setText(thresholdVoltage + "");

		final TextField fieldThickness = new TextField();
		fieldThickness.setPromptText("Thickness");
		fieldThickness.setText(oxideThicknessNm + "");

		grid.add(new Label("Lambda:"), 0, 0);
		grid.add(fieldLambda, 1, 0);
		grid.add(new Label("um"), 2, 0);

		grid.add(new Label("Metal:"), 0, 1);
		grid.add(fieldMetal, 1, 1);

		grid.add(new Label("Transistor threshold:"), 0, 2);
		grid.add(fieldThreshold, 1, 2);
		grid.add(new Label("V"), 2, 2);

		grid.add(new Label("Silicon thickness:"), 0, 3);
		grid.add(fieldThickness, 1, 3);
		grid.add(new Label("nm"), 2, 3);

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
			// TODO save technology file in with xml
		}
	}

	public double getLambda()
	{
		return lambdaUm;
	}

	public void setLambda(final double lambda)
	{
		this.lambdaUm = lambda;
	}

	public String getMetal()
	{
		return metal;
	}

	public void setMetal(final String metal)
	{
		this.metal = metal;
	}

	public double getThresholdVoltage()
	{
		return thresholdVoltage;
	}

	public void setThresholdVoltage(final double thresholdVoltage)
	{
		this.thresholdVoltage = thresholdVoltage;
	}

	public double getOxideThicknessNm()
	{
		return oxideThicknessNm;
	}

	public void setOxideThicknessNm(final double oxideThickness)
	{
		this.oxideThicknessNm = oxideThickness;
	}
}
