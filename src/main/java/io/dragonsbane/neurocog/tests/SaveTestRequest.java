package io.dragonsbane.neurocog.tests;

import io.onemfive.core.ServiceRequest;
import io.onemfive.data.DID;

public class SaveTestRequest extends ServiceRequest {
    public static int TEST_REQUIRED = 1;
    public DID did;
    public ImpairmentTest test;
}
