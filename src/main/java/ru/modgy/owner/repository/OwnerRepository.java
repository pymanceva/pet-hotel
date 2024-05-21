package ru.modgy.owner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.modgy.owner.model.Owner;

import java.util.List;
import java.util.Optional;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
    Optional<Owner> findByMainPhoneOrOptionalPhone(String mainPhoneNumber, String optionalPhoneNumber);

    @Query(value = "select owner from Owner as owner where :direction = 'name'" +
                   " and (owner.firstName like concat ('%', :searchLine, '%')" +
                   " or owner.middleName like concat ('%', :searchLine, '%')" +
                   " or owner.lastName like concat ('%', :searchLine, '%'))" +
                   " or :direction ='phone'" +
                   " and (owner.mainPhone like concat ('%', :searchLine, '%')" +
                   " or owner.optionalPhone like concat ('%', :searchLine, '%'))")
    List<Owner> searchOwner(
            @Param("searchLine") String searchLine,
            @Param("direction") String direction
    );
}
