package managers;

import entity.Medicine;
import entity.Sale;
import entity.Customer;
import entity.User;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

/**
 * Class for managing the pharmacy database.
 */
public class DatabaseManager {
    private EntityManager em;

    public DatabaseManager() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SPTV22MedicinePU");
        this.em = emf.createEntityManager();
    }
    
    /**
     * Method to save medicine information.
     */
    public void saveMedicine(Medicine medicine){
        try {
            em.getTransaction().begin();
            if(medicine.getId() == null){
                em.persist(medicine);
            }else{
                em.merge(medicine);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            // Handle or log the exception
        }
    }
    
    /**
     * Method to save user information.
     */
    public void saveUser(User user){
        try {
            em.getTransaction().begin();
            if(user.getCustomer().getId() == null){
                em.persist(user.getCustomer());
            }else{
                em.merge(user.getCustomer());
            }
            if(user.getId() == null){
                em.persist(user);
            }else{
                em.merge(user);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("User not saved: " + e.getMessage());
        }
    }
    
    /**
     * Method to save sale information.
     */
    public void saveSale(Sale sale){
        try {
            em.getTransaction().begin();
            if(sale.getId() == null){
                em.persist(sale);
            }else{
                em.merge(sale);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            // Handle or log the exception
        }
    }

    /**
     * Get a list of all medicines.
     */
    public List<Medicine> getListMedicines() {
        TypedQuery<Medicine> query = em.createQuery("SELECT m FROM Medicine m", Medicine.class);
        return query.getResultList();
    }

    /**
     * Get a list of all customers.
     */
    public List<Customer> getListCustomers() {
        TypedQuery<Customer> query = em.createQuery("SELECT c FROM Customer c", Customer.class);
        return query.getResultList();
    }
    
    /**
     * Get a list of all users.
     */
    public List<User> getListUsers() {
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u", User.class);
        return query.getResultList();
    }

    /**
     * Authenticate a customer by login and password.
     */
    
    public User authorization(String login, String password) {
    try {
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.login = :login AND u.password = :password", User.class);
        query.setParameter("login", login);
        query.setParameter("password", password);
        return query.getSingleResult();
    } catch (Exception e) {
        return null;
    }
}


    /**
     * Get medicine information by its ID.
     */
    public Medicine getMedicine(Long id) {
        return em.find(Medicine.class, id);
    }
    
    /**
     * Get user information by ID.
     */
    public User getUser(Long id) {
        return em.find(User.class,id);
    }
    
    /**
     * Get customer information by ID.
     */
    public Customer getCustomer(Long id) {
        return em.find(Customer.class,id);
    }
    
    public List<Sale> getAllSales() {
        TypedQuery<Sale> query = em.createQuery("SELECT s FROM Sale s", Sale.class);
        return query.getResultList();
    }

    /**
     * Get a list of sales for a specific year.
     */
    public List<Sale> getSalesByYear(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, Calendar.JANUARY, 1, 0, 0, 0);
        Date startDate = calendar.getTime();
        calendar.set(year, Calendar.DECEMBER, 31, 23, 59, 59);
        Date endDate = calendar.getTime();
        TypedQuery<Sale> query = em.createQuery("SELECT s FROM Sale s WHERE s.saleDate BETWEEN :startDate AND :endDate", Sale.class);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        return query.getResultList();
    }

    /**
     * Get a list of sales for a specific month and year.
     */
    public List<Sale> getSalesByMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1, 0, 0, 0);
        Date startDate = calendar.getTime();
        calendar.set(year, month - 1, calendar.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59);
        Date endDate = calendar.getTime();
        TypedQuery<Sale> query = em.createQuery("SELECT s FROM Sale s WHERE s.saleDate BETWEEN :startDate AND :endDate", Sale.class);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        return query.getResultList();
    }

    /**
     * Get a list of sales for a specific day, month, and year.
     */
    public List<Sale> getSalesByDay(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day, 0, 0, 0);
        Date startDate = calendar.getTime();
        calendar.set(year, month - 1, day, 23, 59, 59);
        Date endDate = calendar.getTime();
        TypedQuery<Sale> query = em.createQuery("SELECT s FROM Sale s WHERE s.saleDate BETWEEN :startDate AND :endDate", Sale.class);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        return query.getResultList();
    }

    // Existing methods...
    
    public List<Sale> getSalesByCustomer(Long customerId) {
        TypedQuery<Sale> query = em.createQuery("SELECT s FROM Sale s WHERE s.customer.id = :customerId", Sale.class);
        query.setParameter("customerId", customerId);
        return query.getResultList();
    }

    public List<Sale> getListSales() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public Map<Long, Integer> getPurchaseDataFromDatabase() {
    Map<Long, Integer> purchaseData = new HashMap<>();
    // Здесь выполняется запрос к базе данных для получения данных о покупках клиентов
    // Пример:
    // ResultSet resultSet = executeQuery("SELECT customer_id, purchase_count FROM purchases");
    // while (resultSet.next()) {
    //     purchaseData.put(resultSet.getLong("customer_id"), resultSet.getInt("purchase_count"));
    // }
    // Замените этот код на ваш фактический запрос к базе данных
    return purchaseData;
}

}
