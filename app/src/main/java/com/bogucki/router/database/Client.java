package com.bogucki.router.database;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michał Bogucki
 */

interface Client {
    //Create
    long addClient(ClientEntity client);
    //Read
    List<ClientEntity> getClients();
    ClientEntity getClientById(long id);
    //Update
    int updateClientById(long id);
    //Delete
    int deleteClientById(long id);
}
