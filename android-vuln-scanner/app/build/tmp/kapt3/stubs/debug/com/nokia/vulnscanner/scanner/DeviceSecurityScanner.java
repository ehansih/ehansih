package com.nokia.vulnscanner.scanner;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ^\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\f2\u0006\u0010\u000e\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\f2\u0006\u0010\u0010\u001a\u00020\f2\u0006\u0010\u0011\u001a\u00020\f2\u0006\u0010\u0012\u001a\u00020\f2\u0006\u0010\u0013\u001a\u00020\f2\u0006\u0010\u0014\u001a\u00020\n2\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00170\u0016H\u0002J\b\u0010\u0018\u001a\u00020\fH\u0002J\b\u0010\u0019\u001a\u00020\fH\u0002J\b\u0010\u001a\u001a\u00020\fH\u0002J\u000e\u0010\u001b\u001a\u00020\fH\u0082@\u00a2\u0006\u0002\u0010\u001cJ\b\u0010\u001d\u001a\u00020\fH\u0002J\b\u0010\u001e\u001a\u00020\fH\u0002J\b\u0010\u001f\u001a\u00020\fH\u0002J\b\u0010 \u001a\u00020\fH\u0002J$\u0010!\u001a\b\u0012\u0004\u0012\u00020\u00170\u00162\u0006\u0010\"\u001a\u00020#2\u0006\u0010$\u001a\u00020#H\u0082@\u00a2\u0006\u0002\u0010%J\u000e\u0010&\u001a\u00020\'H\u0086@\u00a2\u0006\u0002\u0010\u001cR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006("}, d2 = {"Lcom/nokia/vulnscanner/scanner/DeviceSecurityScanner;", "", "context", "Landroid/content/Context;", "nvdApi", "Lcom/nokia/vulnscanner/data/api/NvdApi;", "cveDao", "Lcom/nokia/vulnscanner/data/db/CveDao;", "(Landroid/content/Context;Lcom/nokia/vulnscanner/data/api/NvdApi;Lcom/nokia/vulnscanner/data/db/CveDao;)V", "calculateDeviceScore", "", "rooted", "", "devMode", "usbDebug", "unknownSrc", "screenLock", "encrypted", "biometric", "verifyApps", "patchAge", "osVulns", "", "Lcom/nokia/vulnscanner/data/models/CveRecord;", "checkBiometric", "checkDeveloperMode", "checkEncryption", "checkRoot", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "checkScreenLock", "checkUnknownSources", "checkUsbDebugging", "checkVerifyApps", "lookupOsCves", "androidVersion", "", "patchLevel", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "scan", "Lcom/nokia/vulnscanner/data/models/DeviceSecurityResult;", "app_debug"})
public final class DeviceSecurityScanner {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final com.nokia.vulnscanner.data.api.NvdApi nvdApi = null;
    @org.jetbrains.annotations.NotNull()
    private final com.nokia.vulnscanner.data.db.CveDao cveDao = null;
    
    public DeviceSecurityScanner(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.nokia.vulnscanner.data.api.NvdApi nvdApi, @org.jetbrains.annotations.NotNull()
    com.nokia.vulnscanner.data.db.CveDao cveDao) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object scan(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.nokia.vulnscanner.data.models.DeviceSecurityResult> $completion) {
        return null;
    }
    
    private final java.lang.Object checkRoot(kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
    
    private final boolean checkDeveloperMode() {
        return false;
    }
    
    private final boolean checkUsbDebugging() {
        return false;
    }
    
    private final boolean checkUnknownSources() {
        return false;
    }
    
    private final boolean checkScreenLock() {
        return false;
    }
    
    private final boolean checkEncryption() {
        return false;
    }
    
    private final boolean checkBiometric() {
        return false;
    }
    
    private final boolean checkVerifyApps() {
        return false;
    }
    
    private final java.lang.Object lookupOsCves(java.lang.String androidVersion, java.lang.String patchLevel, kotlin.coroutines.Continuation<? super java.util.List<com.nokia.vulnscanner.data.models.CveRecord>> $completion) {
        return null;
    }
    
    private final int calculateDeviceScore(boolean rooted, boolean devMode, boolean usbDebug, boolean unknownSrc, boolean screenLock, boolean encrypted, boolean biometric, boolean verifyApps, int patchAge, java.util.List<com.nokia.vulnscanner.data.models.CveRecord> osVulns) {
        return 0;
    }
}