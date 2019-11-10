public class Stock {
    private String id;
	private String name;
    private int number;
    protected double currentPrice;//this is in USD
	
    public Stock(String idVal, String nameVal, int numberVal, double currentPriceVal) {
    	id = idVal;
    	name = nameVal;
    	number = numberVal;
    	currentPrice = currentPriceVal;
    }
    
    public void changePrice(double cp) {
		currentPrice = cp;
	}
    
    public double getPrice() {
    	return currentPrice;
    }

	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public int getNumber() {
		return number;
	}
}