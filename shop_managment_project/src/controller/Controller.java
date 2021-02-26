package controller;

import java.awt.event.ActionListener;

import command.AddProductCommand;
import command.CreateProductMapCommand;
import command.DeleteAllProductsCommand;
import command.DeleteLastCommand;
import command.DeleteProductCommand;
import command.GetProductProfitCommand;
import command.GetTotalProfitCommand;
import command.SendNotificationCommand;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert.AlertType;
import model.Model;
import shop_managment_project.Shop;
import view.View;

public class Controller {
	
	static View view;
	static Model model;
	
	
	public Controller(View view, Model model) {
		Controller.view = view;
		this.model = model;
		
	}
	
	public void createProductsMap() {
		System.out.println("selected button: " + view.getSelectedRadioButton().toString());
		//new CreateProductMapCommand(model.getShop(), view.getSelectedRadioButton());
	}

	public void addProduct() {
		System.out.println("asdasd");
		new AddProductCommand(model.getShop(), view);
		
	}
	
	public void deleteProduct() {
		new DeleteProductCommand(model.getShop(), view.getDeleteProductNumber());
	}
	
	public void undoProduct() {
		new DeleteLastCommand(model.getShop());
	}
	
	public void deleteAllProducts() {
		new DeleteAllProductsCommand(model.getShop());
	}
	
	public void getProductProfit() {
//		new GetProductProfitCommand(model.getShop(), view.getProfitProduct());
	}
	
	public void getTotalProfit() {
		new GetTotalProfitCommand(model.getShop());
	}
	
	public void sendNotification() {
		new SendNotificationCommand(model.getShop());
	}
	
//	EventHandler<ActionEvent> addProduct = new EventHandler<ActionEvent>() {
//		@Override
//		public void handle(ActionEvent addProduct) {
//			System.out.println("idannnnnnnnnnnnnnnnnnnnnnnnnn");
//			new AddProductCommand(model.getShop(), view);
//			
//		}
//	};
	
//	class AddProductListener implements ActionListener{
//
//		@Override
//		public void actionPerformed(java.awt.event.ActionEvent e) {
//			
//			
//		}
//		
//	}


	
	

}
