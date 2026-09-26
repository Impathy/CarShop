package ru.impathy.domain.users;

import java.util.UUID;

public class User {
    private UUID id;
    private String name;
    private AccessLevel accessLevel;

    public User(UUID id, String name, AccessLevel accessLevel) {
        this.id = id;
        this.name = name;
        this.accessLevel = accessLevel;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }
}
