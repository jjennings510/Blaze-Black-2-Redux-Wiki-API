package com.github.blazeblack2reduxwikiapi.service;

import com.github.blazeblack2reduxwikiapi.model.Type;
import com.github.blazeblack2reduxwikiapi.repository.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class TypeService {
    private final TypeRepository typeRepository;

    @Autowired
    public TypeService(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    public void addType(String name) {
        Type type = new Type(name);
        if (typeRepository.findByName(name) == null) {
            System.out.println("Saving to DB:\t" + type);
            typeRepository.save(type);
        }
    }
    public Optional<Type> getTypeByName(String name) {
        return typeRepository.findByName(name);
    }

    public long getRepositoryCount() {
        return typeRepository.count();
    }
}
