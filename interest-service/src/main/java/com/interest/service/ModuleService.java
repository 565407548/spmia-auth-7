package com.interest.service;

import com.interest.model.ModuleEntity;

import java.util.List;

public interface ModuleService {

    /**
     * 得到菜单List
     *
     * @param id
     * @return
     */
    List<ModuleEntity> getMyMenuList();

    /**
     * 获取menus列表
     *
     * @param pageSize
     * @param menuId
     * @param i
     * @return
     */
    List<ModuleEntity> menusList(int pageSize, int start, String menuId);

    /**
     * 获取menus列表的总量
     *
     * @param loginName
     * @param pageSize
     * @param menuId
     * @param i
     * @return
     */
    Integer menusSize(int pageSize, int start, String menuId);

    /**
     * 新建菜单信息
     *
     * @param moduleEntity
     */
    void insertMenu(ModuleEntity moduleEntity);

    /**
     * 修改菜单信息
     *
     * @param moduleEntity
     */
    void updateMenu(ModuleEntity moduleEntity);

    /**
     * 删除菜单信息
     *
     * @param groupId
     */
    void deleteMenus(List<String> groupId);

    /**
     * 通过parentId得到menus列表
     *
     * @param parentId
     * @return
     */
    List<ModuleEntity> menusByParentId(int parentId);

    /**
     * 获取二级菜单
     *
     * @return
     */
    List<ModuleEntity> getSubmenus();

}
