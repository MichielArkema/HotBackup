package nl.michielarkema.hotbackupfree.services;

public class BackupResult {

    private final int collectedFiles;
    private final int zipSize;

    public BackupResult(int collectedFiles, int zipSize) {
        this.collectedFiles = collectedFiles;
        this.zipSize = zipSize;
    }
}
