package com.ssafy.uniqon.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
public class BasicServiceTest {

    @Autowired
    BasicService basicService;

    @Test
    public void getPot() throws InterruptedException, ExecutionException, IOException {
        assertThat(basicService.getPot()).isEqualTo(100);
    }

    @Test
    public void setPot() throws InterruptedException, ExecutionException, IOException {
        basicService.setPot(100);
    }
    @Test
    public void create() throws InterruptedException, ExecutionException, IOException {
        basicService.create("0xE9FF62Cc98Ff6cDA925F2730966A2536dEaB8C28", "https://ipfs.io/ipfs/QmR7K5JJsbCSCBmNw9xtN9AHh46WfuUochcNgUaj2Zxi9d");
    }
    @Test
    public void tokenURI() throws InterruptedException, ExecutionException, IOException {
        String tokenURI = basicService.tokenURI(1);
        assertThat(tokenURI).isEqualTo("https://ipfs.io/ipfs/QmR7K5JJsbCSCBmNw9xtN9AHh46WfuUochcNgUaj2Zxi9d");
    }
    @Test
    public void current() throws InterruptedException, ExecutionException, IOException {
        int current = basicService.current();
        assertThat(current).isEqualTo(3);
    }

    @Test
    public void balaceOf() throws Exception {
        basicService.balanceOf();
    }

    @Test
    public void transfer() throws Exception{
        basicService.transfer();
    }
}