package com.bromleyoil.smaugdb;

import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.springframework.stereotype.Component;

/**
 * Contains various helper methods for use in thymeleaf templates.
 * 
 * @author moorwi
 */
@Component
public class Utils {

	public <T> List<List<T>> partition(List<T> list, int partitionSize) {
		return ListUtils.partition(list, partitionSize);
	}
}
