package com.spmia.esservice.controller;

import com.spmia.esservice.data.Goods;
import com.spmia.esservice.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @description:
 * @author: Zheng Jim
 * @date: 19-2-19 下午4:36
 */
@Slf4j
@RestController
@RequestMapping(value = "v1/goods")
public class GoodsController {
    @Autowired
    GoodsService goodsService;

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public List<Goods> getOrganization(@RequestParam(value = "title", required = false) String title,
                                       @RequestParam(value = "name", required = false) String name,
                                       @RequestParam(value = "comment", required = false) String comment) {
        log.info("Looking up data for goods {} {} {}", title, name, comment);

        Goods goods = new Goods();
        goods.setTitle(title);
        goods.setName(name);
        goods.setComment(comment);

        return goodsService.search();
    }
}
