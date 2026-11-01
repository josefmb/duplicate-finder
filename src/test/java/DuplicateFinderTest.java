import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class DuplicateFinderTest {

    @Test
    public void test_findeDublettenMitNormalisierung() {
        Customer customer1 = new Customer(1L, "josef", "josefmb17@gmail.com");
        Customer customer2 = new Customer(2L, "Josef Mathäus", "Josefmb17@gmail.com");
        Customer customer3 = new Customer(3L, "Josef M.", "josefmb17@gmail.com");
        Customer customer4 = new Customer(4L, "Josef B.", "josefmbischof@gmail.com");

        Map<String, List<Customer>> result = DuplicateFinder.findeDubletten(
                List.of(customer1, customer2, customer3, customer4),
                DuplicateKeys::normalizedEmail
        );

        assertEquals(1, result.size());
        assertEquals(List.of(customer1, customer2, customer3), result.get("josefmb17@gmail.com"));
    }

    @Test
    public void test_findeMehrereDubletten() {
        Customer customer1 = new Customer(1L, "josef", "josefmb17@gmail.com");
        Customer customer2 = new Customer(2L, "Josef Mathäus", "Josefmb17@gmail.com");
        Customer customer3 = new Customer(3L, "Josef M.", "josefmb17@gmail.com");
        Customer customer4 = new Customer(4L, "Josef B.", "josefmbischof@gmail.com");
        Customer customer5 = new Customer(1L, "josef B.", "josefmbischof@gmail.com");
        Customer customer6 = new Customer(5L, "Josef M.", "josefmbischof@gmail.com");

        Map<String, List<Customer>> result = DuplicateFinder.findeDubletten(
                List.of(customer1, customer2, customer3, customer4, customer5, customer6),
                DuplicateKeys::normalizedEmail
        );

        assertEquals(2, result.size());
        assertEquals(List.of(customer1, customer2, customer3), result.get("josefmb17@gmail.com"));
        assertEquals(List.of(customer4, customer5, customer6), result.get("josefmbischof@gmail.com"));
    }

    @Test
    public void test_einzelnerCustomer() {
        Customer customer1 = new Customer(1L, "josef", "josefmb17@gmail.com");

        Map<String, List<Customer>> result = DuplicateFinder.findeDubletten(List.of(customer1), DuplicateKeys::normalizedEmail);
        assertTrue(result.isEmpty());
    }

    @Test
    public void test_randfall_keineEmail() {
        Customer customer1 = new Customer(1L, "josef", null);
        assertThrows(IllegalArgumentException.class, () -> DuplicateFinder.findeDubletten(List.of(customer1), DuplicateKeys::normalizedEmail));
    }

    @Test
    public void test_randfall_leerzeichenBeiDerEmail() {
        Customer customer1 = new Customer(1L, "josef", "    josefmb17@gmail.com    ");
        Customer customer2 = new Customer(2L, "Josef Mathäus", "Josefmb17@gmail.com");

        Map<String, List<Customer>> result = DuplicateFinder.findeDubletten(List.of(customer1, customer2), DuplicateKeys::normalizedEmail);

        assertEquals(1, result.size());
        assertEquals(List.of(customer1, customer2), result.get("josefmb17@gmail.com"));
    }

    @Test
    public void test_randfall_leeresResult() {
        Map<String, List<Customer>> result = DuplicateFinder.findeDubletten(List.of(), DuplicateKeys::normalizedEmail);
        assertEquals(0, result.size());
    }

    @Test
    public void test_randfall_leereEmail() {
        Customer customer1 = new Customer(1L, "josef", "      ");
        assertThrows(IllegalArgumentException.class, () -> DuplicateFinder.findeDubletten(List.of(customer1), DuplicateKeys::normalizedEmail));
    }

    @Test
    void test_gruppiertNachBeliebigemKriterium() {
        Customer a = new Customer(1L, "Anna", "a@example.com");
        Customer b = new Customer(2L, "anna", "b@example.com");
        Customer c = new Customer(3L, "Ben", "c@example.com");

        Map<String, List<Customer>> result = DuplicateFinder.findeDubletten(
                List.of(a, b, c),
                customer -> customer.name().toLowerCase(Locale.ROOT)
        );

        assertEquals(List.of(a, b), result.get("anna"));
        assertEquals(1, result.size());
    }

}
