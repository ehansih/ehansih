package com.nokia.vulnscanner.data.models;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0017\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B=\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\u0003\u0012\u0006\u0010\n\u001a\u00020\u0003\u0012\u0006\u0010\u000b\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\fJ\t\u0010\u0017\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0018\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0019\u001a\u00020\u0006H\u00c6\u0003J\t\u0010\u001a\u001a\u00020\bH\u00c6\u0003J\t\u0010\u001b\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001c\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001d\u001a\u00020\u0003H\u00c6\u0003JO\u0010\u001e\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010\n\u001a\u00020\u00032\b\b\u0002\u0010\u000b\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\u001f\u001a\u00020 2\b\u0010!\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\"\u001a\u00020#H\u00d6\u0001J\t\u0010$\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\n\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u000eR\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u000eR\u0011\u0010\t\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u000eR\u0011\u0010\u000b\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u000eR\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016\u00a8\u0006%"}, d2 = {"Lcom/nokia/vulnscanner/data/models/CveRecord;", "", "cveId", "", "description", "cvssScore", "", "severity", "Lcom/nokia/vulnscanner/data/models/Severity;", "publishedDate", "affectedProduct", "references", "(Ljava/lang/String;Ljava/lang/String;DLcom/nokia/vulnscanner/data/models/Severity;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getAffectedProduct", "()Ljava/lang/String;", "getCveId", "getCvssScore", "()D", "getDescription", "getPublishedDate", "getReferences", "getSeverity", "()Lcom/nokia/vulnscanner/data/models/Severity;", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "equals", "", "other", "hashCode", "", "toString", "app_debug"})
@androidx.room.Entity(tableName = "cve_cache")
public final class CveRecord {
    @androidx.room.PrimaryKey()
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String cveId = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String description = null;
    private final double cvssScore = 0.0;
    @org.jetbrains.annotations.NotNull()
    private final com.nokia.vulnscanner.data.models.Severity severity = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String publishedDate = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String affectedProduct = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String references = null;
    
    public CveRecord(@org.jetbrains.annotations.NotNull()
    java.lang.String cveId, @org.jetbrains.annotations.NotNull()
    java.lang.String description, double cvssScore, @org.jetbrains.annotations.NotNull()
    com.nokia.vulnscanner.data.models.Severity severity, @org.jetbrains.annotations.NotNull()
    java.lang.String publishedDate, @org.jetbrains.annotations.NotNull()
    java.lang.String affectedProduct, @org.jetbrains.annotations.NotNull()
    java.lang.String references) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCveId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getDescription() {
        return null;
    }
    
    public final double getCvssScore() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.nokia.vulnscanner.data.models.Severity getSeverity() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getPublishedDate() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getAffectedProduct() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getReferences() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }
    
    public final double component3() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.nokia.vulnscanner.data.models.Severity component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component7() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.nokia.vulnscanner.data.models.CveRecord copy(@org.jetbrains.annotations.NotNull()
    java.lang.String cveId, @org.jetbrains.annotations.NotNull()
    java.lang.String description, double cvssScore, @org.jetbrains.annotations.NotNull()
    com.nokia.vulnscanner.data.models.Severity severity, @org.jetbrains.annotations.NotNull()
    java.lang.String publishedDate, @org.jetbrains.annotations.NotNull()
    java.lang.String affectedProduct, @org.jetbrains.annotations.NotNull()
    java.lang.String references) {
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