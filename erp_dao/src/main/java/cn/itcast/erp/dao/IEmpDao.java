package cn.itcast.erp.dao;

import cn.itcast.erp.entity.Emp;

/**
 * 员工数据访问接口
 *
 */
public interface IEmpDao extends IBaseDao<Emp>{

    /**
     * 校验用户是否存在，登陆验证
     * @param username
     * @param pwd
     * @return
     */
    Emp findByUsernameAndPwd(String username, String pwd);

    /**
     * 修改密码
     * @param newPwd
     * @param uuid
     */
    void updatePwd(String newPwd, Long uuid);

}
