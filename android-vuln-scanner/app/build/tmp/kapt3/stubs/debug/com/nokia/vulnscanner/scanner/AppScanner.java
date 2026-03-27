package com.nokia.vulnscanner.scanner;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\"\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ$\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000e2\u0006\u0010\u0010\u001a\u00020\u000b2\u0006\u0010\u0011\u001a\u00020\u000bH\u0082@\u00a2\u0006\u0002\u0010\u0012Jq\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00140\u000e2[\u0010\u0015\u001aW\b\u0001\u0012\u0013\u0012\u00110\u0017\u00a2\u0006\f\b\u0018\u0012\b\b\u0019\u0012\u0004\b\b(\u001a\u0012\u0013\u0012\u00110\u0017\u00a2\u0006\f\b\u0018\u0012\b\b\u0019\u0012\u0004\b\b(\u001b\u0012\u0013\u0012\u00110\u000b\u00a2\u0006\f\b\u0018\u0012\b\b\u0019\u0012\u0004\b\b(\u0010\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001d0\u001c\u0012\u0006\u0012\u0004\u0018\u00010\u00010\u0016H\u0086@\u00a2\u0006\u0002\u0010\u001eJ&\u0010\u001f\u001a\u00020\u00142\u0006\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020#2\u0006\u0010\u0010\u001a\u00020\u000bH\u0082@\u00a2\u0006\u0002\u0010$R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006%"}, d2 = {"Lcom/nokia/vulnscanner/scanner/AppScanner;", "", "context", "Landroid/content/Context;", "nvdApi", "Lcom/nokia/vulnscanner/data/api/NvdApi;", "cveDao", "Lcom/nokia/vulnscanner/data/db/CveDao;", "(Landroid/content/Context;Lcom/nokia/vulnscanner/data/api/NvdApi;Lcom/nokia/vulnscanner/data/db/CveDao;)V", "dangerousPermissions", "", "", "trustedInstallers", "lookupCves", "", "Lcom/nokia/vulnscanner/data/models/CveRecord;", "appName", "packageName", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "scanInstalledApps", "Lcom/nokia/vulnscanner/data/models/AppScanResult;", "onProgress", "Lkotlin/Function4;", "", "Lkotlin/ParameterName;", "name", "current", "total", "Lkotlin/coroutines/Continuation;", "", "(Lkotlin/jvm/functions/Function4;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "scanSingleApp", "pm", "Landroid/content/pm/PackageManager;", "pkg", "Landroid/content/pm/PackageInfo;", "(Landroid/content/pm/PackageManager;Landroid/content/pm/PackageInfo;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class AppScanner {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final com.nokia.vulnscanner.data.api.NvdApi nvdApi = null;
    @org.jetbrains.annotations.NotNull()
    private final com.nokia.vulnscanner.data.db.CveDao cveDao = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Set<java.lang.String> dangerousPermissions = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Set<java.lang.String> trustedInstallers = null;
    
    public AppScanner(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.nokia.vulnscanner.data.api.NvdApi nvdApi, @org.jetbrains.annotations.NotNull()
    com.nokia.vulnscanner.data.db.CveDao cveDao) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object scanInstalledApps(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function4<? super java.lang.Integer, ? super java.lang.Integer, ? super java.lang.String, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> onProgress, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.nokia.vulnscanner.data.models.AppScanResult>> $completion) {
        return null;
    }
    
    private final java.lang.Object scanSingleApp(android.content.pm.PackageManager pm, android.content.pm.PackageInfo pkg, java.lang.String appName, kotlin.coroutines.Continuation<? super com.nokia.vulnscanner.data.models.AppScanResult> $completion) {
        return null;
    }
    
    private final java.lang.Object lookupCves(java.lang.String appName, java.lang.String packageName, kotlin.coroutines.Continuation<? super java.util.List<com.nokia.vulnscanner.data.models.CveRecord>> $completion) {
        return null;
    }
}