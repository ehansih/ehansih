package com.nokia.vulnscanner.scanner;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2 = {"Lcom/nokia/vulnscanner/scanner/ScanOrchestrator;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "appScanner", "Lcom/nokia/vulnscanner/scanner/AppScanner;", "db", "Lcom/nokia/vulnscanner/data/db/CveDatabase;", "devScanner", "Lcom/nokia/vulnscanner/scanner/DeviceSecurityScanner;", "netScanner", "Lcom/nokia/vulnscanner/scanner/NetworkScanner;", "runFullScan", "Lkotlinx/coroutines/flow/Flow;", "Lcom/nokia/vulnscanner/scanner/ScanState;", "app_debug"})
public final class ScanOrchestrator {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final com.nokia.vulnscanner.data.db.CveDatabase db = null;
    @org.jetbrains.annotations.NotNull()
    private final com.nokia.vulnscanner.scanner.AppScanner appScanner = null;
    @org.jetbrains.annotations.NotNull()
    private final com.nokia.vulnscanner.scanner.DeviceSecurityScanner devScanner = null;
    @org.jetbrains.annotations.NotNull()
    private final com.nokia.vulnscanner.scanner.NetworkScanner netScanner = null;
    
    public ScanOrchestrator(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.nokia.vulnscanner.scanner.ScanState> runFullScan() {
        return null;
    }
}