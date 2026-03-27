package com.nokia.vulnscanner.data.api;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0010\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001BC\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\b\u0012\b\b\u0002\u0010\t\u001a\u00020\u0003\u0012\u000e\b\u0002\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0005\u00a2\u0006\u0002\u0010\fJ\t\u0010\u0015\u001a\u00020\u0003H\u00c6\u0003J\u000f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u00c6\u0003J\u000b\u0010\u0017\u001a\u0004\u0018\u00010\bH\u00c6\u0003J\t\u0010\u0018\u001a\u00020\u0003H\u00c6\u0003J\u000f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0005H\u00c6\u0003JI\u0010\u001a\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u00052\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\b2\b\b\u0002\u0010\t\u001a\u00020\u00032\u000e\b\u0002\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0005H\u00c6\u0001J\u0013\u0010\u001b\u001a\u00020\u001c2\b\u0010\u001d\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001e\u001a\u00020\u001fH\u00d6\u0001J\t\u0010 \u001a\u00020\u0003H\u00d6\u0001R\u001c\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0018\u0010\u0007\u001a\u0004\u0018\u00010\b8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0016\u0010\t\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0010R\u001c\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u000b0\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u000e\u00a8\u0006!"}, d2 = {"Lcom/nokia/vulnscanner/data/api/NvdCve;", "", "id", "", "descriptions", "", "Lcom/nokia/vulnscanner/data/api/NvdDescription;", "metrics", "Lcom/nokia/vulnscanner/data/api/NvdMetrics;", "published", "references", "Lcom/nokia/vulnscanner/data/api/NvdReference;", "(Ljava/lang/String;Ljava/util/List;Lcom/nokia/vulnscanner/data/api/NvdMetrics;Ljava/lang/String;Ljava/util/List;)V", "getDescriptions", "()Ljava/util/List;", "getId", "()Ljava/lang/String;", "getMetrics", "()Lcom/nokia/vulnscanner/data/api/NvdMetrics;", "getPublished", "getReferences", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "", "other", "hashCode", "", "toString", "app_debug"})
public final class NvdCve {
    @com.google.gson.annotations.SerializedName(value = "id")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String id = null;
    @com.google.gson.annotations.SerializedName(value = "descriptions")
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.nokia.vulnscanner.data.api.NvdDescription> descriptions = null;
    @com.google.gson.annotations.SerializedName(value = "metrics")
    @org.jetbrains.annotations.Nullable()
    private final com.nokia.vulnscanner.data.api.NvdMetrics metrics = null;
    @com.google.gson.annotations.SerializedName(value = "published")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String published = null;
    @com.google.gson.annotations.SerializedName(value = "references")
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.nokia.vulnscanner.data.api.NvdReference> references = null;
    
    public NvdCve(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    java.util.List<com.nokia.vulnscanner.data.api.NvdDescription> descriptions, @org.jetbrains.annotations.Nullable()
    com.nokia.vulnscanner.data.api.NvdMetrics metrics, @org.jetbrains.annotations.NotNull()
    java.lang.String published, @org.jetbrains.annotations.NotNull()
    java.util.List<com.nokia.vulnscanner.data.api.NvdReference> references) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.nokia.vulnscanner.data.api.NvdDescription> getDescriptions() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.nokia.vulnscanner.data.api.NvdMetrics getMetrics() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getPublished() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.nokia.vulnscanner.data.api.NvdReference> getReferences() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.nokia.vulnscanner.data.api.NvdDescription> component2() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.nokia.vulnscanner.data.api.NvdMetrics component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.nokia.vulnscanner.data.api.NvdReference> component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.nokia.vulnscanner.data.api.NvdCve copy(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    java.util.List<com.nokia.vulnscanner.data.api.NvdDescription> descriptions, @org.jetbrains.annotations.Nullable()
    com.nokia.vulnscanner.data.api.NvdMetrics metrics, @org.jetbrains.annotations.NotNull()
    java.lang.String published, @org.jetbrains.annotations.NotNull()
    java.util.List<com.nokia.vulnscanner.data.api.NvdReference> references) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}