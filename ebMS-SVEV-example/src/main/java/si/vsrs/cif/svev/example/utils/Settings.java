/*
* Copyright 2015, Supreme Court Republic of Slovenia 
*
* Licensed under the EUPL, Version 1.1 or – as soon they will be approved by 
* the European Commission - subsequent versions of the EUPL (the "Licence");
* You may not use this work except in compliance with the Licence.
* You may obtain a copy of the Licence at:
*
* https://joinup.ec.europa.eu/software/page/eupl
*
* Unless required by applicable law or agreed to in writing, software 
* distributed under the Licence is distributed on an "AS IS" basis, WITHOUT 
* WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the Licence for the specific language governing permissions and  
* limitations under the Licence.
*/
package si.vsrs.cif.svev.example.utils;

import java.beans.IntrospectionException;
import java.io.*;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author Jože Rihtaršič
 */
public class Settings extends ASettings {

    static {
        try {
            File f = Settings.getInstance().getLog4JFile();
            if (f != null) {
                PropertyConfigurator.configure(f.toURI().toURL());
            } else {
                BasicConfigurator.configure();
            }
        } catch (MalformedURLException ex) {
            BasicConfigurator.configure();
            mlog.error(ex);
        }

    }

    public static void addURLToSystemClassLoader(URL url) throws IntrospectionException {
        URLClassLoader systemClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Class<URLClassLoader> classLoaderClass = URLClassLoader.class;

        try {
            Method method = classLoaderClass.getDeclaredMethod("addURL", new Class[]{URL.class});
            method.setAccessible(true);
            method.invoke(systemClassLoader, new Object[]{url});
        } catch (Throwable t) {
            throw new IntrospectionException("Error when adding url " + url.getPath() + " to system ClassLoader ");
        }
    }

    final private static String S_FILE_PMODES = "pmodes.xml";
    final private static String S_FILE_FO_CONFIG = "fop.xconf";
    final private static String S_FILE_DELIVERY_TYPES = "delivery-types.xml";
    final private static String S_FILE_CONFIG = "msh-config.properties";
    final private static String S_FILE_SIG_KEYS = "msh_key-passwords.properties";
    final private static String S_FILE_LOG4J = "log4j.properties";

    final private static String S_INIT_FOLDER_CONFIG = "msh/config";

    

    final private static String S_INIT_FOLDER_INBOX = "msh/inbox/msh-as4";
    final private static String S_INIT_FOLDER_LOG = "msh/log";
    final private static String S_INIT_WS_URL = "http://localhost:9000/msh";
    final private static String S_INIT_PARTY_DOMAIN = "e-box-a.si";

    final private static String S_PROP_FOLDER_INBOX = "msh.folder.inbox";

    final private static String S_PROP_FOLDER_CONFIG = "msh.folder.config";
    final private static String S_PROP_FOLDER_LOG = "msh.folder.log";


    final private static String S_PROP_WS_URL = "msh.ws.url";
    final private static String S_PROP_PARTY_DOMAIN = "msh.config.domain";

    File mfPropFile = null;
    private static Settings mInstance = null;

    public static Settings getInstance() {
        return mInstance == null ? (mInstance = new Settings()) : mInstance;
    }

    /**
     * Creates a new instance of Settings
     */
    protected Settings() {
    }

    @Override
    public void createIniFile() {
        initData(S_PROP_PARTY_DOMAIN, S_INIT_PARTY_DOMAIN);
        initData(S_PROP_FOLDER_CONFIG, S_INIT_FOLDER_CONFIG);

        initData(S_PROP_FOLDER_INBOX, S_INIT_FOLDER_INBOX);

        initData(S_PROP_WS_URL, S_INIT_WS_URL);

        storeProperties();
    }

    @Override
    public void initialize() {

        testFolder(getInboxFolder());


    }

    @Override
    public File getConfigFile() {
        if (mfPropFile == null) {
            File f = new File(System.getProperty(S_PROP_FOLDER_CONFIG, S_INIT_FOLDER_CONFIG));
            if (!f.exists()) {
                f.mkdirs();
            }
            mfPropFile = new File((f.getAbsolutePath().endsWith(File.separator) ? f.getPath() : f.getPath() + File.separator) + S_FILE_CONFIG);
        }
        return mfPropFile;
    }

    public File getFopConfigFile() {
        return getFile(S_PROP_FOLDER_CONFIG, S_INIT_FOLDER_CONFIG, S_FILE_FO_CONFIG);
    }

    public File getLog4JFile() {
        File f = getFile(S_PROP_FOLDER_LOG, S_INIT_FOLDER_LOG, "");
        if (System.getProperty(S_PROP_FOLDER_LOG) == null) {
            System.getProperties().put(S_PROP_FOLDER_LOG, f.getAbsolutePath());
        }
        File lf = getFile(S_PROP_FOLDER_CONFIG, S_INIT_FOLDER_CONFIG, S_FILE_LOG4J);
        if (!lf.exists()) {
            // init log folder

            try (FileWriter fw = new FileWriter(lf);) {
                fw.write("log4j.rootLogger=INFO, stdout, R\n");
                fw.write("log4j.appender.stdout=org.apache.log4j.ConsoleAppender\n");
                fw.write("log4j.appender.stdout.layout=org.apache.log4j.PatternLayout\n");
                fw.write("# Pattern to output the caller's file name and line number.");
                fw.write("log4j.appender.stdout.layout.ConversionPattern=%5p [%t] (%F:%L) - %m%n\n\n");
                fw.write("log4j.appender.R=org.apache.log4j.DailyRollingFileAppender\n");
                fw.write("log4j.appender.R.File=${");
                fw.write(S_PROP_FOLDER_LOG);
                fw.write("}/msh.log\n");
                fw.write("log4j.appender.R.DatePattern='.'yyyy-MM-dd\n");
                fw.write("log4j.appender.R.layout=org.apache.log4j.PatternLayout\n");
                fw.write("log4j.appender.R.layout.ConversionPattern=%d{yyyyMMdd_HH:mm:ss,SSS}  %p %t %c - %m%n\n");
                fw.flush();
            } catch (IOException ex) {
                lf = null;
            }
        }
        return lf;
    }



    public File getPModesFile() {
        return getFile(S_PROP_FOLDER_CONFIG, S_INIT_FOLDER_CONFIG, S_FILE_PMODES);
    }

    public File getDeliveryTypesFile() {
        return getFile(S_PROP_FOLDER_CONFIG, S_INIT_FOLDER_CONFIG, S_FILE_DELIVERY_TYPES);
    }

    public File getInboxFile(String strFileName) {
        return getFile(S_PROP_FOLDER_INBOX, S_INIT_FOLDER_INBOX, strFileName);
    }



    public File getInboxFolder() {
        return getAbsoluteFolder(S_PROP_FOLDER_INBOX, getData(S_PROP_FOLDER_INBOX, S_INIT_FOLDER_INBOX));
    }

   

    

  


    public String getWsUrl() {
        return getData(S_PROP_WS_URL, S_INIT_WS_URL);
    }

    public String getDomain() {
        return getData(S_PROP_PARTY_DOMAIN);
    }

    public static void main(String[] args) {
        Settings s = Settings.getInstance();
        s.storeProperties();
    }

}
