package com.scriptbasic.utility.functions.file;

import com.scriptbasic.spi.NoAccess;

public interface FileHandler extends NoAccess {
    void close() throws Exception;
}
