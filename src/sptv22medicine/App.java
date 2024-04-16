package sptv22medicine;

import entity.User;
import java.util.Scanner;
import entity.Customer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import managers.DatabaseManager;
import managers.MedicineManager;
import managers.UserManager;
import managers.SaleManager;
import managers.UserManager.CustomerRatingSystem;
import tools.InputProtection;
import tools.PassEncrypt;

/**
 * Main class for managing the pharmacy application.
 */
public class App {
    public static enum ROLES { ADMINISTRATOR, MANAGER, USER };
    public static User user;
    private final Scanner scanner; 
    private final DatabaseManager databaseManager;
    private final MedicineManager medicineManager;
    private final UserManager userManager;
    private final SaleManager saleManager;
    
    public App() {
        this.scanner = new Scanner(System.in);
        this.databaseManager = new DatabaseManager();
        this.medicineManager = new MedicineManager(scanner, databaseManager);
        this.userManager = new UserManager(scanner, databaseManager);
        this.saleManager = new SaleManager(scanner, databaseManager);
    }

    /**
     * Method to start the application.
     */
    public void run() {
        initializeAdmin();
        System.out.println("If you have a login and password press y, otherwise press n");
        String word = scanner.nextLine();
        if ("n".equals(word)) {
            if (App.user == null || !App.user.getRoles().contains(App.ROLES.ADMINISTRATOR.toString())) {
                System.out.println("Only administrators can add new users.");
            } else {
                databaseManager.saveUser(userManager.addUser());
            }
        }
        loginUser();
        if (App.user == null) return;
        System.out.printf("Hello %s %s, welcome to the pharmacy%n", App.user.getCustomer().getFirstName(), App.user.getCustomer().getLastName());
        boolean repeat = true;
        System.out.println("------- Pharmacy -------");
        do {
            System.out.println("List tasks:");
            System.out.println("0. Exit");
            System.out.println("1. Add new medicine");
            System.out.println("2. Print list medicine");
            System.out.println("3. Add new user");
            System.out.println("4. Print list users");
            System.out.println("5. Make a sale");
            System.out.println("6. Редактирование пользователя");
            System.out.println("7. Редактирование лекарства");
            System.out.println("8. Функция, отсчитывающая время до скидки");
            System.out.println("9. Рейтинг покупателей по количеству покупок");
            System.out.println("10. Рейтинг продаваемости товаров");
            
            System.out.print("Enter task number: ");
            int task = InputProtection.intInput(0, 11); 
            System.out.printf("You selected task %d, to exit press \"0\", to continue press \"1\": ", task);
            int toContinue = InputProtection.intInput(0, 1);
            if (toContinue == 0) continue;
            switch (task) {
                case 0:
                    repeat = false;
                    break;
                case 1:
                    if (!App.user.getRoles().contains(App.ROLES.MANAGER.toString())) {
                        System.out.println("No permission");
                        break;
                    }
                    medicineManager.addMedicine();
                    break;
                case 2:
                    medicineManager.printListMedicines();
                    break;
                case 3:
                    if (!App.user.getRoles().contains(App.ROLES.MANAGER.toString())) {
                        System.out.println("No permission");
                    } else {
                        databaseManager.saveUser(userManager.addUser());
                    }
                    break;
                case 4:
                    userManager.printListUsers();
                    break;
                case 5:
                    saleManager.makeSale(App.user);
                    break;
                case 6:
                    if (!App.user.getRoles().contains(App.ROLES.MANAGER.toString())) {
                        System.out.println("No permission");
                    } else {
                        userManager.editUser();
                        databaseManager.saveUser(userManager.addUser());
                    }
                    break;
                case 7:
                    if (!App.user.getRoles().contains(App.ROLES.MANAGER.toString())) {
                        System.out.println("No permission");
                    } else {
                        medicineManager.editMedicine();
                        databaseManager.saveUser(userManager.addUser());
                    }
                    break;
                case 8:
                    showTimeUntilDiscount();
                    break;
                case 9:
                    CustomerRatingSystem ratingSystem = userManager.new CustomerRatingSystem(databaseManager);
                    ratingSystem.calculateRatings();
                    break;
                case 10:
                    medicineManager.printSalesRating();
                    break;
                default:
                    System.out.println("Select from list of tasks!");
            }
            System.out.println("-----------------------");
        } while (repeat);
        System.out.println("Goodbye!");
    }
    
    /**
     * Method to check for the existence of an administrator user.
     * If the administrator is absent, an administrator account is created.
     */
    private void initializeAdmin() {
        if (databaseManager.getListUsers().isEmpty()) {
            User admin = new User();
            admin.setLogin("admin");
            PassEncrypt pe = new PassEncrypt();
            admin.setPassword(pe.getEncryptPassword("12345", pe.getSalt()));
            admin.getRoles().add(App.ROLES.ADMINISTRATOR.toString());
            admin.getRoles().add(App.ROLES.MANAGER.toString());
            admin.getRoles().add(App.ROLES.USER.toString());
            Customer customer = new Customer();
            customer.setFirstName("John");
            customer.setLastName("Doe");
            customer.setPhone("1234567890");
            admin.setCustomer(customer);
            databaseManager.saveUser(admin);
        }
    }
    
    /**
     * Method to handle user login.
     */
    private void loginUser() {
        for (int n = 0; n < 3; n++) {
            System.out.print("Please enter your login: ");
            String login = scanner.nextLine();
            System.out.print("Please enter your password: ");
            String password = scanner.nextLine().trim();
            PassEncrypt pe = new PassEncrypt();
            String encryptPassword = pe.getEncryptPassword(password, pe.getSalt());
            App.user = databaseManager.authorization(login, encryptPassword);
            if (App.user != null) {
                break;
            }
            System.out.println("Invalid login or password");
        }
    }
    private void showTimeUntilDiscount() {
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    Date currentDate = new Date();
    Date discountStartDate = calculateDiscountStartDate();
    long timeUntilDiscount = discountStartDate.getTime() - currentDate.getTime();
    
    if (timeUntilDiscount <= 0) {
        System.out.println("Скидка уже началась!");
    } else {
        long days = timeUntilDiscount / (1000 * 60 * 60 * 24);
        long hours = (timeUntilDiscount % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (timeUntilDiscount % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (timeUntilDiscount % (1000 * 60)) / 1000;
        
        System.out.println("Время до начала скидки: " + days + " дней, " + hours + " часов, " + minutes + " минут, " + seconds + " секунд");
    }
}

    // Метод для расчета времени начала скидки через 2 дня в 11 часов
    private Date calculateDiscountStartDate() {
        // Создаем объект Calendar и устанавливаем текущую дату и время
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

        // Добавляем 2 дня к текущей дате
        cal.add(Calendar.DAY_OF_MONTH, 2);

        // Устанавливаем время начала скидки на 11 часов
        cal.set(Calendar.HOUR_OF_DAY, 11);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        // Возвращаем дату и время начала скидки
        return cal.getTime();
    }
}
