import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DuplicateKeysTest {

    @Test
    void normalizedEmailEntferntLeerzeichenUndNutztKleinbuchstaben() {
        Customer c = new Customer(1L, "x", "  Josef@Example.COM ");
        assertEquals("josef@example.com", DuplicateKeys.normalizedEmail(c));
    }
}