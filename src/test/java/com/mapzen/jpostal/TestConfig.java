package com.mapzen.jpostal;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestConfig {
    @Test
    public void testBuilderDefault() {
        Config config = Config.builder().build();

        assertEquals(config, config);
        assertEquals("Config{dataDir=null,libraryFile=null}", config.toString());
        assertNull(config.getDataDir());
        assertNull(config.getLibraryFile());
    }

    @Test
    public void testBuilderDataDir() {
        Config dataDirConfig = Config.builder().dataDir("foo").build();
        Config defaultConfig = Config.builder().build();

        assertEquals(dataDirConfig, dataDirConfig);
        assertNotEquals(dataDirConfig, defaultConfig);
        assertNotEquals(defaultConfig, dataDirConfig);
        assertEquals("Config{dataDir=foo,libraryFile=null}", dataDirConfig.toString());
        assertEquals("foo", dataDirConfig.getDataDir());
        assertNull(dataDirConfig.getLibraryFile());
    }

    @Test
    public void testBuilderLibraryFile() {
        Config libraryFileConfig = Config.builder().libraryFile("foo/libbar.so.1.1").build();
        Config defaultConfig = Config.builder().build();

        assertEquals(libraryFileConfig, libraryFileConfig);
        assertNotEquals(libraryFileConfig, defaultConfig);
        assertNotEquals(defaultConfig, libraryFileConfig);
        assertEquals("Config{dataDir=null,libraryFile=foo/libbar.so.1.1}", libraryFileConfig.toString());
        assertEquals("foo/libbar.so.1.1", libraryFileConfig.getLibraryFile());
        assertNull(libraryFileConfig.getDataDir());
    }

    @Test
    public void testBuilderAll() {
        Config allConfig = Config.builder()
                .dataDir("hello")
                .libraryFile("libworld.so").build();
        Config defaultConfig = Config.builder().build();

        assertEquals(allConfig, allConfig);
        assertNotEquals(allConfig, defaultConfig);
        assertNotEquals(defaultConfig, allConfig);
        assertEquals("Config{dataDir=hello,libraryFile=libworld.so}", allConfig.toString());
        assertEquals("hello", allConfig.getDataDir());
        assertEquals("libworld.so", allConfig.getLibraryFile());
    }
}
