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
	private double vddVoltage;
	private double thresholdVoltage; // FIXME default threshold
	private double oxideThicknessNm; // Silicon dioxide // FIXME confirm 400nm thickness. 600? 300? 500?

	private TextField fieldLambda;
	private TextField fieldMetal;
	private TextField fieldVdd;
	private TextField fieldThreshold;
	private TextField fieldThickness;

	public TechnologyControl(final EditorController editorController)
	{
		this(editorController, 2.5, "Aluminium", 5, 1.5, 400);
	}

	public TechnologyControl(final EditorController editorController, final double lambdaUm, final String metal,
			final double vddVoltage, final double thresholdVoltage, final double oxideThicknessNm)
	{
		this.editorController = editorController;
		this.lambdaUm = lambdaUm;
		this.metal = metal;
		this.vddVoltage = vddVoltage;
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

		fieldLambda = new TextField();
		fieldLambda.setPromptText("Lambda");
		fieldLambda.setText(lambdaUm + "");

		fieldMetal = new TextField();
		fieldMetal.setPromptText("Metal");
		fieldMetal.setText(metal);

		fieldVdd = new TextField();
		fieldVdd.setPromptText("Vdd Voltage");
		fieldVdd.setText(vddVoltage + "");

		fieldThreshold = new TextField();
		fieldThreshold.setPromptText("Threshold");
		fieldThreshold.setText(thresholdVoltage + "");

		fieldThickness = new TextField();
		fieldThickness.setPromptText("Thickness");
		fieldThickness.setText(oxideThicknessNm + "");

		grid.add(new Label("Lambda:"), 0, 0);
		grid.add(fieldLambda, 1, 0);
		grid.add(new Label("um"), 2, 0);

		grid.add(new Label("Metal:"), 0, 1);
		grid.add(fieldMetal, 1, 1);

		grid.add(new Label("Vdd Voltage:"), 0, 2);
		grid.add(fieldVdd, 1, 2);
		grid.add(new Label("V"), 2, 2);

		grid.add(new Label("Transistor Threshold:"), 0, 3);
		grid.add(fieldThreshold, 1, 3);
		grid.add(new Label("V"), 2, 3);

		grid.add(new Label("Silicon Thickness:"), 0, 4);
		grid.add(fieldThickness, 1, 4);
		grid.add(new Label("nm"), 2, 4);

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
			lambdaUm = Double.parseDouble(fieldLambda.getText());
			metal = fieldMetal.getText();
			vddVoltage = Double.parseDouble(fieldVdd.getText());
			thresholdVoltage = Double.parseDouble(fieldThreshold.getText());
			oxideThicknessNm = Double.parseDouble(fieldThickness.getText());
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

	public double getVddVoltage()
	{
		return vddVoltage;
	}

	public void setVddVoltage(final double vddVoltage)
	{
		this.vddVoltage = vddVoltage;
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
