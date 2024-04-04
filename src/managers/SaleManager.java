package managers;

import entity.Medicine;
import entity.Sale;
import entity.User;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * Менеджер для управления продажами.
 */
public class SaleManager {

    private final Scanner scanner;
    private final DatabaseManager databaseManager;

    public SaleManager(Scanner scanner, DatabaseManager databaseManager) {
        this.scanner = scanner;
        this.databaseManager = databaseManager;
    }
    

    /**
     * Метод для проведения продажи.
     * @param user Пользователь, совершающий покупку.
     */
    public void makeSale(User user) {
    // Получить список доступных продуктов
    List<Medicine> medicines = databaseManager.getListMedicines();

    // Вывести список доступных продуктов
    System.out.println("Available Medicines:");
    for (Medicine medicine : medicines) {
        System.out.println("ID: " + medicine.getId() + ", Name: " + medicine.getName() + ", Count: " + medicine.getCount());
    }

    // Запросить у пользователя выбор продукта для покупки
    System.out.print("Enter the ID of the medicine you want to buy: ");
    Long medicineId = scanner.nextLong();
    scanner.nextLine(); // Очистить буфер сканера

    // Найти выбранный продукт по его ID
    Medicine selectedMedicine = null;
    for (Medicine medicine : medicines) {
        if (medicine.getId().equals(medicineId)) {
            selectedMedicine = medicine;
            break;
        }
    }

    // Проверить, был ли выбран продукт
    if (selectedMedicine == null) {
        System.out.println("Invalid medicine ID. Please try again.");
        return;
    }

    // Запросить количество продукта для покупки
    System.out.print("Enter the quantity you want to buy: ");
    int quantity = scanner.nextInt();
    scanner.nextLine(); // Очистить буфер сканера

    // Проверить наличие достаточного количества продукта на складе
    if (selectedMedicine.getCount() < quantity) {
        System.out.println("Insufficient quantity in stock. Please try again.");
        return;
    }

    // Создать новую продажу
    Sale sale = new Sale();
    sale.setMedicine(selectedMedicine);
    sale.setCustomer(user.getCustomer());
    sale.setSaleDate(new Date());
    sale.setUnitsSold(quantity);
    sale.setQuantity(quantity);

    // Вычесть проданные единицы продукта из общего количества на складе
    selectedMedicine.setCount(selectedMedicine.getCount() - quantity);

    // Сохранить информацию о продаже и обновить информацию о продукте в базе данных
    databaseManager.saveSale(sale);
    databaseManager.saveMedicine(selectedMedicine);

    // Вывести подтверждение о покупке
    System.out.println("Purchase successful. Thank you for shopping with us!");
    }
}
