package warehouse;

/*
 *
 * This class implements a warehouse on a Hash Table like structure, 
 * where each entry of the table stores a priority queue. 
 * Due to your limited space, you are unable to simply rehash to get more space. 
 * However, you can use your priority queue structure to delete less popular items 
 * and keep the space constant.
 * 
 * @author Ishaan Ivaturi
 */ 
public class WarehouseSubmission1 {
    private Sector[] sectors;
    
    // Initializes every sector to an empty sector
    public WarehouseSubmission1() {
        sectors = new Sector[10];

        for (int i = 0; i < 10; i++) {
            sectors[i] = new Sector();
        }
    }
    
    /**
     * Provided method, code the parts to add their behavior
     * @param id The id of the item to add
     * @param name The name of the item to add
     * @param stock The stock of the item to add
     * @param day The day of the item to add
     * @param demand Initial demand of the item to add
     */
    public void addProduct(int id, String name, int stock, int day, int demand) {
        evictIfNeeded(id);
        addToEnd(id, name, stock, day, demand);
        fixHeap(id);
    }

    /**
     * Add a new product to the end of the correct sector
     * Requires proper use of the .add() method in the Sector class
     * @param id The id of the item to add
     * @param name The name of the item to add
     * @param stock The stock of the item to add
     * @param day The day of the item to add
     * @param demand Initial demand of the item to add
     */
    private void addToEnd(int id, String name, int stock, int day, int demand) {
        Product add_prod = new Product(id, name, stock, day, demand); 

        int sector_id = id % 10;  

        for (int ind = 0; ind < sectors.length; ind++){
            if (ind == sector_id){
                sectors[ind].add(add_prod);
            }
        }

    }

    /**
     * Fix the heap structure of the sector, assuming the item was already added
     * Requires proper use of the .swim() and .getSize() methods in the Sector class
     * @param id The id of the item which was added
     */

    private void fixHeap(int id) {
        
        int sector_id = id % 10;
        int size = sectors[sector_id].getSize();
        
        sectors[sector_id].swim(size); 

    }

    /**
     * Delete the least popular item in the correct sector, only if its size is 5 while maintaining heap
     * Requires proper use of the .swap(), .deleteLast(), and .sink() methods in the Sector class
     * @param id The id of the item which is about to be added
     */
    private void evictIfNeeded(int id) {

        int sector_id = id % 10;
        int sector_size = sectors[sector_id].getSize(); 
        
        if (sector_size == 5){
            // Swap the first (min) and last term, and then delete the last term
            sectors[sector_id].swap(1, sector_size);
            sectors[sector_id].deleteLast();
            
            // Sink the first value (not min) to where it's supposed to be in the heap
            sectors[sector_id].sink(1); 
        }
    }

    /**
     * Update the stock of some item by some amount
     * Requires proper use of the .getSize() and .get() methods in the Sector class
     * Requires proper use of the .updateStock() method in the Product class
     * @param id The id of the item to restock
     * @param amount The amount by which to update the stock
     */
    public void restockProduct(int id, int amount) {
        int sector_id = id % 10;

        for (int i = 1; i <= sectors[sector_id].getSize(); i++){
            if (sectors[sector_id].get(i).getId() == id){
                // if you found the id of the element, restock it 
                sectors[sector_id].get(i).updateStock(amount);
            }
        }

    }
    
    /**
     * Delete some arbitrary product while maintaining the heap structure in O(logn)
     * Requires proper use of the .getSize(), .get(), .swap(), .deleteLast(), .sink() and/or .swim() methods
     * Requires proper use of the .getId() method from the Product class
     * @param id The id of the product to delete
     */
    public void deleteProduct(int id) {
        // NEED TO TEST THIS WITH OTHER INPUTS

        int sector_id = id % 10;

        // System.out.println("call");
        // System.out.println("id: " + id); 
        // System.out.println("sector id: " + sector_id);

        // Problem: the size of the sector will always be 1, so it's entering the for loop anyway
        // When the size is 1, there will be an empty product, because at 0 index there is null, and an array with 0 index has size 1
        
        // Only go to the for loop if size is not 1 
        
        for (int i = 1; i <= sectors[sector_id].getSize(); i++){
            // System.out.println(sectors[sector_id].get(i).getId()); 
            // Check if i product is null 
            
            if (sectors[sector_id].get(i).getId() == id){
                // Swap the item with the provided ID and last item and then delete the last item from the list
                // System.out.println("i: " + sectors[sector_id].get(i).getName());
                // System.out.println("Last: " + sectors[sector_id].get(sectors[sector_id].getSize()).getName());
                sectors[sector_id].swap(i, sectors[sector_id].getSize());
                sectors[sector_id].deleteLast();

                // Check the size of the PQ
                int size = sectors[sector_id].getSize(); 
                // Sink the value to where it's supposed to be
                if (size > 1){
                    sectors[sector_id].sink(i);
                    sectors[sector_id].swim(i);
                }  

            }

        }


    }
    
    /**
     * Simulate a purchase order for some product
     * Requires proper use of the getSize(), sink(), get() methods in the Sector class
     * Requires proper use of the getId(), getStock(), setLastPurchaseDay(), updateStock(), updateDemand() methods
     * @param id The id of the purchased product
     * @param day The current day
     * @param amount The amount purchased
     */
    public void purchaseProduct(int id, int day, int amount) {
        int sector_id = id % 10;

        for (int i = 1; i <= sectors[sector_id].getSize(); i++){
            System.out.println("entering loop to find matching id"); 
            if (sectors[sector_id].get(i).getId() == id){
                // Only update info if the amount of stock is greater than the purchase amount
                if (amount < sectors[sector_id].get(i).getStock()){
                    System.out.println("updating day, stock, purchase"); 

                    // Update the day purchased
                    sectors[sector_id].get(i).setLastPurchaseDay(day);

                    // Decrease stock by amount
                    int current_stock = sectors[sector_id].get(i).getStock(); 
                    sectors[sector_id].get(i).setStock(current_stock - amount);
    
                    // Increase demand by the amount purchased 
                    sectors[sector_id].get(i).updateDemand(amount);
    
                    // Run a for-loop through the specific sector and sink whatever needs to be sinked
                    int size = sectors[sector_id].getSize(); 
                    
                    if (size > 1){
                        for (int j = 1; j <= sectors[sector_id].getSize(); j++){
                            System.out.println("heap fix for-loop"); 
                            if (sectors[sector_id].get(j) != null){
                                System.out.println("null check"); 
                                sectors[sector_id].sink(j);
                            }
                        }
                    }
                }
            }
        }

    }
    
    /**
     * Construct a better scheme to add a product, where empty spaces are always filled
     * @param id The id of the item to add
     * @param name The name of the item to add
     * @param stock The stock of the item to add
     * @param day The day of the item to add
     * @param demand Initial demand of the item to add
     */
    public void betterAddProduct(int id, String name, int stock, int day, int demand) {
        // INFINITE LOOP
        Product add_prod = new Product(id, name, stock, day, demand); 

        int sector_id = id % 10;  

        for (int ind = 0; ind < sectors.length; ind++){
            System.out.println("betteradd for 1"); 
            if (ind == sector_id){

                if (sectors[sector_id].getSize() < 5){
                    // Sector isn't full, add into it normally & fix heap (can call addProduct)
                    addProduct(id, name, stock, day, demand);
                } else {
                    // Go through the entire sector, max sector.length times (because then you'll be back at the original sector)
                    // If you find a sector that is empty (boolean), store that index, break out of the loop, add the product and fix the heap 
                    // If you don't find an empty sector (boolean = false), call evict, add to end, and fix (addProduct method)
                    // Loop starts at sector_id and runs a max sector.length times
                        // If index hits sector.length set it to 0
                    boolean empty_sector = false;
                    int empty_sector_ind = 0;  

                    int count = 0; 
                    int start_ind = sector_id; 

                    while (count < sectors.length){
                        System.out.println("betteradd while 1"); 
                        if (sectors[start_ind].getSize() < 5){
                            System.out.println("betteradd if 1"); 
                            empty_sector = true; 
                            empty_sector_ind = start_ind; 
                            break; 
                        }

                        start_ind++; 

                        if (start_ind > (sectors.length - 1)){
                            start_ind = 0; 
                            System.out.println("betteradd if 2"); 
                        }
                        
                        count++; 
                        System.out.println("betteradd while end"); 
                    }

                    if (empty_sector == true){
                        sectors[empty_sector_ind].add(add_prod);
                        fixHeap(id);
                        
                    } else {
                        addProduct(id, name, stock, day, demand);
                    }


                }

                
            }
        }
    }

    /*
     * Returns the string representation of the warehouse
     */
    public String toString() {
        String warehouseString = "[\n";

        for (int i = 0; i < 10; i++) {
            warehouseString += "\t" + sectors[i].toString() + "\n";
        }
        
        return warehouseString + "]";
    }

    /*
     * Do not remove this method, it is used by Autolab
     */ 
    public Sector[] getSectors () {
        return sectors;
    }
}
