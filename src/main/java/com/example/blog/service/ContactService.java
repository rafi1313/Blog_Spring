package com.example.blog.service;

import com.example.blog.model.entity.Contact;
import com.example.blog.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactService {

    ContactRepository contactRepository;

    @Autowired
    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public Contact createContact(Contact contact){
        Contact savedContact = contactRepository.save(contact);
        return savedContact;
    }
}
