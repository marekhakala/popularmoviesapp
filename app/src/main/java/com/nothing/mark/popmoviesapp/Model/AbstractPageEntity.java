package com.nothing.mark.popmoviesapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public abstract class AbstractPageEntity {
    @Expose
    protected Long page;

    @SerializedName("total_pages")
    @Expose
    protected Long totalPages;

    @SerializedName("total_results")
    @Expose
    protected Long totalResults;

    public Long getPage() {
        return page;
    }

    public void setPage(Long page) {
        this.page = page;
    }

    public Long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Long totalPages) {
        this.totalPages = totalPages;
    }

    public Long getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Long totalResults) {
        this.totalResults = totalResults;
    }

    public int describeContents() {
        return 0;
    }
}