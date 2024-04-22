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
     * Метод для добавления лекарства в базу данных.
     */
    public void addMedicine() {
        System.out.println("----- Добавить лекарство -----");
        Medicine medicine = new Medicine();
        System.out.print("Введите название: ");
        medicine.setName(scanner.nextLine());
        System.out.print("Введите производителя: ");
        medicine.setManufacturer(scanner.nextLine());
        System.out.print("Введите год производства: ");
        medicine.setProductionYear(InputProtection.intInput(1000, 2030));
        System.out.print("Введите количество: ");
        medicine.setQuantity(InputProtection.intInput(1, 10));
        medicine.setCount(medicine.getQuantity());
        System.out.print("Введите цену: ");
        medicine.setPrice(InputProtection.intInput(1, Integer.MAX_VALUE)); // Предполагается, что цена не может быть отрицательной
        databaseManager.saveMedicine(medicine);
        System.out.println("Добавлено лекарство: " + medicine.toString());
    }

    /**
     * Метод для вывода списка лекарств.
     * @return Количество лекарств в списке.
     */
    public int printListMedicines() {
        System.out.println("----- Список лекарств -----");
        List<Medicine> medicines = databaseManager.getListMedicines();
        for (Medicine medicine : medicines) {
            System.out.printf("%d. %s, Производитель: %s, Год производства: %d, В наличии: %d, Цена: %d%n",
                    medicine.getId(),
                    medicine.getName(),
                    medicine.getManufacturer(),
                    medicine.getProductionYear(),
                    medicine.getCount(), // Изменено с getQuantity() на getCount()
                    medicine.getPrice()); 
        }
        return medicines.size();
    }

    /**
     * Метод для редактирования лекарства в базе данных.
     */
    public void editMedicine() {
        int count = printListMedicines();
        if (count == 0) {
            System.out.println("Нет доступных лекарств для редактирования.");
            return;
        }

        System.out.print("Выберите лекарство для редактирования: ");
        int idMedicine = InputProtection.intInput(1, Integer.MAX_VALUE);
        Medicine medicine = databaseManager.getMedicine((long) idMedicine);

        if (medicine != null) {
            System.out.println("Редактирование лекарства: " + medicine.getName());
            System.out.println("Выберите, что вы хотите отредактировать:");
            System.out.println("1. Название");
            System.out.println("2. Производитель");
            System.out.println("3. Год производства");
            System.out.println("4. Количество");
            System.out.println("5. Цена");

            int choice = InputProtection.intInput(1, 5);

            switch (choice) {
                case 1:
                    System.out.print("Введите новое название: ");
                    medicine.setName(scanner.nextLine());
                    break;
                case 2:
                    System.out.print("Введите нового производителя: ");
                    medicine.setManufacturer(scanner.nextLine());
                    break;
                case 3:
                    System.out.print("Введите новый год производства: ");
                    medicine.setProductionYear(InputProtection.intInput(1000, 2030));
                    break;
                case 4:
                    System.out.print("Введите новое количество: ");
                    medicine.setQuantity(InputProtection.intInput(1, 10));
                    medicine.setCount(medicine.getQuantity());
                    break;
                case 5:
                    System.out.print("Введите новую цену: ");
                    medicine.setPrice(InputProtection.intInput(1, Integer.MAX_VALUE));
                    break;
                default:
                    System.out.println("Недопустимый выбор.");
                    return;
            }
            // Сохранение изменений в базе данных
            databaseManager.saveMedicine(medicine);
            System.out.println("Данные о лекарстве успешно обновлены.");
        } else {
            System.out.println("Лекарство не найдено.");
        }
    }

    /**
     * Метод для вывода рейтинга продаж лекарств.
     */
    public void printSalesRating() {
        List<Medicine> medicines = databaseManager.getListMedicines();
        if (medicines.isEmpty()) {
            System.out.println("Нет доступных лекарств.");
            return;
        }
        medicines.sort(Comparator.comparingInt(Medicine::getYearlySales).reversed());
        System.out.println("Рейтинг продаж:");
        for (int i = 0; i < medicines.size(); i++) {
            Medicine medicine = medicines.get(i);
            System.out.printf("%d. %s - Ежегодные продажи: %d, Количество проданного: %d%n",
                    i + 1, medicine.getName(), medicine.getYearlySales(), medicine.getQuantity() - medicine.getCount());
        }
    }

}
