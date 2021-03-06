package command;

import model.EMassageFromShop;
import model.Shop;

public class DeleteLastCommand implements Command {
	
	private Shop shop;
	
	public DeleteLastCommand(Shop shop) {
		this.shop = shop;
	}

	@Override
	public EMassageFromShop execute() {
		return this.shop.undo(shop.getMemento());
		
	}
	
}
