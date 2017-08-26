package com.bogucki.router.database;

import java.util.ArrayList;

/**
 * Created by Michał Bogucki
 */

interface Client {
    //Create
//    long addClient(ClientEntity client);
    long addClient(String name, String address);
    //Read
    ArrayList<ClientEntity> getClients();
    ClientEntity getClientById(long id);
    ArrayList<ClientEntity> getClientsByName(String name);
    //Update
    int updateClientName(long id, String newName);
    int updateClientAddress(long id, String newAddress);
    //Delete
    int deleteClientById(long id);
}
