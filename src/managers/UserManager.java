/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managers;

import entity.Customer;
import entity.User;
import java.util.List;
import java.util.Scanner;
import sptv22medicine.App;
import tools.InputProtection;
import tools.PassEncrypt;

/**
 * Класс для управления пользователями.
 */
public class UserManager {
    private final Scanner scanner;
    private final DatabaseManager databaseManager;
    
    public UserManager(Scanner scanner,DatabaseManager databaseManager) {
        this.scanner = scanner;
        this.databaseManager = databaseManager;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
    
    /**
     * Метод для добавления нового пользователя (клиента).
     */
    public User addUser() {
    Customer customer = new Customer();
    System.out.println("----- Add Customer -----");
    System.out.print("First name: ");
    customer.setFirstName(scanner.nextLine());
    System.out.print("Last name: ");
    customer.setLastName(scanner.nextLine());
    System.out.print("Phone: ");
    customer.setPhone(scanner.nextLine());
    User user = new User();
    System.out.print("Login: ");
    user.setLogin(scanner.nextLine());
    System.out.print("Password: ");
    PassEncrypt pe = new PassEncrypt();
    user.setPassword(pe.getEncryptPassword(scanner.nextLine().trim(), pe.getSalt()));
    user.setCustomer(customer);
    user.getRoles().add(App.ROLES.USER.toString());
    System.out.println("New customer added!");
    return user;
}


    /**
     * Метод для печати списка пользователей (клиентов).
     */
    public void printListUsers() {
        System.out.println("----- List Customers -----");
        List<User> users = getDatabaseManager().getListUsers();
        for (int i = 0; i < users.size(); i++) {
            System.out.printf("%d. %s %s. Login: %s (phone: %s)%n",
                    users.get(i).getId(),
                    users.get(i).getCustomer().getFirstName(),
                    users.get(i).getCustomer().getLastName(),
                    users.get(i).getLogin(),
                    users.get(i).getCustomer().getPhone()
            );
        }
    }

    /**
     * Метод для изменения роли пользователя.
     */
    public void changeRole() {
        //Выводим список пользователей и выбираем пользователя
        //Выводим список ролей и выбираем роль
        //Выводим список действий и выбираем действие
        printListUsers();
        System.out.println("Choose a user: ");
        int idUser = InputProtection.intInput(1, null);
        for (int i = 0; i < App.ROLES.values().length; i++) {
            System.out.printf("%d %s%n", i + 1, App.ROLES.values()[i].toString());
        }
        System.out.println("Choose a role: ");
        int numRole = InputProtection.intInput(1, 3);
        System.out.println("Choose an action: ");
        System.out.println("1 - Add a role");
        System.out.println("2 - Remove a role");
        int action = InputProtection.intInput(1, 2);
        if (action == 1) {
            this.addRole(idUser, numRole);
        } else if (action == 2) {
            this.removeRole(idUser, numRole);
        }
    }

    /**
     * Метод для добавления роли пользователю.
     */
    private void addRole(int idUser, int numRole) {
        User user = getDatabaseManager().getUser((long)idUser);
        String role = App.ROLES.values()[numRole-1].toString();
        if(!user.getRoles().contains(role)){
            user.getRoles().add(role);
            getDatabaseManager().saveUser(user);
            System.out.println("Role added");
            if(App.user.getId().equals(user.getId())){
                App.user = user;
            }
        } else {
            System.out.printf("User already has the role %s%n", role);
        }
    }

    /**
     * Метод для удаления роли пользователя.
     */
    private void removeRole(int idUser, int numRole) {
        User user = getDatabaseManager().getUser((long)idUser);
        if(user.getLogin().equals("admin")){
            System.out.println("Changes are impossible");
            return;
        }
        String role = App.ROLES.values()[numRole-1].toString();
        if(user.getRoles().contains(role)){
            user.getRoles().remove(role);
            getDatabaseManager().saveUser(user);
            if(App.user.getId().equals(user.getId())){
                App.user = user;
            }
            System.out.println("Role removed");
        } else {
            System.out.printf("User does not have the role %s%n", role);
        }
    }
    
    public void editUser() {
    printListUsers();
    System.out.println("Choose a user to edit: ");
    int idUser = InputProtection.intInput(1, null);
    User user = getDatabaseManager().getUser((long) idUser);
    
    if (user != null) {
        System.out.println("Editing user: " + user.getCustomer().getFirstName() + " " + user.getCustomer().getLastName());
        
        // Prompt for the fields to edit
        System.out.println("Select what you want to edit:");
        System.out.println("1. First Name");
        System.out.println("2. Last Name");
        System.out.println("3. Phone");
        System.out.println("4. Login");
        System.out.println("5. Password");
        
        int choice = InputProtection.intInput(1, 5);
        
        switch (choice) {
            case 1:
                System.out.print("Enter new first name: ");
                user.getCustomer().setFirstName(scanner.nextLine());
                break;
            case 2:
                System.out.print("Enter new last name: ");
                user.getCustomer().setLastName(scanner.nextLine());
                break;
            case 3:
                System.out.print("Enter new phone: ");
                user.getCustomer().setPhone(scanner.nextLine());
                break;
            case 4:
                System.out.print("Enter new login: ");
                user.setLogin(scanner.nextLine());
                break;
            case 5:
                System.out.print("Enter new password: ");
                String newPassword = scanner.nextLine().trim();
                PassEncrypt pe = new PassEncrypt();
                user.setPassword(pe.getEncryptPassword(newPassword, pe.getSalt()));
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }
        
        // Save the changes to the database
        getDatabaseManager().saveUser(user);
        System.out.println("User details updated successfully.");
    } else {
        System.out.println("User not found.");
    }
    }
}

