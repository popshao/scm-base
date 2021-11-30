package ${package};

import com.gangling.scm.base.middleware.datasource.domain.BaseService;
import com.gangling.scm.base.common.page.PageResult;
import ${tableClass.fullClassName};

import java.util.List;

public interface ${tableClass.shortClassName}Service extends BaseService<${tableClass.shortClassName}> {

    PageResult<${tableClass.shortClassName}> selectForPage(${tableClass.shortClassName}Param param);

}




