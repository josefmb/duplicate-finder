import java.util.Locale;

public final class DuplicateKeys {

    private DuplicateKeys() {}

    public static String normalizedEmail(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer ist null");
        }
        if (customer.email() == null || customer.email().isBlank()) {
            throw new IllegalArgumentException("Customer " + customer.id() + " hat keine E-Mail");
        }
        return customer.email().trim().toLowerCase(Locale.ROOT);
    }
}
