package com.technoetic.xplanner.file;

/**
 * The Class DefaultFileSystem.
 */
// DEBT Apply the same naming convention for service locator like SystemAuthorizer
public class DefaultFileSystem {
    
    /** The file system. */
    private static FileSystem fileSystem;

    /**
     * Gets the.
     *
     * @return the file system
     */
    public static FileSystem get() {
        return DefaultFileSystem.fileSystem;
    }

    /**
     * Sets the.
     *
     * @param fileSystem
     *            the file system
     */
    public static void set(final FileSystem fileSystem) {
        DefaultFileSystem.fileSystem = fileSystem;
    }
}
