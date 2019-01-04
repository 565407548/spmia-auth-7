package com.spmia.specialroutes.services;


import com.spmia.specialroutes.exception.NoRouteFound;
import com.spmia.specialroutes.model.AbTestingRoute;
import com.spmia.specialroutes.repository.AbTestingRouteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AbTestingRouteService {

    private static final Logger logger = LoggerFactory.getLogger(AbTestingRouteService.class);

    @Autowired
    private AbTestingRouteRepository abTestingRouteRepository;

    public AbTestingRoute getRoute(String serviceName) {
        AbTestingRoute route = abTestingRouteRepository.findByServiceName(serviceName);

        if (route == null) {
            throw new NoRouteFound();
        }

        logger.info(String.format("old-service:%s, new-service:%s, active:%s, weight:%d", route.getServiceName(), route.getEndpoint(), route.getActive(), route.getWeight()), route.getWeight());

        return route;
    }

    public void saveAbTestingRoute(AbTestingRoute route) {

        abTestingRouteRepository.save(route);

    }

    public void updateRouteAbTestingRoute(AbTestingRoute route) {
        abTestingRouteRepository.save(route);
    }

    public void deleteRoute(AbTestingRoute route) {
        abTestingRouteRepository.deleteById(route.getServiceName());
    }
}
