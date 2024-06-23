package com.udacity.jdnd.course3.critter.service;
import com.udacity.jdnd.course3.critter.exception.CustomerExeption;
import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetDTO;

import java.util.List;

public interface PetService {
    PetDTO savePet(PetDTO petDTO) throws CustomerExeption;
    PetDTO getPet(long id);
    List<PetDTO> getPets();
    List<PetDTO> getPetsByOwner(long ownerId);
}
