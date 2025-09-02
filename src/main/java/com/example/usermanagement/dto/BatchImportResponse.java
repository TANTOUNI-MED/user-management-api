package com.example.usermanagement.dto;

public class BatchImportResponse {
    private int totalRecords;
    private int successfulImports;
    private int failedImports;
    
    // Constructors
    public BatchImportResponse() {}
    
    public BatchImportResponse(int totalRecords, int successfulImports, int failedImports) {
        this.totalRecords = totalRecords;
        this.successfulImports = successfulImports;
        this.failedImports = failedImports;
    }
    
    // Getters and Setters
    public int getTotalRecords() { return totalRecords; }
    public void setTotalRecords(int totalRecords) { this.totalRecords = totalRecords; }
    
    public int getSuccessfulImports() { return successfulImports; }
    public void setSuccessfulImports(int successfulImports) { this.successfulImports = successfulImports; }
    
    public int getFailedImports() { return failedImports; }
    public void setFailedImports(int failedImports) { this.failedImports = failedImports; }
}