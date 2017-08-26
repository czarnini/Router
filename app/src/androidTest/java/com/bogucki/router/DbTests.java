package com.bogucki.router;

import android.support.test.InstrumentationRegistry;

import com.bogucki.router.database.ClientEntity;
import com.bogucki.router.database.DbHelper;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by Michał Bogucki
 */

public class DbTests {

    private DbHelper helper = new DbHelper(InstrumentationRegistry.getTargetContext());
    private final String INSERTED_NAME = "TEST name name";
    private final String INSERTED_ADDRESS = "TEST  ADDRESS ADDRESS";
    private final String UPDATED_NAME = "UPDATE_TEST  NAME NAME";
    private final String UPDATED_ADDRESS = "UPDATE_TEST  ADDRESS ADDRESS";


    @Before
    public void setUp() {
        boolean isDel = InstrumentationRegistry.getTargetContext().deleteDatabase(DbHelper.DB_NAME);
        Assert.assertTrue(isDel);
        Assert.assertTrue(helper.getClients().isEmpty());
    }


    @Test
    public void testAddClient() {
        long insertedId = helper.addClient(INSERTED_NAME, INSERTED_ADDRESS);

        ClientEntity selectedClient = helper.getClientById(insertedId);
        Assert.assertEquals(insertedId, selectedClient.getId());
        Assert.assertEquals(INSERTED_NAME, selectedClient.getName());
        Assert.assertEquals(INSERTED_ADDRESS, selectedClient.getAddress());
    }

    @Test
    public void testGetClientsByName() throws Exception {
        long insertedId = helper.addClient(INSERTED_NAME, INSERTED_ADDRESS);
        ArrayList<ClientEntity> selectedClients = helper.getClientsByName(INSERTED_NAME);
        for (ClientEntity client : selectedClients) {
            Assert.assertEquals(insertedId, client.getId());
            Assert.assertEquals(INSERTED_NAME, client.getName());
            Assert.assertEquals(INSERTED_ADDRESS, client.getAddress());
        }


    }

    @Test
    public void testUpdateClientName() {
        long clientToUpdate = helper.addClient(INSERTED_NAME, INSERTED_ADDRESS);
        helper.updateClientName(clientToUpdate, UPDATED_NAME);

        ClientEntity clientAfterUpdate = helper.getClientById(clientToUpdate);
        Assert.assertEquals(UPDATED_NAME, clientAfterUpdate.getName());
        Assert.assertEquals(INSERTED_ADDRESS, clientAfterUpdate.getAddress());
    }


    @Test
    public void testUpdateClientAddress() {
        long clientToUpdate = helper.addClient(INSERTED_NAME, INSERTED_ADDRESS);
        helper.updateClientAddress(clientToUpdate, UPDATED_ADDRESS);

        ClientEntity clientAfterUpdate = helper.getClientById(clientToUpdate);
        Assert.assertEquals(INSERTED_NAME, clientAfterUpdate.getName());
        Assert.assertEquals(UPDATED_ADDRESS, clientAfterUpdate.getAddress());
    }

    @Test
    public void testRemoveClient() throws Exception {
        ArrayList <ClientEntity> clientsToRemove = helper.getClients();
        if (!clientsToRemove.isEmpty()) {
            for (ClientEntity client : clientsToRemove) {
                helper.deleteClientById(client.getId());
            }

            clientsToRemove = helper.getClients();
            Assert.assertTrue(clientsToRemove.isEmpty());
        }
    }
}
