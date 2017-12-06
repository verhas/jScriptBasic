import com.scriptbasic.api.script.ScriptBasicEngineFactory;

module scriptbasic {
    requires java.scripting;
    provides javax.script.ScriptEngineFactory with ScriptBasicEngineFactory;
    exports com.scriptbasic.api.script;
    exports com.scriptbasic.api;
    exports com.scriptbasic.classification;
}