package se.kth.iv1201.recruitment.domain;

import java.util.Objects;

/**
 * Base class for equals/hashCode/toString functions.
 * Subclasses implement {@link #getId()} to return their key.
 */
public abstract class ObjectUtils<ID> {
    public abstract ID getId();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ObjectUtils<?> other)) return false;
        ID id = getId();
        Object otherId = other.getId();
        return id != null && Objects.equals(id, otherId);
    }

    @Override
    public int hashCode() {
        ID id = getId();
        return (id != null) ? Objects.hashCode(id) : System.identityHashCode(this);
    }
}
