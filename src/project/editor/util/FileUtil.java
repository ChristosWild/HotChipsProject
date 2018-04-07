package project.editor.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.Window;
import project.editor.controller.EditorController;

public final class FileUtil
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
	
	private FileUtil() {};

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
		try
		{
			final DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
			final DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
			final Document doc = documentBuilder.parse(file);
			doc.getDocumentElement().normalize();

			final NodeList layerList = doc.getElementsByTagName(XML_ELEMENT_LAYER);
			for (int layerIndex = 0; layerIndex < layerList.getLength(); layerIndex++)
			{
				final Element layerElement = (Element) layerList.item(layerIndex);
				final String layerName = layerElement.getAttribute(XML_ATTRIBUTE_NAME);
				final Layer layer = Layer.getLayerFromName(layerName);

				if (layer == Layer.INVALID_LAYER)
				{
					System.out.println("Syntax error in file: " + layerName);
					break;
				}

				final NodeList rectangleList = layerElement.getElementsByTagName(XML_ELEMENT_RECTANGLE);
				for (int rectangleIndex = 0; rectangleIndex < rectangleList.getLength(); rectangleIndex++)
				{
					final Element rectangleElement = (Element) rectangleList.item(rectangleIndex);

					final double x = Double
							.parseDouble(rectangleElement.getElementsByTagName(XML_ELEMENT_X).item(0).getTextContent());
					final double y = Double
							.parseDouble(rectangleElement.getElementsByTagName(XML_ELEMENT_Y).item(0).getTextContent());
					final double width = Double.parseDouble(
							rectangleElement.getElementsByTagName(XML_ELEMENT_WIDTH).item(0).getTextContent());
					final double height = Double.parseDouble(
							rectangleElement.getElementsByTagName(XML_ELEMENT_HEIGHT).item(0).getTextContent());
					final double offsetX = Double.parseDouble(
							rectangleElement.getElementsByTagName(XML_ELEMENT_OFFSET_X).item(0).getTextContent());
					final double offsetY = Double.parseDouble(
							rectangleElement.getElementsByTagName(XML_ELEMENT_OFFSET_Y).item(0).getTextContent());

					LayerRectangle layerRect;

					if (layer == Layer.VIA) // TODO creates new image every time
					{
						final ImagePattern fill = new ImagePattern(
								new Image(EditorConstants.PATH_FILE_DATA + EditorConstants.PATH_IMG_VIA));
						final ImagePattern fillSelected = new ImagePattern(
								new Image(EditorConstants.PATH_FILE_DATA + EditorConstants.PATH_IMG_VIA_SELECTED));
						layerRect = new LayerRectangle(x, y, width, height, fill, fillSelected, layer);
					}
					else if (layer == Layer.PIN)
					{
						final ImagePattern fill = new ImagePattern(
								new Image(EditorConstants.PATH_FILE_DATA + EditorConstants.PATH_IMG_PIN));
						final ImagePattern fillSelected = new ImagePattern(
								new Image(EditorConstants.PATH_FILE_DATA + EditorConstants.PATH_IMG_PIN_SELECTED));
						layerRect = new LayerRectangle(x, y, width, height, fill, fillSelected, layer);
					}
					else
					{
						layerRect = new LayerRectangle(x, y, width, height, layer.getColor(), layer);
					}

					layerRect.setOffset(offsetX, offsetY);
					layerRect.setTranslateX(offsetX);
					layerRect.setTranslateY(offsetY);

					editorController.getCanvasController().addNewRectangle(layerRect);
				}
			}
		} catch (ParserConfigurationException | SAXException | IOException ex)
		{
			ex.printStackTrace();
		}
	}

	private static void save(final EditorController editorController, final String filePath)
	{
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

			// Write to file
			final TransformerFactory transformerFactory = TransformerFactory.newInstance();
			final Transformer transformer = transformerFactory.newTransformer();
			final DOMSource source = new DOMSource(doc);
			final StreamResult result = new StreamResult(new File(filePath));

			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			transformer.transform(source, result);

		} catch (ParserConfigurationException | TransformerException ex)
		{
			ex.printStackTrace();
		}
	}
}
