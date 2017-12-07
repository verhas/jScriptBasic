

module scriptbasic {
    requires java.scripting;
    provides javax.script.ScriptEngineFactory with com.scriptbasic.script.ScriptBasicEngineFactory;
    exports com.scriptbasic.script;
    exports com.scriptbasic.api;
    exports com.scriptbasic.spi;
    exports com.scriptbasic.classification;
}