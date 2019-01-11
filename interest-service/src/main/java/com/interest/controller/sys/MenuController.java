package com.interest.controller.sys;

import com.interest.model.ModuleEntity;
import com.interest.model.PageResult;
import com.interest.service.ModuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class MenuController {

    private Logger log = LoggerFactory.getLogger(MenuController.class);

    @Resource(name = "menuServiceImpl")
    private ModuleService menuService;

    /**
     * 获取该用户的菜单权限
     *
     * @return
     */
    @GetMapping("/manage/menu")
    public List<ModuleEntity> menuList() {
        return menuService.getMyMenuList();
    }

    /**
     * 获取menus表数据
     *
     * @param pageSize
     * @param page
     * @return
     */
    @GetMapping("/menus")
    public PageResult menusList(int pageSize, int page, String menuId) {
        PageResult pageResult = new PageResult();
        pageResult.setData(menuService.menusList(pageSize, page * pageSize, menuId));
        pageResult.setTotalCount(menuService.menusSize(pageSize, page * pageSize, menuId));
        log.debug("The method is ending");
        return pageResult;
    }

    /**
     * 通过parentId得到menus列表
     *
     * @param parentId
     * @return
     */
    @GetMapping("/menus/parentId")
    public List<ModuleEntity> menusByParentId(int parentId) {
        return menuService.menusByParentId(parentId);
    }

    /**
     * 新建菜单信息
     *
     * @param moduleEntity
     * @return
     */
    @PostMapping("/menus/menu")
    public ModuleEntity insertMenu(@RequestBody ModuleEntity moduleEntity) {
        menuService.insertMenu(moduleEntity);
        log.debug("The method is ending");
        return moduleEntity;
    }

    /**
     * 修改菜单信息
     *
     * @param moduleEntity
     * @param id
     * @return
     */
    @PutMapping("/menus/{id}")
    public ModuleEntity updateMenu(@RequestBody ModuleEntity moduleEntity, @PathVariable int id) {
        if (moduleEntity.getId() == id) {
            menuService.updateMenu(moduleEntity);
        }
        log.debug("The method is ending");
        return moduleEntity;
    }

    /**
     * 删除菜单信息
     *
     * @param groupId
     * @return
     */
    @DeleteMapping("/menus")
    public List<String> deleteMenus(@RequestBody List<String> groupId) {
        menuService.deleteMenus(groupId);
        return groupId;
    }

    /**
     * 获取二级菜单
     *
     * @return
     */
    @GetMapping("/menus/submenus")
    public List<ModuleEntity> getSubmenus() {
        return menuService.getSubmenus();
    }
}
