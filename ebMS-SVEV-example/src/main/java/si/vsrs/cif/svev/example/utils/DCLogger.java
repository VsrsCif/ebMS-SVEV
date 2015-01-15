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

import java.io.StringWriter;
import java.util.Calendar;
import org.apache.log4j.Logger;

/**
 *
 * @author Jože Rihtaršič
 */
public class DCLogger {

    private final Logger mlgLogger;
    int miMethodStack = 3;
    
    public DCLogger(Class clzz) {        
        mlgLogger = Logger.getLogger(clzz!= null?clzz.getName():this.getClass().getName());
    }
    
    public DCLogger(Class clzz, int iLogStackMethodLevel) {        
        mlgLogger = Logger.getLogger(clzz!= null?clzz.getName():this.getClass().getName());
        miMethodStack = iLogStackMethodLevel;
    }
    
    
    public long getTime() {        
        return Calendar.getInstance().getTimeInMillis();
    }
    
    protected String getCurrentMethodName(){
        return Thread.currentThread().getStackTrace().length>miMethodStack?
                Thread.currentThread().getStackTrace()[miMethodStack].getMethodName():"NULL METHOD";
    }
    
    
    public long logStart( final Object ...  param) {
        long mlTime = getTime();
        String strParams = null;
        if (param!= null && param.length != 0){
            StringWriter sw = new StringWriter();
            int i =0;
            for (Object o: param){
                if(i!=0){
                    sw.append(",");
                }
                sw.append((++i)+":'"+o +"'");
            }
            strParams = sw.toString();
        }
        
        mlgLogger.debug(getCurrentMethodName() + ": - BEGIN' " + (strParams!=null?" params: "+strParams:""));
        return mlTime;
    }
    public long log( final Object ...  param) {
        long mlTime = getTime();
        String strParams = null;
        if (param!= null && param.length != 0){
            StringWriter sw = new StringWriter();
            int i =0;
            for (Object o: param){
                if(i!=0){
                    sw.append(",");
                }
                sw.append("'"+o +"' ");
            }
            strParams = sw.toString();
        }
        
        mlgLogger.info(getCurrentMethodName() + ":" + (strParams!=null?strParams:""));
        return mlTime;
    }
/*
    public void logEnd(long lTime ) {
        mlgLogger.info(getCurrentMethodName() + ": - END ( " + (getTime() - lTime) + " ms)");
    }*/
    public void logEnd(long lTime,final Object ...  param ) {
        String strParams = "";
        if (param!= null && param.length != 0){
            StringWriter sw = new StringWriter();
            int i =0;
            for (Object o: param){
                sw.append((++i)+".-> '"+o +"' ");
            }
            strParams = sw.toString();
        }
        
        
        
        mlgLogger.info(getCurrentMethodName() + ": - END ( " + (getTime() - lTime) + " ms) " + strParams);
    }

    public void logError(long lTime, String strMessage, Exception ex) {
        mlgLogger.error(getCurrentMethodName() + ": - ERROR MSG: '" +strMessage+ "' ( "+ (getTime() - lTime) + " ms )", ex); 
    }
    
    public void logError(long lTime, Exception ex) {
        mlgLogger.error(getCurrentMethodName() + ": - ERROR MSG: '" +(ex != null?ex.getMessage():"")+ "' ( "+ (getTime() - lTime) + " ms )", ex); 
    }
    public void logWarn(long lTime, String strMessage, Exception ex) {
        mlgLogger.warn(getCurrentMethodName() + ": - Warn MSG: '" +strMessage+ "' ( "+ (getTime() - lTime) + " ms )", ex); 
    }

}
