package cn.itcast.erp.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import cn.itcast.erp.biz.IBaseBiz;
import cn.itcast.erp.exception.ErpException;
import cn.itcast.erp.util.WebUtil;

/**
 * 通用Action类
 *
 * @param <T>
 */
@SuppressWarnings("unchecked")
public abstract class BaseAction<T> {

    private IBaseBiz<T> baseBiz;

    public void setBaseBiz(IBaseBiz<T> baseBiz) {
        this.baseBiz = baseBiz;
    }

    private T t; // 编辑数据
    private T t1; // 查询使用
    private T t2; // 查询使用
    private Object obj; // 查询使用

    private int rows; // 每页显示记录数
    private int page; // 页码
    private Long id; // 编号

    public Long getId() {
        return id;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public T getT1() {
        return t1;
    }

    public void setT1(T t1) {
        this.t1 = t1;
    }

    public T getT2() {
        return t2;
    }

    public void setT2(T t2) {
        this.t2 = t2;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public void list() {
        // 得到部门列表
        List<T> list = baseBiz.getList(t1, t2, obj);
        // 把部门列表转成json格式的字符串
        WebUtil.write(list);
    }

    /**
     * 分页查询
     */
    public void listByPage() {
        Long total = baseBiz.getCount(t1, t2, obj);
        List<T> list = baseBiz.getListByPage(t1, t2, total, (page-1) * rows, rows);
        /*{
            "total":15,
            "rows":[
                {"name":"管理员组","tele":"000000","uuid":1},
                {"name":"总裁办","tele":"111111","uuid":2}
            ]
        }*/
		//System.out.println("abc");
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("total", total);
        resultMap.put("rows", list);
        //DisableCircularReferenceDetect 禁止循环引用 去掉$ref
        // dep:{$ref: "$.rows[1].dep"}
        // WriteMapNullValue 如果属性值为空，也要输出这个字段且它的值为null
        String jsonString = JSON.toJSONString(resultMap,
                SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteMapNullValue);
        WebUtil.write(jsonString);
    }

    /**
     * 新增
     */
    public void add() {
        try {
            baseBiz.add(t);
            WebUtil.ajaxReturn(true, "新增成功");
        } catch (ErpException e) {
            e.printStackTrace();
            WebUtil.ajaxReturn(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            WebUtil.ajaxReturn(false, "新增失败");
        }
    }

    /**
     * 删除
     */
    public void delete() {

        try {
            baseBiz.delete(id);
            WebUtil.ajaxReturn(true, "删除成功");
        } catch (ErpException e) {
            e.printStackTrace();
            WebUtil.ajaxReturn(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            WebUtil.ajaxReturn(false, "删除失败");
        }
    }

    /**
     * 回显数据
     */
    public void get() {
        T tShow = baseBiz.get(id);
        //WithDateFormat, 如果转换对象中有日期，则转成指定格式的字符串
        String jsonString = JSON.toJSONStringWithDateFormat(tShow,"yyyy-MM-dd");
        WebUtil.write(mapJson(jsonString,"t."));
    }

    /**
     * 更新
     */
    public void update() {
        try {
            baseBiz.update(t);
            //{success:true,message:''};
            WebUtil.ajaxReturn(true, "更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            WebUtil.ajaxReturn(false, "更新失败");
        }
    }

    /**
     * 给json对象的key值加前缀
     * @param jsonString
     * @param prefix
     * @return
     */
    private Map<String,Object> mapJson(String jsonString, String prefix){
        //{"name":"管理员组","tele":"000000","uuid":1} 不符合要求
        Map<String, Object> jsonMap = JSON.parseObject(jsonString);
        //{"t.name":"管理员组","t.tele":"000000","t.uuid":1}
        Map<String,Object> newMap = new HashMap<String,Object>();

        //"t.dep":{"name":"管理员组","tele":"000000","uuid":1},
        //"t.dep.name":"管理员组","t.dep.tele":"000000","t.dep.uuid":1
        for (String key : jsonMap.keySet()) {
            if(jsonMap.get(key) instanceof Map) {
                //{"name":"管理员组","tele":"000000","uuid":1},

                Map<String,Object> innerMap = (Map<String,Object>)jsonMap.get(key);
                for(String innerKey : innerMap.keySet()) {
                    //"t.dep.name":"管理员组","t.dep.tele":"000000","t.dep.uuid":1
                    newMap.put(prefix + key + "." + innerKey, innerMap.get(innerKey));
                }
            }else {
                newMap.put(prefix + key, jsonMap.get(key));
            }
        }
        return newMap;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
