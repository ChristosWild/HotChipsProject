package project.editor.control;

import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import project.editor.util.EditorConstants;

public class ToolbarControl
{
	private static final String TAB = "\t";
	private static final String TAB_2 = "\t\t";
	private static final String TAB_3 = "\t\t\t";
	private static final String CTRL = "Ctrl  ";
	private static final String CTRL_PLUS = "Ctrl+";
	private static final String ALT = "Alt+";

	private static final String LBL_MENU_FILE = "File";
	private static final String LBL_SUBMENU_NEW = "New" + TAB_2 + CTRL_PLUS + "N";
	private static final String LBL_SUBMENU_OPEN = "Open" + TAB + CTRL_PLUS + "O";
	private static final String LBL_SUBMENU_SAVE = "Save" + TAB_2 + CTRL_PLUS + "S";
	private static final String LBL_SUBMENU_SAVE_AS = "Save As";
	private static final String LBL_SUBMENU_CLOSE = "Close" + TAB + ALT + "F4";

	private static final String LBL_MENU_VIEW = "View";
	private static final String LBL_SUBMENU_SHOW_TOOLBAR = "Show Toolbar" + TAB + CTRL_PLUS + "T";
	private static final String LBL_SUBMENU_ZOOM_IN = "Zoom In" + TAB_2 + CTRL + "+";
	private static final String LBL_SUBMENU_ZOOM_OUT = "Zoom Out" + TAB + CTRL + "-";
	private static final String LBL_SUBMENU_ZOOM_FIT = "Zoom To Fit";
	private static final String LBL_SUBMENU_ZOOM_RESET = "Zoom Reset" + TAB + CTRL_PLUS + "R";

	private static final String LBL_MENU_EDIT = "Edit";
	private static final String LBL_SUBMENU_UNDO = "Undo";
	private static final String LBL_SUBMENU_REDO = "Redo";
	private static final String LBL_SUBMENU_CUT = "Cut" + TAB_3 + CTRL_PLUS + "X";
	private static final String LBL_SUBMENU_COPY = "Copy" + TAB_2 + CTRL_PLUS + "C";
	private static final String LBL_SUBMENU_PASTE = "Paste" + TAB_2 + CTRL_PLUS + "V";
	private static final String LBL_SUBMENU_TECHNOLOGY_FILE = "Technology File";

	private static final String LBL_MENU_EXTRACT = "Extract";
	private static final String LBL_SUBMENU_EXTRACT_SPICE = "To SPICE";
	private static final String LBL_SUBMENU_EXTRACT_JBB = "To JBB";

	private static final String LBL_MENU_HELP = "Help";
	private static final String LBL_SUBMENU_ABOUT = "About";
	private static final String LBL_SUBMENU_HELP = "Help";

	private static final String LBL_BUTTON_DELETE = "Delete";
	private static final String LBL_BUTTON_CLEAR = "Clear";

	private static final String TOOLTIP_BUTTON_SELECT = "(S)";
	private static final String TOOLTIP_BUTTON_DRAW = "(D)";
	private static final String TOOLTIP_BUTTON_DELETE = "(Del)";

	private BorderPane root;

	// FILE
	private MenuItem menuItemNew;
	private MenuItem menuItemOpen;
	private MenuItem menuItemSave;
	private MenuItem menuItemSaveAs;
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
	private MenuItem menuItemTechnologyFile;

	// EDIT
	private MenuItem menuItemExtractSpice;
	private MenuItem menuItemExtractJbb;

	// HELP
	private MenuItem menuItemHelp;
	private MenuItem menuItemAbout;

	// BUTTONS TODO buttons for: new open save select draw delete clear
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
		menuItemSave = new MenuItem(LBL_SUBMENU_SAVE);
		menuItemSaveAs = new MenuItem(LBL_SUBMENU_SAVE_AS);
		menuItemClose = new MenuItem(LBL_SUBMENU_CLOSE);
		menuFile.getItems().addAll(menuItemNew, menuItemOpen, menuItemSave, menuItemSaveAs, menuItemClose);

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
		menuItemTechnologyFile = new MenuItem(LBL_SUBMENU_TECHNOLOGY_FILE);
		menuEdit.getItems().addAll(menuItemUndo, menuItemRedo, menuItemCut, menuItemCopy, menuItemPaste,
				menuItemTechnologyFile);

		// Extract
		final Menu menuExtract = new Menu(LBL_MENU_EXTRACT);
		menuItemExtractSpice = new MenuItem(LBL_SUBMENU_EXTRACT_SPICE);
		menuItemExtractJbb = new MenuItem(LBL_SUBMENU_EXTRACT_JBB);
		menuExtract.getItems().addAll(menuItemExtractSpice, menuItemExtractJbb);

		// HELP
		final Menu menuHelp = new Menu(LBL_MENU_HELP);
		menuItemAbout = new MenuItem(LBL_SUBMENU_ABOUT);
		menuItemHelp = new MenuItem(LBL_SUBMENU_HELP);
		menuHelp.getItems().addAll(menuItemAbout, menuItemHelp);
		menuBar.getMenus().addAll(menuFile, menuView, menuEdit, menuExtract, menuHelp);

		// BUTTONS

		toggleGroup = new ToggleGroup();
		btnSelect = new ToggleButton(null,
				new ImageView(new Image(EditorConstants.PATH_FILE_DATA + EditorConstants.PATH_IMG_MOUSE)));
		btnDraw = new ToggleButton(null,
				new ImageView(new Image(EditorConstants.PATH_FILE_DATA + EditorConstants.PATH_IMG_PENCIL)));
		btnDelete = new Button(LBL_BUTTON_DELETE);
		btnClearAll = new Button(LBL_BUTTON_CLEAR);

		btnSelect.setToggleGroup(toggleGroup);
		btnDraw.setToggleGroup(toggleGroup);
		btnSelect.setSelected(true);

		btnSelect.setTooltip(new Tooltip(TOOLTIP_BUTTON_SELECT));
		btnDraw.setTooltip(new Tooltip(TOOLTIP_BUTTON_DRAW));
		btnDelete.setTooltip(new Tooltip(TOOLTIP_BUTTON_DELETE));

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

	public MenuItem getMenuItemSave()
	{
		return menuItemSave;
	}

	public MenuItem getMenuItemSaveAs()
	{
		return menuItemSaveAs;
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

	public MenuItem getMenuItemTechnologyFile()
	{
		return menuItemTechnologyFile;
	}

	public MenuItem getMenuItemExtractSpice()
	{
		return menuItemExtractSpice;
	}

	public MenuItem getMenuItemExtractJbb()
	{
		return menuItemExtractJbb;
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
