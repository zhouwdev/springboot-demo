/**
 * 
 */
package springboot.demo.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 基础类 MAPPER
 * 
 */
public interface IBaseMapper<T> {
	
    /**
     * 获取 序列号 
     * @return
     */
    Long getIdValue();
	
	/**
	 * 插入记录
	 * @param obj
	 * @return
	 */
    int insert(T obj);

    /**
     * 插入记录(有效字段,即非空字段)
     * @param obj
     * @return
     */
    int insertSelective(T obj);

	/**
	 * 物理删除记录
	 * @param seq
	 * @return
	 */
	int deleteByPrimaryKey(Integer seq);

    /**
     * 根据ids更新一批记录
     * @return
     */
    int updateByIds(@Param(value = "params") Map<String, Object> params);
	
    /**
     * 更新记录
     * @param obj
     * @return
     */
    int updateByPrimaryKey(T obj);
    
    /**
     * 更新记录(有效字段,即非空字段)
     * @param obj
     * @return
     */
    int updateByPrimaryKeySelective(T obj);

    /**
     * 根据主键 返回记录
     * @param seq
     * @return
     */
    T selectByPrimaryKey(Integer seq);
    
    /**
     * 根据 条件返回记录
     * @param params
     * @return
     */
    T selectByParams(@Param(value = "params") Map<String, Object> params);
    
    /**
     * 查询 符合条件的记录总数
     * @param params
     * @return
     */
    int selectCountByParams(@Param(value = "params") Map<String, Object> params);
    
    
    /**
     * 分页查询 记录，分页条件为null，返回所有
     * @param params 查询条件
     * @param pageOffset 开始游标
     * @param pageSize 每页显示的数量z
     * @param orderParam 排序参数
     * @return
     */
    List<T> selectListByParams(@Param(value = "params") Map<String, Object> params,
                               @Param(value = "pageOffset") Integer pageOffset,
                               @Param(value = "pageSize") Integer pageSize,
                               @Param(value = "orderParam") String orderParam);


    /**
     * 批量删除
     * @return
     */
    int deleteByIds(@Param("isActive") String isActive, @Param("ids") Long[] ids);
}
