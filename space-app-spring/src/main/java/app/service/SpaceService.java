package app.service;

import app.repository.SpaceRepository;
import app.util.exception.ResourceNotFoundException;
import app.util.exception.NonUniqueNameException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import app.entity.Space;
import app.entity.SpaceType;
import app.repository.SpaceTypeRepository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
public class SpaceService {
    private final SpaceRepository repo;
    private final SpaceTypeRepository typeRepo;

    @Autowired
    public SpaceService(
            SpaceRepository repo, SpaceTypeRepository typeRepo) {
        this.repo = repo;
        this.typeRepo = typeRepo;
    }

    public List<Space> getAll() {
        return repo.findAllByOrderByIdAsc();
    }

    public Space getById(int id) throws ResourceNotFoundException {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Space with id " + id + " not found"));
    }

    @Transactional
    public Space create(String name, BigDecimal price, int spaceTypeId)
            throws NonUniqueNameException {
        if(repo.existsByName(name)) throw new NonUniqueNameException(
                "Space with name " + name + " already exists");
        return repo.save(new Space(null, name, price, new SpaceType(spaceTypeId, "")));
    }

    @Transactional
    public Space edit(int spaceId, String name, BigDecimal price, int spaceTypeId)
            throws NonUniqueNameException {
        repo.findByName(name).ifPresent(value -> {
            if (value.getId() != spaceId) throw new NonUniqueNameException(
                    "Space with name " + name + " already exists");
        });

        SpaceType type = typeRepo.findById(spaceTypeId).orElseThrow(
                () -> new ResourceNotFoundException("Space type witd id " + spaceTypeId + " not found"));

        return repo.save(new Space(spaceId, name, price, type));
    }

    @Transactional
    public void delete(int id) throws ResourceNotFoundException {
        if(!repo.existsById(id)) throw new ResourceNotFoundException(
                "Space with id " + id + " not found");
        repo.deleteById(id);
    }

    @Transactional
    public SpaceType createType(String name)
            throws NonUniqueNameException {
        if(typeRepo.existsByName(name)) throw new NonUniqueNameException(
                "Space type with name " + name + " already exists");
        return typeRepo.save(new SpaceType(null, name));
    }

    @Transactional
    public void deleteType(int typeId) throws ResourceNotFoundException {
        if(!repo.existsById(typeId)) throw new ResourceNotFoundException(
                "Space type with id " + typeId + " not found");
        typeRepo.deleteById(typeId);
    }

    public List<SpaceType> getAllTypes() {
        return typeRepo.findAll();
    }
}
