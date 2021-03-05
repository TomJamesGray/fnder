package com.gray;

import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;
import org.jboss.weld.environment.se.Weld;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;

public class ServerInitTest {

    @Test
    public void testInjection() throws IOException {
        SeContainerInitializer initializer = SeContainerInitializer.newInstance();
        try(SeContainer initialize = initializer.initialize()){
            Instance<Server> sv = initialize.select(Server.class);
            Server server = sv.get();
            System.out.println(server);
            assertNotNull(server);
            if (server.testClass == null){
                assert false;
            }
            else{
                assert true;
            }
        }
    }
}
