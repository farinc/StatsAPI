package com.farinc.stats.test.factories;

import com.farinc.stats.api.implementations.factories.ComponentDataFactory;
import com.farinc.stats.test.data.TestComponentData;
import com.farinc.stats.test.instances.TestComponent;

public class TestComponentDataFactory extends ComponentDataFactory<TestComponent, TestComponentData> {

    @Override
    public TestComponentData get() {
        return new TestComponentData();
    }

    @Override
    public String getComponentID() {
        return "test";
    }
    
}