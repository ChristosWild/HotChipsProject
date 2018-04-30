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

	private boolean isUICreated = false;
	private Dialog<ButtonType> dialog;

	private String metal;
	private double lambdaUm;
	private double vddVoltage;
	private double vinVoltage;
	private double period;
	private double thresholdVoltage; // FIXME default threshold
	private double oxideThicknessNm; // Silicon dioxide // FIXME confirm 400nm thickness. 600? 300? 500?

	private TextField fieldMetal;
	private TextField fieldLambda;
	private TextField fieldVdd;
	private TextField fieldVin;
	private TextField fieldPeriod;
	private TextField fieldThreshold;
	private TextField fieldThickness;

	public TechnologyControl(final EditorController editorController)
	{
		this("Aluminium", 2.5, 5, 5, 5, 1.5, 400);
	}

	public TechnologyControl(final String metal, final double lambdaUm,
			final double vddVoltage, final double vinVoltage, final double period, final double thresholdVoltage,
			final double oxideThicknessNm)
	{
		this.metal = metal;
		this.lambdaUm = lambdaUm;
		this.vddVoltage = vddVoltage;
		this.vinVoltage = vinVoltage;
		this.period = period;
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

		fieldMetal = new TextField();
		fieldMetal.setPromptText("Metal");
		fieldMetal.setText(metal);

		fieldLambda = new TextField();
		fieldLambda.setPromptText("Lambda");
		fieldLambda.setText(lambdaUm + "");

		fieldVdd = new TextField();
		fieldVdd.setPromptText("Vdd Voltage");
		fieldVdd.setText(vddVoltage + "");

		fieldVin = new TextField();
		fieldVin.setPromptText("Vin Voltage");
		fieldVin.setText(vinVoltage + "");

		fieldPeriod = new TextField();
		fieldPeriod.setPromptText("Vin Clock Period");
		fieldPeriod.setText(period + "");

		fieldThreshold = new TextField();
		fieldThreshold.setPromptText("Threshold");
		fieldThreshold.setText(thresholdVoltage + "");

		fieldThickness = new TextField();
		fieldThickness.setPromptText("Thickness");
		fieldThickness.setText(oxideThicknessNm + "");

		grid.add(new Label("Metal:"), 0, 0);
		grid.add(fieldMetal, 1, 0);

		grid.add(new Label("Lambda:"), 0, 1);
		grid.add(fieldLambda, 1, 1);
		grid.add(new Label("um"), 2, 1); // FIXME micrometre u symbol

		grid.add(new Label("Vdd Voltage:"), 0, 2);
		grid.add(fieldVdd, 1, 2);
		grid.add(new Label("V"), 2, 2);

		grid.add(new Label("Vin Voltage:"), 0, 3);
		grid.add(fieldVin, 1, 3);
		grid.add(new Label("V"), 2, 3);

		grid.add(new Label("Vin Clock Period:"), 0, 4);
		grid.add(fieldPeriod, 1, 4);
		grid.add(new Label("s"), 2, 4);

		grid.add(new Label("Transistor Threshold:"), 0, 5);
		grid.add(fieldThreshold, 1, 5);
		grid.add(new Label("V"), 2, 5);

		grid.add(new Label("Silicon Thickness:"), 0, 6);
		grid.add(fieldThickness, 1, 6);
		grid.add(new Label("nm"), 2, 6);

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
			metal = fieldMetal.getText();
			lambdaUm = Double.parseDouble(fieldLambda.getText());
			vddVoltage = Double.parseDouble(fieldVdd.getText());
			vinVoltage = Double.parseDouble(fieldVin.getText());
			period = Double.parseDouble(fieldPeriod.getText());
			thresholdVoltage = Double.parseDouble(fieldThreshold.getText());
			oxideThicknessNm = Double.parseDouble(fieldThickness.getText());
		}
	}

	public double getLambda()
	{
		return lambdaUm * Math.pow(10, -6);
	}

	public String getMetal()
	{
		return metal;
	}

	public double getVddVoltage()
	{
		return vddVoltage;
	}

	public double getVinVoltage()
	{
		return vinVoltage;
	}

	public double getPeriod()
	{
		return period;
	}

	public double getThresholdVoltage()
	{
		return thresholdVoltage;
	}

	public double getOxideThickness()
	{
		return oxideThicknessNm * Math.pow(10, -9);
	}
}
