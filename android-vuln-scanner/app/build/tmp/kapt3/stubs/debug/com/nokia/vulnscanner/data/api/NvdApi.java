package com.nokia.vulnscanner.data.api;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\bf\u0018\u00002\u00020\u0001J\"\u0010\u0002\u001a\u00020\u00032\b\b\u0001\u0010\u0004\u001a\u00020\u00052\b\b\u0003\u0010\u0006\u001a\u00020\u0007H\u00a7@\u00a2\u0006\u0002\u0010\bJ,\u0010\t\u001a\u00020\u00032\b\b\u0001\u0010\n\u001a\u00020\u00052\b\b\u0003\u0010\u0006\u001a\u00020\u00072\b\b\u0003\u0010\u000b\u001a\u00020\u0007H\u00a7@\u00a2\u0006\u0002\u0010\f\u00a8\u0006\r"}, d2 = {"Lcom/nokia/vulnscanner/data/api/NvdApi;", "", "searchByPlatform", "Lcom/nokia/vulnscanner/data/api/NvdResponse;", "cpeName", "", "resultsPerPage", "", "(Ljava/lang/String;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "searchCves", "keyword", "startIndex", "(Ljava/lang/String;IILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public abstract interface NvdApi {
    
    @retrofit2.http.GET(value = "cves/2.0")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object searchCves(@retrofit2.http.Query(value = "keywordSearch")
    @org.jetbrains.annotations.NotNull()
    java.lang.String keyword, @retrofit2.http.Query(value = "resultsPerPage")
    int resultsPerPage, @retrofit2.http.Query(value = "startIndex")
    int startIndex, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.nokia.vulnscanner.data.api.NvdResponse> $completion);
    
    @retrofit2.http.GET(value = "cves/2.0")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object searchByPlatform(@retrofit2.http.Query(value = "cpeName")
    @org.jetbrains.annotations.NotNull()
    java.lang.String cpeName, @retrofit2.http.Query(value = "resultsPerPage")
    int resultsPerPage, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.nokia.vulnscanner.data.api.NvdResponse> $completion);
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 3, xi = 48)
    public static final class DefaultImpls {
    }
}