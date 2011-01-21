package org.abratuhi.bahnde.db;

import org.apache.log4j.BasicConfigurator;

import junit.framework.TestCase;

public class DbUtilTest extends TestCase{
	
	static{
		BasicConfigurator.configure();
	}
	
	public void testGenerateTrains(){
		DbUtil.generateTrains();
	}

}
