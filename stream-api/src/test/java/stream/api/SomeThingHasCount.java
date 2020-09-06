package stream.api;

import java.util.Optional;

public class SomeThingHasCount {
    Optional<Long> count;

    public SomeThingHasCount(Optional<Long> count) {
        this.count = count;
    }

    public Optional<Long> getCount() {
        return count;
    }
}
