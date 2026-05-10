package com.geoconnect.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse<T> {

	private List<T> content;
	private int pageNumber;
	private int pageSize;
	private long totalElements;
	private int totalPages;
	private boolean isFirst;
	private boolean isLast;


	public static <T> PageResponse<T> fromPage(Page<T> page) {
		PageResponse<T> response = new PageResponse<>();
		response.setContent(page.getContent());
		response.setPageNumber(page.getNumber());
		response.setPageSize(page.getSize());
		response.setTotalElements(page.getTotalElements());
		response.setTotalPages(page.getTotalPages());
		response.setFirst(page.isFirst());
		response.setLast(page.isLast());
		return response;
	}
}