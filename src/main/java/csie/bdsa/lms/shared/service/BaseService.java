package csie.bdsa.lms.shared.service;

import csie.bdsa.lms.shared.dto.BaseDto;
import csie.bdsa.lms.shared.exception.NotFoundException;
import csie.bdsa.lms.shared.mapper.BaseMapper;
import csie.bdsa.lms.shared.model.BaseEntity;
import csie.bdsa.lms.shared.repository.BaseRepository;
import java.util.List;
import java.util.Set;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public abstract class BaseService<MODEL extends BaseEntity<ID>, DTO extends BaseDto<ID>, ID> {

    protected final BaseRepository<MODEL, ID> repository;
    protected final BaseMapper<MODEL, DTO, ID> mapper;

    public List<DTO> findAll() {
        return mapper.toDto((List<MODEL>) repository.findAll());
    }

    public Page<DTO> findAll(Pageable pageable, String search) {
        return repository.findContaining(pageable, "%" + search + "%").map(mapper::toDto);
    }

    public List<DTO> findById(Set<ID> ids) {
        boolean anyNotFound = ids.stream().anyMatch((id) -> !repository.existsById(id));
        if (anyNotFound) {
            throw new NotFoundException("At least an id not found");
        }
        return mapper.toDto((List<MODEL>) repository.findAllById(ids));
    }

    @Transactional
    public DTO save(DTO dto) {
        ID id = dto.getId();
        if (id != null && !repository.existsById(id)) {
            throw new NotFoundException("Id not found");
        }
        return this.forceSave(dto);
    }

    @Transactional
    public DTO forceSave(DTO dto) {
        MODEL model = mapper.toModel(dto);
        return mapper.toDto(repository.save(model));
    }

    @Transactional
    public void delete(Set<ID> ids) {
        boolean anyNotFound = ids.stream().anyMatch((id) -> !repository.existsById(id));
        if (anyNotFound) {
            throw new NotFoundException("At least an id not found");
        }
        repository.softDeleteByIds(ids);
    }
}
