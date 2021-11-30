package ${package};

import com.gangling.scm.base.utils.BeanUtil;
import com.gangling.scm.base.middleware.datasource.dao.BaseDAO;
import com.gangling.scm.base.common.page.PageResult;
import ${tableClass.fullClassName};
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ${tableClass.shortClassName}DAO extends BaseDAO<${tableClass.shortClassName}Mapper, ${tableClass.shortClassName}> {

    public PageResult<${tableClass.shortClassName}> selectForPage(${tableClass.shortClassName}Param param) {
        return selectForPage(param, () -> mapper.select(BeanUtil.copySpring(param, ${tableClass.shortClassName}.class)));
    }
}




