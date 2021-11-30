package ${package};

import com.gangling.scm.base.common.rpc.result.PlainResult;
import com.gangling.scm.base.common.page.PageResult;

import java.util.List;
import java.util.Map;

public interface ${tableClass.shortClassName}API {

    PlainResult<${tableClass.shortClassName}DTO> getById(Long id);

    PlainResult<List<${tableClass.shortClassName}DTO>> listByIdList(List<Long> idList);

    PlainResult<Map<Long, ${tableClass.shortClassName}DTO>> mapByIdList(List<Long> idList);

    PlainResult<PageResult<${tableClass.shortClassName}DTO>> selectForPage(${tableClass.shortClassName}Param param);
}




