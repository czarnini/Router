package com.bogucki.router;

import android.support.test.InstrumentationRegistry;

import com.bogucki.router.database.ClientEntity;
import com.bogucki.router.database.dbHelper;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Michał Bogucki
 */

public class DbTests {

    @Test
    public void testDeletingAndCreating(){
        boolean isDel = InstrumentationRegistry.getTargetContext().deleteDatabase(dbHelper.DB_NAME);
        Assert.assertTrue(isDel);

        dbHelper helper = new dbHelper(InstrumentationRegistry.getTargetContext());
        Assert.assertTrue(helper.getClients().isEmpty());
    }

    @Test
    public void testAddClient(){
        final long INSERTED_ID        = 100;
        final String INSERTED_NAME    = "NAME";
        final String INSERTED_ADDRESS = "ADDRESS";
        dbHelper helper = new dbHelper(InstrumentationRegistry.getTargetContext());



        helper.addClient(new ClientEntity(INSERTED_ID, INSERTED_NAME, INSERTED_ADDRESS));
        ClientEntity insertedClient = helper.getClientById(INSERTED_ID);

        Assert.assertEquals(INSERTED_ID,insertedClient.getId());
        Assert.assertEquals(INSERTED_NAME,insertedClient.getName());
        Assert.assertEquals(INSERTED_ADDRESS,insertedClient.getAddress());

    }
}
