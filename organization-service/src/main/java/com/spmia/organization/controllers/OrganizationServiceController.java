package com.spmia.organization.controllers;


import com.spmia.organization.model.Organization;
import com.spmia.organization.services.OrganizationService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "v1/organizations")
public class OrganizationServiceController {
    // @Slf4j 完成了工作
//    private static final Logger logger = LoggerFactory.getLogger(OrganizationServiceController.class);

    @Autowired
    private OrganizationService orgService;

    @RequestMapping(value = "/{organizationId}", method = RequestMethod.GET)
    public Organization getOrganization(@PathVariable("organizationId") String organizationId) {
        log.info("Looking up data for org {}", organizationId);

        Organization org = orgService.getOrg(organizationId);
        org.setContactName(org.getContactName());

        log.info(String.format("Org: %s", org.toString()));
        return org;
    }

    @RequestMapping(value = "/{organizationId}", method = RequestMethod.PUT)
    public void updateOrganization(@PathVariable("organizationId") String orgId, @RequestBody Organization org) {
        orgService.updateOrg(org);

    }

    @RequestMapping(value = "/{organizationId}", method = RequestMethod.POST)
    public void saveOrganization(@RequestBody Organization org) {
        orgService.saveOrg(org);
    }

    @RequestMapping(value = "/{organizationId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrganization(@PathVariable("organizationId") String orgId) {
        orgService.deleteOrg(orgId);
    }
}
