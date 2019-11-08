public class SelfStock {
    private Stock stock;
    private double buyPrice;//this is in USD
    private int amount;
    
    public SelfStock(Stock s) {
    	stock = s;
    	buyPrice = s.getPrice();
    	amount = 0;
    }
    
    public SelfStock(Stock s, int n) {
    	stock = s;
    	buyPrice = s.getPrice();
    	amount = n;
    }
    
    
    //the following methods allow you to determine how much the stock is worth at purchase time, current time, and the difference between the two
    public double buyValue() {//this should be called to determine if a user can afford the stock
    	return buyPrice * amount;
    }
    
    public double currentValue() {
    	return stock.getPrice() * amount;
    }
    
    public double valueChange() {
    	return buyValue() + currentValue();
    }

}
