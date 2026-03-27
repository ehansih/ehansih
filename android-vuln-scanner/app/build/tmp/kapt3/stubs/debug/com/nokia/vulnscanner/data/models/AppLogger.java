package com.nokia.vulnscanner.data.models;

/**
 * In-app log collector. All scanner code writes here AND to android.util.Log.
 * UI observes [entries] to show the live debug log screen.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0003\n\u0002\b\u0003\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\r\u001a\u00020\u000eJ,\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u000e2\u0006\u0010\u0014\u001a\u00020\u000e2\n\b\u0002\u0010\u0015\u001a\u0004\u0018\u00010\u000eH\u0002J\u0006\u0010\u0016\u001a\u00020\u0010J\u0016\u0010\u0017\u001a\u00020\u00102\u0006\u0010\u0013\u001a\u00020\u000e2\u0006\u0010\u0014\u001a\u00020\u000eJ\"\u0010\u0018\u001a\u00020\u00102\u0006\u0010\u0013\u001a\u00020\u000e2\u0006\u0010\u0014\u001a\u00020\u000e2\n\b\u0002\u0010\u0019\u001a\u0004\u0018\u00010\u001aJ\u0016\u0010\u001b\u001a\u00020\u00102\u0006\u0010\u0013\u001a\u00020\u000e2\u0006\u0010\u0014\u001a\u00020\u000eJ\u0016\u0010\u001c\u001a\u00020\u00102\u0006\u0010\u0013\u001a\u00020\u000e2\u0006\u0010\u0014\u001a\u00020\u000eR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\f\u00a8\u0006\u001d"}, d2 = {"Lcom/nokia/vulnscanner/data/models/AppLogger;", "", "()V", "MAX_ENTRIES", "", "_entries", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "Lcom/nokia/vulnscanner/data/models/LogEntry;", "entries", "Lkotlinx/coroutines/flow/StateFlow;", "getEntries", "()Lkotlinx/coroutines/flow/StateFlow;", "allText", "", "append", "", "level", "Lcom/nokia/vulnscanner/data/models/LogLevel;", "tag", "msg", "throwable", "clear", "d", "e", "t", "", "i", "w", "app_debug"})
public final class AppLogger {
    private static final int MAX_ENTRIES = 500;
    @org.jetbrains.annotations.NotNull()
    private static final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.nokia.vulnscanner.data.models.LogEntry>> _entries = null;
    @org.jetbrains.annotations.NotNull()
    private static final kotlinx.coroutines.flow.StateFlow<java.util.List<com.nokia.vulnscanner.data.models.LogEntry>> entries = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.nokia.vulnscanner.data.models.AppLogger INSTANCE = null;
    
    private AppLogger() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.nokia.vulnscanner.data.models.LogEntry>> getEntries() {
        return null;
    }
    
    public final void d(@org.jetbrains.annotations.NotNull()
    java.lang.String tag, @org.jetbrains.annotations.NotNull()
    java.lang.String msg) {
    }
    
    public final void i(@org.jetbrains.annotations.NotNull()
    java.lang.String tag, @org.jetbrains.annotations.NotNull()
    java.lang.String msg) {
    }
    
    public final void w(@org.jetbrains.annotations.NotNull()
    java.lang.String tag, @org.jetbrains.annotations.NotNull()
    java.lang.String msg) {
    }
    
    public final void e(@org.jetbrains.annotations.NotNull()
    java.lang.String tag, @org.jetbrains.annotations.NotNull()
    java.lang.String msg, @org.jetbrains.annotations.Nullable()
    java.lang.Throwable t) {
    }
    
    public final void clear() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String allText() {
        return null;
    }
    
    private final void append(com.nokia.vulnscanner.data.models.LogLevel level, java.lang.String tag, java.lang.String msg, java.lang.String throwable) {
    }
}