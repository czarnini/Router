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
    public void testAddClient(){
        final long INSERTED_ID        = 100;
        final String INSERTED_NAME    = "NAME";
        final String INSERTED_ADDRESS = "ADDRESS";
        InstrumentationRegistry.getContext().deleteDatabase(dbHelper.DB_NAME);
        dbHelper helper = new dbHelper(InstrumentationRegistry.getTargetContext());



        helper.addClient(new ClientEntity(INSERTED_ID+1, INSERTED_NAME, INSERTED_ADDRESS));
        ClientEntity insertedClient = helper.getClientById(INSERTED_ID);

        Assert.assertEquals(insertedClient.getId(),      INSERTED_ID);
        Assert.assertEquals(insertedClient.getName(),    INSERTED_NAME);
        Assert.assertEquals(insertedClient.getAddress(), INSERTED_ADDRESS);

    }
}
