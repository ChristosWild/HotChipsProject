package project.editor.controller;

import java.util.ArrayList;
import java.util.List;

import project.editor.utils.LayerRectangle;

/**
 * Singleton class that controls the clipboard for cut / copy / paste
 *
 * @author Christos
 *
 */
public class ClipboardController
{
	private static ClipboardController instance;

	private List<LayerRectangle> currentItems = new ArrayList<LayerRectangle>();

	// Singleton class - Prevents external instantiation
	private ClipboardController()
	{
	}

	public static ClipboardController getInstance()
	{
		if (instance == null)
		{
			instance = new ClipboardController();
		}
		return instance;
	}

	public void setCurrentItems(final List<LayerRectangle> currentItems)
	{
		this.currentItems.clear();

		for (final LayerRectangle layerRect : currentItems)
		{
			this.currentItems.add(layerRect.clone());
		}
	}

	public List<LayerRectangle> getCurrentItems()
	{
		return currentItems;
	}
}
