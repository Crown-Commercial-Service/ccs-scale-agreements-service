package uk.gov.crowncommercial.dts.scale.service.agreements.cache;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Cache;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

@Component
@Slf4j
public class CachingService {

    @Autowired
    EntityManager entityManager;

    @Scheduled(fixedDelayString = "PT15M")
    public void evictAllEntities() {
        try {

            Session session = entityManager.unwrap(Session.class);
            SessionFactory sessionFactory = session.getSessionFactory();
            Cache cache=sessionFactory.getCache();
            log.info("Evicting entities from Cache:  Started" );
            cache.evictAll();
            log.info("Evicting entities from Cache:  Completed" );

        } catch (Exception e) {
            log.error("SessionController",
                    "evictAllEntities",
                    "Error evicting hibernate cache entities: ", e);
        }
    }
}
