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

package si.vsrs.cif.svev.example.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import si.vsrs.cif.svev.example.test.server.MSHTestServer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import si.vsrs.cif.svev.example.SvevMshClient;
import si.vsrs.cif.svev.example.utils.Settings;

/**
 *
 * @author sluzba
 */
public class MSHServerTest {
    
    File mOutputFolder = null;
    
    public MSHServerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
         mOutputFolder = new File("unit-test-data");
        if (!mOutputFolder.exists()) {
            mOutputFolder.mkdir();
        }
    }
    
    @After
    public void tearDown() {
    }

    
    @Test
    public void testPushMail() {
        String testCourtServerAddress = "http://localhost:8081/msh";
        String testClientServerAddress = "http://localhost:8091/msh";
        
        // start test client service 
        MSHClientServer clientInstance = new MSHClientServer();
        clientInstance.restart(testClientServerAddress);
        
        
        // create dummy court server
        MSHTestServer courtTestInstance = new MSHTestServer();
        courtTestInstance.restart(testCourtServerAddress);
              HttpURLConnection conn  = null;  
        try {
            // create client and push request.
          
            URL url =  new URL(testClientServerAddress);
            
            //Thread.sleep(15*601000);

            conn =(HttpURLConnection)  url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/soap+xml;charset=UTF-8");
            conn.setRequestProperty("SOAPAction", "");
            OutputStream os =  conn.getOutputStream();
            InputStream isData = MSHServerTest.class.getResourceAsStream("/test-samples/TEST_01-DeliveryNotification-Request.xml");
            byte[] bRead = new byte[1024];
            int len = -1;
            while( (len = isData.read(bRead)) > 0){
                os.write(bRead, 0, len);
            }
            
            // got reponse
            InputStream isReponse = conn.getInputStream();
            // court validates response.:>
            // write reponse to file
            try (FileOutputStream fos = new FileOutputStream(File.createTempFile("client-test-", "-Response.xml", mOutputFolder))) {
                
                len = -1;
                while( (len = isReponse.read(bRead)) > 0){
                    fos.write(bRead, 0, len);
                }
            }
           
            
            
            
        } catch (IOException ex) {            
            ex.printStackTrace();
            fail("Fail while sending message:.");
        } 
        
        // ger key
        System.out.println("------------------------------------------------");
        System.out.println("get key");
        System.out.println("------------------------------------------------");
        SvevMshClient keyClient = new SvevMshClient();
        for (File f: Settings.getInstance().getInboxFolder().listFiles()){
            System.out.println(" get file" + f);
            if (f.isFile() && f.getName().toLowerCase().endsWith(".xml")){
                keyClient.getKeyAndDecryptPayload(f, testCourtServerAddress);
                // za enkra samo ene
                break;
            }
        
        }
        
        
        
    }
    
}
