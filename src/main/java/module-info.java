import com.scriptbasic.api.script.ScriptEngineFactory;

module scriptbasic {
    requires java.scripting;
    provides javax.script.ScriptEngineFactory with ScriptEngineFactory;
    exports com.scriptbasic.api.script;
    exports com.scriptbasic.api;
}