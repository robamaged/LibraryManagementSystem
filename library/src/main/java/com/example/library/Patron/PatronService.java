package com.example.library.Patron;

import com.example.library.Borrowing.BorrowingRecordRepository;
import com.example.library.Borrowing.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PatronService {
    @Autowired
    private  BorrowingRecordRepository borrowingRecordRepository;

    @Autowired
    private PatronRepository patronRepository;

    public List<Patron> getAllPatrons() {
        return patronRepository.findAll();
    }

    public Patron getPatronById(Long id) {
        return patronRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Patron not found"));
    }

    public Patron savePatron(Patron patron) {
        return patronRepository.save(patron);
    }

    public Patron updatePatron(Long id, Patron patronDetails) {
        Patron patron = patronRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Patron not found"));

        patron.setName(patronDetails.getName());
        patron.setContactInformation(patronDetails.getContactInformation());

        return patronRepository.save(patron);
    }

    @Transactional
    public void deletePatron(Long patronId) {
        Patron patron = patronRepository.findById(patronId)
                .orElseThrow(() -> new ResourceNotFoundException("Patron not found with id: " + patronId));

        boolean hasActiveBorrowingRecords = borrowingRecordRepository.existsByPatronIdAndReturnDateIsNull(patronId);

        if (hasActiveBorrowingRecords) {
            throw new IllegalStateException("Cannot delete patron with active borrowing records.");
        }

        patronRepository.deleteById(patronId);
    }
}

