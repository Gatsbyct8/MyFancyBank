import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class TotalStock{
    private HashMap<String,Stock> stockMap;
    private ArrayList<Stock> stockList;
    private Stack<Stock> deletedStocks;
    
    public TotalStock() {
    	stockMap = new HashMap<String,Stock>();
    	deletedStocks = new Stack<Stock>();
    	//add("AAP", "Apple", 100, 10);
    	//add("TEX", "Texas", 1000, 9);
    	//add("GOOG", "Google", 1, 1);
    	getStockList();
    }
    
    //Interact on entire Stock object
    public boolean add(Stock s){
    	if(!stockMap.containsKey(s.getId())) {
    		stockMap.put(s.getId(),s);
    		getStockList();
    		return true;
    	}
    	return false;//stock id already exists!
    }
    
    public boolean update(Stock s, double newPrice){//newPrice is in USD
    	if(newPrice >= 0) {
    		s.changePrice(newPrice);
    		return true;
    	}
    	return false;
    }
    
    public boolean delete(Stock s){
    	if(stockMap.remove(s.getId()) != null) {
    		deletedStocks.push(s);
    		getStockList();
    		return true;
    	}
    	return false;
    }
    
  //Interact on just stock id (and other params for creation)
    public boolean add(String sid, String name, int number, double currentPrice){
    	if(!stockMap.containsKey(sid) && number >= 0 && currentPrice >=0) {
    		Stock s = new Stock(sid,name,number,currentPrice);
	    	stockMap.put(sid,s);
	    	getStockList();
	    	return true;
    	}
    	return false;//stock id already exists!
    }
    
    public boolean update(String sid, double newPrice){
    	if(stockMap.containsKey(sid)) {
    		Stock s = stockMap.get(sid);
        	s.changePrice(newPrice);
        	return true;
    	}
    	return false;
    	
    }
    
    public boolean delete(String sid){
    	Stock toPush = stockMap.remove(sid);
    	if(toPush != null) {
    		deletedStocks.push(toPush);
    		getStockList();
    		return true;
    	}
    	return false;
    }
    
    public Stock getStock(String sid) {
    	return stockMap.get(sid);
    }
    
    public boolean restoreStock() {
    	if(deletedStocks.empty()) {
    		return false;
    	}
    	add(deletedStocks.pop());
    	return true;
    }
    
    public List<Stock> getStockList(){//this must be called updated after every addition or removal to maintain the accurate list
    	stockList = new ArrayList<Stock>(stockMap.values());
    	return stockList;
    }

}
