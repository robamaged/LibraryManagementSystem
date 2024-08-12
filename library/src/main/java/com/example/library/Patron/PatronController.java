package com.example.library.Patron;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patrons")
public class PatronController {
    @Autowired
    private PatronService patronService;

    @GetMapping
    public List<Patron> getAllPatrons() {
        return patronService.getAllPatrons();
    }

    @GetMapping("/{id}")
    public Patron getPatronById(@PathVariable Long id) {
        return patronService.getPatronById(id);
    }

    @PostMapping
    public Patron createPatron(@RequestBody Patron patron) {
        return patronService.savePatron(patron);
    }

    @DeleteMapping("/{id}")
    public void deletePatron(@PathVariable Long id) {
        patronService.deletePatron(id);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Patron> updatePatron(@PathVariable Long id, @RequestBody @Valid Patron patronDetails) {
        Patron updatedPatron = patronService.updatePatron(id, patronDetails);
        return ResponseEntity.ok(updatedPatron);
    }
}
