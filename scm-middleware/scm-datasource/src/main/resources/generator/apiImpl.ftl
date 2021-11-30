package ${package};

import com.gangling.scm.base.utils.BeanUtil;
import com.gangling.scm.base.utils.ListUtil;
import com.gangling.scm.base.utils.PlainResultUtil;
import com.gangling.scm.base.common.rpc.result.PlainResult;
import com.gangling.scm.base.common.page.PageResult;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ${tableClass.shortClassName}APIImpl implements ${tableClass.shortClassName}API  {

    @Autowired
    private ${tableClass.shortClassName}Service ${tableClass.variableName}Service;

    @Override
    public PlainResult<${tableClass.shortClassName}DTO> getById(Long id) {
        return PlainResultUtil.buildSuccessResult(BeanUtil.copySpring(${tableClass.variableName}Service.getById(id), ${tableClass.shortClassName}DTO.class));
    }

    @Override
    public PlainResult<List<${tableClass.shortClassName}DTO>> listByIdList(List<Long> idList) {
        return PlainResultUtil.buildSuccessResult(BeanUtil.copyList(${tableClass.variableName}Service.listByIdList(idList), ${tableClass.shortClassName}DTO.class));
    }

    @Override
    public PlainResult<Map<Long, ${tableClass.shortClassName}DTO>> mapByIdList(List<Long> idList) {
        List<${tableClass.shortClassName}DTO> list = BeanUtil.copyList(${tableClass.variableName}Service.listByIdList(idList), ${tableClass.shortClassName}DTO.class);
        return PlainResultUtil.buildSuccessResult(list.stream().collect(Collectors.toMap(${tableClass.shortClassName}DTO::getId, s -> s, (s1, s2) -> s1)));
    }

    @Override
    public PlainResult<PageResult<${tableClass.shortClassName}DTO>> selectForPage(${tableClass.shortClassName}Param param) {
        return PlainResultUtil.buildSuccessResult(BeanUtil.copyPage(${tableClass.variableName}Service.selectForPage(param), ${tableClass.shortClassName}DTO.class));
    }
}




