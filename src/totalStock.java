import java.util.Iterator;
import java.util.List;


public class totalStock{
    List<Stock> StockList;
    public boolean add(){return false;};
    public boolean update(){return false;};
    public boolean delete(){return false;};
    
    public Stock getStockById(String id) {
    	for(Iterator<Stock> iterator = StockList.iterator();iterator.hasNext();) {
			Stock one = iterator.next();
			if(one.id.matches(id)) {
				return one;
			}
		}
    	return null;
    }
    
    public Stock getStockByName(String name) {
    	for(Iterator<Stock> iterator = StockList.iterator();iterator.hasNext();) {
			Stock one = iterator.next();
			if(one.name.matches(name)) {
				return one;
			}
		}
    	return null;
    }
}