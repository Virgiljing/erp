package cn.itcast.erp.biz.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.erp.biz.IDepBiz;
import cn.itcast.erp.dao.IDepDao;
import cn.itcast.erp.dao.IEmpDao;
import cn.itcast.erp.entity.Dep;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.exception.ErpException;

/**
 * 部门业务逻辑类
 *
 */
@Service("depBiz")
public class DepBiz extends BaseBiz<Dep> implements IDepBiz {

    private IDepDao depDao;

    @Autowired
    private IEmpDao empDao;

    @Resource(name="depDao")
    public void setDepDao(IDepDao depDao) {
        this.depDao = depDao;
        super.setBaseDao(this.depDao);
    }

    @Override
    @Transactional
    public void delete(Long uuid) {

        //构建查询条件
        Emp emp = new Emp();
        emp.setDep(new Dep());
        // 查询条件为部门编号
        emp.getDep().setUuid(uuid);
        //拿部门编号去员工表查询 条件为这个部门的员工个数
        Long count = empDao.getCount(emp, null, null);
        if(count > 0) {
            //部门下有人
            throw new ErpException("该部门下有员工，不能删除");
        }
        super.delete(uuid);
    }

}
