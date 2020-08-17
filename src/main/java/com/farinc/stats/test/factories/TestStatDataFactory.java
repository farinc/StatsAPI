package com.farinc.stats.test.factories;

import com.farinc.stats.api.implementations.factories.StatDataFactory;
import com.farinc.stats.test.data.TestStatData;
import com.farinc.stats.test.instances.TestStat;

public class TestStatDataFactory extends StatDataFactory<TestStat, TestStatData> {

    @Override
    public TestStatData get() {
        return new TestStatData();
    }

    @Override
    public String getStatID() {
        return "test";
    }
    
}