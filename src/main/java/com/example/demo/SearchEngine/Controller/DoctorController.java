package com.example.demo.SearchEngine.Controller;


import com.example.demo.SearchEngine.Service.SyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/doctors")
public class DoctorController {

    @Autowired
    private SyncService syncService;

    @GetMapping
    private void reIndex(){
        syncService.syncData();
    }
}
