import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DuplicateFinder {

    public static <K> Map<K, List<Customer>> findeDubletten(List<Customer> customers,
                                                            Function<Customer, K> schluessel) {

        Objects.requireNonNull(customers, "customers darf nicht null sein");
        Objects.requireNonNull(schluessel, "schluessel darf nicht null sein");

        Function<Customer, K> gesicherterSchluessel = customer -> {
            K key = schluessel.apply(customer);
            if (key == null) {
                throw new IllegalArgumentException(
                        "Kein Schlüssel für Customer " + (customer == null ? "null" : customer.id()));
            }
            return key;
        };

        Map<K, List<Customer>> groups = customers.stream().collect(
                Collectors.groupingBy(gesicherterSchluessel, LinkedHashMap::new, Collectors.toList())
        );

        groups.values().removeIf(group -> group.size() < 2);
        return groups;
    }
}
