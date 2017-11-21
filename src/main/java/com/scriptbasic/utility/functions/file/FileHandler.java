package com.scriptbasic.utility.functions.file;

import com.scriptbasic.interfaces.NoAccess;

public interface FileHandler extends NoAccess {
    void close() throws Exception;
}
