package com.must5.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

/**
 * Paginated API Response wrapper for consistent response format with pagination
 * @param <T> the type of the data payload
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaginatedApiResponse<T> {

    private int code;
    private String status;
    private String message;
    private List<T> data;
    private PaginationInfo paginate;

    public PaginatedApiResponse() {
    }

    public PaginatedApiResponse(int code, String status, String message, List<T> data, PaginationInfo paginate) {
        this.code = code;
        this.status = status;
        this.message = message;
        this.data = data;
        this.paginate = paginate;
    }

    // Static factory methods for common responses
    public static <T> PaginatedApiResponse<T> success(List<T> data, PaginationInfo paginate) {
        return new PaginatedApiResponse<>(200, "SUCCESS", "Orders retrieved successfully", data, paginate);
    }

    // Getters and Setters
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public PaginationInfo getPaginate() {
        return paginate;
    }

    public void setPaginate(PaginationInfo paginate) {
        this.paginate = paginate;
    }

    @Override
    public String toString() {
        return "PaginatedApiResponse{" +
                "code=" + code +
                ", status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", paginate=" + paginate +
                '}';
    }

    /**
     * Pagination information helper class
     */
    public static class PaginationInfo {
        private long total;
        private int page;
        private int size;
        private int totalPages;

        public PaginationInfo() {
        }

        public PaginationInfo(long total, int page, int size, int totalPages) {
            this.total = total;
            this.page = page;
            this.size = size;
            this.totalPages = totalPages;
        }

        public long getTotal() { return total; }
        public void setTotal(long total) { this.total = total; }

        public int getPage() { return page; }
        public void setPage(int page) { this.page = page; }

        public int getSize() { return size; }
        public void setSize(int size) { this.size = size; }

        public int getTotalPages() { return totalPages; }
        public void setTotalPages(int totalPages) { this.totalPages = totalPages; }

        @Override
        public String toString() {
            return "PaginationInfo{" +
                    "total=" + total +
                    ", page=" + page +
                    ", size=" + size +
                    ", totalPages=" + totalPages +
                    '}';
        }
    }
}