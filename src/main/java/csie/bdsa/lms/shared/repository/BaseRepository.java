package csie.bdsa.lms.shared.repository;

import csie.bdsa.lms.shared.model.BaseEntity;
import java.util.Set;
import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

@NoRepositoryBean
@Transactional
public interface BaseRepository<MODEL extends BaseEntity<ID>, ID> extends CrudRepository<MODEL, ID> {

    @Override
    @Query("SELECT x FROM #{#entityName} x WHERE x.deleted = false")
    Iterable<MODEL> findAll();

    @Query("SELECT x FROM #{#entityName} x WHERE x.deleted = true")
    Iterable<MODEL> findAllDeleted();

    @Query("SELECT x FROM #{#entityName} x")
    Iterable<MODEL> findAllWithSoftDeleted();

    @Query("UPDATE #{#entityName} x SET x.deleted = true WHERE x.id = :id")
    @Modifying
    void softDeleteById(ID id);

    @Query("UPDATE #{#entityName} x SET x.deleted = true WHERE x.id IN :ids")
    @Modifying
    void softDeleteByIds(Set<ID> ids);

    @Query("UPDATE #{#entityName} x SET x.deleted = false WHERE x.id = :id")
    @Modifying
    void restoreById(ID id);

    @Query("UPDATE #{#entityName} x SET x.deleted = false WHERE x.id IN ids")
    @Modifying
    void restoreByIds(Set<ID> ids);

    @Query("SELECT x FROM #{#entityName} x WHERE x.deleted = false AND CAST(x.id AS string) LIKE :search")
    Page<MODEL> findContaining(Pageable pageable, @Param("search") String search);

}
