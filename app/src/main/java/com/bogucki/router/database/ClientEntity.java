package com.bogucki.router.database;

import java.util.Locale;

/**
 * Created by Michał Bogucki
 */

public class ClientEntity {
    private int id;
    private String name;
    private String address;

    public ClientEntity(String name, String address) {
        this.id = -1;
        this.name = name;
        this.address = address;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(),
                "id = %d, name = %s, address = %s",
                id, name, address);
    }
}
