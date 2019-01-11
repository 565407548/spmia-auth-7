package com.interest.service.impl;

import com.interest.dao.ModuleDao;
import com.interest.model.ModuleEntity;
import com.interest.service.ModuleService;
import com.interest.utils.SecurityAuthenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("menuServiceImpl")
public class ModuleServiceImpl implements ModuleService {

    @Autowired
    private ModuleDao moduleDao;

    @Override
    public List<ModuleEntity> getMyMenuList() {

        List<String> idsList = moduleDao.getModulesById(SecurityAuthenUtil.getMyRoleList());

        String allIds = "";
        for (String ids : idsList) {
            allIds = allIds + ids;
        }
        String[] ids = allIds.split(";");
        List<ModuleEntity> parentMenuList = moduleDao.getParentMenuListById(ids);
        List<ModuleEntity> childrenMenuList = moduleDao.getMenuListById(ids);
        List<ModuleEntity> menuList = new ArrayList<ModuleEntity>();

        for (ModuleEntity parentMenu : parentMenuList) {
            List<ModuleEntity> menuListTemp = new ArrayList<ModuleEntity>();
            for (ModuleEntity childrenMenu : childrenMenuList) {
                if (parentMenu.getId() == childrenMenu.getParentId()) {
                    menuListTemp.add(childrenMenu);
                }
            }
            parentMenu.setChildren(menuListTemp);
            menuList.add(parentMenu);
        }

        return menuList;
    }

    @Override
    public List<ModuleEntity> menusList(int pageSize, int start, String menuId) {
        return moduleDao.menusList(pageSize, start, menuId);
    }

    @Override
    public Integer menusSize(int pageSize, int start, String menuId) {
        return moduleDao.menusSize(pageSize, start, menuId);
    }

    @Override
    public void insertMenu(ModuleEntity moduleEntity) {
        moduleDao.insertMenu(moduleEntity);
    }

    @Override
    public void updateMenu(ModuleEntity moduleEntity) {
        moduleDao.updateMenu(moduleEntity);
    }

    @Override
    public void deleteMenus(List<String> groupId) {
        moduleDao.deleteMenus(groupId);
    }

    @Override
    public List<ModuleEntity> menusByParentId(int parentId) {
        return moduleDao.menusByParentId(parentId);
    }

    @Override
    public List<ModuleEntity> getSubmenus() {
        return moduleDao.getSubmenus();
    }

}
