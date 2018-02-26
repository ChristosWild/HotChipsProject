package project.editor.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.Window;
import project.editor.controller.EditorController;

public class FileUtil
{
	private static final String TITLE_OPEN_FILE = "Open File";
	private static final String TITLE_SAVE_FILE_AS = "Save File As";

	private static final String FILE_NAME_VLSI = "VLSI Files";
	private static final String FILE_NAME_ALL = "All Files";
	private static final String FILE_EXTENSION_VLSI = "*.vlsi";
	private static final String FILE_EXTENSION_All = "*.*";

	private static final String XML_ELEMENT_VLSI = "vlsi";
	private static final String XML_ELEMENT_LAYER = "layer";
	private static final String XML_ELEMENT_RECTANGLE = "rectangle";
	private static final String XML_ELEMENT_X = "x";
	private static final String XML_ELEMENT_Y = "y";
	private static final String XML_ELEMENT_WIDTH = "width";
	private static final String XML_ELEMENT_HEIGHT = "height";
	private static final String XML_ELEMENT_OFFSET_X = "offsetX";
	private static final String XML_ELEMENT_OFFSET_Y = "offsetY";

	private static final String XML_ATTRIBUTE_NAME = "name";

	public static void openFile(final Window window)
	{
		final FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(TITLE_OPEN_FILE);
		fileChooser.getExtensionFilters().add(new ExtensionFilter(FILE_NAME_VLSI, FILE_EXTENSION_VLSI));

		final File selectedFile = fileChooser.showOpenDialog(window);
		if (selectedFile != null)
		{
			final EditorController newController = new EditorController(new Stage());
			newController.createAndOpen();

			newController.setFilePath(selectedFile.getAbsolutePath());
			newController.setEditorTitle(selectedFile.getName());

			open(newController, selectedFile);
		}
	}

	public static void saveFile(final EditorController editorController, final Window window)
	{
		// If not previously saved, Save As
		final String currentFilePath = editorController.getFilePath();
		if (currentFilePath == null)
		{
			saveFileAs(editorController, window);
		}
		else
		{
			save(editorController, currentFilePath);
		}
	}

	public static void saveFileAs(final EditorController editorController, final Window window)
	{
		final FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(TITLE_SAVE_FILE_AS);
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter(FILE_NAME_VLSI, FILE_EXTENSION_VLSI),
				new ExtensionFilter(FILE_NAME_ALL, FILE_EXTENSION_All));

		final File selectedFile = fileChooser.showSaveDialog(window);
		if (selectedFile != null)
		{
			editorController.setFilePath(selectedFile.getAbsolutePath());
			editorController.setEditorTitle(selectedFile.getName());
			save(editorController, selectedFile.getAbsolutePath());
		}
	}

	private static void open(final EditorController editorController, final File file)
	{
		// TODO open file
	}

	private static void save(final EditorController editorController, final String filePath)
	{
		// TODO deal with save rather than save as - clear file first? delete it? handle overwriting?

		try
		{
			final List<ArrayList<LayerRectangle>> layerRects = editorController.getCanvasController()
					.getAllLayerRectangles();

			final DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			final DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			final Document doc = docBuilder.newDocument();

			// root element
			final Element rootElement = doc.createElement(XML_ELEMENT_VLSI);
			doc.appendChild(rootElement);

			// layer elements
			for (int layerIndex = 0; layerIndex < Layer.values().length - 1; layerIndex++)
			{
				final Element layer = doc.createElement(XML_ELEMENT_LAYER);
				layer.setAttribute(XML_ATTRIBUTE_NAME, Layer.getLayerFromIndex(layerIndex).getDisplayName());
				rootElement.appendChild(layer);

				// rectangle elements
				for (final LayerRectangle rect : layerRects.get(layerIndex))
				{
					final Element rectangle = doc.createElement(XML_ELEMENT_RECTANGLE);
					final Element x = doc.createElement(XML_ELEMENT_X);
					final Element y = doc.createElement(XML_ELEMENT_Y);
					final Element width = doc.createElement(XML_ELEMENT_WIDTH);
					final Element height = doc.createElement(XML_ELEMENT_HEIGHT);
					final Element offsetX = doc.createElement(XML_ELEMENT_OFFSET_X);
					final Element offsetY = doc.createElement(XML_ELEMENT_OFFSET_Y);

					x.appendChild(doc.createTextNode(rect.getX() + ""));
					y.appendChild(doc.createTextNode(rect.getY() + ""));
					width.appendChild(doc.createTextNode(rect.getWidth() + ""));
					height.appendChild(doc.createTextNode(rect.getHeight() + ""));
					offsetX.appendChild(doc.createTextNode(rect.getOffset().x + ""));
					offsetY.appendChild(doc.createTextNode(rect.getOffset().y + ""));

					rectangle.appendChild(x);
					rectangle.appendChild(y);
					rectangle.appendChild(width);
					rectangle.appendChild(height);
					rectangle.appendChild(offsetX);
					rectangle.appendChild(offsetY);
					layer.appendChild(rectangle);
				}
			}

			final TransformerFactory transformerFactory = TransformerFactory.newInstance();
			final Transformer transformer = transformerFactory.newTransformer();
			final DOMSource source = new DOMSource(doc);

			// Write to file
			final StreamResult result = new StreamResult(new File(filePath));

			// Print to console
			// final StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);

		} catch (ParserConfigurationException pce)
		{
			pce.printStackTrace();
		} catch (TransformerException tfe)
		{
			tfe.printStackTrace();
		}
	}
}
