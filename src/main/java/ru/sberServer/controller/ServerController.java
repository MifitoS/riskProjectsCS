package ru.sberServer.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping(path = "/test")
public class ServerController {

     //private AtomicLong counter = new AtomicLong();

    @GetMapping("/client")
    public void ClientController() {



    }

}
