package br.org.apae.api.controllers.patient;

import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;

public class DebugResultHandler implements ResultHandler {
    @Override
    public void handle(MvcResult result) throws Exception {
        System.out.println("========== DEBUG ==========");
        if (result.getResolvedException() != null) {
            result.getResolvedException().printStackTrace();
        }
        System.out.println("Status: " + result.getResponse().getStatus());
        System.out.println("Content: " + result.getResponse().getContentAsString());
        System.out.println("===========================");
    }
}
