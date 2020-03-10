package uk.gov.crowncommercial.dts.scale.agreements.repository;

import java.io.File;
import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * Throw-away component for interim Mock Service to load sample data from JSON
 * files (will be deleted in actual implementation and replaced by Database).
 *
 */
@Component
@Slf4j
public class DataLoader {

	@SuppressWarnings("unchecked")
	public <T> T convertJsonToObject(String filename, Class<?> target) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			File json = new File(getClass().getClassLoader().getResource(filename).getFile());
			return (T) objectMapper.readValue(json, Class.forName(target.getName()));
		} catch (Exception e) {
			log.error(e.toString(), e);
			// RuntimeException is just a convenience for throwaway class
			throw new RuntimeException("Unable to load mock data from file: " + filename);
		}
	}

	public <T> T convertJsonToList(String filename, Class<?> target) {

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			File json = new File(getClass().getClassLoader().getResource(filename).getFile());
			return objectMapper.readValue(json,
					objectMapper.getTypeFactory().constructCollectionType(List.class, Class.forName(target.getName())));

		} catch (Exception e) {
			log.error(e.toString(), e);
			// RuntimeException is just a convenience for throwaway class
			throw new RuntimeException("Unable to load mock data from file: " + filename);
		}
	}
}
