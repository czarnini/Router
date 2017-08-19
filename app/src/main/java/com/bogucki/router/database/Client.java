package com.bogucki.router.database;

import java.util.ArrayList;

/**
 * Created by Michał Bogucki
 */

interface Client {
    //Create
    long addClient(ClientEntity client);
    //Read
    ArrayList <ClientEntity> getClients();
    ClientEntity getClientById(long id);
    //Update
    int updateClientById(long id);
    //Delete
    int deleteClientById(long id);
}
