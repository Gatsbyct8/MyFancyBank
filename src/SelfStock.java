public class SelfStock {
    private Stock a;
    private double buyprice;
    private int amount;
    public Stock getA() {
		return a;
	}

	public void setA(Stock a) {
		this.a = a;
	}

	public double getBuyprice() {
		return buyprice;
	}

	public void setBuyprice(double buyprice) {
		this.buyprice = buyprice;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	String getid(){
        return a.id;
    }

    public SelfStock(Stock a, double buyPrice, int amt) {
    	this.a = a;
    	this.buyprice = buyPrice;
    	this.amount = amt;
    }
}
