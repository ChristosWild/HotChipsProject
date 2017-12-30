package project.editor.utils;

import java.io.File;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;
import project.editor.controller.EditorController;

public class FileUtil
{
	private static final String TITLE_OPEN_FILE = "Open File";
	private static final String TITLE_SAVE_FILE_AS = "Save File As";

	private static final String FILE_NAME_AIC = "AIC Files";
	private static final String FILE_NAME_ALL = "All Files";
	private static final String FILE_EXTENSION_AIC = "*.aic";
	private static final String FILE_EXTENSION_All = "*.*";

	public static void openFile(final EditorController editorController, final Window window)
	{
		final FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(TITLE_OPEN_FILE);
		fileChooser.getExtensionFilters().add(new ExtensionFilter(FILE_NAME_AIC, FILE_EXTENSION_AIC));

		final File selectedFile = fileChooser.showOpenDialog(window);
		if (selectedFile != null)
		{
			editorController.setFilePath(selectedFile.getAbsolutePath());
			open(selectedFile);
		}
	}

	public static void saveFile(final EditorController editorController, final Window window)
	{
		final String currentFilePath = editorController.getFilePath();
		if (currentFilePath == null)
		{
			saveFileAs(editorController, window);
		}
		else
		{
			save(currentFilePath);
		}
	}

	public static void saveFileAs(final EditorController editorController, final Window window)
	{
		final FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(TITLE_SAVE_FILE_AS);
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter(FILE_NAME_AIC, FILE_EXTENSION_AIC),
				new ExtensionFilter(FILE_NAME_ALL, FILE_EXTENSION_All));

		final File selectedFile = fileChooser.showSaveDialog(window);
		if (selectedFile != null)
		{
			editorController.setFilePath(selectedFile.getAbsolutePath());
			save(selectedFile.getAbsolutePath());
		}
	}

	private static void open(final File file)
	{
		// TODO open file
	}

	private static void save(final String filePath)
	{
		// TODO save file
	}
}
