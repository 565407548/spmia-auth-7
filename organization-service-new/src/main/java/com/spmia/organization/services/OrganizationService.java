package com.spmia.organization.services;

import com.spmia.organization.model.Organization;
import com.spmia.organization.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class OrganizationService {
    @Autowired
    private OrganizationRepository orgRepository;

    public Organization getOrg(String organizationId) {
        Optional<Organization> optional = orgRepository.findById(organizationId);

        if (optional != null) {
            return optional.get();
        } else {
            return null;
        }
    }

    public void saveOrg(Organization org) {
        org.setId(UUID.randomUUID().toString());

        orgRepository.save(org);

    }

    public void updateOrg(Organization org) {
        orgRepository.save(org);
    }

    public void deleteOrg(Organization org) {
        orgRepository.deleteById(org.getId());
    }
}
