package com.interest.dao;

import com.interest.model.ModuleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ModuleDao {
    /**
     * 通过id得到Modules集合
     *
     * @param roles
     * @return
     */
    public List<String> getModulesById(@Param("roles") List<String> roles);

    /**
     * 通过用户Id得到一级菜单List
     *
     * @param id
     * @return
     */
    public List<ModuleEntity> getParentMenuListById(@Param("ids") String[] ids);

    public List<ModuleEntity> getMenuListById(@Param("ids") String[] ids);

    /**
     * 获取menus列表
     *
     * @param pageSize
     * @param start
     * @param menuId
     * @return
     */
    public List<ModuleEntity> menusList(@Param("pageSize") int pageSize, @Param("start") int start,
                                        @Param("menuId") String menuId);

    /**
     * 获取menus列表的总量
     *
     * @param pageSize
     * @param start
     * @param menuId
     * @return
     */
    public Integer menusSize(@Param("pageSize") int pageSize, @Param("start") int start,
                             @Param("menuId") String menuId);

    /**
     * 新建菜单信息
     *
     * @param moduleEntity
     */
    public void insertMenu(@Param("moduleEntity") ModuleEntity moduleEntity);

    /**
     * 修改菜单信息
     *
     * @param moduleEntity
     */
    public void updateMenu(@Param("moduleEntity") ModuleEntity moduleEntity);

    /**
     * 删除菜单信息
     *
     * @param groupId
     */
    public void deleteMenus(@Param("groupId") List<String> groupId);

    /**
     * 通过parentId得到menus列表
     *
     * @param parentId
     * @return
     */
    public List<ModuleEntity> menusByParentId(@Param("parentId") int parentId);

    /**
     * 获取二级菜单
     *
     * @return
     */
    public List<ModuleEntity> getSubmenus();

}
