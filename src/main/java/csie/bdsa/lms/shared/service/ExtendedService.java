package csie.bdsa.lms.shared.service;

import csie.bdsa.lms.shared.dto.BaseDto;
import csie.bdsa.lms.shared.mapper.BaseMapper;
import csie.bdsa.lms.shared.model.BaseEntity;
import csie.bdsa.lms.shared.repository.BaseRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;


public abstract class ExtendedService<MODEL extends BaseEntity<ID>, DTO extends BaseDto<ID>, ID> extends BaseService<MODEL, DTO, ID>{

    public ExtendedService(BaseRepository<MODEL, ID> repository, BaseMapper<MODEL, DTO, ID> mapper) {
        super(repository, mapper);
    }

    @Override
    public List<DTO> findAll() {
        List<DTO> dto = super.findAll();
        return dto.isEmpty() ? dto : mapMissingValues(dto);
    }

    @Override
    public Page<DTO> findAll(Pageable pageable, String search) {
        Page<DTO> dto = super.findAll(pageable, search);
        return dto.getContent().isEmpty() ?
                dto : new PageImpl<>(mapMissingValues(dto.getContent()), pageable, dto.getTotalElements());
    }

    @Override
    public List<DTO> findById(Set<ID> ids) {
        List<DTO> dto = super.findById(ids);
        return dto.isEmpty() ? dto : mapMissingValues(dto);
    }

    protected Set<ID> getIds(List<DTO> list, Getter<DTO, ID> getter) {
        return list.stream()
                .filter(e -> getter.get(e) != null)
                .map(e -> getter.get(e).getId())
                .collect(Collectors.toSet());
    }

    protected <MISSING_DTO extends BaseDto<ID>> void replaceIds(List<DTO> list, List<MISSING_DTO> missingList,
                                                               Getter<DTO, ID> getter, Setter<DTO, MISSING_DTO> setter) {
        list.forEach(e -> setter.set(e, missingList.stream()
                        .filter(missing -> getter.get(e).getId().equals(missing.getId()))
                        .findFirst()
                        .orElse(null)));
    }

    protected<MISSING_DTO extends BaseDto<ID>> void map(List<DTO> list, Getter<DTO, ID> getter,
                                                        Setter<DTO, MISSING_DTO> setter, FeignClient<MISSING_DTO, ID> client) {
        Set<ID> ids = getIds(list, getter);
        List<MISSING_DTO> missingList = client.call(ids);
        if (!missingList.isEmpty()) {
           replaceIds(list, missingList, getter, setter);
        }
    }

    protected abstract List<DTO> mapMissingValues(List<DTO> dto);

    @FunctionalInterface
    protected interface FeignClient<DTO, ID> {
        List<DTO> call(Set<ID> ids);
    }

    @FunctionalInterface
    protected interface Getter<DTO, ID> {
        BaseDto<ID> get(DTO dto);
    }

    @FunctionalInterface
    protected interface Setter<DTO, OTHER_DTO> {
        void set(DTO dto, OTHER_DTO otherDto);
    }
}
