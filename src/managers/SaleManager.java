package managers;

import entity.Customer;
import entity.Medicine;
import entity.Sale;
import entity.User;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    System.out.println("Доступные лекарства:");
    for (Medicine medicine : medicines) {
        System.out.println("ID: " + medicine.getId() + ", Имя: " + medicine.getName() + ", Count: " + medicine.getCount());
    }

    // Запросить у пользователя выбор продукта для покупки
    System.out.print("Введите ID лекарства, которое вы хотите купить: ");
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
        System.out.println("Неверный ID лекарства. Пожалуйста, попробуйте еще раз.");
        return;
    }

    // Запросить количество продукта для покупки
    System.out.print("Введите количество, которое вы хотите купить: ");
    int quantity = scanner.nextInt();
    scanner.nextLine(); // Очистить буфер сканера

    // Проверить наличие достаточного количества продукта на складе
    if (selectedMedicine.getCount() < quantity) {
        System.out.println("Недостаточное количество на складе. Пожалуйста, попробуйте еще раз.");
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
    System.out.println("Покупка удачная. Спасибо за покупку!");
    }
    
    public void displayCustomerRankings() {
        // Получить список всех продаж из базы данных
        List<Sale> sales = databaseManager.getAllSales();

        // Создать карту для хранения количества покупок каждого покупателя
        Map<Customer, Integer> customerPurchaseCounts = new HashMap<>();

        // Проанализировать каждую продажу и увеличить количество покупок для соответствующего покупателя
        for (Sale sale : sales) {
            Customer customer = sale.getCustomer();
            int purchaseCount = customerPurchaseCounts.getOrDefault(customer, 0);
            customerPurchaseCounts.put(customer, purchaseCount + sale.getQuantity());
        }

        // Преобразовать карту в список для сортировки
        List<Map.Entry<Customer, Integer>> sortedRankings = new ArrayList<>(customerPurchaseCounts.entrySet());
        sortedRankings.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue())); // Сортировка по убыванию количества покупок

        // Вывести рейтинг покупателей на экран
        System.out.println("Рейтинг покупателей по количеству покупок:");
        int rank = 1;
        for (Map.Entry<Customer, Integer> entry : sortedRankings) {
            System.out.println(rank + ". " + entry.getKey().getFirstName() + ": " + entry.getValue() + " покупок");
            rank++;
        }
    }
}

