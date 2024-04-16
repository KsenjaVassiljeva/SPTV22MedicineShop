package managers;

import entity.Medicine;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import tools.InputProtection;

/**
 * Class for managing medicines.
 */
public class MedicineManager {

    private final Scanner scanner;
    private final DatabaseManager databaseManager;

    public MedicineManager(Scanner scanner, DatabaseManager databaseManager) {
        this.scanner = scanner;
        this.databaseManager = databaseManager;
    }

    /**
     * Method to add a medicine to the database.
     */
    public void addMedicine() {
        System.out.println("----- Add Medicine -----");
        Medicine medicine = new Medicine();
        System.out.print("Enter name: ");
        medicine.setName(scanner.nextLine());
        System.out.print("Enter manufacturer: ");
        medicine.setManufacturer(scanner.nextLine());
        System.out.print("Enter production year: ");
        medicine.setProductionYear(InputProtection.intInput(1000, 2030));
        System.out.print("Enter quantity: ");
        medicine.setQuantity(InputProtection.intInput(1, 10));
        medicine.setCount(medicine.getQuantity());
        System.out.print("Enter price: ");
        medicine.setPrice(InputProtection.intInput(1, Integer.MAX_VALUE)); // Assuming price cannot be negative
        databaseManager.saveMedicine(medicine);
        System.out.println("Added medicine: " + medicine.toString());
    }

    /**
     * Method to print the list of medicines.
     * @return The number of medicines in the list.
     */
    public int printListMedicines() {
        System.out.println("----- List Medicines -----");
        List<Medicine> medicines = databaseManager.getListMedicines();
        for (Medicine medicine : medicines) {
            System.out.printf("%d. %s, Manufacturer: %s, Production Year: %d, In store: %d, Price: %d%n",
                    medicine.getId(),
                    medicine.getName(),
                    medicine.getManufacturer(),
                    medicine.getProductionYear(),
                    medicine.getCount(), // Changed from getQuantity() to getCount()
                    medicine.getPrice()); 
        }
        return medicines.size();
    }

    /**
     * Method to edit a medicine in the database.
     */
    public void editMedicine() {
        int count = printListMedicines();
        if (count == 0) {
            System.out.println("No medicines available to edit.");
            return;
        }

        System.out.print("Choose a medicine to edit: ");
        int idMedicine = InputProtection.intInput(1, Integer.MAX_VALUE);
        Medicine medicine = databaseManager.getMedicine((long) idMedicine);

        if (medicine != null) {
            System.out.println("Editing medicine: " + medicine.getName());
            System.out.println("Select what you want to edit:");
            System.out.println("1. Name");
            System.out.println("2. Manufacturer");
            System.out.println("3. Production Year");
            System.out.println("4. Quantity");
            System.out.println("5. Price");

            int choice = InputProtection.intInput(1, 5);

            switch (choice) {
                case 1:
                    System.out.print("Enter new name: ");
                    medicine.setName(scanner.nextLine());
                    break;
                case 2:
                    System.out.print("Enter new manufacturer: ");
                    medicine.setManufacturer(scanner.nextLine());
                    break;
                case 3:
                    System.out.print("Enter new production year: ");
                    medicine.setProductionYear(InputProtection.intInput(1000, 2030));
                    break;
                case 4:
                    System.out.print("Enter new quantity: ");
                    medicine.setQuantity(InputProtection.intInput(1, 10));
                    medicine.setCount(medicine.getQuantity());
                    break;
                case 5:
                    System.out.print("Enter new price: ");
                    medicine.setPrice(InputProtection.intInput(1, Integer.MAX_VALUE));
                    break;
                default:
                    System.out.println("Invalid choice.");
                    return;
            }
            // Save the changes to the database
            databaseManager.saveMedicine(medicine);
            System.out.println("Medicine details updated successfully.");
        } else {
            System.out.println("Medicine not found.");
        }
    }

    /**
     * Method to print the sales rating of medicines.
     */
    public void printSalesRating() {
        List<Medicine> medicines = databaseManager.getListMedicines();
        if (medicines.isEmpty()) {
            System.out.println("No medicines available.");
            return;
        }
        medicines.sort(Comparator.comparingInt(Medicine::getYearlySales).reversed());
        System.out.println("Sales Rating:");
        for (int i = 0; i < medicines.size(); i++) {
            Medicine medicine = medicines.get(i);
            System.out.printf("%d. %s - Yearly Sales: %d, Quantity Sold: %d%n",
                    i + 1, medicine.getName(), medicine.getYearlySales(), medicine.getQuantity() - medicine.getCount());
        }
    }

}
