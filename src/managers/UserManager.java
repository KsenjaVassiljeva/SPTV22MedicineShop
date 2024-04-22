package managers;

import entity.Customer;
import entity.User;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import sptv22medicine.App;
import tools.InputProtection;
import tools.PassEncrypt;

public class UserManager {
    private final Scanner scanner;
    private final DatabaseManager databaseManager;
    
    public UserManager(Scanner scanner, DatabaseManager databaseManager) {
        this.scanner = scanner;
        this.databaseManager = databaseManager;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
    
    public User addUser() {
        Customer customer = new Customer();
        System.out.println("----- Новый пользователь -----");
        System.out.print("Имя: ");
        customer.setFirstName(scanner.nextLine());
        System.out.print("Фамилия: ");
        customer.setLastName(scanner.nextLine());
        System.out.print("Номер телефона: ");
        customer.setPhone(scanner.nextLine());
        System.out.print("Начальные деньги: ");
        double money = Double.parseDouble(scanner.nextLine());
        customer.setMoney(money);
        User user = new User();
        System.out.print("Логин: ");
        user.setLogin(scanner.nextLine());
        System.out.print("Пароль: ");
        PassEncrypt pe = new PassEncrypt();
        user.setPassword(pe.getEncryptPassword(scanner.nextLine().trim(), pe.getSalt()));
        user.setCustomer(customer);
        user.getRoles().add(App.ROLES.USER.toString());
        System.out.println("Новый пользователь добавлен!");
        return user;
    }

    public void printListUsers() {
        System.out.println("----- Список пользователей -----");
        List<User> users = getDatabaseManager().getListUsers();
        for (User user : users) {
            System.out.printf("%d. %s %s. Login: %s (phone: %s)%n",
                    user.getId(),
                    user.getCustomer().getFirstName(),
                    user.getCustomer().getLastName(),
                    user.getLogin(),
                    user.getCustomer().getPhone(),
                    user.getBalance()
            );
        }
    }

    public void editUser() {
        printListUsers();
        System.out.println("Выберите пользователя для редактирования: ");
        int idUser = InputProtection.intInput(1, null);
        User user = getDatabaseManager().getUser((long) idUser);
        
        if (user != null) {
            System.out.println("Изменить пользователя: " + user.getCustomer().getFirstName() + " " + user.getCustomer().getLastName());
            
            System.out.println("Выберете, что хотите изменить:");
            System.out.println("1. Имя");
            System.out.println("2. Фамилия");
            System.out.println("3. Номер телефона");
            System.out.println("4. Логин");
            System.out.println("5. Пароль");
            System.out.println("6. Количество денег");
            
            int choice = InputProtection.intInput(1, 6);
            
            switch (choice) {
                case 1:
                    System.out.print("Введите новое имя: ");
                    user.getCustomer().setFirstName(scanner.nextLine());
                    break;
                case 2:
                    System.out.print("Введите новую фамилию: ");
                    user.getCustomer().setLastName(scanner.nextLine());
                    break;
                case 3:
                    System.out.print("Введите новый телефон: ");
                    user.getCustomer().setPhone(scanner.nextLine());
                    break;
                case 4:
                    System.out.print("Введите новый логин: ");
                    user.setLogin(scanner.nextLine());
                    break;
                case 5:
                    System.out.print("Введите новый пароль: ");
                    String newPassword = scanner.nextLine().trim();
                    PassEncrypt pe = new PassEncrypt();
                    user.setPassword(pe.getEncryptPassword(newPassword, pe.getSalt()));
                    break;
                case 6:
                    System.out.print("Введите новое количество денег: ");
                    user.setBalance(Double.parseDouble(scanner.nextLine()));
                    break;
                default:
                    System.out.println("Неверный выбор.");
                    return;
            }
            
            getDatabaseManager().saveUser(user);
            System.out.println("Данные пользователя успешно обновлены.");
        } else {
            System.out.println("Пользователь не найден.");
        }
    }
}
