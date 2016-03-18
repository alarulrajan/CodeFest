package com.technoetic.xplanner.file;

// DEBT Apply the same naming convention for service locator like SystemAuthorizer
public class DefaultFileSystem {
	private static FileSystem fileSystem;

	public static FileSystem get() {
		return DefaultFileSystem.fileSystem;
	}

	public static void set(final FileSystem fileSystem) {
		DefaultFileSystem.fileSystem = fileSystem;
	}
}
