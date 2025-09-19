package uk.gov.crowncommercial.dts.scale.service.agreements.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.crowncommercial.dts.scale.service.agreements.BLL.BusinessLogicClient;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.LotEventTypeUpdate;

/**
 * Controller for actions relating to overall management of event types
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class EventTypesController {
    @Autowired
    private BusinessLogicClient businessLogicClient;

    /**
     * Creates a new event type or amends an existing event type that can then be used separately to attach to events
     */
    @PutMapping("/event-types/manage-type")
    public ResponseEntity<?> manageEventTypeConfiguration(@RequestBody final LotEventTypeUpdate model) throws Exception {
        businessLogicClient.manageEventTypeConfig(model);

        // Operation should be complete now, so return ok (errors would have been thrown above)
        return ResponseEntity.ok().build();
    }
}