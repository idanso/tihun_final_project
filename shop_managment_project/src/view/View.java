package view;

import controller.Controller;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import model.EMassageFromShop;
import model.EProductSortType;
import model.Model;
import model.NotificationHandler;

public class View extends Application {

	private Stage window;
	
	private Button backB, 
				   submitAddProductB, 
				   submitSorting,
				   submitSearchB,
				   addProductMainScene, 
	 			   showAllProductsMainScene, 
	 			   searchForRemoveMainScene,
	 			   sendNotificationsMainScene, 
	 			   undoFunctionMainScene,
	 			   bShowProfitSummaryScene,
	 			   bRemoveAllProducts,
	 			   bCloseShowMassagesWindows;
	
	private TextField productName, 
	                  productNumber, 
	                  priceForShop, 
	                  priceForCostumer, 
	                  costumerName, 
	                  phoneNumber,
	                  deleteProduct;

	private ToggleGroup TGSorting;

	private RadioButton notificationForCostumer, 
						sortUp, 
						sortDown, 
						sortOrder;
	
	
	private Label head, 
				  headAddProduct,
				  profitSummaryLabel, 
				  profitSummaryHeadLabel,
				  searchToRemove,
				  showProductsLabel,
				  allProductsLabel,
				  receivedMassagesL ;
	
	private VBox vboxSorting, vboxSearch, vboxShowAllProducts ;
	private HBox hboxAddProduct, hboxSearchProduct;
	private ScrollPane massagesScrollPane, showAllProduct;
	
	private GridPane gridAddProduct;
	
	private Alert alert;
	private TextFlow massagesTextFlow, showAllProductsFlow;
	private NotificationHandler nHandler;
	private TextFlow profitSummaryTextFlow;
	private EMassageFromShop Emassage;
	
	static Controller controller;
	
	private final static String FILE_NAME = "allProducts.txt";

	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage mainWindow) throws Exception {
		Model model = new Model(FILE_NAME);
		controller = new Controller(this, model);

		this.createButtons();
		this.createRadioButtons();
		this.createTextFields();
		
		this.setActions();
		
		alert = new Alert(AlertType.NONE);
		window = mainWindow;
		window.setTitle("Main window");
		mainScene();
		window.show();
	}


	public void mainScene() {
		
		setSortingOptionsLabelText();
		
		
		setVbox();
		submitSorting.setOnAction(e -> choosSorting());
		window.setScene(new Scene(vboxSorting));
	}

	public void mainWindow() {
		
		addProductMainScene.setOnAction(e -> addProductsScene());
		
		showAllProductsMainScene.setOnAction(e -> showProductsScene());
		
		searchForRemoveMainScene.setOnAction(e -> searchProductToRemove());
		
		sendNotificationsMainScene.setOnAction(e -> showReceivedMassages());
		
		bRemoveAllProducts.setOnAction(e -> popupWindowMassage(controller.deleteAllProducts())); 
		
		bShowProfitSummaryScene.setOnAction(e -> showProfitSummaryScene());
		
		undoFunctionMainScene.setOnAction(e -> popupWindowMassage(controller.undoProduct()));
		
		Button exit = new Button("EXIT");
		exit.setOnAction(e -> {
			try {
				stop();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			System.exit(0);
		});
		VBox vbox = new VBox();
		vbox.getChildren().addAll(addProductMainScene, 
								  showAllProductsMainScene, 
								  searchForRemoveMainScene, 
								  bRemoveAllProducts, 
								  sendNotificationsMainScene, 
								  bShowProfitSummaryScene, 
								  undoFunctionMainScene, 
								  exit);
		vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(20, 80, 20, 80));
		vbox.setSpacing(10);
		window.setScene(new Scene(vbox));
	}

	public void addProductsScene() {
		gridAddProduct = new GridPane();
		Button clearB = new Button("Clear");
		hboxAddProduct = new HBox(8);
		hboxAddProduct.getChildren().addAll(submitAddProductB, clearB);

		setTextForAddProduct();
		setGridAddProduct();
		
		submitAddProductB.setOnAction(e -> addProduct());
		clearB.setOnAction(e -> clear());

		gridAddProduct.setAlignment(Pos.BASELINE_CENTER);
		window.setScene(new Scene(gridAddProduct, 500, 300));

	}

	public void showReceivedMassages() {
		Stage massageStage = new Stage();
		VBox receivedMassagesVBox = new VBox(20);
		receivedMassagesVBox.setPadding(new Insets(10, 10, 10, 10));
		setTheWindow();
		bCloseShowMassagesWindows.setOnAction(e -> massageStage.close());
		receivedMassagesVBox.getChildren().addAll(receivedMassagesL, massagesScrollPane, bCloseShowMassagesWindows); // need to add backB
		massageStage.setScene(new Scene(receivedMassagesVBox));
		massageStage.show();
		Platform.runLater(() -> controller.sendNotification());
	}

	public void showProductsScene() {
		vboxShowAllProducts = new VBox(10);
		setWindowLabelsAndTextFlow();
		vboxShowAllProducts.getChildren().addAll(showProductsLabel,showAllProduct, backB);
		controller.printAllProducts();
		window.setScene(new Scene(vboxShowAllProducts, 900, 400));
	}

	public void searchProductToRemove() {
		// search product to remove
		vboxSearch = new VBox(10);
		
	    setVboxSearchToRemove();
	    setHboxForButtons();
	    
		vboxSearch.getChildren().addAll(searchToRemove, deleteProduct, hboxSearchProduct);
		submitSearchB.setOnAction(e -> {
			searchToRemoveFunction();
			deleteProduct.clear();
		});
		window.setScene(new Scene(vboxSearch, 950, 300));
	}

	public void showProfitSummaryScene() {
		
		VBox ProfitSummaryVBox = new VBox(20);
		ProfitSummaryVBox.setPadding(new Insets(10, 10, 10, 10));
		createLabelsTextForSummary();	
		profitSummaryTextFlow.getChildren().add(profitSummaryLabel);
		ScrollPane massagesScrollPane = new ScrollPane(profitSummaryTextFlow);
		ProfitSummaryVBox.getChildren().addAll(profitSummaryHeadLabel,massagesScrollPane, backB);
		controller.getProfitSummary();
		window.setScene(new Scene(ProfitSummaryVBox));
	}


	@Override // do the final things after exit the program
	public void stop() throws Exception {
		controller.closeShopFile();
	}

	public void clear() {
		productName.clear();
		productNumber.clear();
		priceForShop.clear();
		priceForCostumer.clear();
		costumerName.clear();
		phoneNumber.clear();
		notificationForCostumer.setSelected(false);
	}

	public String getProductName() {
		return productName.getText();
	}

	public String getProductNumber() {
		return productNumber.getText();
	}
	
	public Label getProfitSummaryLabel() {
		return profitSummaryLabel;
	}

	public int getPriceForShop() {
		int price = -1;
		try {
			price = Integer.parseInt(priceForShop.getText());
		} catch (Exception e) {
			price=-1;
			popupWindowMassage(Emassage.FAILE);
		}
		return price;
	}

	public int getPriceForCostumer() {
		int price = -1;
		try {
			price = Integer.parseInt(priceForCostumer.getText());
		} catch (Exception e) {
			price =-1;
			popupWindowMassage(Emassage.FAILE);
		}
		return price;
	}

	public String getCostumerName() {
		return costumerName.getText();
	}

	public String getCostumerPhoneNumber() {
		return phoneNumber.getText();
	}

	public boolean getNewsCostumer() {
		return notificationForCostumer.isSelected();
	}

	public EProductSortType getTypeOfSorting() {
		if (sortUp.isSelected() == true)
			return EProductSortType.FROM_DOWN;
		else if (sortDown.isSelected() == true) 
			return EProductSortType.FROM_UP;
		else
			return EProductSortType.ENTER_ORDER;
	}

	public String getDeleteProductNumber() {
		return deleteProduct.getText();
	}
	
	public void setTheWindow() {
		receivedMassagesL = new Label("Received Customers Massages");
		receivedMassagesL.setFont(new Font("Arial", 22));
		nHandler = new NotificationHandler();
		nHandler.setFont(new Font("Arial", 16));
		massagesTextFlow = new TextFlow();
		massagesTextFlow.setPrefSize(400, 300);
		massagesTextFlow.getChildren().add(getnHandler());
		massagesScrollPane = new ScrollPane(massagesTextFlow);
		bCloseShowMassagesWindows = new Button();
		bCloseShowMassagesWindows.setText("Close");
	}


	public void addProduct() {
		if (productName.getText().isEmpty() || productNumber.getText().isEmpty() || priceForShop.getText().isEmpty()
				|| priceForCostumer.getText().isEmpty() || costumerName.getText().isEmpty()
				|| phoneNumber.getText().isEmpty()) {
			popupWindowMassage(Emassage.FAILE);
		} else {
			
			if(controller.addProduct() == Emassage.FAILE) {
				popupWindowMassage(Emassage.FAILE);
			}
			else {
				popupWindowMassage(Emassage.SUCCEES);
				clear();
			}
		}
	}

	public void choosSorting() {
		if (sortUp.isSelected() == false && sortDown.isSelected() == false && sortOrder.isSelected() == false) {
			popupWindowMassage(Emassage.FAILE);
		} else {
			controller.createProductsMap();
			mainWindow();
		}
	}
	

	public NotificationHandler getnHandler() {
		return nHandler;
	}
	
	public void setWindowLabelsAndTextFlow(){
		showProductsLabel = new Label("All the products:");
		showProductsLabel.setFont(new Font("Arial", 22));
		allProductsLabel = new Label();
		allProductsLabel.setFont(new Font("Consolas", 16));
		showAllProductsFlow = new TextFlow();
		showAllProductsFlow.setPrefSize(400, 300);
		showAllProductsFlow.getChildren().add(allProductsLabel);
		showAllProduct = new ScrollPane(showAllProductsFlow);
	}
	
	public Label getAllProductsLabel() {
		return allProductsLabel;
	}

	private void createButtons() {
		submitSorting = new Button("Submit");
		submitAddProductB = new Button("Submit");
		backB = new Button("<<Back");
		addProductMainScene = new Button("Add product");
		showAllProductsMainScene = new Button("Show all the products");
		searchForRemoveMainScene = new Button("remove product");
		bRemoveAllProducts = new Button("remove all products");
		bShowProfitSummaryScene = new Button("show profit summary");
		sendNotificationsMainScene = new Button("send notification to customer");
		undoFunctionMainScene = new Button("Undo");
	}
	
	private void createTextFields() {
		productName = new TextField();
		productNumber = new TextField();
		priceForShop = new TextField();
		priceForCostumer = new TextField();
		costumerName = new TextField();
		phoneNumber = new TextField();
	}
	

	private void createRadioButtons() {
		sortUp = new RadioButton();
		sortDown = new RadioButton();
		sortOrder = new RadioButton();
	}

	private void setActions() {
		backB.setOnAction(e -> mainWindow());
		undoFunctionMainScene.setOnAction(e -> controller.undoProduct());
	}
	
	private void createLabelsTextForSummary() {
		profitSummaryHeadLabel = new Label("Profit Summary");
		profitSummaryLabel = new Label();
		profitSummaryTextFlow = new TextFlow();
		profitSummaryHeadLabel.setFont(new Font("Arial", 22));
		profitSummaryLabel.setFont(new Font("Arial", 14));
		profitSummaryTextFlow.setPrefSize(400, 300);
	}

	private void setSortingOptionsLabelText () {

		head = new Label("Choose type of sorting:");
		head.setFont(new Font("Arial", 22));
		TGSorting = new ToggleGroup();
	
		sortUp.setText("Sorting by Alpha-Bet");
		sortUp.setToggleGroup(TGSorting);
	
		sortDown.setText("Sorting by revers Alpha-Bet");
		sortDown.setToggleGroup(TGSorting);
	
		sortOrder.setText("Sorting by order input");
		sortOrder.setToggleGroup(TGSorting);
	}
	
	private void setVbox() {
		vboxSorting = new VBox();
		
		vboxSorting.getChildren().addAll(head, sortUp, sortDown, sortOrder, submitSorting);
		vboxSorting.setAlignment(Pos.CENTER);
		vboxSorting.setPadding(new Insets(20, 80, 20, 80));
		vboxSorting.setSpacing(10);
	}

	private void setTextForAddProduct() {
		headAddProduct = new Label("Add product:");
		headAddProduct.setFont(new Font("Arial", 22));
		productName.setPromptText("Product name");
		productNumber.setPromptText("Product Number (makat)");
		priceForShop.setPromptText("Price for the shop");
		priceForCostumer.setPromptText("Costumer price");
		costumerName.setPromptText("Costumer Name");
		phoneNumber.setPromptText("Phone number");
		notificationForCostumer = new RadioButton("Send notification");
	}
	
	private void setGridAddProduct() {
		gridAddProduct.setPadding(new Insets(10, 10, 10, 10));
		gridAddProduct.setVgap(5);
		gridAddProduct.setHgap(10);
		gridAddProduct.add(headAddProduct, 0, 0);
		gridAddProduct.add(productName, 1, 0);
		gridAddProduct.add(productNumber, 0, 1);
		gridAddProduct.add(priceForShop, 1, 1);
		gridAddProduct.add(priceForCostumer, 0, 2);
		gridAddProduct.add(costumerName, 1, 2);
		gridAddProduct.add(phoneNumber, 0, 3);
		gridAddProduct.add(notificationForCostumer, 1, 3);
		gridAddProduct.add(hboxAddProduct, 0, 4);
		gridAddProduct.add(backB, 1, 4);
	}
	
	public void setHboxForButtons() {
		hboxSearchProduct = new HBox(8);
		hboxSearchProduct.getChildren().addAll(submitSearchB, backB);
	}
	
	public void setVboxSearchToRemove() {
		deleteProduct = new TextField();
		deleteProduct.setMaxWidth(150);
	    submitSearchB = new Button("Submmit");
	    searchToRemove = new Label ("Enter the product number (makat):");
	    searchToRemove.setFont(new Font("Arial", 12));
	}
	
	public void searchToRemoveFunction() {
		if (deleteProduct.getText().isEmpty()) {
			popupWindowMassage(Emassage.FAILE);
				
		}
		else
			popupWindowMassage(controller.deleteProduct());
	}
	
	public void popupWindowMassage(EMassageFromShop massage) {
		if(massage.equals(EMassageFromShop.SUCCEES)) {
			alert.setAlertType(AlertType.CONFIRMATION);
			alert.setContentText("operation success");
		}
		else if (massage.equals(EMassageFromShop.DOESNT_EXIST)){
			alert.setAlertType(AlertType.WARNING);
			alert.setContentText("Not exist in system");
		}
		else if (massage.equals(EMassageFromShop.FAILE)){
			alert.setAlertType(AlertType.ERROR);
			alert.setContentText("Error accured, no action was taken");
		}
		else if (massage.equals(EMassageFromShop.EMPTY)){
			alert.setAlertType(AlertType.WARNING);
			alert.setContentText("Fields are empty, please fill all text boxes");
		}
		alert.show();
	}

}
