/**
 *
 */
package com.scriptbasic.configuration;
import java.util.Properties;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import com.bdd.Business;
import com.scriptbasic.interfaces.Configuration;
/**
 * @author Peter Verhas
 * date Aug 3, 2012
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ System.class })
public class TestBasicConfiguration {
    private static final Configuration config = new BasicConfiguration();
    private static final String key = "key";
    private static final String value = "value";
    private static final String badValue = "badValue";
    @Before
    public void setUp() {
    }
    @After
    public void tearDown() {
    }
    @Test
    public void testGetConfigValueWhenConfigKeyExistsShallReturnValue() {
        new Business() {
            private String actual;
            private Properties props;
            @Override
            public void given() {
                props = Mockito.mock(Properties.class);
                config.setConfigProperties(props);
                Mockito.when(props.containsKey(key)).thenReturn(true);
                Mockito.when(props.getProperty(key)).thenReturn(value);
            }
            @Override
            public void when() {
                actual = config.getConfigValue(key);
            }
            @Override
            public void then() {
                Mockito.verify(props).containsKey(key);
                Mockito.verify(props).getProperty(key);
                Assert.assertEquals(value, actual);
            }
        }.execute();
    }
    @SuppressWarnings("static-method")
    @Test
    public void testGetConfigValueWhenEnvironmentKeyExist() {
        String actual;
        Properties props;
        // GIVEN
        props = PowerMockito.mock(Properties.class);
        PowerMockito.mockStatic(System.class);
        config.setConfigProperties(props);
        Mockito.when(props.containsKey(key)).thenReturn(true);
        Mockito.when(props.getProperty(key)).thenReturn(badValue);
        Mockito.when(System.getenv("sb4j." + key)).thenReturn(value);
        // WHEN
        actual = config.getConfigValue(key);
        // THEN
        Mockito.verify(props).containsKey(key);
        Mockito.verify(props).getProperty(key);
        PowerMockito.verifyStatic();
        System.getenv("sb4j." + key);
        Assert.assertEquals(value, actual);
    }
    @Test
    public void testGetConfigValueWhenEnvironmentKeyExists() {
        new Business() {
            private String actual;
            private Properties props;
            @Override
            public void given() {
                props = PowerMockito.mock(Properties.class);
                PowerMockito.mockStatic(System.class);
                config.setConfigProperties(props);
                Mockito.when(props.containsKey(key)).thenReturn(true);
                Mockito.when(props.getProperty(key)).thenReturn(badValue);
                Mockito.when(System.getenv("sb4j." + key)).thenReturn(value);
            }
            @Override
            public void when() {
                actual = config.getConfigValue(key);
            }
            @Override
            public void then() {
                Mockito.verify(props).containsKey(key);
                Mockito.verify(props).getProperty(key);
                PowerMockito.verifyStatic();
                System.getenv("sb4j." + key);
                Assert.assertEquals(value, actual);
            }
        }.execute();
    }
    @Test
    public void testGetConfigValueWhenSystemPropertyExists(){
        new Business() {
            private String actual;
            private Properties props;
            @Override
            public void given() {
                props = PowerMockito.mock(Properties.class);
                PowerMockito.mockStatic(System.class);
                config.setConfigProperties(props);
                Mockito.when(props.containsKey(key)).thenReturn(true);
                Mockito.when(props.getProperty(key)).thenReturn(badValue);
                Mockito.when(System.getenv("sb4j." + key)).thenReturn(badValue);
                Mockito.when(System.getProperty("sb4j." + key)).thenReturn(value);
            }
            @Override
            public void when() {
                actual = config.getConfigValue(key);
            }
            @Override
            public void then() {
                Mockito.verify(props).containsKey(key);
                Mockito.verify(props).getProperty(key);
                PowerMockito.verifyStatic();
                System.getenv("sb4j." + key);
                PowerMockito.verifyStatic();
                System.getProperty("sb4j." + key);
                Assert.assertEquals(value, actual);
            }
        }.execute();
    }
}