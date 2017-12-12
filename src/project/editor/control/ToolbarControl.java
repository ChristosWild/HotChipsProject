package project.editor.control;

import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class ToolbarControl
{
	private static final String LBL_MENU_FILE = "File";
	private static final String LBL_SUBMENU_NEW = "New";
	private static final String LBL_SUBMENU_OPEN = "Open";
	private static final String LBL_SUBMENU_CLOSE = "Close";

	private static final String LBL_MENU_VIEW = "View";
	private static final String LBL_SUBMENU_SHOW_TOOLBAR = "Show Toolbar";
	private static final String LBL_SUBMENU_ZOOM_IN = "Zoom In";
	private static final String LBL_SUBMENU_ZOOM_OUT = "Zoom Out";
	private static final String LBL_SUBMENU_ZOOM_FIT = "Zoom To Fit";
	private static final String LBL_SUBMENU_ZOOM_RESET = "Zoom Reset";

	private static final String LBL_MENU_EDIT = "Edit";
	private static final String LBL_SUBMENU_UNDO = "Undo";
	private static final String LBL_SUBMENU_REDO = "Redo";
	private static final String LBL_SUBMENU_CUT = "Cut";
	private static final String LBL_SUBMENU_COPY = "Copy";
	private static final String LBL_SUBMENU_PASTE = "Paste";

	private static final String LBL_MENU_HELP = "Help";
	private static final String LBL_SUBMENU_ABOUT = "About";
	private static final String LBL_SUBMENU_HELP = "Help";

	private BorderPane root;

	// FILE
	private MenuItem menuItemNew;
	private MenuItem menuItemOpen;
	private MenuItem menuItemClose;

	// VIEW
	private MenuItem menuItemShowToolbar;
	private MenuItem menuItemZoomIn;
	private MenuItem menuItemZoomOut;
	private MenuItem menuItemZoomFit;
	private MenuItem menuItemZoomReset;

	// EDIT
	private MenuItem menuItemUndo;
	private MenuItem menuItemRedo;
	private MenuItem menuItemCut;
	private MenuItem menuItemCopy;
	private MenuItem menuItemPaste;

	// HELP
	private MenuItem menuItemHelp;
	private MenuItem menuItemAbout;

	// BUTTONS      new open save    select draw   delete clear
	private ToggleGroup toggleGroup;
	private ToggleButton btnSelect;
	private ToggleButton btnDraw;
	private Button btnDelete;
	private Button btnClearAll;

	public void createPartControl(final BorderPane root)
	{
		this.root = root;
		final VBox menus = new VBox();
		final MenuBar menuBar = new MenuBar();
		final ToolBar toolbar = new ToolBar();
		menus.getChildren().addAll(menuBar, toolbar);

		// FILE
		final Menu menuFile = new Menu(LBL_MENU_FILE);
		menuItemNew = new MenuItem(LBL_SUBMENU_NEW);
		menuItemOpen = new MenuItem(LBL_SUBMENU_OPEN);
		menuItemClose = new MenuItem(LBL_SUBMENU_CLOSE);
		menuFile.getItems().addAll(menuItemNew, menuItemOpen, menuItemClose);

		// VIEW
		final Menu menuView = new Menu(LBL_MENU_VIEW);
		menuItemShowToolbar = new MenuItem(LBL_SUBMENU_SHOW_TOOLBAR);
		menuItemZoomIn = new MenuItem(LBL_SUBMENU_ZOOM_IN);
		menuItemZoomOut = new MenuItem(LBL_SUBMENU_ZOOM_OUT);
		menuItemZoomFit = new MenuItem(LBL_SUBMENU_ZOOM_FIT);
		menuItemZoomReset = new MenuItem(LBL_SUBMENU_ZOOM_RESET);
		menuView.getItems().addAll(menuItemShowToolbar, menuItemZoomIn, menuItemZoomOut, menuItemZoomFit,
				menuItemZoomReset);

		// EDIT
		final Menu menuEdit = new Menu(LBL_MENU_EDIT);
		menuItemUndo = new MenuItem(LBL_SUBMENU_UNDO);
		menuItemRedo = new MenuItem(LBL_SUBMENU_REDO);
		menuItemCut = new MenuItem(LBL_SUBMENU_CUT);
		menuItemCopy = new MenuItem(LBL_SUBMENU_COPY);
		menuItemPaste = new MenuItem(LBL_SUBMENU_PASTE);
		menuEdit.getItems().addAll(menuItemUndo, menuItemRedo, menuItemCut, menuItemCopy, menuItemPaste);

		// HELP
		final Menu menuHelp = new Menu(LBL_MENU_HELP);
		menuItemAbout = new MenuItem(LBL_SUBMENU_ABOUT);
		menuItemHelp = new MenuItem(LBL_SUBMENU_HELP);
		menuHelp.getItems().addAll(menuItemAbout, menuItemHelp);
		menuBar.getMenus().addAll(menuFile, menuView, menuEdit, menuHelp);

		// BUTTONS

		toggleGroup = new ToggleGroup();
		btnSelect = new ToggleButton(null, new ImageView(new Image("file:data/mouse.png")));
		btnDraw = new ToggleButton(null, new ImageView(new Image("file:data/pencil.png")));
		btnSelect.setToggleGroup(toggleGroup);
		btnDraw.setToggleGroup(toggleGroup);
		btnSelect.setSelected(true);

		btnDelete = new Button("Delete");
		btnClearAll = new Button("Clear");
		toolbar.getItems().addAll(new Separator(), btnSelect, btnDraw, new Separator(), btnDelete, btnClearAll);

		root.setTop(menus);
	}

	public BorderPane getRoot()
	{
		return root;
	}

	public MenuItem getMenuItemNew()
	{
		return menuItemNew;
	}

	public MenuItem getMenuItemOpen()
	{
		return menuItemOpen;
	}

	public MenuItem getMenuItemClose()
	{
		return menuItemClose;
	}

	public MenuItem getMenuItemShowToolbar()
	{
		return menuItemShowToolbar;
	}

	public MenuItem getMenuItemZoomIn()
	{
		return menuItemZoomIn;
	}

	public MenuItem getMenuItemZoomOut()
	{
		return menuItemZoomOut;
	}

	public MenuItem getMenuItemZoomFit()
	{
		return menuItemZoomFit;
	}

	public MenuItem getMenuItemZoomReset()
	{
		return menuItemZoomReset;
	}

	public MenuItem getMenuItemUndo()
	{
		return menuItemUndo;
	}

	public MenuItem getMenuItemRedo()
	{
		return menuItemRedo;
	}

	public MenuItem getMenuItemCut()
	{
		return menuItemCut;
	}

	public MenuItem getMenuItemCopy()
	{
		return menuItemCopy;
	}

	public MenuItem getMenuItemPaste()
	{
		return menuItemPaste;
	}

	public MenuItem getMenuItemHelp()
	{
		return menuItemHelp;
	}

	public MenuItem getMenuItemAbout()
	{
		return menuItemAbout;
	}

	public ToggleButton getBtnSelect()
	{
		return btnSelect;
	}

	public ToggleButton getBtnDraw()
	{
		return btnDraw;
	}

	public ToggleGroup getToggleGroup()
	{
		return toggleGroup;
	}

	public Button getBtnDelete()
	{
		return btnDelete;
	}

	public Button getBtnClearAll()
	{
		return btnClearAll;
	}
}
