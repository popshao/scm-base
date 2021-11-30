package ${package};

import com.gangling.scm.base.middleware.datasource.domain.BaseService;
import com.gangling.scm.base.common.page.PageResult;
import ${tableClass.fullClassName};
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ${tableClass.shortClassName}ServiceImpl extends BaseServiceImpl<${tableClass.shortClassName}Mapper, ${tableClass.shortClassName}> implements ${tableClass.shortClassName}Service {

    @Autowired
    private ${tableClass.shortClassName}DAO ${tableClass.variableName}DAO;

    @Override
    public PageResult<${tableClass.shortClassName}> selectForPage(${tableClass.shortClassName}Param param) {
        return ${tableClass.variableName}DAO.selectForPage(param);
    }
}




