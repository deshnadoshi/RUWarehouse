package warehouse;

/*
 * Use this class to put it all together.
 */ 
public class Everything {
    public static void main(String[] args) {
        StdIn.setFile(args[0]);
        StdOut.setFile(args[1]);

        Warehouse warehouse = new Warehouse(); 

        int num_items = StdIn.readInt();  

        while (num_items != 0){
            String type = StdIn.readString(); 
            if (type.equals("add")){
                int day = StdIn.readInt(); 
                int id = StdIn.readInt(); 
                String name = StdIn.readString(); 
                int stock = StdIn.readInt(); 
                int demand = StdIn.readInt(); 

                warehouse.addProduct(id, name, stock, day, demand);
                num_items--; 
            } 

            if (type.equals("restock")){
                int id = StdIn.readInt(); 
                int amount = StdIn.readInt(); 

                warehouse.restockProduct(id, amount); 
                num_items--; 
            }

            if (type.equals("delete")){
                int id = StdIn.readInt(); 

                warehouse.deleteProduct(id); 
                num_items--; 
            }

            if (type.equals("purchase")){
                int day = StdIn.readInt(); 
                int id = StdIn.readInt(); 
                int amount = StdIn.readInt(); 

                warehouse.purchaseProduct(id, day, amount);
                num_items--; 
            }

        }

        StdOut.println(warehouse); 

	// Use this file to test all methods
    }
}
