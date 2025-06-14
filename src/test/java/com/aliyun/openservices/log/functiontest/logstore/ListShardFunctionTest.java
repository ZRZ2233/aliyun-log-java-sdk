package com.aliyun.openservices.log.functiontest.logstore;

import com.aliyun.openservices.log.common.LogStore;
import com.aliyun.openservices.log.common.Shard;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.log.functiontest.MetaAPIBaseFunctionTest;
import com.aliyun.openservices.log.response.ListShardResponse;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ListShardFunctionTest extends MetaAPIBaseFunctionTest {
    String logstore1 = "logstore-listshards";

    @Test
    public void testListShard() throws LogException, InterruptedException {
        Thread.sleep(1000 * 10);
        try {
            client.ListShard(TEST_PROJECT, logstore1);
            fail();
        } catch (LogException ex) {
            assertEquals("LogStoreNotExist", ex.GetErrorCode());
            assertEquals("logstore " + logstore1 + " does not exist", ex.GetErrorMessage());
        }
        LogStore logStore = new LogStore(logstore1, 1, 3);
        createOrUpdateLogStoreNoWait(TEST_PROJECT, logStore);
        Thread.sleep(1000 * 60);
        ListShardResponse response = client.ListShard(TEST_PROJECT, logstore1);
        List<Shard> shards = response.GetShards();
        assertEquals(3, shards.size());
        List<Integer> ids = new ArrayList<Integer>();
        for (Shard shard : shards) {
            ids.add(shard.GetShardId());
        }
        assertTrue(ids.contains(0));
        assertTrue(ids.contains(1));
        assertTrue(ids.contains(2));
    }
}
