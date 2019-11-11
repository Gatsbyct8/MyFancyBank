import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SecureAccount extends Account
{
    List<SelfStock> selfStocks;
    
    public SecureAccount() {
        this.setAccountID(Account.maxID);
        Account.maxIDIncrease();
    }
    
    public String toString() {
    	return "Secure Account "+this.accountID;
    }
    
    public boolean buy(Stock s, int amt, SavingAccount sa)
    {
    	//check if money is sufficient
    	double moneyNeeded = s.currentprice*amt;
    	
    	if(moneyNeeded>sa.getUSDBalance().getAmount()) {
    		System.out.println("Insufficient money");
    		return false;
    	}
    	else {
    		sa.getUSDBalance().setAmount(sa.getUSDBalance().getAmount()-moneyNeeded);
    	}
    	
    	SelfStock target = getSStockByPrice(s.currentprice, s);
    	if(target!=null) {
    		target.setAmount(target.getAmount()+amt);
    		return true;
    	}
    	
    	this.selfStocks.add(new SelfStock(s,s.currentprice,amt));
    	
        return true;
    }
    public boolean sell(Stock s, double buyPrice, double sellPrice, int amt, SavingAccount sa)
    {
    	SelfStock ss;
    	//check if s exists
    	if(isStockExists(s)==false)
    		return false;
    	
    	//get the stock of a certain buyprice
    	ss = getSStockByPrice(buyPrice, s);
    	if(ss==null)
    		return false;
    	
    	//check if amt is enough
    	if(amt>ss.getAmount()) {
    		System.out.println("amount is not enough!");
    		return false;
    	}
    	ss.setAmount(ss.getAmount()-amt);
    	/*TODO
    	 * decide which currency of the savingAccount should be used
    	 * default US Dollar
    	 * Need to get the USD currency amount of the savingAccount
    	 */
    	sa.getUSDBalance().setAmount(sa.getUSDBalance().getAmount()+(amt*sellPrice));
    	
    	//check if the amt of stock is zero
    	if(ss.getAmount()==0) {
    		selfStocks.remove(ss);
    	}
    	
        return true;
    }
    public boolean openAccount()
    {
        return false;
    }
    public SelfStock getSStockByPrice(double buyPrice, Stock s) {
    	List<SelfStock> resultStocks = getStocks(s);
    	
    	for(Iterator<SelfStock> iterator = resultStocks.iterator();iterator.hasNext();) {
			SelfStock one = iterator.next();
			if(one.getBuyprice()==buyPrice) {
				return one;
			}
		}
    	System.out.println("No stock of such price!");
    	return null;
    }
    
    public boolean isStockExists(Stock s) {
    	for(Iterator<SelfStock> iterator = selfStocks.iterator();iterator.hasNext();) {
			SelfStock one = iterator.next();
			if(one.getA().id.matches(s.id)) {
				return true;
			}
		}
    	return false;
    }
    
    public List<SelfStock> getStocks(Stock s) {
    	List resultStocks = new ArrayList<SelfStock>();
    	for(Iterator<SelfStock> iterator = selfStocks.iterator();iterator.hasNext();) {
			SelfStock one = iterator.next();
			if(one.getA().id.matches(s.id)) {
				resultStocks.add(one);
			}
		}
    	return resultStocks;
    }

}
