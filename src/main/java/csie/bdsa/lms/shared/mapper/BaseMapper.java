package csie.bdsa.lms.shared.mapper;

import csie.bdsa.lms.shared.dto.BaseDto;
import csie.bdsa.lms.shared.model.BaseEntity;
import java.util.List;

public interface BaseMapper<MODEL extends BaseEntity<ID>, DTO extends BaseDto<ID>, ID> {

    DTO toDto(MODEL model);
    MODEL toModel(DTO dto);
    List<DTO> toDto(List<MODEL> model);
    List<MODEL> toModel(List<DTO> dto);

}
