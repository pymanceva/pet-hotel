package ru.modgy.pet.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ru.modgy.pet.dto.PetFilterParams;
import ru.modgy.pet.model.Pet;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;

public class SearchPetRepositoryImpl implements SearchPetRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Pet> findAllPetsByParams(PetFilterParams params) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pet> cq = cb.createQuery(Pet.class);
        Root<Pet> petRoot = cq.from(Pet.class);

        String searchValue = ("%" + params.getName() + "%").toLowerCase();

        cq.select(petRoot).where(cb.like(cb.lower(petRoot.get("name")), searchValue));
        return entityManager.createQuery(cq)
                .getResultList();
    }


}
